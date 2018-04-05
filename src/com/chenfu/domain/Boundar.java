package com.chenfu.domain;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
@SuppressWarnings("serial")
public class Boundar extends Frame{

    private int wide;
    private int hight;
    private int size;
    private int cleanCount;
    private int wincount;
    private int score;
    private int speed;
    private boolean isOver;
    private boolean ispause;
    private boolean isHard;
    private int colorfiag;
    private Image image;
    private Square s;
    private int[][] map;
    private Color squareBodyColor;
	private Color squareNodeColor;
    private Color wallColor; 
 
   
	public int[][] getMap() {
		return map;
	}

	public Color getSquareBodyColor() {
		return squareBodyColor;
	}
	
	public Color getSquareNodeColor() {
		return squareNodeColor;
	}
	
	public Color getWallColor() {
		return wallColor;
	}
	public static void main(String[] args) {
		Boundar b = new Boundar();
		b.init();
    }
    public void init(){
    	loadProperties();
    	map = new int[hight][wide];
    	isOver =  false;
		ispause = false;
		colorfiag=1;
		score=0;
		speed=500;
		createMap();
		s = new Square(size,isHard,this);
		run();
    }
    public void loadProperties(){
    	Properties properties = new Properties();
    	InputStream inStream = Boundar.class.getClassLoader().getResourceAsStream("setting.properties");  
    	try {
			properties.load(inStream);
			String strwide = properties.getProperty("wide");
			String strhight = properties.getProperty("hight");
			String strsize = properties.getProperty("size");
			String strcount = properties.getProperty("cleanCount");
			String strwincount = properties.getProperty("wincount");
			String strsquareBodyColor =properties.getProperty("squareBodyColor");
			String strsquareNodeColor =properties.getProperty("squareNodeColor");
			String strwallColor =properties.getProperty("wallColor");
			String strisHard =properties.getProperty("isHard");
			wide=Integer.parseInt(strwide);
			hight=Integer.parseInt(strhight);
			size=Integer.parseInt(strsize);
			cleanCount=Integer.parseInt(strcount);
			wincount=Integer.parseInt(strwincount);
			isHard =Boolean.parseBoolean(strisHard);
			squareBodyColor = new Color(Integer.parseInt(strsquareBodyColor));
			squareNodeColor = new Color(Integer.parseInt(strsquareNodeColor));
			wallColor = new Color(Integer.parseInt(strwallColor));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    public void createsquare()
    {
    	s = new Square(size,isHard,this);
    }
    public void run()
    {
    	
    	this.setLocation(800,50);
    	this.setSize(wide*size,hight*size);
    	this.setBackground(Color.WHITE);
    	this.setResizable(false);
    	this.addWindowListener(new WindowAdapter() {
    		
			public void windowClosing(WindowEvent e) {
			
				System.exit(0);
			}
    	
		});
    	
    	this.setVisible(true);
    	Reflash R=new Reflash();
    	new Thread(R).start();
    	this.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				s.keyPressed(e);
				int key = e.getKeyCode();
				if(key==KeyEvent.VK_SPACE)
				{
					createMap();
					isOver=false;
					score=0;
					new Thread(R).start();
				}
				else if(key==KeyEvent.VK_Y)
				{
					createMap();
				}
			}
    		
		});
    	
    }
    public void createMap()
    {
    	int i,j;
    	for(i=0;i<hight;i++)
    	{
    		for(j=0;j<wide;j++)
    		{
    			if(i==hight-1||j==0||j==wide-1)
    			{
    				map[i][j]=2;
    			}
    			else
    			{
    				map[i][j]=0;
    			}
    		}
    	}
    
    }
    public void update(Graphics g) 
   	{
   		if(image==null)
   		{
   			image=this.createImage(wide*size,hight*size);
   		}
   		Graphics a = image.getGraphics();
   		Color c = a.getColor();
   		a.setColor(Color.WHITE);
   		a.fillRect(0, 0,wide*size,hight*size);
   		a.setColor(c);
   		paint(a);
   		g.drawImage(image, 0, 0, null);
   	}
   public void paint(Graphics g)
    {
	  	Color c = g.getColor();
    	s.draw(g);
    	drawMap(g);
    	g.setColor(Color.BLUE);
		g.setFont(new Font("ËÎÌו",Font.BOLD,20));
    	g.drawString("Score:"+score,wide*size-150,50);
    	if(isOver==true)
    	{
    		g.setColor(Color.BLUE);
    		g.setFont(new Font("ËÎÌו",Font.BOLD,20));
    		g.drawString("author:chenfu",wide*size/2-80,hight*size/2+20);
    		g.drawString("Version:v2.4",wide*size/2-80,hight*size/2+40);
    		g.setColor(Color.red);
    		g.setFont(new Font("ËÎÌו",Font.BOLD,40));
    		g.drawString("Game Over",wide*size/2-100,hight*size/2);
    	}
    	g.setColor(c);
    }
   public void drawMap(Graphics g)
   {
	   for(int i=0;i<hight;i++)
   	{
   		for(int j=0;j<wide;j++)
   		{
   			if(map[i][j]==4)
   			{
   				g.setColor(squareNodeColor);
   				g.fillRect(j*size, i*size, size, size);
   			}
   			else if(map[i][j]==2)
   			{
   				g.setColor(wallColor);
   				g.fillRect(j*size, i*size, size, size);
   			}
   			else if(map[i][j]==1)
   			{
   				g.setColor(squareBodyColor);
   				g.fillRect(j*size, i*size, size, size);
   			}
   			else if(map[i][j]==3||map[i][j]==5)
   			{
   				if(colorfiag>0)
   				{
   					g.setColor(Color.GRAY);
       				g.fillRect(j*size, i*size, size, size);
   				}
   				else
   				{
   					g.setColor(Color.WHITE);
       				g.fillRect(j*size, i*size, size, size);
   				}
   				
   			}	
   		}
   	}
   	colorfiag*=-1;
   }
   public void deleteslant()
   {
	   for(int i=hight-1;i>=0;i--)
		{
			for(int j=wide-1;j>0;j--)
			{
				if(checkRow(i, j)==true||checkLine(i, j)==true||
				checkRightDiagonal(i, j)==true||checkBackDiagonal(i, j)==true)
				{
					checkEmptyGrid();
				}
			}
		}
   }
   public boolean check(int i,int j,char a,char b,char c,char d,int type)
	{
	   		int count1=0;
	  		while(map[operate(i,a,count1)][operate(j,b,count1)]==4)
				{
					count1++;
					if(operate(i,a,count1)<0||operate(j,b,count1)<0)
					{
						break;
					}
				}
	  		if(count1>cleanCount)
			{
				deleteEffect(i, j,count1,type);
				for(int k=0;k<count1;k++)
				{
					map[operate(i,a,k)][operate(j,b,k)]=4;
					checkRightDiagonal(operate(i,a,k),operate(j,b,k));
					checkBackDiagonal(operate(i,a,k),operate(j,b,k));
					checkLine(operate(i,a,k),operate(j,b,k));
					checkRow(operate(i,a,k),operate(j,b,k));
					map[operate(i,a,k)][operate(j,b,k)]=0;
				}
				return true;
			}	
	  		int count2=0;
	  		while(map[operate(i,c,count2)][operate(j,d,count2)]==4)
			{
				count2++;
				if(operate(i,c,count2)>hight-1||operate(j,d,count2)>wide-1)
				{
					break;
				}
				
			}
				if(count2>cleanCount)
				{
					deleteEffect(i, j,count2,type+4);
					for(int k=0;k<count2;k++)
					{
						map[operate(i,c,k)][operate(j,d,k)]=4;
						checkRightDiagonal(operate(i,c,k),operate(j,d,k));
						checkBackDiagonal(operate(i,c,k),operate(j,d,k));
						checkLine(operate(i,c,k),operate(j,d,k));
						checkRow(operate(i,c,k),operate(j,d,k));
						map[operate(i,c,k)][operate(j,d,k)]=0;
					}
					return true;
				}
			return false;
	} 
		
@SuppressWarnings("null")
public int operate (int a,char operator,int b){
	switch(operator){
		case '-':return a-b;
		case '+':return a+b;
		case 'l':return a;
	}
	return (Integer) null;
}
   public boolean checkRightDiagonal(int i,int j)
	{
	   		return check(i,j,'-','-','+','+',2);
	} 
		public boolean checkBackDiagonal(int i,int j)  
	{
			return check(i,j,'-','+','+','-',3);
	}
		public boolean checkLine(int i,int j)  
		{
			return check(i,j,'-','l','+','l',4);
		}
		public boolean checkRow(int i, int j) {
			
			return check(i,j,'l','-','l','+',5);
		}


	public void deleteLine() {
		int k = 0;
		for (int i = hight - 1; i >= 0; i--) {
			for (int j = 0; j < wide; j++) {
				if (map[i][j] == 1) {
					k++;
				}

			}
			if (k == wide - 2) {
				score += 10;
				deleteEffect(i, 0, 0, 1);
				for (int a = i; a > 0; a--) {

					for (int b = 0; b < wide; b++) {
						map[a][b] = map[a - 1][b];
					}
				}
				i++;
			}
			k = 0;
		}
	}
   public void checkEmptyGrid()
   {
	   int k;
	   boolean empty;
	   for(int i=hight-1;i>=0;i--)
		{
			for(int j=wide-1;j>=0;j--)
			{
				k=0;
				empty=false;
			   if(map[i][j]==1||map[i][j]==4)
			   {
				   	while(map[i+k+1][j]==0)
				   	{
				   		empty=true;
				   		if(i+k>hight)
				   		break;
				   		k++;
					}
				   	if(empty==true)
				   	{
				   		map[i+k][j]=map[i][j];
				   		map[i][j]=0;
				   		empty=false;
				   	}
			   }
			}
		}
   }
	public boolean isOver() {
	return isOver;
}
	public void setOver(boolean isOver) {
		this.isOver = isOver;
	}
	public void deleteEffect(int i,int j,int count,int type)
	{
			Reflash r = new Reflash();
		switch (type)
		{
		case 1 :deleteLineEffect(i);
				r.pause(500);
				break;
		case 2 :
			for(int k=0;k<count;k++)
			{
				map[i-k][j-k]=5;
			}
				r.pause(500);
				break;
		case 3 :
			for(int k=0;k<count;k++)
			{
				map[i-k][j+k]=5;
			}
				r.pause(500);
				break;
		case 4:
			for(int k=0;k<count;k++)
			{
				map[i-k][j]=5;
			}
			r.pause(500);
				break;
		case 5:
			for(int k=0;k<count;k++)
			{
				map[i][j-k]=5;
			}
			r.pause(500);
				break;
		case 6 :
			for(int k=0;k<count;k++)
			{
				map[i+k][j+k]=5;
			}
				r.pause(500);
				break;
		case 7 :
			for(int k=0;k<count;k++)
			{
				map[i+k][j-k]=5;
			}
				r.pause(500);
				break;
		case 8:
			for(int k=0;k<count;k++)
			{
				map[i+k][j]=5;
			}
			r.pause(500);
				break;
		case 9:
			for(int k=0;k<count;k++)
			{
				map[i][j+k]=5;
			}
			r.pause(500);
				break;
		}
	}
	public void deleteLineEffect(int i)
	{
		for(int j=1;j<wide-1;j++)
		{
			map[i][j]=3;
		}
	}
	
	 public void gameOver()
	 {
		 	for(int i=0;i<wide;i++)
		 	{
		 		if(map[1][i]==1)
		 		{
		 			isOver=true;
		 		}
		 	}
	  }
	 public void victory()
	 {
		 if(score==wincount)
		 {
			 isOver=true;
		 }
	 }
	 public void drawString()
	 {
		 if(score==400)
		 {
			 Reflash R = new Reflash();
			 R.pause(500);
			 score+=10;
		 }
	 }
	 class Reflash implements Runnable
	 {		
		public void run() {
			while(!isOver)
			{
				try {
					s.down();
					if(!ispause)
					{
						repaint();
					}
					if(score<=300)
					{
						Thread.sleep(speed-score);
					}
					else
					{
						Thread.sleep(200);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		public void pause(int i)
		{
			try {
				ispause=true;
				for(int count=0;count<10;count++)
				{
				repaint();
				Thread.sleep(i/10);
				}
				ispause=false;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
}
