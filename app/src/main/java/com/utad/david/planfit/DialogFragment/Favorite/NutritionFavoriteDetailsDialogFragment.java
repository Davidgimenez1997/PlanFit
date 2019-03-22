package com.utad.david.planfit.DialogFragment.Favorite;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.utad.david.planfit.DialogFragment.Nutrition.NutritionDetailsDialogFragment;
import com.utad.david.planfit.Model.Nutrition.DefaultNutrition;
import com.utad.david.planfit.R;

public class NutritionFavoriteDetailsDialogFragment extends DialogFragment {

    private static String NUTRITION = "NUTRITION";
    private DefaultNutrition defaultNutrition;

    public static NutritionFavoriteDetailsDialogFragment newInstance(DefaultNutrition defaultNutrition) {
        NutritionFavoriteDetailsDialogFragment fragment = new NutritionFavoriteDetailsDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable(NUTRITION, defaultNutrition);
        fragment.setArguments(args);
        return fragment;
    }

    public interface CallbackNutritionFavorite{
        void onClickClose();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        defaultNutrition = getArguments().getParcelable(NUTRITION);
    }

    private TextView textViewTitle;
    private Button buttonOpenRecipe;
    private TextView textViewDescription;
    private ImageView imageViewSport;
    private Button buttonClose;
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
        onClickButtonOpenYoutube();
        onClickCloseButton();

        return view;

    }

    public void findById(View v) {
        textViewTitle = v.findViewById(R.id.textTitleNutrition);
        buttonOpenRecipe = v.findViewById(R.id.open_recipe_nutrition);
        textViewDescription = v.findViewById(R.id.textviewDescriptionNutrition);
        imageViewSport = v.findViewById(R.id.imageViewNutrition);
        buttonClose = v.findViewById(R.id.close_favorite_nutrition);
    }

    private void putData() {
        RequestOptions requestOptions = new RequestOptions();
        textViewTitle.setText(defaultNutrition.getName());
        textViewDescription.setText(defaultNutrition.getDescription());
        requestOptions.placeholder(R.drawable.icon_gallery);
        Glide.with(this).load(defaultNutrition.getPhoto()).into(imageViewSport);
    }

    private void onClickButtonOpenYoutube() {
        buttonOpenRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(defaultNutrition.getUrl()));
                startActivity(i);
            }
        });
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

}
