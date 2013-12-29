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

    sourceGenerators in Compile <+= slickCodeGenTask,

    //slick in Compile <<= slickCodeGenTask,

    // remove compiler warnings as needed
    //scalacOptions ++= Seq("-feature")
    routesImport ++= Seq("language.reflectiveCalls") // remove warnings with routes imports
  ).dependsOn(dbGen)



  lazy val dbGen = play.Project("dbgen", appVersion, dbDependencies, path = file("dbgen")).settings(
  )

  def recursiveListFiles(f: File): Seq[File] = {
    val files = Option(f.listFiles).toSeq.flatten
    files ++ files.filter(_.isDirectory).flatMap(recursiveListFiles)
  }

  // code generation task
  lazy val slick = TaskKey[Seq[File]]("gen-tables")
  lazy val slickCodeGenTask = (baseDirectory, confDirectory, sourceManaged, dependencyClasspath in Compile, runner in Compile, streams) map { (cache, conf, dir, cp, r, s) =>

    val cachedEvolutionsGenerator = FileFunction.cached(cache / "target" / "slick-code-cache", FilesInfo.lastModified, FilesInfo.exists) {
      (inFiles: Set[File]) => {
        s.log.info("Database evolutions have changed. Generating Slick code.")
        val outputDir = (dir / "main").getPath
        toError(r.run("db.DbCodeGenerator", cp.files, Array(outputDir), s.log))
        Set(file(outputDir + "/db/Tables.scala"))
      }
    }

    val evolutions = recursiveListFiles(conf / "evolutions")
    cachedEvolutionsGenerator(evolutions.toSet).toSeq
  }

}
