package com.backup;

import java.io.IOException;

public class Index {
	static Window window = new Window();
	static Dialog dialog = new Dialog(window);

	public static void main(String[] args) {
		// TODO �Զ����ɵķ������
		doMain();
	}
	
	public static void doMain() {
		try {
			window.mkWindow();
		} catch (Exception e) {
			window.addText("��������"+e.getMessage());
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
			window.addText("����"+e.getMessage());
		}
		dialog.setVisible(true);
	}

}
