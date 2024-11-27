package schemer

import com.raquo.laminar.api.L.{*, given}
import com.raquo.waypoint.*
import org.scalajs.dom
import upickle.default.{ReadWriter, read, write}

sealed trait Page derives ReadWriter
case object ColourPickerPage extends Page
case object MySchemesPage extends Page

@main
def run(): Unit = {
  renderOnDomContentLoaded(dom.document.getElementById("app"), Main.appElement())
}

object Main {
  def appElement(): Element = {
    val colourPickerRoute = Route.static(ColourPickerPage, root / "colour-picker" / endOfSegments)

    val mySchemesRoute = Route.static(MySchemesPage, root / "my-schemes" / endOfSegments)

    val router = {
      new Router[Page](
        routes = List(colourPickerRoute, mySchemesRoute),
        getPageTitle = _.toString, // mock page title (displayed in the browser tab next to favicon)
        serializePage = page => write(page), // serialize page data for storage in History API log
        deserializePage = pageStr => read(pageStr) // deserialize the above
      )(
        popStateEvents = windowEvents(_.onPopState), // this is how Waypoint avoids an explicit dependency on Laminar
        owner = unsafeWindowOwner // this router will live as long as the window
      )
    }

    val splitter = {
      SplitRender[Page, HtmlElement](router.currentPageSignal)
        .collectStatic(ColourPickerPage) {
          PickerPage.render()
        }
        .collectStatic(MySchemesPage) {
          SchemesPage.render()
        }
    }

    div(h1("Hello!"), child <-- splitter.signal)
  }
}
