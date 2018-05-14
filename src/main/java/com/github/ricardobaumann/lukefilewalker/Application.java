package com.github.ricardobaumann.lukefilewalker;

import java.io.IOException;

public class Application {

    public static void main(String[] args) throws IOException {
        String result = FileWalker.sort(args[0]);
        System.out.println(result);
    }

}
