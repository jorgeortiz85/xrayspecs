package com.twitter.xrayspecs

import com.twitter.xrayspecs.TimeConversions._
import org.specs.mock.{ClassMocker, JMocker}
import org.specs.Specification

object TimeSpec extends Specification {
  "Duration" should {
    "compare to other durations" in {
      val minute = 1.minute
      val two = 2.minute
      val otherMinute = 60.seconds
      (minute == otherMinute) mustBe true
      (minute == two) mustBe false
      (minute != two) mustBe true
      (minute != otherMinute) mustBe false
      (minute > otherMinute) mustBe false
      (two > minute) mustBe true
      (two >= minute) mustBe true
      (minute <= two) mustBe true
      (minute < two) mustBe true
      (minute > two) mustBe false
      (minute >= two) mustBe false
      (minute >= otherMinute) mustBe true
    }
  }

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
