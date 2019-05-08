/* =========================================================================================
 * Copyright © 2013-2016 the kamon project <http://kamon.io/>
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions
 * and limitations under the License.
 * =========================================================================================
 */


val kamonCore       = "io.kamon" %% "kamon-core"         % "2.0.0-M4"
val kamonTestkit    = "io.kamon" %% "kamon-testkit"      % "2.0.0-M4"
val kamonScala      = "io.kamon" %% "kamon-scala-future" % "2.0.0-4174b904116483573d943b22811a262501116c62"
val kamonExecutors  = "io.kamon" %% "kamon-executors"    % "2.0.0-83b17a1a775aa3860432bd1b67788b53b2fdd017"
val kamonInstrument = "io.kamon" %% "kamon-instrumentation-common" % "2.0.0-3d734de88d883ea580919995b58c12b9755de92d"

val kanelaAgent     =  "io.kamon" % "kanela-agent" % "1.0.0-M2" changing()

val `akka-2.5` = "2.5.13"

def akkaDependency(name: String, version: String) = {
  "com.typesafe.akka" %% s"akka-$name" % version
}

lazy val `kamon-akka` = (project in file("."))
  .settings(noPublishing: _*)
  .aggregate(kamonAkka25)

lazy val kamonAkka25 = Project("kamon-akka-25", file("kamon-akka-2.5.x"))
  .enablePlugins(JavaAgent)
  .settings(instrumentationSettings)
  .settings(
    bintrayPackage := "kamon-akka",
    moduleName := "kamon-akka-2.5",
    scalacOptions += "-target:jvm-1.8",
    resolvers += Resolver.bintrayRepo("kamon-io", "snapshots"),
    resolvers += Resolver.mavenLocal,
    libraryDependencies ++=
      compileScope(kamonCore, kamonInstrument, kamonScala, kamonExecutors) ++
      providedScope(akkaDependency("actor", `akka-2.5`), kanelaAgent) ++
      optionalScope(logbackClassic) ++
      testScope(scalatest, kamonTestkit, akkaDependency("testkit", `akka-2.5`), akkaDependency("slf4j", `akka-2.5`), logbackClassic))

