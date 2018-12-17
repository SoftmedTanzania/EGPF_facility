package com.softmed.htmr_facility.dom.objects;

import android.arch.persistence.room.Entity;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by issy on 12/28/17.
 *
 * @issyzac issyzac.iz@gmail.com
 * On Project HFReferralApp
 */

@Entity(primaryKeys = { "tbPatientID", "encounterMonth" })
public class TbEncounters implements Serializable {

    private String id;

    @NonNull
    @SerializedName("tbPatientId")
    private String tbPatientID;

    @NonNull
    @SerializedName("encounterMonth")
    private int encounterMonth;

    @SerializedName("makohozi")
    private String makohozi;

    @SerializedName("appointmentId")
    private long appointmentId;

    @SerializedName("hasFinishedPreviousMonthMedication")
    private boolean hasFinishedPreviousMonthMedication;

    @SerializedName("medicationStatus")
    private boolean medicationStatus;

    @SerializedName("medicationDate")
    private long medicationDate;

    @SerializedName("scheduledDate")
    private long scheduledDate;


    public String getTbPatientID() {
        return tbPatientID;
    }

    public void setTbPatientID(String tbPatientID) {
        this.tbPatientID = tbPatientID;
    }

    public String getMakohozi() {
        return makohozi;
    }

    public void setMakohozi(String makohozi) {
        this.makohozi = makohozi;
    }

    public boolean isMedicationStatus() {
        return medicationStatus;
    }

    public void setMedicationStatus(boolean medicationStatus) {
        this.medicationStatus = medicationStatus;
    }

    public long getMedicationDate() {
        return medicationDate;
    }

    public void setMedicationDate(long medicationDate) {
        this.medicationDate = medicationDate;
    }

    public long getScheduledDate() {
        return scheduledDate;
    }

    public void setScheduledDate(long scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @NonNull
    public int getEncounterMonth() {
        return encounterMonth;
    }

    public void setEncounterMonth(@NonNull int encounterMonth) {
        this.encounterMonth = encounterMonth;
    }

    public long getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(long appointmentId) {
        this.appointmentId = appointmentId;
    }

    public boolean isHasFinishedPreviousMonthMedication() {
        return hasFinishedPreviousMonthMedication;
    }

    public void setHasFinishedPreviousMonthMedication(boolean hasFinishedPreviousMonthMedication) {
        this.hasFinishedPreviousMonthMedication = hasFinishedPreviousMonthMedication;
    }
}
