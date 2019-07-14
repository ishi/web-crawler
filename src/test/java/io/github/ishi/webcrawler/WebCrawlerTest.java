package io.github.ishi.webcrawler;

import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.Set;

import static io.github.ishi.webcrawler.ExtractedUri.internalLink;
import static io.github.ishi.webcrawler.ExtractedUri.staticResource;
import static org.mockito.Mockito.*;

public class WebCrawlerTest {

    private final DataProvider provider = mock(DataProvider.class);
    private final URIExtractor extractor = mock(URIExtractor.class);

    private final WebCrawler sut = new WebCrawler(provider, extractor);

    @Test
    public void whenProcessStarted_shouldAsForBaseUrlContent() {
        // Given
        String baseUrl = "http://page/";

        // When
        sut.analyze(baseUrl);

        // Then
        verify(provider).getContent(baseUrl);
    }

    @Test
    public void whenBasePageContainsLinks_shouldGetContentForThem() {
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
    public void whenSubPageContainsLinks_shouldGetContentForThem() {
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
    public void whenConsecutivePageContainsDuplicatedLinks_shouldNotFetchThem() {
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
    public void whenPageContainsStaticLinks_shouldNotFetchThem() {
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
}
