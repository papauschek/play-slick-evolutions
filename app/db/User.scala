package db


/*

import slick.driver.MySQLDriver.simple._
import plugins.JodaDateTimeMapper
import org.joda.time.DateTime

case class User(id: Int,
                firstName: String,
                lastName: String,
                email: Option[String],
                create: DateTime) {

}

object Users extends Table[User]("user") {

  implicit val DateTimeMapper = JodaDateTimeMapper

  def id = column[Int]("id")
  def firstName = column[String]("first_name")
  def lastName = column[String]("last_name")
  def email = column[Option[String]]("email")
  def create = column[DateTime]("create_date")

  def * = id ~ firstName ~ lastName ~ email ~  create ~ <> (User, User.unapply _)


  /** by Id finder for all tables */
  val byId = createFinderBy(_.id)

}

  */