package com.utad.david.planfit.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import com.utad.david.planfit.Model.Sport.DefaultSport;

import com.utad.david.planfit.Model.Nutrition.NutritionSlimming;
import com.utad.david.planfit.Model.Nutrition.NutritionToning;
import com.utad.david.planfit.Model.Nutrition.NutritionGainVolume;

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

    // Save Nutrition In Shared Preferences

    public static void saveNutritionSlimming(List<NutritionSlimming> list, Context context, String name) {
        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        prefsEditor.putString(name, json);
        prefsEditor.commit();
    }

    public static void saveNutritionToning(List<NutritionToning> list, Context context, String name) {
        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        prefsEditor.putString(name, json);
        prefsEditor.commit();
    }

    public static void saveNutritionGainVolume(List<NutritionGainVolume> list, Context context, String name) {
        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        prefsEditor.putString(name, json);
        prefsEditor.commit();
    }

    // Load Nutrition In Shared Preferences

    public static List<NutritionSlimming> loadNutritionSlimming(Context context, String name) {
        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        Gson gson = new Gson();
        String json = appSharedPrefs.getString(name, "");
        return gson.fromJson(json, new TypeToken<ArrayList<NutritionSlimming>>(){}.getType());
    }

    public static List<NutritionToning> loadNutritionToning(Context context, String name) {
        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        Gson gson = new Gson();
        String json = appSharedPrefs.getString(name, "");
        return gson.fromJson(json, new TypeToken<ArrayList<NutritionToning>>(){}.getType());
    }

    public static List<NutritionGainVolume> loadNutritionGainVolume(Context context, String name) {
        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        Gson gson = new Gson();
        String json = appSharedPrefs.getString(name, "");
        return gson.fromJson(json, new TypeToken<ArrayList<NutritionGainVolume>>(){}.getType());
    }

}
