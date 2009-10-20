/*
 * Copyright 2009 Twitter, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain
 * a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.twitter.xrayspecs


object TimeConversions {
  class RichAnyVal(wrapped: AnyVal) {
    def toLong = {
      wrapped match {
        case i: Int => i.toLong
        case n: Long => n
      }
    }

    def toInt = {
      wrapped match {
        case i: Int => i
        case n: Long => n.toInt
      }
    }

    def seconds = new Duration(toLong * 1000)
    def second = seconds
    def millis = new Duration(toLong)
  }

  implicit def anyValToRichAnyVal(v: AnyVal) = new RichAnyVal(v)
}


/**
 * Use `Time.now` in your app instead of `System.currentTimeMillis`, and
 * unit tests will be able to adjust the current time to verify timeouts
 * and other time-dependent behavior, without calling `sleep`.
 */
object Time {
  import TimeConversions._

  private var fn: () => Time = null

  reset()

  /**
   * Freeze the clock. Time will not pass until reset.
   */
  def freeze() {
    Time.now = new Time(System.currentTimeMillis)
  }

  def now: Time = fn()
  def never: Time = Time(0.seconds)

  def now_=(at: Time) {
    fn = () => at
  }

  def reset() {
    fn = { () => new Time(System.currentTimeMillis) }
  }

  def apply(at: Duration) = new Time(at.inMillis)

  def advance(delta: Duration) {
    now = now + delta
  }
}


class Duration(at: Long) {
  def inSeconds = (at / 1000L).toInt
  def inMillis = at

  def +(delta: Duration) = new Duration(at + delta.inMillis)
  def -(delta: Duration) = new Duration(at - delta.inMillis)

  def fromNow = Time(Time.now + this)
  def ago = Time(Time.now - this)

  override def toString = inSeconds.toString

  override def equals(other: Any) = {
    other match {
      case other: Time =>
        inSeconds == other.inSeconds
      case _ =>
        false
    }
  }
}


class Time(at: Long) extends Duration(at) {
  override def +(delta: Duration) = new Time(at + delta.inMillis)
  override def -(delta: Duration) = new Time(at - delta.inMillis)
}
