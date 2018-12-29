package com.utad.david.planfit.DialogFragment.Sport;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.utad.david.planfit.Activitys.YoutubeActivity;
import com.utad.david.planfit.Model.Sport.GainVolume;
import com.utad.david.planfit.Model.Sport.Slimming;

import com.utad.david.planfit.Model.Sport.Toning;
import com.utad.david.planfit.R;


public class SportDetailsDialogFragment extends DialogFragment {

    public Slimming slimming;
    public GainVolume gainVolume;
    public Toning toning;
    public int option;
    private static String SLIMMING = "SLIMMING";
    private static String GAINVOLUME = "GAINVOLUME";
    private static String TONING = "TONING";
    private static String OPTION = "OPTION";


    public static SportDetailsDialogFragment newInstanceSlimming(Slimming slimming, int option) {
        SportDetailsDialogFragment fragment = new SportDetailsDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable(SLIMMING, slimming);
        args.putInt(OPTION, option);
        fragment.setArguments(args);
        return fragment;
    }

    public static SportDetailsDialogFragment newInstanceGainVolume(GainVolume gainVolume, int option) {
        SportDetailsDialogFragment fragment = new SportDetailsDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable(GAINVOLUME, gainVolume);
        args.putInt(OPTION, option);
        fragment.setArguments(args);
        return fragment;
    }

    public static SportDetailsDialogFragment newInstanceToning(Toning toning, int option) {
        SportDetailsDialogFragment fragment = new SportDetailsDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable(TONING, toning);
        args.putInt(OPTION, option);
        fragment.setArguments(args);
        return fragment;
    }

    //Nuestra variable communities coge el valor que se le está pasando
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        slimming = getArguments().getParcelable(SLIMMING);
        toning = getArguments().getParcelable(TONING);
        gainVolume = getArguments().getParcelable(GAINVOLUME);
        option = getArguments().getInt(OPTION);
    }

    private TextView textViewTitle;
    private Button buttonOpenYoutube;
    private TextView textViewDescription;
    private ImageView imageViewSport;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sport_dialog_fragment, container, false);
        view.setBackgroundResource(R.drawable.corner_dialog_fragment);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        findById(view);
        putData();
        onClickButtonOpenYoutube();

        return view;

    }

    public void findById(View v) {
        textViewTitle = v.findViewById(R.id.textTitle);
        buttonOpenYoutube = v.findViewById(R.id.open_youtube);
        textViewDescription = v.findViewById(R.id.textviewDescription);
        imageViewSport = v.findViewById(R.id.imageViewSport);
    }

    private void putData() {
        if (option == 0) {
            textViewTitle.setText(slimming.getName());
            textViewDescription.setText(slimming.getDescription());
            Glide.with(this).load(slimming.getPhoto()).into(imageViewSport);
        } else if (option == 1) {
            textViewTitle.setText(gainVolume.getName());
            textViewDescription.setText(gainVolume.getDescription());
            Glide.with(this).load(gainVolume.getPhoto()).into(imageViewSport);
        } else if (option == 2) {
            textViewTitle.setText(toning.getName());
            textViewDescription.setText(toning.getDescription());
            Glide.with(this).load(toning.getPhoto()).into(imageViewSport);
        }
    }

    private void onClickButtonOpenYoutube() {
        buttonOpenYoutube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), YoutubeActivity.class);
                if (option == 0) {
                    intent.putExtra("url", slimming.getVideo());
                } else if (option == 1) {
                    intent.putExtra("url", gainVolume.getVideo());
                } else if (option == 2) {
                    intent.putExtra("url", toning.getVideo());
                }
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
}