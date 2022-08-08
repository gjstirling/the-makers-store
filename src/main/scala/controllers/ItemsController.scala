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

  def deleteItemById(id: Int): Unit = {
    val stock = dBAdapter.getItems()
    val item = stock.filter(item => item.id == id)
    (item.length > 0) match {
      case true => // Item deleted
      case false => throw new Exception("Error: Item id is invalid")
    }
  }
}

