package com.ngbj.browse.util;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by mac on 14/12/18.
 */
public class EncodingUtils {
    public static final String ISO_8859_1 = "ISO-8859-1";
    public static final String US_ASCII = "US-ASCII";
    public static final String UTF_16 = "UTF-16";
    public static final String UTF_16BE = "UTF-16BE";
    public static final String UTF_16LE = "UTF-16LE";
    public static final String UTF_8 = "UTF-8";

    /**
     * Encodes the given string into a sequence of bytes using the ISO-8859-1
     * charset, storing the result into a new byte array.
     *
     * @param string the String to encode
     * @return encoded bytes
     * @throws IllegalStateException Thrown when the charset is missing, which should be never
     *                               according the the Java specification.
     * @see <a
     * href="http://java.sun.com/j2se/1.4.2/docs/api/java/nio/charset/Charset.html">Standard
     * charsets</a>
     * @see #getBytesUnchecked(String, String)
     */
    public static byte[] getBytesIso8859_1(String string) {
        return EncodingUtils.getBytesUnchecked(string, EncodingUtils.ISO_8859_1);
    }

    /**
     * Encodes the given string into a sequence of bytes using the US-ASCII
     * charset, storing the result into a new byte array.
     *
     * @param string the String to encode
     * @return encoded bytes
     * @throws IllegalStateException Thrown when the charset is missing, which should be never
     *                               according the the Java specification.
     * @see <a
     * href="http://java.sun.com/j2se/1.4.2/docs/api/java/nio/charset/Charset.html">Standard
     * charsets</a>
     * @see #getBytesUnchecked(String, String)
     */
    public static byte[] getBytesUsAscii(String string) {
        return EncodingUtils.getBytesUnchecked(string, EncodingUtils.US_ASCII);
    }

    /**
     * Encodes the given string into a sequence of bytes using the UTF-16
     * charset, storing the result into a new byte array.
     *
     * @param string the String to encode
     * @return encoded bytes
     * @throws IllegalStateException Thrown when the charset is missing, which should be never
     *                               according the the Java specification.
     * @see <a
     * href="http://java.sun.com/j2se/1.4.2/docs/api/java/nio/charset/Charset.html">Standard
     * charsets</a>
     * @see #getBytesUnchecked(String, String)
     */
    public static byte[] getBytesUtf16(String string) {
        return EncodingUtils.getBytesUnchecked(string, EncodingUtils.UTF_16);
    }

    /**
     * Encodes the given string into a sequence of bytes using the UTF-16BE
     * charset, storing the result into a new byte array.
     *
     * @param string the String to encode
     * @return encoded bytes
     * @throws IllegalStateException Thrown when the charset is missing, which should be never
     *                               according the the Java specification.
     * @see <a
     * href="http://java.sun.com/j2se/1.4.2/docs/api/java/nio/charset/Charset.html">Standard
     * charsets</a>
     * @see #getBytesUnchecked(String, String)
     */
    public static byte[] getBytesUtf16Be(String string) {
        return EncodingUtils.getBytesUnchecked(string, EncodingUtils.UTF_16BE);
    }

    /**
     * Encodes the given string into a sequence of bytes using the UTF-16LE
     * charset, storing the result into a new byte array.
     *
     * @param string the String to encode
     * @return encoded bytes
     * @throws IllegalStateException Thrown when the charset is missing, which should be never
     *                               according the the Java specification.
     * @see <a
     * href="http://java.sun.com/j2se/1.4.2/docs/api/java/nio/charset/Charset.html">Standard
     * charsets</a>
     * @see #getBytesUnchecked(String, String)
     */
    public static byte[] getBytesUtf16Le(String string) {
        return EncodingUtils.getBytesUnchecked(string, EncodingUtils.UTF_16LE);
    }

    /**
     * Encodes the given string into a sequence of bytes using the UTF-8
     * charset, storing the result into a new byte array.
     *
     * @param string the String to encode
     * @return encoded bytes
     * @throws IllegalStateException Thrown when the charset is missing, which should be never
     *                               according the the Java specification.
     * @see <a
     * href="http://java.sun.com/j2se/1.4.2/docs/api/java/nio/charset/Charset.html">Standard
     * charsets</a>
     * @see #getBytesUnchecked(String, String)
     */
    public static byte[] getBytesUtf8(String string) {
        return EncodingUtils.getBytesUnchecked(string, EncodingUtils.UTF_8);
    }

    /**
     * Encodes the given string into a sequence of bytes using the named
     * charset, storing the result into a new byte array.
     * <p>
     * This method catches {@link UnsupportedEncodingException} and rethrows it
     * as {@link IllegalStateException}, which should never happen for a
     * required charset name. Use this method when the encoding is required to
     * be in the JRE.
     * </p>
     *
     * @param string      the String to encode
     * @param charsetName The name of a required {@link java.nio.charset.Charset}
     * @return encoded bytes
     * @throws IllegalStateException Thrown when a {@link UnsupportedEncodingException} is caught,
     *                               which should never happen for a required charset name.
     * @see String#getBytes(String)
     */
    public static byte[] getBytesUnchecked(String string, String charsetName) {
        if (string == null) {
            return null;
        }
        try {
            return string.getBytes(charsetName);
        } catch (UnsupportedEncodingException e) {
            throw EncodingUtils.newIllegalStateException(charsetName, e);
        }
    }

    private static IllegalStateException newIllegalStateException(
            String charsetName, UnsupportedEncodingException e) {
        return new IllegalStateException(charsetName + ": " + e);
    }

    /**
     * Constructs a new <code>String</code> by decoding the specified array of
     * bytes using the given charset.
     * <p>
     * This method catches {@link UnsupportedEncodingException} and re-throws it
     * as {@link IllegalStateException}, which should never happen for a
     * required charset name. Use this method when the encoding is required to
     * be in the JRE.
     * </p>
     *
     * @param bytes       The bytes to be decoded into characters
     * @param charsetName The name of a required {@link java.nio.charset.Charset}
     * @return A new <code>String</code> decoded from the specified array of
     * bytes using the given charset.
     * @throws IllegalStateException Thrown when a {@link UnsupportedEncodingException} is caught,
     * @see String#String(byte[], String)
     */
    public static String newString(byte[] bytes, String charsetName) {
        if (bytes == null) {
            return null;
        }
        try {
            return new String(bytes, charsetName);
        } catch (UnsupportedEncodingException e) {
            throw EncodingUtils.newIllegalStateException(charsetName, e);
        }
    }

    /**
     * Constructs a new <code>String</code> by decoding the specified array of
     * bytes using the ISO-8859-1 charset.
     *
     * @param bytes The bytes to be decoded into characters
     * @return A new <code>String</code> decoded from the specified array of
     * bytes using the given charset.
     * @throws IllegalStateException Thrown when a {@link UnsupportedEncodingException} is caught,
     *                               which should never happen since the charset is required.
     */
    public static String newStringIso8859_1(byte[] bytes) {
        return EncodingUtils.newString(bytes, EncodingUtils.ISO_8859_1);
    }

    /**
     * Constructs a new <code>String</code> by decoding the specified array of
     * bytes using the US-ASCII charset.
     *
     * @param bytes The bytes to be decoded into characters
     * @return A new <code>String</code> decoded from the specified array of
     * bytes using the given charset.
     * @throws IllegalStateException Thrown when a {@link UnsupportedEncodingException} is caught,
     *                               which should never happen since the charset is required.
     */
    public static String newStringUsAscii(byte[] bytes) {
        return EncodingUtils.newString(bytes, EncodingUtils.US_ASCII);
    }

    /**
     * Constructs a new <code>String</code> by decoding the specified array of
     * bytes using the UTF-16 charset.
     *
     * @param bytes The bytes to be decoded into characters
     * @return A new <code>String</code> decoded from the specified array of
     * bytes using the given charset.
     * @throws IllegalStateException Thrown when a {@link UnsupportedEncodingException} is caught,
     *                               which should never happen since the charset is required.
     */
    public static String newStringUtf16(byte[] bytes) {
        return EncodingUtils.newString(bytes, EncodingUtils.UTF_16);
    }

    /**
     * Constructs a new <code>String</code> by decoding the specified array of
     * bytes using the UTF-16BE charset.
     *
     * @param bytes The bytes to be decoded into characters
     * @return A new <code>String</code> decoded from the specified array of
     * bytes using the given charset.
     * @throws IllegalStateException Thrown when a {@link UnsupportedEncodingException} is caught,
     *                               which should never happen since the charset is required.
     */
    public static String newStringUtf16Be(byte[] bytes) {
        return EncodingUtils.newString(bytes, EncodingUtils.UTF_16BE);
    }

    /**
     * Constructs a new <code>String</code> by decoding the specified array of
     * bytes using the UTF-16LE charset.
     *
     * @param bytes The bytes to be decoded into characters
     * @return A new <code>String</code> decoded from the specified array of
     * bytes using the given charset.
     * @throws IllegalStateException Thrown when a {@link UnsupportedEncodingException} is caught,
     *                               which should never happen since the charset is required.
     */
    public static String newStringUtf16Le(byte[] bytes) {
        return EncodingUtils.newString(bytes, EncodingUtils.UTF_16LE);
    }

    /**
     * Constructs a new <code>String</code> by decoding the specified array of
     * bytes using the UTF-8 charset.
     *
     * @param bytes The bytes to be decoded into characters
     * @return A new <code>String</code> decoded from the specified array of
     * bytes using the given charset.
     * @throws IllegalStateException Thrown when a {@link UnsupportedEncodingException} is caught,
     *                               which should never happen since the charset is required.
     */
    public static String newStringUtf8(byte[] bytes) {
        return EncodingUtils.newString(bytes, EncodingUtils.UTF_8);
    }


    /**
     * 过滤字符串中的表情字符
     *
     * @param content 需要过滤的内容
     * @return 返回不包含表情的字符
     */
    public static String filterEmi(String content) {
        Pattern emoji = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]", Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
        Matcher emojiMatcher = emoji.matcher(content);
        if (emojiMatcher.find()) {
            return emojiMatcher.replaceAll("");
        }
        return content;
    }
}
