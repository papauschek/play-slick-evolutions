package plugins

import scala.slick.jdbc.meta.createModel
import scala.slick.driver.H2Driver

/**
 *  This customizes the Slick code generator. We only do simple name mappings.
 *  For a more advanced example see https://github.com/cvogt/slick-presentation/tree/scala-exchange-2013
 */
object DbCodeGenerator{

  val initScripts = Seq("conf/evolutions/create.sql")
  val url = "jdbc:h2:mem:test;MODE=MySQL;INIT="+initScripts.map("runscript from '"+_+"'").mkString("\\;")
  val jdbcDriver =  "org.h2.Driver"
  val slickProfile = scala.slick.driver.H2Driver

  def main(args: Array[String]) = {
    codegen.writeToFile(
      "scala.slick.driver.H2Driver",
      args(0),
      "db",
      "Tables",
      "Tables.scala"
    )
  }

  val db = H2Driver.simple.Database.forURL(url,driver=jdbcDriver)
  // filter out desired tables
  val excluded = Seq("")
  val model = db.withSession{ implicit session =>
    val tables = H2Driver.getTables.list.filterNot(t => excluded contains t.name.name)
    createModel( tables, H2Driver )
  }
  val codegen = new scala.slick.model.codegen.SourceCodeGenerator(model){

    // customize Scala entity name (case class, etc.)
    override def entityName = dbTableName => dbTableName match {
      case _ => super.entityName(dbTableName)
    }

    // customize Scala table name (table class, table values, ...)
    override def tableName = dbTableName => dbTableName match {
      case _ => super.tableName(dbTableName)
    }

    //override def code = "import plugins.DB._" + "\n" + super.code

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
          if(model.tpe == "java.sql.TimestampX") "org.joda.time.DateTime" else super.rawType
      }

    }
  }
}