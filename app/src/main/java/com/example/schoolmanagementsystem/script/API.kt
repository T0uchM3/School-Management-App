package com.example.schoolmanagementsystem.script

import androidx.navigation.NavHostController
import com.example.schoolmanagementsystem.script.navbar.Screen
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path


@JsonClass(generateAdapter = true)
data class UserInfo(
    // on below line we are creating variables for name and job
    @Json(name = "email")
    var email: String,
    @Json(name = "password")
    var password: String
)


///Boilerplate.
val moshi: Moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()
val loggingInterceptor = HttpLoggingInterceptor()
val okHttpClient = OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()
val retrofit: Retrofit = Retrofit.Builder()
    .baseUrl("http://10.0.2.2:8000/")
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .client(okHttpClient)
    .build()
val backendApi: APIService = retrofit.create(APIService::class.java)

///Services we're using.
interface APIService {
    @Headers("Accept: application/json")
    @POST("/api/login")
    suspend fun login(@Body params: UserInfo): Response<User>

    @GET("/api/users")
    suspend fun getUsers(): Response<UserResults>

    @Headers("Accept: application/json")
    @POST("/api/add")
    suspend fun addUser(@Body params: User): Response<String>

    @Headers("Accept: application/json")
    @POST("/api/delete/{id}")
    suspend fun deleteUser(@Path("id") id: Int): Response<String>

    @Headers("Accept: application/json")
    @POST("/api/updateUser/{id}")
    suspend fun updateUser(@Path("id") id: Int, @Body params: User): Response<User>

    @Headers("Accept: application/json")
    @POST("/api/addContract")
    suspend fun addContract(@Body params: Contract): Response<Contract>

    @Headers("Accept: application/json")
    @GET("/api/contract/{id}")
    suspend fun getContracts(@Path("id") id: Int): Response<List<Contract>>

    @Headers("Accept: application/json")
    @GET("/api/contracts")
    suspend fun getAllContracts(): Response<ContractResults>

    @Headers("Accept: application/json")
    @POST("/api/deleteContract/{id}")
    suspend fun deleteContract(@Path("id") id: Int): Response<String>

    @Headers("Accept: application/json")
    @POST("/api/updateContract/{id}")
    suspend fun updateContract(@Path("id") id: Int, @Body params: PeriodHolder): Response<Contract>

    @Headers("Accept: application/json")
    @POST("/api/invalidContract/{id}")
    suspend fun invalidContract(@Path("id") id: Int): Response<String>

    @Headers("Accept: application/json")
    @POST("/api/addPayment")
    suspend fun addPayment(@Body params: Payment): Response<Payment>

    @Headers("Accept: application/json")
    @POST("/api/deletePayment/{id}")
    suspend fun deletePayment(@Path("id") id: Int): Response<String>

    @Headers("Accept: application/json")
    @POST("/api/updatePayment/{id}")
    suspend fun updatePayment(@Path("id") id: Int, @Body params: Payment): Response<String>


}


fun loginAPI(navCtr: NavHostController?, sharedViewModel: SharedViewModel) {
    loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

    val usermodel = UserInfo("user1@gmail.com", "123123123")
    val adminmodel = UserInfo("admin@gmail.com", "111111111")
//    var isSuccessful by remember { mutableStateOf(false) }
//    val jsonAdapter = moshi.adapter<User>(User::class.java)
//    val retrofit2: RetrofitAPI = retrofit.create(RetrofitAPI::class.java)
//    val backendApi = retrofit.create(APIService::class.java)
//    LaunchedEffect(key1 = Unit) {
    // Your coroutine code here
    CoroutineScope(Dispatchers.IO).launch {
        val response = backendApi.login(adminmodel)
        withContext(Dispatchers.Main) {
            try {
                if (response.isSuccessful) {
                    val user = response.body()

                    if (user != null) {
                        sharedViewModel.defineUser(user)
                        if (user.role == "teacher") {

                            println("TEACHER ")
                            navCtr?.currentBackStackEntry?.arguments?.apply {
                                putString("name", user.name)
                                putString("id", user.id.toString())
                                putString(
                                    "role", user.role
                                )
                            }
                        }
                        if (user.role == "staff") {
                            println("STAFF")
                            navCtr?.currentBackStackEntry?.arguments?.apply {
                                putString("name", user.name)
                                putString("id", user.id.toString())
                                putString(
                                    "role", user.role
                                )
                            }
                        }
                        if (user.role == "admin") {
                            println("ADMIN")
                            println("name= " + user.name)
                            navCtr?.popBackStack()
                            navCtr?.navigate(Screen.NavBar.route)
                        }
                    }

                } else {
                    println("Error: ${response.toString()}")
                    println("Error2: ${response.errorBody().toString()}")
                }
            } catch (e: HttpException) {
                println("Exception ${e.message}")
            } catch (e: Throwable) {
                println("Oops: Something else went wrong ${e.message}")
            }

        }
    }
//    }

}

fun usersAPI(sharedViewModel: SharedViewModel) {
    CoroutineScope(Dispatchers.IO).launch {
        val result = async { backendApi.getUsers() }
        val response = result.await()
        if (response.isSuccessful) {
            val users = response.body()
            sharedViewModel.defineUserList(users?.results)
            users?.results?.forEach { user ->

                println(user.name)
            }
            getContractAndPayment(sharedViewModel = sharedViewModel)
        }
    }
}

fun addUserAPI(
    user: User,
    sharedViewModel: SharedViewModel,
    triggerSecondCall: Boolean? = false
) {
    CoroutineScope(Dispatchers.IO).launch {
        val result = async { backendApi.addUser(user) }
        val response = result.await()
        if (response.isSuccessful) {
            println("USER ADDED")
            if (triggerSecondCall == true)
                usersAPI(sharedViewModel = sharedViewModel)
        }
    }
}

fun deleteUserAPI(id: Int) {
    CoroutineScope(Dispatchers.IO).launch {
        val response = backendApi.deleteUser(id)
        withContext(Dispatchers.Main) {
            if (response.isSuccessful) {
                println("USER DELETED")
            }
        }
    }
}

fun updateUser(
    id: Int, user: User,
    sharedViewModel: SharedViewModel,
    triggerSecondCall: Boolean? = false
) {
    CoroutineScope(Dispatchers.IO).launch {
        val result = async { backendApi.updateUser(id, user) }
        val response = result.await()
        if (response.isSuccessful) {
            println("USER updated")
            if (triggerSecondCall == true)
                usersAPI(sharedViewModel = sharedViewModel)
        }
    }
}

fun getUserContract(id: Int, sharedViewModel: SharedViewModel) {
    CoroutineScope(Dispatchers.IO).launch {
        val response = backendApi.getContracts(id)
        withContext(Dispatchers.Main) {
            if (response.isSuccessful) {
                val contracts = response.body()
                sharedViewModel.defineContractList(contracts)
                println("userContractAPI")
                contracts?.forEach { contract ->

                    println(contract.id)
                }
            }
        }
    }
}

fun getContractAndPayment(sharedViewModel: SharedViewModel) {
    CoroutineScope(Dispatchers.IO).launch {
        val response = backendApi.getAllContracts()
        withContext(Dispatchers.Main) {
            if (response.isSuccessful) {
                val contracts = response.body()
                sharedViewModel.defineContractList(contracts?.resultsC)
                sharedViewModel.definePaymentList(contracts?.resultsP)
                println("userContractAPI")
//                contracts?.results?.forEach { contract ->
//
//                    println(contract.id)
//                }
            }
        }
    }
}

fun deleteContract(id: Int) {
    CoroutineScope(Dispatchers.IO).launch {
        val response = backendApi.deleteContract(id)
        withContext(Dispatchers.Main) {
            if (response.isSuccessful) {
                println("contract DELETED")
            }
        }
    }
}

fun updateContract(id: Int, period: PeriodHolder, sharedViewModel: SharedViewModel) {
    CoroutineScope(Dispatchers.IO).launch {
        val result = async { backendApi.updateContract(id, period) }
        val response = result.await()
        if (response.isSuccessful) {
            println("Contract updated")
            getContractAndPayment(sharedViewModel = sharedViewModel)
        }
    }
}

fun invalidContract(id: Int) {
    CoroutineScope(Dispatchers.IO).launch {
        val response = backendApi.invalidContract(id)
        withContext(Dispatchers.Main) {
            if (response.isSuccessful) {
                println("contract invalidated")
            }
        }
    }
}

fun addContract(
    contract: Contract,
    sharedViewModel: SharedViewModel,
    triggerSecondCall: Boolean? = false
) {
    CoroutineScope(Dispatchers.IO).launch {
        val result = async { backendApi.addContract(contract) }
        val response = result.await()
        if (response.isSuccessful) {
            println("Contract ADDED")
            if (triggerSecondCall == true)
                getContractAndPayment(sharedViewModel = sharedViewModel)
        }
    }
}

fun addPayment(
    payment: Payment, sharedViewModel: SharedViewModel,
    triggerSecondCall: Boolean? = false
) {
    CoroutineScope(Dispatchers.IO).launch {
        val result = async { backendApi.addPayment(payment) }
        val response = result.await()
        if (response.isSuccessful) {
            println("payment ADDED")
            if (triggerSecondCall == true)
                getContractAndPayment(sharedViewModel = sharedViewModel)
        }
    }
}

fun deletePayment(id: Int) {
    CoroutineScope(Dispatchers.IO).launch {
        val response = backendApi.deletePayment(id)
        withContext(Dispatchers.Main) {
            if (response.isSuccessful) {
                println("payment DELETED")
            }
        }
    }
}

fun updatePayment(id: Int, payment: Payment) {
    CoroutineScope(Dispatchers.IO).launch {
        val response = backendApi.updatePayment(id, payment)
        withContext(Dispatchers.Main) {
            if (response.isSuccessful) {
                println("payment updated")
            }
        }
    }
}

