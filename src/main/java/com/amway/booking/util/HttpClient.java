package com.amway.booking.util;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.*;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.*;
import java.util.Map.Entry;

/**
 * http 客户端
 *
 * @author mflih
 *
 */
public class HttpClient {

	/**
	 * http请求
	 *
	 * @param request
	 * @return
	 */
	public static HttpResult httpExecute(HttpRequestBase request, String type) {
		HttpResult httpResult = new HttpResult();
		CloseableHttpClient httpClient = null;
		try {
			httpClient = ("https".equals(type)) ? getHttpsClient() : HttpClients.createDefault();
			// 配置
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(60000).setConnectTimeout(60000)
					.setConnectionRequestTimeout(60000).build();
			request.setConfig(requestConfig);
			CloseableHttpResponse httpResponse = httpClient.execute(request);
			httpResult.setHttpCode(httpResponse.getStatusLine().getStatusCode());
			HttpEntity responseEntity = httpResponse.getEntity();
			String response = EntityUtils.toString(responseEntity);
			httpResult.setResult(response);
		} catch (Exception e) {
			request.abort();
		} finally {
			try {
				request.releaseConnection();
			} catch (Exception e) {
			}
			try {
				httpClient.close();
			} catch (Exception e) {
			}
		}
		return httpResult;
	}

	/**
	 * http get 请求
	 *
	 * @param url
	 * @param headerMap
	 * @param paramsMap
	 * @param encoding
	 * @return
	 */
	public static HttpResult httpGet(String url, Map<String, String> headerMap, Map<String, String> paramsMap) {
		// 初始化头部参数
		if (headerMap == null) {
			headerMap = new HashMap<String, String>();
		}
		if (!headerMap.containsKey("Content-Type")) {
			headerMap.put("Content-Type", "application/x-www-form-urlencoded");
		}
		// 组装url参数
		String paramStr = "";
		if (paramsMap != null && paramsMap.size() > 0) {
			Iterator<Entry<String, String>> iter = paramsMap.entrySet().iterator();
			while (iter.hasNext()) {
				Entry<String, String> entry = iter.next();
				String key = entry.getKey();
				String val = entry.getValue();
				paramStr = paramStr + (paramStr = "&" + key + "=" + val);
			}
		}
		if (!(paramStr.equals(""))) {
			paramStr = paramStr.replaceFirst("&", "?");
			url = url + paramStr;
		}
		// 创建httpGet
		HttpGet httpGet = new HttpGet(url);
		// 设置头部参数
		Iterator<Entry<String, String>> iterHeader = headerMap.entrySet().iterator();
		while (iterHeader.hasNext()) {
			Entry<String, String> entry = iterHeader.next();
			httpGet.setHeader(entry.getKey(), entry.getValue());
		}
		return httpExecute(httpGet, ((url != null) && (url.startsWith("https:"))) ? "https" : "http");
	}

	/**
	 * http post请求
	 *
	 * @param url
	 * @param headerMap
	 * @param paramsMap
	 * @param encoding
	 * @return
	 */
	public static HttpResult httpPost(String url, Map<String, String> headerMap, Map<String, String> paramsMap)
			throws Exception {
		// 初始化头部参数
		if (headerMap == null) {
			headerMap = new HashMap<String, String>();
		}
		if (!headerMap.containsKey("Content-Type")) {
			headerMap.put("Content-Type", "application/x-www-form-urlencoded");
		}
		// 设置post参数
		List<BasicNameValuePair> params = parseMap2BasicForm(paramsMap);
		UrlEncodedFormEntity requestEntity = null;
		if (params != null) {
			requestEntity = new UrlEncodedFormEntity(params);
		}
		// 创建httpPost
		HttpPost httpPost = new HttpPost(url);
		// 设置头部参数
		Iterator<Entry<String, String>> iter = headerMap.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<String, String> entry = iter.next();
			httpPost.setHeader(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));
		}
		httpPost.setEntity(requestEntity);
		return httpExecute(httpPost, ((url != null) && (url.startsWith("https:"))) ? "https" : "http");
	}

	/**
	 * http post json请求
	 *
	 * @param url
	 * @param headerMap
	 * @param paramsMap
	 * @param encoding
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static HttpResult httpPostJson(String url, Map<String, String> headerMap, String paramJson)
			throws Exception {
		// 初始化头部参数
		if (headerMap == null) {
			headerMap = new HashMap<String, String>();
		}
		if (!headerMap.containsKey("Content-Type")) {
			headerMap.put("Content-Type", "application/json");
		}
		// 设置json参数
		StringEntity requestEntity = new StringEntity(paramJson);
		// 创建httpPost
		HttpPost httpPost = new HttpPost(url);

		// 设置头部参数
		Iterator<Entry<String, String>> iter = headerMap.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<String, String> entry = iter.next();
			httpPost.setHeader(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));
		}
		httpPost.setEntity(requestEntity);
		return httpExecute(httpPost, ((url != null) && (url.startsWith("https:"))) ? "https" : "http");
	}

	private static final List<BasicNameValuePair> parseMap2BasicForm(Map<String, String> paramsMap) {
		List<BasicNameValuePair> params = null;
		if ((paramsMap != null) && (paramsMap.size() > 0)) {
			Iterator<String> it = paramsMap.keySet().iterator();
			params = new ArrayList<BasicNameValuePair>();
			String keyTmp = null;
			while (it.hasNext()) {
				keyTmp = it.next();
				params.add(new BasicNameValuePair(keyTmp, paramsMap.get(keyTmp)));
			}
		}
		return params;
	}

	private static final CloseableHttpClient getHttpsClient() throws NoSuchAlgorithmException, KeyManagementException {
		SSLContext sslContext = SSLContext.getInstance("TLS");
		X509TrustManager xtm = new X509TrustManager() {
			public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
			}

			public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
			}

			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}
		};

		HostnameVerifier hostnameVerifier = new HostnameVerifier() {
			@Override
			public boolean verify(String string, SSLSession sslSession) {
				// TODO Auto-generated method stub
				return true;
			}

		};
		sslContext.init(null, new TrustManager[] { xtm }, new java.security.SecureRandom());
		return HttpClients.custom().setSSLHostnameVerifier(hostnameVerifier).setSSLContext(sslContext).build();
	}

	/**
	 * 构造Basic Auth认证头信息
	 *
	 * @return
	 */
	public static String getHeader(String APP_KEY,String SECRET_KEY) {
		String auth = APP_KEY + ":" + SECRET_KEY;
		byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes());
		String authHeader = "Basic " + new String(encodedAuth);
		return authHeader;
	}
}
