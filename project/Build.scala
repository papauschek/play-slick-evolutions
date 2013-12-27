import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "community-quick-start"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    jdbc,
    cache, // play cache external module
    "com.typesafe.slick" % "slick_2.10" % "1.0.1", // database mapper
    "mysql" % "mysql-connector-java" % "5.1.22"
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(

    // disable less compilation
    lessEntryPoints <<= (sourceDirectory in Compile)(base => (
      (base / "assets" / "stylesheets" * "none.less") 
    )),

    // skip api docs generation
    sources in doc in Compile := List(), 

    // remove compiler warnings as needed
    //scalacOptions ++= Seq("-feature")
    routesImport ++= Seq("language.reflectiveCalls") // remove warnings with routes imports
  )

}
