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
	private int offset = 0;// ����˵���Ӧ�岿�ֵ��ֽ�����
	private int content_length;
	private byte[] rqbddata;

	public RequestParser(InputStream in) {
		this.in = in;
	}

	public void parse() throws IOException {
		parse(in);
	}

	/**
	 * ������̽��������ġ� ������ + ͷ�� + ���� + ������
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
				headers = new String(src, 0, index); // ��ȡ����ǰ�������岿��
				offset = src.length - index - 4; // ����Ŀ��к�����ֽ���������ȡ����Ӧ����ֽ���
				break;
			}
		}
		// ����������
		int ind = headers.indexOf("\r\n");
		parseRequestLine(headers.substring(0, ind));
		parseRequestHeaders(headers.substring(ind + 2, headers.length()));// ��������ͷ
		if (offset > 0) {
			requestBody.write(src, index + 4, offset); // �Ѷ����������Ĳ���д�뵽 requestBody ��
		}
		// ����������
		parseParameters();

	}

	/**
	 * ���������С�������������õ�rq�����С�
	 */
	private void parseRequestLine(String line) {
		StringTokenizer stk = new StringTokenizer(line); // Ĭ���Կո�ָ�
		// ��ǰ��uri������?������
		// http://localhost/home.html?a=123&b=45
	}

	/**
	 * ��������ͷ������������õ�rq������
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
		// List���ڱ���һ�Զ�ļ�ֵ������
		Map<String, List<String>> headersMap = new HashMap<String, List<String>>();
		while (stk.hasMoreTokens()) {
			String kvs = stk.nextToken();// Content-Length:40
			// �����key��value��
			String[] kv = kvs.split(":");
			String k = kv[0].trim();
			String v = kv[1].trim();
			// �ȸ���k��ȡ��value�����valueΪnull��
			// ֮ǰû�з������key����Ҫ����һ��List<String
			// ����ֱ�ӷ��롣
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
	 * �������������
	 */
	private void parseParameters() {
		int length = content_length;
		length -= offset;
		byte[] bs = new byte[length];
		try {
			while(length>0) {
			int num = in.read(bs);
			// д�뻺�����С�
			requestBody.write(bs, 0, num);
			length-=num;
			}// ���������屻��ȫ��ȡ����
			rqbddata = requestBody.toByteArray();//
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	// ��ѯdata������src�����г��ֵ�λ��
	private static int findArray(byte[] src, byte[] data) {
		int count_equals_times = 0;// �������ֽ���ȵĴ�������src��ÿ��data. length�����ֽ�������data��ƥ����ֽڵĸ���
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
