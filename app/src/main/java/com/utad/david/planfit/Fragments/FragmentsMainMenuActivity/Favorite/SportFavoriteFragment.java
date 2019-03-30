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
import com.utad.david.planfit.Adapter.Favorite.Sport.SportFavoriteAdapter;
import com.utad.david.planfit.Data.Firebase.FirebaseAdmin;
import com.utad.david.planfit.Data.SessionUser;
import com.utad.david.planfit.DialogFragment.Favorite.SportFavoriteDetailsDialogFragment;
import com.utad.david.planfit.Model.Sport.DefaultSport;
import com.utad.david.planfit.R;

import java.util.List;

public class SportFavoriteFragment extends Fragment implements FirebaseAdmin.FirebaseAdminFavoriteSport, SportFavoriteDetailsDialogFragment.CallbackFavoriteSport{

    public SportFavoriteFragment() {
        // Required empty public constructor
    }

    private SportFavoriteFragment fragment;

    public SportFavoriteFragment newInstanceSlimming() {
        this.fragment = this;
        return this.fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        SessionUser.getInstance().firebaseAdmin.setFirebaseAdminFavoriteSport(this);
        SessionUser.getInstance().firebaseAdmin.downloadAllSportFavorite();
    }

    private RecyclerView mRecyclerView;
    private SportFavoriteAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private LinearLayout linearLayout;
    private SportFavoriteDetailsDialogFragment newFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sport_favorite, container, false);

        showLoading();
        mRecyclerView = view.findViewById(R.id.recycler_view_sport);
        linearLayout = view.findViewById(R.id.linear_empty_favorites);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(getContext(), 2);
        mRecyclerView.setLayoutManager(mLayoutManager);
        hideLoading();

        return view;
    }

    @Override
    public void downloandCollectionSportFavorite(boolean end) {
        if(end==true){
            hideLoading();
            linearLayout.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            List<DefaultSport> allSportFavorite = SessionUser.getInstance().firebaseAdmin.allSportFavorite;

            mAdapter = new SportFavoriteAdapter(allSportFavorite, new SportFavoriteAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(DefaultSport item) {
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    Fragment prev = getFragmentManager().findFragmentByTag("dialog");
                    if (prev != null) {
                        transaction.remove(prev);
                    }
                    transaction.addToBackStack(null);
                    newFragment = SportFavoriteDetailsDialogFragment.newInstance(item);
                    newFragment.setListener(fragment);
                    newFragment.show(transaction, "dialog");
                }
            });

            mRecyclerView.setAdapter(mAdapter);

        }
    }

    @Override
    public void emptyCollectionSportFavorite(boolean end) {
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
            mAdapter.dataChangedDeleteSport(SessionUser.getInstance().firebaseAdmin.allSportFavorite);
            if(SessionUser.getInstance().firebaseAdmin.allSportFavorite.size()==0){
                linearLayout.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.GONE);
            }
            Toast.makeText(getContext(),"Favorito borrado correctamente",Toast.LENGTH_LONG).show();
            hideLoading();
        }
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
    public void inserSportFavoriteFirebase(boolean end) {}

    @Override
    public void deleteFavoriteSport(boolean end) {}
}
