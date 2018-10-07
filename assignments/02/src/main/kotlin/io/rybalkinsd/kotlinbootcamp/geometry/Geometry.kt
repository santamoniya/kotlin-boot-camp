package io.rybalkinsd.kotlinbootcamp.geometry

import java.lang.Integer.max
import java.lang.Integer.min

/**
 * Entity that can physically intersect, like flame and player
 */
interface Collider {
    fun isColliding(other: Collider): Boolean
    fun accept(visitor: Visitor): Boolean
}

/**
 * 2D point with integer coordinates
 */
class Point(val x: Int, val y: Int) : Collider {
    override fun accept(visitor: Visitor): Boolean = visitor.visit(this)

    override fun isColliding(other: Collider): Boolean = other.accept(PointVisitor(this))

    override fun equals(other: Any?): Boolean {
        if (other !is Point) return false
        return this.x == other.x && this.y == other.y
    }
}

/**
 * Bar is a rectangle, which borders are parallel to coordinate axis
 * Like selection bar in desktop, this bar is defined by two opposite corners
 * Bar is not oriented
 * (It does not matter, which opposite corners you choose to define bar)
 */
class Bar(firstCornerX: Int, firstCornerY: Int, secondCornerX: Int, secondCornerY: Int) : Collider {
    override fun accept(visitor: Visitor) = visitor.visit(this)

    val minX = min(firstCornerX, secondCornerX)
    val minY = min(firstCornerY, secondCornerY)
    val maxX = max(firstCornerX, secondCornerX)
    val maxY = max(firstCornerY, secondCornerY)
    val segments = listOf(Segment(Point(minX, minY), Point(minX, maxY)), Segment(Point(maxX, minY), Point(maxX, maxY)),
            Segment(Point(minX, minY), Point(maxX, minY)), Segment(Point(minX, maxY), Point(maxX, maxY)))
    val square = (maxX - minX) * (maxY - minY)

    override fun isColliding(other: Collider): Boolean = other.accept(BarVisitor(this))

    override fun equals(other: Any?): Boolean {
        if (other !is Bar) return false
        return this.maxX == other.maxX &&
                this.minX == other.minX &&
                this.maxY == other.maxY &&
                this.minY == other.minY
    }
}