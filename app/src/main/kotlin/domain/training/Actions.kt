package domain.training

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

object Actions {
    @Root(name = "countdownAction")
    class CountdownAction(
        @field:Element(name = "title")
        var title: String = "",

        @field:Element(name = "timerDescription")
        var timerDescription: String = "",

        @field:Element(name = "duration")
        var duration: Long = 0,
    )

    @Root(name = "stopwatchAction")
    class StopwatchAction(
        @field:Element(name = "title")
        var title: String = "",

        @field:Element(name = "timerDescription")
        var timerDescription: String = ""
    )

    @Root(name = "action")
    data class RawAction(
        @field:Element(name = "title")
        var title: String = "",

        @field:Element(name = "duration")
        var durationMilliseconds: Long = 0,

        @field:Element(name = "imageResId")
        var imageResId: String = ""
    )
}