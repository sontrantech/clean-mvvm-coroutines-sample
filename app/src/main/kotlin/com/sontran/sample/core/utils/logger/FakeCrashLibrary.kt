package com.sontran.sample.core.utils.logger

/** Not a real crash reporting library  */
object FakeCrashLibrary {
    fun log(priority: Int, tag: String?, message: String?) {
        // add log entry to circular buffer.
    }

    fun logWarning(t: Throwable?) {
        // report non-fatal warning.
    }

    fun logError(t: Throwable?) {
        // report non-fatal error.
    }
}