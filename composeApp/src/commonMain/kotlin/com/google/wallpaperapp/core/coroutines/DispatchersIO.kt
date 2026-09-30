package kotlinx.coroutines

val Dispatchers.IO: CoroutineDispatcher
    get() = Dispatchers.Default

val IO: CoroutineDispatcher
    get() = Dispatchers.Default
