package hu.bme.aut.android.mysporttrackerapp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import hu.bme.aut.android.mysporttrackerapp.model.SportEntity

@Database(
    version = 2,
    entities = [SportEntity::class],
    exportSchema = false,

)
@TypeConverters(ImageConverter::class)
abstract class Database : RoomDatabase() {

    abstract fun getSportDao(): SportDao
}