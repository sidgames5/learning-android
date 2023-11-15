package io.github.sidgames5.learning_android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import io.github.sidgames5.learning_android.models.BoardSize
import io.github.sidgames5.learning_android.models.MemoryGame

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: MemoryBoardAdapter
    private lateinit var memoryGame: MemoryGame
    private lateinit var board:RecyclerView
    private lateinit var textMoves:TextView
    private lateinit var textPairs:TextView
    private lateinit var clRoot:ConstraintLayout

    private var boardSize:BoardSize = BoardSize.EASY

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        board = findViewById(R.id.board)
        clRoot = findViewById(R.id.clRoot)
        textMoves = findViewById(R.id.textMoves)
        textPairs = findViewById(R.id.textPairs)

        memoryGame = MemoryGame(boardSize)

        adapter = MemoryBoardAdapter(this, boardSize, memoryGame.cards, object:MemoryBoardAdapter.CardClickListener {
            override fun onCardClicked(position: Int) {
                updateGameWithFlip(position)
            }
        })
        board.adapter = adapter
        board.setHasFixedSize(true)
        board.layoutManager = GridLayoutManager(this, boardSize.getWidth())

        // https://youtu.be/C2DBDZKkLss?t=4820
    }

    private fun updateGameWithFlip(position: Int) {
        if (memoryGame.haveWonGame()) {
            Snackbar.make(clRoot, "You already won!", Snackbar.LENGTH_LONG).show()
            return
        }
        if (memoryGame.isCardFaceUp(position)) {
            Snackbar.make(clRoot, "Invalid move!", Snackbar.LENGTH_LONG).show()
            return
        }
        if (memoryGame.flipCard(position)) {
            Log.i("MainActivity", "Found a match! Pairs found: ${memoryGame.pairsFound}")
        }
        adapter.notifyDataSetChanged()
    }
}