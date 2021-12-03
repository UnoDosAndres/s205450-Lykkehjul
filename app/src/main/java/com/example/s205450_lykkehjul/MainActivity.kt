package com.example.s205450_lykkehjul

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
    private var wordDisplayed: String = ""
    lateinit var charArray: CharArray
    private var listOfWords: ArrayList<String> = arrayListOf()
    lateinit var lettersTried: String
    private var lives = 5

    /*For the part about reading of a txt and general code about the 'guess a word part of the game
        i have taken inspiration from this playlist on youtube
        https://www.youtube.com/playlist?list=PLgTkNlNsy9gVCkeaoudJJWr4P3Gc-AV7f
     */

    /*
    for the wheel and the spinning i have taken inspiration from this video
    https://www.youtube.com/watch?v=5O2Uox-TR00
     */

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.livesLeft.text = "Lives left: $lives"

        val myInputStream:InputStream = assets.open("database_of_words.txt")
        val myScanner = Scanner(myInputStream)
        while (myScanner.hasNext()){
            val wordHolder = myScanner.next()
            listOfWords.add(wordHolder)
        }
        myScanner.close()
        myInputStream.close()

        getSectorDegrees()
        startGame()

        binding.letterGuess.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                if (s.isNotEmpty()) {
                    checkLetter(s[0])
                }
            }
        })

        binding.spinKnap.setOnClickListener(){
            if (!isSpinning){
                isSpinning = true
                spin()
            }

        }
    }
    //checks if the letter is correct and shows it
    //also checks if we have a winner
    @SuppressLint("SetTextI18n")
    private fun checkLetter(letter: Char){
        if (wordToBeGuessed.indexOf(letter) >= 0 && wordDisplayed.indexOf(letter) < 0) {
            showLetterInWord(letter)
            displayWordInApp()
            if (!wordDisplayed.contains("_") && lives > 0) {
                //here the win fragment should show
            }
        }
        else {
            lives = lives-1
            binding.livesLeft.text = "Lives left: $lives"
            if (lives <= 0) {
                //Here the lose fragment should show
            }
            if (lettersTried.indexOf(letter) < 0) {
                lettersTried += "$letter, "
                binding.lettersUsed.text = "Letters tried: $lettersTried"
            }
        }

    }

    //starts the game
    private fun startGame(){
        listOfWords.shuffle()
        wordToBeGuessed = listOfWords[0]
        listOfWords.remove(listOfWords[0])
        charArray = wordToBeGuessed.toCharArray()

        for (i in charArray.indices) {
            charArray[i] = '_'
        }
        wordDisplayed.toCharArray(charArray)

        displayWordInApp()

        binding.letterGuess.setText("")

        lettersTried = " "

    }

    //reveals the chosen letter
    private fun showLetterInWord(letter:Char) {
        var letterIndex = wordToBeGuessed.indexOf(letter)
        while (letterIndex >= 0) {
            charArray[letterIndex] = wordToBeGuessed.get(letterIndex)
            //For at sikre jeg kan finde flere af det samme bogstav
            letterIndex = wordToBeGuessed.indexOf(letter, letterIndex+1)
        }

        wordDisplayed.toCharArray(charArray)
    }

    //updates the view so we see the newly revealed letter
    private fun displayWordInApp(){
        var formattedString = ""
        for (i in charArray) {
            formattedString +=  "$i "
        }
        binding.word.text = formattedString
    }

    // Spins the wheel
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

