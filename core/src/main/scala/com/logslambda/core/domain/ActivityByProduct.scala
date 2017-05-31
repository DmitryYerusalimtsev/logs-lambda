package com.logslambda.core.domain

case class ActivityByProduct(product: String,
                             timestamp_hour: Long,
                             purchase_count: Long,
                             add_to_cart_count: Long,
                             page_view_count: Long)
