package me.hechfx.og

import java.io.File
import javax.imageio.ImageIO
import kotlin.experimental.xor

class OutsideGuessing(
    val key: String,
    var offset: Int
) {
    fun hide(imagePath: String, message: String, outputImagePath: String) {
        val encryptedMessage = encryptMessage(message)

        val image = ImageIO.read(File(imagePath))
        val width = image.width
        val height = image.height

        val messageBytes = (encryptedMessage + '\u0000').toByteArray()
        val keyBytes = key.toByteArray()
        var keyIndex = 0
        var byteIndex = 0
        var bitIndex = 0

        loop@ for (y in 0 until height) {
            for (x in 0 until width) {
                if (byteIndex >= messageBytes.size) break@loop

                val rgb = image.getRGB(x, y)
                val r = (rgb shr 16) and 0xFF
                val g = (rgb shr 8) and 0xFF
                val b = rgb and 0xFF

                val encryptedByte = messageBytes[byteIndex] xor keyBytes[keyIndex]
                val messageBit = (encryptedByte.toInt() shr (7 - bitIndex)) and 0x01
                val newR = (r and 0xFE) or messageBit

                bitIndex++
                if (bitIndex == 8) {
                    bitIndex = 0
                    byteIndex++
                    keyIndex = (keyIndex + 1) % keyBytes.size
                }

                val newRgb = (newR shl 16) or (g shl 8) or b
                image.setRGB(x, y, newRgb)
            }
        }

        ImageIO.write(image, "png", File(outputImagePath))
    }

    fun reveal(imagePath: String): String {
        val image = ImageIO.read(File(imagePath))
        val width = image.width
        val height = image.height

        val keyBytes = key.toByteArray()
        val messageBytes = mutableListOf<Byte>()
        var currentByte = 0
        var bitIndex = 0
        var keyIndex = 0

        loop@ for (y in 0 until height) {
            for (x in 0 until width) {
                val rgb = image.getRGB(x, y)
                val r = (rgb shr 16) and 0xFF

                currentByte = (currentByte shl 1) or (r and 0x01)

                bitIndex++
                if (bitIndex == 8) {
                    val decryptedByte = (currentByte xor keyBytes[keyIndex].toInt())
                    messageBytes.add(decryptedByte.toByte())
                    if (decryptedByte.toByte() == 0.toByte()) {
                        break@loop
                    }
                    currentByte = 0
                    bitIndex = 0
                    keyIndex = (keyIndex + 1) % keyBytes.size
                }
            }
        }

        return decryptMessage(String(messageBytes.toByteArray()).trimEnd('\u0000'))
    }

    private fun decryptMessage(message: String) = message.map {
        it - offset
    }.joinToString("")

    private fun encryptMessage(message: String) = message.map {
        it + offset
    }.joinToString("")
}