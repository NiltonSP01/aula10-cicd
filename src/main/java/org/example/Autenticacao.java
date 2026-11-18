package org.example;

public class Autenticacao {

    private String usuarioCadastrado;
    private String senhaCadastrada;


    public void cadastrar(String usuario, String senha) {

        this.usuarioCadastrado = usuario;
        this.senhaCadastrada = senha;
    }


    public boolean login(String usuario, String senha) {

        return usuarioCadastrado.equals(usuario)
                && senhaCadastrada.equals(senha);
    }

}