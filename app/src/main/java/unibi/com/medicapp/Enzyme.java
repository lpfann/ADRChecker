package unibi.com.medicapp;

import java.io.Serializable;

/**
 * @author Lukas Pfannschmidt
 *         Date: 26.05.2015
 *         Time: 17:16
 */
public class Enzyme implements Serializable{
    String name;
    Long id;

    public Enzyme(String name, Long id) {
        this.name = name;
        this.id = id;
    }
}
