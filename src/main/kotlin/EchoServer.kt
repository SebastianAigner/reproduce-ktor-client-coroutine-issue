import io.ktor.application.install
import io.ktor.http.cio.websocket.Frame
import io.ktor.routing.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.websocket.WebSockets
import io.ktor.websocket.webSocket

fun main() {
    embeddedServer(Netty, 9090) {
        install(WebSockets)
        routing {
            webSocket("/chat") {
                outgoing.send(Frame.Text("You are connected! Thanks for stopping by."))
            }
        }
    }.start(wait = true)
}