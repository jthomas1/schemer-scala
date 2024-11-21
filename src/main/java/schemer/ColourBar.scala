package schemer

import com.raquo.laminar.api.L.*
import org.scalajs.dom.html

import scala.scalajs.js
import scala.util.Random

case class RGB(r: Int, g: Int, b: Int) {

  /** brightness = sqrt( .299*R2 + .587*G2 + .114*B2 ); * from here:
    * alienryderflex.com/hsp.html
    */
  val brightness: Double = Math.sqrt(r * r * .299 + g * g * .587 + b * b * .114)

  val hex: String = f"#$r%02x$g%02x$b%02x"

}

def getTextColour(rgb: RGB): String = {
  if rgb.brightness > 125 then
    "black"
  else {
    "white"
  }
}

object RGB {
  def random(): RGB = {
    val rand = new Random
    RGB(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256))
  }
}

class ColourBar() {
  val id: String = js.Dynamic.global.crypto.randomUUID().toString
  def handleColourPick(colour: String, input: html.Input) = {
    js.Dynamic.global.console.log(colour)
    js.Dynamic.global.console.log(input)
    println(colour)
    println(input)
  }

  val colorisOptions = js
    .Dynamic
    .literal("el" -> ".colour-picker", "onChange" -> handleColourPick)
  js.Dynamic.global.Coloris(colorisOptions)

  def render(): HtmlElement = {
    val rgb = RGB.random()
    val colour = Var(rgb.hex)
    val textColour = Var(getTextColour(rgb))
    div(
      cls := "colour-bar",
      backgroundColor <-- colour,
      color <-- textColour,
      div(
        cls := "colour-bar-content",
        h2(child.text <-- colour),
        button(
          cls := "btn",
          onClick --> { _ =>
            colour.set(RGB.random().hex)
            textColour.set(getTextColour(rgb))
          },
          "Random"
        ),
        input(typ := "text", cls := "colour-picker")
      )
    )
  }
}
