package com.Turbo.Lms.validator;

import com.Turbo.Lms.annotations.TitleCase;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class TitleCaseValidator implements ConstraintValidator<TitleCase, String> {

    private TitleCase.Language language;
    private static final String REGEX_ENG = "([A-Za-z ,\"']+)";
    private static final String REGEX_RUS = "([А-Яа-я ,\"']+)";
    private static final List<String> PREPOSITION = Arrays.asList("a", "but", "for", "or", "not", "the", "an");
    @Override
    public void initialize(TitleCase constraintAnnotation) {
        language = constraintAnnotation.language();
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        boolean flag = false;
        switch (language) {
            case ANY:
                flag = !isExcessSpace(s) && (regexChecker(s,REGEX_RUS) || regexChecker(s,REGEX_ENG));
                break;
            case EN:
                flag = engTitleValidation(s);
                break;
            case RU:
                flag = rusTitleValidation(s);
                break;
        }
        return flag;
    }

    public void setLanguage(TitleCase.Language language) {
        this.language = language;
    }

    private  boolean isExcessSpace(String str) {
        /*
        Если длина строки после удаления лишних пробелов (в начале и конце)
        равна исходной длине строки, то лишних пробелов нет.
        */
        boolean excessSpaceFlag = !(str.trim().length() == str.length());
        if (!excessSpaceFlag) {
            String[] words = str.split(" ");
            for (String word : words) {
                if (word.length() == 0) return true;
            }
        }
        return excessSpaceFlag;
    }

    private boolean regexChecker(String str, String regex) {
        return Pattern.compile(regex).matcher(str).matches();
    }

    private boolean rusTitleValidation(String str) {
        return !isExcessSpace(str) && regexChecker(str, REGEX_RUS)
                && caseOfWord(str);
    }

    private boolean engTitleValidation(String str) {
        String[] words = str.split(" ");
        boolean flag = !isExcessSpace(str) && regexChecker(str, REGEX_ENG) &&
                caseOfWord(words[0]) && caseOfWord(words[words.length - 1]);
        if (flag) {
            for (int i = 1; i < words.length - 1; i++) {
                if ((PREPOSITION.contains(words[i].toLowerCase()))) {
                    if (!words[i].toLowerCase().equals(words[i]))
                        return false;
                } else if (!caseOfWord(words[i]))
                    return false;

            }
        }
        return flag;
    }

    private static boolean caseOfWord(String word) {
        //первая буква может быть заглавной , но остальные НЕ должны писаться заглавными
        return Character.isUpperCase(word.charAt(0)) && word.substring(1).equals(word.substring(1).toLowerCase());
    }
}
