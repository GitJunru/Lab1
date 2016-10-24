import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Vector;

public class Second {
    //main method
    public static void main(String[] args) {
        String st = "3+x*2+x*40*y*5*z*4";
        String Words = "";
        Vector<HashMap<Character, Integer>> exp = new Vector<HashMap<Character, Integer>>();
        @SuppressWarnings("resource")
        Scanner in = new Scanner(System.in);
        for (;;) {
            st = in.nextLine();
            if (!st.isEmpty())
                if (st.charAt(0) != '!') {
                    Words = st;
                    if (expression(st, exp) == 0) {
                        printall(exp);
                    } else
                        continue;
                } else if (st.length() >= 9 && st.substring(0, 9).equals("!simplify")) {
                    simplify(st, exp, Words);
                } else if (st.length() > 4 && st.substring(0, 4).equals("!d/d")) {
                    derivative(st, exp, Words);
                } else {
                    System.out.println("Wrong Order!");
                }
        }
    }

    public static int IsNum(char x) {
        if (x >= 48 && x <= 57) {
            return 1;
        } else
            return 0;
    }
    //print the polyminal
    public static void printall(Vector<HashMap<Character, Integer>> V) {
        for (int i = 0; i < V.size(); i++) {
            Iterator<?> iter = V.get(i).entrySet().iterator();
            while (iter.hasNext()) {
                @SuppressWarnings("rawtypes")
                Map.Entry entry = (Map.Entry) iter.next();
                Object key = entry.getKey();
                Object val = entry.getValue();
                if (key.equals(' ')) {

                    System.out.print(val);
                } else {
                    for (int n = 0; n < (int)val; n++) {
                        System.out.print("*" + key);
                    }
                }
            }
            if (i != V.size() - 1) {
                System.out.print("+");
            }
        }
        System.out.println("");
    }
    //check the same and sum them
    public static void check(Vector<HashMap<Character, Integer>> v) {
        int sum = 0;
        for (int i = 0; i < v.size(); i++) {
            if (v.get(i).size() == 1 && v.get(i).get(' ') != null) {
                sum = sum + (int)v.get(i).get(' ');
                v.remove(i);
                i = i - 1;
            }
        }
        HashMap<Character, Integer> SUM = new HashMap<Character, Integer>();
        if (sum != 0) {
            SUM.put(' ', sum);
            v.add(SUM);
        }
    }
    public static int expression(String st, Vector<HashMap<Character, Integer>> V) {
        int NumTemp = 1;
        int temp = 0;
        int sum = 1;
        boolean NumChange = false;
        HashMap<Character, Integer> Term = new HashMap<Character, Integer>();
        V.removeAll(V);
        if (st.matches("\\s*")){
            System.out.println("no input!");
            return -1;
        }
        if(!st.matches("[a-z1-9\\+\\*\\s*]*")){
            System.out.println("Wrong order!");
            return -1;
        }
        for (int i = 0; i < st.length(); i++) {
            if (st.charAt(i) == ' ' || st.charAt(i) == '\t') continue;
            if (IsNum(st.charAt(i)) == 1) {
                temp = temp * 10 + st.charAt(i) - '0';
                NumTemp = temp;
                NumChange = true;
            } else if (st.charAt(i) == '*') {
                sum = sum * NumTemp;
                NumTemp = 1;
                temp = 0;
            } else if (st.charAt(i) == '+') {
                sum = sum * NumTemp;
                Term.put(' ', sum);
                V.addElement(Term);
                Term = new HashMap<Character, Integer>();
                sum = 1;
                NumTemp = 1;
                temp = 0;
                NumChange = false;
            } else {
                NumChange = false;
                temp = 0;
                sum = sum * NumTemp;
                NumTemp = 1;
                if (Term.get(st.charAt(i)) != null && sum != 0) {
                    Term.put(st.charAt(i), Integer.valueOf(Term.get(st.charAt(i)) + 1));
                } else if (sum != 0) {
                    Term.put(st.charAt(i), 1);
                }
            }
        }
        if (NumChange == true && sum != 0) {
            sum = sum * NumTemp;
            Term.put(' ', sum);
            V.addElement(Term);
        } 
        else {
            Term.put(' ', sum);
            V.addElement(Term);
        }
        return 0;
    }
    //simplify method
    @SuppressWarnings("unchecked")
    public static void simplify(String st, Vector<HashMap<Character, Integer>> exp, String st_c) {
        Vector<HashMap<Character, Integer>> expClone = new Vector<HashMap<Character, Integer>>();
        expClone = (Vector<HashMap<Character, Integer>>)exp.clone();
        if (st.length() == 9) {
            printall(exp);
        } else {
            char x = st.charAt(10);
            int value = 0;
            int mi = 0;
            @SuppressWarnings("unused")
            int item = 0;
            //boolean GET=false;
            for (int i = 10; i < st.length(); i++) {
                if (i < st.length() - 1 && IsNum(st.charAt(i)) != 1 && st.charAt(i + 1) == '=') {
                    x = st.charAt(i);
                    continue;
                }
                if (IsNum(st.charAt(i)) == 1) {
                    value = value * 10 + st.charAt(i) - '0';
                }
                if (st.charAt(i) == ' ' || i == st.length() - 1) {
                    for (int j = 0; j < exp.size(); j++) {
                        if (expClone.get(j).get(x) != null) {
                            //GET=true;
                            mi = exp.get(j).get(x);
                            for (int n = 0; n < mi; n++) {
                                expClone.get(j).put(' ', expClone.get(j).get(' ') * value);
                            }
                            expClone.get(j).remove(x);
                        }
                    }
                    value = 0; mi = 0; item = 0;
                }
            }
            check(expClone);
            printall(expClone);
            expression(st_c, exp);
        }
    }
    //derivative function
    public static void derivative(String st, Vector<HashMap<Character, Integer>> exp, String st_c) {
        char c = st.charAt(4);
        Boolean GET = false;
        for (int i = 0; i < exp.size(); i++) {
            if (exp.get(i).size() == 1) {
                exp.remove(i);
                i--;
            } else if (exp.get(i).get(c) == null) {
                exp.remove(i);
                i--;
                continue;
            } else {
                GET = true;
                exp.get(i).put(' ', exp.get(i).get(' ') * exp.get(i).get(c));
                exp.get(i).put(c, exp.get(i).get(c) - 1);
                if (exp.get(i).get(c) == 0) {
                    exp.get(i).remove(c);
                }
            }
        }
        if (GET == false) {
            System.out.println("Error, no variable");
            return;
        }
        check(exp);
        printall(exp);
        expression(st_c, exp);
    }
}
