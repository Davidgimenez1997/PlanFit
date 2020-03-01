package com.utad.david.planfit.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import com.utad.david.planfit.Model.Sport.SportSlimming;
import com.utad.david.planfit.Model.Sport.SportToning;
import com.utad.david.planfit.Model.Sport.SportGainVolume;

import com.utad.david.planfit.Model.Nutrition.NutritionSlimming;
import com.utad.david.planfit.Model.Nutrition.NutritionToning;
import com.utad.david.planfit.Model.Nutrition.NutritionGainVolume;

import java.util.ArrayList;
import java.util.List;

public class SharedPreferencesManager {

    public static final String SPORT_SLIMING_TAG = "SportSlimming";
    public static final String SPORT_FAVORITE_SLIMING_TAG = "SportFavoriteSlimming";
    public static final String SPORT_TONING_TAG = "SportToning";
    public static final String SPORT_FAVORITE_TONING_TAG = "SportFavoriteToning";
    public static final String SPORT_GAIN_VOLUMEN_TAG = "SportGainVolumen";
    public static final String SPORT_FAVORITE_GAIN_VOLUMEN_TAG = "SportGainFavoriteVolumen";

    public static final String NUTRITION_SLIMING_TAG = "NutritionSlimming";
    public static final String NUTRITION_TONING_TAG = "NutritionToning";
    public static final String NUTRITION_GAIN_VOLUMEN_TAG = "NutritionGainVolumen";

    // Save Sport In Shared Preferences

    public static void saveSportSlimming(List<SportSlimming> list, Context context, String name) {
        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        prefsEditor.putString(name, json);
        prefsEditor.commit();
    }

    public static void saveSportToning(List<SportToning> list, Context context, String name) {
        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        prefsEditor.putString(name, json);
        prefsEditor.commit();
    }

    public static void saveSportGainVolume(List<SportGainVolume> list, Context context, String name) {
        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        prefsEditor.putString(name, json);
        prefsEditor.commit();
    }

    // Load Sport In Shared Preferences

    public static List<SportSlimming> loadSportSlimming(Context context, String name) {
        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        Gson gson = new Gson();
        String json = appSharedPrefs.getString(name, "");
        return gson.fromJson(json, new TypeToken<ArrayList<SportSlimming>>(){}.getType());
    }

    public static List<SportToning> loadSportToning(Context context, String name) {
        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        Gson gson = new Gson();
        String json = appSharedPrefs.getString(name, "");
        return gson.fromJson(json, new TypeToken<ArrayList<SportToning>>(){}.getType());
    }

    public static List<SportGainVolume> loadSportGainVolume(Context context, String name) {
        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        Gson gson = new Gson();
        String json = appSharedPrefs.getString(name, "");
        return gson.fromJson(json, new TypeToken<ArrayList<SportGainVolume>>(){}.getType());
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
