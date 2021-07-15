package mekanism.common.util;

/*
 * This class is based on the MinecraftForge's implementation of
 * their Night-Config wrapper.  This class only is licensed under
 * the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation version 2.1 of the License.
 */

import com.electronwill.nightconfig.core.ConfigFormat;
import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.file.FileWatcher;
import com.electronwill.nightconfig.core.io.ParsingException;
import com.electronwill.nightconfig.core.io.WritingMode;
import mekanism.common.Mekanism;
import mekanism.common.config.BaseMekanismConfig;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Function;

public class ConfigUtils {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Marker CONFIG = MarkerManager.getMarker("CONFIG");

    private ConfigUtils() { }

    private static Path getWrappedFileName(final BaseMekanismConfig config) {
        return Path.of(Mekanism.MOD_NAME, config.getFileName() + ".toml");
    }

    public static void openConfig(final BaseMekanismConfig config) {
        LOGGER.trace(CONFIG, "Loading config file type {} at {} for {}", config.getConfigType(), getWrappedFileName(config), config.getModId());
        final CommentedFileConfig configData = reader().apply(config);
        config.getConfigSpec().setConfig(configData);
        config.clearCache();
        config.getConfigSpec().save();
    }

    public static Function<BaseMekanismConfig, CommentedFileConfig> reader() {
        return (c) -> {
            final Path configPath = FabricLoader.getInstance().getConfigDir().resolve(getWrappedFileName(c));
            final CommentedFileConfig commentedFileConfig = CommentedFileConfig.builder(configPath).sync()
                .preserveInsertionOrder().autosave()
                .onFileNotFound((newfile, configFormat) -> setupConfigFile(c, newfile, configFormat))
                .writingMode(WritingMode.REPLACE).build();
            LOGGER.debug(CONFIG, "Built TOML config for {}", configPath.toString());
            try {
                commentedFileConfig.load();
            } catch (ParsingException ex) {
                LOGGER.error(CONFIG, "Failed to load config file {}", configPath.toString());
            }
            LOGGER.debug(CONFIG, "Loaded TOML config file {}", configPath.toString());
            try {
                ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
                FileWatcher.defaultInstance().addWatch(configPath, () -> {
                    // Force the regular classloader onto the special thread
                    Thread.currentThread().setContextClassLoader(classLoader);
                    if (!c.getConfigSpec().isCorrecting()) {
                        try {
                            commentedFileConfig.load();
                            if(!c.getConfigSpec().isCorrect(commentedFileConfig)) {
                                LOGGER.warn(CONFIG, "Configuration file {} is not correct. Correcting", commentedFileConfig.getFile().getAbsolutePath());
                                c.getConfigSpec().correct(commentedFileConfig);
                                commentedFileConfig.save();
                            }
                        } catch (ParsingException ex) {
                            LOGGER.error(CONFIG, "Failed to reload config file {}", configPath.toString());
                        }
                        LOGGER.debug(CONFIG, "Config file {} changed, sending notifies", getWrappedFileName(c));
                        c.getConfigSpec().afterReload();
                        c.clearCache();
                    }
                });
                LOGGER.debug(CONFIG, "Watching TOML config file {} for changes", configPath.toString());
            } catch (IOException e) {
                throw new RuntimeException("Couldn't watch config file", e);
            }
            return commentedFileConfig;
        };
    }

    private static boolean setupConfigFile(final BaseMekanismConfig c, final Path file, final ConfigFormat<?> conf) throws IOException {
        File parent = file.getParent().toFile();
        if (!parent.mkdir()) return false;
        Files.createFile(file);
        conf.initEmptyFile(file);
        return true;
    }
}