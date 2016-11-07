package uniftec.bsocial;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

public class FAQActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);

        WebView faq = (WebView) findViewById(R.id.webview_faq);
        faq.getSettings().setJavaScriptEnabled(true);
        faq.loadUrl("http://ec2-54-218-233-242.us-west-2.compute.amazonaws.com");
    }

}
