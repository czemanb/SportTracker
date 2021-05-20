package hu.bme.aut.android.mysporttrackerapp.model

import android.graphics.Bitmap
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "sport")
data class SportEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    var avgSpeedInKMH: Float = 0f,
    var distanceInMeters: Int = 0,
    var timeInMillis: Long = 0L,
    var caloriesBurned: Int = 0,
    var timestamp: Long = 0L,
    var img: Bitmap? = null,
):Parcelable