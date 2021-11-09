package com.bravedroid.jobby.domain.utils

import kotlinx.coroutines.flow.flowOf

sealed class Result<out R> {

    data class Success<out T>(val data: T) : Result<T>()
    sealed class Error(val throwable: Throwable) : Result<Nothing>(){
        data class OffLine (val t: Throwable): Error(t)
        data class BadParsing (val t: Throwable): Error(t)
        data class Unknown(val t: Throwable) : Error(t)
    }

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$throwable]"
        }
    }

    companion object {
        val Result<*>.isSucceeded
            get() = this is Result.Success && data != null

       /* inline fun <R> runCatching(block: () -> R): Result<R> {
            return try {
                Success(block())
            } catch (e: Throwable) {
                Error(e)
            }
        }
*/
        fun <T> areAllErrors(vararg results: Result<T>) = results.asList().areAllErrors()

        private fun <T> List<Result<T>>.areAllErrors() = all { it is Result.Error }

        /*
        * Returns the encapsulated result of the given transform function applied to the encapsulated value if this instance
        * represents success or the original encapsulated Throwable exception if it is failure.
        */
        inline fun <R, T> Result<T>.mapIfSuccess(transform: (value: T) -> R): Result<R> =
            when (this) {
                is Result.Success -> {
                    tryMapping { transform(data) }
                }
                is Result.Error -> this
            }

        inline fun <R, T> Result<T>.tryMapping(transform: (value: T) -> R): Result<R> {
            return when (this) {
                is Result.Success -> {
                    try {
                        Result.Success(transform(data))
                    } catch (e: Throwable) {
                        Result.Error.Unknown(e)
                    }
                }
                is Result.Error -> this
            }
        }

        fun <T> Result<T>.asFlow() = flowOf(this)


        fun <T> List<T>.toResultSuccess(): Result.Success<List<T>> {
            return Result.Success(this)
        }

        fun <T> T.toResultSuccess(): Result.Success<T> {
            return Result.Success(this)
        }

       /* fun <T : Throwable> T.toResultError(): Result.Error {
            return Result.Error(this)
        }
*/

        fun <T : Throwable> T.toResultErrorUnknown(): Result.Error {
            return Result.Error.Unknown(this)
        }

        fun <T> Result<T>.getData(): T {
            if (this is Result.Success)
                return this.data
            else
                throw ((this as Result.Error).throwable)
        }

        fun <T> Result<T>.getDataOrNull(): T? =
            if (this is Result.Success)
                this.data
            else
                null

    }
}
