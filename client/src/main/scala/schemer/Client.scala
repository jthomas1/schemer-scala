package schemer

import com.raquo.laminar.api.L.{*, given}
import com.raquo.waypoint.*
import org.scalajs.dom
import upickle.default.{ReadWriter, read, write}

import scala.util.{Try, Success, Failure}

@main
def run(): Unit = {
  renderOnDomContentLoaded(dom.document.getElementById("app"), Main.appElement())
}

object Main {
  sealed trait Page derives ReadWriter
  private case object Home extends Page
  private case object ColourPickerPage extends Page
  private case object MySchemesPage extends Page

  private val homeRoute = Route.static(Home, root / endOfSegments)
  private val colourPickerRoute = Route.static(ColourPickerPage, root / "colour-picker" / endOfSegments)
  private val mySchemesRoute = Route.static(MySchemesPage, root / "my-schemes" / endOfSegments)

  def navigateTo(page: Page): Binder[HtmlElement] = Binder { el =>

    val isLinkElement = el.ref.isInstanceOf[dom.html.Anchor]

    if (isLinkElement) {
      Try(router.absoluteUrlForPage(page)) match {
        case Success(url) =>
          el.amend(href(url))
        case Failure(err) =>
          dom.console.error(err)
      }
    }

    // If element is a link and user is holding a modifier while clicking:
    //  - Do nothing, browser will open the URL in new tab / window / etc. depending on the modifier key
    // Otherwise:
    //  - Perform regular pushState transition
    (
      onClick
        .filter(ev => !(isLinkElement && (ev.ctrlKey || ev.metaKey || ev.shiftKey || ev.altKey)))
        .preventDefault --> (_ => router.pushState(page))
    ).bind(el)
  }

  private val router = {
    new Router[Page](
      routes = List(homeRoute, colourPickerRoute, mySchemesRoute),
      getPageTitle = _.toString, // mock page title (displayed in the browser tab next to favicon)
      serializePage = page => write(page), // serialize page data for storage in History API log
      deserializePage = pageStr => read(pageStr) // deserialize the above
    )(
      popStateEvents = windowEvents(_.onPopState), // this is how Waypoint avoids an explicit dependency on Laminar
      owner = unsafeWindowOwner // this router will live as long as the window
    )
  }

  private val splitter = {
    SplitRender[Page, HtmlElement](router.currentPageSignal)
      .collectStatic(Home) {
        div("Hello home!")
      }
      .collectStatic(ColourPickerPage) {
        PickerPage.render()
      }
      .collectStatic(MySchemesPage) {
        SchemesPage.render()
      }
  }

  def appElement(): Element = {
    div(
      h1("Hello!"),
      navTag(
        ul(
          li(a(navigateTo(Home), "Home")),
          li(a(navigateTo(ColourPickerPage), "Picker")),
          li(a(navigateTo(MySchemesPage), "My Schemes"))
        )
      ),
      child <-- splitter.signal
    )
  }
}
