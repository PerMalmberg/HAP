import tornadofx.App
import tornadofx.reloadStylesheetsOnFocus
import tornadofx.reloadViewsOnFocus
import view.MainView

class HAPed : App(MainView::class) {
    init {
        reloadStylesheetsOnFocus()
        reloadViewsOnFocus()
    }
}