package com.backup;

import java.io.IOException;

public class Index {
	static Window window = new Window();
	static Dialog dialog = new Dialog(window);

	public static void main(String[] args) {
		// TODO 自动生成的方法存根
		doMain();
	}
	
	public static void doMain() {
		try {
			window.mkWindow();
		} catch (Exception e) {
			window.addText("发生错误："+e.getMessage());
		}
	}
	
	public static void doIt() {
		Scan scan = new Scan(window);
		System.out.println("main clicked");
		scan.doScan();
		scan.doKill();
		try {
			scan.doProtect();
		} catch (IOException e) {
			window.addText("错误："+e.getMessage());
		}
		dialog.setVisible(true);
	}

}
