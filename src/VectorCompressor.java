public class VectorCompressor {
    int[][] image = {
            {1, 2, 3, 4, 5, 6},
            {7, 8, 9, 10, 11, 12},
            {13, 14, 15, 16, 17, 18},
            {19, 20, 21, 22, 23, 24},
            {25, 26, 27, 28, 29, 30},
            {31, 32, 33, 34, 35, 36}
    };
    int[][][] vectors;
    public void compress(int codebookSize, int vectorLength, int vectorWidth){
        int numOfBlocks = image.length * image[0].length / (vectorLength * vectorWidth);
        vectors = new int[numOfBlocks][vectorLength][vectorWidth];

        for(int k = 0, x = 0, y = 0; k < numOfBlocks; k++){
            for(int i = 0; i < vectorLength; i++){
                for(int j = 0; j < vectorWidth; j++){
                    vectors[k][i][j] = image[x + i][y + j];
                }
            }
            y = (y + vectorWidth) % image[0].length;
            if(y == 0){
                x += vectorLength;
            }
        }

//        for(int k = 0; k < vectors.length; k++){
//            for(int i = 0; i < vectorLength; i++){
//                for (int j = 0; j < vectorWidth; j++){
//                    System.out.print(vectors[k][i][j] + " ");
//                }
//                System.out.println();
//            }
//            System.out.println();
//        }
    }

    public double[][] getAverageVector(int[][][] vectors){
        double[][] averageVector = new double[vectors[0].length][vectors[0][0].length];
        for(int i = 0; i < vectors[0].length; i++){
            for(int j = 0; j < vectors[0][0].length; j++){
                for(int k = 0; k < vectors.length; k++){
                    averageVector[i][j] += vectors[k][i][j];
                }
            }
        }
        for(int i = 0; i < averageVector.length; i++){
            for(int j = 0 ; j < averageVector[0].length; j++){
                averageVector[i][j] /= vectors.length;
                System.out.print(averageVector[i][j] + " ");
            }
            System.out.println();
        }
        return averageVector;
    }

    public void read(String filePath){

    }

    public void write(){

    }
}
