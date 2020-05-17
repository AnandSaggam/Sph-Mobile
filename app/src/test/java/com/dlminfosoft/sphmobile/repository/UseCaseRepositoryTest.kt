package com.dlminfosoft.sphmobile.repository

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.dlminfosoft.sphmobile.TestUtils
import com.dlminfosoft.sphmobile.TestUtils.getDummyYearlyRecordResultSuccess
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

/**
 * This class contains unit test cases for
 * computing quarterly record and return yearlyRecord list
 */
@RunWith(AndroidJUnit4::class)
class UseCaseRepositoryTest {

    @Test
    fun `verify_getYearlyRecordList()_return_list_of_YearlyRecord`() {
        val actualResult =
            UseCaseRepository.getYearlyRecordList(TestUtils.getDummyUsageDataResponse())
        Assert.assertEquals(true, actualResult.isNotEmpty())
        Assert.assertEquals(getDummyYearlyRecordResultSuccess()[0], actualResult[0])
        Assert.assertEquals(1, actualResult.size)
    }

    @Test
    fun `verify_getYearlyRecordList()_return_empty_list_when_data_not_available`() {
        val actualResult =
            UseCaseRepository.getYearlyRecordList(TestUtils.getDummyUsageDataResponseWithEmptyList())
        Assert.assertNotNull(actualResult)
        Assert.assertEquals(true, actualResult.isEmpty())
    }

    @Test
    fun `verify_getYearlyRecordList()_return_true_for_isDecreaseVolumeData_when_decrease_quarter_volume`() {
        val actualResult =
            UseCaseRepository.getYearlyRecordList(TestUtils.getDummyUsageDataResponseDecreaseQuarterVolume())
        Assert.assertEquals(1, actualResult.size)
        Assert.assertEquals(true, actualResult[0].isDecreaseVolumeData)
        Assert.assertEquals("Q3", actualResult[0].decreaseVolumeQuarterKey)
    }

    @Test
    fun `verify_getYearlyRecordList()_return_false_for_isDecreaseVolumeData_when_no_decrease_quarter_volume`() {
        val actualResult =
            UseCaseRepository.getYearlyRecordList(TestUtils.getDummyUsageDataResponse())
        Assert.assertEquals(false, actualResult[0].isDecreaseVolumeData)
    }
}