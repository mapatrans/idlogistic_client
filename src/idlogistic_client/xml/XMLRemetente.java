package idlogistic_client.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="remetente")
@XmlAccessorType(XmlAccessType.FIELD)
public class XMLRemetente {

	private String cnpjLocal;

	public String getCnpjLocal() {
		return cnpjLocal;
	}

	public void setCnpjLocal(String cnpjLocal) {
		this.cnpjLocal = cnpjLocal;
	}
}
