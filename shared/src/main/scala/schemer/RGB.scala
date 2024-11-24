package schemer

import scala.util.Random
import upickle.default.ReadWriter

case class RGB(r: Int, g: Int, b: Int) derives ReadWriter {

  /** brightness = sqrt( .299*R2 + .587*G2 + .114*B2 ); * from here:
    * alienryderflex.com/hsp.html
    */
  val brightness: Double = Math.sqrt(r * r * .299 + g * g * .587 + b * b * .114)

  val hex: String = f"#$r%02x$g%02x$b%02x"

}

object RGB {
  def getTextColour(rgb: RGB): String = {
    if rgb.brightness > 125 then
      "black"
    else {
      "white"
    }
  }

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
