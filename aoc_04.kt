import java.io.File

val file = File("input.txt").readLines()

fun part_1(): Int {
    return file.map {
        val line = it.substring(it.indexOf(':') + 1 ).split(" | ")
        val winning_numbers = line[0].split(" ").filter { it != "" }.toSet()
        val my_numbers = line[1].split(" ").filter { it != "" }.toSet()
        winning_numbers.intersect(my_numbers).size
    }.filter {it > 0}.map{ 1 shl (it - 1)}.sum()
}

fun part_2(): Int {
    val card_winnings = mutableListOf<Int>()
    var queue = mutableListOf<Int>()
    file.forEachIndexed { card_number, it ->
        val line = it.substring(it.indexOf(':') + 1 ).split(" | ")
        val winning_numbers = line[0].split(" ").filter { it != "" }.toSet()
        val my_numbers = line[1].split(" ").filter { it != "" }.toSet()
        card_winnings.add(winning_numbers.intersect(my_numbers).size)
        queue.add(card_number)
    }
    var sum = 0
    while (!queue.isEmpty()) {
        sum += queue.size
        val next_queue = mutableListOf<Int>()
        queue.forEach { card->
            next_queue += (card + 1..card + card_winnings[card]).toList()
        }
        queue = next_queue
    }
    return sum
}

fun main(args: Array<String>) {
    println(part_1())
    println(part_2())
}