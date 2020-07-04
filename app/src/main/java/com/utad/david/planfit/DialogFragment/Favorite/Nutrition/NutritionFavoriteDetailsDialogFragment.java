package com.utad.david.planfit.DialogFragment.Favorite.Nutrition;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.crashlytics.android.Crashlytics;
import com.utad.david.planfit.Activitys.WebView.WebViewActivity;
import com.utad.david.planfit.Base.BaseDialogFragment;
import com.utad.david.planfit.Model.Nutrition.DefaultNutrition;
import com.utad.david.planfit.R;
import com.utad.david.planfit.Utils.Constants;
import com.utad.david.planfit.Utils.Utils;

import io.fabric.sdk.android.Fabric;

public class NutritionFavoriteDetailsDialogFragment
        extends BaseDialogFragment
        implements NutritionFavoriteDetailsDialogView {

    /******************************** VARIABLES *************************************+/
     *
     */

    private DefaultNutrition defaultNutrition;
    private TextView textViewTitle;
    private Button buttonOpenRecipe;
    private TextView textViewDescription;
    private ImageView imageViewSport;
    private Button buttonClose;
    private Button buttonDelete;
    private Callback callback;
    private NutritionFavoriteDetailsDialogPresenter nutritionDetailsDialogPresenter;

    /******************************** INTERFAZ *************************************+/
     *
     */

    public interface Callback {
        void onClickClose();
        void setDataChange(DefaultNutrition item);
    }

    public void setListener(Callback callback) {
        this.callback = callback;
    }

    /******************************** NEW INSTANCE *************************************+/
     *
     */

    public static NutritionFavoriteDetailsDialogFragment newInstance(DefaultNutrition defaultNutrition) {
        NutritionFavoriteDetailsDialogFragment fragment = new NutritionFavoriteDetailsDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable(Constants.NutritionFavoriteDetails.EXTRA_NUTRICION, defaultNutrition);
        fragment.setArguments(args);
        return fragment;
    }

    /******************************** GET ARGUMENTS *************************************+/
     *
     */

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.nutritionDetailsDialogPresenter = new NutritionFavoriteDetailsDialogPresenter(this);

        if (this.nutritionDetailsDialogPresenter.checkInternetInDevice(getContext())) {
            Fabric.with(getContext(),new Crashlytics());
        }

        this.defaultNutrition = getArguments().getParcelable(Constants.NutritionFavoriteDetails.EXTRA_NUTRICION);
        this.nutritionDetailsDialogPresenter.setNutritionFavorite(this.defaultNutrition);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.nutrition_favorite_dialog_fragment, container, false);
        view.setBackgroundResource(R.drawable.corner_dialog_fragment);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        this.findById(view);
        this.putData();
        this.onClickButtonOpenRecipe();
        this.onClickCloseButton();
        this.onClickDeleteButton();

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
        this.buttonClose = v.findViewById(R.id.close_favorite_nutrition);
        this.buttonDelete = v.findViewById(R.id.delete_favorite_nutrition);
    }

    private void putData() {
        this.textViewTitle.setText(this.defaultNutrition.getName());
        this.textViewDescription.setText(this.defaultNutrition.getDescription());
        Utils.loadImage(this.defaultNutrition.getPhoto(), this.imageViewSport, Utils.PLACEHOLDER_GALLERY);
    }

    /******************************** ABRE LA RECETA EN UN WEBVIEW *************************************+/
     *
     */

    private void onClickButtonOpenRecipe() {
        if (!this.nutritionDetailsDialogPresenter.checkInternetInDevice(getContext())) {
            this.buttonOpenRecipe.setEnabled(false);
        } else {
            this.buttonOpenRecipe.setOnClickListener(v -> {
                Intent intent = new Intent(getContext(), WebViewActivity.class);
                intent.putExtra(WebViewActivity.EXTRA_TITLE, this.defaultNutrition.getName());
                intent.putExtra(WebViewActivity.EXTRA_URL, this.defaultNutrition.getUrl());
                intent.putExtra(WebViewActivity.EXTRA_MODE, Constants.ModeWebView.MODE_RECIPE);
                getActivity().overridePendingTransition(R.anim.slide_in_bottom, R.anim.stay);
                startActivity(intent);
            });
        }
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

    /******************************** BORRAR DE FAVORITOS *************************************+/
     *
     */

    private void onClickDeleteButton(){
        if (this.nutritionDetailsDialogPresenter.checkInternetInDevice(getContext())) {
            this.buttonDelete.setOnClickListener(v -> {
                if (this.callback != null) {
                    this.nutritionDetailsDialogPresenter.onClickDeleleNutrition();
                    dismiss();
                }
            });
        } else {
            this.buttonDelete.setEnabled(false);
        }
    }

    /******************************** CALLBACK DEl PRESENTER *************************************+/
     *
     */

    @Override
    public void deviceOfflineMessage() {
        Toast.makeText(getContext(),getString(R.string.info_network_device),Toast.LENGTH_LONG).show();
    }

    @Override
    public void deleteNutritionFavorite() {
        if (this.callback != null) {
            this.callback.setDataChange(this.defaultNutrition);
        }
    }
}
