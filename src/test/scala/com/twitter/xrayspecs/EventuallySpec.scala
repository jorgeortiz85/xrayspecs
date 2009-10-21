package com.twitter.xrayspecs

import org.specs.mock.{ClassMocker, JMocker}
import org.specs.Specification
import TimeConversions._

object EventuallySpec extends Specification with Eventually {
  "Eventually" should {
    "be patient" in {
      @volatile var finished = false
      new Thread("slow worker") {
        override def run() {
          Thread.sleep(100)
          finished = true
        }
      }.start()

      finished must eventually(beTrue)
    }

    "give up eventually" in {
      var finished = false
      var stackTrace: List[StackTraceElement] = Nil
      (try {
        finished must eventually(2, 10.milliseconds)(beTrue)
      } catch {
        case e: Exception =>
          stackTrace = e.getStackTrace.toList
          throw e
      }) must throwA[Exception]
    }

    "notice an exception" in {
      var count = 2
      def timebomb() {
        count -= 1
        if (count == 0) {
          throw new Exception("kaboom!")
        }
      }

      timebomb() must eventually(throwA[Exception])
    }
  }
}
