package com.sitiaisyah.idn.drivondesign.network

import com.sitiaisyah.idn.drivondesign.model.ResultRoute
import io.reactivex.Flowable
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("json")
    fun actionRoute(@Query("origin")origin: String,
                    @Query("destination")destination: String,
                    @Query("key")key: String): Flowable<ResultRoute>

    @Headers(//firebase console
        "Authorization: key=AAAA2K2-PlA:APA91bFQqk1jg-6tmLEkMbAK_8aVBSDn1adWRH2vJRgHZ5st1HxjI4SGvELC0jiPflzNiKH2htv8fwCRd__flib03IyyNUh2qT7QtVh5Prw87EZkKspxpQzWAana1iAKkqU1BSdZhEAT",
        "Content-Type:application/json"
    )
    @POST("fcm/send")
    fun sendChatNotification(@Body requestNotificaton: RequestNotification): Call<ResponseBody>
}