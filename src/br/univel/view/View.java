package br.univel.view;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridBagLayout;
import javax.swing.JTabbedPane;
import java.awt.GridBagConstraints;
import javax.swing.JDesktopPane;
import javax.swing.JLabel;
import java.awt.Insets;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import java.awt.Color;

public class View extends JFrame {

	private JPanel contentPane;
	private JTextField txtporta;
	private JTextField txtNome;
	private JTextField textField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					View frame = new View();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public View() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{0, 0};
		gbl_contentPane.rowHeights = new int[]{0, 0};
		gbl_contentPane.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		GridBagConstraints gbc_tabbedPane = new GridBagConstraints();
		gbc_tabbedPane.fill = GridBagConstraints.BOTH;
		gbc_tabbedPane.gridx = 0;
		gbc_tabbedPane.gridy = 0;
		contentPane.add(tabbedPane, gbc_tabbedPane);
		
		JDesktopPane desktopPane = new JDesktopPane();
		tabbedPane.addTab("Servidor", null, desktopPane, null);
		GridBagLayout gbl_desktopPane = new GridBagLayout();
		gbl_desktopPane.columnWidths = new int[]{0, 25, 264, 0, 0};
		gbl_desktopPane.rowHeights = new int[]{0, 0, 0, 0, 0};
		gbl_desktopPane.columnWeights = new double[]{1.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
		gbl_desktopPane.rowWeights = new double[]{0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		desktopPane.setLayout(gbl_desktopPane);
		
		JLabel lblIp = new JLabel("IP");
		GridBagConstraints gbc_lblIp = new GridBagConstraints();
		gbc_lblIp.insets = new Insets(0, 0, 5, 5);
		gbc_lblIp.anchor = GridBagConstraints.EAST;
		gbc_lblIp.gridx = 1;
		gbc_lblIp.gridy = 1;
		desktopPane.add(lblIp, gbc_lblIp);
		
		JComboBox cmbIp = new JComboBox();
		GridBagConstraints gbc_cmbIp = new GridBagConstraints();
		gbc_cmbIp.fill = GridBagConstraints.HORIZONTAL;
		gbc_cmbIp.insets = new Insets(0, 0, 5, 5);
		gbc_cmbIp.gridx = 2;
		gbc_cmbIp.gridy = 1;
		desktopPane.add(cmbIp, gbc_cmbIp);
		
		JLabel lblPorta = new JLabel("PORTA");
		GridBagConstraints gbc_lblPorta = new GridBagConstraints();
		gbc_lblPorta.anchor = GridBagConstraints.EAST;
		gbc_lblPorta.insets = new Insets(0, 0, 5, 5);
		gbc_lblPorta.gridx = 1;
		gbc_lblPorta.gridy = 2;
		desktopPane.add(lblPorta, gbc_lblPorta);
		
		txtporta = new JTextField();
		GridBagConstraints gbc_txtporta = new GridBagConstraints();
		gbc_txtporta.insets = new Insets(0, 0, 5, 5);
		gbc_txtporta.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtporta.gridx = 2;
		gbc_txtporta.gridy = 2;
		desktopPane.add(txtporta, gbc_txtporta);
		txtporta.setColumns(10);
		
		JButton btnIniciar = new JButton("Iniciar");
		GridBagConstraints gbc_btnIniciar = new GridBagConstraints();
		gbc_btnIniciar.insets = new Insets(0, 0, 5, 0);
		gbc_btnIniciar.gridx = 3;
		gbc_btnIniciar.gridy = 2;
		desktopPane.add(btnIniciar, gbc_btnIniciar);
		
		JTextArea textArea = new JTextArea();
		textArea.setBackground(Color.GRAY);
		GridBagConstraints gbc_textArea = new GridBagConstraints();
		gbc_textArea.gridwidth = 4;
		gbc_textArea.insets = new Insets(0, 0, 0, 5);
		gbc_textArea.fill = GridBagConstraints.BOTH;
		gbc_textArea.gridx = 0;
		gbc_textArea.gridy = 3;
		desktopPane.add(textArea, gbc_textArea);
		
		JDesktopPane desktopPane_1 = new JDesktopPane();
		tabbedPane.addTab("Cliente", null, desktopPane_1, null);
		GridBagLayout gbl_desktopPane_1 = new GridBagLayout();
		gbl_desktopPane_1.columnWidths = new int[]{0, 0, 0, 0, 0};
		gbl_desktopPane_1.rowHeights = new int[]{0, 0, 0, 0, 0};
		gbl_desktopPane_1.columnWeights = new double[]{0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
		gbl_desktopPane_1.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		desktopPane_1.setLayout(gbl_desktopPane_1);
		
		JLabel lblNome = new JLabel("Nome");
		GridBagConstraints gbc_lblNome = new GridBagConstraints();
		gbc_lblNome.insets = new Insets(0, 0, 5, 5);
		gbc_lblNome.anchor = GridBagConstraints.EAST;
		gbc_lblNome.gridx = 1;
		gbc_lblNome.gridy = 1;
		desktopPane_1.add(lblNome, gbc_lblNome);
		
		txtNome = new JTextField();
		GridBagConstraints gbc_txtNome = new GridBagConstraints();
		gbc_txtNome.insets = new Insets(0, 0, 5, 5);
		gbc_txtNome.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtNome.gridx = 2;
		gbc_txtNome.gridy = 1;
		desktopPane_1.add(txtNome, gbc_txtNome);
		txtNome.setColumns(10);
		
		JLabel lblIp_1 = new JLabel("IP");
		GridBagConstraints gbc_lblIp_1 = new GridBagConstraints();
		gbc_lblIp_1.anchor = GridBagConstraints.EAST;
		gbc_lblIp_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblIp_1.gridx = 1;
		gbc_lblIp_1.gridy = 2;
		desktopPane_1.add(lblIp_1, gbc_lblIp_1);
		
		JComboBox cmbIpC = new JComboBox();
		GridBagConstraints gbc_cmbIpC = new GridBagConstraints();
		gbc_cmbIpC.fill = GridBagConstraints.HORIZONTAL;
		gbc_cmbIpC.insets = new Insets(0, 0, 5, 5);
		gbc_cmbIpC.gridx = 2;
		gbc_cmbIpC.gridy = 2;
		desktopPane_1.add(cmbIpC, gbc_cmbIpC);
		
		JLabel lblPorta_1 = new JLabel("Porta");
		GridBagConstraints gbc_lblPorta_1 = new GridBagConstraints();
		gbc_lblPorta_1.anchor = GridBagConstraints.EAST;
		gbc_lblPorta_1.insets = new Insets(0, 0, 0, 5);
		gbc_lblPorta_1.gridx = 1;
		gbc_lblPorta_1.gridy = 3;
		desktopPane_1.add(lblPorta_1, gbc_lblPorta_1);
		
		textField = new JTextField();
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.insets = new Insets(0, 0, 0, 5);
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridx = 2;
		gbc_textField.gridy = 3;
		desktopPane_1.add(textField, gbc_textField);
		textField.setColumns(10);
		
		JButton btnIniciar_1 = new JButton("Iniciar");
		GridBagConstraints gbc_btnIniciar_1 = new GridBagConstraints();
		gbc_btnIniciar_1.gridx = 3;
		gbc_btnIniciar_1.gridy = 3;
		desktopPane_1.add(btnIniciar_1, gbc_btnIniciar_1);
	}
}
