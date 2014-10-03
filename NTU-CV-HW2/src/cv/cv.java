package cv;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

public class cv {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		
		//
		//System.out.print("test1");
		 String fileName = "./assets/lena.im";
		 int headerLength = 172;
		 int imageWidth = 512;
		 int imageHeight = 512;
		 int unit = 1;
		 int threshold = 128;
		 
		 //static final int UNDEFINED = -1;
		// final int CHECKED = -2;
		 
		 //hw1-a
		 ArrayList<Integer> bytes = GetByteData(fileName);
		 for(int y = 0 ; y<imageHeight; y++)
		 {
			 for(int x = 0; x <imageWidth;x++)
			 {
				 int data = bytes.get(headerLength+(y*imageWidth+x)*unit);
				 
				 if (data < threshold) data = 0;
				 else data = 255;
				 
				 bytes.set(headerLength+(y*imageWidth+x)*unit, data);
			 }
		 }
		 WriteOut(bytes,"./assets/hw2-a.im");
		 
		 
		 //hw2-b
		 bytes = GetByteData(fileName);
		 Hashtable<Integer,Integer> hashtable = new Hashtable<Integer,Integer>();
		 for(int i =0;i<256;i++)
		 {
			 hashtable.put(i, 0);
		 }
		 
		 for(int y = 0 ; y<imageHeight; y++)
		 {
			 for(int x = 0; x <imageWidth;x++)
			 {
				 int data = bytes.get(headerLength+(y*imageWidth+x)*unit);
				 
				 int oldValue = hashtable.get(data);
				 int newValue = oldValue + 1;
				 hashtable.replace(data, newValue);
			 }
		 }
		 
		 
		 File f = new File("./assets/hw2-b.csv");
		 if(f.exists())f.delete();
		 FileWriter writer = new FileWriter(f);
		 
		 writer.write("value, count\n");
		 for(int i = 0 ;i<256;i++)
		 {
			 int value = i;
			 int data = hashtable.get(i);
			 
			 writer.write(value+","+data+"\n");
		 }
		 writer.close();
		 
		 //System.out.println("test2");
		 //hw2-c
		 bytes = GetByteData(fileName);
		 for(int y = 0 ; y<imageHeight; y++)
		 {
			 for(int x = 0; x <imageWidth;x++)
			 {
				 int data = bytes.get(headerLength+(y*imageWidth+x)*unit);
				 
				 if (data < threshold) data = 0;
				 else data = 255;
				 
				 bytes.set(headerLength+(y*imageWidth+x)*unit, data);
			 }
		 }
		 
		// System.out.println("test3");
		 
		//Init
		 
		 int i = 0;
		 ArrayList<Integer> records = new ArrayList<Integer>();
		 HashMap<Integer,ArrayList<Integer>> GroupMap = new HashMap<Integer,ArrayList<Integer>>();
		 for(int y = 0 ; y<imageHeight; y++)
		 {
			 for(int x = 0; x <imageWidth;x++)
			 {
				 records.add(i);
				
				 ArrayList<Integer> oneItem = new ArrayList<Integer>();
				 
				 oneItem.add(i);
				 GroupMap.put(i, oneItem);
				 
				 i++;
			 }
		}
		 
		// System.out.print(GroupMap);
		 
		 
		 for(int y = 0 ; y<imageHeight; y++)
		 {
			 for(int x = 0; x <imageWidth;x++)
			 {
				 connectMapCheck(bytes,records,GroupMap,x,y);
			 }
		}
		
		
	     ArrayList<ArrayList<Integer>> candidates = new  ArrayList<ArrayList<Integer>>();
	     
		
		for(int keyValue : GroupMap.keySet())
		{
			ArrayList<Integer> alloc = GroupMap.get(keyValue);
			
			if(alloc!=null&&alloc.size()>=500)
			{
				candidates.add(GroupMap.get(keyValue));
			//test +=1;
				System.out.println("keyValue("+keyValue+"):"+GroupMap.get(keyValue).size());
			}
		}
		
		ArrayList<Rectangle> rectangles = new ArrayList<Rectangle>();
		
		for(ArrayList<Integer> candidate : candidates)
		{
			rectangles.add(getRectangle(candidate));
		}
		
		for(Rectangle rec :rectangles)
		{
			ReverseColor(bytes,rec);
			System.out.println("x1:"+rec.x1+" x2:"+rec.x2+" y1:"+rec.y1+" y2:"+rec.y2);
		}
		
		 WriteOut(bytes,"./assets/hw2-c.im");
		System.out.println("done");
	}
	
	public static void ReverseColor(ArrayList<Integer> Origin, Rectangle rec)
	{
		
		int centerX = (rec.x1 + rec.x2) /2;
		int cneterY = (rec.y1 + rec.y2) /2;
		int ArrowSize = 10;
		int ArrowWidth = 3;
		
		
		for(int x =rec.x1 ; x<=rec.x2;x++)
		{
			for(int y = rec.y1;y<=rec.y2;y++)
			{
				
				int old = Origin.get(172+ y*512+x);
				
				
				if((x >= centerX - ArrowSize &&x<=centerX + ArrowSize&& y >= cneterY - ArrowWidth && y <= cneterY + ArrowWidth)||
				   (x >= centerX - ArrowWidth &&x<=centerX + ArrowWidth&& y >= cneterY - ArrowSize && y <= cneterY + ArrowSize)	)
				{
					//Region of Arrow
				}
				else
				{
					if(old > 128) old = 0;
					else if(old <= 128)old = 255;
					Origin.set(172+ y*512+x, old);
				}
			}
		}
	}
	
	public static Rectangle getRectangle(ArrayList<Integer> candidate)
	{
		Rectangle rec = new Rectangle();
		rec.x1 = rec.x2 = candidate.get(0)%512;
		rec.y1 = rec.y2 = (int) Math.floor(candidate.get(0)/512);
	
		
		//for(int i =0;i<)
		
		
		System.out.println("size:"+candidate.size());
		
		for(Integer i : candidate)
		{
			//System.out.println(i);
			
			int x = i%512;
			int y = (int) Math.floor(i/512);
			
			if(x<rec.x1)rec.x1 = x;
			if(x>rec.x2)rec.x2 = x;
			
			if(y<rec.y1)rec.y1 = y;
			if(y>rec.y2)rec.y2 = y;
		}
		
		
		return rec;
	}

	
	
	
	
	static public void connectMapCheck(ArrayList<Integer> Origin,ArrayList<Integer> records,HashMap<Integer,ArrayList<Integer>> GroupMap,int x,int y)
	{
		int headerLength = 172;
		int imageWidth = 512;
		int imageHeight = 512;
		int unit = 1;
		int threshold = 128;
		
		
		
		if(Origin.get(headerLength+(y*imageWidth+x))==0)
		{
			GroupMap.get(x+y*imageWidth).clear();
			records.set(x+y*imageWidth, -1);
			return;
		}
		else
		{	
			
			if(y-1>0)
			{
				if(Origin.get(headerLength+(x+(y-1)*imageWidth))!=0)
				merge(x+(y-1)*imageWidth,x+(y)*imageWidth,records,GroupMap);
			}
			
			if(y+1<imageHeight)
			{
				if(Origin.get(headerLength+(x+(y+1)*imageWidth))!=0)
				merge(x+(y+1)*imageWidth,x+(y)*imageWidth,records,GroupMap);
			}
			
			if(x-1>0)
			{
				if(Origin.get(headerLength+(x-1+(y)*imageWidth))!=0)
				merge(x-1+(y)*imageWidth,x+(y)*imageWidth,records,GroupMap);
			}
			
			if(x+1<imageWidth)
			{
				if(Origin.get(headerLength+(x+1+(y)*imageWidth))!=0)
				merge(x+1+(y)*imageWidth,x+(y)*imageWidth,records,GroupMap);
			}
		}
	}
	
	public static void merge(int HighPriorityIndex,int targetIndex,ArrayList<Integer> record,HashMap<Integer,ArrayList<Integer>> GroupMap)
	{
		int origin = record.get(targetIndex);
		ArrayList<Integer> killArray = GroupMap.get(origin);
		ArrayList<Integer> biggerArray = GroupMap.get(record.get(HighPriorityIndex));
		
		if(killArray == biggerArray) return;
		

		
		//refreashRecord
		for(int i= 0;i<killArray.size();i++)
		{
			record.set(killArray.get(i),record.get(HighPriorityIndex));
		}
		biggerArray.addAll(killArray);
		
		GroupMap.replace(origin,null);
		killArray.clear();
		
		
		System.out.println(biggerArray.size());
		
	}
	
	public static void WriteOut(ArrayList<Integer> data,String name) throws IOException
	{
		File f = new File(name);
		
		if(f.exists())f.delete();
		
		FileOutputStream out = null;
		out = new FileOutputStream(name);
		
		for(int i : data)
		{
			out.write((byte)i);
		}
		
		out.flush();
		out.close();
		
	}
	
 public static class Rectangle
	{
		public int x1 = -1;
		public int x2 = -1;
		public int y1 = -1;
		public int y2 = -1;
	}
	
	public static ArrayList<Integer> GetByteData(String fileName) throws IOException
	{
		
		
		 File f = new File(fileName);
		 ArrayList<Integer> bytes = new ArrayList<Integer>();
		
		 //System.out.println("file exist:"+f.exists());
		
		 FileInputStream in = null;
		 in = new FileInputStream(fileName);
		 
		 int c;
		 while ((c = in.read()) != -1) {
			 bytes.add(c);
        }
		 
		 return bytes;
	}
	
	

}
