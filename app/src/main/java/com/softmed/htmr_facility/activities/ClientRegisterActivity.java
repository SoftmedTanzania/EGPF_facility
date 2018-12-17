package com.softmed.htmr_facility.activities;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rey.material.widget.EditText;
import com.softmed.htmr_facility.R;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import com.softmed.htmr_facility.base.AppDatabase;
import com.softmed.htmr_facility.base.BaseActivity;
import com.softmed.htmr_facility.dom.objects.Patient;
import com.softmed.htmr_facility.dom.objects.PostOffice;
import com.softmed.htmr_facility.dom.objects.TbPatient;
import fr.ganfra.materialspinner.MaterialSpinner;

import static com.softmed.htmr_facility.utils.constants.ENTRY_NOT_SYNCED;
import static com.softmed.htmr_facility.utils.constants.FEMALE;
import static com.softmed.htmr_facility.utils.constants.MALE;
import static com.softmed.htmr_facility.utils.constants.POST_DATA_TYPE_PATIENT;

/**
 * Created by issy on 12/14/17.
 *
 * @issyzac issyzac.iz@gmail.com
 * On Project HFReferralApp
 */

public class ClientRegisterActivity extends BaseActivity {

    private Toolbar toolbar;
    private MaterialSpinner genderSpinner;
    private EditText firstName, middleName, surname, phone, ward, village, hamlet, weight, mwenyekiti, careTakerName, careTakerPhone, careTakerRelationship, chbsNumber, ctcNumber;
    private Button btnSave, btnCancel;
    private TextView dateOfBirth;
    private CheckBox pregnant;
    private ProgressDialog dialog;
    private RelativeLayout pregnantWrap;

    private Date dob;
    private String strFname, strMname, strSurname, strGender, strPhone, strWard, strVillage, strHamlet, strCbhsNumber, strCTCNumber;
    private String strDateOfBirth, strWeight, strMwenyekiti, strCareTakerName, strCareTakerPhone, strCareTakerRelationship;
    private boolean isPregnant;
    private boolean isTbClient = false;
    private Calendar dobCalendar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.opd_client_registration_activity);
        setupview();

        if (getIntent().getExtras() != null){
            isTbClient = getIntent().getBooleanExtra("isTbClient", false);
        }

        if (toolbar!=null){
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        dialog = new ProgressDialog(ClientRegisterActivity.this, 0);
        dialog.setTitle("Inahifadhi");
        dialog.setMessage("Inahifadhi. Tafadhali Subiri...");

        //dialog = ProgressDialog.show(TbRegisterActivity.this, "Saving",
        //        "Loading. Please wait...", true);

        final String[] genders = {MALE, FEMALE};
        ArrayAdapter<String> spinAdapter = new ArrayAdapter<String>(this, R.layout.simple_spinner_item_black, genders);
        spinAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item_black);
        genderSpinner.setAdapter(spinAdapter);

        dateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.show(getFragmentManager(),"dateOfBirth");
                datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

                        dateOfBirth.setText((dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth) + "-" + ((monthOfYear + 1) < 10 ? "0" + (monthOfYear + 1) : monthOfYear + 1) + "-" + year);
                        dobCalendar = Calendar.getInstance();
                        dobCalendar.set(year, monthOfYear, dayOfMonth);
                        dob = dobCalendar.getTime();
                    }

                });
            }
        });

        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0){
                    pregnantWrap.setVisibility(View.INVISIBLE);
                }else {
                    pregnantWrap.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getInputs()){
                    saveData();
                }else {
                    Toast.makeText(ClientRegisterActivity.this,
                            "Weka taarifa zote kabla ya kuhifadhi taarifa",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }

    private boolean getInputs(){

        if (firstName.getText().toString().isEmpty()){
            return false;
        }else {
            strFname = firstName.getText().toString();
        }

        strMname = middleName.getText().toString().isEmpty() ? "" : middleName.getText().toString();

        strSurname = surname.getText().toString().isEmpty() ? "" : surname.getText().toString();

        if (genderSpinner.getSelectedItemPosition() == -1){
            return false;
        }else {
            strGender = (String) genderSpinner.getSelectedItem();
        }

        strPhone = phone.getText().toString().isEmpty() ? "" : phone.getText().toString();

        strWard = ward.getText().toString().isEmpty() ? "" : ward.getText().toString();

        strVillage = village.getText().toString().isEmpty() ? "" : village.getText().toString();

        strHamlet = hamlet.getText().toString().isEmpty() ? "" : hamlet.getText().toString();

        strDateOfBirth = dateOfBirth.getText().toString();

        isPregnant = pregnant.isChecked();

        strWeight = weight.getText().toString().isEmpty()? "0.0" : weight.getText().toString();

        strMwenyekiti = mwenyekiti.getText().toString();

        strCareTakerName = careTakerName.getText().toString();

        strCareTakerPhone = careTakerPhone.getText().toString();

        strCareTakerRelationship = careTakerRelationship.getText().toString();

        strCbhsNumber = chbsNumber.getText().toString();

        strCTCNumber = ctcNumber.getText().toString();

        return true;
    }

    private void saveData(){
        Patient patient = new Patient();
        TbPatient tbPatient = new TbPatient();

        long range = 1234567L;
        Random r = new Random();
        long number = (long)(r.nextDouble()*range);

        patient.setId(number);
        patient.setPatientId(number+"");
        patient.setGender(strGender);
        patient.setHamlet(strHamlet);
        patient.setVillage(strVillage);
        patient.setWard(strWard);
        patient.setPhone_number(strPhone);
        patient.setPatientFirstName(strFname);
        patient.setPatientMiddleName(strMname);
        patient.setPatientSurname(strSurname);
        patient.setCurrentOnTbTreatment(isTbClient);//This value depends on where the client has been registered from
        patient.setDateOfBirth(dobCalendar.getTimeInMillis());
        patient.setCareTakerName(strCareTakerName);
        patient.setCareTakerPhoneNumber(strCareTakerPhone);
        patient.setCareTakerRelationship(strCareTakerRelationship);
        patient.setCbhs(strCbhsNumber);
        patient.setCtcNumber(strCTCNumber);

        AddNewPatient addNewPatient = new AddNewPatient(patient, baseDatabase);
        addNewPatient.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }

    private void setupview(){

        pregnantWrap = (RelativeLayout) findViewById(R.id.pregnant_wrap);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        genderSpinner = (MaterialSpinner) findViewById(R.id.spin_gender);

        dateOfBirth = (TextView) findViewById(R.id.date_of_birth);

        firstName = (EditText) findViewById(R.id.fname_et);
        middleName = (EditText) findViewById(R.id.mname_et);
        surname = (EditText) findViewById(R.id.surname_et);
        phone = (EditText) findViewById(R.id.phone_number);
        ward = (EditText) findViewById(R.id.ward);
        village = (EditText) findViewById(R.id.village);
        hamlet = (EditText) findViewById(R.id.hamlet);
        weight = (EditText) findViewById(R.id.weight);
        mwenyekiti = (EditText) findViewById(R.id.mwenyekiti);

        careTakerName = (EditText) findViewById(R.id.care_taker_name);
        careTakerPhone = (EditText) findViewById(R.id.care_taker_phone);
        careTakerRelationship = (EditText) findViewById(R.id.care_taker_relationship);

        chbsNumber = (EditText) findViewById(R.id.cbhs_number);
        ctcNumber = (EditText) findViewById(R.id.patient_ctc_number);

        pregnant = (CheckBox) findViewById(R.id.is_pregnant);

        btnSave = (Button) findViewById(R.id.save_button);
        btnCancel = (Button) findViewById(R.id.cancel_button);
    }

    class AddNewPatient extends AsyncTask<Void, Void, Void> {

        Patient p;
        TbPatient tp;
        AppDatabase database;

        AddNewPatient(Patient patient,  AppDatabase db){
            this.p = patient;
            this.database = db;
            dialog.show();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            database.patientModel().addPatient(p);
            //database.tbPatientModelDao().addPatient(tp);

            //TODO: Add patient to PostOffice and set Sync Status
            PostOffice po = new PostOffice();
            po.setPost_id(p.getPatientId());
            po.setPost_data_type(POST_DATA_TYPE_PATIENT);
            po.setSyncStatus(ENTRY_NOT_SYNCED);
            database.postOfficeModelDao().addPostEntry(po);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dialog.dismiss();
            finish();
            /*if (isTbClient){
                Intent intent = new Intent(ClientRegisterActivity.this, TbClientDetailsActivity.class);
                intent.putExtra("patient", p);
                intent.putExtra("isPatientNew", true);
                startActivity(intent);
                finish();
            }else {
                finish();
            }*/
        }
    }

}
