package fr.univ_tours.etu.index;

import java.io.IOException;

/**
 * Created by Katherine on 24.03.2016.
 */
public interface Synoner {
    public void tokenize() throws IOException;
    public String getSynText();
}
