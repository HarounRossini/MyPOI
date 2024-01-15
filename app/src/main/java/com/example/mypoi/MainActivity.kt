package com.example.mypoi

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.mypoi.databinding.MainBinding
import com.example.mypoi.ui.theme.MyPOITheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(MainBinding.inflate(layoutInflater).root)
        findViewById<Button>(R.id.mapButton).setOnClickListener {
            onButtonClick()
        }

    }

    private fun onButtonClick(){
        val intent = Intent(this, MapsActivity::class.java)
        startActivity(intent)
    }
}


