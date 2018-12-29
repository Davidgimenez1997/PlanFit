package com.utad.david.planfit.Activitys;

import android.os.Bundle;
import android.widget.Button;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.utad.david.planfit.R;


public class YoutubeActivity extends YouTubeBaseActivity {

    public static final String API_KEY = "AIzaSyDSXa5CPSaAJKDCFUgBLWXCYT8fU3QBZUc";
    YouTubePlayerView youTubePlayerView;
    YouTubePlayer.OnInitializedListener onInitializedListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube);

        final String url = getIntent().getStringExtra("url");

        youTubePlayerView = (YouTubePlayerView) findViewById(R.id.youtube_player);

        onInitializedListener = new YouTubePlayer.OnInitializedListener(){

            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {

                youTubePlayer.setFullscreen(true);
                youTubePlayer.loadVideo(url);
                youTubePlayer.play();
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        };

        youTubePlayerView.initialize(API_KEY,onInitializedListener);

    }
}
