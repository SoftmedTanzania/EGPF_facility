package com.softmed.htmr_facility.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import com.softmed.htmr_facility.R;
import com.softmed.htmr_facility.activities.FeedbackDetailsActivity;
import com.softmed.htmr_facility.base.AppDatabase;
import com.softmed.htmr_facility.base.BaseActivity;
import com.softmed.htmr_facility.dom.objects.Referral;

/**
 * Created by issy on 12/7/17.
 *
 * @issyzac issyzac.iz@gmail.com
 * On Project HFReferralApp
 */

public class ReferredClientsrecyclerAdapter extends RecyclerView.Adapter <RecyclerView.ViewHolder> {

    private List<Referral> items;
    private Context context;
    private AppDatabase database;
    private ReferredClientsrecyclerAdapter.ListViewItemViewHolder mViewHolder;


    public ReferredClientsrecyclerAdapter(List<Referral> mItems, Context context){
        this.items = mItems;
        this.database = AppDatabase.getDatabase(context);
    }

    public ReferredClientsrecyclerAdapter(){}

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType){
        context         = viewGroup.getContext();
        View itemView   = null;
        itemView = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.referred_clients_list_item, viewGroup, false);

        return new ReferredClientsrecyclerAdapter.ListViewItemViewHolder(itemView);

    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, int itemPosition){

        final Referral referral = getItem(itemPosition);
        ReferredClientsrecyclerAdapter.ListViewItemViewHolder holder = (ReferredClientsrecyclerAdapter.ListViewItemViewHolder) viewHolder;
        mViewHolder = holder;

        new ReferredClientsrecyclerAdapter.patientDetailsTask(database, referral, holder.clientsNames, holder.serviceName).execute();

        if (referral.getReferralStatus() == 0){
            holder.feedbackStatus.setText("Bado");
            holder.feedbackStatus.setTextColor(context.getResources().getColor(R.color.amber_700));
        }else {
            holder.feedbackStatus.setText("Imefanikiwa");
            holder.feedbackStatus.setTextColor(context.getResources().getColor(R.color.green_a700));
        }

        holder.referralReasons.setText(referral.getReferralReason());
        holder.referralDate.setText(BaseActivity.simpleDateFormat.format(referral.getReferralDate()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, FeedbackDetailsActivity.class);
                intent.putExtra("referal", referral);
                context.startActivity(intent);
            }
        });

    }

    public void addItems (List<Referral> referralList){
        this.items = referralList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount(){
        return items.size();
//        return 10;
    }

    private Referral getItem(int position){
        return items.get(position);
    }

    private class ListViewItemViewHolder extends RecyclerView.ViewHolder {

        TextView clientsNames, feedbackStatus, serviceName, referralReasons, referralDate;
        View viewItem;

        public ListViewItemViewHolder(View itemView){
            super(itemView);
            this.viewItem   = itemView;

            clientsNames = (TextView) itemView.findViewById(R.id.client_name);
            feedbackStatus = (TextView) itemView.findViewById(R.id.feedback_status);
            serviceName = (TextView) itemView.findViewById(R.id.service_name);
            referralReasons = (TextView) itemView.findViewById(R.id.referral_reasons);
            referralDate = (TextView) itemView.findViewById(R.id.ref_date);

        }

    }

    private void setNames(String names){
        mViewHolder.clientsNames.setText(names);
    }

    private static class patientDetailsTask extends AsyncTask<Void, Void, Void> {

        String patientNames, serviceNameString;
        AppDatabase db;
        TextView mText, sname;
        Referral ref;

        patientDetailsTask(AppDatabase database, Referral referral, TextView namesInstance, TextView serviceName){
            this.db = database;
            this.ref = referral;
            mText = namesInstance;
            sname = serviceName;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Log.d("reckless", "doing name search backgroundically!");
            patientNames = db.patientModel().getPatientName(ref.getPatient_id());
            serviceNameString = db.servicesModelDao().getServiceName(ref.getReferralSource());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.d("reckless", "Done background !"+patientNames);
            mText.setText(patientNames);
            sname.setText(serviceNameString);
            //adapter.notifyDataSetChanged();
        }

    }


}
