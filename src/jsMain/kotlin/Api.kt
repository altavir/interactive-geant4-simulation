import io.ktor.http.*
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.content.*
import kotlinx.browser.window

val endpoint = window.location.origin // only needed until https://github.com/ktorio/ktor/issues/1695 is resolved

val jsonClient = HttpClient {
    install(JsonFeature) { serializer = KotlinxSerializer() }
}

suspend fun getEventFull(eventID: Int): List<EventFull> {
    return jsonClient.get(endpoint + EventFull.path + "?eventID=$eventID")
}

suspend fun getNumberOfEvents(runID: Int = 0): Long {
    return jsonClient.get("$endpoint/numberOfEvents?runID=$runID")
}

suspend fun getCounts(): Counts {
    return jsonClient.get(endpoint + Counts.path)
}

suspend fun sendCommand(command: String) {
    jsonClient.post<String>("http://localhost:9080/send/") {
        println("send post command: $command")
        body = TextContent(
            text = "command=$command",
            contentType = ContentType.Text.Plain
        )
    }
}

suspend fun clearDatabase() {
    return jsonClient.get("$endpoint/clear")
}

suspend fun getEventIDsFromRunID(runID: Int): Set<Int> {
    return jsonClient.get("$endpoint/eventIDsFromRunID?runID=$runID")
}

suspend fun getEventEnergyPerVolume(runID: Int, eventID: Int): Map<String, Double> {
    return jsonClient.get("$endpoint/energyPerVolume?runID=$runID&eventID=$eventID")
}

suspend fun getEventEnergyPerVolume(runID: Int, volume: String): Map<Int, Double> {
    return jsonClient.get("$endpoint/energyInVolumeForRun?runID=$runID&volume=$volume")
}
