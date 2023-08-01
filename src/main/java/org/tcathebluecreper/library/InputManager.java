package org.tcathebluecreper.library;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class InputManager {
    Scanner scanner;
    List<String> runQue;
    List<String> runLog;

    public InputManager(List<String> RunQue) {
        scanner = new Scanner(System.in);
        this.runQue = RunQue;
        runLog = new ArrayList<>();
    }

    public String requestInput(String text, String[] values, inputMode mode) {
        if (!text.isEmpty()) System.out.println(text);
        while (true) {
            String s;
            if (runQue.size() != 0) {
                s = runQue.get(0).replaceAll("--", " ");
                runQue.remove(0);
                System.out.println("\u001B[32m" + s + "\u001B[0m");
            } else s = scanner.nextLine();
            runLog.add(text.replaceAll(":", "=") + ":" + s);
            for (int i = values.length - 1; i >= 0; i--) {
                switch (mode) {
                    case anyCap -> {
                        if (s.toLowerCase().matches(values[i].toLowerCase())) return s;
                    }
                    case anyCapInverted -> {
                        if (!s.toLowerCase().matches(values[i].toLowerCase())) return s;
                    }
                    case strict -> {
                        if (s.matches(values[i])) return s;
                    }
                    case strictInverted -> {
                        if (!s.matches(values[i])) return s;
                    }
                    case contains -> {
                        if (s.contains(values[i])) return s;
                    }
                    case containsInverted -> {
                        if (!s.contains(values[i])) return s;
                    }
                    case containsAnyCap -> {
                        if (s.toLowerCase().contains(values[i].toLowerCase())) return s;
                    }
                    case containsAnyCapInverted -> {
                        if (!s.toLowerCase().contains(values[i].toLowerCase())) return s;
                    }
                }
            }
            if (values.length == 0) return s;
            System.out.println("Invalid input.");
        }
    }

    public String requestInput(String text, ExecuteCondition condition) {
        if (!text.isEmpty()) System.out.println(text);
        while (true) {
            String s;
            if (runQue.size() != 0) {
                s = runQue.get(0).replaceAll("--", " ");
                runQue.remove(0);
                System.out.println("\u001B[32m" + s + "\u001B[0m");
            } else s = scanner.nextLine();
            runLog.add(text.replaceAll(":", "=") + ":" + s);
            if (condition.check(s)) return s;

            System.out.println("Invalid input.");
        }
    }

    public enum inputMode {
        strict,
        strictInverted,
        anyCap,
        anyCapInverted,
        contains,
        containsInverted,
        containsAnyCap,
        containsAnyCapInverted,
    }

    public static abstract class ExecuteCondition implements Runnable {
        @Override
        public void run() {

        }

        public abstract boolean check(String arg);

        public static ExecuteCondition checkFilePath() {
            return new ExecuteCondition() {
                @Override
                public boolean check(String s) {
                    File file = new File(s);
                    if(file.exists() && !file.isDirectory()) return true;
                    else return false;

                }
            };
        }
        public static ExecuteCondition checkDirectoryPath() {
            return new ExecuteCondition() {
                @Override
                public boolean check(String s) {
                    File file = new File(s);
                    if(file.exists() && file.isDirectory()) return true;
                    else return false;
                }
            };
        }
    }
}
