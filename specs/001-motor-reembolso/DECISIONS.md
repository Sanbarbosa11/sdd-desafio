# Log de Decisões e Mudanças de Spec

> Uma entrada **toda vez** que a spec mudar. Este arquivo é a prova de que a spec
> foi tratada como artefato vivo e não como cerimônia de abertura.
>
> Spec que não muda em dois dias é spec que ninguém consultou. Mudança não é
> demérito — mudança não registrada é.

Ordem cronológica inversa: a mais recente primeiro.

---

## D-002 — Premissa de viagem tornada permanente após a AMB-007 · `2026-10-18`

**Gatilho:** a decisão da AMB-007 (leitura a: caracterização de viagem fora
de escopo) tornou obsoletos registros já salvos que tratavam a viagem como
pendência: a premissa global das seções 5 e 7 e os campos "Depende de" das
RN-001, RN-002 e RN-003.

**O que mudou na spec:**
- §5 e §7, premissa global: de "assumem despesas fora de viagem… quando
  ela for decidida, os casos afetados serão revistos" para regra permanente
  de limites nominais, sem ampliação por viagem e sem previsão de revisão.
- RN-001, RN-002 e RN-003, "Depende de": a AMB-007 foi removida.
- §3: novo item — o sistema não determina se o colaborador está em viagem e
  não aplica a ampliação de 50% do item 6. Os marcadores de modelo da seção
  foram substituídos por esse item.
- §10: o item "Hospedagem recusada como indício de viagem" deixou de existir
  e foi substituído por "Ampliação de limites em viagem".
- RN-001, Regra: removida a frase "A eventual ampliação de limite por viagem
  é tratada na AMB-007 e opera sobre o valor do limite, não sobre a soma das
  despesas." Origem: de "decisão AMB-001" para "decisões AMB-001 e AMB-007".
- RN-002, Regra: de "O limite aplicável é o da categoria conforme a RN-001,
  com a eventual ampliação por viagem da AMB-007" para "O limite aplicável é
  o limite nominal da categoria conforme a RN-001; não há ampliação por
  viagem (AMB-007)". Origem: de "decisão AMB-002" para "decisões AMB-002 e
  AMB-007".
- AMB-005, Justificativa: de "esvaziando a regra em alimentação e em
  transporte fora de viagem" para "esvaziando a regra em alimentação e em
  transporte urbano".
- AMB-006, Justificativa: de "o limite de alimentação é no máximo R$ 90 e o
  de transporte fora de viagem é R$ 80… Produziria ainda o efeito invertido
  de exigir mais documentação de quem viaja" para "os limites são sempre os
  nominais (AMB-007) — alimentação R$ 60 e transporte urbano R$ 80, ambos
  abaixo de R$ 100… O item 5 da política passaria a valer apenas para
  hospedagem", sem o argumento do efeito invertido.

**Por quê:** a spec descreve o estado atual do sistema. Uma premissa
declarada como provisória, dependências apontando para uma ambiguidade já
decidida e regras que preveem ampliação por viagem fariam o leitor — ou quem
implementa a partir das RN-001 e RN-002 — supor que os limites ainda podem
mudar por viagem.

**O que isso invalidou:** nenhuma regra ou critério de aceite — todos já
assumiam despesas fora de viagem e permanecem válidos. A pendência da §10
sobre hospedagem recusada como indício de viagem deixou de existir. Na
justificativa da AMB-006, o argumento do efeito invertido sobre quem viaja
deixou de se aplicar e foi removido; a decisão da AMB-006 se sustenta pela
razão restante, reforçada.

**Tasks afetadas:** nenhuma — tasks.md ainda não foi escrito.

**Custo:** 1 arquivo, correção pontual.

---

## D-001 — Registros das AMB-001, 002, 004 e 006 atualizados após a AMB-005 · `2026-10-18`

**Gatilho:** a decisão da AMB-005 tornou obsoletas afirmações registradas nas
AMB-002 e AMB-004, que referenciavam a AMB-005 como pendente. A varredura que
se seguiu encontrou o mesmo problema nas AMB-001 e AMB-006.

**O que mudou na spec:** os registros das AMB-001, 002, 004 e 006 passam a
declarar o resultado final em vez de apontar para
ambiguidades já decididas, e a AMB-001 passa a usar a numeração AMB-003 em
vez da numeração da lista de trabalho.
- AMB-001, Consequências: de "(assumindo AMB-002 = corta o excedente)" para
  "(com o reembolso parcial definido na AMB-002: o excedente é cortado)";
  de "a ambiguidade #3" para "a AMB-003".
- AMB-002, d-003: de "80,00, parcial (condicionado à AMB-004/005)" para
  "80,00, parcial; a nota fiscal não é exigida (RN-003)".
- AMB-004, d-004 e d-013: de "resultado definido pela AMB-005" para
  "reembolso 0,00, recusado, motivo SEM_NOTA_FISCAL (RN-004)".
- AMB-006, Custo aceito: de "a severidade dessa diferença depende da
  AMB-005: se… se…" para "foi fixada pela AMB-005: a ausência de nota recusa
  o item integralmente, de modo que d-004 perde R$ 80,00 por conta do
  centavo".
- AMB-006, d-004: de "efeito definido pela AMB-005" para "reembolso 0,00,
  recusado, motivo SEM_NOTA_FISCAL (RN-004)"; d-013 e alimentação de 500,00
  passam a declarar o mesmo resultado.

**Por quê:** a spec descreve o estado atual do sistema. Afirmações
divergentes sobre o mesmo item tornam a spec não verificável.

**O que isso invalidou:** nenhuma regra ou critério de aceite. Apenas texto de
registro que havia se tornado desatualizado.

**Tasks afetadas:** nenhuma — tasks.md ainda não foi escrito.

**Custo:** 1 arquivo, correção pontual.
