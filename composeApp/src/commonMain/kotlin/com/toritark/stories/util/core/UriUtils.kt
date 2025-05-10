package com.toritark.stories.util.core

// Helper function to convert a single hex digit character to its integer value
private fun hexDigitToInt(char: Char): Int {
    val digit = char.digitToInt(16)
    if (digit == -1) {
        throw IllegalArgumentException("Invalid hex digit: $char")
    }
    return digit
}

// Helper function to convert a byte to its two-digit uppercase hex string
private fun byteToHex(byte: Byte): String {
    val hexChars = "0123456789ABCDEF"
    val v = byte.toInt() and 0xFF // Ensure positive value
    return "${hexChars[v ushr 4]}${hexChars[v and 0x0F]}"
}

// Helper function to convert two hex digit characters to a byte
private fun hexToByte(hex1: Char, hex2: Char): Byte {
    val d1 = hexDigitToInt(hex1)
    val d2 = hexDigitToInt(hex2)
    return ((d1 shl 4) + d2).toByte()
}

/**
 * URI-encodes a string according to RFC 3986 for use in URI components.
 * Unreserved characters (A-Z, a-z, 0-9, -, ., _, ~) are left as is.
 * Spaces are encoded as %20. Other characters (reserved or non-ASCII)
 * are encoded as %HH based on their UTF-8 byte representation.
 *
 * @param input The string to encode.
 * @return The URI-encoded string.
 */
fun uriEncode(input: String): String {
    val result = StringBuilder()
    for (char in input) {
        when {
            // Unreserved characters (RFC 3986)
            char.isLetterOrDigit() || char in "-._~" -> {
                result.append(char)
            }
            // Space character (encoded as %20)
            char == ' ' -> {
                result.append("%20")
            }
            // Other characters (reserved or non-ASCII)
            else -> {
                val bytes = char.toString().encodeToByteArray()
                for (byte in bytes) {
                    result.append('%')
                    result.append(byteToHex(byte))
                }
            }
        }
    }
    return result.toString()
}

/**
 * URI-decodes a string encoded using percent-encoding (%HH).
 * Decodes %HH sequences back to bytes and interprets the byte sequence as UTF-8.
 * Handles malformed sequences by treating the '%' and subsequent characters literally.
 * Does NOT decode '+' to space, as this is specific to application/x-www-form-urlencoded.
 *
 * @param input The string to decode.
 * @return The URI-decoded string.
 */
fun uriDecode(input: String): String {
    val bytes = mutableListOf<Byte>()
    var i = 0
    while (i < input.length) {
        when (input[i]) {
            '%' -> {
                if (i + 2 < input.length) {
                    try {
                        val byte = hexToByte(input[i + 1], input[i + 2])
                        bytes.add(byte)
                        i += 3
                    } catch (e: IllegalArgumentException) {
                        // Invalid hex digits, treat '%' and subsequent chars literally
                        bytes.addAll(input[i].toString().encodeToByteArray().asList())
                        i++
                    }
                } else {
                    // Malformed sequence (ends with %), treat '%' literally
                    bytes.addAll(input[i].toString().encodeToByteArray().asList())
                    i++
                }
            }

            else -> {
                // Treat character literally, encoding it to bytes (handles multi-byte chars)
                bytes.addAll(input[i].toString().encodeToByteArray().asList())
                i++
            }
        }
    }
    return bytes.toByteArray().decodeToString()
}
