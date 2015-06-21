package unibi.com.medicapp.model;

import java.io.Serializable;

/**
 * POJO for the Enzyme Object
 * Stores id and name from the DB:
 * @author Lukas Pfannschmidt
 *         Date: 26.05.2015
 *         Time: 17:16
 */
public class Enzyme implements Serializable{
    public String name;
    public Long id;

    public Enzyme(String name, Long id) {
        this.name = name;
        this.id = id;
    }

    public Enzyme() {

    }
}
