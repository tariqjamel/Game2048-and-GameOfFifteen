package games.gameOfFifteen

fun isEven(permutation: List<Int>): Boolean {
    var inversions = 0
    for (i in permutation.indices) {
        for (j in i + 1 until permutation.size) {
            if (permutation[i] > permutation[j]) inversions++
        }
    }
    return inversions % 2 == 0
}