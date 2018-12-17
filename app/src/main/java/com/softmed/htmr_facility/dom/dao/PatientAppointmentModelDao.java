package com.softmed.htmr_facility.dom.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import com.softmed.htmr_facility.dom.objects.PatientAppointment;

/**
 * Created by issy on 1/2/18.
 *
 * @issyzac issyzac.iz@gmail.com
 * On Project HFReferralApp
 */

@Dao
public interface PatientAppointmentModelDao {

    @Query("select * from PatientAppointment order by appointmentDate asc")
    List<PatientAppointment> getAllAppointments();

    @Query("select * from PatientAppointment where appointmentType = 2 order by appointmentDate asc")
    List<PatientAppointment> getAllTbAppointments();

    @Query("select * from PatientAppointment where appointmentType = 1 order by appointmentDate asc")
    List<PatientAppointment> getAllCTCAppointments();

    @Query("select * from PatientAppointment where patientID = :patientId")
    List<PatientAppointment> getThisPatientAppointments(String patientId);

    @Query("select * from PatientAppointment where patientID = :patientId and appointmentDate > Date(:today)")
    List<PatientAppointment> getRemainingAppointments(String patientId, String today);

    @Insert
    void addAppointment(PatientAppointment appointment);

    @Update
    void updateAppointment(PatientAppointment appointment);

    @Delete
    void deleteAppointment(PatientAppointment appointment);

}
