package com.utad.david.planfit.Utils;

import com.utad.david.planfit.R;

public class YoutubePlayerConfig {

    public YoutubePlayerConfig() {
    }

    //API KEY YoutubePlayer
    public static final String API_KEY = String.valueOf(R.string.API_KEY);

    public static String getApiKey() {
        return API_KEY;
    }
}