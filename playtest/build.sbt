import com.typesafe.sbt.jse.JsEngineImport.JsEngineKeys
import com.typesafe.sbt.jshint.Import.JshintKeys
import com.typesafe.sbt.less.Import.LessKeys
import com.typesafe.sbt.web.Import.WebKeys
//import com.typesafe.sbt.rjs.Import.RjsKeys
import com.typesafe.sbt.web.js.JS

name := """play-angular-requirejs-sbadmin"""
organization := "mmizutani"
version := "1.0"
scalaVersion in ThisBuild := "2.11.7"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"

libraryDependencies ++= Seq(
  filters,
  cache,
  "org.webjars.bower" % "requirejs" % "2.1.20",
  "org.webjars.bower" % "underscore" % "1.8.3",
  "org.webjars.bower" % "jquery" % "2.1.4",
  "org.webjars.bower" % "bootstrap" % "3.3.5" exclude("org.webjars.bower", "jquery"),
  "org.webjars.bower" % "angular" % "1.4.3" exclude("org.webjars.bower", "jquery"),
  "org.webjars.bower" % "angular-sanitize" % "1.4.3" exclude("org.webjars.bower", "jquery"),
  "org.webjars.bower" % "angular-animate" % "1.4.3" exclude("org.webjars.bower", "angularjs"),
  "org.webjars.bower" % "angular-ui-bootstrap-bower" % "0.13.2" exclude("org.webjars.bower", "angularjs"),
  "org.webjars.bower" % "angular-ui-router" % "0.2.15" exclude("org.webjars.bower", "angularjs"),
  "org.webjars.bower" % "angular-block-ui" % "0.2.0" exclude("org.webjars.bower", "angularjs"),
  "org.webjars.bower" % "font-awesome" % "4.4.0",
  "org.webjars.bower" % "json3" % "3.3.2",
  // for Heroku https://devcenter.heroku.com/articles/getting-started-with-scala#push-local-changes
  "org.jscience" % "jscience" % "4.3.1",
  specs2 % Test
)

// Scala Compiler Options
scalacOptions in ThisBuild ++= Seq(
  "-target:jvm-1.7",
  "-encoding", "UTF-8",
  "-deprecation", // warning and location for usages of deprecated APIs
  "-feature", // warning and location for usages of features that should be imported explicitly
  "-unchecked", // additional warnings where generated code depends on assumptions
  "-Xlint", // recommended additional warnings
  "-Ywarn-adapted-args", // Warn if an argument list is modified to match the receiver
  "-Ywarn-value-discard", // Warn when non-Unit expression results are unused
  "-Ywarn-inaccessible",
  "-Ywarn-dead-code"
)

//
// sbt-web configuration
// https://github.com/sbt/sbt-web
//

// Configure the steps of the asset pipeline (used in stage and dist tasks)
// rjs = RequireJS, uglifies, shrinks to one file, replaces WebJars with CDN
// digest = Adds hash to filename
// gzip = Zips all assets, Asset controller serves them automatically when client accepts them
// Uncomment this if you want to run all the asset pipeline stages for production
//pipelineStages := Seq(rjs, cssCompress, /*concat,*/ /*uglify,*/ digest, gzip)
//pipelineStages := Seq(rjs)

// RequireJS with sbt-rjs (https://github.com/sbt/sbt-rjs#sbt-rjs)
// ~~~
// The r.js optimizer won't find jsRoutes so we must tell it to ignore it
// Override RequireJS path mappings: module_id -> (build_path -> production_path)
//RjsKeys.paths += ("jsRoutes" -> ("/jsroutes" -> "empty:"))
//RjsKeys.generateSourceMaps := false
//val checkCdn = taskKey[Unit]("Check the CDN")
//checkCdn := println(RjsKeys.paths.value)
//RjsKeys.mainModule := "main"
// This must be turned off when deployed to heroku
//JsEngineKeys.engineType := JsEngineKeys.EngineType.Node

includeFilter in (Assets, LessKeys.less) := "*.less"
excludeFilter in (Assets, LessKeys.less) := "_*.less"

excludeFilter in cssCompress := GlobFilter("lib/*.css")

// Exclude third-party plugins in app/assets/javascripts/plugin from jshint targets
excludeFilter in (Assets, JshintKeys.jshint) := new FileFilter{
  def accept(f: File) = ".*/plugins/.*".r.pattern.matcher(f.getAbsolutePath).matches
}

//excludeFilter in uglify := GlobFilter("*.min.js")

// Asset hashing with sbt-digest (https://github.com/sbt/sbt-digest)
// ~~~
// md5 | sha1
//DigestKeys.algorithms := "md5"
//includeFilter in digest := "..."
//excludeFilter in digest := "..."

// HTTP compression with sbt-gzip (https://github.com/sbt/sbt-gzip)
// ~~~
// includeFilter in GzipKeys.compress := "*.html" || "*.css" || "*.js"
// excludeFilter in GzipKeys.compress := "..."

// JavaScript linting with sbt-jshint (https://github.com/sbt/sbt-jshint)
// ~~~
// JshintKeys.config := ".jshintrc"


// Disable generation of scaladoc in dist task
publishArtifact in (Compile, packageDoc) := false
publishArtifact in packageDoc := false
sources in (Compile,doc) := Seq.empty

routesGenerator := InjectedRoutesGenerator