package com.nlp.aclpkotlin

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*

/**
 * Description: ACLP allows users to say to predefined short statements to automatically generate equivalent code in written
 * in the users selected choice of language. ACLP heavily relies on built-in SpeechRecognizer library to do speech to text operations.
 * Github: "https://github.com/viraj325/ACLPKotlin"
 */

@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class MainActivity : AppCompatActivity() {
    private val programName = "TestACLP.txt"
    var commandList = mutableListOf<KVObject>()
    val recentList = ArrayList<String>()
    private lateinit var adapter: RecentAdapter
    var isRecording = false
    private lateinit var done: Button
    private lateinit var retry: Button
    private lateinit var textview: TextView
    private lateinit var edittext: AppCompatEditText
    private lateinit var enter: Button
    private lateinit var recording: TextView
    private lateinit var fab: FloatingActionButton
    var currentKey = 0

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkPermission()

        val recyclerview = findViewById<RecyclerView>(R.id.recentList)
        recyclerview.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        adapter = RecentAdapter(recentList)
        recyclerview.adapter = adapter

        done = findViewById(R.id.done)
        retry = findViewById(R.id.retry)
        textview = findViewById(R.id.textview)
        edittext = findViewById(R.id.edittext)
        enter = findViewById(R.id.enter)
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
            recentList.clear()
            adapter.notifyDataSetChanged()
            edittext.setText("")
        }

        enter.setOnClickListener {
            if (edittext.text.toString().isNotEmpty()) {
                recentList.add(edittext.text.toString())
                adapter.notifyDataSetChanged()
                val obj = KVObject()
                obj.key = currentKey
                obj.type = ""
                obj.value = edittext.text.toString()
                commandList.add(obj)
                if(edittext.text.toString().uppercase() == "END"){
                    currentKey--
                } else {
                    currentKey++
                }
            }
        }
    }

    private fun checkPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO), 5)
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
            @SuppressLint("NotifyDataSetChanged")
            override fun onResults(bundle: Bundle) {
                val matches = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)//getting all the matches
                //displaying the first match
                if (matches != null) {
                    val sr = matches[0]//.split(" ")[0]
                    edittext.setText(sr)
                    Log.e("Command Log",sr)
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

    private val textToSpeechEngine: TextToSpeech by lazy {
        TextToSpeech(this) { status ->
            if (status == TextToSpeech.SUCCESS)
                textToSpeechEngine.language = Locale.US
        }
    }

    private fun organizeText(){
        //organize the text
        val text = openingComments() + "\n" + ACLPMethods.treeParser(commandList)
        //ACLPOutput.outputFile(result = text, programName, this)
        done.isEnabled = true
        retry.isEnabled = true
        showResult(text = text)
    }

    private fun openingComments(): String{
        return "/*NOTICE FOR AUTOMATED JAVA FILE CREATED BY ACLP.." +
                "\n*MAKE SURE TO RUN THE PROGRAM YOU CHANGE THE EXTENSION FROM .txt TO .java" +
                "\n*THE PROGRAM ISN'T GUARANTEED TO RUN, CODE IS CREATED STRICTLY BASED ON THE ORDER OF YOUR SPEECH COMMANDS*/\n\n"
    }

    private fun showResult(text: String){
        val intent = Intent(this, TextActivity::class.java)
        intent.putExtra("result", text)
        startActivity(intent)
    }

    private fun swapColor(floating: FloatingActionButton, isActive: Boolean){
        if (isActive)
            floating.setBackgroundColor(Color.CYAN)
        else
            floating.setBackgroundColor(Color.WHITE)
    }
}