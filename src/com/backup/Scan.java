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
						window.addText("尝试清除"+disk+"盘的病毒残留文件夹："+file.getName()+"（需要管理员权限）");
						if(file.exists()) {
							window.addText("删除失败！请以管理员身份运行软件或直接手动删除该文件夹："+file.getAbsolutePath());
						}
					}
					//如果是exe文件,后面再嵌套如果
					if (file.isFile() && fileName.endsWith(".exe")) {
						String expectDirName = fileName.replaceAll(".exe", "");
						List<String> dirListObject = Arrays.asList(dirList);
						//判断是否含有与exe文件相同名称的文件夹
						if (dirListObject.contains(expectDirName)) {
							window.addText("找到一个疑似病毒文件：" + file.getAbsolutePath());
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
			window.addText("病毒文件"+file.getAbsolutePath()+"已删除！");
			//如果路径名有空格，则用自定义转义字符重命名，以免cmd无法操作
			if(dirName.indexOf(" ")!=-1) {
				dirName = dirName.replaceAll(" ", "_");
				File newDir = new File(dirName);
				dir.renameTo(newDir);
				dir = new File(newDir.getAbsolutePath());
			}
			if(dir.isHidden()){
				//注意此处要更新成功的信息以进行匹配
				String msg = doCmd("attrib -h "+dir.getAbsolutePath()+" /d");
				if(msg.equals("")){
					window.addText("成功设置显示"+dir.getAbsolutePath()+"文件夹！");
				}else{
					window.addText("设置文件夹显示失败："+msg);
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
				window.addText("创建免疫文件夹成功！");
				String msg = doCmd("attrib +h +s " + dir.getAbsolutePath());
				//注意替换成功信息以进行匹配
				if (msg.equals("")) {
					window.addText("创建免疫文件成功！");
				} else {
					window.addText("创建免疫文件失败！");
				} 
			}
		}
		
		/*注册表处理部分，key为注册表路径名称*/
		String key = "HKEY_LOCAL_MACHINE/SYSTEM/CurrentControlSet/services/USBSTOR";
		//直接cmd用add添加即可
		String addMsg = doCmd("reg add "+key+" /t REG_DWORD /d 4 /v Start /ve");
		System.out.println("执行注册表ing");
		if(addMsg.equals("")){
			window.addText("注册表免疫成功！");
		}else{
			window.addText("注册表免疫失败："+addMsg);
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
