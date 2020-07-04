package com.utad.david.planfit.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.utad.david.planfit.Model.Developer.Developer;
import com.utad.david.planfit.Model.Nutrition.DefaultNutrition;
import com.utad.david.planfit.Model.Sport.DefaultSport;
import java.util.ArrayList;
import java.util.List;

public class SharedPreferencesManager {

    public static final String RESET_SHARED_PREFERENCES = "RESETSHAREDPREFERENCES";

    public static final String SPORT_SLIMING_TAG = "SportSlimming";
    public static final String SPORT_TONING_TAG = "SportToning";
    public static final String SPORT_GAIN_VOLUMEN_TAG = "SportGainVolumen";

    public static final String NUTRITION_SLIMING_TAG = "NutritionSlimming";
    public static final String NUTRITION_TONING_TAG = "NutritionToning";
    public static final String NUTRITION_GAIN_VOLUMEN_TAG = "NutritionGainVolumen";

    public static final String DEVELOPER_INFO = "DeveloperInfo";

    /**
     * Clear all shared preferences for update structure
     * @param context use to shared preferences
     */
    public static void clearAllSharedPreferences(Context context) {
        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        int resetSharedPreferences = appSharedPrefs.getInt(RESET_SHARED_PREFERENCES, 0);
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        if (resetSharedPreferences == 0) {
            prefsEditor.clear();
            prefsEditor.putInt(RESET_SHARED_PREFERENCES, 1);

        }
        prefsEditor.commit();
    }

    /**
     * Save Sport List Get Firebase In SharedPreferences
     * @param list to save
     * @param context use to share preferences
     * @param tag search share preferences
     */
    public static void saveSportList(List<DefaultSport> list, Context context, String tag) {
        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        prefsEditor.putString(tag, json);
        prefsEditor.commit();
    }

    /**
     * Load Sport list Save In SharedPreferences
     * @param context use to sharedpreferences
     * @param tag search share preferences
     * @return sport list
     */
    public static List<DefaultSport> loadSportList(Context context, String tag) {
        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        Gson gson = new Gson();
        String json = appSharedPrefs.getString(tag, "");
        return gson.fromJson(json, new TypeToken<ArrayList<DefaultSport>>(){}.getType());
    }

    /**
     * Save Nutrition List Get Firebase In SharedPreferences
     * @param list to save
     * @param context use to share preferences
     * @param tag search share preferences
     */
    public static void saveNutritionList(List<DefaultNutrition> list, Context context, String tag) {
        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        prefsEditor.putString(tag, json);
        prefsEditor.commit();
    }

    /**
     * Load Nutrition list Save In SharedPreferences
     * @param context use to sharedpreferences
     * @param tag search share preferences
     * @return nutrition list
     */
    public static List<DefaultNutrition> loadNutritionList(Context context, String tag) {
        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        Gson gson = new Gson();
        String json = appSharedPrefs.getString(tag, "");
        return gson.fromJson(json, new TypeToken<ArrayList<DefaultNutrition>>(){}.getType());
    }

    /**
     * Save developer data in SharedPreferences
     * @param developer info to save
     * @param context use to sharedpreferences
     * @param tag for search
     */
    public static void saveDeveloper(Developer developer, Context context, String tag) {
        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        Gson gson =  new Gson();
        String json = gson.toJson(developer);
        prefsEditor.putString(tag, json);
        prefsEditor.commit();
    }

    /**
     * Load  developer info
     * @param context use to sharedpreferences
     * @param tag for search info
     * @return developer data
     */
    public static Developer loadDeveloper(Context context, String tag) {
        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        Gson gson = new Gson();
        String json = appSharedPrefs.getString(tag, "");
        return gson.fromJson(json, Developer.class);
    }

}
