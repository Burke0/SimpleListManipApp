import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.io.*;
import java.util.Scanner;
import javax.swing.filechooser.FileNameExtensionFilter;

/*
 * The code is a Java application that provides a GUI to load, save, add and delete items in a list. 
 * It extends JPanel and implements ActionListener. 
 * The class has instance variables such as JList, DefaultListModel, JFileChooser, BufferedWriter, and File. 
 * In the constructor, the components are initialized, and their properties are set. Action listeners and action commands are added to components such as buttons and menus. 
 * Accelerator keys are also set for some menus.
 * In actionPerformed, different methods are called based on which button is pressed. 
 * storeList is called when the Save button is pressed to write the list model to a file. 
 * The main method creates a JFrame object and sets its properties.
 */

public class SimpleListManipApp extends JPanel implements ActionListener
{
	JList								list;
	DefaultListModel<String>	model;
	JFileChooser					chooser;
	BufferedWriter					bw;
	File 								file=null;
	int 								returnVal;

	public SimpleListManipApp()
	{
		model= 						new DefaultListModel<String>();
		list= 						new JList<String>(model);
		chooser=						new JFileChooser(".");
		JButton loadButton= 		new JButton("Load");
		JButton saveButton=		new JButton("Save");
		JButton saveAsButton=	new JButton("Save As");
		JButton addButton= 		new JButton("Add");
		JButton deleteButton= 	new JButton("Delete");
		JButton exitButton= 		new JButton("Exit");
		JPanel buttonPanel= 		new JPanel();
		JScrollPane scrollPane=	new JScrollPane();
		JMenuBar menuBar=			new JMenuBar();
		JMenu fileMenu=			new JMenu("File");
		JMenu itemMenu=			new JMenu("Item");
		JMenuItem load= 			new JMenuItem("Load");
		JMenuItem save=			new JMenuItem("Save");
		JMenuItem saveAs=			new JMenuItem("save As");
		JMenuItem newItem=		new JMenuItem("New");
		JMenuItem delete=			new JMenuItem("Delete");
		JMenuItem deleteAll=		new JMenuItem("delete All");



		setLayout(new BorderLayout());
		scrollPane.setViewportView(list);

		chooser.addChoosableFileFilter(new FileNameExtensionFilter("*.txt", "txt"));
		loadButton.addActionListener(this);
		saveButton.addActionListener(this);
		saveAsButton.addActionListener(this);
		addButton.addActionListener(this);
		deleteButton.addActionListener(this);
		exitButton.addActionListener(this);
		load.addActionListener(this);
		save.addActionListener(this);
		saveAs.addActionListener(this);
		newItem.addActionListener(this);
		delete.addActionListener(this);
		deleteAll.addActionListener(this);

		loadButton.setActionCommand("Load");
		saveButton.setActionCommand("Save");
		saveAsButton.setActionCommand("saveAs");
		addButton.setActionCommand("Add");
		deleteButton.setActionCommand("Delete");
		exitButton.setActionCommand("Exit");
		load.setActionCommand("Load");
		save.setActionCommand("Save");
		saveAs.setActionCommand("saveAs");
		newItem.setActionCommand("Add");
		delete.setActionCommand("Delete");
		deleteAll.setActionCommand("DeleteAll");

		load.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK));
		save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));
		saveAs.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, KeyEvent.CTRL_DOWN_MASK));
		newItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK));
		delete.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, KeyEvent.CTRL_DOWN_MASK));
		deleteAll.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, KeyEvent.CTRL_DOWN_MASK));

		buttonPanel.add(loadButton);
		buttonPanel.add(saveButton);
		buttonPanel.add(saveAsButton);
		buttonPanel.add(addButton);
		buttonPanel.add(deleteButton);
		buttonPanel.add(exitButton);
		fileMenu.add(load);
		fileMenu.add(save);
		fileMenu.add(saveAs);
		itemMenu.add(newItem);
		itemMenu.add(delete);
		itemMenu.add(deleteAll);
		menuBar.add(fileMenu);
		menuBar.add(itemMenu);

		add(menuBar, BorderLayout.NORTH);
		add(scrollPane,BorderLayout.CENTER);
		add(buttonPanel,BorderLayout.SOUTH);

	}

 	public void actionPerformed(ActionEvent e)
 	{
		if(e.getActionCommand().equals("Load"))
		{
			returnVal= chooser.showOpenDialog(this);
			if(returnVal==JFileChooser.APPROVE_OPTION)
			{
				try
				{
					file = chooser.getSelectedFile();
					Scanner sc= new Scanner(file);
					model.removeAllElements();
					while(sc.hasNextLine())
					{
						model.addElement(sc.nextLine());
					}
					sc.close();
				}
				catch(FileNotFoundException fnf)
				{
					JOptionPane.showMessageDialog(null, "Error Opening File");
				}
			}
		}
		if(e.getActionCommand().equals("Save"))
		{
			try
			{
				if(file!=null)
					storeList(model, file);
				else
				{
					returnVal= chooser.showSaveDialog(this);
					if(returnVal==JFileChooser.APPROVE_OPTION)
						file=chooser.getSelectedFile();
				}
			}
			catch(IOException ioe)
			{
				ioe.printStackTrace();
			}
		}
		if(e.getActionCommand().equals("saveAs"))
		{
			try
			{
				returnVal= chooser.showSaveDialog(this);
				if(returnVal==JFileChooser.APPROVE_OPTION)
					file=chooser.getSelectedFile();
				storeList(model, file);
			}
			catch(IOException ioe)
			{
				ioe.printStackTrace();
			}
		}
		if(e.getActionCommand().equals("Add"))
		{
			String temp= JOptionPane.showInputDialog("Please enter String");
			model.addElement(temp);
		}
		if(e.getActionCommand().equals("Delete"))
		{
			if(list.getSelectedIndices().length>0)
				for(int i=list.getSelectedIndices().length-1; i>=0; i--)
					model.removeElementAt(list.getSelectedIndices()[i]);
		}
		if(e.getActionCommand().equals("DeleteAll"))
			model.removeAllElements();
		if(e.getActionCommand().equals("Exit"))
			System.exit(0);
 	}

 	public void storeList(DefaultListModel model, File file) throws IOException
 	{
		bw = new BufferedWriter(new FileWriter(file, false));
		for(int i=0; i<model.getSize(); i++)
		{
			System.out.println(model.getElementAt(i));
			bw.write(model.getElementAt(i).toString());
			bw.newLine();
		}
		bw.close();
	}
 	public static void main(String[] args)
 	{
		JFrame frame = new JFrame("Word List Editor");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(new SimpleListManipApp());
		frame.setSize(500, 500);
		frame.setVisible(true);

  	}
}
