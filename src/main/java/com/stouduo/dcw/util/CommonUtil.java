package com.stouduo.dcw.util;

import java.util.Random;

/**
 * Created by ChenRui on 2017/10/14.
 */
public class CommonUtil {
    static Random random = new Random();

    public static int getSMSCode() {
        return random.nextInt(900000) + 100000;
    }

    public static void main(String[] args) {
        System.out.println(getSMSCode());
    }
}
