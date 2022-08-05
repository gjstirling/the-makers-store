import main.db.DbAdapterBase
import main.model.Item
import org.scalamock.scalatest.MockFactory
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

import scala.collection.mutable.ArrayBuffer

class ItemsControllerTest extends AnyWordSpec with Matchers with MockFactory {
  "ItemsController.getAll" should {
    "Return an ArrayBuffer of items" in {
      val mockDbAdapter = mock[DbAdapterBase]
      val items = ArrayBuffer(
        new Item(1, "Delicious Soup", 4.5, 15, List("NA", "EU")),
        new Item(2, "Lovely Apple", 1.0, 4, List("NA", "EU"))
      )
      val itemsController = new ItemsController(mockDbAdapter)

      (mockDbAdapter.getItems _).expects().returns(items)

      itemsController.getAllItems() should equal(items)
    }
  }

  "ItemsController.getItemById" should {
    "Return a single item" in {
      val mockDbAdapter = mock[DbAdapterBase]
      val item1 = new Item(1, "Delicious Soup", 4.5, 15, List("NA", "EU"))
      val item2 = new Item(2, "Lovely Apple", 1.0, 4, List("NA", "EU"))
      val items = ArrayBuffer(item1, item2)
      val itemsController = new ItemsController(mockDbAdapter)

      (mockDbAdapter.getItems _).expects().returns(items)
      itemsController.getItemById(2) should equal(item2)
    }
  }
}


