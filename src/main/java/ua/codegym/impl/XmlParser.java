package ua.codegym.impl;

import ua.codegym.Event;
import ua.codegym.EventHandler;
import ua.codegym.Parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

//azzazza
//bbbbbbbbbbb
public class XmlParser implements Parser {

    private final Map<Event, EventHandler> handlers;

    public XmlParser(Map<Event, EventHandler> handlers) {
        this.handlers = handlers;
    }

    @Override
    public void parse(InputStream in) throws IOException {
        Event event = Event.START_ELEMENT;
        int countLineSymb = 0;
        int countLines = 1;
        while (in.available() > 0) {
            int tmp = in.read();
            event = event.next(handlers, tmp, countLineSymb, countLines);
            countLineSymb++;
            if (tmp == 10) {
                countLines++;
            }

        }
    }
}
