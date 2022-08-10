package models

import main.model.Item
import model.Cart
import org.scalamock.scalatest.MockFactory
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

import scala.collection.mutable.ArrayBuffer

class CartTest extends AnyWordSpec with Matchers with MockFactory {

  val apple = new Item(1, "apple", 1.5, 1, List("EU"))
  val coffee = new Item(2, "cawfee", 2.5, 5, List("EU", "NA"))

  "A Cart should" should {
    "Have a UUID" in {
      val subject = new Cart()
      subject.uuid shouldBe a[String]
    }
  }

  "Cart.addItem should" should {
    "Add an item to the cart " in {
      val subject = new Cart()
      subject.addItem(apple)
      subject.items.length shouldBe 1
      subject.items(0).name shouldEqual "apple"
    }
  }

  "Cart.clearCart should" should {
    "Remove all items stored inside the cart" in {
      val subject = new Cart(items = ArrayBuffer(apple, coffee))
      subject.clearCart()
      subject.items.length shouldBe 0
    }
  }

  "Cart.removeItemById should" should {
    "Remove an item stored inside the cart matching the id argument" in {
      val subject = new Cart(items = ArrayBuffer(apple, coffee))
      subject.removeItemById(2)
      subject.items.length shouldBe 1
      subject.items(0).name shouldBe "apple"
    }

    "Raise an error if itemId isn't in the cart" in {
      val subject = new Cart(items = ArrayBuffer(apple, coffee))
      val thrownError = the[Exception] thrownBy {
        subject.removeItemById(3)
      }
      thrownError.getMessage should equal("Error: ItemId not in cart")
    }
  }
}
