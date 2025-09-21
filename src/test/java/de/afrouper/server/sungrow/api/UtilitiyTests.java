package de.afrouper.server.sungrow.api;

import de.afrouper.server.sungrow.api.dto.Language;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Locale;

public class UtilitiyTests {

    @Test
    void createLocale(){
        Language language = Language.fromString(Locale.GERMANY);
        Assertions.assertEquals(Language.GERMAN, language);
        language = Language.fromString(Locale.GERMAN);
        Assertions.assertEquals(Language.GERMAN, language);
        language = Language.fromString(Locale.of("de", "AT"));
        Assertions.assertEquals(Language.GERMAN, language);

        language  = Language.fromString(Locale.ENGLISH);
        Assertions.assertEquals(Language.ENGLISH, language);

        language = Language.fromString(Locale.FRENCH);
        Assertions.assertEquals(Language.FRENCH, language);
        language = Language.fromString(Locale.FRANCE);
        Assertions.assertEquals(Language.FRENCH, language);

        language = Language.fromString(Locale.ITALIAN);
        Assertions.assertEquals(Language.ITALIAN, language);
        language = Language.fromString(Locale.ITALY);
        Assertions.assertEquals(Language.ITALIAN, language);

    }
}
