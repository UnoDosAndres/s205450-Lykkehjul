package com.example.s205450_lykkehjul

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.*
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.example.s205450_lykkehjul.databinding.ActivityMainBinding
import java.lang.Math.random
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private val sectors = arrayOf("Loose a Turn","$800","$500","$650","$500","$900","BANKRUPT",
        "$5000","$500","$600","$700","$600","$650","$500","$700","$500","$600","$550","$500","$600",
        "BANKRUPT","$650","FREE PLAY","$700")
    private val sectorDegrees = intArrayOf(sectors.size)
    private var random = Random
    private var degree = 0
    private var startPoint = 0.0f
    private var isSpinning = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getSectorDegrees()

        binding.spinKnap.setOnClickListener(){
            if (!isSpinning){
                isSpinning = true
                spin()
            }

        }


    }
    private fun spin() {
        degree = random.nextInt(sectors.size-1)
        val degreePoint = (0..360).random()+360
        val rotation = RotateAnimation(0f,degreePoint.toFloat(), RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f)

        rotation.duration = 2000
        rotation.fillAfter = true
        rotation.interpolator = LinearInterpolator()
        rotation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {
                // actually, I don't need this method but I have to implement this.
            }

            override fun onAnimationEnd(animation: Animation?) {
                Toast.makeText(applicationContext,"You've got " + sectors[sectors.size - (degree+1)],Toast.LENGTH_LONG).show()
                startPoint = degreePoint.toFloat()
                
                isSpinning = false
            }

            override fun onAnimationStart(animation: Animation?) {
                // actually, I don't need this method but I have to implement this.
            }
        })
        binding.hjulFelter.startAnimation(rotation)


    }
    
    private fun getSectorDegrees() {
        val sectorDegree = 360/sectors.size

        for (i in sectorDegrees.indices) {
            sectorDegrees[i] = (i+1) * sectorDegree
        }
    }
    
}

