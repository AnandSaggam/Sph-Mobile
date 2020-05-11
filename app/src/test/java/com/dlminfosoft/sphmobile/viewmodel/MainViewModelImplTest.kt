package com.dlminfosoft.sphmobile.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.dlminfosoft.sphmobile.TestUtils
import com.dlminfosoft.sphmobile.model.YearlyRecordResult
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
    fun `test_verify_makeCallToGetYearlyRecords()_invoke`() {
        mainViewModel = MainViewModelImpl(repository)
        Mockito.verify(repository, times(1)).makeCallToGetYearlyRecords()
    }

    @Test
    fun test_check_yearlyRecordListObservable_returns_null() {
        mainViewModel = MainViewModelImpl(repository)
        whenever(repository.makeCallToGetYearlyRecords()).thenReturn(
            MutableLiveData()
        )
        mainViewModel.getListOfData()
        val result = mainViewModel.yearlyRecordListObservable().value
        assertNull(result)
    }

    @Test
    fun test_check_yearlyRecordListObservable_returns_YearlyRecordResult() {
        // Arrange
        mainViewModel = MainViewModelImpl(repository)
        val expectedValue = YearlyRecordResult(true, ArrayList(), true)
        whenever(repository.makeCallToGetYearlyRecords()).thenReturn(
            MutableLiveData(
                YearlyRecordResult(true, ArrayList(), true)
            )
        )

        // Act
        mainViewModel.getListOfData()
        val result = mainViewModel.yearlyRecordListObservable().value

        // Assert
        assertNotNull(result)
        assertEquals(expectedValue, result)
    }

    @Test
    fun test_check_yearlyRecordListObservable_returns_YearlyRecordResult_with_YearlyRecord_list() {
        mainViewModel = MainViewModelImpl(repository)
        val expectedValue = YearlyRecordResult(true, TestUtils.getDummyYearlyRecordList(), true)
        whenever(repository.makeCallToGetYearlyRecords()).thenReturn(
            MutableLiveData(YearlyRecordResult(true, TestUtils.getDummyYearlyRecordList(), true))
        )

        mainViewModel.getListOfData()
        val result = mainViewModel.yearlyRecordListObservable().value
        assertNotNull(result)
        assertEquals(expectedValue, result)
    }
}