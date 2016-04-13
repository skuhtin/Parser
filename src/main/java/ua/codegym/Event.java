package ua.codegym;

import java.util.Map;

public enum Event {
  START_ELEMENT {
    StringBuilder handleOfStartElement = new StringBuilder();
    //int count = 0;

    @Override
    public Event next(Map<Event, EventHandler> handlers, int asciiInt) {
      if (asciiInt == -1) return ERROR;
      if (asciiInt == 60) return START_ELEMENT;
      if (asciiInt == 62) {
        handlers.get(this).handle(handleOfStartElement.toString());
        return END_ELEMENT;
      }
      handleOfStartElement.append((char) asciiInt);
      return START_ELEMENT;
    }
  }, ATTRIBUTE_NAME {
    @Override
    public Event next(Map<Event, EventHandler> handlers, int asciiInt) {
      return ERROR;
    }
  }, ATTRIBUTE_VALUE {
    @Override
    public Event next(Map<Event, EventHandler> handlers, int asciiInt) {
      return ERROR;
    }
  }, VALUE {
    @Override
    public Event next(Map<Event, EventHandler> handlers, int asciiInt) {
      return ERROR;
    }
  }, END_ELEMENT {
    StringBuilder handleOfEndElement = new StringBuilder();
    //int count = 0;

    @Override
    public Event next(Map<Event, EventHandler> handlers, int asciiInt) {
      if (asciiInt == -1) return ERROR;
      if (asciiInt == 60 | asciiInt == 47) return END_ELEMENT;
      if (asciiInt == 62) {
        handlers.get(this).handle(handleOfEndElement.toString());
       return END_ELEMENT;
      }
      handleOfEndElement.append((char) asciiInt);
      return END_ELEMENT;

    }
  }, ERROR {
    @Override
    public Event next(Map<Event, EventHandler> handlers, int asciiInt) {
      return null;
    }
  };

  public abstract Event next(Map<Event, EventHandler> handlers, int asciiInt);
}
