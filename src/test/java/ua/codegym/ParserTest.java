package ua.codegym;

import org.junit.Assert;
import org.junit.Test;
import ua.codegym.impl.XmlFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static ua.codegym.Event.*;

public class ParserTest {


  EventHandler handler = new EventHandler() {
    //@Override
    public void handle(String value) {
      System.out.println(value);
    }
  };
  EventHandler fail = new EventHandler() {
    //@Override
    public void handle(String value) {
      Assert.fail();
    }
  };

  @Test
  public void checkElement() throws IOException{

    AssertHandler startElementHandler = new AssertHandler("a");
    AssertHandler endElementHandler = new AssertHandler("a");

    Parser parser = XmlFactory.newBuilder()  //создаем "общий" экземпл€р класса
        .on(START_ELEMENT, startElementHandler) //поочереди устанавливаютс€ все желаемые параметры
        .on(ATTRIBUTE_NAME, fail)
        .on(ATTRIBUTE_VALUE, fail)
        .on(VALUE, fail)
        .on(END_ELEMENT, endElementHandler)
        .on(ERROR, fail)
        .build();   // создает экземпл€р класса с вышеперечисленными параметрами

    parser.parse(toInputStream("<a></a>"));

    startElementHandler.check();
  }

  @Test
  public void checkNestedElements() throws IOException{

    AssertHandler startElementHandler = new AssertHandler("a", "b", "c", "d");

    Parser parser = XmlFactory.newBuilder()
        .on(START_ELEMENT, startElementHandler)
        .on(ERROR, fail)
        .build();

    parser.parse(toInputStream("<a><b></b><c><d></d></c></a>"));

    startElementHandler.check();
  }

  @Test
  public void checkAttributes() throws IOException{
    AssertHandler attrHandler = new AssertHandler("x", "y");
    AssertHandler attrValueHandler = new AssertHandler("vx", "vy");

    Parser parser = XmlFactory.newBuilder()
        .on(ATTRIBUTE_NAME, attrHandler)
        .on(ATTRIBUTE_VALUE, attrValueHandler)
        .on(ERROR, fail)
        .build();

    parser.parse(toInputStream("<a x=\"vx\" y = \"vy\" ></ a>"));

    attrHandler.check();
    attrValueHandler.check();
  }

  @Test
  public void checkErrorHandling() throws IOException{
    AssertHandler errorHandler = new AssertHandler("Xml is not valid. Error at 1:13");

    Parser parser = XmlFactory.newBuilder()
        .on(ERROR, errorHandler)
        .build();

    parser.parse(toInputStream("<a attr=\"abc\"error\"abc\" ></a>"));

    errorHandler.check();
  }

  private InputStream toInputStream(String s) {
    ByteArrayInputStream in = new ByteArrayInputStream(s.getBytes());
    return in;
  }
}
