import model.{Cart, UuidFactory}
import org.scalamock.scalatest.MockFactory
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

import java.util.UUID

class UuidFactoryTest extends AnyWordSpec with Matchers with MockFactory {

  "A UuidFactory should" should {
    "Create a UUID" in {
      val subject = UuidFactory.create()
      subject shouldBe a[UUID]
    }

  }
}