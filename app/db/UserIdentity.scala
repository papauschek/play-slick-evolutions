package db

import securesocial.core._

case class UserIdentity(
  id: Int,
  identityId: IdentityId,
  firstName: String, lastName: String, fullName: String,
  email: Option[String],
  avatarUrl: Option[String], authMethod: AuthenticationMethod,
  oAuth1Info: Option[OAuth1Info] = None,
  oAuth2Info: Option[OAuth2Info] = None,
  passwordInfo: Option[PasswordInfo] = None) extends Identity {

}