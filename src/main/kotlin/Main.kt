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

/**
 * A custom [JPanel] that allows a child component to follow the mouse cursor.
 *
 * When the mouse moves within the panel or its child, the child component is repositioned
 * so that its center aligns with the mouse cursor. Additionally, the closest side of the panel
 * is determined when the mouse enters the panel.
 *
 * @property child The child component that follows the mouse movement.
 */
class ChildFollowsMousePanel(private val child: Component): JPanel() {

    inner class MouseMoveListener(): MouseAdapter() {
        override fun mouseMoved(moved: MouseEvent) {
            val relativePoint = getRelativeMousePoint(moved)
            updateChildrenLocation(relativePoint)

        }

        override fun mouseDragged(dragged: MouseEvent) {
            val relativePoint = getRelativeMousePoint(dragged)
            updateChildrenLocation(relativePoint)
        }
    }

    inner class MouseEnterListener(): MouseAdapter() {

        override fun mouseEntered(entered: MouseEvent) {
            val relativePoint = getRelativeMousePoint(entered)
            println(closestSide(relativePoint))
        }
        /**
         * Finds the closest side of the panel to the given point.
         *
         * Calculates the distance from the point to each side (top, bottom, left, right)
         * and returns the side with the shortest distance.
         *
         * @param point the point to evaluate.
         * @return the [Side] enum value representing the closest side.
         */
        fun closestSide(point: Point): Side {
            val distances = mapOf(
                Side.TOP to point.y,
                Side.BOTTOM to this@ChildFollowsMousePanel.height - point.y,
                Side.LEFT to point.x,
                Side.RIGHT to this@ChildFollowsMousePanel.width - point.x
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

    /**
     * Converts the mouse event's point from the source component's coordinate system to the [ChildFollowsMousePanel] coordinate system.
     *
     * @param event The mouse event containing the point to convert.
     * @return The point relative to this panel.
     */
    private fun getRelativeMousePoint(event: MouseEvent): Point =
        SwingUtilities.convertPoint(event.component, event.point, this)

    /**
     * Updates the location of the [child] component based on the given point.
     *
     * Positions the child component so that its center aligns with the specified location.
     *
     * @param location The point to align the center of the child component to.
     */
    private fun updateChildrenLocation(location: Point) {
        child.location = Point(location.x - child.size.width / 2, location.y - child.height / 2)
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
