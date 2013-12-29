import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "community-quick-start"
  val appVersion      = "1.0-SNAPSHOT"


  val dbDependencies = Seq(
    jdbc,
    "com.typesafe.slick" %% "slick" % "2.0.0-RC1"
    //"org.slf4j" % "slf4j-nop" % "1.6.4",
    //"com.h2database" % "h2" % "1.3.166"
  )

  val appDependencies = Seq(
    cache, // play cache external module
    "mysql" % "mysql-connector-java" % "5.1.27",
    "securesocial" %% "securesocial" % "2.1.2"
  ) ++ dbDependencies

  val main = play.Project(appName, appVersion, appDependencies).settings(

    resolvers += Resolver.url("sbt-plugin-releases", new URL("http://repo.scala-sbt.org/scalasbt/sbt-plugin-releases/"))(Resolver.ivyStylePatterns),

    // disable less compilation
    lessEntryPoints <<= (sourceDirectory in Compile)(base => (
      (base / "assets" / "stylesheets" * "none.less") 
    )),

    // skip api docs generation
    sources in doc in Compile := List(),

    slick <<= slickCodeGenTask, // register manual sbt command

    sourceGenerators in Compile <+= slick,

    //slick in Compile <<= slickCodeGenTask,

    // remove compiler warnings as needed
    //scalacOptions ++= Seq("-feature")
    routesImport ++= Seq("language.reflectiveCalls") // remove warnings with routes imports
  ).dependsOn(dbGen)



  lazy val dbGen = play.Project("dbgen", appVersion, dbDependencies, path = file("dbgen")).settings(
  )

  // code generation task
  lazy val slick = TaskKey[Seq[File]]("gen-tables")
  lazy val slickCodeGenTask = (sourceManaged, dependencyClasspath in Compile, runner in Compile, streams) map { (dir, cp, r, s) =>
    val outputDir = (dir.getParentFile.getParentFile.getParentFile / "app").getPath
    toError(r.run("plugins.DbCodeGenerator", cp.files, Array(outputDir), s.log))
    Seq.empty[File] //(file(outputDir + "/demo/Tables.scala"))
  }

}
