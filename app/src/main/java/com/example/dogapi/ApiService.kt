package com.example.dogapi
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

//Método de como accede al API
interface ApiService {
        @GET
       suspend fun getDogByRaza(@Url url:String):Response<DogResponse>
}