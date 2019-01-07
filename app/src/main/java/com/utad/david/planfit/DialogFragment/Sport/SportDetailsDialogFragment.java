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
import com.utad.david.planfit.Model.Sport.SportGainVolume;
import com.utad.david.planfit.Model.Sport.SportSlimming;

import com.utad.david.planfit.Model.Sport.SportToning;
import com.utad.david.planfit.R;

import java.util.ArrayList;
import java.util.List;


public class SportDetailsDialogFragment extends DialogFragment implements FirebaseAdmin.FirebaseAdminInsertFavoriteSportAndNutrition {

    public SportSlimming sportSlimming;
    public SportGainVolume sportGainVolume;
    public SportToning sportToning;
    public int option;
    private static String SLIMMING = "SLIMMING";
    private static String GAINVOLUME = "GAINVOLUME";
    private static String TONING = "TONING";
    private static String OPTION = "OPTION";


    public static SportDetailsDialogFragment newInstanceSlimming(SportSlimming sportSlimming, int option) {
        SportDetailsDialogFragment fragment = new SportDetailsDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable(SLIMMING, sportSlimming);
        args.putInt(OPTION, option);
        fragment.setArguments(args);
        SessionUser.getInstance().firebaseAdmin.setFirebaseAdminInsertFavoriteSportAndNutrition(fragment);
        SessionUser.getInstance().firebaseAdmin.downloadSlimmingSportFavorite();
        return fragment;
    }

    public static SportDetailsDialogFragment newInstanceGainVolume(SportGainVolume sportGainVolume, int option) {
        SportDetailsDialogFragment fragment = new SportDetailsDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable(GAINVOLUME, sportGainVolume);
        args.putInt(OPTION, option);
        fragment.setArguments(args);
        SessionUser.getInstance().firebaseAdmin.setFirebaseAdminInsertFavoriteSportAndNutrition(fragment);
        SessionUser.getInstance().firebaseAdmin.downloadGainVolumeSportFavorite();
        return fragment;
    }

    public static SportDetailsDialogFragment newInstanceToning(SportToning sportToning, int option) {
        SportDetailsDialogFragment fragment = new SportDetailsDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable(TONING, sportToning);
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
        sportSlimming = getArguments().getParcelable(SLIMMING);
        sportToning = getArguments().getParcelable(TONING);
        sportGainVolume = getArguments().getParcelable(GAINVOLUME);
        option = getArguments().getInt(OPTION);

    }

    private TextView textViewTitle;
    private Button buttonOpenYoutube;
    private TextView textViewDescription;
    private ImageView imageViewSport;
    private Button buttonInsert;
    private List<SportSlimming> sportSlimmingList;
    private List<SportToning> sportToningList;
    private List<SportGainVolume> sportGainVolumeList;


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
        textViewTitle = v.findViewById(R.id.textTitleSport);
        buttonOpenYoutube = v.findViewById(R.id.open_youtube_sport);
        textViewDescription = v.findViewById(R.id.textviewDescriptionSport);
        imageViewSport = v.findViewById(R.id.imageViewSport);
        buttonInsert = v.findViewById(R.id.insert_favorite_sport);
    }

    private void putData() {
        switch (option){
            case 0:
                textViewTitle.setText(sportSlimming.getName());
                textViewDescription.setText(sportSlimming.getDescription());
                Glide.with(this).load(sportSlimming.getPhoto()).into(imageViewSport);
                break;
            case 1:
                textViewTitle.setText(sportToning.getName());
                textViewDescription.setText(sportToning.getDescription());
                Glide.with(this).load(sportToning.getPhoto()).into(imageViewSport);
                break;
            case 2:
                textViewTitle.setText(sportGainVolume.getName());
                textViewDescription.setText(sportGainVolume.getDescription());
                Glide.with(this).load(sportGainVolume.getPhoto()).into(imageViewSport);
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
                        intent.putExtra("url", sportSlimming.getVideo());
                        break;
                    case 1:
                        intent.putExtra("url", sportToning.getVideo());
                        break;
                    case 2:
                        intent.putExtra("url", sportGainVolume.getVideo());
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
                            SessionUser.getInstance().firebaseAdmin.addFavoriteSportSlimmingCouldFirestore(sportSlimming);
                            break;
                        case 1:
                            SessionUser.getInstance().firebaseAdmin.addFavoriteSportToningCouldFirestore(sportToning);
                            break;
                        case 2:
                            SessionUser.getInstance().firebaseAdmin.addFavoriteSportGainVolumeCouldFirestore(sportGainVolume);
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
                    sportSlimmingList = new ArrayList<>();
                        sportSlimmingList = SessionUser.getInstance().firebaseAdmin.sportSlimmingListSportFavorite;
                        for(int i = 0; i< sportSlimmingList.size(); i++){
                            if(sportSlimmingList.get(i).getName().equals(sportSlimming.getName())){
                                buttonInsert.setEnabled(false);
                            }
                        }
                    break;
                case 1:
                    sportToningList = new ArrayList<>();
                        sportToningList = SessionUser.getInstance().firebaseAdmin.sportToningListSportFavorite;
                        for(int i = 0; i< sportToningList.size(); i++){
                            if(sportToningList.get(i).getName().equals(sportToning.getName())){
                                buttonInsert.setEnabled(false);
                            }
                        }
                    break;
                case 2:
                    sportGainVolumeList = new ArrayList<>();
                     sportGainVolumeList = SessionUser.getInstance().firebaseAdmin.sportGainVolumeListSportFavorite;
                        for(int i = 0; i< sportGainVolumeList.size(); i++){
                            if(sportGainVolumeList.get(i).getName().equals(sportGainVolume.getName())){
                                buttonInsert.setEnabled(false);
                            }
                        }
                    break;
            }

        }
    }
}