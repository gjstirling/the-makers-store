package model
import controllers.ItemsController
import main.model.Item
import services.UuidGenerator


class Cart (val uuid: String = UuidGenerator.create(),
            val itemsController: ItemsController = new ItemsController,
            var order: Order = new Order()
           ){

    //TO ADD:
    //    assuming we still have enough quantity in stock
    //      and it's available in the city we're in
    def addItem(item: Item): Unit = {
        val stock = itemsController.getAllItems()
        val checkStock = stock.filter(stockItem => item.id == stockItem.id)
        if (checkStock(0).quantity < item.quantity){
            throw new Exception("Error: Not enough stock of item")
        }
        else {
            order.items.append(item)
        }
        // filter stock so that only item that matches id exists
        // confirm quantity of stock is greater than or equal to quantity being added
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



