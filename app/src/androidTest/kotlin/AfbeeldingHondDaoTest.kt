import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.dogwalkingservice.data.AfbeeldingHond
import com.example.dogwalkingservice.data.AfbeeldingHondDao
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
import kotlin.jvm.Throws

@RunWith(AndroidJUnit4::class)
class AfbeeldingHondDaoTest {

    private lateinit var dogWalkingServiceDatabase: DogWalkingServiceDatabase

    private lateinit var afbeeldingHondDao: AfbeeldingHondDao
    private lateinit var gebruikerDao: GebruikerDao
    private lateinit var hondDao: HondDao

    /**
     * Needs to make a [Hond] and an [AfbeeldingHond] object.
     */
    private var owner1 = Gebruiker("LeonK", "leon@gmail.com", "ab1cd2", "06-12345678", "2000-1-1", "Onbekend", "0000XX", "Nederland", null, "Eigenaar")

    /**
     * Needs to make an [AfbeeldingHond] object.
     */
    private var dog1 = Hond("NEDFLO570342401", "Spike", "Shiba", owner1.gebruikersnaam)
    private val dog2 = Hond("NEDSPY460166719", "Spy", "Golden Retriever", owner1.gebruikersnaam)

    /**
     * Make an [AfbeeldingHond] object to test the [AfbeeldingHondDao.insert], [AfbeeldingHondDao.getDogPictureByChipnummer] and the [AfbeeldingHondDao.delete] functions.
     */
    private var dogPicture1 = AfbeeldingHond(1, "https://images.dog.ceo/breeds/hound-afghan/n02088094_1003.jpg", owner1.gebruikersnaam, dog1.chipNummer)
    private var dogPicture2 = AfbeeldingHond(2, "https://images.dog.ceo/breeds/hound-afghan/n02088094_1459.jpg", owner1.gebruikersnaam, dog1.chipNummer)
    private var dogPicture3 = AfbeeldingHond(3, "https://images.dog.ceo/breeds/hound-afghan/n02088094_1128.jpg", owner1.gebruikersnaam, dog2.chipNummer)

    /**
     * Create the Database before testing the code.
     */
    @Before
    fun createDb() {
        val context: android.content.Context = ApplicationProvider.getApplicationContext()

        dogWalkingServiceDatabase = Room.inMemoryDatabaseBuilder(context, DogWalkingServiceDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        afbeeldingHondDao = dogWalkingServiceDatabase.afbeeldingHondDao()
        gebruikerDao = dogWalkingServiceDatabase.gebruikerDao()
        hondDao = dogWalkingServiceDatabase.hondDao()
    }

    /**
     * Close the Database after testing the code.
     */
    @After
    fun closeDb() {
        dogWalkingServiceDatabase.close()
    }

    /**
     * Insert a picture of a dog into the Database.
     */
    @Test
    @Throws(IOException::class)
    fun daoInsert_InsertDogPicture() = runBlocking {
        // Insert an owner of the dog into the Database.
        gebruikerDao.insert(owner1)

        // Insert a dog into the Database.
        hondDao.insert(dog1)

        // Insert a picture of Dog1 into the Database
        afbeeldingHondDao.insert(dogPicture1)

        // Get a list of all pictures of the dog from the Database.
        val getDogPictureFromTheDatabase = afbeeldingHondDao
            .getDogPictureByChipnummer(dog1.chipNummer) // Flow<List<AfbeeldingHond>>
            .first() // List<AfbeeldingHond>

        assertEquals(1, getDogPictureFromTheDatabase.size)
    }

    /**
     * Insert the same object twice and except an error.
     */
    @Test
    @Throws(IOException::class)
    fun daoInsert_InsertTheSameObject_WrongPrimaryKey() = runBlocking {
        // Insert an owner of the dog into the Database.
        gebruikerDao.insert(owner1)

        // Insert a dog into the Database.
        hondDao.insert(dog1)

        // Insert the same picture of the dog twice into the Database.
        afbeeldingHondDao.insert(dogPicture1)
        afbeeldingHondDao.insert(dogPicture1)

        // Get a list of all pictures of the dog from the Database.
        val getDogPictureFromTheDatabase = afbeeldingHondDao
            .getDogPictureByChipnummer(dog1.chipNummer)
            .first()

        assertNotEquals(2, getDogPictureFromTheDatabase.size)

        assertEquals(1, getDogPictureFromTheDatabase.size)
    }

    /**
     * Get all pictures of the dog with the same chip number.
     */
    @Test
    @Throws(IOException::class)
    fun daoGet_GetAllPicturesFromChipNumber() = runBlocking {
        // Insert an owner of the dog into the Database.
        gebruikerDao.insert(owner1)

        // Insert a dog into the Database.
        hondDao.insert(dog1)

        // Insert multiple pictures of the dog into the Database.
        afbeeldingHondDao.insert(dogPicture1)
        afbeeldingHondDao.insert(dogPicture2)

        // Get a list of all pictures of the dog from the Database.
        val getAllDogPicturesFromTheDatabase = afbeeldingHondDao
            .getDogPictureByChipnummer(dog1.chipNummer)
            .first()

        assertEquals(2, getAllDogPicturesFromTheDatabase.size)
    }

    /**
     * Delete the dog picture into the Database.
     */
    @Test
    @Throws(IOException::class)
    fun daoDelete_DeleteDogPicture() = runBlocking {
        // Insert an owner of the dog into the database.
        gebruikerDao.insert(owner1)

        // Insert a dog into the Database.
        hondDao.insert(dog2)

        // Insert a picture of the dog into the Database.
        afbeeldingHondDao.insert(dogPicture3)

        // Get a list of all dog pictures from the Database.
        val getDogPictureFromTheDatabase = afbeeldingHondDao
            .getDogPictureByChipnummer(dog2.chipNummer)
            .first()

        assertEquals(1, getDogPictureFromTheDatabase.size)

        // Delete the dog from the Database.
        afbeeldingHondDao.delete(dogPicture3)

        // Get a list of all dog pictures from the Database.
        val getNewDogPicturesFromTheDatabase = afbeeldingHondDao
            .getDogPictureByChipnummer(dog2.chipNummer)
            .first()

        assertEquals(0, getNewDogPicturesFromTheDatabase.size)
    }
}