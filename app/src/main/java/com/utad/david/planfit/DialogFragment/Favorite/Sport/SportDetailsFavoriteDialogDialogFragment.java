package com.utad.david.planfit.DialogFragment.Favorite.Sport;

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
import com.utad.david.planfit.Activitys.YoutubeActivity;
import com.utad.david.planfit.Base.BaseDialogFragment;
import com.utad.david.planfit.Model.Sport.DefaultSport;
import com.utad.david.planfit.R;
import com.utad.david.planfit.Utils.Constants;
import com.utad.david.planfit.Utils.Utils;
import io.fabric.sdk.android.Fabric;

public class SportDetailsFavoriteDialogDialogFragment
        extends BaseDialogFragment
        implements SportDetailsFavoriteDialogView {

    /******************************** VARIABLES *************************************+/
     *
     */

    private static String SPORT = Constants.SportFavoriteDetails.EXTRA_SPORT;
    private static String URL = Constants.SportFavoriteDetails.EXTRA_URL;
    private DefaultSport defaultSport;

    private TextView textViewTitle;
    private Button buttonOpenRecipe;
    private TextView textViewDescription;
    private ImageView imageViewSport;
    private Button buttonClose;
    private Button buttonDelete;
    private Callback callback;
    private SportDetailsFavoriteDialogPresenter sportDetailsFavoriteDialogPresenter;

    /******************************** INTERFAZ *************************************+/
     *
     */

    public interface Callback {
        void onClickClose();
        void setDataChange(DefaultSport item);
    }

    public void setListener(Callback callback) {
        this.callback = callback;
    }


    /******************************** NEW INSTANCE *************************************+/
     *
     */

    public static SportDetailsFavoriteDialogDialogFragment newInstance(DefaultSport defaultSport) {
        SportDetailsFavoriteDialogDialogFragment fragment = new SportDetailsFavoriteDialogDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable(SPORT, defaultSport);
        fragment.setArguments(args);
        return fragment;
    }

    /******************************** GET ARGUMENTS *************************************+/
     *
     */

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.sportDetailsFavoriteDialogPresenter = new SportDetailsFavoriteDialogPresenter(this);

        if (this.sportDetailsFavoriteDialogPresenter.checkInternetInDevice(getContext())) {
            Fabric.with(getContext(),new Crashlytics());
        }

        this.defaultSport = getArguments().getParcelable(SPORT);
        this.sportDetailsFavoriteDialogPresenter.setSportFavorite(this.defaultSport);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.sport_favorite_dialog_fragment, container, false);
        view.setBackgroundResource(R.drawable.corner_dialog_fragment);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        this.findById(view);
        this.putData();
        this.onClickButtonOpenYoutube();
        this.onClickCloseButton();
        this.onClickButtonDelete();

        return view;
    }

    /******************************** CONFIGURA VISTA *************************************+/
     *
     */

    public void findById(View v) {
        this.textViewTitle = v.findViewById(R.id.textTitleSport);
        this.buttonOpenRecipe = v.findViewById(R.id.open_youtube_sport);
        this.textViewDescription = v.findViewById(R.id.textviewDescriptionSport);
        this.imageViewSport = v.findViewById(R.id.imageViewSport);
        this.buttonClose = v.findViewById(R.id.close_favorite_sport);
        this.buttonDelete = v.findViewById(R.id.delete_favorite_sport);
    }

    private void putData() {
        this.textViewTitle.setText(this.defaultSport.getName());
        this.textViewDescription.setText(this.defaultSport.getDescription());
        Utils.loadImage(this.defaultSport.getPhoto(), this.imageViewSport, Utils.PLACEHOLDER_GALLERY);
    }

    /******************************** ABRE EL VIDEO EN LA ACTIVITY DE YOUTUBE *************************************+/
     *
     */

    private void onClickButtonOpenYoutube() {
        if (this.sportDetailsFavoriteDialogPresenter.checkInternetInDevice(getContext())) {
            this.buttonOpenRecipe.setOnClickListener(v -> {
                Intent intent = new Intent(getContext(), YoutubeActivity.class);
                intent.putExtra(URL, this.defaultSport.getVideo());
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            });
        } else {
            this.buttonOpenRecipe.setEnabled(false);
        }

    }

    /******************************** CIERRA LA PANTALLA *************************************+/
     *
     */

    private void onClickCloseButton(){
        this.buttonClose.setOnClickListener(v -> {
            if (callback != null) {
                this.callback.onClickClose();
            }
        });
    }

    /******************************** BORRAR DE FAVORITOS *************************************+/
     *
     */

    private void onClickButtonDelete(){
        if (this.sportDetailsFavoriteDialogPresenter.checkInternetInDevice(getContext())) {
            this.buttonDelete.setOnClickListener(v -> {
                if (this.callback != null) {
                    this.sportDetailsFavoriteDialogPresenter.onClickDeleleSport();
                    dismiss();
                }
            });
        } else {
            this.buttonDelete.setEnabled(false);
        }

    }

    /******************************** CALLBACK DEL PRESENTER *************************************+/
     *
     */

    @Override
    public void deviceOfflineMessage() {
        Toast.makeText(getContext(),getString(R.string.info_network_device),Toast.LENGTH_LONG).show();
    }

    @Override
    public void deleteSportFavorite() {
        if (this.callback != null) {
            this.callback.setDataChange(this.defaultSport);
        }
    }
}
