package com.sontran.sample.core.platform

import com.sontran.sample.core.exception.Failure
import com.sontran.sample.core.functional.Either
import retrofit2.Call

open class NetworkRequest {

    fun <T, R> request(call: Call<T>, transform: (T) -> R, default: T): Either<Failure, R> {
        return try {
            val response = call.execute()
            when (response.isSuccessful) {
                true -> Either.Right(transform((response.body() ?: default)))
                false -> Either.Left(Failure.ServerError)
            }
        } catch (exception: Throwable) {
            Either.Left(Failure.ServerError)
        }
    }

}