package com.logslambda.producer.stream

import com.logslambda.producer.config.Settings

import scala.io.Source

object DataProvider {
  private val productsContent = getClass.getResourceAsStream(Settings.Stream.productsFile)
  private val referrersContent = getClass.getResourceAsStream(Settings.Stream.referrersFile)

  val products = Source.fromInputStream(productsContent).getLines().toArray
  val referrers = Source.fromInputStream(referrersContent).getLines().toArray
  val visitors = (0 to Settings.Stream.visitors).map("Visitor-" + _)
  val pages = (0 to Settings.Stream.pages).map("Page-" + _)
}
