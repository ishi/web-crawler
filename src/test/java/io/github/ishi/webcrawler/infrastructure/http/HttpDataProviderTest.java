package io.github.ishi.webcrawler.infrastructure.http;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HttpDataProviderTest {

    private final HttpClient httpClient = mock(HttpClient.class);
    private final HttpDataProvider sut = new HttpDataProvider(httpClient);

    @Test
    void whenSuccess_shouldReturnOptionalWithContent() throws IOException, InterruptedException {
        // Given
        String url = "http://domain/";
        HttpResponse response = mock(HttpResponse.class);
        when(httpClient.send(any(), any())).thenReturn(response);
        when(response.statusCode()).thenReturn(200);
        when(response.body()).thenReturn("content");

        // When
        Optional<String> result = sut.getContent(url);

        // Then
        assertThat(result)
                .hasValue("content");
    }

    @Test
    void whenNotFound_shouldReturnEmptyOptional() throws IOException, InterruptedException {
        // Given
        String url = "http://domain/";
        HttpResponse response = mock(HttpResponse.class);
        when(httpClient.send(any(), any())).thenReturn(response);
        when(response.statusCode()).thenReturn(404);

        // When
        Optional<String> result = sut.getContent(url);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void whenExceptionWhileRetrieving_shouldReturnEmptyOptional() throws IOException, InterruptedException {
        // Given
        String url = "http://domain/";
        when(httpClient.send(any(), any())).thenThrow(new IOException());

        // When
        Optional<String> result = sut.getContent(url);

        // Then
        assertThat(result).isEmpty();
    }
}