package com.utad.david.planfit.DialogFragment.Nutrition;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
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
import com.crashlytics.android.Crashlytics;
import com.utad.david.planfit.Data.Firebase.FirebaseAdmin;
import com.utad.david.planfit.Data.SessionUser;
import com.utad.david.planfit.Model.Nutrition.NutritionGainVolume;
import com.utad.david.planfit.Model.Nutrition.NutritionSlimming;
import com.utad.david.planfit.Model.Nutrition.NutritionToning;
import com.utad.david.planfit.R;
import io.fabric.sdk.android.Fabric;

import java.util.ArrayList;
import java.util.List;

public class NutritionDetailsDialogFragment extends DialogFragment implements FirebaseAdmin.FirebaseAdminFavoriteNutrition {

    public NutritionSlimming nutritionSlimming;
    public NutritionGainVolume nutritionGainVolume;
    public NutritionToning nutritionToning;
    public int option;
    private CallbackNutrition listener;
    private static String SLIMMING = "SLIMMING";
    private static String GAINVOLUME = "GAINVOLUME";
    private static String TONING = "TONING";
    private static String OPTION = "OPTION";

    public interface CallbackNutrition{
        void onClickClose();
    }

    public static NutritionDetailsDialogFragment newInstanceSlimming(NutritionSlimming nutritionSlimming, int option) {
        NutritionDetailsDialogFragment fragment = new NutritionDetailsDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable(SLIMMING, nutritionSlimming);
        args.putInt(OPTION, option);
        fragment.setArguments(args);
        SessionUser.getInstance().firebaseAdmin.setFirebaseAdminFavoriteNutrition(fragment);
        SessionUser.getInstance().firebaseAdmin.downloadSlimmingNutritionFavorite();
        return fragment;
    }

    public static NutritionDetailsDialogFragment newInstanceGainVolume(NutritionGainVolume nutritionGainVolume, int option) {
        NutritionDetailsDialogFragment fragment = new NutritionDetailsDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable(GAINVOLUME, nutritionGainVolume);
        args.putInt(OPTION, option);
        fragment.setArguments(args);
        SessionUser.getInstance().firebaseAdmin.setFirebaseAdminFavoriteNutrition(fragment);
        SessionUser.getInstance().firebaseAdmin.downloadGainVolumeNutritionFavorite();
        return fragment;
    }

    public static NutritionDetailsDialogFragment newInstanceToning(NutritionToning nutritionToning, int option) {
        NutritionDetailsDialogFragment fragment = new NutritionDetailsDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable(TONING, nutritionToning);
        args.putInt(OPTION, option);
        fragment.setArguments(args);
        SessionUser.getInstance().firebaseAdmin.setFirebaseAdminFavoriteNutrition(fragment);
        SessionUser.getInstance().firebaseAdmin.downloadToningNutritionFavorite();
        return fragment;
    }

    public void setCallbackNutrition(CallbackNutrition listener) {
        this.listener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Fabric.with(getContext(),new Crashlytics());

        nutritionSlimming = getArguments().getParcelable(SLIMMING);
        nutritionToning = getArguments().getParcelable(TONING);
        nutritionGainVolume = getArguments().getParcelable(GAINVOLUME);
        option = getArguments().getInt(OPTION);
    }

    private TextView textViewTitle;
    private Button buttonOpenRecipe;
    private TextView textViewDescription;
    private ImageView imageViewSport;
    private Button buttonInsert;
    private Button buttonDelete;
    private Button buttonClose;
    private List<NutritionSlimming> nutritionSlimmingList;
    private List<NutritionToning> nutritionToningList;
    private List<NutritionGainVolume> nutritionGainVolumeList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.nutrtion_dialog_fragment, container, false);
        view.setBackgroundResource(R.drawable.corner_dialog_fragment);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        showLoading();
        findById(view);
        putData();
        onClickButtonOpenRecipe();
        onClickButtonOpenInsertFavorite();
        onClickButtonOpenDeleteFavorite();
        onClickCloseButton();
        return view;
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

    public void findById(View v) {
        textViewTitle = v.findViewById(R.id.textTitleNutrition);
        buttonOpenRecipe = v.findViewById(R.id.open_recipe_nutrition);
        textViewDescription = v.findViewById(R.id.textviewDescriptionNutrition);
        imageViewSport = v.findViewById(R.id.imageViewNutrition);
        buttonInsert = v.findViewById(R.id.insert_favoriteNutrition);
        buttonDelete = v.findViewById(R.id.delete_favorite_nutrition);
        buttonClose = v.findViewById(R.id.close_nutrition);
    }

    private void putData() {
        RequestOptions requestOptions = new RequestOptions();
        switch (option){
            case 0:
                textViewTitle.setText(nutritionSlimming.getName());
                textViewDescription.setText(nutritionSlimming.getDescription());
                requestOptions.placeholder(R.drawable.icon_gallery);
                Glide.with(this).load(nutritionSlimming.getPhoto()).into(imageViewSport);
                break;
            case 1:
                textViewTitle.setText(nutritionToning.getName());
                textViewDescription.setText(nutritionToning.getDescription());
                requestOptions.placeholder(R.drawable.icon_gallery);
                Glide.with(this).load(nutritionToning.getPhoto()).into(imageViewSport);
                break;
            case 2:
                textViewTitle.setText(nutritionGainVolume.getName());
                textViewDescription.setText(nutritionGainVolume.getDescription());
                requestOptions.placeholder(R.drawable.icon_gallery);
                Glide.with(this).load(nutritionGainVolume.getPhoto()).into(imageViewSport);
                break;
        }
    }

    private void onClickButtonOpenRecipe() {
        buttonOpenRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                switch (option){
                    case 0:
                        i.setData(Uri.parse(nutritionSlimming.getUrl()));
                        break;
                    case 1:
                        i.setData(Uri.parse(nutritionToning.getUrl()));
                        break;
                    case 2:
                        i.setData(Uri.parse(nutritionGainVolume.getUrl()));
                        break;
                }
                startActivity(i);
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
                        SessionUser.getInstance().firebaseAdmin.addFavoriteNutritionCouldFirestore(nutritionSlimming);
                        break;
                    case 1:
                        SessionUser.getInstance().firebaseAdmin.addFavoriteNutritionToningCouldFirestore(nutritionToning);
                        break;
                    case 2:
                        SessionUser.getInstance().firebaseAdmin.addFavoriteNutritionGainVolumeCouldFirestore(nutritionGainVolume);
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
                        SessionUser.getInstance().firebaseAdmin.deleteFavoriteNutritionSlimming(nutritionSlimming);
                        break;
                    case 1:
                        SessionUser.getInstance().firebaseAdmin.deleteFavoriteNutritionToning(nutritionToning);
                        break;
                    case 2:
                        SessionUser.getInstance().firebaseAdmin.deleteFavoriteNutritionGainVolume(nutritionGainVolume);
                        break;
                }
            }
        });
    }

    @Override
    public void inserNutritionFavoriteFirebase(boolean end) {
        if(end){
            buttonInsert.setEnabled(false);
            buttonDelete.setEnabled(true);
            hideLoading();
            switch (option){
                case 0:
                    Toast.makeText(getContext(),nutritionSlimming.getName()+" "+getString(R.string.agregarafavoritos),Toast.LENGTH_LONG).show();
                    break;
                case 1:
                    Toast.makeText(getContext(),nutritionToning.getName()+" "+getString(R.string.agregarafavoritos),Toast.LENGTH_LONG).show();
                    break;
                case 2:
                    Toast.makeText(getContext(),nutritionGainVolume.getName()+" "+getString(R.string.agregarafavoritos),Toast.LENGTH_LONG).show();
                    break;
            }

        }
    }

    @Override
    public void deleteFavoriteNutrition(boolean end) {
        if(end==true){
            buttonInsert.setEnabled(true);
            buttonDelete.setEnabled(false);
            hideLoading();
            switch (option){
                case 0:
                    Toast.makeText(getContext(),nutritionSlimming.getName()+" "+getString(R.string.eliminarafavoritos),Toast.LENGTH_LONG).show();
                    break;
                case 1:
                    Toast.makeText(getContext(),nutritionToning.getName()+" "+getString(R.string.eliminarafavoritos),Toast.LENGTH_LONG).show();
                    break;
                case 2:
                    Toast.makeText(getContext(),nutritionGainVolume.getName()+" "+getString(R.string.eliminarafavoritos),Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }

    @Override
    public void downloandCollectionNutritionFavorite(boolean end) {
        if(end){
            hideLoading();
            switch (option){
                case 0:
                    nutritionSlimmingList = new ArrayList<>();
                    nutritionSlimmingList = SessionUser.getInstance().firebaseAdmin.nutritionSlimmingListNutritionFavorite;
                    for(int i = 0; i< nutritionSlimmingList.size(); i++){
                        if(nutritionSlimmingList.get(i).getName().equals(nutritionSlimming.getName())){
                            buttonInsert.setEnabled(false);
                            buttonDelete.setEnabled(true);
                        }
                    }
                    break;
                case 1:
                    nutritionToningList = new ArrayList<>();
                    nutritionToningList = SessionUser.getInstance().firebaseAdmin.nutritionToningListNutritionFavorite;
                    for(int i = 0; i< nutritionToningList.size(); i++){
                        if(nutritionToningList.get(i).getName().equals(nutritionToning.getName())){
                            buttonInsert.setEnabled(false);
                            buttonDelete.setEnabled(true);
                        }
                    }
                    break;
                case 2:
                    nutritionGainVolumeList = new ArrayList<>();
                    nutritionGainVolumeList = SessionUser.getInstance().firebaseAdmin.nutritionGainVolumeListNutritionFavorite;
                    for(int i = 0; i< nutritionGainVolumeList.size(); i++){
                        if(nutritionGainVolumeList.get(i).getName().equals(nutritionGainVolume.getName())){
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
    public void emptyCollectionNutritionFavorite(boolean end) {}
}