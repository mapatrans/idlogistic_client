package idlogistic_client.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="motorista")
@XmlAccessorType(XmlAccessType.FIELD)
public class XMLMotorista {

	private String codigoTMS;
	private String nome;
	private String cpf;
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getCpf() {
		return cpf;
	}
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	public String getCodTMS() {
		return codigoTMS;
	}
	public void setCodTMS(String codTMS) {
		this.codigoTMS = codTMS;
	}
}
