package hap.ruleengine.editor.view.parts

import hap.ruleengine.editor.view.css.ComponentStyle
import hap.ruleengine.editor.viewmodel.event.ComponentDragged
import hap.ruleengine.editor.viewmodel.event.MouseReleased
import hap.ruleengine.editor.viewmodel.event.SelectComponent
import hap.ruleengine.editor.viewmodel.parts.ComponentVM
import hap.ruleengine.editor.viewmodel.parts.InputVM
import hap.ruleengine.editor.viewmodel.parts.OutputVM
import javafx.beans.binding.Bindings
import javafx.scene.input.MouseButton
import javafx.scene.layout.GridPane
import javafx.scene.paint.Color
import tornadofx.*
import java.util.*


class ComponentView : Fragment() {

    val vm: ComponentVM by param()
    private val myOutputs: ArrayList<OutputView> = ArrayList()
    private val myInputs: ArrayList<InputView> = ArrayList()

    override val root =
            gridpane {
                layoutXProperty().bind(vm.x)
                layoutYProperty().bind(vm.y)

                layoutXProperty().addListener { xValue, old, newValue ->
                    updateWires()
                }

                layoutYProperty().addListener { xValue, old, newValue ->
                    updateWires()
                }

                val inputs = Stack<InputVM>()
                inputs.addAll(vm.inputs.filter { it.connectionPoint.isVisible }.reversed())
                val outputs = Stack<OutputVM>()
                outputs.addAll(vm.outputs.filter { it.connectionPoint.isVisible }.reversed())

                var row = 0
                while (!inputs.isEmpty() || !outputs.isEmpty()) {
                    if (!inputs.isEmpty()) {
                        val inVM = inputs.pop()
                        stackpane {
                            this += find<InputTextView>("vm" to inVM)
                            gridpaneConstraints {
                                columnRowIndex(0, row)
                            }
                        }
                        stackpane {
                            val inputView = find<InputView>("vm" to inVM)
                            myInputs.add(inputView)
                            this += inputView
                            gridpaneConstraints {
                                columnRowIndex(1, row)
                            }
                        }
                    }

                    if (!outputs.isEmpty()) {
                        val outVM = outputs.pop()
                        stackpane {
                            val outputView = find<OutputView>("vm" to outVM)
                            myOutputs.add(outputView)
                            this += outputView
                            gridpaneConstraints {
                                columnRowIndex(3, row)
                            }
                        }

                        stackpane {
                            this += find<OutputTextView>("vm" to outVM)
                            gridpaneConstraints {
                                columnRowIndex(4, row)
                            }
                        }
                    }

                    ++row
                }

                rectangle {
                    fill = Color.GRAY
                    gridpaneConstraints {
                        columnRowIndex(2, 0)
                        rowSpan = row
                    }

                    width = 10.0
                    heightProperty().bind((this.parent as GridPane).heightProperty())
                    addClass(ComponentStyle.componentCenter)

                    if( vm.isSelectable ) {
                        setOnMouseClicked {
                            if( it.button == MouseButton.PRIMARY ) {
                                fire(SelectComponent(vm, it.isControlDown))
                            }
                        }

                        setOnMouseReleased {
                            fire(MouseReleased)
                        }

                        setOnMouseDragged {
                            if( it.button == MouseButton.PRIMARY ) {
                                // Select component if not already selected
                                if( !vm.isSelected) {
                                    fire(SelectComponent(vm, it.isControlDown))
                                }
                                fire(ComponentDragged(it, vm))
                            }
                        }
                    }

                    vm.isSelectedProperty().addListener { observableValue, oldValue, newValue ->
                        if( newValue ) {
                            addClass(ComponentStyle.componentSelected)
                        } else {
                            removeClass(ComponentStyle.componentSelected)
                        }
                    }
                }
            }

    private fun updateWires() {
        myOutputs.forEach { it.updateWires() }
        myInputs.forEach { it.updateWires() }
    }

    fun getInputView(nameOfInput: String): InputView? {
        return myInputs.singleOrNull { nameOfInput == it.name }
    }

    fun getOutputView(nameOfOutput: String): OutputView? {
        return myOutputs.singleOrNull { nameOfOutput == it.name }
    }
}
