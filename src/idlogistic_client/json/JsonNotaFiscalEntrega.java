package idlogistic_client.json;

public class JsonNotaFiscalEntrega {

	private String cnpjRem;
	private String numero;
	private String serie;
	private String ocorrencia;
	private String dataEntrega;
	private String cnpjTransportadora;
	
	public String getCnpjRem() {
		return cnpjRem;
	}
	public void setCnpjRem(String cnpjRem) {
		this.cnpjRem = cnpjRem;
	}
	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	public String getSerie() {
		return serie;
	}
	public void setSerie(String serie) {
		this.serie = serie;
	}
	public String getOcorrencia() {
		return ocorrencia;
	}
	public void setOcorrencia(String ocorrencia) {
		this.ocorrencia = ocorrencia;
	}
	public String getDataEntrega() {
		return dataEntrega;
	}
	public void setDataEntrega(String dataEntrega) {
		this.dataEntrega = dataEntrega;
	}
	public String getCnpjTransportadora() {
		return cnpjTransportadora;
	}
	public void setCnpjTransportadora(String cnpjTransportadora) {
		this.cnpjTransportadora = cnpjTransportadora;
	}
}
