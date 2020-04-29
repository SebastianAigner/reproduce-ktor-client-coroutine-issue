import io.ktor.client.HttpClient
import io.ktor.client.features.websocket.WebSockets
import io.ktor.client.features.websocket.webSocket
import io.ktor.http.HttpMethod
import io.ktor.http.cio.websocket.Frame
import io.ktor.http.cio.websocket.readText
import kotlinx.coroutines.*

fun main() {
    val client = HttpClient { install(WebSockets) }

    println("Entering blocking websocket part...")
    runBlocking {
        client.webSocket(method = HttpMethod.Get, host = "0.0.0.0", port = 9090, path = "/chat") {
            val frame = incoming.receive()
            if(frame is Frame.Text) {
                println("RECV: " + frame.readText())
            }
        }
    }
    client.close()
    println("Done!")
}