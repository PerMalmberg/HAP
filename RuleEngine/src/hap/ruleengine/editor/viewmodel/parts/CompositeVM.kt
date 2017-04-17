package hap.ruleengine.editor.viewmodel.parts

import hap.ruleengine.parts.IComponent

class CompositeVM constructor(val sourceFile: String, component: IComponent, isSelectable: Boolean = true) : ComponentVM(component, isSelectable)

