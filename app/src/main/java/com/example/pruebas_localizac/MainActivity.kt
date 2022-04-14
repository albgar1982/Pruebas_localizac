package com.example.pruebas_localizac

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.pruebas_localizac.databinding.ActivityMainBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import java.util.function.Consumer

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    lateinit var manejadorLoc: LocationManager
    lateinit var proveedorLocalizacion: FusedLocationProviderClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //binding.tvSalida.text = getString(R.string.func) //Probamos que el EditText puede recibir texto. Le asignamos una cadena de string.xml

        manejadorLoc = getSystemService(Context.LOCATION_SERVICE) as LocationManager //Conseguimos el Manejador de Localizacion
        val proveedorGPS = LocationManager.GPS_PROVIDER
        println("Proveedor GPS: "+proveedorGPS)
        println("Lista de proveedores de localización: "+manejadorLoc.allProviders)
        if (ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            println("Está autorizado el permiso para la localización precisa.")
            if (ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                println("Está autorizado el acceso a la localización aproximada.")
                //if(manejadorLoc.isProviderEnabled(proveedorGPS))
                //println(manejadorLoc.getCurrentLocation(proveedorGPS,null,this.mainExecutor,Consumer(proveedorGPS.toInt())))
            }
        }
        if (ActivityCompat.checkSelfPermission(this,Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            println("NO está autorizada la cámara")

        proveedorLocalizacion = LocationServices.getFusedLocationProviderClient(this)

        binding.bLocaliz.setOnClickListener {
            asignarLocalizac()
        }
    }

    private fun asignarLocalizac() {
        val task : Task<Location> = proveedorLocalizacion.lastLocation //Cogemos la última localización (ubícalo en Maps, por ejemplo)
        if (ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //Si no están autorizados los permisos, se piden:
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),101)
        }
        task.addOnSuccessListener {
            if(it != null)
                Toast.makeText(this,"Latitud: ${it.latitude}\n Longitud: ${it.longitude}", Toast.LENGTH_LONG).show()
        }
    }
}