package vn.viethoang.truong.tkbclient;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class GopYActivity extends AppCompatActivity {
    private WebView wbFeedBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gop_y);

        if(isNetworkConnected()){
            wbFeedBack = findViewById(R.id.wbFeedBack);

            wbFeedBack.setWebViewClient(new WebViewClient());
            wbFeedBack.loadUrl("https://docs.google.com/forms/d/e/1FAIpQLScbAZ4xY3AXKFOFccGo1oq8hCcTJQ2wcHArHwk0dMyyHJDsrA/viewform");
            wbFeedBack.getSettings().setJavaScriptEnabled(true);
        }else {
            Toast.makeText(this, "Không có kết nối internet", Toast.LENGTH_LONG).show();
            Intent intent= new Intent(this, TKBMainActivity.class);
            startActivity(intent);
        }

        }
    private boolean isNetworkConnected() {
        // Hàm kiểm tra có kết nối intenet chưa
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;

    }
}
