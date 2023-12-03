import java.util.ArrayList;

//public class Main {
//
//    public static void main(String[] args) {
//        VectorCompressor vc = new VectorCompressor();
//        ArrayList<ArrayList<Integer>> image = new ArrayList<>();
//        for(int i = 0; i < 8; i++){
//            for(int j = 0; i < 8; i++){
//                image.add(i, new ArrayList<>());
//                image.get(i).add(j);
//            }
//        }
//
//        vc.image = image;
//        vc.compress(4, 2, 2);
//    }
//}

public class Main {

    public static int[][][] splitArray(int[][] originalArray, int n) {
        int rows = originalArray.length;
        int cols = originalArray[0].length;

        int subarrayRows = rows / n;
        int subarrayCols = cols / n;

        int[][][] subarrays = new int[subarrayRows][subarrayCols][n * n];

        for (int i = 0; i < subarrayRows; i++) {
            for (int j = 0; j < subarrayCols; j++) {
                for (int x = 0; x < n; x++) {
                    for (int y = 0; y < n; y++) {
                        subarrays[i][j][x * n + y] = originalArray[i * n + x][j * n + y];
                    }
                }
            }
        }

        return subarrays;
    }

    public static void main(String[] args) {
        int[][] originalArray = {
                {1, 2, 3, 4, 5, 6},
                {5, 6, 7, 8, 5, 6},
                {9, 10, 11, 12, 5, 6},
                {13, 14, 15, 16, 5, 6},
                {13, 14, 15, 16, 5, 6},
                {13, 14, 15, 16, 5, 6}
        };
        VectorCompressor vc = new VectorCompressor();
//        vc.image = originalArray;
        vc.compress(2, 0, 0);
        vc.getAverage(0,0,0,0);
//        int n = 2;  // Size of the subarrays
//        int[][][] result = splitArray(originalArray, n);
//
//        // Print the result
//        for (int[][] subarrayRows : result) {
//            for (int[] subarray : subarrayRows) {
//                for (int value : subarray) {
//                    System.out.print(value + " ");
//                }
//                System.out.println();
//            }
//            System.out.println();
//        }


    }
}