package schemer

import com.raquo.laminar.api.L.{*, given}
import org.scalajs.dom
import org.scalajs.dom.{HttpMethod, RequestInit, fetch}
import upickle.default.write
import scala.scalajs.js.Thenable.Implicits.*
import scala.concurrent.ExecutionContext.Implicits.global

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

  private val coloursVar = Var[List[ColourBar]](List(ColourBar()))

  private def renderColourBar(
      colourId: String,
      vm: ColourBar,
      signal: Signal[ColourBar]
  ): HtmlElement = {
    div(
      cls := "colour-bar",
      backgroundColor <-- vm.bgHexVar,
      color <-- vm.textHexVar,
      div(
        cls := "colour-bar-content",
        h2(child.text <-- vm.bgHexVar),
        button(
          cls := "btn",
          onClick --> { _ =>
            vm.updateRandom()
          },
          "Random"
        ),
        div(
          input(
            typ := "text",
            cls := s"coloris-picker-$colourId",
            value <-- vm.bgHexVar,
            onClick --> { _ =>
              vm.openColourPicker()
            }
          )
        ),
        button(
          cls := "btn",
          onClick --> { _ =>
            coloursVar.update(_.filter(_.id != vm.id))
          },
          "Remove"
        )
      )
    )
  }

  private def postColours(colours: List[RGB]): js.Promise[dom.Response] = {
    val url = "http://localhost:8080/colour"

    val myHeaders = new dom.Headers()
    myHeaders.set("Content-Type", "application/json")
    fetch(
      url,
      new RequestInit {
        method = HttpMethod.POST
        body = write(coloursVar.now().map(_.rgb))
        headers = myHeaders
      }
    )
  }

  def appElement(): Element = {

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
        ),
        button(
          cls := "btn",
          onClick --> { _ =>
            for {
              response <- postColours(coloursVar.now().map(_.rgb))
              json <- response.json()
              _ = js.Dynamic.global.console.log(json)
            } yield ()
          },
          "Send to server"
        )
      ),
      div(
        cls := "colour-bar-container",
        children <-- coloursVar.signal.split(_.id)(renderColourBar),
        onKeyPress --> { event =>
          println(event)
          if event.keyCode == 32 then
            coloursVar.set(genList(coloursVar.now().size))
        }
      )
    )
  }
}
