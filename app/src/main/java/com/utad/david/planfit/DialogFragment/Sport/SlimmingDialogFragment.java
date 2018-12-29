package com.utad.david.planfit.DialogFragment.Sport;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.google.android.gms.common.internal.Constants;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.utad.david.planfit.Model.Sport.Slimming;

import com.utad.david.planfit.R;

import java.util.ArrayList;

public class SlimmingDialogFragment extends DialogFragment{

    public Slimming slimming;
    private static String KEY = "Item";

    public static final String API_KEY = "AIzaSyDSXa5CPSaAJKDCFUgBLWXCYT8fU3QBZUc";

    public static final String VIDEO_ID = "G3IpRgeS9c8";

    //Creamos una nueva instancia de esta clase y le pasamos por parámetro un objeto de
    // communities para más adelante recogerlo con los argumentos
    public static SlimmingDialogFragment newInstance(Slimming slimming) {
        SlimmingDialogFragment fragment = new SlimmingDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable(KEY,slimming);
        fragment.setArguments(args);
        return fragment;
    }

    //Nuestra variable communities coge el valor que se le está pasando
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        slimming = getArguments().getParcelable(KEY);
    }

    private static View view;
    private YouTubePlayerSupportFragment youTubePlayerFragment;
    private YouTubePlayer youTubePlayer;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.sport_dialog_fragment, container, false);
            view.setBackgroundResource(R.drawable.corner_dialog_fragment);
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            initializeYoutubePlayer();

            findById(view);
        } catch (InflateException e) {
            /* map is already there, just return view as it is */
        }



        return view;
    }

    private void initializeYoutubePlayer() {

        youTubePlayerFragment = (YouTubePlayerSupportFragment) getFragmentManager()
                .findFragmentById(R.id.youtubesupportfragment);

        if (youTubePlayerFragment == null)
            return;

        youTubePlayerFragment.initialize(API_KEY, new YouTubePlayer.OnInitializedListener() {

            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player,
                                                boolean wasRestored) {
                if (!wasRestored) {
                    youTubePlayer = player;

                    //set the player style default
                    youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);

                    youTubePlayer.cueVideo(slimming.getVideo());


                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider arg0, YouTubeInitializationResult arg1) {

                //print or show error if initialization failed
                Log.e("Youtube", "Youtube Player View initialization failed");
            }
        });
    }

    public void findById(View v){

    }
}