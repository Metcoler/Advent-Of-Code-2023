import java.io.File

fun part_1(): Int {
    return File("input.txt")
        .readLines()
        .map(String::trim).sumOf { line ->
            val pyramid = mutableListOf<List<Int>>(line.split(" ").filter { it != "" }.map { it.toInt() })

            // generate pyramide
            while (!pyramid.last().all { it == 0 }) {
                pyramid.add(
                    (1..pyramid.last().lastIndex)
                        .map { pyramid.last()[it] - pyramid.last()[it - 1] }
                        .toList()
                )
            }
            // calculate next number
            pyramid.reversed().sumOf { it.last() }
        }

}

fun part_2(): Int {
    return File("input.txt")
        .readLines()
        .map(String::trim).sumOf { line ->
            val pyramid = mutableListOf<List<Int>>(line.split(" ").filter { it != "" }.map { it.toInt() })

            // generate pyramide
            while (!pyramid.last().all { it == 0 }) {
                pyramid.add(
                    (1..pyramid.last().lastIndex)
                        .map { pyramid.last()[it] - pyramid.last()[it - 1] }
                        .toList()
                )
            }
            // calculate next number
            var number = 0
            pyramid.reversed().forEach {
                number = it.first() - number
            }
            number
        }

}

fun main(args: Array<String>) {
    println(part_1())
    println(part_2())
}