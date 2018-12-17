package com.softmed.htmr_facility.base;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.softmed.htmr_facility.dom.dao.AppDataModelDao;
import com.softmed.htmr_facility.dom.dao.HealthFacilitiesModelDao;
import com.softmed.htmr_facility.dom.dao.PatientAppointmentModelDao;
import com.softmed.htmr_facility.dom.dao.PatientModelDao;
import com.softmed.htmr_facility.dom.dao.PatientNotificationModelDao;
import com.softmed.htmr_facility.dom.dao.PatientServicesModelDao;
import com.softmed.htmr_facility.dom.dao.PostOfficeModelDao;
import com.softmed.htmr_facility.dom.dao.ReferalModelDao;
import com.softmed.htmr_facility.dom.dao.ReferralIndicatorDao;
import com.softmed.htmr_facility.dom.dao.ReferralServiceIndicatorsDao;
import com.softmed.htmr_facility.dom.dao.TbEncounterModelDao;
import com.softmed.htmr_facility.dom.dao.TbPatientModelDao;
import com.softmed.htmr_facility.dom.dao.UserDataModelDao;
import com.softmed.htmr_facility.dom.objects.AppData;
import com.softmed.htmr_facility.dom.objects.HealthFacilities;
import com.softmed.htmr_facility.dom.objects.Patient;
import com.softmed.htmr_facility.dom.objects.PatientAppointment;
import com.softmed.htmr_facility.dom.objects.HealthFacilityServices;
import com.softmed.htmr_facility.dom.objects.PatientsNotification;
import com.softmed.htmr_facility.dom.objects.PostOffice;
import com.softmed.htmr_facility.dom.objects.Referral;
import com.softmed.htmr_facility.dom.objects.ReferralIndicator;
import com.softmed.htmr_facility.dom.objects.ReferralServiceIndicators;
import com.softmed.htmr_facility.dom.objects.TbEncounters;
import com.softmed.htmr_facility.dom.objects.TbPatient;
import com.softmed.htmr_facility.dom.objects.UserData;

/**
 * Created by issy on 11/28/17.
 */

@Database(
        entities = {
                Patient.class,
                Referral.class,
                PatientsNotification.class,
                PostOffice.class,
                AppData.class,
                TbPatient.class,
                TbEncounters.class,
                PatientAppointment.class,
                HealthFacilityServices.class,
                HealthFacilities.class,
                UserData.class,
                ReferralIndicator.class,
                ReferralServiceIndicators.class
        },
        version = 1)

public abstract class AppDatabase extends RoomDatabase{

    private static AppDatabase INSTANCE;

    public static AppDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "htmr_db")
                            .build();
        }
        return INSTANCE;
    }

    public abstract PatientModelDao patientModel();

    public abstract ReferalModelDao referalModel();

    public abstract PatientNotificationModelDao patientNotificationModelDao();

    public abstract PostOfficeModelDao postOfficeModelDao();

    public abstract AppDataModelDao appDataModelDao();

    public abstract TbPatientModelDao tbPatientModelDao();

    public abstract TbEncounterModelDao tbEncounterModelDao();

    public abstract PatientAppointmentModelDao appointmentModelDao();

    public abstract PatientServicesModelDao servicesModelDao();

    public abstract HealthFacilitiesModelDao healthFacilitiesModelDao();

    public abstract UserDataModelDao userDataModelDao();

    public abstract ReferralIndicatorDao referralIndicatorDao();

    public abstract ReferralServiceIndicatorsDao referralServiceIndicatorsDao();

}
