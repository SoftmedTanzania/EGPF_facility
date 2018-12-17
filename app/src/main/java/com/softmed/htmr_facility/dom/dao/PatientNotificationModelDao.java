package com.softmed.htmr_facility.dom.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.TypeConverters;

import java.util.List;

import com.softmed.htmr_facility.dom.objects.PatientsNotification;
import com.softmed.htmr_facility.utils.DateConverter;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * Created by issy on 12/3/17.
 *
 * @issyzac issyzac.iz@gmail.com
 * On Project HFReferal
 */

@Dao
@TypeConverters(DateConverter.class)
public interface PatientNotificationModelDao {

    @Query("select * from PatientsNotification")
    LiveData<List<PatientsNotification>> getAllNotifications();

    @Query("select * from PatientsNotification where patient_id = :id")
    PatientsNotification getPatientNotificationByPatientId(String id);

    @Insert(onConflict = REPLACE)
    void addPatientNotification(PatientsNotification patientsNotification);

    @Delete
    void deletePatientNotification(PatientsNotification patientsNotification);

}
