package com.ll;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Calc {
    public static boolean debug = true;
    public static int runCallCount = 0;

    public static int run(String exp) {
        runCallCount++;

        exp = exp.trim();
        exp = stripOuterBrackets(exp);

        // 만약에 음수괄호 패턴이면, 우리 해석기가 인식을 못하니, 해석할 수 있는 형태로 변경하자.
        if ( isCaseMinusBracket(exp) ) {
            exp = exp.substring(1) + " * -1";
        }

        if (debug) {
            System.out.printf("exp(%d) : %s\n", runCallCount, exp);
        }


        // 단일항이 입력되면 바로 리턴
        if (!exp.contains(" ")) return Integer.parseInt(exp);

        boolean needToMulti = exp.contains(" * ");
        boolean needToPlus = exp.contains(" + ") || exp.contains(" - ");
        boolean needToCompound = needToMulti && needToPlus;
        boolean needToSplit = exp.contains("(") || exp.contains(")");

        if (needToSplit) {
            int splitPointIndex = findSplitPointIndex(exp);

            String firstExp = exp.substring(0, splitPointIndex);
            String secondExp = exp.substring(splitPointIndex + 1);

            char operationCode = exp.charAt(splitPointIndex);

            exp = Calc.run(firstExp) + " " + operationCode + " " + Calc.run(secondExp);

            return Calc.run(exp);
        } else if (needToCompound) {
            String[] bits = exp.split(" \\+ ");

            String newExp = Arrays.stream(bits)
                    .mapToInt(Calc::run)
                    .mapToObj(e -> e + "")
                    .collect(Collectors.joining(" + "));

            return run(newExp);
        } else if (needToPlus) {
            exp = exp.replaceAll("- ", "+ -");

            String[] bits = exp.split(" \\+ ");

            int sum = 0;

            for (int i = 0; i < bits.length; i++) {
                sum += Integer.parseInt(bits[i]);
            }

            return sum;
        } else if (needToMulti) {
            String[] bits = exp.split(" \\* ");

            int sum = 1;

            for (int i = 0; i < bits.length; i++) {
                sum *= Integer.parseInt(bits[i]);
            }

            return sum;
        }

        throw new RuntimeException("올바른 계산식이 아닙니다.");
    }

    private static boolean isCaseMinusBracket(String exp) {
        // - 로 시작하는지
        if ( exp.startsWith("-(") == false ) return false;

        // 괄호로 감싸여져 있는지
        int bracketsCount = 0;

        for (int i = 0; i < exp.length(); i++) {
            char c = exp.charAt(i);

            if (c == '(') {
                bracketsCount++;
            } else if (c == ')') {
                bracketsCount--;
            }

            if (bracketsCount == 0) {
                if ( exp.length() - 1 == i ) return true;
            }
        }

        return false;
    }

    private static int findSplitPointIndexBy(String exp, char findChar) {
        int bracketsCount = 0;

        for (int i = 0; i < exp.length(); i++) {
            char c = exp.charAt(i);

            if (c == '(') {
                bracketsCount++;
            } else if (c == ')') {
                bracketsCount--;
            } else if (c == findChar) {
                if (bracketsCount == 0) return i;
            }
        }

        return -1;
    }

    private static int findSplitPointIndex(String exp) {
        int index = findSplitPointIndexBy(exp, '+');

        if (index >= 0) return index;

        return findSplitPointIndexBy(exp, '*');
    }

    private static String stripOuterBrackets(String exp) {
        int outerBracketsCount = 0;

        while (exp.charAt(outerBracketsCount) == '(' && exp.charAt(exp.length() - 1 - outerBracketsCount) == ')') {
            outerBracketsCount++;
        }

        if (outerBracketsCount == 0) return exp;

        return exp.substring(outerBracketsCount, exp.length() - outerBracketsCount);
    }
}