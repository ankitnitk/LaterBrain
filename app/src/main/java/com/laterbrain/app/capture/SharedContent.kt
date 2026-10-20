package com.laterbrain.app.capture

import java.net.URI

/** What we could work out from something shared into the app. */
data class SharedContent(
    val title: String,
    val url: String? = null,
    val note: String? = null,
    val placeName: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
)

/**
 * Turns the raw text another app shares (e.g. Chrome sends a link plus the page title,
 * Google Maps sends "Place name, address, link") into a tidy title, link and note.
 * Works fully offline: links are never opened or fetched.
 */
object SharedContentParser {

    private const val MAX_TITLE = 90

    private val URL_REGEX = Regex("""https?://[^\s<>"]+""", RegexOption.IGNORE_CASE)

    // Google Maps puts coordinates in links like ".../@-1.2921,36.8219,15z" or "?q=-1.2921,36.8219".
    private val AT_COORDS = Regex("""@(-?\d{1,2}\.\d+),(-?\d{1,3}\.\d+)""")
    private val QUERY_COORDS = Regex(
        """[?&](?:q|query|ll|destination)=(-?\d{1,2}\.\d+)(?:,|%2C)\s*(-?\d{1,3}\.\d+)""",
        RegexOption.IGNORE_CASE,
    )

    fun parse(text: String?, subject: String?): SharedContent {
        val raw = text?.trim().orEmpty()
        val matched = URL_REGEX.find(raw)?.value
        val url = matched?.trimEnd('.', ',', ')', ']', '!', '?')
        val rest = (if (matched != null) raw.replace(matched, " ") else raw).trim()
        val lines = rest.lines().map { it.trim() }.filter { it.isNotEmpty() }

        val cleanSubject = subject?.trim()?.takeIf { it.isNotEmpty() && it != url }
        val firstLine = lines.firstOrNull()
        val fullTitle = cleanSubject ?: firstLine ?: url?.let(::hostOf).orEmpty()
        val title = shorten(fullTitle)

        // Whatever didn't become the title is kept as the note, so nothing is lost.
        val noteLines = if (cleanSubject == null && firstLine == fullTitle && title == fullTitle) lines.drop(1) else lines
        val note = noteLines.joinToString("\n").takeIf { it.isNotBlank() && it != title }

        val isMaps = url != null && isMapsUrl(url)
        val coords = url?.let(::coordinatesOf)

        return SharedContent(
            title = title,
            url = url,
            note = note,
            placeName = if (isMaps || coords != null) (firstLine ?: cleanSubject) else null,
            latitude = coords?.first,
            longitude = coords?.second,
        )
    }

    fun isMapsUrl(url: String): Boolean {
        val host = hostOf(url) ?: return false
        return host.startsWith("maps.") ||
            (host == "goo.gl" && url.contains("/maps")) ||
            (host.endsWith("google.com") && url.contains("/maps"))
    }

    fun hostOf(url: String): String? =
        runCatching { URI(url).host }.getOrNull()?.lowercase()?.removePrefix("www.")

    private fun coordinatesOf(url: String): Pair<Double, Double>? {
        val match = AT_COORDS.find(url) ?: QUERY_COORDS.find(url) ?: return null
        val lat = match.groupValues[1].toDoubleOrNull() ?: return null
        val lng = match.groupValues[2].toDoubleOrNull() ?: return null
        if (lat !in -90.0..90.0 || lng !in -180.0..180.0) return null
        return lat to lng
    }

    private fun shorten(s: String): String =
        if (s.length <= MAX_TITLE) s else s.take(MAX_TITLE).substringBeforeLast(' ').trimEnd() + "…"
}
