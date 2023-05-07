package ru.zavgorodnev.googletasks

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import ru.zavgorodnev.googletasks.databinding.ActivityMainBinding
import ru.zavgorodnev.googletasks.ui.main.MainFragment
import ru.zavgorodnev.googletasks.utils.Navigator

class MainActivity : AppCompatActivity(), Navigator {

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

    override fun launch(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun goBack() {
        onBackPressed()
    }
}