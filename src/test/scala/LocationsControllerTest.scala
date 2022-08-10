import controllers.LocationsController
import main.db.DbAdapterBase
import main.model.Location
import org.scalamock.scalatest.MockFactory
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

class LocationsControllerTest extends AnyWordSpec with Matchers with MockFactory {
  val glasgow = new Location(1, "Glasgow")
  val edinburgh = new Location(2, "Edinburgh")
  val vancouver = new Location(3, "Vancouver")
  val toronto = new Location(4, "Toronto")
  val newYork = new Location(5, "New York")
  val chicago = new Location(6, "Chicago")

  val allLocationsMock = mutable.LinkedHashMap(
    "EU" -> mutable.LinkedHashMap(
      "UK" -> Seq(glasgow, edinburgh)
    ),
    "NA" -> mutable.LinkedHashMap(
      "CA" -> Seq(vancouver, toronto),
      "US" -> Seq(newYork, chicago)
    )
  )

  "LocationsController.getLocationsFromContinent" should {
    "Return a list of locations within the continent" in {
      val mockDbAdapter = mock[DbAdapterBase]
      (mockDbAdapter.getLocations _).expects().returns(allLocationsMock)
      val locationController = new LocationsController(mockDbAdapter)
      val result = locationController.getLocationsFromContinent("NA")
      result should equal(mutable.Iterable(vancouver, toronto, newYork, chicago))
    }
  }

}
