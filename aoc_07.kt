import java.io.File

class PokerHand(val cards: String, val bid: Int, val joker_replacable: Boolean): Comparable<PokerHand>{
    val CARDS_ORDER = if (joker_replacable)
        listOf<Char>('A', 'K', 'Q', 'T', '9', '8', '7', '6', '5', '4', '3', '2', 'J').reversed()
    else
        listOf<Char>('A', 'K', 'Q', 'J', 'T', '9', '8', '7', '6', '5', '4', '3', '2').reversed()

    // five, four, full , three, two, pair + card, card, card, card, card
    val evaluated_hand: List<Int> = evaluate_hand()

    fun evaluate_hand(): List<Int> {
        if (!joker_replacable)
            return evaluate(cards)
        var best_eval = List<Int>(11) {0}

        CARDS_ORDER.forEach { joker ->
            val potential_eval = evaluate(cards.replace('J', joker))
            if (list_comparison(potential_eval, best_eval) == 1)
                best_eval = potential_eval
        }
        return best_eval
    }

    private fun evaluate(cards: String): List<Int> {
        cards.forEach { card ->
            // Five of a kind
            if (cards.count { it == card } == 5)
                return listOf(1, 0, 0, 0, 0, 0) + card_ranks()
        }

        cards.forEach { card ->
            // Four of a kind
            if (cards.count { it == card } == 4)
                return listOf(0, 1, 0, 0, 0, 0) + card_ranks()
        }

        cards.forEach { card ->
            // Full house
            cards.filter { it != card }.forEach { other_card ->
                if (cards.count { it == card } == 3 && cards.count { it == other_card } == 2)
                    return listOf(0, 0, 1, 0, 0, 0) + card_ranks()
            }
        }

        cards.forEach { card ->
            // Three of a kind
            if (cards.count { it == card } == 3)
                return listOf(0, 0, 0, 1, 0, 0) + card_ranks()
        }

        cards.forEach { card ->
            // Two pair
            cards.filter { it != card }.forEach { other_card ->
                if (cards.count { it == card } == 2 && cards.count { it == other_card } == 2)
                    return listOf(0, 0, 0, 0, 1, 0) + card_ranks()
            }
        }

        cards.forEach { card ->
            // One pair
            if (cards.count { it == card } == 2)
                return listOf(0, 0, 0, 0, 0, 1) + card_ranks()
        }
        return listOf(0, 0, 0, 0, 0, 0) + card_ranks()
    }

    fun card_ranks(): List<Int> {
        return cards.map { CARDS_ORDER.indexOf(it) }
    }

    override fun compareTo(other: PokerHand): Int {
        return list_comparison(evaluated_hand, other.evaluated_hand)
    }

    private fun list_comparison(list: List<Int>, other: List<Int>): Int {
        (0 .. list.lastIndex).forEach {index ->
            if (list[index] < other[index]) return -1
            if (list[index] > other[index]) return 1
        }
        return 0
    }
}


fun part_1(): Int {
    return File("input.txt")
        .readLines()
        .map(String::trim)
        .map { line ->
            val (cards, bid_string) = line.split(" ")
            PokerHand(cards, bid_string.toInt(), false)
        }.sorted()
        .mapIndexed { index, pokerHand -> (index + 1) * pokerHand.bid }
        .sum()
}

fun part_2(): Int {
    return File("input.txt")
        .readLines()
        .map(String::trim)
        .map { line ->
            val (cards, bid_string) = line.split(" ")
            PokerHand(cards, bid_string.toInt(), true)
        }.sorted()
        .mapIndexed { index, pokerHand -> (index + 1) * pokerHand.bid }
        .sum()
}


fun main(args: Array<String>) {
    println(part_1())
    println(part_2())
}