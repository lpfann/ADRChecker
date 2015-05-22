package unibi.com.medicapp;

import java.io.Serializable;

/**
 * @author Lukas Pfannschmidt
 *         Date: 16.05.2015
 *         Time: 13:46
 */
public class Agent implements Serializable{
    private long id;
    private String name;

    public Agent(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
