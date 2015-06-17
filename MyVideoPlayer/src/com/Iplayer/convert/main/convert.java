package com.Iplayer.convert.main;

import it.sauronsoftware.jave.AudioAttributes;
import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.EncoderException;
import it.sauronsoftware.jave.EncodingAttributes;
import it.sauronsoftware.jave.InputFormatException;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import uk.co.caprica.vlcj.binding.internal.libvlc_marquee_position_e;
import uk.co.caprica.vlcj.player.Marquee;

import com.Iplayer.convert.caption.caption;
import com.baidu.speech.serviceapi.Sample;
import com.jikexueyuan.videoplayer.views.MainWindow;

public class convert implements Runnable {

	/**
	 * @param args
	 */
	private static int max_time;
	public int interval=10;
	public String videofilename;
	public String captionfilename;
	public static String filename="target";
	public static Sample Baidurecognization=new Sample();
	public int i=1;
	static MainWindow frame;
	public File file = null;
	public boolean finished=false;
	public String root;
	public String videotitle;
	public boolean started=false;
	public boolean settingcaption=false;
	private int count=0;
	
	public convert(int x,String y,MainWindow z,String k,String m)
	{
		interval=x;
		videofilename=y;
		frame=z;
		root=k;
		//String[] tmp=y.split("\\\\");
		//String tmpx=((tmp[tmp.length-1]).split("\\."))[0];
		captionfilename=root+"res//caption//"+m+".srt";
		videotitle=m;
		file=new File(captionfilename);
	}
	
	
	@Override
	public void run() {
		Marquee marquee;
		if(!file.exists()) 
		{
			System.out.println(1);
			generate_whole_audio();
			caption cap = null;
			
			try {
				cap = new caption(max_time,interval,captionfilename);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			while (true)
			{
				for(;i<=max_time;++i)
				{
					int k=i;
					if(cap.content[k]!=null) continue;
					cut_audio(k,interval);
					String temp="";
					try {
						temp = Baidurecognization.method1(root+"res//audio//"+filename+"_"+ String.valueOf(k)+".wav","en");
						System.out.println("识别结果"+temp);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if(temp!="error")
					{
						cap.content[k]=temp;
					}
					else
					{
						cap.content[k]="";
					}
					//System.out.println(k);
					//System.out.println(cap.time[k]);
					//System.out.println(cap.content[k]);
					try {
						cap.writecaption();
						started=true;
						count++;
						if(settingcaption) 
						{
							frame.getMediaPlayer().enableMarquee(false);
							 marquee= Marquee.marquee()
								    .text("Caption Is Automatically Recognized..completed "+String.valueOf(count)+"/"+String.valueOf(max_time))
								    .size(30)
								    .colour(Color.WHITE)
								    .timeout(100000)
								    .position(libvlc_marquee_position_e.top_left)
								    .opacity(0.8f)
								    .enable();
							marquee.apply(frame.getMediaPlayer());
							
							frame.getMediaPlayer().setSubTitleFile(file);
						}
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				//判断是否识别完
				
				for(int j=1;j<=max_time;++j)
				{
					if(cap.content[j]==null) 
					{
						i=j;
						break;
					}
					else
					{
						if(j==max_time) 
						{
							finished=true;
						}
					}
				}
				
				if(finished) break;
				
			}
		}
		else
		{
			finished=true;
			started=true;
			System.out.println("已有字幕，直接读取");
			if(settingcaption) frame.getMediaPlayer().setSubTitleFile(file);
		}
		frame.getMediaPlayer().setSubTitleFile(file);
		System.out.println("good");
	}
	
	public void main(String[] args) throws Exception {

	}
	
	/**
	 * 从视频中提取出音频文件
	 * WAV，16位，比特率12800，声道1，取样率16000
	 */
	public void generate_whole_audio()
	{
		File source = new File(videofilename);
		File target = new File(root+"res//audio//"+filename+".wav");
		AudioAttributes audio = new AudioAttributes();
		audio.setCodec("pcm_s16le");
		audio.setBitRate(new Integer(128000));
		audio.setChannels(new Integer(1));
		audio.setSamplingRate(new Integer(16000));
		EncodingAttributes attrs = new EncodingAttributes();
		attrs.setFormat("wav");
		attrs.setAudioAttributes(audio);
		Encoder encoder = new Encoder();
		try {
			max_time=(int) Math.ceil((float)encoder.getInfo(source).getDuration()/10000);
			System.out.println(max_time);
		} catch (InputFormatException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (EncoderException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			encoder.encode(source, target, attrs);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InputFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (EncoderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 将音频文件分隔成N段
	 * @param x：第几段音频（从1开始）
	 */
	
	public void cut_audio(int x,int duration)
	{
		File source = new File(root+"res//audio//"+filename+".wav");
		File target = new File(root+"res//audio//"+filename+"_"+ String.valueOf(x)+".wav");
		AudioAttributes audio = new AudioAttributes();
		audio.setCodec("pcm_s16le");
		audio.setBitRate(new Integer(128000));
		audio.setChannels(new Integer(1));
		audio.setSamplingRate(new Integer(16000));
		EncodingAttributes attrs = new EncodingAttributes();
		attrs.setFormat("wav");
		attrs.setOffset((float) duration*(x-1));
		attrs.setDuration((float) duration);
		attrs.setAudioAttributes(audio);
		Encoder encoder = new Encoder();
		try {
			encoder.encode(source, target, attrs);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InputFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (EncoderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}



}
