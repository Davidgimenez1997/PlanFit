package com.utad.david.planfit.Activitys.MainMenu;

import android.content.Context;

import com.utad.david.planfit.Data.User.SessionUser;
import com.utad.david.planfit.Data.User.User.GetUser;
import com.utad.david.planfit.Data.User.User.UserRepository;
import com.utad.david.planfit.Fragments.MainMenu.RootFragment;
import com.utad.david.planfit.R;
import com.utad.david.planfit.Utils.Constants;
import com.utad.david.planfit.Utils.UtilsNetwork;

public class MainMenuPresenter implements GetUser {

    private MainMenuView view;

    public MainMenuPresenter(MainMenuView view) {
        this.view = view;
        UserRepository.getInstance().setGetUser(this);
    }

    public boolean checkInternetDevice(Context context) {
        if (UtilsNetwork.checkConnectionInternetDevice(context)) {
            return true;
        } else {
            this.view.deviceOfflineMessage();
            return false;
        }
    }

    public void clickHeaderMenu(Context context) {
        if (this.checkInternetDevice(context)) {
            this.view.clickHeaderMenu();
        }
    }

    public void loadData() {
        UserRepository.getInstance().getUserData();
    }

    public void checkImagenUser(String imgUser) {
        if (imgUser != null && !imgUser.equals("")) {
            this.view.setImagenUser(imgUser);
        } else {
            this.view.setDefaultImagen();
        }
    }

    public void logout() {
        SessionUser.getInstance().removeUser();
        UserRepository.getInstance().logout();
        this.view.navigateLogout();
    }

    public void onClickDrawerMenu(int itemId) {
        RootFragment rootFragment = null;
        int selected = 0;
        switch (itemId) {
            case R.id.nav_deportes:
                selected = Constants.ModeRootFragment.MODE_SPORT;
                rootFragment = this.getRootFragmentByType(selected);
                this.view.clickDrawerMenu(selected, rootFragment, itemId);
                break;
            case R.id.nav_nutricion:
                selected = Constants.ModeRootFragment.MODE_NUTRITION;
                rootFragment = this.getRootFragmentByType(selected);
                this.view.clickDrawerMenu(selected, rootFragment, itemId);
                break;
            case R.id.nav_favorite:
                selected = Constants.ModeRootFragment.MODE_FAVORITE;
                rootFragment = this.getRootFragmentByType(selected);
                this.view.clickDrawerMenu(selected, rootFragment, itemId);
                break;
            case R.id.nav_crear_tu_plan:
                selected = Constants.ModeRootFragment.MODE_PLAN;
                rootFragment = this.getRootFragmentByType(selected);
                this.view.clickDrawerMenu(selected, rootFragment, itemId);
                break;
            case R.id.nav_user:
                this.view.clickChatDrawerMenu(itemId);
                break;
        }
    }

    public RootFragment getRootFragmentByType(int type) {
        return RootFragment.newInstance(type);
    }

    public void clickSportByType(int type, int title) {
        this.view.clickSportByType(type, title);
    }

    public void clickNutritionByType(int type, int tilte) {
        this.view.clickNutritionByType(type, tilte);
    }

    public void clickSportFavorite(int title) {
        this.view.clickSportFavorite(title);
    }

    public void clickNutritionFavorite(int title) {
        this.view.clickNutritionFavorite(title);
    }

    public void clickCreatePlan(int title) {
        this.view.clickCreatePlan(title);
    }

    public void clickShowPlan(int title) {
        this.view.clickShowPlan(title);
    }

    public void clickSportPlan(int title) {
        this.view.clickSportPlan(title);
    }

    public void clickNutritionPlan(int title) {
        this.view.clickNutritionPlan(title);
    }

    public void clickShowSportPlan(int title) {
        this.view.clickShowSportPlan(title);
    }

    public void clickShowNutritionPlan(int title) {
        this.view.clickShowNutritionPlan(title);
    }

    public void clickSaveExit(int title) {
        this.view.clickSaveExit(title);
    }

    @Override
    public void getUserData(boolean status) {
        if (status) {
            this.view.getUserData();
        }  else {
            if (UserRepository.getInstance().getUser().getImgUser() != null) {
                this.view.getUserData();
            }
        }
    }

    @Override
    public void addUserData(boolean status) {}
}
