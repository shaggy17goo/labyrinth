import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class Gui extends JFrame implements WindowListener,ActionListener {

    JMenuBar menuBar;
    JMenu menu;
    JRadioButtonMenuItem depthButton, divisionButton, autorskiButton, primsButton, graphButton, smallButton, mediumButton, largeButton;
    public int state=1,size=2;
    private boolean clicked=false, loaded=false;
    public String filePath1;

    public JPanel panel;

    /**
     * Constructor handles all important data for us
     */
    public Gui(){
        /**
         * Name of the window
         */
        super("labyrinth");
        setLayout(new FlowLayout());
        addWindowListener(this);
        panel=new MyGraphics();
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(new Dimension(700,Data.windowY+50));
        this.setResizable(false);

        /**
         * Button generating new labyrinth - has to be clicked if we changed the algorithm type
         */
        JButton generateButton=new JButton("Generate new labyrinth");
        generateButton.addActionListener(this);
        this.add(generateButton);

        generateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                if(state==4) Data.generateGraphLabyrinth(size);
                else {
                    do {
                        Data.generateNewLabyrinth(state,size);
                    }
                    while (!Data.labyrinth.normalSolver());
                }
                clicked=false;
                panel.revalidate();
                panel.repaint();
            }
        });

        /**
         * Saves the labyrinth to a file
         */
        JButton saveButton=new JButton("Save this labyrinth");
        saveButton.addActionListener(this);
        this.add(saveButton);
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                if(state!=4) {
                    String filePath = JOptionPane.showInputDialog("Input the name of the file");//pop up input window
                    if (filePath != null) {
                        try {
                            if (filePath.equals("") || filePath.equals("null") ||
                                !(filePath.substring(filePath.length()-4).equals(".txt") ||
                                filePath.substring(filePath.length()-4).equals(".bmp"))) throw new NotAProperFileNameException();
                            Data.saveLabyrinth(filePath);
                            JOptionPane.showMessageDialog(panel, "Labyrinth saved as " + filePath + " !");//pop up text
                            clicked = false;
                        } catch (IOException ioe) {
                            JOptionPane.showMessageDialog(panel, ioe.getMessage());
                        }
                    }
                }

                else  {
                    String filePath = JOptionPane.showInputDialog("Input the name of the file");//pop up input window
                    if (filePath != null) {
                        try {
                            if (filePath.equals("") || filePath.equals("null")  || !filePath.substring(filePath.length()-4).equals(".txt")) throw new NotAProperFileNameException();
                            Data.saveLabyrinth(filePath);
                            JOptionPane.showMessageDialog(panel, "Labyrinth saved as " + filePath + " !");//pop up text
                            clicked = false;
                        } catch (IOException ioe) {
                            JOptionPane.showMessageDialog(panel, ioe.getMessage());
                        }
                    }
                }//pop up text

            }
        });

        /**
         * Button loading the labyrinth from a inputted file path
         */
        JButton loadButton=new JButton("Load the labyrinth from file");
        loadButton.addActionListener(this);
        this.add(loadButton);
        loadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                    String filePath = JOptionPane.showInputDialog("Input the path to the labyrinth file");//pop up input window
                    if (filePath != null) {
                        try {
                            if (filePath.equals("") || filePath.equals("null") ||
                                !(filePath.substring(filePath.length()-4).equals(".txt") ||
                                filePath.substring(filePath.length()-4).equals(".bmp"))) throw new NotAProperFileNameException();
                            Data.loadLabyrinth(filePath);
                            clicked = false;
                            panel.revalidate();
                            panel.repaint();
                            loaded=true;
                            filePath1=filePath;


                        } catch (IOException ioe) {
                            JOptionPane.showMessageDialog(panel, ioe.getMessage());
                        }
                    }

            }
        });
        /**
         * Button which displays the start-finish path
         */
        JButton findPath=new JButton("Show path");
        findPath.addActionListener(this);
        this.add(findPath);
        findPath.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                if(!clicked) {
                    Data.path=true;
                    clicked=true;
                }
                else {
                    Data.path=false;
                    clicked=false;
                }
                panel.revalidate();
                panel.repaint();
            }
        });
        /**
         * Creating a menu bar
         */
        menuBar = new JMenuBar();
        menu = new JMenu("Algorithms");
        menu.setMnemonic(KeyEvent.VK_A);
        menu.getAccessibleContext().setAccessibleDescription(
                "Algorithms menu");
        menuBar.add(menu);
        menu.addSeparator();
        /**
         * Group of buttons by which we change the labyrinth generating algorithm type and size of it
         */
            /**
             * First one
             */
            ButtonGroup group = new ButtonGroup();
            depthButton = new JRadioButtonMenuItem("Depth first search algorithm");
            depthButton.setSelected(true);
            depthButton.setMnemonic(KeyEvent.VK_R);
            group.add(depthButton);
            menu.add(depthButton);
            depthButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    state=1;
                }
            });
            /**
             * Second one
             */
            divisionButton = new JRadioButtonMenuItem("Recursive division algorithm");
            divisionButton.setMnemonic(KeyEvent.VK_O);
            group.add(divisionButton);
            menu.add(divisionButton);
            divisionButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    state=0;
                }
            });
            /**
             * Third one
             */
            autorskiButton = new JRadioButtonMenuItem("Easy peasy lemon squeezy algorithm");
            autorskiButton.setMnemonic(KeyEvent.VK_O);
            group.add(autorskiButton);
            menu.add(autorskiButton);
            autorskiButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    state=2;
                }
            });
            /**
             * Forth one
             */
            primsButton = new JRadioButtonMenuItem("Prim's algorithm");
            primsButton.setMnemonic(KeyEvent.VK_O);
            group.add(primsButton);
            menu.add(primsButton);
            primsButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    state=3;
                }
            });
            /**
             * Fifth one
             */
            graphButton = new JRadioButtonMenuItem("Recursive backtracker algorithm");
            graphButton.setMnemonic(KeyEvent.VK_O);
            group.add(graphButton);
            menu.add(graphButton);
            graphButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    state=4;
                }
            });
        menu.addSeparator();
            /**
             * Small one
             */
            ButtonGroup group2 = new ButtonGroup();
            smallButton = new JRadioButtonMenuItem("Small");
            smallButton.setMnemonic(KeyEvent.VK_R);
            group2.add(smallButton);
            menu.add(smallButton);
            smallButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    size=0;
                }
            });
            /**
             * Medium one
             */
            mediumButton = new JRadioButtonMenuItem("Medium");
            mediumButton.setMnemonic(KeyEvent.VK_O);
            //mediumButton.setSelected(true);
            group2.add(mediumButton);
            menu.add(mediumButton);
            mediumButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    size=1;
                }
            });
            /**
             * Third one
             */
            largeButton = new JRadioButtonMenuItem("Large");
            largeButton.setMnemonic(KeyEvent.VK_O);
            largeButton.setSelected(true);
            group2.add(largeButton);
            menu.add(largeButton);
            largeButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    size=2;
                }
            });
        /**
         * Adding all of the components to the frame
         */
        this.setJMenuBar(menuBar);
        add(panel);


        this.setVisible(true);
    }
    public void actionPerformed(ActionEvent e) {

        if(state!=4)this.setSize(new Dimension(700,Data.windowY+50));
        else this.setSize(new Dimension(700,Data.windowY+110));
        if(loaded) {
            JOptionPane.showMessageDialog(panel, "Labyrinth loaded from " + filePath1 + " !");//pop up text
            loaded=false;
        }
    }

    @Override
    public void windowOpened(WindowEvent windowEvent) {

    }

    @Override
    public void windowClosing(WindowEvent windowEvent) {
        dispose();
        System.exit(0);
    }

    @Override
    public void windowClosed(WindowEvent windowEvent) {

    }

    @Override
    public void windowIconified(WindowEvent windowEvent) {

    }

    @Override
    public void windowDeiconified(WindowEvent windowEvent) {

    }

    @Override
    public void windowActivated(WindowEvent windowEvent) {

    }

    @Override
    public void windowDeactivated(WindowEvent windowEvent) {

    }
}
