package com.softmed.htmr_facility.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.rey.material.widget.ProgressView;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.softmed.htmr_facility.R;
import com.softmed.htmr_facility.base.AppDatabase;
import com.softmed.htmr_facility.base.BaseActivity;
import com.softmed.htmr_facility.dom.objects.Patient;
import com.softmed.htmr_facility.dom.objects.PostOffice;
import com.softmed.htmr_facility.dom.objects.Referral;
import com.softmed.htmr_facility.dom.objects.ReferralIndicator;
import com.softmed.htmr_facility.dom.objects.TbPatient;
import com.softmed.htmr_facility.fragments.IssueReferralDialogueFragment;
import com.softmed.htmr_facility.utils.ListStringConverter;
import fr.ganfra.materialspinner.MaterialSpinner;

import static com.softmed.htmr_facility.utils.constants.ENTRY_NOT_SYNCED;
import static com.softmed.htmr_facility.utils.constants.POST_DATA_REFERRAL_FEEDBACK;
import static com.softmed.htmr_facility.utils.constants.POST_DATA_TYPE_PATIENT;
import static com.softmed.htmr_facility.utils.constants.POST_DATA_TYPE_TB_PATIENT;
import static com.softmed.htmr_facility.utils.constants.REFERRAL_STATUS_COMPLETED;
import static com.softmed.htmr_facility.utils.constants.TB_SERVICE_ID;

/**
 * Created by issy on 12/14/17.
 *
 * @issyzac issyzac.iz@gmail.com
 * On Project HFReferralApp
 */

public class TbReferralDetailsActivity extends BaseActivity {

    private Toolbar toolbar;
    public Button saveButton, referButton;
    private MaterialSpinner servicesSpinner, healthFacilitySpinner;
    public TextView clientName, ctcNumber, referalReasons, villageLeaderValue, referrerName;
    private EditText servicesOfferedEt, otherInformationEt, clientCtcNumber;
    public ProgressView saveProgress;
    private CheckBox tbStatus;
    private RecyclerView indicatorsRecyclerView;
    public TextView clientNames, wardText, villageText, hamletText, patientGender, otherClunucalInformationValue;

    public Dialog referalDialogue;

    private Referral currentReferral;
    private Patient currentPatient;
    private AppDatabase database;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tb_referral_details);
        setupviews();

        if (getIntent().getExtras() != null){
            currentReferral = (Referral) getIntent().getSerializableExtra("referal");
            if (currentReferral != null){
                if (currentReferral.getReferralStatus() == REFERRAL_STATUS_COMPLETED){

                    servicesOfferedEt.setEnabled(false);
                    servicesOfferedEt.setText(currentReferral.getServiceGivenToPatient());

                    otherInformationEt.setText(currentReferral.getOtherNotesAndAdvices());
                    otherInformationEt.setEnabled(false);

                    saveButton.setEnabled(false);
                    referButton.setEnabled(false);
                    tbStatus.setEnabled(false);

                }

                tbStatus.setChecked(currentReferral.isTestResults());

                otherClunucalInformationValue.setText(currentReferral.getOtherClinicalInformation());
                referalReasons.setText(currentReferral.getReferralReason() == null ? "" : currentReferral.getReferralReason());
                villageLeaderValue.setText(currentReferral.getVillageLeader() == null ? "" : currentReferral.getVillageLeader());
                referrerName.setText(currentReferral.getServiceProviderUIID() == null ? "" : currentReferral.getServiceProviderUIID());
                new TbReferralDetailsActivity.patientDetailsTask(baseDatabase, currentReferral.getPatient_id()).execute();
            }
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        referalDialogue = new Dialog(this);
        referalDialogue.requestWindowFeature(Window.FEATURE_NO_TITLE);

        tbStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    saveButton.setText("Ingiza : Kliniki ya TB");
                }else {
                    saveButton.setText("Hifadhi");
                }
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveReferalInformation();
            }
        });

        referButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentPatient!=null){
                    callReferralFragmentDialogue(currentPatient);
                }

            }
        });

    }

    private void saveReferalInformation(){
        if (servicesOfferedEt.getText().toString().isEmpty()){
            Toast.makeText(this, "Tafadhali jaza huduma uliyoitoa", Toast.LENGTH_LONG).show();
        }else {

            String serviceOferedString = servicesOfferedEt.getText().toString();
            String otherInformation = otherInformationEt.getText().toString();
            boolean results = tbStatus.isChecked();

            currentReferral.setTestResults(results);
            currentReferral.setReferralStatus(REFERRAL_STATUS_COMPLETED);
            currentReferral.setServiceGivenToPatient(serviceOferedString);
            currentReferral.setOtherNotesAndAdvices(otherInformation);

            //Show progress bar
            saveProgress.setVisibility(View.VISIBLE);
            saveButton.setVisibility(View.INVISIBLE);

            TbReferralDetailsActivity.UpdateReferralTask updateReferralTask = new TbReferralDetailsActivity.UpdateReferralTask(currentReferral, baseDatabase);
            updateReferralTask.execute();


        }
    }

    private void callReferralFragmentDialogue(Patient patient){

        FragmentManager fm = getSupportFragmentManager();

        IssueReferralDialogueFragment issueReferralDialogueFragment = IssueReferralDialogueFragment.newInstance(patient, TB_SERVICE_ID);
        issueReferralDialogueFragment.show(fm, "referral_fragment_from_adapter");

    }

    private void setupviews(){

        indicatorsRecyclerView = (RecyclerView) findViewById(R.id.indicators_linear_recycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        indicatorsRecyclerView.setLayoutManager(layoutManager);
        indicatorsRecyclerView.setHasFixedSize(true);

        otherClunucalInformationValue = (TextView) findViewById(R.id.other_clinical_inforamtion_value);
        tbStatus = (CheckBox) findViewById(R.id.tb_status);
        saveProgress = (ProgressView) findViewById(R.id.save_progress);
        servicesOfferedEt = (EditText) findViewById(R.id.service_offered_et);
        otherInformationEt = (EditText) findViewById(R.id.other_information_et);
        clientCtcNumber = (EditText) findViewById(R.id.ctc_number_et);
        referrerName = (TextView) findViewById(R.id.referer_name_value);
        villageLeaderValue = (TextView) findViewById(R.id.mwenyekiti_name_value);
        patientGender = (TextView) findViewById(R.id.patient_gender_value);
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

    class UpdateReferralTask extends AsyncTask<Void, Void, Void> {

        Referral referal;
        AppDatabase database;

        UpdateReferralTask(Referral ref, AppDatabase db){
            this.referal = ref;
            this.database = db;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            database.referalModel().updateReferral(referal);

            PostOffice postOffice = new PostOffice();
            postOffice.setPost_id(referal.getReferral_id());
            postOffice.setPost_data_type(POST_DATA_REFERRAL_FEEDBACK);
            postOffice.setSyncStatus(ENTRY_NOT_SYNCED);

            database.postOfficeModelDao().addPostEntry(postOffice);

            currentPatient = database.patientModel().getPatientById(currentReferral.getPatient_id());
            if (tbStatus.isChecked()){

                currentPatient.setCurrentOnTbTreatment(true);
                if (!clientCtcNumber.getText().toString().isEmpty()){
                    currentPatient.setCtcNumber(clientCtcNumber.getText().toString());
                }
                database.patientModel().updatePatient(currentPatient);

                PostOffice patientPost = new PostOffice();
                patientPost.setPost_id(referal.getReferral_id());
                patientPost.setPost_data_type(POST_DATA_TYPE_PATIENT);
                patientPost.setSyncStatus(ENTRY_NOT_SYNCED);

                //TODO: Create a new TbClient Object with basic parameters
                TbPatient tbPatient = new TbPatient();
                tbPatient.setPatientId(Long.parseLong(currentPatient.getPatientId()));
                tbPatient.setTempID(UUID.randomUUID()+"");
                database.tbPatientModelDao().addPatient(tbPatient);

                PostOffice tbPatientPost = new PostOffice();
                tbPatientPost.setPost_id(referal.getReferral_id());
                tbPatientPost.setPost_data_type(POST_DATA_TYPE_TB_PATIENT);
                tbPatientPost.setSyncStatus(ENTRY_NOT_SYNCED);

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            saveProgress.setVisibility(View.GONE);
            saveButton.setVisibility(View.VISIBLE);
            if (tbStatus.isChecked()){
                Intent intent = new Intent(TbReferralDetailsActivity.this, TbClientDetailsActivity.class);
                intent.putExtra("patient", currentPatient);
                intent.putExtra("isPatientNew", true);
                startActivity(intent);
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
            Log.d("reckless", "Done background!"+patientNames);
            clientNames.setText(patientNames);
            if (patient != null){
                wardText.setText(patient.getWard() == null ? "" : patient.getWard());
                villageText.setText(patient.getVillage() == null ? "" : patient.getVillage());
                hamletText.setText(patient.getHamlet() == null ? "" : patient.getHamlet());
                patientGender.setText(patient.getGender());
                clientCtcNumber.setText(patient.getCtcNumber());
            }

            IndicatorsRecyclerAdapter adapter = new IndicatorsRecyclerAdapter(TbReferralDetailsActivity.this, indicators);
            indicatorsRecyclerView.setAdapter(adapter);
        }

    }

}
