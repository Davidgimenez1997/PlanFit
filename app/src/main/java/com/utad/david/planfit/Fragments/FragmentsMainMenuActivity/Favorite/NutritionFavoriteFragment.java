package com.utad.david.planfit.Fragments.FragmentsMainMenuActivity.Favorite;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import butterknife.ButterKnife;
import com.crashlytics.android.Crashlytics;
import com.utad.david.planfit.Adapter.Favorite.Nutrition.NutritionFavoriteAdapter;
import com.utad.david.planfit.Data.Firebase.FirebaseAdmin;
import com.utad.david.planfit.Data.SessionUser;
import com.utad.david.planfit.DialogFragment.Favorite.NutritionFavoriteDetailsDialogFragment;
import com.utad.david.planfit.Model.Nutrition.DefaultNutrition;
import com.utad.david.planfit.R;
import com.utad.david.planfit.Utils.UtilsNetwork;
import io.fabric.sdk.android.Fabric;

import java.util.Collections;
import java.util.List;

public class NutritionFavoriteFragment extends Fragment implements FirebaseAdmin.FirebaseAdminFavoriteNutrition,
        NutritionFavoriteDetailsDialogFragment.CallbackNutritionFavorite {

    public NutritionFavoriteFragment() {
        // Required empty public constructor
    }

    private NutritionFavoriteFragment fragment;

    public NutritionFavoriteFragment newInstanceSlimming() {
        this.fragment = this;
        return this.fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(UtilsNetwork.checkConnectionInternetDevice(getContext())){
            showLoading();
            SessionUser.getInstance().firebaseAdmin.setFirebaseAdminFavoriteNutrition(this);
            SessionUser.getInstance().firebaseAdmin.downloadAllNutritionFavorite();
            Fabric.with(getContext(), new Crashlytics());
        }else{
            hideLoading();
            Toast.makeText(getContext(),getString(R.string.info_network_device),Toast.LENGTH_LONG).show();
        }

    }


    private RecyclerView mRecyclerView;
    private NutritionFavoriteAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private LinearLayout linearLayout;
    private NutritionFavoriteDetailsDialogFragment newFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_nutrition_favorite, container, false);

        mRecyclerView = view.findViewById(R.id.recycler_view_nutrition);
        linearLayout = view.findViewById(R.id.linear_empty_favorites);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(getContext(), 2);
        mRecyclerView.setLayoutManager(mLayoutManager);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private ProgressDialog progressDialog;

    public void showLoading() {
        if (progressDialog != null && progressDialog.isShowing()) {
            return;
        }
        progressDialog = new ProgressDialog(getContext(), R.style.TransparentProgressDialog);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.setCancelable(false);
        RotateAnimation rotate = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setInterpolator(new LinearInterpolator());
        rotate.setDuration(1000);
        rotate.setRepeatCount(Animation.INFINITE);
        ImageView ivLoading = ButterKnife.findById(progressDialog, R.id.image_cards_animation);
        ivLoading.startAnimation(rotate);
        progressDialog.show();
    }

    public void hideLoading() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        hideLoading();
    }

    @Override
    public void onPause() {
        super.onPause();
        hideLoading();
    }

    @Override
    public void downloandCollectionNutritionFavorite(boolean end) {
        if(end==true){
            hideLoading();
            linearLayout.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);

            List<DefaultNutrition> allFavoriteFavorite = SessionUser.getInstance().firebaseAdmin.allNutritionFavorite;

            Collections.sort(allFavoriteFavorite);

            mAdapter = new NutritionFavoriteAdapter(allFavoriteFavorite, item -> {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                Fragment prev = getFragmentManager().findFragmentByTag("dialog");
                if (prev != null) {
                    transaction.remove(prev);
                }
                transaction.addToBackStack(null);
                newFragment = NutritionFavoriteDetailsDialogFragment.newInstance(item);
                newFragment.setListener(fragment);
                newFragment.show(transaction, "dialog");
            });
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    @Override
    public void emptyCollectionNutritionFavorite(boolean end) {
        if(end==true){
            hideLoading();
            linearLayout.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClickClose() {
        newFragment.dismiss();
    }

    @Override
    public void setDataChange() {
        if(mAdapter!=null){
            showLoading();
            mAdapter.dataChangedDeleteSport(SessionUser.getInstance().firebaseAdmin.allNutritionFavorite);
            if(SessionUser.getInstance().firebaseAdmin.allNutritionFavorite.size()==0){
                linearLayout.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.GONE);
            }
            Toast.makeText(getContext(),"Favorito borrado correctamente",Toast.LENGTH_LONG).show();
            hideLoading();
        }
    }

    @Override
    public void inserNutritionFavoriteFirebase(boolean end) {}

    @Override
    public void deleteFavoriteNutrition(boolean end) {}
}
