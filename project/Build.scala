import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "community-quick-start"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    jdbc,
    cache, // play cache external module
    //"com.typesafe.slick" % "slick_2.10" % "1.0.1", // database mapper
    "mysql" % "mysql-connector-java" % "5.1.27",
    "securesocial" %% "securesocial" % "2.1.2",
    "com.typesafe.slick" %% "slick" % "2.0.0-RC1",
    "org.slf4j" % "slf4j-nop" % "1.6.4",
    "com.h2database" % "h2" % "1.3.166"
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(

    resolvers += Resolver.url("sbt-plugin-releases", new URL("http://repo.scala-sbt.org/scalasbt/sbt-plugin-releases/"))(Resolver.ivyStylePatterns),

    // disable less compilation
    lessEntryPoints <<= (sourceDirectory in Compile)(base => (
      (base / "assets" / "stylesheets" * "none.less") 
    )),

    // skip api docs generation
    sources in doc in Compile := List(),

    slick <<= slickCodeGenTask, // register manual sbt command

    // remove compiler warnings as needed
    //scalacOptions ++= Seq("-feature")
    routesImport ++= Seq("language.reflectiveCalls") // remove warnings with routes imports
  ).dependsOn(codegenProject, dbGen)

  lazy val codegenProject = Project(
    id="codegen",
    base=file("codegen"),
    settings = Project.defaultSettings ++ Seq(libraryDependencies ++= appDependencies)
  )

  lazy val dbGen = play.Project("dbgen", appVersion, appDependencies, path = file("dbgen")).settings(
    resolvers += Resolver.url("sbt-plugin-releases", new URL("http://repo.scala-sbt.org/scalasbt/sbt-plugin-releases/"))(Resolver.ivyStylePatterns)
  )

  // code generation task
  lazy val slick = TaskKey[Seq[File]]("gen-tables")
  lazy val slickCodeGenTask = (sourceManaged, dependencyClasspath in Compile, runner in Compile, streams) map { (dir, cp, r, s) =>
    val outputDir = (dir.getParentFile.getParentFile.getParentFile / "app").getPath // place generated files in sbt's managed sources folder
    toError(r.run("plugins.DbCodeGenerator", cp.files, Array(outputDir), s.log))
    Seq.empty[File] //(file(outputDir + "/demo/Tables.scala"))
  }

}
