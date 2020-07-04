package com.utad.david.planfit.Fragments.MainMenu.Sport;

import com.utad.david.planfit.Model.Sport.DefaultSport;

import java.util.List;

public interface SportView {
    void configurationRecycleView(List<DefaultSport> data);
    void onClickItemRecycleView(DefaultSport defaultSport, int mode);
    void deviceOfflineMessage();
}
