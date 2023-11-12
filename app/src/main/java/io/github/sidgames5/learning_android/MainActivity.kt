package io.github.sidgames5.learning_android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.github.sidgames5.learning_android.models.BoardSize
import io.github.sidgames5.learning_android.util.DEFAULT_ICONS

class MainActivity : AppCompatActivity() {

    private lateinit var board:RecyclerView
    private lateinit var textMoves:TextView
    private lateinit var textPairs:TextView

    private var boardSize:BoardSize = BoardSize.EASY

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        board = findViewById(R.id.board)
        textMoves = findViewById(R.id.textMoves)
        textPairs = findViewById(R.id.textPairs)

        val images = DEFAULT_ICONS.shuffled().take(boardSize.getPairs())
        val randomizedImages = (images + images).shuffled()

        board.adapter = MemoryBoardAdapter(this, boardSize, randomizedImages)
        board.setHasFixedSize(true)
        board.layoutManager = GridLayoutManager(this, boardSize.getWidth())

        // https://youtu.be/C2DBDZKkLss?t=3105
    }
}