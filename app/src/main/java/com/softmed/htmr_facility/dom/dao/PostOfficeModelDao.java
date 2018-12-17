package com.softmed.htmr_facility.dom.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import com.softmed.htmr_facility.dom.objects.PostOffice;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * Created by issy on 12/12/17.
 *
 * @issyzac issyzac.iz@gmail.com
 * On Project HFReferralApp
 */

@Dao
public interface PostOfficeModelDao {

    @Query("select * from PostOffice")
    LiveData<List<PostOffice>> getPostOfficeEntries();

    @Query("select COUNT(*) from PostOffice where syncStatus = 0 ")
    int getUnpostedDataCount();

    @Query("select COUNT(*) from PostOffice where syncStatus = 1 ")
    int getPostedDataCount();

    @Query("select * from PostOffice where syncStatus = 0 ")
    List<PostOffice> getUnpostedData();

    @Query("select * from PostOffice where syncStatus = 0 ")
    LiveData<List<PostOffice>> getUnpostedLiveData();

    @Query("select * from PostOffice where syncStatus = 1")
    LiveData<List<PostOffice>> getPostedData();

    @Insert(onConflict = REPLACE)
    void addPostEntry(PostOffice postOffice);

    @Delete
    void deletePostData(PostOffice postOffice);

}
