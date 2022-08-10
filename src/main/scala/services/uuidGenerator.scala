package services

object UuidGenerator {

  def create(): String  ={
    java.util.UUID.randomUUID.toString
  }

}
