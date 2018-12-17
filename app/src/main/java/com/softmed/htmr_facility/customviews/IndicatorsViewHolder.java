package com.softmed.htmr_facility.customviews;

import android.view.View;
import android.widget.TextView;

import com.bignerdranch.android.multiselector.SwappingHolder;
import com.softmed.htmr_facility.R;


/**
 * Created by issy on 22/01/2018.
 *
 * @issyzac issyzac.iz@gmail.com
 * On Project HFReferralApp
 */

public class IndicatorsViewHolder extends SwappingHolder
        implements View.OnClickListener{

    public TextView indicatorName;

    public IndicatorsViewHolder(View itemView) {
        super(itemView);
        indicatorName = (TextView) itemView.findViewById(R.id.indicator_name);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
//        if (!multiSelector.isSelectable()) { // (3)
//            IssueReferralDialogueFragment.multiSelector.setSelectable(true); // (4)
//            multiSelector.setSelected(IndicatorsViewHolder.this, true); // (5)
//        }
    }

}