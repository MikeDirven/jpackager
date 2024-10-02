package nl.mdsystems.util

import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import java.io.File

object Serializer {
    private val jsonSerializer = Json {
        serializersModule = SerializersModule {
            contextual(File::class, FileSerializer)
        }
    }

    operator fun invoke() : Json {
        return jsonSerializer
    }
}