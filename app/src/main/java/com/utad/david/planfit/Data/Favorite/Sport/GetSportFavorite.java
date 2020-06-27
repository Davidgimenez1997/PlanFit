package com.utad.david.planfit.Data.Favorite.Sport;

import com.utad.david.planfit.Model.Sport.DefaultSport;
import java.util.List;

public interface GetSportFavorite {
    void addSportFavorite (boolean status);
    void deleteSportFavorite(boolean status);
    void getSportFavoriteListByType(boolean status, List<DefaultSport> list);
    void getSportFavoriteList(boolean status, List<DefaultSport> defaultSports);
    void getEmptySportFavoriteList(boolean status);
}
