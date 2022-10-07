// Importing the package names
import cleansing.RemoveNullValues
// Import with aliased name
import connector.{SQLConnector=>sql}
// Importing a class instead of object
import store.StoreInHbase
// Importing multiple classes
import transform._

object PackagesAndImportDemo {

  def main(args: Array[String]): Unit = {
    // Normal object import
    RemoveNullValues.remove()
    // Import with aliased name
    println(sql.connect(10, 20))
    // Importing a scala class instead of an object
    var importedClass = new StoreInHbase()
    importedClass.yeet()
    // Imported both of these classes using _
    TransformData.trans()
    TransformData2.trans()
  }

}
