package com.example.dogwalkingservice.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "AfbeeldingHond",
    foreignKeys = [
        ForeignKey(entity = Gebruiker::class, parentColumns = ["gebruikersnaam"], childColumns = ["eigenaar"]),
        ForeignKey(entity = Hond::class, parentColumns = ["chipnummer"], childColumns = ["chipnummer"])
    ])
data class AfbeeldingHond (

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("afbeelding_id")
    val afbeeldingId: Int,

    @ColumnInfo("afbeelding_url")
    val afbeeldingUrl: String,

    @ColumnInfo("eigenaar")
    val eigenaar: String,

    @ColumnInfo("chipnummer")
    val chipNummer: String
)