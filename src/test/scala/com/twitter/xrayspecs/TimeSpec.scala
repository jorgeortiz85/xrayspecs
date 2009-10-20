package com.twitter.xrayspecs

import com.twitter.xrayspecs.TimeConversions._
import org.specs.mock.{ClassMocker, JMocker}
import org.specs.Specification

object TimeSpec extends Specification {
  "Time" should {
    "convert to/from int/long" in {
      Time.now = Time(1234567890L.millis)
      val now = Time.now
      now.inMillis mustEqual 1234567890L
      now.inSeconds mustEqual 1234567
    }

    "freeze" in {
      Time.freeze()
      val now = Time.now
      Thread.sleep(2)
      Time.now mustEqual now
      Time.now.inMillis mustEqual now.inMillis
      Time.now.inSeconds mustEqual now.inSeconds
    }
  }
}
