package ru.zavgorodnev.googletasks

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.zavgorodnev.googletasks.databinding.ActivityMainBinding
import ru.zavgorodnev.googletasks.ui.main.MainFragment

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, MainFragment())
                .commit()
        }
    }
}