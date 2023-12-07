fun main() {

    fun calculate(seeds: Sequence<Long>, rawInput: String): Long {
        val mappings = rawInput.split("\n\n").drop(1)
            .map { section ->
                section.split("\n")
                    .drop(1)
                    .map { it.split(" ").map(String::toLong) }
                    .map { (dest, source, length) -> source..source + length to dest..dest + length }
            }

        return seeds.minOf { seed ->
            mappings.fold(seed) { acc, maps ->
                maps.firstOrNull { map -> map.first.contains(acc) }
                    ?.let { map -> map.second.first + acc - map.first.first }
                    ?: acc
            }
        }
    }

    fun part1(input: String): Long {
        val seeds = input.takeWhile { it != '\n' }.drop(7).split(" ").map(String::toLong)

        return calculate(seeds.asSequence(), input)
    }

    /**
     * WARNING! Bruteforce solution, takes a lot of time to complete. But hey - it completes! :)
     */
    fun part2(input: String): Long {
        val seeds = input.takeWhile { it != '\n' }.drop(7).split(" ").map(String::toLong)
            .windowed(2, step = 2)
            .map { (a, b) -> (a..<a + b).asSequence() }
            .reduce { acc, seq -> acc + seq }

        return calculate(seeds, input)
    }

    val testInput = readInputAsString("Day05_test").dropLast(1)
    check(part1(testInput) == 35L)
    check(part2(testInput) == 46L)

    val input = readInputAsString("Day05").dropLast(1)
    part1(input).println()
    part2(input).println()
}
