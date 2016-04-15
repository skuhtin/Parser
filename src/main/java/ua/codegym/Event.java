package ua.codegym;

import java.util.Map;

public enum Event {
  START_ELEMENT {

    @Override
    public Event next(Map<Event, EventHandler> handlers, int asciiInt) {
      if (asciiInt == -1) return ERROR;
      if (asciiInt == 60) return START_ELEMENT;
      if (asciiInt == 47) return END_ELEMENT;
      if (asciiInt == 62) return START_ELEMENT;
      char handler = (char) asciiInt;
      handlers.get(this).handle(String.valueOf(handler));
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
    @Override
    public Event next(Map<Event, EventHandler> handlers, int asciiInt) {
      if (asciiInt == -1) return ERROR;
      if (asciiInt == 62) return START_ELEMENT;
      //char handler = (char) asciiInt;
      //handlers.get(this).handle(String.valueOf(handler));
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
