package model
import java.util.UUID

class Cart (val uuidFactory: FactoryBase[UUID] = UuidFactory) {

    val uuid = uuidFactory.create().toString


}


