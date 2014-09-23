package idlogistic_client;

import idlogistic_client.json.JsonConhecimento;
import idlogistic_client.json.JsonMotorista;
import idlogistic_client.json.JsonNotaFiscal;
import idlogistic_client.json.JsonOrdemTransporte;
import idlogistic_client.xml.XMLConhecimento;
import idlogistic_client.xml.XMLMarshaller;
import idlogistic_client.xml.XMLMotorista;
import idlogistic_client.xml.XMLNotaFiscal;
import idlogistic_client.xml.XMLOrdemTransporte;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.http.Consts;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class KitWS {
	 public static final String UTF8_BOM = "\uFEFF";
	// Arquivo que contem as configuracoes. Deve estar localizado no mesmo nível que o diretorio do projeto
	public static Properties CONFIG_PROP = new Properties() {
		private static final long serialVersionUID = 9062726191143976752L;
	{
		try {
			load(new FileInputStream("../config.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}};

	private static FilenameFilter FILTRO_ARQUIVOS_XML = new FilenameFilter() {
		@Override
		public boolean accept(File dir, String name) {
			return name.endsWith(".xml");
		}
	};

	public static void main(String[] args) {
		
		Integer sleep = Integer.valueOf(CONFIG_PROP.getProperty("ler.entrega.intervalo"));
		String txtDir = CONFIG_PROP.getProperty("txt.diretorio.caminho");
		String transpId = CONFIG_PROP.getProperty("transportadora.id");
		String url = CONFIG_PROP.getProperty("leitura.txt.url");

		
		new Thread(new LerEntregaThread(sleep, txtDir, transpId, url)).start();
		
		while(true) {
			try {
				enviarOrdemTransporte();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}

	private static void enviarOrdemTransporte() {
		String caminhoDirXML = CONFIG_PROP.getProperty("xml.diretorio.caminho");
		File diretorioXML = new File(caminhoDirXML);
		if (diretorioXML.exists()) {
			for (File file : diretorioXML.listFiles(FILTRO_ARQUIVOS_XML)) {
				try {
                    String xmlStr = lerXml(file);
                    xmlStr = removeUTF8BOM(xmlStr);
					XMLOrdemTransporte ordemTransporte = XMLMarshaller.unmarshal(xmlStr, XMLOrdemTransporte.class);

					JsonOrdemTransporte json = montarJson(ordemTransporte);
					Gson gson = new GsonBuilder().create();
					String jsonStr = gson.toJson(json);
					
					enviarJson(jsonStr);

					// Renomeando para .old
					file.renameTo(new File(caminhoDirXML + "/" + file.getName() + new Date().getTime() + ".old"));
				} catch (Exception e) {
					file.renameTo(new File(caminhoDirXML + "/" + file.getName() + new Date().getTime() + ".err"));
					e.printStackTrace();
				}
			}
		} else {
			throw new RuntimeException("Caminho especificado no config.properties para diretorio de xml nao existe!");
		}
	}

	private static void enviarJson(String jsonStr) throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("j", jsonStr));
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, Consts.UTF_8);
		
		CloseableHttpClient httpclient = HTTPClientFactory.createHttpsClient();
		
		HttpPost httpPost = new HttpPost(CONFIG_PROP.getProperty("envio.xml.url"));
		httpPost.setEntity(entity);

		CloseableHttpResponse response = null;
		try {
			response = httpclient.execute(httpPost);
			System.out.println(response.getStatusLine().getStatusCode());
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
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

	private static JsonOrdemTransporte montarJson(XMLOrdemTransporte xml) {
		JsonOrdemTransporte json = new JsonOrdemTransporte();
		List<JsonConhecimento> jsonConhecimentos = new ArrayList<JsonConhecimento>();
		for (XMLConhecimento xmlConhecimento : xml.getCtrc()) {
			JsonConhecimento jsonConhecimento = new JsonConhecimento();
			List<JsonNotaFiscal> jsonNotas = new ArrayList<JsonNotaFiscal>();
			for (XMLNotaFiscal xmlNotaFiscal : xmlConhecimento.getNotaFiscal()) {
				JsonNotaFiscal jsonNotaFiscal = new JsonNotaFiscal();
				jsonNotaFiscal.setNumero(xmlNotaFiscal.getNumero());
				jsonNotaFiscal.setSerie(xmlNotaFiscal.getSerie());
				
				jsonNotas.add(jsonNotaFiscal);
			}
			jsonConhecimento.setCnpjRem(xmlConhecimento.getRemetente().getCnpjLocal());
			jsonConhecimento.setNomeDest(xmlConhecimento.getDestinatario().getNomeLocal());
			jsonConhecimento.setNumero(xmlConhecimento.getNumeroCTRC());
			jsonConhecimento.setNotas(jsonNotas);
			
			jsonConhecimentos.add(jsonConhecimento);
		}
		
		JsonMotorista jsonMotorista = new JsonMotorista();
		XMLMotorista xmlMotorista = xml.getMotorista();
		jsonMotorista.setCnh(xmlMotorista.getCpf());
		jsonMotorista.setCpf(xmlMotorista.getCpf());
		jsonMotorista.setCodTMS(xmlMotorista.getCodTMS());
		jsonMotorista.setNome(xmlMotorista.getNome());
		
		json.setTransportadora(CONFIG_PROP.getProperty("transportadora.id"));
		json.setCNPJTransportadora(xml.getTransportadora().getCnpj());
		json.setConhecimentos(jsonConhecimentos);
		json.setMotorista(jsonMotorista);

		return json;
	}

	private static String lerXml(File arquivo) {
		StringBuilder xml = new StringBuilder();
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(
					new FileInputStream(arquivo), "UTF-8"));
			String linha;
			while ((linha = in.readLine()) != null) {
				xml.append(linha);
			}
			in.close();
		} catch (IOException e) {
			System.out.println("-------------------------- PROBLEMA NA LEITURA DO ARQUIVO DISPONIBILIZADO NO DIRETORIO ------------------------------");
			e.printStackTrace();
		}
		return xml.toString();
	}
	
	private static String removeUTF8BOM(String s) {
        if (s.startsWith(UTF8_BOM)) {
            s = s.substring(1);
        }
        return s;
    }
}
