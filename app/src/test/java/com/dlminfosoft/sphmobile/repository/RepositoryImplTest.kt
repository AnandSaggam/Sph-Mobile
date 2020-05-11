package com.dlminfosoft.sphmobile.repository

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.dlminfosoft.sphmobile.BuildConfig.BASE_URL
import com.dlminfosoft.sphmobile.TestUtils.getDummyEmptyYearlyRecordList
import com.dlminfosoft.sphmobile.TestUtils.getDummyYearlyRecordList
import com.dlminfosoft.sphmobile.database.SphMobileDatabase
import com.dlminfosoft.sphmobile.database.YearlyRecordDao
import com.dlminfosoft.sphmobile.utility.NetManager
import com.dlminfosoft.sphmobile.webservice.IApiServiceMethods
import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.*
import org.junit.rules.ExpectedException
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.coroutines.CoroutineContext


@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
class RepositoryImplTest : CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val exceptionRule: ExpectedException = ExpectedException.none()

    private val testDispatcher = TestCoroutineDispatcher()
    private lateinit var repository: IRepository
    private lateinit var apiService: IApiServiceMethods
    private lateinit var netManager: NetManager
    private lateinit var db: SphMobileDatabase
    private lateinit var dbDao: YearlyRecordDao
    @Before
    fun setup() {
        netManager = mock()
        val context = ApplicationProvider.getApplicationContext<Context>()
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = spy(retrofit.create<IApiServiceMethods>(IApiServiceMethods::class.java))
        db = Room.inMemoryDatabaseBuilder(context, SphMobileDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dbDao = spy(db.getYearlyRecordDao())
        repository = RepositoryImpl(dbDao, apiService, netManager)
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun teardown() {
        db.close()
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `verify_makeCallToGetYearlyRecords()_return_no_record_when_internet_not_available`() {
        whenever(netManager.isConnectedToInternet).thenReturn(false)
        val actualResult = repository.makeCallToGetYearlyRecords()
        Assert.assertEquals(true, actualResult.value?.isSuccess)
        Assert.assertEquals(false, actualResult.value?.isInternetAvailable)
        Assert.assertEquals(true, actualResult.value?.recordList?.isEmpty())
    }

    @Test
    fun `verify_makeCallToGetYearlyRecords()_return_record_from_database_when_internet_not_available`() {
        whenever(netManager.isConnectedToInternet).thenReturn(false)
        testDispatcher.runBlockingTest {
            repository.insertIntoTable(getDummyYearlyRecordList())
            val actualResult = repository.makeCallToGetYearlyRecords()
            Assert.assertEquals(true, actualResult.value?.isSuccess)
            Assert.assertEquals(false, actualResult.value?.isInternetAvailable)
            Assert.assertEquals(
                getDummyYearlyRecordList().size,
                actualResult.value?.recordList?.size
            )
        }
    }

    @Test
    fun `verify_insertAll()_not_invoke_when_executing_insertIntoTable()_from_repository_and_list_is_empty`() {
        testDispatcher.runBlockingTest {
            repository.insertIntoTable(getDummyEmptyYearlyRecordList())
            verify(dbDao, never()).insertAll(getDummyEmptyYearlyRecordList())
        }
    }

    @Test
    fun `verify_deleteAllRecords()_invoke_when_executing_deleteAllRecord()_from_repository()`() {
        testDispatcher.runBlockingTest {
            repository.deleteAllRecord()
            verify(dbDao, times(1)).deleteAllRecords()
        }
    }

    @Test
    fun `verify_getAllRecordsList()_invoke_when_executing_getAllRecordsFromTable()_from_repository()`() {
        testDispatcher.runBlockingTest {
            repository.getAllRecordsFromTable()
            verify(dbDao, times(1)).getAllRecordsList()
        }
    }

    @Test
    fun `verify_insertAll()_inserted_list_of_record_successfully_into_YearlyRecords_table`() {
        testDispatcher.runBlockingTest {
            whenever(dbDao.getAllRecordsList()).thenReturn(
                getDummyYearlyRecordList()
            )
            val arr: Array<Long> = arrayOf(1, 2, 3)
            whenever(dbDao.insertAll(getDummyYearlyRecordList())).thenReturn(arr)
            val insertedResult = dbDao.insertAll(getDummyYearlyRecordList())
            val getResult = (dbDao).getAllRecordsList()
            Assert.assertEquals(insertedResult.size, getResult.size)
        }
    }

    @Test
    fun `verify_getAllRecordList()_return_exact_number_of_records_inserted_into_YearlyRecords_table`() {
        testDispatcher.runBlockingTest {
            // Arrange
            whenever(dbDao.getAllRecordsList()).thenReturn(
                getDummyYearlyRecordList()
            )
            // Act
            val insertResult =
                db.getYearlyRecordDao().insertAll(getDummyYearlyRecordList())
            val actualResult = dbDao.getAllRecordsList()
            //Assert
            Assert.assertEquals(insertResult.size, actualResult.size)
        }
    }
}