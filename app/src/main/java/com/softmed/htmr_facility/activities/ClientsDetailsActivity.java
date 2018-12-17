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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.rey.material.widget.ProgressView;

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
import static com.softmed.htmr_facility.utils.constants.POST_DATA_TYPE_PATIENT;
import static com.softmed.htmr_facility.utils.constants.REFERRAL_STATUS_COMPLETED;

/**
 * Created by issy on 11/17/17.
 */

public class ClientsDetailsActivity extends BaseActivity {

    private Toolbar toolbar;
    public Button saveButton, referButton;
    public TextView ctcNumber, referalReasons, villageLeaderValue, referrerName, testResultsHint, hivStatusTitle;
    private EditText servicesOfferedEt, otherInformationEt, ctcNumberEt;
    public ProgressView saveProgress;
    private CheckBox hivStatus;
    private RecyclerView indicatorsRecyclerView;
    public TextView clientNames, wardText, villageText, hamletText, patientGender, otherClinicalInformationValue;
    public Dialog referalDialogue;

    private int service;
    private Referral currentReferral;
    private Patient currentPatient;
    private boolean isNewCase = false;
    private boolean isCTCNumberEmpty = false;
    private String clientCTCNumber;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_details);
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

                    saveButton.setEnabled(false);
                    referButton.setEnabled(false);
                    hivStatus.setEnabled(false);

                }

                hivStatus.setChecked(currentReferral.isTestResults());
                villageLeaderValue.setText(currentReferral.getVillageLeader() == null ? "N/A" : currentReferral.getVillageLeader());
                otherClinicalInformationValue.setText(currentReferral.getOtherClinicalInformation() == null ? "N/A" : currentReferral.getOtherClinicalInformation());
                referalReasons.setText(currentReferral.getReferralReason() == null ? "N/A" : currentReferral.getReferralReason());
                villageLeaderValue.setText(currentReferral.getVillageLeader() == null ? "N/A" : currentReferral.getVillageLeader());
                referrerName.setText(currentReferral.getServiceProviderUIID() == null ? "N/A" : currentReferral.getServiceProviderUIID());

                new patientDetailsTask(baseDatabase, currentReferral.getPatient_id()).execute();

            }
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        referalDialogue = new Dialog(this);
        referalDialogue.requestWindowFeature(Window.FEATURE_NO_TITLE);

        hivStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    ctcNumberEt.setVisibility(View.VISIBLE);
                    if (isCTCNumberEmpty){
                        isNewCase = true;
                    }else {
                        isNewCase = false;
                    }
                }else{
                    ctcNumberEt.setVisibility(View.GONE);
                    isNewCase = false;
                }
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveReferalInformation(false);
            }
        });

        referButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                referalDialogueEvents();

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

            clientCTCNumber = ctcNumberEt.getText().toString();

            boolean result = hivStatus.isChecked();

            currentReferral.setTestResults(result);
            currentReferral.setReferralStatus(REFERRAL_STATUS_COMPLETED);
            currentReferral.setServiceGivenToPatient(serviceOferedString);
            currentReferral.setOtherNotesAndAdvices(otherInformation);

            //Show progress bar
            saveProgress.setVisibility(View.VISIBLE);
            saveButton.setVisibility(View.INVISIBLE);

            UpdateReferralTask updateReferralTask = new UpdateReferralTask(currentReferral, baseDatabase);
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
        hivStatus = (CheckBox) findViewById(R.id.hiv_status);
        saveProgress = (ProgressView) findViewById(R.id.save_progress);
        servicesOfferedEt = (EditText) findViewById(R.id.service_offered_et);
        otherInformationEt = (EditText) findViewById(R.id.other_information_et);
        ctcNumberEt = (EditText) findViewById(R.id.ctc_number_et);
        ctcNumberEt.setVisibility(View.GONE);
        referrerName = (TextView) findViewById(R.id.referer_name_value);
        villageLeaderValue = (TextView) findViewById(R.id.mwenyekiti_name_value);
        patientGender = (TextView) findViewById(R.id.patient_gender_value);
        testResultsHint = (TextView) findViewById(R.id.test_results_hint);
        hivStatusTitle = (TextView) findViewById(R.id.hiv_status_title);
        wardText = (TextView) findViewById(R.id.client_kata_value);
        villageText = (TextView) findViewById(R.id.client_kijiji_value);
        hamletText = (TextView) findViewById(R.id.client_kitongoji_value);
        saveButton = (Button) findViewById(R.id.save_button);
        referButton = (Button) findViewById(R.id.referal_button);
        clientNames = (TextView) findViewById(R.id.client_name);
        referalReasons = (TextView) findViewById(R.id.sababu_ya_rufaa_value);

    }

    class IndicatorsRecyclerAdapter  extends RecyclerView.Adapter<IndicatorsViewHolder> {

        private List<ReferralIndicator> indicators = new ArrayList<>();
        private LayoutInflater mInflater;

        // data is passed into the constructor
        public IndicatorsRecyclerAdapter(Context context, List<ReferralIndicator> items) {
            this.mInflater = LayoutInflater.from(context);
            this.indicators = items;
        }

        // inflates the cell layout from xml when needed
        @Override
        public IndicatorsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = mInflater.inflate(R.layout.referral_details_indicatior_list_item, parent, false);
            IndicatorsViewHolder holder = new IndicatorsViewHolder(view);
            return holder;
        }

        // binds the data to the textview in each cell
        @Override
        public void onBindViewHolder(IndicatorsViewHolder holder, int position) {
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
            indicatorName.setText(referralIndicator.getIndicatorName());
        }

    }

    class UpdateReferralTask extends AsyncTask<Boolean, Void, Void>{

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

            if (isNewCase){

                Patient patient = database.patientModel().getPatientById(referal.getPatient_id());
                patient.setCtcNumber(clientCTCNumber);
                patient.setHivStatus(true);

                database.patientModel().updatePatient(patient);

                PostOffice postOffice1 = new PostOffice();
                postOffice1.setPost_id(patient.getPatientId());
                postOffice1.setPost_data_type(POST_DATA_TYPE_PATIENT);
                postOffice1.setSyncStatus(ENTRY_NOT_SYNCED);

                database.postOfficeModelDao().addPostEntry(postOffice1);

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            saveProgress.setVisibility(View.GONE);
            saveButton.setVisibility(View.VISIBLE);

            if (isForwardingThisReferral){
                hivStatus.setEnabled(false);
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
                if (patient.getCtcNumber() != null){
                    testResultsHint.setVisibility(View.VISIBLE);
                    hivStatus.setVisibility(View.VISIBLE);
                    hivStatusTitle.setVisibility(View.VISIBLE);
                    ctcNumberEt.setVisibility(View.VISIBLE);

                    ctcNumberEt.setText(patient.getCtcNumber());
                    isCTCNumberEmpty =false;

                }else {

                    isCTCNumberEmpty = true;

                    testResultsHint.setVisibility(View.VISIBLE);
                    hivStatus.setVisibility(View.VISIBLE);
                    hivStatusTitle.setVisibility(View.VISIBLE);
                }
            }

            IndicatorsRecyclerAdapter adapter = new IndicatorsRecyclerAdapter(ClientsDetailsActivity.this, indicators);
            indicatorsRecyclerView.setAdapter(adapter);

        }

    }

}
