package com.laterbrain.app.capture

import com.laterbrain.app.data.Category

/**
 * Guesses a category from the website and the words used. Simple keyword rules, no AI.
 * The user can always change the guess.
 */
object CategoryGuesser {

    private val hostRules: List<Pair<Category, List<String>>> = listOf(
        Category.VIDEO to listOf("youtube.com", "youtu.be", "vimeo.com", "tiktok.com", "netflix.com"),
        Category.RECIPE to listOf("allrecipes.com", "bbcgoodfood.com", "tasty.co", "food.com", "seriouseats.com", "cookpad.com"),
        Category.RESTAURANT to listOf("tripadvisor.com", "yelp.com", "zomato.com", "opentable.com"),
        Category.TRAVEL to listOf("booking.com", "airbnb.com", "expedia.com", "skyscanner.net", "kayak.com"),
        Category.BOOK to listOf("goodreads.com", "audible.com"),
        Category.SHOPPING to listOf("amazon.com", "jumia.co.ke", "kilimall.co.ke", "ebay.com", "aliexpress.com", "etsy.com"),
        Category.ARTICLE to listOf(
            "medium.com", "substack.com", "nytimes.com", "bbc.com", "bbc.co.uk", "theguardian.com",
            "nation.africa", "standardmedia.co.ke", "wikipedia.org",
        ),
    )

    // Order matters: the first category with a matching word wins.
    private val keywordRules: List<Pair<Category, List<String>>> = listOf(
        Category.RECIPE to listOf("recipe", "how to cook", "how to make", "ingredients", "bake", "baking"),
        Category.RESTAURANT to listOf(
            "restaurant", "cafe", "café", "coffee", "bistro", "brunch", "pizza", "sushi", "grill",
            "nyama choma", "eatery", "dinner at", "lunch at",
        ),
        Category.TRAVEL to listOf("hotel", "flight", "trip", "travel", "safari", "beach", "resort", "lodge", "itinerary"),
        Category.VIDEO to listOf("video", "watch", "movie", "film", "series", "episode", "trailer"),
        Category.BOOK to listOf("book", "novel", "author", "audiobook"),
        Category.SHOPPING to listOf("buy", "price", "shop", "deal", "discount", "sale"),
        Category.ARTICLE to listOf("article", "blog", "news", "essay", "guide", "tips"),
    )

    fun guess(title: String, url: String?, note: String? = null): Category {
        val host = url?.let(SharedContentParser::hostOf)
        if (host != null) {
            hostRules.firstOrNull { (_, hosts) -> hosts.any { host == it || host.endsWith(".$it") } }
                ?.let { return it.first }
        }

        val text = listOfNotNull(title, note).joinToString(" ").lowercase()
        keywordRules.firstOrNull { (_, words) -> words.any { text.contains(it) } }
            ?.let { return it.first }

        return when {
            url != null && SharedContentParser.isMapsUrl(url) -> Category.PLACE
            url != null -> Category.ARTICLE
            text.isNotBlank() -> Category.IDEA
            else -> Category.OTHER
        }
    }
}
