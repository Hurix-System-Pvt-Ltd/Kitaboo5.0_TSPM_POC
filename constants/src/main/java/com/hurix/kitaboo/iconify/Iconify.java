package com.hurix.kitaboo.iconify;

import static android.text.Html.fromHtml;
import static android.text.Html.toHtml;
import static com.hurix.kitaboo.iconify.Utils.replaceIcons;
import android.content.Context;
import android.graphics.Typeface;
import android.text.Spanned;
import android.widget.TextView;

public final class Iconify {

    public static final String TAG = Iconify.class.getSimpleName();

    private static Typeface typeface = null;

    private Iconify() {
        // Prevent instantiation
    }

    /**
     * Transform the given TextViews replacing {icon_xxx} texts with icons.
     */
    public static final void addIcons(String ttfFile, TextView... textViews) {
        for (TextView textView : textViews) {
            textView.setTypeface(getTypeface(textView.getContext(),ttfFile));
            textView.setAllCaps(false);
            textView.setText(compute(textView.getText()));
        }
    }

    public static CharSequence compute(CharSequence charSequence) {
        if (charSequence instanceof Spanned) {
            String text = toHtml((Spanned) charSequence);
            return fromHtml(replaceIcons(new StringBuilder((text))).toString());
        }
        String text = charSequence.toString();
        return replaceIcons(new StringBuilder(text));
    }

    public static final void setIcon(String ttfFile,TextView textView, String character) {
        textView.setTypeface(getTypeface(textView.getContext(),ttfFile));
        textView.setAllCaps(false);
        textView.setText(character);
    }

    /**
     * The typeface that contains FontAwesome icons.
     *
     * @return the typeface, or null if something goes wrong.
     */
    public static final Typeface getTypeface(Context context,String TTF_FILE) {
        if (typeface == null) {
            typeface = Typefaces.get(context, TTF_FILE);//Typeface.createFromFile(resourceToFile(context, TTF_FILE));
        }
        return typeface;
    }

//    public static enum IconValue {
//
//    	icon_a('a'),
//    	icon_b('b'),
//    	icon_c('c'),
//    	icon_d('d'),
//    	icon_e('e'),
//    	icon_f('f'),
//    	icon_g('g'),
//    	icon_h('h'),
//    	icon_i('i'),
//    	icon_j('j'),
//    	icon_k('k'),
//    	icon_l('l'),
//    	icon_m('m'),
//    	icon_n('n'),
//    	icon_o('o'),
//    	icon_p('p'),
//    	icon_q('q'),
//    	icon_r('r'),
//    	icon_s('s'),
//    	icon_t('t'),
//    	icon_u('u'),
//    	icon_v('v'),
//    	icon_w('w'),
//    	icon_x('x'),
//    	icon_y('y'),
//    	icon_z('z'),
//    	icon_A('A'),
//    	icon_B('B'),
//    	icon_C('C'),
//    	icon_D('D'),
//    	icon_E('E'),
//    	icon_F('F'),
//    	icon_G('G'),
//    	icon_H('H'),
//    	icon_I('I'),
//    	icon_J('J'),
//    	icon_K('K'),
//    	icon_L('L'),
//    	icon_M('M'),
//    	icon_N('N'),
//    	icon_O('O'),
//    	icon_P('P'),
//    	icon_Q('Q'),
//    	icon_R('R'),
//    	icon_S('S'),
//    	icon_T('T'),
//    	icon_U('U'),
//    	icon_V('V'),
//    	icon_W('W'),
//    	icon_X('X'),
//    	icon_Y('Y'),
//    	icon_Z('Z'),
//    	icon_0('0'),
//    	icon_1('1'),
//    	icon_2('2'),
//    	icon_3('3'),
//    	icon_4('4'),
//    	icon_5('5'),
//    	icon_6('6'),
//    	icon_7('7'),
//    	icon_8('8'),
//    	icon_9('9'),
//    	icon_exclaimation('!'),
//    	icon_double_quote('\"'),
//    	icon_hash('#'),
//    	icon_dollar('$'),
//    	icon_percent('%'),
//    	icon_ampersand('&'),
//    	icon_single_quote('\''),
//    	icon_brace_open('('),
//    	icon_brace_closed(')'),
//    	icon_asterics('*'),
//    	icon_plus('+'),
//    	icon_comma(','),
//    	icon_subtract('-'),
//    	icon_dot('.'),
//    	icon_slash('/'),
//    	icon_colon(':'),
//    	icon_semi_colon(';'),
//    	icon_less_than('<'),
//    	icon_equals('='),
//    	icon_greater_than('>'),
//    	icon_question_mark('?'),
//    	icon_at_the_rate('@'),
//    	icon_bracket_open('['),
//    	icon_bracket_close(']'),
//    	icon_caret('^'),
//    	icon_underscore('_'),
//    	icon_grave_accent('`'),
//    	icon_curly_start('{'),
//    	icon_curly_end('}'),
//    	icon_vertical_bar('|'),
//    	icon_backslash('\\'),
//    	icon_tilde('~');
//    	
//
//        char character;
//
//        IconValue(char character) {
//            this.character = character;
//        }
//
//        public String formattedName() {
//            return "{" + name() + "}";
//        }
//
//        public char character() {
//            return character;
//        }
//    }
}
