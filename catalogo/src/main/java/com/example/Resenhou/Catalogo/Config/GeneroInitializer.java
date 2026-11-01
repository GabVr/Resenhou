package com.example.Resenhou.Catalogo.Config;

import com.example.Resenhou.Catalogo.Model.Genero;
import com.example.Resenhou.Catalogo.Repository.GeneroRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class GeneroInitializer implements CommandLineRunner {

    private final GeneroRepository generoRepository;

    public GeneroInitializer(GeneroRepository generoRepository) {
        this.generoRepository = generoRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        // O catálogo usa esta coleção também para categorias e palavras-chave.
        List<String> nomes = List.of(
                // Gêneros e formas literárias
                "Romance",
                "Drama",
                "Aventura",
                "Fantasia",
                "Alta fantasia",
                "Fantasia urbana",
                "Romantasia",
                "Ficção científica",
                "Cyberpunk",
                "Space opera",
                "Terror",
                "Terror psicológico",
                "Suspense",
                "Mistério",
                "Policial",
                "Ficção histórica",
                "Ficção contemporânea",
                "Distopia",
                "Utopia",
                "Realismo mágico",
                "Romance histórico",
                "Comédia romântica",
                "Romance de época",
                "Romance paranormal",
                "Poesia",
                "Contos",
                "Crônicas",
                "Teatro",
                "Fábulas",
                "Ensaios",
                "Biografia",
                "Autobiografia",
                "Memórias",
                "Reportagem",
                "Humor",
                "Sátira",
                "Quadrinhos",
                "Mangá",
                "Graphic novel",

                // Públicos e categorias editoriais
                "Literatura infantil",
                "Literatura juvenil",
                "Young adult",
                "New adult",
                "Clássicos",
                "Literatura brasileira",
                "Literatura portuguesa",
                "Literatura latino-americana",
                "Literatura africana",
                "Literatura asiática",
                "Literatura indígena",
                "Literatura afro-brasileira",

                // Assuntos de não ficção
                "História",
                "Filosofia",
                "Psicologia",
                "Ciência",
                "Divulgação científica",
                "Autoajuda",
                "Desenvolvimento pessoal",
                "Negócios",
                "Educação",
                "Religião",
                "Espiritualidade",
                "Viagem",
                "Folclore",
                "Mitologia",
                "Política",
                "Sociologia",
                "Antropologia",
                "Economia",
                "Tecnologia",
                "Computação",
                "Artes",
                "Música",
                "Cinema",
                "Culinária",
                "Saúde",
                "Esportes",
                "Direito",
                "Meio ambiente",
                "Sustentabilidade",

                // Temas e representatividade: palavras-chave, não gêneros formais
                "Literatura LGBTQIA+",
                "Romance LGBTQIA+",
                "Representatividade lésbica",
                "Representatividade gay",
                "Representatividade bissexual",
                "Representatividade trans",
                "Representatividade não binária",
                "Diversidade de gênero",
                "Identidade e pertencimento",
                "Feminismo",
                "Antirracismo",
                "Representatividade negra",
                "Povos indígenas",
                "Representatividade de pessoas com deficiência",
                "Neurodiversidade",
                "Saúde mental",
                "Amizade",
                "Família",
                "Amadurecimento",
                "Luto",
                "Imigração",
                "Desigualdade social",

                // Palavras-chave de enredo usadas para descobrir romances
                "Enemies to lovers",
                "Friends to lovers",
                "Slow burn",
                "Relacionamento de fachada",
                "Segunda chance",
                "Amor proibido",
                "Família encontrada"
        );

        for (String nome : nomes) {
            if (generoRepository.findByNomeIgnoreCase(nome).isEmpty()) {
                Genero genero = new Genero();
                genero.setNome(nome);
                genero.setDescricao("Livros associados à categoria ou palavra-chave: " + nome);
                generoRepository.save(genero);
            }
        }
    }
}
