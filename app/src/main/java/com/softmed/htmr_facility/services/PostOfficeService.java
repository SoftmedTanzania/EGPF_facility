package com.softmed.htmr_facility.services;

import android.app.IntentService;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import java.util.List;

import com.softmed.htmr_facility.api.Endpoints;
import com.softmed.htmr_facility.base.AppDatabase;
import com.softmed.htmr_facility.base.BaseActivity;
import com.softmed.htmr_facility.dom.objects.Patient;
import com.softmed.htmr_facility.dom.objects.PatientAppointment;
import com.softmed.htmr_facility.dom.objects.PostOffice;
import com.softmed.htmr_facility.dom.objects.Referral;
import com.softmed.htmr_facility.dom.objects.TbEncounters;
import com.softmed.htmr_facility.dom.objects.TbPatient;
import com.softmed.htmr_facility.dom.objects.UserData;
import com.softmed.htmr_facility.dom.responces.PatientResponce;
import com.softmed.htmr_facility.utils.ServiceGenerator;
import com.softmed.htmr_facility.utils.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.softmed.htmr_facility.utils.constants.POST_DATA_REFERRAL_FEEDBACK;
import static com.softmed.htmr_facility.utils.constants.POST_DATA_TYPE_ENCOUNTER;
import static com.softmed.htmr_facility.utils.constants.POST_DATA_TYPE_PATIENT;
import static com.softmed.htmr_facility.utils.constants.POST_DATA_TYPE_REFERRAL;
import static com.softmed.htmr_facility.utils.constants.POST_DATA_TYPE_TB_PATIENT;

/**
 * Created by issy on 1/6/18.
 *
 * @issyzac issyzac.iz@gmail.com
 * On Project HFReferralApp
 */

public class PostOfficeService extends IntentService {

    AppDatabase database;
    SessionManager sess;
    Endpoints.PatientServices patientServices;
    Endpoints.ReferalService referalService;

    public PostOfficeService() {
        super("PostOfficeService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        database = AppDatabase.getDatabase(this.getApplicationContext());
        sess = new SessionManager(this.getApplicationContext());

        patientServices = ServiceGenerator.createService(Endpoints.PatientServices.class,
                sess.getUserName(),
                sess.getUserPass(),
                sess.getKeyHfid());

        referalService = ServiceGenerator.createService(Endpoints.ReferalService.class,
                sess.getUserName(),
                sess.getUserPass(),
                sess.getKeyHfid());

        //Check if data is available in the PostOffice
        if (database.postOfficeModelDao().getUnpostedDataCount() > 0) {
            List<PostOffice> unpostedData = database.postOfficeModelDao().getUnpostedData();
            for (int i = 0; i < unpostedData.size(); i++) {

                final PostOffice data = unpostedData.get(i);
                Log.d("PostOfficeService", data.getPost_data_type());

                if (data.getPost_data_type().equals(POST_DATA_TYPE_PATIENT)) {

                    final Patient patient = database.patientModel().getPatientById(data.getPost_id());
                    //final TbPatient tbPatient = database.tbPatientModelDao().getTbPatientById(patient.getPatientId());
                    final UserData userData = database.userDataModelDao().getUserDataByUserUIID(sess.getUserDetails().get("uuid"));

                    Call call = patientServices.postPatient(BaseActivity.getPatientRequestBody(patient, userData));
                    call.enqueue(new Callback() {
                        @Override
                        public void onResponse(Call call, Response response) {
                            Patient patient1 = (Patient) response.body();
                            //Store Received Patient Information, TbPatient as well as PatientAppointments
                            if (response.body() != null) {
                                Log.d("patient_response", response.body().toString());

                                new ReplacePatientObject().execute(patient, patient1);

                                new DeletePOstData(database).execute(data); //This can be removed and data may be set synced status to SYNCED

                            } else {
                                Log.d("patient_response", "Patient Responce is null " + response.body());
                            }

                        }

                        @Override
                        public void onFailure(Call call, Throwable t) {
                            Log.d("patient_response", t.getMessage());
                        }
                    });
                }else if (data.getPost_data_type().equals(POST_DATA_TYPE_TB_PATIENT)){

                    final Patient patient = database.patientModel().getPatientById(data.getPost_id());
                    final TbPatient tbPatient = database.tbPatientModelDao().getTbPatientById(patient.getPatientId());
                    final UserData userData = database.userDataModelDao().getUserDataByUserUIID(sess.getUserDetails().get("uuid"));

                    Call call = patientServices.postPatient(BaseActivity.getTbPatientRequestBody(patient, tbPatient, userData));
                    call.enqueue(new Callback() {
                        @Override
                        public void onResponse(Call call, Response response) {
                            PatientResponce patientResponce = (PatientResponce) response.body();
                            //Store Received Patient Information, TbPatient as well as PatientAppointments
                            if (response.body()!=null){
                                Log.d("POST_DATA_TYPE_TP", response.body().toString());

                                new ReplaceTbPatientAndAppointments(database, patient, tbPatient).execute(patientResponce);

                                new DeletePOstData(database).execute(data); //Remove PostOffice Entry, set synced SYNCED may also be used to flag data as already synced

                            }else {
                                Log.d("POST_DATA_TYPE_TP","Patient Responce is null "+response.body());
                            }

                        }

                        @Override
                        public void onFailure(Call call, Throwable t) {
                            Log.d("patient_response", t.getMessage());
                        }
                    });

                }else if (data.getPost_data_type().equals(POST_DATA_TYPE_REFERRAL)) {

                    final Referral referral = database.referalModel().getReferalById(data.getPost_id());
                    final UserData userData = database.userDataModelDao().getUserDataByUserUIID(sess.getUserDetails().get("uuid"));

                    Call call = referalService.postReferral(BaseActivity.getReferralRequestBody(referral, userData));
                    call.enqueue(new Callback() {
                        @Override
                        public void onResponse(Call call, Response response) {
                            Referral receivedReferral = (Referral) response.body();
                            if (response.body() != null) {

                                Log.d("PostReferral", response.body().toString());
                                new ReplaceReferralObject().execute(referral, receivedReferral);

                                new DeletePOstData(database).execute(data); //This can be removed and data may be set synced status to SYNCED

                            } else {
                                Log.d("PostReferral", "Responce is Null : " + response.body());
                            }
                        }

                        @Override
                        public void onFailure(Call call, Throwable t) {
                            Log.d("PostReferral", t.getMessage());
                        }
                    });

                } else if (data.getPost_data_type().equals(POST_DATA_REFERRAL_FEEDBACK)) {

                    final Referral referral = database.referalModel().getReferalById(data.getPost_id());
                    final UserData userData = database.userDataModelDao().getUserDataByUserUIID(sess.getUserDetails().get("uuid"));

                    Call call = referalService.sendReferralFeedback(BaseActivity.getReferralFeedbackRequestBody(referral, userData));
                    call.enqueue(new Callback() {
                        @Override
                        public void onResponse(Call call, Response response) {
                            Log.d("POST_RESPOMCES", "Saved to seerver : " + response.body());
                            //database.postOfficeModelDao().deletePostData(data);
                            if (response.code() == 200) {
                                new DeletePOstData(database).execute(data);
                            }
                        }

                        @Override
                        public void onFailure(Call call, Throwable t) {
                            Log.d("POST_RESPOMCES", "Failed with message : " + t.getMessage());
                        }
                    });

                } else if (data.getPost_data_type().equals(POST_DATA_TYPE_ENCOUNTER)) {

                    List<TbEncounters> encounter = database.tbEncounterModelDao().getEncounterByPatientID(data.getPost_id());
                    for (TbEncounters e : encounter){

                        Call call = patientServices.postEncounter(BaseActivity.getTbEncounterRequestBody(e));
                        call.enqueue(new Callback() {
                            @Override
                            public void onResponse(Call call, Response response) {
                                Log.d("POST_DATA_TE", "Response Received : "+response.body());
                            }

                            @Override
                            public void onFailure(Call call, Throwable t) {
                                Log.d("POST_DATA_TE", "Error : "+t.getMessage());
                            }
                        });

                    }

                }
            }

        }


        // Release the wake lock provided by the WakefulBroadcastReceiver.
        WakefulBroadcastReceiver.completeWakefulIntent(intent);
    }


    class ReplacePatientObject extends AsyncTask<Patient, Void, Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Patient... patients) {

            //Delete the old object
            database.patientModel().deleteAPatient(patients[0]);
            //Insert server's patient reference
            database.patientModel().addPatient(patients[1]);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    class ReplaceReferralObject extends AsyncTask<Referral, Void, Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Referral... referrals) {

            database.referalModel().deleteReferal(referrals[0]); //Delete local referral reference
            database.referalModel().addReferal(referrals[1]); //Store the server's referral reference

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    class DeletePOstData extends AsyncTask<PostOffice, Void, Void>{

        AppDatabase database;

        DeletePOstData(AppDatabase db){
            this.database = db;
        }

        @Override
        protected Void doInBackground(PostOffice... postOffices) {
            database.postOfficeModelDao().deletePostData(postOffices[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }
    }

    class ReplaceTbPatientAndAppointments extends AsyncTask<PatientResponce, Void, Void>{

        Patient patient;
        TbPatient tbPatient;
        PatientAppointment patientAppointment;
        AppDatabase database;

        ReplaceTbPatientAndAppointments(AppDatabase db, Patient pt, TbPatient tp){
            this. database = db;
            this.patient = pt;
            this.tbPatient = tp;
        }

        @Override
        protected Void doInBackground(PatientResponce... patientResponces) {

            database.tbPatientModelDao().deleteAPatient(tbPatient);

            TbPatient tbPatient1 = patientResponces[0].getTbPatient();
            List<PatientAppointment> appointments = patientResponces[0].getPatientAppointments();

            List<PatientAppointment> oldAppointments = database.appointmentModelDao().getThisPatientAppointments(patient.getPatientId());
            for (int i=0; i<oldAppointments.size(); i++){
                database.appointmentModelDao().deleteAppointment(oldAppointments.get(i));
            }

            //Insert server's patient reference
            database.tbPatientModelDao().addPatient(tbPatient1);
            for (int j=0; j<appointments.size(); j++){
                database.appointmentModelDao().addAppointment(appointments.get(j));
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

}
