import java.io.File

fun change_range_if_overlaps_with_source(range: LongRange, map_range: LongRange, change: Long): List<LongRange> {
    // returns list of ranges, but on index 0 there will be changed range... rest of list will be unchanged

    // range:                     |------|
    // map_range:             |--------------|
    if (range.first in map_range && range.last in map_range)
        return listOf((range.first + change .. range.last + change))

    // range:         |-----------------|
    // map_range:           |----|
    if (map_range.first in range && map_range.last in range)
        return listOf(
            (map_range.first + change .. map_range.last + change),
            (range.first..< map_range.first),
            (map_range.last + 1..< range.last)
        )

    // range:         |-------------|
    // map_range:           |------------|
    if (map_range.first in range)
        return listOf(
            (map_range.first + change .. range.last + change), (range.first..<map_range.first)
        )

    // range:                |-------------|
    // map_range:   |-----------|
    if (map_range.last in range)
        return listOf(
            (range.first + change .. map_range.last + change), (map_range.last + 1 ..<range.last)
        )

    // No overlap
    return listOf((0L..<0L), range)
}

fun find_minimal_destination_range(range: LongRange, remaining_maps: List<List<List<Long>>>): LongRange {

    // final range
    if (remaining_maps.isEmpty())
        return range

    val current_map = remaining_maps[0]

    // go to next map
    if (current_map.isEmpty())
        return find_minimal_destination_range(range, remaining_maps.drop(1))

    val (destination, source, length) = current_map[0]
    val change = destination - source
    val source_interval = (source ..< source + length)
    val new_intervals = change_range_if_overlaps_with_source(range, source_interval, change)
    val changed_interval = new_intervals[0]

    // if there is no overlap
    if (changed_interval.isEmpty())
        return find_minimal_destination_range(range, listOf(current_map.drop(1)) + remaining_maps.drop(1))

    return new_intervals.mapIndexed { index, interval ->
        // changed interval (that overlapped)
        if (index == 0)
            find_minimal_destination_range(interval, remaining_maps.drop(1))
        // no changed interval/s
        else
            find_minimal_destination_range(interval, listOf(current_map.drop(1)) + remaining_maps.drop(1))
    }.minBy { it.first }
}


fun part_1(): Long {

    var file = File("input.txt").readLines().map { line -> line.trim() }
    val seeds = file[0].substringAfter("seeds: ").split(" ").map { it.toLong() }.toMutableList()

    file = file.subList(1, file.size)
    val visited = mutableSetOf<Long>()
    for (line in file) {

        if (line.isEmpty())
            continue
        if ("map" in line) {
            visited.clear();
            continue
        }
        val (destination, source, length) = line.split(" ").map { it.toLong() }
        val change = destination - source

        for (index in (0 .. seeds.lastIndex)){
            val seed = seeds[index]
            if (seed in visited) continue
            if (seed !in (source ..< source + length)) continue
            seeds[index] = seed + change
            visited.add(seed + change)
        }
    }
    return seeds.min()
}



fun part_2(): Long {
    var file = File("input.txt").readLines().map { line -> line.trim() }
    val seeds_line = file[0].substringAfter("seeds: ").split(" ").map { it.toLong() }.toMutableList()

    // construct ranges
    val seeds_ranges = (0 ..< seeds_line.lastIndex step 2).map{
        val range_from = seeds_line[it]
        val length = seeds_line[it + 1]
        (range_from ..< range_from + length)
    }.toMutableList()

    // construct maps instructions
    file = file.subList(1, file.size)
    val all_maps = mutableListOf<List<List<Long>>>()
    val map = mutableListOf<List<Long>>()
    for (line in file) {
        if (line.isEmpty())
            continue
        if ("map" in line) {
            all_maps.add(map.toList())
            map.clear()
            continue
        }
        val submap = line.split(" ").map { it.toLong() }
        map.add(submap)
    }
    all_maps.add(map.toList())

    return seeds_ranges.map {
        find_minimal_destination_range(it, all_maps.toList())
    }.minBy { it.first }.first
}





fun main(args: Array<String>) {
    println(part_1())
    println(part_2())
}