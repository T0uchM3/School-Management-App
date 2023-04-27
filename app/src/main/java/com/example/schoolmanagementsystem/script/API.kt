package com.example.schoolmanagementsystem.script

import androidx.navigation.NavHostController
import com.example.schoolmanagementsystem.script.navbar.Screen
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
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
                        println("//////////////////////////////////////")
                        sharedViewModel?.defineUser(user)
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

fun usersAPI(navCtr: NavHostController?, sharedViewModel: SharedViewModel) {
    CoroutineScope(Dispatchers.IO).launch {
        val response = backendApi.getUsers()
        withContext(Dispatchers.Main) {
            if (response.isSuccessful) {
                val users = response.body()
                sharedViewModel.defineUserList(users?.results)
                println("//////////////////////////////////////")
                users?.results?.forEach { user ->

                    println(user.name)
                }
            }
        }
    }
}