package utils

import android.util.Log

/**
 * Класс для логирования
 */
class CustomLogger {
    val DEBUG_TAG = "SDBG"
    val ERROR_TAG = "SERR"

    /**
     * Метод для создания отладочного лога
     * @param message
     */
    fun logDebug(message: String) {
        Log.d(DEBUG_TAG, message)
    }

    /**
     * Метод для создания лога ошибки
     * @param message
     */
    fun logError(message: String) {
        Log.e(ERROR_TAG, message)
    }
}
