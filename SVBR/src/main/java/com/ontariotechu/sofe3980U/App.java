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
    // Set file paths for the three models
    String[] files = {"model_1.csv", "model_2.csv", "model_3.csv"};
    double[] bceValues = new double[3];
    double[] accValues = new double[3];
    double[] precValues = new double[3];
    double[] recValues = new double[3];
    double[] f1Values = new double[3];
    double[] aucValues = new double[3];

    // Same as the SVCR loop but with calculations for BCE
    // confusion matrix, accuracy, precision, recall, f1 score, and AUC ROC.
    for (int f = 0; f < files.length; f++) {
        String filePath = files[f];
        FileReader filereader;
        List<String[]> allData;
        try {
            filereader = new FileReader(filePath);
            CSVReader csvReader = new CSVReaderBuilder(filereader).withSkipLines(1).build();
            allData = csvReader.readAll();
        } catch (Exception e) {
            System.out.println("Error reading the CSV file");
            return;
        }

        double bce = 0;
        int TP = 0, FP = 0, TN = 0, FN = 0;
        int n = allData.size();

        // this for loop iterates through each row of the data
        // calculates the true value and predicted value
        for (String[] row : allData) {
            int y_true = Integer.parseInt(row[0]);
            float y_predicted = Float.parseFloat(row[1]);

            // if the true value is 1, we add the log of 
            // the predicted probability to the BCE
            if (y_true == 1) bce += Math.log(y_predicted);
			else bce += Math.log(1 - y_predicted);

            // Confusion matrix at threshold 0.5
            int y_binary = y_predicted >= 0.5 ? 1 : 0;
            if (y_true == 1 && y_binary == 1) TP++;
            else if (y_true == 0 && y_binary == 1) FP++;
            else if (y_true == 0 && y_binary == 0) TN++;
            else if (y_true == 1 && y_binary == 0) FN++;
        }

        bce = -bce / Math.log(10);
    
        double accuracy = (double)(TP + TN) / (TP + TN + FP + FN);
        double precision = (double)TP / (TP + FP);
        double recall = (double)TP / (TP + FN);
		double f1 = 2 * precision * recall / (precision + recall);

        // Calculate AUC ROC
        int nPositive = 0, nNegative = 0;
        for (String[] row : allData) {
            if (Integer.parseInt(row[0]) == 1) nPositive++;
            else nNegative++;
        }

        double[] x = new double[101];
        double[] y = new double[101];
        // Iterate through thresholds from 0 to 1 and calculate
        // TPR and FPR for each threshold
        for (int i = 0; i <= 100; i++) {
            double th = i / 100.0;
            int tp = 0, fp = 0;
            for (String[] row : allData) {
                int y_true = Integer.parseInt(row[0]);
                float y_predicted = Float.parseFloat(row[1]);
                if (y_true == 1 && y_predicted >= th) tp++;
                if (y_true == 0 && y_predicted >= th) fp++;
            }
            y[i] = (double)tp / nPositive;
            x[i] = (double)fp / nNegative;
        }

        double auc = 0;
        for (int i = 1; i <= 100; i++) {
            auc += (y[i-1] + y[i]) * Math.abs(x[i-1] - x[i]) / 2;
        }

        bceValues[f] = bce;
        accValues[f] = accuracy;
        precValues[f] = precision;
        recValues[f] = recall;
        f1Values[f] = f1;
        aucValues[f] = auc;

        System.out.println("for " + filePath);
        System.out.println("\tBCE =" + (float)bce);
        System.out.println("\tConfusion matrix");
        System.out.println("\t\t\ty=1\t y=0");
        System.out.println("\t\ty^=1\t" + TP + "\t" + FP);
        System.out.println("\t\ty^=0\t" + FN + "\t" + TN);
        System.out.println("\tAccuracy =" + (float)accuracy);
        System.out.println("\tPrecision =" + (float)precision);
        System.out.println("\tRecall =" + (float)recall);
        System.out.println("\tf1 score =" + (float)f1);
        System.out.println("\tauc roc =" + (float)auc);
    }

    // Find best model in each metric and print it out
    int bestBCE = 0, bestAcc = 0, bestPrec = 0, bestRec = 0, bestF1 = 0, bestAUC = 0;
    for (int i = 1; i < 3; i++) {
        if (bceValues[i] < bceValues[bestBCE]) bestBCE = i;
        if (accValues[i] > accValues[bestAcc]) bestAcc = i;
        if (precValues[i] > precValues[bestPrec]) bestPrec = i;
        if (recValues[i] > recValues[bestRec]) bestRec = i;
        if (f1Values[i] > f1Values[bestF1]) bestF1 = i;
        if (aucValues[i] > aucValues[bestAUC]) bestAUC = i;
    }

    String[] files2 = {"model_1.csv", "model_2.csv", "model_3.csv"};
    System.out.println("According to BCE, The best model is " + files2[bestBCE]);
    System.out.println("According to Accuracy, The best model is " + files2[bestAcc]);
    System.out.println("According to Precision, The best model is " + files2[bestPrec]);
    System.out.println("According to Recall, The best model is " + files2[bestRec]);
    System.out.println("According to F1 score, The best model is " + files2[bestF1]);
    System.out.println("According to AUC ROC, The best model is " + files2[bestAUC]);
}
	
}
