package com.dlminfosoft.sphmobile.utility

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nhaarman.mockitokotlin2.mock
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`

@RunWith(AndroidJUnit4::class)
class NetManagerTest {
    private lateinit var context: Context
    private lateinit var netManager: NetManager
    private lateinit var cManager: ConnectivityManager
    private lateinit var networkInfo: NetworkInfo

    @Before
    fun setup() {
        context = mock()
        netManager = NetManager(context)
        cManager = mock()
        networkInfo = mock()
        `when`(context.getSystemService(Context.CONNECTIVITY_SERVICE))
            .thenReturn(cManager)
    }

    @Test
    fun check_is_internet_connection_available() {
        `when`(cManager.activeNetworkInfo).thenReturn(networkInfo)
        `when`(networkInfo.isConnected).thenReturn(true)
        val actualResult = netManager.isConnectedToInternet
        Assert.assertEquals(true, actualResult)
    }

    @Test
    fun check_is_internet_connection_not_available() {
        `when`(cManager.activeNetworkInfo).thenReturn(networkInfo)
        `when`(networkInfo.isConnected).thenReturn(false)
        val actualResult = netManager.isConnectedToInternet
        Assert.assertEquals(false, actualResult)
    }

    @Test
    fun check_is_internet_connection_not_available_and_network_info_is_null() {
        `when`(cManager.activeNetworkInfo).thenReturn(null)
        val actualResult = netManager.isConnectedToInternet
        Assert.assertNull(cManager.activeNetworkInfo)
        Assert.assertEquals(false, actualResult)
    }
}