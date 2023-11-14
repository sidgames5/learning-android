package io.github.sidgames5.learning_android.models

data class MemoryCard(
    val id:Int,
    var isFaceUp:Boolean = false,
    var isMatched:Boolean = false
)