package dev.viniciussr.gamerental.enums;

/**
 * Representa as plataformas disponíveis para os jogos cadastrados no sistema.
 * <p>
 * Utilizado para categorizar os jogos conforme o tipo de plataforma ou dispositivo
 * em que podem ser executados.
 * </p>
 */
public enum Platforms {

    /**
     * PlayStation.
     */
    PLAYSTATION,

    /**
     * Xbox.
     */
    XBOX,

    /**
     * Nintendo.
     */
    NINTENDO,

    /**
     * Arcade (fliperamas).
     */
    ARCADE,

    /**
     * Mobile (smartphones, tablets).
     */
    MOBILE,

    /**
     * PCs (desktop, notebooks).
     */
    PC,

    /**
     * Consoles de realidade virtual (VR).
     */
    VR,

    /**
     * Outras plataformas não especificadas.
     */
    OTHER
}