package schemer

import com.raquo.laminar.api.L.{*, given}
import org.scalajs.dom.*

import scala.scalajs.js

class ColourBar(val rgb: RGB = RGB.random()) {
  val id: String = js.Dynamic.global.crypto.randomUUID().toString
  val bgHexVar: Var[String] = Var(rgb.hex)
  val textHexVar: Var[String] = Var(RGB.getTextColour(rgb))

  private def handleColourPick(colour: String, input: html.Input): Unit = {
    update(RGB.fromHex(colour))
  }

  private def onInput(picker: js.Object) = {
    js.Dynamic.global.console.log(picker)
  }

  def update(rgb: RGB) = {
    bgHexVar.set(rgb.hex)
    textHexVar.set(RGB.getTextColour(rgb))
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
