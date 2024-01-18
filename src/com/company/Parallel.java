package com.company;

import java.util.*;

public class Parallel {

    static List<Main.TThread> createParallel(int size, Random random, NeuralNetWork currentNeuralNetwork, int tCount, int interactions) { //Создаем параллельный алгоритм

        //Инициализация списка массивов
        List<double[][]> input = new ArrayList<>();
        List<double[][]> output = new ArrayList<>();

        for (int i = 0; i < tCount; i++) { //Создаем необходимое кол-во input & output
            input.add(new double[size / tCount][4]);
            output.add(new double[1][size / tCount]);
        }
        // Заполнение списков массивов (распределение данных)
        for (int k = 0; k < tCount; k++) {
            for (int i = 0; i < input.get(k).length; i++) {
                for (int j = 0; j < input.get(k)[i].length; j++) {
                    input.get(k)[i][j] = random.nextInt(2); //0-1 бинарные
                }
                if (input.get(k)[i][0] == 0) {
                    output.get(k)[0][i] = 0;
                } else {
                    output.get(k)[0][i] = 1;
                }
            }
        }

        //Создание массивов trainingOutputs (данные на которых обучается НС (истина))
        List<double[][]> trainings = new ArrayList<>();
        for (double[][] ou : output) {
            trainings.add(NeuralNetWork.transposeMatrix(ou));
        }

        List<Main.TThread> threads = new ArrayList<>(); //Список, в котором сохраняем нужное кол-во потоков

        for (int count = 0; count < tCount; count++) {  //Итерация потоков
            threads.add(new Main.TThread(currentNeuralNetwork, input.get(count), trainings.get(count), interactions)); //Создаем нужное кол-во потоков + передаем в них списки input & output
        }
        return threads;
    }

    static void startParallel(int Size, Random Random, NeuralNetWork curNeuralNetwork, int TCount, int Interactions) throws InterruptedException { //Запуск потоков (параллельного алгоритма)
        Date start, stop;
        var Threads = createParallel(Size, Random, curNeuralNetwork, TCount, Interactions); //Вызываем список который создает CreateParallel

        start = new Date();
        for (var t : Threads)
            t.start(); //Запускаем потоки
        for (var t : Threads)
            t.join(); //Ожидает остановку всех потоков. (Чтобы знать, в какой момент времени остановилось выполнение потоков)
        stop = new Date();

        System.out.println("\nВитрачений час: " + (stop.getTime() - start.getTime()) + " мс\n");
    }
}