package com.softmed.htmr_facility.dom.objects;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * Created by issy on 12/12/17.
 *
 * @issyzac issyzac.iz@gmail.com
 * On Project HFReferralApp
 */

@Entity
public class PostOffice implements Serializable{

    @NonNull
    @PrimaryKey(autoGenerate = false)
    private String post_id;

    private String post_data_type;

    /**
     *  1 == POSTED
     *  0 == NOT_POSTED
     */
    private int syncStatus;

    @NonNull
    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(@NonNull String post_id) {
        this.post_id = post_id;
    }

    public String getPost_data_type() {
        return post_data_type;
    }

    public void setPost_data_type(String post_data_type) {
        this.post_data_type = post_data_type;
    }

    public int getSyncStatus() {
        return syncStatus;
    }

    public void setSyncStatus(int syncStatus) {
        this.syncStatus = syncStatus;
    }

}
