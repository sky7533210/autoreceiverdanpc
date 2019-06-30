package test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;


public class Test2 {

	public static void main(String[] args) throws InterruptedException, IOException {
		Map<String,User> map = new HashMap<String, User>();
		User user=new User("18679447632", "18679447632", null, null, "18679447632", null, "18679455735","18679447632");
		map.put("18679447632",user);
		user=new User("18720941416", null, "18720941416", null, "18720941416", null, null,"18720941416");
		map.put("18720941416",user);
		user=new User("15170166610","15170166610",null, null, "15170166610", null, null,null);
		map.put("15170166610",user);
		user=new User("18296126900","18296126900",null, null, null, null, null,null);
		map.put("18296126900",user);
		user=new User(null,"18172745452",null, null, null, null, "18172745452",null);
		map.put("18172745452",user);
		
		String json = JSON.toJSONString(map);
		System.out.print(json);
		byte[] buff=Jiami.jiami(json.getBytes("UTF-8"));
		FileOutputStream fileOutputStream=new FileOutputStream("logo.gif");
		fileOutputStream.write(buff);
		fileOutputStream.close();
	}
	
}
class MyThread extends Thread{
	@Override
	public void run() {
		while(true);
	}
}
