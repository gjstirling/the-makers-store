package main.services

import main.model.Location
import scala.collection.mutable

  case class FlatLocation(id: Int, name: String, region: String, continent: String)

object LocationHelper {

  def flattenLocations(locations: mutable.LinkedHashMap[String, mutable.LinkedHashMap[String, Seq[Location]]]): List[FlatLocation] ={
    locations.foldLeft(List[FlatLocation]())((outputList, hMapOfRegion) => {
      val (continent, regionMap) = hMapOfRegion

      //INPUT: LinkedHashMap[String, Seq[Location]]
      //      println(s"${continent}\n")
      //      println(s"${regionMap}\n")

      outputList ++ regionMap.foldLeft(List[FlatLocation]())((subList, hMapOfLocation)=> {
        val (region, locationSeq) = hMapOfLocation

      //        println(s"${region}\n")
      //        println(s"${locationSeq}\n")
        // OUTPUT: List[FlatLocation]
        subList ++ locationSeq.map(l => {
          FlatLocation(l.id, l.name, region, continent)
        }).toList
      })
    })
  }

    def getContinentFromLocationId(locations: mutable.LinkedHashMap[String, mutable.LinkedHashMap[String, Seq[Location]]], locationId: Int): String ={
    val flattened = flattenLocations(locations)

    flattened.find(fl => fl.id == locationId) match {
      case Some(foundLocation) => foundLocation.continent
      case None => throw new Exception("Error: Location Id doesn't exist")
    }
  }

}

// Another method of filtering a list and returning a associated Continent
//  def getContinentByLocationId(id: Int): String ={
//    val continents = dBAdapter.getLocations()
//    continents.find((continent) => continent._2.values.exists(
//      country => country.exists(
//        location => location.id == id
//      )
//    )) match {
//      case Some(result) => result._1
//      case None => throw new Exception("Error: Location id doesn't exist")
//    }
//  }

