package com.study.tinkoff

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.study.tinkoff.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            activityMvTest.onAddReactionClickListener = {
                Toast.makeText(this@MainActivity, "Test message", Toast.LENGTH_SHORT)
                    .show()
            }
            activityEvTest.setEmoji("🥶", 99)
        }
    }
}
