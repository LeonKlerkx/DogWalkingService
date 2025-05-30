import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.dogwalkingservice.data.Afspraak
import com.example.dogwalkingservice.data.AfspraakDao
import com.example.dogwalkingservice.data.DogWalkingServiceDatabase
import com.example.dogwalkingservice.data.Gebruiker
import com.example.dogwalkingservice.data.GebruikerDao
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import kotlin.jvm.Throws

@RunWith(AndroidJUnit4::class)
class AfspraakDaoTest {

    private lateinit var dogWalkingServiceDatabase: DogWalkingServiceDatabase
    private lateinit var afspraakDao: AfspraakDao

    /**
     * Add users to reference it from the Afspraak table. FK tussen [Afspraak.oppasser]-> [Gebruiker.gebruikersnaam]
     */
    private lateinit var gebruikerDao: GebruikerDao

    private var oppasser1 = Gebruiker(gebruikersnaam = "PietE", emailadres = "Piete@gmail.com", wachtwoord = "ab1cd2", telefoonnummer = "0612395678", geboortedatum = "2000-04-04", adres = "Onbekend", postcode = "1012YI", woonplaats = "Nederland", persoonsomschrijving = null, rolnaam = "Oppasser")
    private var oppasser2 = Gebruiker(gebruikersnaam = "HenkL", emailadres = "Henkl@gmail.com", wachtwoord = "ab1cd2", telefoonnummer = "0612395678", geboortedatum = "2000-04-04", adres = "Onbekend", postcode = "1012YI", woonplaats = "Nederland", persoonsomschrijving = null, rolnaam = "Oppasser")


    // Objecten om de Dao functies te testen.
    private var appointment1 = Afspraak(1, "2025-1-6 13:00", "2025-1-6 18:00", oppasser1.gebruikersnaam)
    private var appointment2 = Afspraak(2, "2025-2-6 10:00", "2025-2-6 17:00", oppasser2.gebruikersnaam)
    private var appointment3 = Afspraak(3, "2025-3-6 14:30", "2025-3-6 16:00", oppasser2.gebruikersnaam)

    /**
     * Create the Database instance before testing the code.
     */
    @Before
    fun createDb() {
        val context: android.content.Context = ApplicationProvider.getApplicationContext()
        dogWalkingServiceDatabase = Room.inMemoryDatabaseBuilder(context, DogWalkingServiceDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        afspraakDao = dogWalkingServiceDatabase.afspraakDao()
        gebruikerDao = dogWalkingServiceDatabase.gebruikerDao()
    }

    /**
     * Close the Database Instance after testing the code.
     */
    @After
    @Throws(IOException::class)
    fun closeDb() {
        dogWalkingServiceDatabase.close()
    }

    /**
     * Insert an appointment into the Database.
     */
    @Test
    @Throws(IOException::class)
    fun daoInsert_InsertAppointment() = runBlocking {
        /* Voegt de gebruikers aan de tabel toe zodat de tabel Afspraak naar de tabel Gebruiker kan refereren.
           Foreign key constraint tussen Afspraak.oppasser -> Gebruiker.gebruikersnaam. */
        gebruikerDao.insert(oppasser1)
        gebruikerDao.insert(oppasser2)

        // Afspraken toevoegen.
        afspraakDao.insert(appointment1)
        afspraakDao.insert(appointment2)
        afspraakDao.insert(appointment3)

        val getAppointsments = afspraakDao
            .getAllAppointsmentsByOppasser("HenkL") // Flow<List<Afspraak>>
            .first() // List<Afspraak>

        // Check or the first index appointment is the same appointment or appointment2.
        assertEquals(getAppointsments[0], appointment2)

        // Check if the second oppasser in the array is the same oppasser as from appointment2.
        assertEquals(getAppointsments[1].oppasser, appointment2.oppasser)
    }

    /**
     * Insert a duplicate appointment with the same ID into the Database and except an error.
     */
    @Test
    @Throws(IOException::class)
    fun daoInsert_InsertAppointment_WrongPrimaryKey() = runBlocking {
        // Oppasser toevoegen om een afspraak aan te kunnen maken.
        gebruikerDao.insert(oppasser1)

        // Add double appointments.
        afspraakDao.insert(appointment1)
        afspraakDao.insert(appointment1)

        // Retrieve all appointments.
        val getAppointments = afspraakDao
            .getAllAppointsmentsByOppasser(oppasser1.gebruikersnaam) // Flow<List<Afspraak>>
            .first() // List<Afspraak>

        /* The size of the getAppointments is 1,
            because there is an OnConflictStrategy.IGNORE on the @Insert query of the Appointment,
            so the second insert is not valid and will be ignored (same primary key).
         */
        assertEquals(getAppointments.size, 1)

        assertNotEquals(getAppointments.size, 2)
    }

    /**
     * Get all appointments from a specific oppasser.
     */
    @Test
    @Throws(IOException::class)
    fun daoGet_getAllAppointsmentsByOppasser() = runBlocking {
        // Insert the user.
        gebruikerDao.insert(oppasser2)

        // Insert two appointments.
        afspraakDao.insert(appointment2)
        afspraakDao.insert(appointment3)

        // Select all appointments from a specific user.
        val getAllAppointmentBySpecificOppasser = afspraakDao
            .getAllAppointsmentsByOppasser(oppasser2.gebruikersnaam)
            .first()

        assertEquals(getAllAppointmentBySpecificOppasser.size, 2)
    }

    /**
     * Get the appointment from the [Afspraak.beginmoment], [Afspraak.eindmoment] en [Afspraak.oppasser].
     *
     * [Afspraak.beginmoment], [Afspraak.eindmoment] en [Afspraak.oppasser] is een unique constraint.
     */
    @Test
    @Throws(IOException::class)
    fun daoGet_GetAppointmentByStartmomentEndmomentAndOppasser() = runBlocking {
        // Oppasser toevoegen.
        gebruikerDao.insert(oppasser1)

        val newAppointment = Afspraak(1, "2025-10-6 7:30",
            "2025-10-6 11:00", oppasser1.gebruikersnaam)

        // Add an appointment.
        afspraakDao.insert(newAppointment)

        // Check of the appointment is exists in the Database.
        val getAvailableAppointment =
            afspraakDao.checkIfTheAppointmentIsExists("2025-10-6 7:30",
            "2025-10-6 11:00", oppasser1.gebruikersnaam)
                .first()

        // The new appointment is succesfull insert and queried through the Database.
        assertEquals(getAvailableAppointment, newAppointment)

        // Appointment is exists in the Database.
        assertNotEquals(getAvailableAppointment, null)
    }

    /**
     * Update an appointment into the Database.
     */
    @Test
    @Throws(IOException::class)
    fun daoUpdate_UpdateAppointmentIntoTheDatabase() = runBlocking {
        // Oppassers toevoegen. 1 voor afspraak en 1 om de afspraken de wijzigen.
        gebruikerDao.insert(oppasser1)
        gebruikerDao.insert(oppasser2)

        // Add an appointment with oppasser1.
        afspraakDao.insert(appointment1)

        // Update the appointment with another oppasser.
        afspraakDao.update(Afspraak(1, "2025-2-6 9:00", "2025-2-6 11:30", oppasser2.gebruikersnaam))

        // Get the new appointment after the update function is executed.
        val getNewAppointment = afspraakDao.getAllAppointsmentsByOppasser(oppasser2.gebruikersnaam)

        // Check if the appointment has another oppasser.
        assertEquals(getNewAppointment.first().first().oppasser, oppasser2.gebruikersnaam)
    }

    /**
     * Delete an appointment into the Database.
     */
    @Test
    @Throws(IOException::class)
    fun daoDelete_DeleteAppointmentIntoTheDatabase() = runBlocking {
        // Add oppasser to make an appointment.
        gebruikerDao.insert(oppasser1)

        // Add appointment.
        afspraakDao.insert(appointment1)

        // Remove appointment.
        afspraakDao.delete(appointment1)

        // Retrieve appointment from the Database.
        val getAppointment = afspraakDao
            .getAllAppointsmentsByOppasser(oppasser1.gebruikersnaam)
            .first()

        // Object cannot be found, because the objects are removed in the Database.
        assertEquals(getAppointment.isEmpty(), true)
    }
}