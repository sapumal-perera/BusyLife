package com.emperia.isurup.busylife;

/**
 * Created by IsuruP on 4/15/2018.
 */

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.content.Context;

public class XMLParser {
    /***
     * declare & initialize variables
     */
    private static final String KEY_LIST = "list";
    private static final String KEY_CATEGORY = "category";
    private static final String KEY_SYNONYMS = "synonyms";

    /**
     *  get Synonyms From File
     * @param ctx
     * @return
     */
    public static List<Synonym> getSynonymsFromFile(Context ctx) {
        List<Synonym> synonymList;
        synonymList = new ArrayList<>();
        Synonym currentSynonym = null;
        String currentText = "";

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp = factory.newPullParser();
            FileInputStream fis = ctx.openFileInput("synonyms.xml");
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            xpp.setInput(reader);
            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagName = xpp.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if (tagName.equalsIgnoreCase(KEY_LIST)) {
                            currentSynonym = new Synonym();
                        }
                        break;

                    case XmlPullParser.TEXT:
                        currentText = xpp.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        if (tagName.equalsIgnoreCase(KEY_LIST)) {

                            synonymList.add(currentSynonym);
                        } else if (tagName.equalsIgnoreCase(KEY_CATEGORY)) {

                            currentSynonym.setCategory(currentText);
                        } else if (tagName.equalsIgnoreCase(KEY_SYNONYMS)) {

                            currentSynonym.setSynonyms(currentText);
                        }
                        break;

                    default:
                        break;
                }

                eventType = xpp.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return synonymList;
    }
}
