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
