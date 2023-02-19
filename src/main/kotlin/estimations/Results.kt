package estimations

internal fun estimateSuccess(vararg param: Double) = Result.success(doubleArrayOf(*param))
internal fun <T> estimateFailure(message: String) = Result.failure<T>(IllegalArgumentException(message))
internal fun allNonnegative(data: DoubleArray) = data.all { it >= 0 }