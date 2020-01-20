package net.dblsaiko.winwonders;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static net.dblsaiko.winwonders.WindowWonders.MOD_ID;

public class Config {

    public static final Path DEFAULT_PATH = Paths.get("config").resolve("winsize.cfg");
    private static Config INSTANCE;

    public boolean restorePosition = true;
    public boolean restoreDimensions = true;
    public boolean savePosition = true;
    public boolean saveDimensions = true;
    public int winX = -1;
    public int winY = -1;
    public int winWidth = -1;
    public int winHeight = -1;

    private Config() {}

    public static Config getInstance() {
        if (INSTANCE == null) {
            INSTANCE = Config.read(DEFAULT_PATH);
        }

        return INSTANCE;
    }

    public static Config read(Path path) {
        Config config = new Config();
        if (Files.isRegularFile(path)) {
            try {
                for (String line : Files.readAllLines(path, StandardCharsets.UTF_8)) {
                    String[] args = line.replaceFirst("//.*$", "").trim().split("\\s+");
                    if (args.length > 1) {
                        switch (args[0]) {
                            case "restore_pos":
                                config.restorePosition = parseIntOrZero(args[1]) != 0;
                                break;
                            case "restore_dim":
                                config.restoreDimensions = parseIntOrZero(args[1]) != 0;
                                break;
                            case "save_pos":
                                config.savePosition = parseIntOrZero(args[1]) != 0;
                                break;
                            case "save_dim":
                                config.saveDimensions = parseIntOrZero(args[1]) != 0;
                                break;
                            case "pos":
                                if (args.length < 3) break;
                                config.winX = parseIntOrZero(args[1]);
                                config.winY = parseIntOrZero(args[2]);
                                break;
                            case "dim":
                                if (args.length < 3) break;
                                config.winWidth = parseIntOrZero(args[1]);
                                config.winHeight = parseIntOrZero(args[2]);
                                break;
                            default:
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return config;
    }

    public void write(Path path) {
        try {
            Files.createDirectories(path.getParent());
        } catch (IOException e) {
            e.printStackTrace();
            return; // don't need to even try writing the file
        }

        try (PrintWriter out = new PrintWriter(Files.newBufferedWriter(path, StandardCharsets.UTF_8))) {
            out.printf("// Configuration for %s\n", MOD_ID);
            out.println();
            out.println("// Whether to restore the position/dimensions of the game window (1 = yes, 0 = no)");
            out.printf("restore_pos %d\n", restorePosition ? 1 : 0);
            out.printf("restore_dim %d\n", restoreDimensions ? 1 : 0);
            out.println();
            out.println("// Whether to save the position/dimensions of the game window to this file (1 = yes, 0 = no)");
            out.printf("save_pos %d\n", savePosition ? 1 : 0);
            out.printf("save_dim %d\n", saveDimensions ? 1 : 0);
            out.println();
            out.println("// Last position of the game window (x, y)");
            out.printf("pos %d %d\n", winX, winY);
            out.println();
            out.println("// Last dimensions of the game window (width, height)");
            out.printf("dim %d %d\n", winWidth, winHeight);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int parseIntOrZero(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

}
