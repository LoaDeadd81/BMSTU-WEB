package api.api.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.intellij.markdown.flavours.commonmark.CommonMarkFlavourDescriptor
import org.intellij.markdown.html.HtmlGenerator
import org.intellij.markdown.parser.MarkdownParser
import java.io.File

fun Route.readme(){
    route("/readme"){
        get {
            val src = File("../README.md").readText()
            val flavour = CommonMarkFlavourDescriptor()
            val parsedTree = MarkdownParser(flavour).buildMarkdownTreeFromString(src)
            val html = HtmlGenerator(src, parsedTree, flavour).generateHtml()
            call.respondText(html, ContentType.Text.Html)
        }

    }
}