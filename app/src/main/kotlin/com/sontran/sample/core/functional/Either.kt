/**
 * Copyright (C) 2019 Son Tran Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sontran.sample.core.functional

/**
 * Represents a value of one of two possible types (a disjoint union).
 * Instances of [Either] are either an instance of [Left] or [Right].
 * FP Convention dictates that [Left] is used for "failure"
 * and [Right] is used for "success".
 *
 * @see Left
 * @see Right
 */
sealed class Either<out L, out R> {
    /** * Represents the left side of [Either] class which by convention is a "Failure". */
    data class Left<out L>(val a: L) : Either<L, Nothing>()

    /** * Represents the right side of [Either] class which by convention is a "Success". */
    data class Right<out R>(val b: R) : Either<Nothing, R>()

    /**
     * Returns true if this is a Right, false otherwise.
     * @see Right
     */
    val isRight get() = this is Right<R>

    /**
     * Returns true if this is a Left, false otherwise.
     * @see Left
     */
    val isLeft get() = this is Left<L>

    /**
     * Creates a Left type.
     * @see Left
     */
    fun <L> left(a: L) = Left(a)


    /**
     * Creates a Left type.
     * @see Right
     */
    fun <R> right(b: R) = Right(b)

    /**
     * Applies fnL if this is a Left or fnR if this is a Right.
     * @see Left
     * @see Right
     */
    fun fold(fnL: (L) -> Any, fnR: (R) -> Any): Any =
            when (this) {
                is Left -> fnL(a)
                is Right -> fnR(b)
            }
}