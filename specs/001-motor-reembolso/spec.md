# Spec — Motor de Cálculo de Reembolso

**Versão:** 1.3 · **Status:** completa · **Última alteração:** 2026-10-19

---

## 1. Problema

A empresa reembolsa as despesas de trabalho dos colaboradores, e hoje esse
reembolso é decidido à mão: alguém do financeiro confere cada despesa contra a
política do RH e devolve uma relação de aprovações e recusas. O processo é
lento, e a política contém lacunas que cada conferente preenche à sua maneira
— esta especificação identifica vinte e uma delas na §6 —, de modo que a mesma
despesa pode receber decisões diferentes e nenhuma delas vem acompanhada de
uma justificativa que o colaborador ou o auditor consigam verificar. O custo
aparece em pagamentos indevidos, recusas injustas e retrabalho para contestar
e reconferir.

## 2. Objetivo

Dadas as despesas de um colaborador em um período, o reembolso de cada
despesa e do período é determinado sempre da mesma forma, por regras
escritas, e cada resultado vem acompanhado das informações que permitem
verificá-lo sem conhecer o sistema.

## 3. Fora de escopo

- Não determina se o colaborador está em viagem, e portanto não aplica a
  ampliação de 50% prevista no item 6 da política. Ver AMB-007.
- Não atribui efeito a campos da entrada que nenhuma regra utilize —
  identificador, nome e centro de custo do colaborador, rótulo de competência
  do período e descrição da despesa. Esses campos são aceitos, opcionais, e
  não alteram nenhum resultado. O fornecedor não é interpretado, mas é
  obrigatório, por ser chave de comparação da RN-009. Ver AMB-015.

## 4. Entrada e saída

Esta seção descreve a informação que entra e a que sai, e por que cada uma é
necessária. A forma como essa informação é representada pertence ao plan.md.

### 4.1 Entrada

A entrada é um **lote**: as despesas de um colaborador em um período.

**Informações do lote**

| Informação | Tipo | Significado | Obrigatória | Origem |
|---|---|---|---|---|
| Identificador do colaborador | texto | Quem apresenta o lote | não | AMB-015 |
| Nome do colaborador | texto | Idem | não | AMB-015 |
| Centro de custo do colaborador | texto | Alocação contábil; sem efeito no cálculo | não | AMB-015 |
| Data de início do período | data (dia, sem hora) | Primeiro dia da competência, incluído | **sim** | RN-005 |
| Data de fim do período | data (dia, sem hora) | Último dia da competência, incluído | **sim** | RN-005 |
| Rótulo de competência | texto (ano e mês) | Informativo; não participa da avaliação | não | AMB-015, AMB-016 |
| Despesas do lote | zero ou mais despesas | O que será avaliado; um lote sem nenhuma despesa é válido | **sim** | RN-010 |

**Informações de cada despesa**

| Informação | Tipo | Significado | Obrigatória | Origem |
|---|---|---|---|---|
| Identificador | texto | Identifica a despesa; único no lote; desempata duplicatas em ordem lexicográfica; comparado e apresentado após a remoção de espaços nas extremidades | **sim** | RN-009, RN-012, AMB-020 |
| Data | data (dia, sem hora) | Dia em que a despesa ocorreu; decide a pertinência ao período | **sim** | RN-005 |
| Categoria | texto | Comparada após normalização; qualquer texto é aceito, e categoria fora das três elegíveis é recusa, não erro | **sim** | RN-008, AMB-017 |
| Descrição | texto | Não é interpretada | não | AMB-015 |
| Fornecedor | texto | Chave de comparação da duplicidade, após normalização; não é interpretado | **sim** | RN-009, AMB-015 |
| Valor | número decimal | Valor lançado; normalizado para centavos antes de qualquer regra; após normalização, deve ser maior que zero | **sim** | RN-010, RN-011 |
| Nota fiscal apresentada | sim ou não | Se a despesa tem comprovante fiscal | **sim** | RN-003, RN-010 |

Um texto obrigatório que fique vazio após a remoção de espaços nas
extremidades é tratado como ausente. Uma informação não obrigatória
informada com tipo incompatível é tratada como ausente e não constitui erro
(AMB-015). Informações não previstas nesta seção são aceitas e ignoradas
(AMB-015).

**Espaços nas extremidades.** Em toda esta spec — na regra acima, na
normalização de categoria e fornecedor (RN-008) e na comparação de
identificadores (RN-012) —, espaços nas extremidades são todos os caracteres
que o padrão Unicode reconhece como espaço em branco, incluindo a tabulação e
os espaços não separáveis. Nenhum outro caractere é removido.

**Lote inválido (erro de lote, RN-010 e RN-012).** O lote inteiro é
rejeitado, sem que nenhuma despesa seja avaliada, quando: a entrada não pode
ser lida, inclusive quando é sintaticamente válida mas sua estrutura não é a
de um lote (RN-010); o período está ausente ou tem tipo incompatível; a
data de início ou a de fim está ausente ou não é data válida; a data de início é posterior
à de fim; as despesas do lote não são informadas ou a relação de despesas
tem tipo incompatível; ou duas ou mais despesas têm o mesmo identificador.
Uma data de início igual à de fim é válida: o período tem um dia.

**Despesa inválida (erro de item, RN-010).** Uma despesa é classificada como
erro quando lhe falta informação obrigatória — inclusive o identificador —,
quando uma informação obrigatória tem tipo incompatível — valor que não é
número, indicação de nota fiscal que não é sim ou não, fornecedor que não é
texto —, quando a data não existe no
calendário, quando o valor normalizado é menor ou igual a zero, ou quando o
elemento da relação de despesas não é uma despesa (AMB-020). Todas as causas
aplicáveis são reportadas (AMB-021); o elemento que não é uma despesa tem
uma única causa: a própria despesa, com tipo incompatível (AMB-020).

### 4.2 Saída

**Por despesa.** Cada despesa da entrada aparece exatamente uma vez na saída,
na ordem em que foi apresentada. A independência de ordem garantida pela
RN-001 e pela RN-009 é dos resultados de cada despesa, não da sequência de
apresentação.

| Informação | Quando aparece | Por que existe (regra que a exige) |
|---|---|---|
| Identificador, após a remoção de espaços nas extremidades | sempre; em despesa com erro cujo identificador falta ou tem tipo incompatível, é substituído pela posição da despesa na relação de despesas da entrada, contada a partir de um | Relaciona o resultado à despesa que o originou; todo aceite desta spec se refere à despesa por ele (RN-009, RN-012); apresentado normalizado para mostrar o mesmo valor que a comparação usa (AMB-020) |
| Classificação: integral, parcial, recusada ou erro | sempre | RN-002 (integral, parcial), RN-007 (os quatro valores), RN-010 (erro) |
| Valor considerado: o valor após normalização | sempre que o valor da entrada for numérico; ausente apenas quando a informação falta ou tem tipo incompatível | RN-011 ("é também o valor apresentado na saída"); permite verificar a fronteira da nota fiscal (RN-003) e o arredondamento de d-011 |
| Categoria considerada: a categoria após normalização | sempre que a categoria for informada como texto; ausente apenas quando falta ou tem tipo incompatível | RN-008; permite verificar que "ALIMENTACAO" em d-014 foi tratada como alimentacao |
| Valor reembolsado | sempre; 0,00 em recusa e em erro | RN-002, RN-004, RN-006, RN-008, RN-009, RN-010 |
| Limite aplicado | integral e parcial | RN-001, RN-002; sem ele, aceites como "d-011 → limite 60,00" (AMB-007) e "d-010 → limite 250,00" (AMB-009) não são verificáveis, pois o valor reembolsado de d-011 é o mesmo com qualquer limite acima de 33,33 |
| Excedente não reembolsado | parcial | RN-002, cujos aceites declaram o excedente |
| Exigência de nota fiscal: exigida ou não exigida | integral, parcial e recusada | RN-003; "d-003 → nota fiscal não exigida" e "transporte de 100,01 com NF → exigida e presente" produzem o mesmo reembolso, e sem esta informação os dois aceites não se distinguem |
| Lista de motivos de recusa, na ordem da §8 | sempre; vazia em integral, parcial e erro | RN-007, AMB-018 |
| Despesa mantida: o identificador da despesa que permaneceu com o reembolso | quando a lista de motivos contém DUPLICATA | RN-009; permite verificar qual despesa do grupo foi mantida e que inverter a ordem da entrada não a altera |
| Causas do erro: para cada informação que falhou, qual e de que forma — ausente, tipo incompatível, data inexistente ou valor não positivo —, todas as aplicáveis, na ordem em que as informações da despesa são declaradas na §4.1; o elemento da relação que não é uma despesa tem uma única causa: a própria despesa, com tipo incompatível | erro | RN-010, AMB-017, AMB-020, AMB-021; separadas da lista de motivos, porque erro não é recusa (RN-007) |

Esses campos, em conjunto, são a justificativa de cada despesa: respondem por
que ela recebeu o que recebeu. Uma frase legível construída a partir deles é
apresentação, e pertence ao plan.md.

Não aparecem na saída o identificador, o nome e o centro de custo do
colaborador, o rótulo de competência e a descrição das despesas: são
informações que nenhuma regra utiliza (AMB-015).

**Notação usada nesta spec.** Nos aceites e nos casos de borda, a lista de
motivos de recusa é escrita entre colchetes, na ordem da §8 — por exemplo,
motivos [FORA_DA_COMPETENCIA, SEM_NOTA_FISCAL]. Os códigos de motivo escritos
em maiúsculas (FORA_DA_COMPETENCIA, CATEGORIA_INELEGIVEL, SEM_NOTA_FISCAL,
DUPLICATA) são identificadores de negócio definidos por esta spec, não nomes
de implementação. Do mesmo modo, alimentacao, transporte_urbano e hospedagem
são o vocabulário de valores de categoria da entrada: identificadores de
negócio definidos por esta spec, escritos sem acento e com o separador
sublinhado, e não nomes de implementação; é com eles que a categoria
normalizada é comparada (RN-008, AMB-013). Nem os colchetes nem a grafia dos
códigos de motivo prescrevem a forma da saída.

**Consolidado do lote.**

| Informação | Por que existe |
|---|---|
| Período avaliado: datas de início e de fim | Mostra contra qual intervalo a pertinência foi julgada (RN-005) |
| Total reembolsado: soma dos valores reembolsados de todas as despesas | É o que o motor existe para determinar; confere com a soma dos itens porque cada valor já está em centavos (AMB-014) |
| Quantidade de despesas por classificação — integral, parcial, recusada, erro — e quantidade total | Prova que nenhuma despesa foi omitida: a quantidade total é igual à de despesas da entrada (AMB-010) |

Um lote sem despesas produz total reembolsado 0,00 e quantidade zero em todas
as classificações.

**Lote rejeitado.** A saída informa que o lote foi rejeitado e todas as
causas de erro de lote detectáveis, não apenas a primeira, pelo mesmo
princípio da AMB-018; a entrada que não pode ser lida é a exceção, por
impedir a detecção das demais. Não há resultado por despesa nem consolidado:
um total de 0,00 seria confundido com um lote avaliado em que nada foi
reembolsado.

**Exemplo.** Lote de 01/07 a 31/07 com d-001, d-006, d-007 e d-009:

| Despesa | Classificação | Valor considerado | Categoria considerada | Limite | Reembolsado | Excedente | Nota fiscal | Motivos | Outras |
|---|---|---|---|---|---|---|---|---|---|
| d-001 (alimentação, 72,50, com NF) | parcial | 72,50 | alimentacao | 60,00 | 60,00 | 12,50 | não exigida | nenhum | — |
| d-006 (alimentação, Bistro Central, 54,90, com NF) | integral | 54,90 | alimentacao | 60,00 | 54,90 | — | não exigida | nenhum | — |
| d-007 (idêntica a d-006) | recusada | 54,90 | alimentacao | — | 0,00 | — | não exigida | DUPLICATA | despesa mantida: d-006 |
| d-009 (transporte urbano, −45,00, sem NF) | erro | −45,00 | transporte_urbano | — | 0,00 | — | — | nenhum | causas: valor não positivo |

Consolidado: período de 01/07 a 31/07; total reembolsado 114,90; 1 integral,
1 parcial, 1 recusada, 1 erro; 4 despesas.

## 5. Regras de negócio

Cada regra recebe um ID (`RN-001`, ...). As tasks vão referenciar esses IDs.

> **Limites nominais, sem ampliação por viagem:** o sistema não determina se o
> colaborador está em viagem (AMB-007). Todas as despesas são avaliadas com os
> limites nominais da política — alimentação R$ 60,00, transporte urbano
> R$ 80,00, hospedagem R$ 250,00 por lançamento (uma diária) —, e os
> critérios de aceite e os casos de borda deste documento valem para qualquer
> despesa, sem distinção de viagem.

### RN-001 — Limite de categoria aplicado por despesa individual

**Regra:** cada despesa de alimentação, transporte urbano ou hospedagem é
comparada, isoladamente, com o limite da sua categoria (alimentação R$ 60,00;
transporte urbano R$ 80,00; hospedagem R$ 250,00). Nenhuma outra despesa
altera o resultado da despesa avaliada por meio de agregação de valores: não
há soma de despesas da mesma categoria no mesmo dia. Não existe teto diário
agregado por categoria. Em hospedagem, o limite é por diária e cada
lançamento vale uma diária: R$ 250,00 por lançamento (AMB-009).
**Origem:** política do RH, itens 1, 2 e 3; decisões AMB-001, AMB-007 e AMB-009
**Depende de:** RN-002 (reembolso parcial, decidido na AMB-002).
**Aceite:**
- d-001 (03/07, alimentação, 72,50) → reembolso 60,00, parcial.
- d-002 (03/07, alimentação, 38,00) → reembolso 38,00, integral.
- O dia 03/07 totaliza 98,00 reembolsados em alimentação.
- Inverter a ordem de d-001 e d-002 na entrada não altera nenhum dos valores acima.
- Três despesas de alimentação de 59,00 no mesmo dia → 59,00 cada, 177,00 no total.
- d-010 (14/07, hospedagem, 480,00, com NF, "Hotel Rio - 2 diarias") →
  limite 250,00; reembolso 250,00, parcial; excedente 230,00. A descrição não
  altera o limite.

### RN-002 — Reembolso parcial: o limite é teto

**Regra:** uma despesa cujo valor é estritamente maior que o limite aplicável
tem reembolso igual ao limite aplicável; o excedente (valor menos limite) não
é reembolsado e a despesa é classificada como parcial. Uma despesa cujo valor
é menor ou igual ao limite aplicável tem reembolso igual ao seu valor e é
classificada como integral. O limite aplicável é o limite nominal da
categoria conforme a RN-001; não há ampliação por viagem (AMB-007).
**Origem:** política do RH, item 4; decisões AMB-002 e AMB-007
**Depende de:** RN-001 (limite da categoria).
**Aceite:**
- d-001 (03/07, alimentação, 72,50) → reembolso 60,00, parcial; excedente 12,50.
- Alimentação de 60,00 → reembolso 60,00, integral.
- Alimentação de 60,01 → reembolso 60,00, parcial; excedente 0,01.
- Transporte urbano de 90,00 → reembolso 80,00, parcial; excedente 10,00.

### RN-003 — Nota fiscal obrigatória acima de R$ 100,00

**Regra:** uma despesa cujo valor é estritamente maior que R$ 100,00 exige
nota fiscal. Uma despesa de valor menor ou igual a R$ 100,00 não exige nota
fiscal, tenha ela nota ou não. A comparação usa o valor lançado da despesa,
já normalizado conforme a RN-011, antes de qualquer aplicação de limite; o
valor reembolsável não interfere na exigência. Uma despesa que exige nota
fiscal e a apresenta não sofre nenhum efeito desta regra e segue para o
limite da sua categoria. Uma despesa que exige nota fiscal e não a
apresenta é recusada conforme a RN-004.
**Origem:** política do RH, item 5; decisões AMB-004 e AMB-006
**Depende de:** RN-002 (reembolso parcial, decidido na AMB-002); RN-004
(recusa por ausência de nota fiscal exigida, decidida na AMB-005); RN-011
(normalização monetária, que define a fronteira para valores como 100,004).
**Aceite:**
- d-003 (06/07, transporte urbano, 100,00, sem NF) → nota fiscal não exigida;
  reembolso 80,00, parcial.
- d-004 (06/07, transporte urbano, 100,01, sem NF) → nota fiscal exigida e
  ausente; reembolso 0,00, recusado (RN-004).
- Transporte urbano de 100,01, com NF → nota fiscal exigida e presente;
  reembolso 80,00, parcial.
- Despesa de 100,004 → valor normalizado 100,00 (RN-011); nota fiscal não
  exigida.
- Despesa de 100,005 → valor normalizado 100,01 (RN-011); nota fiscal
  exigida.
- Alimentação de 50,00, sem NF → nota fiscal não exigida; reembolso 50,00,
  integral.
- Alimentação de 500,00, sem NF → nota fiscal exigida e ausente, embora o
  teto da categoria seja 60,00; reembolso 0,00, recusado (RN-004).
- d-013 (22/07, hospedagem, 690,00, sem NF) → nota fiscal exigida e ausente;
  reembolso 0,00, recusado (RN-004).

### RN-004 — Recusa integral por ausência de nota fiscal exigida

**Regra:** uma despesa que exige nota fiscal conforme a RN-003 e não a
apresenta tem reembolso 0,00 e é classificada como recusada, com o motivo
SEM_NOTA_FISCAL na sua lista de motivos (RN-007). A recusa é integral e
independe do limite da categoria: o item não é avaliado contra o limite
(RN-001, RN-002), e um lançamento que seria integral caso tivesse nota é
recusado da mesma forma. O motivo SEM_NOTA_FISCAL é distinto de qualquer
outra causa de recusa, em particular de CATEGORIA_INELEGIVEL; quando mais de
uma causa se aplica, os motivos coexistem na lista.
**Origem:** política do RH, item 5; decisões AMB-005 e AMB-018
**Depende de:** RN-003 (exigência de nota fiscal); RN-007 (lista de motivos).
**Aceite:**
- d-004 (06/07, transporte urbano, 100,01, sem NF) → reembolso 0,00,
  recusado, motivos [SEM_NOTA_FISCAL].
- d-013 (22/07, hospedagem, 690,00, sem NF) → reembolso 0,00, recusado,
  motivos [SEM_NOTA_FISCAL].
- Alimentação de 500,00, sem NF → reembolso 0,00, recusado, motivos
  [SEM_NOTA_FISCAL], embora o teto da categoria seja 60,00.
- Hospedagem de 200,00, sem NF → reembolso 0,00, recusado, motivos
  [SEM_NOTA_FISCAL]; com NF, seria 200,00, integral.
- d-003 (06/07, transporte urbano, 100,00, sem NF) → nota fiscal não exigida;
  esta regra não se aplica; reembolso 80,00, parcial.
- Transporte urbano de 100,01, com NF → esta regra não se aplica; reembolso
  80,00, parcial.

### RN-005 — Período de competência e pertinência da despesa

**Regra:** o período de competência é o intervalo que vai da data de início à
data de fim informadas no período da entrada, com ambas as bordas inclusivas:
uma data igual à de início ou igual à de fim pertence ao período. O rótulo de
competência (o mês informado, como "2026-07") é informativo e não participa
da avaliação; divergência entre ele e o par de datas não impede o
processamento. Uma despesa pertence ao período quando a sua data está nesse
intervalo. A data da despesa é o único dado avaliado; a descrição não é
interpretada. Uma despesa fora do período é recusada conforme a RN-006.
**Origem:** política do RH, item 7; decisões AMB-008 e AMB-016
**Depende de:** RN-006 (recusa por despesa fora do período).
**Aceite:**
- Lote de exemplo → período de 2026-07-01 a 2026-07-31, ambas as datas
  incluídas.
- d-014 (31/07, igual à data de fim) → dentro do período.
- Despesa em 2026-07-01 (igual à data de início) → dentro do período.
- Despesa em 2026-08-01 → fora do período.
- d-008 (15/04, "Almoco de abril lancado com atraso") → fora do período; a
  descrição não altera o resultado.
- d-001 a d-007 e d-010 a d-013 (datas entre 03/07 e 22/07) → dentro do
  período. d-009 (11/07) é erro de item e não é avaliada por esta regra
  (RN-010).

### RN-006 — Recusa por despesa fora do período

**Regra:** uma despesa cuja data está fora do período conforme a RN-005 tem
reembolso 0,00 e é classificada como recusada, com o motivo
FORA_DA_COMPETENCIA na sua lista de motivos (RN-007). O item aparece na saída
com seus motivos, como qualquer outro. A recusa independe do limite da
categoria: o item não é avaliado contra o limite (RN-001, RN-002). Uma
despesa fora do período não é avaliada por nenhuma regra de valor e não
integra grupo de duplicatas (RN-009); as demais regras de elegibilidade
continuam a ser avaliadas e acrescentam seus motivos (RN-007).
**Origem:** política do RH, item 7; decisões AMB-010 e AMB-018
**Depende de:** RN-005 (período e pertinência); RN-007 (lista de motivos).
**Aceite:**
- d-008 (15/04, alimentação, 41,00, com NF) → reembolso 0,00, recusado,
  motivos [FORA_DA_COMPETENCIA], embora seja válida em todos os outros aspectos.
- d-014 (31/07) → dentro do período; esta regra não se aplica.
- Despesa em 2026-08-01, alimentação, 30,00, com NF → reembolso 0,00,
  recusado, motivos [FORA_DA_COMPETENCIA].

### RN-007 — Lista de motivos de recusa

**Regra:** a saída de cada despesa traz a lista de todos os motivos de recusa
aplicáveis, e não um motivo único. Todas as regras de elegibilidade são
avaliadas para toda despesa sem erro de item (RN-010), sem interrupção na
primeira que se aplica, e cada uma que se aplica acrescenta seu motivo à
lista. Se a lista contém qualquer motivo, a despesa é recusada com reembolso
0,00 e não é avaliada contra o limite. Se a lista está vazia, a despesa segue
para o limite da categoria e é classificada como integral ou parcial conforme
a RN-002; o reembolso parcial não é motivo de recusa e não entra na lista. A
lista é ordenada na ordem das regras de elegibilidade declarada na §8, de
modo que a mesma entrada produza sempre a mesma saída. A classificação de
cada despesa assume um de quatro valores: integral, parcial, recusada ou
erro. Uma despesa com erro de item é classificada como erro e tem a lista de
motivos de recusa vazia: erro não é recusa (RN-010).
**Origem:** decisões AMB-017 e AMB-018
**Depende de:** §8 (ordem das regras de elegibilidade).
**Aceite:**
- Despesa de 150,00, sem NF, com data fora do período → reembolso 0,00,
  recusado, motivos [FORA_DA_COMPETENCIA, SEM_NOTA_FISCAL].
- d-008 → reembolso 0,00, recusado, motivos [FORA_DA_COMPETENCIA].
- d-004 → reembolso 0,00, recusado, motivos [SEM_NOTA_FISCAL].
- Despesa sem nenhuma violação (por exemplo, d-002) → lista de motivos vazia.
- d-001 (72,50, acima do limite) → reembolso 60,00, parcial; lista de motivos
  vazia.
- d-009 (−45,00) → classificação erro; reembolso 0,00; lista de motivos de
  recusa vazia.

### RN-008 — Normalização de categoria e fornecedor; elegibilidade da categoria

**Regra:** antes de qualquer comparação, a categoria e o fornecedor de cada
despesa são normalizados: letras maiúsculas são convertidas em minúsculas e
espaços nas extremidades, no alcance declarado na §4.1, são removidos.
Nenhuma outra transformação é aplicada — acentuação, separadores e
sinônimos não são normalizados. Toda regra que compara categorias ou
fornecedores usa os valores normalizados.

A conversão de maiúsculas em minúsculas usa o mapeamento completo de caixa
definido pelo padrão Unicode, independente da localidade do ambiente de
execução. Independente de localidade significa que a mesma entrada produz a
mesma saída em qualquer máquina, qualquer idioma de sistema e qualquer
configuração regional: o mapeamento é o do padrão Unicode, não o do idioma
do usuário.

Mapeamento completo significa que um caractere pode ser convertido em mais
de um, e que a conversão pode depender da posição do caractere na palavra.
Isso é intencional: é o que faz a conversão respeitar a grafia da língua. O
sigma grego maiúsculo (U+03A3) convertido quando precedido de letra e não
seguido de letra produz o sigma final (U+03C2), que é a forma correta em
grego; convertido em qualquer outra posição produz o sigma comum (U+03C3).

As categorias elegíveis são
exatamente três: alimentacao, transporte_urbano e hospedagem. Uma despesa
cuja categoria normalizada seja qualquer outra é recusada, com o motivo
CATEGORIA_INELEGIVEL na sua lista de motivos (RN-007).
**Origem:** política do RH, item 9; decisão AMB-013
**Depende de:** RN-007 (lista de motivos).
**Aceite:**
- d-014 ("ALIMENTACAO", 61,00, com NF, 31/07) → categoria normalizada
  alimentacao; reembolso 60,00, parcial; lista de motivos vazia.
- d-005 ("coworking", 89,00, com NF) → reembolso 0,00, recusado, motivos
  [CATEGORIA_INELEGIVEL].
- "Alimentacao", "ALIMENTACAO" e " alimentacao " → a mesma categoria,
  alimentacao.
- "alimentação", com cedilha e til → não normalizada; recusada, motivos
  [CATEGORIA_INELEGIVEL].
- "transporte-urbano" → não normalizado para transporte_urbano; recusado,
  motivos [CATEGORIA_INELEGIVEL].
- Fornecedores "Bistro Central", "BISTRO CENTRAL" e " Bistro Central " → o
  mesmo fornecedor; "Bistrô Central" → fornecedor distinto.
- "ALIMENTACAO" → alimentacao em qualquer localidade, inclusive turca e
  azerbaijana, onde a conversão dependente de localidade produziria
  alımentacao (U+0131, i sem ponto) e tornaria a despesa inelegível. O caso
  d-014 da §9 depende desta garantia.
- Fornecedores "ΟΔΟΣ" e "Οδος" → a mesma chave, οδος; são o mesmo fornecedor
  para a RN-009. Com mapeamento simples produziriam οδοσ e οδος, chaves
  distintas, e um grupo de duplicatas deixaria de ser detectado.
- "ALİMENTACAO" (U+0130, I turco com ponto) → ali̇mentacao, com o ponto como
  caractere combinante separado (U+0307), que não é nenhuma das três
  categorias elegíveis; recusada, motivos [CATEGORIA_INELEGIVEL]. Isso é
  intencional: U+0130 é um caractere distinto de U+0049, e tratá-los como
  iguais seria o motor adivinhando a intenção de quem digitou, contra o
  alcance estreito decidido na AMB-013.
- Duas despesas de alimentação no mesmo dia e de mesmo valor, com
  fornecedores "ΟΔΟΣ" e "Οδος" → grupo de duplicatas pela RN-009.

### RN-009 — Duplicidade

**Regra:** duas ou mais despesas formam um grupo de duplicatas quando
coincidem em data, categoria normalizada (RN-008), fornecedor normalizado
(RN-008) e valor normalizado (RN-011). A descrição e a presença de nota
fiscal não integram o critério. Só integram grupos as despesas que não
receberam motivo de nenhuma outra regra de elegibilidade e que não têm erro
de item (RN-010): uma despesa
fora do período, de categoria inelegível, sem a nota fiscal exigida ou com
erro de item nunca é mantida nem recusada como duplicata. Dentro de um grupo,
a despesa de menor identificador é avaliada normalmente e as demais são
recusadas, com o motivo DUPLICATA na sua lista de motivos (RN-007). Os
identificadores são comparados em ordem lexicográfica, caractere a
caractere, pelo ponto de código Unicode de cada caractere, sem extração de
número e sem normalização de caixa: letras maiúsculas precedem as
minúsculas, e, com identificadores de larguras diferentes, "d-10" precede
"d-9". O resultado
não depende da ordem das despesas na entrada.
**Origem:** política do RH, item 8; decisão AMB-011
**Depende de:** RN-007 (lista de motivos); RN-008 (categoria e fornecedor
normalizados); RN-011 (valor normalizado).
**Aceite:**
- d-006 e d-007 (09/07, alimentação, Bistro Central, 54,90, com NF) → grupo de
  duplicatas; d-006 → reembolso 54,90, integral, lista de motivos vazia;
  d-007 → reembolso 0,00, recusado, motivos [DUPLICATA].
- Inverter a ordem de d-006 e d-007 na entrada → os mesmos dois resultados.
- Duas despesas iguais no critério, de valor até 100,00 (nota não exigida),
  uma com NF e outra sem → mesmo grupo; a de menor identificador é mantida,
  qualquer que seja a que tem a nota.
- Duas hospedagens iguais no critério, de 200,00, a de menor identificador
  sem NF e a outra com NF → a sem NF é recusada, motivos [SEM_NOTA_FISCAL], e
  não integra grupo; a com NF → reembolso 200,00, integral, sem DUPLICATA.
- Duas despesas iguais no critério, com descrições diferentes → grupo de
  duplicatas.
- Duas despesas iguais no critério, ambas fora do período → ambas recusadas,
  motivos [FORA_DA_COMPETENCIA]; nenhuma recebe DUPLICATA.
- Duas diárias no mesmo hotel, 240,00 cada, com NF, lançadas com as datas de
  cada noite (14/07 e 15/07) → não são duplicatas; reembolso 240,00, integral,
  cada uma. As mesmas duas lançadas ambas em 14/07 → grupo de duplicatas; a
  de menor identificador → 240,00, integral; a outra → motivos [DUPLICATA].
- Duas despesas iguais no critério, com identificadores "d-9" e "d-10" → "d-10"
  é a de menor identificador (ordem lexicográfica) e é mantida; "d-9" →
  motivos [DUPLICATA].
- Duas despesas iguais no critério, com fornecedores "Bistro Central" e
  "BISTRO CENTRAL" → grupo de duplicatas.
- Duas despesas iguais no critério, com identificadores "d-006" e "D-006" →
  grupo de duplicatas; mantém-se "D-006", que precede "d-006" pela ordem dos
  pontos de código; "d-006" → motivos [DUPLICATA].
- Duas despesas iguais no critério, com identificadores que diferem apenas no
  último caractere — U+FF71 (ｱ, katakana de meia largura) num e U+1F600 (😀)
  no outro → mantém-se o de U+FF71, cujo ponto de código é menor; o outro →
  motivos [DUPLICATA].

### RN-010 — Validação da entrada: erro de lote e erro de item

**Regra:** antes de qualquer regra de elegibilidade ou de valor, a entrada é
validada contra o contrato de entrada declarado na §4.1. Distinguem-se três
situações:

1. **Erro de lote** — a estrutura da entrada impede qualquer avaliação: a
   entrada não pode ser lida; o período está ausente ou tem tipo
   incompatível; a data de início ou a de fim está ausente ou não é data
   válida; a data de início é posterior à de fim; as despesas do lote não são
   informadas ou a relação de despesas tem tipo incompatível; ou há
   identificador de despesa repetido (RN-012). Nenhuma despesa é processada e
   o lote é rejeitado. A saída informa todas as causas de erro de lote
   detectáveis; a entrada que não pode ser lida é a exceção, por impedir a
   detecção das demais. Uma entrada sintaticamente válida cuja estrutura não
   seja a de um lote também é entrada que não pode ser lida, e constitui
   causa única de rejeição: sem a estrutura de um lote não há onde procurar o
   período nem a relação de despesas, e afirmar que estão ausentes seria
   dizer algo sobre uma estrutura que nunca existiu — a falha está no nível
   do lote inteiro, não no de um campo, pelo mesmo critério de abrangência
   da AMB-020.
2. **Erro de item** — uma despesa não pode ser avaliada por informação
   obrigatória ausente — inclusive o identificador e a indicação de nota
   fiscal —, tipo incompatível, data inexistente no calendário ou valor
   normalizado (RN-011) menor ou igual a zero, ou porque o elemento da
   relação de despesas não é uma despesa (AMB-020). Um texto obrigatório que
   fique vazio após a remoção de espaços nas extremidades (§4.1) é tratado
   como ausente. Só as informações obrigatórias produzem erro por tipo
   incompatível; uma informação não obrigatória e sem regra associada com
   tipo incompatível é tratada como ausente (AMB-015). A despesa é
   classificada como erro, com reembolso 0,00, e
   não é avaliada por nenhuma regra de elegibilidade ou de valor; não integra
   grupo de duplicatas (RN-009). A saída traz todas as causas do erro, na
   ordem em que as informações da despesa são declaradas na §4.1 (AMB-021);
   o elemento que não é uma despesa tem uma única causa: a própria despesa,
   com tipo incompatível (AMB-020);
   a despesa cujo identificador falta ou tem tipo incompatível é identificada
   pela sua posição na relação de despesas, contada a partir de um (AMB-020).
   As demais despesas do lote são processadas normalmente.
3. **Recusa** — a despesa é bem formada e uma regra de elegibilidade a
   rejeita (RN-004, RN-006, RN-008, RN-009). Recebe a classificação recusada,
   com seus motivos (RN-007).

Os valores monetários aceitos são estritamente maiores que zero; um valor
igual ou inferior a zero constitui erro de item. A faixa é avaliada sobre o
valor já normalizado para duas casas decimais (RN-011), e não sobre o valor
escrito na entrada.

Um lote sem nenhuma despesa é válido. Um período cuja data de início é igual
à de fim é válido e tem um dia. Informações não previstas no contrato de
entrada são aceitas e ignoradas.
**Origem:** decisões AMB-014, AMB-015, AMB-017, AMB-019, AMB-020 e AMB-021;
§4.1
**Depende de:** RN-011 (normalização monetária); RN-012 (unicidade do
identificador); §4.1 (contrato de entrada).
**Aceite:**
- d-009 (11/07, transporte urbano, −45,00, sem NF) → erro de item; reembolso
  0,00; não é avaliada por nenhuma regra de elegibilidade.
- d-005 (coworking, 89,00) → bem formada; recusada, motivos
  [CATEGORIA_INELEGIVEL]; não é erro.
- Entrada com data de início posterior à data de fim → lote rejeitado;
  nenhuma despesa processada.
- Despesa sem o campo de valor → erro de item; as demais despesas do lote são
  processadas.
- Despesa de valor 0,00 → erro de item.
- Despesa de valor 0,004 → valor normalizado 0,00; erro de item.
- Despesa sem a indicação de nota fiscal → erro de item; não é recusada por
  SEM_NOTA_FISCAL.
- Despesa com fornecedor formado apenas por espaços → erro de item
  (informação obrigatória ausente).
- Despesa com data 2026-02-30 → erro de item (data inexistente).
- Lote sem nenhuma despesa → válido; total reembolsado 0,00; quantidade zero
  em todas as classificações.
- Período com data de início igual à de fim → válido.
- Lote com data de início posterior à de fim e com identificador repetido →
  lote rejeitado; a saída informa as duas causas.
- Um erro de item em uma despesa não altera o resultado de nenhuma outra.
- Despesa sem identificador, na terceira posição da relação → erro de item,
  identificada na saída pela posição 3; as demais despesas são processadas.
- Duas despesas sem identificador → ambas erro de item; não constituem
  identificador repetido; o lote não é rejeitado.
- Elemento da relação de despesas que não é uma despesa → erro de item,
  identificado pela sua posição, com uma única causa: a própria despesa, com
  tipo incompatível; as demais despesas são processadas.
- Relação de despesas com tipo incompatível → lote rejeitado.
- Período com tipo incompatível → lote rejeitado.
- Entrada sintaticamente válida cuja estrutura não é a de um lote — por
  exemplo, uma relação ou um número no lugar do lote → lote rejeitado; causa
  única: entrada que não pode ser lida; não se informam período ausente nem
  despesas não informadas.
- Despesa sem fornecedor e com valor −5,00 → erro de item; causas: fornecedor
  ausente e valor não positivo, nessa ordem.
- Despesa com fornecedor formado apenas por tabulação ou por espaço não
  separável → erro de item (informação obrigatória ausente).
- Despesa com descrição informada como número → processada normalmente; a
  descrição é tratada como ausente (AMB-015).
- Despesa com fornecedor informado como número → erro de item; causa:
  fornecedor com tipo incompatível.
- Lote com centro de custo do colaborador ou rótulo de competência
  informados com tipo incompatível → processado normalmente (AMB-015).

### RN-012 — Unicidade do identificador de despesa

**Regra:** cada despesa do lote tem identificador único. Duas ou mais despesas
com o mesmo identificador constituem erro de lote (RN-010): o lote é
rejeitado e nenhuma despesa é avaliada, inclusive as que têm identificador
único. Os identificadores são comparados exatamente como informados, após a
remoção de espaços nas extremidades (§4.1), e são apresentados na saída
nessa mesma forma. A regra compara apenas identificadores informados: uma
despesa sem identificador, ou com identificador de tipo incompatível, é erro
de item (RN-010) e não constitui repetição com nenhuma outra (AMB-020).
**Origem:** decisões AMB-019 e AMB-020
**Depende de:** RN-010 (erro de lote).
**Aceite:**
- Lote com duas despesas de identificador d-006 → lote rejeitado; causa:
  identificador repetido; nenhuma despesa é avaliada.
- Lote de exemplo, com identificadores d-001 a d-014, todos distintos → esta
  regra não produz erro.
- "d-006" e "D-006" → identificadores distintos; esta regra não produz erro de
  lote, ainda que categoria e fornecedor sejam normalizados (RN-008) —
  normaliza-se o que é termo de negócio, não o que é chave.
- " d-006" e "d-006" → o mesmo identificador após a remoção de espaços; lote
  rejeitado; causa: identificador repetido.
- Identificador " d-001 " → apresentado na saída como "d-001".
- Duas despesas sem identificador → esta regra não produz erro de lote; ambas
  são erro de item (RN-010).

---

### RN-011 — Normalização monetária

**Regra:** todo valor monetário da entrada é normalizado para duas casas
decimais por arredondamento, com a metade arredondada para cima, antes da
aplicação de qualquer regra — inclusive da validação de faixa da RN-010.
"Para cima" significa afastando-se do zero, com o mesmo critério para valores
positivos e negativos; como valor normalizado menor ou igual a zero é sempre
erro de item (RN-010), em valor negativo o critério afeta apenas o valor
apresentado na saída, não a classificação. O
valor normalizado é o único considerado em todo o processamento:
comparações de limite (RN-001, RN-002), exigência de nota fiscal (RN-003),
critério de duplicidade (RN-009) e cálculo do reembolso. É também o valor
apresentado na saída.
**Origem:** decisão AMB-014
**Aceite:**
- d-011 (15/07, alimentação, 33,333, com NF) → valor normalizado 33,33;
  reembolso 33,33, integral.
- Despesa de 100,004 → valor normalizado 100,00; nota fiscal não exigida.
- Despesa de 100,005 → valor normalizado 100,01; nota fiscal exigida.
- Alimentação de 60,004 → valor normalizado 60,00; reembolso 60,00, integral.
- 54,90 e 54,904 → ambos normalizados para 54,90; coincidindo nos demais
  campos do critério, formam grupo de duplicatas (RN-009).
- 54,90 e 54,900 → o mesmo valor, 54,90.
- Despesa de 0,004 → valor normalizado 0,00; erro de item (RN-010).
- Despesa de −45,005 → valor normalizado −45,01; erro de item (RN-010); a
  saída apresenta −45,01.

---

## 6. Ambiguidades identificadas e decisões

### AMB-001 — Unidade de aplicação do limite por categoria

**Texto original do RH:** "Alimentação tem limite de R$ 60 por dia." / "Transporte urbano
tem limite de R$ 80 por dia." / "Hospedagem tem limite de R$ 250 por diária."
**O que não está claro:** a política não diz se o limite incide sobre cada
despesa individualmente ou sobre a soma das despesas da mesma categoria no
mesmo dia.
**Decisão:** o limite incide sobre CADA DESPESA individualmente, uniformemente
nas três categorias. Em hospedagem, o limite incide por diária e não por dia
de calendário; cada lançamento de hospedagem corresponde a uma diária
(AMB-009), e seu limite é R$ 250,00.
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
**Consequências verificáveis** (com o reembolso parcial definido na AMB-002:
o excedente é cortado):
- d-001 (alimentação, 72,50) → reembolso 60,00, parcial
- d-002 (alimentação, 38,00) → reembolso 38,00, integral
- dia 03/07 totaliza 98,00
- d-006 e d-007 (54,90 cada) deixam de competir pelo limite; d-006 → 54,90,
  integral; d-007 → 0,00, recusado, motivos [DUPLICATA] (RN-009)
- a AMB-003 (qual item do dia sofre o corte) deixa de existir

**Em aberto:** registrado na §10.
**Regra afetada:** RN-001

### AMB-002 — Significado de "reembolso parcial"

**Texto original do RH:** "Despesas acima do limite são reembolsadas parcialmente." (item 4)
**O que não está claro:** "parcialmente" admite três leituras — (a) paga-se o
valor do limite e o excedente é cortado; (b) recusa-se o item inteiro; (c)
paga-se uma fração percentual do valor, sem que a política informe qual.
**Decisão:** adotada a leitura (a). O reembolso de uma despesa acima do limite
é o valor do limite aplicável; o excedente não é reembolsado e o item é
classificado como parcial. "Acima do limite" significa estritamente maior:
uma despesa de valor exatamente igual ao limite é reembolsada integralmente.
**Justificativa:** a leitura (c) é inoperável, pois exigiria inventar uma
porcentagem que a política não fornece. A leitura (b) contraria a palavra
"parcialmente" e puniria integralmente desvio mínimo — d-014 perderia R$ 61
por estourar R$ 1. A leitura (a) é a única compatível com a letra da política
e com a justificativa já adotada na AMB-001: o limite é teto de razoabilidade,
a empresa paga o que considera razoável e o colaborador arca com o excesso.
**Custo aceito:** o limite deixa de funcionar como barreira — não há desincentivo
a gastar acima dele, já que o colaborador sempre recebe o teto. Aceito porque
a alternativa pune desvio mínimo de forma desproporcional.
**Consequências verificáveis:**
- d-001 (72,50, limite 60) → 60,00, parcial
- d-014 (61,00, "ALIMENTACAO", limite 60) → 60,00, parcial; a categoria é
  normalizada para alimentacao (RN-008)
- d-003 (100,00, limite 80) → 80,00, parcial; a nota fiscal não é exigida (RN-003)
- uma despesa de exatamente 60,00 em alimentação → 60,00, integral
- a RN-001 e seus aceites permanecem válidos sem alteração

**Regra afetada:** RN-002

### AMB-003 — Distribuição do corte entre despesas do mesmo dia (dissolvida)

**Texto original do RH:** não há. A dúvida derivava da leitura agregada do
limite diário.
**O que não estava claro:** se o limite incidisse sobre a soma das despesas da
mesma categoria no mesmo dia, seria necessário decidir qual despesa sofre o
corte quando o conjunto estoura — ordem de aparição, maior valor, menor
valor ou rateio proporcional. d-001 e d-002 têm a mesma data sem hora, de
modo que nenhum critério cronológico resolveria o desempate.
**Decisão:** dissolvida pela AMB-001. Com o limite incidindo sobre cada despesa
individualmente, não há competição pelo mesmo teto e não existe corte a
distribuir.

Registrada para manter a rastreabilidade da numeração e documentar que a
dúvida foi identificada antes de ser eliminada.

### AMB-004 — Fronteira da obrigatoriedade de nota fiscal

**Texto original do RH:** "Nota fiscal é obrigatória acima de R$ 100." (item 5)
**O que não está claro:** "acima de R$ 100" admite duas leituras — (a)
estritamente maior, de modo que uma despesa de exatamente R$ 100,00 não
exige nota fiscal; (b) maior ou igual, de modo que R$ 100,00 já exige.
**Decisão:** adotada a leitura (a). A nota fiscal é obrigatória para despesas
de valor estritamente maior que R$ 100,00. Uma despesa de exatamente
R$ 100,00 não exige nota fiscal. A comparação usa o valor da despesa já
normalizado conforme a AMB-014 (precisão monetária).
**Justificativa:** "acima de" em português corrente exclui o valor citado, e a
AMB-002 já adotou esse mesmo sentido para "acima do limite" no item 4 da
política. Manter a mesma expressão com o mesmo significado nos dois itens
evita que a spec atribua sentidos diferentes à mesma palavra sem motivo
declarado. Uma política que pretendesse incluir o valor teria escrito "a
partir de R$ 100".
**Custo aceito:** R$ 100,00 passa a ser o maior valor reembolsável sem
comprovante fiscal, e um lançamento redondo nesse valor sem nota — como
d-003, uma corrida de táxi de exatamente R$ 100,00 — é aprovado sem
comprovação documental. Aceito porque a alternativa exigiria afirmar que o
RH escreveu "acima de" querendo dizer "a partir de", e a correção dessa
brecha cabe ao texto da política, não à interpretação do motor.
**Consequências verificáveis:**
- d-003 (transporte urbano, 100,00, sem NF) → nota fiscal dispensada; segue
  para o limite de 80,00 → reembolso 80,00, parcial
- d-004 (transporte urbano, 100,01, sem NF) → nota fiscal exigida e ausente;
  reembolso 0,00, recusado, motivos [SEM_NOTA_FISCAL] (RN-004)
- d-013 (hospedagem, 690,00, sem NF) → nota fiscal exigida e ausente;
  reembolso 0,00, recusado, motivos [SEM_NOTA_FISCAL] (RN-004)
- d-010 (hospedagem, 480,00, com NF) → nota fiscal exigida e presente; segue
  para o limite de 250,00 → reembolso 250,00, parcial
- uma despesa de 100,01 com nota fiscal → segue para o limite normalmente

**Regra afetada:** RN-003

### AMB-005 — Efeito da ausência de nota fiscal exigida

**Texto original do RH:** "Nota fiscal é obrigatória acima de R$ 100." (item 5)
A política estabelece quando a nota é exigida, mas não diz o que acontece
quando ela é exigida e não está presente.
**O que não está claro:** quatro leituras — (a) o item é recusado
integralmente; (b) o reembolso é limitado ao teto de dispensa de R$ 100,00;
(c) o item fica pendente de comprovação, com status próprio; (d) o item é
reembolsado normalmente e a saída registra um alerta.
**Decisão:** adotada a leitura (a). Uma despesa que exige nota fiscal e não a
apresenta tem reembolso zero e é classificada como recusada, com motivo
SEM_NOTA_FISCAL. A recusa é integral e independe do limite da categoria: o
item não é avaliado contra o limite, e um lançamento que estaria integral
caso tivesse nota é recusado da mesma forma. O motivo da recusa é registrado
de forma distinta de outras causas de recusa, em particular de
CATEGORIA_INELEGIVEL.
**Justificativa:** "obrigatória" estabelece condição, não recomendação. Sem o
comprovante, a empresa não tem como atestar que a transação ocorreu nem
quanto custou, de modo que não há base para pagar parcela alguma — o
pagamento parcial reembolsaria parte de uma despesa não comprovada. Esta é
também a consequência coerente com a AMB-006, que estabeleceu a natureza
binária da documentação: o comprovante existe ou não existe. A leitura (b)
produziria exatamente o resultado que a justificativa da AMB-006 usou para
rejeitar a leitura pelo valor reembolsável — um jantar de R$ 500 sem
comprovante renderia R$ 60 — esvaziando a regra em alimentação e em
transporte urbano. A leitura (d) trataria "obrigatória" como
"recomendada" e devolveria a decisão à conferência manual. A leitura (c)
tem mérito ao distinguir o que tem conserto do que não tem, mas exigiria um
estado adicional e um campo de valor potencial que a política não prevê, além
de abrir a questão nova de até quando a nota pode ser apresentada; o ganho
que ela traz é capturado aqui pelo motivo estruturado, sem criar estado novo.
**Custo aceito:** o salto na fronteira é máximo. d-003 (R$ 100,00, sem nota)
rende R$ 80,00 e d-004 (R$ 100,01, sem nota) rende R$ 0,00 — um centavo que
a empresa não pagaria em nenhum dos casos, já que o teto de transporte é
R$ 80,00, determina uma diferença de R$ 80,00 no reembolso. Aceito porque a
regra é documental e não comporta gradação: admitir pagamento parcial sem
comprovante seria admitir que a obrigatoriedade é negociável. O custo recai
sobretudo sobre fornecedores que habitualmente não emitem nota, conforme já
registrado na §10 — d-013 perde integralmente o reembolso.
**Consequências verificáveis:**
- d-004 (transporte urbano, 100,01, sem NF) → reembolso 0,00, recusado,
  motivos [SEM_NOTA_FISCAL]
- d-013 (hospedagem, 690,00, sem NF) → reembolso 0,00, recusado, motivos
  [SEM_NOTA_FISCAL]
- alimentação de 500,00 sem NF → reembolso 0,00, recusado, motivos
  [SEM_NOTA_FISCAL], embora o teto da categoria seja 60,00
- d-003 (transporte urbano, 100,00, sem NF) → nota não exigida; reembolso
  80,00, parcial — inalterado
- transporte urbano de 100,01 com NF → reembolso 80,00, parcial — inalterado

**Ordem de aplicação:** a verificação de nota fiscal precede a aplicação do
limite; registrado na §8.
**Em aberto:** registrado na §10.
**Regra afetada:** RN-003, RN-004

### AMB-006 — Valor de referência para a exigência de nota fiscal

**Texto original do RH:** item 5, "Nota fiscal é obrigatória acima de R$ 100." A política
informa a fronteira, mas não diz qual valor é comparado com ela.
**O que não está claro:** três leituras — (a) o valor original da despesa, já
normalizado; (b) o valor reembolsável após a aplicação do limite; (c) o valor
agregado de um grupo de despesas (dia, fornecedor ou ambos).
**Decisão:** adotada a leitura (a). A exigência de nota fiscal é avaliada sobre
o valor da despesa lançado, já normalizado conforme a AMB-014, antes de
qualquer aplicação de limite.
**Justificativa:** a nota fiscal comprova que a transação existiu e custou o
valor declarado — o objeto da comprovação é a despesa, não a parcela que a
empresa decide pagar. A leitura (b) esvaziaria a regra: como os limites são
sempre os nominais (AMB-007) — alimentação R$ 60 e transporte urbano R$ 80,
ambos abaixo de R$ 100 —, nenhuma despesa dessas categorias poderia exigir
nota, qualquer que fosse o valor lançado, e um jantar de R$ 500 sem
comprovante renderia R$ 60. O item 5 da política passaria a valer apenas
para hospedagem, a única categoria cujo limite supera R$ 100 — e nela a
leitura (b) produz o mesmo resultado que a (a), porque o limite de
hospedagem é sempre R$ 250,00 (AMB-009). A leitura (c) reintroduziria
agregação, que a AMB-001 rejeitou, abriria a questão nova de qual é o grupo,
e alteraria uma consequência já registrada na AMB-004.

Quanto à coerência com a AMB-002: lá se rejeitou punir desvio mínimo porque
a regra é de cálculo e opera sobre uma régua contínua — faz sentido pagar
proporcionalmente. Aqui a regra é documental, e documentação é binária: o
comprovante existe ou não existe, não há meio comprovante. Um limiar nítido
é próprio da natureza da regra, não uma inconsistência com a decisão anterior.

**Custo aceito:** a fronteira fica abrupta no valor lançado. d-004 (R$ 100,01)
difere de d-003 (R$ 100,00) por um centavo que a empresa não pagaria em
nenhum dos dois casos, já que o teto de transporte é R$ 80 — e ainda assim
a exigência de nota muda. A severidade dessa diferença foi fixada pela
AMB-005: a ausência de nota recusa o item integralmente, de modo que d-004
perde R$ 80,00 por conta do centavo. Registro aqui que o desconforto existe;
ele é aceito na AMB-005 como custo da natureza documental da regra.
**Consequências verificáveis:**
- d-004 (transporte urbano, 100,01, sem NF) → valor de referência 100,01;
  nota fiscal exigida e ausente; reembolso 0,00, recusado, motivos
  [SEM_NOTA_FISCAL] (RN-004)
- d-003 (transporte urbano, 100,00, sem NF) → valor de referência 100,00;
  nota fiscal não exigida; reembolso 80,00, parcial
- d-013 (hospedagem, 690,00, sem NF) → valor de referência 690,00; nota
  fiscal exigida e ausente; resultado idêntico nas leituras (a) e (b),
  porque o limite de hospedagem é sempre R$ 250,00 (AMB-009); reembolso 0,00,
  recusado, motivos [SEM_NOTA_FISCAL] (RN-004)
- alimentação de 500,00 sem NF → nota fiscal exigida e ausente (na leitura
  (b) não seria exigida, pois o teto é 60,00); reembolso 0,00, recusado,
  motivos [SEM_NOTA_FISCAL] (RN-004)

**Sem circularidade:** a exigência de nota passa a depender apenas do valor
normalizado, de modo que a cadeia valor → nota fiscal → limite → reembolso
é linear. A leitura (b) exigiria definir explicitamente qual "valor
reembolsável" é usado, sob pena de contradição lógica — um item cujo
reembolso cai abaixo de R$ 100 por efeito da ausência de nota deixaria de
exigir nota, e voltaria a exigi-la ao ser reembolsado integralmente.
**Regra afetada:** RN-003

### AMB-007 — Caracterização de "em viagem" e ampliação de limites

**Texto original do RH:** "Colaborador em viagem tem limites ampliados em 50%." (item 6)
**O que não está claro:** a política não define o que caracteriza estar em
viagem, não diz se o estado se aplica ao colaborador no período, ao dia ou à
despesa, e não informa quais limites são ampliados. A entrada não possui
nenhum campo que registre o estado de viagem — os únicos dados por despesa
são data, categoria, descrição, fornecedor, valor e presença de nota fiscal.
As leituras consideradas foram: (a) declarar a caracterização fora de escopo;
(b) estender a entrada com um campo explícito; (c) inferir viagem a partir de
lançamentos de hospedagem; (d) inferir a partir de termos na descrição ou no
fornecedor; (e) considerar todo o período em viagem se houver qualquer
hospedagem nele.
**Decisão:** adotada a leitura (a). A caracterização de estar em viagem está
fora do escopo deste sistema. O motor aplica sempre os limites nominais de
cada categoria e o item 6 da política não é implementado. A premissa global
declarada nas seções 5 e 7 deixa de ser provisória e passa a ser regra
permanente: todas as despesas são avaliadas com os limites nominais.
**Justificativa:** estar em viagem é um fato administrativo — alguém autoriza o
deslocamento — e a entrada não o registra. As leituras (c), (d) e (e)
deduzem a causa a partir de consequências e erram de formas previsíveis: a
hospedagem não captura deslocamento sem pernoite, como a corrida ao
aeroporto de d-003; o texto livre é sensível a grafia, produz falso positivo
em despesas locais que mencionem hotel, e é redigido pelo próprio
colaborador, que passaria a controlar o próprio limite; e considerar o
período inteiro ampliaria o limite de despesas sem relação alguma com a
viagem, como o almoço de plantão em sábado de d-012. As três também fariam o
reembolso de uma despesa depender da existência de outra no mesmo lote,
contrariando a justificativa da AMB-001, segundo a qual o colaborador sabe
se uma despesa será paga no momento em que a faz. A leitura (b) exigiria
estender um formato de entrada que o enunciado declara fixo, e produziria
viagem autodeclarada. Entre inferir um fato que o dado não contém e declarar
que o sistema não o determina, a segunda é a única posição que o motor
consegue sustentar diante de uma auditoria.
**Custo aceito:** o item 6 da política não é cumprido pelo motor, e o erro é
sempre na mesma direção — contra o colaborador em viagem, nunca contra a
empresa. No lote de exemplo, d-010 (hospedagem de 480,00 com nota)
recebe 250,00 em vez dos 375,00 que receberia se a diária fosse ampliada;
a diferença é de 125,00. Despesas de
alimentação e transporte de colaboradores efetivamente em viagem são
limitadas a 60,00 e 80,00 em vez de 90,00 e 120,00. A regularização desses
casos permanece com o financeiro, por conferência manual. Aceito porque a
alternativa é o motor conceder ampliação com base em indício que ele não
consegue comprovar, produzindo resultado que o auditor não tem como
verificar e que o colaborador pode influenciar.
**Consequências verificáveis:**
- todos os limites são os nominais: alimentação 60,00; transporte urbano
  80,00; hospedagem 250,00 por lançamento (uma diária)
- d-010 (hospedagem, 480,00, com NF) → limite 250,00, sem ampliação;
  reembolso 250,00, parcial (AMB-009)
- d-011 (alimentação, 15/07, no mesmo fornecedor de d-010) → limite 60,00;
  a proximidade com um lançamento de hospedagem não altera o limite
- d-003 (transporte urbano, 100,00, "Corrida aeroporto") → limite 80,00; a
  descrição não altera o limite
- d-013 (hospedagem, 690,00, sem NF) → recusado pela RN-004; a ausência de
  efeito sobre outras despesas é consequência desta decisão, não da recusa
- todos os aceites e casos de borda já registrados permanecem válidos sem
  alteração

**Em aberto:** registrado na §10.
**Regra afetada:** RN-001, RN-002; §3 (fora de escopo)

### AMB-008 — Dado que determina a pertinência ao período

**Texto original do RH:** "Despesas devem ser lançadas dentro do período de
competência." (item 7)
**O que não está claro:** a palavra "lançadas" sugere a data do lançamento, que
a entrada não possui — cada despesa traz apenas uma data, sem indicação de
quando foi registrada. As leituras consideradas foram: (a) usar a data da
despesa; (b) usar a data do lançamento, que não existe, deixando a regra sem
efeito ou fora de escopo; (c) aceitar qualquer despesa ocorrida até o fim do
período, recusando apenas datas futuras.
**Decisão:** adotada a leitura (a). A pertinência ao período é avaliada pela
data da despesa, comparada ao intervalo definido na AMB-016.
**Justificativa:** a data da despesa é o único dado de data que a entrada
fornece, e é estruturada e precisa. Lida como regra contábil, a frase do item
7 determina que cada despesa seja lançada na competência a que pertence — e
uma despesa pertence à competência em que ocorreu. A leitura (b) deixaria o
item 7 sem efeito algum: o motor reembolsaria despesas de qualquer data,
inclusive de anos anteriores. A leitura (c) faria uma despesa de abril ser
paga no fechamento de julho, dissolvendo a separação contábil por mês, que é
a finalidade do item 7.

Sobre a diferença em relação à AMB-007: ali a caracterização de viagem foi
declarada fora de escopo porque o dado não existe e não há substituto
confiável — toda inferência disponível era frágil ou manipulável. Aqui o dado
literal ("data do lançamento") também não existe, mas há substituto preciso e
estruturado: a data da despesa. Declarar a regra fora de escopo havendo dado
adequado seria descartar informação que a entrada fornece.

**Custo aceito:** uma despesa legítima apresentada no lote de uma competência
diferente da sua perde o reembolso. d-008 (alimentação de 41,00, com nota
fiscal, ocorrida em 15/04 e apresentada no lote de julho) não é reembolsada,
embora seja uma despesa válida em todos os outros aspectos. Como na AMB-009,
a correção cabe ao processo de lançamento: apresentar a despesa no lote da
competência correta.
**Consequências verificáveis:**
- d-008 (15/04, lote de julho) → fora do período; reembolso 0,00, recusado,
  motivos [FORA_DA_COMPETENCIA] (RN-006)
- as despesas entre 03/07 e 22/07 estão dentro do período, exceto d-009,
  que é erro de item e não é avaliada por esta regra (RN-010)
- a descrição de d-008 ("Almoco de abril lancado com atraso") não é
  interpretada, em coerência com a AMB-007 e a AMB-009

**Regra afetada:** RN-005

### AMB-009 — Número de diárias em um lançamento de hospedagem

**Texto original do RH:** "Hospedagem tem limite de R$ 250 por diária." (item 3)
**O que não está claro:** a política estabelece o limite por diária, mas a
entrada não informa quantas diárias um lançamento cobre. Cada despesa tem
uma única data, sem entrada nem saída, e nenhum campo de quantidade. O
número aparece apenas em texto livre na descrição, com redação variável —
"2 diarias" em d-010 e "3 noites" em d-013. As leituras consideradas foram:
(a) cada lançamento vale uma diária; (b) extrair o número da descrição;
(c) inferir o número a partir do valor, dividindo pelo limite; (d) estender
a entrada com um campo explícito.
**Decisão:** adotada a leitura (a). Cada lançamento de hospedagem é avaliado
como uma diária, e seu limite é sempre R$ 250,00. Uma estadia de várias
noites deve ser lançada como uma despesa por diária, e cada lançamento de
diária traz a data da noite a que se refere, de modo que diárias consecutivas
tenham datas distintas e não constituam duplicata (AMB-011). A descrição não
é interpretada.
**Justificativa:** o número de diárias não é dado estruturado, e as demais
leituras o produzem de formas que o motor não consegue sustentar. A leitura
(b) faria o limite depender de texto redigido pelo próprio colaborador —
escrever uma diária a mais na descrição vale R$ 250,00 de limite adicional,
sem que nada na despesa mude — e é o mesmo motivo pelo qual a AMB-007
rejeitou inferir viagem a partir de texto livre; o vocabulário é
comprovadamente aberto, já que o próprio lote de exemplo usa duas
palavras distintas para a mesma coisa. A leitura (c) esvaziaria o item 3 da
política: como o número de diárias seria sempre o suficiente para cobrir o
valor, nenhuma hospedagem jamais excederia o limite. A leitura (d) estenderia
um formato de entrada que o enunciado declara fixo, e no lote de exemplo
o resultado dependeria inteiramente do valor padrão adotado. A leitura (a) é
a única que produz o limite a partir de campos estruturados, é imune a
redação, e converte o "por diária" da política em uma regra de lançamento
verificável: uma diária, um lançamento.
**Custo aceito:** uma estadia lançada de forma consolidada é penalizada pela
forma do lançamento, não pelo valor. d-010 (R$ 480,00 por duas noites,
R$ 240,00 por noite, abaixo do limite) recebe R$ 250,00 e perde R$ 230,00 —
sendo que as duas diárias, lançadas separadamente, cada uma com a data da sua
noite, seriam reembolsadas integralmente. A correção cabe ao processo de lançamento, não ao motor.
Aceito porque a alternativa é conceder limite adicional com base em
informação que o colaborador escreve e que o sistema não tem como conferir.
**Consequências verificáveis:**
- o limite de qualquer lançamento de hospedagem é R$ 250,00
- d-010 (hospedagem, 480,00, com NF, "Hotel Rio - 2 diarias") → reembolso
  250,00, parcial; excedente de 230,00 não reembolsado
- d-013 (hospedagem, 690,00, sem NF) → reembolso 0,00, recusado, motivos
  [SEM_NOTA_FISCAL]; a recusa precede a avaliação do limite, de modo que esta
  decisão não altera seu resultado
- hospedagem de 400,00 com NF descrita como "1 diaria" e a mesma hospedagem
  de 400,00 descrita como "2 diarias" produzem o mesmo resultado: 250,00,
  parcial
- hospedagem de 200,00 com NF → reembolso 200,00, integral

**Regra afetada:** RN-001

### AMB-010 — Efeito de uma despesa fora do período

**Texto original do RH:** "Despesas devem ser lançadas dentro do período de
competência." (item 7) A política não estabelece a consequência de não
estarem.
**O que não está claro:** quatro leituras — (a) recusar com motivo próprio; (b)
excluir do processamento, de modo que o item não apareça na saída; (c) marcar
como pendente para a competência correta; (d) reembolsar normalmente e
registrar um alerta.
**Decisão:** adotada a leitura (a). Uma despesa cuja data esteja fora do
período tem reembolso 0,00 e é classificada como recusada, com motivo
FORA_DA_COMPETENCIA. O item aparece na saída com seu motivo, como qualquer
outro.
**Justificativa:** é o mesmo padrão adotado na AMB-005 — o item permanece
visível e o colaborador sabe por que não foi reembolsado e o que fazer, que é
apresentá-lo no lote da competência correta. A leitura (b) retiraria o item
da saída, e o enunciado exige que o motor justifique cada decisão; um item
ausente não distingue "excluído" de "esquecido". As leituras (c) e (d) foram
rejeitadas na AMB-005 pelos mesmos motivos que valem aqui: a primeira cria um
estado adicional e abre a questão de até quando a reapresentação é possível; a
segunda trata uma obrigação da política como recomendação.
**Custo aceito:** a saída passa a conter itens com reembolso zero que não
foram avaliados contra as demais regras de valor. Aceito porque a
visibilidade do item e a explicitação do motivo valem mais que a brevidade da
saída.
**Consequências verificáveis:**
- d-008 (alimentação, 41,00, com NF, 15/04) → reembolso 0,00, recusado,
  motivos [FORA_DA_COMPETENCIA]
- d-014 (31/07) → dentro do período; esta regra não se aplica
- uma despesa fora do período não é avaliada por nenhuma regra de valor e não
  integra grupo de duplicatas; as demais regras de elegibilidade continuam a
  ser avaliadas e acrescentam seus motivos (RN-007)

**Regra afetada:** RN-006

### AMB-011 — Duplicidade: critério e tratamento

**Texto original do RH:** "Duplicatas devem ser tratadas." (item 8) A política
não define o que caracteriza uma duplicata nem o que significa tratá-la.
**O que não está claro:** duas questões. (i) qual conjunto de campos precisa
coincidir para que duas despesas sejam consideradas a mesma; (ii) o que o
sistema faz com um grupo de duplicatas.
**Decisão:** duas ou mais despesas constituem um grupo de duplicatas quando
coincidem em data, categoria normalizada (AMB-013), fornecedor normalizado
(AMB-013) e valor normalizado (AMB-014). A descrição e a presença de nota
fiscal não integram o critério. Dentro de um grupo, a despesa de menor
identificador — na ordem dos pontos de código Unicode dos caracteres,
comparados um a um (RN-009) — é avaliada normalmente e as demais são
recusadas, com o motivo DUPLICATA em sua lista de motivos. Despesas já
recusadas por outra regra de elegibilidade não integram grupos de
duplicatas.
**Justificativa:** incluir a descrição no critério faria uma diferença de
acentuação ou de redação desfazer a detecção, e a spec já estabeleceu que a
descrição não é interpretada; exigir apenas o identificador repetido tornaria
a regra inócua, já que identificadores distintos são atribuídos a cada
lançamento. Quanto ao tratamento, recusar todas as despesas do grupo puniria
também o lançamento legítimo, e reembolsar todas registrando um alerta
trataria "devem ser tratadas" como recomendação — objeção já levantada na
AMB-005. Manter a de menor identificador, e não a primeira da entrada,
preserva o critério de aceite da RN-001, segundo o qual inverter a ordem das
despesas na entrada não altera nenhum resultado. A ordenação pelos pontos de
código é a única que produz resultado determinístico
independentemente de configuração regional ou de convenção alfabética local;
e é o ponto de código, e não a forma como o texto é codificado, porque a
comparação precisa ser independente da representação interna de texto
adotada pela implementação: em representações que dividem um caractere fora
do plano multilíngue básico em duas unidades, a ordem por unidade diverge da
ordem por caractere, e a despesa mantida mudaria com a representação;
uma ordenação que ignore a caixa deixaria "d-006" e "D-006" empatados, sem
critério de desempate, e a independência de ordem exigida pela RN-001 só se
garante com um critério total.
**Custo aceito:** duas despesas legítimas de mesmo valor, no mesmo fornecedor e
na mesma data — dois almoços de equipe no mesmo restaurante, por exemplo —
são tratadas como duplicata, e a segunda é recusada. A duplicidade é
inferência, não fato observado: o sistema não tem como distinguir a cópia do
lançamento genuíno. A correção cabe ao colaborador, que pode diferenciar os
lançamentos pelo valor efetivo de cada um.
**Consequências verificáveis:**
- d-006 e d-007 (ambos 09/07, alimentação, Bistro Central, 54,90) formam
  grupo de duplicatas; d-006 → reembolso 54,90, integral, lista de motivos
  vazia; d-007 → reembolso 0,00, recusado, motivos [DUPLICATA]
- inverter a ordem de d-006 e d-007 na entrada não altera nenhum dos dois
  resultados
- duas despesas iguais em todos os campos do critério, de valor até
  R$ 100,00, uma com nota fiscal e outra sem, formam o mesmo grupo; a de
  menor identificador é mantida, independentemente de qual delas possui a
  nota
- duas despesas iguais no critério, mas com descrições diferentes, formam
  grupo de duplicatas
- uma despesa fora do período (RN-006) não integra grupo de duplicatas, nem
  como mantida nem como recusada
- identificadores que diferem apenas por U+FF71 num e U+1F600 no outro → o
  de U+FF71 precede, por ter o menor ponto de código

**Regra afetada:** RN-009; AMB-009 (data de cada diária)

### AMB-012 — Tratamento de lançamentos de valor negativo (dissolvida)

**Texto original do RH:** não há; a política não menciona créditos, estornos
ou devoluções.
**O que não estava claro:** o lote de exemplo contém um lançamento de valor
negativo descrito como estorno (d-009, −45,00), e era necessário decidir se
ele seria ignorado, abatido do total do período, abatido do limite do dia, ou
tratado de outra forma.
**Decisão:** dissolvida pela AMB-017. O critério estrutural ali adotado
estabelece que valor menor ou igual a zero constitui erro de item, de modo que
d-009 recebe classificação erro, reembolso 0,00, e não é avaliado por nenhuma
regra de elegibilidade ou de valor. Não resta decisão de negócio a tomar sobre
lançamentos negativos: eles não são dado válido segundo o contrato de
entrada.

Registrada para manter a rastreabilidade da numeração e documentar que a
questão foi identificada antes de ser eliminada.
**Regra afetada:** nenhuma (dissolvida).

### AMB-013 — Identidade e elegibilidade da categoria

**Texto original do RH:** "Categorias fora da política não são reembolsáveis."
(item 9) A política nomeia três categorias — alimentação, transporte urbano e
hospedagem — ao estabelecer limites, e não apresenta lista de categorias
inelegíveis.
**O que não está claro:** duas questões. (i) se a comparação da categoria
distingue maiúsculas de minúsculas, já que d-014 traz "ALIMENTACAO"; (ii) se
as três categorias nomeadas esgotam as elegíveis, ou se são apenas aquelas
que possuem limite, podendo haver outras elegíveis sem teto.
**Decisão:** a categoria é normalizada antes da comparação, convertendo-se
maiúsculas em minúsculas — pelo mapeamento completo de caixa do padrão
Unicode, independente de localidade (RN-008) — e removendo-se espaços nas
extremidades. Nenhuma
outra transformação é aplicada: acentuação, separadores e sinônimos não são
normalizados. As categorias elegíveis são exatamente três — alimentacao,
transporte_urbano e hospedagem, na grafia do vocabulário de categoria
(§4.2, Notação). Qualquer outra categoria é inelegível, e a
despesa é recusada com o motivo CATEGORIA_INELEGIVEL em sua lista de motivos.
**Justificativa:** a categoria é campo estruturado com vocabulário fechado, e
uniformizar a caixa não constitui interpretação de texto livre — distingue-se,
nesse ponto, do que a AMB-007 e a AMB-009 recusaram. Tratar "ALIMENTACAO"
como categoria distinta faria d-014 perder os R$ 61,00 integrais por uma
diferença de grafia, o mesmo tipo de desproporção que a AMB-002 rejeitou ao
decidir o reembolso parcial. O alcance da normalização é mantido estreito
justamente para que a fronteira permaneça verificável: cada transformação
adicional aproximaria a regra da inferência. Quanto à segunda questão, admitir
categorias elegíveis fora das três esvaziaria o item 9 e eliminaria todo
controle de valor sobre elas, já que a política não estabelece limite para
categoria que não nomeia — um lançamento de R$ 5.000,00 em categoria
arbitrária seria reembolsado integralmente.

**Extensão ao fornecedor:** a mesma normalização, com o mesmo alcance — caixa
e espaços nas extremidades, nada mais —, aplica-se ao fornecedor, usado no
critério de duplicidade (AMB-011). "Bistro Central" e "BISTRO CENTRAL" são o
mesmo fornecedor, e deixar a comparação exata faria uma diferença de grafia
desfazer a detecção, que é o que a exclusão da descrição do critério
pretendia evitar. O alcance permanece estreito pelo mesmo motivo declarado
acima: ampliá-lo aproximaria a regra da inferência.

**Custo aceito:** despesas de trabalho legítimas cuja categoria a política não
nomeia nunca são reembolsadas pelo motor. d-005 (espaço de coworking, R$ 89,00,
com nota fiscal) é recusado, embora seja despesa plausível de trabalho. A
inclusão de novas categorias cabe ao RH, por alteração da política.
**Consequências verificáveis:**
- d-014 ("ALIMENTACAO", 61,00, com NF, 31/07) → categoria normalizada para
  alimentacao; dentro do período; reembolso 60,00, parcial; lista de motivos
  vazia
- d-005 ("coworking", 89,00, com NF) → reembolso 0,00, recusado, motivos
  [CATEGORIA_INELEGIVEL]
- "Alimentacao", "ALIMENTACAO" e " alimentacao " são a mesma categoria
- "alimentação", com cedilha e til, não é normalizada para "alimentacao" e é
  categoria inelegível
- "transporte-urbano" não é normalizado para "transporte_urbano" e é
  categoria inelegível
- "Bistro Central", "BISTRO CENTRAL" e " Bistro Central " são o mesmo
  fornecedor; "Bistrô Central" não é

**Regra afetada:** RN-008; RN-009 (fornecedor normalizado); RN-004 (o motivo
CATEGORIA_INELEGIVEL, já citado ali, passa a existir)

### AMB-014 — Precisão monetária

**Texto original do RH:** não há. A política expressa seus limites em reais
inteiros e não estabelece tratamento para valores com precisão inferior ao
centavo. O lote de exemplo contém d-011 com valor 33,333.
**O que não está claro:** duas questões. (i) como tratar um valor com mais de
duas casas decimais — arredondar, truncar ou considerar dado inválido; (ii)
em que momento a normalização ocorre: antes de qualquer regra, ou apenas
sobre o valor a ser reembolsado.
**Decisão:** todo valor monetário da entrada é normalizado para duas casas
decimais por arredondamento, com a metade arredondada para cima, antes da
aplicação de qualquer regra. O valor normalizado é o único valor considerado
em todo o processamento — comparações de limite, exigência de nota fiscal,
critério de duplicidade e cálculo do reembolso — e é também o valor
apresentado na saída. A normalização precede a validação de faixa da RN-010.
**Justificativa:** o arredondamento comercial com a metade para cima é a
convenção que o colaborador e o auditor reconhecem sem precisar da regra
escrita; o arredondamento para o par, embora estatisticamente neutro,
produziria resultados contraintuitivos em valores vizinhos, e o truncamento
favoreceria sistematicamente a empresa. Tratar precisão abaixo do centavo
como dado inválido faria d-011 perder os R$ 33,33 integrais por uma questão
de formato, mesma desproporção que a AMB-013 rejeitou ao normalizar a caixa
da categoria. Quanto ao momento, normalizar na entrada é o que os registros
já existentes pressupõem — RN-003, AMB-004, AMB-006 e RN-009 referem-se ao
valor normalizado — e garante que a saída exiba o mesmo valor sobre o qual as
regras operaram; normalizar apenas no resultado produziria uma despesa
recusada por exceder R$ 100,00 exibindo valor de R$ 100,00, o que o auditor
não conseguiria verificar. A normalização precede a validação de faixa porque,
na ordem inversa, um valor de 0,004 seria aceito como positivo e passaria a
constar como despesa de R$ 0,00 classificada como integral.
**Custo aceito:** um valor cuja fração seja inferior a meio centavo deixa de
ser considerado, e um valor cuja fração seja exatamente meio centavo é
arredondado a favor do colaborador. Um lançamento de R$ 100,004 passa a valer
R$ 100,00 e escapa da exigência de nota fiscal por uma diferença que não
existe em dinheiro. Aceito porque a alternativa é operar sobre frações que não
podem ser pagas, produzindo totais que não conferem com a soma dos itens.
**Consequências verificáveis:**
- d-011 (alimentação, 33,333, com NF) → valor normalizado 33,33; reembolso
  33,33, integral
- uma despesa de 100,004 → valor normalizado 100,00; nota fiscal não exigida
- uma despesa de 100,005 → valor normalizado 100,01; nota fiscal exigida
- uma despesa de 60,004 em alimentação → valor normalizado 60,00; reembolso
  60,00, integral
- 54,90 e 54,904 normalizam ambos para 54,90 e, coincidindo nos demais campos,
  constituem duplicata
- uma despesa de 0,004 → valor normalizado 0,00; erro de item, pois a
  normalização precede a validação de faixa (RN-010)

**Regra afetada:** RN-011; RN-010 (faixa sobre o valor normalizado); RN-003 e
RN-009 (referências ao valor normalizado)

### AMB-015 — Campos da entrada sem regra associada

**Texto original do RH:** não há. A entrada traz o centro de custo do
colaborador, e nenhum item da política o menciona. O mesmo vale para o
identificador e o nome do colaborador.
**O que não está claro:** se esses campos afetam o cálculo, se devem ser
reproduzidos na saída, e se são obrigatórios no contrato de entrada. As
leituras consideradas foram: (a) declará-los fora de escopo, lidos e sem
efeito; (b) mantê-los fora do cálculo mas reproduzi-los na saída; (c)
atribuir-lhes efeito sobre limites ou elegibilidade.
**Decisão:** adotada a leitura (a), aplicada a todos os campos da entrada que
nenhuma regra utiliza. São eles, na entrada de referência: o identificador, o
nome e o centro de custo do colaborador; o rótulo de competência do período
(AMB-016); e a descrição da despesa, que não é utilizada por regra alguma
(AMB-007, AMB-008, AMB-009). Esses campos são aceitos na entrada, não
participam de nenhuma regra, não afetam nenhum resultado, e são opcionais no
contrato de entrada: sua ausência não constitui erro. O mesmo vale para o
tipo do dado: um desses campos informado com tipo incompatível é tratado
como ausente, e não constitui erro — um campo que nenhuma regra utiliza não
pode alterar resultado algum, e um tipo incompatível nele é apenas mais uma
forma de o campo não trazer informação aproveitável. O fornecedor não
integra esta lista: é utilizado pela RN-009 como chave de comparação e é,
portanto, campo obrigatório, de modo que a sua ausência ou o seu tipo
incompatível constituem erro de item (RN-010). O que não é utilizado é o seu
conteúdo — o fornecedor não é interpretado, apenas comparado após a
normalização da RN-008.
**Justificativa:** atribuir efeito a um campo que a política não menciona seria
criar regra de negócio inexistente, a objeção mais forte entre todas as
leituras rejeitadas nesta spec. Reproduzi-los na saída comprometeria o
contrato de saída com dados que o motor não utiliza, sem que a política o
exija. Torná-los obrigatórios faria um lote inteiro ser rejeitado pela RN-010
por ausência de campo sem efeito algum sobre o resultado, desproporção que a
decisão evita. A decisão é enunciada de forma geral, e não apenas para o
centro de custo, porque a situação é idêntica para os três campos e um
critério único evita decisões separadas para cada um.
**Custo aceito:** se a política passar a estabelecer regras por centro de
custo — limites diferenciados, por exemplo — a spec precisará ser alterada
para incorporar o campo. Hoje não há perda, pois nenhum resultado depende
dele.
**Consequências verificáveis:**
- o centro de custo CC-ENG-PLATAFORMA do lote de exemplo não altera
  nenhum resultado
- um lote cujo colaborador não traga centro de custo é processado normalmente
- dois lotes idênticos em tudo exceto o centro de custo produzem resultados
  idênticos
- uma despesa sem descrição é processada normalmente
- um lote sem rótulo de competência, com início e fim presentes, é processado
  normalmente
- uma despesa sem fornecedor → erro de item (RN-010), por ser campo
  obrigatório
- uma despesa com descrição informada como número → processada
  normalmente; a descrição é tratada como ausente
- uma despesa com fornecedor informado como número → erro de item (RN-010),
  tipo incompatível em campo obrigatório

**Regra afetada:** RN-010 (tipo incompatível em campo opcional não é erro);
§3 (fora de escopo); §4 (contrato de entrada, campos opcionais e
obrigatoriedade do fornecedor)

### AMB-016 — Representação do período de competência

**Texto original do RH:** "Despesas devem ser lançadas dentro do período de
competência." (item 7) A política não diz como o período é representado. A
entrada traz três informações sobre ele: um rótulo de competência com o ano e
o mês ("2026-07"), uma data de início ("2026-07-01") e uma data de fim
("2026-07-31").
**O que não está claro:** duas questões independentes. (i) qual informação
define o período quando há mais de uma — o rótulo de competência, o par de
datas de início e de fim, ou as três precisam concordar; (ii) se as datas de
borda pertencem ao período.
**Decisão:** o período é o intervalo definido pelas datas de início e de fim,
com ambas as bordas inclusivas. Uma despesa cuja data seja igual à data de
início ou igual à data de fim está dentro do período. O rótulo de
competência é tratado como informativo e não participa da avaliação;
divergência entre ele e o par de datas não impede o processamento.
**Justificativa:** o par de datas de início e de fim é o dado mais expressivo
— o rótulo de competência é derivável dele, mas o inverso não: um fechamento
que vá de 26/06 a 25/07 não cabe em nenhum rótulo de mês. Exigir concordância
entre as três informações faria da divergência um erro de lote (RN-010), e um
engano de digitação no cabeçalho bloquearia o reembolso de todas as despesas
do lote, custo alto para proteger contra algo que a entrada de referência não
apresenta. Quanto às bordas, as datas de início e de fim são datas sem hora,
e "data de fim: 31/07" em linguagem corrente inclui o dia 31; adotar
intervalo semiaberto faria o último dia de todo mês não pertencer a
competência alguma, já que também não pertenceria à competência seguinte — o
que não é convenção de intervalo, é perda de dia.
**Custo aceito:** o rótulo de competência deixa de ter efeito sobre o
cálculo. Um lote cujo rótulo diga "2026-07" mas cujo par de datas cubra
agosto será processado como agosto, sem alerta. Aceito porque o par de datas é a
representação precisa, e tratar a divergência como erro de lote (RN-010)
bloquearia o lote inteiro por uma inconsistência que não impede a avaliação.
**Consequências verificáveis:**
- o período do lote de exemplo é [2026-07-01, 2026-07-31], ambas inclusivas
- d-014 (31/07) está dentro do período; seu resultado segue para as demais
  regras
- uma despesa em 2026-07-01 está dentro do período
- uma despesa em 2026-08-01 está fora do período

**Regra afetada:** RN-005

### AMB-017 — Distinção entre recusa e erro de entrada

**Texto original do RH:** não há. A política trata de despesas bem formadas e
não estabelece o que fazer diante de dado que não possa ser avaliado.
**O que não está claro:** casos como d-005 (categoria não prevista) e d-009
(valor negativo) não são cobertos pela política, e não havia critério
declarado que determinasse se constituem decisão de negócio ou dado
inválido. As leituras consideradas foram: (a) critério estrutural — erro é o
dado que não pode ser avaliado, recusa é o dado bem formado que uma regra
rejeita; (b) critério pela política — recusa quando há regra que rejeite,
erro quando nenhuma regra cobre o caso; (c) tratar tudo como recusa com
motivo, exceto o que impeça o processamento do lote.
**Decisão:** adotado o critério estrutural (a). Distinguem-se três situações:

1. Erro de lote — a estrutura da entrada impede qualquer avaliação: data de
   início posterior à data de fim, período ausente, ou a entrada não pode ser
   lida, entre outras causas declaradas na RN-010. Nenhuma despesa é
   processada e o lote é rejeitado.
2. Erro de item — uma despesa não pode ser avaliada por campo obrigatório
   ausente, tipo incompatível, data inválida ou valor normalizado (AMB-014)
   menor ou igual a zero. A despesa recebe a classificação erro, com
   reembolso 0,00, e não é avaliada por nenhuma regra de elegibilidade ou de
   valor. As demais despesas do lote são processadas normalmente.
3. Recusa — a despesa é bem formada e uma regra de elegibilidade a rejeita.
   Recebe a classificação recusada, com seus motivos.

Os valores monetários aceitos são estritamente maiores que zero; um valor
normalizado (AMB-014) igual ou inferior a zero constitui erro de item.

**Justificativa:** o critério estrutural mantém a fronteira estável — depende
do contrato da entrada, não do conjunto de regras vigentes. O critério (b) a
deslocaria a cada regra acrescentada: um caso hoje classificado como erro
passaria a recusa assim que uma regra o cobrisse, sem que nada no dado
tivesse mudado. O critério (c) faria a lista de motivos misturar decisões de
negócio com problemas de formato, e o colaborador deixaria de distinguir
"não foi reembolsado" de "foi lançado incorretamente", que exigem ações
diferentes. Quanto ao estado adicional: a AMB-005 recusou criar um quarto
estado ao rejeitar "pendente", mas aquele seria um estado de negócio que a
política não previa; "erro" é de outra natureza — não é uma decisão sobre o
reembolso, é a constatação de que não há decisão possível. Manter essa
constatação sob a mesma classificação de uma recusa fundamentada confundiria
as duas.
**Custo aceito:** a decisão exige que o contrato de entrada seja declarado na
§4 — campos obrigatórios, tipos e faixas admitidas — e torna o sistema mais
rígido quanto à forma do dado. Um lote inteiro é rejeitado por uma
inconsistência no cabeçalho do período, e uma despesa isolada é descartada
por um campo ausente. Aceito porque avaliar dado que não se pode interpretar
produziria resultado que ninguém consegue justificar.
**Consequências verificáveis:**
- d-009 (transporte urbano, −45,00) → erro de item; reembolso 0,00; não é
  avaliado por nenhuma regra de elegibilidade
- d-005 (coworking, 89,00) → bem formado; recusado, motivos
  [CATEGORIA_INELEGIVEL]; não é erro
- uma entrada com data de início posterior à data de fim → lote rejeitado,
  nenhuma despesa processada
- uma despesa sem o campo de valor → erro de item; as demais despesas do
  lote são processadas
- um erro de item em uma despesa não impede o processamento das demais

**Contrato de entrada:** declarado na §4.1.
**Regra afetada:** RN-010; RN-007 (quarto valor de classificação); RN-009
(despesa com erro não integra grupo)

### AMB-018 — Motivos múltiplos de recusa

**Texto original do RH:** não há. A dúvida surgiu das decisões AMB-005 e
AMB-010, que criaram dois motivos de recusa independentes.
**O que não está claro:** uma despesa pode violar mais de uma regra
simultaneamente — por exemplo, uma despesa de abril, de R$ 150,00, sem nota
fiscal, viola competência e documentação. A spec não declarava se a saída
reporta um único motivo ou todos.
**Decisão:** a saída de cada despesa traz a lista de todos os motivos
aplicáveis, e não um motivo único. Todas as regras de elegibilidade são
avaliadas e seus motivos coletados; o reembolso é zero se qualquer motivo de
recusa estiver presente. A lista é ordenada de forma determinística, na
ordem declarada na §8, de modo que a mesma entrada produza sempre a mesma
saída.
**Justificativa:** as causas de recusa são independentes entre si, e eleger
uma delas exigiria uma ordem de precedência arbitrária que esconderia as
demais. Reportar todas permite ao colaborador corrigir tudo de uma vez, em
lugar de descobrir um problema por submissão. A decisão também simplifica a
§8: a ordem das regras passa a tratar apenas do que afeta o valor
reembolsado, não do que afeta o rótulo.
**Custo aceito:** a representação da saída fica mais complexa, com vários
motivos no lugar de um só, e é necessário declarar a ordenação da lista
para que o resultado seja determinístico.
**Consequências verificáveis:**
- uma despesa de 150,00, sem nota fiscal, com data fora do período →
  reembolso 0,00, recusado, motivos [FORA_DA_COMPETENCIA, SEM_NOTA_FISCAL]
- d-008 → reembolso 0,00, recusado, motivos [FORA_DA_COMPETENCIA]
- d-004 → reembolso 0,00, recusado, motivos [SEM_NOTA_FISCAL]
- uma despesa sem nenhuma violação → lista de motivos vazia

**Regra afetada:** RN-004, RN-006, RN-007; §8

### AMB-019 — Identificador de despesa repetido no lote

**Texto original do RH:** não há. A política não trata da identificação das
despesas; a dúvida surgiu ao declarar o contrato de entrada (§4.1).
**O que não está claro:** o que acontece quando duas ou mais despesas do lote
têm o mesmo identificador. As leituras consideradas foram: (a) erro de lote;
(b) erro de item nas despesas que compartilham o identificador, processando
as demais.
**Decisão:** adotada a leitura (a). Identificador de despesa repetido
constitui erro de lote: o lote é rejeitado e nenhuma despesa é avaliada.
**Justificativa:** o problema não está numa despesa específica, mas na
integridade do lote: com identificadores repetidos não há como relacionar
cada resultado da saída à despesa que o originou, e o desempate da RN-009,
que mantém a despesa de menor identificador, perde o critério. Tratar como
erro de item nas duas despesas deixaria o restante do lote processado
produzindo uma saída ambígua.
**Custo aceito:** um identificador repetido por engano bloqueia o lote inteiro,
inclusive as despesas corretas, que só serão avaliadas quando o lote for
reapresentado com identificadores únicos.
**Consequências verificáveis:**
- um lote com duas despesas de identificador d-006 → lote rejeitado, causa
  identificador repetido; nenhuma despesa avaliada, nem as de identificador
  único
- o lote de exemplo, com d-001 a d-014 distintos, não é afetado por esta
  decisão
- um lote com identificador repetido e data de início posterior à de fim →
  lote rejeitado, com as duas causas informadas

**Regra afetada:** RN-012; RN-010 (nova causa de erro de lote)

### AMB-020 — Abrangência de um defeito de estrutura: lote ou despesa

**Texto original do RH:** não há. A dúvida surgiu ao projetar a
implementação do contrato de entrada (§4.1), depois do fechamento da spec.
**O que não está claro:** três situações que a RN-010 não classificava. (i)
Uma despesa sem identificador, ou com identificador de tipo incompatível: o
identificador é obrigatório, mas a §4.2 exige que ele apareça sempre na
saída, e a RN-012 não dizia se duas despesas sem identificador constituem
repetição. (ii) O período ou a relação de despesas informados com tipo
incompatível, situação distinta de "não informados". (iii) Um elemento da
relação de despesas que não é uma despesa. Para cada uma, as leituras
consideradas foram: (a) erro de lote; (b) erro de item.
**Decisão:** o critério é a abrangência do dano.
- Se o período ou a relação de despesas tem tipo incompatível, a estrutura do
  lote está quebrada e nenhuma despesa pode ser localizada: erro de lote.
- Se o defeito está numa despesa — inclusive identificador ausente ou de tipo
  incompatível, e elemento da relação que não é uma despesa —, apenas aquela
  despesa é inavaliável: erro de item. O elemento que não é uma despesa tem
  uma única causa de erro: a própria despesa, com tipo incompatível.
- Uma despesa com erro cujo identificador falta ou tem tipo incompatível é
  identificada na saída pela sua posição na relação de despesas da entrada,
  contada a partir de um.
- O identificador apresentado na saída é o normalizado — após a remoção de
  espaços nas extremidades (RN-012) —, o mesmo valor que a comparação usa.
- Duas ou mais despesas sem identificador, ou com identificador de tipo
  incompatível, não constituem repetição: a ausência não é um valor, e a
  RN-012 compara identificadores informados.

**Justificativa:** uma despesa sem identificador não deve derrubar o lote
inteiro, do mesmo modo que uma despesa sem valor não derruba (AMB-017): o
defeito é dela, e as demais continuam avaliáveis e relacionáveis aos seus
resultados. Um período ou uma relação de despesas de tipo incompatível, ao
contrário, impede localizar qualquer despesa ou julgar a pertinência de
qualquer uma, e avaliar o restante seria avaliar sem estrutura. A posição é a
única referência que a própria entrada oferece para uma despesa sem
identificador. Apresentar o identificador normalizado garante que a saída
mostre o valor sobre o qual a RN-009 e a RN-012 operaram. Tratar ausências
como repetição faria duas despesas defeituosas rejeitarem o lote inteiro pela
RN-012, contrariando a abrangência adotada.
**Custo aceito:** uma despesa sem identificador é referida na saída pela
posição, que muda se a entrada for reordenada; a independência de ordem
garantida pela RN-001 e pela RN-009 é dos resultados, não dessa referência
(§4.2). Um período de tipo incompatível rejeita o lote inteiro, inclusive as
despesas bem formadas.
**Consequências verificáveis:**
- uma despesa sem identificador, na terceira posição → erro de item,
  identificada pela posição 3; as demais despesas são processadas
- duas despesas sem identificador → ambas erro de item; o lote não é
  rejeitado
- um elemento da relação de despesas que não é uma despesa → erro de item na
  sua posição; causa: a própria despesa, com tipo incompatível
- a relação de despesas ou o período com tipo incompatível → lote rejeitado
- identificadores " d-006" e "d-006" → repetidos; lote rejeitado (RN-012)
- identificador " d-001 " → apresentado como "d-001"

**Regra afetada:** RN-010, RN-012; §4.1, §4.2

### AMB-021 — Causas múltiplas de erro de item

**Texto original do RH:** não há. A dúvida surgiu ao projetar a
implementação do resultado de erro (§4.2), depois do fechamento da spec.
**O que não está claro:** a §4.2 falava em "a causa do erro", no singular,
mas uma despesa pode ter mais de um defeito ao mesmo tempo — fornecedor
ausente e valor não positivo, por exemplo. As leituras consideradas foram:
(a) reportar uma única causa; (b) reportar todas. A AMB-018 decidiu essa
questão para os motivos de recusa, mas erro não é recusa (RN-007), e a
decisão não o alcançava.
**Decisão:** adotada a leitura (b). A saída de uma despesa com erro traz
todas as causas aplicáveis, ordenadas pela ordem em que as informações da
despesa são declaradas na §4.1, de modo que a mesma entrada produza sempre a
mesma saída.
**Justificativa:** a mesma da AMB-018: as causas são independentes, e reportar
uma só esconderia as demais, obrigando o colaborador a descobrir um problema
por submissão. A ordem da tabela da §4.1 é a única ordem dos campos já
declarada na spec, e adotá-la evita criar uma segunda.
**Custo aceito:** como na AMB-018, a saída de erro passa a conter várias
causas no lugar de uma, e a ordenação precisa ser declarada.
**Consequências verificáveis:**
- despesa sem fornecedor e com valor −5,00 → erro de item; causas: fornecedor
  ausente e valor não positivo, nessa ordem
- despesa sem identificador e com data 2026-02-30 → erro de item,
  identificada pela posição; causas: identificador ausente e data
  inexistente, nessa ordem
- d-009 → erro de item; causas: valor não positivo

**Regra afetada:** RN-010; §4.2

---

## 7. Casos de borda

> **Limites nominais, sem ampliação por viagem:** o sistema não determina se o
> colaborador está em viagem (AMB-007). Todas as despesas são avaliadas com os
> limites nominais da política — alimentação R$ 60,00, transporte urbano
> R$ 80,00, hospedagem R$ 250,00 por lançamento (uma diária) —, e os
> critérios de aceite e os casos de borda deste documento valem para qualquer
> despesa, sem distinção de viagem.

| Caso | Entrada | Comportamento esperado | Regra |
|---|---|---|---|
| Duas despesas de alimentação no mesmo dia; esta acima do limite | d-001: 03/07, alimentação, 72,50 | Reembolso 60,00 (parcial); excedente de 12,50 não reembolsado | RN-001, RN-002 |
| Duas despesas de alimentação no mesmo dia; esta abaixo do limite, a outra acima | d-002: 03/07, alimentação, 38,00 | Reembolso 38,00 (integral); d-001 no mesmo dia não reduz o valor | RN-001 |
| Despesa ligeiramente acima do limite | alimentação, 61,00 | Reembolso 60,00 (parcial); excedente de 1,00 não reembolsado | RN-002 |
| Despesa de valor exatamente igual ao limite | alimentação, 60,00 | Reembolso 60,00 (integral) | RN-002 |
| Despesa de exatamente R$ 100,00 sem nota fiscal | d-003: 06/07, transporte urbano, 100,00, sem NF | Nota fiscal não exigida; reembolso 80,00 (parcial); excedente de 20,00 não reembolsado | RN-002, RN-003 |
| Despesa logo acima de R$ 100,00 com nota fiscal | transporte urbano, 100,01, com NF | Nota fiscal exigida e presente; reembolso 80,00 (parcial); excedente de 20,01 não reembolsado | RN-002, RN-003 |
| Despesa acima de R$ 100,00 cujo teto fica abaixo de R$ 100,00, sem nota fiscal | d-004: 06/07, transporte urbano, 100,01, sem NF | Nota fiscal exigida e ausente: a exigência usa o valor lançado (100,01), não o teto de 80,00. Reembolso 0,00 (recusado), motivos [SEM_NOTA_FISCAL] | RN-003, RN-004 |
| Despesa muito acima do teto, sem nota fiscal | alimentação, 500,00, sem NF | Nota fiscal exigida e ausente, embora o teto da categoria seja 60,00. Reembolso 0,00 (recusado), motivos [SEM_NOTA_FISCAL] | RN-003, RN-004 |
| Hospedagem acima de R$ 100,00 sem nota fiscal | d-013: 22/07, hospedagem, 690,00, sem NF | Nota fiscal exigida e ausente. Reembolso 0,00 (recusado), motivos [SEM_NOTA_FISCAL] | RN-003, RN-004 |
| Hospedagem de várias noites lançada num único lançamento | d-010: 14/07, hospedagem, 480,00, com NF, "Hotel Rio - 2 diarias" | Nota fiscal exigida e presente; limite 250,00 (um lançamento, uma diária; a descrição não é interpretada); reembolso 250,00 (parcial); excedente de 230,00 não reembolsado | RN-001, RN-002, RN-003 |
| Despesa que seria integral com nota, sem nota fiscal exigida | hospedagem, 200,00, sem NF | Reembolso 0,00 (recusado), motivos [SEM_NOTA_FISCAL]; a recusa não depende do limite (com NF seria 200,00, integral) | RN-004 |
| Despesa de outra competência, válida em todos os outros aspectos | d-008: 15/04, alimentação, 41,00, com NF, "Almoco de abril lancado com atraso" | Fora do período (a data da despesa decide; a descrição não é interpretada). Reembolso 0,00 (recusado), motivos [FORA_DA_COMPETENCIA]; não é avaliada contra o limite | RN-005, RN-006 |
| Despesa que viola duas regras de elegibilidade | 150,00, sem NF, data fora do período | Reembolso 0,00 (recusado), motivos [FORA_DA_COMPETENCIA, SEM_NOTA_FISCAL], nessa ordem (§8) | RN-004, RN-006, RN-007 |
| Despesa na data de fim do período, com categoria em maiúsculas | d-014: 31/07, "ALIMENTACAO", 61,00, com NF | Dentro do período (bordas inclusivas); categoria normalizada para alimentacao; reembolso 60,00 (parcial); excedente de 1,00 não reembolsado; lista de motivos vazia | RN-001, RN-002, RN-005, RN-008 |
| Par de despesas idênticas exceto pelo identificador; esta com o menor | d-006: 09/07, alimentação, Bistro Central, 54,90, com NF | Grupo de duplicatas com d-007; mantida por ter o menor identificador; reembolso 54,90 (integral); lista de motivos vazia | RN-009 |
| Par de despesas idênticas exceto pelo identificador; esta com o maior | d-007: 09/07, alimentação, Bistro Central, 54,90, com NF | Grupo de duplicatas com d-006; reembolso 0,00 (recusado), motivos [DUPLICATA]; o resultado não muda se a ordem na entrada for invertida | RN-009 |
| Valor negativo (estorno) | d-009: 11/07, transporte urbano, −45,00, sem NF, "Estorno de corrida cancelada" | Erro de item (valor menor ou igual a zero); classificação erro; reembolso 0,00; não é avaliada por nenhuma regra de elegibilidade ou de valor; não abate nenhuma outra despesa | RN-010 |
| Valor com mais de duas casas decimais | d-011: 15/07, alimentação, 33,333, com NF | Valor normalizado 33,33 (arredondamento, metade para cima) antes de qualquer regra; reembolso 33,33 (integral); a saída apresenta 33,33 | RN-011, RN-001 |
| Categoria fora das três elegíveis | d-005: 07/07, "coworking", 89,00, com NF | Reembolso 0,00 (recusado), motivos [CATEGORIA_INELEGIVEL]; não é erro de entrada | RN-008 |
| Despesa em dia não útil | d-012: 18/07 (sábado), alimentação, 47,20, com NF | Avaliada normalmente; nenhuma regra considera o dia da semana. Reembolso 47,20 (integral) | RN-001, RN-002 |

## 8. Ordem de aplicação das regras

A avaliação tem três etapas:

0. **Normalização e validação.** Primeiro, todo valor monetário é
   normalizado para duas casas decimais (RN-011). Em seguida, a entrada é
   validada (RN-010), com a faixa de valor avaliada sobre o valor já
   normalizado. Um erro de lote rejeita a entrada inteira, e nenhuma despesa
   é processada. Uma despesa com erro de item é classificada como erro, com
   reembolso 0,00, e não segue para as etapas 1 e 2.
1. **Elegibilidade.** Para cada despesa sem erro de item, todas as regras de
   elegibilidade são avaliadas, sem interrupção na primeira que se aplica, e
   cada uma que se aplica acrescenta seu motivo à lista de motivos (RN-007),
   nesta ordem:
   1. Pertinência ao período (RN-005, RN-006) → FORA_DA_COMPETENCIA.
   2. Elegibilidade da categoria (RN-008) → CATEGORIA_INELEGIVEL.
   3. Exigência de nota fiscal (RN-003, RN-004) → SEM_NOTA_FISCAL.
   4. Duplicidade (RN-009) → DUPLICATA, avaliada apenas entre as despesas
      que não receberam motivo nos passos 1.1 a 1.3.
2. **Valor.** Apenas para despesa com lista de motivos vazia: limite da
   categoria (RN-001) e reembolso parcial (RN-002). Uma despesa com qualquer
   motivo de recusa tem reembolso 0,00 e não é avaliada contra o limite.

A normalização monetária precede a validação de faixa porque, na ordem
inversa, um valor de 0,004 seria aceito como positivo e passaria a constar
como despesa de 0,00 classificada como integral (origem: AMB-014). A
validação precede as demais etapas porque uma despesa que não pode ser
avaliada não tem como passar por nenhuma regra (origem: AMB-017). A ordem
entre as etapas 1 e 2 afeta o valor: a elegibilidade precede o limite
(origem: AMB-005), de modo que uma despesa recusada não é avaliada contra o
limite. A duplicidade
vem por último na elegibilidade porque é a única regra que compara despesas
entre si, e só deve considerar as que passaram nas demais (origem: AMB-011);
movê-la para antes mudaria quais despesas formam grupo e, portanto, qual é
mantida. Fora isso, a ordem dentro da etapa de elegibilidade não afeta o
valor — basta um motivo para o reembolso ser zero —; ela define a ordenação
da lista de motivos, para que a mesma entrada produza sempre a mesma saída
(origem: AMB-018).

## 9. Critérios de aceite

O sistema está pronto quando:

**Validação e normalização**

- [ ] O lote é rejeitado — sem resultado por despesa nem consolidado, com
  todas as causas detectáveis informadas — quando o período falta ou tem tipo
  incompatível, quando a data de início ou de fim falta ou é inválida, quando
  a data de início é posterior à de fim, quando as despesas não são
  informadas ou a relação de despesas tem tipo incompatível, ou quando há
  identificador repetido (RN-010, RN-012, AMB-020, §4.2).
- [ ] Uma despesa com informação obrigatória ausente — inclusive o
  identificador —, tipo incompatível, data inexistente ou valor normalizado
  menor ou igual a zero, ou que não é uma despesa, recebe classificação erro,
  reembolso 0,00, todas as causas do erro na ordem da §4.1 — ou, se não é
  uma despesa, a causa única "a própria despesa, com tipo incompatível" — e
  lista de motivos vazia; sem identificador, é identificada pela posição; as
  demais despesas são processadas normalmente (RN-010, AMB-020, AMB-021; d-009).
- [ ] Espaços nas extremidades são todos os caracteres de espaço em branco do
  padrão Unicode, e o identificador apresentado é o normalizado (§4.1,
  RN-012).
- [ ] Um lote sem despesas é válido, com total 0,00 e zero em todas as
  classificações (RN-010).
- [ ] Todo valor é normalizado para centavos, com a metade arredondada para
  cima, antes de qualquer regra, e é esse valor que a saída apresenta
  (RN-011; d-011).

**Elegibilidade**

- [ ] Uma despesa com data fora do intervalo de início a fim, ambos
  incluídos, é recusada com FORA_DA_COMPETENCIA (RN-005, RN-006; d-008,
  d-014).
- [ ] A categoria é normalizada apenas quanto à caixa e aos espaços nas
  extremidades, e qualquer categoria fora de alimentacao, transporte_urbano
  e hospedagem é recusada com CATEGORIA_INELEGIVEL (RN-008; d-005, d-014).
- [ ] A nota fiscal é exigida quando o valor normalizado excede 100,00; se
  exigida e ausente, a despesa é recusada com SEM_NOTA_FISCAL (RN-003,
  RN-004; d-003, d-004, d-013).
- [ ] Despesas que coincidem em data, categoria e fornecedor normalizados e
  valor normalizado formam grupo; mantém-se a de menor identificador pela
  ordem dos pontos de código Unicode, e as demais recebem DUPLICATA; despesas
  com outro motivo ou com erro não integram grupo (RN-009; d-006, d-007).
- [ ] Todos os motivos de recusa aplicáveis são reportados, na ordem da §8, e
  o reembolso parcial não é motivo (RN-007).

**Valor**

- [ ] Cada despesa é comparada isoladamente com o limite nominal da sua
  categoria — 60,00, 80,00 ou 250,00 por lançamento —, sem soma entre
  despesas e sem ampliação por viagem (RN-001; d-001, d-002, d-010).
- [ ] Uma despesa acima do limite recebe o limite e é classificada como
  parcial, com o excedente informado; uma despesa igual ao limite ou abaixo
  dele é integral (RN-002).
- [ ] Uma despesa recusada ou com erro não é avaliada contra o limite (§8).

**Saída**

- [ ] Cada despesa da entrada aparece exatamente uma vez, na ordem da
  entrada, com as informações da §4.2 aplicáveis à sua classificação.
- [ ] O consolidado traz o período, o total reembolsado — igual à soma dos
  reembolsos — e a contagem por classificação, cuja soma é igual ao número
  de despesas (§4.2).
- [ ] A mesma entrada produz sempre a mesma saída, e inverter a ordem das
  despesas não altera o resultado de nenhuma delas (RN-001, RN-009,
  AMB-018).
- [ ] As informações sem regra associada não alteram nenhum resultado e não
  aparecem na saída (§3, AMB-015).

**Conformidade**

- [ ] Todos os aceites das RN-001 a RN-012 e todos os casos de borda da §7
  são satisfeitos.
- [ ] O lote de exemplo produz exatamente estes resultados:

| d-001 | d-002 | d-003 | d-004 | d-005 | d-006 | d-007 |
|---|---|---|---|---|---|---|
| 60,00 parcial | 38,00 integral | 80,00 parcial | 0,00 recusada [SEM_NOTA_FISCAL] | 0,00 recusada [CATEGORIA_INELEGIVEL] | 54,90 integral | 0,00 recusada [DUPLICATA] |

| d-008 | d-009 | d-010 | d-011 | d-012 | d-013 | d-014 |
|---|---|---|---|---|---|---|
| 0,00 recusada [FORA_DA_COMPETENCIA] | 0,00 erro | 250,00 parcial | 33,33 integral | 47,20 integral | 0,00 recusada [SEM_NOTA_FISCAL] | 60,00 parcial |

  Consolidado: total reembolsado 623,43; 4 integrais, 4 parciais, 5
  recusadas, 1 erro; 14 despesas.

## 10. O que fica em aberto

- **Ausência de teto diário agregado (origem: AMB-001).** Com avaliação por
  despesa, a política não limita o gasto diário agregado. Se o RH pretendia
  esse controle, seria necessário um teto diário adicional que a política
  atual não estabelece.
- **Disponibilidade de nota fiscal por tipo de fornecedor (origem: AMB-006).**
  A política trata a nota fiscal como universalmente disponível. Na prática,
  fornecedores como hospedagem por aplicativo (d-013, Airbnb) e transporte
  informal podem não emitir nota. A spec não diferencia fornecedores porque a
  política não prevê essa distinção; acomodá-la exigiria regra específica
  sobre formas alternativas de comprovação, que caberia ao RH definir.
- **Ampliação de limites em viagem (origem: AMB-007).** O item 6 da política
  não é implementado porque a entrada não registra o estado de viagem. Para
  implementá-lo seria necessário um campo explícito na entrada, informado
  pelo processo que autoriza a viagem — não pelo colaborador que lança a
  despesa. Enquanto isso não existir, colaboradores em viagem recebem menos
  do que a política lhes garante, e a regularização cabe ao financeiro.
- **Dia útil e dia não útil (origem: §7, d-012).** A política não distingue
  despesas em dia útil de despesas em fim de semana ou feriado, e esta spec
  também não o faz: o dia da semana não é considerado por nenhuma regra. Se o
  RH pretende tratamento diferente para dias não úteis — exigir justificativa
  para plantão, por exemplo —, a política precisaria estabelecê-lo.
