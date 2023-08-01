package org.tcathebluecreper.mclangtool;

import org.tcathebluecreper.library.InputManager;
import org.tcathebluecreper.library.InputManager.*;
import static org.tcathebluecreper.library.util.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.io.*;


public class Main {
    public static String mainPath = "C:\\Users\\user\\AppData\\Local\\MClangTOOL";
    public static ArrayList<String> runQue;
    public static ArrayList<String> runLog;
    static InputManager IM;
    public static void main(String[] args) throws IOException {
        runQue = new ArrayList<>(List.of(args));
        runLog = new ArrayList<>(List.of(args));
        Scanner scanner = new Scanner(System.in);
        IM = new InputManager(runQue);
        switch (IM.requestInput("create: create new lang project\nload: load old lang project", new String[]{"create", "load"}, inputMode.anyCap)) {
            case "load" ->
                loadProject(IM.requestInput(Arrays.toString(removeExtensions(listProjects())), removeExtensions(listProjects()), inputMode.anyCap), scanner);

            case "create" -> {
                String name = IM.requestInput("name", removeExtensions(listProjects()), inputMode.anyCapInverted);
                createProject(name, IM.requestInput("lang file path", new String[0], inputMode.anyCap), scanner);
            }
        }

    }
    static String[] listProjects() throws IOException {
        Path path = Paths.get(mainPath);
        if(!Files.exists(path)) Files.createDirectories(path);
        return new File(mainPath).list();
    }
    static void loadProject(String id, Scanner scanner) {
        System.out.println();
        ProjectData pd = getPD(id);
        Hashtable<String, Hashtable<String, String>> lang = getProjectLang(pd);

        System.out.println("loaded into project: " + id);

        while(true) {
            System.out.println();
            String input = IM.requestInput("add: add a new translation\nrename: rename an element\nsave: save lang file\nexit: exit project\nclose: exit application\nlist: list all translations\ninput log: show all the inputs this session\nproj edit: edit project settings", new String[]{"add", "rename", "save", "delete", "exit", "close","list", "input log", "proj edit"}, inputMode.anyCap);
            switch (input) {
                case "add" -> {
                    String namespace = IM.requestInput("namespace, i.e. block.contenttweaker", new String[0], inputMode.anyCap);
                    String[] ids = IM.requestInput("translation id, comma spread", new String[0], inputMode.anyCap).split(",");

                    for (String s : ids) {
                        String S = s.trim();
                        String _s = namespace + "." + S;
                        String _S = IM.requestInput("name for " + S, new String[0], inputMode.anyCap);
                        String type = namespace.substring(0, _s.indexOf('.'));
                        if (!lang.containsKey(type))
                            lang.put(type, new Hashtable<>());
                        lang.get(type).put(_s, _S);
                    }
                }
                case "rename" -> {
                    String type = IM.requestInput("category", Collections.list(lang.keys()).toArray(new String[0]), inputMode.contains);
                    String namespace = "";

                    if(type.indexOf(".") > 0) {
                        namespace = type.substring(type.indexOf("."));
                        type = type.substring(0,type.indexOf("."));
                    }
                    String toRename = IM.requestInput(type + " to rename", new String[0], inputMode.anyCap);
                    String[] toRenames = toRename.split(",");
                    ArrayList<String> renameTo = new ArrayList<>();

                    for (int i = 1 - toRenames.length; i <= 0; i++) {
                        toRenames[i] = toRenames[i].trim();
                        renameTo.add(IM.requestInput("what to rename " + toRenames[i] + " to", new String[0], inputMode.anyCap));
                    }

                    String[] values = lang.get(type).values().toArray(new String[0]);

                    String[] keys = Collections.list(lang.get(type).keys()).toArray(new String[0]);
                    for (int r = toRenames.length - 1; r >= 0; r--) {
                        for (int i = values.length - 1; i >= 0; i--) {
                            String v = keys[i];
                            String V = v.substring(v.lastIndexOf(".") + 1);
                            if(V.matches(toRenames[r]) && !v.contains(".") && !type.contains(".")) {
                                lang.get(type).put(v, renameTo.get(r));
                                break;
                            }
                            if(v.contains(toRenames[r]) && v.contains(".") && !type.contains(".")) {
                                lang.get(type).put(v, renameTo.get(r));
                                break;
                            }
                            if(v.contains(toRenames[r]) && v.contains(".") && type.contains(".")) {
                                lang.get(type.substring(0, type.indexOf("."))).put(v, renameTo.get(r));
                                break;
                            }
                        }
                    }
                }
                case "delete" -> {
                    String type = IM.requestInput("category", Collections.list(lang.keys()).toArray(new String[0]), inputMode.contains);
                    String toDelete;
                    String nameSpace = "";
                    if(type.contains(".")) {
                        nameSpace = type.substring(type.indexOf("."));
                        type = type.substring(0, type.indexOf("."));
                    }
                    if(type.contains(".")) toDelete = IM.requestInput(type + " to delete", new String[0], inputMode.anyCap);
                    else toDelete = IM.requestInput(type, new String[0], inputMode.anyCap);
                    String[] toDeletes = toDelete.split(",");
                    String[] keys = Collections.list(lang.get(type).keys()).toArray(new String[0]);
                    for(int r = toDeletes.length - 1; r >= 0; r--) {
                        toDeletes[r] = toDeletes[r].trim();
                        boolean worked = false;
                        for(int i = keys.length - 1; i >= 0; i--) {
                            String k = keys[i];
                            String ks = k.substring(k.lastIndexOf(".") + 1);
                            if(!nameSpace.isEmpty()) if(ks.matches(nameSpace)) {
                                lang.get(type).remove(k);
                                worked = true;
                                break;
                            }
                            if(nameSpace.isEmpty()) if(ks.matches(toDeletes[r])) {
                                lang.get(type).remove(k);
                                worked = true;
                                break;
                            }
                        }
                        if(!worked) System.out.println(type + " of name \"" + toDeletes[r] + "\" was not found!\n");
                    }
                }
                case "list" -> {
                    Enumeration<String> keys = lang.keys();
                    while (keys.hasMoreElements()) {
                        String key = keys.nextElement();

                        Enumeration<String> Keys = lang.get(key).keys();
                        while (Keys.hasMoreElements()) {
                            String Key = Keys.nextElement();
                            System.out.println(Key + " :" + lang.get(key).get(Key));
                        }
                    }
                    System.out.println("");
                }
                case "exit" -> {
                    try {
                        new Main().main(new String[0]);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                case "close" -> {
                    return;
                }
                case "save" -> {
                    Enumeration<String> keys = lang.keys();
                    String save = "{\n";
                    while (keys.hasMoreElements()) {
                        String key = keys.nextElement();

                        Enumeration<String> Keys = lang.get(key).keys();
                        while (Keys.hasMoreElements()) {
                            String Key = Keys.nextElement();
                            //System.out.println(Key + " :" + lang.get(key).get(Key));
                            save += "\t\"" + Key + "\":\"" + lang.get(key).get(Key) + '"';
                            if(Keys.hasMoreElements()) save += ",\n";
                            System.out.println(Key + " :" + lang.get(key).get(Key));
                        }
                    }
                    save +=  "\n}";
                    File file = new File(pd.path);
                    try {
                        Formatter f = new Formatter(file);
                        Scanner s = new Scanner(file);
                        while(s.hasNext()) {
                            f.format("");
                        }
                        FileWriter writer = new FileWriter(file);
                        writer.write(save);
                        writer.close();
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                case "input log" -> {
                    String s = "";
                    int c = runLog.size();
                    for(int i = 0; i < c; i++) {
                        s += runLog.get(i);
                        if(i + 1 < c) s += ", ";
                    }
                    System.out.println(s);
                    switch(IM.requestInput("show unmapped for args: y/n", new String[]{"y","yes","n","no"}, inputMode.anyCap).toLowerCase()) {
                        case "yes", "y" -> {
                            s = "";
                            c = runLog.size();

                            for(int i = 0; i < c - c / 2 - 2; i++) {
                                String l = runLog.get(i).substring(runLog.get(i).indexOf(":") + 1).replaceAll(" ", "--");
                                s += l;
                                if(i + 1 < c) s += " ";
                            }
                            System.out.println(s);
                        }
                    }
                }
                case "proj edit" -> {
                    String inputArg = IM.requestInput("name: change project name\npath: change project output file path", new String[]{"name","path"},inputMode.anyCap);
                    switch(inputArg) {
                        case "path" -> {
                            String path = IM.requestInput("new path", ExecuteCondition.checkFilePath());
                            File file = new File(mainPath + "\\" + id + ".lpd");
                            try{
                                file.createNewFile();
                                FileWriter writer = new FileWriter(file);
                                writer.write(path);
                                writer.close();
                                System.out.println("Make sure to save before closing!");
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }
            }
        }
    }
    static void createProject(String name, String path, Scanner scanner) {
        File file = new File(mainPath + "\\" + name + ".lpd");
        System.out.println("creating project file");
        try{
            file.createNewFile();
            FileWriter writer = new FileWriter(file);
            writer.write(path);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        loadProject(name, scanner);
    }
    static ProjectData getPD(String id) {
        File _file = new File(mainPath + "\\" + id + ".lpd");
        try {
            Scanner file = new Scanner(_file);
            return new ProjectData(file.nextLine());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    static Hashtable<String, Hashtable<String, String>> getProjectLang(ProjectData pd) {
        try {
            Hashtable<String, Hashtable<String, String>> lang = new Hashtable<>();
            Scanner file = new Scanner(new File(pd.path));

            while(true) {
                if(!file.hasNextLine()) return lang;
                String s = file.nextLine();
                String[] S = s.trim().split(":", 2);
                if(S.length == 2) {
                    String _s = S[0].substring(S[0].indexOf('\"') + 1, S[0].lastIndexOf('\"')).replaceAll("\"", "");
                    String _S = S[1].substring(S[1].indexOf('\"') + 1, S[1].lastIndexOf('\"'));
                    String type = _s;
                    try{
                        type = _s.substring(0, _s.indexOf('.'));
                    } catch (StringIndexOutOfBoundsException e) {
                        System.out.println("\u001B[31m invalid json file, either corrupt or has been messed with");
                        throw new RuntimeException(e);
                    }
                    if (!lang.containsKey(type))
                        lang.put(type, new Hashtable<>());
                    lang.get(type).put(_s, _S);
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
