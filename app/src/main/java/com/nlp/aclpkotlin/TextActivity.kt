package com.nlp.aclpkotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.widget.Toolbar

/**
 * The purpose of this activity is to just show the result using textview.
 * TODO add tab to the lines of text.
 */
class TextActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text)
        val text = intent.extras?.getString("result")
        Log.d("ACLPKotlinResult", text.toString())
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        val textView: TextView = findViewById(R.id.text)
        textView.text = text
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}