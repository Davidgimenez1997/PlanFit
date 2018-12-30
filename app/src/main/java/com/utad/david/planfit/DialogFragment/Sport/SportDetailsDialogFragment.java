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
import com.utad.david.planfit.Data.Firebase.FirebaseAdmin;
import com.utad.david.planfit.Data.SessionUser;
import com.utad.david.planfit.Model.Sport.GainVolume;
import com.utad.david.planfit.Model.Sport.Slimming;

import com.utad.david.planfit.Model.Sport.Toning;
import com.utad.david.planfit.R;

import java.util.ArrayList;
import java.util.List;


public class SportDetailsDialogFragment extends DialogFragment implements FirebaseAdmin.FirebaseAdminInsertFavoriteSportAndNutrition {

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
        SessionUser.getInstance().firebaseAdmin.setFirebaseAdminInsertFavoriteSportAndNutrition(fragment);
        SessionUser.getInstance().firebaseAdmin.downloadSlimmingSportFavorite();
        return fragment;
    }

    public static SportDetailsDialogFragment newInstanceGainVolume(GainVolume gainVolume, int option) {
        SportDetailsDialogFragment fragment = new SportDetailsDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable(GAINVOLUME, gainVolume);
        args.putInt(OPTION, option);
        fragment.setArguments(args);
        SessionUser.getInstance().firebaseAdmin.setFirebaseAdminInsertFavoriteSportAndNutrition(fragment);
        SessionUser.getInstance().firebaseAdmin.downloadGainVolumeSportFavorite();
        return fragment;
    }

    public static SportDetailsDialogFragment newInstanceToning(Toning toning, int option) {
        SportDetailsDialogFragment fragment = new SportDetailsDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable(TONING, toning);
        args.putInt(OPTION, option);
        fragment.setArguments(args);
        SessionUser.getInstance().firebaseAdmin.setFirebaseAdminInsertFavoriteSportAndNutrition(fragment);
        SessionUser.getInstance().firebaseAdmin.downloadToningSportFavorite();
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
    private Button buttonInsert;
    private List<Slimming> slimmingList;
    private List<Toning> toningList;
    private List<GainVolume> gainVolumeList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sport_dialog_fragment, container, false);
        view.setBackgroundResource(R.drawable.corner_dialog_fragment);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        findById(view);
        putData();
        onClickButtonOpenYoutube();
        onClickButtonOpenInsertFavorite();

        return view;

    }

    public void findById(View v) {
        textViewTitle = v.findViewById(R.id.textTitle);
        buttonOpenYoutube = v.findViewById(R.id.open_youtube);
        textViewDescription = v.findViewById(R.id.textviewDescription);
        imageViewSport = v.findViewById(R.id.imageViewSport);
        buttonInsert = v.findViewById(R.id.insert_favorite);
    }

    private void putData() {
        switch (option){
            case 0:
                textViewTitle.setText(slimming.getName());
                textViewDescription.setText(slimming.getDescription());
                Glide.with(this).load(slimming.getPhoto()).into(imageViewSport);
                break;
            case 1:
                textViewTitle.setText(toning.getName());
                textViewDescription.setText(toning.getDescription());
                Glide.with(this).load(toning.getPhoto()).into(imageViewSport);
                break;
            case 2:
                textViewTitle.setText(gainVolume.getName());
                textViewDescription.setText(gainVolume.getDescription());
                Glide.with(this).load(gainVolume.getPhoto()).into(imageViewSport);
            break;
        }
    }

    private void onClickButtonOpenYoutube() {
        buttonOpenYoutube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), YoutubeActivity.class);
                switch (option){
                    case 0:
                        intent.putExtra("url", slimming.getVideo());
                        break;
                    case 1:
                        intent.putExtra("url", toning.getVideo());
                        break;
                    case 2:
                        intent.putExtra("url", gainVolume.getVideo());
                        break;
                }
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    private void onClickButtonOpenInsertFavorite() {
            buttonInsert.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (option){
                        case 0:
                            SessionUser.getInstance().firebaseAdmin.addFavoriteSportSlimmingCouldFirestore(slimming);
                            break;
                        case 1:
                            SessionUser.getInstance().firebaseAdmin.addFavoriteSportToningCouldFirestore(toning);
                            break;
                        case 2:
                            SessionUser.getInstance().firebaseAdmin.addFavoriteSportGainVolumeCouldFirestore(gainVolume);
                            break;
                    }
                }
            });
    }

    @Override
    public void inserSportFavoriteFirebase(boolean end) {
        if(end){
            buttonInsert.setEnabled(false);
        }
    }

    @Override
    public void downloandCollectionSportFavorite(boolean end) {
        if(end){
            switch (option){
                case 0:
                    slimmingList = new ArrayList<>();
                        slimmingList = SessionUser.getInstance().firebaseAdmin.slimmingListSportFavorite;
                        Slimming current;
                        for(int i=0;i<slimmingList.size();i++){
                            current = slimmingList.get(i);
                            if(current.getName().equals(slimming.getName())){
                                buttonInsert.setEnabled(false);
                            }
                        }
                    break;
                case 1:
                    toningList = new ArrayList<>();
                        toningList = SessionUser.getInstance().firebaseAdmin.toningListSportFavorite;
                        for(int i=0;i<toningList.size();i++){
                            if(toningList.get(i).getName().equals(toning.getName())){
                                buttonInsert.setEnabled(false);
                            }
                        }
                    break;
                case 2:
                    gainVolumeList = new ArrayList<>();
                     gainVolumeList = SessionUser.getInstance().firebaseAdmin.gainVolumeListSportFavorite;
                        for(int i=0;i<gainVolumeList.size();i++){
                            if(gainVolumeList.get(i).getName().equals(gainVolume.getName())){
                                buttonInsert.setEnabled(false);
                            }
                        }
                    break;
            }

        }
    }
}