fun main() {

    /**
     * @return List of 2 sets: wining and my numbers
     */
    fun parse(input: List<String>): List<Pair<Set<Int>, Set<Int>>> = input.map { line ->
        val (wining, my) = line.split(":", "|")
            .drop(1)
            .map {
                it.split(" ").map(String::trim).filter(String::isNotBlank).map(String::toInt).toSet()
            }
        wining to my
    }

    fun part1(input: List<String>): Int {
        return parse(input).map { (wining, my) -> wining.intersect(my).size }
            .sumOf { winingCount -> if (winingCount == 0) 0 else 1 shl (winingCount - 1) }
    }

    fun part2(input: List<String>): Int {
        val scores = parse(input).map { (wining, my) -> wining.intersect(my).size }

        val copies = IntArray(scores.size) { 1 }
        for (i in scores.indices) repeat(scores[i]) { copies[it + i + 1] += copies[i] }

        return copies.sum()
    }

    fun part2NoMut(input: List<String>): Int {
        val scores = parse(input).map { (wining, my) -> wining.intersect(my).size }
        return scores.reversed().fold(emptyList<Int>()) { acc, i ->
            val count = 1 + (0..<i).sumOf { acc[it] }
            listOf(count) + acc
        }
            .sum()
    }

    val testInput = readInput("Day04_test")
    check(part1(testInput) == 13)
    check(part2(testInput) == 30 && part2(testInput) == part2NoMut(testInput))

    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}
