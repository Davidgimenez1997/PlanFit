package com.utad.david.planfit.Fragments.FragmentsFirstActivity.Login;

import com.utad.david.planfit.Base.BasePresenter;
import com.utad.david.planfit.Base.BaseView;
import com.utad.david.planfit.Data.SessionUser;

public class LoginPresenter extends BasePresenter {

    private LoginView view;

    @Override
    public void onViewAttached(BaseView view) {
        this.view = (LoginView) view;
    }

    @Override
    public void onViewDetached() {
        this.view = null;
    }

    public void checkLoggedUser() {
        SessionUser.getInstance().firebaseAdmin.authStateListener = firebaseAuth -> {
            SessionUser.getInstance().firebaseAdmin.currentUser = firebaseAuth.getCurrentUser();
            if (SessionUser.getInstance().firebaseAdmin.currentUser != null) {
                view.userStatusLogged();


            }
        };
    }


}
