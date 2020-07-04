package com.utad.david.planfit.Fragments.MainMenu.Favorite.Sport;

import com.utad.david.planfit.Model.Sport.DefaultSport;
import java.util.List;

public interface SportFavoriteView {
    void deviceOfflineMessage();
    void updateEmptyUI();
    void getFavoriteList(List<DefaultSport> data);
}
