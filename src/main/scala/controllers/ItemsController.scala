import main.db.{DbAdapter, DbAdapterBase}
import main.model.Item

import scala.collection.mutable.ArrayBuffer

class ItemsController(val dBAdapter: DbAdapterBase = DbAdapter) {

  def getAllItems(): ArrayBuffer[Item] = {
    dBAdapter.getItems()
  }

  def getItemById(id: Int): Item ={
    val stock = dBAdapter.getItems()
    stock(1)
  }
}