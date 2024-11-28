package schemer

import upickle.default.{read, write}

object Server extends cask.MainRoutes {
  @cask.get("/")
  def hello(): String = {
    "Hello, world!"
  }

  @cask.get("/colour")
  def listColours(request: cask.Request): String = {
    val colours = List(RGB.random(), RGB.random(), RGB.random())
    write(colours)
  }

  @cask.postJson("/colour")
  def echoList(colours: ujson.Value): String = {
    val rgbList = read[List[RGB]](colours)

    println(s"Received ${rgbList.size} colours")
    println(rgbList.map(_.hex))

    write(rgbList)
  }

  initialize()
}
