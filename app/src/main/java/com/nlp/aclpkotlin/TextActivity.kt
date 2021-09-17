package com.nlp.aclpkotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.widget.Toolbar

class TextActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text)
        val text = intent.extras?.getString("result")
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        val textView: TextView = findViewById(R.id.text)
        if (text.isNullOrEmpty())
            textView.text = text
    }
}