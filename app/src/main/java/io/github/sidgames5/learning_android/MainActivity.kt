package io.github.sidgames5.learning_android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var board:RecyclerView
    private lateinit var textMoves:TextView
    private lateinit var textPairs:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        board = findViewById(R.id.board)
        textMoves = findViewById(R.id.textMoves)
        textPairs = findViewById(R.id.textPairs)

        // https://youtu.be/C2DBDZKkLss?t=1183
    }
}