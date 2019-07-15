package io.github.ishi.webcrawler;

import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Collectors;

public class XmlSitemapOutputFormat implements OutputFormat {

    @Override
    public String format(Collection<ExtractedUri> uris) {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">\n"
                + uris.stream().map(toXml()).collect(Collectors.joining())
                + "</urlset>";
    }

    private Function<ExtractedUri, String> toXml() {
        return uri -> "  <url>\n"
                + "    <loc>" + uri.getUri() + "</loc>\n"
                + "    <type>" + uri.getType() + "</type>\n"
                + "  </url>\n";
    }
}
