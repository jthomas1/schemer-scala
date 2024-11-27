import org.scalajs.linker.interface.ModuleSplitStyle

ThisBuild / scalaVersion := "3.5.2"

lazy val root = project.in(file(".")).aggregate(client, server)

lazy val shared = crossProject(JSPlatform, JVMPlatform)
  .in(file("."))
  .settings(libraryDependencies ++= Seq("com.lihaoyi" %%% "upickle" % "4.0.2"))

lazy val server = project
  .in(file("./server"))
  .settings(
    /* Normal scala dependencies */
    libraryDependencies ++= Seq("com.lihaoyi" %% "cask" % "0.10.1")
  )
  .dependsOn(shared.jvm)

lazy val client = project
  .in(file("./client"))
  .enablePlugins(ScalaJSPlugin) // Enable the Scala.js plugin in this project
  .settings(
    // Tell Scala.js that this is an application with a main method
    scalaJSUseMainModuleInitializer := true,

    /* Configure Scala.js to emit modules in the optimal way to
     * connect to Vite's incremental reload.
     * - emit ECMAScript modules
     * - emit as many small modules as possible for classes in the "schemer" package
     * - emit as few (large) modules as possible for all other classes
     *   (in particular, for the standard library)
     */
    scalaJSLinkerConfig ~= {
      _.withModuleKind(ModuleKind.ESModule)
        .withModuleSplitStyle(ModuleSplitStyle.SmallModulesFor(List("schemer")))
    },

    /* Depend on the scalajs-dom library.
     * It provides static types for the browser DOM APIs.
     */
    libraryDependencies ++=
      Seq(
        "org.scala-js" %%% "scalajs-dom" % "2.8.0",
        "com.raquo" %%% "laminar" % "16.0.0",
        "com.raquo" %%% "waypoint" % "8.0.1"
      )
  ).dependsOn(shared.js)

// Run the frontend development loop (also run vite: `cd frontend; npm run dev`)
addCommandAlias("cup", ";~client/fastLinkJS")
// Start the backend server, and make sure to stop it afterwards
addCommandAlias("sup", ";server/reStop ;~server/reStart ;server/reStop")
