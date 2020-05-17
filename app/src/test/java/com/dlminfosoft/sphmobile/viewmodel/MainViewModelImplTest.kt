package com.dlminfosoft.sphmobile.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.dlminfosoft.sphmobile.TestUtils
import com.dlminfosoft.sphmobile.model.MainApiResponse
import com.dlminfosoft.sphmobile.repository.RepositoryImpl
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito

/**
 * This class contains unit test MainViewModelImpl class
 * covered test cases for different livedata value
 */

@RunWith(AndroidJUnit4::class)
class MainViewModelImplTest {
    private lateinit var repository: RepositoryImpl
    private lateinit var mainViewModel: IMainViewModel
    @Before
    fun setup() {
        repository = mock()
    }

    @Test
    fun `test_verify_fetchDataFromServerOrDb()_invoke`() {
        mainViewModel = MainViewModelImpl(repository)
        Mockito.verify(repository, times(1)).fetchDataFromServerOrDb()
    }

    @Test
    fun `test_check_fetchDataFromServerOrDb()_returns_null`() {
        mainViewModel = MainViewModelImpl(repository)
        whenever(repository.fetchDataFromServerOrDb()).thenReturn(
            MutableLiveData()
        )
        mainViewModel.fetchDataFromRepo()
        val result = mainViewModel.getObservableMainApiResponse().value
        assertNull(result)
    }

    @Test
    fun test_check_mainResponseLiveData_returns_list_of_YearlyRecord() {
        mainViewModel = MainViewModelImpl(repository)
        val expectedValue = TestUtils.getDummyYearlyRecordList()
        whenever(repository.fetchDataFromServerOrDb()).thenReturn(
            MutableLiveData(
                MainApiResponse(
                    MutableLiveData(TestUtils.getDummyYearlyRecordList()), MutableLiveData()
                )
            )
        )

        mainViewModel.fetchDataFromRepo()
        val result =
            mainViewModel.getObservableMainApiResponse().value?.yearlyRecordListLiveData?.value
        assertNotNull(result)
        assertEquals(expectedValue, result)
        assertEquals(expectedValue.size, result?.size)
    }

    @Test
    fun test_check_mainResponseLiveData_returns_empty_list_with_error_msg_no_data() {
        mainViewModel = MainViewModelImpl(repository)
        whenever(repository.fetchDataFromServerOrDb()).thenReturn(
            MutableLiveData(
                MainApiResponse(
                    MutableLiveData(ArrayList()),
                    MutableLiveData(Error("No records available to display"))
                )
            )
        )

        mainViewModel.fetchDataFromRepo()
        val resultErrorMsg =
            mainViewModel.getObservableMainApiResponse()
                .value?.errorLiveData?.value?.message.toString()
        assertTrue(
            mainViewModel.getObservableMainApiResponse().value?.yearlyRecordListLiveData?.value?.isEmpty()
                ?: false
        )
        assertEquals("No records available to display", resultErrorMsg)
    }

    @Test
    fun test_check_mainResponseLiveData_returns_Error_liveData_with_no_internet_message() {
        // Arrange
        mainViewModel = MainViewModelImpl(repository)
        whenever(repository.fetchDataFromServerOrDb()).thenReturn(
            MutableLiveData(
                MainApiResponse(
                    MutableLiveData(),
                    MutableLiveData(Error("Please check internet connection and try again"))
                )
            )
        )

        // Act
        mainViewModel.fetchDataFromRepo()
        val result =
            mainViewModel.getObservableMainApiResponse()
                .value?.errorLiveData?.value?.message.toString()

        // Assert
        assertNotNull(result)
        assertEquals("Please check internet connection and try again", result)
    }

    @Test
    fun test_check_mainResponseLiveData_returns_Error_liveData_with_something_wrong_message() {
        // Arrange
        mainViewModel = MainViewModelImpl(repository)
        whenever(repository.fetchDataFromServerOrDb()).thenReturn(
            MutableLiveData(
                MainApiResponse(
                    MutableLiveData(),
                    MutableLiveData(Error("Something went wrong please try again"))
                )
            )
        )

        // Act
        mainViewModel.fetchDataFromRepo()
        val result = mainViewModel.getObservableMainApiResponse()
            .value?.errorLiveData?.value?.message.toString()
        // Assert
        assertNotNull(result)
        assertEquals("Something went wrong please try again", result)
    }
}