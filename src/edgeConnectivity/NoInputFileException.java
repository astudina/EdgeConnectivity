package edgeConnectivity;

class NoInputFileException extends GraphException {
    NoInputFileException() {
        super("Нет файла ввода");
    }
}
