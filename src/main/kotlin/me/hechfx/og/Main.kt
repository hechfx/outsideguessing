package me.hechfx.og

fun main() {
    val internalKey = "your-key"

    val outsideGuessing = OutsideGuessing(internalKey, internalKey.hashCode())

    val message = "Hello, world!"
    outsideGuessing.hide("data.png", message, "output.png")
    val revealedMessage = outsideGuessing.reveal("output.png")

    println("revealedMessage: $revealedMessage")
}