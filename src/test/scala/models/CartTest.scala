package models

import main.model.Item
import model.{Cart, Order}
import org.scalamock.scalatest.MockFactory
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

import scala.collection.mutable.ArrayBuffer

class CartTest extends AnyWordSpec with Matchers with MockFactory {

  val apple = new Item(1, "apple", 1.5, 1, List("EU"))
  val coffee = new Item(2, "cawfee", 2.5, 5, List("EU", "NA"))
  val emptyCart = new Cart()
  val cartWithItems = new Cart(order = new Order(ArrayBuffer(apple, coffee)))

  "A Cart should" should {
    "Have a UUID" in {
      emptyCart.uuid shouldBe a[String]
    }
  }

  "Cart.addItem should" should {
    "Add an item to the cart " in {
      emptyCart.addItem(apple)
      emptyCart.order.items.length shouldBe 1
      emptyCart.order.items(0).name shouldEqual "apple"
    }
  }

  "Cart.removeItemById should" should {
    "Remove an item stored inside the cart matching the id argument" in {
      cartWithItems.removeItemById(2)
      cartWithItems.order.items.length shouldBe 1
      cartWithItems.order.items(0).name shouldBe "apple"
    }

    "Raise an error if itemId isn't in the cart" in {
      val thrownError = the[Exception] thrownBy {
        cartWithItems.removeItemById(3)
      }
      thrownError.getMessage should equal("Error: ItemId not in cart")
    }
  }

  "Cart.clearCart should" should {
    "Remove all items stored inside the cart" in {
      cartWithItems.clearCart()
      cartWithItems.order.items.length shouldBe 0
    }
  }
}
