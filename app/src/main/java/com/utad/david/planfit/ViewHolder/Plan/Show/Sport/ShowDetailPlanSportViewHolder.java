package com.utad.david.planfit.ViewHolder.Plan.Show.Sport;

import android.view.View;
import android.widget.EditText;
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
        this.setTimeStart(planSport);
        this.setTimeEnd(planSport);
        this.setImageView(planSport);

    }

    private void setTimeStart(PlanSport planSport) {
        String str_timeStart = String.valueOf(planSport.getTimeStart());
        BigDecimal bigDecimal_start = new BigDecimal(str_timeStart);
        long first_start = bigDecimal_start.longValue();
        BigDecimal second_start = bigDecimal_start.remainder(BigDecimal.ONE);
        StringBuilder stringBuilder_start = new StringBuilder(second_start.toString());
        stringBuilder_start.delete(0,2);
        this.checkSetTime(this.timeStart, stringBuilder_start, first_start);
    }

    private void setTimeEnd(PlanSport planSport) {
        String str_timeEnd = String.valueOf(planSport.getTimeEnd());
        BigDecimal bigDecimal_end = new BigDecimal(str_timeEnd);
        long first_end = bigDecimal_end.longValue();
        BigDecimal second_End = bigDecimal_end.remainder(BigDecimal.ONE);
        StringBuilder stringBuilder_end = new StringBuilder(second_End.toString());
        stringBuilder_end.delete(0,2);

        this.checkSetTime(this.timeEnd, stringBuilder_end, first_end);


    }

    private void checkSetTime(TextView editText, StringBuilder stringBuilder, Long value) {
        if(stringBuilder.toString().length()==1){
            editText.setText(Long.valueOf(value)+":"+stringBuilder.toString()+"0");
        }else{
            editText.setText(Long.valueOf(value)+":"+stringBuilder.toString());
        }
    }

    private void setImageView (PlanSport planSport) {
        if(planSport.getIsOk().equals(Constants.ModePlan.YES)){
            imageViewCheck.setVisibility(View.VISIBLE);
        }else if(planSport.getIsOk().equals(Constants.ModePlan.NO)){
            imageViewCheck.setVisibility(View.INVISIBLE);
        }

    }
}
