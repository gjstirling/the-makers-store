import controllers.ItemsController
import main.db.DbAdapterBase
import main.model.{Item, Location}
import org.scalamock.scalatest.MockFactory
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

class ItemsControllerTest extends AnyWordSpec with Matchers with MockFactory {

  val item1 = new Item(1, "Delicious Soup", 4.5, 15, List("NA", "EU"))
  val item2 = new Item(2, "Lovely American Apple", 1.0, 4, List("NA"))

  val allLocationsMock = mutable.LinkedHashMap(
    "EU" -> mutable.LinkedHashMap(
      "UK" -> Seq(
        new Location(1, "Glasgow"),
        new Location(2, "Edinburgh")
        )
    ),
    "NA" -> mutable.LinkedHashMap(
      "CA" -> Seq(
        new Location(3, "Vancouver"),
        new Location(4, "Toronto"),
      ),
      "US" -> Seq(
        new Location(5, "New York"),
        new Location(6, "Chicago"),
      )
    )
  )

  // CREATE routes:
  // returning a Unit confirms this is behaving correctly ? As only other return is a raised exception
  "controllers.ItemsController.createItem" should {
    "Create a new item" in {
      val mockDbAdapter = mock[DbAdapterBase]
      val items = ArrayBuffer(item1)
      val itemsController = new ItemsController(mockDbAdapter)

      (mockDbAdapter.getItems _).expects().returns(items)
      (mockDbAdapter.createItem _).expects(item2).returns()
      itemsController.createItem(item2) shouldEqual()
    }

    "raise error if item already exists" in {
      val mockDbAdapter = mock[DbAdapterBase]
      val items = ArrayBuffer(item1)
      val itemsController = new ItemsController(mockDbAdapter)
      (mockDbAdapter.getItems _).expects().returns(items)
      val thrownError = the[Exception] thrownBy {
        itemsController.createItem(item1)
      }
      thrownError.getMessage should equal("Error: Duplicated Item id")
    }
  }

  // GET route tests:
  "controllers.ItemsController.getAll" should {
    "Return an ArrayBuffer of items" in {
      val mockDbAdapter = mock[DbAdapterBase]
      val items = ArrayBuffer(item1, item2)
      val itemsController = new ItemsController(mockDbAdapter)

      (mockDbAdapter.getItems _).expects().returns(items)

      itemsController.getAllItems() should equal(items)
    }
  }

  "controllers.ItemsController.getItemById" should {
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

      val thrownError = the[Exception] thrownBy {
        itemsController.getItemById(2)
      }
      thrownError.getMessage should equal("Item id error")
    }
  }

  // UPDATE Routes:
  "Can update an item" in {
    val mockDbAdapter = mock[DbAdapterBase]
    val itemsController = new ItemsController(mockDbAdapter)
    val items = ArrayBuffer(item1)
    (mockDbAdapter.getItems _).expects().returns(items)

    val result = itemsController.updateItemById(1, Option("Average Soup"), Option(2.5), Option(10), Option(List("NA")))
    result.name shouldBe "Average Soup"
    result.price shouldBe 2.5
    result.quantity shouldBe 10
    result.availableLocales shouldBe List("NA")
  }

  "Can update single attributes of an item" in {
    val mockDbAdapter = mock[DbAdapterBase]
    val itemsController = new ItemsController(mockDbAdapter)
    val items = ArrayBuffer(item1)
    (mockDbAdapter.getItems _).expects().returns(items)
    itemsController.updateItemById(1, name = Option("Average Soup")).name shouldBe "Average Soup"
  }

  // DELETE Routes:
  "controllers.ItemsController.deleteItem" should {
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
      val thrownError = the[Exception] thrownBy {
        itemsController.deleteItemById(2)
      }
      thrownError.getMessage should equal("Error: Item id is invalid")
    }
  }

  "controllers.ItemsController.getItemsByLocationId" should {
    "Return a list of items based on a location id" in {
      val mockDbAdapter = mock[DbAdapterBase]
      val items = ArrayBuffer(item1, item2)
      (mockDbAdapter.getLocations _).expects().returns(allLocationsMock)
      (mockDbAdapter.getItems _).expects().returns(items)
      val itemsController = new ItemsController(mockDbAdapter)
      itemsController.getItemsByLocationId(2) should equal(ArrayBuffer(item1))
    }

    "Returns error if location id is invalid" in {
      val mockDbAdapter = mock[DbAdapterBase]
      val itemsController = new ItemsController(mockDbAdapter)
      val items = ArrayBuffer(item1, item2)
      (mockDbAdapter.getLocations _).expects().returns(allLocationsMock)
      (mockDbAdapter.getItems _).expects().returns(items)

      val thrownError = the[Exception] thrownBy {
        itemsController.getItemsByLocationId(11)
      }
      thrownError.getMessage should equal("Error: Location Id doesn't exist")
    }
  }

}


