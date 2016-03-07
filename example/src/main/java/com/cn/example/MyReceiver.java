package com.cn.example;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Administrator on 2016/3/7.
 */
public class MyReceiver extends BroadcastReceiver {
    private static final String SEND_BROADCASTRECEIVER_NAME = "send.data.to.other.activity";//这个名字就是你set的那个action

    //TODO 定义好接收的广播后需要在manifest文件中注册广播，请移步至清单文件查看注册代码
    @Override
    public void onReceive(Context context, Intent intent) {
        //TODO 这里传入的inten就可以理解成我们发广播时发出的那个intent（可以这么理解，说说而已别当真 - -）
        /*
        如果你定义该类接收好多广播的话这里就需要根据它的action来判断是谁发过来的

         */
        if (intent.getAction().equals(SEND_BROADCASTRECEIVER_NAME)) {
            //广播接收成功 json为接收到的参数
            String json = intent.getStringExtra("key_br");
            MainActivity.setText(json);
//            myInterface.fromNetWorkData(json);
        }
    }

}
