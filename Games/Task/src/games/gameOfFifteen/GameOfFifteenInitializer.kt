package games.gameOfFifteen

interface GameOfFifteenInitializer {
    val initialPermutation: List<Int>
}

class RandomGameInitializer : GameOfFifteenInitializer {
    override val initialPermutation by lazy {
        generateValidPermutation()
    }

    private fun generateValidPermutation(): List<Int> {
        val permutation = (1..15).shuffled()
        return if (isEven(permutation)) permutation else {
            // Swap first two elements to make permutation even
            listOf(permutation[1], permutation[0]) + permutation.subList(2, permutation.size)
        }
    }
}