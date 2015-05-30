package com.Iplayer.convert.main;

import it.sauronsoftware.jave.AudioAttributes;
import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.EncoderException;
import it.sauronsoftware.jave.EncodingAttributes;
import it.sauronsoftware.jave.InputFormatException;

import java.io.File;

import com.Iplayer.convert.caption.caption;
import com.baidu.speech.serviceapi.Sample;

public class convert {

	/**
	 * @param args
	 */
	private static int max_time;
	private static int interval=10;
	public static String videofilename="putclub.com_2015bv21_cameron_eu_chief.rm";
	public static String filename="target";
	public static Sample Baidurecognization=new Sample();
	
	public static void main(String[] args) throws Exception {
		System.out.println(1);
		generate_whole_audio();
		for(int i=1;i<=max_time;++i)
		{
			cut_audio(i,interval);
		}
		caption cap=new caption(max_time,interval,filename);
		//cap.FileInputStreamDemo("res//caption//"+filename+".srt");
		for(int i=1;i<=cap.total;++i)
		{
			String temp=Baidurecognization.method1("res//audio//"+filename+"_"+ String.valueOf(i)+".wav","en");
			if(temp!="error")
			{
				cap.content[i]=temp;
			}
			System.out.println(i);
			System.out.println(cap.time[i]);
			System.out.println(cap.content[i]);
		}
		cap.writecaption();
	}
	
	/**
	 * 从视频中提取出音频文件
	 * WAV，16位，比特率12800，声道1，取样率16000
	 */
	public static void generate_whole_audio()
	{
		File source = new File("res//video//"+videofilename);
		File target = new File("res//audio//"+filename+".wav");
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
	
	public static void cut_audio(int x,int duration)
	{
		File source = new File("res//audio//"+filename+".wav");
		File target = new File("res//audio//"+filename+"_"+ String.valueOf(x)+".wav");
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
