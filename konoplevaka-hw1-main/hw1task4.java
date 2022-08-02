import java.util.Scanner;

public class taskD {
    public static void main(String[] args) {
        Scanner scn = new Scanner(System.in);
        int N = scn.nextInt();
        int k = scn.nextInt();
        scn.close();

        int start = 0;
        for (int iter = 2; iter <= N; ++iter) {
            start = (start + k) % iter;
        }

        System.out.println(start + 1);
    }
}

