package com.utad.david.planfit.DialogFragment.Nutrition;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.crashlytics.android.Crashlytics;
import com.utad.david.planfit.Activitys.WebViewActivity;
import com.utad.david.planfit.Base.BaseDialogFragment;
import com.utad.david.planfit.Data.Favorite.Nutrition.GetNutritionFavorite;
import com.utad.david.planfit.Data.Favorite.Nutrition.NutritionFavoriteRepository;
import com.utad.david.planfit.Data.Firebase.FirebaseAdmin;
import com.utad.david.planfit.Data.SessionUser;
import com.utad.david.planfit.Model.Nutrition.DefaultNutrition;
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

public class NutritionDetailsDialogFragment
        extends BaseDialogFragment
        implements GetNutritionFavorite {

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
            NutritionFavoriteRepository.getInstance().setGetNutritionFavorite(fragment);
            NutritionFavoriteRepository.getInstance().getSlimmingNutritionFavorite();
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
            NutritionFavoriteRepository.getInstance().setGetNutritionFavorite(fragment);
            NutritionFavoriteRepository.getInstance().getGainVolumeNutritionFavorite();
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
            NutritionFavoriteRepository.getInstance().setGetNutritionFavorite(fragment);
            NutritionFavoriteRepository.getInstance().getToningNutritionFavorite();
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
                        NutritionFavoriteRepository.getInstance().addFavoriteNutritionnSlimming(nutritionSlimming);
                        break;
                    case 1:
                        NutritionFavoriteRepository.getInstance().addFavoriteNutritionToning(nutritionToning);
                        break;
                    case 2:
                        NutritionFavoriteRepository.getInstance().addFavoriteNutritionGainVolume(nutritionGainVolume);
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
                        NutritionFavoriteRepository.getInstance().deleteFavoriteNutritionSlimming(nutritionSlimming);
                        break;
                    case 1:
                        NutritionFavoriteRepository.getInstance().deleteFavoriteNutritionToning(nutritionToning);
                        break;
                    case 2:
                        NutritionFavoriteRepository.getInstance().deleteFavoriteNutritionGainVolume(nutritionGainVolume);
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
    public void addNutritionFavorite(boolean status) {
        if(status){
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
    public void deleteNutritionFavorite(boolean status) {
        if(status){
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
    public void getNutritionSlimmingFavorite(boolean status, List<NutritionSlimming> nutritionSlimmings) {
        if (status) {
            hideLoading();
            for(int i = 0; i< nutritionSlimmings.size(); i++){
                if(nutritionSlimmings.get(i).getName().equals(nutritionSlimming.getName())){
                    buttonInsert.setEnabled(false);
                    buttonDelete.setEnabled(true);
                    return;
                }
            }
        }
    }

    @Override
    public void getNutritionToningFavorite(boolean status, List<NutritionToning> nutritionTonings) {
        if (status) {
            hideLoading();
            for(int i = 0; i< nutritionTonings.size(); i++){
                if(nutritionTonings.get(i).getName().equals(nutritionToning.getName())){
                    buttonInsert.setEnabled(false);
                    buttonDelete.setEnabled(true);
                    return;
                }
            }
        }
    }

    @Override
    public void getNutritionGainVolumeFavorite(boolean status, List<NutritionGainVolume> nutritionGainVolumes) {
        if (status) {
            hideLoading();
            for(int i = 0; i< nutritionGainVolumes.size(); i++){
                if(nutritionGainVolumes.get(i).getName().equals(nutritionGainVolume.getName())){
                    buttonInsert.setEnabled(false);
                    buttonDelete.setEnabled(true);
                    return;
                }
            }
        }
    }

    @Override
    public void emptyNutritionFavorite(boolean status) {}
    @Override
    public void getNutritionAllFavorite(boolean status, List<DefaultNutrition> defaultNutritions) {}
}