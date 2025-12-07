package dev.crayson.smartgrades.database.codec

import org.bson.BsonReader
import org.bson.BsonWriter
import org.bson.codecs.Codec
import org.bson.codecs.DecoderContext
import org.bson.codecs.EncoderContext
import org.bson.codecs.configuration.CodecRegistry
import java.util.UUID

class MongoDBUUIDCodec(
    registry: CodecRegistry,
) : Codec<UUID> {
    private val stringCodec: Codec<String> = registry[String::class.java]

    override fun getEncoderClass(): Class<UUID> = UUID::class.java

    override fun encode(
        writer: BsonWriter,
        value: UUID,
        context: EncoderContext,
    ) {
        stringCodec.encode(writer, value.toString(), context)
    }

    override fun decode(
        reader: BsonReader,
        context: DecoderContext,
    ): UUID = UUID.fromString(stringCodec.decode(reader, context))
}
