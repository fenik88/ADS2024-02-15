package by.it.group351002.bob.lesson06;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Задача на программирование: наибольшая возрастающая подпоследовательность
см.     https://ru.wikipedia.org/wiki/Задача_поиска_наибольшей_увеличивающейся_подпоследовательности
        https://en.wikipedia.org/wiki/Longest_increasing_subsequence

Дано:
    целое число 1≤n≤1000
    массив A[1…n] натуральных чисел, не превосходящих 2E9.

Необходимо:
    Выведите максимальное 1<=k<=n, для которого гарантированно найдётся
    подпоследовательность индексов i[1]<i[2]<…<i[k] <= длины k,
    где каждый элемент A[i[k]] больше любого предыдущего
    т.е. для всех 1<=j<k, A[i[j]]<A[i[j+1]].

Решить задачу МЕТОДАМИ ДИНАМИЧЕСКОГО ПРОГРАММИРОВАНИЯ

    Sample Input:
    5
    1 3 3 2 6

    Sample Output:
    3
*/

public class A_LIS {


    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = A_LIS.class.getResourceAsStream("dataA.txt");
        A_LIS instance = new A_LIS();
        int result = instance.getSeqSize(stream);
        System.out.print(result);
    }

    int getSeqSize(InputStream stream) throws FileNotFoundException {
        //подготовка к чтению данных
        Scanner scanner = new Scanner(stream);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
/* Подготовка к чтению данных:
Изначально считывается длина последовательности n и сама последовательность m.

Инициализация массива динамического программирования:
Создается массив d, где d[i] представляет собой длину наибольшей возрастающей подпоследовательности,
заканчивающейся на элементе m[i].
Изначально все элементы массива d инициализируются значением 1,
так как наихудшим случаем будет подпоследовательность, состоящая из одного элемента.

Решение задачи:
Для каждого элемента m[i] вычисляется длина наибольшей возрастающей подпоследовательности,
заканчивающейся на этом элементе.
Это делается путем перебора всех элементов, предшествующих m[i],
и проверки, можно ли добавить текущий элемент к подпоследовательности,
заканчивающейся на m[i].
Если элемент m[j] (где j < i) меньше m[i] и длина подпоследовательности до m[j] плюс один больше,
чем длина подпоследовательности до m[i], то обновляется значение d[i].

Нахождение максимальной длины:
Во время вычислений ведется отслеживание максимальной длины найденной подпоследовательности,
которая и будет результатом задачи.

Возврат результата:
После завершения вычислений возвращается найденная максимальная длина
наибольшей возрастающей подпоследовательности.*/

        //общая длина последовательности
        int n = scanner.nextInt();
        int[] m = new int[n];
        int[] d = new int[n];
        //читаем всю последовательность
        for (int i = 0; i < n; i++) {
            m[i] = scanner.nextInt();
            d[i] = 1;
        }
        int result = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < i; j++) {
                if ((m[j]<m[i])&&(d[j]+1>d[i])){
                    d[i] = d[j] + 1;
                    if (d[i]>result){
                        result = d[i];
                    }
                }

            }
        }




        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }
}
