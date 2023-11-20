package io.github.sidgames5.learning_android

import android.animation.ArgbEvaluator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener
import com.google.android.material.snackbar.Snackbar
import io.github.sidgames5.learning_android.models.BoardSize
import io.github.sidgames5.learning_android.models.MemoryGame
import io.github.sidgames5.learning_android.util.EXTRA_BOARD_SIZE

class MainActivity : AppCompatActivity() {

    companion object {
        private const val CREATE_REQUEST_CODE = 248
    }

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

        setupBoard()

        // https://youtu.be/C2DBDZKkLss?t=6745
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.mi_refresh -> {
                if (memoryGame.getMoves() > 0 && !memoryGame.haveWonGame()) {
                    showAlertDialog("Quit your current game?", null, View.OnClickListener {
                        setupBoard()
                    })
                } else {
                    setupBoard()
                }
                return true
            }
            R.id.mi_new_size -> {
                showNewSizeDialog()
                return true
            }
            R.id.mi_custom -> {
                showCreationDialog()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showCreationDialog() {
        val bsv = LayoutInflater.from(this).inflate(R.layout.dialog_board_size, null)
        val rgSize = bsv.findViewById<RadioGroup>(R.id.radioGroup)
        showAlertDialog("Create your own memory board", bsv, View.OnClickListener {
            // Set new value for size
            val desiredBoardSize = when (rgSize.checkedRadioButtonId) {
                R.id.rbEasy -> BoardSize.EASY
                R.id.rbMed -> BoardSize.MEDIUM
                else -> BoardSize.HARD
            }
            // Navigate to new activity
            val intent = Intent(this, CreateActivity::class.java)
            intent.putExtra(EXTRA_BOARD_SIZE, desiredBoardSize)
            startActivityForResult(intent, CREATE_REQUEST_CODE)
        })
    }

    private fun showNewSizeDialog() {
        val bsv = LayoutInflater.from(this).inflate(R.layout.dialog_board_size, null)
        val rgSize = bsv.findViewById<RadioGroup>(R.id.radioGroup)
        when (boardSize) {
            BoardSize.EASY -> rgSize.check(R.id.rbEasy)
            BoardSize.MEDIUM -> rgSize.check(R.id.rbMed)
            BoardSize.HARD -> rgSize.check(R.id.rbHard)
        }
        showAlertDialog("Choose new size", bsv, View.OnClickListener {
            // Set new value for size
            boardSize = when (rgSize.checkedRadioButtonId) {
                R.id.rbEasy -> BoardSize.EASY
                R.id.rbMed -> BoardSize.MEDIUM
                else -> BoardSize.HARD
            }
            setupBoard()
        })
    }

    private fun showAlertDialog(title:String, view: View?, positiveButtonClickListener: View.OnClickListener) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setView(view)
            .setNegativeButton("Cancel", null)
            .setPositiveButton("OK") { _, _ ->
                positiveButtonClickListener.onClick(null)
            }.show()
    }

    private fun setupBoard() {
        when (boardSize) {
            BoardSize.EASY -> {
                textMoves.text = "Easy: 4 x 2"
                textPairs.text = "Pairs: 0 / 4"
            }
            BoardSize.MEDIUM -> {
                textMoves.text = "Medium: 6 x 3"
                textPairs.text = "Pairs: 0 / 9"
            }
            BoardSize.HARD -> {
                textMoves.text = "Hard: 6 x 4"
                textPairs.text = "Pairs: 0 / 12"
            }
        }

        memoryGame = MemoryGame(boardSize)

        adapter = MemoryBoardAdapter(
            this,
            boardSize,
            memoryGame.cards,
            object : MemoryBoardAdapter.CardClickListener {
                override fun onCardClicked(position: Int) {
                    updateGameWithFlip(position)
                }
            })
        board.adapter = adapter
        board.setHasFixedSize(true)
        board.layoutManager = GridLayoutManager(this, boardSize.getWidth())

        textPairs.setTextColor(ContextCompat.getColor(this, R.color.color_progress_none))
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
            val color = ArgbEvaluator().evaluate((memoryGame.pairsFound / boardSize.getPairs()).toFloat(), ContextCompat.getColor(this, R.color.color_progress_none), ContextCompat.getColor(this, R.color.color_progress_full)) as Int
            textPairs.setTextColor(color)
            textPairs.text = "Pairs: ${memoryGame.pairsFound} / ${boardSize.getPairs()}"
            if (memoryGame.haveWonGame()) {
                Snackbar.make(clRoot, "You won!", Snackbar.LENGTH_LONG).show()
            }
        }
        textMoves.text = "Moves: ${memoryGame.getMoves()}"
        adapter.notifyDataSetChanged()
    }
}