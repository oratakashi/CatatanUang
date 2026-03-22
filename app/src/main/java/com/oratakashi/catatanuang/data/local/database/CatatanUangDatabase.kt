package com.oratakashi.catatanuang.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.oratakashi.catatanuang.data.local.dao.TransaksiDao
import com.oratakashi.catatanuang.data.local.entity.TransaksiEntity

@Database(
    entities = [TransaksiEntity::class],
    version = 1,
    exportSchema = false
)
abstract class CatatanUangDatabase : RoomDatabase() {

    abstract fun transaksiDao(): TransaksiDao

    companion object {
        @Volatile
        private var INSTANCE: CatatanUangDatabase? = null

        fun getInstance(context: Context): CatatanUangDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CatatanUangDatabase::class.java,
                    "catatan_uang.db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

