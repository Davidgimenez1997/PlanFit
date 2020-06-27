package com.utad.david.planfit.DialogFragment.Sport;

import com.utad.david.planfit.Model.Sport.DefaultSport;

import java.util.List;

public interface SportDetailsView {
    void deviceOfflineMessage();
    void addFavoriteSport();
    void deleteFavoriteSport();
    void getFavoriteSportList();
    void updateButtonsUi();
}
