import java.io.File
import play.api._
import play.api.db.evolutions.Evolutions
import play.api.Application
import scala.slick.model.codegen.SourceCodeGenerator
import scala.slick.jdbc.meta.createModel
import scala.slick.driver.H2Driver.simple._
import scala.slick.driver.H2Driver

/**
 *  This code generator runs Play Framework Evolutions against an in-memory database
 *  and then generates code from this database using the default Slick code generator.
 *
 *  Parameters: Output directory for the generated code.
 *
 *  Other parameters are taken from this modules conf/application.conf
 */
object PlaySlickCodeGenerator{

  def main(args: Array[String]) = {
    try
    {
      run(outputDir = args(0))
    }
    catch {
      case ex: Throwable => println("Could not generate code: " + ex.getMessage)
    }
    finally {
      Play.stop()
    }
  }

  private def run(outputDir: String) = {

    // start fake application using in-memory database
    implicit val app = FakeApplication(
      path = new File("dbgen").getCanonicalFile,
      classloader = Thread.currentThread().getContextClassLoader)

    Play.start(app)

    // read database configuration
    val databaseNames = app.configuration.getConfig("db").toSeq.flatMap(_.subKeys)
    val databaseName = databaseNames.headOption.getOrElse("")
    val outputPackage = app.configuration.getString(s"db.$databaseName.outputPackage").getOrElse("")
    val outputProfile = app.configuration.getString(s"db.$databaseName.outputProfile").getOrElse("")

    if (databaseName.length == 0)
      throw new IllegalArgumentException("No database name found in configuration")
    else if (outputPackage.length == 0)
      throw new IllegalArgumentException("No outputPackage found in configuration")
    else if (outputProfile.length == 0)
      throw new IllegalArgumentException("No outputProfile found in configuration")

    // apply evolutions from main project
    Evolutions.applyFor(databaseName)

    // get list of tables for which code will be generated
    // also, we exclude the play evolutions table
    val db = Database.forDataSource(play.api.db.DB.getDataSource(databaseName))
    val excludedTables = Seq("play_evolutions")
    val model = db.withSession {
      implicit session =>
        val tables = H2Driver.getTables.list.filterNot(t => excludedTables contains t.name.name)
        createModel( tables, H2Driver )
    }

    // generate slick db code
    val codegen = new SourceCodeGenerator(model)

    codegen.writeToFile(
      profile = outputProfile,
      folder = outputDir,
      pkg = outputPackage,
      container = "Tables",
      fileName = "Tables.scala")

    Play.stop()
  }

}

/** Fake application needed for running evolutions outside normal Play app */
case class FakeApplication(
    override val path: java.io.File = new java.io.File("."),
    override val classloader : ClassLoader = classOf[FakeApplication].getClassLoader,
    val additionalConfiguration: Map[String, _ <: Any] = Map.empty) extends {
  override val sources = None
  override val mode = play.api.Mode.Test
} with Application with WithDefaultConfiguration with WithDefaultGlobal with WithDefaultPlugins {

  override def configuration =
    super.configuration ++ play.api.Configuration.from(additionalConfiguration)

}
