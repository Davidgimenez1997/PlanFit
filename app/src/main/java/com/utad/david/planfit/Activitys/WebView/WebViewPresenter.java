package com.utad.david.planfit.Activitys.WebView;
import android.view.KeyEvent;

public class WebViewPresenter {

    private int mode;
    private String title;
    private String url;
    private WebViewView view;

    public WebViewPresenter(WebViewView view) {
        this.view = view;
    }

    public void setExtras(String title, String url, int mode) {
        this.title = title;
        this.url = url;
        this.mode = mode;
    }

    public int getMode() {
        return mode;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public void onKeyDown(int keyCode, KeyEvent event, boolean goBack) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                if (goBack) {
                    this.view.goBackWebView();
                } else {
                    this.view.finishActivity();
                }
            }
        }
    }
}
