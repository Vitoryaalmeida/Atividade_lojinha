package br.com.lojinha.client;

import br.com.lojinha.exception.EstoqueInsuficienteException;
import br.com.lojinha.model.Cliente;
import br.com.lojinha.model.ItemPedido;
import br.com.lojinha.model.Pagamento;
import br.com.lojinha.model.PagamentoStatus;
import br.com.lojinha.model.Pedido;
import br.com.lojinha.model.Produto;
import br.com.lojinha.model.TipoPagamento;
import br.com.lojinha.server.LojinhaServer;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class LojinhaDesktopApp {
    private static final Color CANVAS = new Color(241, 244, 248);
    private static final Color TEXT_PRIMARY = new Color(28, 32, 39);
    private static final Color TEXT_SECONDARY = new Color(103, 112, 124);
    private static final Color TEXT_MUTED = new Color(138, 146, 157);
    private static final Color BLUE = new Color(115, 162, 255, 210);
    private static final Color AQUA = new Color(161, 219, 255, 175);
    private static final Color LILAC = new Color(209, 199, 255, 145);
    private static final Color PANEL_FILL = new Color(255, 255, 255, 156);
    private static final Color PANEL_BORDER = new Color(255, 255, 255, 190);
    private static final Color STRONG_PANEL = new Color(255, 255, 255, 210);
    private static final Color CHECKOUT_FILL = new Color(255, 255, 255, 132);
    private static final Color BUTTON_DARK = new Color(41, 50, 66);
    private static final Color BUTTON_LIGHT = new Color(255, 255, 255, 188);
    private static final Color SUCCESS = new Color(42, 132, 96);
    private static final Color ERROR = new Color(183, 68, 68);
    private static final NumberFormat CURRENCY = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("pt-BR"));

    private final LojinhaServer server;
    private final Map<Integer, JSpinner> quantidadePorProduto = new LinkedHashMap<>();
    private final Map<Integer, JLabel> estoquePorProduto = new LinkedHashMap<>();

    private JFrame frame;
    private JComboBox<Cliente> clienteComboBox;
    private JComboBox<TipoPagamento> tipoPagamentoComboBox;
    private JTextField tokenField;
    private JCheckBox forcarFalhaCheckBox;
    private JTextArea resumoArea;
    private JLabel totalLabel;
    private JLabel statusLabel;
    private JLabel itensSelecionadosLabel;

    public LojinhaDesktopApp(LojinhaServer server) {
        this.server = server;
    }

    public void mostrar() {
        frame = new JFrame("Lojinha Online");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(1320, 860));
        frame.setContentPane(criarConteudo());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private JPanel criarConteudo() {
        CanvasPanel root = new CanvasPanel();
        root.setLayout(new BorderLayout(22, 22));
        root.setBorder(new EmptyBorder(24, 24, 24, 24));
        root.add(criarTopBar(), BorderLayout.NORTH);
        root.add(criarCorpo(), BorderLayout.CENTER);
        return root;
    }

    private JPanel criarTopBar() {
        GlassPanel topBar = new GlassPanel(new BorderLayout(18, 0), STRONG_PANEL, 30, 0.34f);
        topBar.setBorder(new EmptyBorder(18, 24, 18, 24));

        JPanel left = transparente(new BorderLayout(0, 6));

        JPanel titleWrap = transparente();
        titleWrap.setLayout(new BoxLayout(titleWrap, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Lojinha Online");
        title.setFont(font(Font.BOLD, 34f));
        title.setForeground(TEXT_PRIMARY);

        JLabel subtitle = new JLabel(
                "<html><div style='width:560px'>Catálogo elegante, checkout translúcido e integração de pagamento centralizada em um monólito Java.</div></html>");
        subtitle.setFont(font(Font.PLAIN, 14f));
        subtitle.setForeground(TEXT_SECONDARY);

        titleWrap.add(title);
        titleWrap.add(Box.createVerticalStrut(8));
        titleWrap.add(subtitle);

        left.add(titleWrap, BorderLayout.CENTER);

        GlassPanel right = new GlassPanel(new BorderLayout(0, 10), new Color(255, 255, 255, 132), 26, 0.24f);
        right.setPreferredSize(new Dimension(320, 120));
        right.setBorder(new EmptyBorder(18, 18, 18, 18));

        JLabel chip = new JLabel("Gateway único");
        chip.setFont(font(Font.BOLD, 13f));
        chip.setForeground(new Color(71, 84, 101));

        JLabel body = new JLabel(
                "<html><div style='font-size:13px; color:#333a44;'><b>Singleton ativo</b><br/>A comunicação com o sistema externo permanece centralizada em uma única instância durante toda a execução.</div></html>");

        right.add(chip, BorderLayout.NORTH);
        right.add(body, BorderLayout.CENTER);

        topBar.add(left, BorderLayout.CENTER);
        topBar.add(right, BorderLayout.EAST);
        return topBar;
    }

    private JPanel criarCorpo() {
        JPanel content = transparente(new BorderLayout(22, 0));
        content.add(criarPainelCatalogo(), BorderLayout.CENTER);
        content.add(criarPainelCheckout(), BorderLayout.EAST);
        return content;
    }

    private JPanel criarPainelCatalogo() {
        GlassPanel panel = new GlassPanel(new BorderLayout(0, 18), PANEL_FILL, 34, 0.28f);
        panel.setBorder(new EmptyBorder(22, 22, 22, 22));

        JPanel header = transparente();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));

        JPanel metrics = transparente(new FlowLayout(FlowLayout.LEFT, 10, 0));
        metrics.add(criarInfoChip("Produtos", String.valueOf(server.listarProdutos().size())));
        metrics.add(criarInfoChip("Clientes", String.valueOf(server.listarClientes().size())));
        metrics.add(criarInfoChip("Fluxo", "Catálogo → Pedido → Pagamento"));

        JLabel title = new JLabel("Catálogo curado");
        title.setFont(font(Font.BOLD, 28f));
        title.setForeground(TEXT_PRIMARY);

        JLabel subtitle = new JLabel("Uma vitrine mais limpa, com foco em leitura, ritmo e espaço.");
        subtitle.setFont(font(Font.PLAIN, 14f));
        subtitle.setForeground(TEXT_SECONDARY);

        header.add(metrics);
        header.add(Box.createVerticalStrut(18));
        header.add(title);
        header.add(Box.createVerticalStrut(6));
        header.add(subtitle);

        JPanel cards = transparente(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 14, 0);

        quantidadePorProduto.clear();
        estoquePorProduto.clear();

        for (Produto produto : server.listarProdutos()) {
            cards.add(criarCardProduto(produto), gbc);
            gbc.gridy++;
        }

        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        cards.add(transparente(), gbc);

        JScrollPane scroll = new JScrollPane(cards);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(null);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        panel.add(header, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    private JPanel criarCardProduto(Produto produto) {
        GlassPanel card = new GlassPanel(new BorderLayout(18, 0), new Color(255, 255, 255, 130), 28, 0.2f);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(0, 0, 0, 0),
                new EmptyBorder(20, 20, 20, 20)));

        JPanel left = transparente();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));

        JPanel topLine = transparente(new FlowLayout(FlowLayout.LEFT, 10, 0));
        topLine.setAlignmentX(Component.LEFT_ALIGNMENT);
        topLine.add(criarCategoriaPill(produto.getEstoque() > 0 ? "Disponível" : "Sem estoque"));

        JLabel nome = new JLabel(produto.getNome());
        nome.setFont(font(Font.BOLD, 24f));
        nome.setForeground(TEXT_PRIMARY);
        nome.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel descricao = new JLabel("<html>" + produto.getDescricao() + "</html>");
        descricao.setFont(font(Font.PLAIN, 14f));
        descricao.setForeground(TEXT_SECONDARY);
        descricao.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel footer = transparente(new FlowLayout(FlowLayout.LEFT, 0, 0));
        JLabel preco = new JLabel(CURRENCY.format(produto.getPreco()));
        preco.setFont(font(Font.BOLD, 22f));
        preco.setForeground(new Color(46, 59, 84));

        JLabel divisor = new JLabel("  ·  ");
        divisor.setFont(font(Font.PLAIN, 14f));
        divisor.setForeground(TEXT_MUTED);

        JLabel estoque = new JLabel("Estoque: " + produto.getEstoque());
        estoque.setFont(font(Font.PLAIN, 13f));
        estoque.setForeground(TEXT_SECONDARY);

        footer.add(preco);
        footer.add(divisor);
        footer.add(estoque);
        footer.setAlignmentX(Component.LEFT_ALIGNMENT);

        left.add(topLine);
        left.add(Box.createVerticalStrut(12));
        left.add(nome);
        left.add(Box.createVerticalStrut(8));
        left.add(descricao);
        left.add(Box.createVerticalStrut(16));
        left.add(footer);

        JPanel controls = transparente();
        controls.setLayout(new BoxLayout(controls, BoxLayout.Y_AXIS));

        JLabel label = new JLabel("Quantidade");
        label.setFont(font(Font.BOLD, 12f));
        label.setForeground(TEXT_MUTED);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);

        JSpinner spinner = new JSpinner(new SpinnerNumberModel(0, 0, produto.getEstoque(), 1));
        spinner.setPreferredSize(new Dimension(84, 36));
        spinner.setMaximumSize(new Dimension(84, 36));
        estilizarCampo(spinner);

        controls.add(Box.createVerticalGlue());
        controls.add(label);
        controls.add(Box.createVerticalStrut(10));
        controls.add(spinner);
        controls.add(Box.createVerticalGlue());

        quantidadePorProduto.put(produto.getId(), spinner);
        estoquePorProduto.put(produto.getId(), estoque);

        card.add(left, BorderLayout.CENTER);
        card.add(controls, BorderLayout.EAST);
        return card;
    }

    private JPanel criarPainelCheckout() {
        GlassPanel panel = new GlassPanel(new BorderLayout(0, 18), CHECKOUT_FILL, 34, 0.32f);
        panel.setPreferredSize(new Dimension(390, 0));
        panel.setBorder(new EmptyBorder(22, 22, 22, 22));

        JPanel content = transparente();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        JPanel heading = transparente();
        heading.setLayout(new BoxLayout(heading, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Checkout");
        title.setFont(font(Font.BOLD, 28f));
        title.setForeground(TEXT_PRIMARY);

        JLabel subtitle = new JLabel("<html>Revise a compra e envie para o gateway com o mínimo de ruído visual.</html>");
        subtitle.setFont(font(Font.PLAIN, 14f));
        subtitle.setForeground(TEXT_SECONDARY);

        heading.add(title);
        heading.add(Box.createVerticalStrut(6));
        heading.add(subtitle);

        GlassPanel summaryCard = new GlassPanel(new BorderLayout(0, 8), new Color(255, 255, 255, 118), 24, 0.18f);
        summaryCard.setBorder(new EmptyBorder(16, 16, 16, 16));

        JLabel summaryTitle = new JLabel("Resumo dinâmico");
        summaryTitle.setFont(font(Font.BOLD, 13f));
        summaryTitle.setForeground(TEXT_MUTED);

        totalLabel = new JLabel("Total selecionado: " + CURRENCY.format(BigDecimal.ZERO));
        totalLabel.setFont(font(Font.BOLD, 18f));
        totalLabel.setForeground(TEXT_PRIMARY);

        itensSelecionadosLabel = new JLabel("Nenhum item no carrinho");
        itensSelecionadosLabel.setFont(font(Font.PLAIN, 13f));
        itensSelecionadosLabel.setForeground(TEXT_SECONDARY);

        summaryCard.add(summaryTitle, BorderLayout.NORTH);

        JPanel summaryBody = transparente();
        summaryBody.setLayout(new BoxLayout(summaryBody, BoxLayout.Y_AXIS));
        summaryBody.add(totalLabel);
        summaryBody.add(Box.createVerticalStrut(6));
        summaryBody.add(itensSelecionadosLabel);
        summaryCard.add(summaryBody, BorderLayout.CENTER);

        clienteComboBox = new JComboBox<>(new DefaultComboBoxModel<>(server.listarClientes().toArray(Cliente[]::new)));
        tipoPagamentoComboBox = new JComboBox<>(TipoPagamento.values());
        tokenField = new JTextField("token-aprovado");
        forcarFalhaCheckBox = new JCheckBox("Simular recusa do gateway");
        forcarFalhaCheckBox.setOpaque(false);
        forcarFalhaCheckBox.setFont(font(Font.PLAIN, 13f));
        forcarFalhaCheckBox.setForeground(TEXT_SECONDARY);
        forcarFalhaCheckBox.addActionListener(evento ->
                tokenField.setText(forcarFalhaCheckBox.isSelected() ? "recusar-pagamento" : "token-aprovado"));

        estilizarCampo(clienteComboBox);
        estilizarCampo(tipoPagamentoComboBox);
        estilizarCampo(tokenField);

        JPanel form = transparente();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.add(criarCampo("Cliente", clienteComboBox));
        form.add(Box.createVerticalStrut(14));
        form.add(criarCampo("Pagamento", tipoPagamentoComboBox));
        form.add(Box.createVerticalStrut(14));
        form.add(criarCampo("Token externo", tokenField));
        form.add(Box.createVerticalStrut(8));
        form.add(forcarFalhaCheckBox);

        statusLabel = new JLabel("Selecione os itens para atualizar o resumo.");
        statusLabel.setFont(font(Font.PLAIN, 13f));
        statusLabel.setForeground(TEXT_SECONDARY);

        JPanel actions = transparente(new FlowLayout(FlowLayout.LEFT, 10, 0));
        JButton calcular = criarBotao("Atualizar resumo", BUTTON_LIGHT, TEXT_PRIMARY, false);
        JButton finalizar = criarBotao("Finalizar compra", BUTTON_DARK, Color.WHITE, true);
        calcular.addActionListener(evento -> atualizarResumoParcial());
        finalizar.addActionListener(evento -> finalizarCompra());
        actions.add(calcular);
        actions.add(finalizar);

        resumoArea = new JTextArea(16, 24);
        resumoArea.setEditable(false);
        resumoArea.setLineWrap(true);
        resumoArea.setWrapStyleWord(true);
        resumoArea.setFont(font(Font.PLAIN, 13f));
        resumoArea.setForeground(TEXT_PRIMARY);
        resumoArea.setBackground(new Color(255, 255, 255, 150));
        resumoArea.setBorder(new EmptyBorder(18, 18, 18, 18));
        resumoArea.setText("Nenhum item selecionado ainda.");

        JScrollPane resumoScroll = new JScrollPane(resumoArea);
        resumoScroll.setBorder(BorderFactory.createEmptyBorder());
        resumoScroll.setOpaque(false);
        resumoScroll.getViewport().setOpaque(false);
        resumoScroll.setPreferredSize(new Dimension(0, 320));

        GlassPanel resumoCard = new GlassPanel(new BorderLayout(), new Color(255, 255, 255, 116), 24, 0.16f);
        resumoCard.setBorder(new EmptyBorder(0, 0, 0, 0));
        resumoCard.add(resumoScroll, BorderLayout.CENTER);

        content.add(heading);
        content.add(Box.createVerticalStrut(18));
        content.add(summaryCard);
        content.add(Box.createVerticalStrut(18));
        content.add(form);
        content.add(Box.createVerticalStrut(18));
        content.add(statusLabel);
        content.add(Box.createVerticalStrut(16));
        content.add(actions);
        content.add(Box.createVerticalStrut(18));
        content.add(resumoCard);

        panel.add(content, BorderLayout.CENTER);
        return panel;
    }

    private JPanel criarCampo(String titulo, JComponent component) {
        JPanel panel = transparente();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel label = new JLabel(titulo);
        label.setFont(font(Font.BOLD, 12f));
        label.setForeground(TEXT_MUTED);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        component.setAlignmentX(Component.LEFT_ALIGNMENT);
        component.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        component.setPreferredSize(new Dimension(300, 38));

        panel.add(label);
        panel.add(Box.createVerticalStrut(8));
        panel.add(component);
        return panel;
    }

    private void estilizarCampo(JComponent component) {
        component.setFont(font(Font.PLAIN, 13f));
        component.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 255, 255, 175), 1),
                new EmptyBorder(8, 10, 8, 10)));
        component.setOpaque(true);
        component.setBackground(new Color(255, 255, 255, 170));
        component.setForeground(TEXT_PRIMARY);
    }

    private JButton criarBotao(String text, Color background, Color foreground, boolean filled) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setFont(font(Font.BOLD, 13f));
        button.setForeground(foreground);
        button.setBackground(background);
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setContentAreaFilled(true);
        button.setBorder(new EmptyBorder(11, 16, 11, 16));
        if (!filled) {
            button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(255, 255, 255, 160), 1),
                    new EmptyBorder(10, 15, 10, 15)));
        }
        return button;
    }

    private JPanel criarInfoChip(String title, String value) {
        GlassPanel chip = new GlassPanel(new BorderLayout(0, 2), new Color(255, 255, 255, 122), 22, 0.14f);
        chip.setBorder(new EmptyBorder(10, 12, 10, 12));

        JLabel top = new JLabel(title);
        top.setFont(font(Font.BOLD, 11f));
        top.setForeground(TEXT_MUTED);

        JLabel bottom = new JLabel(value);
        bottom.setFont(font(Font.PLAIN, 13f));
        bottom.setForeground(TEXT_PRIMARY);

        chip.add(top, BorderLayout.NORTH);
        chip.add(bottom, BorderLayout.CENTER);
        return chip;
    }

    private JLabel criarCategoriaPill(String texto) {
        JLabel label = new JLabel(texto);
        label.setOpaque(true);
        label.setFont(font(Font.BOLD, 11f));
        label.setForeground(new Color(69, 91, 113));
        label.setBackground(new Color(239, 247, 255, 170));
        label.setBorder(new EmptyBorder(6, 10, 6, 10));
        return label;
    }

    private void atualizarResumoParcial() {
        Map<Produto, Integer> selecao = coletarSelecaoAtual();
        if (selecao.isEmpty()) {
            totalLabel.setText("Total selecionado: " + CURRENCY.format(BigDecimal.ZERO));
            itensSelecionadosLabel.setText("Nenhum item no carrinho");
            statusLabel.setForeground(TEXT_SECONDARY);
            statusLabel.setText("Selecione os itens para atualizar o resumo.");
            resumoArea.setText("Nenhum item selecionado ainda.");
            return;
        }

        StringBuilder resumo = new StringBuilder();
        BigDecimal total = BigDecimal.ZERO;
        int itens = 0;

        for (Map.Entry<Produto, Integer> entry : selecao.entrySet()) {
            BigDecimal subtotal = entry.getKey().getPreco().multiply(BigDecimal.valueOf(entry.getValue()));
            total = total.add(subtotal);
            itens += entry.getValue();
            resumo.append("• ")
                    .append(entry.getKey().getNome())
                    .append(" × ")
                    .append(entry.getValue())
                    .append("   ")
                    .append(CURRENCY.format(subtotal))
                    .append("\n");
        }

        resumo.append("\nTotal previsto: ").append(CURRENCY.format(total));
        totalLabel.setText("Total selecionado: " + CURRENCY.format(total));
        itensSelecionadosLabel.setText(itens + " item(ns) selecionado(s)");
        statusLabel.setForeground(TEXT_SECONDARY);
        statusLabel.setText("Resumo atualizado. Checkout pronto.");
        resumoArea.setText(resumo.toString());
    }

    private void finalizarCompra() {
        Map<Produto, Integer> selecao = coletarSelecaoAtual();
        if (selecao.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Selecione pelo menos um produto.", "Carrinho vazio",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        Cliente cliente = (Cliente) clienteComboBox.getSelectedItem();
        TipoPagamento tipoPagamento = (TipoPagamento) tipoPagamentoComboBox.getSelectedItem();

        if (cliente == null || tipoPagamento == null) {
            JOptionPane.showMessageDialog(frame, "Selecione cliente e forma de pagamento.", "Dados incompletos",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        Map<Integer, Integer> itens = new LinkedHashMap<>();
        for (Map.Entry<Produto, Integer> entry : selecao.entrySet()) {
            itens.put(entry.getKey().getId(), entry.getValue());
        }

        try {
            Pedido pedido = server.criarPedido(cliente, itens);
            Pagamento pagamento = server.processarPagamento(pedido, tipoPagamento, tokenField.getText());
            atualizarTelaAposPedido(pedido, pagamento);
        } catch (EstoqueInsuficienteException exception) {
            statusLabel.setForeground(ERROR);
            statusLabel.setText("Falha ao criar pedido.");
            JOptionPane.showMessageDialog(frame, exception.getMessage(), "Estoque insuficiente",
                    JOptionPane.ERROR_MESSAGE);
            recarregarProdutos();
        }
    }

    private void atualizarTelaAposPedido(Pedido pedido, Pagamento pagamento) {
        StringBuilder resumo = new StringBuilder();
        resumo.append("Pedido #").append(pedido.getId()).append("\n");
        resumo.append("Cliente: ").append(pedido.getCliente().getNome()).append("\n");
        resumo.append("Data: ")
                .append(pedido.getDataPedido().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")))
                .append("\n\nItens\n");

        int itens = 0;
        for (ItemPedido item : pedido.getItens()) {
            itens += item.getQuantidade();
            resumo.append("• ")
                    .append(item.getProduto().getNome())
                    .append(" × ")
                    .append(item.getQuantidade())
                    .append("   ")
                    .append(CURRENCY.format(item.getSubtotal()))
                    .append("\n");
        }

        resumo.append("\nTotal: ").append(CURRENCY.format(pedido.getValorTotal()));
        resumo.append("\nPagamento: ").append(pagamento.getTipoPagamento());
        resumo.append("\nStatus do pagamento: ").append(pagamento.getStatusPagamento());
        resumo.append("\nStatus do pedido: ").append(pedido.getStatusPedido());

        resumoArea.setText(resumo.toString());
        totalLabel.setText("Total selecionado: " + CURRENCY.format(pedido.getValorTotal()));
        itensSelecionadosLabel.setText(itens + " item(ns) processado(s)");

        if (pagamento.getStatusPagamento() == PagamentoStatus.APROVADO) {
            statusLabel.setForeground(SUCCESS);
            statusLabel.setText("Pagamento aprovado. Pedido confirmado.");
        } else {
            statusLabel.setForeground(ERROR);
            statusLabel.setText("Pagamento recusado pelo gateway externo.");
        }

        limparSelecao();
        recarregarProdutos();
    }

    private Map<Produto, Integer> coletarSelecaoAtual() {
        Map<Produto, Integer> selecao = new LinkedHashMap<>();
        for (Produto produto : server.listarProdutos()) {
            JSpinner spinner = quantidadePorProduto.get(produto.getId());
            if (spinner == null) {
                continue;
            }
            int quantidade = (int) spinner.getValue();
            if (quantidade > 0) {
                selecao.put(produto, quantidade);
            }
        }
        return selecao;
    }

    private void limparSelecao() {
        for (JSpinner spinner : quantidadePorProduto.values()) {
            spinner.setValue(0);
        }
    }

    private void recarregarProdutos() {
        for (Produto produto : server.listarProdutos()) {
            JLabel estoque = estoquePorProduto.get(produto.getId());
            JSpinner spinner = quantidadePorProduto.get(produto.getId());
            if (estoque != null) {
                estoque.setText("Estoque: " + produto.getEstoque());
            }
            if (spinner != null) {
                spinner.setModel(new SpinnerNumberModel(0, 0, produto.getEstoque(), 1));
                estilizarCampo(spinner);
            }
        }
    }

    private JPanel transparente() {
        return transparente(new BorderLayout());
    }

    private JPanel transparente(LayoutManager layout) {
        JPanel panel = new JPanel(layout);
        panel.setOpaque(false);
        return panel;
    }

    private Font font(int style, float size) {
        String[] families = {
            ".AppleSystemUIFont",
            ".SF NS Text",
            ".SF NS Display",
            "Helvetica Neue",
            "Helvetica",
            "Arial",
            Font.SANS_SERIF
        };

        for (String family : families) {
            Font candidate = new Font(family, style, Math.round(size));
            if (candidate != null && candidate.getFamily() != null && !Font.DIALOG.equals(candidate.getFamily())) {
                return candidate.deriveFont(style, size);
            }
        }

        return new Font(Font.SANS_SERIF, style, Math.round(size)).deriveFont(style, size);
    }

    private static final class CanvasPanel extends JPanel {
        private CanvasPanel() {
            setOpaque(true);
            setBackground(CANVAS);
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);
            Graphics2D g2 = (Graphics2D) graphics.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Paint bg = new GradientPaint(0, 0, new Color(248, 250, 253), getWidth(), getHeight(), new Color(232, 237, 244));
            g2.setPaint(bg);
            g2.fillRect(0, 0, getWidth(), getHeight());

            g2.setPaint(BLUE);
            g2.fill(new Ellipse2D.Float(-120, -80, 420, 420));

            g2.setPaint(AQUA);
            g2.fill(new Ellipse2D.Float(getWidth() - 360, 60, 300, 300));

            g2.setPaint(LILAC);
            g2.fill(new Ellipse2D.Float(getWidth() / 2f - 170, getHeight() - 260, 360, 220));

            g2.dispose();
        }
    }

    private static final class GlassPanel extends JPanel {
        private final Color fill;
        private final int radius;
        private final float borderOpacity;

        private GlassPanel(BorderLayout layout, Color fill, int radius, float borderOpacity) {
            super(layout);
            this.fill = fill;
            this.radius = radius;
            this.borderOpacity = borderOpacity;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            Graphics2D g2 = (Graphics2D) graphics.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            RoundRectangle2D shape = new RoundRectangle2D.Float(0.5f, 0.5f, getWidth() - 1f, getHeight() - 1f, radius, radius);
            g2.setColor(fill);
            g2.fill(shape);

            g2.setStroke(new BasicStroke(1.1f));
            g2.setColor(new Color(PANEL_BORDER.getRed(), PANEL_BORDER.getGreen(), PANEL_BORDER.getBlue(),
                    Math.min(255, Math.round(255 * borderOpacity))));
            g2.draw(shape);
            g2.dispose();
            super.paintComponent(graphics);
        }
    }
}
