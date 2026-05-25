package com.example.ticketocr

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: add CameraX UI
    }

    fun recognize(uri: Uri) {
        val image = InputImage.fromFilePath(this, uri)

        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

        recognizer.process(image)
            .addOnSuccessListener { result ->
                val text = result.text
                val ticket = parse(text)
            }
    }

    fun parse(text: String): Ticket {
        val date = Regex("\d{4}-\d{1,2}-\d{1,2}").find(text)?.value ?: ""
        val plate = Regex("[A-Z][A-Z0-9]{6}").find(text)?.value ?: ""
        val weight = Regex("(\d+\.?\d*)").find(text)?.value?.toDoubleOrNull() ?: 0.0

        val amount = weight * 100

        return Ticket(date, plate, weight, amount)
    }
}

data class Ticket(
    val date: String,
    val plate: String,
    val weight: Double,
    val amount: Double
)
