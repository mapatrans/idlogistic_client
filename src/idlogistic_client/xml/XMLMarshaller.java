package idlogistic_client.xml;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

public class XMLMarshaller {

	@SuppressWarnings("unchecked")
	public static <T> T unmarshal(final String xml, Class<T> clazz) throws JAXBException {
		return (T) JAXBContext.newInstance(clazz).createUnmarshaller().unmarshal(new StringReader(xml));
	}

	public static <T> String marshall(T obj) {
		try {
			Marshaller m = JAXBContext.newInstance(obj.getClass()).createMarshaller();
			m.setProperty("com.sun.xml.bind.xmlDeclaration", Boolean.FALSE);
			final StringWriter w = new StringWriter();
			m.marshal(obj, w);
			return w.toString();
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return null;
	}
}
