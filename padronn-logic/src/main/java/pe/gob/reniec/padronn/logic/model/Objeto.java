package pe.gob.reniec.padronn.logic.model;

/**
 * Created with IntelliJ IDEA.
 * User: jfloresh
 * Date: 17/07/13
 * Time: 05:57 PM
 */
public class Objeto implements java.io.Serializable{

    String id;
    String text;

    public Objeto() {
    }

    public String getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "Objeto{" +
                "id='" + id + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}
