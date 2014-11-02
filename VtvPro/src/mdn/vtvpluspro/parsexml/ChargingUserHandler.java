package mdn.vtvpluspro.parsexml;

import mdn.vtvpluspro.object.ChargedUserInfo;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Created by hieuxit on 8/12/14.
 */
public class ChargingUserHandler extends DefaultHandler {

	// this holds the data
	private ChargedUserInfo _data;
	String elementValue = null;
	Boolean elementOn = false;

	/**
	 * Returns the data object
	 *
	 * @return
	 */
	public ChargedUserInfo getData() {
		return _data;
	}

	/**
	 * This gets called at the start of an element. Here we're also setting the booleans to true if it's at that specific tag. (so we
	 * know where we are)
	 *
	 * @param namespaceURI
	 * @param localName
	 * @param qName
	 * @param atts
	 * @throws SAXException
	 */
	@Override
	public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
		elementOn = true;
		if (localName.equals("response")) {
			_data = new ChargedUserInfo();
		}else if(localName.equals("registered_service_list")){
			try {
				_data.registered = Integer.parseInt(atts.getValue("registered"));
				_data.note = atts.getValue("note");
			}catch (Exception e){
				_data.registered = 0;
			}
		}
	}

	/**
	 * Called at the end of the element. Setting the booleans to false, so we know that we've just left that tag.
	 *
	 * @param namespaceURI
	 * @param localName
	 * @param qName
	 * @throws SAXException
	 */
	@Override
	public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
		elementOn = false;

		/**
		 * Sets the values after retrieving the values from the XML tags
		 * */
		if (localName.equalsIgnoreCase("error_no")) {
			try {
				_data.error = Integer.parseInt(elementValue);
			} catch (Exception e) {
				_data.error = 1;
			}
		}
	}

	/**
	 * Calling when we're within an element. Here we're checking to see if there is any content in the tags that we're interested in
	 * and populating it in the Config object.
	 *
	 * @param ch
	 * @param start
	 * @param length
	 */
	@Override
	public void characters(char ch[], int start, int length) {
		if (elementOn) {
			elementValue = new String(ch, start, length);
			elementOn = false;
		}
	}
}
