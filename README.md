# Catalog Service

O serviço de Catálogo visa automatizar o gerenciamento de produtos, categorias e ofertas de lojas.

[Click aqui](./docs/README.md) para saber mais sobre as regras de negócio em implementações.

## Como executar o projeto

Na pasta do projeto, execute os seguintes comandos para realizar a build da aplicação:

```bash
./gradlew build -x check
```

Após a conclusão do build, execute o seguinte comando para subir localmente a base de dados e a aplicação:

```bash
docker-compose up -d application
```

### Nota

1. Caso a aplicação tenha sido modificada, é necessário realizar novamente os passos anteriores.
2. Caso o arquivo `Dockerfile` tenha sido alterado, um novo build da imagem deverá ser realizado para que a alteração
   surta efeito. Para isso, execute o seguinte comando:

```bash
docker-compose build --no-cache application
```

## Mais sobre o projeto

Este projeto foi criado com o intuito de testar e colocar em prática os conhecimentos adquiridos ao longo da minha
carreira. O tema "Catálogo" foi escolhido por ser um desafio pessoal e para medir minha evolução, pois é um tema com o
qual já trabalhei, permitindo uma comparação mais clara do meu progresso.

### Arquitetura

A arquitetura escolhida para o desenvolvimento do projeto foi a Arquitetura Hexagonal.

### Desafios

Além dos desafios de implementar uma nova arquitetura de software, também me impus o desafio de documentar e realizar
testes na aplicação, mantendo um alto nível de cobertura de código. Para assegurar que os testes são de fato efetivos,
utilizei a ferramenta [pitest](https://pitest.org/).

Manter o código limpo e aderente a boas práticas de programação não é uma tarefa fácil. Por isso, utilizei
o [detekt](https://detekt.dev/) no projeto para manter o padrão durante a codificação.
