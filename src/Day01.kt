fun main() {

    fun part1(input: List<String>) = input
        .map { line ->
            val digits = line.filter { it.isDigit() }
            buildString { append(digits.first()); append(digits.last()) }
        }
        .sumOf { it.toInt() }

    val possibleSpelledDigits = listOf("one", "two", "three", "four", "five", "six", "seven", "eight", "nine")
    val digitsValueLookupTable: Map<String, Int> =
        possibleSpelledDigits.mapIndexed { index, spelledDigit -> spelledDigit to (index + 1) }.toMap() +
                (0..9).associateBy { it.toString() }

    fun part2(input: List<String>): Int = input.map { line ->
        val (_, firstDigit) = requireNotNull(line.findAnyOf(digitsValueLookupTable.keys))
        val (_, lastDigit) = requireNotNull(line.findLastAnyOf(digitsValueLookupTable.keys))

        buildString {
            append(digitsValueLookupTable[firstDigit])
            append(digitsValueLookupTable[lastDigit])
        }
    }.sumOf { it.toInt() }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    val testInput2 = readInput("Day01_test2")

    check(part1(testInput) == 142)
    check(part2(testInput2) == 281 + 18)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
