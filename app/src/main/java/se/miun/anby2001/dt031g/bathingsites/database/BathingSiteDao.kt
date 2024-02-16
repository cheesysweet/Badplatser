package se.miun.anby2001.dt031g.bathingsites.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface BathingSiteDao {

    @Query("SELECT * FROM BathingSitesTable ORDER BY name")
    fun getAllBathingSites(): LiveData<List<BathingSite>>

    @Query("SELECT * FROM BathingSitesTable LIMIT 1 OFFSET :rNum")
    fun getRandomBathingSite(rNum: Int): BathingSite

    @Query("SELECT COUNT(*) FROM BathingSitesTable")
    fun countBathingSites(): LiveData<Int>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertBathingSite(bathSite: BathingSite): Long
}