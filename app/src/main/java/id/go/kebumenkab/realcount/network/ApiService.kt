package id.go.kebumenkab.realcount.network

import id.go.kebumenkab.realcount.model.LoginResponse
import id.go.kebumenkab.realcount.model.ServiceResponse
import id.go.kebumenkab.realcount.model.UserResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
interface ApiService {
    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Header("App-Version") appVersion: String,
        @Field("nip") username: String,
        @Field("password") password: String
    ): LoginResponse

    @GET("apipersonal/data_kehadiran/{nip}/{year}/{month}")
    suspend fun getHistories(
        @Header("Authorization") token: String,
        @Path("nip") nip: String,
        @Path("year") year: String,
        @Path("month") month: String
    ): List<UserResponse>

    @Multipart
    @POST("entridata")
    suspend fun uploadData(
        @Header("Authorization") token: String,
        @Header("App-Version") appVersion: String,
        @Part("jenis") serviceType: RequestBody?,
        @Part("type") presenceType: RequestBody?,
        @Part image: MultipartBody.Part?,
        @Part document: MultipartBody.Part?,
        @Part("tanggal") date: RequestBody?,
        @Part("tanggal_selesai") returnDate: RequestBody?,
        @Part("jam") time: RequestBody?,
        @Part("lat") latitude: RequestBody?,
        @Part("long") longitude: RequestBody?,
        @Part("keterangan") desc: RequestBody?,
    ): ServiceResponse
}