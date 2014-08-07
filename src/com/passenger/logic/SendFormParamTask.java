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
 * 发起带参数的HttpClient的post网络请求
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
			// 表示使用post方法
			// 访问网络，使用httpClient

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
			// 设置参数
			post.setEntity(entity);
			request = post;

			// 创建请求体
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
			System.out.println("编码不支持异常！");
		} catch (ClientProtocolException e) {
			System.out.println("客户端协议异常！");
		} catch (IllegalStateException e) {
			System.out.println("不合法的状态异常！");
		} catch (IOException e) {
			System.out.println("IO异常");
		} finally {
			try {
				is.close();
				isr.close();
				br.close();
			} catch (IOException e) {
				System.out.println("IO异常");
			}
		}
		return s;
	}

	/**
	 * 当访问网络结束后，客户端获取网络端的数据后，在主线程运行的代码
	 */
	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
	}

}
