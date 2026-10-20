package com.laterbrain.app

import com.laterbrain.app.capture.CategoryGuesser
import com.laterbrain.app.capture.SharedContentParser
import com.laterbrain.app.data.Category
import com.laterbrain.app.data.ReminderWindow
import com.laterbrain.app.data.SavedItem
import com.laterbrain.app.domain.MaybeToday
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDateTime

class SharedContentParserTest {

    @Test
    fun chromeShare_usesSubjectAsTitle() {
        val c = SharedContentParser.parse("https://www.bbc.com/news/some-story", "Why sleep matters")
        assertEquals("Why sleep matters", c.title)
        assertEquals("https://www.bbc.com/news/some-story", c.url)
        assertNull(c.note)
    }

    @Test
    fun linkOnly_fallsBackToWebsiteName() {
        val c = SharedContentParser.parse("https://www.allrecipes.com/recipe/123/pilau/", null)
        assertEquals("allrecipes.com", c.title)
    }

    @Test
    fun mapsShare_keepsPlaceNameAndCoordinates() {
        val c = SharedContentParser.parse(
            "Java House Kimathi\nKimathi St, Nairobi\nhttps://www.google.com/maps/place/Java/@-1.2841,36.8233,17z",
            null,
        )
        assertEquals("Java House Kimathi", c.title)
        assertEquals("Java House Kimathi", c.placeName)
        assertEquals("Kimathi St, Nairobi", c.note)
        assertEquals(-1.2841, c.latitude!!, 0.0001)
        assertEquals(36.8233, c.longitude!!, 0.0001)
    }

    @Test
    fun mapsShortLink_hasPlaceButNoCoordinates() {
        val c = SharedContentParser.parse("Carnivore Restaurant\nhttps://maps.app.goo.gl/AbCdEf", null)
        assertEquals("Carnivore Restaurant", c.placeName)
        assertNull(c.latitude)
    }

    @Test
    fun longPlainText_isShortenedButKeptInNote() {
        val long = "Idea: " + "build a little garden on the balcony with herbs and tomatoes ".repeat(3)
        val c = SharedContentParser.parse(long, null)
        assertTrue(c.title.length <= 91)
        assertTrue(c.title.endsWith("…"))
        assertEquals(long.trim(), c.note)
    }

    @Test
    fun trailingPunctuation_isStrippedFromLink() {
        val c = SharedContentParser.parse("Look at this: https://example.com/page.", null)
        assertEquals("https://example.com/page", c.url)
        assertEquals("Look at this:", c.title)
    }
}

class CategoryGuesserTest {

    @Test
    fun guessesByWebsite() {
        assertEquals(Category.VIDEO, CategoryGuesser.guess("Anything", "https://youtu.be/abc"))
        assertEquals(Category.RECIPE, CategoryGuesser.guess("Pilau", "https://www.allrecipes.com/x"))
        assertEquals(Category.SHOPPING, CategoryGuesser.guess("Shoes", "https://www.jumia.co.ke/shoes"))
    }

    @Test
    fun guessesByWords() {
        assertEquals(Category.RECIPE, CategoryGuesser.guess("Easy chapati recipe", null))
        assertEquals(Category.RESTAURANT, CategoryGuesser.guess("New sushi place in Westlands", null))
        assertEquals(Category.TRAVEL, CategoryGuesser.guess("Diani beach weekend", null))
    }

    @Test
    fun fallbacks() {
        assertEquals(Category.PLACE, CategoryGuesser.guess("Somewhere", "https://maps.app.goo.gl/x"))
        assertEquals(Category.ARTICLE, CategoryGuesser.guess("Something", "https://example.com"))
        assertEquals(Category.IDEA, CategoryGuesser.guess("Call grandma", null))
    }
}

class MaybeTodayTest {

    private val saturdayMorning = LocalDateTime.of(2026, 10, 24, 9, 0)
    private val tuesdayEvening = LocalDateTime.of(2026, 10, 20, 19, 0)

    @Test
    fun recipeWinsOnWeekendMorning() {
        val recipe = SavedItem(id = 1, title = "Pancakes", category = Category.RECIPE, reminderWindow = ReminderWindow.WEEKEND)
        val article = SavedItem(id = 2, title = "Sleep", category = Category.ARTICLE, reminderWindow = ReminderWindow.TONIGHT)
        assertEquals(1L, MaybeToday.pick(listOf(article, recipe), saturdayMorning)?.id)
    }

    @Test
    fun tonightItemWinsOnWeekdayEvening() {
        val recipe = SavedItem(id = 1, title = "Pancakes", category = Category.RECIPE, reminderWindow = ReminderWindow.WEEKEND)
        val article = SavedItem(id = 2, title = "Sleep", category = Category.ARTICLE, reminderWindow = ReminderWindow.TONIGHT)
        assertEquals(2L, MaybeToday.pick(listOf(recipe, article), tuesdayEvening)?.id)
    }

    @Test
    fun tieGoesToOldest() {
        val older = SavedItem(id = 1, title = "A", category = Category.IDEA, createdAt = 1_000)
        val newer = SavedItem(id = 2, title = "B", category = Category.IDEA, createdAt = 2_000)
        assertEquals(1L, MaybeToday.pick(listOf(newer, older), tuesdayEvening)?.id)
    }

    @Test
    fun emptyListGivesNothing() {
        assertNull(MaybeToday.pick(emptyList(), tuesdayEvening))
    }
}
