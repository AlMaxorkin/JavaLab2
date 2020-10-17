import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Класс для тестов
 *
 * @author Alexandr Makhorkin
 * @see Expression
 */
class ExpressionTest {

    /**
     * Тест hasPrecedence
     */
    @Test
    void hasPrecedence() {
        Expression expression = new Expression();
        assertTrue(expression.hasPrecedence('+', '/'));
    }

    /**
     * Тест evaluate
     */
    @Test
    void isCorrect() {
        Expression expr1 = new Expression("2 + 2.5");
        Expression expr2 = new Expression("((2 + a)/(b - 3.5)) + (2 -b) * 8 ");
        Expression expr3 = new Expression("(2 + a + (b + c)) / 2");

        Expression expr4 = new Expression("((a + 2)");
        Expression expr5 = new Expression("a-b % 2");
        Expression expr6 = new Expression("(a-/b)+a");


        assertTrue(expr1.isCorrect());
        assertTrue(expr2.isCorrect());
        assertTrue(expr3.isCorrect());

        assertFalse(expr4.isCorrect());
        assertFalse(expr5.isCorrect());
        assertFalse(expr6.isCorrect());
    }

    /**
     * Тест evaluate
     */
    @Test
    void evaluate() {
        Expression expr1 = new Expression("2 + 2 * 2");
        Expression expr2 = new Expression("3 / (0.5 + 2 -1)");
        Expression expr3 = new Expression("2 - 5 + 14 / 2.0");

        try {
            assertEquals(6, expr1.evaluate());
            assertEquals(2, expr2.evaluate());
            assertEquals(4, expr3.evaluate());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}