package io.github.ishi.webcrawler.infrastructure;

import io.github.ishi.webcrawler.core.WebCrawler;
import io.github.ishi.webcrawler.core.output.XmlSitemapOutputFormat;
import io.github.ishi.webcrawler.infrastructure.html.HtmlURIExtractor;
import io.github.ishi.webcrawler.infrastructure.http.HttpDataProvider;

import java.net.http.HttpClient;

public class Main {
    public static void main(String[] args) {

        HttpClient client = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build();

        HttpDataProvider dataProvider = new HttpDataProvider(client);
        new WebCrawler(dataProvider, new HtmlURIExtractor(), new XmlSitemapOutputFormat(), System.out::println)
                .analyze("http://wiprodigital.com");
    }
}
