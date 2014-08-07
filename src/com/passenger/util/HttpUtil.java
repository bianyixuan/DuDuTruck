package com.passenger.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

public class HttpUtil {

	/**
	 * http�������ݣ�Ĭ�Ϸ��ص�����ΪUTF-8����
	 * 
	 * @param url
	 *            url��ַ
	 * @param method
	 *            �ύ��ʽ
	 * @return
	 * @throws IOException
	 */
	public static String getUrl(String url) throws IOException {
		HttpGet request = new HttpGet(url);
		HttpClient httpClient = new DefaultHttpClient();
		HttpResponse response = httpClient.execute(request);
		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			return EntityUtils.toString(response.getEntity());
		} else {
			return "";
		}
	}

	/**
	 * post�ύ����
	 * 
	 * @param url
	 *            �ύ��ַ
	 * @param params
	 *            ����
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String postUrl(String url, Map<String, String> params)
			throws ClientProtocolException, IOException {
		return postUrl(url, params, "UTF-8");
	}

	/**
	 * post�ύ����
	 * 
	 * @param url
	 *            �ύ��ַ
	 * @param params
	 *            ����
	 * @param encoding
	 *            ��������
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String postUrl(String url, Map<String, String> params,
			String encoding) throws ClientProtocolException, IOException {
		List<NameValuePair> param = new ArrayList<NameValuePair>(); // ����
		// param.add(new BasicNameValuePair("par", "request-post"));
		// //��������Ҳ�֪���ǲ��Ƿ�Ҫ�����

		// ��Ӳ���
		Iterator<Entry<String, String>> iterator = params.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<String, String> entry = iterator.next();

			param.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
		}

		HttpPost request = new HttpPost(url);
		HttpEntity entity = new UrlEncodedFormEntity(param, encoding);
		request.setEntity(entity);

		HttpClient client = new DefaultHttpClient();
		client.getParams().setParameter(
				CoreConnectionPNames.CONNECTION_TIMEOUT, 10000); // ��������ʱʱ��
		client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 10000); // ��ȡ��ʱ

		HttpResponse response = client.execute(request);
		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			return EntityUtils.toString(response.getEntity());
		} else {
			return null;
		}
	}

}
