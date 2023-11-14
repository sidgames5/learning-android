package io.github.sidgames5.learning_android.models

import io.github.sidgames5.learning_android.util.DEFAULT_ICONS

class MemoryGame(private val boardSize: BoardSize) {
    val cards:List<MemoryCard>
    val pairsFound = 0

    init {
        val images = DEFAULT_ICONS.shuffled().take(boardSize.getPairs())
        val randomizedImages = (images + images).shuffled()
        cards = randomizedImages.map { MemoryCard(it) }
    }
}