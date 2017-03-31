package br.univel.view;

import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import br.univel.jshare.comum.Arquivo;
import br.univel.jshare.comum.Cliente;
import br.univel.jshare.comum.IServer;
import br.univel.jshare.enun.TipoFiltro;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ConfigServidor extends JFrame implements IServer {

	private JPanel contentPane;
	private JTextField txtPorta;
	private JComboBox cmbIps;
	private IServer servidor;
	private Registry registry;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ConfigServidor frame = new ConfigServidor();
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
	public ConfigServidor() {
		setTitle("Configuração Servidor");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[] { 77, 85, 0, 0 };
		gbl_contentPane.rowHeights = new int[] { 0, 0, 0, 0, 0 };
		gbl_contentPane.columnWeights = new double[] { 0.0, 1.0, 0.0, Double.MIN_VALUE };
		gbl_contentPane.rowWeights = new double[] { 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE };
		contentPane.setLayout(gbl_contentPane);

		JLabel lblIp = new JLabel("Ip");
		GridBagConstraints gbc_lblIp = new GridBagConstraints();
		gbc_lblIp.insets = new Insets(0, 0, 5, 5);
		gbc_lblIp.anchor = GridBagConstraints.EAST;
		gbc_lblIp.gridx = 0;
		gbc_lblIp.gridy = 0;
		contentPane.add(lblIp, gbc_lblIp);

		cmbIps = new JComboBox();
		GridBagConstraints gbc_cmbIps = new GridBagConstraints();
		gbc_cmbIps.insets = new Insets(0, 0, 5, 5);
		gbc_cmbIps.fill = GridBagConstraints.HORIZONTAL;
		gbc_cmbIps.gridx = 1;
		gbc_cmbIps.gridy = 0;
		contentPane.add(cmbIps, gbc_cmbIps);

		JLabel lblPorta = new JLabel("Porta");
		GridBagConstraints gbc_lblPorta = new GridBagConstraints();
		gbc_lblPorta.anchor = GridBagConstraints.EAST;
		gbc_lblPorta.insets = new Insets(0, 0, 5, 5);
		gbc_lblPorta.gridx = 0;
		gbc_lblPorta.gridy = 1;
		contentPane.add(lblPorta, gbc_lblPorta);

		txtPorta = new JTextField();
		txtPorta.setText("1818");
		GridBagConstraints gbc_txtPorta = new GridBagConstraints();
		gbc_txtPorta.insets = new Insets(0, 0, 5, 5);
		gbc_txtPorta.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtPorta.gridx = 1;
		gbc_txtPorta.gridy = 1;
		contentPane.add(txtPorta, gbc_txtPorta);
		txtPorta.setColumns(10);

		JButton btnLigar = new JButton("Ligar");
		btnLigar.addActionListener(new ActionListener() {
			private boolean servidorLigado;

			public void actionPerformed(ActionEvent arg0) {

				iniciarRMI();
				servidorLigado = true;
				txtPorta.setEnabled(false);
				cmbIps.setEditable(false);

				if (servidorLigado) {
					btnLigar.setText("Desligar");
				}

				if (btnLigar.equals("Desligar")) {
					desligarRMI();
				}

			}
		});
		GridBagConstraints gbc_btnLigar = new GridBagConstraints();
		gbc_btnLigar.insets = new Insets(0, 0, 5, 0);
		gbc_btnLigar.gridx = 2;
		gbc_btnLigar.gridy = 1;
		contentPane.add(btnLigar, gbc_btnLigar);
		
		JLabel lblArquivosDisponveis = new JLabel("Arquivos Dispon\u00EDveis");
		GridBagConstraints gbc_lblArquivosDisponveis = new GridBagConstraints();
		gbc_lblArquivosDisponveis.gridwidth = 3;
		gbc_lblArquivosDisponveis.insets = new Insets(0, 0, 5, 5);
		gbc_lblArquivosDisponveis.gridx = 0;
		gbc_lblArquivosDisponveis.gridy = 2;
		contentPane.add(lblArquivosDisponveis, gbc_lblArquivosDisponveis);

		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.gridwidth = 3;
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 3;
		contentPane.add(scrollPane, gbc_scrollPane);

		JTextArea arqDisponivel = new JTextArea();
		scrollPane.setViewportView(arqDisponivel);

		configuraTela();

	}

	protected void desligarRMI() {

	}

	protected void iniciarRMI() {

		String porta = txtPorta.getText().trim();

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

			registry = LocateRegistry.createRegistry(intPorta);

			registry.rebind(IServer.NOME_SERVICO, this);

		} catch (RemoteException e) {
			e.printStackTrace();
		}

	}

	private void configuraTela() {

		List<String> listaIps = getIpsDisponiveis();

		cmbIps.setModel(new DefaultComboBoxModel<>(new Vector<String>(listaIps)));
		cmbIps.setSelectedIndex(0);
	}

	private List<String> getIpsDisponiveis() {

		List<String> addrList = new ArrayList<String>();
		try {
			Enumeration<NetworkInterface> ifaces = NetworkInterface.getNetworkInterfaces();

			while (ifaces.hasMoreElements()) {
				NetworkInterface ifc = ifaces.nextElement();
				if (ifc.isUp()) {
					Enumeration<InetAddress> addresses = ifc.getInetAddresses();
					while (addresses.hasMoreElements()) {

						InetAddress addr = addresses.nextElement();

						String ip = addr.getHostAddress();

						if (ip.matches("[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}")) {
							addrList.add(ip);
						}

					}
				}
			}
		} catch (SocketException e) {
			e.printStackTrace();
		}

		return addrList;
	}

	@Override
	public void registrarCliente(Cliente c) throws RemoteException {
		// TODO Auto-generated method stub

	}

	@Override
	public void publicarListaArquivos(Cliente c, List<Arquivo> lista) throws RemoteException {
		// TODO Auto-generated method stub

	}

	@Override
	public Map<Cliente, List<Arquivo>> procurarArquivo(String query, TipoFiltro tipoFiltro, String filtro)
			throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public byte[] baixarArquivo(Cliente cli, Arquivo arq) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void desconectar(Cliente c) throws RemoteException {
		// TODO Auto-generated method stub

	}

}
