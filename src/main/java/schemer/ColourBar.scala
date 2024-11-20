package schemer

import com.raquo.laminar.api.L.*

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
  val dataColoris: HtmlAttr[String] = dataAttr("coloris")

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
        input(typ := "text", dataColoris := "")
      )
    )
  }
}
