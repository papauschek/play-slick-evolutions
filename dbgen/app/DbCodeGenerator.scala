package db

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
 *  This customizes the Slick code generator. We only do simple name mappings.
 *  For a more advanced example see https://github.com/cvogt/slick-presentation/tree/scala-exchange-2013
 */
object DbCodeGenerator{

  val url = "jdbc:h2:mem:test;MODE=MySQL"
  val jdbcDriver =  "org.h2.Driver"
  val slickProfile = scala.slick.driver.H2Driver
  val dbName = "default"
  val codePackage = "db"
  val outputProfile = "scala.slick.driver.MySQLDriver"

  def main(args: Array[String]) = {
    run(outputDir = args(0))
  }

  private def run(outputDir: String) = {

    // start fake application using in-memory database
    implicit val app = FakeApplication(
      path = new File("dbgen").getCanonicalFile,
      classloader = Thread.currentThread().getContextClassLoader,
      additionalConfiguration = Map(
        s"db.$dbName.driver" -> jdbcDriver,
        s"db.$dbName.url" -> url
      ))

    Play.start(app)

    // apply evolutions from main project
    Evolutions.applyFor(dbName)

    // get list of tables for which code will be generated
    val db = Database.forDataSource(play.api.db.DB.getDataSource(dbName))
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
      pkg = codePackage,
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
