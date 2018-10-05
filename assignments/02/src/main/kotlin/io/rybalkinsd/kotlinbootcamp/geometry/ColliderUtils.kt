package io.rybalkinsd.kotlinbootcamp.geometry

infix fun Point.intersect(other: Point): Boolean = this.x == other.x && this.y == other.y

infix fun Point.intersect(bar: Bar): Boolean =
    this.x >= bar.minX &&
    this.x <= bar.maxX &&
    this.y >= bar.minY &&
    this.y <= bar.maxY

infix fun Bar.intersect(other: Bar): Boolean {
    // if one bar includes other
    if (this.square <= other.square) {
        if (this.minX >= other.minX && this.maxX <= other.maxX && this.minY >= other.minY && this.maxY <= other.maxY)
            return true
    } else {
        if (this.minX <= other.minX && this.maxX >= other.maxX && this.minY <= other.minY && this.maxY >= other.maxY)
            return true
    }
    // other variants of intersection
    var result = false
    for (segment1 in this.segments) {
        for (segment2 in other.segments)
            result = result || segment1 intersect segment2
    }
    return result
}

/**
 * Segment - one side of bar
 * Coordinate of start point of segment lower than some coordinate of end point
 * Segments may be either vertical, or horizontal
 */
class Segment(first: Point, second: Point) {
    val start = if (first.x <= second.x || first.y <= second.y) first else second
    val end = if (start == first) second else first
    fun isVertical() = this.start.x == this.end.x
    fun isHorizontal() = this.start.y == this.end.y
}

infix fun Segment.intersect(other: Segment): Boolean {
    if (this.isHorizontal() && other.isHorizontal() || this.isVertical() && other.isVertical()) return false
    var seg1 = this
    var seg2 = other
    if (other.isVertical() && this.isHorizontal()) {
        seg1 = other
        seg2 = this
    }
    return seg1.start.x >= seg2.start.x &&
    seg1.start.x <= seg2.end.x &&
    seg2.start.y >= seg1.start.y &&
    seg2.start.y <= seg1.end.y
}