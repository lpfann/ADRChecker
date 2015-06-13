package unibi.com.medicapp.model;

import java.io.Serializable;

/**
 * @author Lukas Pfannschmidt
 *         Date: 16.05.2015
 *         Time: 13:46
 */
public class Substance implements Serializable {
    public long id;
    public String name;

    public Substance(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Substance() {
    }


    @Override
    public String toString() {
        return name;
    }
}
