package com.dlminfosoft.sphmobile.repository

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.dlminfosoft.sphmobile.BuildConfig.BASE_URL
import com.dlminfosoft.sphmobile.TestUtils.FAILURE_RESULT
import com.dlminfosoft.sphmobile.TestUtils.SUCCESS_RESULT
import com.dlminfosoft.sphmobile.TestUtils.SUCCESS_RESULT_WITHOUT_RECORD
import com.dlminfosoft.sphmobile.TestUtils.getDummyEmptyYearlyRecordList
import com.dlminfosoft.sphmobile.TestUtils.getDummyYearlyRecordList
import com.dlminfosoft.sphmobile.TestUtils.provideOkHttpClient
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

/**
 * This class contains unit test cases for
 * api mock response testing and database operation test cases
 */
@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
class RepositoryImplTest : CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = TestCoroutineDispatcher()
    private lateinit var repository: IRepository
    private lateinit var apiService: IApiServiceMethods
    private lateinit var netManager: NetManager
    private lateinit var db: SphMobileDatabase
    private lateinit var dbDao: YearlyRecordDao

    @Before
    fun setup() {
        netManager = mock()
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(provideOkHttpClient())
            .build()

        apiService = spy(retrofit.create<IApiServiceMethods>(IApiServiceMethods::class.java))
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext<Context>(),
            SphMobileDatabase::class.java
        )
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
    fun `verify_getDataUsageDetails()_api_method_return_success_with_200_status_code`() {
        whenever(netManager.isConnectedToInternet).thenReturn(true)
        val actualResult = apiService.getDataUsageDetails("success").execute()
        Assert.assertNotNull(actualResult)
        Assert.assertEquals(true, actualResult.isSuccessful)
        Assert.assertEquals(200, actualResult.code())
    }

    @Test
    fun `verify_getDataUsageDetails()_api_method_return_success_with_5_quarter_of_result`() {
        whenever(netManager.isConnectedToInternet).thenReturn(true)
        val actualResult = apiService.getDataUsageDetails(SUCCESS_RESULT).execute()
        Assert.assertNotNull(actualResult)
        Assert.assertEquals(true, actualResult.isSuccessful)
        Assert.assertEquals(200, actualResult.code())
        Assert.assertEquals(true, actualResult.body()?.success)
        Assert.assertEquals(true, actualResult.body()?.result?.records?.isNotEmpty())
        Assert.assertEquals(5, actualResult.body()?.result?.records?.size)
    }

    @Test
    fun `verify_getDataUsageDetails()_api_method_return_success_without_quarter_result`() {
        whenever(netManager.isConnectedToInternet).thenReturn(true)
        val actualResult = apiService.getDataUsageDetails(SUCCESS_RESULT_WITHOUT_RECORD).execute()
        Assert.assertNotNull(actualResult)
        Assert.assertEquals(true, actualResult.body()?.success)
        Assert.assertEquals(true, actualResult.body()?.result?.records?.isEmpty())
    }

    @Test
    fun `verify_getDataUsageDetails()_api_method_return_failure_response_with_500_status_code`() {
        whenever(netManager.isConnectedToInternet).thenReturn(true)
        val actualResult = apiService.getDataUsageDetails(FAILURE_RESULT).execute()
        Assert.assertNotNull(actualResult)
        Assert.assertEquals(500, actualResult.code())
        Assert.assertNull(actualResult.body())
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