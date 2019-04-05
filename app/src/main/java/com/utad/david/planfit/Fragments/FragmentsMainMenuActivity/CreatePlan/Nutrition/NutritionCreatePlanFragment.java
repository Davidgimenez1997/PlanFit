package com.utad.david.planfit.Fragments.FragmentsMainMenuActivity.CreatePlan.Nutrition;

import android.app.ProgressDialog;
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
import butterknife.ButterKnife;
import com.crashlytics.android.Crashlytics;
import com.utad.david.planfit.Adapter.Plan.Create.Nutrition.CreateNutritionPlanAdapter;
import com.utad.david.planfit.Data.Firebase.FirebaseAdmin;
import com.utad.david.planfit.Data.SessionUser;
import com.utad.david.planfit.DialogFragment.Plan.Nutrition.CreateNutritionPlanDetailsDialogFragment;
import com.utad.david.planfit.Model.Nutrition.DefaultNutrition;
import com.utad.david.planfit.R;
import io.fabric.sdk.android.Fabric;

import java.util.List;

public class NutritionCreatePlanFragment extends Fragment implements FirebaseAdmin.FirebaseAdminFavoriteNutrition,CreateNutritionPlanDetailsDialogFragment.CallbackCreateNutrtion{

    public NutritionCreatePlanFragment() {
    }

    private NutritionCreatePlanFragment fragment;

    public NutritionCreatePlanFragment newInstanceSlimming() {
        this.fragment = this;
        return this.fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(getContext(), new Crashlytics());

    }

    @Override
    public void onStart() {
        super.onStart();
        SessionUser.getInstance().firebaseAdmin.setFirebaseAdminFavoriteNutrition(this);
        SessionUser.getInstance().firebaseAdmin.downloadAllNutritionFavorite();
    }

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private LinearLayout linearLayout;
    private CreateNutritionPlanDetailsDialogFragment newFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View  view =  inflater.inflate(R.layout.fragment_nutrition_create_plan, container, false);
        showLoading();
        mRecyclerView = view.findViewById(R.id.recycler_view_nutrition);
        linearLayout = view.findViewById(R.id.linear_empty_favorites);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(getContext(), 2);
        mRecyclerView.setLayoutManager(mLayoutManager);
        hideLoading();
        return view;
    }

    @Override
    public void onClickClose() {
        newFragment.dismiss();
    }

    @Override
    public void downloandCollectionNutritionFavorite(boolean end) {
        if(end==true){
            hideLoading();
            linearLayout.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            List<DefaultNutrition> allFavoriteFavorite = SessionUser.getInstance().firebaseAdmin.allNutritionFavorite;
            mAdapter = new CreateNutritionPlanAdapter(allFavoriteFavorite, new CreateNutritionPlanAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(DefaultNutrition item) {
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    Fragment prev = getFragmentManager().findFragmentByTag("dialog");
                    if (prev != null) {
                        transaction.remove(prev);
                    }
                    transaction.addToBackStack(null);
                    newFragment = CreateNutritionPlanDetailsDialogFragment.newInstance(item);
                    newFragment.setListener(fragment);
                    newFragment.show(transaction, "dialog");
                }
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
    public void inserNutritionFavoriteFirebase(boolean end) {}

    @Override
    public void deleteFavoriteNutrition(boolean end) {}
}
