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
  
  /** Entity class storing rows of table Session
   *  @param secret Database column secret PrimaryKey
   *  @param userId Database column user_id 
   *  @param createDate Database column create_date 
   *  @param expireDate Database column expire_date  */
  case class SessionRow(secret: String, userId: Int, createDate: java.sql.Timestamp, expireDate: java.sql.Timestamp)
  /** GetResult implicit for fetching SessionRow objects using plain SQL queries */
  implicit def GetResultSessionRow(implicit e0: GR[String], e1: GR[Int], e2: GR[java.sql.Timestamp]): GR[SessionRow] = GR{
    prs => import prs._
    SessionRow.tupled((<<[String], <<[Int], <<[java.sql.Timestamp], <<[java.sql.Timestamp]))
  }
  /** Table description of table session. Objects of this class serve as prototypes for rows in queries. */
  class Session(tag: Tag) extends Table[SessionRow](tag, "session") {
    def * = (secret, userId, createDate, expireDate) <> (SessionRow.tupled, SessionRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (secret.?, userId.?, createDate.?, expireDate.?).shaped.<>({r=>import r._; _1.map(_=> SessionRow.tupled((_1.get, _2.get, _3.get, _4.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))
    
    /** Database column secret PrimaryKey */
    val secret: Column[String] = column[String]("secret", O.PrimaryKey)
    /** Database column user_id  */
    val userId: Column[Int] = column[Int]("user_id")
    /** Database column create_date  */
    val createDate: Column[java.sql.Timestamp] = column[java.sql.Timestamp]("create_date")
    /** Database column expire_date  */
    val expireDate: Column[java.sql.Timestamp] = column[java.sql.Timestamp]("expire_date")
    
    /** Foreign key referencing User (database name session_user_id) */
    val userFk = foreignKey("session_user_id", userId, User)(r => r.id, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Restrict)
  }
  /** Collection-like TableQuery object for table Session */
  lazy val Session = TableQuery[Session]
  
  /** Entity class storing rows of table Token
   *  @param token Database column token PrimaryKey
   *  @param email Database column email 
   *  @param isSignUp Database column is_sign_up 
   *  @param createDate Database column create_date 
   *  @param expireDate Database column expire_date  */
  case class TokenRow(token: String, email: String, isSignUp: Byte, createDate: java.sql.Timestamp, expireDate: java.sql.Timestamp)
  /** GetResult implicit for fetching TokenRow objects using plain SQL queries */
  implicit def GetResultTokenRow(implicit e0: GR[String], e1: GR[Byte], e2: GR[java.sql.Timestamp]): GR[TokenRow] = GR{
    prs => import prs._
    TokenRow.tupled((<<[String], <<[String], <<[Byte], <<[java.sql.Timestamp], <<[java.sql.Timestamp]))
  }
  /** Table description of table token. Objects of this class serve as prototypes for rows in queries. */
  class Token(tag: Tag) extends Table[TokenRow](tag, "token") {
    def * = (token, email, isSignUp, createDate, expireDate) <> (TokenRow.tupled, TokenRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (token.?, email.?, isSignUp.?, createDate.?, expireDate.?).shaped.<>({r=>import r._; _1.map(_=> TokenRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))
    
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
  /** Collection-like TableQuery object for table Token */
  lazy val Token = TableQuery[Token]
  
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
   *  @param providerId Database column provider_id 
   *  @param provider Database column provider 
   *  @param userId Database column user_id 
   *  @param firstName Database column first_name 
   *  @param lastName Database column last_name 
   *  @param email Database column email 
   *  @param password Database column password  */
  case class UserLoginRow(providerId: String, provider: String, userId: Int, firstName: Option[String], lastName: Option[String], email: Option[String], password: Option[String])
  /** GetResult implicit for fetching UserLoginRow objects using plain SQL queries */
  implicit def GetResultUserLoginRow(implicit e0: GR[String], e1: GR[Int]): GR[UserLoginRow] = GR{
    prs => import prs._
    UserLoginRow.tupled((<<[String], <<[String], <<[Int], <<?[String], <<?[String], <<?[String], <<?[String]))
  }
  /** Table description of table user_login. Objects of this class serve as prototypes for rows in queries. */
  class UserLogin(tag: Tag) extends Table[UserLoginRow](tag, "user_login") {
    def * = (providerId, provider, userId, firstName, lastName, email, password) <> (UserLoginRow.tupled, UserLoginRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (providerId.?, provider.?, userId.?, firstName, lastName, email, password).shaped.<>({r=>import r._; _1.map(_=> UserLoginRow.tupled((_1.get, _2.get, _3.get, _4, _5, _6, _7)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))
    
    /** Database column provider_id  */
    val providerId: Column[String] = column[String]("provider_id")
    /** Database column provider  */
    val provider: Column[String] = column[String]("provider")
    /** Database column user_id  */
    val userId: Column[Int] = column[Int]("user_id")
    /** Database column first_name  */
    val firstName: Column[Option[String]] = column[Option[String]]("first_name")
    /** Database column last_name  */
    val lastName: Column[Option[String]] = column[Option[String]]("last_name")
    /** Database column email  */
    val email: Column[Option[String]] = column[Option[String]]("email")
    /** Database column password  */
    val password: Column[Option[String]] = column[Option[String]]("password")
    
    /** Primary key of UserLogin (database name CONSTRAINT_C) */
    val pk = primaryKey("CONSTRAINT_C", (providerId, provider))
    
    /** Foreign key referencing User (database name user_login_user_id) */
    val userFk = foreignKey("user_login_user_id", userId, User)(r => r.id, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Restrict)
  }
  /** Collection-like TableQuery object for table UserLogin */
  lazy val UserLogin = TableQuery[UserLogin]
}