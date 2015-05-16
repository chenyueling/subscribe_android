package com.chenyueling.subscribe.common;

/**
 * Created by chenyueling on 2015/5/13.
 */
public class ConfigHelper {
    public static String KYE_DEVICE_CODE = "DEVICE_CODE";
    public static String SUBSCRIBE = "SUBSCRIBE";
    public static final String host = "http://192.168.1.219";
    public static final String port = "8080";
    public static final String project = "jersey-jetty";
    public static final String subscribeArticles = host + ":" + port + "/" + project + "/" + "rest" + "/" + "device/articles/subscribe_list";

    public static final String registerDevice = host + ":" + port + "/" + project + "/" + "rest" + "/" + "device" + "/register";
    public static final String publicServer = host + ":" + port + "/" + project + "/" + "rest" + "/" + "device" + "/servers/public";
    public static final String privateServer = host + ":" + port + "/" + project + "/" + "rest" + "/" + "device" + "/servers/private";
    public static final String subscribeServer = host + ":" + port + "/" + project + "/" + "rest" + "/" + "device" + "/servers/subscribe_list";


}
