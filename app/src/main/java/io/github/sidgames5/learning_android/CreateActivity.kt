package io.github.sidgames5.learning_android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import io.github.sidgames5.learning_android.models.BoardSize
import io.github.sidgames5.learning_android.util.EXTRA_BOARD_SIZE

class CreateActivity : AppCompatActivity() {
    private lateinit var boardSize: BoardSize
    private var imagesRequired = -1
    private var imagesAdded = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        boardSize = intent.getSerializableExtra(EXTRA_BOARD_SIZE) as BoardSize
        imagesRequired = boardSize.getPairs() * 2
        supportActionBar?.title = "Choose ${imagesRequired - imagesAdded} pictures"
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}