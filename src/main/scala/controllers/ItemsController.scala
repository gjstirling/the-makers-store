import main.db.{DbAdapter, DbAdapterBase}
import main.model.Item

import scala.collection.mutable.ArrayBuffer

class ItemsController(val dBAdapter: DbAdapterBase = DbAdapter) {

  def getAllItems(): ArrayBuffer[Item] = {
    dBAdapter.getItems()
  }

  def getItemById(id: Int): Item = {
    val stock = dBAdapter.getItems()
    val item = stock.filter(item => item.id == id)
    val foundItem = (item.length == 1)
    foundItem match {
      case true => item(0)
      case false => throw new Exception("Item id error")
    }
  }

  def getItemsByLocationId(id: Int): ArrayBuffer[Item] ={
    val continent = getContinentByLocationId(id)
    val items = getAllItems()
    val filteredItems = items.filter(item => {
      item.availableLocales.contains(continent)
    })
    filteredItems
  }

  def createItem(newItem: Item): Unit = {
    val stock = dBAdapter.getItems()
    val item = stock.filter(item => item.id == newItem.id)
    val itemExists = item.length > 0
    itemExists match {
      case false => {
        dBAdapter.createItem(newItem)
      }
      case _ => throw new Exception("Error: Duplicated Item id")
    }
  }

  def updateItemById(
              id: Int,
              name: Option[String] = None,
              price: Option[Double] = None,
              quantity: Option[Int] = None,
              availableLocales: Option[List[String]] = None
            )
  : Item = {
    val itemToUpdate = getItemById(id)
    val nameUpdate = name match {
      case Some(nameChange) => nameChange
      case None => itemToUpdate.name
    }
    val priceUpdate = price match {
      case Some(priceChange) => priceChange
      case None => itemToUpdate.price
    }
    val quantityUpdate = quantity match {
      case Some(quantityChange) => quantityChange
      case None => itemToUpdate.quantity
    }
    val availableLocalesUpdate = availableLocales match {
      case Some(localeChange) => localeChange
      case None => itemToUpdate.availableLocales
    }
    val itemUpdate = new Item(id, nameUpdate, priceUpdate, quantityUpdate, availableLocalesUpdate)
    DbAdapter.updateItem(id, itemUpdate)
    itemUpdate
  }

  def deleteItemById(id: Int): Unit = {
    val stock = dBAdapter.getItems()
    val item = stock.filter(item => item.id == id)
    (item.length > 0) match {
      case true => // Item deleted
      case false => throw new Exception("Error: Item id is invalid")
    }
  }

  private def getContinentByLocationId(id: Int): String ={
    dBAdapter.getLocations()
    "EU"
  }
}

