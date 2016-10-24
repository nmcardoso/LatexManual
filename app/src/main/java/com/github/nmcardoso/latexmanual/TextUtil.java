package com.github.nmcardoso.latexmanual;

import android.text.Spannable;
import android.text.style.BackgroundColorSpan;
import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class TextUtil {
    public static Spannable highlight(String content, String token) {
        List<Pair<Integer, Integer>> pairs = new ArrayList<>();
        Spannable spanText = Spannable.Factory.getInstance().newSpannable(content);
        int count = 0;
        String contentLC = content.toLowerCase();
        String tokenLC = token.toLowerCase();

        if (!contentLC.contains(tokenLC) || token.equals("")) {
            return spanText;
        }

        while (count < contentLC.length() &&
                contentLC.substring(count).contains(tokenLC)) {
            int index = contentLC.indexOf(tokenLC);
            pairs.add(new Pair<Integer, Integer>(index, index + tokenLC.length()));
            count += index + tokenLC.length();
        }

        for (int i = 0; i < pairs.size(); i++) {
            spanText.setSpan(new BackgroundColorSpan(0xFFFFFF00), pairs.get(i).first,
                    pairs.get(i).second, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        return spanText;
    }
}
