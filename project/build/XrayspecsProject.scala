import sbt._

class XrayspecsProject(info: ProjectInfo) extends DefaultProject(info) {
  val specs = "org.scala-tools.testing" %% "specs" % "1.6.5"
  val configgy = "net.lag" %% "configgy" % "1.5.5"
  val testinterface = "org.scala-tools.testing" % "test-interface" % "0.5" 
}
