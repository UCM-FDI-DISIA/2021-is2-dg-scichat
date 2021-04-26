const WebSocket = require('ws');

const wss = new WebSocket.Server({port: 8080});
console.log(`WebSocket server on 8080`);

let clients = [];

wss.on('connection', (ws) => {
    ws.send(JSON.stringify({
        type: 'PING'
    }));

    for (const client of clients) {
        client.send(JSON.stringify({
            type: "CLIENT_CONNECTED"
        }));
    }

    clients.push(ws);
    console.log(`Client #${clients.length} connected`);

    ws.on("close", (code, reason) => {
        const _index = clients.indexOf(ws);
        if (_index != 1) {
            clients.splice(_index, 1);
            console.log(`Client #${_index} disconnected`);
        }

        for (const client of clients) {
            client.send(JSON.stringify({
                type: "CLIENT_DISCONNECTED"
            }));
        }
    })

    ws.on('message', (message) => {
        console.log('received: %s', message);
        /// Enviar a todos los clientes el mismo mensaje

        for (const client of clients) {
            if (client == ws) continue;
            client.send(message);
        }
    });
});