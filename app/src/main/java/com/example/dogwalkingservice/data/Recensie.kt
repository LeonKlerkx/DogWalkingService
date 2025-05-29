package com.example.dogwalkingservice.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(tableName = "Recensie",
    primaryKeys = ["eigenaar", "moment"],
    foreignKeys = [
        ForeignKey(entity = Gebruiker::class, parentColumns = ["gebruikersnaam"], childColumns = ["eigenaar"]),
        ForeignKey(entity = Gebruiker::class, parentColumns = ["gebruikersnaam"], childColumns = ["oppasser"])
    ])
data class Recensie (
    @ColumnInfo("eigenaar")
    val eigenaar: String,

    @ColumnInfo("moment")
    val moment: String,

    @ColumnInfo("oppasser")
    val oppasser: String,

    @ColumnInfo("recensiebeschrijving")
    val recensiebeschrijving: String,

    @ColumnInfo("waardering")
    val waardering: Int
)
