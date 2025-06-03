package games.game2048

import board.Cell
import board.GameBoard
import kotlin.Pair
import kotlin.random.Random


interface Game2048Initializer<T> {
    fun nextValue(board: GameBoard<T?>): Pair<Cell, T>?
}

object RandomGame2048Initializer : Game2048Initializer<Int> {
    private fun generateRandomStartValue(): Int =
        if (Random.nextInt(10) == 9) 4 else 2

    override fun nextValue(board: GameBoard<Int?>): Pair<Cell, Int>? {
        val freeCells = board.getAllCells().filter { board[it] == null }
        if (freeCells.isEmpty()) return null
        val randomCell = freeCells.random()
        val value = generateRandomStartValue()
        return randomCell to value
    }
}