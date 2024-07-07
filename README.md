# Catalog Service

O serviço de Catálogo visa automatizar o gerenciamento dos produtos, categorias e ofertas de lojas.

[Click aqui](./docs/README.md) para saber mais sobre regras de negócio em implementações.

## Como executar o projeto

Na pasta do projeto, execute os seguintes comandos para realizar a build da aplicação:

```bash
./gradlew build -x check
```

Após a conclusão do build, execute o seguinte comando para subir localmente a base de dados e a aplicação:

```bash
docker-compose up -d application
```

### Nota:

Caso a aplicação tenha sido modificada, é necessário realizar novamente os passos anteriores.

Caso o arquivo Dockerfile tenha sido alterado, um novo build da imagem deverá ser realizado para que a alteração surta
efeito.
Para isso execute o seguinte comando:

```bash
docker-compose build --no-cache application
```

## Mais sobre o projeto

O projeto foi criado com o intuito de testar e colocar em prática os conhecimentos adquiridos ao longo da minha
carreira.

O tema Catálogo de Lojas foi escolhido por ser um desafio pessoal e para medir minha evolução pessoal, pois por ser um
tema onde já trabalhei, fica mais claro a minha evolução.

A arquitetura escolhida para desenvolvimento do projeto foi a Arquitetura Hexagonal.

Além dos desafios de implementar uma nova arquitetura de software, também foi autoimposto um desafio de documentar e
realizar testes na aplicação mantendo um alto nível de cobertura de código além de usar a
ferramenta [pitest](https://pitest.org/) para assegurar que os testes estavam sendo de fato efetivos.

Manter o código limpo e aderente a boas práticas de programação não é uma tarefa fácil, por isso
o [detekt](https://detekt.dev/) foi utilizado no projeto para manter o padrão durante a codificação.