package com.company;

import java.util.Random;
import java.util.Scanner;
import java.util.stream.IntStream;

/**
 * Training Neural Network using parallel & sequential algorithms
 * Sergey Shulga 2022
 */

public class Main {
    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";

    static void printMatrix(double[][] matrix) //Метод для вывода любой матрицы
    {
        int colLength = matrix[0].length;

        for (double[] doubles : matrix) {
            IntStream.range(0, colLength).mapToObj(j -> doubles[j] + " ").forEach(System.out::print);
            System.out.println();
        }
    }

    static String toString(double[][] arr) //Для возврата строки из массива
    {
        String currentRow = "";

        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                currentRow += arr[i][j] + " ";
            }
        }
        return currentRow;
    }

    static void checkResult(NeuralNetWork neuralnetwork, double[][] training) //Метод для проверки результатов обучения нейронной сети
    {
        var tOutput = neuralnetwork.prediction(training); //Вызываем метод train
        System.out.print("\nРезультати тренування для набору { " + toString(training) + "}: ");
        printMatrix(tOutput); //Вывод результата НС
        if (tOutput[0][0] > 0.5 && training[0][0] == 1 && tOutput[0][0] <= 1)
            System.out.println(GREEN + "Навчання пройшло успішно! \nІстинний (очікуваний) результат = 1. \n" + RESET);
        else if (tOutput[0][0] < 0.5 && training[0][0] == 0 && tOutput[0][0] >= 0)
            System.out.println(GREEN + "Навчання пройшло успішно! \nІстинний (очікуваний) результат = 0. \n" + RESET);
        else
            System.out.println(RED + "Навчання дало збій! Спробуйте перезапустити програму. \n" + RESET);
    }

    static class TThread extends Thread {
        NeuralNetWork currentNeuralNetwork;
        double[][] trainingInputs;
        double[][] trainingOutputs;
        int interactions;

        TThread(NeuralNetWork currentNeuralNetworkurNeuralNetwork, double[][] trainingInputs, double[][] trainingOutputs, int interactions) {
            this.currentNeuralNetwork = currentNeuralNetworkurNeuralNetwork;
            this.trainingInputs = trainingInputs;
            this.trainingOutputs = trainingOutputs;
            this.interactions = interactions;
        }

        public void run() { //Запуск потоков
            currentNeuralNetwork.train(trainingInputs, trainingOutputs, interactions);
        }
    }

    public static void main(String[] args)throws InterruptedException {
        do {

            Random random = new Random(1);
            var currentNeuralNetwork = new NeuralNetWork();

            Scanner scanner = new Scanner(System.in);
            System.out.print("Введіть розмір масиву: ");
            int size = scanner.nextInt();

            if (size >= 5) {

                currentNeuralNetwork.neuralNetWork(1, 4);
                System.out.print("\nВаги до тренування:\n");
                printMatrix(currentNeuralNetwork.synapsesMatrix);
                var trainingMatrix = new double[][]{{1, 1, 1, 1}};

                //Sequential.startSequential(size, random, currentNeuralNetwork, 10000);
                //Parallel.startParallel(size, random, currentNeuralNetwork, 2, 10000);
                Parallel.startParallel(size, random, currentNeuralNetwork, 5, 10000);

                System.out.print("Ваги після тренування:\n");
                printMatrix(currentNeuralNetwork.synapsesMatrix);
                checkResult(currentNeuralNetwork, trainingMatrix);

            } else {
                System.out.println("\nРозмір даних менший за розмір масиву! \nВедіть правильну кількість даних. \n");
            }
        } while (true);
    }
}