package mdn.vtvpluspro.object;

import mdn.vtvpluspro.parsexml.LoginInfoHandler;
import mdn.vtvpluspro.util.HLog;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.StringReader;

/**
 * Created by hieuxit on 8/25/14.
 */
public class LoginInfo {
	public String mPhoneNumber;
	public String mAccountName;
	public String mAccountPassword;

	public static LoginInfo fromResponse(String response) {
		try {
			SAXParserFactory saxPF = SAXParserFactory.newInstance();
			SAXParser saxP = saxPF.newSAXParser();
			XMLReader xmlR = saxP.getXMLReader();
			LoginInfoHandler myXMLHandler = new LoginInfoHandler();
			xmlR.setContentHandler(myXMLHandler);
			xmlR.parse(new InputSource(new StringReader(response)));
			return myXMLHandler.getData();
		} catch (Exception e) {
			e.printStackTrace();
			HLog.e("Parse xml error: " + e.toString());
		}
		return new LoginInfo();
	}

	@Override
	public String toString() {
		return "[username:" + mAccountName + ", pass: " + mAccountPassword + "]";
	}
}
