package com.agileengine;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class Helper {
    private static final String CHARSET_NAME = "utf8";
    private static final Logger LOGGER = LoggerFactory.getLogger(Helper.class);

    public static Optional<Elements> findMatches(Element originEl, File diffFile, Set<String> searchAttrs) throws IOException {

        List<String> selectors = originEl.attributes().asList().stream()
                .filter(attribute -> searchAttrs.contains(attribute.getKey()))
                .map(attribute -> createSelector(originEl, attribute.getKey(), attribute.getValue()))
                .collect(Collectors.toList());

        if (selectors.size() > 0) {
            Elements resultElems = findElementsByQuery(diffFile, selectors.get(0));
            for (int i = 1; i < selectors.size(); i++) {
                resultElems = resultElems.select(selectors.get(i));
            }
            if (resultElems.isEmpty()) {
                LOGGER.warn("No matches found");
            }
            return Optional.of(resultElems);
        }
        return Optional.empty();
    }

    public static Optional<Element> findElementById(File htmlFile, String targetElementId) {
        try {
            Document doc = Jsoup.parse(htmlFile, CHARSET_NAME, htmlFile.getAbsolutePath());
            return Optional.ofNullable(doc.getElementById(targetElementId));

        } catch (IOException e) {
            LOGGER.error("Error reading [{}] file", htmlFile.getAbsolutePath(), e);
            return Optional.empty();
        }
    }

    private static Elements findElementsByQuery(File htmlFile, String cssQuery) throws IOException {
        try {
            LOGGER.info("Searching for elements by query: " + cssQuery);
            Document doc = Jsoup.parse(htmlFile, CHARSET_NAME, htmlFile.getAbsolutePath());
            return doc.select(cssQuery);
        } catch (IOException e) {
            LOGGER.error("Error reading [{}] file", htmlFile.getAbsolutePath(), e);
            throw e;
        }
    }

    private static String createSelector(Element originEl, String attr, String value) {
        StringBuilder selectorBuilder = new StringBuilder();
        return selectorBuilder.append(originEl.tagName()).append("[").append(attr).append("=")
                .append(value).append("]").toString();
    }
}
