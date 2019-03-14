package com.tony.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * 检查指定目录下是否有项目进行部署
 * 
 * @author Tony
 *
 */
public class ProjectChecker {

	public static Set<String> check(String work_space) throws Exception {
		Set<String> projects = new HashSet<>();
		File ws = new File(work_space);
		// 解压war包
		FilenameFilter warFilter = new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".war");
			}
		};
		File[] wars = ws.listFiles(warFilter);
		for (File war : wars) {
			unzip(war.getPath());
			System.out.println("解压" + war.getName());
		}
		System.out.println("******************解压完毕********************");

		// 根据文件查询出项目
		File file = new File(work_space);
		File[] listFiles = file.listFiles();
		for (File project : listFiles) {
			if (project.isDirectory()) {
				System.out.println("发现项目：" + project.getName());
				projects.add(project.getName());
			}
		}
		System.out.println("******************项目搜寻完毕********************");
		return projects;
	}

	/**
	 * 解压
	 * 
	 * @param filePath
	 * @throws Exception
	 */
	private static void unzip(String filePath) throws Exception {
		File zipFile = new File(filePath);
		String descDir = zipFile.getParentFile().getAbsolutePath() + "\\";

		@SuppressWarnings("resource")
		ZipFile zip = new ZipFile(zipFile, Charset.forName("GBK"));// 解决中文文件夹乱码
		String name = zip.getName().substring(zip.getName().lastIndexOf('\\') + 1, zip.getName().lastIndexOf('.'));

		File pathFile = new File(descDir + name);
		if (!pathFile.exists()) {
			pathFile.mkdirs();
		}

		for (Enumeration<? extends ZipEntry> entries = zip.entries(); entries.hasMoreElements();) {
			ZipEntry entry = (ZipEntry) entries.nextElement();
			String zipEntryName = entry.getName();
			InputStream in = zip.getInputStream(entry);
			String outPath = (descDir + name + "/" + zipEntryName).replaceAll("\\*", "/");

			// 判断路径是否存在,不存在则创建文件路径
			File file = new File(outPath.substring(0, outPath.lastIndexOf('/')));
			if (!file.exists()) {
				file.mkdirs();
			}
			// 判断文件全路径是否为文件夹,如果是上面已经上传,不需要解压
			if (new File(outPath).isDirectory()) {
				continue;
			}

			FileOutputStream out = new FileOutputStream(outPath);
			byte[] buf1 = new byte[1024];
			int len;
			while ((len = in.read(buf1)) > 0) {
				out.write(buf1, 0, len);
			}
			in.close();
			out.close();
		}
		return;
	}
}
