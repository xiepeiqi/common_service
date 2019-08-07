package com.xpq.cs.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * http请求服务工具
 * @author xiepeiqi @date 2019年8月6日
 */
public class HttpUtils {
	
	private static final Logger LOGGER=LoggerFactory.getLogger(HttpUtils.class);

	/**
	 * 请求超时，短时间，8,单位秒
	 */
	private final static int TIMEOUT_SHORT_SECOND = 8;

	/**
	 * 释放资源
	 * @param HttpsResponseResult
	 * @param client
	 * @param response
	 * @date 2019年8月6日 @time 下午4:42:26
	 * @author xiepeiqi
	 */
	public static void releaseResources(HttpHttpsResponseResult HttpsResponseResult, CloseableHttpClient client,
			CloseableHttpResponse response) {
		if (response == null) {// 请求失败处理
			if (HttpsResponseResult!=null) {
				HttpsResponseResult.setStatusCode(-1);
			}
		}

		if (response != null) {
			try {
				response.close();// 关闭链接
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if (client != null) {
			try {
				client.close();// 释放资源
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * 释放资源
	 * @param HttpsResponseResult
	 * @param client
	 * @param response
	 * @param inputStream
	 * @author xiepeiqi @date 2019年8月6日
	 */
	public static void releaseResources(HttpHttpsResponseResult HttpsResponseResult, CloseableHttpClient client,
			CloseableHttpResponse response ,InputStream inputStream) {
		if (response == null) {// 请求失败处理
			if (HttpsResponseResult!=null) {
				HttpsResponseResult.setStatusCode(-1);
			}
		}
		
		if (response != null) {
			try {
				response.close();// 关闭链接
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		if (client != null) {
			try {
				client.close();// 释放资源
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (inputStream != null) {
			try {
				inputStream.close();// 释放资源
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 处理http/https请求的返回结果
	 * @param HttpsResponseResult
	 * @param response
	 * @author xiepeiqi @date 2019年8月6日
	 */
	public static  void dealWithHttpResponse(HttpHttpsResponseResult HttpsResponseResult, CloseableHttpResponse response) {
		InputStream content = null;
		ByteArrayOutputStream out = null;
		HttpsResponseResult.setStatusCode(response.getStatusLine().getStatusCode());// 设置状态码
		Header[] allHeaders = response.getAllHeaders();
		if (allHeaders != null && allHeaders.length > 0) {
			Map<String, String> headersMap = new HashMap<String, String>();
			for (Header header : allHeaders) {
				headersMap.put(header.getName(), header.getValue());
			}
			HttpsResponseResult.setHeaders(headersMap);// 设置响应头信息
		}
		if ( response.getEntity() != null) {
			try {
				content=response.getEntity().getContent();
				out = new ByteArrayOutputStream();
				IOUtils.copy(content, out);
				HttpsResponseResult.setObject(out);// 设置获取的内容
			} catch (ParseException  | IOException e) {
				e.printStackTrace();
			} finally {
					try {
						if (out != null) {
							out.close();// 释放字节流
						}
						if (content != null) {
							content.close();// 释放输入流
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
			}
		}
	}
	
	/**
	 * 发送get请求
	 * @param url
	 * @return
	 * @date 2019年8月6日 @time 下午5:42:01
	 * @author xiepeiqi
	 */
	public static HttpHttpsResponseResult httpGet(String url) {
		HttpHttpsResponseResult HttpsResponseResult = new HttpHttpsResponseResult();
		// 创建httpclient对象
		CloseableHttpClient client =  HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(url);
		RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(TIMEOUT_SHORT_SECOND * 1000).setSocketTimeout(TIMEOUT_SHORT_SECOND * 1000).setConnectTimeout(TIMEOUT_SHORT_SECOND * 1000).build();// 设置请求和传输超时时间
		httpGet.setConfig(requestConfig);
		CloseableHttpResponse response = null;
		try {
			response = client.execute(httpGet);// 执行请求操作，并拿到结果（同步阻塞）
			dealWithHttpResponse(HttpsResponseResult, response);
		} catch (Exception e) {
			LOGGER.error("get请求失败! url:[{}]，errorMessage :{} ", url,e.getMessage());
		} finally {
			releaseResources(HttpsResponseResult, client, response);
			httpGet=null;
		}
		return HttpsResponseResult;
	}
	
	/**
	 * 把图片的url转为输入流
	 * @param imgUrl
	 * @return
	 * @date 2019年8月6日 @time 下午5:41:52
	 * @author xiepeiqi
	 */
	public static InputStream urlToInputStream(Object imgUrl) {
		if (imgUrl == null || StringUtils.isBlank(imgUrl.toString())) {
			return null;
		}
		HttpHttpsResponseResult result = httpGet(imgUrl.toString());
		// 设置返回图片结果
		if (result.getObject() != null) {
			return new ByteArrayInputStream( ((ByteArrayOutputStream) result.getObject()).toByteArray());
		}
		return null;
	}
	
	/**
	 * 把图片的url转为byte数组
	 * @param imgUrl
	 * @return
	 * @date 2019年8月6日 @time 下午5:43:37
	 * @author xiepeiqi
	 */
	public static byte[] urlToByte(Object imgUrl) {
		if (imgUrl == null || StringUtils.isBlank(imgUrl.toString())) {
			return null;
		}
		HttpHttpsResponseResult result = httpGet(imgUrl.toString());
		// 设置返回图片结果
		if (result.getObject() != null) {
			return ((ByteArrayOutputStream) result.getObject()).toByteArray();
		}
		return null;
	}
	
	
	/**
	 * post请求，针对下面两种请求情况:
	 * multipart/form-data
	 * application/x-www-form-urlencoded
	 * 使用方法，当bytes和byteNames参数有上传且符合上传要求时，请求头会自动设置为 multipart/form-data
	 * 其它情况下为 application/x-www-form-urlencoded
	 * @param httpUrl 请求url
	 * @param headerParams 请求头映射。可不传
	 * @param bodyParams 请求实体映射。可不传
	 * @param bytes 文件的字节数组集合。长度要与文件名集合相等，可不传
	 * @param byteNames 文件名集合。长度要与文件字节数组集合相等，可不传
	 * @return
	 * @author xiepeiqi @date 2019年8月6日
	 */
	public static HttpHttpsResponseResult  postTypeGeneral(String httpUrl,Map<String, String>headerParams,Map<String, String>bodyParams,List<byte[]> bytes,List<String> byteNames) {
		CloseableHttpResponse response = null;
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(TIMEOUT_SHORT_SECOND*1000).setConnectTimeout(TIMEOUT_SHORT_SECOND*1000).build();// 设置请求和传输超时时间
		CloseableHttpClient client=HttpClients.createDefault();
		HttpPost post=null;
		post=new HttpPost(httpUrl);
		post.setConfig(requestConfig);
		if (bytes!=null && byteNames!=null  && bytes.size()>0 && byteNames.size()==bytes.size()) {
			post.setHeader("Content-Type", "multipart/form-data;charset=UTF-8");
			MultipartEntityBuilder builder = MultipartEntityBuilder.create();
			//设置请求参数
			if (bodyParams!=null && !bodyParams.isEmpty()) {
				for (Entry<String, String> entry : bodyParams.entrySet()) {
					builder.addPart(entry.getKey(),new StringBody(entry.getValue(), ContentType.create("plain/text", Consts.UTF_8)));
				}
			}
			for (int i = 0; i < bytes.size(); i++) {
				builder.addPart(byteNames.get(i), new ByteArrayBody(bytes.get(i), byteNames.get(i)));
			}
			HttpEntity entity = builder.build();
			post.setEntity(entity);
		}else {
			post.setHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");	
			//设置请求参数
			if (bodyParams!=null && !bodyParams.isEmpty()) {
				List<NameValuePair> list = new ArrayList<NameValuePair>();
				for (Entry<String, String> entry : bodyParams.entrySet()) {
					list.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
				}
				try {
					if (!list.isEmpty()) {
						post.setEntity(new UrlEncodedFormEntity(list,"UTF-8"));
					}
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
		}
		//设置请求头
		if (headerParams!=null && !headerParams.isEmpty()) {
			for (Entry<String, String> entry : headerParams.entrySet()) {
				post.setHeader(entry.getKey(), entry.getValue());
			}
		}
		HttpHttpsResponseResult responseResult =new HttpHttpsResponseResult();
		try {
			response = client.execute(post);// 执行请求操作，并拿到结果（同步阻塞）
			dealWithHttpResponse(responseResult, response);
			if (responseResult.getStatusCode()!=200) {
				LOGGER.error("post请求失败! url:[{}] ,statusCode:[{}],Content-Type:[{},result:[{}]] ",httpUrl,responseResult.getStatusCode(),post.getFirstHeader("Content-Type"),Objects.toString(responseResult.getObject(),""));
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("post请求失败! url:[{}] ,Content-Type:[{}], errorMessage :{} ",httpUrl,post.getFirstHeader("Content-Type"), e.getMessage());
		} finally {
			releaseResources(responseResult, client, response);
			post=null;
		}
	    return responseResult;
	}
	
	/**
	 * post请求，针对 Content-Type:application/json 请求情况:
	 * @param httpUrl 请求url
	 * @param headerParams 请求头映射。可不传
	 * @param bodyJSONString 请求实体JSON字符串
	 * @return
	 * @author xiepeiqi @date 2019年8月6日
	 */
	public static HttpHttpsResponseResult  postTypeJson(String httpUrl,Map<String, String>headerParams,String bodyJSONString) {
		CloseableHttpResponse response = null;
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(TIMEOUT_SHORT_SECOND*1000).setConnectTimeout(TIMEOUT_SHORT_SECOND*1000).build();// 设置请求和传输超时时间
		CloseableHttpClient client=HttpClients.createDefault();
		HttpPost post=null;
		post=new HttpPost(httpUrl);
		post.setConfig(requestConfig);
		post.setHeader("Content-Type", "application/json;charset=utf-8");	
		StringEntity stringentity = new StringEntity(bodyJSONString, Charset.forName("UTF-8"));
		stringentity.setContentType("application/json");
		post.setEntity(stringentity);
		//设置请求头
		if (headerParams!=null && !headerParams.isEmpty()) {
			for (Entry<String, String> entry : headerParams.entrySet()) {
				post.setHeader(entry.getKey(), entry.getValue());
			}
		}
		HttpHttpsResponseResult responseResult =new HttpHttpsResponseResult();
		try {
			response = client.execute(post);// 执行请求操作，并拿到结果（同步阻塞）
			dealWithHttpResponse(responseResult, response);
			if (responseResult.getStatusCode()!=200) {
				LOGGER.error("post请求失败! url:[{}] ,statusCode:[{}],Content-Type:[{},result:[{}]] ",httpUrl,responseResult.getStatusCode(),post.getFirstHeader("Content-Type"),Objects.toString(responseResult.getObject(),""));
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("post请求失败! url:[{}] ,Content-Type:[{}], errorMessage :{} ",httpUrl,post.getFirstHeader("Content-Type"), e.getMessage());
		} finally {
			releaseResources(responseResult, client, response);
			post=null;
		}
	    return responseResult;
	}

	/**
	 * 请求返回实体
	 * @date 2019年8月6日 @time 上午9:30:34
	 * @author xiepeiqi
	 */
	public static class HttpHttpsResponseResult {

		private int statusCode;

		private Object object;

		private Map<String, String> headers;

		public int getStatusCode() {
			return statusCode;
		}

		public void setStatusCode(int statusCode) {
			this.statusCode = statusCode;
		}

		public Map<String, String> getHeaders() {
			return headers;
		}

		public void setHeaders(Map<String, String> headers) {
			this.headers = headers;
		}

		public Object getObject() {
			return object;
		}

		public void setObject(Object object) {
			this.object = object;
		}

	}
	
}
