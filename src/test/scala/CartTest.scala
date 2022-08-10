import model.Cart
import org.scalamock.scalatest.MockFactory
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class CartTest extends AnyWordSpec with Matchers with MockFactory {

  "A Cart should" should {
    "Create a UUID" in {
      val cart = new Cart()
      println(s"UUID: ${cart.uuid}")
    }

  }
}
