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
import android.widget.TextView;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.utad.david.planfit.Model.Sport.Slimming;

import com.utad.david.planfit.R;

public class SlimmingDialogFragment extends DialogFragment {

    public Slimming slimming;
    private static String KEY = "Item";

    public static final String API_KEY = "AIzaSyDSXa5CPSaAJKDCFUgBLWXCYT8fU3QBZUc";

    public static SlimmingDialogFragment newInstance(Slimming slimming) {
        SlimmingDialogFragment fragment = new SlimmingDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable(KEY, slimming);
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
            view = inflater.inflate(R.layout.sport_dialog_fragment, container, false);
            view.setBackgroundResource(R.drawable.corner_dialog_fragment);
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            initializeYoutubePlayer();

            TextView textView = view.findViewById(R.id.textViewnamedialogSport);
            textView.setText(slimming.getName());
            findById(view);
        return view;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        youTubePlayerFragment = (YouTubePlayerSupportFragment) getFragmentManager()
                .findFragmentById(R.id.youtubesupportfragment);
        if (youTubePlayer != null)
            getFragmentManager().beginTransaction().remove(youTubePlayerFragment).commit();
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

    public void findById(View v) {

    }
}