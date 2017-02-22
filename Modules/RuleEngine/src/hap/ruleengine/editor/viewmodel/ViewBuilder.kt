package hap.ruleengine.editor.viewmodel

import hap.ruleengine.component.IPropertyDisplay
import hap.ruleengine.editor.view.css.PropertyEditorStyle
import hap.ruleengine.parts.IComponent
import hap.ruleengine.parts.property.BooleanProperty
import hap.ruleengine.parts.property.DoubleProperty
import hap.ruleengine.parts.property.IntProperty
import hap.ruleengine.parts.property.StringProperty
import javafx.scene.layout.StackPane
import javafx.util.converter.NumberStringConverter
import tornadofx.*
import java.util.*

/**
 * Created by Per Malmberg on 2017-01-20.
 */
class ViewBuilder(root: StackPane, val component: IComponent) : IPropertyDisplay
{
	private val myProps = ArrayList<ViewModel>()

	override fun show(property: DoubleProperty)
	{
		myProps.add(property)

		with(myForm) {
			field(property.header) {
				hbox(spacing = 4) {
					// https://docs.oracle.com/javase/8/docs/api/index.html?java/text/DecimalFormat.html
					textfield(property.value, NumberStringConverter("#.##########")) {
						tooltip(property.informationMessage)
						validator {
							property.validate(it)
						}
					}

					button {
						addClass(PropertyEditorStyle.revertButton)
						setOnAction {
							property.rollback()
						}
						disableWhen { property.dirty.not() }
					}
				}
			}
		}
	}

	var myForm: Fieldset by singleAssign()

	override fun show(property: IntProperty)
	{
		myProps.add(property)

		with(myForm) {
			field(property.header) {
				hbox(spacing = 4) {
					// https://docs.oracle.com/javase/8/docs/api/index.html?java/text/DecimalFormat.html
					textfield(property.value, NumberStringConverter("#")) {
						tooltip(property.informationMessage)
						validator {
							property.validate(it)
						}
					}

					button {
						addClass(PropertyEditorStyle.revertButton)
						setOnAction {
							property.rollback()
						}
						disableWhen { property.dirty.not() }
					}
				}
			}
		}
	}

	override fun show(property: StringProperty)
	{
		myProps.add(property)

		with(myForm) {
			field(property.header) {
				hbox(spacing = 4) {
					textfield(property.value) {
						tooltip(property.informationMessage)
						validator {
							property.validate(it)
						}
					}

					button {
						addClass(PropertyEditorStyle.revertButton)
						setOnAction {
							property.rollback()
						}
						disableWhen { property.dirty.not() }
					}
				}
			}
		}
	}

	override fun show(property: BooleanProperty)
	{
		myProps.add(property)

		with(myForm) {
			field(property.header) {
				hbox(spacing = 4) {
					checkbox("", property.value) {
						tooltip(property.informationMessage)
					}

					button {
						addClass(PropertyEditorStyle.revertButton)
						setOnAction {
							property.rollback()
						}
						disableWhen { property.dirty.not() }
					}
				}
			}
		}
	}

	init
	{
		with(root) {
			vbox {
				form {
					fieldset("Component Properties").apply {
						myForm = this
					}
				}
				hbox(spacing = 4) {
					button {
						addClass(PropertyEditorStyle.applyButton)
						tooltip("Apply all")
						setOnAction {
							if( !myProps.any { !it.commit() } ) {
								component.propertiesApplied()
							}
						}
					}
					button {
						addClass(PropertyEditorStyle.revertButton)
						tooltip("Revert all")
						setOnAction {
							myProps.forEach { it.rollback() }
						}
					}
				}
			}
		}
	}
}