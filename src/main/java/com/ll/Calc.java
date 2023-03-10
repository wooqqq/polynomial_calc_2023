package com.ll;

public class Calc {
    public static int run(String exp) {
        boolean needToMulti = exp.contains("*");
        boolean needToPlus = !needToMulti;

        if (needToPlus) {
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
}
