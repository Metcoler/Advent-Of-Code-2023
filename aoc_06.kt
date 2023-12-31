import java.io.File

fun number_of_possible_wins(record_time: Long, record_distance: Long): Long {
    return (1 .. record_time).map { hold_time ->
        val speed = hold_time
        (record_time - hold_time) * speed
    }.filter { distance -> distance > record_distance }.count().toLong()
}

fun part_1(): Long {
    val file = File("input.txt").readLines().map{ it.trim() }
    val record_times = file[0].substringAfter("Time:").split(" ").filter { it != "" }.map { it.toLong() }
    val record_distances = file[1].substringAfter("Distance:").split(" ").filter { it != "" }.map { it.toLong() }
    val num_races = record_times.size

    return (0 ..< num_races)
        .map { number_of_possible_wins(record_times[it], record_distances[it])}
        .reduce{ possible_wins_1, possible_wins_2 -> possible_wins_1 * possible_wins_2 }
}

fun part_2(): Long {
    val file = File("input.txt").readLines().map{ it.trim() }
    val record_time = file[0].filter { it.isDigit() }.toLong()
    val record_distance = file[1].filter { it.isDigit() }.toLong()

    return number_of_possible_wins(record_time, record_distance)
}

fun main(args: Array<String>) {
    println(part_1())
    println(part_2())
}