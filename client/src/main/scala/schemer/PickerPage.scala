package schemer

import com.raquo.laminar.api.L.*
import org.scalajs.dom
import org.scalajs.dom.{HttpMethod, RequestInit, fetch}
import upickle.default.{ReadWriter, write, read}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js
import scala.scalajs.js.Thenable.Implicits.*

object PickerPage {
  private def genList(n: Int): List[ColourBar] = {
    List.tabulate(n)(_ => ColourBar())
  }

  private val coloursVar = Var[List[ColourBar]](List(ColourBar()))

  private def renderColourBar(colourId: String, vm: ColourBar, signal: Signal[ColourBar]): HtmlElement = {
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
          disabled <-- vm.lockVar,
          "Random"
        ),
        div(
          input(
            typ := "text",
            cls := s"coloris-picker-$colourId",
            value <-- vm.bgHexVar,
            onClick --> { _ =>
              vm.openColourPicker()
            },
            disabled <-- vm.lockVar
          )
        ),
        div(
          label(
            "Lock",
            input(
              typ := "checkbox",
              value <-- vm.lockVar.signal.map(_.toString),
              onClick.mapToChecked --> vm.lockVar.writer
            )
          )
        ),
        button(
          cls := "btn",
          onClick --> { _ =>
            coloursVar.update(_.filter(_.id != vm.id))
          },
          disabled <-- vm.lockVar,
          "Remove"
        )
      )
    )
  }

  private case class PostRequestPayload(colours: List[RGB]) derives ReadWriter

  private def postColours(colours: List[RGB]): js.Promise[dom.Response] = {
    val url = "http://localhost:8080/colour"

    val myHeaders = new dom.Headers()
    myHeaders.set("Content-Type", "application/json")
    fetch(
      url,
      new RequestInit {
        method = HttpMethod.POST
        body = write(PostRequestPayload(coloursVar.now().map(_.rgb)))
        headers = myHeaders
      }
    )
  }

  def render(): HtmlElement = {
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
            val updated = coloursVar
              .now()
              .map(colour => {
                if (!colour.lockVar.now()) {
                  colour.updateRandom()
                }
                colour
              })
            coloursVar.set(updated)
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
