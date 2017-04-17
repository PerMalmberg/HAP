package hap.ruleengine.editor.viewmodel.parts

import hap.ruleengine.parts.IConnectionPoint
import hap.ruleengine.parts.IValueChangeReceiver
import javafx.scene.paint.Color
import tornadofx.ViewModel
import tornadofx.getProperty
import tornadofx.observable


class OutputVM(val index: Int, val color: Color, val connectionPoint: IConnectionPoint) : ViewModel(), IValueChangeReceiver
{
	val name = bind(autocommit = true) { connectionPoint.observable(IConnectionPoint::getName, IConnectionPoint::setName) }
	val id = bind(autocommit = true) { connectionPoint.observable(IConnectionPoint::getId, IConnectionPoint::setId) }

	fun disconnect()
	{
		connectionPoint.disconnectAll()
		connectionPoint.unsubscribeToValueChanges(this)
	}

	var currentValue: String by tornadofx.property("")
	fun currentValueProperty() = getProperty(OutputVM::currentValue)

	init
	{
		connectionPoint.subscribeToValueChanges(this)
	}

	override fun newValue(value: String?)
	{
		currentValueProperty().value = value ?: ""
	}
}