package com.softmed.htmr_facility.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import com.softmed.htmr_facility.R;
import com.softmed.htmr_facility.activities.TbClientDetailsActivity;
import com.softmed.htmr_facility.activities.TbClientListActivity;
import com.softmed.htmr_facility.base.AppDatabase;
import com.softmed.htmr_facility.dom.objects.Patient;
import com.softmed.htmr_facility.dom.objects.TbPatient;
import com.softmed.htmr_facility.fragments.IssueReferralDialogueFragment;

/**
 * Created by issy on 12/28/17.
 *
 * @issyzac issyzac.iz@gmail.com
 * On Project HFReferralApp
 */

public class TbClientListAdapter extends RecyclerView.Adapter <RecyclerView.ViewHolder> {

    List<Patient> items;
    private Context context;
    private int serviceID;

    public TbClientListAdapter(List<Patient> mItems, Context context, int serviceId){
        this.items = mItems;
        this.serviceID = serviceId;
    }

    public TbClientListAdapter(){}

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType){
        context         = viewGroup.getContext();
        View itemView   = null;
        itemView = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.tb_client_list_item, viewGroup, false);

        return new TbClientListAdapter.ListViewItemViewHolder(itemView);

    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, int itemPosition){

        final Patient patient = getItem(itemPosition);
        TbClientListAdapter.ListViewItemViewHolder holder = (TbClientListAdapter.ListViewItemViewHolder) viewHolder;

        holder.clientFirstName.setText(patient.getPatientFirstName());
        holder.clientLastName.setText(patient.getPatientSurname());
        holder.clientVillage.setText(patient.getVillage());
        holder.clientPhoneNumber.setText(patient.getPhone_number());
        holder.clientGender.setText(patient.getGender());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //UpdatePatientInformation and save data
                Intent intent = new Intent(context, TbClientDetailsActivity.class);
                intent.putExtra("patient", patient);
                intent.putExtra("isPatientNew", false);
                context.startActivity(intent);
            }
        });

        holder.rufaaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callReferralFragmentDialogue(patient);
            }
        });

        new GetTbDetails(AppDatabase.getDatabase(context), holder).execute(patient.getPatientId());

    }

    private void callReferralFragmentDialogue(Patient patient){
        TbClientListActivity activity = (TbClientListActivity) context;
        FragmentManager fm = activity.getSupportFragmentManager();

        IssueReferralDialogueFragment issueReferralDialogueFragment = IssueReferralDialogueFragment.newInstance(patient, serviceID);
        issueReferralDialogueFragment.show(fm, "referral_fragment_from_adapter");

    }

    public void addItems (List<Patient> pat){
        this.items = pat;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount(){
        return items.size();
    }

    private Patient getItem(int position){
        return items.get(position);
    }

    private class ListViewItemViewHolder extends RecyclerView.ViewHolder {

        TextView clientFirstName, clientLastName, clientVillage, clientPhoneNumber, clientGender, clientTreatment;
        View viewItem;
        Button rufaaButton;

        public ListViewItemViewHolder(View itemView){
            super(itemView);
            this.viewItem   = itemView;

            rufaaButton = (Button) itemView.findViewById(R.id.rufaa_button);

            clientFirstName = (TextView) itemView.findViewById(R.id.client_f_name);
            clientLastName = (TextView) itemView.findViewById(R.id.client_l_name);
            clientVillage = (TextView) itemView.findViewById(R.id.client_village);
            clientPhoneNumber = (TextView) itemView.findViewById(R.id.client_phone_number);
            clientGender =  (TextView) itemView.findViewById(R.id.client_gender);
            clientTreatment = (TextView) itemView.findViewById(R.id.client_treatment);

        }

    }

    class GetTbDetails extends AsyncTask<String, Void, Void>{

        TbPatient tbPatient;
        AppDatabase database;
        TbClientListAdapter.ListViewItemViewHolder holder;
                //= (TbClientListAdapter.ListViewItemViewHolder) viewHolder;

        GetTbDetails(AppDatabase db, TbClientListAdapter.ListViewItemViewHolder vh){
            this.database = db;
            this.holder = vh;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {
                holder.clientTreatment.setText(tbPatient.getTreatment_type());
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        @Override
        protected Void doInBackground(String... args) {
            tbPatient = database.tbPatientModelDao().getTbPatientById(args[0]);
            return null;
        }
    }


}
