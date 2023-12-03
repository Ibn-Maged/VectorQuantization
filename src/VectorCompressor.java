import java.util.ArrayList;

public class VectorCompressor {
    int[][] image = {
            {1, 2, 3, 4, 5, 6},
            {5, 6, 7, 8, 5, 6},
            {9, 10, 11, 12, 5, 6},
            {13, 14, 15, 16, 5, 6},
            {13, 14, 15, 16, 5, 6},
            {13, 14, 15, 16, 5, 6}
    };
    int[][][] vectors = new int[3][3][4];
    int codebookSize = 2;

    public void compress(int codebookSize, int vectorSizeX, int vectorSizeY){
        int rows = image.length;
        int cols = image[0].length;
        this.codebookSize = codebookSize;

        int subarrayRows = rows / codebookSize;
        int subarrayCols = cols / codebookSize;

//        int[][][] subarrays = new int[subarrayRows][subarrayCols][codebookSize * codebookSize];

        for (int i = 0; i < subarrayRows; i++) {
            for (int j = 0; j < subarrayCols; j++) {
                for (int x = 0; x < codebookSize; x++) {
                    for (int y = 0; y < codebookSize; y++) {
                        vectors[i][j][x * codebookSize + y] = image[i * codebookSize + x][j * codebookSize + y];
                    }
                }
            }
        }

    }

    void getAverage(int x1, int x2, int y1, int y2){
        double[][] average = new double[codebookSize][codebookSize];
        for(int i = 0; i < codebookSize; i++){
            for(int j = 0; j < codebookSize; j++){
                average[i][j] = 0;
            }
        }
        for(int i = 0; i < vectors.length; i++){
            for(int j = 0; j < vectors[i].length; j++){
                for(int k = 0; k < vectors[i][j].length; k++){
                    average[j][k] += vectors[i][j][k];
                }
            }
        }
        for(int i = 0; i < codebookSize; i++){
            for(int j = 0; j < codebookSize; j++){
                average[i][j] /= vectors.length;
                System.out.println(average[i][j]);
            }
        }
    }

    public void read(String filePath){

    }

    public void write(){

    }
}
