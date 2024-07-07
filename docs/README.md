# Sobre o Domínio

Um catálogo de produtos deve conseguir prover para os usuários uma vasta opção de operações, ele deve conseguir
comportar os cenários reais desde os mais simples, criar um produto e criar uma oferta de venda para esse
produto, até os mais complexos, definir N customizações nos mais diversos níveis para uma oferta de venda.

## Definindo os Casos de Uso

Definir os casos de uso não é uma tarefa fácil, é necessário entender quais os comportamentos do usuário no mundo real.

Foram divididos os casos de uso por contexto para ficar mais simples a visualização. São eles:

* Caso de uso da Categoria
* Caso de uso do Produto
* Caso de uso da Oferta

Foi levantado apenas os casos de uso onde o usuário já possui uma loja e que apenas saber da existência dela basta para
o domínio, abrindo assim uma possível implementação de outro sistema responsável por gerenciar as lojas.

### Caso de uso da Categoria

As ações levantadas de um usuário com uma Categoria foram:

![category use cases](./images/categoryusecases.drawio.png)

### Caso de uso do Produto

As ações levantadas de um usuário com um Produto foram:

![product use cases](./images/productusecases.drawio.png)

### Caso de uso da Oferta

A oferta é a raiz da agregação das entidades Customização, Opção e a própria Oferta, isso significa que todas as ações
realizadas em alguma entidade filha dela, deve obrigatóriamente passar por ela antes.

As ações levantadas de um usuário com um Oferta foram:

![offer use cases](./images/offerusecases.drawio.png)

## Definindo as entidades e relacionamentos

Ao realizar o levantamento dos casos de uso, algumas entidades já haviam sido pensadas, mas ainda era necessário
levantar o relacionamento entre elas.

Nesse momento também foram pensadas algumas regras de negócio, onde ficou claro o forte relacionamento entre uma Oferta
e suas Customizações e Opções. Com isso, ficou definido que a Oferta é a raiz da agregação contemplando as Customizações
e Opções como entidades filhas.

Algumas regras das propriedades das entidades também foram levantadas e por isso foi necessário criar Objetos de Valor
que pudessem ser reutilizados e que mantivessem a regra de negócio protegida.

![classes](./images/classes.drawio.png)

## Mapeando as entidades para o banco de dados

Após o desenvolvimento do projeto ter sido iniciado e o banco de dados ter sido escolhido, foi necessário mapear as
entidades para tabelas do banco de dados.

O banco de dados escolhido foi o PostgreSQL, mas a implementação também foi realizada um banco de dados não relacional
(DynamoDB) para afins de teste:

![db diagram](./images/dbdiagram.png)

## Mapeando a API

A forma escolhida para a utilização do sistema foi uma API Rest por ser fácilmente implementada por aplicações web e
mobile.

A API foi documentada utilizando o swagger ui e está disponível para consulta no endpoint padrão do swagger na
aplicação.

![swagger](./images/swagger.png)
