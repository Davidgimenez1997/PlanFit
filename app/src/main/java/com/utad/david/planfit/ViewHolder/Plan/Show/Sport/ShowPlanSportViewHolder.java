package com.utad.david.planfit.ViewHolder.Plan.Show.Sport;

import android.view.View;
import android.widget.TextView;

import com.utad.david.planfit.Base.BaseViewHolder;
import com.utad.david.planfit.Model.Plan.PlanSport;
import com.utad.david.planfit.R;

import java.math.BigDecimal;
import java.util.ArrayList;

public class ShowPlanSportViewHolder extends BaseViewHolder {
    private TextView timeStart;

    public ShowPlanSportViewHolder(View v) {
        super(v);
        timeStart = v.findViewById(R.id.timeStart);
    }

    public void setData(ArrayList<PlanSport> planSport){

        for(int i=0;i<planSport.size();i++){

            String str_timeStart = String.valueOf(planSport.get(i).getTimeStart());
            BigDecimal bigDecimal_start = new BigDecimal(str_timeStart);
            long first_start = bigDecimal_start.longValue();
            BigDecimal second_start = bigDecimal_start.remainder(BigDecimal.ONE);
            StringBuilder stringBuilder_start = new StringBuilder(second_start.toString());
            stringBuilder_start.delete(0,2);

            if(stringBuilder_start.toString().length()==1){
                timeStart.setText(Long.valueOf(first_start)+":"+stringBuilder_start.toString()+"0");
            }else{
                timeStart.setText(Long.valueOf(first_start)+":"+stringBuilder_start.toString());
            }
        }

    }
}
