package com.company;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class Sequential {

    static List<double[][]> createSequential(int Size, Random Random) { //Создание последовательного алгоритма

        List<double[][]> trainings = new ArrayList<>(); //Создаем список данных для тренировки (хранит массивы)
        var trainingInput = new double[Size][4];
        var trainingOutput = new double[1][Size];

        for (int i = 0; i < trainingInput.length; i++) {
            for (int j = 0; j < trainingInput[i].length; j++) {
                trainingInput[i][j] = Random.nextInt(2); //0-1
            }
            if (trainingInput[i][0] == 0)
                trainingOutput[0][i] = 0;
            else
                trainingOutput[0][i] = 1;
        }

        var trainingOutputs = NeuralNetWork.transposeMatrix(trainingOutput); //Транспонируем (потому что НС принимает траснпонированную матрицу)
        trainings.add(trainingInput); //Возврат результата
        trainings.add(trainingOutputs);

        return trainings;
    }

    static void startSequential(int size, Random random, NeuralNetWork curNeuralNetwork, int Interactions){ //Запуск последовательного алгоритма
        var Trainings = createSequential(size, random); //Вызываем функцию (функция возвращает набор данных для training)

        Date start, stop;
        start = new Date();

        curNeuralNetwork.train(Trainings.get(0), Trainings.get(1), Interactions);

        stop = new Date();
        System.out.println("\nВитрачений час: " + (stop.getTime() - start.getTime()) + " мс\n");
    }
}