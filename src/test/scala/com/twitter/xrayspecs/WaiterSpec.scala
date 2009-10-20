package com.twitter.xrayspecs

import org.specs.mock.{ClassMocker, JMocker}
import org.specs.Specification

object WaiterSpec extends Specification with Waiter {
  "Waiter" should {
    "be patient" in {
      @volatile var finished = false
      new Thread("slow worker") {
        override def run() {
          Thread.sleep(100)
          finished = true
        }
      }.start()
      waitUntil { finished == true }
    }

    "give up eventually" in {
      var finished = false
      waitUntil(2, 100) { finished == true } must throwA[Exception]
    }

    "notice an exception" in {
      var count = 2
      def timebomb() {
        count -= 1
        if (count == 0) {
          throw new Exception("kaboom!")
        }
      }

      waitUntilThrown(classOf[Exception]) { timebomb() }
    }
  }
}
