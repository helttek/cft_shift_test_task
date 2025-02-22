package cftShift.fileFilter;

import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ArgsValidator {
    private static final int MAX_FILE_NAME_LENGTH = 255;
    private static final int MAX_OUTPUT_FILE_LENGTH_NO_PREFIX = 12;// TODO: recalculate if a new type of output file is added
    private static Config config;

    public ArgsValidator() {
        config = new Config();
    }

    /*
     * TODO:
     * -
     */

    public Config validate(String[] args) {
        String curPath = null;
        String curPrefix = null;
        for (int i = 0; i < args.length; i++) {
            //process option arguments
            if (curPath != null) {
                if (isValidPath(curPath)) {
                    config.setPath(curPath);
                }
                curPath = null;
                continue;
            }
            if (curPrefix != null) {
                if (isValidPrefix(curPrefix)) {
                    config.setPrefix(curPrefix);
                }
                curPrefix = null;
                continue;
            }

            switch (args[i]) {
                case "-o":
                    if (i + 1 < args.length) {
                        if (config.getPath() != null) {
                            System.out.println("WARNING: Path has been specified already (rewriting the path to [" + args[i + 1] + "])");
                        }
                    } else {
                        if (config.getPath() != null) {
                            System.out.println("ERROR: Path wasn't provided in the last '-o' instance. Output files will be created in directory provided by the previous path [" + config.getPath() + "].");
                        } else {
                            System.out.println("ERROR: Path wasn't provided. Output files will be created in the current directory.");
                        }
                        break;
                    }
                    curPath = args[i + 1];
                    break;

                case "-p":
                    if (i + 1 < args.length) {
                        if (config.getPrefix() != null) {
                            System.out.println("WARNING: Prefix has been specified already (rewriting the prefix to [" + args[i + 1] + "])");
                        }
                    } else {
                        if (config.getPrefix() != null) {
                            System.out.println("ERROR: Prefix wasn't provided in the last '-o' instance. Output files will be created with the previous prefix [" + config.getPrefix() + "].");
                        } else {
                            System.out.println("ERROR: Prefix wasn't provided. Output files will be created without the prefix.");
                        }
                        break;
                    }
                    curPrefix = args[i + 1];
                    break;

                case "-a":
                    config.setPrependToExisting(true);
                    break;

                case "-s":
                    config.setShortStatistics(true);
                    break;

                case "-f":
                    config.setFullStatistics(true);
                    break;

                default:
                    if (isValidInputFile(args[i])) {
                        config.addInputFile(args[i]);
                    }
                    break;
            }
        }
        if (config.getPath() == null) {
            config.setPathToDefault();
        }
        if (config.getPrefix() == null) {
            config.setPrefixToDefault();
        }
        if (config.noInputFiles()) {
            System.out.println("ERROR: No input files were provided.");
            return null;
        }
        return config;
    }

    private static boolean isValidPath(String path) {
        try {
            Path dir = Path.of(path);
            if (Files.isDirectory(dir)) {
                if (!Files.isExecutable(dir) || !Files.isWritable(dir)) {
                    System.err.println("ERROR: Provided path [" + path + "] lacks permissions. Output file(s) will be created in the current directory.");
                    return false;
                }
            } else {
                System.err.println("ERROR: File lead to by provided path [" + path + "] is not a directory. Output file(s) will be created in the current directory.");
                return false;
            }
        } catch (InvalidPathException | SecurityException e) {
            System.err.println("ERROR: Provided path [" + path + "] is not valid (" + e.getMessage() + "). Output file(s) will be created in the current directory.");
            return false;
        }
        return true;
    }

    private static boolean isValidPrefix(String prefix) {
        if (MAX_FILE_NAME_LENGTH - prefix.length() < MAX_OUTPUT_FILE_LENGTH_NO_PREFIX) {
            System.err.println("ERROR: Prefix [" + config.getPrefix() + "] is too big. The output files will be created without the prefixes.");
            return false;
        }
        return true;
    }

    private static boolean isValidInputFile(String inFile) {
        try {
            Path file = Paths.get(inFile);
            if (Files.exists(file)) {
                if (!Files.isReadable(file) || !Files.isWritable(file)) {
                    System.err.println("ERROR: Provided input file [" + inFile + "] lacks permissions. Skipping this file.");
                    return false;
                }
            } else {
                System.err.println("ERROR: File [" + inFile + "] does not exist. Skipping this file.");
                return false;
            }
        } catch (InvalidPathException e) {
            System.err.println("ERROR: Provided path [" + inFile + "] is not valid (" + e.getMessage() + "). Skipping this file.");
            return false;
        }
        return true;
    }
}
