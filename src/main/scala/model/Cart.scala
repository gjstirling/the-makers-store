package model
import factories.{FactoryBase, UuidFactory}
import main.db.{DbAdapter, DbAdapterBase}
import main.model.Item

import java.util.UUID
import scala.collection.mutable.ArrayBuffer

class Cart (val uuidFactory: FactoryBase[UUID] = UuidFactory,
            val dBAdapter: DbAdapterBase = DbAdapter,
            val items: ArrayBuffer[Item] = ArrayBuffer()) {

    val uuid = uuidFactory.create().toString


    def addItem(item: Item): Unit ={
        items.append(item)
    }

    def clearCart(): Unit ={
        items.clear()
    }

}



