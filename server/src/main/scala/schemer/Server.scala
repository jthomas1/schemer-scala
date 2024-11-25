package schemer

import upickle.default.{read, write}

object Server extends cask.MainRoutes {
  @cask.get("/")
  def hello() = {
    RGB.random().hex
  }

  @cask.get("/colour")
  def list(request: cask.Request) = {
    val colours = List(RGB.random(), RGB.random(), RGB.random())
    write(colours)
  }

  @cask.postJson("/colour")
  def echoList(colours: ujson.Value) = {
    val rgbList = read[List[RGB]](colours)

    println(s"Received ${rgbList.size} colours")
    println(rgbList.map(_.hex))

    write(rgbList)
  }

  initialize()
}
