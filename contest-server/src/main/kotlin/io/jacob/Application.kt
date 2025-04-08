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
import kotlin.collections.set
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

            val resource = this::class.java.classLoader.getResource("static/scoreboard.html") ?: return@get
            val template = File(resource.toURI()).readText()
            val text = template.replace("\${content}", sorted.joinToString("") {
                """<tr><td>${it.name}</td><td>${it.score.count { it.value }}</td><td>${it.score}</td></tr>"""
            })

            call.respondText(text, contentType = Html)
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
            call.respondRedirect("/upload-done.html")
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
    uploadDir.listFiles { f -> f.extension == "jar" }?.forEach { jar ->
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

data class User(val name: String, val score: Map<String, Boolean>)
