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
}


