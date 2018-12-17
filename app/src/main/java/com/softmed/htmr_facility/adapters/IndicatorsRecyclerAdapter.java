package com.softmed.htmr_facility.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bignerdranch.android.multiselector.MultiSelector;
import com.bignerdranch.android.multiselector.SwappingHolder;

import java.util.ArrayList;
import java.util.List;

import com.softmed.htmr_facility.R;
import com.softmed.htmr_facility.dom.objects.ReferralIndicator;

/**
 * Created by issy on 22/01/2018.
 *
 * @issyzac issyzac.iz@gmail.com
 * On Project HFReferralApp
 */

public class IndicatorsRecyclerAdapter  extends RecyclerView.Adapter<IndicatorsRecyclerAdapter.IndicatorsViewHolder> {

    private List<ReferralIndicator> indicators = new ArrayList<>();
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private MultiSelector multiSelector = new MultiSelector();

    // data is passed into the constructor
    public IndicatorsRecyclerAdapter(Context context, List<ReferralIndicator> items) {
        this.mInflater = LayoutInflater.from(context);
        this.indicators = items;
    }

    // inflates the cell layout from xml when needed
    @Override
    public IndicatorsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.indicator_grid_item, parent, false);
        return new IndicatorsViewHolder(view);
    }

    // binds the data to the textview in each cell
    @Override
    public void onBindViewHolder(IndicatorsViewHolder holder, int position) {
        ReferralIndicator indicator = indicators.get(position);
        holder.indicatorName.setText(indicator.getIndicatorName());
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return indicators.size();
    }


    // stores and recycles views as they are scrolled off screen
    class IndicatorsViewHolder extends SwappingHolder
            implements View.OnClickListener{

        public TextView indicatorName;
        private ReferralIndicator referralIndicator;

        public IndicatorsViewHolder(View itemView) {
            super(itemView, multiSelector);
            indicatorName = (TextView) itemView.findViewById(R.id.indicator_name);
            itemView.setOnClickListener(this);
        }

        public void bindIndicator(ReferralIndicator indicator){
            this.referralIndicator = indicator;
            indicatorName.setText(referralIndicator.getIndicatorName());
        }

        @Override
        public void onClick(View view) {
            if (!multiSelector.isSelectable()) { // (3)
                multiSelector.setSelectable(true); // (4)
                multiSelector.setSelected(IndicatorsViewHolder.this, true); // (5)
            }

        }

    }


    // convenience method for getting data at click position
    ReferralIndicator getItem(int id) {
        return indicators.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}