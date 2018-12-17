package com.softmed.htmr_facility.adapters;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import com.softmed.htmr_facility.R;
import com.softmed.htmr_facility.base.AppDatabase;
import com.softmed.htmr_facility.dom.objects.PatientAppointment;

import static com.softmed.htmr_facility.utils.constants.STATUS_COMPLETED;
import static com.softmed.htmr_facility.utils.constants.STATUS_PENDING;

/**
 * Created by issy on 1/2/18.
 *
 * @issyzac issyzac.iz@gmail.com
 * On Project HFReferralApp
 */

public class AppointmentRecyclerAdapter extends RecyclerView.Adapter <RecyclerView.ViewHolder>  {

    private List<PatientAppointment> items;
    private Context context;
    private AppDatabase database;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, MMM dd, yyyy");

    public AppointmentRecyclerAdapter(List<PatientAppointment> mItems, Context context, AppDatabase db){
        this.items = mItems;
        this.database = db;
    }

    public AppointmentRecyclerAdapter(){}

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType){
        context         = viewGroup.getContext();
        View itemView   = null;
        itemView = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.patient_appointment_list_item, viewGroup, false);

        return new AppointmentRecyclerAdapter.ListViewItemViewHolder(itemView);

    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, int itemPosition){

        final PatientAppointment patientAppointment = getItem(itemPosition);
        AppointmentRecyclerAdapter.ListViewItemViewHolder holder = (AppointmentRecyclerAdapter.ListViewItemViewHolder) viewHolder;

        GetPatientNames getPatientNames = new GetPatientNames(database, holder);
        getPatientNames.execute(patientAppointment.getPatientID());
        holder.appointmentDate.setText(simpleDateFormat.format(patientAppointment.getAppointmentDate()));
        if (patientAppointment.getStatus().equals(STATUS_PENDING)){
            holder.appointmentStatus.setTextColor(context.getResources().getColor(R.color.amber_800));
        }else if (patientAppointment.getStatus().equals(STATUS_COMPLETED)){
            holder.appointmentStatus.setTextColor(context.getResources().getColor(R.color.green_800));
        }

        holder.appointmentStatus.setText(patientAppointment.getStatus());

    }

    public void addItems (List<PatientAppointment> pat){
        this.items = pat;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount(){
        return items.size();
    }

    private PatientAppointment getItem(int position){
        return items.get(position);
    }

    private class ListViewItemViewHolder extends RecyclerView.ViewHolder {

        TextView patientNames, appointmentDate, appointmentStatus;
        View viewItem;

        public ListViewItemViewHolder(View itemView){
            super(itemView);
            this.viewItem   = itemView;

            patientNames = (TextView) viewItem.findViewById(R.id.patient_appointment_name);
            appointmentDate = (TextView) viewItem.findViewById(R.id.patient_appointment_date);
            appointmentStatus = (TextView) viewItem.findViewById(R.id.patient_appointment_status);

        }

    }

    class GetPatientNames extends AsyncTask<String, Void, String>{

        AppDatabase database;
        AppointmentRecyclerAdapter.ListViewItemViewHolder holder;

        GetPatientNames(AppDatabase db, AppointmentRecyclerAdapter.ListViewItemViewHolder viewHolder){
            this.database = db;
            this.holder = viewHolder;
        }

        @Override
        protected void onPostExecute(String patientNames) {
            super.onPostExecute(patientNames);
            holder.patientNames.setText(patientNames);
        }

        @Override
        protected String doInBackground(String... strings) {
            String patientNames = database.patientModel().getPatientName(strings[0]);
            return patientNames;
        }
    }

}
