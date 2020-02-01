package com.utad.david.planfit.Fragments.FragmentsMainMenuActivity.Plan.Create.Nutrition;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.crashlytics.android.Crashlytics;
import com.utad.david.planfit.Adapter.Plan.Create.Nutrition.CreateNutritionPlanAdapter;
import com.utad.david.planfit.Base.BaseFragment;
import com.utad.david.planfit.Data.Firebase.FirebaseAdmin;
import com.utad.david.planfit.Data.SessionUser;
import com.utad.david.planfit.DialogFragment.Plan.Nutrition.CreateNutritionPlanDetailsDialogFragment;
import com.utad.david.planfit.Model.Nutrition.DefaultNutrition;
import com.utad.david.planfit.R;
import com.utad.david.planfit.Utils.Constants;
import com.utad.david.planfit.Utils.UtilsNetwork;
import io.fabric.sdk.android.Fabric;
import java.util.Collections;
import java.util.List;

public class NutritionCreatePlanFragment extends BaseFragment
        implements FirebaseAdmin.FirebaseAdminFavoriteNutrition,
        CreateNutritionPlanDetailsDialogFragment.Callback {

    /******************************** VARIABLES *************************************+/
     *
     */

    private NutritionCreatePlanFragment fragment;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private LinearLayout linearLayout;
    private CreateNutritionPlanDetailsDialogFragment newFragment;
    private Runnable toolbarRunnable;

    /******************************** NEW INSTANCE *************************************+/
     *
     */

    public NutritionCreatePlanFragment newInstanceSlimming() {
        this.fragment = this;
        return this.fragment;
    }

    /******************************** SET Runnable *************************************+/
     *
     */

    public void setToolbarRunnable(Runnable toolbarRunnable) {
        this.toolbarRunnable = toolbarRunnable;
    }


    /******************************** SET CALLBACK FIREBASE *************************************+/
     *
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(UtilsNetwork.checkConnectionInternetDevice(getContext())){
            showLoading();
            Fabric.with(getContext(), new Crashlytics());
            SessionUser.getInstance().firebaseAdmin.setFirebaseAdminFavoriteNutrition(this);
            SessionUser.getInstance().firebaseAdmin.downloadAllNutritionFavorite();
        }else{
            hideLoading();
            Toast.makeText(getContext(),getString(R.string.info_network_device),Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View  view =  inflater.inflate(R.layout.fragment_nutrition_create_plan, container, false);


        if(toolbarRunnable != null) {
            toolbarRunnable.run();
        }

        mRecyclerView = view.findViewById(R.id.recycler_view_nutrition);
        linearLayout = view.findViewById(R.id.linear_empty_favorites);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(getContext(), 2);
        mRecyclerView.setLayoutManager(mLayoutManager);

        return view;
    }

    /******************************** CreateNutritionPlanDetailsDialogFragment.Callback *************************************+/
     *
     */

    @Override
    public void onClickClose() {
        newFragment.dismiss();
    }

    /******************************** CALLBACK FIREBASE *************************************+/
     *
     */

    @Override
    public void downloandCollectionNutritionFavorite(boolean end) {
        if(end==true){
            hideLoading();
            linearLayout.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);

            List<DefaultNutrition> allFavoriteFavorite = SessionUser.getInstance().firebaseAdmin.allNutritionFavorite;

            Collections.sort(allFavoriteFavorite);

            mAdapter = new CreateNutritionPlanAdapter(allFavoriteFavorite, item -> {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                Fragment prev = getFragmentManager().findFragmentByTag(Constants.TagDialogFragment.TAG);
                if (prev != null) {
                    transaction.remove(prev);
                }
                transaction.addToBackStack(null);
                newFragment = CreateNutritionPlanDetailsDialogFragment.newInstance(item);
                newFragment.setListener(fragment);
                newFragment.show(transaction, Constants.TagDialogFragment.TAG);
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
    public void inserNutritionFavoriteFirebase(boolean end) {}
    @Override
    public void deleteFavoriteNutrition(boolean end) {}
}
