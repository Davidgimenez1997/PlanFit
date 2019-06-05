package com.utad.david.planfit.Base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

public abstract class BaseFragment extends Fragment {

    protected BaseFragment fragment;
    protected BaseActivity activity;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity = (BaseActivity) getActivity();
    }

    public void showLoading() {
        activity.showLoading();
    }

    public void hideLoading() {
        activity.hideLoading();
    }

}
