package com.softmed.htmr_facility.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

import com.softmed.htmr_facility.base.AppDatabase;
import com.softmed.htmr_facility.base.BaseActivity;
import com.softmed.htmr_facility.dom.objects.Referral;

import static com.softmed.htmr_facility.utils.constants.CHW_TO_FACILITY;
import static com.softmed.htmr_facility.utils.constants.HIV_SERVICE_ID;
import static com.softmed.htmr_facility.utils.constants.INTERFACILITY;
import static com.softmed.htmr_facility.utils.constants.INTRAFACILITY;
import static com.softmed.htmr_facility.utils.constants.OPD_SERVICE_ID;
import static com.softmed.htmr_facility.utils.constants.TB_SERVICE_ID;

/**
 * Created by issy on 11/28/17.
 */

public class ReferalListViewModel extends AndroidViewModel{

    private final LiveData<List<Referral>> referalList;
    private final LiveData<List<Referral>> unattendedReferrals;
    private final LiveData<List<Referral>> tbReferalList;

    private final LiveData<List<Referral>> hivReferredClientsList;
    private final LiveData<List<Referral>> tbReferredClientsList;
    private final LiveData<List<Referral>> referredClientsList;

    private final LiveData<List<Referral>> referalListHfSource;
    private final LiveData<List<Referral>> referalListChwSource;

    private final LiveData<List<Referral>> referalListHfSourceTb;
    private final LiveData<List<Referral>> referalListChwSourceTb;

    private final LiveData<List<Referral>> allReferralListFromChw;
    private final LiveData<List<Referral>> allReferralListFromHealthFacilities;

    private AppDatabase appDatabase;

    public ReferalListViewModel(Application application){
        super(application);
        appDatabase = AppDatabase.getDatabase(this.getApplication());
        referalList = appDatabase.referalModel().getAllReferals(HIV_SERVICE_ID);
        unattendedReferrals = appDatabase.referalModel().getUnattendedReferals(HIV_SERVICE_ID);
        tbReferalList = appDatabase.referalModel().getTbReferralList(TB_SERVICE_ID);

        hivReferredClientsList = appDatabase.referalModel().getReferredClients(HIV_SERVICE_ID, BaseActivity.getThisFacilityId());
        tbReferredClientsList = appDatabase.referalModel().getReferredClients(TB_SERVICE_ID, BaseActivity.getThisFacilityId());
        referredClientsList = appDatabase.referalModel().getReferredClients(OPD_SERVICE_ID, BaseActivity.getThisFacilityId());

        referalListHfSource = appDatabase.referalModel().getReferralsBySourceId(HIV_SERVICE_ID, new int[]{INTERFACILITY, INTRAFACILITY});
        referalListChwSource = appDatabase.referalModel().getReferralsBySourceId(HIV_SERVICE_ID, new int[] {CHW_TO_FACILITY});

        referalListHfSourceTb = appDatabase.referalModel().getReferralsBySourceId(TB_SERVICE_ID, new int[]{INTERFACILITY, INTRAFACILITY});
        referalListChwSourceTb = appDatabase.referalModel().getReferralsBySourceId(TB_SERVICE_ID, new int[] {CHW_TO_FACILITY});

        allReferralListFromChw = appDatabase.referalModel().getAllReferalsBySource(new int[] {CHW_TO_FACILITY});
        allReferralListFromHealthFacilities = appDatabase.referalModel().getAllReferalsBySource(new int[] {INTRAFACILITY, INTERFACILITY});

    }

    public LiveData<List<Referral>> getTbReferalList() {
        return tbReferalList;
    }

    public LiveData<List<Referral>> getReferalListHfSource() {
        return referalListHfSource;
    }

    public LiveData<List<Referral>> getReferalListChwSource() {
        return referalListChwSource;
    }

    public LiveData<List<Referral>> getReferalList(){
        return referalList;
    }

    public LiveData<List<Referral>> getHivReferredClientsList() {
        return hivReferredClientsList;
    }

    public LiveData<List<Referral>> getReferredClientsList() {
        return referredClientsList;
    }

    public LiveData<List<Referral>> getTbReferredClientsList() {
        return tbReferredClientsList;
    }

    public LiveData<List<Referral>> getAllReferralListFromChw() {
        return allReferralListFromChw;
    }

    public LiveData<List<Referral>> getAllReferralListFromHealthFacilities() {
        return allReferralListFromHealthFacilities;
    }

    public LiveData<List<Referral>> getUnattendedReferrals(){
        return unattendedReferrals;
    }

    public void deleteReferal(Referral referral){
        new deleteAsyncTask(appDatabase).execute(referral);
    }

    public LiveData<List<Referral>> getTbReferralList(){
        return tbReferalList;
    }

    public LiveData<List<Referral>> getReferalListHfSourceTb() {
        return referalListHfSourceTb;
    }

    public LiveData<List<Referral>> getReferalListChwSourceTb() {
        return referalListChwSourceTb;
    }

    private static class deleteAsyncTask extends AsyncTask<Referral, Void, Void>{

        private AppDatabase db;

        deleteAsyncTask(AppDatabase appDatabase){
            db = appDatabase;
        }

        @Override
        protected Void doInBackground(Referral... referrals) {
            db.referalModel().deleteReferal(referrals[0]);
            return null;
        }
    }

}
