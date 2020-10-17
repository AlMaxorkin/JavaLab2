import java.util.*;
import java.lang.Character;
import java.lang.String;


/**
 *Класс выражений с методами их проверки и вычисления
 *
 * @author Alexandr Makhorkin
 * @version 1.0
 * @see Expression
 */

public class Expression {
    /** Поле выражение*/
    String expression;

    /**
     * Конструктор по умолчанию
     */
    public Expression(){
        this.expression = "";
    }

    /** Конструктор
     * @param expression - выражение
     * */
    public Expression(String expression) {
        this.expression = expression;
    }


    /**
     *Вспомашательная функции для проверки приоритета операций.
     * @param op1 текущая символ операции
     * @param op2 символ операции из стека
     * @return возвращат true, если первая операция менее приориетна и ни один из символов не является скобкой
     */
    public boolean hasPrecedence(char op1, char op2)
    {
        if (op2 == '(' || op2 == ')')
            return false;
        return (op1 != '*' && op1 != '/') || (op2 != '+' && op2 != '-');
    }

    /**
     *Проверяет является ли символ оператором
     * @param c символ
     * @return true, если символ является опреатором
     */
    private boolean isOperator(Character c){
        return (c == '+' || c == '-' || c == '*' || c == '/');
    }

    /**
     *Применяет операцию к двум числам
     * @param op символ операции
     * @param b 1 число
     * @param a 2 число
     * @return результат применения операции к 2 числам
     */
    private double applyOperation(char op, double b, double a)
    {
        switch (op)
        {
            case '+':
                return a + b;
            case '-':
                return a - b;
            case '*':
                return a * b;
            case '/':
                if (b == 0)
                    throw new ArithmeticException("В выражении присутсвует деление на 0.");
                return a / b;
        }
        return 0;
    }

    /**
     *Проверет выражение на правильное расположение скобок
     */
    private boolean hasCorrectBrackets(){
        Stack<Character> brackets = new Stack<>();

        if(expression.isEmpty())
            return false;

        for (int i = 0; i < expression.length(); i++) {
            char current = expression.charAt(i);

            if (current == '(') {
                brackets.push(current);
                continue;
            }

            if(current == ')')
                if (brackets.isEmpty())
                    return false;
                else
                    brackets.pop();

        }
        return (brackets.isEmpty());
    }

    /**
     * Проверяет выражение на корректность
     * @return false если:
     * 2 операции идут подряд, пропущена операция, неправиольно расставлены скобки,
     * содержится непонятный символ.
     * Основан на методе evaluate
     */
    public boolean isCorrect(){
        int valuesSize = 0;
        Stack<Character> operations = new Stack<>();

        //Проверяется расстановка скобок
        if(!this.hasCorrectBrackets())
            return false;

        //Цикл по всей строке
        for(int i = 0; i < expression.length(); i++) {

            //Каждый раз проверяется количество полученных значений.
            //Отрицательно значение получается в случае наличия лишних операций
            if(valuesSize < 0)
                return false;

            if (Character.isSpaceChar((expression.charAt(i))))
                continue;

            //Считываются числа и переменные и добавляется количество значений
            if (Character.isDigit(expression.charAt(i))) {

                while (i < expression.length() && (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.'))
                    i++;
                i--;
                valuesSize++;
            }

            else if (Character.isAlphabetic(expression.charAt(i))) {
                while (i < expression.length() && Character.isLetterOrDigit(expression.charAt(i)))
                    i++;
                i--;
                valuesSize++;

            }

            else if (expression.charAt(i) == '(')
                operations.push(expression.charAt(i));

            else if (expression.charAt(i) == ')') {
                while (operations.peek() != '(') {

                    //Если значений меньше 2, то не к чему применять операцию.
                    // Значит выражение некорректно
                    if(valuesSize < 2)
                        return false;
                    //После применения операции вместо 2-х получается одно значение
                    valuesSize--;
                    operations.pop();
                }
                //Извлекается открывающая скобка
                operations.pop();
            }

            else if (isOperator(expression.charAt(i))) {
                while (!operations.empty() && isOperator(expression.charAt(i)) && isOperator(operations.peek())){
                    if(valuesSize < 2)
                        return false;
                    valuesSize--;
                    operations.pop();
                }
                operations.push(expression.charAt(i));
            }

            //Неизвестный символ
            else
                return false;
        }

        while (!operations.empty()) {
            valuesSize--;
            operations.pop();
        }


        //В конце должен остаться один элемент
        return valuesSize == 1;

    }


    /**
     * Вычисление значения выражения с переменными, значения которых заправшиваются с клавиатуры
     * @return значение выражения
     * @throws Exception в случае если выражение некорректно
     */
    public double evaluate() throws Exception{
        Map<String, Double> variables = new HashMap<>();
        Stack<Double> values = new Stack<>();
        Stack<Character> operations = new Stack<>();

        if(!this.isCorrect())
            throw new Exception("Некорректное выражение.");

        //Цикл по строке
        for (int i = 0; i < expression.length(); i++) {
            //Пробелы пропускаются
            if (Character.isSpaceChar(expression.charAt(i)))
                continue;

            //Если встречается цифра то во вложенном цикле считывается число и кладется в стек значений
            if (Character.isDigit(expression.charAt(i))) {
                StringBuilder buff = new StringBuilder();

                while (i < expression.length() && (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.'))
                    buff.append(expression.charAt(i++));
                i--;
                values.push(Double.parseDouble(buff.toString()));
            }

            //Если встречается буква, то считывается переменная.
            else if (Character.isAlphabetic(expression.charAt(i))) {
                StringBuilder buff = new StringBuilder();
                while (i < expression.length() && Character.isLetterOrDigit(expression.charAt(i)))
                    buff.append(expression.charAt(i++));
                i--;

                String str = buff.toString();

                /*Если переменная уже встречалась в выражении и ее значение хранится в словаре(Map),
                то в стек значений кладется значение из словаря, иначе значение вводится с клавиатуры
                */
                if (variables.containsKey(str)) {
                    values.push(variables.get(str));
                } else {
                    System.out.println("Введите переменную " + buff + ": ");
                    Scanner in = new Scanner(System.in);
                    double temp = in.nextDouble();
                    values.push(temp);
                    variables.put(str, temp);

                }

            }

            //Открывающая скобка кладется в стек операций
            else if (expression.charAt(i) == '(')
                operations.push(expression.charAt(i));

            //Когда встречается закрывающая скобка просходит вычисление выражения внутри скобок
            else if (expression.charAt(i) == ')') {
                while (operations.peek() != '(')
                    values.push(applyOperation(operations.pop(), values.pop(), values.pop()));
                operations.pop();
            }

            //Если встречается оператор
            else if (isOperator(expression.charAt(i))) {

                //Пока стек не пуст и текущая операция лежащая сверху в стеке имеет приоритет над текущей
                //в стек значений кладется результат применения операции в стеке к 2 переменным
                while (!operations.empty() && hasPrecedence(expression.charAt(i), operations.peek()))
                    values.push(applyOperation(operations.pop(), values.pop(), values.pop()));


                operations.push(expression.charAt(i));
            }

        }

        //Применяются оставшиеся операции
        while (!operations.empty())
            values.push(applyOperation(operations.pop(), values.pop(), values.pop()));

        //Возврат значения оставшегося в стеке
        return values.pop();
    }

    /**
     * Записывет значение выражения в строку
     * @return строку со значением или сообщением об ошибке
     */
    public String showResult(){
        try {
            return Double.toString(this.evaluate());
        } catch (Exception e){
            return e.getMessage();
        }
    }

}


