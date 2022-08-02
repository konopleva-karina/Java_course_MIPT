import java.util.Scanner;
import java.util.ArrayList;

class StackMax {
    ArrayList<Integer> stack_max_;

    StackMax(ArrayList<Integer> B) {
        stack_max_ = new ArrayList<>(B.size());
        for (int idx = 0; idx < B.size(); ++idx) {
            stack_max_.add(0);
        }
        int curr_max_idx = B.size() - 1;
        for (int idx = B.size() - 1; idx >= 0; --idx) {
            if (B.get(idx) >= B.get(curr_max_idx)) {
                curr_max_idx = idx;
            }
            stack_max_.set(idx, curr_max_idx);
        }
    }

    public Integer get(int idx) {
        return stack_max_.get(idx);
    }
}

public class TaskA {
    public static void main(String[] args) {
        Scanner scn = new Scanner(System.in);
        int n = scn.nextInt();
        ArrayList<Integer> A = new ArrayList<>(n);
        ArrayList<Integer> B = new ArrayList<>(n);
        for (int idx = 0; idx < n; ++idx) {
            A.add(scn.nextInt());
        }
        for (int idx = 0; idx < n; ++idx) {
            B.add(scn.nextInt());
        }
        scn.close();

        StackMax stackMaxForB = new StackMax(B);
        int i_0 = 0;
        for (int idx = 0; idx < n; ++idx) {
            if (A.get(i_0) + B.get(stackMaxForB.get(i_0)) < A.get(idx) + B.get(stackMaxForB.get(idx))) {
                i_0 = idx;
            }
        }
        System.out.println(i_0 + " " + stackMaxForB.get(i_0));
    }
}

