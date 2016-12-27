package hap.ruleengine.editor.viewmodel

import javafx.collections.ObservableList
import tornadofx.observable
import java.util.*


class NativeCategory constructor(val category: String) {
    val components: ObservableList<ComponentVM> = ArrayList<ComponentVM>().observable()
}