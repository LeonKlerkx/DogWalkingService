import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.dogwalkingservice.data.DogWalkingServiceDatabase
import com.example.dogwalkingservice.data.Gebruiker
import com.example.dogwalkingservice.data.GebruikerDao
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import kotlin.jvm.Throws

@RunWith(AndroidJUnit4::class)
class GebruikerDaoTest {

    private lateinit var dogWalkingServiceDatabase: DogWalkingServiceDatabase
    private lateinit var gebruikerDao: GebruikerDao

    // Objecten om de Dao functies te testen.
    private var eigenaar1 = Gebruiker(gebruikersnaam = "LeonK", emailadres = "Leonk@gmail.com", wachtwoord = "ab1cd2", telefoonnummer = "0612345678", geboortedatum = "2000-04-04", adres = "Onbekend", postcode = "1012YI", woonplaats = "Nederland", persoonsomschrijving = null, rolnaam = "Eigenaar")
    private var oppasser1 = Gebruiker(gebruikersnaam = "PietE", emailadres = "pietE@gmail.com", wachtwoord = "fv3gb4", telefoonnummer = "0698765432", geboortedatum = "2002-11-10", adres = "Bekend", postcode = "5032IK", woonplaats = "Noord-Brabant", persoonsomschrijving = "Ik ben Piet E en ik ben een oppasser", rolnaam = "Oppasser")

    private suspend fun addOwnerToDb() {
        gebruikerDao.insert(eigenaar1)
    }

    private suspend fun addOppasserToDb() {
        gebruikerDao.insert(oppasser1)
    }

    /**
     * Create the Database before testing the code.
     */
    @Before
    fun createDatabase() {
        val context: android.content.Context = ApplicationProvider.getApplicationContext()

        dogWalkingServiceDatabase = Room.inMemoryDatabaseBuilder(context, DogWalkingServiceDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        gebruikerDao = dogWalkingServiceDatabase.gebruikerDao()
    }

    /**
     * Close the Database after testing the code.
     */
    @After
    @Throws(IOException::class)
    fun closeDatabase() {
        dogWalkingServiceDatabase.close()
    }

    /**
     * Insert user into the DB and use all get functions to check if it works.
     */
    @Test
    @Throws(IOException::class)
    fun daoInsert_InsertUserIntoDb() = runBlocking {
        addOwnerToDb()

        // Testing each function from the Dao.
        val getOwnerName = gebruikerDao.getUsername("LeonK").first()
        val getOwnerEmailAddress = gebruikerDao.getEmailAddress("Leonk@gmail.com").first()
        val getOwnerPhoneNumber = gebruikerDao.getPhoneNumber("0612345678").first()

        assertEquals(getOwnerName, eigenaar1)
        assertEquals(getOwnerEmailAddress, eigenaar1)
        assertEquals(getOwnerPhoneNumber, eigenaar1)
    }

    /**
     * Update user into the DB and check wheter user is changed.
     */
    @Test
    @Throws(IOException::class)
    fun daoUpdate_UpdateUserIntoDb() = runBlocking {
        addOwnerToDb()
        addOppasserToDb()

        // Update the email address to check whether the update function will be succesful run.
        gebruikerDao.update(Gebruiker(gebruikersnaam = "LeonK", emailadres = "leonk@hotmail.com", wachtwoord = "gptpyfp", telefoonnummer = "0648291042", geboortedatum = "1987-02-06", adres = "bloem 2", postcode = "4053OK", woonplaats = "Amsterdam", persoonsomschrijving = null, rolnaam = "Eigenaar"))
        gebruikerDao.update(Gebruiker(gebruikersnaam = "PietE", emailadres = "pietvanEl@live.nl", wachtwoord = "togvdo", telefoonnummer = "0679432210", geboortedatum = "1988-12-10", adres = "?", postcode = "3020OJ", woonplaats = "Utrecht", persoonsomschrijving = "Ik ben Mien T en ik ben een oppasser", rolnaam = "Oppasser"))

        // Get the new objects after the update functions are excecuted.
        val getNewUser1 = gebruikerDao.getUsername("LeonK").first()
        val getNewUser2 = gebruikerDao.getUsername("PietE").first()

        assertEquals(getNewUser1, Gebruiker(gebruikersnaam = "LeonK", emailadres = "leonk@hotmail.com", wachtwoord = "gptpyfp", telefoonnummer = "0648291042", geboortedatum = "1987-02-06", adres = "bloem 2", postcode = "4053OK", woonplaats = "Amsterdam", persoonsomschrijving = null, rolnaam = "Eigenaar"))
        assertEquals(getNewUser2, Gebruiker(gebruikersnaam = "PietE", emailadres = "pietvanEl@live.nl", wachtwoord = "togvdo", telefoonnummer = "0679432210", geboortedatum = "1988-12-10", adres = "?", postcode = "3020OJ", woonplaats = "Utrecht", persoonsomschrijving = "Ik ben Mien T en ik ben een oppasser", rolnaam = "Oppasser"))
    }

    /**
     * Delete users into the Database.
     */
    @Test
    @Throws(IOException::class)
    fun daoDelete_DeleteUserIntoDb() = runBlocking {
        addOwnerToDb()
        addOppasserToDb()

        gebruikerDao.delete(eigenaar1)
        gebruikerDao.delete(oppasser1)

        // Retrieve users from the Database.
        val getEigenaar = gebruikerDao.getUsername("LeonK").first()
        val getOppasser = gebruikerDao.getUsername("PietE").first()

        // Objects cannot be found, because the objects are removed in the Database.
        assertEquals(getEigenaar, null)
        assertEquals(getOppasser, null)
    }
}