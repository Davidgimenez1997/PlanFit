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
        this.timeStart = v.findViewById(R.id.timeStart);
    }

    public void setData(ArrayList<PlanSport> planSport) {
        for (int i = 0; i < planSport.size(); i++) {
            String aux = String.valueOf(planSport.get(i).getTimeStart());
            BigDecimal bigDecimal = new BigDecimal(aux);
            long longValue = bigDecimal.longValue();
            BigDecimal bigDecimal1 = bigDecimal.remainder(BigDecimal.ONE);
            StringBuilder stringBuilder = new StringBuilder(bigDecimal1.toString());
            stringBuilder.delete(0, 2);
            this.setTimeStart(stringBuilder, longValue);
        }
    }

    private void setTimeStart(StringBuilder stringBuilder, long value) {
        String result = Long.valueOf(value) + ":" + stringBuilder.toString();
        if (stringBuilder.toString().length() == 1) {
            String aux = result + "0";
            this.timeStart.setText(aux);
        } else {
            this.timeStart.setText(result);
        }
    }
}
