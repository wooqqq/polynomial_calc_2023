package com.ll;

public class Calc {
    public static int run(String exp) {
        exp = exp.replaceAll("- ", "+ -");


        String[] bits = exp.split(" \\+ ");


//        int a = Integer.parseInt(bits[0]);
//        int b = Integer.parseInt(bits[1]);
//        int c = 0;
//        int d = 0;

//        if (bits.length > 2) {
//            c = Integer.parseInt(bits[2]);
//        }
//        if (bits.length > 3) {
//            d = Integer.parseInt(bits[3]);
//        }
        int sum = 0;

        for (int i = 0; i < bits.length; i++) {
            sum += Integer.parseInt(bits[i]);
        }

        return sum;

//        throw new RuntimeException("올바른 계산식이 아닙니다.");
    }
}
