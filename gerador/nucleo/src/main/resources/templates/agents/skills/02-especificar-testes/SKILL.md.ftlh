---
name: especificar-testes
description: Especificar testes
---
Para cada regra de negócio **R** de uma funcionalidade **F** de um subdomínio **S**, existe pelo menos um cenário de teste no arquivo dominio-**S**/src/test/resources/features/**C**.feature, onde **C** é o nome do conceito gerenciado por **F**, em minúsculas e com as palavras separadas por hífen, por exemplo, unidade-federativa.feature. Esse conceito dá nome ao agregado projetado a partir de **F**.

Os arquivos .feature começam com `# language: pt` e usam as palavras-chave do Gherkin em português (Funcionalidade, Contexto, Cenário, Dado, Quando, Então, E).