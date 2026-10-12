---
name: desenvolver-testes
description: Desenvolver testes
---
Desenvolver os testes de uma funcionalidade **F** de um subdomínio **S** consiste em desenvolver, com [programação defensiva](../programar-defensivamente/SKILL.md), no pacote dominio.**S** do módulo dominio-**S**, a classe **F**TestesCucumber, cujo código automatiza a execução dos cenários de teste contidos em **F**.feature.

Mock os repositórios e outras SPIs com Mockito.