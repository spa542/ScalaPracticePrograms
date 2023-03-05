package demo.part4Implicits

import java.util.Date

object JSONSerialization extends App {

  /*
    Users, Posts, Feeds
    Serialize to JSON
   */

  case class User(name: String, age: Int, email: String)
  case class Post(content: String, createdAt: Date)
  case class Feed(user: User, posts: List[Post])

  /*
    1. Create intermediate datatypes (that can be stringified to json from primitive types) (int, string, list, etc)
    2. Create Type Classes for conversions to intermediate datatypes
    3. Serialize those intermediate types to json
   */

  // 1
  // sealed keyword forces use to define the subclasses of this trait in the current source file
  sealed trait JSONValue { // Starts the hierarchy of our intermediate datatype
    def stringify: String
  }

  final case class JSONString(value: String) extends JSONValue {
    override def stringify: String =
      "\"" + value + "\"" // not considering case where "" could be in value (simple example)
  }

  final case class JSONNumber(value: Int) extends JSONValue {
    override def stringify: String = value.toString
  }

  final case class JSONArray(values: List[JSONValue]) extends JSONValue {
    override def stringify: String = values.map(_.stringify).mkString("[", ",", "]")
  }

  final case class JSONObject(values: Map[String, JSONValue]) extends JSONValue {
    /*
      {
        name: "John",
        age: 22,
        friends: [ ... ],
        latestPost: {
          content: "Scala rocks!",
          date: ...
        }
      }
     */
    override def stringify: String = values.map {
      case (key, value) => "\"" + key + "\":" + value.stringify
    }
      .mkString("{", ",", "}")
  }

  val data = JSONObject(Map(
    "user" -> JSONString("Ryan"),
    "post" -> JSONArray(List(
      JSONString("Scala rocks!"),
      JSONNumber(453)
    ))
  ))

  println(data.stringify)

  // 2
  // Type Class
  /*
    1. Type class itself
    2. Type class instances (implicit)
    3. Pimp library to use type class instances
   */

  // 2.1 (this is our type class)
  trait JSONConverter[T] {
    def convert(value: T): JSONValue
  }

  // 2.3 conversion
  // Ops or Enrichment (same thing)
  implicit class JSONOps[T](value: T) {
    def toJSON(implicit converter: JSONConverter[T]): JSONValue =
      converter.convert(value)
  }

  // 2.2

  // existing data types
  implicit object StringConverter extends JSONConverter[String] {
    override def convert(value: String): JSONValue = JSONString(value)
  }

  implicit object NumberConverter extends JSONConverter[Int] {
    override def convert(value: Int): JSONValue = JSONNumber(value)
  }

  // custom data types
  implicit object UserConverter extends JSONConverter[User] {
    override def convert(user: User): JSONValue = JSONObject(Map(
      "name" -> JSONString(user.name),
      "age" -> JSONNumber(user.age),
      "email" -> JSONString(user.email)
    ))
  }

  implicit object PostConverter extends JSONConverter[Post] {
    override def convert(post: Post): JSONValue = JSONObject(Map(
      "content" -> JSONString(post.content),
      "createdAt" -> JSONString(post.createdAt.toString)
    ))
  }

  implicit object FeedConverter extends JSONConverter[Feed] {
    override def convert(feed: Feed): JSONValue = JSONObject(Map(
      // Will find toJSON from implicit ops, then find User converter as part of implicit converter in toJSON
      "user" -> feed.user.toJSON, // was originally UserConverter.convert(feed.user) -> can use the implicit ops instead!
      "posts" -> JSONArray(feed.posts.map(_.toJSON)) // was originally (PostConverter.convert) -> unnecessary dependency!
    ))
  }

  // 3
  // Call stringify on result
  val now = new Date(System.currentTimeMillis())
  val john = User("John", 34, "john@ryry.com")
  val feed = Feed(john, List(
    Post("hello", now),
    Post("look at this cute puppy", now)
  ))

  // Makes use of all the magic done so far
  println(feed.toJSON.stringify)

}
