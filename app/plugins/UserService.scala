package plugins

import _root_.db.{DB, UserIdentity}
import scala.slick.driver.MySQLDriver.simple._
import db.Tables._
import play.api._
import securesocial.core._
import securesocial.core.providers.Token

class UserService(application: Application) extends UserServicePlugin(application) {

  implicit val mapper = DB.JodaTimeMapper

  /**
   * Finds a user that maches the specified id
   *
   * @param id the user id
   * @return an optional user
   */
  def find(id: IdentityId):Option[Identity] = {
    DB {
      implicit session => {
        (for {
          ul <- UserLogin if ul.provider === id.providerId && ul.providerUserId === id.userId
          u <- ul.userFk
        } yield(ul, u)).firstOption.map(x => toIdentity(x._1, x._2))
      }
    }
  }

  private def toIdentity(login: UserLoginRow, user: UserRow) : UserIdentity =
    UserIdentity(
      id = login.userId,
      identityId = IdentityId(login.providerUserId, login.provider),
      firstName = user.firstName,
      lastName = user.lastName,
      fullName = user.firstName + " " + user.lastName,
      email = user.email,
      avatarUrl = None,
      authMethod = AuthenticationMethod(login.providerMethod),
      oAuth1Info = None,
      oAuth2Info = None,
      passwordInfo = login.password.map(password => {
        PasswordInfo(login.hasher.getOrElse(""), password, login.salt)
      }))

  private def toUserLogin(identity: Identity, userId: Int) : UserLoginRow =
    UserLoginRow(
      providerUserId = identity.identityId.userId,
      provider = identity.identityId.providerId,
      providerMethod = identity.authMethod.method,
      userId = userId,
      firstName = Some(identity.firstName),
      lastName = Some(identity.lastName),
      email = identity.email,
      hasher = identity.passwordInfo.map(_.hasher),
      password = identity.passwordInfo.map(_.password),
      salt = identity.passwordInfo.flatMap(_.salt))

  /**
   * Finds a user by email and provider id.
   *
   * Note: If you do not plan to use the UsernamePassword provider just provide en empty
   * implementation.
   *
   * @param email - the user email
   * @param providerId - the provider id
   * @return
   */
  def findByEmailAndProvider(email: String, providerId: String):Option[Identity] =
  {
    DB {
      implicit session => {
        (for {
          ul <- UserLogin if ul.email === email && ul.provider === providerId
          u <- ul.userFk
        } yield(ul, u)).firstOption.map(x => toIdentity(x._1, x._2))
      }
    }
  }

  /**
   * Saves the user.  This method gets called when a user logs in.
   * This is your chance to save the user information in your backing store.
   * @param user
   */
  def save(user: Identity) : Identity = {
    DB {
      implicit session => {
        val userRow = UserRow(0, user.firstName, user.lastName, user.email, DB.utcNow)
        val userId : Int = User returning User.map(_.id) insert(userRow)
        val userLoginRow = toUserLogin(user, userId)
        UserLogin.insert(userLoginRow)
        toIdentity(userLoginRow, userRow)
      }
    }
  }

  /**
   * Saves a token.  This is needed for users that
   * are creating an account in the system instead of using one in a 3rd party system.
   *
   * Note: If you do not plan to use the UsernamePassword provider just provide en empty
   * implementation
   *
   * @param token The token to save
   * @return A string with a uuid that will be embedded in the welcome email.
   */
  def save(token: Token) {
    DB {
      implicit session => {
        val emailToken = EmailTokenRow(token.uuid, token.email, token.isSignUp, token.creationTime, token.expirationTime)
        println(emailToken)
        EmailToken.insert(emailToken)
        val users = User.filter(_.firstName like "%chris%").list
        println(users.map(_.id).mkString(", "))
      }
    }
  }


  /**
   * Finds a token (UsernamePassword)
   */
  def findToken(token: String): Option[Token] = {
    DB {
      implicit session => {
        EmailToken.filter(_.token === token).firstOption.map(
          t => Token(t.token, t.email, t.createDate, t.expireDate, t.isSignUp)
        )
      }
    }
  }

  /**
   * Deletes a token (UsernamePassword)
   */
  def deleteToken(uuid: String) {
    DB {
      implicit session => {
        EmailToken.filter(_.token === uuid).delete
      }
    }
  }

  /**
   * Deletes all expired tokens (UsernamePassword)
   */
  def deleteExpiredTokens() {
    DB {
      implicit session => {
        EmailToken.filter(_.expireDate < DB.utcNow).delete
      }
    }
  }
}