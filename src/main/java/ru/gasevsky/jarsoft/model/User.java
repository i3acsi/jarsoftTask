package ru.gasevsky.jarsoft.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class User {
    private String ipAddress;
    private String userAgent;
}
