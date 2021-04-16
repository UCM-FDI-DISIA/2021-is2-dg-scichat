package utils;

public enum Mode {
    Traditional("Modo Tradicional"),
    Fast("Modo rápido");

    private final String name;

    Mode(String _name) {
        this.name = _name;
    }

    public String getName() {
        return name;
    }
}
