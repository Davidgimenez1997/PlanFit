package com.utad.david.planfit.DialogFragment.Sport;

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
import com.utad.david.planfit.Activitys.YoutubeActivity;
import com.utad.david.planfit.Base.BaseDialogFragment;
import com.utad.david.planfit.Data.Favorite.Sport.GetSportFavorite;
import com.utad.david.planfit.Data.Favorite.Sport.SportFavoriteRepository;
import com.utad.david.planfit.Model.Sport.DefaultSport;
import com.utad.david.planfit.Model.Sport.SportGainVolume;
import com.utad.david.planfit.Model.Sport.SportSlimming;
import com.utad.david.planfit.Model.Sport.SportToning;
import com.utad.david.planfit.R;
import com.utad.david.planfit.Utils.Constants;
import com.utad.david.planfit.Utils.Utils;
import com.utad.david.planfit.Utils.UtilsNetwork;
import io.fabric.sdk.android.Fabric;
import java.util.List;
import java.util.stream.Collectors;

public class SportDetailsDialogFragment extends BaseDialogFragment
        implements GetSportFavorite {

    /******************************** VARIABLES *************************************+/
     *
     */

    private static String SLIMMING = Constants.SportDetails.EXTRA_SLIMMING;
    private static String GAINVOLUME = Constants.SportDetails.EXTRA_GAINVOLUME;
    private static String TONING = Constants.SportDetails.EXTRA_TONING;
    private static String OPTION = Constants.SportDetails.EXTRA_OPTION;
    private String URL = Constants.SportDetails.EXTRA_URL;

    public SportSlimming sportSlimming;
    public SportGainVolume sportGainVolume;
    public SportToning sportToning;
    public int option;

    private TextView textViewTitle;
    private Button buttonOpenYoutube;
    private TextView textViewDescription;
    private ImageView imageViewSport;
    private Button buttonInsert;
    private Button buttonDelete;
    private Button buttonClose;
    public Callback listener;

    private List<SportSlimming> sportSlimmings;
    private List<SportToning> sportTonings;
    private List<SportGainVolume> sportGainVolumes;

    /******************************** INTERFAZ *************************************+/
     *
     */

    public interface Callback {
        void onClickClose();
    }

    public void setListener(Callback listener) {
        this.listener = listener;
    }

    /******************************** NEW INSTANCE ADELGAZAR *************************************+/
     *
     */

    public static SportDetailsDialogFragment newInstanceSlimming(SportSlimming sportSlimming, int option, Context context) {
        SportDetailsDialogFragment fragment = new SportDetailsDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable(SLIMMING, sportSlimming);
        args.putInt(OPTION, option);
        fragment.setArguments(args);
        return fragment;
    }

    /******************************** NEW INSTANCE GANAR VOLUMEN *************************************+/
     *
     */

    public static SportDetailsDialogFragment newInstanceGainVolume(SportGainVolume sportGainVolume, int option,Context context) {
        SportDetailsDialogFragment fragment = new SportDetailsDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable(GAINVOLUME, sportGainVolume);
        args.putInt(OPTION, option);
        fragment.setArguments(args);
        return fragment;
    }

    /******************************** NEW INSTANCE TONIFICAR *************************************+/
     *
     */

    public static SportDetailsDialogFragment newInstanceToning(SportToning sportToning, int option,Context context) {
        SportDetailsDialogFragment fragment = new SportDetailsDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable(TONING, sportToning);
        args.putInt(OPTION, option);
        fragment.setArguments(args);
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

        sportSlimming = getArguments().getParcelable(SLIMMING);
        sportToning = getArguments().getParcelable(TONING);
        sportGainVolume = getArguments().getParcelable(GAINVOLUME);
        option = getArguments().getInt(OPTION);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.sport_dialog_fragment, container, false);
        view.setBackgroundResource(R.drawable.corner_dialog_fragment);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        showLoading();
        findById(view);
        getData();
        setData();
        onClickButtonOpenYoutube();
        onClickButtonOpenInsertFavorite();
        onClickButtonOpenDeleteFavorite();
        onClickCloseButton();

        return view;
    }

    /******************************** CONFIGURA VISTA *************************************+/
     *
     */

    public void findById(View v) {
        textViewTitle = v.findViewById(R.id.textTitleSport);
        buttonOpenYoutube = v.findViewById(R.id.open_youtube_sport);
        textViewDescription = v.findViewById(R.id.textviewDescriptionSport);
        imageViewSport = v.findViewById(R.id.imageViewSport);
        buttonInsert = v.findViewById(R.id.insert_favorite_sport);
        buttonDelete = v.findViewById(R.id.delete_favorite_sport);
        buttonClose = v.findViewById(R.id.close_info_sport);
    }

    private void getData() {
        if (UtilsNetwork.checkConnectionInternetDevice(getContext())) {
            SportFavoriteRepository.getInstance().setGetSportFavorite(this);
            switch (option){
                case Constants.SportNutritionOption.SLIMMING:
                    SportFavoriteRepository.getInstance().getSlimmingSportFavorite();
                    break;
                case Constants.SportNutritionOption.TONING:
                    SportFavoriteRepository.getInstance().getToningSportFavorite();
                    break;
                case Constants.SportNutritionOption.GAIN_VOLUMEN:
                    SportFavoriteRepository.getInstance().getGainVolumeSportFavorite();
                    break;
            }
        } else {
            Toast.makeText(getContext(),getString(R.string.info_network_device),Toast.LENGTH_LONG).show();
        }

    }

    private void setData() {
        switch (option){
            case Constants.SportNutritionOption.SLIMMING:
                textViewTitle.setText(sportSlimming.getName());
                textViewDescription.setText(sportSlimming.getDescription());
                Utils.loadImage(sportSlimming.getPhoto(),imageViewSport,Utils.PLACEHOLDER_GALLERY);

                break;
            case Constants.SportNutritionOption.TONING:
                textViewTitle.setText(sportToning.getName());
                textViewDescription.setText(sportToning.getDescription());
                Utils.loadImage(sportToning.getPhoto(),imageViewSport,Utils.PLACEHOLDER_GALLERY);
                break;
            case Constants.SportNutritionOption.GAIN_VOLUMEN:
                textViewTitle.setText(sportGainVolume.getName());
                textViewDescription.setText(sportGainVolume.getDescription());
                Utils.loadImage(sportGainVolume.getPhoto(),imageViewSport,Utils.PLACEHOLDER_GALLERY);
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

    /******************************** ABRE EL VIDEO EN LA ACTIVITY DE YOUTUBE *************************************+/
     *
     */

    private void onClickButtonOpenYoutube() {
        if(UtilsNetwork.checkConnectionInternetDevice(getContext())){
            buttonOpenYoutube.setOnClickListener(v -> {
                Intent intent = new Intent(getContext(), YoutubeActivity.class);
                switch (option){
                    case Constants.SportNutritionOption.SLIMMING:
                        intent.putExtra(URL, sportSlimming.getVideo());
                        break;
                    case Constants.SportNutritionOption.TONING:
                        intent.putExtra(URL, sportToning.getVideo());
                        break;
                    case Constants.SportNutritionOption.GAIN_VOLUMEN:
                        intent.putExtra(URL, sportGainVolume.getVideo());
                        break;
                }
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            });
        }else{
            buttonOpenYoutube.setEnabled(false);
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
                    case Constants.SportNutritionOption.SLIMMING:
                        SportFavoriteRepository.getInstance().addFavoriteSportSlimming(sportSlimming);
                        break;
                    case Constants.SportNutritionOption.TONING:
                        SportFavoriteRepository.getInstance().addFavoriteSportToning(sportToning);
                        break;
                    case Constants.SportNutritionOption.GAIN_VOLUMEN:
                        SportFavoriteRepository.getInstance().addFavoriteSportGainVolume(sportGainVolume);
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
                    case Constants.SportNutritionOption.SLIMMING:
                        SportFavoriteRepository.getInstance().deleteFavoriteSportSlimming(sportSlimming);
                        break;
                    case Constants.SportNutritionOption.TONING:
                        SportFavoriteRepository.getInstance().deleteFavoriteSportToning(sportToning);
                        break;
                    case Constants.SportNutritionOption.GAIN_VOLUMEN:
                        SportFavoriteRepository.getInstance().deleteFavoriteSportGainVolume(sportGainVolume);
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
    public void addSportFavorite(boolean status) {
        if(status){
            buttonInsert.setEnabled(false);
            buttonDelete.setEnabled(true);
            hideLoading();
            switch (option){
                case Constants.SportNutritionOption.SLIMMING:
                    Toast.makeText(getContext(),sportSlimming.getName()+" "+getString(R.string.agregarafavoritos),Toast.LENGTH_LONG).show();
                    break;
                case Constants.SportNutritionOption.TONING:
                    Toast.makeText(getContext(),sportToning.getName()+" "+getString(R.string.agregarafavoritos),Toast.LENGTH_LONG).show();
                    break;
                case Constants.SportNutritionOption.GAIN_VOLUMEN:
                    Toast.makeText(getContext(),sportGainVolume.getName()+" "+getString(R.string.agregarafavoritos),Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }

    @Override
    public void deleteSportFavorite(boolean status) {
        if(status){
            buttonInsert.setEnabled(true);
            buttonDelete.setEnabled(false);
            hideLoading();
            switch (option){
                case Constants.SportNutritionOption.SLIMMING:
                    Toast.makeText(getContext(),sportSlimming.getName()+" "+getString(R.string.eliminarafavoritos),Toast.LENGTH_LONG).show();
                    break;
                case Constants.SportNutritionOption.TONING:
                    Toast.makeText(getContext(),sportToning.getName()+" "+getString(R.string.eliminarafavoritos),Toast.LENGTH_LONG).show();
                    break;
                case Constants.SportNutritionOption.GAIN_VOLUMEN:
                    Toast.makeText(getContext(),sportGainVolume.getName()+" "+getString(R.string.eliminarafavoritos),Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }

    @Override
    public void getSportSlimmingFavorite(boolean status, List<SportSlimming> sportSlimmings) {
        if (status) {
            this.sportSlimmings = sportSlimmings;
            hideLoading();
            if (checkIsFavorite(this.sportSlimmings, this.sportTonings, this.sportGainVolumes)) {
                buttonInsert.setEnabled(false);
                buttonDelete.setEnabled(true);
            }
        }
    }

    @Override
    public void getSportToningFavorite(boolean status, List<SportToning> sportTonings) {
        if (status) {
            this.sportTonings = sportTonings;
            hideLoading();
            if (checkIsFavorite(this.sportSlimmings, this.sportTonings, this.sportGainVolumes)){
                buttonInsert.setEnabled(false);
                buttonDelete.setEnabled(true);
            }
        }
    }

    @Override
    public void getSportGainVolumeFavorite(boolean status, List<SportGainVolume> sportGainVolumes) {
        if (status) {
            this.sportGainVolumes = sportGainVolumes;
            hideLoading();
            if (checkIsFavorite(this.sportSlimmings, this.sportTonings, this.sportGainVolumes)){
                buttonInsert.setEnabled(false);
                buttonDelete.setEnabled(true);
            }
        }
    }

    private boolean checkIsFavorite (List<SportSlimming> sportSlimmings, List<SportToning> sportTonings, List<SportGainVolume> sportGainVolumes) {
        boolean isFavorite = false;
        switch (option) {
            case Constants.SportNutritionOption.SLIMMING:
                List<SportSlimming> slimmings = sportSlimmings.stream()
                        .filter(item -> item.getName().equals(sportSlimming.getName()))
                        .collect(Collectors.toList());
                isFavorite = slimmings.size() != 0;
                return isFavorite;
            case Constants.SportNutritionOption.TONING:
                List<SportToning> tonings = sportTonings.stream()
                        .filter(item -> item.getName().equals(sportToning.getName()))
                        .collect(Collectors.toList());
                isFavorite = tonings.size() != 0;
                return isFavorite;
            case Constants.SportNutritionOption.GAIN_VOLUMEN:
                List<SportGainVolume> gainVolumes = sportGainVolumes.stream()
                        .filter(item -> item.getName().equals(sportGainVolume.getName()))
                        .collect(Collectors.toList());
                isFavorite = gainVolumes.size() != 0;
                return isFavorite;
        }
        return isFavorite;
    }


    @Override
    public void getSportAllFavorite(boolean status, List<DefaultSport> defaultSports) {}

    @Override
    public void emptySportFavorite(boolean status) {}
}