package org.tcathebluecreper.mineutils;

import org.tcathebluecreper.colorpicker.ColorPicker;
import org.tcathebluecreper.library.InputManager;
import org.tcathebluecreper.mclangtool.mcLangTool;

import java.io.IOException;
import java.util.Arrays;

public class Main {
    static InputManager IM;
    public static final String VersionID = "v1.0.0";
    public static void main(String[] args) {
        init(args);
        initMsg();

        while(true) {
            String input = IM.requestInput(getControls(), getPrograms(), InputManager.inputMode.anyCap);
            switch(input) {
                case "mclangtool" -> {
                    try {
                        mcLangTool.start(IM);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                case "color picker" -> {
                    ColorPicker cp = new ColorPicker();
                    ColorPicker.main(new String[0]);
                }
            }
        }
    }
    static void initMsg() {
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n" + "   Welcome to Tcat's MineUtils " + VersionID + "\n" + "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ \n");
    }
    static void init(String[] args) {
        System.out.println("init...");
        IM = new InputManager(Arrays.stream(args).toList());
    }
    static String[] getPrograms() {
        return new String[] {mcLangTool.name, ColorPicker.name};
    }
    static String getControls() {
        String output = "";
        String[] programs = new String[] {mcLangTool.info, ColorPicker.info};
        for(String s : programs) {
            output += s + "\n";
        }
        return output;
    }
}
