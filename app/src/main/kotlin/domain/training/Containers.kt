package domain.training

import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

object Containers {
    @Root(name = "iconList")
    data class RawIconList(
        @field:ElementList(inline = true, entry = "icon")
        var items: MutableList<Icons.RawIcon> = mutableListOf()
    )

    @Root(name = "container")
    class Container(
        @field:Element(name = "countdownAction")
        var countdownAction: Actions.CountdownAction = Actions.CountdownAction(),

        @field:Element(name = "stopwatchAction")
        var stopwatchAction: Actions.StopwatchAction = Actions.StopwatchAction(),
    )

    @Root(name = "actionList")
    data class RawActionList(
        @field:ElementList(inline = true, entry = "action")
        var items: MutableList<Actions.RawAction> = mutableListOf()
    )
}