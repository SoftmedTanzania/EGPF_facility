package com.softmed.htmr_facility.dom.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import com.softmed.htmr_facility.dom.objects.ReferralIndicator;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * Created by issy on 21/01/2018.
 *
 * @issyzac issyzac.iz@gmail.com
 * On Project HFReferralApp
 */

@Dao
public interface ReferralIndicatorDao  {

    @Query("select * from ReferralIndicator")
    LiveData<List<ReferralIndicator>> getAllIndicators();

    @Query("select * from ReferralIndicator where serviceID = :serviceID")
    List<ReferralIndicator> getIndicatorsByServiceId(long serviceID);

    @Query("select * from ReferralIndicator where referralServiceIndicatorId = :referralIndicatorID")
    ReferralIndicator getReferralIndicatorById(long referralIndicatorID);

    @Insert(onConflict = REPLACE)
    void addIndicator(ReferralIndicator referralIndicator);

    @Delete
    void deleteAPatient(ReferralIndicator referralIndicator);

}
