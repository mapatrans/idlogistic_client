package idlogistic_client.json;

import java.util.List;

public class JsonConhecimento {

	private String numero;
	private String cnpjRem;
	private String nomeDest;
	private List<JsonNotaFiscal> notas;
	
	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	public String getCnpjRem() {
		return cnpjRem;
	}
	public void setCnpjRem(String cnpjRem) {
		this.cnpjRem = cnpjRem;
	}
	public String getNomeDest() {
		return nomeDest;
	}
	public void setNomeDest(String nomeDest) {
		this.nomeDest = nomeDest;
	}
	public List<JsonNotaFiscal> getNotas() {
		return notas;
	}
	public void setNotas(List<JsonNotaFiscal> notas) {
		this.notas = notas;
	}
	
}
