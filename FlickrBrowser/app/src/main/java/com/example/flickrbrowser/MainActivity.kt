package com.example.flickrbrowser

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var etPicId = findViewById<EditText>(R.id.etPicId)
        var searchBtn = findViewById<Button>(R.id.searchBtn)

        searchBtn.setOnClickListener {
            var item = etPicId.text.toString()
            val intent = Intent(this, MainActivity2::class.java)
            intent.putExtra("searchItem", item )
            startActivity(intent) }
    }
}