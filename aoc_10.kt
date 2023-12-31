import java.io.File
import java.security.KeyStore.TrustedCertificateEntry

val down_set = "|7FS".toSet()
val up_set = "|LJS".toSet()
val left_set = "-J7S".toSet()
val right_set = "-LFS".toSet()

class Node(val x: Int, val y: Int){
    override fun hashCode(): Int {
        return 31 * x + y
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Node) return false
        return x == other.x && y == other.y
    }

    override fun toString(): String {
        return "($x, $y)"
    }
    fun up() = Node(x, y-1)
    fun down() = Node(x, y+1)
    fun left() = Node(x-1, y)
    fun right() = Node(x+1, y)
    fun all_neighbours() = listOf<Node>(up(), down(), left(), right())

}

fun part_1(): Int {
    val playground = File("input.txt").readLines().map(String::trim).map { it.toList() }
    fun correct_node(node: Node): Boolean {
        return (node.x in (0 .. playground[0].lastIndex) && node.y in (0..playground.lastIndex))
    }

    var start: Node = Node(0, 0)
    val graph = mutableMapOf<Node, List<Node>>()
    playground.forEachIndexed { y, line ->
        line.forEachIndexed { x, pipe ->
            val node = Node(x, y)
            if (pipe == 'S')
                start = node
            if (pipe in up_set && correct_node(node.up()))
                graph[node] = graph.getOrDefault(node, listOf()) + listOf(node.up())
            if (pipe in down_set && correct_node(node.down()))
                graph[node] = graph.getOrDefault(node, listOf()) + listOf(node.down())
            if (pipe in right_set && correct_node(node.right()))
                graph[node] = graph.getOrDefault(node, listOf()) + listOf(node.right())
            if (pipe in left_set && correct_node(node.left()))
                graph[node] = graph.getOrDefault(node, listOf()) + listOf(node.left())
        }
    }

    var loop = listOf<Node>()
    graph[start]!!.forEach {next_node ->
        var queue = listOf<List<Node>>(mutableListOf(start, next_node))
        while (!queue.isEmpty()) {
            val next_queue = mutableListOf<List<Node>>()
            for (path in queue) {
                val node = path.last()
                val neighbours = graph.getOrDefault(node, listOf())
                if (path.size > 2 && start in neighbours) {
                    if (loop.size < path.size)
                        loop = path.toList()
                    next_queue.clear()
                    break
                }
                next_queue.addAll( neighbours.filter { it !in path }.map{path + it} )
            }
            queue = next_queue
        }
    }
    return loop.size / 2
}


fun part_2(): Int {
    val playground = File("input.txt").readLines().map(String::trim).map { it.toList() }
    fun correct_node(node: Node): Boolean {
        return (node.x in (0 .. playground[0].lastIndex) && node.y in (0..playground.lastIndex))
    }
    fun correct_node_bigger(node: Node): Boolean {
        return (node.x in (0 ..< 2*playground[0].size) && node.y in (0..< 2*playground.size))
    }

    var start: Node = Node(0, 0)
    val graph = mutableMapOf<Node, List<Node>>()
    playground.forEachIndexed { y, line ->
        line.forEachIndexed { x, pipe ->
            val node = Node(x, y)
            if (pipe == 'S')
                start = node
            if (pipe in up_set && correct_node(node.up()))
                graph[node] = graph.getOrDefault(node, listOf()) + listOf(node.up())
            if (pipe in down_set && correct_node(node.down()))
                graph[node] = graph.getOrDefault(node, listOf()) + listOf(node.down())
            if (pipe in right_set && correct_node(node.right()))
                graph[node] = graph.getOrDefault(node, listOf()) + listOf(node.right())
            if (pipe in left_set && correct_node(node.left()))
                graph[node] = graph.getOrDefault(node, listOf()) + listOf(node.left())
        }
    }

    var loop = listOf<Node>()
    graph[start]!!.forEach {next_node ->
        var queue = listOf<List<Node>>(mutableListOf(start, next_node))
        while (!queue.isEmpty()) {
            val next_queue = mutableListOf<List<Node>>()
            for (path in queue) {
                val node = path.last()
                val neighbours = graph.getOrDefault(node, listOf())
                if (path.size > 2 && start in neighbours) {
                    if (loop.size < path.size)
                        loop = path.toList()
                    next_queue.clear()
                    break
                }
                next_queue.addAll( neighbours.filter { it !in path }.map{path + it} )
            }
            queue = next_queue
        }
    }
    graph[start] = loop.filter { node -> start in graph[node]!! }.toList()

    val bigger_playground = MutableList<MutableList<Int>>(2 * playground.size) { MutableList<Int>(2 * playground[0].size) {0}  }

    fun inside_bigger_loop(node: Node, visited: MutableSet<Node>): Boolean {
        var output = true
        for (neighbour in node.all_neighbours()) {
            if (neighbour in visited)
                continue
            if (!correct_node_bigger(neighbour))
                return false
            if (bigger_playground[neighbour.y][neighbour.x] != 0)
                continue
            visited.add(neighbour)
            output = output && inside_bigger_loop(neighbour, visited)
        }
        return output
    }

    loop.forEach { node ->
        graph[node]!!.forEach { neighbour ->
            val dx = neighbour.x - node.x
            val dy = neighbour.y - node.y
            bigger_playground[2*node.y + dy][2*node.x + dx] = 1
        }
        bigger_playground[2*node.y][2*node.x] = 1
    }

    var counter = 0
    playground.forEachIndexed { y, line ->
        line.forEachIndexed { x, pipe ->
            val node = Node(x, y)
            val bigger_node = Node(2*x, 2*y)
            if (node !in loop && inside_bigger_loop(bigger_node, mutableSetOf(bigger_node)))
                counter++
        }
    }
    return counter
}




fun main(args: Array<String>) {
    println(part_1())
    println(part_2())
}