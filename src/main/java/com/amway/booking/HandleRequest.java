package com.amway.booking;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

public class HandleRequest implements RequestHandler<Map<String,Object>, LambdaResult> {
	private static final Logger logger = LoggerFactory.getLogger(HandleRequest.class);
	@SuppressWarnings("unchecked")
	@Override
	public LambdaResult handleRequest(Map<String,Object> event, Context context) {

		logger.info("sync msb order pay status start");
		LambdaResult response = new LambdaResult(0);

		try{
			logger.info("This lambda was called finish!");
			response.setResultCode(1);
		}catch (Exception e){
			logger.error(e.getMessage(),e);
			response.setResultCode(0);
			response.setErrorMessage(e.getMessage());
		}
		logger.info("sync msb order pay status finish");
		return response;
	}

	public static void main(String[] args) throws IOException {

		logger.info("This lambda was called finish!");
	}


}