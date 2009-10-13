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

import org.specs.Specification

trait Waiter { self: Specification =>
  def waitUntil(outcome: => Boolean): Boolean = waitUntil(40, 100)(outcome)

  def waitUntil(retries: Int, sleep: Long)(outcome: => Boolean): Boolean = {
    if (retries == 0) {
      throw new Exception("Condition never occurred.")
    } else {
      outcome || {
        Thread.sleep(sleep)
        waitUntil(retries - 1, sleep)(outcome)
      }
    }
  }

  def waitUntilThrown[T <: Throwable](ex: Class[T], f: => Unit, wantThrown: Boolean): Boolean = {
    waitUntil {
      try {
        f
        wantThrown
      } catch {
        case e: Throwable =>
          if (ex.isAssignableFrom(e.getClass)) {
            !wantThrown
          } else {
            throw e
          }
      }
    }
  }

  def waitUntilThrown[T <: Throwable](ex: Class[T])(f: => Unit): Unit = {
    if (!waitUntilThrown(ex, f, false)) {
      throw new Exception("Exception never thrown.")
    }
  }

  def waitUntilNotThrown[T <: Throwable](ex: Class[T])(f: => Unit): Unit = {
    if (!waitUntilThrown(ex, f, true)) {
      throw new Exception("Exception never stopped being thrown.")
    }
  }
}
