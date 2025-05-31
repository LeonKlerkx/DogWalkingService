import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.dogwalkingservice.data.AanmeldenHond
import com.example.dogwalkingservice.data.AanmeldenHondDao
import com.example.dogwalkingservice.data.Afspraak
import com.example.dogwalkingservice.data.AfspraakDao
import com.example.dogwalkingservice.data.DogWalkingServiceDatabase
import com.example.dogwalkingservice.data.Gebruiker
import com.example.dogwalkingservice.data.GebruikerDao
import com.example.dogwalkingservice.data.Hond
import com.example.dogwalkingservice.data.HondDao
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class AanmeldenHondDaoTest {

    private lateinit var dogWalkingServiceDatabase: DogWalkingServiceDatabase

    private lateinit var aanmeldenHondDao: AanmeldenHondDao
    private lateinit var afspraakDao: AfspraakDao
    private lateinit var hondDao: HondDao
    private lateinit var gebruikerDao: GebruikerDao

    /**
     * Needs to make a [Hond] object.
     */
    private var owner1 = Gebruiker("LeonK", "leon@gmail.com", "ab1cd2", "06-12345678", "2000-1-1", "Onbekend", "0000XX", "Nederland", null, "Eigenaar")

    /**
     * Needs to make an [Afspraak] object.
     */
    private var oppasser1 = Gebruiker("PietE", "pietE@gmail.com", "fv3gb4", "0698765432", "2002-11-10", "Bekend", "5032IK", "Noord-Brabant","Ik ben Piet E en ik ben een oppasser", "Oppasser")

    /**
     * Needs to make an [AanmeldenHond] object.
     */
    private var dog1 = Hond("NEDFLO570342401", "Spike", "Shiba", owner1.gebruikersnaam)
    private val dog2 = Hond("NEDSPY460166719", "Spy", "Golden Retriever", owner1.gebruikersnaam)

    /**
     * Needs to make an [AanmeldenHond] object.
     */
    private var appointment1 = Afspraak(1, "2025-2-6 13:00", "2025-2-6 18:00", oppasser1.gebruikersnaam)

    /**
     * Make an [AanmeldenHond] object to test the [AanmeldenHondDao.insert] and [AanmeldenHondDao.getAllSignInDogsFromAnAppointment] functions.
     */
    private var signUpDog1 = AanmeldenHond(appointment1.afspraakId, dog1.chipNummer)
    private var signUpDog2 = AanmeldenHond(appointment1.afspraakId, dog2.chipNummer)

    /**
     * Create the Database instance before testing the code.
     */
    @Before
    fun createDb() {
        var context: android.content.Context = ApplicationProvider.getApplicationContext()

        dogWalkingServiceDatabase = Room.inMemoryDatabaseBuilder(context, DogWalkingServiceDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        aanmeldenHondDao = dogWalkingServiceDatabase.aanmeldenHondDao()
        afspraakDao = dogWalkingServiceDatabase.afspraakDao()
        hondDao = dogWalkingServiceDatabase.hondDao()
        gebruikerDao = dogWalkingServiceDatabase.gebruikerDao()
    }

    /**
     * Close the Database Instance after testing the code.
     */
    @After
    fun closeDb() {
        dogWalkingServiceDatabase.close()
    }

    /**
     * Insert a dog to an appointment.
     */
    @Test
    @Throws(IOException::class)
    fun daoInsert_InsertSignupDogToAppointment() = runBlocking {
        // Insert an owner and an oppasser.
        gebruikerDao.insert(owner1)
        gebruikerDao.insert(oppasser1)

        // Insert a dog into the Database.
        hondDao.insert(dog1)

        // Insert an appointment into the Database.
        afspraakDao.insert(appointment1)

        // Insert a dog to the appointment.
        aanmeldenHondDao.insert(signUpDog1)

        // Get the sign up dog from the appointment.
        val getListSignUpDogFromAppointment = aanmeldenHondDao
            .getAllSignInDogsFromAnAppointment(appointment1.afspraakId) // Flow<List<AanmeldenHond>>
            .first() // List<AanmeldenHond>

        assertEquals(1, getListSignUpDogFromAppointment.size)
    }

    /**
     * Insert multiple dogs to an appointment.
     */
    @Test
    @Throws(IOException::class)
    fun daoInsert_InsertMultipleSignupDogsToAppointment() = runBlocking {
        // Insert an owner and an oppasser.
        gebruikerDao.insert(owner1)
        gebruikerDao.insert(oppasser1)

        // Insert dogs into the Database.
        hondDao.insert(dog1)
        hondDao.insert(dog2)

        // Insert an appointment into the Database.
        afspraakDao.insert(appointment1)

        // Insert dogs to the appointment.
        aanmeldenHondDao.insert(signUpDog1)
        aanmeldenHondDao.insert(signUpDog2)

        // Get the sign up dogs from the appointment.
        val getListSignUpDogFromTheAppointment = aanmeldenHondDao
            .getAllSignInDogsFromAnAppointment(appointment1.afspraakId) // Flow<List<AanmeldenHond>>
            .first() // List<AanmeldenHond>

        assertEquals(2, getListSignUpDogFromTheAppointment.size)
    }

    /**
     * Insert the same dog twice to an appointment and except an error.
     */
    @Test
    @Throws(IOException::class)
    fun daoInsert_InsertDoubleSignUpDogToAppointment_WrongPrimaryKey() = runBlocking {
        // Insert an owner and an oppasser.
        gebruikerDao.insert(owner1)
        gebruikerDao.insert(oppasser1)

        // Insert one dog into the Database.
        hondDao.insert(dog1)

        // Insert an appointment into the Database.
        afspraakDao.insert(appointment1)

        // Insert the same dog twice to an appointment.
        aanmeldenHondDao.insert(signUpDog1)
        aanmeldenHondDao.insert(signUpDog1)

        // Get the sign up dogs from the appointment.
        val getListSignUpDogsFromTheAppointment = aanmeldenHondDao
            .getAllSignInDogsFromAnAppointment(appointment1.afspraakId)
            .first()

        // You can add one dog to an appointment. The second dog will be ignored.
        assertNotEquals(2, getListSignUpDogsFromTheAppointment.size)

        // There is one dog available in the appointment.
        assertEquals(1, getListSignUpDogsFromTheAppointment.size)
    }

    /**
     * Get all sign up dogs from an appointment and show the
     * [Afspraak.beginmoment], [Afspraak.eindmoment], [Afspraak.oppasser],
     * the [Hond.hondnaam] and the [Hond.eigenaar].
     */
    @Test
    @Throws(IOException::class)
    fun daoGet_GetAllSignInDogsFromAnAppointment() = runBlocking {
        // Insert an owner and an oppasser.
        gebruikerDao.insert(owner1)
        gebruikerDao.insert(oppasser1)

        // Insert two dogs into the Database.
        hondDao.insert(dog1)
        hondDao.insert(dog2)

        // Insert an appointment into the Database.
        afspraakDao.insert(appointment1)

        // Insert two dogs to an appointment.
        aanmeldenHondDao.insert(signUpDog1)
        aanmeldenHondDao.insert(signUpDog2)

        // Get the sign up dogs from the appointment.
        val getSignUpDogsFromTheAppointment = aanmeldenHondDao
            .getAllSignInDogsFromAnAppointment(appointment1.afspraakId)
            .first()

        // There are 2 dogs sign in to the appointment.
        assertEquals(2, getSignUpDogsFromTheAppointment.size)

        // Get information of Dog 1.
        val getInfoDog1 = hondDao.getDogByPrimaryKey(dog1.chipNummer)
            .first()

        // Get information of Dog 2.
        val getInfoDog2 = hondDao.getDogByPrimaryKey(dog2.chipNummer)
            .first()

        // Get information of Appointment 1.
        val getInfoAppointment1 = afspraakDao.getAppointmentByPrimaryKey(appointment1.afspraakId)
            .first()

        assertEquals(
            "2025-2-6 13:00 - 2025-2-6 18:00 \n " +
                    " Oppasser: PietE \n " +
                    " Aangemelde honden: \n " +
                    " Spike - LeonK \n " +
                    " Spy - LeonK"
            ,
            "${getInfoAppointment1.beginmoment} - ${getInfoAppointment1.eindmoment} \n " +
                    " Oppasser: ${getInfoAppointment1.oppasser} \n " +
                    " Aangemelde honden: \n " +
                    " ${getInfoDog1.hondnaam} - ${getInfoDog1.eigenaar} \n " +
                    " ${getInfoDog2.hondnaam} - ${getInfoDog2.eigenaar}")
    }

    /**
     * Get an empty list from an appointment. Nobody has sign his dog to the appointment.
     */
    @Test
    @Throws(IOException::class)
    fun daoGet_GetAllSignInDogsFromAnAppointment_EmptyList() = runBlocking {

        // Get the sign up dogs from the appointment.
        val getListOfSignUpDogsFromTheAppointment =  aanmeldenHondDao
            .getAllSignInDogsFromAnAppointment(appointment1.afspraakId)
            .first()

        // This list has no records, because there are nothing added.
        assertEquals(0, getListOfSignUpDogsFromTheAppointment.size)
    }
}