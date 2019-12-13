package edgeConnectivity;

class IncorrectGraphEntryException extends GraphException {
    IncorrectGraphEntryException() {
        super("Неверный ввод графа");
    }
}
