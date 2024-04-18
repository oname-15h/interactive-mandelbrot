import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import java.awt.event.MouseAdapter;

public class run extends Canvas{
	int xsize;
	int ysize;
	int threads;
	int ytrans;
	Graphics cg;
	Canvas thiscanvas;
	BufferedImage[] workingbuffer;
	Thread[] waitingThreads;

	long timestart;
	public long globaltimestart;
	int iteration=0;
	int iterationlimit=7;
	int donecount;

	public run(int xd, int yd, int threadcount) {
		thiscanvas = ((Canvas)this);
		thiscanvas.setBackground(Color.black);
		xsize = xd;
		ysize = yd;
		threads = threadcount;  
		workingbuffer = new BufferedImage[threads];
		waitingThreads= new Thread[threads];

		int tempysize = Math.round(ysize/threads);
		ytrans=tempysize;
		int count=0;
		for(int i=0; i<workingbuffer.length; i++) {
			workingbuffer[i] =new BufferedImage(xsize,tempysize,BufferedImage.TYPE_INT_RGB);
		}
		timestart=System.currentTimeMillis();
		globaltimestart=timestart;
		for(BufferedImage a: workingbuffer) {
			//System.out.println("count:"+count+" xsize:"+xsize+" ysize:"+tempysize*threads+" ySize:"+tempysize+" ytrans:"+tempysize*count);
			calculate(count,a,xsize,tempysize,tempysize*count);
			count++;
		}
		//System.out.println("");

	}
	public void calculate(int index, BufferedImage img,int xsize, int ySize, int ytrans) {
		Graphics2D g2 = (Graphics2D) img.getGraphics();
		Thread a = new Thread() {
			public void run() {
				Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
				//////	
				for(double oi=0; oi<iterationlimit;oi+=1) {
					for(int y=-(ysize/2)+ytrans; y<=(-(ysize/2)+ytrans)+(ySize); y++) {

						//////	
						for(int x = (-xsize/2); x<xsize/2; x++) {
							double rtrans=-0.7241127834;
							double itrans=-0.2864591370;
							int i = calculatemendelbrot(new imag( ((1.0*x/(ysize/4))/Math.pow(10,oi))+rtrans , ((1.0*y/(ysize/4))/Math.pow(10,oi))+itrans ), oi+1);
							int color= (int) ((int)i/(oi+1));
							/*COORDS
							 * 0.4 , 0.1943
							 * -0.02725265 , 0.699
							 * -0.77568377 + 0.13646737i
							 * -0.10109636384562 + 0.95628651080914i
							 * -0.7241127834 + -0.2864591370i
							 * -0.1528364368 + 1.039708485i
							 */

//							g2.setColor(new Color(color,color,color));
//							g2.setColor(colorscheme(color));
//							g2.fillOval((int) (x+(xsize/2)), (int) ((-y)+(-(ysize/2)+ytrans)+(ySize)), 2, 2);
							try {
							img.setRGB((int) (x+(xsize/2)), (int) ((-y)+(-(ysize/2)+ytrans)+(ySize)),colorscheme(color).getRGB());
							}
							catch(ArrayIndexOutOfBoundsException e) {
								//e.printStackTrace();
							}
						}
						//////	
					}
					waitingThreads[index]=Thread.currentThread();
					synchronized(waitingThreads[index]) {
						if(telldone()==0) {
							try {
								waitingThreads[index].wait();
							} catch (InterruptedException e) {e.printStackTrace();}	
						}
					}
				}
				//////	

			}
		};
		a.start();  

	}
	private int telldone() {
		donecount++;
		//System.out.println("done #"+donecount+"/"+threads);
		if(donecount==threads) {
			thiscanvas.repaint();
			System.out.println("×10E"+iteration);
			if(iteration==(iterationlimit-1)) {
				System.out.println("\033[1m"+"TOTAL TIME:"+(System.currentTimeMillis()-globaltimestart)+"ms("+((1.0*System.currentTimeMillis()-globaltimestart)/1000)+"s)\033[0m");
				//System.exit(0);
			}
			iteration++;
			System.out.println("\033[1mTIME: "+(System.currentTimeMillis()-timestart)+"ms\033[0m");
			donecount=0;
			System.out.println("");
			timestart=System.currentTimeMillis();
			for(Thread a: waitingThreads) {

				synchronized(a) {
					a.notifyAll();
				}
			}
			return 1;
		}
		return 0;
	}
	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		//			BufferedImage out = new BufferedImage(xsize,ysize,BufferedImage.TYPE_INT_RGB);
		//			Graphics2D gg2 = ((Graphics2D)out.getGraphics());

		for(int i=threads-1; i>=0; i--) {					
			//					gg2.drawImage(workingbuffer[i], null, 0,((ytrans)*((threads-1)-i)));	
			g2.drawImage(workingbuffer[i], null, 0,((ytrans)*((threads-1)-i)));	
		}
		g2.setColor(Color.blue);
		g2.drawLine(0,ysize/2,xsize,ysize/2);
		g2.drawLine(xsize/2,0,xsize/2,ysize);
		//			int d = thiscanvas.getHeight();
		//			if(thiscanvas.getWidth()<thiscanvas.getHeight()) {
		//				d= thiscanvas.getWidth();
		//			}
		//			g2.drawImage(out.getScaledInstance(d,(int)Math.round(d*0.625),0),0,0,null);
	}
	private int calculatemendelbrot(imag c,double detail) {
		imag z = new imag(0,0);
		double x=0;
		double y=0;
		for(int i=0; i<(int)Math.round(255*detail); i++) {;
		//graph is 400×400
		//need to multiply all y axis by -1 to fix it
		//z=mendelbrot(z,c);
		z=imag.add(imag.multiply(z,z),c);
		x=Math.round(z.realpart());
		y=Math.round(z.imaginarypart());
		if(Math.abs(x)>2.0||Math.abs(y)>2.0) {
			return i;
		}
		}
		return (int) Math.round(255*detail);
	}
	public static Color colorscheme(int a) {
		//0<=a<=255
		//Black -> Red -> Yellow -> White

		double r1,r2,g1,g2,b1,b2;
		int rf= 255;
		int bf=255;
		int gf=255;
		Color Aa = new Color(2,2,2);

		if(a==0) {
			return new Color(0,0,0);
			//BLACK
		}
		else if(a<=85) {
			//RED
			r1=(0*(1.0-(1.0*a/85)));
			g1=(0*(1.0-(1.0*a/85)));
			b1=(0*(1.0-(1.0*a/85)));
			r2=(255*(1.0*a/85));
			g2=(0*(1.0*a/85));
			b2=(0*(1.0*a/85));

			rf=(int)Math.round((r1+r2));
			bf=(int)Math.round((b1+b2));
			gf=(int)Math.round((g1+g2));


		}
		else if(a<=113) {
			a-=85;

			r1=(255*(1.0-(1.0*a/28)));
			g1=(0*(1.0-(1.0*a/28)));
			b1=(0*(1.0-(1.0*a/28)));
			r2=(245*(1.0*a/28));
			g2=(245*(1.0*a/28));
			b2=(66*(1.0*a/28));

			rf=(int)Math.round((r1+r2));
			bf=(int)Math.round((b1+b2));
			gf=(int)Math.round((g1+g2));

			//YELLOW
		}
		else if(a<=255) {
			a-=113;

			r1=(245*(1.0-(1.0*a/142)));
			g1=(245*(1.0-(1.0*a/142)));
			b1=(66*(1.0-(1.0*a/142)));
			r2=(255*(1.0*a/142));
			g2=(255*(1.0*a/142));
			b2=(255*(1.0*a/142));

			rf=(int)Math.round((r1+r2));
			bf=(int)Math.round((b1+b2));
			gf=(int)Math.round((g1+g2));

			//WHITE
		}
		else {
			return new Color(255,255,255);
		}
		return new Color(rf,bf,gf);

	}
	public static void main(String[] args) throws IOException {

		Dimension d = new Dimension(1280,800);
		try {
			JFrame frame = new JFrame("Fractal");
			frame.setSize(d);
			Canvas canvas;
			canvas = new run(d.width,d.height,Runtime.getRuntime().availableProcessors());
			canvas.setSize(d.width, d.height);
			frame.add(canvas);
			frame.setVisible(true);
			//	frame.pack();
			frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
			Runtime.getRuntime().addShutdownHook(new Thread() {
				public void run() {
					long ms = System.currentTimeMillis()-((run)canvas).globaltimestart;
					System.out.println("\033[1m"+"FINAL TIME:"+ms+"ms("+(1.0*ms/1000)+"s ("+((ms/1000)/60)+"m "+((1.0*ms/1000)%60)+"s))\033[0m");
				}
			});

		}
		catch(Exception e) {
			Canvas canvas = new run(d.width,d.height,1);
			BufferedImage img = new BufferedImage(1280,900,BufferedImage.TYPE_INT_RGB);
			canvas.paint(img.getGraphics());
			ImageIO.write(img,"png" ,new File("./graph.png"));
		}
		//		for(int i = 0; i<=255;i++) {
		//			System.out.println(i+"	"+run.colorscheme(i));
		//		}


	}

}
