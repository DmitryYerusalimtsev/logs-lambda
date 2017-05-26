package com.logslambda.stream

import com.logslambda.config.Settings

import scala.io.Source

object DataProvider {
  private val productsContent = getClass.getResourceAsStream(Settings.productsFile)
  private val referrersContent = getClass.getResourceAsStream(Settings.referrersFile)

  val products = Source.fromInputStream(productsContent).getLines().toArray
  val referrers = Source.fromInputStream(referrersContent).getLines().toArray
  val visitors = (0 to Settings.visitors).map("Visitor-" + _)
  val pages = (0 to Settings.pages).map("Page-" + _)
}
