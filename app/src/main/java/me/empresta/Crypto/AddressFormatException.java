package me.empresta.Crypto;

@SuppressWarnings("serial")
public class AddressFormatException extends Exception {
    public AddressFormatException() {
        super();
    }

    public AddressFormatException(String message) {
        super(message);
    }
}
