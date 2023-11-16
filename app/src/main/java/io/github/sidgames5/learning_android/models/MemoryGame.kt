package io.github.sidgames5.learning_android.models

import io.github.sidgames5.learning_android.util.DEFAULT_ICONS

class MemoryGame(private val boardSize: BoardSize) {
    private var indexOfSingleSelectedCard:Int? = null
    private var numFlips = 0
    
    fun flipCard(position: Int):Boolean {
        numFlips++
        val card = cards[position]
        var foundMatch = false
        
        if (indexOfSingleSelectedCard == null) {
            restoreCards()
            indexOfSingleSelectedCard = position
        } else {
            foundMatch = checkForMatch(indexOfSingleSelectedCard!!, position)
            indexOfSingleSelectedCard = null
        }
        card.isFaceUp = !card.isFaceUp
        return foundMatch
    }

    private fun checkForMatch(pos1: Int, pos2: Int): Boolean {
        if (cards[pos1].id != cards[pos2].id) {
            return false
        }
        cards[pos1].isMatched = true
        cards[pos2].isMatched = true
        pairsFound++
        return true
    }

    private fun restoreCards() {
        for (card in cards) {
            if (!card.isMatched) {
                card.isFaceUp = false
            }
        }
    }

    fun haveWonGame(): Boolean {
        return pairsFound == boardSize.getPairs()
    }

    fun isCardFaceUp(position: Int): Boolean {
        return cards[position].isFaceUp
    }

    fun getMoves(): Int {
        return numFlips / 2
    }

    var cards:List<MemoryCard>
    var pairsFound = 0

    init {
        val images = DEFAULT_ICONS.shuffled().take(boardSize.getPairs())
        val randomizedImages = (images + images).shuffled()
        cards = randomizedImages.map { MemoryCard(it) }
    }
}