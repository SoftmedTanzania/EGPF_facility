package com.softmed.htmr_facility.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

import com.softmed.htmr_facility.base.AppDatabase;
import com.softmed.htmr_facility.dom.objects.PostOffice;

/**
 * Created by issy on 12/12/17.
 *
 * @issyzac issyzac.iz@gmail.com
 * On Project HFReferralApp
 */

public class PostOfficeListViewModel extends AndroidViewModel {

    private final LiveData<List<PostOffice>> unpostedDataList;
    private final LiveData<List<PostOffice>> postedDataList;

    AppDatabase appDatabase;

    public PostOfficeListViewModel(Application application){
        super(application);
        appDatabase = AppDatabase.getDatabase(this.getApplication());
        unpostedDataList = appDatabase.postOfficeModelDao().getUnpostedLiveData();
        postedDataList = appDatabase.postOfficeModelDao().getPostedData();
    }

    public LiveData<List<PostOffice>> getUnpostedDataList() {
        return unpostedDataList;
    }

    public LiveData<List<PostOffice>> getPostedDataList() {
        return postedDataList;
    }

    public void deletePostOfficeData(PostOffice postOffice){
        new PostOfficeListViewModel.deleteAsyncTask(appDatabase).execute(postOffice);
    }

    private static class deleteAsyncTask extends AsyncTask<PostOffice, Void, Void> {

        private AppDatabase db;

        deleteAsyncTask(AppDatabase appDatabase){
            db = appDatabase;
        }

        @Override
        protected Void doInBackground(PostOffice... postOffices) {
            db.postOfficeModelDao().deletePostData(postOffices[0]);
            return null;
        }
    }

}
