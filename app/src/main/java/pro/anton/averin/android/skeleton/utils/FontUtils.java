package pro.anton.averin.android.skeleton.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by AAverin on 30.01.14.
 */
public class FontUtils {

    public static interface FontTypes {
//        public static String ARIALN = "ArialN";
//        public static String ARIAL_NARROW = "ArialNarrow";
        public static String HELVETICA_NEUE_LT = "HelveticaNeueLt";
        public static String HELVETICA_NEUE_LTCN = "HelveticaNeueLtCn";
//        public static String HELVETICA_NEUE_MD = "HelveticaNeueMd";
//        public static String HELVETICA_NEUE_BD = "HelveticaNeueBD";
//        public static String HELVETICA_NEUE_MDIT = "HelveticaNeueMdIt";
    }

    /**
     * map of font types to font paths in assets
     */
    private static Map<String, String> fontMap = new HashMap<String, String>();
    static {
//        fontMap.put(FontTypes.ARIALN, "fonts/ARIALN.TTF");
//        fontMap.put(FontTypes.ARIAL_NARROW, "fonts/ArialNarrow.ttf");
        fontMap.put(FontTypes.HELVETICA_NEUE_LT, "fonts/HelveticaNeueLTStd-Lt.ttf");
//        fontMap.put(FontTypes.HELVETICA_NEUE_LTCN, "fonts/HelveticaNeueLTStd-LtCn.otf");
//        fontMap.put(FontTypes.HELVETICA_NEUE_MD, "fonts/HelveticaNeueLTStd-Md.otf");
//        fontMap.put(FontTypes.HELVETICA_NEUE_BD, "fonts/HelveticaNeueLTStd-Bd.otf");
//        fontMap.put(FontTypes.HELVETICA_NEUE_MDIT, "fonts/HelveticaNeueLTStd-MdIt.otf");
    }

    /* cache for loaded typefaces*/
    private static Map<String, Typeface> typefaceCache = new HashMap<String, Typeface>();

    /**
     * Creates Roboto typeface and puts it into cache
     * @param context
     * @param fontType
     * @return
     */
    public static Typeface getTypeface(Context context, String fontType) {
        String fontPath = fontMap.get(fontType);
        Typeface newTypeface = null;
        try {
            if (!typefaceCache.containsKey(fontType))
            {
                typefaceCache.put(fontType, Typeface.createFromAsset(context.getAssets(), fontPath));
            }
            newTypeface = typefaceCache.get(fontType);
        } catch (NullPointerException e) {

        } catch (RuntimeException r) {
            newTypeface = Typeface.DEFAULT;
        }

        return newTypeface;
    }

    /**
     * Gets roboto typeface according to passed typeface style settings.
     * Will get Roboto-Bold for Typeface.BOLD etc
     * @param context
     * @return
     */
    private static Typeface getHelveticaTypeface(Context context, Typeface originalTypeface) {
        String helveticaFontType = FontTypes.HELVETICA_NEUE_LT; //default Light Roboto font
        if (originalTypeface != null) {
            int style = originalTypeface.getStyle();
            switch (style) {
                case Typeface.BOLD:
                    helveticaFontType = FontTypes.HELVETICA_NEUE_LTCN;
            }
        }
        return getTypeface(context, helveticaFontType);
    }

    /**
     * Walks ViewGroups, finds TextViews and applies Typefaces taking styling in consideration
     * @param context - to reach assets
     * @param view - root view to apply typeface to
     */
    public static void setHelveticaFont(Context context, View view)
    {
        if (view instanceof ViewGroup)
        {
            for (int i = 0; i < ((ViewGroup)view).getChildCount(); i++)
            {
                setHelveticaFont(context, ((ViewGroup) view).getChildAt(i));
            }
        }
        else if (view instanceof TextView)
        {
            Typeface currentTypeface = ((TextView) view).getTypeface();
            ((TextView) view).setTypeface(getHelveticaTypeface(context, currentTypeface));
        }
    }
}

