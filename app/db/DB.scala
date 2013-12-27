package plugins

import scala.slick.driver.MySQLDriver.simple._
import play.api.Play._
import org.joda.time.{DateTimeZone, DateTime}
import java.sql.Timestamp

object DB {

  implicit val JodaTimeMapper = MappedColumnType.base[DateTime, java.sql.Timestamp](
    dt => new Timestamp(dt.withZone(DateTimeZone.UTC).getMillis),
    ts => new DateTime(ts).withZone(DateTimeZone.UTC)
  )

  lazy private val default = Database.forDataSource(_root_.play.api.db.DB.getDataSource("default")(current))

  def utcNow = DateTime.now.withZone(DateTimeZone.UTC)

  /**
   * Run the supplied function with a new session and automatically close the session at the end.
   */
  def apply[T](f: Session => T): T = default.withSession(f)

}
