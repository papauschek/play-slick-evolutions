package controllers

import scala.slick.driver.MySQLDriver.simple._
import db.Tables._
import play.api.mvc._
import db.DB

object Application extends Controller {

  def index = Action {
    DB { implicit session =>
      val users = User.filter(_.firstName like "%chris%").list
      val output = users.map(_.id).mkString(", ")
      Ok(views.html.index(output))
    }
  }

}