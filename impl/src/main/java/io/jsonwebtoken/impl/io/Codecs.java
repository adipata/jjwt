package io.jsonwebtoken.impl.io;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;

public class Codecs {

    public static final CodecConverter<byte[], String> BASE64 = new CodecConverter<>(Encoders.BASE64, Decoders.BASE64);
    public static final CodecConverter<byte[], String> BASE64URL = new CodecConverter<>(Encoders.BASE64URL, Decoders.BASE64URL);
}
