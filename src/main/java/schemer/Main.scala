package schemer

import scala.scalajs.js
import scala.scalajs.js.annotation.*

import org.scalajs.dom
import com.raquo.laminar.api.L.{*, given}

@main
def run(): Unit = {
  renderOnDomContentLoaded(
    dom.document.getElementById("app"),
    Main.appElement()
  )
}

object Main {
  private def genList(n: Int): List[ColourBar] = {
    List.tabulate(n)(_ => ColourBar())
  }

  def appElement(): Element = {
    val coloursVar = Var[List[ColourBar]](List(ColourBar()))
    div(
      cls := "container",
      children <--
        coloursVar.signal.split(_.id)((_, input, _) => input.render()),
      onKeyDown --> { event =>
        js.Dynamic.global.console.log(event.keyCode)
        if event.keyCode == 32 then
          coloursVar.set(genList(coloursVar.now().size))
      }
    )
  }
}
