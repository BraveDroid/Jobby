package com.bravedroid.jobby.domain.utils
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn

object FlowExt {
    fun <T1, T2, T3, R> combineOnThreeFlows(
        dispatcher: CoroutineDispatcher,
        flow: Flow<T1>,
        flow2: Flow<T2>,
        flow3: Flow<T3>,
        transform: suspend (T1, T2, T3) -> R,
    ): Flow<R> = combine(
        flow.flowOn(dispatcher),
        flow2.flowOn(dispatcher),
        flow3.flowOn(dispatcher),
        transform
    )

    fun <T1, T2,  R> combineOn(
        dispatcher: CoroutineDispatcher,
        flow: Flow<T1>,
        flow2: Flow<T2>,
        transform: suspend (T1, T2) -> R,
    ): Flow<R> = combine(
        flow.flowOn(dispatcher),
        flow2.flowOn(dispatcher),
        transform
    )
}
