package com.utad.david.planfit.ViewHolder.Plan.Show.Sport.Details;

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
        this.name = v.findViewById(R.id.titleSport);
        this.photo = v.findViewById(R.id.imageViewShowSport);
        this.timeEnd = v.findViewById(R.id.timeEnd);
        this.timeStart = v.findViewById(R.id.timeStart);
        this.imageViewCheck = v.findViewById(R.id.imageViewCheck);
    }

    public void setData(PlanSport planSport){
        this.name.setText(planSport.getName());
        Utils.loadImage(planSport.getPhoto(), this.photo, Utils.PLACEHOLDER_GALLERY);
        this.setTime(planSport.getTimeStart(), this.timeStart);
        this.setTime(planSport.getTimeEnd(), this.timeEnd);
        this.setImageView(planSport.getIsOk());
    }

    private void setTime(double time, TextView textView) {
        String aux = String.valueOf(time);
        BigDecimal bigDecimal = new BigDecimal(aux);
        long longValue = this.getLongValue(bigDecimal);
        BigDecimal bigDecimal1 = bigDecimal.remainder(BigDecimal.ONE);
        StringBuilder stringBuilder = this.createStringBuilder(bigDecimal1);
        this.checkSetTime(textView, stringBuilder, longValue);
    }

    private Long getLongValue(BigDecimal bigDecimal) {
        return bigDecimal.longValue();
    }

    private StringBuilder createStringBuilder(BigDecimal bigDecimal) {
        StringBuilder stringBuilder = new StringBuilder(bigDecimal.toString());
        stringBuilder.delete(0, 2);
        return stringBuilder;
    }

    private void checkSetTime(TextView editText, StringBuilder stringBuilder, Long value) {
        String result = Long.valueOf(value) + ":" + stringBuilder.toString();
        if (stringBuilder.toString().length() == 1) {
            String aux = result + "0";
            editText.setText(aux);
        } else {
            editText.setText(result);
        }
    }

    private void setImageView (String isOk) {
        if (isOk.equals(Constants.ModePlan.YES)) {
            this.imageViewCheck.setVisibility(View.VISIBLE);
        } else if(isOk.equals(Constants.ModePlan.NO)) {
            this.imageViewCheck.setVisibility(View.INVISIBLE);
        }

    }
}
