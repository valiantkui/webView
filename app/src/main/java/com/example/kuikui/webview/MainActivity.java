package com.example.kuikui.webview;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private WebView webView;

    private ProgressDialog webDialog;//显示网页加载的进度
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web);
//        Uri uri = Uri.parse("www.baidu.com");
//        Intent intent = new Intent(Intent.ACTION_VIEW,uri);
//       // startActivity(intent);

        init2();

    }

    private void init2(){
        webView = findViewById(R.id.webView);
        webView.loadUrl("file:///android_asset/onChange.html");
       // webView.loadUrl("http://www.runoob.com");
    }

    private void init(){
        webView = findViewById(R.id.webView);

        /*
            1.访问本地资源
         */
      //  webView.loadUrl("file:///android_asset/onChange.html");

        /*
            2.访问外地资源
         */
        webView.loadUrl("http://www.runoob.com");
       // Toast.makeText(MainActivity.this,"test",Toast.LENGTH_LONG).show();
        //覆盖webView默认通过第三方或者时系统浏览器打开网页的行为，使得网页可以在webView中打开


        webView.setWebViewClient(new WebViewClient(){
            @Override
            /**
             * 当返回值为true时，控制网页在webView中打开
             * 如果为false,调用系统浏览器或者第三方浏览器打开
             */
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Toast.makeText(MainActivity.this,url,Toast.LENGTH_LONG).show();
                view.loadUrl(url);
                return true;
            }
            //webViewClient帮助webView去处理一些页面控制和请求；

        });

        //启用支持javaScript
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);

        /*
         *webView加载页面优先使用缓存加载
         */
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);


        webView.setWebChromeClient(new WebChromeClient(){

            @Override
            /**
             * newProgress:1--100之间的整数，网页加载的进度
             */
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    //网页加载完毕,关闭ProgressDialog
                    closeDialog();
                } else {
                    //网页正在加载,打开ProgressDialog
                    openDialog(newProgress);
                }

                /**
                 * 打开进度条
                 */
            }
            private void openDialog(int newProgress){
                if(webDialog == null){
                    webDialog = new ProgressDialog(MainActivity.this);//其中的参数表示对话框显示的父容器
                    webDialog.setTitle("正在加载");
                    webDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    webDialog.setProgress(newProgress);
                    webDialog.show();
                }else{
                    webDialog.setProgress(newProgress);
                    webDialog.show();
                }

            }

            /**
             * 关闭进度条
             */
            private void closeDialog(){
                if(webDialog != null && webDialog.isShowing()){
                    webDialog.dismiss();//关闭对话进度条
                    webDialog = null;
                }
            }

        });
    }

    //改写物理按键--返回的逻辑

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode == KeyEvent.KEYCODE_BACK){

            //test
            //Toast.makeText(MainActivity.this,webView.getUrl(),Toast.LENGTH_LONG).show();
            //判断webView能否返回上一级页面
            if(webView.canGoBack()){
                webView.goBack();
                return true;
            }else{
                System.exit(0);//退出程序
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
