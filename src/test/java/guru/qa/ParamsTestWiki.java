package guru.qa;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.util.List;
import java.util.stream.Stream;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static java.util.Arrays.asList;

public class ParamsTestWiki {

    @BeforeEach
    public void beforeEach() {
        Selenide.open("https://www.wikipedia.org");
    }

    @ValueSource(strings = {"1984", "451"})
    @ParameterizedTest(name = "При вводе в строку поиска на wikipedia {0} в выпадающем списке отображаются статьи " +
            "с названиями, cодержащими {0}")
    void wikiTestCommon(String testName) {
        $("#searchInput").setValue(testName);
        $$(".suggestions-dropdown").find(text(testName)).shouldBe(visible);

    }

    @CsvSource(value = {
            "1984, 1984 (роман)",
            "451, 451 градус по Фаренгейту"
    })
    @ParameterizedTest(name = "При вводе в строку поиска на wikipedia {0} в выпадающем списке отображаются статьи " +
            "с названиями, cодержащими {1}")
    void wikiTestComplex(String searchName, String expectedResult) {
        $("#searchInput").setValue(searchName);
        $$(".suggestions-dropdown").find(text(expectedResult)).shouldBe(visible);
    }

    static Stream<Arguments> wikiTestComplexDataProvider() {
        return Stream.of(
                Arguments.of("1984", asList("1984 год", "1984 (роман)", "1984 год в кино",
                        "1984 год в музыке", "1984 (фильм, 1984)", "1984 (альбом)")),
                Arguments.of("451", asList("451 год", "451 градус по Фаренгейту",
                        "451 градус по Фаренгейту (фильм, 2018)", "451 градус по Фаренгейту (фильм, 1966)",
                        "451 год до н. э.", "451 градус по Фаренгейту (значения)"))
        );
    }

    @MethodSource(value = "wikiTestComplexDataProvider")
    @ParameterizedTest(name = "При вводе в строку поиска на wikipedia {0} в выпадающем списке отображаются статьи " +
            "с названиями, cодержащими {1}")
    void wikiTestComplex(String searchName, List<String> expectedResult) {
        $("#searchInput").setValue(searchName);
        $$(".suggestion-title").shouldHave(CollectionCondition.texts(expectedResult));
    }


    @EnumSource(EnumWiki.class)
    @ParameterizedTest
    void enumTest(EnumWiki enumWiki) {
        $("#searchInput").setValue(enumWiki.desc);
        $("button[type='submit']").click();
        $$("#firstHeading").find(text(enumWiki.desc)).shouldBe(visible);
    }

}
