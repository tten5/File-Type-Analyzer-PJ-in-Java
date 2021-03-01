package analyzer;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    private static final int BUFFER_SIZE = 4096; // 4KB

    public static void main(String[] args) {

        if (args.length < 2) {
            System.out.println("Please provide directory containing target-files and a file containing target-pattern arguments");
            System.exit(0);
        }

        //String method = args[0];
        String method = "--RbK";

        // the directory contain file need to be analyzed
        String inputDir = args[0];

        // the file contain pattern list in priorities order
        String patternFile = args[1];
        var patternList = ReadAFileIntoList(patternFile);

        // create new File object
        File directory = new File(inputDir);
        File[] files = directory.listFiles();

        // check whether this is a directory
        if (files == null) {
            System.out.println("Please input a directory");
            System.exit(0);
        }

        // executor for multi-threading pattern check
        ExecutorService executor = Executors.newFixedThreadPool(10);
        for (File inputFile : files) {
            for (int i = patternList.size() - 1; i >= 0; i--) {

                // get the pattern from high priorities (at the end of the list) to low priorities (at the top of the list)
                String pattern = patternList.get(i)[1];
                String fileType = patternList.get(i)[2];

                // submit the checkPattern task to executor
                executor.submit(() -> {
                    if (checkPatternFromInputFile(inputFile.getPath(), method, pattern)) {
                        System.out.println(inputFile.getName() + ": " + fileType);
                    } else {
                        System.out.println(inputFile.getName() + ": Unknown file type");
                    }
                });
            }
        }

        executor.shutdown();

        try {
            executor.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    static boolean checkPatternFromInputFile(String inputFile, String method, String pattern) {

        // boolean to check is the file of a specific type
        boolean isType = false;

        // read the input file
        try (
                InputStream inputStream = new FileInputStream(inputFile)
        ) {
            // initialize a buffer that FileInputStream save data read from the file into
            byte[] buffer = new byte[BUFFER_SIZE];

            // create an FindPatternMachine instance
            FindPatternMachine findPatternMachine = new FindPatternMachine();

            if (method.equals("--naive")) {
                findPatternMachine.setMethod(new NaiveMethod());
            } else if (method.equals("--KMP")) {
                findPatternMachine.setMethod(new KMPMethod());
            } else if (method.equals("--RbK")) {
                findPatternMachine.setMethod(new RabinKarpMethod());
            }

            // process each input buffer until buffer is found or until the end of the file
            while (inputStream.read(buffer) != -1) {

                // convert the buffer into String
                String inputBufferAsString = new String(buffer, StandardCharsets.UTF_8);

                // make a intervalSubstring to avoid case when pattern is being divided into different buffer block
                String intervalSubstring = inputBufferAsString.substring(inputBufferAsString.length() - pattern.length());

                // compare this combinedString
                String combinedString = intervalSubstring + inputBufferAsString;

                if (findPatternMachine.find(combinedString, pattern)) {
                    isType = true;
                    break;
                }
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return isType;

    }

    static List<String[]> ReadAFileIntoList(String pathToFile) {
        List<String[]> lines = new ArrayList<>();
        File file = new File(pathToFile);

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNext()) {
                var line = scanner.nextLine().replaceAll("\"", "");
                lines.add(line.split(";"));
            }

        } catch (FileNotFoundException e) {
            System.out.println("No file found: " + pathToFile);
        }
        return lines;
    }
}
