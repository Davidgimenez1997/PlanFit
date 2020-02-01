package com.utad.david.planfit.Activitys;

import android.os.Bundle;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.utad.david.planfit.Utils.Constants;
import com.utad.david.planfit.Utils.YoutubePlayerConfig;
import com.utad.david.planfit.R;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

public class YoutubeActivity extends YouTubeBaseActivity {


    /******************************** VARIABLES *************************************+/
     *
     */

    private YouTubePlayerView youTubePlayerView;
    private YouTubePlayer.OnInitializedListener onInitializedListener;
    private String urlData;
    private String URL = Constants.ConfigureYouTube.EXTRA_URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Fabric.with(this, new Crashlytics());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube);
        youTubePlayerView = findViewById(R.id.youtube_player);
        urlData = getIntent().getStringExtra(URL);
        onInitializedListener();
    }

    /******************************** CONFIGURA LA REPRODUCION DEL VIDEO *************************************+/
     *
     */

    private void onInitializedListener(){
        onInitializedListener = new YouTubePlayer.OnInitializedListener(){
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                youTubePlayer.setFullscreen(true);
                youTubePlayer.loadVideo(urlData);
                youTubePlayer.play();
            }
            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        };
        youTubePlayerView.initialize(YoutubePlayerConfig.getApiKey(),onInitializedListener);
    }
}
