package io.github.ishi.webcrawler.core;

import io.github.ishi.webcrawler.core.WebCrawler;
import io.github.ishi.webcrawler.core.content.URIExtractor;
import io.github.ishi.webcrawler.core.input.DataProvider;
import io.github.ishi.webcrawler.core.output.DataOutput;
import io.github.ishi.webcrawler.core.output.OutputFormat;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.Set;

import static io.github.ishi.webcrawler.core.model.ExtractedUri.internalLink;
import static io.github.ishi.webcrawler.core.model.ExtractedUri.staticResource;
import static org.mockito.Mockito.*;

class WebCrawlerTest {

    private final DataProvider provider = mock(DataProvider.class);
    private final URIExtractor extractor = mock(URIExtractor.class);
    private final OutputFormat formatter = mock(OutputFormat.class);
    private final DataOutput output = mock(DataOutput.class);

    private final WebCrawler sut = new WebCrawler(provider, extractor, formatter, output);

    @Test
    void whenProcessStarted_shouldAsForBaseUrlContent() {
        // Given
        String baseUrl = "http://page/";

        // When
        sut.analyze(baseUrl);

        // Then
        verify(provider).getContent(baseUrl);
    }

    @Test
    void whenBasePageContainsLinks_shouldGetContentForThem() {
        // Given
        String baseBody = "base";
        when(provider.getContent("http://page/")).thenReturn(Optional.of(baseBody));
        when(extractor.extract(baseBody)).thenReturn(
                Set.of(internalLink("http://page/subpage1"), internalLink("http://page/subpage2")));

        // When
        sut.analyze("http://page/");

        // Then
        verify(provider).getContent("http://page/subpage1");
        verify(provider).getContent("http://page/subpage2");
    }

    @Test
    void whenSubPageContainsLinks_shouldGetContentForThem() {
        // Given
        String baseBody = "baseBody";
        when(provider.getContent("http://page/")).thenReturn(Optional.of(baseBody));
        when(extractor.extract(baseBody)).thenReturn(
                Set.of(internalLink("http://page/subpage1"), internalLink("http://page/subpage2")));
        String subpage1Body = "subpage1";
        when(provider.getContent("http://page/subpage1")).thenReturn(Optional.of(subpage1Body));
        when(extractor.extract(subpage1Body)).thenReturn(
                Set.of(internalLink("http://page/subpage1.1"), internalLink("http://page/subpage1.2")));

        // When
        sut.analyze("http://page/");

        // Then
        verify(provider).getContent("http://page/subpage1.1");
        verify(provider).getContent("http://page/subpage1.2");
    }

    @Test
    void whenConsecutivePageContainsDuplicatedLinks_shouldNotFetchThem() {
        // Given
        String baseBody = "baseBody";
        when(provider.getContent("http://page/")).thenReturn(Optional.of(baseBody));
        when(extractor.extract(baseBody)).thenReturn(
                Set.of(internalLink("http://page/subpage1"), internalLink("http://page/subpage2")));
        String subpage1Body = "subpage1";
        when(provider.getContent("http://page/subpage1")).thenReturn(Optional.of(subpage1Body));
        when(extractor.extract(subpage1Body)).thenReturn(
                Set.of(internalLink("http://page/subpage1.1"), internalLink("http://page/subpage1.2")));
        String subpage2Body = "subpage2";
        when(provider.getContent("http://page/subpage2")).thenReturn(Optional.of(subpage2Body));
        when(extractor.extract(subpage2Body)).thenReturn(
                Set.of(internalLink("http://page/subpage2.1"), internalLink("http://page/subpage1.1")));

        // When
        sut.analyze("http://page/");

        // Then
        verify(provider, atMostOnce()).getContent("http://page/subpage1.1");
        verify(provider).getContent("http://page/subpage1.2");
    }

    @Test
    void whenPageContainsStaticLinks_shouldNotFetchThem() {
        // Given
        String baseBody = "base";
        when(provider.getContent("http://page/")).thenReturn(Optional.of(baseBody));
        when(extractor.extract(baseBody)).thenReturn(
                Set.of(internalLink("http://page/subpage1"), staticResource("http://page/static")));

        // When
        sut.analyze("http://page/");

        // Then
        verify(provider).getContent("http://page/");
        verify(provider).getContent("http://page/subpage1");
        verifyNoMoreInteractions(provider);
    }


    @Test
    void whenPageContainsExternalLinks_shouldNotFetchThem() {
        // Given
        String baseBody = "base";
        when(provider.getContent("http://page/")).thenReturn(Optional.of(baseBody));
        when(extractor.extract(baseBody)).thenReturn(
                Set.of(internalLink("http://page/subpage1"), staticResource("http://external/")));

        // When
        sut.analyze("http://page/");

        // Then
        verify(provider).getContent("http://page/");
        verify(provider).getContent("http://page/subpage1");
        verifyNoMoreInteractions(provider);
    }

    @Test
    void whenLinksExtracted_shouldUseFormatterToCreateOutputContent() {
        // Given
        String baseBody = "base";
        when(provider.getContent("http://page/")).thenReturn(Optional.of(baseBody));
        when(extractor.extract(baseBody)).thenReturn(
                Set.of(internalLink("http://page/subpage1"), staticResource("http://page/static/")));

        // When
        sut.analyze("http://page/");

        // Then
        verify(formatter).format(Set.of(internalLink("http://page/subpage1"), staticResource("http://page/static/")));
    }

    @Test
    void whenLinksExtracted_shouldUseDataOutputToStoreData() {
        // Given
        String baseBody = "base";
        when(provider.getContent("http://page/")).thenReturn(Optional.of(baseBody));
        when(extractor.extract(baseBody)).thenReturn(
                Set.of(internalLink("http://page/subpage1"), staticResource("http://page/static/")));
        when(formatter.format(anyCollection())).thenReturn("formatted output");

        // When
        sut.analyze("http://page/");

        // Then
        verify(output).accept("formatted output");
    }

}
