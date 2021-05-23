package network.models;

import org.json.JSONObject;

/**
 * Interfaz para aquellos modelos que puede ser enviado por Socket
 * Exige la implementación del método toJSON para serializar en JSON
 */
public interface SocketMessage {
    JSONObject toJSON();
}
