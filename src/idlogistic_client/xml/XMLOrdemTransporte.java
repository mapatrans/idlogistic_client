package idlogistic_client.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="ordemOrdemTransporte")
@XmlAccessorType(XmlAccessType.FIELD)
public class XMLOrdemTransporte {

	private XMLMotorista motorista;

	@XmlElementWrapper(name="ctrcs")
	private List<XMLConhecimento> ctrc;
	
	private XMLTransportadora transportadora;

	public XMLMotorista getMotorista() {
		return motorista;
	}

	public void setMotorista(XMLMotorista motorista) {
		this.motorista = motorista;
	}

	public List<XMLConhecimento> getCtrc() {
		return ctrc;
	}

	public void setCtrc(List<XMLConhecimento> ctrc) {
		this.ctrc = ctrc;
	}

	public XMLTransportadora getTransportadora() {
		return transportadora;
	}

	public void setTransportadora(XMLTransportadora transportadora) {
		this.transportadora = transportadora;
	}	
}
