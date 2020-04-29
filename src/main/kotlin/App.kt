import io.ktor.client.HttpClient
import io.ktor.client.features.websocket.WebSockets
import io.ktor.client.features.websocket.webSocket
import io.ktor.http.HttpMethod
import io.ktor.http.cio.websocket.Frame
import io.ktor.http.cio.websocket.readText
import kotlinx.coroutines.*

fun main() {
    val client = HttpClient {
        install(WebSockets)
    }

    println("Entering blocking websocket part...")

    val scope = MainScope()
    runBlocking {
        client.webSocket("wss://echo.websocket.org") {
            val messageSender = scope.launch(Dispatchers.Default) {
                repeat(3) {
                    delay(500)
                    outgoing.send(Frame.Text("ping $it"))
                    delay(500)
                }
            }

            val messageReceiver = scope.launch(Dispatchers.Default) {
                try {
                    for(message in incoming) {
                        if(message is Frame.Text) {
                            println("RECV: " + message.readText())
                        }
                    }
                }
                catch(e: Exception) {
                    println(e.localizedMessage)
                }
            }

            messageSender.join()
            println("Message sender is done. Sending cancellation to message receiver...")
            messageReceiver.cancelAndJoin()
        }
        client.close()
    }
    scope.cancel()
    println("Exited blocking websocket part.")
    println("Nothing left to do, goodbye!")
}