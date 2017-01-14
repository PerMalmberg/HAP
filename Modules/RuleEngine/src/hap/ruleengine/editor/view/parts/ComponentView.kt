package hap.ruleengine.editor.view.parts

import hap.ruleengine.editor.view.css.ComponentStyle
import hap.ruleengine.editor.viewmodel.event.ComponentDragged
import hap.ruleengine.editor.viewmodel.event.DeleteComponent
import hap.ruleengine.editor.viewmodel.event.MouseReleased
import hap.ruleengine.editor.viewmodel.event.SelectComponent
import hap.ruleengine.editor.viewmodel.parts.ComponentVM
import hap.ruleengine.editor.viewmodel.parts.InputVM
import hap.ruleengine.editor.viewmodel.parts.OutputVM
import javafx.geometry.Pos
import javafx.scene.input.MouseButton
import javafx.scene.layout.GridPane
import javafx.scene.paint.Color
import tornadofx.*
import java.util.*


class ComponentView : Fragment() {

    val vm: ComponentVM by param()
    private val myOutputs: ArrayList<OutputView> = ArrayList()
    private val myInputs: ArrayList<InputView> = ArrayList()
    private val inputColumn = 0
    private val inputTextColumn = 1
    private val centerColumn = 2
    private val outputColumn = 3
    private val outputTextColumn = 4

    override val root =
            borderpane {
                layoutXProperty().bindBidirectional(vm.x)
                layoutYProperty().bindBidirectional(vm.y)

                layoutXProperty().addListener { xValue, old, newValue ->
                    updateWires()
                }

                layoutYProperty().addListener { xValue, old, newValue ->
                    updateWires()
                }

                center {
                    gridpane {
                        style {
                            alignment = Pos.BASELINE_CENTER
                        }

                        vgap = connectionPointSize / 2

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
                                        columnRowIndex(inputColumn, row)
                                    }
                                }
                                stackpane {
                                    val inputView = find<InputView>("vm" to inVM)
                                    myInputs.add(inputView)
                                    this += inputView
                                    gridpaneConstraints {
                                        columnRowIndex(inputTextColumn, row)
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
                                        columnRowIndex(outputColumn, row)
                                    }
                                }

                                stackpane {
                                    this += find<OutputTextView>("vm" to outVM)
                                    gridpaneConstraints {
                                        columnRowIndex(outputTextColumn, row)
                                    }
                                }
                            }

                            ++row
                        }

                        // Center area
                        rectangle {
                            fill = Color.GRAY
                            gridpaneConstraints {
                                columnRowIndex(centerColumn, 0)
                                rowSpan = row
                            }


                            widthProperty().bind(heightProperty())
                            heightProperty().bind((this.parent as GridPane).heightProperty())
                            addClass(ComponentStyle.componentCenter)

                            if (vm.isSelectable) {
                                setOnMouseClicked {
                                    if (it.button == MouseButton.PRIMARY) {
                                        fire(SelectComponent(vm, it.isControlDown))
                                    } else if (it.button == MouseButton.SECONDARY) {
                                        fire(DeleteComponent(this@ComponentView))
                                    }
                                }

                                setOnMouseReleased {
                                    fire(MouseReleased)
                                }

                                setOnMouseDragged {
                                    if (it.button == MouseButton.PRIMARY) {
                                        // Select component if not already selected
                                        if (!vm.isSelected) {
                                            fire(SelectComponent(vm, it.isControlDown))
                                        }
                                        fire(ComponentDragged(it, vm))
                                    }
                                }
                            }

                            vm.isSelectedProperty().addListener { observableValue, oldValue, newValue ->
                                if (newValue) {
                                    addClass(ComponentStyle.componentSelected)
                                } else {
                                    removeClass(ComponentStyle.componentSelected)
                                }
                            }
                        }
                    }
                }
                bottom {
                    stackpane {
                        label {
                            bind(vm.name)
                        }.apply {
                            alignment = Pos.BASELINE_CENTER
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

    fun delete() {
        removeFromParent()
        myInputs.map { it.disconnectAllWires() }
        myOutputs.map { it.disconnectAllWires() }
        vm.delete()
    }
}
