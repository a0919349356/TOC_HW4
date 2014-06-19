
import org.json.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.*;
import java.net.*;
import java.util.*;

class node
{
	int first;
	int high,low;
	int cnt;
	boolean[][] mon = new boolean[40][13];
	String name;
	
	public node(int x,int y,String n,int doll)
	{
		first=x;
		cnt=1;
		name = n;
		high=low=doll;
		for(int i=0;i<40;i++)
		{
			for(int j=1;j<=12;j++)
				mon[i][j]=false;
		}
		mon[y/100-80][y%100]=true;
	}
	
}

public class TocHw4 {
	
	public static void main(String[] argv) throws IOException, JSONException
	{
		String tmp = getUrlSource(argv[0]);
		List<node> list = new ArrayList<node>();
		HashMap<String, node> box = new HashMap<String, node>();
		int max=0;
		JSONArray  yy=null;;
		yy = new JSONArray(tmp);
		for(int i=0;i<yy.length();i++)
		{
			JSONObject jo = yy.getJSONObject(i);
			String add=jo.getString("土地區段位置或建物區門牌");
			int pos=-1;
			if(add.lastIndexOf("大道")!=-1)
			{
				pos=add.lastIndexOf("大道")+1;
			}
			else if(add.lastIndexOf('街')!=-1)
			{
				pos=add.lastIndexOf('街');
			}
			else if(add.lastIndexOf('路')!=-1)
			{
				pos=add.lastIndexOf('路');
			}
			else if(add.lastIndexOf('巷')!=-1)
			{
				pos=add.lastIndexOf('巷');
			}
			if(pos == -1)
				continue;
			add=add.substring(0, pos+1);
			node tt=box.get(add);
			if(tt==null)
			{
				int year = jo.getInt("交易年月");
				int doll=jo.getInt("總價元");
				node n=new node(i,year,add,doll);
				box.put(add,n);
			}
			else
			{
				int month = jo.getInt("交易年月")%100;
				int year = jo.getInt("交易年月")/100-80;
				if(jo.getInt("總價元")>tt.high)
					tt.high=jo.getInt("總價元");
				if(jo.getInt("總價元")<tt.low)
					tt.low=jo.getInt("總價元");
				if(tt.mon[year][month]==false)
				{
					tt.mon[year][month]=true;
					tt.cnt++;
					if(tt.cnt>max)
					{
						max=tt.cnt;
						list.clear();
						list.add(tt);
					}
					else if(tt.cnt==max)
					{
						list.add(tt);
					}
				}
			}
		}
		Collections.sort(list,
		        new Comparator<node>() {
		            public int compare(node o1, node o2) {
		                return o1.first-o2.first; 
		            }
		        });
		for(node o:list)
		{
			System.out.println(o.name+", 最高成交價: "+o.high+", 最低成交價: "+o.low);
		}
		//System.out.println(minye);
		
		
	}
	private static String getUrlSource(String url) throws IOException {
        URL yahoo = new URL(url);
        URLConnection yc = yahoo.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(
                yc.getInputStream(), "UTF-8"));
        String inputLine;
        StringBuilder a = new StringBuilder();
        while ((inputLine = in.readLine()) != null)
            a.append(inputLine);
        in.close();

        return a.toString();
    }
}
