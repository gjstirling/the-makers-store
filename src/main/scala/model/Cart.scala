package model
import controllers.ItemsController
import main.model.Item
import services.UuidGenerator

import scala.collection.mutable.ArrayBuffer

class Cart (val uuid: String = UuidGenerator.create(),
            val shopLocation: Int,
            val itemsController: ItemsController = new ItemsController,
            var order: Order = new Order()
           ){

    def addItem(item: Item): Unit = {
        val stock = itemsController.getItemsByLocationId(shopLocation)
        val checkStock = stock.filter(stockItem => item.id == stockItem.id)
        if (checkStock == ArrayBuffer()){
            throw new Exception("Error: Item not availiable at your location")
        }
        else if (checkStock(0).quantity < item.quantity){
            throw new Exception("Error: Not enough stock of item")
        }
        else {
            order.items.append(item)
        }
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

    def onPaymentFailure(): Unit ={
        clearCart()
    }

    def onPaymentSuccess(): ArrayBuffer[Item] = {
        val stock = itemsController.getItemsByLocationId(shopLocation)
        val itemsBought = order.items
        val newItems = stock.map(item => {
            val findItem = itemsBought.filter(orderItem => item.id == orderItem.id)
            findItem(0).quantity
            val updatedQuantity = Option(item.quantity - findItem(0).quantity)
            val result = itemsController.updateItemById(item.id, quantity = updatedQuantity)
            println("result", result)
            result
        })
        newItems
    }

//    val stockTransform = stock.map( item => {
//        Map.apply("id" -> item.id, "quantity" -> item.quantity)
//    })
//    println(stockTransform)
//    val itemsTransform = itemsBought.map( item => {
//        Map.apply("id" -> item.id, "quantity" -> item.quantity)
//    })
//    println(itemsTransform)
}



