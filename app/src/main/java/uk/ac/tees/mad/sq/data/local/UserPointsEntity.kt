package uk.ac.tees.mad.sq.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_points")
data class UserPointsEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val points: Int
)