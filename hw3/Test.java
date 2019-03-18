package hw3;

public class Test {
    public static void main(String[] args){
        String t = "five";
        boolean b = true;
        for (int i = 0; i < t.length(); i++){
            if (!(t.charAt(i) > 0 && t.charAt(i) < 9)){
                b = false;
            }
        }
//        b = true;
        if(!b) {
            System.out.println(Float.parseFloat(t));
        }else{
            System.out.println(b);
        }
    }
}
