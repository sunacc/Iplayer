package com.jikexueyuan.videoplayer.views;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

import javax.swing.JButton;

import com.jikexueyuan.videoplayer.main.PlayMain;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JProgressBar;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSlider;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

public class MainWindow extends JFrame {

	private JPanel contentPane;
	EmbeddedMediaPlayerComponent playerComponent;
	private JPanel panel;
	private JButton btnPlay;
	private JButton btnPause;
	private JButton btnStop;
	private JPanel controlPanel;
	private JProgressBar progress;
	private JMenuBar menuBar;
	private JMenu mnFile;
	private JMenuItem mntmOpenVideo;
	private JMenuItem mntmOpenSubtitle;
	private JMenuItem mntmExit;
	private JSlider slider;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
	}

	/**
	 * Create the frame.
	 */
	public MainWindow() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 616, 466);
		
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		mntmOpenVideo = new JMenuItem("Open Video...");
		mnFile.add(mntmOpenVideo);
		
		mntmOpenSubtitle = new JMenuItem("Open Subtitle...");
		mnFile.add(mntmOpenSubtitle);
		
		mntmExit = new JMenuItem("Exit");
		mnFile.add(mntmExit);
		
		mntmOpenVideo.addActionListener(new ActionListener() {
			
			
			public void actionPerformed(ActionEvent e) {
				PlayMain.openVideo();
			}
		});
		
		mntmOpenSubtitle.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				PlayMain.openSubtitle();
			}
		});
		
		mntmExit.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				PlayMain.exit();
			}
		});
		
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel videopane = new JPanel();
		contentPane.add(videopane, BorderLayout.CENTER);
		videopane.setLayout(new BorderLayout(0, 0));
		
		playerComponent = new EmbeddedMediaPlayerComponent();
		videopane.add(playerComponent);
		
		panel = new JPanel();
		videopane.add(panel, BorderLayout.SOUTH);
		panel.setLayout(new BorderLayout(0, 0));
		
		controlPanel = new JPanel();
		panel.add(controlPanel);
		
		btnStop = new JButton("Stop");
		controlPanel.add(btnStop);
		
		btnPlay = new JButton("Play");
		controlPanel.add(btnPlay);
		
		btnPause = new JButton("Pause");
		controlPanel.add(btnPause);
		
		slider = new JSlider();
		slider.setValue(100);
		slider.setMaximum(120);
		slider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				PlayMain.setVol(slider.getValue());
			}
		});
		controlPanel.add(slider);
		
		progress = new JProgressBar();
		progress.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				int x = e.getX();
				PlayMain.jumpTo((float)x/progress.getWidth());
				progress.setValue((int)((float)x/progress.getWidth()*100));
			}
		});
		progress.setStringPainted(true);
		panel.add(progress, BorderLayout.NORTH);
		btnPause.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				PlayMain.pause();
			}
		});
		btnPlay.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent arg0) {
				PlayMain.play();
			}
		});
		btnStop.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				
					try {
						PlayMain.stop();
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				
			}
		});
	}

	public EmbeddedMediaPlayer getMediaPlayer() {
		return playerComponent.getMediaPlayer();
	}
	
	public JProgressBar getProgressBar() {
		return progress;
	}
}
