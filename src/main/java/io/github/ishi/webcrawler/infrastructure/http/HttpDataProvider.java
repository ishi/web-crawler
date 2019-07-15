package io.github.ishi.webcrawler.infrastructure.http;

import io.github.ishi.webcrawler.core.input.DataProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Optional;

public class HttpDataProvider implements DataProvider {
    private static final Logger logger = LoggerFactory.getLogger(HttpDataProvider.class);

    private final HttpClient httpClient;

    public HttpDataProvider(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public Optional<String> getContent(String uri) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(uri))
                    .timeout(Duration.ofMinutes(1))
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                return Optional.of(response.body());
            }
        } catch (IOException | InterruptedException ex) {
            logger.error("Failed retrieving uri content {}", uri, ex);
        }
        return Optional.empty();
    }
}
