package com.softmed.htmr_facility.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

import com.softmed.htmr_facility.base.AppDatabase;
import com.softmed.htmr_facility.dom.objects.PatientsNotification;

/**
 * Created by issy on 12/3/17.
 *
 * @issyzac issyzac.iz@gmail.com
 * On Project HFReferal
 */

public class PatientNotificationListViewModel extends AndroidViewModel{

    private final LiveData<List<PatientsNotification>> patientNotificationsList;

    private AppDatabase appDatabase;

    public PatientNotificationListViewModel(Application application){
        super(application);
        appDatabase = AppDatabase.getDatabase(this.getApplication());
        patientNotificationsList = appDatabase.patientNotificationModelDao().getAllNotifications();
    }

    public LiveData<List<PatientsNotification>> getPatientNotificationsList(){
        return patientNotificationsList;
    }

    public void deletePatientNotificationItem(PatientsNotification patientsNotification){
        new deleteAsyncTask(appDatabase).execute(patientsNotification);
    }

    private static class deleteAsyncTask extends AsyncTask<PatientsNotification, Void, Void> {

        private AppDatabase database;

        deleteAsyncTask(AppDatabase appDatabase){
            this.database = appDatabase;
        }

        @Override
        protected Void doInBackground(final PatientsNotification... params){
            database.patientNotificationModelDao().deletePatientNotification(params[0]);
            return null;
        }

    }

}
