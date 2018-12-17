package com.softmed.htmr_facility.dom.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import com.softmed.htmr_facility.dom.objects.ReferralServiceIndicators;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * Created by issy on 21/01/2018.
 *
 * @issyzac issyzac.iz@gmail.com
 * On Project HFReferralApp
 */

@Dao
public interface ReferralServiceIndicatorsDao {

    @Query("select * from ReferralServiceIndicators")
    List<ReferralServiceIndicators> getAllServices();

    @Query("select * from ReferralServiceIndicators where serviceId = :serviceID")
    ReferralServiceIndicators getServiceById(long serviceID);

    @Query("select serviceName || ' ' from ReferralServiceIndicators where serviceId = :serviceID")
    String getServiceNameById(int serviceID);

    @Insert(onConflict = REPLACE)
    void addService(ReferralServiceIndicators referralServiceIndicator);

    @Delete
    void deleteAPatient(ReferralServiceIndicators referralServiceIndicators);

}
