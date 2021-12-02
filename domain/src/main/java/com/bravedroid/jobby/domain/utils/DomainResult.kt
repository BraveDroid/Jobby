package com.bravedroid.jobby.domain.utils

import com.bravedroid.jobby.domain.entities.ErrorEntity
import kotlinx.coroutines.flow.flowOf

sealed class DomainResult<out R> {

    data class Success<out T>(val data: T) : DomainResult<T>()
    data class Error(val errorEntity: ErrorEntity) : DomainResult<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[errorResult=$errorEntity]"
        }
    }

    companion object {
        val DomainResult<*>.isSucceeded
            get() = this is DomainResult.Success && data != null

        /* inline fun <R> runCatching(block: () -> R): Result<R> {
             return try {
                 Success(block())
             } catch (e: Throwable) {
                 Error(e)
             }
         }
 */
        fun <T> areAllErrors(vararg domainResults: DomainResult<T>) =
            domainResults.asList().areAllErrors()

        private fun <T> List<DomainResult<T>>.areAllErrors() = all { it is DomainResult.Error }

        /*
        * Returns the encapsulated result of the given transform function applied to the encapsulated value if this instance
        * represents success or the original encapsulated Throwable exception if it is failure.
        */
        inline fun <R, T> DomainResult<T>.mapToResultSuccessOrKeepSameResultError(transform: (value: T) -> R): DomainResult<R> =
            when (this) {
                is DomainResult.Success -> {
                    tryMapping { transform(data) }
                }
                is DomainResult.Error -> this
            }

        @PublishedApi
        internal inline fun <R, T> DomainResult<T>.tryMapping(transform: (value: T) -> R): DomainResult<R> {
            return when (this) {
                is DomainResult.Success -> {
                    try {
                        DomainResult.Success(transform(data))
                    } catch (e: Throwable) {
                        DomainResult.Error(ErrorEntity.Unknown)
                    }
                }
                is DomainResult.Error -> this
            }
        }

        fun <T> DomainResult<T>.asFlow() = flowOf(this)


        fun <T> List<T>.toResultSuccess(): DomainResult.Success<List<T>> {
            return DomainResult.Success(this)
        }

        fun <T> T.toResultSuccess(): DomainResult.Success<T> {
            return DomainResult.Success(this)
        }

        fun ErrorEntity.toResultError(): DomainResult.Error = DomainResult.Error(this)

        /* fun <T : Throwable> T.toResultError(): Result.Error {
             return Result.Error(this)
         }
 */

//        fun <T : Throwable> T.toResultErrorUnknown(): DomainResult.Error {
//            return DomainResult.Error.Unknown(this)
//        }

//        fun <T> DomainResult<T>.getData(): T {
//            if (this is DomainResult.Success)
//                return this.data
//            else
//                throw ((this as DomainResult.Error).throwable)
//        }

        fun <T> DomainResult<T>.getDataOrNull(): T? =
            if (this is DomainResult.Success)
                this.data
            else
                null

        fun <T> DomainResult<T>.getErrorOrNull(): ErrorEntity? =
            if (this is DomainResult.Error)
                this.errorEntity
            else
                null

    }
}
