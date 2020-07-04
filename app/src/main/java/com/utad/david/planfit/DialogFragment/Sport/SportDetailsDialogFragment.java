package com.utad.david.planfit.DialogFragment.Sport;

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
import com.utad.david.planfit.Model.Sport.DefaultSport;
import com.utad.david.planfit.R;
import com.utad.david.planfit.Utils.Constants;
import com.utad.david.planfit.Utils.Utils;
import io.fabric.sdk.android.Fabric;

public class SportDetailsDialogFragment extends BaseDialogFragment
        implements SportDetailsDialogView {

    /******************************** VARIABLES *************************************+/
     *
     */

    private String URL = Constants.SportDetails.EXTRA_URL;
    private DefaultSport item;
    private int mode;
    public Callback callback;

    private TextView textViewTitle;
    private Button buttonOpenYoutube;
    private TextView textViewDescription;
    private ImageView imageViewSport;
    private Button buttonInsert;
    private Button buttonDelete;
    private Button buttonClose;

    private SportDetailsDialogPresenter sportDetailsDialogPresenter;


    /******************************** INTERFAZ *************************************+/
     *
     */

    public interface Callback {
        void onClickClose();
    }

    public void setListener(Callback callback) {
        this.callback = callback;
    }

    public static SportDetailsDialogFragment newInstance(DefaultSport item, int mode) {
        SportDetailsDialogFragment fragment = new SportDetailsDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable(Constants.SportDetails.EXTRA_ITEM, item);
        args.putInt(Constants.SportDetails.EXTRA_MODE, mode);
        fragment.setArguments(args);
        return fragment;
    }

    /******************************** GET ARGUMENTS *************************************+/
     *
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mode = getArguments().getInt(Constants.SportDetails.EXTRA_MODE);
        this.item = getArguments().getParcelable(Constants.SportDetails.EXTRA_ITEM);
        this.sportDetailsDialogPresenter = new SportDetailsDialogPresenter(this.item, this.mode, this);
        if (this.sportDetailsDialogPresenter.checkConnectionInternet(getContext())) {
            Fabric.with(getContext(),new Crashlytics());
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.sport_dialog_fragment, container, false);
        view.setBackgroundResource(R.drawable.corner_dialog_fragment);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        showLoading();
        this.findById(view);
        this.getData();
        this.setData();
        this.onClickButtonOpenYoutube();
        this.onClickButtonOpenInsertFavorite();
        this.onClickButtonOpenDeleteFavorite();
        this.onClickCloseButton();

        return view;
    }

    /******************************** CONFIGURA VISTA *************************************+/
     *
     */

    public void findById(View v) {
        this.textViewTitle = v.findViewById(R.id.textTitleSport);
        this.buttonOpenYoutube = v.findViewById(R.id.open_youtube_sport);
        this.textViewDescription = v.findViewById(R.id.textviewDescriptionSport);
        this.imageViewSport = v.findViewById(R.id.imageViewSport);
        this.buttonInsert = v.findViewById(R.id.insert_favorite_sport);
        this.buttonDelete = v.findViewById(R.id.delete_favorite_sport);
        this.buttonClose = v.findViewById(R.id.close_info_sport);
    }

    private void getData() {
        if (this.sportDetailsDialogPresenter.checkConnectionInternet(getContext())) {
           this.sportDetailsDialogPresenter.getSportListByType();
        }
    }

    private void setData() {
        String title = "", description = "", photo = "";
        switch (this.mode){
            case Constants.SportNutritionOption.SLIMMING:
                title = this.sportDetailsDialogPresenter.getTitleItem();
                description = this.sportDetailsDialogPresenter.getDescriptionItem();
                photo = this.sportDetailsDialogPresenter.getPhotoItem();
                break;
            case Constants.SportNutritionOption.TONING:
                title = this.sportDetailsDialogPresenter.getTitleItem();
                description = this.sportDetailsDialogPresenter.getDescriptionItem();
                photo = this.sportDetailsDialogPresenter.getPhotoItem();
                break;
            case Constants.SportNutritionOption.GAIN_VOLUMEN:
                title = this.sportDetailsDialogPresenter.getTitleItem();
                description = this.sportDetailsDialogPresenter.getDescriptionItem();
                photo = this.sportDetailsDialogPresenter.getPhotoItem();
                break;
        }
        this.setUiInfo(title, description, photo);
    }

    private void setUiInfo(String title, String description, String photo) {
        this.textViewTitle.setText(title);
        this.textViewDescription.setText(description);
        Utils.loadImage(photo, this.imageViewSport, Utils.PLACEHOLDER_GALLERY);
    }

    /******************************** CIERRA LA PANTALLA *************************************+/
     *
     */

    private void onClickCloseButton() {
        this.buttonClose.setOnClickListener(v -> {
            if(this.callback != null){
                this.callback.onClickClose();
            }
        });
    }

    /******************************** ABRE EL VIDEO EN LA ACTIVITY DE YOUTUBE *************************************+/
     *
     */

    private void onClickButtonOpenYoutube() {
        if(this.sportDetailsDialogPresenter.checkConnectionInternet(getContext())){
            this.buttonOpenYoutube.setOnClickListener(v -> {
                String video = "";
                switch (this.mode){
                    case Constants.SportNutritionOption.SLIMMING:
                        video = this.sportDetailsDialogPresenter.getVideoItem();
                        break;
                    case Constants.SportNutritionOption.TONING:
                        video = this.sportDetailsDialogPresenter.getVideoItem();
                        break;
                    case Constants.SportNutritionOption.GAIN_VOLUMEN:
                        video = this.sportDetailsDialogPresenter.getVideoItem();
                        break;
                }
                this.intentOpenYoutubeActivity(video);
            });
        } else {
            this.buttonOpenYoutube.setEnabled(false);
        }
    }

    private void intentOpenYoutubeActivity(String video) {
        Intent intent = new Intent(getContext(), YoutubeActivity.class);
        intent.putExtra(URL, video);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    /******************************** AGREGAR A FAVORITOS *************************************+/
     *
     */

    private void onClickButtonOpenInsertFavorite() {
        if (this.sportDetailsDialogPresenter.checkConnectionInternet(getContext())) {
            this.buttonInsert.setOnClickListener(v -> {
                showLoading();
                this.sportDetailsDialogPresenter.clickInsertSportFavorite();
            });
        } else {
            this.buttonInsert.setEnabled(false);
        }
    }

    /******************************** BORRAR DE FAVORITOS *************************************+/
     *
     */

    private void onClickButtonOpenDeleteFavorite(){
        if (this.sportDetailsDialogPresenter.checkConnectionInternet(getContext())) {
            showLoading();
            this.buttonDelete.setOnClickListener(v -> {
                this.sportDetailsDialogPresenter.clickDeleteSportFavorite();
            });
        } else {
            this.buttonDelete.setEnabled(false);
        }
    }

    /******************************** CALLBACK DEL PRESENTER *************************************+/
     *
     */

    @Override
    public void addFavoriteSport() {
        hideLoading();
        this.buttonInsert.setEnabled(false);
        this.buttonDelete.setEnabled(true);
        String name = "";
        switch (this.mode) {
            case Constants.SportNutritionOption.SLIMMING:
                name = this.sportDetailsDialogPresenter.getTitleItem();
                break;
            case Constants.SportNutritionOption.TONING:
                name = this.sportDetailsDialogPresenter.getTitleItem();
                break;
            case Constants.SportNutritionOption.GAIN_VOLUMEN:
                name = this.sportDetailsDialogPresenter.getTitleItem();
                break;
        }
        Toast.makeText(getContext(),name+" "+getString(R.string.agregarafavoritos),Toast.LENGTH_LONG).show();
    }

    @Override
    public void deleteFavoriteSport() {
        hideLoading();
        this.buttonInsert.setEnabled(true);
        this.buttonDelete.setEnabled(false);
        String name = "";
        switch (this.mode){
            case Constants.SportNutritionOption.SLIMMING:
                name = this.sportDetailsDialogPresenter.getTitleItem();
                break;
            case Constants.SportNutritionOption.TONING:
                name = this.sportDetailsDialogPresenter.getTitleItem();
                break;
            case Constants.SportNutritionOption.GAIN_VOLUMEN:
                name = this.sportDetailsDialogPresenter.getTitleItem();
                break;
        }
        Toast.makeText(getContext(), name+" "+getString(R.string.eliminarafavoritos),Toast.LENGTH_LONG).show();
    }

    @Override
    public void getFavoriteSportList() {
        hideLoading();
    }

    @Override
    public void updateButtonsUi() {
        this.buttonInsert.setEnabled(false);
        this.buttonDelete.setEnabled(true);
    }

    @Override
    public void deviceOfflineMessage() {
        Toast.makeText(getContext(),getString(R.string.info_network_device),Toast.LENGTH_LONG).show();
    }
}