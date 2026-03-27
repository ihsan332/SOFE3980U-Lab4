package com.ontariotechu.sofe3980U;


import java.io.FileReader; 
import java.util.List;
import com.opencsv.*;

public class App 
{
    public static void main( String[] args )
{   
    // Set file paths for the three models
    String[] files = {"model_1.csv", "model_2.csv", "model_3.csv"};
    double[] mseValues = new double[3];
    double[] maeValues = new double[3];
    double[] mareValues = new double[3];

    // What this for loop does is that it iterates through each of the three model files
    // reads the data, calculates the MSE, MAE, and MARE for each model, and stores 
    // those values in the respective arrays. It also prints out the results 
    // for each model.
    for (int f = 0; f < files.length; f++) {
        String filePath = files[f];
        FileReader filereader;
        List<String[]> allData;
        // Try to read the CSV file. If there's an error, print a message and exit.
        try {
            filereader = new FileReader(filePath);
            CSVReader csvReader = new CSVReaderBuilder(filereader).withSkipLines(1).build();
            allData = csvReader.readAll();
        } catch (Exception e) {
            System.out.println("Error reading the CSV file");
            return;
        }

        double mse = 0, mae = 0, mare = 0;
        int n = allData.size();
        
        // Iterate through each row of the data, calculate the true value and predicted value
        // and update the MSE, MAE, and MARE calculations.
        for (String[] row : allData) {
            float y_true = Float.parseFloat(row[0]);
            float y_predicted = Float.parseFloat(row[1]);
            mse += Math.pow(y_true - y_predicted, 2);
            mae += Math.abs(y_true - y_predicted);
            mare += Math.abs(y_true - y_predicted) / (Math.abs(y_true) + 1e-10);
        }

        mse /= n;
        mae /= n;
        mare /= n;

        mseValues[f] = mse;
        maeValues[f] = mae;
        mareValues[f] = mare;

        System.out.println("for " + filePath);
        System.out.println("\tMSE =" + (float)mse);
        System.out.println("\tMAE =" + (float)mae);
        System.out.println("\tMARE =" + (float)mare);
    }

    // Find the best model in each metric and print it out
    int bestMSE = 0, bestMAE = 0, bestMARE = 0;
    for (int i = 1; i < 3; i++) {
        if (mseValues[i] < mseValues[bestMSE]) bestMSE = i;
        if (maeValues[i] < maeValues[bestMAE]) bestMAE = i;
        if (mareValues[i] < mareValues[bestMARE]) bestMARE = i;
    }

    System.out.println("According to MSE, The best model is " + files[bestMSE]);
    System.out.println("According to MAE, The best model is " + files[bestMAE]);
    System.out.println("According to MARE, The best model is " + files[bestMARE]);
}
}
