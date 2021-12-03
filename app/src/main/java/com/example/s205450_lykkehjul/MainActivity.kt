package com.example.s205450_lykkehjul

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.*
import android.widget.Toast
import com.example.s205450_lykkehjul.databinding.ActivityMainBinding
import java.io.InputStream
import java.util.*
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
    private var degreePoint = 0
    lateinit var wordToBeGuessed: String
    lateinit var wordDisplayed: String
    lateinit var charArray: CharArray
    private var listOfWords: ArrayList<String> = arrayListOf()
    lateinit var lettersTried: String
    private val winMessage= "You Won!"
    private val loseMessage= "You Lost!"

    /*For the part about reading of a txt and general code about the 'guess a word part of the game
        i have taken inspiration from this playlist on youtube
        https://www.youtube.com/playlist?list=PLgTkNlNsy9gVCkeaoudJJWr4P3Gc-AV7f
     */

    /*
    for the wheel and the spinning i have taken inspiration from this video
    https://www.youtube.com/watch?v=5O2Uox-TR00
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val myInputStream:InputStream
        myInputStream = assets.open("database_of_words.txt")
        val myScanner = Scanner(myInputStream)
        while (myScanner.hasNext()){
            val wordHolder = myScanner.next()
            listOfWords.add(wordHolder)
        }
        myScanner.close()
        myInputStream.close()

        getSectorDegrees()
        startGame()

        binding.spinKnap.setOnClickListener(){
            if (!isSpinning){
                isSpinning = true
                spin()
            }

        }


    }
    private fun startGame(){

    }

    // i ended up being unable to get it to show the right amount of points from the wheel, but it does show some
    private fun spin() {
        degree = random.nextInt(sectors.size-1)
        degreePoint = (0..360).random()
        val rotation = RotateAnimation(0f,(degreePoint+360).toFloat(), RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f)

        rotation.duration = 2000
        rotation.fillAfter = true
        rotation.interpolator = LinearInterpolator()
        rotation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {
            }

            override fun onAnimationEnd(animation: Animation?) {
                Toast.makeText(applicationContext,"You've got " + sectors[sectors.size - (degree+1)],Toast.LENGTH_LONG).show()
                startPoint = degreePoint.toFloat()

                isSpinning = false
            }

            override fun onAnimationStart(animation: Animation?) {
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

