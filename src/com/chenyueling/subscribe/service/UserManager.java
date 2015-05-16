package com.chenyueling.subscribe.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;
import com.chenyueling.subscribe.common.ConfigHelper;
import com.chenyueling.subscribe.common.Result;
import com.chenyueling.subscribe.utils.HttpRequestException;
import com.chenyueling.subscribe.utils.JsonUtil;
import com.chenyueling.subscribe.utils.NativeHttpClient;


/**
 * Created by chenyueling on 2015/5/13.
 */
public class UserManager {


    private static UserManager userManager;


    private  String imei;
    private  Context CONTEXT;
    private  String DEVICECODE;

    private static int SUCCESS = 1;

    private static int ERROR = 0;

     Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == SUCCESS) {
                SharedPreferences sharedPreferences =  CONTEXT.getSharedPreferences(ConfigHelper.SUBSCRIBE,Context.MODE_PRIVATE);
                SharedPreferences.Editor edit = sharedPreferences.edit();
                edit.putString(ConfigHelper.KYE_DEVICE_CODE,DEVICECODE);
            } else if (msg.what == ERROR) {

            }
        }
    };

        private UserManager() {
    }

    public static UserManager getInstance() {
        if(userManager == null){
            userManager = new UserManager();
        }
        return userManager;
    }

    public  void register(Context context){


        CONTEXT = context;
        TelephonyManager tm = (TelephonyManager) CONTEXT.getSystemService(CONTEXT.TELEPHONY_SERVICE);
        imei = tm.getDeviceId();


        Thread thread = new Thread(){
            @Override
            public void run(){
                String url = ConfigHelper.registerDevice;
                try {
                    String json = NativeHttpClient.post(url, JsonUtil.toJson(imei));
                    Result result = JsonUtil.format(json, Result.class);

                    if("success".equals(result.getStatus())){
                        Log.d("imei",result.getData());
                        DEVICECODE = result.getData();
                        handler.sendEmptyMessage(SUCCESS);
                    }
                } catch (HttpRequestException e) {
                    e.printStackTrace();
                }
            }
        };

        thread.setContextClassLoader(ClassLoader.getSystemClassLoader());
        thread.run();

    }
}
