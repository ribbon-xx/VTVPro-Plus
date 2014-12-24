package mdn.vtvsport.parsexml;

import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

public class ParserListStream extends DefaultHandler {

	Boolean currentElement = false;
	String currentValue = "";
	String linkStream = "";
	private ArrayList<String> listStream = new ArrayList<String>();

	public ArrayList<String> getItemsList() {
		return listStream;
	}

	// Called when tag starts
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {

		currentElement = true;
		currentValue = "";
		if (localName.equals("stream")) {
		}

	}

	// Called when tag closing
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {

		currentElement = false;

		/** set value */
		if (localName.equalsIgnoreCase("url"))
			linkStream = currentValue;
		else if (localName.equalsIgnoreCase("stream"))
			listStream.add(linkStream);
	}

	// Called to get tag characters
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {

		if (currentElement) {
			currentValue = currentValue + new String(ch, start, length);
		}

	}

	public static ArrayList<String> parse(String response) {

		try {
			/** Handling XML */
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();
			XMLReader xr = sp.getXMLReader();

			ParserListStream listLeaderboard = new ParserListStream();

			xr.setContentHandler(listLeaderboard);
			InputSource inStream = new InputSource();

			inStream.setCharacterStream(new StringReader(response));
			xr.parse(inStream);

			ArrayList<String> itemsList = listLeaderboard.getItemsList();
			return itemsList;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}
}