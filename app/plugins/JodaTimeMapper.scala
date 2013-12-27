package plugins

/*

import scala.slick.lifted.{BaseTypeMapper, MappedTypeMapper}
import org.joda.time.{DateTimeZone, DateTime}
import java.sql.Timestamp

object JodaDateTimeMapper extends MappedTypeMapper[DateTime, java.sql.Timestamp] with BaseTypeMapper[DateTime] {

  def map(dt: DateTime): java.sql.Timestamp = {
    new Timestamp(dt.withZone(DateTimeZone.UTC).getMillis)
  }

  def comap(ts: java.sql.Timestamp): DateTime = {
    new DateTime(ts).withZone(DateTimeZone.UTC)
  }

}
*/