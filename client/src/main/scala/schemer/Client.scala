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
  def appElement(): Element = {
    div(cls := "container", h1("Hello client!"))
  }
}

//object Main {
//  private def genList(n: Int): List[ColourBarViewModel] = {
//    List.tabulate(n)(_ => ColourBarViewModel())
//  }
//
//  private val coloursVar = Var[List[ColourBarViewModel]](
//    List(ColourBarViewModel())
//  )
//
//  private def renderColourBar(
//                               colourId: String,
//                               vm: ColourBarViewModel,
//                               signal: Signal[ColourBarViewModel]
//                             ): HtmlElement = {
//    div(
//      cls := "colour-bar",
//      backgroundColor <-- vm.bgHexVar,
//      color <-- vm.textHexVar,
//      div(
//        cls := "colour-bar-content",
//        h2(child.text <-- vm.bgHexVar),
//        button(
//          cls := "btn",
//          onClick --> { _ =>
//            vm.updateRandom()
//          },
//          "Random"
//        ),
//        div(
//          input(
//            typ := "text",
//            cls := s"coloris-picker-$colourId",
//            value <-- vm.bgHexVar,
//            onClick --> { _ =>
//              vm.openColourPicker()
//            }
//          )
//        ),
//        button(
//          cls := "btn",
//          onClick --> { _ =>
//            coloursVar.update(_.filter(_.id != vm.id))
//          },
//          "Remove"
//        )
//      )
//    )
//  }
//
//  def appElement(): Element = {
//
//    div(
//      cls := "container",
//      headerTag(
//        cls := "header",
//        button(
//          cls := "btn",
//          onClick --> { _ =>
//            coloursVar.set(coloursVar.now() :+ ColourBarViewModel())
//          },
//          "Add Colour"
//        ),
//        button(
//          cls := "btn",
//          onClick --> { _ =>
//            coloursVar.set(coloursVar.now().dropRight(1))
//          },
//          "Remove Colour"
//        ),
//        button(
//          cls := "btn",
//          onClick --> { _ =>
//            coloursVar.set(genList(coloursVar.now().size))
//          },
//          "Randomise"
//        )
//      ),
//      div(
//        cls := "colour-bar-container",
//        children <-- coloursVar.signal.split(_.id)(renderColourBar),
//        onKeyPress --> { event =>
//          println(event)
//          if event.keyCode == 32 then
//            coloursVar.set(genList(coloursVar.now().size))
//        }
//      )
//    )
//  }
//}
