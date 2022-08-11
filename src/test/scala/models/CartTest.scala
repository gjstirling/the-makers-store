package models
import controllers.ItemsController
import main.model.Item
import model.{Cart, Order}
import org.scalamock.scalatest.MockFactory
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

import scala.collection.mutable.ArrayBuffer

class CartTest extends AnyWordSpec with Matchers with MockFactory {

  val appleStock = new Item(1, "apple", 1.5, 3, List("EU"))
  val coffeeStock = new Item(2, "café", 2.5, 5, List("EU"))
  val appleOrder = new Item(1, "apple", 1.5, 2, List("EU"))
  val bigAppleOrder = new Item(1, "apple", 1.5, 4, List("EU"))
  val coffeeOrder = new Item(2, "café", 2.5, 5, List("EU"))
  val cawfee = new Item(8, "cawfee", 2.5, 5, List("NA"))
  val stock = ArrayBuffer(appleStock, coffeeStock)
  val items = ArrayBuffer(appleOrder, coffeeOrder)
  val cartWithItems = new Cart("12345", 1, order = new Order(ArrayBuffer(appleOrder, coffeeOrder)))

  "A Cart Should" should {
    "Have a String UUID" in {
      val mockItemsController = mock[ItemsController]
      val cart = new Cart("12345", 1, mockItemsController)
      cart.uuid shouldBe a[String]
    }
  }

  "Cart.addItem" should {
    "Add an item to the cart " in {
      val mockItemsController = mock[ItemsController]
      val cart = new Cart("12345", 1, mockItemsController)
      (mockItemsController.getItemsByLocationId _).expects(1).returns(items)
      cart.addItem(appleOrder)
      cart.order.items.length shouldBe 1
      cart.order.items(0).name shouldEqual "apple"
    }

    "Return an error if quantity ordered is greater than stock" in {
      val mockItemsController = mock[ItemsController]
      (mockItemsController.getItemsByLocationId _).expects(1).returns(items)
      val cart = new Cart("12345", 1, mockItemsController)
      val thrownError = the[Exception] thrownBy {
        cart.addItem(bigAppleOrder)
      }
      thrownError.getMessage should equal("Error: Not enough stock of item")
    }

    "Return an error if item is not avaiable at location" in {
      val mockItemsController = mock[ItemsController]
      (mockItemsController.getItemsByLocationId _).expects(1).returns(items)
      val cart = new Cart("12345", 1, mockItemsController)
      val thrownError = the[Exception] thrownBy {
        cart.addItem(cawfee)
      }
      thrownError.getMessage should equal("Error: Item not availiable at your location")
    }
  }

  "Cart.removeItemById" should {
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

  "Cart.clearCart" should {
    "Remove all items stored inside the cart" in {
      cartWithItems.clearCart
      cartWithItems.order.items.length shouldBe 0
    }
  }

//  "Cart.onPaymentSuccess" should {
//    "Update the inventory" in {
//      // update item by subtracting quantity of each item
//      // send updated items back to DB
//    }

    // Repeated behaviour ??
    "Cart.onPaymentFailure should" should {
      "Empty the cart" in {
        cartWithItems.onPaymentFailure
        cartWithItems.order.items.length shouldBe 0
      }
    }
}
