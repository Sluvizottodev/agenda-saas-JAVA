package br.cefet.agendasaas.utils;

import java.util.regex.Pattern;

public final class InputValidator {
    private static final Pattern EMAIL = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");

    private InputValidator() {
    }

    public static String requireNonBlank(String v, String fieldName) {
        if (v == null || v.trim().isEmpty())
            throw new ValidationException(fieldName + " é obrigatório");
        return v.trim();
    }

    public static int parsePositiveInt(String v, String fieldName) {
        try {
            int x = Integer.parseInt(requireNonBlank(v, fieldName));
            if (x <= 0)
                throw new ValidationException(fieldName + " deve ser um número positivo");
            return x;
        } catch (NumberFormatException e) {
            throw new ValidationException(fieldName + " deve ser um número inteiro");
        }
    }

    public static String validateEmail(String email) {
        String e = requireNonBlank(email, "E-mail");
        if (!EMAIL.matcher(e).matches())
            throw new ValidationException("E-mail inválido");
        return e;
    }
}
