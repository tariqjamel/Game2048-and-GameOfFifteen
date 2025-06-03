package games.gameOfFifteen

import board.Cell
import board.Direction
import board.GameBoard
import board.createGameBoard
import games.game.Game

fun newGameOfFifteen(initializer: GameOfFifteenInitializer = RandomGameInitializer()): Game =
    GameOfFifteen(initializer)

class GameOfFifteen(private val initializer: GameOfFifteenInitializer) : Game {
    private val board = createGameBoard<Int?>(4)
    private val emptyCell = Cell(4, 4)

    override fun initialize() {
        val cells = board.getAllCells()
            .filter { it != emptyCell }
            .sortedWith(compareBy<Cell> { it.i }.thenBy { it.j })

        initializer.initialPermutation.forEachIndexed { index, value ->
            board[cells[index]] = value
        }
        board[emptyCell] = null
    }

    override fun hasWon(): Boolean {
        val cells = board.getAllCells()
            .filter { it != emptyCell }
            .sortedWith(compareBy<Cell> { it.i }.thenBy { it.j })

        return cells.mapIndexed { index, cell ->
            board[cell] == index + 1
        }.all { it }
    }

    override fun canMove() = true

    override fun processMove(direction: Direction) {
        val empty = board.find { it == null } ?: return

        val neighbor = when (direction) {
            Direction.UP -> board.getCellOrNull(empty.i + 1, empty.j)
            Direction.DOWN -> board.getCellOrNull(empty.i - 1, empty.j)
            Direction.LEFT -> board.getCellOrNull(empty.i, empty.j + 1)
            Direction.RIGHT -> board.getCellOrNull(empty.i, empty.j - 1)
        }

        if (neighbor != null) {
            board[empty] = board[neighbor]
            board[neighbor] = null
        }
    }

    override fun get(i: Int, j: Int): Int? = board.run { get(getCell(i, j)) }
}