package com.c0rdination.openwidgets.network;

public enum ServerPacket {
    PACKET_DISCONNECTED((short) 2),
    PACKET_HANDSHAKE((short) 3),              // Рукопожатие S -> C => C -> S (Instance UUID)
    PACKET_LOGS_UPLOADING_REQUEST((short) 4), // Запрос на получение логов S -> C
    PACKET_LOGS_UPLOADING((short) 5),         // Пакет отправки логов C -> S (LOGS)
    PACKET_LOGS_CLEARING_REQUEST((short) 6),  // Запрос на удаление логов
    PACKET_UNKNOWN((short) 7),                // От собеседника получен неизвестный пакет (Полученный пакет)
    PACKET_ERROR_CAUSED_CLIENT((short) 8),    // Ошибка на стороне сервера вызванная клиентом S -> C
    PACKET_ERROR_CAUSED_SERVER((short) 9),    // Ошибка на стороне клиента вызванная сервером C -> S
    PACKET_TEST((short) 10);                  // Тестовый пакет.

    short packetId;
    ServerPacket(short packetId) {
        this.packetId = packetId;
    }

    public short getPacketId() {
        return packetId;
    }
}
