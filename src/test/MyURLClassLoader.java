package test;

import java.net.URL;
import java.net.URLClassLoader;

public class MyURLClassLoader extends URLClassLoader {

	public MyURLClassLoader(URL[] urls) {
		super(urls);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException{
		// TODO Auto-generated method stub
		return super.findClass(name);
	}
}