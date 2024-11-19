package schemer

import com.raquo.laminar.api.L.*

import java.util.UUID
import scala.util.Random

case class RGB(r: Int, g: Int, b: Int) {

  /** brightness = sqrt( .299*R2 + .587*G2 + .114*B2 ); * from here:
    * alienryderflex.com/hsp.html
    */
  val brightness: Double = {
    Math.sqrt(r * r * .299 + g * g * .587 + b * b * .114)
  }

  def toHex: String = {
    f"#$r%02x$g%02x$b%02x"
  }
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
  val id: String = new Random().nextInt(1000000).toString

  def render(): HtmlElement = {
    val rgb = RGB.random()
    val colour = Var(rgb.toHex)
    val textColour = Var(getTextColour(rgb))
    div(
      cls := "colour-bar",
      backgroundColor <-- colour,
      color <-- textColour,
      h1(child.text <-- colour),
      onClick --> { event =>
        val newColour = RGB.random()
        colour.set(newColour.toHex)
        textColour.set(getTextColour(newColour))
      }
    )
  }
}
