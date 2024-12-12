package by.it.group351002.bob.lesson12;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

// Реализация Map на основе АВЛ-дерева. АВЛ-дерево - это сбалансированное двоичное дерево поиска,
// в котором разница высот между левым и правым поддеревьями любой вершины не превышает 1.
// В данной реализации поддерживаются операции добавления, удаления, поиска (в том числе минимального и максимального элемента),
// а также балансировка дерева.
public class MyAvlMap implements Map<Integer, String> {

    private int size = 0; // Хранит количество элементов в дереве.
    private MyNode root; // Корень дерева.

    // Узел дерева, хранящий ключ, значение, высоту (для балансировки) и ссылки на дочерние узлы.
    static private class MyNode {
        Integer key; // Ключ (целое число).
        String value; // Значение (строка).
        int height; // Высота узла для балансировки.
        MyNode left, right; // Ссылки на левые и правые дочерние узлы.

        MyNode(Integer key, String value) {
            this.key = key;
            this.value = value;
            this.height = 1; // Изначально высота равна 1, т.к. это лист.
            this.right = null;
            this.left = null;
        }
    }

    // Возвращает высоту узла или 0, если узел равен null.
    private int height(MyNode node) {
        return (node != null) ? node.height : 0;
    }

    // Вычисляет балансировочный фактор для узла (разница высот правого и левого поддеревьев).
    private int bfactor(MyNode node) {
        return height(node.right) - height(node.left);
    }

    // Пересчитывает высоту узла на основе высот его дочерних узлов.
    private void fixheight(MyNode node) {
        int heightright = height(node.right);
        int heightleft = height(node.left);
        node.height = Math.max(heightright, heightleft) + 1;
    }

    // Добавляет содержимое дерева в строку в формате ключ=значение, по порядку ключей.
    private void addtostring(MyNode parent, StringBuilder str) {
        if (parent.left != null)
            addtostring(parent.left, str); // Рекурсивно идем в левое поддерево.
        str.append(parent.key);
        str.append("=");
        str.append(parent.value);
        str.append(", ");
        if (parent.right != null)
            addtostring(parent.right, str); // Рекурсивно идем в правое поддерево.
    }

    // Возвращает строковое представление дерева в формате {ключ=значение, ключ=значение, ...}.
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        if (root != null) {
            addtostring(root, sb);
            sb.delete(sb.length() - 2, sb.length()); // Удаляем лишнюю запятую и пробел.
        }
        sb.append("}");
        return sb.toString();
    }

    // Выполняет правый поворот вокруг заданного узла для балансировки дерева.
    private MyNode rotateright(MyNode node) {
        MyNode nextnode = node.left;
        node.left = nextnode.right;
        nextnode.right = node;
        fixheight(node); // Обновляем высоты узлов после поворота.
        fixheight(nextnode);
        return nextnode;
    }

    // Выполняет левый поворот вокруг заданного узла для балансировки дерева.
    private MyNode rotateleft(MyNode node) {
        MyNode nextnode = node.right;
        node.right = nextnode.left;
        nextnode.left = node;
        fixheight(node); // Обновляем высоты узлов после поворота.
        fixheight(nextnode);
        return nextnode;
    }

    // Балансирует узел, выполняя повороты при необходимости.
    private MyNode balance(MyNode node) {
        fixheight(node); // Сначала обновляем высоту узла.
        int h = bfactor(node);
        if (h == 2) { // Правое поддерево слишком высокое.
            if (bfactor(node.right) < 0)
                node.right = rotateright(node.right); // Двойной поворот (правый, затем левый).
            return rotateleft(node);
        }
        if (h == -2) { // Левое поддерево слишком высокое.
            if (bfactor(node.left) > 0)
                node.left = rotateleft(node.left); // Двойной поворот (левый, затем правый).
            return rotateright(node);
        }
        return node; // Узел уже сбалансирован.
    }

    // Рекурсивно вставляет пару ключ-значение в дерево.
    private MyNode insert(MyNode node, Integer key, String value, StringBuilder oldvalue) {
        if (node == null) { // Если узел отсутствует, создаем новый.
            size++;
            return new MyNode(key, value);
        }
        if (key < node.key)
            node.left = insert(node.left, key, value, oldvalue); // Идем в левое поддерево.
        else if (key > node.key)
            node.right = insert(node.right, key, value, oldvalue); // Идем в правое поддерево.
        else { // Ключ уже существует.
            oldvalue.append(node.value); // Сохраняем старое значение.
            node.value = value; // Обновляем значение.
            return node;
        }
        return balance(node); // Балансируем узел после вставки.
    }

    // Добавляет пару ключ-значение в дерево. Возвращает старое значение, если ключ уже существовал.
    @Override
    public String put(Integer key, String value) {
        StringBuilder oldvalue = new StringBuilder();
        root = insert(root, key, value, oldvalue);
        return oldvalue.isEmpty() ? null : oldvalue.toString();
    }

    // Ищет минимальный узел в поддереве.
    private MyNode findmin(MyNode node) {
        if (node.left == null)
            return node; // Минимальный узел найден (нет левого потомка).
        return findmin(node.left);
    }

    // Удаляет узел с заданным ключом из дерева.
    private MyNode delete(MyNode node, Integer key, StringBuilder oldvalue) {
        if (node.key.equals(key)) { // Узел найден.
            size--;
            if (oldvalue != null)
                oldvalue.append(node.value); // Сохраняем удаляемое значение.
            if (node.left == null && node.right == null)
                return null; // Узел является листом.
            if (node.left == null)
                return node.right; // Нет левого поддерева.
            if (node.right == null)
                return node.left; // Нет правого поддерева.
            size++; // Для корректировки после замены.
            MyNode minnode = findmin(node.right); // Ищем минимальный узел в правом поддереве.
            node.key = minnode.key;
            node.value = minnode.value; // Заменяем текущий узел минимальным.
            node.right = delete(node.right, minnode.key, null); // Удаляем минимальный узел из правого поддерева.
            return node;
        }
        if (key < node.key) {
            if (node.left != null)
                node.left = delete(node.left, key, oldvalue); // Идем в левое поддерево.
        } else {
            if (node.right != null)
                node.right = delete(node.right, key, oldvalue); // Идем в правое поддерево.
        }
        return balance(node); // Балансируем узел после удаления.
    }

    // Удаляет элемент с заданным ключом. Возвращает удаленное значение, если ключ существовал.
    @Override
    public String remove(Object key) {
        int oldsize = size;
        StringBuilder oldvalue = new StringBuilder();
        root = delete(root, (Integer) key, oldvalue);
        return oldsize == size ? null : oldvalue.toString();
    }

    // Находит и возвращает значение по ключу. Если ключ не найден, возвращает null.
    @Override
    public String get(Object key) {
        MyNode x = root;
        while (x != null) {
            if (x.key.equals(key))
                return x.value;
            if ((Integer) key < x.key)
                x = x.left;
            else
                x = x.right;
        }
        return null;
    }

    // Проверяет, существует ли заданный ключ в дереве.
    @Override
    public boolean containsKey(Object key) {
        MyNode x = root;
        while (x != null) {
            if (x.key.equals(key))
                return true;
            if ((Integer) key < x.key)
                x = x.left;
            else
                x = x.right;
        }
        return false;
    }

    // Возвращает количество элементов в дереве.
    @Override
    public int size() {
        return size;
    }

    // Удаляет все узлы из дерева, освобождая память.
    private MyNode eraseNode(MyNode node) {
        if (node != null) {
            node.right = eraseNode(node.right);
            node.left = eraseNode(node.left);
            node.key = null;
            node.value = null;
        }
        return null;
    }

    // Полностью очищает дерево.
    @Override
    public void clear() {
        size = 0;
        root = eraseNode(root);
    }

    // Проверяет, пусто ли дерево.
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    // Методы ниже не реализованы в данной версии.
    @Override
    public boolean containsValue(Object value) {
        return false;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
    }

    @Override
    public Set<Integer> keySet() {
        return null;
    }

    @Override
    public Collection<String> values() {
        return null;
    }

    @Override
    public Set<Entry<Integer, String>> entrySet() {
        return null;
    }
}
