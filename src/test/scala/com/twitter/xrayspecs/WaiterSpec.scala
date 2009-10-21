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
      var stackTrace: List[StackTraceElement] = Nil
      (try {
        waitUntil(2, 100) { finished == true }
      } catch {
        case e: Exception =>
          stackTrace = e.getStackTrace.toList
          throw e
      }) must throwA[Exception]

//      stackTrace.filter { _.toString contains "Waiter.scala" }.size mustEqual 1
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
