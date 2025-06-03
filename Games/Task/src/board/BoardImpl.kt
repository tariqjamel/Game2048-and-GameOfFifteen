package board

fun createSquareBoard(width: Int): SquareBoard = SquareBoardImpl(width)

fun <T> createGameBoard(width: Int): GameBoard<T> = GameBoardImpl(width)

private open class SquareBoardImpl(override val width: Int) : SquareBoard {
    private val cells = List(width) { i ->
        List(width) { j -> Cell(i + 1, j + 1) }
    }

    override fun getCellOrNull(i: Int, j: Int): Cell? {
        if (i !in 1..width || j !in 1..width) return null
        return cells[i - 1][j - 1]
    }

    override fun getCell(i: Int, j: Int): Cell {
        return getCellOrNull(i, j) ?: throw IllegalArgumentException("Invalid cell coordinates ($i, $j)")
    }

    override fun getAllCells(): List<Cell> {
        return cells.flatten()
    }

    override fun getRow(i: Int, jRange: IntProgression): List<Cell> {
        return jRange.mapNotNull { j -> getCellOrNull(i, j) }
    }

    override fun getColumn(iRange: IntProgression, j: Int): List<Cell> {
        return iRange.mapNotNull { i -> getCellOrNull(i, j) }
    }

    override fun Cell.getNeighbour(direction: Direction): Cell? {
        return when (direction) {
            Direction.UP -> getCellOrNull(i - 1, j)
            Direction.DOWN -> getCellOrNull(i + 1, j)
            Direction.LEFT -> getCellOrNull(i, j - 1)
            Direction.RIGHT -> getCellOrNull(i, j + 1)
        }
    }
}

private class GameBoardImpl<T>(width: Int) : SquareBoardImpl(width), GameBoard<T> {
    private val cellValues = mutableMapOf<Cell, T?>()

    init {
        getAllCells().forEach { cell ->
            cellValues[cell] = null
        }
    }

    override fun get(cell: Cell): T? {
        return cellValues[cell]
    }

    override fun set(cell: Cell, value: T?) {
        cellValues[cell] = value
    }

    override fun filter(predicate: (T?) -> Boolean): Collection<Cell> {
        return cellValues.filter { predicate(it.value) }.keys
    }

    override fun find(predicate: (T?) -> Boolean): Cell? {
        return cellValues.entries.firstOrNull { predicate(it.value) }?.key
    }

    override fun any(predicate: (T?) -> Boolean): Boolean {
        return cellValues.any { predicate(it.value) }
    }

    override fun all(predicate: (T?) -> Boolean): Boolean {
        return cellValues.all { predicate(it.value) }
    }
}