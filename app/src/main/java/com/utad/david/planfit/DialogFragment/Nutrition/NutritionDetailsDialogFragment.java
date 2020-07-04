package com.utad.david.planfit.DialogFragment.Nutrition;

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
import com.utad.david.planfit.Activitys.WebView.WebViewActivity;
import com.utad.david.planfit.Base.BaseDialogFragment;
import com.utad.david.planfit.Model.Nutrition.DefaultNutrition;
import com.utad.david.planfit.R;
import com.utad.david.planfit.Utils.Constants;
import com.utad.david.planfit.Utils.Utils;
import io.fabric.sdk.android.Fabric;

public class NutritionDetailsDialogFragment
        extends BaseDialogFragment
        implements NutritionDetailsDialogView {

    /******************************** VARIABLES *************************************+/
     *
     */

    private TextView textViewTitle;
    private Button buttonOpenRecipe;
    private TextView textViewDescription;
    private ImageView imageViewSport;
    private Button buttonInsert;
    private Button buttonDelete;
    private Button buttonClose;
    private Callback callback;

    private DefaultNutrition item;
    private int mode;
    private NutritionDetailsDialogPresenter detailsDialogPresenter;

    /******************************** INTERFAZ *************************************+/
     *
     */

    public interface Callback {
        void onClickClose();
    }

    public void setCallbackNutrition(Callback callback) {
        this.callback = callback;
    }

    /******************************** NEW INSTANCE *************************************+/
     *
     */

    public static NutritionDetailsDialogFragment newInstance(DefaultNutrition item, int mode) {
        NutritionDetailsDialogFragment fragment = new NutritionDetailsDialogFragment();
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
        this.detailsDialogPresenter = new NutritionDetailsDialogPresenter(this.item, this.mode, this);
        if (this.detailsDialogPresenter.checkConnectionInternet(getContext())) {
            Fabric.with(getContext());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.nutrtion_dialog_fragment, container, false);
        view.setBackgroundResource(R.drawable.corner_dialog_fragment);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        showLoading();
        this.findById(view);
        this.putData();
        this.setData();
        this.onClickButtonOpenRecipe();
        this.onClickButtonOpenInsertFavorite();
        this.onClickButtonOpenDeleteFavorite();
        this.onClickCloseButton();

        return view;
    }

    /******************************** CONFIGURA VISTA *************************************+/
     *
     */

    public void findById(View v) {
        this.textViewTitle = v.findViewById(R.id.textTitleNutrition);
        this.buttonOpenRecipe = v.findViewById(R.id.open_recipe_nutrition);
        this.textViewDescription = v.findViewById(R.id.textviewDescriptionNutrition);
        this.imageViewSport = v.findViewById(R.id.imageViewNutrition);
        this.buttonInsert = v.findViewById(R.id.insert_favoriteNutrition);
        this.buttonDelete = v.findViewById(R.id.delete_favorite_nutrition);
        this.buttonClose = v.findViewById(R.id.close_nutrition);
    }

    private void putData() {
        if (this.detailsDialogPresenter.checkConnectionInternet(getContext())) {
            this.detailsDialogPresenter.getNutritionListByType();
        }
    }

    private void setData() {
        String title = "", description = "", photo = "";
        switch (this.mode){
            case Constants.SportNutritionOption.SLIMMING:
                title = this.detailsDialogPresenter.getTitleItem();
                description = this.detailsDialogPresenter.getDescriptionItem();
                photo = this.detailsDialogPresenter.getPhotoItem();
                break;
            case Constants.SportNutritionOption.TONING:
                title = this.detailsDialogPresenter.getTitleItem();
                description = this.detailsDialogPresenter.getDescriptionItem();
                photo = this.detailsDialogPresenter.getPhotoItem();
                break;
            case Constants.SportNutritionOption.GAIN_VOLUMEN:
                title = this.detailsDialogPresenter.getTitleItem();
                description = this.detailsDialogPresenter.getDescriptionItem();
                photo = this.detailsDialogPresenter.getPhotoItem();
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

    private void onClickCloseButton(){
        this.buttonClose.setOnClickListener(v -> {
            if (this.callback != null) {
                this.callback.onClickClose();
            }
        });
    }

    /******************************** ABRE LA RECETA EN UN WEBVIEW *************************************+/
     *
     */

    private void onClickButtonOpenRecipe() {
        if (this.detailsDialogPresenter.checkConnectionInternet(getContext())) {
            this.buttonOpenRecipe.setOnClickListener(v -> {
                String title = null, url = null;
                switch (this.mode){
                    case Constants.SportNutritionOption.SLIMMING:
                        title = this.item.getName();
                        url = this.item.getUrl();
                        break;
                    case Constants.SportNutritionOption.TONING:
                        title = this.item.getName();
                        url = this.item.getUrl();
                        break;
                    case Constants.SportNutritionOption.GAIN_VOLUMEN:
                        title = this.item.getName();
                        url = this.item.getUrl();
                        break;
                }
                this.intentOpenRecipe(title, url);
            });
        } else {
            this.buttonOpenRecipe.setEnabled(false);
        }
    }

    private void intentOpenRecipe(String title, String url) {
        Intent intent = new Intent(getContext(), WebViewActivity.class);
        intent.putExtra(WebViewActivity.EXTRA_TITLE, title);
        intent.putExtra(WebViewActivity.EXTRA_URL, url);
        intent.putExtra(WebViewActivity.EXTRA_MODE, Constants.ModeWebView.MODE_RECIPE);
        getActivity().overridePendingTransition(R.anim.slide_in_bottom, R.anim.stay);
        startActivity(intent);
    }

    /******************************** AGREGAR A FAVORITOS *************************************+/
     *
     */

    private void onClickButtonOpenInsertFavorite() {
        if (this.detailsDialogPresenter.checkConnectionInternet(getContext())) {
            this.buttonInsert.setOnClickListener(v -> {
                showLoading();
                this.detailsDialogPresenter.clickInsertNutritionFavorite();
            });
        } else {
            this.buttonInsert.setEnabled(false);
        }
    }

    /******************************** BORRAR DE FAVORITOS *************************************+/
     *
     */

    private void onClickButtonOpenDeleteFavorite(){
        if (this.detailsDialogPresenter.checkConnectionInternet(getContext())) {
            this.buttonDelete.setOnClickListener(v -> {
                showLoading();
                this.detailsDialogPresenter.clickDeleteNutritionFavorite();
            });
        } else {
            this.buttonDelete.setEnabled(false);
        }
    }

    /******************************** CALLBACK DE FIREBASE *************************************+/
     *
     */

    @Override
    public void deviceOfflineMessage() {
        Toast.makeText(getContext(),getString(R.string.info_network_device),Toast.LENGTH_LONG).show();
    }

    @Override
    public void addFavoriteNutrition() {
        hideLoading();
        this.buttonInsert.setEnabled(false);
        this.buttonDelete.setEnabled(true);
        String name = "";
        switch (this.mode) {
            case Constants.SportNutritionOption.SLIMMING:
                name = this.detailsDialogPresenter.getTitleItem();
                break;
            case Constants.SportNutritionOption.TONING:
                name = this.detailsDialogPresenter.getTitleItem();
                break;
            case Constants.SportNutritionOption.GAIN_VOLUMEN:
                name = this.detailsDialogPresenter.getTitleItem();
                break;
        }
        Toast.makeText(getContext(),name+" "+getString(R.string.agregarafavoritos),Toast.LENGTH_LONG).show();
    }

    @Override
    public void deleteFavoriteNutrition() {
        hideLoading();
        this.buttonInsert.setEnabled(true);
        this.buttonDelete.setEnabled(false);
        String name = "";
        switch (this.mode){
            case Constants.SportNutritionOption.SLIMMING:
                name = this.detailsDialogPresenter.getTitleItem();
                break;
            case Constants.SportNutritionOption.TONING:
                name = this.detailsDialogPresenter.getTitleItem();
                break;
            case Constants.SportNutritionOption.GAIN_VOLUMEN:
                name = this.detailsDialogPresenter.getTitleItem();
                break;
        }
        Toast.makeText(getContext(), name+" "+getString(R.string.eliminarafavoritos),Toast.LENGTH_LONG).show();
    }

    @Override
    public void getFavoriteNutritonList() {
        hideLoading();
    }

    @Override
    public void updateButtonsUi() {
        this.buttonInsert.setEnabled(false);
        this.buttonDelete.setEnabled(true);
    }
}