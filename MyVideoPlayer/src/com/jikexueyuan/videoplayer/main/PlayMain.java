package com.jikexueyuan.videoplayer.main;

import java.awt.EventQueue;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.swing.JFileChooser;
import javax.swing.SwingWorker;
import javax.swing.UIManager;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.player.media.Media;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

import com.Iplayer.convert.main.convert;
import com.jikexueyuan.videoplayer.views.MainWindow;
import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;

import uk.co.caprica.vlcj.binding.*;

import org.slf4j.Logger;  
import org.slf4j.LoggerFactory;
public class PlayMain {

	static MainWindow frame;
	private static final String NATIVE_LIBRARY_SEARCH_PATH = "res/vlc/vlc-2.2.0";
	public static convert video;
	public static Thread t1;
	public static File file;
	
	public static void main(String[] args) {
		NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), getPath()+NATIVE_LIBRARY_SEARCH_PATH);
        System.out.println(LibVlc.INSTANCE.libvlc_get_version());

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel("com.jtattoo.plaf.bernstein.BernsteinLookAndFeel");
					frame = new MainWindow();
					frame.setVisible(true);
					String options[] = {"--subsdec-encoding=utf8"};
					frame.getMediaPlayer().prepareMedia("/Users/acely/Movies/BBC Earth.The.Biography/Earth.The.Biography.UNRATED.Ep04.2007.BluRay.720p.x264.DTS-WiKi.chs.mkv",options);
					//					frame.getMediaPlayer().playMedia("/Users/acely/Movies/BBC Earth.The.Biography/Earth.The.Biography.UNRATED.Ep04.2007.BluRay.720p.x264.DTS-WiKi.chs.mkv",options);
					new SwingWorker<String, Integer>() {


						protected String doInBackground() throws Exception {
							while (true) {
								long total = frame.getMediaPlayer().getLength();
								long curr = frame.getMediaPlayer().getTime();
								float percent = (float)curr/total;
								publish((int)(percent*100));
								Thread.sleep(100);
							}
						}



						protected void process(java.util.List<Integer> chunks) {
							for (int v : chunks) {
								frame.getProgressBar().setValue(v);
							}

						};
					}.execute();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public static void play() {
		frame.getMediaPlayer().play();
		//if(t1!=null) t1.notify();
		System.out.println(getPath()+"res\\caption\\"+
		frame.getMediaPlayer().getMediaMeta().getTitle()+".srt");
		frame.getMediaPlayer().setSubTitleFile(getPath()+
		"res//caption//"+frame.getMediaPlayer().getMediaMeta().getTitle()+".srt");
	}

	public static void pause() {
		frame.getMediaPlayer().pause();
	}

	public static void stop() throws Exception{
		try{
			frame.getMediaPlayer().stop();
		}
		catch(Exception e)
		{
			System.out.println("关闭部分失败");
		}
		//if(t1!=null) t1.wait();
	}

	public static void jumpTo(float to) {
		frame.getMediaPlayer().setTime((long)(to*frame.getMediaPlayer().getLength()));
		video.i=(int) (Math.ceil(to*frame.getMediaPlayer().getLength()/10000));
	}
	
	public static void setVol(int v) {
		frame.getMediaPlayer().setVolume(v);
	}

	public static void openVideo() {
		JFileChooser chooser = new JFileChooser();
		int v = chooser.showOpenDialog(null);
		if (v == JFileChooser.APPROVE_OPTION) {
			File file = chooser.getSelectedFile();
	        String s = file.getAbsolutePath();
	       
			
	        String t = null;
	        try {
				 t = URLDecoder.decode(s, "ascii");
				System.out.println(t);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				System.out.println("dog");				
				e.printStackTrace();
			}
	        //String[] options={"--freetype-fontsize","20"};
			frame.getMediaPlayer().playMedia(t);
			 //开始识别
			if(video!=null) 
			{
				if(video.finished!=true)
				{
					System.out.println(video.file.delete());
				}
				t1.stop();
			}
			try {
				Thread.currentThread().sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			frame.getMediaPlayer().pause();
	        video=new convert(10,s,frame,getPath());
	        t1 = new Thread(video);
			t1.start();
			
			
		}
	}

	@SuppressWarnings("deprecation")
	public static void openSubtitle() {
		JFileChooser chooser = new JFileChooser();
		int v = chooser.showOpenDialog(null);
		if (v == JFileChooser.APPROVE_OPTION) {
			file = chooser.getSelectedFile();
			frame.getMediaPlayer().setSubTitleFile(file);
			t1.suspend();
		}
	}
	
	@SuppressWarnings("deprecation")
	public static void exit() {
		frame.getMediaPlayer().release();
		System.out.println(video.finished);
		if(video.finished!=true)
		{
			System.out.println(video.file.delete());
		}
		t1.stop();
		System.exit(0);
	}
	
	 public static String getPath(){  
		 	/*
	        String filePath = System.getProperty("java.class.path");  
	        String pathSplit = System.getProperty("path.separator");//windows下是";",linux下是":"  
	          
	        if(filePath.contains(pathSplit)){  
	            filePath = filePath.substring(0,filePath.indexOf(pathSplit));  
	        }else if (filePath.endsWith(".jar")) {
	            filePath = filePath.substring(0, filePath.lastIndexOf(File.separator) + 1);  
	              
	        }  
	        
	        return filePath;  
	        */
		 	return "";
	    }  
	
}