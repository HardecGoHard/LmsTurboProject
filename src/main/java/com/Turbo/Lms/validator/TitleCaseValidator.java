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

    @Override
    public void initialize(TitleCase constraintAnnotation) {
        language = constraintAnnotation.language();
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        boolean flag = false;
        switch (language) {
            case ANY:
                flag = rusTitleValidation(s) || engTitleValidation(s);
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

    private boolean isExcessSpace(String str) {
        //Если длина строки после удаления лишних пробелов в начале и конце равна исходной длине строки,
        // то лишних пробелов нет.
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

    private boolean isBadSymbols(String str) {
        return str.contains("\n") || str.contains("\t") || str.contains("\r");
    }

    public boolean rusTitleValidation(String str) {
        return isBadSymbols(str) && !isExcessSpace(str) && regexChecker(str, REGEX_RUS)
                && caseOfWord(str);
    }

    public boolean engTitleValidation(String str) {
        String[] words = str.split(" ");
        boolean flag = isBadSymbols(str) && !isExcessSpace(str) && regexChecker(str, REGEX_ENG) &&
                caseOfWord(words[0]) && caseOfWord(words[words.length-1]);
        if (!flag) {
            List<String> preposition = Arrays.asList("a", "but","for", "or", "not", "the", "an");
            for (int i = 1; i< words.length-1;i++){
                if ((!preposition.contains(words[i]) && !caseOfWord(words[i])))
                        return false;
            }
        }
        return flag;
    }
    private boolean caseOfWord(String word){
        //(первая буква может быть либо заглавной либо маленькой, но остальные не должны писаться заглавными)
        return Character.isUpperCase(word.charAt(0)) && word.substring(1).equals(word.substring(1).toLowerCase());
    }
}
