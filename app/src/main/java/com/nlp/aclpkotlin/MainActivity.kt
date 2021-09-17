package com.nlp.aclpkotlin

import android.Manifest
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*

/**
 * What does the program do?
 * @sample
 */

@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class MainActivity : AppCompatActivity() {
    private val programName = "TestACLP.txt"
    var commandList = mutableListOf<KVObject>()
    var isRecording = false
    private lateinit var done: Button
    private lateinit var retry: Button
    private lateinit var textview: TextView
    private lateinit var recording: TextView
    private lateinit var fab: FloatingActionButton
    var currentKey = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkPermission()

        done = findViewById(R.id.done)
        retry = findViewById(R.id.retry)
        textview = findViewById(R.id.textview)
        recording = findViewById(R.id.recording)
        fab = findViewById(R.id.fab)
        startRecording()

        done.setOnClickListener {
            done.isEnabled = false
            retry.isEnabled = false
            organizeText()
        }

        retry.setOnClickListener {
            commandList.clear()
            done.visibility = View.GONE
            retry.visibility = View.GONE
        }
    }

    private fun startRecording(){
        val speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
        val speechRecognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Start saying your command in order")
        speechRecognizer.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(bundle: Bundle) {}
            override fun onBeginningOfSpeech() {}
            override fun onRmsChanged(v: Float) {}
            override fun onBufferReceived(bytes: ByteArray) {}
            override fun onEndOfSpeech() {}
            override fun onError(i: Int) {}
            override fun onResults(bundle: Bundle) {
                val matches = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)//getting all the matches
                //displaying the first match
                if (matches != null) {
                    val sr = matches[0]//.split(" ")[0]
                    textview.text = sr
                    Log.e("Command Log",sr)
                    val obj = KVObject()
                    obj.key = currentKey
                    obj.type = ""
                    obj.value = sr
                    commandList.add(obj)
                    if(sr.uppercase() == "END"){
                        currentKey--
                    } else {
                        currentKey++
                    }
                    /*valueList.add(sr[0].uppercaseChar().toString())
                    speechCommandList.add(sr[1].toString())
                    for (i in valueList.indices) {
                        done.visibility = View.VISIBLE
                        retry.visibility = View.VISIBLE
                    }*/
                }
            }
            override fun onPartialResults(bundle: Bundle) {}
            override fun onEvent(i: Int, bundle: Bundle) {}
        })

        fab.setOnClickListener {
            if(isRecording){
                swapColor(fab, false)
                isRecording = !isRecording
                recording.visibility = View.GONE
                speechRecognizer.stopListening()
            } else {
                swapColor(fab, true)
                isRecording = !isRecording
                recording.visibility = View.VISIBLE
                speechRecognizer.startListening(speechRecognizerIntent)
            }
        }
    }

    private fun organizeText(){
        //organize the text
        val text = openingComments() + "\n" + ACLPMethods.treeParser(commandList)
        //ACLPOutput.outputFile(result = text, programName, this)
        done.isEnabled = true
        retry.isEnabled = true
        done.visibility = View.GONE
        retry.visibility = View.GONE
        showResult(text = text)
    }

    private fun showResult(text: String){
        val intent = Intent(this, TextActivity::class.java)
        intent.putExtra("result", text)
        startActivity(intent)
    }

    private fun openingComments(): String{
        return "/*NOTICE FOR AUTOMATED JAVA FILE CREATED BY ACLP.." +
                "\n*MAKE SURE TO RUN THE PROGRAM YOU CHANGE THE EXTENSION FROM .txt TO .java" +
                "\n*THE PROGRAM ISN'T GUARANTEED TO RUN, CODE IS CREATED STRICTLY BASED ON THE ORDER OF YOUR SPEECH COMMANDS*/\n\n"
    }

    /*private fun loadJavaCommands(){
        commandsList["CLASS"] = "public class"//SHOULD BE THE SAME AS THE FILE NAME..........CLASS
        commandsList["MAIN"] = "public static void main(String []args){"//MAIN METHOD
        commandsList["PRINT"] = "System.out.print("//PRINT
        commandsList["STRING"] = "String"//VARIABLES
        commandsList["DOUBLE"] = "double"
        commandsList["FLOAT"] = "float"
        commandsList["INT"] = "int"
        commandsList["BOOLEAN"] = "boolean"
        commandsList["IF"] = "if()"
        commandsList["ELSE IF"] = "else if()"
        commandsList["ELSE"] = "else"
        commandsList["FOR"] = "for()"
        commandsList["WHILE"] = "while()"
    }*/

    private fun checkPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO), 5)
    }

    private fun swapColor(floating: FloatingActionButton, isActive: Boolean){
        if (isActive)
            floating.setBackgroundColor(Color.CYAN)
        else
            floating.setBackgroundColor(Color.WHITE)
    }
}