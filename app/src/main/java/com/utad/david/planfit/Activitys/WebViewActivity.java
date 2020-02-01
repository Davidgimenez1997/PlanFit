package com.utad.david.planfit.Activitys;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.utad.david.planfit.Base.BaseActivity;
import com.utad.david.planfit.R;
import com.utad.david.planfit.Utils.Constants;

public class WebViewActivity extends BaseActivity {


    /******************************** VARIABLES *************************************+/
     *
     */

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

    /******************************** CONFIGURA LA VISTA *************************************+/
     *
     */

    private void setUI() {
        setSupportActionBar(toolbar);
        setTitle(title);
        configureWebView(url);
    }


    /******************************** CONFIGURA EL WEBVIEW *************************************+/
     *
     */

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

    /******************************** SI LE DAS ATRAS DENTRO DEL WEBVIEW *************************************+/
     *
     */

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

    /******************************** BOTON ATRAS DEL TELEFONO *************************************+/
     *
     */

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            finish();
        }
    }


    /******************************** CREA MENU DE LA IZQUIERDA *************************************+/
     *
     */

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
                case Constants.ModeWebView.MODE_CHAT:
                    menu.getItem(0).setTitle(getString(R.string.abrir_enlace));
                    break;
        }
        return true;
    }

    /******************************** AL PINCHAR EN EL ITEM DEL MENU *************************************+/
     *
     */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.nav_open_web:
                openBrowser(url);
                break;
        }

        return true;
    }

    /******************************** ABRE EL NAVEGADOR *************************************+/
     *
     */

    private void openBrowser(String url) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }
}
