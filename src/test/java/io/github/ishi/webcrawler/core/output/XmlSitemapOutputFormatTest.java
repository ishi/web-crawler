package io.github.ishi.webcrawler.core.output;

import io.github.ishi.webcrawler.core.model.ExtractedUri;
import io.github.ishi.webcrawler.core.output.XmlSitemapOutputFormat;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.github.ishi.webcrawler.core.model.ExtractedUri.*;
import static org.assertj.core.api.Assertions.assertThat;

public class XmlSitemapOutputFormatTest {

    private final XmlSitemapOutputFormat sut = new XmlSitemapOutputFormat();

    @Test
    void whenNoUrisProvided_shouldGenerateEmptyTemplate() {
        // Given
        List<ExtractedUri> uris = List.of();

        // When
        String result = sut.format(uris);

        // Then
        assertThat(result)
                .isEqualTo("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                        + "<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">\n"
                        + "</urlset>");
    }

    @Test
    void whenInternalUrisProvided_shoulIncludeThemInOutput() {
        // Given
        List<ExtractedUri> uris = List.of(internalLink("http://domain/page1"), internalLink("http://domain/page2"));

        // When
        String result = sut.format(uris);

        // Then
        assertThat(result)
                .isEqualTo("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                        + "<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">\n"
                        + "  <url>\n"
                        + "    <loc>http://domain/page1</loc>\n"
                        + "    <type>INTERNAL_LINK</type>\n"
                        + "  </url>\n"
                        + "  <url>\n"
                        + "    <loc>http://domain/page2</loc>\n"
                        + "    <type>INTERNAL_LINK</type>\n"
                        + "  </url>\n"
                        + "</urlset>");
    }

    @Test
    void whenExternalAndResourceUrisProvided_shoulMarkThemProperly() {
        // Given
        List<ExtractedUri> uris = List.of(
                internalLink("http://internal"), externalLink("http://external"), staticResource("http://static"));

        // When
        String result = sut.format(uris);

        // Then
        assertThat(result)
                .isEqualTo("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                        + "<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">\n"
                        + "  <url>\n"
                        + "    <loc>http://internal</loc>\n"
                        + "    <type>INTERNAL_LINK</type>\n"
                        + "  </url>\n"
                        + "  <url>\n"
                        + "    <loc>http://external</loc>\n"
                        + "    <type>EXTERNAL_LINK</type>\n"
                        + "  </url>\n"
                        + "  <url>\n"
                        + "    <loc>http://static</loc>\n"
                        + "    <type>STATIC_RESOURCE</type>\n"
                        + "  </url>\n"
                        + "</urlset>");
    }
}
