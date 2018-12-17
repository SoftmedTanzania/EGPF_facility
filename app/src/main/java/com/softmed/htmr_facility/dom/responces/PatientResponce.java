package com.softmed.htmr_facility.dom.responces;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import com.softmed.htmr_facility.dom.objects.Patient;
import com.softmed.htmr_facility.dom.objects.PatientAppointment;
import com.softmed.htmr_facility.dom.objects.TbPatient;

/**
 * Created by issy on 1/6/18.
 *
 * @issyzac issyzac.iz@gmail.com
 * On Project HFReferralApp
 */

public class PatientResponce implements Serializable{

    @SerializedName("patientsDTO")
    private Patient patient;

    @SerializedName("tbPatientDTO")
    private TbPatient tbPatient;

    @SerializedName("patientsAppointmentsDTOS")
    private List<PatientAppointment> patientAppointments;

    PatientResponce(){}

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public TbPatient getTbPatient() {
        return tbPatient;
    }

    public void setTbPatient(TbPatient tbPatient) {
        this.tbPatient = tbPatient;
    }

    public List<PatientAppointment> getPatientAppointments() {
        return patientAppointments;
    }

    public void setPatientAppointments(List<PatientAppointment> patientAppointments) {
        this.patientAppointments = patientAppointments;
    }
}
