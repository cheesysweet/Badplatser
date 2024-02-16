package se.miun.anby2001.dt031g.bathingsites.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "BathingSitesTable", indices = [Index(value = ["latitude", "longitude"], unique = true)])
data class BathingSite(
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "description")
    val description: String?,
    @ColumnInfo(name = "address")
    val address: String?,
    @ColumnInfo(name = "latitude")
    var latitude: Double,
    @ColumnInfo(name = "longitude")
    var longitude: Double,
    @ColumnInfo(name = "grade")
    val grade: Float?,
    @ColumnInfo(name = "temp")
    val temp: Double?,
    @ColumnInfo(name = "date")
    val date: String?,

    @PrimaryKey(autoGenerate = true)
    var id: Long? = null
        ) : Parcelable