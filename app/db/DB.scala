package db

import scala.slick.driver.MySQLDriver.simple._
import play.api.Play._

object DB {

  lazy private val default = Database.forDataSource(play.api.db.DB.getDataSource("default"))

  /**
   * Run the supplied function with a new session and automatically close the session at the end.
   */
  def apply[T](f: Session => T): T = default.withSession(f)

}
