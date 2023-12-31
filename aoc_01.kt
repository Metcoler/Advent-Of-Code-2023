import java.io.File
import kotlin.text.StringBuilder

val str_to_numbers: List<String> = listOf(
    "zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine"
)

fun part_1(): Int {
    return File("input.txt")
        .readLines()
        .map{line -> "${line.first { it.isDigit() }}${line.last{ it.isDigit()}}".toInt()}
        .sum()
}

fun part_2(): Int {
    return File("input.txt")
        .readLines()
        .map {line ->
            val digit_line = StringBuilder()
            for((index, character) in line.withIndex()) {
                if (character.isDigit()) {
                    digit_line.append(character)
                    continue
                }
                str_to_numbers.forEachIndexed { int_digit, string_digit ->
                    if (line.indexOf(string_digit) == index || line.lastIndexOf(string_digit) == index)
                        digit_line.append(int_digit)
                }
            }
            "${digit_line.first { it.isDigit() }}${digit_line.last{ it.isDigit()}}".toInt()
    }.sum()
}


fun main(args: Array<String>) {
    println(part_1())
    println(part_2())
}