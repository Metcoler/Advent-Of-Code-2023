import java.io.File
import kotlin.math.abs

class Galaxy(val x: Long, val y: Long) {
    fun distance(other: Galaxy) = abs(x - other.x) + abs(y - other.y)
}

fun part_1(): Long {
    val matrix = File("input.txt").readLines().map(String::trim).map { it.toList() }
    val galaxies = mutableListOf<Galaxy>()

    var offset_y = 0L
    for ((y, line) in matrix.withIndex()) {
        if (line.all { it == '.' }) {
            offset_y++
            continue
        }

        var offset_x = 0L
        for ((x, char) in line.withIndex()) {
            if (matrix.all { it[x] == '.' }) {
                offset_x++
                continue
            }
            if (char == '#')
                galaxies.add((Galaxy(x + offset_x, y + offset_y)))
        }
    }

    var sum = 0L
    galaxies.forEachIndexed { index, galaxy ->
        galaxies.forEachIndexed { other_index, other_galaxy ->
            if (index < other_index)
                sum += galaxy.distance(other_galaxy)
        }
    }
    return sum
}


fun part_2(): Long {
    val matrix = File("input.txt").readLines().map(String::trim).map { it.toList() }
    val galaxies = mutableListOf<Galaxy>()

    var offset_y = 0L
    val expand = 1000000 - 1
    for ((y, line) in matrix.withIndex()) {
        if (line.all { it == '.' }) {
            offset_y += expand
            continue
        }

        var offset_x = 0L
        for ((x, char) in line.withIndex()) {
            if (matrix.all { it[x] == '.' }) {
                offset_x += expand
                continue
            }
            if (char == '#')
                galaxies.add((Galaxy(x + offset_x, y + offset_y)))
        }
    }

    var sum = 0L
    galaxies.forEachIndexed { index, galaxy ->
        galaxies.forEachIndexed { other_index, other_galaxy ->
            if (index < other_index)
                sum += galaxy.distance(other_galaxy)
        }
    }
    return sum
}

fun main(args: Array<String>) {
    println(part_1())
    println(part_2())
}