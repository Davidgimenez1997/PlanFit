package com.utad.david.planfit.DialogFragment.Favorite;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.crashlytics.android.Crashlytics;
import com.utad.david.planfit.Activitys.WebViewActivity;
import com.utad.david.planfit.Data.Firebase.FirebaseAdmin;
import com.utad.david.planfit.Data.SessionUser;
import com.utad.david.planfit.Model.Nutrition.DefaultNutrition;
import com.utad.david.planfit.R;
import com.utad.david.planfit.Utils.Constants;
import com.utad.david.planfit.Utils.UtilsNetwork;
import io.fabric.sdk.android.Fabric;

public class NutritionFavoriteDetailsDialogFragment extends DialogFragment implements FirebaseAdmin.FirebaseAdminFavoriteNutrition{

    private DefaultNutrition defaultNutrition;

    public static NutritionFavoriteDetailsDialogFragment newInstance(DefaultNutrition defaultNutrition) {
        NutritionFavoriteDetailsDialogFragment fragment = new NutritionFavoriteDetailsDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable(Constants.NutricionFavoriteDetails.EXTRA_NUTRICION, defaultNutrition);
        fragment.setArguments(args);
        return fragment;
    }

    public interface CallbackNutritionFavorite{
        void onClickClose();
        void setDataChange();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(UtilsNetwork.checkConnectionInternetDevice(getContext())){
            Fabric.with(getContext(),new Crashlytics());
            SessionUser.getInstance().firebaseAdmin.setFirebaseAdminFavoriteNutrition(this);
        }else{
            Toast.makeText(getContext(),getString(R.string.info_network_device),Toast.LENGTH_LONG).show();
        }

        defaultNutrition = getArguments().getParcelable(Constants.NutricionFavoriteDetails.EXTRA_NUTRICION);
    }

    private TextView textViewTitle;
    private Button buttonOpenRecipe;
    private TextView textViewDescription;
    private ImageView imageViewSport;
    private Button buttonClose;
    private Button buttonDelete;
    private CallbackNutritionFavorite listener;

    public void setListener(CallbackNutritionFavorite listener) {
        this.listener = listener;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.nutrition_favorite_dialog_fragment, container, false);
        view.setBackgroundResource(R.drawable.corner_dialog_fragment);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        findById(view);
        putData();
        onClickButtonOpenRecipe();
        onClickCloseButton();
        onClickDeleteButton();
        return view;
    }

    public void findById(View v) {
        textViewTitle = v.findViewById(R.id.textTitleNutrition);
        buttonOpenRecipe = v.findViewById(R.id.open_recipe_nutrition);
        textViewDescription = v.findViewById(R.id.textviewDescriptionNutrition);
        imageViewSport = v.findViewById(R.id.imageViewNutrition);
        buttonClose = v.findViewById(R.id.close_favorite_nutrition);
        buttonDelete = v.findViewById(R.id.delete_favorite_nutrition);
    }

    private void putData() {
        RequestOptions requestOptions = new RequestOptions();
        textViewTitle.setText(defaultNutrition.getName());
        textViewDescription.setText(defaultNutrition.getDescription());
        requestOptions.placeholder(R.drawable.icon_gallery);
        Glide.with(this).load(defaultNutrition.getPhoto()).into(imageViewSport);
    }

    private void onClickButtonOpenRecipe() {
        if(!UtilsNetwork.checkConnectionInternetDevice(getContext())){
            buttonOpenRecipe.setEnabled(false);
        }else{
            buttonOpenRecipe.setOnClickListener(v -> {
                Intent intent = new Intent(getContext(), WebViewActivity.class);
                intent.putExtra(WebViewActivity.EXTRA_TITLE, defaultNutrition.getName());
                intent.putExtra(WebViewActivity.EXTRA_URL, defaultNutrition.getUrl());
                intent.putExtra(WebViewActivity.EXTRA_MODE, Constants.ModeWebView.MODE_RECIPE);
                getActivity().overridePendingTransition(R.anim.slide_in_bottom, R.anim.stay);
                startActivity(intent);
            });
        }
    }

    private void onClickCloseButton(){
        buttonClose.setOnClickListener(v -> {
            if(listener!=null){
                listener.onClickClose();
            }
        });
    }

    private void onClickDeleteButton(){
        if(UtilsNetwork.checkConnectionInternetDevice(getContext())){
            buttonDelete.setOnClickListener(v -> {
                if(listener!=null){
                    SessionUser.getInstance().firebaseAdmin.deleteDefaultNutritionFavorite(defaultNutrition);
                    dismiss();
                }
            });
        }else{
            buttonDelete.setEnabled(false);
        }
    }

    @Override
    public void deleteFavoriteNutrition(boolean end) {
        if(end==true){
            if(listener!=null){
                listener.setDataChange();
            }
        }
    }

    @Override
    public void inserNutritionFavoriteFirebase(boolean end) {}


    @Override
    public void downloandCollectionNutritionFavorite(boolean end) {}

    @Override
    public void emptyCollectionNutritionFavorite(boolean end) {}

}
