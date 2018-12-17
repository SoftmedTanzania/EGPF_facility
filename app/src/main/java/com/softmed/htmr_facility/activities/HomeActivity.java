package com.softmed.htmr_facility.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;

import com.softmed.htmr_facility.R;
import com.softmed.htmr_facility.api.Endpoints;
import com.softmed.htmr_facility.base.AppDatabase;
import com.softmed.htmr_facility.base.BaseActivity;
import com.softmed.htmr_facility.customviews.NonSwipeableViewPager;
import com.softmed.htmr_facility.dom.objects.Patient;
import com.softmed.htmr_facility.dom.objects.PatientAppointment;
import com.softmed.htmr_facility.dom.objects.PostOffice;
import com.softmed.htmr_facility.dom.objects.Referral;
import com.softmed.htmr_facility.dom.objects.TbEncounters;
import com.softmed.htmr_facility.dom.objects.TbPatient;
import com.softmed.htmr_facility.dom.objects.UserData;
import com.softmed.htmr_facility.dom.responces.PatientResponce;
import com.softmed.htmr_facility.fragments.HivFragment;
import com.softmed.htmr_facility.fragments.OPDFragment;
import com.softmed.htmr_facility.fragments.TbFragment;
import com.softmed.htmr_facility.utils.AlarmReceiver;
import com.softmed.htmr_facility.utils.Config;
import com.softmed.htmr_facility.utils.ServiceGenerator;
import com.softmed.htmr_facility.viewmodels.PostOfficeListViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.AlarmManager.INTERVAL_FIFTEEN_MINUTES;
import static com.softmed.htmr_facility.utils.constants.POST_DATA_REFERRAL_FEEDBACK;
import static com.softmed.htmr_facility.utils.constants.POST_DATA_TYPE_ENCOUNTER;
import static com.softmed.htmr_facility.utils.constants.POST_DATA_TYPE_PATIENT;
import static com.softmed.htmr_facility.utils.constants.POST_DATA_TYPE_REFERRAL;
import static com.softmed.htmr_facility.utils.constants.POST_DATA_TYPE_TB_PATIENT;

/**
 * Created by issy on 12/4/17.
 *
 * @issyzac issyzac.iz@gmail.com
 * On Project HFReferralApp
 */

public class HomeActivity extends BaseActivity {

    private final String TAG = HomeActivity.class.getSimpleName();

    private TabLayout tabLayout;
    public static NonSwipeableViewPager viewPager;
    private Toolbar toolbar;
    private TextView toolbarTitle, unsynced;
    private CircularProgressView syncProgressBar;
    private ImageView manualSync;

    private PostOfficeListViewModel viewModel;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private AppDatabase database;
    private Endpoints.PatientServices patientServices;
    private Endpoints.ReferalService referalService;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setupviews();

        database = AppDatabase.getDatabase(this);

        session.checkLogin();

        patientServices = ServiceGenerator.createService(Endpoints.PatientServices.class,
                session.getUserName(),
                session.getUserPass(),
                session.getKeyHfid());

        referalService = ServiceGenerator.createService(Endpoints.ReferalService.class,
                session.getUserName(),
                session.getUserPass(),
                session.getKeyHfid());

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null){
            setSupportActionBar(toolbar);
        }

        if (session.isLoggedIn()){
            toolbarTitle.setText(session.getUserName());
        }

        checkPostOfficeData();

        viewPager = (NonSwipeableViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
                setupTabIcons();
            }
        });

        manualSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Manual Sync the data in postman
                new SyncPostOfficeData().execute();
            }
        });

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);
                    SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
                    String regId = pref.getString("regId", null);
                    Log.d(TAG, "Registration ID broadcasted in HomeActivity : "+regId);

                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    String message = intent.getStringExtra("message");

                    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();

                    Log.d(TAG, "Message received in HomeActivity : "+message);

                }
            }
        };


        scheduleAlarm();

    }

    private void checkPostOfficeData(){

        viewModel = ViewModelProviders.of(this).get(PostOfficeListViewModel.class);
        viewModel.getUnpostedDataList().observe(HomeActivity.this, new Observer<List<PostOffice>>() {
            @Override
            public void onChanged(@Nullable List<PostOffice> postOffices) {
                if (postOffices.size() > 0){
                    unsynced.setText("Taarifa Hazijaoanishwa : "+postOffices.size());
                    unsynced.setTextColor(getResources().getColor(R.color.orange_400));
                }else {
                    unsynced.setText("Taarifa Zimeoanishwa");
                    unsynced.setTextColor(getResources().getColor(R.color.green_600));
                }

            }
        });

    }

    private void syncDataInPostOffice(){
        //Check if data is available in the PostOffice
        if (database.postOfficeModelDao().getUnpostedDataCount() > 0){
            List<PostOffice> unpostedData = database.postOfficeModelDao().getUnpostedData();
            for (int i=0; i<unpostedData.size(); i++){

                final PostOffice data = unpostedData.get(i);
                Log.d("PostOfficeService", data.getPost_data_type());

                if (data.getPost_data_type().equals(POST_DATA_TYPE_PATIENT)){

                    final Patient patient = database.patientModel().getPatientById(data.getPost_id());
                    final UserData userData = database.userDataModelDao().getUserDataByUserUIID(session.getUserDetails().get("uuid"));

                    Call call = patientServices.postPatient(getPatientRequestBody(patient, userData));
                    call.enqueue(new Callback() {
                        @Override
                        public void onResponse(Call call, Response response) {
                            //Store Received Patient Information, TbPatient as well as PatientAppointments
                            if (response.body()!=null){

                                Patient patient1 = (Patient) response.body();
                                Log.d("POST_DATA_TYPE_PATIENT", patient1.getPatientFirstName());

                                /**
                                 * TODO: 1: Update patient object with the new patient id (Delete old insert new)
                                 * TODO: 2: Update all referrals with this patient ID to the newly created patient ID
                                 */

                                new ReplacePatientObject().execute(patient, patient1);

                                new DeletePOstData(database).execute(data); //This can be removed and data may be set synced status to SYNCED

                            }else {
                                Log.d("POST_DATA_TYPE_PATIENT","Patient Responce is null "+response.body());
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
                    final UserData userData = database.userDataModelDao().getUserDataByUserUIID(session.getUserDetails().get("uuid"));

                    Call call = patientServices.postPatient(getTbPatientRequestBody(patient, tbPatient, userData));
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

                } else if (data.getPost_data_type().equals(POST_DATA_TYPE_REFERRAL)){

                    final Referral referral = database.referalModel().getReferalById(data.getPost_id());
                    final UserData userData = database.userDataModelDao().getUserDataByUserUIID(session.getUserDetails().get("uuid"));

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

                }else if (data.getPost_data_type().equals(POST_DATA_REFERRAL_FEEDBACK)){

                    Log.d("POST_DATA_REFERRAL_FB", data.getPost_data_type());

                    final Referral referral = database.referalModel().getReferalById(data.getPost_id());
                    final UserData userData = database.userDataModelDao().getUserDataByUserUIID(session.getUserDetails().get("uuid"));

                    Call call = referalService.sendReferralFeedback(BaseActivity.getReferralFeedbackRequestBody(referral, userData));
                    call.enqueue(new Callback() {
                        @Override
                        public void onResponse(Call call, Response response) {
                            Log.d("POST_DATA_REFERRAL_FB", "Outside 200 : "+response.body());
                            //database.postOfficeModelDao().deletePostData(data);
                            if (response.code() == 200){
                                Log.d("POST_DATA_REFERRAL_FB", "Saved to seerver : "+response.body());
                                new DeletePOstData(database).execute(data);
                            }

                        }

                        @Override
                        public void onFailure(Call call, Throwable t) {
                            Log.d("POST_RESPOMCES", "Failed with message : " + t.getMessage());
                        }
                    });

                }else if(data.getPost_data_type().equals(POST_DATA_TYPE_ENCOUNTER)){

                    List<TbEncounters> encounter = database.tbEncounterModelDao().getEncounterByPatientID(data.getPost_id());
                    for (TbEncounters e : encounter){

                        Call call = patientServices.postEncounter(BaseActivity.getTbEncounterRequestBody(e));
                        call.enqueue(new Callback() {
                            @Override
                            public void onResponse(Call call, Response response) {
                                Log.d("POST_DATA_TE", "Response Received : "+response.body());

                                new DeletePOstData(database).execute(data); //TODO REMOVE THIS

                            }

                            @Override
                            public void onFailure(Call call, Throwable t) {
                                Log.d("POST_DATA_TE", "Error : "+t.getMessage());

                                new DeletePOstData(database).execute(data); //TODO REMOVE THIS

                            }
                        });

                    }

                }
            }

        }
    }

    private void setupviews(){

        syncProgressBar = (CircularProgressView) findViewById(R.id.manual_sync_loader);
        syncProgressBar.setVisibility(View.INVISIBLE);

        manualSync = (ImageView) findViewById(R.id.manual_sync);

        toolbarTitle = (TextView) findViewById(R.id.toolbar_user_name);
        unsynced = (TextView) findViewById(R.id.unsynced);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.logout){
            session.logoutUser();
        }

        if (id == R.id.notifications){
            //Display List Of Reminders
            View menuItemView = findViewById(R.id.notifications); // SAME ID AS MENU ID
            PopupMenu popupMenu = new PopupMenu(this, menuItemView);
            popupMenu.inflate(R.menu.notification_list);
            // ...
            popupMenu.show();
            // ...
        }

        if (id == R.id.sync_data){
            //Sync data from PostOffice
        }

        if (id == R.id.reminder){
            //Open Reminder Activity
            startActivity(new Intent(this, AppointmentActivity.class));
        }

        return true;
    }

    public void setupTabIcons() {

        View ccmView = getLayoutInflater().inflate(R.layout.custom_tabs, null);
        TextView ccmTitle = (TextView) ccmView.findViewById(R.id.title_text);
        ccmTitle.setText("OPD");
        ImageView iv3    = (ImageView) ccmView.findViewById(R.id.icon);
        iv3.setColorFilter(this.getResources().getColor(R.color.white));
        Glide.with(this).load(R.mipmap.ic_face).into(iv3);
        tabLayout.getTabAt(0).setCustomView(ccmView);

        View homeView = getLayoutInflater().inflate(R.layout.custom_tabs, null);
        TextView homeTitle = (TextView) homeView.findViewById(R.id.title_text);
        ImageView iv    = (ImageView) homeView.findViewById(R.id.icon);
//        iv.setColorFilter(this.getResources().getColor(R.color.colorPrimary));
        Glide.with(this).load(R.mipmap.ic_hiv).into(iv);
        homeTitle.setText("Huduma za VVU/Ukimwi");
        tabLayout.getTabAt(1).setCustomView(homeView);

        View newsView = getLayoutInflater().inflate(R.layout.custom_tabs, null);
        TextView newsTitle = (TextView) newsView.findViewById(R.id.title_text);
        newsTitle.setText("Kifua Kikuu");
        ImageView iv2    = (ImageView) newsView.findViewById(R.id.icon);
        iv2.setColorFilter(this.getResources().getColor(R.color.white));
        Glide.with(this).load(R.mipmap.ic_tb).into(iv2);
        tabLayout.getTabAt(2).setCustomView(newsView);

    }

    public void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new OPDFragment(), "opd");
        adapter.addFragment(new HivFragment(), "hiv");
        adapter.addFragment(new TbFragment(), "tb");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
//            return mFragmentTitleList.get(position);
            return null;
        }
    }

    public void scheduleAlarm() {
        // Construct an intent that will execute the AlarmReceiver
        Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
        // Create a PendingIntent to be triggered when the alarm goes off
        final PendingIntent pIntent = PendingIntent.getBroadcast(this, AlarmReceiver.REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        // Setup periodic alarm every every half hour from this point onwards
        long firstMillis = System.currentTimeMillis(); // alarm is set right away
        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        // First parameter is the type: ELAPSED_REALTIME, ELAPSED_REALTIME_WAKEUP, RTC_WAKEUP
        // Interval can be INTERVAL_FIFTEEN_MINUTES, INTERVAL_HALF_HOUR, INTERVAL_HOUR, INTERVAL_DAY
        alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, firstMillis,
                INTERVAL_FIFTEEN_MINUTES, pIntent);
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

    class SyncPostOfficeData extends AsyncTask<Void,Void, Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            manualSync.setVisibility(View.INVISIBLE);
            syncProgressBar.setVisibility(View.VISIBLE);
            unsynced.setText("Taarifa zinaoanishwa..");
            unsynced.setTextColor(getResources().getColor(R.color.white));
        }

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                Thread.sleep(5000, 0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            syncDataInPostOffice();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            manualSync.setVisibility(View.VISIBLE);
            syncProgressBar.setVisibility(View.INVISIBLE);
            unsynced.setText("Taarifa Zimeoanishwa!");
            unsynced.setTextColor(getResources().getColor(R.color.white));
            checkPostOfficeData();
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
