import java.io._
import scala.io.Source

// Serializer
@SerialVersionUID(15L)
class Animal(name: String, age: Int) extends Serializable {
  override def toString = s"Animal($name, $age)"
}


@SerialVersionUID(15L)
case class Person(name: String) extends Serializable


object FileHandlingDemo {

  def main(args: Array[String]): Unit = {
    // Read from the command line
    /*
    println("Enter your name:")
    var name = scala.io.StdIn.readLine()
    println(s"Hello ${name}")

    println("Enter your phone number:")
    var phoneNumber = scala.io.StdIn.readLine()
    println(s"Your phone number is ${phoneNumber}")

    println("How old are you?")
    var age = scala.io.StdIn.readInt()
    println(s"You are ${age} year(s) old")
    */

    // File Handling Demo

    // Read File (normally use an absolute path)
    val sampleFilePath = "/Users/rosi/Repositories/ScalaPracticePrograms/ScalaDay24FileHandling/src/main/scala/hello.txt"
    // Read the file
    val lines = scala.io.Source.fromFile(sampleFilePath).getLines()
    //val lines = scala.io.Source.fromFile(sampleFilePath).foreach(line => println(line)) // Another way to write it
    lines.foreach(println)

    // Write File (normally use absolute path)
    val outputFilePath = "/Users/rosi/Repositories/ScalaPracticePrograms/ScalaDay24FileHandling/src/main/scala/hellothere.txt"
    val outputFilePath2 = "/Users/rosi/Repositories/ScalaPracticePrograms/ScalaDay24FileHandling/src/main/scala/hellothereboy.txt"
    val outFh = new File(outputFilePath)
    val outFileWriter = new PrintWriter(outFh)

    println(outFileWriter.checkError())

    outFileWriter.write("Hello world!!\n")
    outFileWriter.write("I love you!!!\n")
    outFileWriter.write("Why hello there!\n")

    outFileWriter.close()

    // Using FileWriter/BufferedWriter
    val outFh2 = new File(outputFilePath2)
    val outFileWriter3 = new FileWriter(outFh2)
    val outFileWriter2 = new BufferedWriter(outFileWriter3) // Get the buffered writer

    outFileWriter2.write("YEET")

    outFileWriter2.flush() // Need to do manually or when closed
    outFileWriter.close()

    // Reading CSV
    val outputFilePath4 = "/Users/rosi/Repositories/ScalaPracticePrograms/ScalaDay24FileHandling/src/main/scala/test.csv"
    val lines2 = Source.fromFile(outputFilePath4).getLines()
    var counter = 1
    for (line <- lines2) {
      if (counter == 1) {
        val Array(word1: String, word2: String, word3: String, number1: String) = line.split(',')
        println(s"Word1 is ${word1} and Word2 is ${word2} and Word3 is ${word3} and Number1 is ${number1}")
      }
      counter += 1
    }

    // Serializing Data
    val fos = new FileOutputStream("/Users/rosi/Repositories/ScalaPracticePrograms/ScalaDay24FileHandling/src/main/scala/sampleserializer.txt")
    val oos = new ObjectOutputStream(fos)

    oos.writeObject(new Animal("Dvorak", 12))
    oos.writeObject(Person("Ryan"))
    oos.close()
    fos.close()

    // Deserializing Data
    val fis = new FileInputStream("/Users/rosi/Repositories/ScalaPracticePrograms/ScalaDay24FileHandling/src/main/scala/sampleserializer.txt")
    val ois = new ObjectInputStream(fis)

    val animal = ois.readObject()
    val person = ois.readObject()

    ois.close()
    fis.close()

    // Print the retrieved data
    println(animal)
    println(person)


  }

}
