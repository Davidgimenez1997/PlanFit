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
import com.utad.david.planfit.Adapter.Nutrition.NutritionGainVolumeAdapter;
import com.utad.david.planfit.Data.Firebase.FirebaseAdmin;
import com.utad.david.planfit.Data.SessionUser;
import com.utad.david.planfit.DialogFragment.Nutrition.NutritionDetailsDialogFragment;
import com.utad.david.planfit.Model.Nutrition.NutritionGainVolume;
import com.utad.david.planfit.R;

import java.util.List;

public class NutritionGainVolumeFragment extends Fragment implements FirebaseAdmin.FirebaseAdminDownloandFragmentData,NutritionDetailsDialogFragment.CallbackNutrition {

    public NutritionGainVolumeFragment() {
        // Required empty public constructor
    }

    private NutritionGainVolumeFragment fragment;

    public NutritionGainVolumeFragment newInstanceSlimming() {
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
        SessionUser.getInstance().firebaseAdmin.downloadGainVolumeNutrition();
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
    public void downloandCollectionNutritionGainVolume(boolean end) {
        if(end){
            List<NutritionGainVolume> nutritionGainVolumes = SessionUser.getInstance().firebaseAdmin.nutritionGainVolumeListNutrition;
            mAdapter = new NutritionGainVolumeAdapter(nutritionGainVolumes, new NutritionGainVolumeAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(NutritionGainVolume item) {
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    Fragment prev = getFragmentManager().findFragmentByTag("dialog");
                    if (prev != null) {
                        transaction.remove(prev);
                    }
                    transaction.addToBackStack(null);
                    newFragment = NutritionDetailsDialogFragment.newInstanceGainVolume(item,2);
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
    public void downloandCollectionSportGainVolume(boolean end) {

    }

    @Override
    public void downloandCollectionNutritionSlimming(boolean end) {

    }

    @Override
    public void downloandCollectionNutritionToning(boolean end) {
    }

    @Override
    public void downloandCollectionSportSlimming(boolean end) {
    }

    @Override
    public void downloandCollectionSportToning(boolean end) {

    }
}
