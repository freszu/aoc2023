import kotlin.math.max
import kotlin.math.min

typealias Matrix<T> = List<List<T>>

inline fun <T, R> Matrix<T>.map2d(transform: (T) -> R) = this.map { it.map(transform) }

inline fun <T, R> Matrix<T>.map2dIndexed(transform: (x: Int, y: Int, T) -> R) = mapIndexed { y, rows ->
    rows.mapIndexed { x, t -> transform(x, y, t) }
}

operator fun <T> Matrix<T>.get(position: Position): T = this[position.y][position.x]
fun <T> Matrix<T>.getOrNull(position: Position): T? = this.getOrNull(position.y)?.getOrNull(position.x)
fun <T> Matrix<T>.getOrNull(x: Int, y: Int) = this.getOrNull(y)?.getOrNull(x)

fun <T> Matrix<T>.replace(position: Position, value: T): Matrix<T> {
    return this.toMutableList().apply {
        this[position.y] = this[position.y].toMutableList().apply {
            this[position.x] = value
        }
    }
}

/**
 * Clockwise: Left, left-top, top etc.
 *
 * @return Sequence of adjacent positions and their values
 */
fun <T> Matrix<T>.adjacent(position: Position): Sequence<Pair<Position, T>> {
    val (x, y) = position

    return sequenceOf(
        x - 1 to y,
        x - 1 to y + 1,
        x to y + 1,
        x + 1 to y + 1,
        x + 1 to y,
        x + 1 to y - 1,
        x to y - 1,
        x - 1 to y - 1
    )
        .mapNotNull {
            val value = getOrNull(it.x, it.y)
            if (value == null) null else it to value
        }
}

fun <T> Matrix<T>.findAll(value: T) = sequence {
    for (y in indices) {
        for (x in this@findAll[y].indices) {
            if (this@findAll[y][x] == value) yield(x to y)
        }
    }
}

fun Position.walkHorizontallyOrVertically(to: Position): Sequence<Position> = sequence {
    if (x != to.x) {
        for (x in min(x, to.x)..max(x, to.x)) {
            yield(copy(x = x))
        }
    }
    if (y != to.y) {
        for (y in min(y, to.y)..max(y, to.y)) {
            yield(copy(y = y))
        }
    }
}

fun <T> Matrix<T>.nicePrint() = joinToString("\n")

enum class Direction { LEFT, TOP, RIGHT, BOTTOM }

fun <T> Matrix<T>.walkToEdge(position: Position, direction: Direction): Sequence<Pair<Position, T>> = sequence {
    var offset = 1
    while (true) {
        val newPosition = when (direction) {
            Direction.LEFT -> position.x - offset to position.y
            Direction.TOP -> position.x to position.y - offset
            Direction.RIGHT -> position.x + offset to position.y
            Direction.BOTTOM -> position.x to position.y + offset
        }
        val inLine = this@walkToEdge.getOrNull(newPosition)?.let { newPosition to it }
        if (inLine == null) break else yield(inLine)
        offset++
    }
}

data class Position(val x: Int, val y: Int)

infix fun Int.to(y: Int) = Position(this, y)

val Pair<Int, Int>.x: Int get() = this.first

val Pair<Int, Int>.y: Int get() = this.second

