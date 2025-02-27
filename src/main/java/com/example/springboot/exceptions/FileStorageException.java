package com.example.springboot.exceptions;

import java.io.IOException;

public class FileStorageException extends Throwable {
    public FileStorageException(String s, IOException ex) {
        super(s, ex);
    }
}
