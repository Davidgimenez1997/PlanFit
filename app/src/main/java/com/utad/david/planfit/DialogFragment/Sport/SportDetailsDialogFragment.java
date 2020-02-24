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

public class SportDetailsDialogFragment extends BaseDialogFragment
        implements GetSportFavorite {

    /******************************** VARIABLES *************************************+/
     *
     */

    public SportSlimming sportSlimming;
    public SportGainVolume sportGainVolume;
    public SportToning sportToning;
    public Callback listener;
    public int option;
    private static String SLIMMING = Constants.SportDetails.EXTRA_SLIMMING;
    private static String GAINVOLUME = Constants.SportDetails.EXTRA_GAINVOLUME;
    private static String TONING = Constants.SportDetails.EXTRA_TONING;
    private static String OPTION = Constants.SportDetails.EXTRA_OPTION;
    private String URL = Constants.SportDetails.EXTRA_URL;

    private TextView textViewTitle;
    private Button buttonOpenYoutube;
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

        if(UtilsNetwork.checkConnectionInternetDevice(context)){
            SportFavoriteRepository.getInstance().setGetSportFavorite(fragment);
            SportFavoriteRepository.getInstance().getSlimmingSportFavorite();
        }else{
            Toast.makeText(context,"Comprueba su conexion de internet y reinice la aplicación",Toast.LENGTH_LONG).show();
        }

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

        if(UtilsNetwork.checkConnectionInternetDevice(context)){
            SportFavoriteRepository.getInstance().setGetSportFavorite(fragment);
            SportFavoriteRepository.getInstance().getGainVolumeSportFavorite();
        }else{
            Toast.makeText(context,"Comprueba su conexion de internet y reinice la aplicación",Toast.LENGTH_LONG).show();
        }

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

        if(UtilsNetwork.checkConnectionInternetDevice(context)){
            SportFavoriteRepository.getInstance().setGetSportFavorite(fragment);
            SportFavoriteRepository.getInstance().getToningSportFavorite();
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
        putData();
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

    private void putData() {
        switch (option){
            case 0:
                textViewTitle.setText(sportSlimming.getName());
                textViewDescription.setText(sportSlimming.getDescription());
                Utils.loadImage(sportSlimming.getPhoto(),imageViewSport,Utils.PLACEHOLDER_GALLERY);

                break;
            case 1:
                textViewTitle.setText(sportToning.getName());
                textViewDescription.setText(sportToning.getDescription());
                Utils.loadImage(sportToning.getPhoto(),imageViewSport,Utils.PLACEHOLDER_GALLERY);
                break;
            case 2:
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
                    case 0:
                        SportFavoriteRepository.getInstance().addFavoriteSportSlimming(sportSlimming);
                        break;
                    case 1:
                        SportFavoriteRepository.getInstance().addFavoriteSportToning(sportToning);
                        break;
                    case 2:
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
                    case 0:
                        SportFavoriteRepository.getInstance().deleteFavoriteSportSlimming(sportSlimming);
                        break;
                    case 1:
                        SportFavoriteRepository.getInstance().deleteFavoriteSportToning(sportToning);
                        break;
                    case 2:
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
    public void deleteSportFavorite(boolean status) {
        if(status){
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
    public void getSportSlimmingFavorite(boolean status, List<SportSlimming> sportSlimmings) {
        if (status) {
            hideLoading();
            for(int i = 0; i< sportSlimmings.size(); i++){
                if(sportSlimmings.get(i).getName().equals(sportSlimming.getName())){
                    buttonInsert.setEnabled(false);
                    buttonDelete.setEnabled(true);
                    return;
                }
            }
        }
    }

    @Override
    public void getSportToningFavorite(boolean status, List<SportToning> sportTonings) {
        if (status) {
            hideLoading();
            for(int i = 0; i< sportTonings.size(); i++){
                if(sportTonings.get(i).getName().equals(sportToning.getName())){
                    buttonInsert.setEnabled(false);
                    buttonDelete.setEnabled(true);
                    return;
                }
            }
        }
    }

    @Override
    public void getSportGainVolumeFavorite(boolean status, List<SportGainVolume> sportGainVolumes) {
        if (status) {
            hideLoading();
            for(int i = 0; i< sportGainVolumes.size(); i++){
                if(sportGainVolumes.get(i).getName().equals(sportGainVolume.getName())){
                    buttonInsert.setEnabled(false);
                    buttonDelete.setEnabled(true);
                    return;
                }
            }
        }
    }

    @Override
    public void getSportAllFavorite(boolean status, List<DefaultSport> defaultSports) {}

    @Override
    public void emptySportFavorite(boolean status) {}
}