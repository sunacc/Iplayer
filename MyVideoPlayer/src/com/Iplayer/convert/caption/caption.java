package com.Iplayer.convert.caption;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

public class caption {
	
	/**
	 * total:字幕条数
	 * duration:每条字幕时间间隔
	 * name:文件名
	 * content[]：每条字幕内容
	 */
	
	public int total=0;
	private int duration;
	private String name;
	private String root,root2;
	public String[] content;
	public String[] time;
	public FileOutputStream out=null;
	
	public caption(int x,int y,String z) throws IOException{
		total=x;
		duration=y;
		name=z;
		root=name;
		validate();
		content=new String[total+1];
		time=new String[total+1];
	}
	
	public void validate() throws IOException
	{
        File file=new File(root);
        if(!file.exists()) file.createNewFile();
	}
	
	public void readcaption()
	{
		FileInputStreamDemo(root);
	}
	
	public void writecaption() throws FileNotFoundException
	{
		out = new FileOutputStream(new File(root));
		System.out.println("ready_write");
    	for(int i=1;i<=total;++i)
    	{
    		//System.out.println(i);
    		try
    		{
		        out.write((String.valueOf(i)+"\n").getBytes());
		        if(time[i]==null)
		        {
			        int second=(i-1)*duration;
			        String hour=convert_to_time(second/3600);
			        String minute=convert_to_time(second%3600/60);
			        String second_tmp=convert_to_time(second%60);
			        out.write((hour+":"+minute+":"+second_tmp+",000 ").getBytes());
			        out.write("--> ".getBytes());
			        
			        second=i*duration;
			        hour=convert_to_time(second/3600);
			        minute=convert_to_time(second%3600/60);
			        second_tmp=convert_to_time(second%60);
			        out.write((hour+":"+minute+":"+second_tmp+",000\n").getBytes());
		        }
		        else
		        {
		        	out.write((time[i]+"\n").getBytes());
		        }
		        if(content[i]!=null)
		        {
		        	out.write((content[i]+"\n\n").getBytes());
		        }
		        else
		        {
		        	out.write("\n\n".getBytes());
		        }
		        //System.out.println("all_done");
    		}
    		catch(Exception e){
    			System.out.println("出错了"+String.valueOf(i));
    		}
    	}
	}
	
	public void FileInputStreamDemo(String path){
		File file = new File(path);
        BufferedReader reader = null;
        try {
            System.out.println("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;
            int temp=1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                // 显示行号
            	try{
            		if(tempString.length()==0) continue;
            		temp = Integer.parseInt(tempString);
            		if(total<temp)
            		{
            			total=temp;
            		}
            	}catch(Exception e){
            	    if(tempString.indexOf("-->")!=-1)
            	    {
            	    	time[temp]=tempString;
            	    }
            	    else
            	    {
            	    	content[temp]=tempString;
            	    }	
            	}
                line++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
    }
	
	private static String convert_to_time(int x)
    {
    	if(x/10==0)
    	{
    		return "0"+String.valueOf(x);
    	}
    	else
    	{
    		return String.valueOf(x);	
    	}
    }
}
