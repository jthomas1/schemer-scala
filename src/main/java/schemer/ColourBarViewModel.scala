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

  def fromHex(hex: String): RGB = {
    val cleanHex = hex.stripPrefix("#")
    if (cleanHex.length != 6) {
      throw new IllegalArgumentException(s"Invalid hex color code: $hex")
    }

    val r = Integer.parseInt(cleanHex.substring(0, 2), 16)
    val g = Integer.parseInt(cleanHex.substring(2, 4), 16)
    val b = Integer.parseInt(cleanHex.substring(4, 6), 16)

    RGB(r, g, b)
  }
}

class ColourBarViewModel(val rgb: RGB = RGB.random()) {
  val id: String = js.Dynamic.global.crypto.randomUUID().toString
  val bgHexVar: Var[String] = Var(rgb.hex)
  val textHexVar: Var[String] = Var(getTextColour(rgb))

  private def handleColourPick(colour: String, input: html.Input): Unit = {
    update(RGB.fromHex(colour))
  }

  private def onInput(picker: js.Object) = {
    js.Dynamic.global.console.log(picker)
  }

  def update(rgb: RGB) = {
    bgHexVar.set(rgb.hex)
    textHexVar.set(getTextColour(rgb))
  }

  def updateRandom(): Unit = update(RGB.random())

  def openColourPicker(): Unit = {
    val colorisOptions = js
      .Dynamic
      .literal(
        "el" -> s".coloris-picker-$id",
        "onChange" -> handleColourPick,
        "wrap" -> false
      )
    js.Dynamic.global.Coloris(colorisOptions)
  }
}
