package main.services
import main.model.Location
import org.scalamock.scalatest.MockFactory
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import services.{FlatLocation, LocationHelper}

import scala.collection.mutable

class LocationHelperTest extends AnyWordSpec with Matchers with MockFactory {

  val mockLocation1 = new Location(0, "Testville")
  val mockLocation2 = new Location(1, "Test Town")
  val mockLocation3 = new Location(2, "WeeTestToon")
  val mockLocation4 = new Location(3, "Der Test")
  val mockLocation5 = new Location(4, "Das Test")

  val mockFlatLocation1 = FlatLocation(0, "Testville", "US", "NA")
  val mockFlatLocation2 = FlatLocation(1, "Test Town", "CA", "NA")
  val mockFlatLocation3 = FlatLocation(2, "WeeTestToon", "UK", "EU")
  val mockFlatLocation4 = FlatLocation(3, "Der Test", "DE", "EU")
  val mockFlatLocation5 = FlatLocation(4, "Das Test", "DE", "EU")

  val mockLocations = mutable.LinkedHashMap(
    "NA" -> mutable.LinkedHashMap(
      "US" -> Seq(mockLocation1),
      "CA" -> Seq(mockLocation2)
    ),
    "EU" -> mutable.LinkedHashMap(
      "UK" -> Seq(mockLocation3),
      "DE" -> Seq(mockLocation4, mockLocation5)
    )
  )

  "An LocationHelper" should {
    "flatten a hash map of locations into a list of FlatLocations" which {
      "Returns a continent String" in {
        val subject = LocationHelper

        subject.flattenLocations(mockLocations) should contain allOf(mockFlatLocation1, mockFlatLocation2, mockFlatLocation3, mockFlatLocation4, mockFlatLocation5)
      }
    }

    "Get the continent a location id" which {
      "Returns a continent String" in {
        val subject = LocationHelper
        subject.getContinentFromLocationId(locations = mockLocations, 2) shouldBe "EU"
      }
    }

    "Reject invalid location ids" which {
      "Returns a error message" in {
        val subject = LocationHelper
        val thrownError = the[Exception] thrownBy {
          subject.getContinentFromLocationId(locations = mockLocations, 5)
        }
        thrownError.getMessage should equal("Error: Location Id doesn't exist")
      }
      }
    }
}

