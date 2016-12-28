package hap.ruleengine.editor.viewmodel

import javafx.collections.ObservableList
import tornadofx.observable
import java.util.*


class NativeCategory constructor(val category: String) {
    val components: ObservableList<ComponentVM> = ArrayList<ComponentVM>().observable()

    fun equals(other: NativeCategory): Boolean {
        return category == other.category
    }

    override fun equals(other: Any?): Boolean {
        if( other == null ) {
            return false
        }
        return this.equals( other as NativeCategory)
    }

    override fun hashCode(): Int {
        return category.hashCode()
    }


}