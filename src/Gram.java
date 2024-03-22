import java.util.ArrayList;
import java.util.Objects;

public class Gram {
    String text;
    ArrayList<Character> children = new ArrayList<>();

    public Gram() {
    }

    public String getText() {
        return text;
    }

    @Override
    public int hashCode() {
        return text != null ? text.hashCode() : 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Gram gram = (Gram) obj;

//        return text != null ? text.equals(gram.text) : gram.text == null;
        return Objects.equals(text, gram.text);

    }
}
