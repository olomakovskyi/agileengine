package com.agileengine;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MainApp {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainApp.class);
    private static String targetElementId = "make-everything-ok-button";
    private static String attributesForSearch = "class,href,onclick";

    public static void main(String[] args) {

        if (args.length < 2) {
            LOGGER.error("Not enough arguments");
            return;
        }
        if (args.length > 2) {
            targetElementId = args[2];
        }
        if (args.length > 3) {
            attributesForSearch = args[3];
        }

        try {
            Set<String> searchAttrs = Stream.of(attributesForSearch.split(",")).collect(Collectors.toSet());
            File originFile = new File(args[0]);
            File diffFile = new File(args[1]);

            Optional<Element> originElOpt = Helper.findElementById(originFile, targetElementId);
            if (!originElOpt.isPresent()) {
                LOGGER.error("Key element was not found for id=" + targetElementId);
                return;
            }
            LOGGER.info("Key element was found for id=" + targetElementId);
            Optional<Elements> matches = Helper.findMatches(originElOpt.get(), diffFile, searchAttrs);

            matches.ifPresent(result -> {
                for (Element el : result) {
                    StringBuilder resultBuilder = new StringBuilder();
                    while (el.hasParent()) {
                        resultBuilder.append(el.tagName());
                        el = el.parent();
                        if (el.hasParent())
                            resultBuilder.append(" > ");
                    }
                    //print result to console
                    System.out.println(resultBuilder.toString());
                }
            });
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
    }
}
