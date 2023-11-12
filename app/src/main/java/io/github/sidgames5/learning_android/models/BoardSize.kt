package io.github.sidgames5.learning_android.models

enum class BoardSize(val cards:Int) {
    EASY(8),
    MEDIUM(18),
    HARD(24);

    fun getWidth():Int {
        return when (this) {
            EASY -> 2
            MEDIUM -> 3
            HARD -> 4
        }
    }

    fun getHeight():Int {
        return cards / getWidth()
    }

    fun getPairs():Int {
        return cards / 2
    }
}