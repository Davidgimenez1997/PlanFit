package com.utad.david.planfit.Fragments.MainMenu.Nutrition;

import android.content.Context;

import com.utad.david.planfit.Data.Nutrition.GetNutrition;
import com.utad.david.planfit.Data.Nutrition.NutritionRepository;
import com.utad.david.planfit.Model.Nutrition.DefaultNutrition;
import com.utad.david.planfit.Utils.Constants;
import com.utad.david.planfit.Utils.SharedPreferencesManager;
import com.utad.david.planfit.Utils.UtilsNetwork;

import java.util.Collections;
import java.util.List;

public class NutritionPresenter implements GetNutrition {

    private NutritionView view;
    private int mode;
    private Context context;

    private List<DefaultNutrition> nutritionToningsCache;
    private List<DefaultNutrition> nutritionSlimmingsCache;
    private List<DefaultNutrition> nutritionGainVolumesCache;

    public NutritionPresenter(NutritionView view, int mode, Context context) {
        this.view =  view;
        this.mode = mode;
        this.context = context;
        NutritionRepository.getInstance().setGetNutrition(this);
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
                    this.nutritionSlimmingsCache = SharedPreferencesManager.loadNutritionList(this.context, SharedPreferencesManager.NUTRITION_SLIMING_TAG);
                    if (this.nutritionSlimmingsCache == null) {
                        NutritionRepository.getInstance().getNutritionList(this.mode);
                    } else {
                        this.view.configurationRecycleView(this.nutritionSlimmingsCache);
                    }
                    break;
                case Constants.SportNutritionOption.TONING:
                    this.nutritionToningsCache = SharedPreferencesManager.loadNutritionList(this.context, SharedPreferencesManager.NUTRITION_TONING_TAG);
                    if (this.nutritionToningsCache == null) {
                        NutritionRepository.getInstance().getNutritionList(this.mode);
                    } else {
                        this.view.configurationRecycleView(this.nutritionToningsCache);
                    }
                    break;
                case Constants.SportNutritionOption.GAIN_VOLUMEN:
                    this.nutritionGainVolumesCache = SharedPreferencesManager.loadNutritionList(this.context, SharedPreferencesManager.NUTRITION_GAIN_VOLUMEN_TAG);
                    if (this.nutritionGainVolumesCache == null) {
                        NutritionRepository.getInstance().getNutritionList(this.mode);
                    } else {
                        this.view.configurationRecycleView(this.nutritionGainVolumesCache);
                    }
                    break;
            }
        } else {
            this.view.deviceOfflineMessage();
        }
    }

    public void onListItemClick(DefaultNutrition item, int mode) {
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
     * Callback firebase (NutritionRepository)
     */
    @Override
    public void getNutritionList(boolean status, List<DefaultNutrition> list, int mode) {
        if (status) {
            Collections.sort(list);
            switch (mode) {
                case Constants.SportNutritionOption.SLIMMING:
                    SharedPreferencesManager.saveNutritionList(list, this.context, SharedPreferencesManager.NUTRITION_SLIMING_TAG);
                    break;
                case Constants.SportNutritionOption.TONING:
                    SharedPreferencesManager.saveNutritionList(list, this.context, SharedPreferencesManager.NUTRITION_TONING_TAG);
                    break;
                case Constants.SportNutritionOption.GAIN_VOLUMEN:
                    SharedPreferencesManager.saveNutritionList(list, this.context, SharedPreferencesManager.NUTRITION_GAIN_VOLUMEN_TAG);
                    break;
            }
            this.view.configurationRecycleView(list);
        }
    }
}
