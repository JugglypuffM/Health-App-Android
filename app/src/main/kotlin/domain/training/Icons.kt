package domain.training

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

object Icons {
    @Root(name = "icon")
    data class RawIcon(
        @field:Element(name = "title")
        var title: String = "",

        @field:Element(name = "description")
        var description: String = "",

        @field:Element(name = "imageResId")
        var imageResId: String = "",

        @field:Element(name = "activityId")
        var activityClass: String = ""
    )
}