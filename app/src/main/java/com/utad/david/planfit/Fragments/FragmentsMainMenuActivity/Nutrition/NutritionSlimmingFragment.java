package com.utad.david.planfit.Fragments.FragmentsMainMenuActivity.Nutrition;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.utad.david.planfit.Adapter.Nutrition.NutritionSlimmingAdapter;
import com.utad.david.planfit.Adapter.Sport.SportSlimmingAdapter;
import com.utad.david.planfit.Data.Firebase.FirebaseAdmin;
import com.utad.david.planfit.Data.SessionUser;
import com.utad.david.planfit.DialogFragment.Nutrition.NutritionDetailsDialogFragment;
import com.utad.david.planfit.DialogFragment.Sport.SportDetailsDialogFragment;
import com.utad.david.planfit.Model.Nutrition.NutritionSlimming;
import com.utad.david.planfit.Model.Sport.SportSlimming;
import com.utad.david.planfit.R;

import java.util.List;

public class NutritionSlimmingFragment extends Fragment implements FirebaseAdmin.FirebaseAdminDownloandFragmentData ,NutritionDetailsDialogFragment.CallbackNutrition{

    public NutritionSlimmingFragment() {
        // Required empty public constructor
    }

    private NutritionSlimmingFragment fragment;

    public NutritionSlimmingFragment newInstanceSlimming() {
        this.fragment = this;
        return this.fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SessionUser.getInstance().firebaseAdmin.setFirebaseAdminDownloandFragmentData(this);
    }

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private NutritionDetailsDialogFragment newFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_nutrition_recycleview, container, false);
        SessionUser.getInstance().firebaseAdmin.downloadSlimmingNutrition();
        mRecyclerView = view.findViewById(R.id.recycler_view_nutrition);
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


    @Override
    public void downloandCollectionNutritionSlimming(boolean end) {
        if(end){
            List<NutritionSlimming> nutritionSlimmingList = SessionUser.getInstance().firebaseAdmin.nutritionSlimmingListNutrition;
            mAdapter = new NutritionSlimmingAdapter(nutritionSlimmingList, new NutritionSlimmingAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(NutritionSlimming item) {
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    Fragment prev = getFragmentManager().findFragmentByTag("dialog");
                    if (prev != null) {
                        transaction.remove(prev);
                    }
                    transaction.addToBackStack(null);
                    newFragment = NutritionDetailsDialogFragment.newInstanceSlimming(item,0);
                    newFragment.setCallbackNutrition(fragment);
                    newFragment.show(transaction, "dialog");
                }
            });
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    @Override
    public void onClickClose() {
        newFragment.dismiss();
    }

    @Override
    public void downloandCollectionSportSlimming(boolean end) {

    }

    @Override
    public void downloandCollectionSportToning(boolean end) {

    }

    @Override
    public void downloandCollectionSportGainVolume(boolean end) {

    }

    @Override
    public void downloandCollectionNutritionToning(boolean end) {

    }

    @Override
    public void downloandCollectionNutritionGainVolume(boolean end) {

    }
}

