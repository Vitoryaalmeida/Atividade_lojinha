package br.com.lojinha;

import br.com.lojinha.client.LojinhaClientApp;
import br.com.lojinha.client.LojinhaDesktopApp;
import br.com.lojinha.server.LojinhaServer;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {
    public static void main(String[] args) {
        LojinhaServer server = new LojinhaServer();

        if (args.length > 0 && "--console".equalsIgnoreCase(args[0])) {
            boolean forcarFalha = args.length > 1 && "--falha".equalsIgnoreCase(args[1]);
            LojinhaClientApp clientApp = new LojinhaClientApp(server);
            clientApp.executarSimulacao(forcarFalha);
            return;
        }

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }

        SwingUtilities.invokeLater(() -> new LojinhaDesktopApp(server).mostrar());
    }
}
