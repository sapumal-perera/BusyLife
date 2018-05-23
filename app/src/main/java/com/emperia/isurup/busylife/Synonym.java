package com.emperia.isurup.busylife;

/**
 * Created by IsuruP on 4/15/2018.
 */

public class Synonym {
    /***
     * declare & initialize variables
     */
    private String category;
    private String synonyms;

    /**
     * generated getters and setters
     * @return
     */

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSynonyms() {
        return synonyms;
    }

    public void setSynonyms(String synonyms) {
        this.synonyms = synonyms;
    }

    @Override
    public String toString() {
        return "Synonym [Category=" + category + ", Synonyms = " + synonyms + "]";
    }
}
