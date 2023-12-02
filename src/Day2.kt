fun main() {
    val redRegex = Regex("(\\d+) red")
    val greenRegex = Regex("(\\d+) green")
    val blueRegex = Regex("(\\d+) blue")

    fun parseInput(input: List<String>): List<List<Triple<Int, Int, Int>>> = input.map {
        it.split(";")
            .map { round ->
                val red = redRegex.find(round)?.groupValues?.get(1)?.toInt() ?: 0
                val green = greenRegex.find(round)?.groupValues?.get(1)?.toInt() ?: 0
                val blue = blueRegex.find(round)?.groupValues?.get(1)?.toInt() ?: 0
                Triple(red, green, blue)
            }
    }

    fun part1(input: List<String>): Int {
        val maxRed = 12
        val maxGreen = 13
        val maxBlue = 14

        return parseInput(input)
            .map { game ->
                game.map { (red, green, blue) ->
                    !(red > maxRed || green > maxGreen || blue > maxBlue)
                }
                    .all { isLegalRound -> isLegalRound }
            }
            .mapIndexed { index, isLegalGame -> if (isLegalGame) index + 1 else 0 }
            .sum()
    }

    fun part2(input: List<String>): Int = parseInput(input).sumOf { game ->
        val redMax = game.maxOf { (red, green, blue) -> red }
        val greenMax = game.maxOf { (red, green, blue) -> green }
        val blueMax = game.maxOf { (red, green, blue) -> blue }
        redMax * greenMax * blueMax
    }

    val testInput = readInput("Day02_test")
    check(part1(testInput) == 8)
    check(part2(testInput) == 2286)

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}
