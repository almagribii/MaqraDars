// app/src/main/java/com/maqradars/data/MaqraDarsDatabase.kt

package com.maqradars.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.maqradars.data.dao.*
import com.maqradars.data.entity.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [Maqam::class, AyatExample::class, GlosariumTerm::class, User::class, MaqamVariant::class],
    version = 2, // <-- TINGKATKAN VERSI INI! (dari 1 menjadi 2)
    exportSchema = false
)
abstract class MaqraDarsDatabase : RoomDatabase(){

    abstract fun maqamDao() : MaqamDao
    abstract fun ayatExampleDao() : AyatExampleDao
    abstract fun glosariumTermDao() : GlosariumTermDao
    abstract fun userDao() : UserDao
    abstract fun maqamVariantDao(): MaqamVariantDao

    companion object {
        @Volatile
        private var INSTANCE : MaqraDarsDatabase? = null

        fun getDatabase(context: Context): MaqraDarsDatabase {
            return INSTANCE?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MaqraDarsDatabase::class.java,
                    "maqradars_database"
                )
                    // TAMBAHKAN INI UNTUK MENGHAPUS DATABASE LAMA DAN MEMBUAT BARU
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}