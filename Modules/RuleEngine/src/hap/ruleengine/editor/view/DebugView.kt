package hap.ruleengine.editor.view

import hap.ruleengine.editor.viewmodel.event.SelectedComponentsChanged
import hap.ruleengine.editor.viewmodel.parts.InputVM
import hap.ruleengine.editor.viewmodel.parts.OutputVM
import javafx.scene.paint.Color
import javafx.scene.text.FontWeight
import tornadofx.*

class DebugView : View("")
{
	override val root = borderpane {
		subscribe<SelectedComponentsChanged> {
			top {
				text("Debug Data") {
					style {
						fontSize = 14.px
						fontWeight = FontWeight.BOLD
					}
				}
			}
			center {
				vbox {
					this.replaceChildren {
						if (it.selectedComponents.size == 1)
						{
							val single = it.selectedComponents.values.first()

							hbox {
								text("ID")
								textfield(single.component.id.toString()) {
									isEditable = false
								}
							}

							tableview<OutputVM> {
								items = single.outputs.observable()
								column("Name", OutputVM::name).cellFormat {
									graphic = textfield(itemProperty()) {
										isEditable = false
										style {
											focusColor = Color.TRANSPARENT
											backgroundColor += Color.TRANSPARENT
										}
									}
								}
								column("ID", OutputVM::id)
										.cellFormat {
											graphic = textfield(itemProperty(), UUIDConverter()) {
												isEditable = false
												style {
													focusColor = Color.TRANSPARENT
													backgroundColor += Color.TRANSPARENT
												}
											}
										}

							}

							tableview<InputVM> {
								items = single.inputs.observable()
								column("Name", InputVM::name).cellFormat {
									graphic = textfield(itemProperty()) {
										isEditable = false
										style {
											focusColor = Color.TRANSPARENT
											backgroundColor += Color.TRANSPARENT
										}
									}
								}
								column("ID", InputVM::id).cellFormat {
									graphic = textfield(itemProperty(), UUIDConverter()) {
										isEditable = false
										style {
											focusColor = Color.TRANSPARENT
											backgroundColor += Color.TRANSPARENT
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}
}
