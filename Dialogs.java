//
//  Name:       Brenden Johnson
//  Project:   #6
//  Due:        5-12-2023
//  Course:     cs-2450-01-sp23
//
//  Description:
//              Dialogs Class

//package com.mycompany.jnotepad;
import java.awt.*;
import javax.swing.*;

public class Dialogs {
        static Font font;
        static int choice;
        static String input;
        
        public interface FindCallback {
            int onFind(String input, int lastIndex);
        }
        
        public static String showFindDialog(Frame frame, FindCallback callback){
            JDialog dl = new JDialog(frame, "Find in file", false);
            dl.setSize(300,120);
            dl.setLayout(new FlowLayout());
            int[] lastIndex = {-1}; 
            
            JLabel label = new JLabel("Find:");
            dl.add(label, BorderLayout.NORTH);
            
            JTextField field = new JTextField(25);
            dl.add(field);
            
            JButton find = new JButton("Find");
            find.addActionListener((ae) -> {
                input = field.getText();
                lastIndex[0] = -1;
                lastIndex[0] = callback.onFind(input, lastIndex[0]);
            });
            dl.add(find, BorderLayout.SOUTH);
            dl.getRootPane().setDefaultButton(find);
            
            JButton next = new JButton("Next");
            next.addActionListener((ae)->{
                lastIndex[0] = callback.onFind(input, lastIndex[0]);
            });
            dl.add(next);
           

            JButton cancel = new JButton("Cancel");
            cancel.addActionListener((ae) -> {
                dl.dispose();
            });
            dl.add(cancel, BorderLayout.SOUTH);

            
            input = null;
            dl.setLocationRelativeTo(frame);
            dl.setVisible(true);
            
            return input;
        }
        
        public static int showCloseDialog(Frame frame){
            JDialog dl = new JDialog(frame, "Confirm Exit", true);
            dl.setSize(300,120);
            dl.setLayout(new FlowLayout());

            dl.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            
            JLabel label = new JLabel("<html>You have unsaved changes.<br> Are you sure you would like to continue?");
            dl.add(label);
            
            JButton discard = new JButton("Discard");
            discard.addActionListener((ae) -> {
                choice = 0;
                dl.dispose();
            });
            dl.add(discard, BorderLayout.SOUTH);
            
            JButton save = new JButton("Save");
            save.addActionListener((ae) -> {
                choice = 2;
                dl.dispose();
            });
            dl.add(save, BorderLayout.SOUTH);
            
            JButton cancel = new JButton("Cancel");
            cancel.addActionListener((ae) -> {
                dl.dispose();
            });
            dl.add(cancel, BorderLayout.SOUTH);
            
            choice = 1;
            dl.setLocationRelativeTo(frame);
            dl.setVisible(true);
            
            return choice;
        }
        
        public static Font showFontDialog(Frame frame, String title, Font initialFont){
            String fontscollection[] = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
            JPanel centerPanel, fontsPanel, stylePanel, effectsPanel, sizePanel;
            JCheckBox caps;
            JSlider slider;
            JLabel sizeLabel, fontsLabel;
            JRadioButton regular, italic, bold;
            JList fontlist;
            JScrollPane scrollPane;
            ButtonGroup stylesGroup;

            JDialog dl = new JDialog(frame, title, true);
            dl.setSize(700, 300);
            dl.setLayout(new FlowLayout());
            dl.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            
            
            sizePanel = new JPanel(new GridLayout(2, 1));
            //Slider Code
            slider = new JSlider(JSlider.HORIZONTAL, 8, 20, initialFont.getSize());
            slider.setMinorTickSpacing(2);
            slider.setSnapToTicks(true);
            slider.setPaintTicks(true);
            //Label for Slider
            sizeLabel = new JLabel("Size:");
            sizeLabel.setDisplayedMnemonic('S');
            sizeLabel.setLabelFor(slider);
            //Add Components
            sizePanel.add(sizeLabel);
            sizePanel.add(slider);

            //Center Panel
            centerPanel = new JPanel(new GridLayout(1, 3));

            //Font Panel
            fontsPanel = new JPanel();
            fontsPanel.setLayout(new BoxLayout(fontsPanel, BoxLayout.Y_AXIS));
            fontsPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
            //Font list
            fontlist = new JList<>(fontscollection);
            fontlist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            fontlist.setSelectedValue(initialFont.getFamily(), true);
            //Scroll Pane
            scrollPane = new JScrollPane(fontlist);        
            scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
            //Label for Font Panel
            fontsLabel = new JLabel("Fonts:");
            fontsLabel.setDisplayedMnemonic('F');
            fontsLabel.setLabelFor(fontlist);
            //Add Components to Font Panel
            fontsPanel.add(fontsLabel);
            fontsPanel.add(scrollPane);
            centerPanel.add(fontsPanel);

            //Style Panel
            stylePanel = new JPanel(new GridLayout(4, 1));
            stylePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
            //Radio Buttons
            regular = new JRadioButton("Regular");
            regular.setMnemonic('R');
            italic = new JRadioButton("Italic");
            italic.setMnemonic('I');
            bold = new JRadioButton("Bold");
            bold.setMnemonic('B');
            switch(initialFont.getStyle()){
                case 0:
                    regular.setSelected(true);
                    break;
                case 1:
                    bold.setSelected(true);
                    break;
                case 2:
                    italic.setSelected(true);
                    break;
            }
            //Radio Button Group
            stylesGroup = new ButtonGroup();
            stylesGroup.add(regular);
            stylesGroup.add(italic);
            stylesGroup.add(bold);
            //AddComponents to panel
            stylePanel.add(new JLabel("Style:"));
            stylePanel.add(regular);
            stylePanel.add(italic);
            stylePanel.add(bold);
            centerPanel.add(stylePanel);

            //Effects Panel
            effectsPanel = new JPanel(new GridLayout(4, 1));
            effectsPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
//            effectsPanel.add(new JLabel("Effects:"));
            //Caps Check Box
            caps = new JCheckBox("All Caps");
            caps.setMnemonic('c');
            //Add To Panel
//            effectsPanel.add(caps);
            effectsPanel.add(new JLabel());
            effectsPanel.add(new JLabel());
            centerPanel.add(effectsPanel);

            //Add components
            dl.add(sizePanel, BorderLayout.NORTH);
            dl.add(centerPanel);

            JButton ok = new JButton("Ok");
            ok.addActionListener((ae) -> {
                if(italic.isSelected())
                    font = new Font(fontscollection[fontlist.getSelectedIndex()], Font.ITALIC,slider.getValue());
                else if(bold.isSelected())
                    font = new Font(fontscollection[fontlist.getSelectedIndex()], Font.BOLD,slider.getValue());
                else
                    font = new Font(fontscollection[fontlist.getSelectedIndex()], Font.PLAIN,slider.getValue());             
                dl.dispose();
            });
            dl.add(ok, BorderLayout.SOUTH);

            JButton cancel = new JButton("Cancel");
            cancel.addActionListener((ae) -> {
                dl.dispose();
            });
            dl.add(cancel, BorderLayout.SOUTH);



            font = null;
            dl.setLocationRelativeTo(frame);
            dl.setVisible(true);

            return font;
        }
}
