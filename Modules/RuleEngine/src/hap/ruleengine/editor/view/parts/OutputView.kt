package hap.ruleengine.editor.view.parts

import hap.ruleengine.editor.view.css.ComponentStyle
import hap.ruleengine.editor.viewmodel.event.MouseDragDropReleased
import hap.ruleengine.editor.viewmodel.event.UpdateDragWire
import hap.ruleengine.editor.viewmodel.parts.OutputVM
import javafx.scene.input.TransferMode
import tornadofx.*
import java.util.*

class OutputView : ConnectionPointView()
{
	val vm: OutputVM by param()

	override val root =
			stackpane {
				rectangle(0.0, 0.0, connectionPointSize, connectionPointSize) {
					fill = vm.color
					addClass(ComponentStyle.output)

					setOnDragDetected {
						startConnectWire(vm.connectionPoint, getSceneRelativeCenter())
						startFullDrag()
					}

					setOnMouseDragged {
						fire(UpdateDragWire(it.sceneX, it.sceneY))
					}

					setOnMouseDragExited {
						enteredConnectionPoint(null)
					}

					setOnMouseDragEntered {
						enteredConnectionPoint(vm.connectionPoint)
					}

					setOnMouseDragReleased {
						fire(MouseDragDropReleased(it.sceneX, it.sceneY))
					}

					setOnDragOver {
						it.acceptTransferModes(TransferMode.COPY)
					}

					tooltip {
						this.textProperty().bind(vm.currentValueProperty())
					}
				}.apply {
					myConnectionPoint = this
				}
			}

	fun connect(targetInput: InputView): WireView
	{
		return WireView(this, targetInput)
	}

	override fun disconnect(wire: WireView)
	{
		myWire.remove(wire)
	}

	val id: UUID = vm.id.value
	val name: String = vm.name.value
	val color = vm.color
}
