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
import org.specs.matcher.Matcher
import TimeConversions._


trait Eventually { self: Specification =>
  def eventually[T](retries: Int, sleep: Duration)(nested: Matcher[T]): Matcher[T] = new Matcher[T]() {
    def apply(a: => T) = retry(retries, sleep, a)

    def retry(retries: Int, sleep: Duration, a: => T): (Boolean, String, String) = {
      val result = nested(a)
      if (result.success || retries == 1) {
        result
      } else {
        Thread.sleep(sleep.inMillis)
        retry(retries - 1, sleep, a)
      }
    }
  }

  def eventually[T](nested: Matcher[T]): Matcher[T] = eventually(40, 100.milliseconds)(nested)
}
