import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
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
class HondDaoTest {

    private lateinit var dogWalkingServiceDatabase: DogWalkingServiceDatabase
    private lateinit var hondDao: HondDao
    private lateinit var gebruikerDao: GebruikerDao

    private val owner1 = Gebruiker("LeonK", "leon@gmail.com", "ab1cd2", "06-12345678", "2000-1-1", "Onbekend", "0000XX", "Nederland", null, "Eigenaar")
    private val owner2 = Gebruiker("PietJ", "piet@gmail.com", "ef3gh4", "06-87960543", "1998-7-5", "Onbekend", "1111YY", "Nederland", null, "Eigenaar")
    private val dog1 = Hond("NEDFLO570342401", "Spike", "Shiba", owner1.gebruikersnaam)
    private val dog2 = Hond("NEDSPY460166719", "Spy", "Golden Retriever", owner1.gebruikersnaam)
    private val dog3 = Hond("NEDGUS247019698", "Guus", "Rottweiler", owner2.gebruikersnaam)

    /**
     * Create the Database instance before testing the code.
     */
    @Before
    fun createDb() {
        var context: android.content.Context = ApplicationProvider.getApplicationContext()

        dogWalkingServiceDatabase = Room.inMemoryDatabaseBuilder(context, DogWalkingServiceDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        hondDao = dogWalkingServiceDatabase.hondDao()
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
     * Insert a dog into the Database.
     */
    @Test
    @Throws(IOException::class)
    fun daoInsert_InsertDog() = runBlocking {
        // You need an owner to insert a dog. Foreign Key constraint Hond.eigenaar -> Gebruiker.gebruikersnaam.
        gebruikerDao.insert(owner1)

        hondDao.insert(dog1)

        // Get a list of the dogs from the Database.
        val getHondList = hondDao
            .getAllDogsFromOwner(dog1.eigenaar) // Flow<List<Hond>>
            .first() // List<Hond>

        assertEquals(getHondList.size, 1)
        assertEquals(getHondList[0].eigenaar, owner1.gebruikersnaam)
    }

    /**
     * Insert two dogs with the same Primary Key into the Database.
     */
    @Test
    @Throws(IOException::class)
    fun daoInsert_InsertDog_DuplicatePrimaryKey() = runBlocking {
        // You need an owner to insert a dog. Foreign Key constraint Hond.eigenaar -> Gebruiker.gebruikersnaam.
        gebruikerDao.insert(owner1)

        hondDao.insert(dog1)
        hondDao.insert(dog1)

        val getHondList = hondDao
            .getAllDogsFromOwner(dog1.eigenaar) // Flow<List<Hond>>
            .first() // List<Hond>

        /* The second insert will be ignored, because there is a OnConflictStrategy.IGNORE
           on the @Insert function and the same dog (with the same Primary Key)
            will be inserting into the Database. */
        assertNotEquals(getHondList.size, 2)

        assertEquals(getHondList.size, 1)

    }

    /**
     * Get all dogs from a specific owner.
     */
    @Test
    @Throws(IOException::class)
    fun daoGet_GetAllDogFromOwner() = runBlocking {
        // Insert owners to retrieve all dogs.
        gebruikerDao.insert(owner1)
        gebruikerDao.insert(owner2)

        // Insert the dogs.
        hondDao.insert(dog1)
        hondDao.insert(dog2)
        hondDao.insert(dog3)

        val getAllDogs = hondDao
            .getAllDogsFromOwner(owner1.gebruikersnaam)
            .first()


        assertNotEquals(getAllDogs.size, 3)

        assertEquals(getAllDogs.isEmpty(), false)
        assertEquals(getAllDogs.size, 2)
    }

    /**
     * Update a dog into the Database.
     */
    @Test
    @Throws(IOException::class)
    fun daoUpdate_UpdateDogIntoTheDatabase() = runBlocking {
        // Insert owners.
        gebruikerDao.insert(owner1)
        gebruikerDao.insert(owner2)

        // Insert a dog.
        hondDao.insert(dog3)

        // Update the dog.
        hondDao.update(Hond("NEDGUS247019698", "Basje", "Bpouvier", owner1.gebruikersnaam))

        // Get the new dog after the update function is executed.
        val getNewDog = hondDao.getAllDogsFromOwner(owner1.gebruikersnaam)
            .first()

        // Check if the original owner has no dogs.
        val dogIsChangedFromOwner = hondDao.getAllDogsFromOwner(owner2.gebruikersnaam)
            .first()

        assertEquals(getNewDog.size, 1)

        // The dog has a new owner. The original owner is gone.
        assertNotEquals(getNewDog[0].eigenaar, owner2.gebruikersnaam)
        assertEquals(dogIsChangedFromOwner.isEmpty(), true)

        // New owner of the dog.
        assertEquals(getNewDog[0].eigenaar, owner1.gebruikersnaam)
    }

    /**
     * Delete a dog into the Database.
     */
    @Test
    @Throws(IOException::class)
    fun daoDelete_DeleteDogIntoTheDatabase() = runBlocking {
        // Insert an owner from the dog.
        gebruikerDao.insert(owner1)

        // Insert a dog.
        hondDao.insert(dog1)

        // Retrieve the dog from the Database.
        val getDog = hondDao
            .getAllDogsFromOwner(owner1.gebruikersnaam)
            .first()

        // Check if there are dogs available in the Database.
        assertEquals(getDog.size, 1)

        // Remove the dog.
        hondDao.delete(dog1)

        // Retrieve all dogs of the owner from the Database.
        val getDogAfterRemoveObject = hondDao
            .getAllDogsFromOwner(owner1.gebruikersnaam)
            .first()

        // Object cannot be found, because the object are removed in the Database.
        assertEquals(getDogAfterRemoveObject.isEmpty(), true)
    }
}