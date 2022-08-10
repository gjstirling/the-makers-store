package controllers
import main.db.{DbAdapter, DbAdapterBase}
import main.model.Location

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

class LocationsController(val dBAdapter: DbAdapterBase = DbAdapter) {

  def getLocationsFromContinent(continent: String): mutable.Iterable[Location] = {
    val locations = dBAdapter.getLocations()
    val pullLocationsFromCountry = locations(continent).map((country) => country._2).flatten
    pullLocationsFromCountry
  }

}
