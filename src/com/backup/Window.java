package com.backup;

import java.awt.Container;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;

public class Window extends JFrame{
	static JFrame window;
	static Container container;
	static JTextArea status;
	static JButton button;
	static JScrollPane jsp;
	static String text="";
	
	public static void main(String[] args) throws Exception {
		mkWindow();
	}
	
	public static void mkWindow() throws Exception{
		window = new JFrame("文件夹exe专清");
		container = window.getContentPane();
		status = new JTextArea();
		button =  new JButton("点击开始杀毒");
		status = new JTextArea();
		jsp = new JScrollPane(status);
		
		
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		window.setIconImage(Toolkit.getDefaultToolkit().getImage("./300.png"));
		window.setLayout(null);
		window.setBounds(600, 300, 400, 400);
		window.setResizable(false);
		window.setVisible(true);
		
		button.setBounds(25, 300, 350, 50);
		button.setFont(new Font("微软雅黑", 1, 20));
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("clicked");
				clicked();
			}
		});
		container.add(button);
		
		status.setFont(new Font("宋体", 1, 15));
		status.setWrapStyleWord(true);
		status.setEditable(false);
		addText("欢迎使用exe文件夹病毒专清器");
		addText("©PeterZhong版权所有");
		addText("版本：1.0.0");
		addText("========================");
		jsp.setBounds(25, 50, 350, 200);
		container.add(jsp);
	}
	
	public static void addText(String text) {
		Window.text = Window.text+text+"\n";
		status.setText(Window.text);
		status.setCaretPosition(status.getText().length()); 
		status.paintImmediately(status.getBounds()); 
		System.out.println(text);
	}
	
	public static void clicked() {
		status.setText("");
		text = "";
		addText("欢迎使用exe文件夹病毒专清器");
		addText("©PeterZhong版权所有");
		addText("版本：1.0.0");
		addText("========================");
		status.setCaretPosition(0); 
		status.paintImmediately(status.getBounds()); 
		new Index().doIt();
	}
}

class Dialog extends JDialog{
	public Dialog(JFrame window) {
		super(window, "杀毒成功",true);
		setIconImage(Toolkit.getDefaultToolkit().getImage("./300.png"));
		Container container = getContentPane();
		container.add(new JLabel("  病毒清除与免疫完成，感谢使用！"));
		setBounds(600, 300, 250, 100);
	}
}
