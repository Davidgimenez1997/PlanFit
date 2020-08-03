package com.utad.david.planfit.Activitys.MainMenu;

import com.utad.david.planfit.Fragments.MainMenu.RootFragment;

public interface MainMenuView {
    void deviceOfflineMessage();
    void navigateLogout();
    void getUserData();
    void clickHeaderMenu();
    void setImagenUser(String imgUser);
    void setDefaultImagen();
    void clickDrawerMenu(int selected, RootFragment rootFragment, int itemId);
    void clickChatDrawerMenu(int itemId);
    void clickSportByType(int type, int title);
    void clickNutritionByType(int type, int tilte);

    void clickSportFavorite(int title);

    void clickNutritionFavorite(int title);

    void clickCreatePlan(int title);

    void clickShowPlan(int title);
    void clickSportPlan(int title);
    void clickNutritionPlan(int title);
    void clickSaveExit(int title);
    void clickShowSportPlan(int title);
    void clickShowNutritionPlan(int title);
}
