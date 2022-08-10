package model
import main.db.{DbAdapter, DbAdapterBase}
import main.model.Item
import services.UuidGenerator
import scala.collection.mutable.ArrayBuffer

class Cart (val uuid: String = UuidGenerator.create(),
            val dBAdapter: DbAdapterBase = DbAdapter,
            val items: ArrayBuffer[Item] = ArrayBuffer()) {

    def addItem(item: Item): Unit ={
        items.append(item)
    }

    def clearCart(): Unit ={
        items.clear()
    }

}


