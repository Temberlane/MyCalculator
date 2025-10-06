package com.example.mycalculator;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Small expression evaluator used by "=".
 * Supports +, -, ×, ÷, decimals, and operator precedence.
 * Returns "Error" for malformed input or divide-by-zero.
 * - quick validation
 * - divide-by-zero/NaN/Infinity guard
 * - cleaner decimal formatting
 */
public final class MathEval {
    private MathEval() { }

    public static String eval(String expr) {
        try {
            // Accept nulls
            if (expr == null) {
                expr = "";
            }

            // Normalize friendly UI symbols to programming symbols
            String normalized = expr.replace('×','*')
                                    .replace('÷','/')
                                    .replace('−','-')
                                    .trim();
            if (normalized.isEmpty()) {
                return "0";
            }

            // Simple validity guard (ends with operator -> malformed)
            if (endsWithOperator(normalized)) {
                return "Error";
            }

            // Guard against trailing decimal point (e.g. "9.")
            if (normalized.endsWith(".")) { 
                return "Error";
            }

            // Parse and evaluate
            Expression e = new ExpressionBuilder(normalized).build();
            double result = e.evaluate();

            // Guard against NaN/Infinity (e.g. divide by zero)
            if (Double.isNaN(result) || Double.isInfinite(result)) {
                return "Error";
            }

            // If whole number, show as integer (e.g. 9.0 -> 9)
            if (Math.rint(result) == result) {
                return String.valueOf((long) result); 
            }

            // Otherwise, format as decimal (strip trailing zeros)
            return format(result);

        } catch (ArithmeticException div0) {
            // divide-by-zero or similar
            return "Error";
        } catch (Exception ex) {
            // any parse/eval error
            return "Error";
        }
    }

    // ---- helpers ----

    private static boolean endsWithOperator(String s) {
        if (s.isEmpty()) return false;
        char c = s.charAt(s.length() - 1);
        return c == '+' || c == '-' || c == '*' || c == '/';
    }

    private static String format(double value) {
        // BigDecimal for stable text; strip trailing zeros
        BigDecimal bd = new BigDecimal(Double.toString(value))
                .setScale(10, RoundingMode.HALF_UP)
                .stripTrailingZeros();
        String s = bd.toPlainString();
        if (s.endsWith(".0")) {
            s = s.substring(0, s.length() - 2);
        }
        return s;
    }
}
