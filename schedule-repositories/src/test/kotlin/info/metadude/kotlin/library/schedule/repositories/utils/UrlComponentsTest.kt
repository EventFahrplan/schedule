package info.metadude.kotlin.library.schedule.repositories.utils

import com.google.common.truth.Truth.assertThat
import info.metadude.kotlin.library.schedule.repositories.utils.UrlComponents.Companion.getUrlComponents
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class UrlComponentsTest {

    @Test
    fun `getComponent returns UrlComponents when URL has host and path`() {
        val components = "https://example.com/schedule.json".getUrlComponents()
        assertThat(components.baseUrl).isEqualTo("https://example.com/")
        assertThat(components.path).isEqualTo("schedule.json")
    }

    @Test
    fun `getComponent returns empty path when path is lacking`() {
        val components = "https://example.com".getUrlComponents()
        assertThat(components.baseUrl).isEqualTo("https://example.com/")
        assertThat(components.path).isEmpty()
    }

    @Test
    fun `getComponent throws exception when host is missing`() {
        val exception = assertThrows<IllegalArgumentException> { "https://".getUrlComponents() }
        assertThat(exception.message).isEqualTo("""Invalid URL host: """"")
    }

}
