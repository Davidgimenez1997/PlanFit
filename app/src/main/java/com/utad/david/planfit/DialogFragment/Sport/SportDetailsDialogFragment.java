package com.utad.david.planfit.DialogFragment.Sport;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.utad.david.planfit.Activitys.YoutubeActivity;
import com.utad.david.planfit.Data.Firebase.FirebaseAdmin;
import com.utad.david.planfit.Data.SessionUser;
import com.utad.david.planfit.Model.Sport.SportGainVolume;
import com.utad.david.planfit.Model.Sport.SportSlimming;
import com.utad.david.planfit.Model.Sport.SportToning;
import com.utad.david.planfit.R;
import java.util.ArrayList;
import java.util.List;

public class SportDetailsDialogFragment extends DialogFragment implements FirebaseAdmin.FirebaseAdminFavoriteSportAndNutrition {

    public SportSlimming sportSlimming;
    public SportGainVolume sportGainVolume;
    public SportToning sportToning;
    public CallbackSport listener;
    public int option;
    private static String SLIMMING = "SLIMMING";
    private static String GAINVOLUME = "GAINVOLUME";
    private static String TONING = "TONING";
    private static String OPTION = "OPTION";
    private String URL = "URL";

    public interface CallbackSport{
        void onClickClose();
    }

    public static SportDetailsDialogFragment newInstanceSlimming(SportSlimming sportSlimming, int option) {
        SportDetailsDialogFragment fragment = new SportDetailsDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable(SLIMMING, sportSlimming);
        args.putInt(OPTION, option);
        fragment.setArguments(args);
        SessionUser.getInstance().firebaseAdmin.setFirebaseAdminFavoriteSportAndNutrition(fragment);
        SessionUser.getInstance().firebaseAdmin.downloadSlimmingSportFavorite();
        return fragment;
    }

    public static SportDetailsDialogFragment newInstanceGainVolume(SportGainVolume sportGainVolume, int option) {
        SportDetailsDialogFragment fragment = new SportDetailsDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable(GAINVOLUME, sportGainVolume);
        args.putInt(OPTION, option);
        fragment.setArguments(args);
        SessionUser.getInstance().firebaseAdmin.setFirebaseAdminFavoriteSportAndNutrition(fragment);
        SessionUser.getInstance().firebaseAdmin.downloadGainVolumeSportFavorite();
        return fragment;
    }

    public static SportDetailsDialogFragment newInstanceToning(SportToning sportToning, int option) {
        SportDetailsDialogFragment fragment = new SportDetailsDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable(TONING, sportToning);
        args.putInt(OPTION, option);
        fragment.setArguments(args);
        SessionUser.getInstance().firebaseAdmin.setFirebaseAdminFavoriteSportAndNutrition(fragment);
        SessionUser.getInstance().firebaseAdmin.downloadToningSportFavorite();
        return fragment;
    }

    public void setListener(SportDetailsDialogFragment.CallbackSport listener) {
        this.listener = listener;
    }

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
    private Button buttonDelete;
    private Button buttonClose;
    private List<SportSlimming> sportSlimmingList;
    private List<SportToning> sportToningList;
    private List<SportGainVolume> sportGainVolumeList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sport_dialog_fragment, container, false);
        view.setBackgroundResource(R.drawable.corner_dialog_fragment);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        showLoading();
        findById(view);
        putData();
        onClickButtonOpenYoutube();
        onClickButtonOpenInsertFavorite();
        onClickButtonOpenDeleteFavorite();
        onClickCloseButton();
        return view;
    }

    public void findById(View v) {
        textViewTitle = v.findViewById(R.id.textTitleSport);
        buttonOpenYoutube = v.findViewById(R.id.open_youtube_sport);
        textViewDescription = v.findViewById(R.id.textviewDescriptionSport);
        imageViewSport = v.findViewById(R.id.imageViewSport);
        buttonInsert = v.findViewById(R.id.insert_favorite_sport);
        buttonDelete = v.findViewById(R.id.delete_favorite_sport);
        buttonClose = v.findViewById(R.id.close_info_sport);
    }

    private void onClickCloseButton(){
        buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null){
                    listener.onClickClose();
                }
            }
        });
    }

    private void putData() {
        RequestOptions requestOptions = new RequestOptions();
        switch (option){
            case 0:
                textViewTitle.setText(sportSlimming.getName());
                textViewDescription.setText(sportSlimming.getDescription());
                requestOptions.placeholder(R.drawable.icon_gallery);
                Glide.with(this).load(sportSlimming.getPhoto()).into(imageViewSport);
                break;
            case 1:
                textViewTitle.setText(sportToning.getName());
                textViewDescription.setText(sportToning.getDescription());
                requestOptions.placeholder(R.drawable.icon_gallery);
                Glide.with(this).load(sportToning.getPhoto()).into(imageViewSport);
                break;
            case 2:
                textViewTitle.setText(sportGainVolume.getName());
                textViewDescription.setText(sportGainVolume.getDescription());
                requestOptions.placeholder(R.drawable.icon_gallery);
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
                        intent.putExtra(URL, sportSlimming.getVideo());
                        break;
                    case 1:
                        intent.putExtra(URL, sportToning.getVideo());
                        break;
                    case 2:
                        intent.putExtra(URL, sportGainVolume.getVideo());
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
                    showLoading();
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

    private void onClickButtonOpenDeleteFavorite(){
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoading();
                switch (option){
                    case 0:
                        SessionUser.getInstance().firebaseAdmin.deleteFavoriteSportSlimming(sportSlimming);
                        break;
                    case 1:
                        SessionUser.getInstance().firebaseAdmin.deleteFavoriteSportToning(sportToning);
                        break;
                    case 2:
                        SessionUser.getInstance().firebaseAdmin.deleteFavoriteSportGainVolume(sportGainVolume);
                        break;
                }
            }
        });
    }

    @Override
    public void inserSportFavoriteFirebase(boolean end) {
        if(end){
            buttonInsert.setEnabled(false);
            buttonDelete.setEnabled(true);
            hideLoading();
            switch (option){
                case 0:
                    Toast.makeText(getContext(),sportSlimming.getName()+" "+getString(R.string.agregarafavoritos),Toast.LENGTH_LONG).show();
                    break;
                case 1:
                    Toast.makeText(getContext(),sportToning.getName()+" "+getString(R.string.agregarafavoritos),Toast.LENGTH_LONG).show();
                    break;
                case 2:
                    Toast.makeText(getContext(),sportGainVolume.getName()+" "+getString(R.string.agregarafavoritos),Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }

    @Override
    public void deleteFavoriteSport(boolean end) {
        if(end==true){
            buttonInsert.setEnabled(true);
            buttonDelete.setEnabled(false);
            hideLoading();
            switch (option){
                case 0:
                    Toast.makeText(getContext(),sportSlimming.getName()+" "+getString(R.string.eliminarafavoritos),Toast.LENGTH_LONG).show();
                    break;
                case 1:
                    Toast.makeText(getContext(),sportToning.getName()+" "+getString(R.string.eliminarafavoritos),Toast.LENGTH_LONG).show();
                    break;
                case 2:
                    Toast.makeText(getContext(),sportGainVolume.getName()+" "+getString(R.string.eliminarafavoritos),Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }

    @Override
    public void downloandCollectionSportFavorite(boolean end) {
        if(end){
            hideLoading();
            switch (option){
                case 0:
                    sportSlimmingList = new ArrayList<>();
                        sportSlimmingList = SessionUser.getInstance().firebaseAdmin.sportSlimmingListSportFavorite;
                        for(int i = 0; i< sportSlimmingList.size(); i++){
                            if(sportSlimmingList.get(i).getName().equals(sportSlimming.getName())){
                                buttonInsert.setEnabled(false);
                                buttonDelete.setEnabled(true);
                            }
                        }
                    break;
                case 1:
                    sportToningList = new ArrayList<>();
                        sportToningList = SessionUser.getInstance().firebaseAdmin.sportToningListSportFavorite;
                        for(int i = 0; i< sportToningList.size(); i++){
                            if(sportToningList.get(i).getName().equals(sportToning.getName())){
                                buttonInsert.setEnabled(false);
                                buttonDelete.setEnabled(true);
                            }
                        }
                    break;
                case 2:
                    sportGainVolumeList = new ArrayList<>();
                     sportGainVolumeList = SessionUser.getInstance().firebaseAdmin.sportGainVolumeListSportFavorite;
                        for(int i = 0; i< sportGainVolumeList.size(); i++){
                            if(sportGainVolumeList.get(i).getName().equals(sportGainVolume.getName())){
                                buttonInsert.setEnabled(false);
                                buttonDelete.setEnabled(true);
                            }
                        }
                    break;
            }

        }
    }

    private ProgressDialog progressDialog;

    public void showLoading() {
        if (progressDialog != null && progressDialog.isShowing()) {
            return;
        }
        progressDialog = new ProgressDialog(getContext(), R.style.TransparentProgressDialog);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.setCancelable(false);
        RotateAnimation rotate = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setInterpolator(new LinearInterpolator());
        rotate.setDuration(1000);
        rotate.setRepeatCount(Animation.INFINITE);
        ImageView ivLoading = ButterKnife.findById(progressDialog, R.id.image_cards_animation);
        ivLoading.startAnimation(rotate);
        progressDialog.show();
    }

    public void hideLoading() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        hideLoading();
    }

    @Override
    public void onPause() {
        super.onPause();
        hideLoading();
    }

    @Override
    public void emptyCollectionSportFavorite(boolean end) {

    }

    @Override
    public void inserNutritionFavoriteFirebase(boolean end) {

    }

    @Override
    public void deleteFavoriteNutrition(boolean end) {

    }

    @Override
    public void downloandCollectionNutritionFavorite(boolean end) {

    }

    @Override
    public void emptyCollectionNutritionFavorite(boolean end) {

    }
}