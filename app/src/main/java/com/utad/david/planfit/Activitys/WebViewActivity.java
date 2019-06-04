package com.utad.david.planfit.Activitys;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.utad.david.planfit.R;
import com.utad.david.planfit.Utils.Constants;

public class WebViewActivity extends AppCompatActivity {

    public static String EXTRA_TITLE = Constants.ConfigurateWebView.EXTRA_TITLE;
    public static String EXTRA_URL = Constants.ConfigurateWebView.EXTRA_URL;
    public static String EXTRA_MODE = Constants.ModeWebView.EXTRA_MODE;

    private WebView webView;
    private String title;
    private String url;
    private int mode;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            title = extras.getString(EXTRA_TITLE);
            url = extras.getString(EXTRA_URL);
            mode = extras.getInt(EXTRA_MODE);
        }

        toolbar = findViewById(R.id.toolbar);
        webView = findViewById(R.id.webView);

        setUI();
    }

    private void setUI() {
        setSupportActionBar(toolbar);
        setTitle(title);
        configureWebView(url);
    }

    private ProgressDialog progressDialog;

    public void showLoading() {
        if (progressDialog != null && progressDialog.isShowing()) {
            return;
        }
        progressDialog = new ProgressDialog(this, R.style.TransparentProgressDialog);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.setCancelable(false);
        RotateAnimation rotate = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setInterpolator(new LinearInterpolator());
        rotate.setDuration(1000);
        rotate.setRepeatCount(Animation.INFINITE);
        ImageView ivLoading = ButterKnife.findById(progressDialog, R.id.image_cards_animation);
        ivLoading.startAnimation(rotate);
        progressDialog.show();
    }

    public void hideLoading() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }


    private void configureWebView(String url){
        webView.setVerticalScrollBarEnabled(false);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                showLoading();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                hideLoading();
            }
        });
        webView.loadUrl(url);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (webView.canGoBack()) {
                        webView.goBack();
                    } else {
                        finish();
                    }
                    return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_option_web, menu);
        switch (mode){
            case Constants.ModeWebView.MODE_RECIPE:
                menu.getItem(0).setTitle(getString(R.string.mode_recipe));
                break;
            case Constants.ModeWebView.MODE_LINKEDIN:
                menu.getItem(0).setTitle(getString(R.string.mode_linkedin));
                break;
            case Constants.ModeWebView.MODE_PRIVACITY:
                menu.getItem(0).setTitle(getString(R.string.abrir_politica));
                break;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.nav_open_web:
                openBrowser(url);
                break;
        }

        return true;
    }

    private void openBrowser(String url) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

}
