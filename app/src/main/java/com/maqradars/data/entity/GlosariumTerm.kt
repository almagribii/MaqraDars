package com.maqradars.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.zone.ZoneOffsetTransitionRule.TimeDefinition

@Entity(tableName = "glosarium_terms")
data class GlosariumTerm(
    @PrimaryKey(autoGenerate = true)
    val id : Long = 0,

    @ColumnInfo(name = "term")
    val term: String,

    @ColumnInfo(name = "definition")
    val definition: String
)