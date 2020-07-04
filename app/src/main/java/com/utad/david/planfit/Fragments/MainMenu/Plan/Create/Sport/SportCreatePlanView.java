package com.utad.david.planfit.Fragments.MainMenu.Plan.Create.Sport;

import com.utad.david.planfit.Model.Sport.DefaultSport;
import java.util.List;

public interface SportCreatePlanView {
    void deviceOfflineMessage();
    void getSportFavoriteList(List<DefaultSport> list);
    void getEmptySportFavoriteList();
    void clickItem(DefaultSport item);
}
