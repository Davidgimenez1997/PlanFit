package com.utad.david.planfit.DialogFragment.Favorite;

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
import com.utad.david.planfit.Data.Firebase.FirebaseAdmin;
import com.utad.david.planfit.Data.SessionUser;
import com.utad.david.planfit.Model.Sport.DefaultSport;
import com.utad.david.planfit.R;
import com.utad.david.planfit.Utils.Constants;
import com.utad.david.planfit.Utils.Utils;
import com.utad.david.planfit.Utils.UtilsNetwork;
import io.fabric.sdk.android.Fabric;

public class SportFavoriteDetailsDialogFragment extends BaseDialogFragment implements FirebaseAdmin.FirebaseAdminFavoriteSport {

    /******************************** VARIABLES *************************************+/
     *
     */

    private static String SPORT = Constants.DeportesFavoriteDetails.EXTRA_SPORT;
    private static String URL = Constants.DeportesFavoriteDetails.EXTRA_URL;
    private DefaultSport defaultSport;

    private TextView textViewTitle;
    private Button buttonOpenRecipe;
    private TextView textViewDescription;
    private ImageView imageViewSport;
    private Button buttonClose;
    private Button buttonDelete;
    private Callback listener;

    /******************************** INTERFAZ *************************************+/
     *
     */

    public interface Callback {
        void onClickClose();
        void setDataChange();
    }

    public void setListener(Callback listener) {
        this.listener = listener;
    }


    /******************************** NEW INSTANCE *************************************+/
     *
     */

    public static SportFavoriteDetailsDialogFragment newInstance(DefaultSport defaultSport) {
        SportFavoriteDetailsDialogFragment fragment = new SportFavoriteDetailsDialogFragment();
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

        if(UtilsNetwork.checkConnectionInternetDevice(getContext())){
            Fabric.with(getContext(),new Crashlytics());
            SessionUser.getInstance().firebaseAdmin.setFirebaseAdminFavoriteSport(this);
        }else{
            Toast.makeText(getContext(),getString(R.string.info_network_device),Toast.LENGTH_LONG).show();
        }

        defaultSport = getArguments().getParcelable(SPORT);
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
        onClickButtonDelete();

        return view;
    }

    /******************************** CONFIGURA VISTA *************************************+/
     *
     */

    public void findById(View v) {
        textViewTitle = v.findViewById(R.id.textTitleSport);
        buttonOpenRecipe = v.findViewById(R.id.open_youtube_sport);
        textViewDescription = v.findViewById(R.id.textviewDescriptionSport);
        imageViewSport = v.findViewById(R.id.imageViewSport);
        buttonClose = v.findViewById(R.id.close_favorite_sport);
        buttonDelete = v.findViewById(R.id.delete_favorite_sport);
    }

    private void putData() {
        textViewTitle.setText(defaultSport.getName());
        textViewDescription.setText(defaultSport.getDescription());
        Utils.loadImage(defaultSport.getPhoto(),imageViewSport,Utils.PLACEHOLDER_GALLERY);
    }

    /******************************** ABRE EL VIDEO EN LA ACTIVITY DE YOUTUBE *************************************+/
     *
     */

    private void onClickButtonOpenYoutube() {
        if(UtilsNetwork.checkConnectionInternetDevice(getContext())){
            buttonOpenRecipe.setOnClickListener(v -> {
                Intent intent = new Intent(getContext(), YoutubeActivity.class);
                intent.putExtra(URL, defaultSport.getVideo());
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            });
        }else{
            buttonOpenRecipe.setEnabled(false);
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

    /******************************** BORRAR DE FAVORITOS *************************************+/
     *
     */

    private void onClickButtonDelete(){
        if(UtilsNetwork.checkConnectionInternetDevice(getContext())){
            buttonDelete.setOnClickListener(v -> {
                if(listener!=null){
                    SessionUser.getInstance().firebaseAdmin.deleteDefaultSportFavorite(defaultSport);
                    dismiss();
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
    public void deleteFavoriteSport(boolean end) {
        if(end==true){
            if(listener!=null){
                listener.setDataChange();
            }
        }
    }

    @Override
    public void downloandCollectionSportFavorite(boolean end) {}
    @Override
    public void inserSportFavoriteFirebase(boolean end) {}
    @Override
    public void emptyCollectionSportFavorite(boolean end) {}
}
