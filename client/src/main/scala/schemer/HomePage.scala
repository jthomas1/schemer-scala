package schemer

import com.raquo.laminar.api.L.*
import org.scalajs.dom

import scala.scalajs.js
import scala.scalajs.js.annotation.*

@js.native
@JSImport("/garden.jpg", JSImport.Default)
val gardenImage: String = js.native

object HomePage {
  val myObj = js.Dynamic.literal("hello" -> "world")
  val myDict = js.Dictionary("hello" -> "world")
  val myArray = js.Array(1, 2, 3)
  val myPromise = js.Promise.resolve("Hello promise")

  dom.console.log(myObj.hello)
  dom.console.log(myDict("hello"))
  dom.console.log(myArray)
  dom.console.log(myPromise)

  // dom.window.alert("Hello world!")

  def render(): HtmlElement = {
    div(cls := "container centred", div(p("Look at the pretty LLM garden"), img(src := gardenImage)))

  }
}
