package com.example.dogwalkingservice.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(tableName = "AanmeldenHond",
    primaryKeys = ["afspraakId", "chipnummer"],
    foreignKeys = [
        ForeignKey(entity = Afspraak::class, parentColumns = ["afspraakId"], childColumns = ["afspraakId"]),
        ForeignKey(entity = Hond::class, parentColumns = ["chipnummer"], childColumns = ["chipnummer"])
    ])
data class AanmeldenHond (

    @ColumnInfo("afspraakId")
    val afspraakId: Int,

    @ColumnInfo("chipnummer")
    val chipNummer: String
)