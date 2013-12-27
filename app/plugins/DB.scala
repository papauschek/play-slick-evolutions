package plugins

/*

import scala.slick.session.{Session, Database}
import play.api.Play._
import org.joda.time.{DateTimeZone, DateTime}

object DB {

  lazy private val default = Database.forDataSource(_root_.play.api.db.DB.getDataSource("default")(current))

  def utcNow = DateTime.now.withZone(DateTimeZone.UTC)

  /**
   * Run the supplied function with a new session and automatically close the session at the end.
   */
  def apply[T](f: Session => T): T = default.withSession(f)

}

*/