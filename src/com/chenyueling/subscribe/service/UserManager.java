package com.chenyueling.subscribe.service;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;
import com.chenyueling.subscribe.common.ConfigHelper;
import com.chenyueling.subscribe.common.Result;
import com.chenyueling.subscribe.utils.HttpRequestException;
import com.chenyueling.subscribe.utils.JsonUtil;
import com.chenyueling.subscribe.utils.NativeHttpClient;
import com.chenyueling.subscribe.utils.PreferencesUtil;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by chenyueling on 2015/5/13.
 */
public class UserManager {


    private static UserManager userManager;


    private  String imei;
    private static Context CONTEXT;

    private static int SUCCESS = 1;

    private static int ERROR = 0;

     Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == SUCCESS) {
                PreferencesUtil.setString(CONTEXT,ConfigHelper.KYE_DEVICE_CODE,msg.obj+"");
                Toast.makeText(CONTEXT,PreferencesUtil.getString(CONTEXT,ConfigHelper.KYE_DEVICE_CODE),Toast.LENGTH_SHORT).show();
            } else if (msg.what == ERROR) {
                Toast.makeText(CONTEXT,"device register error",Toast.LENGTH_SHORT).show();
            }
        }
    };

        private UserManager() {
    }

    public static UserManager getInstance(Context context) {
        CONTEXT = context;
        if(userManager == null){
            userManager = new UserManager();
        }
        return userManager;
    }

    public  void register(){
        System.out.println("register...");
        TelephonyManager tm = (TelephonyManager) CONTEXT.getSystemService(Context.TELEPHONY_SERVICE);
        imei = tm.getDeviceId();


        Thread thread = new Thread(){
            @Override
            public void run(){
                String url = ConfigHelper.registerDevice;
                Map<String,String> param = new HashMap<String, String>();
                param.put("imei",imei);
                try {
                    String json = NativeHttpClient.post(url, JsonUtil.toJson(param));
                    Result result = JsonUtil.format(json, Result.class);
                    System.out.println("register" + json);
                    if("success".equals(result.getStatus())){
                        Log.d("imei",result.getData());
                        Message msg = Message.obtain();
                        msg.what = SUCCESS;
                        msg.obj = result.getData();
                        handler.sendMessage(msg);
                    }
                } catch (HttpRequestException e) {
                    e.printStackTrace();
                }
            }
        };

        thread.setContextClassLoader(ClassLoader.getSystemClassLoader());
        thread.start();

    }

    /**
     * GET DEVICE CODE
     * @return dc
     */
    public String getUserDeviceCode(){
        String dc = PreferencesUtil.getString(CONTEXT,ConfigHelper.KYE_DEVICE_CODE);
        if(dc == null || "".equals(dc)){
            register();
        }
        return dc;
    }
}
