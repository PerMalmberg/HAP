import hap.ruleengine.editor.view.css.ComponentStyle
import tornadofx.App
import tornadofx.importStylesheet
import view.MainView

class HAPed : App(MainView::class) {
init {
    importStylesheet(ComponentStyle::class)
}
}