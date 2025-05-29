package com.example.dogwalkingservice.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "Gebruiker")
data class Gebruiker (

    @PrimaryKey
    @ColumnInfo("gebruikersnaam")
    val gebruikersnaam: String,

    @ColumnInfo("emailadres")
    val emailadres: String,

    @ColumnInfo("wachtwoord")
    val wachtwoord: String,

    @ColumnInfo("telefoonnummer")
    val telefoonnummer: String,

    @ColumnInfo("geboortedatum")
    val geboortedatum: String,

    @ColumnInfo("adres")
    val adres: String,

    @ColumnInfo("postcode")
    val postcode: String,

    @ColumnInfo("woonplaats")
    val woonplaats: String,

    @ColumnInfo("persoonsomschrijving")
    val persoonsomschrijving: String?,

    @ColumnInfo("rolnaam")
    val rolnaam: String
)