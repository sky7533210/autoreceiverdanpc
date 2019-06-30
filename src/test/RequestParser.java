package test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RequestParser {
	private InputStream in;
	private ByteArrayOutputStream requestBody = new ByteArrayOutputStream();
	private int offset = 0;// 多读了的响应体部分的字节数。
	private int content_length;
	private byte[] rqbddata;

	public RequestParser(InputStream in) {
		this.in = in;
	}

	public void parse() throws IOException {
		parse(in);
	}

	/**
	 * 面向过程解析请求报文。 请求行 + 头部 + 空行 + 请求体
	 * 
	 * @return
	 * @throws IOException
	 */
	private void parse(InputStream in) throws IOException {
		byte[] bs = new byte[1024];
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		byte[] data = "\r\n\r\n".getBytes();
		byte[] src = null;
		int index = 0;
		int num;
		String headers = null;
		while (true) {
			num = in.read(bs);
			bout.write(bs, 0, num);
			src = bout.toByteArray();
			index = findArray(src, data);
			if (index != -1) {
				headers = new String(src, 0, index); // 截取空行前的请求体部分
				offset = src.length - index - 4; // 多读的空行后面的字节数，即读取的响应体的字节数
				break;
			}
		}
		// 解析请求行
		int ind = headers.indexOf("\r\n");
		parseRequestLine(headers.substring(0, ind));
		parseRequestHeaders(headers.substring(ind + 2, headers.length()));// 解析请求头
		if (offset > 0) {
			requestBody.write(src, index + 4, offset); // 把多读的请求体的部分写入到 requestBody 中
		}
		// 解析参数。
		parseParameters();

	}

	/**
	 * 解析请求行。将解析结果设置到rq对象中。
	 */
	private void parseRequestLine(String line) {
		StringTokenizer stk = new StringTokenizer(line); // 默认以空格分割
		// 当前的uri可能有?参数。
		// http://localhost/home.html?a=123&b=45
	}

	/**
	 * 解析请求头。并将结果设置到rq对象中
	 * 
	 * @param str
	 */
	public byte[] getData() {
		return rqbddata;
	}
	public int getContent_Length() {
		return content_length;
	}
	private void parseRequestHeaders(String str) {
		StringTokenizer stk = new StringTokenizer(str, "\r\n");
		// List用于保存一对多的键值对数据
		Map<String, List<String>> headersMap = new HashMap<String, List<String>>();
		while (stk.hasMoreTokens()) {
			String kvs = stk.nextToken();// Content-Length:40
			// 分离出key和value。
			String[] kv = kvs.split(":");
			String k = kv[0].trim();
			String v = kv[1].trim();
			// 先根据k获取出value。如果value为null。
			// 之前没有放入过该key，需要创建一个List<String
			// 否则直接放入。
			List<String> values = headersMap.get(k);
			if (values == null) {
				values = new ArrayList<String>();
				headersMap.put(k, values);
			}
			values.add(v);
		}
		//

		content_length = Integer.parseInt(headersMap.get("Content-Length").get(0));

	}

	/**
	 * 解析请求参数。
	 */
	private void parseParameters() {
		int length = content_length;
		length -= offset;
		byte[] bs = new byte[length];
		try {
			while(length>0) {
			int num = in.read(bs);
			// 写入缓冲流中。
			requestBody.write(bs, 0, num);
			length-=num;
			}// 至此请求体被完全读取入流
			rqbddata = requestBody.toByteArray();//
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	// 查询data数组在src数组中出现的位置
	private static int findArray(byte[] src, byte[] data) {
		int count_equals_times = 0;// 待查找字节相等的次数。即src中每段data. length长度字节数组与data相匹配的字节的个数
		for (int i = 0; i < src.length - data.length + 1; i++) {
			count_equals_times = 0;//
			for (int j = 0; j < data.length; j++) {
				if (data[j] == src[i + j])
					count_equals_times++;
				else
					break;
			}
			if (count_equals_times == data.length) {
				return i;
			}
		}
		return -1;
	}

}
