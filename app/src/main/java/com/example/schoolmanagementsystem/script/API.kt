package com.example.schoolmanagementsystem.script

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import com.example.schoolmanagementsystem.BuildConfig
import com.example.schoolmanagementsystem.script.navbar.Screen
import com.example.schoolmanagementsystem.ui.theme.localStudentList
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.PartMap
import retrofit2.http.Path
import java.io.File
import java.lang.Exception


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

    //check if running on emulator or not and on local server or not
    .baseUrl(
        if (BuildConfig.DEV.toBoolean()) {
            if (Build.HARDWARE == "ranchu") "http://10.0.2.2:8000/" else
                "http://192.168.1.7:8000"
        } else
            BuildConfig.Host
    )
    .addConverterFactory(MoshiConverterFactory.create(moshi).asLenient())
    .client(okHttpClient)
    .build()
val backendApi: APIService = retrofit.create(APIService::class.java)

///Services we're using.
interface APIService {
    @Headers("Accept: application/json")
    @POST("/api/login")
    suspend fun login(@Body params: UserInfo): Response<User>

    @GET("/api/users")
    suspend fun getUsers(): Response<List<User>>

    @GET("/api/allUsers")
    suspend fun getAllUsers(): Response<List<User>>

    @Headers("Accept: application/json")
    @Multipart
    @POST("/api/add")
    suspend fun addUser(
        @Part("name") name: RequestBody,
        @Part("email") email: RequestBody,
        @Part("cin") cin: RequestBody,
        @Part("date_naiss") date_naiss: RequestBody,
        @Part("sex") sex: RequestBody,
        @Part("bank") bank: RequestBody,
        @Part("tel") tel: RequestBody,
        @Part("rib") rib: RequestBody,
        @Part("poste") poste: RequestBody,
        @Part("adresse") adresse: RequestBody,
        @Part("role") role: RequestBody,
        @Part("password") password: RequestBody,
        @Part photo: MultipartBody.Part
    ): Response<User>

    @Headers("Accept: application/json")
    @POST("/api/delete/{id}")
    suspend fun deleteUser(@Path("id") id: Int): Response<String>

    @Headers("Accept: application/json")
    @Multipart
    @POST("/api/updateUser/{id}")
    suspend fun updateUser(
        @Path("id") id: Int,
        @Part("name") name: RequestBody,
        @Part("email") email: RequestBody,
        @Part("cin") cin: RequestBody,
        @Part("date_naiss") date_naiss: RequestBody,
        @Part("sex") sex: RequestBody,
        @Part("bank") bank: RequestBody,
        @Part("tel") tel: RequestBody,
        @Part("rib") rib: RequestBody,
        @Part("poste") poste: RequestBody,
        @Part("adresse") adresse: RequestBody,
        @Part("role") role: RequestBody,
        @Part("password") password: RequestBody,
        @Part photo: MultipartBody.Part
    ): Response<User>

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
    suspend fun updateContract(
        @Path("id") id: Int,
        @Body params: PeriodHolder
    ): Response<Contract>

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
    suspend fun updatePayment(
        @Path("id") id: Int,
        @Body params: Payment
    ): Response<String>

    @Headers("Accept: application/json")
    @GET("/api/getMessageAPI/{id}")
    suspend fun getMessage(
        @Path("id") id: Int
    ): Response<List<Message>>

    @Headers("Accept: application/json")
    @POST("/api/sendMessageAPI/{id}")
    suspend fun sendMessage(@Path("id") id: Int, @Body params: MessageToSend): Response<String>

    @Headers("Accept: application/json")
    @POST("/api/vuMessageAPI")
    suspend fun updateMessagesView(
        @Body params: MessagesIds
    ): Response<String>

    @Headers("Accept: application/json")
    @POST("/api/deleteToMessage/{id}")
    suspend fun deleteFromThisSide(
        @Path("id") id: Int
    ): Response<String>

    @Headers("Accept: application/json")
    @POST("/api/deleteFromMessage/{id}")
    suspend fun deleteFromOtherSide(
        @Path("id") id: Int
    ): Response<String>

    @Headers("Accept: application/json")
    @GET("/api/getStudents")
    suspend fun getStudents(): Response<List<Student>>


    @Headers("Accept: application/json")
    @Multipart
    @POST("/api/addStudent")
    suspend fun addStudent(
        @Part("name") name: RequestBody,
        @Part("email") email: RequestBody,
        @Part("cin") cin: RequestBody,
        @Part("date_naiss") date_naiss: RequestBody,
        @Part("sex") sex: RequestBody,
        @Part("tel") tel: RequestBody,
        @Part("adresse") adresse: RequestBody,
        @Part("password") password: RequestBody,
        @Part("groupe_id") groupe_id: RequestBody,
        @Part("remarque") remarque: RequestBody,
        @Part("user_id") user_id: RequestBody,
        @Part("parent") parent: RequestBody,
        @Part photo: MultipartBody.Part
    ): Response<Student>

    @Headers("Accept: application/json")
    @Multipart
    @POST("/api/updateStudent/{id}")
    suspend fun updateStudent(
        @Path("id") id: Int,
        @Part("name") name: RequestBody,
        @Part("email") email: RequestBody,
        @Part("cin") cin: RequestBody,
        @Part("date_naiss") date_naiss: RequestBody,
        @Part("sex") sex: RequestBody,
        @Part("tel") tel: RequestBody,
        @Part("adresse") adresse: RequestBody,
        @Part("password") password: RequestBody,
        @Part("groupe_id") groupe_id: RequestBody,
        @Part("remarque") remarque: RequestBody,
        @Part("user_id") user_id: RequestBody,
        @Part("parent") parent: RequestBody,
        @Part photo: MultipartBody.Part
    ): Response<Student>

    @Headers("Accept: application/json")
    @GET("/api/getGroups")
    suspend fun getGroups(): Response<List<Group>>


    @Headers("Accept: application/json")
    @POST("/api/deleteGroupe/{id}")
    suspend fun deleteGroup(@Path("id") id: Int): Response<String>

    @Headers("Accept: application/json")
    @POST("/api/updateGroupe/{id}")
    suspend fun updateGroup(@Path("id") id: Int, @Body params: Group): Response<String>

    @Headers("Accept: application/json")
    @POST("/api/deleteStudent/{id}")
    suspend fun deleteStudent(@Path("id") id: Int): Response<String>

    @Headers("Accept: application/json")
    @POST("/api/addGroupe")
    suspend fun addGroup(@Body params: Group): Response<Group>

    @Headers("Accept: application/json")
    @GET("/api/getNiveau")
    suspend fun getNiveau(): Response<List<Niveau>>

    @Headers("Accept: application/json")
    @POST("/api/addNiveau")
    suspend fun addNiveau(@Body params: Niveau): Response<Niveau>

    @Headers("Accept: application/json")
    @POST("/api/deleteNiveau/{id}")
    suspend fun deleteNiveau(@Path("id") id: Int): Response<String>

    @Headers("Accept: application/json")
    @POST("/api/updateNiveau/{id}")
    suspend fun updateNiveau(@Path("id") id: Int, @Body params: Niveau): Response<String>
}


fun loginAPI(
    navCtr: NavHostController?,
    sharedViewModel: SharedViewModel,
    mail: String,
    pass: String
) {

    loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
    sharedViewModel.defineWrongCred(false)
    val usermodel = UserInfo("user1@gmail.com", "123123123")
//    val adminmodel = UserInfo("admin@gmail.com", "111111111")
//    val adminmodel = UserInfo(mail, pass)
    //    val adminmodel = UserInfo("walid@gmail.com", "gina")
    val adminmodel =
        if (!BuildConfig.DEV.toBoolean()) UserInfo(BuildConfig.LoginMail, BuildConfig.LoginMdp) else
//    UserInfo("houssem.hammami@bestsol.tn", "K64nGMpC")
            UserInfo("admin@gmail.com", "111111111")
//            UserInfo("ahmed@gmail.com", "123456")
//        UserInfo("benSalah@gmail.com", "123321")", "123321")
//            UserInfo(mail, pass)
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
                            navCtr?.popBackStack()
                            navCtr?.navigate(Screen.NavBar.route)
                        }
                        if (user.role == "staff") {
                            println("STAFF")
                            navCtr?.popBackStack()
                            navCtr?.navigate(Screen.NavBar.route)
                        }
                        if (user.role == "admin") {
                            navCtr?.popBackStack()
                            navCtr?.navigate(Screen.NavBar.route)
                        }
                        if (user.role == "student") {


                            navCtr?.popBackStack()
                            navCtr?.navigate(Screen.NavBar.route)
                            getStudent(sharedViewModel = sharedViewModel)

                        }
                    }

                } else {
                    if (response.code().toString() == "422") {
                        sharedViewModel.defineWrongCred(true)
                        println("Wrong credentials, try again.")
                    }

                    println("Error2: ${response.errorBody().toString()}")
                }
            } catch (e: HttpException) {
                println("Exception ${e.message}")
            } catch (e: Throwable) {
                println("Oops: Something else went wrong ${e.message}")
            }

        }
    }

}

fun usersAPI(sharedViewModel: SharedViewModel) {
    CoroutineScope(Dispatchers.IO).launch {
        val result = async { backendApi.getUsers() }
        val response = result.await()
        if (response.isSuccessful) {
            val users = response.body()
            sharedViewModel.defineUserList(users)
            sharedViewModel.defineUser(sharedViewModel.userList.filter { it.id == sharedViewModel.user!!.id }[0])

            users?.forEach { user ->

                println(user.name)
            }
            getContractAndPayment(sharedViewModel = sharedViewModel)
        }
    }
}

fun allUsersAPI(sharedViewModel: SharedViewModel) {
    CoroutineScope(Dispatchers.IO).launch {
        val result = async { backendApi.getAllUsers() }
        val response = result.await()
        if (response.isSuccessful) {
            val users = response.body()
            sharedViewModel.defineUserList(users)
            users?.forEach { user ->

                println(user.name)
            }
            getContractAndPayment(sharedViewModel = sharedViewModel)
        }
    }
}

fun getRequestBody(userValue: String): RequestBody {
    return userValue.toRequestBody("text/plain".toMediaTypeOrNull())
}

fun addUserAPI(
    user: User,
    sharedViewModel: SharedViewModel,
    file: MutableState<File>?,
    triggerSecondCall: Boolean? = false
) {
    CoroutineScope(Dispatchers.IO).launch {

        var rBodyFile = getRequestBody("")
        if (file?.value!!.exists()) {
            rBodyFile = file.value.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        }
        val mpb = MultipartBody.Part.createFormData("photo", file.value.name, rBodyFile)

        val result = async {
            backendApi.addUser(
                getRequestBody(user.name!!),
                getRequestBody(user.email!!),
                getRequestBody(user.cin!!),
                getRequestBody(user.date_naiss!!),
                getRequestBody(user.sex!!),
                getRequestBody(user.bank!!),
                getRequestBody(user.tel!!),
                getRequestBody(user.rib!!),
                getRequestBody(user.poste!!),
                getRequestBody(user.adresse!!),
                getRequestBody(user.role!!),
                getRequestBody(user.password!!),
                mpb

            )
        }
        val response = result.await()
        println("USER ADDED111  " + response.message())
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
    file: MutableState<File>?,
    sharedViewModel: SharedViewModel,
    triggerSecondCall: Boolean? = false
) {
    CoroutineScope(Dispatchers.IO).launch {
        var rBodyFile = getRequestBody("")
        if (file?.value!!.exists()) {
            rBodyFile = file.value.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        }
        val mpb = MultipartBody.Part.createFormData("photo", file.value.name, rBodyFile)

        val result = async {
            backendApi.updateUser(
                id,
                getRequestBody(user.name!!),
                getRequestBody(user.email!!),
                getRequestBody(user.cin!!),
                getRequestBody(user.date_naiss!!),
                getRequestBody(user.sex!!),
                getRequestBody(user.bank!!),
                getRequestBody(user.tel!!),
                getRequestBody(user.rib!!),
                getRequestBody(user.poste!!),
                getRequestBody(user.adresse!!),
                getRequestBody(user.role!!),
                getRequestBody(user.password!!),
                mpb

            )
        }
        val response = result.await()
//        println("USER updated  " + response.message())
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

fun updatePayment(id: Int, payment: Payment, sharedViewModel: SharedViewModel) {
    CoroutineScope(Dispatchers.IO).launch {
        val result = async { backendApi.updatePayment(id, payment) }
        val response = result.await()
        if (response.isSuccessful) {
            println("payment updated")
            getContractAndPayment(sharedViewModel = sharedViewModel)
        }
    }
}

fun sendMessage(
    id: Int, message: MessageToSend, sharedViewModel: SharedViewModel,
    triggerSecondCall: Boolean? = false
) {
    CoroutineScope(Dispatchers.IO).launch {
        val result = async { backendApi.sendMessage(id, message) }
        val response = result.await()
        println("message info " + response.message())
        if (response.isSuccessful) {
            println("message SENT")
            if (triggerSecondCall == true) {
                sharedViewModel.defineGetMessageWorked(false)
                while (!sharedViewModel.getMessageWorked) {
                    getMessages(id, sharedViewModel)
                    delay(1000)
                }
            }
        }
        sharedViewModel.defineSpinSendBtn(false)
    }
}

fun getMessages(id: Int, sharedViewModel: SharedViewModel) {
    println(sharedViewModel.gettingMsg.toString() + " STATES " + sharedViewModel.getMessageWorked)
    // Making sure no more than 1 getMessages running in the background
    if (sharedViewModel.gettingMsg && !sharedViewModel.getMessageWorked) {//T & F GTFO
        return
    }
    sharedViewModel.defineGettingMsg(true)

//    sharedViewModel.messageList.clear()
//    sharedViewModel.targetedMessageList.clear()
    CoroutineScope(Dispatchers.IO).launch {
        // Prevent app crash when the json is too large? lol
        // this's the exception: java.io.EOFException: End of input, it's a moshi thing it seems
        // using try and catch didn't fix the crash btw
        supervisorScope {
            val result = async { backendApi.getMessage(id) }
            var response: Response<List<Message>>? = null
            try {
                response = result.await()
            } catch (e: Exception) {
                println("EXCEPTIONNN!! " + e.message)
            }

            var filteredList = mutableStateOf(listOf<Message>())
            var unseenMessages = mutableStateOf(listOf<Int>())
            var tempMsg = mutableStateOf(listOf<Int>())
            if (response?.isSuccessful == true) {
                sharedViewModel.messageList.clear()
                // TagetedMessageList storing the msgs between this user and the one they just selected
                // doing it like this (full api call) in case a message get received when entering a convo
                sharedViewModel.targetedMessageList.clear()
                sharedViewModel.defineGetMessageWorked(true)
                val messages = response.body()
                if (sharedViewModel.messageList.isNotEmpty())
                    sharedViewModel.messageList.clear()
                sharedViewModel.defineMessageList(messages)
                sharedViewModel.defineUnseenMsgExist(false)
                messages!!.forEach { message ->
                    if (message.to_user_id == sharedViewModel.user?.id.toString()) {
                        if (message.vu == "0")
                            sharedViewModel.defineUnseenMsgExist(true)
                    }
                }
                // This will run ONLY if user reading actual messages
                if (sharedViewModel.userToMessage != null) {
                    messages!!.forEach { message ->
                        if (message.to_user_id == sharedViewModel.userToMessage!!.id.toString() ||
                            message.from_user_id == sharedViewModel.userToMessage!!.id.toString()
                        ) {
                            filteredList.value = filteredList.value + message
                            unseenMessages.value += message.id.toInt()
                        }
                    }
                }
                sharedViewModel.defineTargetedMessageList(filteredList.value)
                // Get only the messages that got send to this user
                filteredList.value.forEach { unseenMsg ->
                    if (unseenMsg.to_user_id == sharedViewModel.user?.id.toString()) {
                        tempMsg.value += unseenMsg.id.toInt()
                    }
                }
                var msgUnseen = MessagesIds(ids = tempMsg.value)
                sharedViewModel.defineUnseenMessages(msgUnseen)
                println(sharedViewModel.targetedMessageList.size.toString() + "receiving messages " + sharedViewModel.targetedMessageList.size)
            }
        }
        sharedViewModel.defineGettingMsg(false)
    }
}

fun updateMessageView(unseenMsg: MessagesIds, sharedViewModel: SharedViewModel) {
    CoroutineScope(Dispatchers.IO).launch {
        val result = async { backendApi.updateMessagesView(unseenMsg) }
        val response = result.await()
        if (response.isSuccessful) {
            println("messages updated")
//                getContractAndPayment(sharedViewModel = sharedViewModel)
        }
    }
}

fun deleteOtherMsg(
    id: Int, sharedViewModel: SharedViewModel,
    triggerSecondCall: Boolean? = false
) {
    CoroutineScope(Dispatchers.IO).launch {
        val result = async { backendApi.deleteFromOtherSide(id) }
        val response = result.await()
        println("message info " + response.message())
        if (response.isSuccessful) {
            println("message deleted")
        }
    }
}

fun deleteThisMsg(
    id: Int, sharedViewModel: SharedViewModel,
    triggerSecondCall: Boolean? = false
) {
    CoroutineScope(Dispatchers.IO).launch {
        val result = async { backendApi.deleteFromThisSide(id) }
        val response = result.await()
        println("message info " + response.message())
        if (response.isSuccessful) {
            println("message deleted")
            if (triggerSecondCall == true) {
                deleteOtherMsg(id, sharedViewModel)
            }
        }
    }
}

fun isInternetAvailable(context: Context): Boolean {
    var result = false
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val networkCapabilities = connectivityManager.activeNetwork ?: return false
        val actNw = connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
        result = when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    } else {
        connectivityManager.run {
            connectivityManager.activeNetworkInfo?.run {
                result = when (type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
    }
    return result
}

fun getStudent(sharedViewModel: SharedViewModel) {
    CoroutineScope(Dispatchers.IO).launch {
        val result = async { backendApi.getStudents() }
        val response = result.await()
        if (response.isSuccessful) {
            val students = response.body()
            sharedViewModel.studentList.clear()
            sharedViewModel.defineStudentList(students)
            if (sharedViewModel.user?.role == "student")
                sharedViewModel.defineUser(sharedViewModel.studentList.filter { it.user?.id == sharedViewModel.user!!.id }[0].user!!)
            getGroups(sharedViewModel = sharedViewModel)
        }
    }
}

fun addStudent(
    student: Student,
    sharedViewModel: SharedViewModel,
    file: MutableState<File>?,
    triggerSecondCall: Boolean? = false
) {
    CoroutineScope(Dispatchers.IO).launch {

        var rBodyFile = getRequestBody("")
        if (file?.value!!.exists()) {
            rBodyFile = file.value.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        }
        val mpb = MultipartBody.Part.createFormData("photo", file.value.name, rBodyFile)

        val result = async {
            backendApi.addStudent(
                getRequestBody(student.name!!),
                getRequestBody(student.email!!),
                getRequestBody(student.cin!!),
                getRequestBody(student.date_naiss!!),
                getRequestBody(student.sex!!),
                getRequestBody(student.tel!!),
                getRequestBody(student.adresse!!),
                getRequestBody(student.password!!),
                getRequestBody(student.groupe_id!!),
                getRequestBody(student.remarque!!),
                getRequestBody(student.user_id!!),
                getRequestBody(student.parent!!),
                mpb

            )
        }
        val response = result.await()
        println("STUDENT ADDED111  " + response.message())
        if (response.isSuccessful) {
            println("STUDENT ADDED")
            if (triggerSecondCall == true)
                getStudent(sharedViewModel = sharedViewModel)
        }
    }
}

fun editStudent(
    student: Student,
    sharedViewModel: SharedViewModel,
    file: MutableState<File>?,
    triggerSecondCall: Boolean? = false
) {
    CoroutineScope(Dispatchers.IO).launch {

        var rBodyFile = getRequestBody("")
        if (file?.value!!.exists()) {
            rBodyFile = file.value.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        }
        val mpb = MultipartBody.Part.createFormData("photo", file.value.name, rBodyFile)

        val result = async {
            backendApi.updateStudent(
                student.id.toInt(),
                getRequestBody(student.name!!),
                getRequestBody(student.email!!),
                getRequestBody(student.cin!!),
                getRequestBody(student.date_naiss!!),
                getRequestBody(student.sex!!),
                getRequestBody(student.tel!!),
                getRequestBody(student.adresse!!),
                getRequestBody(student.password!!),
                getRequestBody(student.groupe_id!!),
                getRequestBody(student.remarque!!),
                getRequestBody(student.user_id!!),
                getRequestBody(student.parent!!),
                mpb

            )
        }
        val response = result.await()
        println("STUDENT updated  " + response.message())
        if (response.isSuccessful) {
            println("STUDENT updated")
            if (triggerSecondCall == true)
                getStudent(sharedViewModel = sharedViewModel)
        }
    }
}


fun getGroups(sharedViewModel: SharedViewModel) {
    CoroutineScope(Dispatchers.IO).launch {
        val result = async { backendApi.getGroups() }
        val response = result.await()
        if (response.isSuccessful) {
            val groups = response.body()
            sharedViewModel.groupList.clear()
            sharedViewModel.defineGroupList(groups)
            getNiveau(sharedViewModel)
        }
    }
}

fun deleteGroup(id: Int) {
    CoroutineScope(Dispatchers.IO).launch {
        val response = backendApi.deleteGroup(id)
        withContext(Dispatchers.Main) {
            if (response.isSuccessful) {
                println("group DELETED")
            }
        }
    }
}

fun deleteStudent(id: Int) {
    CoroutineScope(Dispatchers.IO).launch {
        val response = backendApi.deleteStudent(id)
        withContext(Dispatchers.Main) {
            if (response.isSuccessful) {
                println("student DELETED")
            }
        }
    }
}

fun addGroup(
    group: Group, sharedViewModel: SharedViewModel,
    triggerSecondCall: Boolean? = false
) {
    CoroutineScope(Dispatchers.IO).launch {
        val result = async { backendApi.addGroup(group) }
        val response = result.await()
        if (response.isSuccessful) {
            println("group ADDED")
            if (triggerSecondCall == true)
                getGroups(sharedViewModel = sharedViewModel)
        }
    }
}

fun addNiveau(
    nivName: String, sharedViewModel: SharedViewModel,
    triggerSecondCall: Boolean? = false
) {
    var niv = Niveau()
    niv.name = nivName
    CoroutineScope(Dispatchers.IO).launch {
        val result = async { backendApi.addNiveau(niv) }
        val response = result.await()
        if (response.isSuccessful) {
            println("niveau ADDED")
            if (triggerSecondCall == true)
                getNiveau(sharedViewModel = sharedViewModel)
        }
    }
}

fun getNiveau(sharedViewModel: SharedViewModel) {
    CoroutineScope(Dispatchers.IO).launch {
        val result = async { backendApi.getNiveau() }
        val response = result.await()
        if (response.isSuccessful) {
            sharedViewModel.niveauList.clear()
            val niveaux = response.body()
            sharedViewModel.defineNiveauList(niveaux)
        }
    }
}

fun deleteNiveau(id: Int) {
    CoroutineScope(Dispatchers.IO).launch {
        val response = backendApi.deleteNiveau(id)
        withContext(Dispatchers.Main) {
            if (response.isSuccessful) {
                println("niveau DELETED")
            }
        }
    }
}

fun updateNiveau(id: Int, niveau: Niveau, sharedViewModel: SharedViewModel) {
    CoroutineScope(Dispatchers.IO).launch {
        val response = backendApi.updateNiveau(id, niveau)
        withContext(Dispatchers.Main) {
            if (response.isSuccessful) {
                println("niveau updated")
                getNiveau(sharedViewModel = sharedViewModel)
            }
        }
    }
}

fun updateGroup(id: Int, group: Group, sharedViewModel: SharedViewModel) {
    CoroutineScope(Dispatchers.IO).launch {
        val response = backendApi.updateGroup(id, group)
        withContext(Dispatchers.Main) {
            if (response.isSuccessful) {
                println("group updated")
                getGroups(sharedViewModel = sharedViewModel)
            }
        }
    }
}
