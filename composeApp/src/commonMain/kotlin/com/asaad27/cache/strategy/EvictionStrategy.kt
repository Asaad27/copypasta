package com.asaad27.cache.strategy

interface EvictionStrategy<K, V> {
    fun evict(cache: MutableMap<K, V>)
}