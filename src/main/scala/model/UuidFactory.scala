package model
import java.util.UUID;

trait FactoryBase[T] {
  def create(): T
}

object UuidFactory extends FactoryBase[UUID] {
  def create(): UUID = {
    java.util.UUID.randomUUID
  }
}
