package com.logslambda.core

import com.logslambda.core.domain.ActivityByProduct
import org.apache.spark.streaming.State
import com.twitter.algebird.{HLL, HyperLogLogMonoid}

package object functions {

  def mapActivityStateFunc = (_: (String, Long), v: Option[ActivityByProduct],
                              state: State[(Long, Long, Long)]) => {

    var (purchase_count, add_to_cart_count, page_view_count) = state.getOption().getOrElse((0L, 0L, 0L))

    val newVal = v match {
      case Some(a: ActivityByProduct) => (a.purchase_count, a.add_to_cart_count, a.page_view_count)
      case None => (0L, 0L, 0L)
    }

    purchase_count += newVal._1
    add_to_cart_count += newVal._2
    page_view_count += newVal._3

    state.update((purchase_count, add_to_cart_count, page_view_count))

    val underExposed = if (purchase_count == 0) 0 else page_view_count / purchase_count

    underExposed
  }

  def mapVisitorsStateSpec = (k: (String, Long), v: Option[HLL], state: State[HLL]) => {
    val currentVisitorHLL = state.getOption().getOrElse(new HyperLogLogMonoid(12).zero)
    val newVisitorHLL = v match {
      case Some(visitorHLL) => currentVisitorHLL + visitorHLL
      case None => currentVisitorHLL
    }
    state.update(newVisitorHLL)
    val output = newVisitorHLL.approximateSize.estimate
    output
  }
}
