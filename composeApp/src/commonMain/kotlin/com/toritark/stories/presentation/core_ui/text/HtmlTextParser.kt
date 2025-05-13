package com.toritark.stories.presentation.core_ui.text

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import co.touchlab.kermit.Logger
import com.mohamedrejeb.ksoup.html.parser.KsoupHtmlHandler
import com.mohamedrejeb.ksoup.html.parser.KsoupHtmlParser

private const val LOG_TAG = "HtmlTextParser"
private val logger = Logger.withTag(LOG_TAG)

fun htmlToAnnotatedString(htmlString: String): AnnotatedString {
    val string = AnnotatedString.Builder()

    val handler = KsoupHtmlHandler
        .Builder()
        .onOpenTag { name, attributes, isImplied ->
            when (name) {
                "b" -> string.pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
                "u" -> string.pushStyle(SpanStyle(textDecoration = TextDecoration.Underline))
                "i" -> string.pushStyle(SpanStyle(fontStyle = FontStyle.Italic))
                "s" -> string.pushStyle(SpanStyle(textDecoration = TextDecoration.LineThrough))
                else -> {
                    logger.d { "onOpenTag: Unhandled span $name" }
                }
            }
        }
        .onCloseTag { name, isImplied ->
            when (name) {
                "b", "u", "i", "s" -> string.pop()
                else -> {
                    logger.d { "onCloseTag: Unhandled span $name" }
                }
            }
        }
        .onText { text ->
            string.append(text)
        }
        .build()

    val ksoupHtmlParser = KsoupHtmlParser(handler)
    ksoupHtmlParser.write(htmlString)
    ksoupHtmlParser.end()

    return string.toAnnotatedString()
}