package com.example.schoolmanagementsystem

import androidx.lifecycle.ViewModelProvider.NewInstanceFactory.Companion.instance
import com.example.schoolmanagementsystem.script.UserInfo
import com.example.schoolmanagementsystem.script.backendApi
import com.example.schoolmanagementsystem.script.getContractAndPayment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import okhttp3.internal.wait
import org.junit.Test
import retrofit2.Retrofit

class APITest{
    @Test
    fun testGetUsers() {
        CoroutineScope(Dispatchers.IO).launch {
            val result = async { backendApi.getUsers() }
            val response = result.await()
            val errorBody = response.errorBody()
            assert(errorBody == null)
            //Check for success body
            val responseWrapper = response.body()
            assert(responseWrapper != null)
            assert(response.code() == 200)
        }
    }
    @Test
    fun testGetContractsPayments() {
        CoroutineScope(Dispatchers.IO).launch {
            val response = backendApi.getAllContracts()
            val errorBody = response.errorBody()
            assert(errorBody == null)
            //Check for success body
            val responseWrapper = response.body()
            assert(responseWrapper != null)
            assert(response.code() == 200)
        }
    }

    @Test
    fun testLogin() {
        CoroutineScope(Dispatchers.IO).launch {
            val adminmodel = UserInfo(BuildConfig.LoginMail, BuildConfig.LoginMdp)
            val response = backendApi.login(adminmodel)
            val errorBody = response.errorBody()
            assert(errorBody == null)
            //Check for success body
            val responseWrapper = response.body()
            assert(responseWrapper != null)
            assert(response.code() == 200)
        }
    }

    @Test
    fun testGetStudent() {
        CoroutineScope(Dispatchers.IO).launch {
            val response = backendApi.getStudents()
            val errorBody = response.errorBody()
            assert(errorBody == null)
            //Check for success body
            val responseWrapper = response.body()
            assert(responseWrapper != null)
            assert(response.code() == 200)
        }
    }

    @Test
    fun testGetGroups() {
        CoroutineScope(Dispatchers.IO).launch {
            val response = backendApi.getGroups()
            val errorBody = response.errorBody()
            assert(errorBody == null)
            //Check for success body
            val responseWrapper = response.body()
            assert(responseWrapper != null)
            assert(response.code() == 200)
        }
    }
    @Test
    fun testGetNiveau() {
        CoroutineScope(Dispatchers.IO).launch {
            val response = backendApi.getNiveau()
            val errorBody = response.errorBody()
            assert(errorBody == null)
            //Check for success body
            val responseWrapper = response.body()
            assert(responseWrapper != null)
            assert(response.code() == 200)
        }
    }

}