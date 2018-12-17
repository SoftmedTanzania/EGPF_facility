package com.softmed.htmr_facility.dom.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import com.softmed.htmr_facility.dom.objects.HealthFacilityServices;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * Created by issy on 1/7/18.
 *
 * @issyzac issyzac.iz@gmail.com
 * On Project HFReferralApp
 */

@Dao
public interface PatientServicesModelDao {

    @Query("select * from HealthFacilityServices")
    List<HealthFacilityServices> getAllServices();

    @Insert(onConflict = REPLACE)
    void addService(HealthFacilityServices service);

    @Delete
    void deleteService(HealthFacilityServices service);

    @Query("select serviceName || ' ' from HealthFacilityServices where id = :serviceID")
    String getServiceName(int serviceID);

}
