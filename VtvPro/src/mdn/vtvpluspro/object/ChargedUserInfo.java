package mdn.vtvpluspro.object;

import mdn.vtvpluspro.parsexml.ChargingUserHandler;
import mdn.vtvpluspro.util.HLog;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.StringReader;

/**
 * Created by hieuxit on 8/12/14.
 */
public class ChargedUserInfo {
	public int error;
	public int registered;
	public String note;

	public static ChargedUserInfo fromResponse(String response) {
		try {
			SAXParserFactory saxPF = SAXParserFactory.newInstance();
			SAXParser saxP = saxPF.newSAXParser();
			XMLReader xmlR = saxP.getXMLReader();
			ChargingUserHandler myXMLHandler = new ChargingUserHandler();
			xmlR.setContentHandler(myXMLHandler);
			xmlR.parse(new InputSource(new StringReader(response)));
			return myXMLHandler.getData();
		} catch (Exception e) {
			e.printStackTrace();
			HLog.e("Parse xml error: " + e.toString());
		}
		return new ChargedUserInfo();
	}

	@Override
	public String toString() {
		return "[error:" + error + ", registered: " + registered + ", note: " + note + "]";
	}
}
