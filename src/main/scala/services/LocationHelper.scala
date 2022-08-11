package main.services

import com.sun.tools.javac.file.Locations
import main.model.Location
import scala.collection.mutable

  case class FlatLocation(id: Int, name: String, region: String, continent: String)

object LocationHelper {

  def flattenLocations(locations: mutable.LinkedHashMap[String, mutable.LinkedHashMap[String, Seq[Location]]]): List[FlatLocation] ={
    val test = locations.foldLeft(List[FlatLocation]())((outputList, hMapOfRegion) => {
      val (continent, regionMap) = hMapOfRegion

      //INPUT: LinkedHashMap[String, Seq[Location]]
      //OUTPUT: List[Location]
//      println(s"${continent}\n")
//      println(s"${regionMap}\n")

      outputList ++ regionMap.foldLeft(List[FlatLocation]())((subList, hMapOfLocation)=> {
        val (region, locationSeq) = hMapOfLocation

//        println(s"${region}\n")
//        println(s"${locationSeq}\n")

        subList ++ locationSeq.map(l => {
          FlatLocation(l.id, l.name, region, continent)
        }).toList
      })
    })
    println(test)
    List()
  }


//  def getContinentFromLocationId(locations: mutable.LinkedHashMap[String, mutable.LinkedHashMap[String, Seq[Location]]], locationId: Int): String ={
//    ""
//  }

}

