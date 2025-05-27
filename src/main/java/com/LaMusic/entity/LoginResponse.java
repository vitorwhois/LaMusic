package com.LaMusic.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor // Simplifica, já que os outros construtores podem ser inferidos ou criados no serviço
public class LoginResponse {

    private boolean success;
    private String message; // Usado para mensagens de erro ou sucesso
    private User user; // O tipo User aqui será o atualizado com UUID

    // Construtor para sucesso
    public LoginResponse(boolean success, User user) {
        this.success = success;
        this.user = user;
        if (success) {
            this.message = "Login successful."; // Mensagem padrão de sucesso
        }
    }

    // Construtor para falha
    public LoginResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
        this.user = null; // Garante que não haja usuário em caso de falha
    }

    public boolean isSuccess() {
        return success;
    }
}