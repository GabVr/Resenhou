package com.example.Resenhou.Usuarios.Service;

import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final Resend resend;

    public EmailService(@Value("${resend.api-key}") String apiKey) {
        this.resend = new Resend(apiKey);
    }

    public CreateEmailResponse enviarEmail(
            String destinatario,
            String assunto,
            String mensagem
    ) throws ResendException {

        CreateEmailOptions params = CreateEmailOptions.builder()
                .from("onboarding@resend.dev")
                .to(destinatario)
                .subject(assunto)
                .html(mensagem)
                .build();

        return resend.emails().send(params);
    }
}