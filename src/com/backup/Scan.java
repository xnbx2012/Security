package com.backup;

import java.util.*;
import java.io.*;

public class Scan{
	static List<String> killList = new ArrayList<String>();
	static Window window;
	
	public Scan(Window window) {
		this.window = window;
	}
	
	public static void main(String[] args) throws Exception{
		//doScan();
		//doKill();
		doProtect();
		//changeStart(true);
	}
	
	@SuppressWarnings("static-access")
	public static void doScan(){
		String[] diskList = {"C:","D:","E:","F:","G:","H:","I:","J:"};
		
		for(String disk:diskList){
			File dir = new File(disk);
			String[] dirList = dir.list();
			if (dirList!=null) {
				for (String fileName : dirList) {
					File file = new File(disk + fileName);
					if(file.isDirectory()&&file.getName().equals("$RECYCLE.BIN")) {
						String msg = doCmd("attrib -s "+file.getAbsolutePath());
						file.delete();
						window.addText("�������"+disk+"�̵Ĳ��������ļ��У�"+file.getName()+"����Ҫ����ԱȨ�ޣ�");
						if(file.exists()) {
							window.addText("ɾ��ʧ�ܣ����Թ���Ա������������ֱ���ֶ�ɾ�����ļ��У�"+file.getAbsolutePath());
						}
					}
					//�����exe�ļ�,������Ƕ�����
					if (file.isFile() && fileName.endsWith(".exe")) {
						String expectDirName = fileName.replaceAll(".exe", "");
						List<String> dirListObject = Arrays.asList(dirList);
						//�ж��Ƿ�����exe�ļ���ͬ���Ƶ��ļ���
						if (dirListObject.contains(expectDirName)) {
							window.addText("�ҵ�һ�����Ʋ����ļ���" + file.getAbsolutePath());
							if (killList!=null) {
								killList.add(disk+expectDirName);
							}
						}
					}
				} 
			}
		}
	}
	
	public static void doKill(){
		Object[] killObjArr = killList.toArray();
		String[] killArr= new String[killObjArr.length];
		for(int i=0;i<killObjArr.length;i++) {
			killArr[i] = (String)killObjArr[i];
		}
		for(String dirName:killArr){
			File file = new File(dirName+".exe");
			File dir = new File(dirName);
			file.delete();
			window.addText("�����ļ�"+file.getAbsolutePath()+"��ɾ����");
			//���·�����пո������Զ���ת���ַ�������������cmd�޷�����
			if(dirName.indexOf(" ")!=-1) {
				dirName = dirName.replaceAll(" ", "_");
				File newDir = new File(dirName);
				dir.renameTo(newDir);
				dir = new File(newDir.getAbsolutePath());
			}
			if(dir.isHidden()){
				//ע��˴�Ҫ���³ɹ�����Ϣ�Խ���ƥ��
				String msg = doCmd("attrib -h "+dir.getAbsolutePath()+" /d");
				if(msg.equals("")){
					window.addText("�ɹ�������ʾ"+dir.getAbsolutePath()+"�ļ��У�");
				}else{
					window.addText("�����ļ�����ʾʧ�ܣ�"+msg);
				}
			}
		}
	}
	
	public static void doProtect() throws IOException{
		String[] diskList = {"C:","D:","E:","F:","G:","H:","I:","J:"};
		for(String disk:diskList){
			if (new File(disk).list()!=null) {
				File dir = new File(disk + "\\autonrun.inf");
				if (dir.exists()) {
					dir.delete();
				}
				dir.mkdirs();
				File doubleDir = new File(disk + "\\autorun.inf\\protectFolder");
				if (doubleDir.exists()) {
					doubleDir.delete();
				}
				doubleDir.mkdirs();
				window.addText("���������ļ��гɹ���");
				String msg = doCmd("attrib +h +s " + dir.getAbsolutePath());
				//ע���滻�ɹ���Ϣ�Խ���ƥ��
				if (msg.equals("")) {
					window.addText("���������ļ��ɹ���");
				} else {
					window.addText("���������ļ�ʧ�ܣ�");
				} 
			}
		}
		
		/*ע������֣�keyΪע���·������*/
		String key = "HKEY_LOCAL_MACHINE/SYSTEM/CurrentControlSet/services/USBSTOR";
		//ֱ��cmd��add��Ӽ���
		String addMsg = doCmd("reg add "+key+" /t REG_DWORD /d 4 /v Start /ve");
		System.out.println("ִ��ע���ing");
		if(addMsg.equals("")){
			window.addText("ע������߳ɹ���");
		}else{
			window.addText("ע�������ʧ�ܣ�"+addMsg);
		}
	}
	
	private static void changeStart(boolean isStartAtLogon) throws IOException{  
		String regKey = "HKEY_CURRENT_USER\\SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Run";  
		String myAppName = "mgtest";  
		String exePath = "\"D:\\Program Files (x86)\\love\\HelloWorld.exe\"";  
		Runtime.getRuntime().exec("reg "+(isStartAtLogon?"add ":"delete ")+regKey+" /v "+myAppName+(isStartAtLogon?" /t reg_sz /d "+exePath:" /f"));  
	}
	
	private static String doCmd(String cmdCode) {
        BufferedReader br = null;
        try {
            Process p = Runtime.getRuntime().exec("cmd /c "+cmdCode);
            br = new BufferedReader(new InputStreamReader(p.getInputStream(), "GBK"));
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
            System.out.println(sb.toString());
			return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
			return e.getMessage();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (Exception e) {
                    e.printStackTrace();
					return e.getMessage();
                }
            }
        }
    }
}
