package io.rybalkinsd.kotlinbootcamp.geometry

interface Visitor {
    fun visit(p: Point): Boolean
    fun visit(b: Bar): Boolean
}

class PointVisitor(private val p: Point) : Visitor {
    override fun visit(b: Bar) = this.p intersect b

    override fun visit(p: Point) = this.p intersect p
}
class BarVisitor(private val b: Bar) : Visitor {
    override fun visit(b: Bar) = this.b intersect b

    override fun visit(p: Point) = p intersect this.b
}