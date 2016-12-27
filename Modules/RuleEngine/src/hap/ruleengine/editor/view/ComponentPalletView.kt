package hap.ruleengine.editor.view

import hap.ruleengine.editor.view.parts.ComponentView
import hap.ruleengine.editor.viewmodel.ComponentPallet
import hap.ruleengine.editor.viewmodel.NativeCategory
import javafx.scene.control.TitledPane
import tornadofx.*


class ComponentPalletView : Fragment() {

    val pallet: ComponentPallet by inject()

    override val root = listview<NativeCategory>() {
        cellCache {
            TitledPane(it.category,
                    listview(it.components) { // TODO: Use titledpane-builder when TornadoFx is updated to >=1.5.10 https://kotlinlang.slack.com/files/edvin/F3K326W6P/cleaner__but_a_bit_more_advanced__and_requires_snapshot_.kt
                        cellCache {
                            stackpane {
                                this += ComponentView(0.0, 0.0, it)
                            }
                        }
                    }).apply {
                pallet.categoryExpanded(it.category).bind(this.expandedProperty())
            }
        }
    }

    init {
        with(root)
        {
            items = pallet.categories.observable()
        }
    }


}
