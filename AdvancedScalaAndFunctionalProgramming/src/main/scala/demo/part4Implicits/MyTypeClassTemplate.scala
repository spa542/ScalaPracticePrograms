package demo.part4Implicits

// TYPE CLASS TEMPLATE
trait MyTypeClassTemplate[T] {
  def action(value: T): String // or some other type
}

object MyTypeClassTemplate {
  def apply[T](implicit instance: MyTypeClassTemplate[T]) = instance
}
