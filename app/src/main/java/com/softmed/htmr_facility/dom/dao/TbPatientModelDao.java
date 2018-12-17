package com.softmed.htmr_facility.dom.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import com.softmed.htmr_facility.dom.objects.TbPatient;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * Created by issy on 12/30/17.
 *
 * @issyzac issyzac.iz@gmail.com
 * On Project HFReferralApp
 */

@Dao
public interface TbPatientModelDao  {

    @Query("select * from TbPatient")
    LiveData<List<TbPatient>> getAllTbPatients();

    @Query("select * from TbPatient where patientId = :id")
    TbPatient getTbPatientById(String id);

    @Insert(onConflict = REPLACE)
    void addPatient(TbPatient tbPatients);

    @Update
    void updateTbPatient(TbPatient patient);

    @Delete
    void deleteAPatient(TbPatient patients);

}
