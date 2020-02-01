package com.utad.david.planfit.ViewHolder.Plan.Show.Sport;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.utad.david.planfit.Base.BaseViewHolder;
import com.utad.david.planfit.Model.Plan.PlanSport;
import com.utad.david.planfit.R;
import com.utad.david.planfit.Utils.Constants;
import com.utad.david.planfit.Utils.Utils;

import java.math.BigDecimal;

public class ShowDetailPlanSportViewHolder extends BaseViewHolder {

    private TextView name;
    private TextView timeStart;
    private TextView timeEnd;
    private ImageView photo;
    private ImageView imageViewCheck;

    public ShowDetailPlanSportViewHolder(View v) {
        super(v);
        name = v.findViewById(R.id.titleSport);
        photo = v.findViewById(R.id.imageViewShowSport);
        timeEnd = v.findViewById(R.id.timeEnd);
        timeStart = v.findViewById(R.id.timeStart);
        imageViewCheck = v.findViewById(R.id.imageViewCheck);
    }

    public void setData(PlanSport planSport){
        name.setText(planSport.getName());

        Utils.loadImage(planSport.getPhoto(),photo,Utils.PLACEHOLDER_GALLERY);

        String str_timeStart = String.valueOf(planSport.getTimeStart());
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

        String str_timeEnd = String.valueOf(planSport.getTimeEnd());
        BigDecimal bigDecimal_end = new BigDecimal(str_timeEnd);
        long first_end = bigDecimal_end.longValue();
        BigDecimal second_End = bigDecimal_end.remainder(BigDecimal.ONE);
        StringBuilder stringBuilder_end = new StringBuilder(second_End.toString());
        stringBuilder_end.delete(0,2);

        if(stringBuilder_end.toString().length()==1){
            timeEnd.setText(Long.valueOf(first_end)+":"+stringBuilder_end.toString()+"0");
        }else{
            timeEnd.setText(Long.valueOf(first_end)+":"+stringBuilder_end.toString());
        }

        if(planSport.getIsOk().equals(Constants.ModePlan.YES)){
            imageViewCheck.setVisibility(View.VISIBLE);
        }else if(planSport.getIsOk().equals(Constants.ModePlan.NO)){
            imageViewCheck.setVisibility(View.INVISIBLE);
        }

    }
}
