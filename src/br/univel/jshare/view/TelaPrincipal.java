package br.univel.jshare.view;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import br.univel.jshare.comum.Arquivo;
import br.univel.jshare.comum.Cliente;
import br.univel.jshare.comum.IServer;
import br.univel.jshare.comum.TipoFiltro;
import br.univel.jshare.model.MeuModelo;
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
	private IServer conexaoServidor;
	private Registry registryServ;
	private Registry registryCliente;
	private JTextField txtPortaServidor;
	private List<Cliente> clientes;
	private File defaultFile;
	private long idProd = 0;
	private HashMap<Cliente, List<Arquivo>> mapaclientesArq;
	private List<Arquivo> resultArqs;
	private JTextField txtMeuIp;
	private JButton btnDesconectar;
	private JButton btndesligarServ;
	private JButton btnPesquisar;
	private JButton btnDownload;
	private JTextField txtValorFiltro;
	private JComboBox cmbFiltros;
	private static final String PATH_DOW_UP = "./Share";
	private JButton btnLigarServidor;
	private Thread thread;
	private JSplitPane splitPane;
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy H:mm:ss");
	private JScrollPane scrollPane_1;
	private JTextArea logServidor;
	private JScrollPane scrollPane_2;
	private JTextArea logCliente;

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
		gbl_contentPane.rowHeights = new int[] { 20, 0, 23, 22, 0, 130, 0, 137, 0 };
		gbl_contentPane.columnWeights = new double[] { 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		gbl_contentPane.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE };
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

		btnLigarServidor = new JButton("Ligar Servidor");
		btnLigarServidor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				configuracaoInicial();

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
		btnDesconectar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				try {

					desconectar(getClienteLocal());

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

				HashMap<Cliente, List<Arquivo>> resultSearch = new HashMap<>();

				try {
					resultSearch = (HashMap<Cliente, List<Arquivo>>) conexaoServidor.procurarArquivo(search, filtro,
							vlrFiltro);

					if (!resultSearch.isEmpty()) {

						btnDownload.setEnabled(true);

						MeuModelo model = new MeuModelo(resultSearch);
						table.setModel(model);

					}

				} catch (RemoteException e1) {
					e1.printStackTrace();
				}

			}
		});
		GridBagConstraints gbc_btnPesquisar = new GridBagConstraints();
		gbc_btnPesquisar.fill = GridBagConstraints.BOTH;
		gbc_btnPesquisar.insets = new Insets(0, 0, 5, 0);
		gbc_btnPesquisar.gridx = 5;
		gbc_btnPesquisar.gridy = 3;
		contentPane.add(btnPesquisar, gbc_btnPesquisar);

		btnDownload = new JButton("Download");
		btnDownload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				getArquivoCliente(table.getSelectedRow());

			}
		});
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
		table.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent click) {

				if (click.getClickCount() == 2) {

					int linhaSelecionada = table.getSelectedRow();

					getArquivoCliente(linhaSelecionada);

				}

			}

		});

		splitPane = new JSplitPane();
		GridBagConstraints gbc_splitPane = new GridBagConstraints();
		gbc_splitPane.gridheight = 2;
		gbc_splitPane.insets = new Insets(0, 0, 5, 0);
		gbc_splitPane.gridwidth = 6;
		gbc_splitPane.fill = GridBagConstraints.BOTH;
		gbc_splitPane.gridx = 0;
		gbc_splitPane.gridy = 6;
		contentPane.add(splitPane, gbc_splitPane);

		scrollPane_1 = new JScrollPane();
		splitPane.setRightComponent(scrollPane_1);

		logServidor = new JTextArea();
		scrollPane_1.setViewportView(logServidor);

		scrollPane_2 = new JScrollPane();
		splitPane.setLeftComponent(scrollPane_2);

		logCliente = new JTextArea();
		scrollPane_2.setViewportView(logCliente);

		// Cria listagem de clientes
		clientes = new ArrayList<Cliente>();

		// Create folder of arqs
		defaultFile = new File("./Share");
		defaultFile.mkdir();

		// Creating map about clients and arqs
		mapaclientesArq = new HashMap<>();

		configuracaoInicial();

	}

	/**
	 * Busca as informa�oes do objeto a ser baixado atrav�s da linha selecionada pelo usu�rio
	 * 
	 * @param int
	 *            linhaSelecionada
	 */
	protected void getArquivoCliente(int linhaSelecionada) {

		if (linhaSelecionada < 0) {
			JOptionPane.showMessageDialog(TelaPrincipal.this, "Nenhuma linha selecionada!!!");
		} else {

			Cliente cliente = new Cliente();
			Arquivo arq = new Arquivo();

			// pegando cliente
			cliente.setNome(table.getValueAt(linhaSelecionada, 0).toString());
			cliente.setIp(table.getValueAt(linhaSelecionada, 1).toString());
			cliente.setPorta(Integer.parseInt(table.getValueAt(linhaSelecionada, 2).toString()));

			// pegando arquivo
			arq.setNome(table.getValueAt(linhaSelecionada, 3).toString());
			arq.setPath(table.getValueAt(linhaSelecionada, 4).toString());
			arq.setExtensao(table.getValueAt(linhaSelecionada, 5).toString());
			arq.setTamanho(new Long(table.getValueAt(linhaSelecionada, 6).toString()));
			arq.setMd5(table.getValueAt(linhaSelecionada, 7).toString());

			try {

				registryCliente = LocateRegistry.getRegistry(cliente.getIp(), cliente.getPorta());

				IServer conecServArq = (IServer) registryCliente.lookup(IServer.NOME_SERVICO);

				byte[] arqBytes = conecServArq.baixarArquivo(cliente, arq);

				copiarArquivo(cliente, new File("C�pia de " + arq.getNome()), arqBytes, arq);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	
	/**
	 * 
	 * Grava o arquivo ap�s realizar o download
	 * 
	 * @param cliente
	 * @param file
	 * @param arqBytes
	 * @param arq
	 */
	private void copiarArquivo(Cliente cliente, File file, byte[] arqBytes, Arquivo arq) {

		try {
			Files.write(Paths.get(PATH_DOW_UP.concat("\\" + file.getName() + arq.getExtensao())), arqBytes,
					StandardOpenOption.CREATE);

			imprimirServidor(
					"Usu�rio " + cliente.getNome() + " baixou o arquivo: " + arq.getNome() + arq.getExtensao());

			JOptionPane.showMessageDialog(TelaPrincipal.this, "Arquivo baixado com sucesso.", "Informa��o",
					JOptionPane.INFORMATION_MESSAGE);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	
	/**
	 * 
	 * Configuracao de botoes e fields de tela
	 * 
	 */
	private void configuracaoInicial() {

		// Begining RMI
		iniciarRMI();
		btnLigarServidor.setEnabled(false);
		txtMinhaPorta.setEditable(false);
		txtMeuIp.setEnabled(false);
		btnDesconectar.setEnabled(false);

		btnDesconectar.setEnabled(false);
		btnPesquisar.setEnabled(false);
		btnDownload.setEnabled(false);

	}

	/**
	 * 
	 * Retornar um objeto Cliente com as informa�oes do cliente onde o sistema
	 * est� rodando
	 * 
	 * @return
	 */
	public Cliente getClienteLocal() {

		Cliente cliente = new Cliente();
		cliente.setIp(txtMeuIp.getText().trim());
		cliente.setNome(txtNomeCliente.getText().trim());
		cliente.setPorta(Integer.parseInt(txtMinhaPorta.getText()));

		return cliente;

	}

	/**
	 * 
	 * Finaliza o servico do servidor, removendo o objeto esportado
	 * 
	 */
	public void desligarServidor() {

		try {
			UnicastRemoteObject.unexportObject(TelaPrincipal.this, true);
			conexaoServidor = null;
			registryServ = null;
			if (!(thread == null)) {
				thread.interrupt();
			}

			txtMinhaPorta.setEditable(true);
			txtMeuIp.setEnabled(true);

			imprimirServidor("Servidor est� sendo encerrado!!!");

		} catch (NoSuchObjectException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * Retorna o ip da m�quina onde o sistema est� rodando
	 * 
	 * @return
	 */
	private String getMeuIp() {
		InetAddress IP = null;

		try {
			IP = InetAddress.getLocalHost();

		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		return IP.getHostAddress().toString();
	}

	/**
	 * 
	 * Retorna um objeto cliente a partir do HashMap
	 * 
	 * @param e
	 * @return
	 */
	private Cliente getClient(java.util.Map.Entry<Cliente, List<Arquivo>> e) {

		Cliente client = new Cliente();
		client.setId(e.getKey().getId());
		client.setIp(e.getKey().getIp());
		client.setNome(e.getKey().getNome());
		client.setPorta(e.getKey().getPorta());

		return client;
	}


	/**
	 * 
	 * Retorna um List com os arquivos que o usu�rio local possui
	 * 
	 * @return
	 */
	protected List<Arquivo> getArquivosDisponiveis() {

		// File dirStart = defaultFile;
		File dirStart = new File("." + File.separatorChar + "share" + File.separatorChar);
		List<Arquivo> listArq = new ArrayList<Arquivo>();

		for (File file : dirStart.listFiles()) {
			if (file.isFile()) {
				Arquivo arq = new Arquivo();
				arq.setId(new Long(idProd++));
				arq.setNome(new MethodUtils().getNome(file.getName()));
				arq.setExtensao(new MethodUtils().getExtension(file.getName()));
				arq.setTamanho(file.getUsableSpace());
				arq.setDataHoraModificacao(new Date(file.lastModified()));
				arq.setPath(file.getPath());
				arq.setMd5(new MethodUtils().getMD5Checksum(arq.getPath()));
				arq.setTamanho(file.length());

				listArq.add(arq);
			}
		}
		return listArq;

	}


	/**
	 * 
	 * Retorna uma String com a extensao do arquivo
	 * 
	 * @param file2
	 * @return
	 */
	private String getFileExtension(File file) {
		String fileName = file.getName();

		if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
			return fileName.substring(fileName.lastIndexOf(".") + 1);
		else
			return "";

	}

	/**
	 * 
	 * Realiza conexao com o servidor de terceiros
	 * 
	 */
	protected void conectarServidor() {

		String meuNome = txtNomeCliente.getText().trim();
		if (meuNome.length() == 0) {
			JOptionPane.showMessageDialog(this, "Voc� precisa digitar um nome!");
			return;
		}

		// Get My ip
		String host = txtIpServidor.getText().trim();
		if (!host.matches("[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}")) {
			JOptionPane.showMessageDialog(this, "O endere�o ip parece ser inv�lido!");
			return;
		}

		// Get my port
		String strPorta = txtPortaServidor.getText().trim();
		if (!strPorta.matches("[0-9]+") || strPorta.length() > 5) {
			JOptionPane.showMessageDialog(this, "A porta deve ser um valor m�ximo de no m�ximo 5 d�gitos!");
			return;
		}

		Cliente cliente = getClienteLocal();

		int intPorta = Integer.parseInt(strPorta);

		try {
			registryCliente = LocateRegistry.getRegistry(host, intPorta);

			conexaoServidor = (IServer) registryCliente.lookup(IServer.NOME_SERVICO);

			conexaoServidor.registrarCliente(cliente);

			thread = new Thread(new Runnable() {

				@Override
				public void run() {

					while (true) {

						try {

							conexaoServidor.publicarListaArquivos(cliente, getArquivosDisponiveis());
							Thread.sleep(5000);

						} catch (Exception e) {
							e.printStackTrace();
						}

					}
				}
			});

			thread.start();

		} catch (Exception e) {
			JOptionPane.showMessageDialog(TelaPrincipal.this, "Erro ao conectar ao servidor!!!", "Erro",
					JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}

		btnConectar.setEnabled(false);
		txtIpServidor.setEditable(false);
		txtNomeCliente.setEditable(false);
		txtPortaServidor.setEditable(false);
		btnPesquisar.setEnabled(true);
	}

	/**
	 * M�todo que inicia a aplica��o RMI e esporta o servidor para conex�es de
	 * clientes
	 */
	private void iniciarRMI() {

		String porta = txtMinhaPorta.getText().trim();

		if (!porta.matches("[0-9]+") || porta.length() > 5) {
			JOptionPane.showMessageDialog(this, "A porta deve ser um valor num�rico de no maximo 5 digitos!");
			return;
		}

		int intPorta = Integer.parseInt(porta);
		if (intPorta < 1024 || intPorta > 65535) {
			JOptionPane.showMessageDialog(this, "A porta deve estar entre 1024 e 65535");
			return;
		}

		try {

			if (servidor == null) {
				servidor = (IServer) UnicastRemoteObject.exportObject(TelaPrincipal.this, 0);
			}

			registryServ = LocateRegistry.createRegistry(intPorta);

			registryServ.rebind(IServer.NOME_SERVICO, servidor);

		} catch (RemoteException e) {
			e.printStackTrace();
		}

		imprimirServidor("Servidor RMI iniciado");

	}

	@Override
	public void registrarCliente(Cliente c) throws RemoteException {

		if (mapaclientesArq.containsKey(c)) {
			JOptionPane.showMessageDialog(TelaPrincipal.this, "Cliente j� registrado no servidor.");

		} else {

			mapaclientesArq.put(c, new ArrayList<>());
			imprimirServidor(c.getNome() + " se conectou.");

		}

	}

	@Override
	public void publicarListaArquivos(Cliente c, List<Arquivo> lista) throws RemoteException {

		if (mapaclientesArq.containsKey(c)) {
			mapaclientesArq.entrySet().forEach(e -> {
				if (e.getKey().equals(c)) {
					e.setValue(lista);
					imprimirCliente("Lista do cliente " + c.getNome() + " foi atualizada!!!");
				}
			});
		}

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

					if (arq.getNome().contains(query)) {

						resultArqs.add(arq);

					}

					break;

				case TAMANHO_MIN:

					try {

						if (arq.getTamanho() > Integer.parseInt(filtro)) {

							if (arq.getNome().contains(query)) {

								resultArqs.add(arq);
							}

						}
					} catch (Exception e2) {
					}
					break;
				case TAMANHO_MAX:
					try {
						if (arq.getTamanho() < Integer.parseInt(query)) {

							if (arq.getNome().contains(query)) {
								resultArqs.add(arq);
							}

						}
					} catch (Exception e3) {

					}
					break;
				case EXTENSAO:

					if (arq.getExtensao().equalsIgnoreCase(query)) {
						resultArqs.add(arq);
					}
					break;

				default:
					JOptionPane.showMessageDialog(null, "Algo deu errado. Verifique sua pesquisa");
					break;
				}

			}

			resultSearch.put(e.getKey(), resultArqs);
		}

		mapaclientesArq.forEach((key, value) -> {
			List<Arquivo> listaResult = new ArrayList<>();
			value.forEach(e -> {
				listaResult.add(e);
			});
			resultSearch.put(key, listaResult);
		});

		return resultSearch;
	}

	@Override
	public byte[] baixarArquivo(Cliente cli, Arquivo arq) throws RemoteException {

		byte[] arqCop = null;

		Path path = Paths.get(arq.getPath());

		try {
			arqCop = Files.readAllBytes(path);

		} catch (IOException e) {

			JOptionPane.showMessageDialog(TelaPrincipal.this, "Erro ao ler arquivo", "Erro", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}

		return arqCop;

	}

	@Override
	public void desconectar(Cliente c) throws RemoteException {

		if (c != null) {
			// if (mapaclientesArq.containsKey(c)) {
			// mapaclientesArq.remove(c);
			// System.out.println("Cliente removido com sucesso!!!");
			// }

			if (mapaclientesArq.containsKey(c)) {

				mapaclientesArq.remove(c);
				thread.stop();
				btnDesconectar.setEnabled(false);
				btnConectar.setEnabled(true);
				btnPesquisar.setEnabled(false);
				txtIpServidor.setEditable(true);
				txtPortaServidor.setEditable(true);
				txtNomeCliente.setEditable(true);
				imprimirServidor("Usu�rio " + c.getNome() + " se desconectou.");
				imprimirCliente("Usu�rio desconectado, sua lista foi removida do servidor.");
			}

		}

	}

	/**
	 * Responsavel por imprimir em tela as informa��es que est�o ocorrendo no
	 * servidor
	 * 
	 * @param texto
	 */
	public void imprimirServidor(String texto) {

		logServidor.append(sdf.format(new Date()));
		logServidor.append(" --> ");
		logServidor.append(texto);
		logServidor.append("\n");

	}

	public void imprimirCliente(String texto) {

		logCliente.append(sdf.format(new Date()));
		logCliente.append(" --> ");
		logCliente.append(texto);
		logCliente.append("\n");

	}

}
