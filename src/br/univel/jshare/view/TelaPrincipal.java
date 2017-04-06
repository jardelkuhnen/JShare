package br.univel.jshare.view;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.TrayIcon.MessageType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Pattern;

import javax.swing.DefaultComboBoxModel;
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
import br.univel.jshare.comum.TipoFiltro;
import br.univel.jshare.model.ResultadoModel;
import br.univel.jshare.utils.MethodUtils;

public class TelaPrincipal extends JFrame implements IServer, Serializable {

	private JPanel contentPane;
	private JTextField txtNomeCliente;
	private JTextField txtMinhaPorta;
	private JTable table;
	private JTextField txtPesquisa;
	private JButton btnConectar;
	private JTextField txtIpServidor;
	private IServer servidor;
	private IServer clienteServ;
	private Registry registryServ;
	private Registry registryClient;
	private JTextField txtPortaServidor;
	private long idCliente = 0;
	private List<Cliente> clientes;
	private File file;
	private HashMap<Cliente, List<Arquivo>> mapaclientesArq;
	private List<Arquivo> listaArqs;
	private List<Arquivo> resultArqs;
	private JTextField txtMeuIp;
	private JButton btnDesconectar;
	private JButton btndesligarServ;
	private JButton btnPesquisar;
	private JButton btnDownload;
	private JTextField txtValorFiltro;
	private JComboBox cmbFiltros;

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
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 647, 495);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[] { 63, 186, 89, 46, 92, 99, 0 };
		gbl_contentPane.rowHeights = new int[] { 20, 0, 23, 22, 0, 20, 0, 0 };
		gbl_contentPane.columnWeights = new double[] { 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		gbl_contentPane.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE };
		contentPane.setLayout(gbl_contentPane);

		JLabel lblIp = new JLabel("Meu Ip Servidor");
		GridBagConstraints gbc_lblIp = new GridBagConstraints();
		gbc_lblIp.anchor = GridBagConstraints.EAST;
		gbc_lblIp.insets = new Insets(0, 0, 5, 5);
		gbc_lblIp.gridx = 0;
		gbc_lblIp.gridy = 0;
		contentPane.add(lblIp, gbc_lblIp);

		txtMeuIp = new JTextField();
		txtMeuIp.setText(getMeuIp());
		GridBagConstraints gbc_txtMeuIp = new GridBagConstraints();
		gbc_txtMeuIp.insets = new Insets(0, 0, 5, 5);
		gbc_txtMeuIp.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtMeuIp.gridx = 1;
		gbc_txtMeuIp.gridy = 0;
		contentPane.add(txtMeuIp, gbc_txtMeuIp);
		txtMeuIp.setColumns(10);

		JLabel lblPorta = new JLabel("Minha Porta Servidor");
		GridBagConstraints gbc_lblPorta = new GridBagConstraints();
		gbc_lblPorta.anchor = GridBagConstraints.EAST;
		gbc_lblPorta.insets = new Insets(0, 0, 5, 5);
		gbc_lblPorta.gridx = 2;
		gbc_lblPorta.gridy = 0;
		contentPane.add(lblPorta, gbc_lblPorta);

		txtMinhaPorta = new JTextField();
		txtMinhaPorta.setText("1818");
		GridBagConstraints gbc_txtMinhaPorta = new GridBagConstraints();
		gbc_txtMinhaPorta.anchor = GridBagConstraints.NORTH;
		gbc_txtMinhaPorta.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtMinhaPorta.insets = new Insets(0, 0, 5, 5);
		gbc_txtMinhaPorta.gridx = 3;
		gbc_txtMinhaPorta.gridy = 0;
		contentPane.add(txtMinhaPorta, gbc_txtMinhaPorta);
		txtMinhaPorta.setColumns(10);

		JButton btnLigarServidor = new JButton("Ligar Servidor");
		btnLigarServidor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				// Begining RMI
				iniciarRMI();
				btnLigarServidor.setEnabled(false);
				txtMinhaPorta.setEditable(false);
				txtMeuIp.setEnabled(false);
				btndesligarServ.setEnabled(true);

			}
		});
		GridBagConstraints gbc_btnLigarServidor = new GridBagConstraints();
		gbc_btnLigarServidor.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnLigarServidor.insets = new Insets(0, 0, 5, 5);
		gbc_btnLigarServidor.gridx = 4;
		gbc_btnLigarServidor.gridy = 0;
		contentPane.add(btnLigarServidor, gbc_btnLigarServidor);

		btndesligarServ = new JButton("Desligar Servidor");
		btndesligarServ.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				desligarServidor();
				btnLigarServidor.setEnabled(true);
				btndesligarServ.setEnabled(false);

			}
		});
		GridBagConstraints gbc_btndesligarServ = new GridBagConstraints();
		gbc_btndesligarServ.fill = GridBagConstraints.HORIZONTAL;
		gbc_btndesligarServ.insets = new Insets(0, 0, 5, 0);
		gbc_btndesligarServ.gridx = 5;
		gbc_btndesligarServ.gridy = 0;
		contentPane.add(btndesligarServ, gbc_btndesligarServ);

		JLabel lblNome = new JLabel("Nome");
		GridBagConstraints gbc_lblNome = new GridBagConstraints();
		gbc_lblNome.anchor = GridBagConstraints.EAST;
		gbc_lblNome.insets = new Insets(0, 0, 5, 5);
		gbc_lblNome.gridx = 0;
		gbc_lblNome.gridy = 1;
		contentPane.add(lblNome, gbc_lblNome);

		txtNomeCliente = new JTextField();
		txtNomeCliente.setText("Jardel");
		GridBagConstraints gbc_txtNomeCliente = new GridBagConstraints();
		gbc_txtNomeCliente.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtNomeCliente.insets = new Insets(0, 0, 5, 5);
		gbc_txtNomeCliente.gridwidth = 3;
		gbc_txtNomeCliente.gridx = 1;
		gbc_txtNomeCliente.gridy = 1;
		contentPane.add(txtNomeCliente, gbc_txtNomeCliente);
		txtNomeCliente.setColumns(10);

		JLabel lblSeuIp = new JLabel("Ip Servidor");
		GridBagConstraints gbc_lblSeuIp = new GridBagConstraints();
		gbc_lblSeuIp.anchor = GridBagConstraints.EAST;
		gbc_lblSeuIp.insets = new Insets(0, 0, 5, 5);
		gbc_lblSeuIp.gridx = 0;
		gbc_lblSeuIp.gridy = 2;
		contentPane.add(lblSeuIp, gbc_lblSeuIp);

		txtIpServidor = new JTextField();
		txtIpServidor.setText(getMeuIp());
		GridBagConstraints gbc_txtIpCliente = new GridBagConstraints();
		gbc_txtIpCliente.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtIpCliente.insets = new Insets(0, 0, 5, 5);
		gbc_txtIpCliente.gridx = 1;
		gbc_txtIpCliente.gridy = 2;
		contentPane.add(txtIpServidor, gbc_txtIpCliente);
		txtIpServidor.setColumns(10);

		JLabel lblSuaPorta = new JLabel("Porta Servidor");
		GridBagConstraints gbc_lblSuaPorta = new GridBagConstraints();
		gbc_lblSuaPorta.anchor = GridBagConstraints.EAST;
		gbc_lblSuaPorta.insets = new Insets(0, 0, 5, 5);
		gbc_lblSuaPorta.gridx = 2;
		gbc_lblSuaPorta.gridy = 2;
		contentPane.add(lblSuaPorta, gbc_lblSuaPorta);

		txtPortaServidor = new JTextField();
		txtPortaServidor.setText("1818");
		GridBagConstraints gbc_txtPortaCliente = new GridBagConstraints();
		gbc_txtPortaCliente.insets = new Insets(0, 0, 5, 5);
		gbc_txtPortaCliente.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtPortaCliente.gridx = 3;
		gbc_txtPortaCliente.gridy = 2;
		contentPane.add(txtPortaServidor, gbc_txtPortaCliente);
		txtPortaServidor.setColumns(10);

		btnConectar = new JButton("Conectar");
		btnConectar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				conectarServidor();
				btnDesconectar.setEnabled(true);
				btnConectar.setEnabled(false);

			}
		});
		GridBagConstraints gbc_btnConectar = new GridBagConstraints();
		gbc_btnConectar.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnConectar.insets = new Insets(0, 0, 5, 5);
		gbc_btnConectar.gridx = 4;
		gbc_btnConectar.gridy = 2;
		contentPane.add(btnConectar, gbc_btnConectar);

		btnDesconectar = new JButton("Desconectar");
		btnLigarServidor.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				try {

					desconectar(getClienteLocal());

					btnConectar.setEnabled(true);
					btnDesconectar.setEnabled(false);

				} catch (RemoteException e) {
					e.printStackTrace();
				}

			}
		});
		GridBagConstraints gbc_btnDesconectar = new GridBagConstraints();
		gbc_btnDesconectar.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnDesconectar.insets = new Insets(0, 0, 5, 0);
		gbc_btnDesconectar.gridx = 5;
		gbc_btnDesconectar.gridy = 2;
		contentPane.add(btnDesconectar, gbc_btnDesconectar);

		JLabel lblPesquisa = new JLabel("Pesquisa");
		GridBagConstraints gbc_lblPesquisa = new GridBagConstraints();
		gbc_lblPesquisa.anchor = GridBagConstraints.EAST;
		gbc_lblPesquisa.insets = new Insets(0, 0, 5, 5);
		gbc_lblPesquisa.gridx = 0;
		gbc_lblPesquisa.gridy = 3;
		contentPane.add(lblPesquisa, gbc_lblPesquisa);

		txtPesquisa = new JTextField();
		txtPesquisa.setFont(new Font("Tahoma", Font.PLAIN, 18));
		GridBagConstraints gbc_txtPesquisa = new GridBagConstraints();
		gbc_txtPesquisa.gridwidth = 4;
		gbc_txtPesquisa.insets = new Insets(0, 0, 5, 5);
		gbc_txtPesquisa.fill = GridBagConstraints.BOTH;
		gbc_txtPesquisa.gridx = 1;
		gbc_txtPesquisa.gridy = 3;
		contentPane.add(txtPesquisa, gbc_txtPesquisa);
		txtPesquisa.setColumns(10);

		btnPesquisar = new JButton("Pesquisar");
		btnPesquisar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				String search = txtPesquisa.getText().trim();
				TipoFiltro filtro = TipoFiltro.valueOf(cmbFiltros.getSelectedItem().toString());
				String vlrFiltro = txtValorFiltro.getText().trim();

				if (search.equals("")) {
					JOptionPane.showMessageDialog(null, "Atenção, campo de busca vazio");
				} else {

					HashMap<Cliente, List<Arquivo>> resultSearch = new HashMap<>();

					try {
						resultSearch = (HashMap<Cliente, List<Arquivo>>) procurarArquivo(search, filtro, vlrFiltro);

						if (resultSearch.size() > 0) {

							ResultadoModel model = new ResultadoModel(resultSearch);
							table.setModel(model);

						} else {
							JOptionPane.showMessageDialog(TelaPrincipal.this, "Nenhum resultado encontrado");
						}

					} catch (RemoteException e1) {
						e1.printStackTrace();
					}
				}

			}
		});
		GridBagConstraints gbc_btnPesquisar = new GridBagConstraints();
		gbc_btnPesquisar.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnPesquisar.insets = new Insets(0, 0, 5, 0);
		gbc_btnPesquisar.gridx = 5;
		gbc_btnPesquisar.gridy = 3;
		contentPane.add(btnPesquisar, gbc_btnPesquisar);

		btnDownload = new JButton("Download");
		GridBagConstraints gbc_btnDownload = new GridBagConstraints();
		gbc_btnDownload.insets = new Insets(0, 0, 5, 5);
		gbc_btnDownload.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnDownload.gridx = 0;
		gbc_btnDownload.gridy = 4;
		contentPane.add(btnDownload, gbc_btnDownload);

		cmbFiltros = new JComboBox(TipoFiltro.values());
		cmbFiltros.setToolTipText("Filtro");
		GridBagConstraints gbc_cmbFiltros = new GridBagConstraints();
		gbc_cmbFiltros.insets = new Insets(0, 0, 5, 5);
		gbc_cmbFiltros.fill = GridBagConstraints.HORIZONTAL;
		gbc_cmbFiltros.gridx = 4;
		gbc_cmbFiltros.gridy = 4;
		contentPane.add(cmbFiltros, gbc_cmbFiltros);

		txtValorFiltro = new JTextField();
		txtValorFiltro.setToolTipText("Valor Filtro");
		txtValorFiltro.setText("Valor Filtro");
		GridBagConstraints gbc_txtValorFiltro = new GridBagConstraints();
		gbc_txtValorFiltro.insets = new Insets(0, 0, 5, 0);
		gbc_txtValorFiltro.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtValorFiltro.gridx = 5;
		gbc_txtValorFiltro.gridy = 4;
		contentPane.add(txtValorFiltro, gbc_txtValorFiltro);
		txtValorFiltro.setColumns(10);

		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPane.gridwidth = 6;
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 5;
		contentPane.add(scrollPane, gbc_scrollPane);

		table = new JTable();
		scrollPane.setViewportView(table);

		// Cria listagem de clientes
		clientes = new ArrayList<Cliente>();

		// Create folder of arqs
		file = new File("C:\\Share");
		file.mkdir();

		// Creating map about clients and arqs
		mapaclientesArq = new HashMap<>();

		configuracaoInicial();

	}

	private void configuracaoInicial() {

		btnDesconectar.setEnabled(false);
		btndesligarServ.setEnabled(false);
		btnPesquisar.setEnabled(false);
		btnDownload.setEnabled(false);

	}

	public Cliente getClienteLocal() {

		Cliente cliente = new Cliente();
		cliente.setId(new Long(idCliente++));
		cliente.setIp(txtMeuIp.getText().trim());
		cliente.setNome(txtNomeCliente.getText().trim());
		cliente.setPorta(Integer.parseInt(txtMinhaPorta.getText()));

		return cliente;

	}

	// Desliga o servico do servidor
	protected void desligarServidor() {

		try {
			UnicastRemoteObject.unexportObject(servidor, true);
			servidor = null;

		} catch (NoSuchObjectException e) {
			e.printStackTrace();
		}

	}

	private String getMeuIp() {
		InetAddress IP = null;

		try {
			IP = InetAddress.getLocalHost();

		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		return IP.getHostAddress().toString();
	}

	private void setViewSearch(HashMap<Cliente, List<Arquivo>> resultSearch) {

		// ResultadoModel model = new ResultadoModel(resultSearch);
		//
		// table.setModel(model);

	}

	// Return a client of mapClients
	private Cliente getClient(java.util.Map.Entry<Cliente, List<Arquivo>> e) {

		Cliente client = new Cliente();
		client.setId(e.getKey().getId());
		client.setIp(e.getKey().getIp());
		client.setNome(e.getKey().getNome());
		client.setPorta(e.getKey().getPorta());

		return client;
	}

	// Return one lista with arqs
	protected List<Arquivo> getArquivosDisponiveis() {

		File dirStart = file;

		List<Arquivo> listArq = new ArrayList<>();

		for (File file : dirStart.listFiles()) {
			if (file.isFile()) {
				Arquivo arq = new Arquivo();
				arq.setNome(new MethodUtils().getNome(file.getName()));
				arq.setExtensao(new MethodUtils().getExtension(file.getName()));
				arq.setTamanho(file.length());
				arq.setDataHoraModificacao(new Date(file.lastModified()));
				arq.setPath(file.getPath());
				arq.setMd5(new MethodUtils().getMD5(arq.getPath()));
				arq.setTamanho(file.getTotalSpace());
				listArq.add(arq);
			}
		}
		return listArq;
	}

	// Retorna a extensao do arquivo
	private String getFileExtension(File file2) {
		String fileName = file.getName();
		if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
			return fileName.substring(fileName.lastIndexOf(".") + 1);
		else
			return "";

	}

	protected void conectarServidor() {
		String meuNome = txtNomeCliente.getText().trim();
		if (meuNome.length() == 0) {
			JOptionPane.showMessageDialog(this, "VocÃª precisa digitar um nome!");
			return;
		}

		// Get My ip
		String host = txtIpServidor.getText().trim();
		if (!host.matches("[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}")) {
			JOptionPane.showMessageDialog(this, "O endereço ip parece ser inválido!");
			return;
		}

		// Get my port
		String strPorta = txtPortaServidor.getText().trim();
		if (!strPorta.matches("[0-9]+") || strPorta.length() > 5) {
			JOptionPane.showMessageDialog(this, "A porta deve ser um valor máximo de no máximo 5 dígitos!");
			return;
		}

		Cliente cliente = getClienteLocal();

		int intPorta = Integer.parseInt(strPorta);

		try {
			registryClient = LocateRegistry.getRegistry(host, intPorta);

			clienteServ = (IServer) registryClient.lookup(IServer.NOME_SERVICO);

			clienteServ.registrarCliente(cliente);

		} catch (Exception e) {
			e.printStackTrace();
		}

		btnConectar.setEnabled(false);
		txtIpServidor.setEditable(false);
		txtNomeCliente.setEditable(false);
		txtPortaServidor.setEditable(false);
		btnPesquisar.setEnabled(true);
	}

	private void iniciarRMI() {

		String porta = txtMinhaPorta.getText().trim();

		if (!porta.matches("[0-9]+") || porta.length() > 5) {
			JOptionPane.showMessageDialog(this, "A porta deve ser um valor numï¿½rico de no maximo 5 digitos!");
			return;
		}

		int intPorta = Integer.parseInt(porta);
		if (intPorta < 1024 || intPorta > 65535) {
			JOptionPane.showMessageDialog(this, "A porta deve estar entre 1024 e 65535");
			return;
		}

		try {

			servidor = (IServer) UnicastRemoteObject.exportObject(TelaPrincipal.this, 0);

			registryServ = LocateRegistry.createRegistry(intPorta);

			registryServ.rebind(IServer.NOME_SERVICO, servidor);

		} catch (RemoteException e) {
			e.printStackTrace();
		}

		System.out.println("Servidor RMI iniciado");

	}

	@Override
	public void registrarCliente(Cliente c) throws RemoteException {

		mapaclientesArq.put(c, getArquivosDisponiveis());

		System.out.println("Clliente " + c.getNome() + " conectou");

	}

	@Override
	public void publicarListaArquivos(Cliente c, List<Arquivo> lista) throws RemoteException {

		mapaclientesArq.put(c, lista);

	}

	@Override
	public Map<Cliente, List<Arquivo>> procurarArquivo(String query, TipoFiltro tipoFiltro, String filtro)
			throws RemoteException {

		resultArqs = new ArrayList<>();
		HashMap<Cliente, List<Arquivo>> resultSearch = new HashMap<>();

		Pattern pat = Pattern.compile(".*" + query + ".*");

		for (java.util.Map.Entry<Cliente, List<Arquivo>> e : mapaclientesArq.entrySet()) {

			Cliente client = getClient(e);

			for (Arquivo arq : e.getValue()) {

				switch (tipoFiltro) {
				case NOME:

					if (arq.getNome().contains(query.toLowerCase())) {

						resultArqs.add(arq);

					}

				case TAMANHO_MIN:

					try {

						if (arq.getTamanho() > Integer.parseInt(filtro)) {

							if (arq.getNome().contains(query.toLowerCase())) {

								resultArqs.add(arq);
							}

						}
					} catch (Exception e2) {
					}

				case TAMANHO_MAX:
					try {
						if (arq.getTamanho() < Integer.parseInt(query.toLowerCase())) {

							if (arq.getNome().contains(query.toLowerCase())) {
								resultArqs.add(arq);
							}

						}
					} catch (Exception e3) {

					}
				case EXTENSAO:

					if (arq.getExtensao().equals(query.toLowerCase())) {
						resultArqs.add(arq);
					}

				default:
					JOptionPane.showMessageDialog(null, "Algo deu errado. Verifique sua pesquisa");
					break;
				}

				resultSearch.put(client, listaArqs);

			}
		}

		return resultSearch;
	}

	@Override
	public byte[] baixarArquivo(Cliente cli, Arquivo arq) throws RemoteException {
		return null;
	}

	@Override
	public void desconectar(Cliente c) throws RemoteException {

		if (servidor != null) {
			servidor.desconectar(c);
			servidor = null;
		}

	}

}
