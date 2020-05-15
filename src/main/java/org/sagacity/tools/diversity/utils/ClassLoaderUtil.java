/**
 * 
 */
package org.sagacity.tools.diversity.utils;

import java.io.File;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * @project sagacity-core
 * @description 类加载器工具包，提供jar、class等动态加载功能
 * @author zhongxuchen $<a href="mailto:zhongxuchen@hotmail.com">联系作者</a>$
 * @version $id:ClassLoaderUtil.java,Revision:v1.0,Date:2008-12-14 下午07:57:11 $
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ClassLoaderUtil {
	/**
	 * 定义日志
	 */
	@SuppressWarnings("unused")
	private final static Logger logger = LoggerUtil.getLogger();

	private static Field classes;
	private static Method addURL;
	static {
		try {
			classes = ClassLoader.class.getDeclaredField("classes");
			addURL = URLClassLoader.class.getDeclaredMethod("addURL", new Class[] { URL.class });
		} catch (Exception e) {
			// won't happen,but remain it throw new RootException(e);
		}
		classes.setAccessible(true);
		addURL.setAccessible(true);
	}

	private static URLClassLoader system = (URLClassLoader) getSystemClassLoader();

	private static URLClassLoader ext = (URLClassLoader) getExtClassLoader();

	public static ClassLoader getSystemClassLoader() {
		return ClassLoader.getSystemClassLoader();
	}

	public static ClassLoader getExtClassLoader() {
		return getSystemClassLoader().getParent();
	}

	/**
	 * * 获得加载的类 * *
	 * 
	 * @return
	 */
	public static List getClassesLoadedBySystemClassLoader() {
		return getClassesLoadedByClassLoader(getSystemClassLoader());
	}

	public static List getClassesLoadedByExtClassLoader() {
		return getClassesLoadedByClassLoader(getExtClassLoader());
	}

	public static List getClassesLoadedByClassLoader(ClassLoader cl) {
		try {
			return (List) classes.get(cl);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static URL[] getSystemURLs() {
		return system.getURLs();
	}

	public static URL[] getExtURLs() {
		return ext.getURLs();
	}

	private static void list(PrintStream ps, URL[] classPath) {
		for (int i = 0; i < classPath.length; i++) {
			ps.println(classPath[i]);
		}
	}

	public static void listSystemClassPath() {
		listSystemClassPath(System.out);
	}

	public static void listSystemClassPath(PrintStream ps) {
		ps.println("SystemClassPath:");
		list(ps, getSystemClassPath());
	}

	public static void listExtClassPath() {
		listExtClassPath(System.out);
	}

	public static void listExtClassPath(PrintStream ps) {
		ps.println("ExtClassPath:");
		list(ps, getExtClassPath());
	}

	public static URL[] getSystemClassPath() {
		return getSystemURLs();
	}

	public static URL[] getExtClassPath() {
		return getExtURLs();
	}

	public static void addURL2SystemClassLoader(URL[] urls) {
		try {
			invoke(system, urls);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void addURL2ExtClassLoader(URL[] urls) {
		try {
			invoke(ext, urls);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 增加url到指定的UrlClassLoader中
	 * 
	 * @param urlClassLoader
	 * @param url
	 */
	public static void addURL2ClassLoader(URLClassLoader urlClassLoader, URL[] urls) {
		try {
			invoke(urlClassLoader, urls);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 增加jar或zip文件到指定的UrlClassLoader中
	 * 
	 * @param urlClassLoader
	 * @param dirOrJars
	 */
	public static void addClassPath(URLClassLoader urlClassLoader, File[] dirOrJars) {
		try {
			URL[] urls = convertFile2URL(dirOrJars);
			invoke(urlClassLoader, urls);
			// addURL.invoke(urlClassLoader, urls);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void addClassPath(String path) {
		addClassPath(new File[] { new File(path) });
	}

	public static void addExtClassPath(String path) {
		addExtClassPath(new File[] { new File(path) });
	}

	public static void addClassPath(File[] dirOrJars) {
		try {
			URL[] urls = convertFile2URL(dirOrJars);
			addURL2SystemClassLoader(urls);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	public static void addExtClassPath(File[] dirOrJars) {
		try {
			URL[] urls = convertFile2URL(dirOrJars);
			addURL2ExtClassLoader(urls);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("deprecation")
	private static URL[] convertFile2URL(File[] dirOrJars) throws MalformedURLException {
		if (dirOrJars == null || dirOrJars.length < 1)
			return null;
		URL[] urls = new URL[dirOrJars.length];
		for (int i = 0; i < urls.length; i++) {
			urls[i] = dirOrJars[i].toURL();
		}
		return urls;
	}

	private static void invoke(URLClassLoader urlClassLoader, URL[] urls) throws Exception {
		for (int i = 0; i < urls.length; i++)
			addURL.invoke(urlClassLoader, urls[i]);
	}

	/**
	 * @todo 加载jar文件
	 * @param jarFiles
	 */
	public static void loadJarFiles(List jarFiles) {
		List pureJarFiles = new ArrayList();
		File tmpFile;
		for (int i = 0; i < jarFiles.size(); i++) {
			tmpFile = (File) jarFiles.get(i);
			if (tmpFile.exists() && !tmpFile.isDirectory())
				pureJarFiles.add(jarFiles.get(i));
		}

		// 加载驱动类
		if (!pureJarFiles.isEmpty()) {
			File[] pureFiles = new File[pureJarFiles.size()];
			for (int i = 0; i < pureFiles.length; i++)
				pureFiles[i] = (File) pureJarFiles.get(i);
			addClassPath((URLClassLoader) ClassLoader.getSystemClassLoader(), pureFiles);
		}
	}
}
