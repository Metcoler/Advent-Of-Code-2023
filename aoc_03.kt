import java.io.File
import java.lang.StringBuilder

val file = File("input.txt").readLines()
val graph = file.map { it.toList() }
val height = graph.size
val width = graph[0].size
var visited = mutableSetOf<Pair<Int, Int>>();

fun valid_title(x: Int, y: Int) = (x in 0 until width && y in 0 until height)

fun all_neighbours(x: Int, y: Int): List<Pair<Int, Int>> {
    val neighbours = mutableListOf<Pair<Int, Int>>()
    (-1 .. 1).forEach{dy ->
        (-1 .. 1).filter { dx -> (dy != 0 || dx != 0) && valid_title(x + dx, y + dy) }
            .forEach { dx -> neighbours.add(Pair(x + dx, y + dy)) }
    }
    return neighbours.toList()
}

fun scan_number(x: Int, y: Int): Int {
    val number = StringBuilder()
    var scan_x = x
    while (scan_x >= 0 && graph[y][scan_x].isDigit()){
        number.insert(0, graph[y][scan_x])
        visited.add(Pair(scan_x, y))
        scan_x--
    }
    scan_x = x + 1
    while (scan_x < width && graph[y][scan_x].isDigit()) {
        number.append(graph[y][scan_x])
        visited.add(Pair(scan_x, y))
        scan_x++
    }
    return number.toString().toInt()
}

fun part_1(): Int {
    visited.clear()
    var sum = 0
    graph.forEachIndexed { y, line ->
        line.forEachIndexed { x, character ->
            if (!character.isDigit() && character != '.')
                for ((close_x, close_y) in all_neighbours(x, y)) {
                    if (Pair(close_x, close_y) in visited)
                        continue
                    if (graph[close_y][close_x].isDigit())
                        sum += scan_number(close_x, close_y)
                }
        }
    }
    return sum
}


fun part_2(): Int {
    visited.clear()
    var sum = 0
    graph.forEachIndexed { y, line ->
        line.forEachIndexed { x, character ->
            if (character == '*') {
                val gears = mutableListOf<Int>()
                for ((close_x, close_y) in all_neighbours(x, y)) {
                    if (Pair(close_x, close_y) in visited)
                        continue
                    if (graph[close_y][close_x].isDigit())
                        gears.add(scan_number(close_x, close_y))
                }
                if (gears.size == 2)
                    sum += gears[0] * gears[1]
            }
        }
    }
    return sum
}


fun main(args: Array<String>) {
    println(part_1())
    println(part_2())
}