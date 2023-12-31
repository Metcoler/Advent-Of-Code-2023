import java.io.File

val colors = listOf<String>("red", "green", "blue")
val compare_to = listOf<Int>(12, 13, 14)

fun part_1(): Int {
    val file = File("input.txt").readLines()
    var output = 0
    file.forEachIndexed { index, line ->
        val game_id = index + 1
        if (line.substring(line.indexOf(':') + 1)
            .strip()
            .split("; ")
            .all { it.split(", ")
                .all {
                    val number_color = it.split(" ")
                    val number = number_color[0].toInt()
                    val color = number_color[1]
                    val color_index = colors.indexOf(color)
                    number <= compare_to[color_index]
                }
            }
        ) output += game_id

    }
    return output
}



fun part_2(): Int {
    val file = File("input.txt").readLines()
    var output = 0
    file.forEach {game ->
        val minimum = mutableListOf<Int>(0, 0, 0)
        game.substring(game.indexOf(':') + 1)
                .strip()
                .split("; ")
                .forEach { set ->
                    set.split(", ").forEach {
                        val number_color = it.split(" ")
                        val number = number_color[0].toInt()
                        val color = number_color[1]
                        val color_index = colors.indexOf(color)
                        minimum[color_index] = maxOf(minimum[color_index], number)
                    }
                }
        output += minimum.reduce{ num1, num2 -> num1 * num2 }

    }
    return output
}

fun main(args: Array<String>) {
    println(part_1())
    println(part_2())
}