public class test4 {
    public static void main(String[] args) {
        String str="dsafanfvapdifiaiogfa";
        int a='2';

        char A='a';
        char B='b';
        int count=0;
        char[] c=new char[str.length()];
        for (int i = 0; i < str.length(); i++) {
            if(A==str.charAt(i)){
                c[i]=B;
                count++;
            }

            else {
                c[i]=str.charAt(i);
            }

        }
        String d=new String(c);
        System.out.println(d);
        System.out.println(count);
    }
}
