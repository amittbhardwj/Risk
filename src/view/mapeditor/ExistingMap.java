package view.mapeditor;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import model.MapNode;

import java.awt.GridLayout;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Vector;
import java.awt.event.ActionEvent;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JTable;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import java.awt.Color;
import java.awt.Font;

/**
 *Existing Map class opens the JFrame view for
 *choosing map file
 */
public class ExistingMap extends JFrame {

	private JPanel contentPane;
	private JTable table;
	private JTextField txtContinentName;
	private JTextField txtControlValue;
	private JTextField textField;
	private JTextField txtContinentName_1;
	private JTextField txtControlValue_1;
	private JComboBox comboBox;
	private JLabel lblName_1;
	private JTextField textField_1;
	private JTextField txtCountryName;
	private JLabel lblNeighbours;
	private JComboBox comboBox_1;
	private JButton btnAddCountry;
	private JLabel lblPickContinent;
	private JButton btnDeleteContinent;
	private JComboBox comboBox_2;
	private JButton btnDeleteCountry;
	private JComboBox comboBox_3;
	ArrayList<MapNode> existingMap = new ArrayList<MapNode>();

	/**
	 * Existing Map constructor calls initialize method of the class
	 */

	public ExistingMap(ArrayList<MapNode> map)
	{
		existingMap = map;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	public void initialize() {
		this.setLocationRelativeTo(null);
		this.setTitle("Existing Map");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(140, 140, 500, 340);
		this.setExtendedState(JFrame.MAXIMIZED_BOTH); 

		contentPane = new JPanel();
		contentPane.setBackground(Color.GRAY);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_contentPane.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_contentPane.columnWeights = new double[]{0.0, 0.0, 0.0, 1.0, 1.0, 0.0, 0.0, 0.0, 1.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);

		String[] column1 = {"Continents", "Control Value"};
		DefaultTableModel model1 = new DefaultTableModel(column1, 0);

		for (int i = 0; i < existingMap.size(); i++) {
			String[] contName = {existingMap.get(i).getContinentName(), Integer.toString(existingMap.get(i).getControlValue())};
			model1.addRow(contName);
		}
		JTable table1 = new JTable(model1);
		table1.setToolTipText("Map file displayed here");
		GridBagConstraints gbc_table1 = new GridBagConstraints();
		gbc_table1.gridwidth = 2;
		gbc_table1.gridheight = 2;
		gbc_table1.insets = new Insets(0, 0, 5, 0);
		gbc_table1.fill = GridBagConstraints.BOTH;
		gbc_table1.gridx = 0;
		gbc_table1.gridy = 0;
		contentPane.add(add(new JScrollPane(table1)), gbc_table1);


		String[] column2 = {"Countries"};
		DefaultTableModel model2 = new DefaultTableModel(column2, 0);
		JTable table2 = new JTable(model2);
		table2.setToolTipText("Map file displayed here");
		GridBagConstraints gbc_table2 = new GridBagConstraints();
		gbc_table2.gridwidth = 2;
		gbc_table2.gridheight = 2;
		gbc_table2.insets = new Insets(0, 0, 5, 0);
		gbc_table2.fill = GridBagConstraints.BOTH;
		gbc_table2.gridx = 2;
		gbc_table2.gridy = 0;
		contentPane.add(add(new JScrollPane(table2)), gbc_table2);


		String[] column3 = {"N Countries"};
		DefaultTableModel model3 = new DefaultTableModel(column3, 0);
		JTable table3 = new JTable(model3);
		table3.setToolTipText("Map file displayed here");
		GridBagConstraints gbc_table3 = new GridBagConstraints();
		gbc_table3.gridwidth = 2;
		gbc_table3.gridheight = 2;
		gbc_table3.insets = new Insets(0, 0, 5, 0);
		gbc_table3.fill = GridBagConstraints.BOTH;
		gbc_table3.gridx = 4;
		gbc_table3.gridy = 0;
		contentPane.add(add(new JScrollPane(table3)), gbc_table3);

		table1.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				model2.setRowCount(0);
				model3.setRowCount(0);
				String selectedCell = table1.getValueAt(table1.getSelectedRow(), table1.getSelectedColumn()).toString();
				for (int i = 0; i < existingMap.size(); i++) {
					if (selectedCell.compareTo(existingMap.get(i).getContinentName())==0) {
						for (int j = 0; j < existingMap.get(i).getCountries().length; j++) {
							String[] countryName = {existingMap.get(i).getCountries()[j].getCountryName()};
							model2.addRow(countryName);
						}
					}
				}
			}
		});

		table2.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				model3.setRowCount(0);
				String selectedCell1 = table1.getValueAt(table1.getSelectedRow(), table1.getSelectedColumn()).toString();
				String selectedCell2 = table2.getValueAt(table2.getSelectedRow(), table2.getSelectedColumn()).toString();
				for (int i = 0; i < existingMap.size(); i++) {
					if (selectedCell1.compareTo(existingMap.get(i).getContinentName())==0) {
						for (int j = 0; j < existingMap.get(i).getCountries().length; j++) {
							if (selectedCell2.compareTo(existingMap.get(i).getCountries()[j].getCountryName())==0) {

								for (int k = 0; k < existingMap.get(i).getCountries()[j].getNeighbourCountries().length; k++) {
									String[] countryInfo = {existingMap.get(i).getCountries()[j].getNeighbourCountries()[k].getCountryName()};
									model3.addRow(countryInfo);
								}
								break;
							}
						}
						break;
					}
				}
			}
		});

		JButton btnEdit = new JButton("Edit");
		GridBagConstraints gbc_btnEdit = new GridBagConstraints();
		gbc_btnEdit.insets = new Insets(0, 0, 5, 0);
		gbc_btnEdit.gridx = 15;
		gbc_btnEdit.gridy = 2;
		contentPane.add(btnEdit, gbc_btnEdit);

		JButton btnAddContinent = new JButton("Add Continent");
		btnAddContinent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		GridBagConstraints gbc_btnAddContinent = new GridBagConstraints();
		gbc_btnAddContinent.fill = GridBagConstraints.BOTH;
		gbc_btnAddContinent.insets = new Insets(0, 0, 5, 5);
		gbc_btnAddContinent.gridx = 2;
		gbc_btnAddContinent.gridy = 3;
		contentPane.add(btnAddContinent, gbc_btnAddContinent);

		btnAddCountry = new JButton("Add Country");
		GridBagConstraints gbc_btnAddCountry = new GridBagConstraints();
		gbc_btnAddCountry.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnAddCountry.insets = new Insets(0, 0, 5, 5);
		gbc_btnAddCountry.gridx = 9;
		gbc_btnAddCountry.gridy = 3;
		contentPane.add(btnAddCountry, gbc_btnAddCountry);

		JLabel lblName = new JLabel("Name");
		GridBagConstraints gbc_lblName = new GridBagConstraints();
		gbc_lblName.anchor = GridBagConstraints.EAST;
		gbc_lblName.insets = new Insets(0, 0, 5, 5);
		gbc_lblName.gridx = 1;
		gbc_lblName.gridy = 4;
		contentPane.add(lblName, gbc_lblName);

		txtContinentName_1 = new JTextField();
		txtContinentName_1.setForeground(Color.GRAY);
		txtContinentName_1.setFont(new Font("Tahoma", Font.ITALIC, 13));
		txtContinentName_1.setText("Continent Name...");
		GridBagConstraints gbc_txtContinentName_1 = new GridBagConstraints();
		gbc_txtContinentName_1.gridwidth = 2;
		gbc_txtContinentName_1.insets = new Insets(0, 0, 5, 5);
		gbc_txtContinentName_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtContinentName_1.gridx = 2;
		gbc_txtContinentName_1.gridy = 4;
		contentPane.add(txtContinentName_1, gbc_txtContinentName_1);
		txtContinentName_1.setColumns(10);

		lblPickContinent = new JLabel("Pick Continent");
		GridBagConstraints gbc_lblPickContinent = new GridBagConstraints();
		gbc_lblPickContinent.anchor = GridBagConstraints.EAST;
		gbc_lblPickContinent.insets = new Insets(0, 0, 5, 5);
		gbc_lblPickContinent.gridx = 8;
		gbc_lblPickContinent.gridy = 4;
		contentPane.add(lblPickContinent, gbc_lblPickContinent);

		comboBox = new JComboBox();
		comboBox.setToolTipText("Choose an existing continent...");
		GridBagConstraints gbc_comboBox = new GridBagConstraints();
		gbc_comboBox.gridwidth = 6;
		gbc_comboBox.insets = new Insets(0, 0, 5, 5);
		gbc_comboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox.gridx = 9;
		gbc_comboBox.gridy = 4;
		contentPane.add(comboBox, gbc_comboBox);

		JLabel lblValue = new JLabel("Value");
		GridBagConstraints gbc_lblValue = new GridBagConstraints();
		gbc_lblValue.anchor = GridBagConstraints.EAST;
		gbc_lblValue.insets = new Insets(0, 0, 5, 5);
		gbc_lblValue.gridx = 1;
		gbc_lblValue.gridy = 5;
		contentPane.add(lblValue, gbc_lblValue);

		txtControlValue_1 = new JTextField();
		txtControlValue_1.setForeground(Color.GRAY);
		txtControlValue_1.setFont(new Font("Tahoma", Font.ITALIC, 13));
		txtControlValue_1.setText("Control Value...");
		GridBagConstraints gbc_txtControlValue_1 = new GridBagConstraints();
		gbc_txtControlValue_1.gridwidth = 2;
		gbc_txtControlValue_1.insets = new Insets(0, 0, 5, 5);
		gbc_txtControlValue_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtControlValue_1.gridx = 2;
		gbc_txtControlValue_1.gridy = 5;
		contentPane.add(txtControlValue_1, gbc_txtControlValue_1);
		txtControlValue_1.setColumns(10);

		lblName_1 = new JLabel("Name");
		GridBagConstraints gbc_lblName_1 = new GridBagConstraints();
		gbc_lblName_1.anchor = GridBagConstraints.EAST;
		gbc_lblName_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblName_1.gridx = 8;
		gbc_lblName_1.gridy = 5;
		contentPane.add(lblName_1, gbc_lblName_1);

		txtCountryName = new JTextField();
		txtCountryName.setForeground(Color.GRAY);
		txtCountryName.setFont(new Font("Tahoma", Font.ITALIC, 13));
		txtCountryName.setText("Country name...");
		GridBagConstraints gbc_txtCountryName = new GridBagConstraints();
		gbc_txtCountryName.gridwidth = 6;
		gbc_txtCountryName.insets = new Insets(0, 0, 5, 5);
		gbc_txtCountryName.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtCountryName.gridx = 9;
		gbc_txtCountryName.gridy = 5;
		contentPane.add(txtCountryName, gbc_txtCountryName);
		txtCountryName.setColumns(10);

		btnDeleteContinent = new JButton("Delete Continent");
		GridBagConstraints gbc_btnDeleteContinent = new GridBagConstraints();
		gbc_btnDeleteContinent.insets = new Insets(0, 0, 5, 5);
		gbc_btnDeleteContinent.gridx = 2;
		gbc_btnDeleteContinent.gridy = 6;
		contentPane.add(btnDeleteContinent, gbc_btnDeleteContinent);

		comboBox_2 = new JComboBox();
		comboBox_2.setToolTipText("Select a continent to delete...");
		GridBagConstraints gbc_comboBox_2 = new GridBagConstraints();
		gbc_comboBox_2.insets = new Insets(0, 0, 5, 5);
		gbc_comboBox_2.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox_2.gridx = 3;
		gbc_comboBox_2.gridy = 6;
		contentPane.add(comboBox_2, gbc_comboBox_2);

		lblNeighbours = new JLabel("Neighbours");
		GridBagConstraints gbc_lblNeighbours = new GridBagConstraints();
		gbc_lblNeighbours.anchor = GridBagConstraints.EAST;
		gbc_lblNeighbours.insets = new Insets(0, 0, 5, 5);
		gbc_lblNeighbours.gridx = 8;
		gbc_lblNeighbours.gridy = 6;
		contentPane.add(lblNeighbours, gbc_lblNeighbours);

		comboBox_1 = new JComboBox();
		comboBox_1.setToolTipText("Select neighbours");
		GridBagConstraints gbc_comboBox_1 = new GridBagConstraints();
		gbc_comboBox_1.insets = new Insets(0, 0, 5, 5);
		gbc_comboBox_1.gridwidth = 6;
		gbc_comboBox_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox_1.gridx = 9;
		gbc_comboBox_1.gridy = 6;
		contentPane.add(comboBox_1, gbc_comboBox_1);

		btnDeleteCountry = new JButton("Delete Country");
		GridBagConstraints gbc_btnDeleteCountry = new GridBagConstraints();
		gbc_btnDeleteCountry.insets = new Insets(0, 0, 0, 5);
		gbc_btnDeleteCountry.gridx = 9;
		gbc_btnDeleteCountry.gridy = 7;
		contentPane.add(btnDeleteCountry, gbc_btnDeleteCountry);

		comboBox_3 = new JComboBox();
		comboBox_3.setToolTipText("Select a country to delete...");
		GridBagConstraints gbc_comboBox_3 = new GridBagConstraints();
		gbc_comboBox_3.insets = new Insets(0, 0, 0, 5);
		gbc_comboBox_3.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox_3.gridx = 10;
		gbc_comboBox_3.gridy = 7;
		contentPane.add(comboBox_3, gbc_comboBox_3);


	}

}
