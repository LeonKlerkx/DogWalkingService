package com.example.dogwalkingservice.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "Hond",
    foreignKeys = [
        ForeignKey(entity = Gebruiker::class, parentColumns = ["gebruikersnaam"], childColumns = ["eigenaar"])
    ])
data class Hond (

    @PrimaryKey
    @ColumnInfo("chipnummer")
    val chipNummer: String,

    @ColumnInfo("hondnaam")
    val hondnaam: String,

    @ColumnInfo("hondenras")
    val hondenras: String,

    @ColumnInfo("eigenaar")
    val eigenaar: String
)