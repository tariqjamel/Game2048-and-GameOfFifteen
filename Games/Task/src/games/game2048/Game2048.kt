package games.game2048

import board.Cell
import board.Direction
import board.GameBoard
import board.createGameBoard
import games.game.Game

fun newGame2048(initializer: Game2048Initializer<Int> = RandomGame2048Initializer): Game =
    Game2048(initializer)

class Game2048(private val initializer: Game2048Initializer<Int>) : Game {
    private val board = createGameBoard<Int?>(4)

    override fun initialize() {
        repeat(2) {
            board.addNewValue(initializer)
        }
    }

    override fun canMove() = board.any { it == null }

    override fun hasWon() = board.any { it == 2048 }

    override fun processMove(direction: Direction) {
        if (board.moveValues(direction)) {
            board.addNewValue(initializer)
        }
    }

    override fun get(i: Int, j: Int): Int? = board.run { get(getCell(i, j)) }
}

fun GameBoard<Int?>.addNewValue(initializer: Game2048Initializer<Int>) {
    val next = initializer.nextValue(this)
    if (next != null) {
        this[next.first] = next.second
    }
}

fun GameBoard<Int?>.moveValuesInRowOrColumn(rowOrColumn: List<Cell>): Boolean {
    val oldValues = rowOrColumn.map { this[it] }
    val merged = oldValues.filterNotNull().moveAndMergeEqual { it * 2 }
    val newValues = merged + List(rowOrColumn.size - merged.size) { null }

    var changed = false
    for ((index, cell) in rowOrColumn.withIndex()) {
        if (this[cell] != newValues[index]) {
            this[cell] = newValues[index]
            changed = true
        }
    }
    return changed
}



fun GameBoard<Int?>.moveValues(direction: Direction): Boolean {
    val width = this.width
    var changed = false

    when (direction) {
        Direction.UP -> {
            for (j in 1..width) {
                val col = getColumn(1..width, j)
                changed = changed or moveValuesInRowOrColumn(col)
            }
        }
        Direction.DOWN -> {
            for (j in 1..width) {
                val col = getColumn(1..width, j).reversed()
                changed = changed or moveValuesInRowOrColumn(col)
            }
        }
        Direction.LEFT -> {
            for (i in 1..width) {
                val row = getRow(i, 1..width)
                changed = changed or moveValuesInRowOrColumn(row)
            }
        }
        Direction.RIGHT -> {
            for (i in 1..width) {
                val row = getRow(i, 1..width).reversed()
                changed = changed or moveValuesInRowOrColumn(row)
            }
        }
    }
    return changed
}