package idlogistic_client;

import idlogistic_client.json.JsonEntrega;
import idlogistic_client.json.JsonNotaFiscalEntrega;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class LerEntregaThread implements Runnable {
	private Integer sleep;
	private HttpGet httpGet;
	private String txtDir;


	public LerEntregaThread(Integer sleep, String txtDir, String transpId, String url) {
		this.sleep = sleep;
		this.txtDir = txtDir;
		httpGet = new HttpGet(url + "?t=" + transpId);
	}
	
	@Override
	public void run() {
		while(true) {
			lerEntrega();
			
			try {
				Thread.sleep(sleep * 60 * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
				break;
			}
		}
	}


	private void lerEntrega() {
		CloseableHttpClient httpclient = null;
		CloseableHttpResponse response = null;
		try {
			httpclient = HTTPClientFactory.createHttpsClient();
			response = httpclient.execute(httpGet);
			String jsonStr = EntityUtils.toString(response.getEntity());
			
			lerJson(jsonStr);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (response != null) {
				try {
					response.close();
					httpclient.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		}
		
	}


	public void lerJson(String jsonStr) throws ParseException {
		System.out.println(jsonStr);
		
		Gson gson = new GsonBuilder().create();
		JsonEntrega json = gson.fromJson(jsonStr, JsonEntrega.class);
		
		if (json == null || json.getNotas() == null || json.getNotas().isEmpty())
			return;
		
		String cnpjTranspAtual = json.getNotas().get(0).getCnpjTransportadora();
		List<JsonNotaFiscalEntrega> nfsPorTrans = new ArrayList<JsonNotaFiscalEntrega>();
		for (JsonNotaFiscalEntrega n : json.getNotas()) {
			if (cnpjTranspAtual.equals(n.getCnpjTransportadora())) {
				nfsPorTrans.add(n);
			} else {
				criarTxtEntrega(nfsPorTrans);
				nfsPorTrans.clear();
				cnpjTranspAtual = n.getCnpjTransportadora();

				nfsPorTrans.add(n);
			}
		}
		
		criarTxtEntrega(nfsPorTrans);
	}


	private void criarTxtEntrega(List<JsonNotaFiscalEntrega> nfs) throws ParseException {
		StringBuilder conteudoB = new StringBuilder("000");
		conteudoB.append(completarComEspaco("KIT", 35));
		conteudoB.append(completarComEspaco("TMS", 35));
		String dt = new SimpleDateFormat("ddMMyy").format(new Date());
		conteudoB.append(dt);
		conteudoB.append(new SimpleDateFormat("HHmm").format(new Date()));
		dt = new SimpleDateFormat("ddMM").format(new Date());
		//conteudoB.append(completarComEspaco("OCO" + dt, 12));
		conteudoB.append("OCO" + dt);
		conteudoB.append(new SimpleDateFormat("HHmm").format(new Date()));
		conteudoB.append("0");
		conteudoB.append(completarComEspaco("", 25));
		conteudoB.append("\n");
		
		conteudoB.append("340");
		conteudoB.append("OCORR" + dt);
		conteudoB.append(new SimpleDateFormat("HHmm").format(new Date()));
		conteudoB.append("0");		
		conteudoB.append(completarComEspaco("", 103));
		conteudoB.append("\n");
		
		conteudoB.append("341");
		conteudoB.append(nfs.get(0).getCnpjTransportadora());
		conteudoB.append(completarComEspaco("IDLOGISTIC", 40));
		conteudoB.append(completarComEspaco("", 63));
		conteudoB.append("\n");
		
		for (JsonNotaFiscalEntrega n : nfs) {
			String dataEntregaStr = n.getDataEntrega();
			Date dataEntrega = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss aaa").parse(dataEntregaStr);
			String cnpjRem = n.getCnpjRem();
			String numero = n.getNumero();
			String ocorrencia = n.getOcorrencia();
			String serie = n.getSerie();
			
			conteudoB.append("342");
			conteudoB.append(cnpjRem);
			conteudoB.append(completarComEspaco(serie, 3));
			conteudoB.append("00000000");
			
			if (ocorrencia.trim().equals("SU")) {
				conteudoB.append("001");
			} else {
				conteudoB.append(completarComEspaco(ocorrencia, 3));	
			}
			
			conteudoB.append(new SimpleDateFormat("ddMMyyyy").format(dataEntrega));
			conteudoB.append(new SimpleDateFormat("HHmm").format(dataEntrega));
			conteudoB.append("00");
			conteudoB.append(completarComEspaco("", 79));
			conteudoB.append(completarComZeros(numero,9));
			conteudoB.append(completarComEspaco("", 70));
			conteudoB.append("\n");
			
		}
		
		 // Gravar arquivo de retorno
        FileWriter fileWriter = null;
        try {
            File entregaTxt = new File(txtDir + "/entrega_" + System.currentTimeMillis() + ".txt");
            entregaTxt.createNewFile();
            fileWriter = new FileWriter(entregaTxt);
            fileWriter.write(conteudoB.toString());
            fileWriter.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                fileWriter.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
	}
	
	private String completarComEspaco(String str, int tam) {
		while(str.length() < tam) {
			str += " ";
		}
		return str;
	}

	private String completarComZeros(String str, int tam) {
		while(str.length() < tam) {
			str = "0" + str.trim();
			
		}
		return str;
	}

}

