package com.yuhang.vediostream.base.util;


import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.HttpEntityWrapper;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpsUtil {
	private static  Logger log = LoggerFactory.getLogger(HttpsUtil.class);
	
	private static HashMap<String, DefaultHttpClient> sessionMap = new HashMap<String, DefaultHttpClient>();

	private static HashMap<String, HttpContext> contextMap = new HashMap<String, HttpContext>();
	private static HashMap<String, CookieStore> cookieMap = new HashMap<String, CookieStore>();

	public HttpsUtil() {
	}
	
	/**
	 * 访问https的网站
	 * 
	 * @param httpclient
	cong */
	private static void enableSSL(DefaultHttpClient httpclient) {
		// 调用ssl
		try {
			SSLContext sslcontext = SSLContext.getInstance("TLS");
			sslcontext.init(null, new TrustManager[] { truseAllManager }, null);
			SSLSocketFactory sf = new SSLSocketFactory(sslcontext);
			sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			Scheme https = new Scheme("https", sf, 443);
			httpclient.getConnectionManager().getSchemeRegistry()
					.register(https);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 重写验证方法，取消检测ssl
	 */
	private static TrustManager truseAllManager = new X509TrustManager() {

		public void checkClientTrusted(
				java.security.cert.X509Certificate[] arg0, String arg1) {
			// TODO Auto-generated method stub

		}

		public void checkServerTrusted(
				java.security.cert.X509Certificate[] arg0, String arg1) {
			// TODO Auto-generated method stub

		}

		public java.security.cert.X509Certificate[] getAcceptedIssuers() {
			// TODO Auto-generated method stub
			return null;
		}

	};
	
	
	
	public static HashMap postDataToMap(String data,String encode){
		HashMap map=new HashMap();
		
		if (data!=null)
		try{
			String[] ss=data.split("&");
			for (String s:ss){
				String[] tt=s.split("=");
				if (tt.length>1){
					String key=tt[0];
					String value=tt[1];
					
					if (value.matches("(%[a-zA-Z0-9]{2})+")){
						value=URLDecoder.decode(value,encode);
					}

					map.put(key, value);
					
				}
			}
			
		}catch(Exception e){}
		
		return map;
	}
	
	public static void setSessionId(String name, String sessionId) {
		if (name == null)
			return;

		try {
			DefaultHttpClient httpclient;

			if (sessionMap.containsKey(name)) {
				httpclient = sessionMap.get(name);
			} else {
				httpclient = createHttpClient(name);
				sessionMap.put(name, httpclient);
			}

			CookieStore cs = cookieMap.get(name);

			if (cs != null) {
//				cs.addCookie(new BasicClientCookie("JSESSIONID", sessionId));
//				
//				System.out.println(cs.toString());
				
				 if (cs != null) {
				 List<Cookie> cookies = cs.getCookies();
				 for (int i = 0; i < cookies.size(); i++) {
//				 System.out.println("Local cookie: " + cookies.get(i));
					 Cookie cookie=cookies.get(i);
					 
					 if ("BSS_JSESSIONID".equals(cookie.getName())){
						BasicClientCookie bcc=new BasicClientCookie("BSS_JSESSIONID", sessionId);
						bcc.setDomain(cookie.getDomain());
						bcc.setVersion(cookie.getVersion());
						bcc.setPath(cookie.getPath());
						cs.addCookie(bcc);
						break;
					 }
				 }
				
				 }
				
				
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public static void addCookie1(String name,String key, String value) {
		if (name == null)
			return;

		try {
			DefaultHttpClient httpclient;

			if (sessionMap.containsKey(name)) {
				httpclient = sessionMap.get(name);
			} else {
				httpclient = createHttpClient(name);
				sessionMap.put(name, httpclient);
			}

			CookieStore cs = cookieMap.get(name);

			if (cs != null) {
				if (cs != null) {
					String domain = "10.110.0.100";
					String path = "/";
					int version = 0;
					try {
						List<Cookie> cookies = cs.getCookies();
						Cookie cookie = cookies.get(0);
						domain = cookie.getDomain();
						path = cookie.getPath();
						version = cookie.getVersion();

					} catch (Exception e) {
					}

					BasicClientCookie bcc = new BasicClientCookie(key, value);
					bcc.setDomain(domain);
					bcc.setVersion(version);
					bcc.setPath(path);
					cs.addCookie(bcc);
				}
				
			}
			
			

		} catch (Exception e) {
			e.printStackTrace();
		}

		
	}
	

	/**
	 * get httpGet
	 * 
	 * @param url
	 *            String
	 * @param name
	 *            String 连接名称，用以维护session,不需要维持连接时请置null，操作结束后请调用destroy方法
	 * @param charset
	 *            String
	 * @return String
	 */
	public static String get(String url, String name, String charset) {
		if (charset == null)
			charset = HTTP.UTF_8;

		try {
			DefaultHttpClient httpclient;

			if (name == null) { // 不需要维持连接
				httpclient = createHttpClient(name);
			} else if (sessionMap.containsKey(name)) {
				httpclient = sessionMap.get(name);
			} else {
				httpclient = createHttpClient(name);
				sessionMap.put(name, httpclient);
			}

			HttpGet httpget1 = new HttpGet(url);
			log.info("querying url :"+url);
			
			CookieStore cs = cookieMap.get(name);

			if (cs != null) {
				 List<Cookie> cookies = cs.getCookies();
				 for (int i = 0; i < cookies.size(); i++) {
					 log.info("Local cookie: " + cookies.get(i));
				 }
			}
					 

			 httpget1.setHeader(new BasicHeader("User-Agent",
			 "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; InfoPath.2)"));
			// CLR 2.0.50727; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)"
			// "Nokia6510/1.0 (04.21)"));
			// httpget1.getParams().setParameter("http.protocol.allow-circular-redirects", true);
			HttpResponse response1;
			if (contextMap.get(name) != null) {
				response1 = httpclient.execute(httpget1, contextMap.get(name));
			} else {
				response1 = httpclient.execute(httpget1);
			}
			HttpEntity entity1 = response1.getEntity();

			if (entity1 != null) {
				String ss=EntityUtils.toString(entity1, charset);
				log.debug("html : "+ss);
				return ss;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// CookieStore cookieStore = cookieMap.get(name);
			// if (cookieStore != null) {
			// List<Cookie> cookies = cookieStore.getCookies();
			// for (int i = 0; i < cookies.size(); i++) {
			// System.out.println("Local cookie: " + cookies.get(i));
			// }
			//
			// }
		}

		return null;
	}

	
	public static byte[] getBytes(String url, String name) {
		try {
			DefaultHttpClient httpclient;

			if (name == null) { // 不需要维持连接
				httpclient = createHttpClient(name);
			} else if (sessionMap.containsKey(name)) {
				httpclient = sessionMap.get(name);
			} else {
				httpclient = createHttpClient(name);
				sessionMap.put(name, httpclient);
			}

			HttpGet httpget1 = new HttpGet(url);
			log.info("querying url :"+url);
			
			CookieStore cs = cookieMap.get(name);

			if (cs != null) {
				 List<Cookie> cookies = cs.getCookies();
				 for (int i = 0; i < cookies.size(); i++) {
					 log.info("Local cookie: " + cookies.get(i));
				 }
			}
					 

			 httpget1.setHeader(new BasicHeader("User-Agent",
			 "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; InfoPath.2)"));
			// CLR 2.0.50727; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)"
			// "Nokia6510/1.0 (04.21)"));

			HttpResponse response1;
			if (contextMap.get(name) != null) {
				response1 = httpclient.execute(httpget1, contextMap.get(name));
			} else {
				response1 = httpclient.execute(httpget1);
			}
			HttpEntity entity1 = response1.getEntity();

			if (entity1 != null) {
				byte[] r=EntityUtils.toByteArray(entity1);
				
				 CookieStore cookieStore = cookieMap.get(name);
				 if (cookieStore != null) {
				 List<Cookie> cookies = cookieStore.getCookies();
				 for (int i = 0; i < cookies.size(); i++) {
				 System.out.println("Local cookie2: " + cookies.get(i));
				 }
				
				 }
				
				return r;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			 CookieStore cookieStore = cookieMap.get(name);
			 if (cookieStore != null) {
			 List<Cookie> cookies = cookieStore.getCookies();
			 for (int i = 0; i < cookies.size(); i++) {
			 System.out.println("Local cookie1: " + cookies.get(i));
			 }
			
			 }
		}

		return null;
	}
	
	/**
	 * post
	 * 
	 * @param url
	 *            String
	 * @param map
	 *            HashMap 提交表单的键值对
	 * @param name
	 *            String 连接名称，用以维护session
	 * @param charset
	 *            String
	 * @return String
	 */
	public static String post(String url, HashMap<String, String> map,
			String name, String charset) {
		if (charset == null)
			charset = HTTP.UTF_8;

		try {
			DefaultHttpClient httpclient;

			if (name == null) { // 不需要维持连接
				httpclient = createHttpClient(name);
			} else if (sessionMap.containsKey(name)) {
				httpclient = sessionMap.get(name);
			} else {
				httpclient = createHttpClient(name);
				sessionMap.put(name, httpclient);
			}

			HttpPost httpost = new HttpPost(url);
			log.info("post url : "+url);
			if (map!=null)
			log.info("post data : "+map.entrySet().toString());

			CookieStore cs = cookieMap.get(name);

			if (cs != null) {
				 List<Cookie> cookies = cs.getCookies();
				 for (int i = 0; i < cookies.size(); i++) {
					 log.info("Local cookie: " + cookies.get(i));
				 }
			}
			
			
			httpost.setHeader(new BasicHeader("User-Agent",
			 "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)"));
			
			// httpost.setHeader(new BasicHeader("User-Agent",
			// "Nokia6510/1.0 (04.21)"));

			List<NameValuePair> nvps = new ArrayList<NameValuePair>();

			if (map != null) {
				Iterator it = map.keySet().iterator();

				while (it.hasNext()) {
					String key = (String) it.next();
					nvps.add(new BasicNameValuePair(key, map.get(key)));
				}
			}

			httpost.setEntity(new UrlEncodedFormEntity(nvps, charset));
//			System.out.println(EntityUtils.toString(httpost.getEntity()));
						
			HttpResponse response;
			if (contextMap.get(name) != null) {
				response = httpclient.execute(httpost, contextMap.get(name));
			} else {
				response = httpclient.execute(httpost);
			}

			HttpEntity entity1 = response.getEntity();

			if (entity1 != null){
				String ss=EntityUtils.toString(entity1, charset);
				log.debug("html : "+ss);
				return ss;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// CookieStore cookieStore = cookieMap.get(name);
			// if (cookieStore != null) {
			// List<Cookie> cookies = cookieStore.getCookies();
			// for (int i = 0; i < cookies.size(); i++) {
			// System.out.println("Local cookie: " + cookies.get(i));
			// }
			//
			// }

		}

		return null;
	}

/*	public static String postm(String url, HashMap<String, String> map,
			String name, String charset) {
		if (charset == null)
			charset = HTTP.UTF_8;

		try {
			DefaultHttpClient httpclient;

			if (name == null) { // 不需要维持连接
				httpclient = createHttpClient(name);
			} else if (sessionMap.containsKey(name)) {
				httpclient = sessionMap.get(name);
			} else {
				httpclient = createHttpClient(name);
				sessionMap.put(name, httpclient);
			}

			HttpPost httpost = new HttpPost(url);

			Header[] headers = new BasicHeader[] {
					new BasicHeader(
							"Referer",
							"http://10.110.0.100/npage/sq100/sq100_1.jsp?opCode=1100&opName=客户开户&crmActiveOpCode=1100"),
					// new BasicHeader("Content-Type",
					// "multipart/form-data"),
					new BasicHeader("User-Agent",
							"Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; InfoPath.2)") };

			httpost.setHeaders(headers);

			// List<NameValuePair> nvps = new ArrayList<NameValuePair>();

			MultipartEntity me = new MultipartEntity();
			if (map != null) {
				Iterator it = map.keySet().iterator();

				while (it.hasNext()) {
					String key = (String) it.next();
					me.addPart(key, new StringBody(map.get(key), Charset
							.forName(charset)));
					// nvps.add(new BasicNameValuePair(key, map.get(key)));
					// me.addPart(key, new
					// StringBody("",Charset.forName("utf-8")));
				}
			}

			httpost.setEntity(me);
			// httpost.setc

			System.out.println(httpost.getRequestLine());
			

			// InputStream in=me.getContent();
			// byte[] bs=new byte[in.available()];
			// in.read(bs);
			// System.out.println("---------------------------");
			// System.out.println(new String(bs));
			// System.out.println("---------------------------");

			HttpResponse response;
			if (contextMap.get(name) != null) {
				response = httpclient.execute(httpost, contextMap.get(name));
			} else {
				response = httpclient.execute(httpost);
			}
			
			List<Cookie> cl=httpclient.getCookieStore().getCookies();
			
			System.out.println("cookie : ");
			
			for (Cookie c:cl){
				System.out.println(c.getName()+":"+c.getValue()+","+c.getDomain()+","+c.getPath());
			}
			
			HttpEntity entity1 = response.getEntity();

			if (entity1 != null)
				return EntityUtils.toString(entity1, charset);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// CookieStore cookieStore = cookieMap.get(name);
			// if (cookieStore != null) {
			// List<Cookie> cookies = cookieStore.getCookies();
			// for (int i = 0; i < cookies.size(); i++) {
			// System.out.println("Local cookie: " + cookies.get(i));
			// }
			//
			// }

		}

		return null;
	}*/

	public static String post(String url, String content, String name,
			String charset) {
		if (charset == null)
			charset = HTTP.UTF_8;

		try {
			DefaultHttpClient httpclient;

			if (name == null) { // 不需要维持连接
				httpclient = createHttpClient(name);
			} else if (sessionMap.containsKey(name)) {
				httpclient = sessionMap.get(name);
			} else {
				httpclient = createHttpClient(name);
				sessionMap.put(name, httpclient);
			}

			HttpPost httpost = new HttpPost(url);

			if (content != null) {
				httpost.setEntity(new StringEntity(content, charset));
			}

			// httpost.setHeader(new BasicHeader("User-Agent",
			// "Nokia6510/1.0 (04.21)"));

			HttpResponse response;
			if (contextMap.get(name) != null) {
				response = httpclient.execute(httpost, contextMap.get(name));
			} else {
				response = httpclient.execute(httpost);
			}

			HttpEntity entity1 = response.getEntity();
			if (entity1 != null)
				return EntityUtils.toString(entity1, charset);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static void destroy(String name) {
		HttpClient httpclient = sessionMap.get(name);
		httpclient.getConnectionManager().shutdown();
		sessionMap.remove(name);
		cookieMap.remove(name);
		contextMap.remove(name);
	}

	private static DefaultHttpClient createHttpClient(String name) {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		enableSSL(httpclient);
		
		try {

			// Create a local instance of cookie store
			CookieStore cookieStore = new BasicCookieStore();

			// Create local HTTP context
			HttpContext localContext = new BasicHttpContext();
			// Bind custom cookie store to the local context
			localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);

			cookieMap.put(name, cookieStore);
			contextMap.put(name, localContext);

			httpclient.setCookieStore(cookieStore);
			
		    HttpParams params = httpclient.getParams();
		    HttpConnectionParams.setConnectionTimeout(params, 60000);
		    HttpConnectionParams.setSoTimeout(params, 60000);
		    
		    
		} catch (Exception e) {
			e.printStackTrace();
		}
		return httpclient;

	}

	static class GzipDecompressingEntity extends HttpEntityWrapper {

		public GzipDecompressingEntity(final HttpEntity entity) {
			super(entity);
		}

		@Override
		public InputStream getContent() throws IOException,
				IllegalStateException {

			// the wrapped entity's getContent() decides about repeatability
			InputStream wrappedin = wrappedEntity.getContent();

			return new GZIPInputStream(wrappedin);
		}

		@Override
		public long getContentLength() {
			// length of ungzipped content is not known
			return -1;
		}

	}

	public static String getReg(String src, String reg) {
		if (src == null || reg == null)
			return null;
		Pattern p = Pattern.compile(reg);
		Matcher m = p.matcher(src);

		if (m.find()) {
			return m.group(1);
		}

		return null;
	}

	public static String getSeg(String src, String start, String end) {
		if (src == null)
			return null;
		int ps = 0;
		if (start != null && start.length() > 0) {
			ps = src.indexOf(start);
		}
		if (ps < 0)
			return null;
		int pe = src.length();
		if (end != null && end.length() > 0) {
			pe = src.indexOf(end, ps);
		}
		if (pe <= ps)
			return null;

		return src.substring(ps, pe);
	}
	
	public static HashMap getValues(String html) {
		if (html==null) return new HashMap();
		String reg = "<.*[ ]+NAME=\"([^\"]*)\"[ ]+value=\"(.*)\".*>";

		Pattern p = Pattern.compile(reg);
		Matcher m = p.matcher(html);

		HashMap map = new HashMap();
		while (m.find()) {
			map.put(m.group(1), m.group(2));
		}

		return map;
	}
	 

	public static String[] split(String s){
		if (s==null) return null;
		if (s.indexOf("=")>0){
			String[] ss=new String[2];
			ss[0]=s.substring(0,s.indexOf("="));
			ss[1]=s.substring(s.indexOf("=")+1);
			return ss;
		}
		return new String[]{s};
	}
	
	public static String encodeURL(String url,String charset) {
		if (url == null || url.indexOf("?") < 0)
			return url;
		String s = url.substring(url.indexOf("?") + 1);
		if (s == null || s.length() < 1)
			return url;
		String[] ss = s.split("&");
		StringBuffer buf = new StringBuffer();
		buf.append(url.substring(0, url.indexOf("?") + 1));
		boolean first = true;
		for (String s1 : ss) {
			String[] ss1 = split(s1);
			if (ss1.length == 2) {
				if (first) {
					buf.append(ss1[0]).append('=').append(encode(ss1[1],charset));
					first = false;
				} else {
					buf.append('&').append(ss1[0]).append('=')
							.append(encode(ss1[1],charset));
				}
			} else {
				if (first) {
					buf.append(s1);
				} else {
					buf.append('&').append(s1);
				}
			}
		}
		return buf.toString();

	}
	
	public static String encode(String s,String charset) {
		try {
			return URLEncoder.encode(s, charset);
		} catch (Exception e) {
			e.printStackTrace();
			return s;
		}
	}
	
}
