package idlogistic_client.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="ctrc")
@XmlAccessorType(XmlAccessType.FIELD)
public class XMLConhecimento {

	private String numeroCTRC;
	
	private XMLRemetente remetente;
	
	private XMLDestinatario destinatario;
	
	@XmlElementWrapper(name="notasFiscais")
	private List<XMLNotaFiscal> notaFiscal;

	public String getNumeroCTRC() {
		return numeroCTRC;
	}

	public void setNumeroCTRC(String numeroCTRC) {
		this.numeroCTRC = numeroCTRC;
	}

	public XMLRemetente getRemetente() {
		return remetente;
	}

	public void setRemetente(XMLRemetente remetente) {
		this.remetente = remetente;
	}

	public XMLDestinatario getDestinatario() {
		return destinatario;
	}

	public void setDestinatario(XMLDestinatario destinatario) {
		this.destinatario = destinatario;
	}

	public List<XMLNotaFiscal> getNotaFiscal() {
		return notaFiscal;
	}

	public void setNotaFiscal(List<XMLNotaFiscal> notaFiscal) {
		this.notaFiscal = notaFiscal;
	}
	
	
}
