package com.example.dogapi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dogapi.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity(),SearchView.OnQueryTextListener, OnQueryTextListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter:DogAdapter
    private val dogImages = mutableListOf<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.svDogs.setOnQueryTextListener(this)
        initRecyclerView()

    }
    private fun initRecyclerView(){
        adapter = DogAdapter(dogImages)
        binding.rvDogs.layoutManager = LinearLayoutManager(this)
        binding.rvDogs.adapter = adapter
    }
    private fun getRetrofit():Retrofit{
        return Retrofit.Builder()
            .baseUrl("https://dog.ceo/api/breed/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun searchByName(query:String){
        CoroutineScope(Dispatchers.IO).launch {
            val call= getRetrofit().create(ApiService::class.java).getDogByRaza("$query/images")
            val cachoros:DogResponse?=call.body()
            runOnUiThread {
                if(call.isSuccessful){
                    //Mostrar imagenes en el RecyclerView
                    val images:List<String> = cachoros?.images ?: emptyList()
                    dogImages.clear()
                    dogImages.addAll(images)
                    adapter.notifyDataSetChanged()

                }else{
                    //Mostrar error
                    showError()
                }
            }

        }
    }
    private fun showError(){
        Toast.makeText(this,"Ha ocurrido un error o no existe esa raza",Toast.LENGTH_LONG).show()
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if(!query.isNullOrEmpty()){
            //Toast.makeText(this,"Buscando: ${query.lowercase()}", Toast.LENGTH_LONG).show()
            searchByName(query.lowercase())
        }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return true
    }
}