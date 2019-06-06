package com.utad.david.planfit.DialogFragment.Nutrition;

import android.app.ProgressDialog;
import android.content.Context;
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
import com.crashlytics.android.Crashlytics;
import com.utad.david.planfit.Activitys.WebViewActivity;
import com.utad.david.planfit.Data.Firebase.FirebaseAdmin;
import com.utad.david.planfit.Data.SessionUser;
import com.utad.david.planfit.Model.Nutrition.NutritionGainVolume;
import com.utad.david.planfit.Model.Nutrition.NutritionSlimming;
import com.utad.david.planfit.Model.Nutrition.NutritionToning;
import com.utad.david.planfit.R;
import com.utad.david.planfit.Utils.Constants;
import com.utad.david.planfit.Utils.Utils;
import com.utad.david.planfit.Utils.UtilsNetwork;
import io.fabric.sdk.android.Fabric;

import java.util.ArrayList;
import java.util.List;

public class NutritionDetailsDialogFragment extends DialogFragment
        implements FirebaseAdmin.FirebaseAdminFavoriteNutrition {

    /******************************** VARIABLES *************************************+/
     *
     */

    public NutritionSlimming nutritionSlimming;
    public NutritionGainVolume nutritionGainVolume;
    public NutritionToning nutritionToning;
    public int option;
    private Callback listener;
    private static String SLIMMING = Constants.NutritionDetails.EXTRA_SLIMMING;
    private static String GAINVOLUME = Constants.NutritionDetails.EXTRA_GAINVOLUME;
    private static String TONING = Constants.NutritionDetails.EXTRA_TONING;
    private static String OPTION = Constants.NutritionDetails.EXTRA_OPTION;

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


    /******************************** PROGRESS DIALOG Y METODOS *************************************+/
     *
     */

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

    /******************************** INTERFAZ *************************************+/
     *
     */

    public interface Callback {
        void onClickClose();
    }

    public void setCallbackNutrition(Callback listener) {
        this.listener = listener;
    }

    /******************************** NEW INSTANCE ADELGAZAR *************************************+/
     *
     */

    public static NutritionDetailsDialogFragment newInstanceSlimming(NutritionSlimming nutritionSlimming, int option, Context context) {
        NutritionDetailsDialogFragment fragment = new NutritionDetailsDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable(SLIMMING, nutritionSlimming);
        args.putInt(OPTION, option);
        fragment.setArguments(args);

        if(UtilsNetwork.checkConnectionInternetDevice(context)){
            SessionUser.getInstance().firebaseAdmin.setFirebaseAdminFavoriteNutrition(fragment);
            SessionUser.getInstance().firebaseAdmin.downloadSlimmingNutritionFavorite();
        }else{
            Toast.makeText(context,"Comprueba su conexion de internet y reinice la aplicación",Toast.LENGTH_LONG).show();
        }

        return fragment;
    }

    /******************************** NEW INSTANCE GANAR VOLUMEN *************************************+/
     *
     */

    public static NutritionDetailsDialogFragment newInstanceGainVolume(NutritionGainVolume nutritionGainVolume, int option, Context context) {
        NutritionDetailsDialogFragment fragment = new NutritionDetailsDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable(GAINVOLUME, nutritionGainVolume);
        args.putInt(OPTION, option);
        fragment.setArguments(args);

        if(UtilsNetwork.checkConnectionInternetDevice(context)){
            SessionUser.getInstance().firebaseAdmin.setFirebaseAdminFavoriteNutrition(fragment);
            SessionUser.getInstance().firebaseAdmin.downloadGainVolumeNutritionFavorite();
        }else{
            Toast.makeText(context,"Comprueba su conexion de internet y reinice la aplicación",Toast.LENGTH_LONG).show();
        }

        return fragment;
    }

    /******************************** NEW INSTANCE TONIFICAR *************************************+/
     *
     */

    public static NutritionDetailsDialogFragment newInstanceToning(NutritionToning nutritionToning, int option, Context context) {
        NutritionDetailsDialogFragment fragment = new NutritionDetailsDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable(TONING, nutritionToning);
        args.putInt(OPTION, option);
        fragment.setArguments(args);

        if(UtilsNetwork.checkConnectionInternetDevice(context)){
            SessionUser.getInstance().firebaseAdmin.setFirebaseAdminFavoriteNutrition(fragment);
            SessionUser.getInstance().firebaseAdmin.downloadToningNutritionFavorite();
        }else{
            Toast.makeText(context,"Comprueba su conexion de internet y reinice la aplicación",Toast.LENGTH_LONG).show();
        }

        return fragment;
    }


    /******************************** GET ARGUMENTS *************************************+/
     *
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(UtilsNetwork.checkConnectionInternetDevice(getContext())){
            Fabric.with(getContext(),new Crashlytics());
        }

        nutritionSlimming = getArguments().getParcelable(SLIMMING);
        nutritionToning = getArguments().getParcelable(TONING);
        nutritionGainVolume = getArguments().getParcelable(GAINVOLUME);
        option = getArguments().getInt(OPTION);
    }

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

    /******************************** CONFIGURA VISTA *************************************+/
     *
     */

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
        switch (option){
            case 0:
                textViewTitle.setText(nutritionSlimming.getName());
                textViewDescription.setText(nutritionSlimming.getDescription());
                Utils.loadImage(nutritionSlimming.getPhoto(),imageViewSport,Utils.PLACEHOLDER_GALLERY);
                break;
            case 1:
                textViewTitle.setText(nutritionToning.getName());
                textViewDescription.setText(nutritionToning.getDescription());
                Utils.loadImage(nutritionToning.getPhoto(),imageViewSport,Utils.PLACEHOLDER_GALLERY);
                break;
            case 2:
                textViewTitle.setText(nutritionGainVolume.getName());
                textViewDescription.setText(nutritionGainVolume.getDescription());
                Utils.loadImage(nutritionGainVolume.getPhoto(),imageViewSport,Utils.PLACEHOLDER_GALLERY);
                break;
        }
    }

    /******************************** CIERRA LA PANTALLA *************************************+/
     *
     */

    private void onClickCloseButton(){
        buttonClose.setOnClickListener(v -> {
            if(listener!=null){
                listener.onClickClose();
            }
        });
    }

    /******************************** ABRE LA RECETA EN UN WEBVIEW *************************************+/
     *
     */

    private void onClickButtonOpenRecipe() {
        if(UtilsNetwork.checkConnectionInternetDevice(getContext())){
            buttonOpenRecipe.setOnClickListener(v -> {

                String title = null,url = null;

                switch (option){
                    case 0:
                        title = nutritionSlimming.getName();
                        url = nutritionSlimming.getUrl();
                        break;
                    case 1:
                        title = nutritionToning.getName();
                        url = nutritionToning.getUrl();
                        break;
                    case 2:
                        title = nutritionGainVolume.getName();
                        url = nutritionGainVolume.getUrl();
                        break;
                }

                Intent intent = new Intent(getContext(), WebViewActivity.class);
                intent.putExtra(WebViewActivity.EXTRA_TITLE, title);
                intent.putExtra(WebViewActivity.EXTRA_URL, url);
                intent.putExtra(WebViewActivity.EXTRA_MODE, Constants.ModeWebView.MODE_RECIPE);
                getActivity().overridePendingTransition(R.anim.slide_in_bottom, R.anim.stay);
                startActivity(intent);
            });
        }else{
            buttonOpenRecipe.setEnabled(false);
        }
    }

    /******************************** AGREGAR A FAVORITOS *************************************+/
     *
     */

    private void onClickButtonOpenInsertFavorite() {
        if(UtilsNetwork.checkConnectionInternetDevice(getContext())){
            buttonInsert.setOnClickListener(v -> {
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
            });
        }else{
            buttonInsert.setEnabled(false);
        }
    }

    /******************************** BORRAR DE FAVORITOS *************************************+/
     *
     */

    private void onClickButtonOpenDeleteFavorite(){
        if(UtilsNetwork.checkConnectionInternetDevice(getContext())){
            buttonDelete.setOnClickListener(v -> {
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
            });
        }else{
            buttonDelete.setEnabled(false);
        }
    }

    /******************************** CALLBACK DE FIREBASE *************************************+/
     *
     */

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

    @Override
    public void emptyCollectionNutritionFavorite(boolean end) {}
}