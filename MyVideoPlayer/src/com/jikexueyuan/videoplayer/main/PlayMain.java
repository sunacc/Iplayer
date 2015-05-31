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
	private static final String NATIVE_LIBRARY_SEARCH_PATH = "F:/学习/交大/作业/大二下/软件工程/大作业/播放器/vlc-2.2.0-win64/vlc-2.2.0";
	public static convert video;
	public static Thread t1;
	
	public static void main(String[] args) {
		NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), NATIVE_LIBRARY_SEARCH_PATH);
        System.out.println(LibVlc.INSTANCE.libvlc_get_version());

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
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

						private void publish(int i) {
							// TODO Auto-generated method stub
							
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
	}

	public static void pause() {
		frame.getMediaPlayer().pause();
	}

	public static void stop() {
		frame.getMediaPlayer().stop();
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
			frame.getMediaPlayer().playMedia(t);
			
			 //开始识别
	        video=new convert(10,s,frame);
	        t1 = new Thread(video);
			t1.start();
			frame.getMediaPlayer().pause();
		}
	}

	public static void openSubtitle() {
		JFileChooser chooser = new JFileChooser();
		int v = chooser.showOpenDialog(null);
		if (v == JFileChooser.APPROVE_OPTION) {
			File file = chooser.getSelectedFile();
			frame.getMediaPlayer().setSubTitleFile(file);
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
}