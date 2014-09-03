import sbt._
import Keys._
import play.Play.autoImport._

object ApplicationBuild extends Build {

  val appName         = "play-slick-evolutions"
  val appVersion      = "1.0-SNAPSHOT"
  val commonScalaVersion = "2.11.2" // 2.11.2

  val appDependencies = Seq(
    jdbc,
    cache, // play cache external module
    "com.typesafe.slick" %% "slick" % "2.1.0",
    "mysql" % "mysql-connector-java" % "5.1.27"
  )

  // main Play project
  val main = Project(appName, file(".")).enablePlugins(play.PlayScala).settings(
    scalaVersion := commonScalaVersion,
    version := appVersion,
    libraryDependencies ++= appDependencies,
    slickCodeGen <<= slickCodeGenTask, // register manual sbt command
    sourceGenerators in Compile <+= slickCodeGenTask // generate slick code
  ).dependsOn(dbGen)

  // Slick code generator module
  lazy val dbGen = Project("dbgen", file("dbgen")).enablePlugins(play.PlayScala).settings(
    scalaVersion := commonScalaVersion,
    version := appVersion,
    libraryDependencies ++= appDependencies
  )

  // Code generation task
  lazy val slickCodeGen = TaskKey[Seq[File]]("gen-tables")
  lazy val slickCodeGenTask = (baseDirectory, sourceManaged, dependencyClasspath in Compile, runner in Compile, streams) map { (cache, dir, cp, r, s) =>

    // get a list of all files in directory, recursively
    def recursiveListFiles(f: File): Seq[File] = {
      val files = Option(f.listFiles).toSeq.flatten
      files ++ files.filter(_.isDirectory).flatMap(recursiveListFiles)
    }

    // run slick code generation only when evolution files have changed
    val cachedEvolutionsGenerator = FileFunction.cached(cache / "target" / "slick-code-cache", FilesInfo.lastModified, FilesInfo.exists) {
      (inFiles: Set[File]) => {
        // evolution files have changed: run code generator from dbGen module
        s.log.info("Database evolutions have changed. Generating Slick code.")
        val outputDir = (dir / "main").getPath
        toError(r.run("PlaySlickCodeGenerator", cp.files, Array(outputDir), s.log))
        Set(file(outputDir + "/db/Tables.scala"))
      }
    }

    // we're monitoring file changes in the conf/evolutions folder
    val evolutions = recursiveListFiles(cache / "conf" / "evolutions")
    cachedEvolutionsGenerator(evolutions.toSet).toSeq
  }

}
