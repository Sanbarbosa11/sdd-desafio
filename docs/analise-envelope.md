# Análise do envelope lacrado (Dia 2)

> Feita em 2026-10-19, depois de commitar os arquivos do envelope (`2aed7d1`) e
> **antes** de qualquer mudança na spec. As ambiguidades listadas aqui estão
> abertas de propósito: as decisões estão na spec 2.0, AMB-022 a AMB-038, e o
> evento inteiro está registrado em DECISIONS.md, D-012.


Arquivos analisados: exemplos/envelope/ (commit 2aed7d1): 00-ENVELOPE-LACRADO.md,
politica-v4.json, cambio.json, despesas-envelope.json, despesas-envelope-cc-desconhecido.json.

## Achado principal

O lote do Dia 1 (exemplos/despesas-exemplo.json) é do colaborador c-0417, centro de custo
CC-ENG-PLATAFORMA. A v4 é "retroativa à competência atual" (07/2026, a mesma do lote) e a
tabela desse CC tem alimentação 75, transporte 80, hospedagem 0,00 "não reembolsável".
Consequência: a §9 da spec (total 623,43; 4 integrais, 4 parciais, 5 recusadas, 1 erro) deixa
de valer. d-001 72,50 → integral 72,50 (era 60,00 parcial); d-014 61,00 → integral 61,00
(era 60,00 parcial); d-010 hospedagem 480,00 → 0,00 (classificação em aberto); d-013 pode
ganhar CATEGORIA_INELEGIVEL além de SEM_NOTA_FISCAL. Total: 386,93.

## 1. O que quebra na spec

Seções: §3 (centro de custo "sem efeito" cai; viagem a rever); §4.1 (centro de custo deixa de
ser sem efeito; falta campo moeda; política e câmbio viram entradas; a regra "informações não
previstas são aceitas e ignoradas" faria o motor pagar e-002, 22,00 EUR, como R$ 22,00 em
silêncio); §4.2 (valor considerado passa a ser convertido; auditor precisa de moeda, valor
original e taxa); §7 (linhas com d-001, d-014, d-010, d-013 mudam); §8 etapa 0 (onde entra a
conversão); §9 (total e contagens caem); §10 (viagem).

RN: RN-001 (limites fixos 60/80/250 → tabela externa por CC; aceites de d-001 e d-010 caem);
RN-002 (origem do limite; aceites genéricos só sob política padrão); RN-003 (limiar 100 vem do
arquivo; moeda de comparação em aberto); RN-008 ("exatamente três categorias" cai;
representacao no CC-COMERCIAL; aceite de d-014 cai); RN-009 (valor normalizado em BRL ou na
moeda original); RN-010 (validação da moeda, obrigatoriedade do CC, política/câmbio
ilegíveis); RN-011 (ordem normalizar/converter). RN-004, 005, 006, 007, 012: texto continua
válido; RN-007 e §8 só mudam se surgir motivo novo.

AMB: AMB-013 invalidada no "exatamente três"; AMB-015 invalidada para o centro de custo e
inadequada para moeda; AMB-007 a rever (comunicado liga viagem a moeda estrangeira; arquivo
traz acrescimo_em_viagem_percentual 50); AMB-001 reaberta em parte (periodicidade "dia" vs
"diaria"); AMB-006 e AMB-014 ganham a dimensão de moeda; AMB-009 mantém a decisão mas perde as
consequências sobre d-010 (e-007 "3 noites" a exercita).

## 2. Ambiguidades novas (nenhuma decidida)

"Aplica-se a política padrão":
- P1 o que é a padrão: bloco `padrao` do arquivo ou v3 no código (hoje números iguais).
- P2 vale só para CC ausente ou também para categoria ausente em CC listado (CC-ADM sem
  hospedagem: 200,00 com NF → 200,00 integral vs CATEGORIA_INELEGIVEL).
- P3 CC ausente/nulo/tipo incompatível: padrão vs erro de lote.
- P4 normalização do código do CC ("cc-comercial" cairia na padrão em silêncio).
- P5 representacao sob a padrão: f-003 (190,00) 0,00 inelegível vs 190,00.
- P6 limite 0,00 "não reembolsável": recusa CATEGORIA_INELEGIVEL / motivo novo / parcial com
  0,00 (afeta d-010 e d-013 do Dia 1).

"Taxa da data da despesa":
- T1 data sem cotação: e-004 (30,00 EUR, sábado 18/07): dia útil anterior 178,80 → 90,00
  parcial exc. 88,80; próximo 180,30 → 90,00 parcial exc. 90,30; erro ou recusa 0,00.
- T2 data fora da faixa da tabela (13/07–28/07, período 01/07–31/07): hipotético.
- T3 moeda sem cotação — e-006 (55,00 GBP): NÃO é caso de "taxa da data"; em 21/07 há cotação,
  só não de GBP, em data nenhuma. Leituras: erro de item com causa nova; recusa com motivo
  novo; erro de lote (rejeita o lote todo do Rafael).
- T4 ordem normalizar/converter: casos do envelope dão centavos exatos; 33,333 EUR divergiria.
- T6 moeda do limiar de NF: e-005 (40,00 USD sem NF = 220,00 BRL): em BRL → 0,00 recusada
  SEM_NOTA_FISCAL; na moeda original → 150,00 parcial. Maior impacto.
- T7 chave de duplicidade em BRL ou na moeda original.
- T8 moeda nula/vazia/minúscula/inválida: tratar como ausente (BRL) vs erro de item.

Não apontadas pelo envelope: V1 moeda estrangeira caracteriza viagem? (e-002 90,00 vs 130,46;
e-004 90,00 vs 135,00; e-007 400,00 vs 600,00, que entraria no item C); A1 periodicidade "dia"
reabre AMB-001 (d-001+d-002 no Dia 1: 110,50 vs 75,00); R1 vigência retroativa e lotes de
competência anterior; F1 política/câmbio ilegíveis (o plan §4 rejeitou arquivo de configuração
justamente por esses modos de falha); C1 item C (limite estrito, pendência no total, estado).

Leitura de referência (só para comparar): Rafael (CC-COMERCIAL) total 1.143,26; Dani
(CC-SUPORTE-N2, padrão) total 373,76.

## 3. Arquitetura: absorve vs resiste

Absorve: ordem da §8 no Motor, coleta de motivos, duplicidade em duas passadas, aparar(),
validação de lote e de item, OrdemMotivosSecao8Test (motivo novo = enum + linha na §8); ponto
único limiteAplicavel (plan §4 previa "uma ampliação condicional altera essa função e mais
nada"); fronteira do domínio DT-009 (política e câmbio entram por adaptadores); Valor/RN-011
(conversão = multiplicação + normalizar); categoria como String nos resultados; saída, entrada
e CLI ainda não implementadas (T-017 a T-019).

Resiste: (1) Politica.kt — enum Categoria com limites fixos e três valores não representa
categorias por CC vindas de arquivo; contradiz o plan §4 ("constante, não arquivo de
configuração"); (2) a CLI é fixa pelo DESAFIO (calcular --input --output) e não diz de onde vêm
politica-v4.json e cambio.json; (3) AMB-015 "por construção" — o adaptador não lia o CC; os
testes previstos da T-017 para AMB-015 se invertem; (4) regra "campo não previsto é ignorado"
é o oposto do que moeda precisa; (5) testes das RN-001/002/003/008 sem CC sobrevivem só na
leitura P3(a); (6) §9 e T-020 caem antes de serem implementados.
