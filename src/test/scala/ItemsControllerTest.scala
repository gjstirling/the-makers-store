import main.db.DbAdapterBase
import main.model.{Item, Location}
import org.scalamock.scalatest.MockFactory
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

import scala.collection.mutable.ArrayBuffer

class ItemsControllerTest extends AnyWordSpec with Matchers with MockFactory {

  val item1 = new Item(1, "Delicious Soup", 4.5, 15, List("NA", "EU"))
  val item2 = new Item(2, "Lovely Apple", 1.0, 4, List("EU"))

  // CREATE routes:
  // returning a Unit confirms this is behaving correctly ? As only other return is a raised exception
  "ItemsController.createItem" should {
    "Create a new item" in {
      val mockDbAdapter = mock[DbAdapterBase]
      val items = ArrayBuffer(item1)
      val itemsController = new ItemsController(mockDbAdapter)

      (mockDbAdapter.getItems _).expects().returns(items)
      (mockDbAdapter.createItem _).expects(item2).returns()
      itemsController.createItem(item2) shouldEqual ()
    }

    "raise error if item already exists" in {
      val mockDbAdapter = mock[DbAdapterBase]
      val items = ArrayBuffer(item1)
      val itemsController = new ItemsController(mockDbAdapter)
      (mockDbAdapter.getItems _).expects().returns(items)
      val thrownError = the [Exception] thrownBy {itemsController.createItem(item1)}
      thrownError.getMessage should equal("Error: Duplicated Item id")
    }
  }

// GET route tests:
  "ItemsController.getAll" should {
    "Return an ArrayBuffer of items" in {
      val mockDbAdapter = mock[DbAdapterBase]
      val items = ArrayBuffer(item1, item2)
      val itemsController = new ItemsController(mockDbAdapter)

      (mockDbAdapter.getItems _).expects().returns(items)

      itemsController.getAllItems() should equal(items)
    }
  }

  "ItemsController.getItemById" should {
    "Return a single item" in {
      val mockDbAdapter = mock[DbAdapterBase]
      val items = ArrayBuffer(item1, item2)
      val itemsController = new ItemsController(mockDbAdapter)

      (mockDbAdapter.getItems _).expects().returns(items)
      itemsController.getItemById(2) should equal(item2)
    }

    "Raises an error if item id doesn't exist" in {
      val mockDbAdapter = mock[DbAdapterBase]
      val items = ArrayBuffer(item1)
      val itemsController = new ItemsController(mockDbAdapter)
      (mockDbAdapter.getItems _).expects().returns(items)

      val thrownError = the [Exception] thrownBy {itemsController.getItemById(2)}
      thrownError.getMessage should equal("Item id error")
    }
  }

  // UPDATE Routes:
//  "Can update an item" in {
//    val item1 = new Item(1, "Delicious Soup", 4.5, 15, List("NA", "EU"))
//  }


  // DELETE Routes:
  "ItemsController.deleteItem" should {
    "Delete an item" in {
      val mockDbAdapter = mock[DbAdapterBase]
      val items = ArrayBuffer(item1, item2)
      val itemsController = new ItemsController(mockDbAdapter)
      (mockDbAdapter.getItems _).expects().returns(items)
      itemsController.deleteItemById(1) should equal()
    }

    "Raise an error if id is invalid" in {
      val mockDbAdapter = mock[DbAdapterBase]
      val items = ArrayBuffer(item1)
      val itemsController = new ItemsController(mockDbAdapter)
      (mockDbAdapter.getItems _).expects().returns(items)
      val thrownError = the [Exception] thrownBy {itemsController.deleteItemById(2)}
      thrownError.getMessage should equal("Error: Item id is invalid")
    }
  }


}


