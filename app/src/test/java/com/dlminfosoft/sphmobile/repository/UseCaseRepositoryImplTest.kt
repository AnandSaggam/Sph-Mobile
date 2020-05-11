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
class UseCaseRepositoryImplTest {

    @Test
    fun `test_verify_getYearlyRecordResult()_return_list_of_YearlyRecord`() {
        val actualResult =
            UseCaseRepository.getYearlyRecordResult(TestUtils.getDummyUsageDataResponse())
        Assert.assertNotNull(actualResult)
        Assert.assertEquals(true, actualResult.isSuccess)
        Assert.assertEquals(
            getDummyYearlyRecordResultSuccess().recordList[0], actualResult.recordList[0]
        )
    }

    @Test
    fun `test_verify_getYearlyRecordResult()_return_isSuccess_false_on_failure_case`() {
        val actualResult =
            UseCaseRepository.getYearlyRecordResult(TestUtils.getDummyUsageDataResponseFailureCase())
        Assert.assertNotNull(actualResult)
        Assert.assertEquals(false, actualResult.isSuccess)
    }

    @Test
    fun `test_verify_getYearlyRecordResult()_return_empty_list_on_failure_case`() {
        val actualResult =
            UseCaseRepository.getYearlyRecordResult(TestUtils.getDummyUsageDataResponseFailureCase())
        Assert.assertNotNull(actualResult)
        Assert.assertEquals(false, actualResult.isSuccess)
        Assert.assertEquals(0, actualResult.recordList.size)
    }

    @Test
    fun `test_verify_getYearlyRecordResult()_return_true_for_isDecreaseVolumeData_when_decrease_quarter_volume`() {
        val actualResult =
            UseCaseRepository.getYearlyRecordResult(TestUtils.getDummyUsageDataResponseDecreaseQuarterVolume())
        Assert.assertNotNull(actualResult)
        Assert.assertEquals(true, actualResult.isSuccess)
        Assert.assertEquals(1, actualResult.recordList.size)
        Assert.assertEquals(true, actualResult.recordList[0].isDecreaseVolumeData)
        Assert.assertEquals("Q3", actualResult.recordList[0].decreaseVolumeQuarterKey)
    }

    @Test
    fun `test_verify_getYearlyRecordResult()_return_false_for_for_isDecreaseVolumeData_when_no_decrease_quarter_volume`() {
        val actualResult =
            UseCaseRepository.getYearlyRecordResult(TestUtils.getDummyUsageDataResponse())
        Assert.assertNotNull(actualResult)
        Assert.assertEquals(true, actualResult.isSuccess)
        Assert.assertEquals(false, actualResult.recordList[0].isDecreaseVolumeData)
    }
}