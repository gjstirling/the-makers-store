import main.db.DbAdapterBase
import main.model.Item
import org.scalamock.scalatest.MockFactory
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

import scala.collection.mutable.ArrayBuffer

class ItemsControllerTest extends AnyWordSpec with Matchers with MockFactory {

  val item1 = new Item(1, "Delicious Soup", 4.5, 15, List("NA", "EU"))
  val item2 = new Item(2, "Lovely Apple", 1.0, 4, List("NA", "EU"))

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
      itemsController.getItemById(2) should equal()
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

  "ItemsController.createItem" should {
    "Create a new item" in {
      val mockDbAdapter = mock[DbAdapterBase]
      val items = ArrayBuffer(item1)
      val itemsController = new ItemsController(mockDbAdapter)
      (mockDbAdapter.getItems _).expects().returns(items)
      itemsController.createItem(item2) should equal()
    }

    "raise error if item already exists" in {

    }

  }

//  "ItemsController.deleteItem" should {}
}


