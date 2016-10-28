package com.github.nmcardoso.latexmanual;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextUtil {
    public static Spannable highlight(String text, String token) {
        Spannable spannable = new SpannableString(text);
        String regex = token.replace(" ", "|");
        regex = "\\Q" + regex + "\\E"; // Escape all special chars into regex
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
            spannable.setSpan(new BackgroundColorSpan(0xFFFFFF00), matcher.start(),
                    matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        return spannable;
    }

    public static Spannable highlightNearText(String text, String token) {
        String regex = token.replace(" ", "|");
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);

        Spannable spannable;

        if (matcher.find()) {
            text = text.substring(matcher.start(), text.length() > (matcher.start() + 100) ?
                    (matcher.start() + 100) : text.length() - 1);

            spannable = new SpannableString(text);
            matcher.reset(text);

            while (matcher.find()) {
                spannable.setSpan(new BackgroundColorSpan(0xFFFFFF00), matcher.start(),
                        matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        } else {
            spannable = new SpannableString(text);
        }

        return spannable;
    }
}
