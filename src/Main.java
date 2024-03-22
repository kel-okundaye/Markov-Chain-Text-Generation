import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

public class Main {

    static ArrayList<Gram> ngrams = new ArrayList<>();
    static int order = 6; // Default order
    static String txt; // This will now be set by the user

    public static void main(String[] args) {
        theFrame();
    }

    public static void setup(String inputText, int newOrder) {
        txt = inputText.toLowerCase();
        order = newOrder;
        ngrams.clear(); // Clear existing n-grams
        for (int i = 0; i < txt.length() - order; i++) {
            String gramText = txt.substring(i, i + order);
            Gram gram = new Gram();
            gram.text = gramText;
            if (!ngrams.contains(gram)) {
                gram.children.add(txt.charAt(i + order));
                ngrams.add(gram);
            } else {
                int index = ngrams.indexOf(gram);
                Gram existingGram = ngrams.get(index);
                existingGram.children.add(txt.charAt(i + order));
            }
        }
    }

    public static void theFrame() {
        JFrame frame = new JFrame("Markov Chain Text Generator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JTextArea inputTextArea = new JTextArea(5, 30);
        inputTextArea.setLineWrap(true);
        inputTextArea.setWrapStyleWord(true);
        JScrollPane inputScrollPane = new JScrollPane(inputTextArea);

        JTextArea outputTextArea = new JTextArea(10, 30);
        outputTextArea.setEditable(false);
        outputTextArea.setLineWrap(true);
        outputTextArea.setWrapStyleWord(true);
        JScrollPane outputScrollPane = new JScrollPane(outputTextArea);

        JButton updateTextButton = new JButton("Update Text");
        updateTextButton.addActionListener(e -> {
            String inputText = inputTextArea.getText();
            if (!inputText.isEmpty()) {
                setup(inputText, order);
            }
        });

        JButton generateTextButton = new JButton("Generate Text");
        generateTextButton.addActionListener(e -> outputTextArea.append(markovIt()));

        JSlider orderSlider = new JSlider(JSlider.HORIZONTAL, 1, 10, order);
        orderSlider.setMajorTickSpacing(1);
        orderSlider.setPaintTicks(true);
        orderSlider.setPaintLabels(true);
        orderSlider.addChangeListener(e -> {
            JSlider source = (JSlider) e.getSource();
            if (!source.getValueIsAdjusting()) {
                int newOrder = source.getValue();
                if (txt != null && !txt.isEmpty()) {
                    setup(txt, newOrder);
                }
            }
        });

        JPanel controlPanel = new JPanel();
        controlPanel.add(orderSlider);
        controlPanel.add(updateTextButton);
        controlPanel.add(generateTextButton);

        frame.add(inputScrollPane, BorderLayout.NORTH);
        frame.add(controlPanel, BorderLayout.CENTER);
        frame.add(outputScrollPane, BorderLayout.SOUTH);

        frame.pack();
        frame.setVisible(true);
    }

    public static String markovIt() {
        if (txt == null || txt.isEmpty()) return "Please input text and update.\n";
        if (order > txt.length()) return "Order too large for text length.\n";
        String currentGram = txt.substring(0, Math.min(order, txt.length()));
        StringBuilder result = new StringBuilder(currentGram);
        for (int i = 0; i < 100; i++) {
            Gram gram = new Gram();
            gram.text = currentGram;
            if (ngrams.contains(gram)) {
                int index = ngrams.indexOf(gram);
                Gram nextGram = ngrams.get(index);
                if (!nextGram.children.isEmpty()) {
                    int randomIndex = (int) (Math.random() * nextGram.children.size());
                    char nextChar = nextGram.children.get(randomIndex);
                    result.append(nextChar);
                    currentGram = result.substring(result.length() - order);
                }
            }
        }
        return result.toString() + "\n"+ "\n";
    }
}
