package utils

import android.app.Application
import org.simpleframework.xml.core.Persister
import java.io.InputStream

class XMLReader(private val context: Application) {
    class XMLReaderException(message: String) : Exception(message)

    fun <T> read(cls: Class<T>, resId: Int): T {
        return try {
            val inputStream: InputStream = context.resources.openRawResource(resId)
            val serializer = Persister()
            serializer.read(cls, inputStream)
        } catch (e: Exception) {
            throw XMLReaderException("Exception when trying to read XML from raw resource: ${e.message}")
        }
    }
}