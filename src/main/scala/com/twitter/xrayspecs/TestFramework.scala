package com.twitter.xrayspecs

import java.io.File
import net.lag.configgy.Configgy
import org.specs.Specification
import org.specs.runner.{SpecsFramework, TestInterfaceRunner}
import org.scalatools.testing.{EventHandler, Logger}


class XraySpecsFramework extends SpecsFramework {
  override def testRunner(classLoader: ClassLoader, loggers: Array[Logger]) =
    new XraySpecsTestRunner(classLoader, loggers)
}

class XraySpecsTestRunner(loader: ClassLoader, val loggers: Array[Logger]) extends TestInterfaceRunner(loader, loggers) {
  override def run(specification: Option[Specification], handler: EventHandler) = {
    var configFilename = System.getProperty("basedir") + "/config/" + System.getProperty("stage", "test") + ".conf"
    if (new File(configFilename).exists()) {
      Configgy.configure(configFilename)
    }
    super.run(specification, handler)
  }
}
