package com.utad.david.planfit.DialogFragment.Plan;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.utad.david.planfit.Model.Nutrition.DefaultNutrition;
import com.utad.david.planfit.R;

public class CreateNutritionPlanDetailsDialogFragment extends DialogFragment {

    private static String NUTRITION = "NUTRITION";
    private DefaultNutrition defaultNutrition;

    public static CreateNutritionPlanDetailsDialogFragment newInstance(DefaultNutrition defaultNutrition) {
        CreateNutritionPlanDetailsDialogFragment fragment = new CreateNutritionPlanDetailsDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable(NUTRITION, defaultNutrition);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        defaultNutrition = getArguments().getParcelable(NUTRITION);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.create_nutrition_plan_details_dialog_fragment, container, false);
        view.setBackgroundResource(R.drawable.corner_dialog_fragment);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        return view;
    }
}