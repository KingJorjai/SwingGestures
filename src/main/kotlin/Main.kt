import java.awt.Color
import java.awt.Component
import java.awt.Dimension
import java.awt.Point
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.SwingUtilities
import javax.swing.WindowConstants

enum class Side{TOP,BOTTOM,LEFT,RIGHT}

class ChildFollowsMousePanel(private val child: Component): JPanel() {

    inner class MouseMoveListener(): MouseAdapter() {
        override fun mouseMoved(e: MouseEvent) {
            val relativePoint = SwingUtilities.convertPoint(e.component, e.point, this@ChildFollowsMousePanel)

            this@ChildFollowsMousePanel.
            child.location = Point(relativePoint.x-child.size.width/2, relativePoint.y-child.height/2)
        }
    }

    inner class MouseEnterListener(): MouseAdapter() {
        override fun mouseEntered(e: MouseEvent) {
            val relativePoint = SwingUtilities.convertPoint(e.component, e.point, this@ChildFollowsMousePanel)
            println(closestSide(relativePoint))
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
                Side.BOTTOM to this@ChildFollowsMousePanel.height - p.y,
                Side.LEFT to p.x,
                Side.RIGHT to this@ChildFollowsMousePanel.width - p.x
            )

            return distances.minByOrNull { it.value }!!.key
        }
    }

    init {
        addMouseMotionListener(MouseMoveListener())

        child.addMouseMotionListener(MouseMoveListener())
        child.addMouseListener(MouseEnterListener())

        add(child)
        layout = null
    }
}

fun main() {
    val panel = ChildFollowsMousePanel(JPanel().apply {
        size = Dimension(150,50)
        background = Color.black
    })
    val frame = JFrame().apply {
        add(panel)
        setSize(600, 600)
        setLocationRelativeTo(null)
        defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
        isVisible = true
    }
}
