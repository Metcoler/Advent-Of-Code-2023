import java.io.File

class Node(val name: String, val left: String, val right: String) {
    override fun hashCode(): Int {
        return name.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Node) return false
        return name == other.name
    }
}

fun part_1(): Int {
    var file = File("input.txt").readLines().map(String::trim)
    val instructions = file[0]
    file = file.drop(2)

    val graph = mutableMapOf<String, Node>()
    file.forEach { line ->
        val (name, children) = line.split(" = ")
        val (left, right) = children.replace("(", "").replace(")", "").split(", ")
        graph[name] = Node(name, left, right)
    }

    var node = graph["AAA"]!!
    var instruction_reader = 0
    var steps = 0
    while (node.name != "ZZZ") {
        node = if (instructions.get(instruction_reader) == 'R')
            graph[node.right]!!
        else
            graph[node.left]!!
        instruction_reader = (instruction_reader + 1) % instructions.length
        steps++
    }
    return steps
}

fun part_2(): Long {
    fun greatest_common_divisor(number1: Long, number2: Long): Long {
        if (number1 == 0L) return number2
        return greatest_common_divisor(number2 % number1, number1)
    }
    fun smallest_common_multiple(number1: Long, number2: Long): Long {
        return (number1 * number2) / greatest_common_divisor(number1, number2)
    }
    var file = File("input.txt").readLines().map(String::trim)
    val instructions = file[0]
    file = file.drop(2)

    val graph = mutableMapOf<String, Node>()
    file.forEach { line ->
        val (name, children) = line.split(" = ")
        val (left, right) = children.replace("(", "").replace(")", "").split(", ")
        graph[name] = Node(name, left, right)
    }

    return graph.filter { it.key.last() == 'A' }.map {
        var node = it.value
        var instruction_reader = 0
        var distance = 0L
        while (node.name.last() != 'Z') {
            node = if (instructions.get(instruction_reader) == 'R')
                graph[node.right]!!
            else
                graph[node.left]!!
            instruction_reader = (instruction_reader + 1) % instructions.length
            distance++
        }
        distance
    }.reduce{distance1, distance2 -> smallest_common_multiple(distance1, distance2)}
}

fun main(args: Array<String>) {
    println(part_1())
    println(part_2())
}