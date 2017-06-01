package com.logslambda.core.domain

case class VisitorsByProduct(product: String,
                             timestamp_hour: Long,
                             unique_visitors: Long)
