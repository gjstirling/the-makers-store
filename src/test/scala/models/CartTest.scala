package models
import controllers.ItemsController
import main.model.Item
import model.{Cart, Order}
import org.scalamock.scalatest.MockFactory
import org.scalatest.BeforeAndAfterEach
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

import scala.collection.mutable.ArrayBuffer

class CartTest extends AnyWordSpec with Matchers with MockFactory {

  "A Cart Should" should {
    "Have a String UUID" in {
      val mockItemsController = mock[ItemsController]
      val cart = new Cart("12345", 1, mockItemsController)
      cart.uuid shouldBe a[String]
    }
  }

  "Cart.addItem" should {

    "Add an item to the cart " in {
      val appleOrder = new Item(1, "apple", 1.5, 2, List("EU"))
      val coffeeOrder = new Item(2, "café", 2.5, 5, List("EU"))
      val items = ArrayBuffer(appleOrder, coffeeOrder)

      val mockItemsController = mock[ItemsController]
      val cart = new Cart("12345", 1, mockItemsController)
      (mockItemsController.getItemsByLocationId _).expects(1).returns(items)
      cart.addItem(appleOrder)
      cart.order.items.length shouldBe 1
      cart.order.items(0).name shouldEqual "apple"
    }

    "Return an error if quantity ordered is greater than stock" in {
      val appleOrder = new Item(1, "apple", 1.5, 2, List("EU"))
      val coffeeOrder = new Item(2, "café", 2.5, 5, List("EU"))
      val items = ArrayBuffer(appleOrder, coffeeOrder)
      val bigAppleOrder = new Item(1, "apple", 1.5, 4, List("EU"))

      val mockItemsController = mock[ItemsController]
      (mockItemsController.getItemsByLocationId _).expects(1).returns(items)
      val cart = new Cart("12345", 1, mockItemsController)
      val thrownError = the[Exception] thrownBy {
        cart.addItem(bigAppleOrder)
      }
      thrownError.getMessage should equal("Error: Not enough stock of item")
    }

    "Return an error if item is not avaiable at location" in {
      val appleOrder = new Item(1, "apple", 1.5, 2, List("EU"))
      val coffeeOrder = new Item(2, "café", 2.5, 5, List("EU"))
      val cawfee = new Item(8, "cawfee", 2.5, 5, List("NA"))
      val items = ArrayBuffer(appleOrder, coffeeOrder)

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
      val appleOrder = new Item(1, "apple", 1.5, 2, List("EU"))
      val coffeeOrder = new Item(2, "café", 2.5, 5, List("EU"))
      val cartWithItems = new Cart("12345", 1, order = new Order(ArrayBuffer(appleOrder, coffeeOrder)))

      cartWithItems.removeItemById(2)
      cartWithItems.order.items.length shouldBe 1
      cartWithItems.order.items(0).name shouldBe "apple"
    }

    "Raise an error if itemId isn't in the cart" in {
      val appleOrder = new Item(1, "apple", 1.5, 2, List("EU"))
      val coffeeOrder = new Item(2, "café", 2.5, 5, List("EU"))
      val cartWithItems = new Cart("12345", 1, order = new Order(ArrayBuffer(appleOrder, coffeeOrder)))

      val thrownError = the[Exception] thrownBy {
        cartWithItems.removeItemById(3)
      }
      thrownError.getMessage should equal("Error: ItemId not in cart")
    }
  }

  "Cart.clearCart" should {
    "Remove all items stored inside the cart" in {
      val appleOrder = new Item(1, "apple", 1.5, 2, List("EU"))
      val coffeeOrder = new Item(2, "café", 2.5, 5, List("EU"))
      val cartWithItems = new Cart("12345", 1, order = new Order(ArrayBuffer(appleOrder, coffeeOrder)))

      cartWithItems.clearCart
      cartWithItems.order.items.length shouldBe 0
    }
  }

  // Repeated behaviour ??
  "Cart.onPaymentFailure should" should {
    "Empty the cart" in {
      val appleOrder = new Item(1, "apple", 1.5, 2, List("EU"))
      val coffeeOrder = new Item(2, "café", 2.5, 5, List("EU"))
      val cartWithItems = new Cart("12345", 1, order = new Order(ArrayBuffer(appleOrder, coffeeOrder)))

      cartWithItems.onPaymentFailure
      cartWithItems.order.items.length shouldBe 0
    }
  }

  "Cart.onPaymentSuccess" should {
    "Update the inventory" in {
      val appleOrder = new Item(1, "apple", 1.5, 2, List("EU"))
      val coffeeOrder = new Item(2, "café", 2.5, 5, List("EU"))
      val appleStock = new Item(1, "apple", 1.5, 3, List("EU"))
      val coffeeStock = new Item(2, "café", 2.5, 5, List("EU"))
      val updatedAppleStock = new Item(1, "apple", 1.5, 1, List("EU"))
      val updatedCoffeeStock = new Item(2, "café", 2.5, 0, List("EU"))
      val stock = ArrayBuffer(appleStock, coffeeStock)
      val updatedItemStock = ArrayBuffer(new Item(1, "apple", 1.5, 1, List("EU")), new Item(2, "café", 2.5, 0, List("EU")))
      val mockItemsController = mock[ItemsController]
      (mockItemsController.getItemsByLocationId _).expects(1).returns(stock)
      val cart = new Cart("12345", 1, mockItemsController, order = new Order(ArrayBuffer(appleOrder, coffeeOrder)))

      (mockItemsController.updateItemById _).expects(1, None, None, Some(1), None).returns(updatedAppleStock)
      (mockItemsController.updateItemById _).expects(2, None, None, Some(0), None).returns(updatedCoffeeStock)
      cart.onPaymentSuccess() shouldBe (ArrayBuffer(updatedAppleStock, updatedCoffeeStock))
    }
  }
}
