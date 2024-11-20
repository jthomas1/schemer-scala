package schemer

import com.raquo.laminar.api.L.{*, given}
import org.scalajs.dom

import scala.scalajs.js

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
      headerTag(
        cls := "header",
        button(
          cls := "btn",
          onClick --> { _ =>
            coloursVar.set(coloursVar.now() :+ ColourBar())
          },
          "Add Colour"
        ),
        button(
          cls := "btn",
          onClick --> { _ =>
            coloursVar.set(coloursVar.now().dropRight(1))
          },
          "Remove Colour"
        ),
        button(
          cls := "btn",
          onClick --> { _ =>
            coloursVar.set(genList(coloursVar.now().size))
          },
          "Randomise"
        )
      ),
      div(
        cls := "colour-bar-container",
        children <--
          coloursVar.signal.split(_.id)((_, input, _) => input.render()),
        onKeyPress --> { event =>
          println(event)
          if event.keyCode == 32 then
            coloursVar.set(genList(coloursVar.now().size))
        }
      )
    )
  }
}
