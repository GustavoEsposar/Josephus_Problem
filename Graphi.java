import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentListener;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.awt.event.ComponentEvent;
import ListaDupla.ListaDuplamenteLigadaCircular;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * Classe com os atributos e metodo necessarios para construir a interface
 * e seus componentes de interacao com o usuario para mostrar a solucao do
 * Josephus de forma Grafica.
 * 
 * @author Gustavo Barbieri Esposar
 *         Caio de Nasi Sclavi
 * @version v1.0 2022 06 23
 */
public class Graphi extends JFrame {

    /**
     * Atributos
     */
    private int qntd;
    private int passoQ;
    private int velocidadeQ;
    JLabel quant;
    JLabel pass;
    JLabel tem;
    JFrame frame;
    JPanel quadrados;
    JPanel menu;
    GridBagConstraints gbc;
    JPanel painel = new JPanel(new GridBagLayout());
    JSlider quantidade;
    JSlider passo;
    JSlider velocidade;
    JPanel painelJogo;
    String[] valores;
    ListaDuplamenteLigadaCircular lista = new ListaDuplamenteLigadaCircular();
    boolean lock = false;

    /**
     * Metodo construtor da GUI
     */
    public Graphi() {
        setQuantidade(0);
        setPasso(0);
        setVelocidade(1);
        constroiFrame();
        constroiPainelMenu();
        constroiPainelJogo();
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * setters
     */
    public void setVelocidade(int x) {
        velocidadeQ = x;
    }

    public void setQuantidade(int x) {
        qntd = x;
    }

    public void setPasso(int x) {
        passoQ = x;
    }

    /**
     * Getters
     */
    public int getVelocidace() {
        return velocidadeQ;
    }

    public int getPasso() {
        return passoQ;
    }

    public int getQuantidade() {
        return qntd;
    }

    /**
     * Metodo para configurar o JFrame
     */
    public void constroiFrame() {
        // inicializacao dos labels
        quant = new JLabel("Quantidade: 2", SwingConstants.CENTER);
        pass = new JLabel("Passo: 2", SwingConstants.CENTER);
        tem = new JLabel("Velocidade: 1", SwingConstants.CENTER);
        frame = new JFrame("Josephus");
        // configuracao do frame
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setMinimumSize(new Dimension(600, 400));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Listener para atualizar o JPanel quadrados ao alterar o frame
        frame.addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
                chamarMontarJogo();
            }

            public void componentHidden(ComponentEvent e) {
            }

            public void componentMoved(ComponentEvent e) {
            }

            public void componentShown(ComponentEvent e) {
            }
        });

        frame.addWindowStateListener((e) -> {
            chamarMontarJogo();
        });
    }

    /**
     * Metodo que inicializa e configura o JPanel de visualizacao do Problema
     * Josephus
     */
    public void constroiPainelJogo() {
        // instancia JPanel composto pelos quadrados (pessoas) do problema
        quadrados = new JPanel(new GridBagLayout());
        quadrados.setBackground(Color.gray);
        GridBagConstraints c = new GridBagConstraints();
        for (int i = 1; i <= 2; i++) {
            JTextField tc = new JTextField();
            tc.setPreferredSize(new Dimension(50, 50));
            tc.setHorizontalAlignment(JTextField.CENTER);
            tc.setForeground(Color.white);
            tc.setText("" + (i + 1));
            tc.setBackground(Color.black);
            tc.setEditable(false);
            c.gridx = 0;
            quadrados.add(tc, c);
        }

        // instanciando e configurando JPanel que contem o painel quadrados
        painelJogo = new JPanel();
        painelJogo.setBackground(Color.GRAY);
        painelJogo.add(quadrados);
        gbc.gridy = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        painel.add(painelJogo, gbc);
        frame.add(painel);
    }

    /**
     * Metodo para configurar o JPainel do Menu de opcoes das variaveis do josephus
     */
    public void constroiPainelMenu() {
        // instanciando e configurando JPanel do Menu interativo
        menu = new JPanel(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        menu.setSize(790, 50);
        menu.setBackground(Color.lightGray);
        // intanciando e configurando sliders utilizados para obter valores escolhidos
        // pelo usuario
        quantidade = new JSlider(JSlider.HORIZONTAL, 2, 60, 2);
        passo = new JSlider(JSlider.HORIZONTAL, 2, 60, 2);
        velocidade = new JSlider(JSlider.HORIZONTAL, 1, 10, 1);
        quantidade.setBackground(Color.lightGray);
        setPasso(2);
        setQuantidade(2);
        passo.setBackground(Color.lightGray);
        velocidade.setBackground(Color.lightGray);
        JButton iniciar = new JButton("Iniciar");   //instanciando botao que inicializa a simulacao

        // adicionando Action Listener para o slider de quantidade (de pessoas a
        // entrarem na permutacao)
        quantidade.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                setQuantidade(quantidade.getValue());
                //passo.setMaximum(getQuantidade());
                quant.setText("Quantidade: " + getQuantidade());
                montarJogo(valores);
            }
        });

        // adicionando Action Listener para o slider de passo, setPasso(valor lido no
        // slider);
        passo.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                setPasso(passo.getValue());
                pass.setText("Passo: " + getPasso());
            }
        });

        // Action Listener para o slider da velocidade (usada no tempo da Permutacao);
        velocidade.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                setVelocidade(velocidade.getValue());
                tem.setText("Velocidade: " + getVelocidace());
            }
        });

        //Action Listener para iniciar a simulacao com os devidos parametros ao pressionar o JButton iniciar
        iniciar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                lock = true;
                frame.setResizable(false);
                quantidade.setEnabled(false);
                rodaJosephus();

            }
        });

        // Posicionando Labels, sliders e botoes no JPanel menu (gridBagLayout);
        gbc.weightx = 1;
        gbc.weighty = 1;

        gbc.gridx = 0;
        gbc.gridy = 0;
        menu.add(quant, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        menu.add(quantidade, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        menu.add(pass, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        menu.add(passo, gbc);

        gbc.gridx = 2;
        gbc.gridy = 0;
        menu.add(tem, gbc);

        gbc.gridx = 2;
        gbc.gridy = 1;
        menu.add(velocidade, gbc);

        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.gridheight = 2;
        gbc.fill = GridBagConstraints.BOTH;
        menu.add(iniciar, gbc);

        gbc.gridheight = GridBagConstraints.RELATIVE;
        gbc.gridwidth = 3;
        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.weighty = 0;
        gbc.anchor = GridBagConstraints.PAGE_START;
        gbc.ipady = 10;
        gbc.gridy = 0;
        painel.add(menu, gbc);
    }

    /**
     * Metodo utilizado para atualizar o JPanel quadrados com o valor do slider quantidade,
     * fornecido pelo usuario.
     */
    public void montarJogo(String[] valores) {
        //utilizado para atualizar o codigo da interface quando o josephus estiver rodando (true)
        if (lock == true) { 
            for (int aux = 0; aux < getQuantidade() - 1; aux++) { //Gera os quadrados
                if (lock == true) {
                    try {
                        TimeUnit.MILLISECONDS.sleep(1000 / getVelocidace());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Dimension size = frame.getSize();
                quadrados.removeAll();
                gbc.fill = GridBagConstraints.BOTH;
                int xMax = size.width / 50;
                GridBagConstraints c = new GridBagConstraints();
                int x = 0;
                int y = 0;
                int aux2 = 0;
                boolean vermelho = false;
                for (int i = 0; i < getQuantidade(); i++) { //Gera os quadrados pintados
                    JTextField tc = new JTextField();
                    tc.setPreferredSize(new Dimension(50, 50));
                    tc.setHorizontalAlignment(JTextField.CENTER);
                    tc.setBackground(Color.black);

                    if (valores != null && !vermelho) {
                        for (int j = 0; j <= aux; j++) {
                            if (Integer.parseInt(valores[j]) == i + 1) {
                                aux2++;
                                tc.setBackground(Color.red);
                                if (aux2 == aux + 1) {
                                    vermelho = true;
                                }
                            }
                        }
                    }
                    tc.setForeground(Color.white);
                    tc.setText("" + (i + 1));
                    tc.setEditable(false);
                    if (i % xMax == 0) {
                        y++;
                        x = 0;
                    }
                    c.gridx = x;
                    c.gridy = y;
                    x++;

                    quadrados.add(tc, c);

                }
                Graphics test = quadrados.getGraphics(); //Grava a posicao e cores dos quadrados
                quadrados.paintAll(test); //pinta os quadrados nas posicoes salvas

            }
            lock = false;
            frame.setResizable(true);
            quantidade.setEnabled(true);
        } else {
            Dimension size = frame.getSize();
                quadrados.removeAll();
                gbc.fill = GridBagConstraints.BOTH;
                int xMax = size.width / 50;
                GridBagConstraints c = new GridBagConstraints();
                int x = 0;
                int y = 0;
            for (int i = 0; i < getQuantidade(); i++) {
                
                JTextField tc = new JTextField();
                tc.setPreferredSize(new Dimension(50, 50));
                tc.setHorizontalAlignment(JTextField.CENTER);
                tc.setBackground(Color.black);
                tc.setForeground(Color.white);
                tc.setText("" + (i + 1));
                tc.setEditable(false);
                if (i % xMax == 0) {
                    y++;
                    x = 0;
                }
                c.gridx = x;
                c.gridy = y;
                x++;

                quadrados.add(tc, c);

            }
            Graphics test = quadrados.getGraphics();
            quadrados.paintAll(test);
            lock = false;

        }
    }

    /**
     * Metodo que temporiza a chamada de montarJogo() para refresh
     */
    static boolean cooldown = false;

    public void chamarMontarJogo() {
        if (cooldown)
            return;

        cooldown = true;
        montarJogo(valores);
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.schedule(Graphi::liberarMontarJogo, 100, TimeUnit.MILLISECONDS);
    }

    /**
     * Metodo para liberar a chamada do cooldawn
     */
    public static void liberarMontarJogo() {
        cooldown = false;
    }

    /**
     * Metodo para executar o codigo do josephus
     */
    public void rodaJosephus() {
        lista.limparLista();
        lista = new ListaDuplamenteLigadaCircular();
        for (int i = 1; i <= getQuantidade(); i++) {
            lista.inserirFim(i);
        }
        valores = RegrasJosephus.ResolverJoesephus(lista, getPasso());
        montarJogo(valores);
        valores = null;
        System.out.println(lista.toString());
    }
}