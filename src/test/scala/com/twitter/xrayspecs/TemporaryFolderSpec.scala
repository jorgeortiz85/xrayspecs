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

import java.io.File
import org.specs.mock.{ClassMocker, JMocker}
import org.specs.Specification
import TimeConversions._

object TemporaryFolderSpec extends Specification with TemporaryFolder {
  "TemporaryFolder" should {
    "make a folder" in {
      withTemporaryFolder {
        folderName must not(beNull)
      }
    }

    "erase the folder after the test" in {
      var tempFolder: String = null
      withTemporaryFolder {
        folderName must not(beNull)
        tempFolder = folderName
        new File(tempFolder).exists mustBe true
      }
      new File(tempFolder).exists mustBe false
    }
  }
}
