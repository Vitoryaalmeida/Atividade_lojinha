# Lojinha Online em Java

Simulação de uma lojinha online baseada em uma arquitetura cliente-servidor monolítica. O projeto entrega os artefatos pedidos na atividade: diagrama de atividades, DER e código-fonte Java com processamento de pagamento via Singleton, agora com interface gráfica desktop em Swing.

## Estrutura do projeto

```text
.
├── docs/diagrams
│   ├── atividade-lojinha.svg
│   └── der-lojinha.svg
└── src/br/com/lojinha
    ├── Main.java
    ├── client
    ├── exception
    ├── integration
    ├── model
    ├── repository
    ├── server
    └── service
```

## Principais decisões arquiteturais

- O cliente foi representado por duas entradas: `LojinhaDesktopApp` para a interface gráfica e `LojinhaClientApp` para a simulação via console.
- O backend monolítico foi centralizado na classe `LojinhaServer`, responsável por concentrar autenticação, catálogo, pedidos e pagamentos em uma única aplicação servidora.
- Os dados são mantidos em memória por repositórios simples (`ClienteRepository`, `ProdutoRepository`, `PedidoRepository`), suficientes para a simulação pedida.
- O fluxo principal cobre identificação de cliente, listagem de produtos, criação de pedido, comunicação com o sistema externo e retorno do status do pagamento.
- A interface gráfica foi construída em Swing para evitar dependências externas e permitir execução direta com o JDK local do projeto.

## Singleton no sistema de pagamento

O padrão Singleton foi aplicado em `GatewayPagamentoSingleton`.

Justificativa:

- A atividade exige apenas uma instância responsável pela comunicação com o serviço de pagamento durante toda a execução.
- Centralizar essa integração evita múltiplas conexões redundantes com o sistema externo.
- Também facilita manter uma única porta de acesso para regras de comunicação e futuras configurações do gateway.

No código, a instância é obtida por `GatewayPagamentoSingleton.getInstance()` dentro do servidor monolítico.

## Como executar

### Interface gráfica

```bash
./run.sh
```

Se o projeto ainda não tiver um JDK disponível, rode antes:

```bash
./setup-java.sh
```

### Modo console

```bash
./run.sh --console
./run.sh --console --falha
```

### Compilar manualmente

```bash
mkdir -p out
javac -d out $(find src -name "*.java")
```

### Executar interface gráfica manualmente

```bash
java -cp out br.com.lojinha.Main
```

### Executar console manualmente

```bash
java -cp out br.com.lojinha.Main --console
java -cp out br.com.lojinha.Main --console --falha
```

## Fluxo simulado

1. O cliente é identificado a partir de um cadastro estático.
2. O servidor lista os produtos disponíveis.
3. O cliente seleciona produtos e o pedido é criado.
4. O servidor aciona o gateway externo de pagamento.
5. O sistema retorna confirmação ou falha do pagamento.

## Interface gráfica

- Seleção de cliente por combo box.
- Catálogo visual com cards de produto, preço, descrição e estoque.
- Checkout lateral com forma de pagamento, token externo e opção de simular falha.
- Resumo visual do pedido com status final do pagamento e do pedido.

## Diagramas

- Diagrama de atividades: [docs/diagrams/atividade-lojinha.svg](/Users/winistonalle/Desktop/atividade-lojinha/docs/diagrams/atividade-lojinha.svg)
- DER: [docs/diagrams/der-lojinha.svg](/Users/winistonalle/Desktop/atividade-lojinha/docs/diagrams/der-lojinha.svg)
