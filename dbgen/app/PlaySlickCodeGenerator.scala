import scala.slick.jdbc.meta.createModel
import scala.slick.driver.H2Driver.simple._
import scala.slick.driver.H2Driver
import play.api._
import java.io._
import play.api.db.evolutions.Evolutions
import play.api.Application
import scala.slick.model.codegen.SourceCodeGenerator
import scala.slick.model.Model

/**
 *  This customizes the Slick code generator.
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
    val db = Database.forDataSource(play.api.db.DB.getDataSource(databaseName))
    val excludedTables = Seq("play_evolutions") // exclude play evolutions table
    val model = db.withSession {
        implicit session =>
          val tables = H2Driver.getTables.list.filterNot(t => excludedTables contains t.name.name)
          createModel( tables, H2Driver )
      }

    // generate slick db code
    val codegen = new PlaySlickCodeGenerator(model)

    codegen.writeToFile(
      profile = outputProfile,
      folder = outputDir,
      pkg = outputPackage,
      container = "Tables",
      fileName = "Tables.scala")

    Play.stop()
  }

}

class PlaySlickCodeGenerator(model: Model) extends SourceCodeGenerator(model) {

  // customize Scala entity name (case class, etc.)
  override def entityName = dbTableName => dbTableName match {
    case _ => super.entityName(dbTableName)
  }

  // customize Scala table name (table class, table values, ...)
  override def tableName = dbTableName => dbTableName match {
    case _ => super.tableName(dbTableName)
  }

  override def code = "import db.DB._" + "\n" + super.code

  // override generator responsible for tables
  override def Table = new Table(_){
    table =>
    // customize table value (TableQuery) name (uses tableName as a basis)
    override def TableValue = new TableValue{
      override def rawName = super.rawName
    }
    // override generator responsible for columns
    override def Column = new Column(_){
      // customize Scala column names
      override def rawName = (table.model.name.table,this.model.name) match {
        case _ => super.rawName
      }
      /** JodaTime */
      override def rawType =
        if(model.tpe == "java.sql.Timestamp") "org.joda.time.DateTime" else super.rawType
    }

  }
}

case class FakeApplication(
                            override val path: java.io.File = new java.io.File("."),
                            override val classloader : ClassLoader,// = .getClassLoader,
                            val additionalConfiguration: Map[String, _ <: Any] = Map.empty) extends {
  override val sources = None
  override val mode = play.api.Mode.Test
} with Application with WithDefaultConfiguration with WithDefaultGlobal with WithDefaultPlugins {

  override def configuration = {
    super.configuration ++ play.api.Configuration.from(additionalConfiguration)
  }

}
