package edgeConnectivity;

class InputFileIsEmptyException extends GraphException {
    InputFileIsEmptyException() {
        super("Файл ввода пуст");
    }
}
