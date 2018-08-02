/*
 * Copyright (C) 2014 jsonwebtoken.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.jsonwebtoken;

import io.jsonwebtoken.io.Decoder;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoder;
import io.jsonwebtoken.io.Serializer;
import io.jsonwebtoken.security.InvalidKeyException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureAlgorithms;

import java.security.Key;
import java.security.Provider;
import java.security.SecureRandom;
import java.util.Date;
import java.util.Map;

/**
 * A builder for constructing JWTs.
 *
 * @since 0.1
 */
public interface JwtBuilder extends ClaimsMutator<JwtBuilder> {

    /**
     * Sets the JCA Provider to use during cryptographic signing or encryption operations, or {@code null} if the
     * JCA subsystem preferred provider should be used.
     *
     * @param provider the JCA Provider to use during cryptographic signing or encryption operations, or {@code null} if the
     *                 JCA subsystem preferred provider should be used.
     * @return the builder for method chaining.
     * @since JJWT_RELEASE_VERSION
     */
    JwtBuilder setProvider(Provider provider);

    /**
     * Sets the {@link SecureRandom} to use during cryptographic signing or encryption operations, or {@code null} if
     * a default {@link SecureRandom} should be used.
     *
     * @param secureRandom the {@link SecureRandom} to use during cryptographic signing or encryption operations, or
     *                     {@code null} if a default {@link SecureRandom} should be used.
     * @return the builder for method chaining.
     * @since JJWT_RELEASE_VERSION
     */
    JwtBuilder setSecureRandom(SecureRandom secureRandom);

    /**
     * Sets (and replaces) any existing header with the specified header.  If you do not want to replace the existing
     * header and only want to append to it, use the {@link #setHeaderParams(java.util.Map)} method instead.
     *
     * @param header the header to set (and potentially replace any existing header).
     * @return the builder for method chaining.
     */
    JwtBuilder setHeader(Header header); //replaces any existing header with the specified header.

    /**
     * Sets (and replaces) any existing header with the specified header.  If you do not want to replace the existing
     * header and only want to append to it, use the {@link #setHeaderParams(java.util.Map)} method instead.
     *
     * @param header the header to set (and potentially replace any existing header).
     * @return the builder for method chaining.
     */
    JwtBuilder setHeader(Map<String, Object> header);

    /**
     * Applies the specified name/value pairs to the header.  If a header does not yet exist at the time this method
     * is called, one will be created automatically before applying the name/value pairs.
     *
     * @param params the header name/value pairs to append to the header.
     * @return the builder for method chaining.
     */
    JwtBuilder setHeaderParams(Map<String, Object> params);

    //sets the specified header parameter, overwriting any previous value under the same name.

    /**
     * Applies the specified name/value pair to the header.  If a header does not yet exist at the time this method
     * is called, one will be created automatically before applying the name/value pair.
     *
     * @param name  the header parameter name
     * @param value the header parameter value
     * @return the builder for method chaining.
     */
    JwtBuilder setHeaderParam(String name, Object value);

    /**
     * Sets the JWT's payload to be a plaintext (non-JSON) string.  If you want the JWT body to be JSON, use the
     * {@link #setClaims(Claims)} or {@link #setClaims(java.util.Map)} methods instead.
     *
     * <p>The payload and claims properties are mutually exclusive - only one of the two may be used.</p>
     *
     * @param payload the plaintext (non-JSON) string that will be the body of the JWT.
     * @return the builder for method chaining.
     */
    JwtBuilder setPayload(String payload);

    /**
     * Sets the JWT payload to be a JSON Claims instance.  If you do not want the JWT body to be JSON and instead want
     * it to be a plaintext string, use the {@link #setPayload(String)} method instead.
     *
     * <p>The payload and claims properties are mutually exclusive - only one of the two may be used.</p>
     *
     * @param claims the JWT claims to be set as the JWT body.
     * @return the builder for method chaining.
     */
    JwtBuilder setClaims(Claims claims);

    /**
     * Sets the JWT payload to be a JSON Claims instance populated by the specified name/value pairs.  If you do not
     * want the JWT body to be JSON and instead want it to be a plaintext string, use the {@link #setPayload(String)}
     * method instead.
     *
     * <p>The payload* and claims* properties are mutually exclusive - only one of the two may be used.</p>
     *
     * @param claims the JWT claims to be set as the JWT body.
     * @return the builder for method chaining.
     */
    JwtBuilder setClaims(Map<String, ?> claims);

    /**
     * Adds all given name/value pairs to the JSON Claims in the payload. If a Claims instance does not yet exist at the
     * time this method is called, one will be created automatically before applying the name/value pairs.
     *
     * <p>The payload and claims properties are mutually exclusive - only one of the two may be used.</p>
     *
     * @param claims the JWT claims to be added to the JWT body.
     * @return the builder for method chaining.
     * @since 0.8
     */
    JwtBuilder addClaims(Map<String, Object> claims);

    /**
     * Sets the JWT Claims <a href="https://tools.ietf.org/html/draft-ietf-oauth-json-web-token-25#section-4.1.1">
     * <code>iss</code></a> (issuer) value.  A {@code null} value will remove the property from the Claims.
     *
     * <p>This is a convenience method.  It will first ensure a Claims instance exists as the JWT body and then set
     * the Claims {@link Claims#setIssuer(String) issuer} field with the specified value.  This allows you to write
     * code like this:</p>
     *
     * <pre>
     * String jwt = Jwts.builder().setIssuer("Joe").compact();
     * </pre>
     *
     * <p>instead of this:</p>
     * <pre>
     * Claims claims = Jwts.claims().setIssuer("Joe");
     * String jwt = Jwts.builder().setClaims(claims).compact();
     * </pre>
     * <p>if desired.</p>
     *
     * @param iss the JWT {@code iss} value or {@code null} to remove the property from the Claims map.
     * @return the builder instance for method chaining.
     * @since 0.2
     */
    @Override
    //only for better/targeted JavaDoc
    JwtBuilder setIssuer(String iss);

    /**
     * Sets the JWT Claims <a href="https://tools.ietf.org/html/draft-ietf-oauth-json-web-token-25#section-4.1.2">
     * <code>sub</code></a> (subject) value.  A {@code null} value will remove the property from the Claims.
     *
     * <p>This is a convenience method.  It will first ensure a Claims instance exists as the JWT body and then set
     * the Claims {@link Claims#setSubject(String) subject} field with the specified value.  This allows you to write
     * code like this:</p>
     *
     * <pre>
     * String jwt = Jwts.builder().setSubject("Me").compact();
     * </pre>
     *
     * <p>instead of this:</p>
     * <pre>
     * Claims claims = Jwts.claims().setSubject("Me");
     * String jwt = Jwts.builder().setClaims(claims).compact();
     * </pre>
     * <p>if desired.</p>
     *
     * @param sub the JWT {@code sub} value or {@code null} to remove the property from the Claims map.
     * @return the builder instance for method chaining.
     * @since 0.2
     */
    @Override
    //only for better/targeted JavaDoc
    JwtBuilder setSubject(String sub);

    /**
     * Sets the JWT Claims <a href="https://tools.ietf.org/html/draft-ietf-oauth-json-web-token-25#section-4.1.3">
     * <code>aud</code></a> (audience) value.  A {@code null} value will remove the property from the Claims.
     *
     * <p>This is a convenience method.  It will first ensure a Claims instance exists as the JWT body and then set
     * the Claims {@link Claims#setAudience(String) audience} field with the specified value.  This allows you to write
     * code like this:</p>
     *
     * <pre>
     * String jwt = Jwts.builder().setAudience("You").compact();
     * </pre>
     *
     * <p>instead of this:</p>
     * <pre>
     * Claims claims = Jwts.claims().setAudience("You");
     * String jwt = Jwts.builder().setClaims(claims).compact();
     * </pre>
     * <p>if desired.</p>
     *
     * @param aud the JWT {@code aud} value or {@code null} to remove the property from the Claims map.
     * @return the builder instance for method chaining.
     * @since 0.2
     */
    @Override
    //only for better/targeted JavaDoc
    JwtBuilder setAudience(String aud);

    /**
     * Sets the JWT Claims <a href="https://tools.ietf.org/html/draft-ietf-oauth-json-web-token-25#section-4.1.4">
     * <code>exp</code></a> (expiration) value.  A {@code null} value will remove the property from the Claims.
     *
     * <p>A JWT obtained after this timestamp should not be used.</p>
     *
     * <p>This is a convenience method.  It will first ensure a Claims instance exists as the JWT body and then set
     * the Claims {@link Claims#setExpiration(java.util.Date) expiration} field with the specified value.  This allows
     * you to write code like this:</p>
     *
     * <pre>
     * String jwt = Jwts.builder().setExpiration(new Date(System.currentTimeMillis() + 3600000)).compact();
     * </pre>
     *
     * <p>instead of this:</p>
     * <pre>
     * Claims claims = Jwts.claims().setExpiration(new Date(System.currentTimeMillis() + 3600000));
     * String jwt = Jwts.builder().setClaims(claims).compact();
     * </pre>
     * <p>if desired.</p>
     *
     * @param exp the JWT {@code exp} value or {@code null} to remove the property from the Claims map.
     * @return the builder instance for method chaining.
     * @since 0.2
     */
    @Override
    //only for better/targeted JavaDoc
    JwtBuilder setExpiration(Date exp);

    /**
     * Sets the JWT Claims <a href="https://tools.ietf.org/html/draft-ietf-oauth-json-web-token-25#section-4.1.5">
     * <code>nbf</code></a> (not before) value.  A {@code null} value will remove the property from the Claims.
     *
     * <p>A JWT obtained before this timestamp should not be used.</p>
     *
     * <p>This is a convenience method.  It will first ensure a Claims instance exists as the JWT body and then set
     * the Claims {@link Claims#setNotBefore(java.util.Date) notBefore} field with the specified value.  This allows
     * you to write code like this:</p>
     *
     * <pre>
     * String jwt = Jwts.builder().setNotBefore(new Date()).compact();
     * </pre>
     *
     * <p>instead of this:</p>
     * <pre>
     * Claims claims = Jwts.claims().setNotBefore(new Date());
     * String jwt = Jwts.builder().setClaims(claims).compact();
     * </pre>
     * <p>if desired.</p>
     *
     * @param nbf the JWT {@code nbf} value or {@code null} to remove the property from the Claims map.
     * @return the builder instance for method chaining.
     * @since 0.2
     */
    @Override
    //only for better/targeted JavaDoc
    JwtBuilder setNotBefore(Date nbf);

    /**
     * Sets the JWT Claims <a href="https://tools.ietf.org/html/draft-ietf-oauth-json-web-token-25#section-4.1.6">
     * <code>iat</code></a> (issued at) value.  A {@code null} value will remove the property from the Claims.
     *
     * <p>The value is the timestamp when the JWT was created.</p>
     *
     * <p>This is a convenience method.  It will first ensure a Claims instance exists as the JWT body and then set
     * the Claims {@link Claims#setIssuedAt(java.util.Date) issuedAt} field with the specified value.  This allows
     * you to write code like this:</p>
     *
     * <pre>
     * String jwt = Jwts.builder().setIssuedAt(new Date()).compact();
     * </pre>
     *
     * <p>instead of this:</p>
     * <pre>
     * Claims claims = Jwts.claims().setIssuedAt(new Date());
     * String jwt = Jwts.builder().setClaims(claims).compact();
     * </pre>
     * <p>if desired.</p>
     *
     * @param iat the JWT {@code iat} value or {@code null} to remove the property from the Claims map.
     * @return the builder instance for method chaining.
     * @since 0.2
     */
    @Override
    //only for better/targeted JavaDoc
    JwtBuilder setIssuedAt(Date iat);

    /**
     * Sets the JWT Claims <a href="https://tools.ietf.org/html/draft-ietf-oauth-json-web-token-25#section-4.1.7">
     * <code>jti</code></a> (JWT ID) value.  A {@code null} value will remove the property from the Claims.
     *
     * <p>The value is a CaSe-SenSiTiVe unique identifier for the JWT. If specified, this value MUST be assigned in a
     * manner that ensures that there is a negligible probability that the same value will be accidentally
     * assigned to a different data object.  The ID can be used to prevent the JWT from being replayed.</p>
     *
     * <p>This is a convenience method.  It will first ensure a Claims instance exists as the JWT body and then set
     * the Claims {@link Claims#setId(String) id} field with the specified value.  This allows
     * you to write code like this:</p>
     *
     * <pre>
     * String jwt = Jwts.builder().setId(UUID.randomUUID().toString()).compact();
     * </pre>
     *
     * <p>instead of this:</p>
     * <pre>
     * Claims claims = Jwts.claims().setId(UUID.randomUUID().toString());
     * String jwt = Jwts.builder().setClaims(claims).compact();
     * </pre>
     * <p>if desired.</p>
     *
     * @param jti the JWT {@code jti} (id) value or {@code null} to remove the property from the Claims map.
     * @return the builder instance for method chaining.
     * @since 0.2
     */
    @Override
    //only for better/targeted JavaDoc
    JwtBuilder setId(String jti);

    /**
     * Sets a custom JWT Claims parameter value.  A {@code null} value will remove the property from the Claims.
     *
     * <p>This is a convenience method.  It will first ensure a Claims instance exists as the JWT body and then set the
     * named property on the Claims instance using the Claims {@link Claims#put(Object, Object) put} method. This allows
     * you to write code like this:</p>
     *
     * <pre>
     * String jwt = Jwts.builder().claim("aName", "aValue").compact();
     * </pre>
     *
     * <p>instead of this:</p>
     * <pre>
     * Claims claims = Jwts.claims().put("aName", "aValue");
     * String jwt = Jwts.builder().setClaims(claims).compact();
     * </pre>
     * <p>if desired.</p>
     *
     * @param name  the JWT Claims property name
     * @param value the value to set for the specified Claims property name
     * @return the builder instance for method chaining.
     * @since 0.2
     */
    JwtBuilder claim(String name, Object value);

    /**
     * Signs the constructed JWT with the specified key using the key's
     * {@link SignatureAlgorithms#forSigningKey(Key) recommended signature algorithm}, producing a JWS. If the
     * recommended signature algorithm isn't sufficient for your needs, consider using
     * {@link #signWith(Key, io.jsonwebtoken.security.SignatureAlgorithm)} instead.
     *
     * <p>If you are looking to invoke this method with a byte array that you are confident may be used for HMAC-SHA
     * algorithms, consider using {@link Keys Keys}.{@link Keys#hmacShaKeyFor(byte[]) hmacShaKeyFor(bytes)} to
     * convert the byte array into a valid {@code Key}.</p>
     *
     * @param key the key to use for signing
     * @return the builder instance for method chaining.
     * @throws InvalidKeyException if the Key is insufficient or explicitly disallowed by the JWT specification as
     *                             described by {@link SignatureAlgorithms#forSigningKey(Key)}.
     * @see #signWith(Key, io.jsonwebtoken.security.SignatureAlgorithm)
     * @since 0.10.0
     */
    JwtBuilder signWith(Key key) throws InvalidKeyException;

    /**
     * Signs the constructed JWT using the specified algorithm with the specified key, producing a JWS.
     *
     * <h4>Deprecation Notice: Deprecated as of 0.10.0</h4>
     *
     * <p>Use {@link Keys Keys}.{@link Keys#hmacShaKeyFor(byte[]) hmacShaKeyFor(bytes)} to
     * obtain the {@code Key} and then invoke {@link #signWith(Key)} or
     * {@link #signWith(Key, io.jsonwebtoken.security.SignatureAlgorithm)}.</p>
     *
     * <p>This method will be removed in the 1.0 release.</p>
     *
     * @param alg       the JWS algorithm to use to digitally sign the JWT, thereby producing a JWS.
     * @param secretKey the algorithm-specific signing key to use to digitally sign the JWT.
     * @return the builder for method chaining.
     * @throws InvalidKeyException if the Key is insufficient for the specified algorithm or explicitly disallowed by
     *                             the JWT specification.
     * @deprecated as of 0.10.0: use {@link Keys Keys}.{@link Keys#hmacShaKeyFor(byte[]) hmacShaKeyFor(bytes)} to
     * obtain the {@code Key} and then invoke {@link #signWith(Key)} or
     * {@link #signWith(Key, io.jsonwebtoken.security.SignatureAlgorithm)}.
     * This method will be removed in the 1.0 release.
     */
    @Deprecated
    JwtBuilder signWith(SignatureAlgorithm alg, byte[] secretKey) throws InvalidKeyException;

    /**
     * Signs the constructed JWT using the specified algorithm with the specified key, producing a JWS.
     *
     * <p>This is a convenience method: the string argument is first BASE64-decoded to a byte array and this resulting
     * byte array is used to invoke {@link #signWith(SignatureAlgorithm, byte[])}.</p>
     *
     * <h4>Deprecation Notice: Deprecated as of 0.10.0, will be removed in the 1.0 release.</h4>
     *
     * <p>This method has been deprecated because the {@code key} argument for this method can be confusing: keys for
     * cryptographic operations are always binary (byte arrays), and many people were confused as to how bytes were
     * obtained from the String argument.</p>
     *
     * <p>This method always expected a String argument that was effectively the same as the result of the following
     * (pseudocode):</p>
     *
     * <p>{@code String base64EncodedSecretKey = base64Encode(secretKeyBytes);}</p>
     *
     * <p>However, a non-trivial number of JJWT users were confused by the method signature and attempted to
     * use raw password strings as the key argument - for example {@code with(HS256, myPassword)} - which is
     * almost always incorrect for cryptographic hashes and can produce erroneous or insecure results.</p>
     *
     * <p>See this
     * <a href="https://stackoverflow.com/questions/40252903/static-secret-as-byte-key-or-string/40274325#40274325">
     * StackOverflow answer</a> explaining why raw (non-base64-encoded) strings are almost always incorrect for
     * signature operations.</p>
     *
     * <p>To perform the correct logic with base64EncodedSecretKey strings with JJWT >= 0.10.0, you may do this:
     * <pre><code>
     * byte[] keyBytes = {@link Decoders Decoders}.{@link Decoders#BASE64 BASE64}.{@link Decoder#decode(Object) decode(base64EncodedSecretKey)};
     * Key key = {@link Keys Keys}.{@link Keys#hmacShaKeyFor(byte[]) hmacShaKeyFor(keyBytes)};
     * jwtBuilder.with(key); //or {@link #signWith(Key, SignatureAlgorithm)}
     * </code></pre>
     * </p>
     *
     * <p>This method will be removed in the 1.0 release.</p>
     *
     * @param alg                    the JWS algorithm to use to digitally sign the JWT, thereby producing a JWS.
     * @param base64EncodedSecretKey the BASE64-encoded algorithm-specific signing key to use to digitally sign the
     *                               JWT.
     * @return the builder for method chaining.
     * @throws InvalidKeyException if the Key is insufficient or explicitly disallowed by the JWT specification as
     *                             described by {@link SignatureAlgorithm#forSigningKey(Key)}.
     * @deprecated as of 0.10.0: use {@link #signWith(Key)} or {@link #signWith(Key, SignatureAlgorithm)} instead.  This
     * method will be removed in the 1.0 release.
     */
    @Deprecated
    JwtBuilder signWith(SignatureAlgorithm alg, String base64EncodedSecretKey) throws InvalidKeyException;

    /**
     * Signs the constructed JWT using the specified algorithm with the specified key, producing a JWS.
     *
     * <p>It is typically recommended to call the {@link #signWith(Key)} instead for simplicity.
     * However, this method can be useful if the recommended algorithm heuristics do not meet your needs or if
     * you want explicit control over the signature algorithm used with the specified key.</p>
     *
     * @param alg the JWS algorithm to use to digitally sign the JWT, thereby producing a JWS.
     * @param key the algorithm-specific signing key to use to digitally sign the JWT.
     * @return the builder for method chaining.
     * @throws InvalidKeyException if the Key is insufficient or explicitly disallowed by the JWT specification for
     *                             the specified algorithm.
     * @see #signWith(Key)
     * @deprecated since 0.10.0: use {@link #signWith(Key, SignatureAlgorithm)} instead.  This method will be removed
     * in the 1.0 release.
     */
    @Deprecated
    JwtBuilder signWith(SignatureAlgorithm alg, Key key) throws InvalidKeyException;

    /**
     * <h3>Deprecation Notice</h3>
     * <p><b>This has been deprecated since JJWT_RELEASE_VERSION.  Use
     * {@link #signWith(Key, io.jsonwebtoken.security.SignatureAlgorithm)} instead.</b>.  Standard JWA algorithms
     * are represented as instances of this new interface in the {@link io.jsonwebtoken.security.SignatureAlgorithms}
     * enum class.</p>
     *
     * Signs the constructed JWT with the specified key using the specified algorithm, producing a JWS.
     *
     * <p>It is typically recommended to call the {@link #signWith(Key)} instead for simplicity.
     * However, this method can be useful if the recommended algorithm heuristics do not meet your needs or if
     * you want explicit control over the signature algorithm used with the specified key.</p>
     *
     * @param key the signing key to use to digitally sign the JWT.
     * @param alg the JWS algorithm to use with the key to digitally sign the JWT, thereby producing a JWS.
     * @return the builder for method chaining.
     * @throws InvalidKeyException if the Key is insufficient or explicitly disallowed by the JWT specification for
     *                             the specified algorithm.
     * @see #signWith(Key)
     * @since 0.10.0
     * @deprecated since JJWT_RELEASE_VERSION to use a more the more flexible {@link io.jsonwebtoken.security.SignatureAlgorithm}.
     */
    @Deprecated
    JwtBuilder signWith(Key key, SignatureAlgorithm alg) throws InvalidKeyException;

    /**
     * Signs the constructed JWT with the specified key using the specified algorithm, producing a JWS.
     *
     * <p>It is typically recommended to call the {@link #signWith(Key)} instead for simplicity.
     * However, this method can be useful if the recommended algorithm heuristics do not meet your needs or if
     * you want explicit control over the signature algorithm used with the specified key.</p>
     *
     * @param key the signing key to use to digitally sign the JWT.
     * @param alg the JWS algorithm to use with the key to digitally sign the JWT, thereby producing a JWS.
     * @return the builder for method chaining.
     * @throws InvalidKeyException if the Key is insufficient or explicitly disallowed by the JWT specification for
     *                             the specified algorithm.
     * @see #signWith(Key)
     * @see SignatureAlgorithms#forSigningKey(Key)
     * @since JJWT_RELEASE_VERSION
     */
    JwtBuilder signWith(Key key, io.jsonwebtoken.security.SignatureAlgorithm alg) throws InvalidKeyException;

    /**
     * Compresses the JWT body using the specified {@link CompressionCodec}.
     *
     * <p>If your compact JWTs are large, and you want to reduce their total size during network transmission, this
     * can be useful.  For example, when embedding JWTs  in URLs, some browsers may not support URLs longer than a
     * certain length.  Using compression can help ensure the compact JWT fits within that length.  However, NOTE:</p>
     *
     * <h3>Compatibility Warning</h3>
     *
     * <p>The JWT family of specifications defines compression only for JWE (JSON Web Encryption)
     * tokens.  Even so, JJWT will also support compression for JWS tokens as well if you choose to use it.
     * However, be aware that <b>if you use compression when creating a JWS token, other libraries may not be able to
     * parse that JWS token</b>.  When using compression for JWS tokens, be sure that all parties accessing the
     * JWS token support compression for JWS.</p>
     *
     * <p>Compression when creating JWE tokens however should be universally accepted for any
     * library that supports JWE.</p>
     *
     * @param codec implementation of the {@link CompressionCodec} to be used.
     * @return the builder for method chaining.
     * @see io.jsonwebtoken.CompressionCodecs
     * @since 0.6.0
     */
    JwtBuilder compressWith(CompressionCodec codec);

    /**
     * Perform Base64Url encoding with the specified Encoder.
     *
     * <p>JJWT uses a spec-compliant encoder that works on all supported JDK versions, but you may call this method
     * to specify a different encoder if you desire.</p>
     *
     * @param base64UrlEncoder the encoder to use when Base64Url-encoding
     * @return the builder for method chaining.
     * @since 0.10.0
     */
    JwtBuilder base64UrlEncodeWith(Encoder<byte[], String> base64UrlEncoder);

    /**
     * Performs object-to-JSON serialization with the specified Serializer.  This is used by the builder to convert
     * JWT/JWS/JWT headers and claims Maps to JSON strings as required by the JWT specification.
     *
     * <p>If this method is not called, JJWT will use whatever serializer it can find at runtime, checking for the
     * presence of well-known implementations such Jackson, Gson, and org.json.  If one of these is not found
     * in the runtime classpath, an exception will be thrown when the {@link #compact()} method is invoked.</p>
     *
     * @param serializer the serializer to use when converting Map objects to JSON strings.
     * @return the builder for method chaining.
     * @since 0.10.0
     */
    JwtBuilder serializeToJsonWith(Serializer<Map<String, ?>> serializer);

    /**
     * Actually builds the JWT and serializes it to a compact, URL-safe string according to the
     * <a href="https://tools.ietf.org/html/draft-ietf-oauth-json-web-token-25#section-7">JWT Compact Serialization</a>
     * rules.
     *
     * @return A compact URL-safe JWT string.
     */
    String compact();
}
