

import org.apache.commons.lang.StringEscapeUtils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.io.OutputStream;

public class XmlEncoder extends DefaultHandler {
    private final OutputStream os;
    private final static String DOC_START = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" +
            "<!DOCTYPE html\n" +
            "  PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n";

    public XmlEncoder(final OutputStream os) {
        this.os = os;
    }

    @Override
    public void startDocument() throws SAXException {
        try {
            os.write(DOC_START.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void startElement(final String uri, final String localName, final String qName, final Attributes attributes) throws SAXException {
        try {
            os.write(("<" + qName).getBytes());
            for (int i = 0; i < attributes.getLength(); i++) {
                os.write((" " + attributes.getQName(i) + "=\"" + StringEscapeUtils.escapeXml(attributes.getValue(i)) + "\"").getBytes());
            }
            os.write(">".getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void endElement(final String uri, final String localName, final String qName) throws SAXException {
        try {
            os.write(("</" + qName + ">").getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void characters(final char[] ch, final int start, final int length) throws SAXException {
        try {
            os.write(StringEscapeUtils.escapeXml(new String(ch, start, length)).getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
	
    @Override
    public void endDocument() throws SAXException {
        try {
            os.flush();
			os.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }	
}
