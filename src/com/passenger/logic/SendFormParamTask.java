package com.passenger.logic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.os.AsyncTask;

/**
 * �����������HttpClient��post��������
 * @author bianyixuan
 *
 */
public class SendFormParamTask extends AsyncTask<Map, Integer, String> {

	@Override
	protected String doInBackground(Map... params) {
		InputStream is = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		String s = "";
		try {
			Map map = null;
			Map<String, Object> param = params[0];
			HttpUriRequest request = null;
			HttpClient client = new DefaultHttpClient();
			// ��ʾʹ��post����
			// �������磬ʹ��httpClient

			List<NameValuePair> params1 = new ArrayList<NameValuePair>();
			for (Map.Entry<String, Object> entry : param.entrySet()) {
				params1.add(new BasicNameValuePair(entry.getKey(),
						(String) entry.getValue()));
			}
			if (params.length > 1) {
				map = params[1];
			}

			HttpPost post = new HttpPost(map.get("url").toString());
			HttpEntity entity = new UrlEncodedFormEntity(params1);
			// ���ò���
			post.setEntity(entity);
			request = post;

			// ����������
			HttpResponse response = client.execute(request);
			is = response.getEntity().getContent();
			isr = new InputStreamReader(is);
			br = new BufferedReader(isr);
			StringBuffer buffer = new StringBuffer();
			String str = "";
			while ((str = br.readLine()) != null) {
				buffer.append(str);
			}
			s = buffer.toString();
		} catch (UnsupportedEncodingException e) {
			System.out.println("���벻֧���쳣��");
		} catch (ClientProtocolException e) {
			System.out.println("�ͻ���Э���쳣��");
		} catch (IllegalStateException e) {
			System.out.println("���Ϸ���״̬�쳣��");
		} catch (IOException e) {
			System.out.println("IO�쳣");
		} finally {
			try {
				is.close();
				isr.close();
				br.close();
			} catch (IOException e) {
				System.out.println("IO�쳣");
			}
		}
		return s;
	}

	/**
	 * ��������������󣬿ͻ��˻�ȡ����˵����ݺ������߳����еĴ���
	 */
	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
	}

}
