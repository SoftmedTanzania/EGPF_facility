package com.softmed.htmr_facility.dom.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import com.softmed.htmr_facility.dom.objects.HealthFacilities;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * Created by issy on 1/7/18.
 *
 * @issyzac issyzac.iz@gmail.com
 * On Project HFReferralApp
 */

@Dao
public interface HealthFacilitiesModelDao {

    @Query("select * from HealthFacilities")
    List<HealthFacilities> getAllHealthFacilities();

    @Insert(onConflict = REPLACE)
    void addHealthFacility(HealthFacilities hf);

    @Delete
    void deleteHealthFacility(HealthFacilities hf);

}
