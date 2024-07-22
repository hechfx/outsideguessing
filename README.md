# Outside Guessing

Outside Guessing is a Kotlin-based application designed for hiding and revealing messages within images through a simple steganography technique (OutGuess-based).

## Features

- **Hide Messages**: Embed secret messages into images without altering their visible appearance.
- **Reveal Messages**: Extract hidden messages from images, assuming the correct key is provided.
- **Encryption**: Messages are encrypted with a simple character shift based on a user-provided key, enhancing the security of the hidden message.

## Getting Started

### Prerequisites

- JDK 21 or higher
- Kotlin 1.9.23
- Gradle (Kotlin DSL)

### Setup

1. Clone the repository to your local machine.
2. Open the project in an IDE that supports Gradle projects (e.g., IntelliJ IDEA).
3. Ensure the JDK is correctly set up in your IDE or development environment.

### Usage

1. Initialize an instance of `OutsideGuessing` with a secret key and an offset derived from the key's hash code.
2. To hide a message within an image, call the `hide` method with the path to the original image, the secret message, and the path where the output image should be saved.
3. To reveal a message from an image, call the `reveal` method with the path to the image containing the hidden message.

```kotlin
val internalKey = "your-key"
val outsideGuessing = OutsideGuessing(internalKey, internalKey.hashCode())

// Hiding a message
outsideGuessing.hide("path/to/original/image.png", "secret message", "path/to/output/image.png")

// Revealing a message
val revealedMessage = outsideGuessing.reveal("path/to/output/image.png")
println("Revealed Message: $revealedMessage")
```