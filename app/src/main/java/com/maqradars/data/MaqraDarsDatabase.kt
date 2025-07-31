package com.maqradars.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase // Tambahkan import ini
import com.maqradars.data.dao.AyatExampleDao
import com.maqradars.data.dao.GlosariumTermDao
import com.maqradars.data.dao.MaqamDao
import com.maqradars.data.dao.UserDao
import com.maqradars.data.entity.AyatExample
import com.maqradars.data.entity.GlosariumTerm
import com.maqradars.data.entity.Maqam
import com.maqradars.data.entity.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [Maqam::class, AyatExample::class, GlosariumTerm::class, User::class],
    version = 1,
    exportSchema = false
)
abstract class MaqraDarsDatabase : RoomDatabase(){
    abstract fun maqamDao() : MaqamDao
    abstract fun ayatExampleDao() : AyatExampleDao
    abstract fun glosariumTermDao() : GlosariumTerm
    abstract fun userDao() : UserDao

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
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}