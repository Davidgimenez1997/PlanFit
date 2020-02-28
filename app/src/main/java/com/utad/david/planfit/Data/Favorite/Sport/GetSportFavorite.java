package com.utad.david.planfit.Data.Favorite.Sport;

import com.utad.david.planfit.Model.Sport.DefaultSport;
import com.utad.david.planfit.Model.Sport.SportGainVolume;
import com.utad.david.planfit.Model.Sport.SportSlimming;
import com.utad.david.planfit.Model.Sport.SportToning;

import java.util.List;

public interface GetSportFavorite {
    void addSportFavorite (boolean status);
    void deleteSportFavorite(boolean status);
    void getSportSlimmingFavorite(boolean status, List<SportSlimming> sportSlimmings);
    void getSportToningFavorite(boolean status, List<SportToning> sportTonings);
    void getSportGainVolumeFavorite(boolean status, List<SportGainVolume> sportGainVolumes);
    void getSportAllFavorite(boolean status, List<DefaultSport> defaultSports);
    void emptySportFavorite(boolean status);
}
