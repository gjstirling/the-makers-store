package controllers
import main.db.{DbAdapter, DbAdapterBase}
import main.model.Location
import scala.collection.mutable

class LocationsController(val dBAdapter: DbAdapterBase = DbAdapter) {

  def getLocationsFromContinent(continent: String): mutable.Iterable[Seq[Location]] = {
    val locations = dBAdapter.getLocations()
    val pullLocationsFromCountry = locations(continent).map((country) => country._2)
    println(s"\n ${pullLocationsFromCountry}\n")
    pullLocationsFromCountry
  }

}
