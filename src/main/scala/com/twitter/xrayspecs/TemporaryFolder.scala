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

import _root_.java.io.File
import _root_.java.util.UUID
import _root_.org.specs.Specification


trait TemporaryFolder { self: Specification =>
  private val _folderName = new ThreadLocal[File]

  /**
   * Recursively delete a folder. Should be built in; bad java.
   */
  private def deleteFolder(folder: File): Unit = {
    val files = folder.listFiles
    if (files != null) {
      for (f <- files) {
        if (f.isDirectory) {
          deleteFolder(f)
        } else {
          f.delete
        }
      }
    }
    folder.delete
  }

  def makeTemporaryFolder(): File = {
    val tempFolder = System.getProperty("java.io.tmpdir")
    var folder: File = null
    do {
      folder = new File(tempFolder, "scala-specs-" + UUID.randomUUID.toString)
    } while (! folder.mkdir)
    _folderName.set(folder)
    folder
  }

  def deleteTemporaryFolder() {
    deleteFolder(_folderName.get)
  }

  def withTemporaryFolder[T](f: => T): T = {
    makeTemporaryFolder()
    try {
      f
    } finally {
      deleteTemporaryFolder()
    }
  }

  def folderName = { _folderName.get.getPath }

  def canonicalFolderName = { _folderName.get.getCanonicalPath }
}
