import java.util.Scanner;
import java.util.ArrayList;

public class TaskC {
    public static void main(String[] args) {
        Scanner scn = new Scanner(System.in);
        int size_A = scn.nextInt();
        ArrayList<Integer> A = new ArrayList<>(size_A);
        for (int idx = 0; idx < size_A; ++idx) {
            A.add(scn.nextInt());
        }

        int size_B = scn.nextInt();
        ArrayList<Integer> B = new ArrayList<>(size_B);
        for (int idx = 0; idx < size_B; ++idx) {
            B.add(scn.nextInt());
        }

        int k = scn.nextInt();

        scn.close();

        int idx_A = 0;
        int idx_B = size_B - 1;

        int answer = 0;

        while (idx_B >= 0 && idx_A < size_A) {
            if (A.get(idx_A) + B.get(idx_B) > k) {
                --idx_B;
            } else if (A.get(idx_A) + B.get(idx_B) < k) {
                ++idx_A;
            } else if (A.get(idx_A) + B.get(idx_B) == k) {
                ++idx_A;
                --idx_B;
                ++answer;
            }
        }

        while (idx_B >= 0) {
            if (A.get(size_A - 1) + B.get(idx_B) == k) {
                ++answer;
                break;
            }
            --idx_B;
        }

        while (idx_A < size_A) {
            if (A.get(idx_A) + B.get(0) == k) {
                ++answer;
                break;
            }
            ++idx_A;
        }

        System.out.println(answer);
    }
}

