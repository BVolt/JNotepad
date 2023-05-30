//
//  Name:       Johnson, Brenden
//  Project:    #6
//  Due:        5-12-2023
//  Course:     CS-2450-01-sp23
//  
//  Description:
//              A notepad application similar to windows notepad.
//

//package com.mycompany.jnotepad;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.event.*;
import javax.swing.filechooser.FileFilter;
import java.util.Date;
import java.text.SimpleDateFormat;



public class JNotepad implements ActionListener {
    private String filename;
    private Font font;
    private File currentFile;
    private boolean madechanges;
    private JFrame frame;
    private JMenuBar menubar;
    private JTextArea notepad;
    private JFileChooser filechooser;
    private JColorChooser colorchooser;
    private JPopupMenu popup;
    private JMenuItem popupcut, popupcopy, popuppaste, saveas, save;

    public JNotepad(String[] args) {
        //Initialize Variables
        if (args.length > 0) {
            filename = args[0];
        }
        font = new Font("Courier", Font.PLAIN, 12);
        frame = new JFrame(((filename != null) ? filename : "") + " - JNotepad");
        frame.setIconImage(new ImageIcon("JNotepad.png").getImage());
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
                if (madechanges == true) {
                    int resp = Dialogs.showCloseDialog(frame);
                    if (resp == JOptionPane.YES_OPTION) {
                        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    } else if (resp == 2) {
                        save.doClick();
                        if (madechanges == false) {
                            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        } else {
                            frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                            JOptionPane.showMessageDialog(frame, "Error While Saving!");
                        }
                    } else {
                        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                    }
                }
            }
        });

        filechooser = new JFileChooser();
        filechooser.addChoosableFileFilter(new FileFilter() {
            public String getDescription() {
                return "Text Files (*.txt)";
            }

            public boolean accept(File file) {
                if (file.isDirectory()) {
                    return true;
                }
                return file.getName().endsWith(".txt");
            }
        });

        filechooser.addChoosableFileFilter(new FileFilter() {
            public String getDescription() {
                return "Java Files (*.java)";
            }

            public boolean accept(File file) {
                if (file.isDirectory()) {
                    return true;
                }
                return file.getName().endsWith(".java");
            }
        });

        //JMenu Initialization
        initializeMenus();

        //Text Area Set up
        initializeNotepad();

        //Initialize Frame
        frame.setJMenuBar(menubar);
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }

    public void actionPerformed(ActionEvent ae) {
        switch (ae.getActionCommand()) {
            case "New":
                if (madechanges == true) {
                    int resp = Dialogs.showCloseDialog(frame);
                    if (resp == JOptionPane.YES_OPTION) {
                        currentFile = null;
                        notepad.setText("");
                        frame.setTitle(" - JNotepad");
                        madechanges = false;
                    } else if (resp == 2) {
                        save.doClick();
                        if (madechanges == false) {
                            currentFile = null;
                            notepad.setText("");
                            frame.setTitle(" - JNotepad");
                        } else {
                            JOptionPane.showMessageDialog(frame, "Error While Saving!");
                        }
                    }
                } else {
                    currentFile = null;
                    notepad.setText("");
                    frame.setTitle(" - JNotepad");
                    madechanges = false;
                }
                break;
            case "Open":
                filechooser.showOpenDialog(frame);
                try {
                    FileReader reader = new FileReader(filechooser.getSelectedFile().getAbsolutePath());
                    if (madechanges == true) {
                        int resp = Dialogs.showCloseDialog(frame);
                        if (resp == JOptionPane.YES_OPTION) {
                            notepad.read(reader, "");
                            currentFile = filechooser.getSelectedFile();
                            frame.setTitle(filechooser.getSelectedFile().getName() + " - JNotepad");
                            addDocListener();
                            madechanges = false;
                        } else if (resp == 2) {
                            save.doClick();
                            if (madechanges == false) {
                                notepad.read(reader, "");
                                currentFile = filechooser.getSelectedFile();
                                frame.setTitle(filechooser.getSelectedFile().getName() + " - JNotepad");
                                addDocListener();
                            } else {
                                JOptionPane.showMessageDialog(frame, "Error While Saving!");
                            }
                        }
                    } else {
                        notepad.read(reader, "");
                        currentFile = filechooser.getSelectedFile();
                        frame.setTitle(filechooser.getSelectedFile().getName() + " - JNotepad");
                        addDocListener();
                        madechanges = false;
                    }
                } catch (Exception e) {
                    System.out.println(e);
                }
                break;
            case "Save":
                if (currentFile == null)
                    saveas.doClick();
                else {
                    try {
                        FileWriter fw  = new FileWriter(currentFile);
                        fw.write(notepad.getText());
                        fw.close();
                        madechanges = false;
                    } catch (Exception e) {

                    }
                }
                break;
            case "Save As":
                int val = filechooser.showSaveDialog(frame);
                if (val == JFileChooser.APPROVE_OPTION) {
                    File file = filechooser.getSelectedFile();
                    try {
                        FileWriter fw = new FileWriter(file);
                        fw.write(notepad.getText());
                        fw.close();
                        currentFile = file;
                        frame.setTitle(filechooser.getSelectedFile().getName() + " - JNotepad");
                        madechanges = false;
                    } catch (Exception e) {

                    }
                }
                break;
//            case "Page Setup":
//                break;
//            case "Print":
//                break;
            case "Exit":
                if (madechanges == true) {
                    int resp = Dialogs.showCloseDialog(frame);
                    if (resp == JOptionPane.YES_OPTION) {
                        System.exit(0);
                    } else if (resp == 2) {
                        save.doClick();
                        if (madechanges == false) {
                            System.exit(0);
                        } else {
                            JOptionPane.showMessageDialog(frame, "Error While Saving!");
                        }
                    }
                } else {
                    System.exit(0);
                }
                break;
//            case "Undo":
//                break;
            case "Cut":
                notepad.cut();
                break;
            case "Copy":
                notepad.copy();
                break;
            case "Paste":
                notepad.paste();
                break;
            case "Delete":
                notepad.replaceSelection("");
                break;
            case "Find":
                Dialogs.showFindDialog(frame, (input, lastIndex) -> {
                    if (input != null) {
                        input = input.toLowerCase();
                        String text = notepad.getText().toLowerCase();
                        int startIndex = lastIndex + 1;
                        boolean found = false;
                        
                        outerloop:
                        for (int i = startIndex; i < text.length(); i++) {
                            if (text.charAt(i) == input.charAt(0)) {
                                for (int j = 0; j < input.length(); j++) {
                                    if (i + j >= text.length()) {
                                        break;
                                    }
                                    if (text.charAt(i + j) != input.charAt(j)) {
                                        break;
                                    }
                                    if (j == input.length() - 1) {
                                        notepad.select(i, i + j + 1);
                                        lastIndex = i;
                                        found = true;
                                        break outerloop;
                                    }
                                }
                            }
                        }
                        if (!found) {
                            lastIndex = -1;
                        }
                    }
                    return lastIndex;
                });
                break;
//            case "Find Next":
//                break;
//            case "Replace":
//                break;
            case "Go To":
                String linenum = JOptionPane.showInputDialog(frame, "Enter Line Number");
                try {
                    notepad.setCaretPosition(notepad.getDocument().getDefaultRootElement().getElement(Integer.parseInt(linenum) - 1).getStartOffset());
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(frame, "Invalid line number!");
                }
                break;
            case "Select All":
                notepad.selectAll();
                break;
            case "Time/Date":
                Date currentdate = new Date();
                SimpleDateFormat format = new SimpleDateFormat("hh:mm a MM/dd/yyyy");
                notepad.insert(format.format(currentdate), notepad.getCaretPosition());
                break;
            case "Word Wrap":
                notepad.setWrapStyleWord(!notepad.getWrapStyleWord());
                break;
            case "Font":
                Font newFont = Dialogs.showFontDialog(frame, "Select Font", font);
                if (newFont != null){
                    font = newFont;
                    notepad.setFont(font);
                }
                break;
            case "Background":
                Color newColor = colorchooser.showDialog(frame, "Choose Background", notepad.getBackground());
                notepad.setBackground(newColor);
                break;
            case "Foreground":
                Color newC = colorchooser.showDialog(frame, "Choose Background", notepad.getForeground());
                notepad.setForeground(newC);
                break;
//            case "Status Bar":
//                break;
//            case "View Help":
//                break;
            case "Extra Credits":
                JOptionPane.showMessageDialog(frame, "No Extra Credits Implemented");
                break;
            case "About Notepad":
                JOptionPane.showMessageDialog(frame, "Copyright (c) 2023 B. Johnson", "About Notepad", 0, new ImageIcon("JNotepad.png"));
                break;
        }
    }
    

    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(()-> new JNotepad(args));
    }
    
    public void initializeNotepad(){
        notepad = new JTextArea();
        if (filename != null){
            try {
                File specifiedFile = new File(filename);
                FileReader reader = new FileReader(specifiedFile);
                notepad.read(reader, "center");
                currentFile = specifiedFile;
            } catch (IOException e) {
                System.out.println("File not found!");
                currentFile = null;
                frame.setTitle(" - JNotepad");
            }
        }
        notepad.setOpaque(true);
        notepad.setLineWrap(true);
        notepad.setWrapStyleWord(true);
        notepad.setFont(font);
        JScrollPane scrollpane = new JScrollPane(notepad);
        frame.add(scrollpane);
        
        popup = new JPopupMenu();
        popupcut = new JMenuItem("Cut",'t');
        popupcopy = new JMenuItem("Copy", 'C');
        popuppaste = new JMenuItem("Paste", 'P');
        popupcut.addActionListener(this);
        popupcopy.addActionListener(this);
        popuppaste.addActionListener(this);
        popup.add(popupcut);
        popup.add(popupcopy);
        popup.add(popuppaste);
        notepad.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent e){
                if(e.getButton() == MouseEvent.BUTTON3)
                    popup.show(frame,e.getX(), e.getY() );
            }
        });
        notepad.getDocument().addDocumentListener(new DocumentListener(){
            public void removeUpdate(DocumentEvent e){
                madechanges = true;
            }
            public void insertUpdate(DocumentEvent e){
                madechanges = true;
            }
            public void changedUpdate(DocumentEvent e){
                madechanges = true;
            }
        });
    }
    
    public void initializeMenus(){
        JMenu file, edit, format, color, view, help;
        JMenuItem 
                psetup, print, undo, fnext, replace, stbar, vhelp, ecredits,
                nw, open, exit, cut, copy, paste, delete, find, gto, selectall, td, wrap, 
                fnt, bground, fground, about;
        menubar = new JMenuBar();
        file = new JMenu("File");
        edit = new JMenu("Edit");
        format = new JMenu("Format");
        color = new JMenu("Color");
        view = new JMenu("View");
        help = new JMenu("Help");
        nw = new JMenuItem("New", 'N');
        open = new JMenuItem("Open", 'O');
        save = new JMenuItem("Save", 'S');
        saveas = new JMenuItem("Save As", 'A');
        psetup = new JMenuItem("Page Setup",'u');
        print = new JMenuItem("Print", 'P');
        exit = new JMenuItem("Exit", 'x');
        undo = new JMenuItem("Undo", 'U');
        cut = new JMenuItem("Cut",'t');
        copy = new JMenuItem("Copy", 'C');
        paste = new JMenuItem("Paste", 'P');
        delete = new JMenuItem("Delete", 'l');
        find = new JMenuItem("Find", 'F');
        fnext = new JMenuItem("Find Next", 'N');
        replace = new JMenuItem("Replace", 'R');
        gto = new JMenuItem("Go To", 'G');
        selectall = new JMenuItem("Select All", 'A');
        td = new JMenuItem("Time/Date", 'D');
        wrap = new JMenuItem("Word Wrap", 'W');
        fnt = new JMenuItem("Font", 'F');
        bground = new JMenuItem("Background", 'B');
        fground = new JMenuItem("Foreground", 'F');
        stbar = new JMenuItem("Status Bar", 'S');
        vhelp = new JMenuItem("View Help", 'H');
        ecredits = new JMenuItem("Extra Credits", 'x');
        about = new JMenuItem("About Notepad", 'A');
        
        file.setMnemonic('F');
        edit.setMnemonic('E');
        format.setMnemonic('o');
        color.setMnemonic('C');
        view.setMnemonic('V');
        help.setMnemonic('H');
        
        nw.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_MASK));
        open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_MASK));
        save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_MASK));
        print.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, KeyEvent.CTRL_MASK));
        cut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, KeyEvent.CTRL_MASK));
        copy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_MASK));
        paste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.CTRL_MASK));
        delete.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
        find.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, KeyEvent.CTRL_MASK));
        replace.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, KeyEvent.CTRL_MASK));
        gto.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, KeyEvent.CTRL_MASK));
        selectall.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, KeyEvent.CTRL_MASK));
        td.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0));
        
        
        
        nw.addActionListener(this);
        open.addActionListener(this);
        save.addActionListener(this);
        saveas.addActionListener(this);
        psetup.addActionListener(this);
        print.addActionListener(this);
        exit.addActionListener(this);
        undo.addActionListener(this);
        cut.addActionListener(this);
        copy.addActionListener(this);
        paste.addActionListener(this);
        delete.addActionListener(this);
        find.addActionListener(this);
        fnext.addActionListener(this);
        replace.addActionListener(this);
        gto.addActionListener(this);
        selectall.addActionListener(this);
        td.addActionListener(this);
        wrap.addActionListener(this);
        fnt.addActionListener(this);
        bground.addActionListener(this);
        fground.addActionListener(this);
        stbar.addActionListener(this);
        vhelp.addActionListener(this);
        ecredits.addActionListener(this);
        about.addActionListener(this);
        
        file.add(nw);
        file.add(open);
        file.add(save);
        file.add(saveas);
        file.addSeparator();
        file.add(psetup);
        file.add(print);
        file.addSeparator();
        file.add(exit);
        edit.add(undo);
        edit.addSeparator();
        edit.add(cut);
        edit.add(copy);
        edit.add(paste);
        edit.add(delete);
        edit.addSeparator();
        edit.add(find);
        edit.add(fnext);
        edit.add(replace);
        edit.add(gto);
        edit.addSeparator();
        edit.add(selectall);
        edit.add(td);
        format.add(wrap);
        format.add(fnt);
        format.add(color);
        color.add(bground);
        color.add(fground);
        view.add(stbar);
        help.add(vhelp);
        help.add(ecredits);
        help.addSeparator();
        help.add(about);
        menubar.add(file);
        menubar.add(edit);
        menubar.add(format);
        menubar.add(view);
        menubar.add(help);
        
        //Disable unused extra credit menu items
        psetup.setEnabled(false);
        print.setEnabled(false);
        undo.setEnabled(false);
        fnext.setEnabled(false);
        replace.setEnabled(false);
        stbar.setEnabled(false);
        vhelp.setEnabled(false);
        ecredits.setEnabled(false);
    }
    
    public void addDocListener(){
        notepad.getDocument().addDocumentListener(new DocumentListener(){
            public void removeUpdate(DocumentEvent e){
                madechanges = true;
            }
            public void insertUpdate(DocumentEvent e){
                madechanges = true;
            }
            public void changedUpdate(DocumentEvent e){
                madechanges = true;
            }
        });
    }
}
