package eu.slickbot.vremedo.extension

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

suspend fun <T, R> Iterable<T>.asyncMap(
    context: CoroutineContext = Dispatchers.Default,
    transform: suspend CoroutineScope.(T) -> R,
): List<R> {
    return withContext(context) { map { async { transform(it) } }.awaitAll() }
}

suspend fun <T> Iterable<T>.asyncDistinct(
    context: CoroutineContext = Dispatchers.Default,
): List<T> {
    return withContext(context) { distinct() }
}
