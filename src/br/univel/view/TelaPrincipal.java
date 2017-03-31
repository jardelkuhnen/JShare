package br.univel.view;

import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import br.univel.jshare.comum.Arquivo;
import br.univel.jshare.comum.Cliente;
import br.univel.jshare.comum.IServer;
import br.univel.jshare.enun.TipoFiltro;

public class TelaPrincipal extends JFrame implements IServer {

	private JPanel contentPane;
	private JTextField txtNomeCliente;
	private JTextField txtPorta;
	private JTable table;
	private JTextField txtPesquisa;
	private JButton btnConectar;
	private JTextField txtIpServidor;
	private IServer servidor;
	private Registry registry;
	private Cliente cliente;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TelaPrincipal frame = new TelaPrincipal();
					frame.setVisible(true);
					frame.setLocationRelativeTo(null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public TelaPrincipal() {
		setTitle("JShare");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 647, 495);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[] { 137, 216, 89, 0, 69, 99, 0 };
		gbl_contentPane.rowHeights = new int[] { 23, 20, 23, 22, 20, 0, 0 };
		gbl_contentPane.columnWeights = new double[] { 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		gbl_contentPane.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE };
		contentPane.setLayout(gbl_contentPane);

		JLabel lblNome = new JLabel("Nome");
		GridBagConstraints gbc_lblNome = new GridBagConstraints();
		gbc_lblNome.anchor = GridBagConstraints.EAST;
		gbc_lblNome.insets = new Insets(0, 0, 5, 5);
		gbc_lblNome.gridx = 0;
		gbc_lblNome.gridy = 0;
		contentPane.add(lblNome, gbc_lblNome);

		txtNomeCliente = new JTextField();
		txtNomeCliente.setText("Jardel");
		GridBagConstraints gbc_txtNomeCliente = new GridBagConstraints();
		gbc_txtNomeCliente.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtNomeCliente.insets = new Insets(0, 0, 5, 5);
		gbc_txtNomeCliente.gridwidth = 4;
		gbc_txtNomeCliente.gridx = 1;
		gbc_txtNomeCliente.gridy = 0;
		contentPane.add(txtNomeCliente, gbc_txtNomeCliente);
		txtNomeCliente.setColumns(10);

		JLabel lblIp = new JLabel("Ip Servidor");
		GridBagConstraints gbc_lblIp = new GridBagConstraints();
		gbc_lblIp.anchor = GridBagConstraints.EAST;
		gbc_lblIp.insets = new Insets(0, 0, 5, 5);
		gbc_lblIp.gridx = 0;
		gbc_lblIp.gridy = 1;
		contentPane.add(lblIp, gbc_lblIp);

		txtIpServidor = new JTextField();
		GridBagConstraints gbc_txtIpServidor = new GridBagConstraints();
		gbc_txtIpServidor.gridwidth = 2;
		gbc_txtIpServidor.insets = new Insets(0, 0, 5, 5);
		gbc_txtIpServidor.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtIpServidor.gridx = 1;
		gbc_txtIpServidor.gridy = 1;
		contentPane.add(txtIpServidor, gbc_txtIpServidor);
		txtIpServidor.setColumns(10);

		JLabel lblPorta = new JLabel("Porta Servidor");
		GridBagConstraints gbc_lblPorta = new GridBagConstraints();
		gbc_lblPorta.anchor = GridBagConstraints.EAST;
		gbc_lblPorta.insets = new Insets(0, 0, 5, 5);
		gbc_lblPorta.gridx = 4;
		gbc_lblPorta.gridy = 1;
		contentPane.add(lblPorta, gbc_lblPorta);

		txtPorta = new JTextField();
		txtPorta.setText("1818");
		GridBagConstraints gbc_txtPorta = new GridBagConstraints();
		gbc_txtPorta.anchor = GridBagConstraints.NORTH;
		gbc_txtPorta.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtPorta.insets = new Insets(0, 0, 5, 0);
		gbc_txtPorta.gridx = 5;
		gbc_txtPorta.gridy = 1;
		contentPane.add(txtPorta, gbc_txtPorta);
		txtPorta.setColumns(10);

		JButton btnLigarServidor = new JButton("Vou ser o Servidor");
		btnLigarServidor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				new ConfigServidor().setVisible(true);

			}
		});
		GridBagConstraints gbc_btnLigarServidor = new GridBagConstraints();
		gbc_btnLigarServidor.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnLigarServidor.insets = new Insets(0, 0, 5, 5);
		gbc_btnLigarServidor.gridx = 0;
		gbc_btnLigarServidor.gridy = 2;
		contentPane.add(btnLigarServidor, gbc_btnLigarServidor);
		
				btnConectar = new JButton("Conectar");
				btnConectar.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {

						conectarServidor();

					}
				});
				GridBagConstraints gbc_btnConectar = new GridBagConstraints();
				gbc_btnConectar.fill = GridBagConstraints.HORIZONTAL;
				gbc_btnConectar.insets = new Insets(0, 0, 5, 0);
				gbc_btnConectar.gridx = 5;
				gbc_btnConectar.gridy = 2;
				contentPane.add(btnConectar, gbc_btnConectar);

		JLabel lblPesquisa = new JLabel("Pesquisa");
		GridBagConstraints gbc_lblPesquisa = new GridBagConstraints();
		gbc_lblPesquisa.anchor = GridBagConstraints.EAST;
		gbc_lblPesquisa.insets = new Insets(0, 0, 5, 5);
		gbc_lblPesquisa.gridx = 0;
		gbc_lblPesquisa.gridy = 3;
		contentPane.add(lblPesquisa, gbc_lblPesquisa);

		txtPesquisa = new JTextField();
		GridBagConstraints gbc_txtPesquisa = new GridBagConstraints();
		gbc_txtPesquisa.gridwidth = 2;
		gbc_txtPesquisa.insets = new Insets(0, 0, 5, 5);
		gbc_txtPesquisa.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtPesquisa.gridx = 1;
		gbc_txtPesquisa.gridy = 3;
		contentPane.add(txtPesquisa, gbc_txtPesquisa);
		txtPesquisa.setColumns(10);

		JLabel lblFiltro = new JLabel("Filtro");
		GridBagConstraints gbc_lblFiltro = new GridBagConstraints();
		gbc_lblFiltro.insets = new Insets(0, 0, 5, 5);
		gbc_lblFiltro.anchor = GridBagConstraints.EAST;
		gbc_lblFiltro.gridx = 3;
		gbc_lblFiltro.gridy = 3;
		contentPane.add(lblFiltro, gbc_lblFiltro);

		JComboBox cmbFiltros = new JComboBox(TipoFiltro.values());
		GridBagConstraints gbc_cmbFiltros = new GridBagConstraints();
		gbc_cmbFiltros.insets = new Insets(0, 0, 5, 5);
		gbc_cmbFiltros.fill = GridBagConstraints.HORIZONTAL;
		gbc_cmbFiltros.gridx = 4;
		gbc_cmbFiltros.gridy = 3;
		contentPane.add(cmbFiltros, gbc_cmbFiltros);

		JButton btnPesquisar = new JButton("Pesquisar");
		GridBagConstraints gbc_btnPesquisar = new GridBagConstraints();
		gbc_btnPesquisar.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnPesquisar.insets = new Insets(0, 0, 5, 0);
		gbc_btnPesquisar.gridx = 5;
		gbc_btnPesquisar.gridy = 3;
		contentPane.add(btnPesquisar, gbc_btnPesquisar);

		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.gridheight = 2;
		gbc_scrollPane.gridwidth = 6;
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 4;
		contentPane.add(scrollPane, gbc_scrollPane);

		table = new JTable();
		scrollPane.setViewportView(table);

		configuraTela();
	}

	protected void conectarServidor() {

		String meuNome = txtNomeCliente.getText().trim();
		if (meuNome.length() == 0) {
			JOptionPane.showMessageDialog(this, "VocÃª precisa digitar um nome!");
			return;
		}

		String host = txtIpServidor.getText().trim();
		if (!host.matches("[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}")) {
			JOptionPane.showMessageDialog(this, "O endereço ip parece ser inválido!");
			return;
		}

		String strPorta = txtPorta.getText().trim();
		if (!strPorta.matches("[0-9]+") || strPorta.length() > 5) {
			JOptionPane.showMessageDialog(this, "A porta deve ser um valor máximo de no máximo 5 dígitos!");
			return;
		}

		int intPorta = Integer.parseInt(strPorta);

		try {
			registry = LocateRegistry.getRegistry(host, intPorta);

			servidor = (IServer) registry.lookup(IServer.NOME_SERVICO);

			cliente = (Cliente) UnicastRemoteObject.exportObject(this, 0);

			servidor.registrarCliente(cliente);


		} catch (Exception e) {
			e.printStackTrace();
		}

		btnConectar.setEnabled(false);
		txtIpServidor.setEditable(false);
		txtNomeCliente.setEditable(false);
		txtPorta.setEditable(false);
	}

	private void configuraTela() {

	}

	@Override
	public void registrarCliente(Cliente c) throws RemoteException {

	}

	@Override
	public void publicarListaArquivos(Cliente c, List<Arquivo> lista) throws RemoteException {

	}

	@Override
	public Map<Cliente, List<Arquivo>> procurarArquivo(String query, TipoFiltro tipoFiltro, String filtro)
			throws RemoteException {
		return null;
	}

	@Override
	public byte[] baixarArquivo(Cliente cli, Arquivo arq) throws RemoteException {
		return null;
	}

	@Override
	public void desconectar(Cliente c) throws RemoteException {

	}

}
