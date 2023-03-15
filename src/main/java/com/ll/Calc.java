package com.ll;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Calc {
    public static int run(String exp) {
        exp = exp.trim();
        exp = stripOuterBrackets(exp);

        // 단일항이 입력되면 바로 리턴
        if ( !exp.contains(" ") ) return Integer.parseInt(exp);

        boolean needToMulti = exp.contains(" * ");
        boolean needToPlus = exp.contains(" + ") || exp.contains(" - ");
        boolean needToCompound = needToMulti && needToPlus;
        boolean needToSplit =  exp.contains("(") || exp.contains(")");

        if ( needToSplit ) {
            int splitPointIndex = findSplitPointIndex(exp);

            String firstExp = exp.substring(0, splitPointIndex);
            String secondExp = exp.substring(splitPointIndex + 1);

            // 괄호 후 덧셈만 있을 때
            // return Calc.run(firstExp) + Calc.run(secondExp);

            /* 괄호 후 곱셈 있을 때 나의 답
            if (exp.contains("*")) {
                return Calc.run(firstExp) * Calc.run(secondExp);
            } else {
                return Calc.run(firstExp) + Calc.run(secondExp);
            }
            */

            // 강사님 답 : if ~ else 문 안쓰고 덧셈곱셈 다 가능
            char operationCode = exp.charAt(splitPointIndex);

            exp = Calc.run(firstExp) + " " + operationCode + " " + Calc.run(secondExp);

            return Calc.run(exp);
        }
        else if ( needToCompound ) {
            String[] bits = exp.split(" \\+ ");

            String newExp = Arrays.stream(bits)
                    .mapToInt(Calc::run)
                    .mapToObj(e -> e + "")
                    .collect(Collectors.joining(" + "));

            return run(newExp);
        }
        else if ( needToPlus ) {
            exp = exp.replaceAll("- ", "+ -");

            String[] bits = exp.split(" \\+ ");

            int sum = 0;

            for (int i = 0; i < bits.length; i++) {
                sum += Integer.parseInt(bits[i]);
            }

            return sum;
        }
        else if ( needToMulti ) {
            String[] bits = exp.split(" \\* ");

            int sum = 1;

            for (int i = 0; i < bits.length; i++) {
                sum *= Integer.parseInt(bits[i]);
            }

            return sum;
        }

        throw new RuntimeException("올바른 계산식이 아닙니다.");
    }

    private static int findSplitPointIndexBy(String exp, char findChar) {
        int bracketsCount = 0;
        
        for (int i = 0; i < exp.length(); i++) {
            char c = exp.charAt(i);
            
            if (c == '(') {
                bracketsCount++;
            } else if ( c == ')') {
                bracketsCount--;
            } else if ( c == findChar ) {
                if (bracketsCount == 0) return i;
            }
        }

        return -1;
    }

    private static int findSplitPointIndex(String exp) {
        int index = findSplitPointIndexBy( exp, '+' );

        if (index >= 0) return index;

        return findSplitPointIndexBy(exp, '*');
    }

    private static String stripOuterBrackets(String exp) {
        int outerBracketsCount = 0;

        while ( exp.charAt(outerBracketsCount) == '(' && exp.charAt(exp.length() - 1 - outerBracketsCount) == ')' ) {
            outerBracketsCount++;
        }

        if ( outerBracketsCount == 0 ) return exp;

        return exp.substring(outerBracketsCount, exp.length() - outerBracketsCount);
    }
}