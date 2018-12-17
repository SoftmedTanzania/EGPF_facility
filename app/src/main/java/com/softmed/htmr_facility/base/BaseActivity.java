package com.softmed.htmr_facility.base;

import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.softmed.htmr_facility.R;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;

import com.softmed.htmr_facility.api.Endpoints;
import com.softmed.htmr_facility.dom.objects.Patient;
import com.softmed.htmr_facility.dom.objects.PostOffice;
import com.softmed.htmr_facility.dom.objects.Referral;
import com.softmed.htmr_facility.dom.objects.TbEncounters;
import com.softmed.htmr_facility.dom.objects.TbPatient;
import com.softmed.htmr_facility.dom.objects.UserData;
import com.softmed.htmr_facility.utils.SessionManager;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Retrofit;

import static com.softmed.htmr_facility.utils.constants.HIV_SERVICE_ID;
import static com.softmed.htmr_facility.utils.constants.TB_SERVICE_ID;

/**
 * Created by issy on 11/14/17.
 */

public class BaseActivity extends AppCompatActivity {

    public static Typeface Julius, Avenir, RobotoCondenced, RobotoCondencedItalic, RobotoThin, RobotoLight, RosarioRegular, RobotoMedium, athletic, FunRaiser;
    public static Typeface CabinSketchRegular, CabinSketchBold;

    public Retrofit retrofit;
    public Endpoints apiEndpoints;

    public static AppDatabase baseDatabase;

    // Session Manager Class
    public static SessionManager session;

    final public static DatePickerDialog toDatePicker = new DatePickerDialog();
    final public static DatePickerDialog fromDatePicker = new DatePickerDialog();
    final public static DatePickerDialog datePickerDialog = new DatePickerDialog();

    final public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setuptypeface();

        // Session class instance
        session = new SessionManager(getApplicationContext());
        baseDatabase = AppDatabase.getDatabase(this);

        fromDatePicker.setAccentColor(getResources().getColor(R.color.colorPrimary));
        toDatePicker.setAccentColor(getResources().getColor(R.color.colorPrimary));
        datePickerDialog.setAccentColor(getResources().getColor(R.color.colorPrimary));

    }

    public static String getThisFacilityId(){
        return session.getKeyHfid();
    }

    public String getServiceName(int serviceID){
        if (serviceID == HIV_SERVICE_ID){
            return "CTC";
        }else if (serviceID == TB_SERVICE_ID){
            return "Kifua Kikuu";
        }else {
            return "Nyingine";
        }
    }

    public static RequestBody getReferralFeedbackRequestBody(Referral referral, UserData userData){
        RequestBody body;
        String datastream = "";
        JSONObject object   = new JSONObject();

        try {

            object.put("patientId", referral.getPatient_id());
            object.put("communityBasedHivService", referral.getCommunityBasedHivService());
            object.put("referralReason", referral.getReferralReason());
            object.put("serviceId", referral.getServiceId());
            object.put("ctcNumber", referral.getCtcNumber());
            object.put("referralUUID", referral.getReferralUUID());
            object.put("serviceProviderUIID", referral.getServiceProviderUIID());
            object.put("serviceProviderGroup", referral.getServiceProviderGroup());
            object.put("villageLeader", referral.getVillageLeader());
            object.put("otherClinicalInformation", referral.getOtherClinicalInformation());
            object.put("fromFacilityId", referral.getFromFacilityId());
            object.put("referralSource", referral.getReferralSource());
            object.put("referralType", referral.getReferralType());
            object.put("referralDate", referral.getReferralDate());
            object.put("facilityId", referral.getFacilityId());
            object.put("referralStatus", referral.getReferralStatus());

            JSONArray serviceIndicatorsArray = new JSONArray();
            for (int i=0; i< referral.getServiceIndicatorIds().size(); i++){
                long indicator = Long.parseLong(referral.getServiceIndicatorIds().get(i)+"");
                serviceIndicatorsArray.put(indicator);
            }

            object.put("serviceIndicatorIds", serviceIndicatorsArray);

            object.put("referralId", referral.getReferral_id());
            object.put("serviceGivenToPatient", referral.getServiceGivenToPatient());
            object.put("otherNotes", referral.getOtherNotesAndAdvices());
            object.put("testResults", referral.isTestResults());
            object.put("healthFacilityCode", userData.getUserFacilityId());

            datastream = object.toString();
            Log.d("PostOfficeService", datastream);

            body = RequestBody.create(MediaType.parse("application/json"), datastream);

        }catch (Exception e){
            e.printStackTrace();
            body = RequestBody.create(MediaType.parse("application/json"), datastream);
        }

        return body;

    }

    public static class DeletePostData extends AsyncTask<PostOffice, Void, Void> {

        AppDatabase db;

        public DeletePostData(AppDatabase database){
            this.db = database;
        }

        @Override
        protected Void doInBackground(PostOffice... postOffices) {
            db.postOfficeModelDao().deletePostData(postOffices[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    public static RequestBody getTbEncounterRequestBody(TbEncounters encounters){
        RequestBody body;
        String datastream = "";
        JSONObject object   = new JSONObject();

        try {

            object.put("tbPatientId", Long.parseLong(encounters.getTbPatientID()));
            object.put("makohozi", encounters.getMakohozi());
            object.put("appointmentId", encounters.getAppointmentId());
            object.put("encounterMonth", encounters.getEncounterMonth());
            object.put("hasFinishedPreviousMonthMedication", encounters.isHasFinishedPreviousMonthMedication());
            object.put("scheduledDate", encounters.getScheduledDate());
            object.put("medicationDate", encounters.getMedicationDate());
            object.put("medicationStatus", encounters.isMedicationStatus());

            datastream = object.toString();

            Log.d("PostOfficeService", datastream);

            body = RequestBody.create(MediaType.parse("application/json"), datastream);

        }catch (Exception e){
            e.printStackTrace();
            body = RequestBody.create(MediaType.parse("application/json"), datastream);
        }

        return body;

    }

    public static RequestBody getReferralRequestBody(Referral referral, UserData userData){
        RequestBody body;
        String datastream = "";
        JSONObject object   = new JSONObject();

        try {

            int patientID = Integer.parseInt(referral.getPatient_id());

            object.put("patientId", patientID);
            object.put("communityBasedHivService", referral.getCommunityBasedHivService());
            object.put("referralReason", referral.getReferralReason());
            //object.put("ctcNumber", referral.getCtcNumber());
            object.put("serviceId", referral.getServiceId());
            object.put("referralUUID", referral.getReferralUUID());
            object.put("serviceProviderUIID", referral.getServiceProviderUIID());
            object.put("serviceProviderGroup", referral.getServiceProviderGroup());
            object.put("villageLeader", referral.getVillageLeader());
            object.put("otherClinicalInformation", referral.getOtherClinicalInformation());
            object.put("otherNotes", referral.getOtherNotesAndAdvices() == null ? "" : referral.getOtherNotesAndAdvices());
            object.put("serviceGivenToPatient", referral.getServiceGivenToPatient() == null ? "" : referral.getServiceGivenToPatient());
            object.put("testResults", referral.isTestResults()+"");
            object.put("fromFacilityId", referral.getFromFacilityId());
            object.put("referralSource", referral.getReferralSource());
            object.put("referralType", referral.getReferralType());
            object.put("referralDate", referral.getReferralDate());
            object.put("facilityId", referral.getFacilityId());
            object.put("referralStatus", referral.getReferralStatus());

            JSONArray serviceIndicatorsArray = new JSONArray();
            for (int i=0; i< referral.getServiceIndicatorIds().size(); i++){
                long indicator = Long.parseLong(referral.getServiceIndicatorIds().get(i)+"");
                serviceIndicatorsArray.put(indicator);
            }

            object.put("serviceIndicatorIds", serviceIndicatorsArray);
            //object.put("healthFacilityCode", userData.getUserFacilityId());

            datastream = object.toString();

            Log.d("PostOfficeService", datastream);

            body = RequestBody.create(MediaType.parse("application/json"), datastream);

        }catch (Exception e){
            e.printStackTrace();
            body = RequestBody.create(MediaType.parse("application/json"), datastream);
        }

        return body;

    }

    public static RequestBody getPatientRequestBody(Patient patient, UserData userData){

        RequestBody body;
        String datastream = "";
        JSONObject object   = new JSONObject();

        try {
            object.put("firstName", patient.getPatientFirstName());
            object.put("middleName", patient.getPatientMiddleName());
            object.put("phoneNumber", patient.getPhone_number());
            object.put("ward", patient.getWard());
            object.put("village", patient.getVillage());
            object.put("hamlet", patient.getHamlet());
            object.put("dateOfBirth", patient.getDateOfBirth());
            object.put("surname", patient.getPatientSurname());
            object.put("gender", patient.getGender());
            //object.put("healthFacilityCode", "2ff3e6fb-eb85-49eb-b7b3-564ddc26b9d4");
            object.put("currentOnTbTreatment", patient.isCurrentOnTbTreatment());
            object.put("healthFacilityCode", userData.getUserFacilityId());
            object.put("communityBasedHivService", patient.getCbhs());
            object.put("ctcNumber", patient.getCtcNumber());
            object.put("hivStatus", patient.isHivStatus());
            object.put("careTakerName", patient.getCareTakerName());
            object.put("careTakerPhoneNumber", patient.getCareTakerPhoneNumber());
            object.put("careTakerRelationship", patient.getCareTakerRelationship());

            /*object.put("patientType", tbPatient.getPatientType());
            object.put("transferType", tbPatient.getTransferType());
            object.put("referralType", tbPatient.getReferralType());
            object.put("veo", tbPatient.getVeo());
            object.put("weight", tbPatient.getWeight());
            object.put("xray", tbPatient.getXray());
            object.put("makohozi", tbPatient.getMakohozi());
            object.put("otherTests", tbPatient.getOtherTests());
            object.put("treatment_type", tbPatient.getTreatment_type());
            object.put("outcome", tbPatient.getOutcome());
            object.put("outcomeDate", tbPatient.getOutcomeDate());
            object.put("outcomeDetails", tbPatient.getOutcomeDetails());*/

            datastream = object.toString();

            Log.d("PostOfficeService", datastream);

            body = RequestBody.create(MediaType.parse("application/json"), datastream);

        }catch (Exception e){
            e.printStackTrace();
            body = RequestBody.create(MediaType.parse("application/json"), datastream);
        }

        return body;

    }

    public static RequestBody getTbPatientRequestBody(Patient patient, TbPatient tbPatient, UserData userData){
        RequestBody body;
        String datastream = "";
        JSONObject object   = new JSONObject();

        try {
            object.put("patientId", patient.getPatientId());
            object.put("firstName", patient.getPatientFirstName());
            object.put("middleName", patient.getPatientMiddleName());
            object.put("phoneNumber", patient.getPhone_number());
            object.put("ward", patient.getWard());
            object.put("village", patient.getVillage());
            object.put("hamlet", patient.getHamlet());
            object.put("dateOfBirth", patient.getDateOfBirth());
            object.put("surname", patient.getPatientSurname());
            object.put("gender", patient.getGender());
            object.put("hivStatus", patient.isHivStatus());
            object.put("isPRegnant", false);
            object.put("dateOfDeath", patient.getDateOfDeath());
            object.put("healthFacilityPatientId", 0);
            object.put("patientType", tbPatient.getPatientType());
            object.put("transferType", tbPatient.getTransferType());
            object.put("referralType", tbPatient.getReferralType());
            object.put("veo", tbPatient.getVeo());
            object.put("weight", tbPatient.getWeight());
            object.put("xray", tbPatient.getXray());
            object.put("makohozi", tbPatient.getMakohozi());
            object.put("otherTests", tbPatient.getOtherTests());
            object.put("treatment_type", tbPatient.getTreatment_type());
            object.put("outcome", tbPatient.getOutcome());
            object.put("outcomeDate", tbPatient.getOutcomeDate());
            object.put("outcomeDetails", tbPatient.getOutcomeDetails());
            object.put("healthFacilityCode", userData.getUserFacilityId());

            //object.put("healthFacilityCode", "2ff3e6fb-eb85-49eb-b7b3-564ddc26b9d4");
            //object.put("currentOnTbTreatment", patient.isCurrentOnTbTreatment());
            //object.put("communityBasedHivService", patient.getCbhs());
            //object.put("ctcNumber", patient.getCtcNumber());
            //object.put("careTakerName", patient.getCareTakerName());
            //object.put("careTakerPhoneNumber", patient.getCareTakerPhoneNumber());
            //object.put("careTakerRelationship", patient.getCareTakerRelationship());

            datastream = object.toString();

            Log.d("PostOfficeService", datastream);

            body = RequestBody.create(MediaType.parse("application/json"), datastream);

        }catch (Exception e){
            e.printStackTrace();
            body = RequestBody.create(MediaType.parse("application/json"), datastream);
        }

        return body;
    }

    public static RequestBody getEncounterRequestBody(TbEncounters encounters){
        RequestBody body;
        String datastream = "";
        JSONObject object   = new JSONObject();

        try {

            object.put("", 0);
            object.put("", 0);
            object.put("", 0);
            object.put("", 0);
            object.put("", 0);

            datastream = object.toString();

            Log.d("PostOfficeService", datastream);

            body = RequestBody.create(MediaType.parse("application/json"), datastream);

        }catch (Exception e){
            e.printStackTrace();
            body = RequestBody.create(MediaType.parse("application/json"), datastream);
        }

        return body;

    }


    public void setuptypeface(){
        Julius = Typeface.createFromAsset(this.getAssets(), "JuliusSansOne-Regular.ttf");
        Avenir = Typeface.createFromAsset(this.getAssets(), "avenir-light.ttf");
        RobotoCondenced = Typeface.createFromAsset(this.getAssets(), "Roboto-Condensed.ttf");
        RobotoCondencedItalic = Typeface.createFromAsset(this.getAssets(), "Roboto-CondensedItalic.ttf");
        RobotoThin = Typeface.createFromAsset(this.getAssets(), "Roboto-Thin.ttf");
        RobotoLight = Typeface.createFromAsset(this.getAssets(), "Roboto-Light.ttf");
        RobotoMedium = Typeface.createFromAsset(this.getAssets(), "Roboto-Medium.ttf");
        RosarioRegular = Typeface.createFromAsset(this.getAssets(), "Rosario-Regular.ttf");
        athletic = Typeface.createFromAsset(this.getAssets(), "athletic.ttf");
        FunRaiser = Typeface.createFromAsset(this.getAssets(), "Fun-Raiser.ttf");
        CabinSketchRegular = Typeface.createFromAsset(this.getAssets(), "CabinSketch-Regular.ttf");
        CabinSketchBold = Typeface.createFromAsset(this.getAssets(), "CabinSketch-Bold.ttf");
    }

}
