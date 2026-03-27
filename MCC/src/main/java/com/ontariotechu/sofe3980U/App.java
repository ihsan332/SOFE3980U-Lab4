package com.ontariotechu.sofe3980U;


import java.io.FileReader; 
import java.util.List;
import com.opencsv.*;

/**
 * Evaluate Single Variable Continuous Regression
 *
 */
public class App 
{
    public static void main( String[] args )
	{
    String filePath="model.csv";
    FileReader filereader;
    List<String[]> allData;
    try{
        filereader = new FileReader(filePath);
        CSVReader csvReader = new CSVReaderBuilder(filereader).withSkipLines(1).build();
        allData = csvReader.readAll();
    }
    catch(Exception e){
        System.out.println("Error reading the CSV file");
        return;
    }

    int n = allData.size();
    double ce = 0;
    int[][] confusion = new int[5][5];

	// this loop iterates through each row of the data
	//  calculates the true value and predicted value
	// and then updates the CE and confusion matrix calculations.
    for (String[] row : allData) {
        int y_true = Integer.parseInt(row[0]);
        float[] y_predicted = new float[5];
        for (int i = 0; i < 5; i++){
            y_predicted[i] = Float.parseFloat(row[i+1]);
        }

        // CE
        ce += Math.log(y_predicted[y_true - 1]);

        // Confusion matrix
        int y_hat = 0;
        for (int i = 1; i < 5; i++){
            if (y_predicted[i] > y_predicted[y_hat]) y_hat = i;
        }

        confusion[y_hat][y_true - 1]++;
    }

    ce = -ce / n;

    System.out.println("CE =" + (float)ce);
    System.out.println("Confusion matrix");
    System.out.println("\t\ty=1\t y=2\t y=3\t y=4\t y=5");
	// this loop prints out the confusion matrix in a readable format.
    for (int i = 0; i < 5; i++){
        System.out.print("\ty^=" + (i+1) + "\t");
        for (int j = 0; j < 5; j++){
            System.out.print(confusion[i][j] + "\t");
        }
        System.out.println();
    }
}
	
}
