package schemer

import upickle.default.write

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

  @cask.post("/colour")
  def create(request: cask.Request) = {
    request.text()
  }

  initialize()
}
