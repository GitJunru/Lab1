import java.util.*;

public class second {
	
	public static void main(String[] args) {
		String st="3+x*2+x*40*y*5*z*4"; 		//任意初始化字串
		String Words="";
		Vector<HashMap<Character,Integer>> exp=new Vector<HashMap<Character,Integer>>();
		//HashMap<String,Integer> Term=new HashMap<String,Integer>();
		Scanner in=new Scanner(System.in);
		for(;;){
			st=in.nextLine();
			if(st.charAt(0)!='!'){
				Words=st;
				if(expression(st,exp)==0){
					PrintAll(exp);
				}
				else
					continue;
			}
			else if(st.length()>=9 && st.substring(0,9).equals("!simplify")){
				simplify(st,exp,Words);
			}
			else if(st.length()>4 && st.substring(0,4).equals("!d/d")){
				derivative(st,exp,Words);
			}
			else{
				System.out.println("Wrong Order!");
			}
		}
	}
	
	public static int IsNum(char x){		//判断是否为数字
		if(x>=48 && x<=57)
			return 1;
		else
			return 0;
	}
	public static void PrintAll(Vector<HashMap<Character,Integer>> V){		//打印表达式
		for(int i=0;i<V.size();i++){
			Iterator iter = V.get(i).entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				Object key = entry.getKey();
				Object val = entry.getValue();
				if(key.equals(' ')){
					System.out.print(val);
				}
				else{
					for(int n=0;n<(int)val;n++){
						System.out.print("*"+key);
					}
				}
			}
			if(i!=V.size()-1){
				System.out.print("+");
			}
		}
		System.out.println("");
	}
	public static void Check(Vector<HashMap<Character,Integer>> v){		//检查函数，检查是否存在常数未合并或未形成常数项
		int sum=0;
		for(int i=0;i<v.size();i++){
			if(v.get(i).size()==1 && v.get(i).get(' ')!=null){
				sum=sum+(int)v.get(i).get(' ');
				v.remove(i);
				i=i-1;
			}
		}
		HashMap<Character,Integer> SUM=new HashMap<Character,Integer>();
		if(sum!=0){
			SUM.put(' ', sum);
			v.add(SUM);
		}
	}
	public static int Right(char x){		//检查函数，判断是否存在不可接受的字符
		if(x>='0' && x<='9' || x>='a' &&x<='z' || x=='+' ||x=='*' ||x==' ' ||x=='\t')
			return 1;
		else
			return 0;
	}
 	public static int expression(String st,Vector<HashMap<Character,Integer>> V){		//表达式处理函数
		int NumTemp=1;
		int temp=0;
		boolean NumChange=false;
		HashMap<Character,Integer> Term=new HashMap<Character,Integer>();
		V.removeAll(V);
		for(int i=0;i<st.length();i++){
			if(Right(st.charAt(i))==0){
				System.out.println("Wrong Order!");
				return -1;
			}
			if(st.charAt(i)==' ' || st.charAt(i)=='\t') continue;
			if(IsNum(st.charAt(i))==1){
				temp=temp*10+st.charAt(i)-'0';
				NumChange=true;
			}
			else if(st.charAt(i)=='*'){
				if(temp!=0){
					NumTemp=NumTemp*temp;
					NumChange=true;
					temp=0;
				}
			}
			else if(st.charAt(i)=='+'){
				if(temp!=0){
					NumTemp=NumTemp*temp;
					NumChange=true;
				}
				if(NumChange==true){
					if(temp==0){
						NumTemp=0;
					}
					Term.put(' ', NumTemp);
				}
				else
					Term.put(' ',1);
				V.addElement(Term);
				Term=new HashMap<Character,Integer>();
				NumTemp=1;
				temp=0;
				NumChange=false;
			}
			else{
				if(Term.get(st.charAt(i))!=null){
					Term.put(st.charAt(i), Integer.valueOf(Term.get(st.charAt(i))+1));
				}
				else{
					Term.put(st.charAt(i), 1);
				}
			}
		}
		if(temp!=0){
			NumTemp=NumTemp*temp;
		}
		Term.put(' ', NumTemp);
		V.addElement(Term);
		return 0;
	}
	public static void simplify(String st,Vector<HashMap<Character,Integer>> exp,String st_c){
		Vector<HashMap<Character,Integer>> expClone=new Vector<HashMap<Character,Integer>>();
		expClone=(Vector)exp.clone();
		if(st.length()==9){
			PrintAll(exp);
		}
		else{
			char x=st.charAt(10);
			int value=0;
			int mi=0;
			int item=0;
			//boolean GET=false;
			for(int i=10;i<st.length();i++){
				if(i<st.length()-1 && IsNum(st.charAt(i))!=1 && st.charAt(i+1)=='='){
					x=st.charAt(i);
					continue;
				}
				if(IsNum(st.charAt(i))==1){
					value=value*10+st.charAt(i)-'0';
				}
				if(st.charAt(i)==' ' || i==st.length()-1){
					for(int j=0;j<exp.size();j++){
						if(expClone.get(j).get(x)!=null){
							//GET=true;
							mi=exp.get(j).get(x);
							for(int n=0;n<mi;n++){
								expClone.get(j).put(' ',expClone.get(j).get(' ')*value);
							}
							expClone.get(j).remove(x);
						}
					}
					value=0;mi=0;item=0;
				}
			}
			Check(expClone);
			PrintAll(expClone);
			//exp.removeAll(exp);
			expression(st_c,exp);
		}
	}
	public static void derivative(String st,Vector<HashMap<Character,Integer>> exp,String st_c){
		char c=st.charAt(4);
		Boolean GET=false;
		for(int i=0;i<exp.size();i++){
			if(exp.get(i).size()==1){
				exp.remove(i);
				i--;
			}
			else if(exp.get(i).get(c)==null){
				exp.remove(i);
				i--;
				continue;
			}
			else{
				GET=true;
				exp.get(i).put(' ', exp.get(i).get(' ')*exp.get(i).get(c));
				exp.get(i).put(c, exp.get(i).get(c)-1);
				if(exp.get(i).get(c)==0){
					exp.get(i).remove(c);
				}
			}
		}
		if(GET==false){
			System.out.println("Error, no variable");
			return;
		}
		Check(exp);
		PrintAll(exp);
		expression(st_c,exp);
	}
}

//1.4第一次修改

//1.7第二次修改

//2.5 B1修改四