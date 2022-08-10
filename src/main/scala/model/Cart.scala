package model
import main.db.{DbAdapter, DbAdapterBase}
import main.model.Item
import services.UuidGenerator
import scala.collection.mutable.ArrayBuffer

class Cart (val uuid: String = UuidGenerator.create(),
            val dBAdapter: DbAdapterBase = DbAdapter,
            var items: ArrayBuffer[Item] = ArrayBuffer()) {


    //    A Cart allows us to add an item to our cart,
    //TO ADD:
    //    assuming we still have enough quantity in stock
    //      and it's available in the city we're in
    def addItem(item: Item): Unit = {
        items.append(item)
    }

    //    Cart also allows us to clear its contents entirely,
    def clearCart(): Unit = {
        items.clear()
    }

    //    or remove a particular item
        def removeItemById(itemId: Int): Unit ={
            val itemsInCart = items.length
            items = items.filter(item => item.id != itemId )
            if (itemsInCart == items.length){
                throw new Exception("Error: ItemId not in cart")
            }
        }


    //    We can change the quantity of an item already in our cart,
    //    so long as the new quantity is in stock
    // def updateQuantityOfItem(itemId: Int){}


//    We should have two functions onPaymentSuccess and onPaymentFailed.
//    A failed payment should clear the cart,
//    while a successful payment should update our inventory
//    to subtract the recently sold stock.

}


