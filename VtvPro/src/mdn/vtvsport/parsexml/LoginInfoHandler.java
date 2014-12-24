package mdn.vtvsport.parsexml;

import mdn.vtvsport.object.LoginInfo;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Created by hieuxit on 8/12/14.
 */
public class LoginInfoHandler extends DefaultHandler {

	// this holds the data
	private LoginInfo _data;
	String elementValue = null;
	Boolean elementOn = false;

	/**
	 * Returns the data object
	 *
	 * @return
	 */
	public LoginInfo getData() {
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
	 * @throws org.xml.sax.SAXException
	 */
	@Override
	public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
		elementOn = true;
		if (localName.equals("result")) {
			_data = new LoginInfo();
		}
	}

	/**
	 * Called at the end of the element. Setting the booleans to false, so we know that we've just left that tag.
	 *
	 * @param namespaceURI
	 * @param localName
	 * @param qName
	 * @throws org.xml.sax.SAXException
	 */
	@Override
	public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
		elementOn = false;

		/**
		 * Sets the values after retrieving the values from the XML tags
		 * */
		if (localName.equalsIgnoreCase("phone")) {
			_data.mPhoneNumber = elementValue;
		}else if(localName.equalsIgnoreCase("username")){
			_data.mAccountName = elementValue;
		}else if(localName.equalsIgnoreCase("matkhau")){
			_data.mAccountPassword = elementValue;
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
