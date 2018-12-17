package com.softmed.htmr_facility.activities;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import com.softmed.htmr_facility.R;
import com.softmed.htmr_facility.base.AppDatabase;
import com.softmed.htmr_facility.base.BaseActivity;
import com.softmed.htmr_facility.dom.objects.Patient;
import com.softmed.htmr_facility.dom.objects.PostOffice;
import com.softmed.htmr_facility.dom.objects.Referral;
import com.softmed.htmr_facility.dom.objects.ReferralIndicator;
import com.softmed.htmr_facility.fragments.IssueReferralDialogueFragment;
import com.softmed.htmr_facility.utils.ListStringConverter;

import static com.softmed.htmr_facility.utils.constants.ENTRY_NOT_SYNCED;
import static com.softmed.htmr_facility.utils.constants.POST_DATA_REFERRAL_FEEDBACK;
import static com.softmed.htmr_facility.utils.constants.REFERRAL_STATUS_COMPLETED;

/**
 * Created by issy on 05/02/2018.
 *
 * @issyzac issyzac.iz@gmail.com
 * On Project HFReferralApp
 */

public class OpdReferralDetailsActivity extends BaseActivity {

    private Toolbar toolbar;
    public Button cancelButton, referButton;
    public TextView ctcNumber, referalReasons, villageLeaderValue, referrerName;
    private EditText servicesOfferedEt, otherInformationEt;
    private RecyclerView indicatorsRecyclerView;
    public TextView clientNames, wardText, villageText, hamletText, patientGender, otherClinicalInformationValue;
    public Dialog referalDialogue;

    private int service;
    private Referral currentReferral;
    private Patient currentPatient;
    private boolean isNewCase = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opd_referral_details);
        setupviews();

        if (getIntent().getExtras() != null){
            currentReferral = (Referral) getIntent().getSerializableExtra("referal");
            service = getIntent().getIntExtra("service", 0);

            if (currentReferral != null){
                if (currentReferral.getReferralStatus() == REFERRAL_STATUS_COMPLETED){

                    servicesOfferedEt.setEnabled(false);
                    servicesOfferedEt.setText(currentReferral.getServiceGivenToPatient());

                    otherInformationEt.setText(currentReferral.getOtherNotesAndAdvices());
                    otherInformationEt.setEnabled(false);

                    referButton.setEnabled(false);

                }

                villageLeaderValue.setText(currentReferral.getVillageLeader() == null ? "N/A" : currentReferral.getVillageLeader());
                otherClinicalInformationValue.setText(currentReferral.getOtherClinicalInformation() == null ? "N/A" : currentReferral.getOtherClinicalInformation());
                referalReasons.setText(currentReferral.getReferralReason() == null ? "N/A" : currentReferral.getReferralReason());
                villageLeaderValue.setText(currentReferral.getVillageLeader() == null ? "N/A" : currentReferral.getVillageLeader());
                referrerName.setText(currentReferral.getServiceProviderUIID() == null ? "N/A" : currentReferral.getServiceProviderUIID());

                new OpdReferralDetailsActivity.patientDetailsTask(baseDatabase, currentReferral.getPatient_id()).execute();

            }
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        referalDialogue = new Dialog(this);
        referalDialogue.requestWindowFeature(Window.FEATURE_NO_TITLE);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Finish Activity
                finish();
            }
        });

        referButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentPatient != null){
                    //Needs to save current referral feedback before issuing another referral
                    saveReferalInformation(true);
                }

            }
        });

    }

    private void saveReferalInformation(boolean isForwardingReferral){
        if (servicesOfferedEt.getText().toString().isEmpty()){
            Toast.makeText(this, "Tafadhali jaza huduma uliyoitoa", Toast.LENGTH_LONG).show();
        }else {

            String serviceOferedString = servicesOfferedEt.getText().toString();
            String otherInformation = otherInformationEt.getText().toString();

            currentReferral.setReferralStatus(REFERRAL_STATUS_COMPLETED);
            currentReferral.setServiceGivenToPatient(serviceOferedString);
            currentReferral.setOtherNotesAndAdvices(otherInformation);

            //Show progress bar
            cancelButton.setVisibility(View.INVISIBLE);

            OpdReferralDetailsActivity.UpdateReferralTask updateReferralTask = new OpdReferralDetailsActivity.UpdateReferralTask(currentReferral, baseDatabase);
            updateReferralTask.execute(isForwardingReferral, isNewCase);

        }
    }

    private void callReferralFragmentDialogue(Patient patient){

        FragmentManager fm = getSupportFragmentManager();

        IssueReferralDialogueFragment issueReferralDialogueFragment = IssueReferralDialogueFragment.newInstance(patient, service);
        issueReferralDialogueFragment.show(fm, "referral_fragment_from_adapter");

    }

    private void setupviews(){

        indicatorsRecyclerView = (RecyclerView) findViewById(R.id.indicators_linear_recycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        indicatorsRecyclerView.setLayoutManager(layoutManager);
        indicatorsRecyclerView.setHasFixedSize(true);
        otherClinicalInformationValue = (TextView) findViewById(R.id.other_clinical_inforamtion_value);

        servicesOfferedEt = (EditText) findViewById(R.id.service_offered_et);
        otherInformationEt = (EditText) findViewById(R.id.other_information_et);

        referrerName = (TextView) findViewById(R.id.referer_name_value);
        villageLeaderValue = (TextView) findViewById(R.id.mwenyekiti_name_value);
        patientGender = (TextView) findViewById(R.id.patient_gender_value);
        clientNames = (TextView) findViewById(R.id.client_name);
        referalReasons = (TextView) findViewById(R.id.sababu_ya_rufaa_value);

        wardText = (TextView) findViewById(R.id.client_kata_value);
        villageText = (TextView) findViewById(R.id.client_kijiji_value);
        hamletText = (TextView) findViewById(R.id.client_kitongoji_value);

        cancelButton = (Button) findViewById(R.id.cancel);
        referButton = (Button) findViewById(R.id.referal_button);

    }

    class IndicatorsRecyclerAdapter  extends RecyclerView.Adapter<OpdReferralDetailsActivity.IndicatorsViewHolder> {

        private List<ReferralIndicator> indicators = new ArrayList<>();
        private LayoutInflater mInflater;

        // data is passed into the constructor
        public IndicatorsRecyclerAdapter(Context context, List<ReferralIndicator> items) {
            this.mInflater = LayoutInflater.from(context);
            this.indicators = items;
        }

        // inflates the cell layout from xml when needed
        @Override
        public OpdReferralDetailsActivity.IndicatorsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = mInflater.inflate(R.layout.referral_details_indicatior_list_item, parent, false);
            OpdReferralDetailsActivity.IndicatorsViewHolder holder = new OpdReferralDetailsActivity.IndicatorsViewHolder(view);
            return holder;
        }

        // binds the data to the textview in each cell
        @Override
        public void onBindViewHolder(OpdReferralDetailsActivity.IndicatorsViewHolder holder, int position) {
            ReferralIndicator indicator = indicators.get(position);
            holder.bindIndicator(indicator);
        }

        // total number of cells
        @Override
        public int getItemCount() {
            return indicators.size();
        }

        // convenience method for getting data at click position
        ReferralIndicator getItem(int id) {
            return indicators.get(id);
        }

    }

    class IndicatorsViewHolder extends RecyclerView.ViewHolder {

        private TextView indicatorName;
        private ReferralIndicator referralIndicator;

        private IndicatorsViewHolder(View itemView) {
            super(itemView);
            indicatorName = (TextView) itemView.findViewById(R.id.indicator_name);
        }

        private void bindIndicator(ReferralIndicator indicator){
            this.referralIndicator = indicator;
            indicatorName.setText(referralIndicator==null?"":referralIndicator.getIndicatorName());
        }

    }

    class UpdateReferralTask extends AsyncTask<Boolean, Void, Void> {

        Referral referal;
        AppDatabase database;
        Boolean isForwardingThisReferral = false;
        Boolean isNewCase;

        UpdateReferralTask(Referral ref, AppDatabase db){
            this.referal = ref;
            this.database = db;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Boolean... booleans) {

            isForwardingThisReferral = booleans[0];
            isNewCase = booleans[1];

            database.referalModel().updateReferral(referal);

            PostOffice postOffice = new PostOffice();
            postOffice.setPost_id(referal.getReferral_id());
            postOffice.setSyncStatus(ENTRY_NOT_SYNCED);
            postOffice.setPost_data_type(POST_DATA_REFERRAL_FEEDBACK);
            database.postOfficeModelDao().addPostEntry(postOffice);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            cancelButton.setVisibility(View.VISIBLE);

            if (isForwardingThisReferral){
                servicesOfferedEt.setEnabled(false);
                otherInformationEt.setEnabled(false);
                callReferralFragmentDialogue(currentPatient);
            }else {
                finish();
            }


        }
    }

    class patientDetailsTask extends AsyncTask<Void, Void, Void> {

        String patientNames, patientId;
        Patient patient;
        AppDatabase db;
        List<ReferralIndicator> indicators =  new ArrayList<>();

        patientDetailsTask(AppDatabase database, String patientID){
            this.db = database;
            this.patientId = patientID;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            patientNames = db.patientModel().getPatientName(patientId);
            patient = db.patientModel().getPatientById(patientId);
            currentPatient = patient;

            List<Long> ids = currentReferral.getServiceIndicatorIds();

            //Call Patient Referral Indicators
            for (int i=0; i<ids.size(); i++){
                long id = Long.parseLong(ids.get(i)+"");
                ReferralIndicator referralIndicator = db.referralIndicatorDao().getReferralIndicatorById(id);
                indicators.add(referralIndicator);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            clientNames.setText(patientNames);
            if (patient != null){

                String wardTitle = getResources().getString(R.string.ward);
                String villageTitle = getResources().getString(R.string.village);
                String mapCueTitle = getResources().getString(R.string.map_cue);

                wardText.setText(patient.getWard() == null ? "N/A " : patient.getWard());
                villageText.setText(patient.getVillage() == null ? "N/A " : patient.getVillage());
                hamletText.setText(patient.getHamlet() == null ? "N/A " : patient.getHamlet());
                patientGender.setText(patient.getGender());
            }

            OpdReferralDetailsActivity.IndicatorsRecyclerAdapter adapter = new OpdReferralDetailsActivity.IndicatorsRecyclerAdapter(OpdReferralDetailsActivity.this, indicators);
            indicatorsRecyclerView.setAdapter(adapter);

        }

    }

}
