package com.softmed.htmr_facility.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

import com.softmed.htmr_facility.base.AppDatabase;
import com.softmed.htmr_facility.dom.objects.TbPatient;

/**
 * Created by issy on 12/30/17.
 *
 * @issyzac issyzac.iz@gmail.com
 * On Project HFReferralApp
 */

public class TbPatientListViewModel extends AndroidViewModel {

    private final LiveData<List<TbPatient>> patientsList;

    private AppDatabase appDatabase;

    public TbPatientListViewModel(Application application) {
        super(application);
        appDatabase = AppDatabase.getDatabase(this.getApplication());
        patientsList = appDatabase.tbPatientModelDao().getAllTbPatients();
    }

    public LiveData<List<TbPatient>> getPatientsList() {
        return patientsList;
    }

    public void deleteItem(TbPatient patient) {
        new TbPatientListViewModel.deleteAsyncTask(appDatabase).execute(patient);
    }

    private static class deleteAsyncTask extends AsyncTask<TbPatient, Void, Void> {

        private AppDatabase db;

        deleteAsyncTask(AppDatabase appDatabase) {
            db = appDatabase;
        }

        @Override
        protected Void doInBackground(final TbPatient... params) {
            db.tbPatientModelDao().deleteAPatient(params[0]);
            return null;
        }

    }

}
