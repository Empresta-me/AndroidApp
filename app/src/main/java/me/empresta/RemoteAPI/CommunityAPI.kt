package me.empresta.RemoteAPI

import me.empresta.RemoteAPI.DTO.RegisterBody
import me.empresta.RemoteAPI.DTO.VouchBody
import okhttp3.Response
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Url

interface CommunityAPI {

    @GET()
    suspend fun getInfo(@Url url: String): ResponseBody


    @POST()
    suspend fun postChallenge(@Url url: String, @Header("challenge") challenge:String): ResponseBody

    @POST()
    suspend fun postAssociate(@Url url: String, @Header("password") password: String): ResponseBody

    @GET()
    suspend fun getChallenge(@Url url: String, @Header("token") token: String,@Header("public-key") public_key: String): ResponseBody

    @POST()
    suspend fun postRegister(@Url url: String, @Body body: RegisterBody): ResponseBody

    @POST()
    suspend fun postVouch(@Url url: String, @Body body: VouchBody): ResponseBody


    @POST()
    suspend fun postPermitInfo(@Url url: String,  @Header("host-key") host: String,@Header("guest-key") guest: String,@Header("response") response: String): ResponseBody

}