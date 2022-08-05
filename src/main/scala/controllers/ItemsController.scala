import main.db.{DbAdapter, DbAdapterBase}
import main.model.Item

import scala.collection.mutable.ArrayBuffer

class ItemsController(val dBAdapter: DbAdapterBase = DbAdapter) {

  def getAllItems(): ArrayBuffer[Item] = {
    dBAdapter.getItems()
  }

  def getItemById(id: Int): Item ={
    val stock = dBAdapter.getItems()
    val item = stock.filter(item => item.id == id)
    val foundItem = (item.length == 1)
    foundItem match {
      case true => item(0)
      case false => throw new Exception("Item id error")
    }
  }
}