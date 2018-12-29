package com.utad.david.planfit.Fragments.FragmentsMainMenuActivity.Sport;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.utad.david.planfit.Adapter.SlimmingAdapter;
import com.utad.david.planfit.Data.Firebase.FirebaseAdmin;
import com.utad.david.planfit.Data.SessionUser;
import com.utad.david.planfit.Model.Slimming;
import com.utad.david.planfit.R;

import java.util.List;

public class SlimmingFragment extends Fragment implements FirebaseAdmin.FirebaseAdminDownloandFragmentData {

    public SlimmingFragment() {
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
       View view = inflater.inflate(R.layout.fragment_slimming, container, false);
        SessionUser.getInstance().firebaseAdmin.downloadSlimmingSport();
        mRecyclerView = view.findViewById(R.id.recycler_view_slimming);
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
    public void downloandCollectionSportSlimming(boolean end) {
        if(end){
            List<Slimming> slimmingList = SessionUser.getInstance().firebaseAdmin.slimmingListSport;
            mAdapter = new SlimmingAdapter(slimmingList);
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    @Override
    public void downloandCollectionSportToning(boolean end) {

    }
}
