package com.utad.david.planfit.Fragments.FragmentsMainMenuActivity.Sport;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.utad.david.planfit.Adapter.Sport.SportGainVolumeAdapter;
import com.utad.david.planfit.Data.Firebase.FirebaseAdmin;
import com.utad.david.planfit.Data.SessionUser;
import com.utad.david.planfit.DialogFragment.Sport.SportDetailsDialogFragment;
import com.utad.david.planfit.Model.Sport.SportGainVolume;
import com.utad.david.planfit.R;

import java.util.List;

public class SportGainVolumeFragment extends Fragment implements FirebaseAdmin.FirebaseAdminDownloandFragmentData {

    public SportGainVolumeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SessionUser.getInstance().firebaseAdmin.setFirebaseAdminDownloandFragmentData(this);
    }

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sport_recycleview, container, false);
        SessionUser.getInstance().firebaseAdmin.downloadGainVolumeSport();
        mRecyclerView = view.findViewById(R.id.recycler_view_sport);
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
    public void downloandCollectionSportGainVolume(boolean end) {
        if(end){
            List<SportGainVolume> sportGainVolumes = SessionUser.getInstance().firebaseAdmin.sportGainVolumeListSport;
            mAdapter = new SportGainVolumeAdapter(sportGainVolumes, new SportGainVolumeAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(SportGainVolume item) {
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    Fragment prev = getFragmentManager().findFragmentByTag("dialog");
                    if (prev != null) {
                        transaction.remove(prev);
                    }
                    transaction.addToBackStack(null);
                    SportDetailsDialogFragment newFragment = SportDetailsDialogFragment.newInstanceGainVolume(item,2);
                    newFragment.show(transaction, "dialog");
                }
            });
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    @Override
    public void downloandCollectionSportSlimming(boolean end) {
    }

    @Override
    public void downloandCollectionSportToning(boolean end) {
    }

    @Override
    public void downloandCollectionNutritionSlimming(boolean end) {

    }

    @Override
    public void downloandCollectionNutritionToning(boolean end) {

    }

    @Override
    public void downloandCollectionNutritionGainVolume(boolean end) {

    }
}
