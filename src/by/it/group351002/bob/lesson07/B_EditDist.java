package by.it.group351002.bob.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Задача на программирование: расстояние Левенштейна
    https://ru.wikipedia.org/wiki/Расстояние_Левенштейна
    http://planetcalc.ru/1721/

Дано:
    Две данных непустые строки длины не более 100, содержащие строчные буквы латинского алфавита.

Необходимо:
    Решить задачу МЕТОДАМИ ДИНАМИЧЕСКОГО ПРОГРАММИРОВАНИЯ
    Итерационно вычислить расстояние редактирования двух данных непустых строк

    Sample Input 1:
    ab
    ab
    Sample Output 1:
    0

    Sample Input 2:
    short
    ports
    Sample Output 2:
    3

    Sample Input 3:
    distance
    editing
    Sample Output 3:
    5

*/

public class B_EditDist {

    /*Инициализация таблицы:
    Создается двумерный массив dp, где dp[i][j] будет хранить расстояние Левенштейна
    между подстроками one.substring(0, i) и two.substring(0, j).

Базовые случаи:
Первая строка (или столбец) массива dp инициализируется значениями от 0 до len1 (или len2),
так как расстояние Левенштейна между пустой строкой и любой другой строкой равно длине последней.

Заполнение таблицы:
Для каждой пары символов one.charAt(i - 1) и two.charAt(j - 1) проверяется,
совпадают ли они. Если да, то значение dp[i][j] берется из dp[i - 1][j - 1],
так как никаких операций не требуется.
В противном случае, мы выбираем минимальное значение из трех возможных случаев:

dp[i - 1][j - 1] + 1 (замена символа),
dp[i][j - 1] + 1 (вставка символа),
dp[i - 1][j] + 1 (удаление символа).
Возврат результата:
В конце работы алгоритма значение dp[len1][len2] содержит минимальное количество операций,
необходимых для превращения строки one в строку two. Это и будет результатом работы программы.*/

    int getDistanceEdinting(String one, String two) {
        int len1 = one.length();
        int len2 = two.length();

        // Создаем таблицу для хранения результатов
        int[][] dp = new int[len1 + 1][len2 + 1];

        // Инициализируем первую строку и первый столбец
        for (int i = 0; i <= len1; i++) {
            dp[i][0] = i;
        }
        for (int j = 0; j <= len2; j++) {
            dp[0][j] = j;
        }

        // Заполняем остальные ячейки таблицы
        for (int i = 1; i <= len1; i++) {
            for (int j = 1; j <= len2; j++) {
                if (one.charAt(i - 1) == two.charAt(j - 1)) {
                    // Символы совпадают, нет необходимости в операции редактирования
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    // Символы не совпадают, выбираем минимальную операцию из трех возможных
                    dp[i][j] = 1 + Math.min(dp[i - 1][j - 1], Math.min(dp[i][j - 1], dp[i - 1][j]));
                }
            }
        }

        // Возвращаем значение в нижней правой ячейке таблицы
        return dp[len1][len2];
    }

    public static void main(String[] args) throws FileNotFoundException {
        B_EditDist instance = new B_EditDist();

        // Чтение данных из файла
        InputStream stream = B_EditDist.class.getResourceAsStream("dataABC.txt");
        Scanner scanner = new Scanner(stream);

        // Чтение строк и вычисление расстояния редактирования
        while (scanner.hasNextLine()) {
            String one = scanner.nextLine();
            String two = scanner.nextLine();
            int distance = instance.getDistanceEdinting(one, two);
            System.out.println(distance);
        }

        scanner.close();
    }
}