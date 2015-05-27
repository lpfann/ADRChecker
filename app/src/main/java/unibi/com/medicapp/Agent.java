package unibi.com.medicapp;

import java.io.Serializable;

/**
 * @author Lukas Pfannschmidt
 *         Date: 16.05.2015
 *         Time: 13:46
 */
public class Agent implements Serializable{
    long id;
    String name;

    public Agent(long id, String name) {
        this.id = id;
        this.name = name;
    }



    @Override
    public String toString() {
        return name;
    }
}
