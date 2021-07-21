package com.Turbo.Lms;

import com.Turbo.Lms.annotations.TitleCase;
import com.Turbo.Lms.validator.TitleCaseValidator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
public class TitleCaseValidatorTest {

    private static TitleCaseValidator titleCaseValidator;
    private static final String[] BAD_RUS_TITLE = {"КуРс молодого бойца", "курс молодого бойца", "Курс молодого Бойца",
                                              "Курс\nмолодого бойца", "Курс мо\rлодого бойца", "Курс мо\rлодого бойца",
                                              " Курс молодого бойца", "Курс молодого бойца ",
                                              "    Курс    молодого    бойца    ", "Курс  молодого  бойца",
                                              "Курс молодого @бойца", "Курс ! молодого бойца", "Курс young бойца",
                                              "Курс молодого бой;ца", "Young fighter course"
    };

    private static final String[] GOOD_RUS_TITLE = {"Курс молодого бойца", "Курс, молодого, бойца",
                                                    "Курс 'молодого бойца'", "Курс \"молодого бойца\""
    };
    private static final String[] BAD_EN_TITLE ={"ThE Young but Fighters", "The young Fighters","The youNg Fighters",
                                                 "The Young But Fighters","the Young Fighters","The  Young Fighters",
                                        "The Young Fighters "," The Young Fighters ","         The   Young Fighters ",
                                        "Young Fighter курс","You*ng F)(ighter Course","The Young bu\t Fighters",
                                        "The \n Young but Fighters","Young Fighter C2urse","Курс молодого бойца"
    };
     private static final String[] BAD_ANY_TITLE ={"Th2E Yo5ung but Fighters","The   young Fighters",
             "The young Fighters  ", "error Ошибка", "Afdt 4ete", "Курс мо\rлодого бойца","Курс мо\rлодого бойца",
                };

    private static final String[] GOOD_EN_TITLE = { "Young Fighter Course",  "Young a Fighter Course",
                                                     "An Young a Fighter Course", "The Young but Fighters",
                                                     "The Young, but Fighters"};

    private static final String[] GOOD_ANY_TITLE = { "Young Fighter Course",  "Young a Fighter Course",
                                                    "Ночь, улица, Фонарь, аптека","курс по 'Слепой печати'",
                                                     "she is lost control"};

    @BeforeAll
    public void init() {
        titleCaseValidator = new TitleCaseValidator();
    }

    //Недопустимые по тз заголовки
    @Test
    public void badRusTitle() throws Exception {
        titleCaseValidator.setLanguage(TitleCase.Language.RU);
        for (String s : BAD_RUS_TITLE) {
            assertFalse(callValidMethod(s));
        }
    }

    @Test
    public void badEngTitle() throws Exception {
        titleCaseValidator.setLanguage(TitleCase.Language.EN);
         for (String s : BAD_EN_TITLE) {
             assertFalse(callValidMethod(s));
         }
    }

    @Test
    public void badAnyTitle() throws Exception {
        titleCaseValidator.setLanguage(TitleCase.Language.ANY);
        for (String s : BAD_ANY_TITLE) {
            assertFalse(callValidMethod(s));
        }

    }
    //Допустимые по тз заголовки
    @Test
    public void goodRusTitle() throws Exception {
        titleCaseValidator.setLanguage(TitleCase.Language.RU);
        for (String s : GOOD_RUS_TITLE) {
            assertTrue(callValidMethod(s));
        }
    }

    @Test
    public void goodEngTitle() throws Exception {
        titleCaseValidator.setLanguage(TitleCase.Language.EN);
        for (String s : GOOD_EN_TITLE) {
            assertTrue(callValidMethod(s));
        }
    }
    @Test
    public void goodAnyTitle() throws Exception {
        titleCaseValidator.setLanguage(TitleCase.Language.ANY);
        for (String s : GOOD_ANY_TITLE) {
            System.out.println(s);
            assertTrue(callValidMethod(s));
        }

    }
    private boolean callValidMethod(String str) {
        return titleCaseValidator.isValid(str, null);
    }
}
