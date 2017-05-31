package com.logslambda.producer.stream

import java.io.FileWriter

import com.logslambda.producer.config.Settings
import org.apache.commons.io.FileUtils

import scala.util.Random

object LogProducer extends App {

  var rnd = new Random()

  val incrementTimeEvery = rnd.nextInt(Settings.records - 1) + 1
  var timestamp = System.currentTimeMillis()
  var adjustedTimestamp = timestamp

  for (_ <- 1 to Settings.numberOfFiles) {
    generateFile()

    val outputFile = FileUtils.getFile(s"${Settings.destPath}data_$timestamp")
    println(s"Moving produced data to $outputFile")
    FileUtils.moveFile(FileUtils.getFile(Settings.filePath), outputFile)
    val sleeping = 5000
    println(s"Sleeping for $sleeping")
  }

  def generateFile(): Unit = {
    val fw = new FileWriter(Settings.filePath, true)
    for (i <- 1 to Settings.records) {
      adjustedTimestamp = adjustedTimestamp + ((System.currentTimeMillis() - timestamp) * Settings.timeMultiplier)
      timestamp = System.currentTimeMillis()

      val action = getAction(i)
      val referrer = DataProvider.referrers(rnd.nextInt(DataProvider.referrers.length - 1))
      val prevPage = getPrevPage(referrer)
      val visitor = DataProvider.visitors(rnd.nextInt(DataProvider.visitors.length - 1))
      val page = DataProvider.pages(rnd.nextInt(DataProvider.pages.length - 1))
      val product = DataProvider.products(rnd.nextInt(DataProvider.products.length - 1))

      val line = s"$adjustedTimestamp\t$referrer\t$action\t$prevPage\t$visitor\t$page\t$product\n"
      fw write line

      sleep(i)
    }
    fw.close()
  }

  def getAction(iter: Int): String = {
    val action = iter % (rnd.nextInt(200) + 1) match {
      case 0 => "purchase"
      case 1 => "add_to_cart"
      case _ => "page_view"
    }
    action
  }

  def getPrevPage(referr: String): String = {
    val prevPage = referr match {
      case "Internal" => DataProvider.pages(rnd.nextInt(DataProvider.pages.length - 1))
      case _ => ""
    }
    prevPage
  }

  def sleep(iter: Int) = {
    if (iter % incrementTimeEvery == 0) {
      println(s"Sent $iter messages!")
      val sleeping = rnd.nextInt(incrementTimeEvery * 60)
      println(s"Sleeping for $sleeping ms")
      Thread sleep sleeping
    }
  }
}
