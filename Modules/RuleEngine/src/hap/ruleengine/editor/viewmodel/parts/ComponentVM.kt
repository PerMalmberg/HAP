package hap.ruleengine.editor.viewmodel.parts

import hap.ruleengine.editor.viewmodel.IDrawingSurfaceView
import hap.ruleengine.parts.IComponent
import hap.ruleengine.parts.IConnectionPoint
import hap.ruleengine.parts.input.BooleanInput
import hap.ruleengine.parts.input.DoubleInput
import hap.ruleengine.parts.input.StringInput
import hap.ruleengine.parts.output.BooleanOutput
import hap.ruleengine.parts.output.DoubleOutput
import hap.ruleengine.parts.output.StringOutput
import javafx.beans.property.DoubleProperty
import javafx.beans.property.StringProperty
import javafx.scene.paint.Color
import tornadofx.ViewModel
import tornadofx.getProperty
import tornadofx.observable
import tornadofx.property
import java.util.*

///////////////////////////////////////////////////////////////////////////////
//
//
///////////////////////////////////////////////////////////////////////////////
class ComponentVM constructor(val component: IComponent, val isSelectable: Boolean = true) : ViewModel() {

    val inputs: ArrayList<InputVM> = ArrayList()
    val outputs: ArrayList<OutputVM> = ArrayList()
    var dragStartX: Double = 0.0
    var dragStartY: Double = 0.0
    var originalX: Double = 0.0
    var originalY: Double = 0.0

    ///////////////////////////////////////////////////////////////////////////////
    //
    //
    ///////////////////////////////////////////////////////////////////////////////
    init {
        // Mark the component as visualized so that inputs/outputs that should not be
        // shown are handled correctly. Specifically, this applies to nodes.
        component.setVisualized()

        var index = 0
        component.booleanInputs.values.map {
            inputs.add(InputVM(index++, getConnectionPointColor(it), it))
        }
        component.doubleInputs.values.map {
            inputs.add(InputVM(index++, getConnectionPointColor(it), it))
        }
        component.stringInputs.values.map {
            inputs.add(InputVM(index++, getConnectionPointColor(it), it))
        }

        index = 0
        component.booleanOutputs.values.map {
            outputs.add(OutputVM(index++, getConnectionPointColor(it), it))
        }
        component.doubleOutputs.values.map {
            outputs.add(OutputVM(index++, getConnectionPointColor(it), it))
        }
        component.stringOutputs.values.map {
            outputs.add(OutputVM(index++, getConnectionPointColor(it), it))
        }
    }

    ///////////////////////////////////////////////////////////////////////////////
    //
    //
    ///////////////////////////////////////////////////////////////////////////////
    private fun getConnectionPointColor(input: IConnectionPoint): Color =
            when (input) {
                is BooleanInput -> Color.BLACK
                is BooleanOutput -> Color.BLACK
                is DoubleInput -> Color.CYAN
                is DoubleOutput -> Color.CYAN
                is StringInput -> Color.DEEPPINK
                is StringOutput -> Color.DEEPPINK
                else -> Color.RED
            }

    val componentType: String = component.javaClass.name
    val x = bind(autocommit = true) { component.observable(IComponent::getX, IComponent::setX) } as DoubleProperty
    val y = bind(autocommit = true) { component.observable(IComponent::getY, IComponent::setY) } as DoubleProperty

    val name = bind { component.observable(IComponent::getName, IComponent::setName) } as StringProperty

    var isSelected: Boolean by property(false)
    fun isSelectedProperty() = getProperty(ComponentVM::isSelected)

    fun startDragComponent(sceneX: Double, sceneY: Double, surface: IDrawingSurfaceView) {
        val xy = surface.sceneToLocal(sceneX, sceneY)
        dragStartX = xy.x
        dragStartY = xy.y
        originalX = x.value
        originalY = y.value
    }

    fun moveFromOriginalPosition(sceneX: Double, sceneY: Double, surface: IDrawingSurfaceView) {
        val xy = surface.sceneToLocal(sceneX, sceneY)
        val xOffset = xy.x - dragStartX
        val yOffset = xy.y - dragStartY
        x.value = originalX + xOffset
        y.value = originalY + yOffset
    }

    fun delete() {
        inputs.map { it.disconnect() }
        outputs.map { it.disconnect() }
    }
}