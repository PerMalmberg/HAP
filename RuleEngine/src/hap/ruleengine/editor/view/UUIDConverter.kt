package hap.ruleengine.editor.view

import javafx.util.StringConverter
import java.util.*

class UUIDConverter : StringConverter<UUID>()
{
    override fun fromString(string: String?): UUID {
        return UUID.fromString(string)
    }

    override fun toString( o: UUID?): String {
        if( o == null) {
            return ""
        }
        else {
            return o.toString()
        }
    }

}