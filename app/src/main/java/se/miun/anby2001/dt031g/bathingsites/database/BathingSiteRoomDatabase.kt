package se.miun.anby2001.dt031g.bathingsites.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [BathingSite::class], version = 2, exportSchema = false)
abstract class BathingSiteRoomDatabase: RoomDatabase() {
    abstract fun bathingDao(): BathingSiteDao

    companion object {
        private const val DATABASE = "BATH_DATABASE"

        @Volatile
        private var INSTANCE: BathingSiteRoomDatabase? = null

        fun getDatabase(context: Context): BathingSiteRoomDatabase? {
            if (INSTANCE == null) {
                synchronized(BathingSiteRoomDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            BathingSiteRoomDatabase::class.java, DATABASE
                        )
                            .fallbackToDestructiveMigration()
                            .build()
                    }
                }
            }
            return INSTANCE
        }
    }
}