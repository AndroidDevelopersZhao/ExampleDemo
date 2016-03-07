package com.cn.example;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * git commit test log
 * TODO 这个demo我不删留着，你以后哪里碰到问题给我说下，我给你写成demo加在下面，以后记不起了可以看看
 * Created by Administrator on 2016/3/7.
 */
public class MainActivity extends Activity {
    private static TextView tv_resp;
    private static String url = "http://221.228.88.249:8080/SmallRecipeService/";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main);
        initView();
    }

    private void initView() {

        tv_resp = (TextView) this.findViewById(R.id.tv_resp);
        this.findViewById(R.id.bt_req).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*网络请求的结果除了我昨天给你说的接口回调能实现，还有发出广播的形式来实现，首先我在下面做网络请求*/
                //这里网络请求我用android-async-http-1.4.8版的请求框架，下面的请求方式是这个框架最原始的请求方式，我没有做封装
                appedText("开始网络请求...");
                asyncTest();
            }
        });

    }


    private void asyncTest() {
        /*
        两种构造都可以得到client请求对象，前者不能绕过SSL协议，后者可以绕过SSL协议。
        至于什么是SSL协议，你去百度下，你只要记着这个有参的构造可以访问https网页，
        以后你上班了可能就会用到，涉及证书、签名，我不给你细说了。
         */
//        AsyncHttpClient client = new AsyncHttpClient();
        AsyncHttpClient client = new AsyncHttpClient(true, 80, 443);//这是它的第二个构造函数

        //如果需要传参数过去可以使用如下方式得到传参对象
        RequestParams params = new RequestParams();
        //调用这个传参对象的put方法就可以传入参数,key位置为传参的key，value位置可以传入很多类型数据，例如基本的数据类型，file、inputStream等
        params.put("key", "value");
        params.put("key2", "value2");

        //最后调用client的dopost或doget方法进行请求(参数1为请求的URL，当有参数需要传递时参数2为传参对象，不需要参数时参数2传监听就可以了)
        appedText("上送的参数：" + params.toString());
        client.get(url + "authsessionid", params, listener);
//        client.post("url",params,listener);
        //如果请求不需要带参数过去时
//        client.get("url", listener);
//        client.post("url",listener);

    }

    private Intent intent = null;
    private AsyncHttpResponseHandler listener = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int i, org.apache.http.Header[] headers, byte[] bytes) {

            //请求成功时会进这个回调，byte[]为返回的数据
            //TODO 当你的URL指向一张网络图片时使用BitmapFactory直接对这个数组解码就可以得到图片
//            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            //TODO 当你请求的是网络数据接口时，返回的数据直接new String就好
            String data = new String(bytes);
            //当接收到这个请求的数据后发出一条广播
            intent = new Intent();
            intent.setAction("send.data.to.other.activity");
            //将请求得到的结果data发出去
            intent.putExtra("key_br", data);
            sendBroadcast(intent);
            appedText("获取数据成功，广播已发出...");
            //广播发出后我们定义一个类，继承自BroadcastReceiver，重新他的onReceive方法来接收就可以，请请移步至自定一的类MyReceiver
        }

        @Override
        public void onFailure(int i, org.apache.http.Header[] headers, byte[] bytes, Throwable throwable) {
            //网络访问失败时的回调，比如常见情况为网络异常、404错误等
            intent = new Intent();
            intent.setAction("send.data.to.other.activity");
            intent.putExtra("key_br", "网络异常");
            sendBroadcast(intent);
            appedText("广播已发出...");
        }
    };

    public static void setText(String json) {
        appedText("广播接收成功");
        appedText("广播内容：" + json);
          /*lib下还有个jar为Gson的解析包，现在有好多人Json解析都用这个jar，下面是解析的流程*/
        //比如这个接口返回这样的数据：{"errorCode":-1,"resultMsg":"用户账号或sessionid为空"}
        //分析：Json对象里存了两个字段，第一个字段errorCode的参数类型为int 第二字段的参数类型为String
        //我们先去定义一个实体类RespData，让这个实体类里包括这两个成员变量（注：字段、数据类型需要对应）
        appedText("开始解析数据...");
        RespData respData = new Gson().fromJson(json, RespData.class);
        appedText("errorCode(Gson解析后)：" + respData.getErrorCode());
        appedText("resultMsg(Gson解析后)：" + respData.getResultMsg());

    }

    private static void appedText(String msg) {
        tv_resp.append("\n\n" + msg);
    }

}
