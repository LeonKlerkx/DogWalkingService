import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.dogwalkingservice.data.DogWalkingServiceDatabase
import com.example.dogwalkingservice.data.Gebruiker
import com.example.dogwalkingservice.data.GebruikerDao
import com.example.dogwalkingservice.data.Recensie
import com.example.dogwalkingservice.data.RecensieDao
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class RecensieDaoTest {

    /**
     * Database instance.
     */
    private lateinit var dogWalkingServiceDatabase: DogWalkingServiceDatabase

    /**
     * Add users to reference it from the Recensie table. FK tussen Recensie -> Gebruiker
     */
    private lateinit var gebruikerDao: GebruikerDao
    // Objecten om de Dao functies te testen.
    private var eigenaar1 = Gebruiker(gebruikersnaam = "LeonK", emailadres = "Leonk@gmail.com", wachtwoord = "ab1cd2", telefoonnummer = "0612345678", geboortedatum = "2000-04-04", adres = "Onbekend", postcode = "1012YI", woonplaats = "Nederland", persoonsomschrijving = null, rolnaam = "Eigenaar")
    private var eigenaar2 = Gebruiker(gebruikersnaam = "MienR", emailadres = "Mienr@gmail.com", wachtwoord = "ab1cd2", telefoonnummer = "0614345678", geboortedatum = "2000-04-04", adres = "Onbekend", postcode = "1012YI", woonplaats = "Nederland", persoonsomschrijving = null, rolnaam = "Eigenaar")
    private var oppasser1 = Gebruiker(gebruikersnaam = "PietE", emailadres = "Piete@gmail.com", wachtwoord = "ab1cd2", telefoonnummer = "0612395678", geboortedatum = "2000-04-04", adres = "Onbekend", postcode = "1012YI", woonplaats = "Nederland", persoonsomschrijving = null, rolnaam = "Oppasser")

    private suspend fun addUsersToDatabase() {
        gebruikerDao.insert(eigenaar1)
        gebruikerDao.insert(eigenaar2)
        gebruikerDao.insert(oppasser1)
    }

    /**
     * RecensieDao to test it.
     */
    private lateinit var recensieDao: RecensieDao

    /**
     * Object to test the Dao functions.
     */
    private val recensie1 = Recensie("LeonK", "2025-5-29 19:13", "PietE", "Piet heeft goed op mijn hond gepassen.", 4)
    private val recensie2 = Recensie("MienR", "2025-5-30 16:30", "PietE", "Piet heeft niet goed voor mijn hond gezorgd.", 2)

    /**
     * Insert a recensie into the Database.
     */
    private suspend fun addFirstRecensieToDatabase() {
        recensieDao.insert(recensie1)
    }

    /**
     * Insert the second recensie into the Database.
     */
    private suspend fun addSecondRecensieToDatabase() {
        recensieDao.insert(recensie2)
    }

    /**
     * Create the Database instance before testing the code.
     */
    @Before
    fun createDb(){
        val context: android.content.Context = ApplicationProvider.getApplicationContext()
        dogWalkingServiceDatabase = Room.inMemoryDatabaseBuilder(context, DogWalkingServiceDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        recensieDao = dogWalkingServiceDatabase.recensieDao()

        gebruikerDao = dogWalkingServiceDatabase.gebruikerDao()
    }

    /**
     * Close the Database Instance after testing the code.
     */
    @After
    @Throws(IOException::class)
    fun closeDatabase() {
        dogWalkingServiceDatabase.close()
    }

    @Test
    @Throws(IOException::class)
    fun daoGet_GetRecensiesByOppasser() = runBlocking {
        addUsersToDatabase()

        addFirstRecensieToDatabase()
        addSecondRecensieToDatabase()

        val list = recensieDao.
        getRecensiesByOppasser("PietE") // Flow<List<Recensie>>
            .first() // List<Recensie>

        assertEquals(list[0], recensie1)
        assertEquals(list[1], recensie2)
    }
}