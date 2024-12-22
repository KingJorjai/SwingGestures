import java.awt.Point
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.WindowConstants

enum class Side{TOP,BOTTOM,LEFT,RIGHT}

fun main() {
    val panel = JPanel()
    val frame = JFrame().apply {
        setSize(600, 600)
        setLocationRelativeTo(null)
        defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
        isVisible = true
        add(panel)
    }

    panel.addMouseListener(object : MouseAdapter() {
        override fun mouseEntered(e: MouseEvent) {
            println(closestSide(e.point))
        }

        /**
         * Finds the closest side of the panel to the given point.
         *
         * Calculates the distance from the point to each side (top, bottom, left, right)
         * and returns the side with the shortest distance.
         *
         * @param p the point to evaluate.
         * @return the [Side] enum value representing the closest side.
         */
        fun closestSide(p: Point): Side {
            val distances = mapOf(
                Side.TOP to p.y,
                Side.BOTTOM to panel.height - p.y,
                Side.LEFT to p.x,
                Side.RIGHT to panel.width - p.x
            )

            return distances.minByOrNull { it.value }!!.key
        }
    })
}
