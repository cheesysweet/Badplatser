package se.miun.anby2001.dt031g.bathingsites.database

import android.content.Context
import androidx.lifecycle.LiveData


class BathingSiteRepository(context: Context) {
    private val bathDao: BathingSiteDao

    init {
        val database = BathingSiteRoomDatabase.getDatabase(context)
        bathDao = database!!.bathingDao()
    }

    fun getAllBathingSites(): LiveData<List<BathingSite>> {
        return bathDao.getAllBathingSites()
    }

    fun getRandomBathingSite(rNum: Int): BathingSite {
        return bathDao.getRandomBathingSite(rNum)
    }

    fun getCount(): LiveData<Int> {
        return bathDao.countBathingSites()
    }

    fun addBathingSite(bathingSite: BathingSite): Long {
        return bathDao.insertBathingSite(bathingSite)
    }
}