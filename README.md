[![Build Status](https://travis-ci.org/jovanibrasil/votacao-api.svg?branch=develop)](https://travis-ci.org/jovanibrasil/votacao-api)
![Codecov branch](https://img.shields.io/codecov/c/github/jovanibrasil/votacao-api/develop)

# votacao-api
## API de votação

No cooperativismo cada associado possui um voto e as decisões são tomadas em assembleias, por votacao. Esta api é o backend da solução para este problema, considerando o seguinte:

- É possível buscar, cadastrar, atualizar e remover assembleias, pautas e itens de pauta.
- É possível votar. Cada associado é identificado por um ID único e pode votar apenas uma vez por item de pauta.
- Uma sessão de votação é atrelada a uma pauta e fica aberta por um tempo determinado ou 1 minuto por default.
- É possível buscar os resultados de votação de uma pauta.
- Um usuário deve estar autenticado para utilizar o sistema.

Para informar o resultado das votações para a plataforma, quando uma votação fecha uma mensagem com o resultado da votação é enviada para uma instância de mensageria. Atualmente para fins de simplificação e teste enviamos e recebemos essa mensagem (somos producer e consumer). 

A autenticação é feita utilizando JWT, sendo que associados tem papel de USER e podem apenas votar e buscar (assembleias, pautas, etc) e os admistradores do sistema, que poderiam ser os gerentes, possuem papel de ADMIN e pode realizar todas as operações no sistema. 

A API foi implementada utilizando **Spring Boot**, **Maven**, H2, PostgresQL, Lombok, Mapstruct. O serviço de mensageria utilizado foi o **Kafka**,  sendo este utilizado junto ao Docker. A documentação foi feita utilizando **Swagger**, inclusive você pode dar uma olhada nela [aqui](https://app.swaggerhub.com/apis-docs/konohaTeam/votacao-api/1.0.0-oas3). Os testes foram feitos utilizando **Junit**, **Mockito**, Jacoco e o **Postman**. Para controle de versionamento usamos o git seguindo alguns princípios do **Gitflow**. Todo o desenvolvimento foi sempre pautado no desenvolvimento com código limpo e orientado a testes (**TDD**).

É importante lembrar que o projeto ainda está em desenvolvimento, logo muitas melhorias e correções de possíveis bugs estão por vir. Uma cobertura maior de testes, testes de desempenho e um APP Web **Angular** serão provavelmente serão os próximos artefatos produzidos no projeto. 

## Execução

Para compilar e executar a aplicação é necessário você ter o [maven](http://maven.apache.org/) e o [docker](https://www.docker.com/) instalados na sua máquina. Alguns comandos foram disponibilizados em um Makefile para automatização da execução do Kafka no docker. Para rodar um Makefile no Windows você precisa usar o comando nmake e talvez seja necessário instalar as [ferramentas de build para c++](https://visualstudio.microsoft.com/pt-br/visual-cpp-build-tools).

Considerando o diretório raíz do projeto vá até o diretório [kafka](https://github.com/jovanibrasil/votacao-api/tree/develop/kafka). Pode ser usando no terminal o comando ```cd kafka```. Dentro deste diretório você deve executar o comando ```make up``` (Linux) ou ```nmake up``` (Windows). Desta forma você terá um container docker com o Kafka rodando. Para parar basta rodar o comando ```make down``` (Linux) ou ```nmake down``` Windows.

Se tudo ocorreu bem, você pode voltar a para a raíz do projeto e você pode executar od seguintes comandos:

- ```mvn spring-boot:run``` para rodar a aplicação. Por default ela rodará no profile dev com o banco H2.
- ```mvn clean compile``` para apenas compilar o projeto. 
- ```mvn test ``` para rodar todos os testes implementados para a aplicação. 

## Problemas

Poderia adiantar que o problema mais comum visto é em relação a integração da IDE Eclipse com o Lombok e o MapStruct. Para solucionar os problemas decorrentes de ambos, geralmente ajuda se você executar um ```mvn clean compile``` e também ir até o Eclipse e fazer um clean do project. 

Se você tiver qualquer dúvida, problema ou sugestão não exite em contato conosco.












