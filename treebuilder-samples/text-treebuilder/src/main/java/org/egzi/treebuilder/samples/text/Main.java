package org.egzi.treebuilder.samples.text;

import org.egzi.treebuilder.*;
import org.egzi.treebuilder.visitors.AsyncWalker;
import org.egzi.treebuilder.visitors.Visitor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by Егор on 20.11.2016.
 */
public class Main {
    public static void main(String[] args) {
        String data = "1 A\n" +
                "2 3 B\n" +
                "3 1 C\n" +
                "4 2 D";

    }
}
