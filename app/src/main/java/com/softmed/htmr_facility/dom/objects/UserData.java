package com.softmed.htmr_facility.dom.objects;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Created by issy on 1/8/18.
 *
 * @issyzac issyzac.iz@gmail.com
 * On Project HFReferralApp
 */

@Entity
public class UserData {

    @PrimaryKey
    @NonNull
    private String UserUIID;

    private String userName;

    private String userFacilityId;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserFacilityId() {
        return userFacilityId;
    }

    public void setUserFacilityId(String userFacilityId) {
        this.userFacilityId = userFacilityId;
    }

    @NonNull
    public String getUserUIID() {
        return UserUIID;
    }

    public void setUserUIID(@NonNull String userUIID) {
        UserUIID = userUIID;
    }
}
