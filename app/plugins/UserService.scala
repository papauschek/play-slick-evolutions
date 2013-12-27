package plugins

import db.UserIdentity
import scala.slick.driver.MySQLDriver.simple._
import db.Tables._
import play.api._
import securesocial.core._
import securesocial.core.providers.Token

class UserService(application: Application) extends UserServicePlugin(application) {

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
          u <- User if ul.userId === u.id
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
      passwordInfo = None)

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
    ???
  }

  /**
   * Saves the user.  This method gets called when a user logs in.
   * This is your chance to save the user information in your backing store.
   * @param user
   */
  def save(user: Identity) : Identity = {

    ???
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
    ???
  }


  /**
   * Finds a token
   *
   * Note: If you do not plan to use the UsernamePassword provider just provide en empty
   * implementation
   *
   * @param token the token id
   * @return
   */
  def findToken(token: String): Option[Token] = {
    ???
  }

  /**
   * Deletes a token
   *
   * Note: If you do not plan to use the UsernamePassword provider just provide en empty
   * implementation
   *
   * @param uuid the token id
   */
  def deleteToken(uuid: String) {
    ???
  }

  /**
   * Deletes all expired tokens
   *
   * Note: If you do not plan to use the UsernamePassword provider just provide en empty
   * implementation
   *
   */
  def deleteExpiredTokens() {
    ???
  }
}