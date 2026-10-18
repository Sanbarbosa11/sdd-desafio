# Log de Decisões e Mudanças de Spec

> Uma entrada **toda vez** que a spec mudar. Este arquivo é a prova de que a spec
> foi tratada como artefato vivo e não como cerimônia de abertura.
>
> Spec que não muda em dois dias é spec que ninguém consultou. Mudança não é
> demérito — mudança não registrada é.

Ordem cronológica inversa: a mais recente primeiro.

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
