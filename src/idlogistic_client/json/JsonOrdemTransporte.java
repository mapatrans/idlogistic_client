package idlogistic_client.json;

import java.util.List;

public class JsonOrdemTransporte {
	
	private String transportadora;
	private JsonMotorista motorista;
	private List<JsonConhecimento> conhecimentos;
	private String cnpjTransportadora;
	public String getTransportadora() {
		return transportadora;
	}
	public void setTransportadora(String transportadora) {
		this.transportadora = transportadora;
	}
	public JsonMotorista getMotorista() {
		return motorista;
	}
	public void setMotorista(JsonMotorista motorista) {
		this.motorista = motorista;
	}
	public List<JsonConhecimento> getConhecimentos() {
		return conhecimentos;
	}
	public void setConhecimentos(List<JsonConhecimento> conhecimentos) {
		this.conhecimentos = conhecimentos;
	}
	public void setCNPJTransportadora(String cnpj) {
		this.cnpjTransportadora = cnpj;
	}
	public String getCnpj() {
		return cnpjTransportadora;
	}
}
