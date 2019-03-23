package com.utad.david.planfit.DialogFragment.Favorite;

import android.app.ProgressDialog;
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
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.utad.david.planfit.Activitys.YoutubeActivity;
import com.utad.david.planfit.Model.Sport.DefaultSport;
import com.utad.david.planfit.R;

public class SportFavoriteDetailsDialogFragment extends DialogFragment {

    private static String SPORT = "SPORT";
    private DefaultSport defaultSport;

    public static SportFavoriteDetailsDialogFragment newInstance(DefaultSport defaultSport) {
        SportFavoriteDetailsDialogFragment fragment = new SportFavoriteDetailsDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable(SPORT, defaultSport);
        fragment.setArguments(args);
        return fragment;
    }

    public interface CallbackFavoriteSport{
        void onClickClose();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        defaultSport = getArguments().getParcelable(SPORT);
    }

    private TextView textViewTitle;
    private Button buttonOpenRecipe;
    private TextView textViewDescription;
    private ImageView imageViewSport;
    private Button buttonClose;
    private CallbackFavoriteSport listener;

    public void setListener(CallbackFavoriteSport listener) {
        this.listener = listener;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.sport_favorite_dialog_fragment, container, false);
        view.setBackgroundResource(R.drawable.corner_dialog_fragment);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        findById(view);
        putData();
        onClickButtonOpenYoutube();
        onClickCloseButton();

        return view;

    }

    public void findById(View v) {
        textViewTitle = v.findViewById(R.id.textTitleSport);
        buttonOpenRecipe = v.findViewById(R.id.open_youtube_sport);
        textViewDescription = v.findViewById(R.id.textviewDescriptionSport);
        imageViewSport = v.findViewById(R.id.imageViewSport);
        buttonClose = v.findViewById(R.id.close_favorite_sport);
    }

    private void putData() {
        RequestOptions requestOptions = new RequestOptions();
        textViewTitle.setText(defaultSport.getName());
        textViewDescription.setText(defaultSport.getDescription());
        requestOptions.placeholder(R.drawable.icon_gallery);
        Glide.with(this).load(defaultSport.getPhoto()).into(imageViewSport);
    }

    private void onClickButtonOpenYoutube() {
        buttonOpenRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), YoutubeActivity.class);
                intent.putExtra("url", defaultSport.getVideo());
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
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
