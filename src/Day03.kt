fun main() {

    /**
     * Algo:
     * 1. find most left
     * 2. walk back to right and collect number
     *
     * @return list of positions and number
     */
    fun collectNumber(matrix: Matrix<Char>, position: Position): Pair<List<Position>, Int> {
        val leftMost = matrix.walkToEdge(position, Direction.LEFT)
            .takeWhile { (_, value) -> value.isDigit() }
            .lastOrNull()
            ?: (position to matrix[position])

        val adjacentNumber = listOf(leftMost) + matrix.walkToEdge(leftMost.first, Direction.RIGHT)
            .takeWhile { (_, value) -> value.isDigit() }
            .toList()

        return adjacentNumber.map { it.first } to adjacentNumber.map { it.second }.joinToString("").toInt()
    }

    fun part1(input: List<String>): Int {
        val matrix: Matrix<Char> = input.map { it.toList() }

        val symbolsCoordinates: List<Position> = matrix.map2dIndexed { x, y, c ->
            if (!c.isDigit() && c != '.') x to y else null
        }
            .flatten()
            .filterNotNull()

        val visited = mutableSetOf<Position>()
        val result = symbolsCoordinates.sumOf {
            matrix.adjacent(it).mapNotNull { (position, char) ->
                if (visited.contains(position)) {
                    null
                } else if (char.isDigit()) {
                    val (visitedForCollect, number) = collectNumber(matrix, position)
                    visited.addAll(visitedForCollect)
                    number
                } else {
                    visited.add(position)
                    null
                }
            }
                .sum()
        }

        return result
    }

    fun part2(input: List<String>): Int {
        val matrix: Matrix<Char> = input.map { it.toList() }

        val potentialGears: List<Position> = matrix.map2dIndexed { x, y, c ->
            if (c == '*') x to y else null
        }
            .flatten()
            .filterNotNull()

        val visited = mutableSetOf<Position>()
        val result = potentialGears.sumOf {
            val potentialGearsValues = matrix.adjacent(it).mapNotNull { (position, char) ->
                if (visited.contains(position)) {
                    null
                } else if (char.isDigit()) {
                    val (visitedForCollect, number) = collectNumber(matrix, position)
                    visited.addAll(visitedForCollect)
                    number
                } else {
                    visited.add(position)
                    null
                }
            }.toList()

            if (potentialGearsValues.size == 2) {
                potentialGearsValues.reduce(Int::times)
            } else {
                0
            }
        }

        return result
    }

    val testInput = readInput("Day03_test")
    check(part1(testInput) == 4361)
    check(part2(testInput) == 467835)

    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}
