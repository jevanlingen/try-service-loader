package io.jacob

import io.ktor.http.ContentType.Text.Html
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.netty.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.io.File
import java.net.URLClassLoader
import java.util.*
import kotlin.reflect.KFunction
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.findAnnotation

fun main(args: Array<String>) = EngineMain.main(args)

val uploadDir = File("uploads").apply { mkdir() }
val userScores = mutableMapOf<String, User>()

fun Application.module() {
    routing {
        staticResources("/", "static")

        get("/scoreboard") {
            val sorted = synchronized(userScores) {
                userScores.values.sortedByDescending { it.score.count { it.value } }
            }
            call.respondText(buildHtmlScoreboard(sorted), contentType = Html)
        }

        post("/upload") {
            val multipart = call.receiveMultipart()
            multipart.forEachPart { part ->
                if (part is PartData.FileItem) {
                    val fileName = part.originalFileName ?: return@forEachPart
                    val file = File(uploadDir, fileName)
                    part.streamProvider().use { input -> file.outputStream().use { input.copyTo(it) } }
                }
                part.dispose()
            }
            scanAndEvaluate()
            call.respondText("Upload successful")
        }
    }
    scanAndEvaluate()
}

fun scanAndEvaluate() {
    fun Contest.test(fn: KFunction<*>, vararg args: Any) =
        try { fn.call(*arrayOf(this, *args)) } catch (e: Throwable) { false }

    fun Contest.evaluate(fn: KFunction<*>) =
        when (fn) {
            Contest::highestNumber -> test(fn, 3, 7) == 7
            Contest::removeDuplicates -> test(fn, "banana") == "ban"
            Contest::countVowels -> test(fn, "vowel") == 2
            else -> false
        }

    val updatedScores = mutableMapOf<String, User>()
    uploadDir.listFiles { it.extension == "jar" }?.forEach { jar ->
        try {
            val loader = URLClassLoader(arrayOf(jar.toURI().toURL()), Contest::class.java.classLoader)
            val providers = ServiceLoader.load(Contest::class.java, loader)
            val contest = providers.firstOrNull() ?: return@forEach

            val results = mutableMapOf<String, Boolean>()
            Contest::class.declaredFunctions.forEach {
                val exp = it.findAnnotation<Assignment>() ?: return@forEach
                // TODO: Do something with `exp.shouldBeFixedBefore`
                results[it.name] = contest.evaluate(it)
            }
            val playerName = jar.name.split("-")[0].capitalize()
            updatedScores[playerName] = User(playerName, results)
        } catch (e: Exception) {
            println("Error loading jar ${jar.name}: ${e.message}")
        }
    }
    synchronized(userScores) {
        userScores.clear()
        userScores.putAll(updatedScores)
    }
}

fun buildHtmlScoreboard(users: List<User>): String = buildString {
    append("""
        <html><head><title>Scoreboard</title></head><body>
        <h1>Scoreboard</h1>
        <table border="1">
        <tr><th>Player</th><th>Score</th><th>Details</th></tr>
    """)
    users.forEach { user ->
        append("""<tr><td>${user.name}</td><td>${user.score.count { it.value }}</td><td>${user.score}</td></tr>""")
    }
    append("</table></body></html>")
}

data class User(val name: String, val score: Map<String, Boolean>)
