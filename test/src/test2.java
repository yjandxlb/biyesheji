import java.util.Scanner;

public class test2 {
    public static void main(String[] args) {

        Scanner in=new Scanner(System.in);
        int i = in.nextInt();
        System.out.println(i);
        int a=0;
        int d=0;
        boolean b=false;
        String e="";
        char[] c=new char[10];
        int index=0;
        while(true){
            b=false;
            d=i%10;
            boolean contains = e.contains("a");
            for (int j = 0; j < c.length; j++) {
                if(c[j]==(char)(d+48)){
                    b=true;
                    break;
                }
            }
            if(!b)
            a=a*10+i%10;

            if(i/10==0)
            break;

            c[index]= (char) (d+48);
            index++;
            i=i/10;

        }
        if(c[0]==48){
            System.out.println("0"+a);

        }
        else {
            System.out.println(a);
        }

    }
}
