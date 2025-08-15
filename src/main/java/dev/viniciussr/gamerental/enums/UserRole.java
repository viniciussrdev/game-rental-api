package dev.viniciussr.gamerental.enums;

/**
 * Representa os perfis de acesso de um usuário no sistema.
 * <p>
 * Utilizado para controle de permissões e autorização.
 * </p>
 */
public enum UserRole {

    /**
     * Acesso administrativo, com permissões completas.
     */
    ADMIN,

    /**
     * Acesso padrão, com permissões limitadas.
     */
    USER
}