import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import static java.lang.Math.*;

public class VectorCompressor {
//    int[][] image = {
//            {1, 2, 3, 4, 5, 6},
//            {7, 8, 9, 10, 11, 12},
//            {13, 14, 15, 16, 17, 18},
//            {19, 20, 21, 22, 23, 24},
//            {25, 26, 27, 28, 29, 30},
//            {31, 32, 33, 34, 35, 36}
//    };
//    int[][] image = {
//        {1, 2, 7, 9, 4, 11},
//        {3, 4, 6, 6, 12, 12},
//        {4, 9, 15, 14, 9, 9},
//        {10, 10, 20, 18, 8, 8},
//        {4, 3, 17, 16, 1, 4},
//        {4, 5, 18, 18, 5, 6}
//    };
    int[][] image;
    int[][][] vectors;
    ArrayList<double[][]> codebook = new ArrayList<>();
    public void compress(int codebookSize, int vectorLength, int vectorWidth, String filePath){
        read(filePath);
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
        ArrayList<int[][]> blocks = new ArrayList<>();
        blocks.addAll(Arrays.asList(vectors));
        split(getAverageVector(blocks), codebookSize);
//        for(int k = 0; k < codebook.size(); k++){
//            for(int i = 0; i < vectorLength; i++){
//                for(int j = 0; j < vectorWidth; j++){
//                    System.out.print(codebook.get(k)[i][j] + " ");
//                }
//                System.out.println();
//            }
//            System.out.println();
//        }


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

    public double[][] getAverageVector(ArrayList<int[][]> vectors){
        double[][] averageVector = new double[vectors.get(0).length][vectors.get(0)[0].length];
        for(int i = 0; i < vectors.get(0).length; i++){
            for(int j = 0; j < vectors.get(0)[0].length; j++){
                for(int k = 0; k < vectors.size(); k++){
                    averageVector[i][j] += vectors.get(k)[i][j];
                }
            }
        }
        for(int i = 0; i < averageVector.length; i++){
            for(int j = 0 ; j < averageVector[0].length; j++){
                averageVector[i][j] /= vectors.size();
//                System.out.print(averageVector[i][j] + " ");
            }
//            System.out.println();
        }
        return averageVector;
    }

    public void split(double[][] vector, int codebookSize){
        if(codebookSize == codebook.size()){
            int[] distances = new int[codebookSize];
            String compressedStream = "";
            for(int k =0; k < vectors.length; k++){
                for(int i = 0; i < vectors[0].length; i++){
                    for(int j = 0; j < vectors[0][0].length; j++){
                        for(int l = 0; l < codebookSize; l++){
                            distances[l] += abs(vectors[k][i][j] - codebook.get(l)[i][j]);
                            distances[l] += abs(vectors[k][i][j] - codebook.get(l)[i][j]);
                        }
                    }
                }

                int minDistance = distances[0], minIndex = 0;
                for(int i = 1; i < distances.length; i++){
                    if(distances[i] < minDistance){
                        minDistance = distances[i];
                        minIndex = i;
                    }
                }
                Arrays.fill(distances, 0);
                compressedStream += Integer.toBinaryString(minIndex);
            }

            for(int k = 0; k < codebook.size(); k++){
                for (int i = 0; i < codebook.get(0).length; i++){
                    for(int j = 0; j < codebook.get(0)[0].length; j++){
                        codebook.get(k)[i][j] = ceil(codebook.get(k)[i][j]);
                    }
                }
            }
            write(compressedStream);
            return;
        }

        codebook.remove(vector);
        int[][] vector1 = new int[vector.length][vector[0].length];
        int[][] vector2 = new int[vector.length][vector[0].length];
        for(int i = 0; i < vector.length; i++){
            for(int j = 0; j < vector[0].length; j++){
                if(floor(vector[i][j]) != vector[i][j]){
                    vector1[i][j] = (int) floor(vector[i][j]);
                    vector2[i][j] = (int) ceil(vector[i][j]);
                } else {
                    vector1[i][j] = (int) vector[i][j] - 1;
                    vector2[i][j] = (int) vector[i][j] + 1;
                }
            }
        }
        ArrayList<int[][]> nearToVector1 = new ArrayList<>();
        ArrayList<int[][]> nearToVector2 = new ArrayList<>();

        for(int k =0; k < vectors.length; k++){
            int distanceFrom1 = 0, distanceFrom2 = 0;
            for(int i = 0; i < vectors[0].length; i++){
                for(int j = 0; j < vectors[0][0].length; j++){
                    distanceFrom1 += abs(vectors[k][i][j] - vector1[i][j]);
                    distanceFrom2 += abs(vectors[k][i][j] - vector2[i][j]);
                }
            }
            if(distanceFrom1 <= distanceFrom2){
                nearToVector1.add(vectors[k]);
            } else{
                nearToVector2.add(vectors[k]);
            }
        }
        double[][] averageVector1 = getAverageVector(nearToVector1);
        double[][] averageVector2 = getAverageVector(nearToVector2);
        codebook.add(averageVector1);
        codebook.add(averageVector2);
        split(averageVector1, codebookSize);
        split(averageVector2, codebookSize);
//        for(int i = 0; i < vector1.length; i++){
//            for(int j = 0; j < vector1[0].length; j++){
//                System.out.print(vector1[i][j] + " ");
//            }
//            System.out.println();
//        }
//        System.out.println();
//        for(int i = 0; i < vector2.length; i++){
//            for(int j = 0; j < vector2[0].length; j++){
//                System.out.print(vector2[i][j] + " ");
//            }
//            System.out.println();
//        }
    }

    public void read(String filePath){
        File file = new File(filePath);
        try{
            BufferedImage img = ImageIO.read(file);
            int width = img.getWidth();
            int height = img.getHeight();
            image = new int[width][height];
            Raster raster = img.getData();
            for(int i = 0; i < width; i++){
                for (int j = 0; j < height; j++){
                    image[i][j] = raster.getSample(i, j, 0);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void write(String compressedStream){
        File file = new File("output.bin");
        int lastByteSize = compressedStream.length() % 8;
        try{
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(codebook.size());
            fos.write(image.length);
            fos.write(image[0].length);
            fos.write(codebook.get(0).length);
            fos.write(codebook.get(0)[0].length);
            for(int i = 0; i < codebook.size(); i++){
                for (int j = 0; j < codebook.get(0).length; j++) {
                    for (int k = 0; k < codebook.get(0)[0].length; k++) {
                        fos.write((int) codebook.get(i)[j][k]);
                    }
                }
            }
            fos.write(lastByteSize);
            String current = "";
            for(int i = 0; i < compressedStream.length(); i++){
                current += compressedStream.charAt(i);
                if(current.length() == 8){
                    fos.write((char) Integer.parseInt(current, 2));
                    current = "";
                }
            }
            if(current != ""){
                String leadingZeros = "";
                for(int i = 0; i < 8 - current.length(); i++){
                    leadingZeros += "0";
                }
                current = leadingZeros + current;
                fos.write((char) Integer.parseInt(current, 2));
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
