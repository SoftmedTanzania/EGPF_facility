package com.softmed.htmr_facility.dom.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import com.softmed.htmr_facility.dom.objects.UserData;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * Created by issy on 1/8/18.
 *
 * @issyzac issyzac.iz@gmail.com
 * On Project HFReferralApp
 */

@Dao
public interface UserDataModelDao {

    @Query("select * from UserData")
    LiveData<List<UserData>> getUserData();

    @Query("select * from UserData where UserUIID = :useruiid")
    UserData getUserDataByUserUIID(String useruiid);

    @Insert(onConflict = REPLACE)
    void addUserData(UserData data);

}
