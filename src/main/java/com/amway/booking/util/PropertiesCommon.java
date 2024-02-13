package com.amway.booking.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesCommon {
    public static String userName;
    public static String password;
    public static String checkUnpaidOrderUrl;
    public static String bookingApiAuthToken;

    static {
        initConfig();
        //initConfigByPropeties();
    }
    private static void initConfig() {
        userName = System.getenv("userName");
        password = System.getenv("password");
        checkUnpaidOrderUrl = System.getenv("checkUnpaidOrderUrl");
        bookingApiAuthToken = System.getenv("bookingApiAuthToken");



    }
    private static void initConfigByPropeties() {
        InputStream inf = PropertiesCommon.class.getClassLoader().getResourceAsStream("database.properties");
        Properties properties = new Properties();
        try {
            properties.load(inf);
            userName = properties.getProperty("userName");
            password = properties.getProperty("password");
            checkUnpaidOrderUrl = properties.getProperty("checkUnpaidOrderUrl");
            bookingApiAuthToken = properties.getProperty("bookingApiAuthToken");

        } catch (IOException e) {
            System.out.println("读取配置文件失败");
        }
    }

}
