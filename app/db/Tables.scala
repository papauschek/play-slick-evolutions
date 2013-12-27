package db
// AUTO-GENERATED Slick data model
/** Stand-alone Slick data model for immediate use */
object Tables extends {
  val profile = scala.slick.driver.H2Driver
} with Tables

/** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this late.) */
trait Tables {
  val profile: scala.slick.driver.JdbcProfile
  import profile.simple._
  import scala.slick.model.ForeignKeyAction
  import scala.slick.jdbc.{GetResult => GR}
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  
  /** Entity class storing rows of table EmailToken
   *  @param token Database column token PrimaryKey
   *  @param email Database column email 
   *  @param isSignUp Database column is_sign_up 
   *  @param createDate Database column create_date 
   *  @param expireDate Database column expire_date  */
  case class EmailTokenRow(token: String, email: String, isSignUp: Byte, createDate: java.sql.Timestamp, expireDate: java.sql.Timestamp)
  /** GetResult implicit for fetching EmailTokenRow objects using plain SQL queries */
  implicit def GetResultEmailTokenRow(implicit e0: GR[String], e1: GR[Byte], e2: GR[java.sql.Timestamp]): GR[EmailTokenRow] = GR{
    prs => import prs._
    EmailTokenRow.tupled((<<[String], <<[String], <<[Byte], <<[java.sql.Timestamp], <<[java.sql.Timestamp]))
  }
  /** Table description of table email_token. Objects of this class serve as prototypes for rows in queries. */
  class EmailToken(tag: Tag) extends Table[EmailTokenRow](tag, "email_token") {
    def * = (token, email, isSignUp, createDate, expireDate) <> (EmailTokenRow.tupled, EmailTokenRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (token.?, email.?, isSignUp.?, createDate.?, expireDate.?).shaped.<>({r=>import r._; _1.map(_=> EmailTokenRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))
    
    /** Database column token PrimaryKey */
    val token: Column[String] = column[String]("token", O.PrimaryKey)
    /** Database column email  */
    val email: Column[String] = column[String]("email")
    /** Database column is_sign_up  */
    val isSignUp: Column[Byte] = column[Byte]("is_sign_up")
    /** Database column create_date  */
    val createDate: Column[java.sql.Timestamp] = column[java.sql.Timestamp]("create_date")
    /** Database column expire_date  */
    val expireDate: Column[java.sql.Timestamp] = column[java.sql.Timestamp]("expire_date")
  }
  /** Collection-like TableQuery object for table EmailToken */
  lazy val EmailToken = TableQuery[EmailToken]
  
  /** Entity class storing rows of table User
   *  @param id Database column id AutoInc, PrimaryKey
   *  @param firstName Database column first_name 
   *  @param lastName Database column last_name 
   *  @param email Database column email 
   *  @param createDate Database column create_date  */
  case class UserRow(id: Int, firstName: String, lastName: String, email: Option[String], createDate: java.sql.Timestamp)
  /** GetResult implicit for fetching UserRow objects using plain SQL queries */
  implicit def GetResultUserRow(implicit e0: GR[Int], e1: GR[String], e2: GR[java.sql.Timestamp]): GR[UserRow] = GR{
    prs => import prs._
    UserRow.tupled((<<[Int], <<[String], <<[String], <<?[String], <<[java.sql.Timestamp]))
  }
  /** Table description of table user. Objects of this class serve as prototypes for rows in queries. */
  class User(tag: Tag) extends Table[UserRow](tag, "user") {
    def * = (id, firstName, lastName, email, createDate) <> (UserRow.tupled, UserRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (id.?, firstName.?, lastName.?, email, createDate.?).shaped.<>({r=>import r._; _1.map(_=> UserRow.tupled((_1.get, _2.get, _3.get, _4, _5.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))
    
    /** Database column id AutoInc, PrimaryKey */
    val id: Column[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column first_name  */
    val firstName: Column[String] = column[String]("first_name")
    /** Database column last_name  */
    val lastName: Column[String] = column[String]("last_name")
    /** Database column email  */
    val email: Column[Option[String]] = column[Option[String]]("email")
    /** Database column create_date  */
    val createDate: Column[java.sql.Timestamp] = column[java.sql.Timestamp]("create_date")
    
    /** Uniqueness Index over (email) (database name email_uk_index_2) */
    val index1 = index("email_uk_index_2", email, unique=true)
  }
  /** Collection-like TableQuery object for table User */
  lazy val User = TableQuery[User]
  
  /** Entity class storing rows of table UserLogin
   *  @param providerUserId Database column provider_user_id 
   *  @param provider Database column provider 
   *  @param providerMethod Database column provider_method 
   *  @param userId Database column user_id 
   *  @param firstName Database column first_name 
   *  @param lastName Database column last_name 
   *  @param email Database column email 
   *  @param hasher Database column hasher 
   *  @param password Database column password 
   *  @param salt Database column salt  */
  case class UserLoginRow(providerUserId: String, provider: String, providerMethod: String, userId: Int, firstName: Option[String], lastName: Option[String], email: Option[String], hasher: Option[String], password: Option[String], salt: Option[String])
  /** GetResult implicit for fetching UserLoginRow objects using plain SQL queries */
  implicit def GetResultUserLoginRow(implicit e0: GR[String], e1: GR[Int]): GR[UserLoginRow] = GR{
    prs => import prs._
    UserLoginRow.tupled((<<[String], <<[String], <<[String], <<[Int], <<?[String], <<?[String], <<?[String], <<?[String], <<?[String], <<?[String]))
  }
  /** Table description of table user_login. Objects of this class serve as prototypes for rows in queries. */
  class UserLogin(tag: Tag) extends Table[UserLoginRow](tag, "user_login") {
    def * = (providerUserId, provider, providerMethod, userId, firstName, lastName, email, hasher, password, salt) <> (UserLoginRow.tupled, UserLoginRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (providerUserId.?, provider.?, providerMethod.?, userId.?, firstName, lastName, email, hasher, password, salt).shaped.<>({r=>import r._; _1.map(_=> UserLoginRow.tupled((_1.get, _2.get, _3.get, _4.get, _5, _6, _7, _8, _9, _10)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))
    
    /** Database column provider_user_id  */
    val providerUserId: Column[String] = column[String]("provider_user_id")
    /** Database column provider  */
    val provider: Column[String] = column[String]("provider")
    /** Database column provider_method  */
    val providerMethod: Column[String] = column[String]("provider_method")
    /** Database column user_id  */
    val userId: Column[Int] = column[Int]("user_id")
    /** Database column first_name  */
    val firstName: Column[Option[String]] = column[Option[String]]("first_name")
    /** Database column last_name  */
    val lastName: Column[Option[String]] = column[Option[String]]("last_name")
    /** Database column email  */
    val email: Column[Option[String]] = column[Option[String]]("email")
    /** Database column hasher  */
    val hasher: Column[Option[String]] = column[Option[String]]("hasher")
    /** Database column password  */
    val password: Column[Option[String]] = column[Option[String]]("password")
    /** Database column salt  */
    val salt: Column[Option[String]] = column[Option[String]]("salt")
    
    /** Primary key of UserLogin (database name CONSTRAINT_C) */
    val pk = primaryKey("CONSTRAINT_C", (providerUserId, provider))
    
    /** Foreign key referencing User (database name user_login_user_id) */
    val userFk = foreignKey("user_login_user_id", userId, User)(r => r.id, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Restrict)
  }
  /** Collection-like TableQuery object for table UserLogin */
  lazy val UserLogin = TableQuery[UserLogin]
  
  /** Entity class storing rows of table UserSession
   *  @param secret Database column secret PrimaryKey
   *  @param userId Database column user_id 
   *  @param createDate Database column create_date 
   *  @param expireDate Database column expire_date  */
  case class UserSessionRow(secret: String, userId: Int, createDate: java.sql.Timestamp, expireDate: java.sql.Timestamp)
  /** GetResult implicit for fetching UserSessionRow objects using plain SQL queries */
  implicit def GetResultUserSessionRow(implicit e0: GR[String], e1: GR[Int], e2: GR[java.sql.Timestamp]): GR[UserSessionRow] = GR{
    prs => import prs._
    UserSessionRow.tupled((<<[String], <<[Int], <<[java.sql.Timestamp], <<[java.sql.Timestamp]))
  }
  /** Table description of table user_session. Objects of this class serve as prototypes for rows in queries. */
  class UserSession(tag: Tag) extends Table[UserSessionRow](tag, "user_session") {
    def * = (secret, userId, createDate, expireDate) <> (UserSessionRow.tupled, UserSessionRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (secret.?, userId.?, createDate.?, expireDate.?).shaped.<>({r=>import r._; _1.map(_=> UserSessionRow.tupled((_1.get, _2.get, _3.get, _4.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))
    
    /** Database column secret PrimaryKey */
    val secret: Column[String] = column[String]("secret", O.PrimaryKey)
    /** Database column user_id  */
    val userId: Column[Int] = column[Int]("user_id")
    /** Database column create_date  */
    val createDate: Column[java.sql.Timestamp] = column[java.sql.Timestamp]("create_date")
    /** Database column expire_date  */
    val expireDate: Column[java.sql.Timestamp] = column[java.sql.Timestamp]("expire_date")
    
    /** Foreign key referencing User (database name user_session_user_id) */
    val userFk = foreignKey("user_session_user_id", userId, User)(r => r.id, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Restrict)
  }
  /** Collection-like TableQuery object for table UserSession */
  lazy val UserSession = TableQuery[UserSession]
}