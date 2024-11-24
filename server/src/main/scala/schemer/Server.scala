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
  def echoList(request: ujson.Arr) = {
    val rgbList = read[List[RGB]](request)

    println(s"Received ${rgbList.size} colours")
    println(rgbList)

    write(rgbList)
  }

  initialize()
}
