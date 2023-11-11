package MorseCode;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;

public class Main{
    public static void main(String[] args) {
        launch();
        System.out.println(new File(Paths.get("MorseCode","beepl.wav").toAbsolutePath().toString()));
    }

    static JFrame myFrame = new JFrame("Morse Converter");
    static Boolean stopAudioThread;

    public static void launch() {
        String[] opts = {"Morse->Text","Text->Morse"};
        JPanel p1 = new JPanel();
        JPanel p2 = new JPanel();
        JPanel p3 = new JPanel();
        JLabel title = new JLabel("Morse Converter");
        JComboBox<String> optbox = new JComboBox<>(opts);
        JTextArea field = new JTextArea();
        JButton btn = new JButton("Convert");
        JScrollPane sp = new JScrollPane(field);
        myFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        myFrame.setSize(500,300);
        myFrame.setLayout(new BorderLayout());
        p1.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        p2.setLayout(new BorderLayout());
        p3.setLayout(new BorderLayout());
        p3.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        optbox.setBounds(0,0,100,20);
        optbox.setBackground(Color.WHITE);
        field.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        btn.setFocusable(false);
        field.setLineWrap(true);
        field.setWrapStyleWord(true);
        field.setFont(new Font("Courier New", Font.BOLD, 14));
        p1.add(title);
        p1.add(optbox);
        p1.add(btn);
        p3.add(sp, BorderLayout.CENTER);
        p2.add(p3, BorderLayout.CENTER);
        p1.setBackground(Color.LIGHT_GRAY);
        myFrame.add(p1, BorderLayout.NORTH);
        myFrame.add(p2, BorderLayout.CENTER);
        myFrame.setResizable(false);
        myFrame.setLocationRelativeTo(null);
        myFrame.setVisible(true);

        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(optbox.getSelectedIndex()==0){
                    String[] argsArray = field.getText().replace("\n", "  ").split(" ");
                    String output = "";
                    for(String args : argsArray){
                        output = output.concat(toText(args));
                    }
                    System.out.println(output);
                    display(output);
                } 
                else if(optbox.getSelectedIndex()==1){
                    char[] charArray = field.getText().toUpperCase().replace("\n", " ").toCharArray();
                    String output = "";
                    for(char c : charArray){
                        output = output.concat(getMorse(c));
                    }
                    System.out.println(output);
                    display(output);
                    char[] beepList = output.toCharArray();
                    stopAudioThread=false;

                    Thread audioThread = new Thread(new Runnable() {
                        @Override
                        public void run(){
                            for(char c : beepList){
                                if(stopAudioThread){
                                    return;
                                }
                                switch (c) {
                                    case '.':
                                        playAudio("beeps.wav");
                                        break;
                                    case '-':
                                        playAudio("beepl.wav");
                                        break;
                                    case ' ':
                                        try {
                                            Thread.sleep(200);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        break;
                                }
                            }
                        }
                    });
                    audioThread.start();
                }
            }
        });
        optbox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                field.setText("");
            }
        });
    }

    public static void playAudio(String fileName){
        try {
            File audioFile = new File(Paths.get("MorseCode",fileName).toAbsolutePath().toString());
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();

            Thread.sleep(clip.getMicrosecondLength() / 600);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String toText(String query) {
        Map<Character, String> morseCodeMap = globalMap();
        Map<String, Character> inverseMap = new HashMap<>();
        for(Map.Entry<Character, String> entry : morseCodeMap.entrySet()){
            inverseMap.put(entry.getValue(), entry.getKey());
        }
        if(inverseMap.get(query)==null){
            return "?";
        }
        else{
            return inverseMap.get(query).toString();
        }
    }

    public static String getMorse(Character query) {
        Map<Character, String> morseCodeMap = globalMap();
        if(morseCodeMap.get(query)==null){
            return "x";
        }
        else{
            return morseCodeMap.get(query).toString().concat(" ");
        }

    }

    public static Map<Character, String> globalMap() {
        Map<Character, String> morseCodeMap = new HashMap<>();
            morseCodeMap.put('A', ".-");
            morseCodeMap.put('B', "-...");
            morseCodeMap.put('C', "-.-.");
            morseCodeMap.put('D', "-..");
            morseCodeMap.put('E', ".");
            morseCodeMap.put('F', "..-.");
            morseCodeMap.put('G', "--.");
            morseCodeMap.put('H', "....");
            morseCodeMap.put('I', "..");
            morseCodeMap.put('J', ".---");
            morseCodeMap.put('K', "-.-");
            morseCodeMap.put('L', ".-..");
            morseCodeMap.put('M', "--");
            morseCodeMap.put('N', "-.");
            morseCodeMap.put('O', "---");
            morseCodeMap.put('P', ".--.");
            morseCodeMap.put('Q', "--.-");
            morseCodeMap.put('R', ".-.");
            morseCodeMap.put('S', "...");
            morseCodeMap.put('T', "-");
            morseCodeMap.put('U', "..-");
            morseCodeMap.put('V', "...-");
            morseCodeMap.put('W', ".--");
            morseCodeMap.put('X', "-..-");
            morseCodeMap.put('Y', "-.--");
            morseCodeMap.put('Z', "--..");
            morseCodeMap.put(' ', "/");
            morseCodeMap.put('.', ".-.-.-");
            morseCodeMap.put(',', "--..--");
            morseCodeMap.put('?', "..--..");
            morseCodeMap.put('!', "-.-.--");
            morseCodeMap.put('&', ".-...");  
            morseCodeMap.put('\'', ".----."); 
            morseCodeMap.put('@', ".--.-."); 
            morseCodeMap.put(':', "---..."); 
            morseCodeMap.put('-', "-....-");
            morseCodeMap.put('(', "-.--."); 
            morseCodeMap.put(')', "-.--.-");
            morseCodeMap.put('"', ".-..-.");
            morseCodeMap.put('0', "-----");
            morseCodeMap.put('1', ".----");
            morseCodeMap.put('2', "..---");
            morseCodeMap.put('3', "...--");
            morseCodeMap.put('4', "....-");
            morseCodeMap.put('5', ".....");
            morseCodeMap.put('6', "-....");
            morseCodeMap.put('7', "--...");
            morseCodeMap.put('8', "---..");
            morseCodeMap.put('9', "----.");
        return morseCodeMap;
    }

    public static void display(String disptext){
        myFrame.setVisible(false);
        JFrame df = new JFrame("Morse Converter");
        JPanel p1 = new JPanel();
        JTextArea disp = new JTextArea();
        JScrollPane sp = new JScrollPane(disp);
        df.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        disp.setText(disptext);
        disp.setEditable(false);
        disp.setLineWrap(true);
        disp.setWrapStyleWord(true);
        disp.setFont(new Font("Courier New", Font.BOLD, 14));
        df.setSize(600,400);
        p1.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        p1.setLayout(new BorderLayout());
        df.add(p1);
        p1.add(sp, BorderLayout.CENTER);
        df.setResizable(false);
        df.setLocationRelativeTo(null);
        df.setVisible(true);
        df.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                myFrame.setVisible(true);
                stopAudioThread = true;
            }
        });
    }

}
