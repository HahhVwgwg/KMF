package kz.edu.kmf.data.api

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


const val BASE_URL_OWN = "https://petstore.swagger.io/"

interface OwnApiService {
    @GET("v2/user/{username}")
    suspend fun getUser(@Path(value = "username", encoded = true) username: String?): UserResponse

    @POST("v2/user")
    suspend fun registerUser(@Body login: UserResponse?): JsonObject
}


class UserResponse {

    @SerializedName("username")
    var username: String? = null

    @SerializedName("firstName")
    var firstName: String? = null

    @SerializedName("lastName")
    var lastName: String? = null

    @SerializedName("email")
    var email: String? = null

    @SerializedName("password")
    var password: String? = null

    @SerializedName("phone")
    var phone: String? = null

    @SerializedName("userStatus")
    var userStatus: Int = 0
}

object OwnApi {
    private val client = OkHttpClient.Builder().apply {
        addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
    }.build()

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .baseUrl(BASE_URL_OWN)
        .build()

    val retrofitService: OwnApiService by lazy {
        retrofit.create(OwnApiService::class.java)
    }
}