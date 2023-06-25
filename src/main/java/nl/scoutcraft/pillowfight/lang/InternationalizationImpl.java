package nl.scoutcraft.pillowfight.lang;

import nl.scoutcraft.eagle.api.locale.Internationalization;
import org.bukkit.plugin.Plugin;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;

public class InternationalizationImpl extends Internationalization {

    public InternationalizationImpl(Plugin plugin) {
        super(plugin);
    }

    protected ResourceBundle loadBundle(Locale locale) {
        try {
            return ResourceBundle.getBundle("messages", locale, new URLClassLoader(new URL[]{this.langDir.toUri().toURL()}));
        } catch (MalformedURLException exc) {
            LOGGER.log(Level.SEVERE, "Failed to load resource bundle for " + locale.toLanguageTag() + ": using default file", exc);
            return ResourceBundle.getBundle("lang/messages", locale);
        }
    }

    protected void saveDefaultLangFiles(Plugin plugin) {
        try {
            if (!Files.exists(super.langDir))
                Files.createDirectories(super.langDir);

            this.saveDefaultLangFile(plugin, super.langDir, "messages_nl.properties");
            this.saveDefaultLangFile(plugin, super.langDir, "messages_en.properties");
        } catch (IOException exc) {
            LOGGER.log(Level.SEVERE, "Failed to save default lang files!", exc);
        }
    }

    private void saveDefaultLangFile(Plugin plugin, Path langDir, String fileName) throws IOException {
        Path target = langDir.resolve(fileName);

        if (!Files.exists(target))
            Files.copy(plugin.getClass().getClassLoader().getResourceAsStream("lang/" + fileName), target);
    }
}
