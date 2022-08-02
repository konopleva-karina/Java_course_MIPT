import java.util.Scanner;
import java.util.ArrayList;

class Vertex {
    int x_;
    int y_;

    Vertex(int x, int y) {
        x_ = x;
        y_ = y;
    }
}

class Square {
    public static boolean isPositivelyOriented(Vertex start, Vertex end) {
        return  start.x_ <= end.x_;
    }

    public static long calculateDoubleSquare(Vertex start, Vertex end) {
        if (!isPositivelyOriented(start, end)) {
            return -calculateDoubleSquare(end, start);
        }
        return (long) (start.y_ + end.y_) * (end.x_ - start.x_);
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner scn = new Scanner(System.in);
        int vertices_numb = scn.nextInt();
        ArrayList<Vertex> vertexes = new ArrayList<>(vertices_numb);

        for (int idx = 0; idx < vertices_numb; ++idx) {
            vertexes.add(new Vertex(scn.nextInt(), scn.nextInt()));
        }
        scn.close();

        long square = 0;
        for (int idx = 0; idx < vertices_numb; ++idx) {
            Vertex start = vertexes.get(idx);
            Vertex end = vertexes.get((idx + 1) % vertices_numb);
            square += Square.calculateDoubleSquare(start, end);
        }
        System.out.println(square * 0.5);
    }
}

