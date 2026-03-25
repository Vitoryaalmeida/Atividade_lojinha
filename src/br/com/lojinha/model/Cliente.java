package br.com.lojinha.model;

public class Cliente {
    private final int id;
    private final String nome;
    private final String email;
    private final String senhaHash;

    public Cliente(int id, String nome, String email, String senhaHash) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senhaHash = senhaHash;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getSenhaHash() {
        return senhaHash;
    }

    @Override
    public String toString() {
        return "Cliente{id=%d, nome='%s', email='%s'}".formatted(id, nome, email);
    }
}
