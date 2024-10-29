package com;

import org.yaml.snakeyaml.Yaml;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 读取文件中的环境变量
 * 保存到指定的文件
 *
 * args[0]: source file path, default: src/main/resources/application.yml
 * args[1]: target file path, default: deployment/env/env.txt
 *
 * 相对路径，自动拼接当前目录：String userDir = System.getProperty("user.dir");
 *
 */
public class ToolGenerateEnv {
    private static String sourceFile = "src/main/resources/application.yml";
    private static String sourceFile2 = "src/main/resources/application.yaml";
    private static String targetFile = "deployment/env/env.txt";

    public static void main(String[] args) {
        boolean sourceFileFlag = false;
        if (args.length > 0) {
            sourceFile = args[0];
            sourceFileFlag = true;
        }
        if (args.length > 1) {
            targetFile = args[1];
        }
        String userDir = System.getProperty("user.dir");

        if (!sourceFileFlag) {
            if (getFilePath(sourceFile) == null) {
                sourceFile = sourceFile2;
            }
        }
        sourceFile = userDir + "/" + sourceFile;
        targetFile = userDir + "/" + targetFile;
        System.out.println("sourceFile: " + sourceFile);
        System.out.println("targetFile: " + targetFile);

        clearTargetFile();
        parseYaml();

        System.out.println("-----------Done!----------");
    }

    private static Path getFilePath(String file) {
        Path filePath = Paths.get(file);
        if (!Files.exists(filePath)) {
            return null;
        }
        return filePath;
    }

    private static void parseYaml() {
        Path sourceFilePath = getFilePath(sourceFile);
        if (sourceFilePath == null) {
            throw new RuntimeException("source file not found: " + sourceFile);
        }

        Yaml yaml = new Yaml();
        try (InputStream in = Files.newInputStream(sourceFilePath)) {
            Map<String, Object> yamlMap = yaml.load(in);
            printMap("", yamlMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void printMap(String prefix, Map<String, Object> yamlMap) {
        for (Map.Entry<String, Object> entry : yamlMap.entrySet()) {
            if (entry.getValue() instanceof Map) {
                printMap(prefix + entry.getKey() + ".", (Map<String, Object>) entry.getValue());
            } else {
                String value = entry.getValue().toString();
                Pattern pattern = Pattern.compile("\\$\\{(.+?)(:.+)?\\}");
                Matcher                matcher = pattern.matcher(value);
                while (matcher.find()) {
                    String envVar = matcher.group(1);
                    String defaultValue = matcher.group(2);
                    if (defaultValue != null) {
                        envVar += ": " + defaultValue.substring(1);
                    } else {
                        envVar += ": #must-modify";
                    }
                    appendToFile(envVar + "\n");
                }
            }
        }
    }

    private static void clearTargetFile() {
        try (PrintWriter writer = new PrintWriter(targetFile)) {
            writer.print("");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void appendToFile(String text) {
        try (FileWriter writer = new FileWriter(targetFile, true)) {
            writer.write(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}