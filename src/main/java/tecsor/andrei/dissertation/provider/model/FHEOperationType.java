package tecsor.andrei.dissertation.provider.model;

public enum FHEOperationType {
    ENCRYPT(0),
    DECRYPT(1);

    public final int value;

    FHEOperationType(int value) {
        this.value = value;
    }
}
