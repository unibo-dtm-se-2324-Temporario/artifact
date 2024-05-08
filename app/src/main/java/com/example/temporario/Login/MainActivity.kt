package com.example.temporario.Login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.temporario.Home.InitializationFragment
import com.example.temporario.R
import com.example.temporario.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Used to add events to the database for production and testing scopes.
//        supportFragmentManager.beginTransaction().apply {
//            add(R.id.fragment_container, InitializationFragment::class.java, null)
//            commit()
//        }

        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, SplashFragment::class.java, null)
            .commit()

    }
}

