package ua.codegym;

import java.util.Map;

public enum Event {
  START_ELEMENT {
    @Override
    public Event next(Map<Event, EventHandler> handlers, int asciiInt) {
      if (asciiInt == -1) return ERROR;
      if (asciiInt == 60 | asciiInt == 62)
        return START_ELEMENT;  // int 60 - char '<' int 62 - char '>'
      if (asciiInt == 47) return END_ELEMENT;    // int 47 - char '/'
      if (asciiInt == 32) return ATTRIBUTE_NAME; // int 32 - char ' '

      char handler = (char) asciiInt;
      if (handlers.containsKey(this)) {
        handlers.get(this).handle(String.valueOf(handler));
      }
      return START_ELEMENT;
    }
  }, ATTRIBUTE_NAME {
    @Override
    public Event next(Map<Event, EventHandler> handlers, int asciiInt) {
      if (asciiInt == -1) return ERROR;
      if (asciiInt == 61 | asciiInt == 32)
        return ATTRIBUTE_NAME;  // int 61 - char '=' int 32 - char ' '
      if (asciiInt == 34) return ATTRIBUTE_VALUE;  // int 34 - char ' " '
      if (asciiInt == 62) return START_ELEMENT;    // int 62 - char '>'

      char handler = (char) asciiInt;
      if (handlers.containsKey(this)) {
        handlers.get(this).handle(String.valueOf(handler));
      }
      return ATTRIBUTE_NAME;
    }
  }, ATTRIBUTE_VALUE {
    StringBuilder handler = new StringBuilder();

    @Override
    public Event next(Map<Event, EventHandler> handlers, int asciiInt) {
      if (asciiInt == 34) {      // int 34 - char ' " '
        if (handlers.containsKey(this)) {
          handlers.get(this).handle(handler.toString());
        }
        handler = new StringBuilder();
        return START_ELEMENT;
      }
      char tmpHandler = (char) asciiInt;
      handler.append(tmpHandler);
      return ATTRIBUTE_VALUE;
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
      if (asciiInt == 32) return END_ELEMENT; // int 32 - char ' '
      char handler = (char) asciiInt;
      if (handlers.containsKey(this)) {
        handlers.get(this).handle(String.valueOf(handler));
      }
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
