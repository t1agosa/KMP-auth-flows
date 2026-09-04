package com.tiago.kmpauthflows.domain.util

// out T: covariante porque Result solo PRODUCE T, nunca lo recibe como parámetro.
sealed interface Result<out T> {
    data class Success<T>(val data: T) : Result<T>
    data class Error(val exception: Throwable) : Result<Nothing>
}