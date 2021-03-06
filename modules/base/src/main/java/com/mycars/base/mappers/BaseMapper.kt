package com.mycars.base.mappers

/**
 * Base class for mappers, avoid use gson directly to have more control mapping
 */
interface BaseMapper<in R, T> {

    /**
     * Get a [T] object from [R] element
     * return [T] element if can map each key
     * throw [Throwable]
     */
    @Throws(Throwable::class)
    fun getFromElement(element: R): T
}
