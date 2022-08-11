package model
import main.db.{DbAdapter, DbAdapterBase}
import main.model.Item
import services.UuidGenerator

class Cart (val uuid: String = UuidGenerator.create(),
            val dBAdapter: DbAdapterBase = DbAdapter,
            var order: Order = new Order()) {

    //TO ADD:
    //    assuming we still have enough quantity in stock
    //      and it's available in the city we're in
    def addItem(item: Item): Unit = {

        order.items.append(item)
    }

    //    Cart also allows us to clear its contents entirely
    def clearCart(): Unit = {
        order.items.clear()
    }

    // remove a particular item
        def removeItemById(itemId: Int): Unit = {
            val filteredCart = order.items.filter(item => item.id != itemId)
            (filteredCart.length == order.items.length) match {
                case true => {
                    throw new Exception("Error: ItemId not in cart")
                }
                case _ => {order.items = filteredCart}
            }
        }

    //    We can change the quantity of an item already in our cart,
    //    so long as the new quantity is in stock
}


