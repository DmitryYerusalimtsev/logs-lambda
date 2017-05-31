package com.logslambda.core.domain

case class Activity(timestamp_hour: Long,
                    referrer: String,
                    action: String,
                    prevPage: String,
                    page: String,
                    visitor: String,
                    product: String,
                    inputProps: Map[String, String] = Map()
                   )