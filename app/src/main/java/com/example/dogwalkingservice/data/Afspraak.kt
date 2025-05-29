package com.example.dogwalkingservice.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "Afspraak",
    foreignKeys = [
        ForeignKey(entity = Gebruiker::class, parentColumns = ["gebruikersnaam"], childColumns = ["oppasser"])
    ])
data class Afspraak (

    @PrimaryKey
    @ColumnInfo("afspraakId")
    val afspraakId: Int,

    @ColumnInfo("beginmoment")
    val beginmoment: String,

    @ColumnInfo("eindmoment")
    val eindmoment: String,

    @ColumnInfo("oppasser")
    val oppasser: String
)