package com.soft224.cesatrack;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.*;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.core.splashscreen.SplashScreen;
/*
import androidx.lifecycle.lifecycleScope;
import kotlinx.coroutines.delay;
import kotlinx.coroutines.launch;*/

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
  private LinearLayout error;
  
  private ImageButton reload;
  
  private int reset = 0;
  
  Timer timer;
  
  private WebView webView;
  
  protected void onCreate(Bundle paramBundle) {
   // setTheme(R.style.MyApp.Splash);
     // SplashScreen.installSplashScreen(this);
    super.onCreate(paramBundle);
    setContentView(R.layout.activity_main);
    this.timer = new Timer();
    TimerTask timerTask = new TimerTask() {

        public void run() {
            //System.out.println("timer run exect with reset: "+reset);
          reset=0;
            //System.out.println("timer run exect with reset after: "+reset);
        }
      };
    this.timer.schedule(timerTask, 3000L, 4000L);
    this.webView = (WebView)findViewById(R.id.webview);
    this.error = (LinearLayout)findViewById(R.id.error);
   // ImageButton imageButton
      this.reload= (ImageButton)findViewById(R.id.reload);
   // this.reload = imageButton;
    reload.setOnClickListener(new View.OnClickListener() {




        public void onClick(View param1View) {
            Log.i("Click", "Reloading...");
            webView.reload();
          }
        });
    this.webView.getSettings().setJavaScriptEnabled(true);
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
          this.webView.getSettings().setAllowContentAccess(true);
      }
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
          this.webView.getSettings().setAllowFileAccess(true);
      }
      this.webView.getSettings().setBlockNetworkImage(false);
    this.webView.getSettings().setLoadsImagesAutomatically(true);
    this.webView.loadUrl("https://basic.satgroupe.com/app/mlogin.php");
    this.webView.setWebViewClient(new WebViewClient() {

          
          public void onPageStarted(WebView param1WebView, String param1String, Bitmap param1Bitmap) {
            super.onPageStarted(param1WebView, param1String, param1Bitmap);
          }
          
          public void onReceivedError(WebView param1WebView, WebResourceRequest param1WebResourceRequest, WebResourceError param1WebResourceError) {
            //super.onReceivedError(param1WebView, param1WebResourceRequest, param1WebResourceError);
              Log.d("pageReceivedError 1","errorCode: "+param1WebResourceRequest);
              if(param1WebResourceRequest.isForMainFrame()){
                  error.setVisibility(View.VISIBLE);
              }
          }

        @Override
        public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
            Log.d("onReceivedHttpError","errorCode: "+errorResponse.getStatusCode());
            if(errorResponse.getStatusCode()==400 || errorResponse.getStatusCode()==500){
                error.setVisibility(View.VISIBLE);
            }
        }

        public boolean shouldOverrideUrlLoading(View param1View, String param1String) {
            if (param1String.startsWith("http:") || param1String.startsWith("https:"))
              return false; 
            Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(param1String));
            MainActivity.this.startActivity(intent);
            return true;
          }
        });
    OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(true) {




        public void handleOnBackPressed() {
          //MainActivity.access$008(MainActivity.this);

          if (webView.canGoBack()) {
            webView.goBack();
          } else if (reset < 2) {
              reset++;
              Log.i("Info","Reset: "+reset);
            Toast.makeText((Context)MainActivity.this, "Taper encore la touche Arrière pour quitter", Toast.LENGTH_SHORT).show();
          } else {
            finish();
            System.exit(0);
          } 
        }
      };
    getOnBackPressedDispatcher().addCallback((LifecycleOwner)this, onBackPressedCallback);
  }
}
