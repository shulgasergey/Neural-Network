package com.company;

import java.util.Random;

public class NeuralNetWork { //Клас нейронной сети

    private Random random; //Создаем переменную рандома (для матрицы синапсиса)
    public int synapseMatrixColumns; //Колонки матрицы синапсиса
    public int synapseMatrixLines; //Строчки матрицы синапсиса
    public double[][] synapsesMatrix; //Матрица синапсиса

    public void neuralNetWork(int SynapseMatrixColumn, int SynapseMatrixLine) { //Конструктор нейронки
        synapseMatrixColumns = SynapseMatrixColumn; //Передаем кол-во колонок в матрицу синапсиса
        synapseMatrixLines = SynapseMatrixLine; //Передаем кол-во строк в матрицу синапсиса

        random = new Random(1);
        generateSynapsesMatrix(); //Функция генерации матрицы синапсиса
    }

    private void generateSynapsesMatrix() { //Генерация матрицы синапсиса
        synapsesMatrix = new double[synapseMatrixLines][synapseMatrixColumns]; //Сама матрица синапсиса

        for (var i = 0; i < synapseMatrixLines; i++) { //Заполнение матрицы синапсиса
            for (var j = 0; j < synapseMatrixColumns; j++) {
                synapsesMatrix[i][j] = (2 * random.nextDouble()) - 1;
            }
        }
    }

    private double[][] сalculateSigmoid(double[][] matrix) { //Считаем сигмоиду
        int rowLength = matrix.length; //Кол-во строк
        int colLength = matrix[0].length; //Кол-во столбцов

        for (var i = 0; i < rowLength; i++) {
            for (var j = 0; j < colLength; j++) {
                var value = matrix[i][j];
                matrix[i][j] = 1 / (1 + Math.exp(value * -1));
            }
        }
        return matrix;
    }

    private double[][] сalculateSigmoidDerivative(double[][] matrix) { //Вычисляем производную матрицы синапсиса
        int rowLength = matrix.length;
        int colLength = matrix[0].length;

        for (var i = 0; i < rowLength; i++) {
            for (var j = 0; j < colLength; j++) {
                var Value = matrix[i][j];
                matrix[i][j] = Value * (1 - Value);
            }
        }
        return matrix;
    }

    public double[][] prediction(double[][] inputMatrix) { //Метод для предсказания (проверка работы нейронки)
        var productOfTheInputsAndWeights = dotProductMatrix(inputMatrix, synapsesMatrix); //Передаем матрицу + матрицу синапсиса

        return сalculateSigmoid(productOfTheInputsAndWeights); //Передаем сюда предыдущую переменную и вызываем сигмоиду
    }

    public void train(double[][] trainInputMatrix, double[][] TrainOutputMatrix, int Interactions) { //Тренировка нейронки
        for (var i = 0; i < Interactions; i++) { //Все наборы данных итерируем некоторое кол-во раз
            var Output = prediction(trainInputMatrix); //Вызываем метод prediction
            var Error = substractMatrix(TrainOutputMatrix, Output); //Вычисляем ошибку по формуле
            var CurSigmoidDerivative = сalculateSigmoidDerivative(Output); //Вызываем произоводную
            var ErrorSigmoidDerivative = productMatrix(Error, CurSigmoidDerivative); //Ошибка по производной
            var Adjustment = dotProductMatrix(transposeMatrix(trainInputMatrix), ErrorSigmoidDerivative); //Вычисляем значения корректирвоки весов

            sumMatrix(synapsesMatrix, Adjustment); //Обновляем матрицу синапсиса
        }
    }

    public static double[][] transposeMatrix(double[][] matrix) { //Транспонируем матрицу (меняем строки и столбцы местами)
        var rowsA = matrix.length; //Строчки
        var colsA = matrix[0].length; //Колонки

        double[][] result = new double[colsA][rowsA]; //Создаем новую матрицу для сохранения результата транспонированной матрицы

        for (var i = 0; i < rowsA; i++) { //Заполняем её
            for (var j = 0; j < colsA; j++) {
                result[j][i] = matrix[i][j];
            }
        }
        return result;
    }

    synchronized void sumMatrix(double[][] matrixa, double[][] matrixb) { //Считаем сумму первой и второй матрицы (Нужно для коректировки весов)
        var rowsA = matrixa.length;
        var colsA = matrixa[0].length;

        for (var i = 0; i < rowsA; i++) {
            for (var u = 0; u < colsA; u++) {
                matrixa[i][u] += matrixb[i][u];
            }
        }
    }

    double[][] substractMatrix(double[][] matrixa, double[][] matrixb) { //Отнимаем значение одной матрицы от другой (Для вычисления ошибки)
        var rowsA = matrixa.length;
        var colsA = matrixa[0].length;

        var result = new double[rowsA][colsA];

        for (var i = 0; i < rowsA; i++) {
            for (var u = 0; u < colsA; u++) {
                result[i][u] = matrixa[i][u] - matrixb[i][u];
            }
        }
        return result;
    }

    double[][] productMatrix(double[][] matrixa, double[][] matrixb) { //Умножаем две матрицы (Для вычисления ошибки производной)

        var rowsA = matrixa.length;
        var colsA = matrixa[0].length;

        var result = new double[rowsA][colsA];

        for (var i = 0; i < rowsA; i++) {
            for (var u = 0; u < colsA; u++) {
                result[i][u] = matrixa[i][u] * matrixb[i][u];
            }
        }
        return result;
    }

    double[][] dotProductMatrix(double[][] matrixa, double[][] matrixb) {

        var rowsA = matrixa.length;
        var colsA = matrixa[0].length;

        var rowsB = matrixb.length;
        var colsB = matrixb[0].length;

        var result = new double[rowsA][colsB];

        for (var i = 0; i < rowsA; i++) {
            for (var j = 0; j < colsB; j++) {
                for (var k = 0; k < rowsB; k++)
                    result[i][j] += matrixa[i][k] * matrixb[k][j];
            }
        }
        return result;
    }
}