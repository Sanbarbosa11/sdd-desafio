# Notas de processo
> Rascunho. Alimenta docs/RELATORIO.md. Anotar NA HORA.

## Diligência
18/10 — AMB-001: li o diff inteiro antes de aprovar. Peguei duas coisas:
(1) a RN-001 afirmava que nenhuma outra despesa altera o limite aplicável,
o que contradiz o item 6 da política (viagem amplia limites) e fecharia a
porta pra AMB-007; (2) a premissa "assumindo AMB-002" estava dentro do
campo Aceite, tornando o critério não verificável hoje. Pedi correção antes
de salvar. Ele aplicou o critério (2) a um terceiro lugar que eu não tinha
apontado (coluna Regra da §7) — aceitei.

18/10 — AMB-002: li o diff inteiro. Aceitei as duas alterações que ele fez
sem eu pedir, depois de verificar que estavam certas.

## Delegação
18/10 — AMB-002: o agente apontou por conta própria duas inconsistências
que eu não tinha visto (o "Depende de" da RN-001 ficaria falso depois da
decisão, e o d-001 passou a depender de duas regras). Também identificou
que a linha de d-014 na §7 carregava premissa pendente — mesmo problema que
eu tinha corrigido na AMB-001, agora pego por ele. O fiscal do CLAUDE.md
começou a funcionar sozinho. Optei por trocar a entrada por um caso genérico
de 61,00 em vez de remover a linha, pra não perder o caso de estouro mínimo.

## Erros do agente
18/10 — AMB-004: o agente afirmou que, no arquivo de exemplo, nenhuma
despesa passa de R$ 100 antes do corte e fica abaixo depois — logo a AMB-006
(NF pelo valor original ou pelo reembolsável) não teria caso concreto. Usei
essa afirmação na minha resposta. Na rodada seguinte ele voltou atrás
sozinho: d-004 é exatamente esse caso (100,01 originais, 80,00 reembolsáveis),
e d-013 também se a AMB-009 contar 1 diária. A AMB-006 decide d-004 entre
0,00 e 80,00. Não detectei por conta própria — aceitei a afirmação dele sem
cruzar com os números do JSON.

## Li o diff?
18/10 — AMB-004: li o diff, mas aceitei sem verificar uma afirmação factual
sobre o conteúdo do despesas-exemplo.json. Custou um raciocínio errado que
só não virou decisão errada porque ele corrigiu. Lição: conferir afirmação
sobre dado contra o dado, não contra a memória.

## Paradas por bug de spec
<!-- data | regra que eu ia explicar no chat | AMB/RN criada | commit -->

## Perguntas que eu faria ao RH
<!-- pergunta | decisão que tomei sem resposta -->
