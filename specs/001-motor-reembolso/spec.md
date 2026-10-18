# Spec — Motor de Cálculo de Reembolso

**Versão:** 1.0 · **Status:** rascunho · **Última alteração:** `<data>`

> **Regra de ouro deste arquivo:** ele descreve o QUÊ e o PORQUÊ. Nenhuma linha
> aqui pode citar linguagem, biblioteca, classe, função ou estrutura de pasta.
> Se apareceu solução, o lugar dela é o `plan.md`.
>
> **Teste de aceitação da própria spec:** uma pessoa que nunca viu o projeto
> consegue, lendo só este arquivo, verificar se o sistema está correto?

---

## 1. Problema

<Duas ou três frases. Quem sofre hoje, com o quê, e por quê isso custa caro.>

## 2. Objetivo

<Uma frase. O que passa a ser verdade quando isto existir.>

## 3. Fora de escopo

<Lista explícita. Este bloco vale ponto: ele impede o agente de inventar feature
e impede você de mover a trave depois.>

- Não faz `<...>`
- Não faz `<...>`

## 4. Entrada e saída

**Entrada:** conforme `exemplos/despesas-exemplo.json`. Campos e significado:

| Campo | Tipo | Significado | Obrigatório |
|---|---|---|---|
| | | | |

**Saída:** definida por mim. Estrutura e significado de cada campo:

| Campo | Tipo | Significado |
|---|---|---|
| | | |

<Cole um exemplo de saída para uma entrada pequena. Vale mais que três parágrafos.>

## 5. Regras de negócio

Cada regra recebe um ID (`RN-001`, ...). As tasks vão referenciar esses IDs.

### RN-001 — Limite de categoria aplicado por despesa individual

**Regra:** cada despesa de alimentação, transporte urbano ou hospedagem é
comparada, isoladamente, com o limite da sua categoria (alimentação R$ 60,00;
transporte urbano R$ 80,00; hospedagem R$ 250,00 por diária). Nenhuma outra
despesa altera o resultado da despesa avaliada por meio de agregação de
valores: não há soma de despesas da mesma categoria no mesmo dia. A eventual
ampliação de limite por viagem é tratada na AMB-007 e opera sobre o valor do
limite, não sobre a soma das despesas. Não existe teto diário
agregado por categoria. Em hospedagem, o limite é por diária: o limite de um
lançamento de hospedagem é R$ 250,00 multiplicado pelo número de diárias que
ele cobre, número definido pela AMB-009 (pendente).
**Origem:** política do RH, itens 1, 2 e 3; decisão AMB-001
**Depende de:** AMB-002 (significado de reembolso parcial) e AMB-009 (número
de diárias em lançamento de hospedagem), ambas pendentes.
**Aceite:**
- d-001 (03/07, alimentação, 72,50) → reembolso 60,00, parcial.
- d-002 (03/07, alimentação, 38,00) → reembolso 38,00, integral.
- O dia 03/07 totaliza 98,00 reembolsados em alimentação.
- Inverter a ordem de d-001 e d-002 na entrada não altera nenhum dos valores acima.
- Três despesas de alimentação de 59,00 no mesmo dia → 59,00 cada, 177,00 no total.

### RN-002 — ...

---

## 6. Ambiguidades identificadas e decisões

> **Esta seção é o coração da spec e vale a maior parte dos 25 pontos do critério 1.**
> Uma ambiguidade que você resolveu no código sem registrar aqui conta como
> não resolvida.

### AMB-001 — Unidade de aplicação do limite por categoria

**Texto original do RH:** "Alimentação tem limite de R$ 60 por dia." / "Transporte urbano
tem limite de R$ 80 por dia." / "Hospedagem tem limite de R$ 250 por diária."
**O que não está claro:** a política não diz se o limite incide sobre cada
despesa individualmente ou sobre a soma das despesas da mesma categoria no
mesmo dia.
**Decisão:** o limite incide sobre CADA DESPESA individualmente, uniformemente
nas três categorias. Em hospedagem, o limite incide por diária e não por dia
de calendário — um lançamento pode cobrir múltiplas diárias numa única data;
quantas é objeto da AMB-009, e o limite aplicável a um lançamento de
hospedagem fica condicionado àquela decisão.
**Justificativa:** o limite é teto de razoabilidade por despesa, não orçamento
diário. Um colaborador que almoça e janta a trabalho tem duas necessidades
legítimas; zerar a segunda porque a primeira consumiu o teto puniria gasto
que a empresa reconhece como necessário. A avaliação por item reproduz a
conferência manual atual, é independente da ordem dos lançamentos, e permite
ao colaborador saber se uma despesa será paga no momento em que a faz.
**Custo aceito:** não existe teto diário agregado. Três almoços de R$ 59 no
mesmo dia somam R$ 177 reembolsados integralmente. Em transporte, d-003 e
d-004 poderiam somar até R$ 160 num dia contra um limite nominal de R$ 80.
Priorizo não punir despesa legítima sobre controle orçamentário diário.
**Consequências verificáveis** (assumindo AMB-002 = corta o excedente):
- d-001 (alimentação, 72,50) → reembolso 60,00, parcial
- d-002 (alimentação, 38,00) → reembolso 38,00, integral
- dia 03/07 totaliza 98,00
- d-006 e d-007 (54,90 cada) deixam de competir pelo limite; o tratamento
  passa a depender apenas da regra de duplicata (AMB-011)
- a ambiguidade #3 (qual item do dia sofre o corte) deixa de existir

**Em aberto:** registrado na §10.
**Regra afetada:** RN-001

### AMB-002 — ...

<A política tem no mínimo oito. Se você achou menos, releia
`exemplos/despesas-exemplo.json` — cada item daquele arquivo existe por um motivo.>

---

## 7. Casos de borda

| Caso | Entrada | Comportamento esperado | Regra |
|---|---|---|---|
| Duas despesas de alimentação no mesmo dia; esta acima do limite | d-001: 03/07, alimentação, 72,50 | Reembolso 60,00 (parcial); excedente de 12,50 não reembolsado | RN-001 |
| Duas despesas de alimentação no mesmo dia; esta abaixo do limite, a outra acima | d-002: 03/07, alimentação, 38,00 | Reembolso 38,00 (integral); d-001 no mesmo dia não reduz o valor | RN-001 |

## 8. Ordem de aplicação das regras

<Quando duas regras incidem sobre a mesma despesa, qual vale primeiro? A ordem
muda o resultado. Declarar isso separa spec boa de spec média.>

## 9. Critérios de aceite

O sistema está pronto quando:

- [ ] <critério verificável, sem ler código>
- [ ] <...>

## 10. O que fica em aberto

- **Ausência de teto diário agregado (origem: AMB-001).** Com avaliação por
  despesa, a política não limita o gasto diário agregado. Se o RH pretendia
  esse controle, seria necessário um teto diário adicional que a política
  atual não estabelece.
