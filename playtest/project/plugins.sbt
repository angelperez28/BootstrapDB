logLevel := Level.Warn

resolvers += "Typesafe repository" at "https://repo.typesafe.com/typesafe/releases/"

addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.4.2")

addSbtPlugin("com.typesafe.sbt" % "sbt-jshint" % "1.0.2")
addSbtPlugin("com.typesafe.sbt" % "sbt-less" % "1.0.6")
//addSbtPlugin("com.typesafe.sbt" % "sbt-rjs" % "1.0.7")
addSbtPlugin("com.typesafe.sbt" % "sbt-digest" % "1.1.0")
addSbtPlugin("com.typesafe.sbt" % "sbt-gzip" % "1.0.0")
//addSbtPlugin("com.typesafe.sbt" % "sbt-uglify" % "1.0.3")
addSbtPlugin("net.ground5hark.sbt" % "sbt-concat" % "0.1.8")
addSbtPlugin("net.ground5hark.sbt" % "sbt-css-compress" % "0.1.3")
