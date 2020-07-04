package com.utad.david.planfit.Fragments.MainMenu.Sport;

import android.content.Context;

import com.utad.david.planfit.Data.Sport.GetSport;
import com.utad.david.planfit.Data.Sport.SportRepository;
import com.utad.david.planfit.Model.Sport.DefaultSport;
import com.utad.david.planfit.Utils.Constants;
import com.utad.david.planfit.Utils.SharedPreferencesManager;
import com.utad.david.planfit.Utils.UtilsNetwork;

import java.util.Collections;
import java.util.List;

public class SportPresenter implements GetSport {

    private SportView view;
    private int mode;
    private Context context;

    private List<DefaultSport> sportToningsCache;
    private List<DefaultSport> sportSlimmingsCache;
    private List<DefaultSport> sportGainVolumesCache;


    public SportPresenter(SportView view, int mode, Context context) {
        this.view = view;
        this.mode = mode;
        this.context = context;
        SportRepository.getInstance().setGetSport(this);
    }

    /**
     * Load data
     * If download data read for shared preferences if not download
     * call to sport repository for get data
     */
    public void loadData() {
        if (UtilsNetwork.checkConnectionInternetDevice(this.context)) {
            switch (this.mode) {
                case Constants.SportNutritionOption.SLIMMING:
                    this.sportSlimmingsCache = SharedPreferencesManager.loadSportList(this.context, SharedPreferencesManager.SPORT_SLIMING_TAG);
                    if (this.sportSlimmingsCache == null) {
                        SportRepository.getInstance().getSportList(this.mode);
                    } else {
                        this.view.configurationRecycleView(this.sportSlimmingsCache);
                    }
                    break;
                case Constants.SportNutritionOption.TONING:
                    this.sportToningsCache = SharedPreferencesManager.loadSportList(this.context, SharedPreferencesManager.SPORT_TONING_TAG);
                    if (this.sportToningsCache == null) {
                        SportRepository.getInstance().getSportList(this.mode);
                    } else {
                        this.view.configurationRecycleView(this.sportToningsCache);
                    }
                    break;
                case Constants.SportNutritionOption.GAIN_VOLUMEN:
                    this.sportGainVolumesCache = SharedPreferencesManager.loadSportList(this.context, SharedPreferencesManager.SPORT_GAIN_VOLUMEN_TAG);
                    if (this.sportGainVolumesCache == null) {
                        SportRepository.getInstance().getSportList(this.mode);
                    } else {
                        this.view.configurationRecycleView(this.sportGainVolumesCache);
                    }
                    break;
            }
        } else {
            this.view.deviceOfflineMessage();
        }
    }

    public void onListItemClick(DefaultSport item, int mode) {
        switch (mode) {
            case Constants.SportNutritionOption.SLIMMING:
                this.view.onClickItemRecycleView(item, mode);
                break;
            case Constants.SportNutritionOption.TONING:
                this.view.onClickItemRecycleView(item, mode);
                break;
            case Constants.SportNutritionOption.GAIN_VOLUMEN:
                this.view.onClickItemRecycleView(item, mode);
                break;
        }
    }

    /**
     * Callback firebase (SportRepository)
     */
    @Override
    public void getSportList(boolean status, List<DefaultSport> list, int mode) {
        if (status) {
            Collections.sort(list);
            switch (mode) {
                case Constants.SportNutritionOption.SLIMMING:
                    SharedPreferencesManager.saveSportList(list, this.context, SharedPreferencesManager.SPORT_SLIMING_TAG);
                    break;
                case Constants.SportNutritionOption.TONING:
                    SharedPreferencesManager.saveSportList(list, this.context, SharedPreferencesManager.SPORT_TONING_TAG);
                    break;
                case Constants.SportNutritionOption.GAIN_VOLUMEN:
                    SharedPreferencesManager.saveSportList(list, this.context, SharedPreferencesManager.SPORT_GAIN_VOLUMEN_TAG);
                    break;
            }
            this.view.configurationRecycleView(list);
        }
    }
}
