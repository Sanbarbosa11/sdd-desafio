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

18/10 — AMB-005: ele sugeriu manter as referências desatualizadas como
"registro histórico". Recusei — a spec descreve o estado atual do sistema,
e registro histórico é função do git log. Manter duas afirmações divergentes
sobre d-003 tornaria a spec não verificável.

18/10 — No texto do D-001 eu escrevi que mudaram as "Consequências
verificáveis" das quatro ambiguidades, mas um dos ajustes estava no "Custo
aceito" da AMB-006. Ele apontou a imprecisão e sugeriu "os registros das
AMB-001, 002, 004 e 006". Aceitei. Erro meu, pego por ele.

## Delegação
18/10 — AMB-002: o agente apontou por conta própria duas inconsistências
que eu não tinha visto (o "Depende de" da RN-001 ficaria falso depois da
decisão, e o d-001 passou a depender de duas regras). Também identificou
que a linha de d-014 na §7 carregava premissa pendente — mesmo problema que
eu tinha corrigido na AMB-001, agora pego por ele. O fiscal do CLAUDE.md
começou a funcionar sozinho. Optei por trocar a entrada por um caso genérico
de 61,00 em vez de remover a linha, pra não perder o caso de estouro mínimo.

18/10 — Reflexão sobre a ordem: percebi que subagente/MCP/hooks são
multiplicadores de execução, não de direção. Usá-los antes da spec seria
chegar mais rápido ao lugar errado. Os dois recursos que usei nesta fase
foram o CLAUDE.md como fiscal (devolve as decisões pra mim em vez de decidir)
e delegação em modo aumentação (ele levanta leituras e calcula números, eu
decido e justifico). Planejo um subagente revisor de spec depois que ela
fechar, com contexto limpo, pra procurar o que eu não vi.

18/10 — Sessão nova com /clear: ele trabalhou a AMB-005 inteira lendo só a
spec e o CLAUDE.md, usando corretamente as decisões anteriores (AMB-004,
AMB-006, premissa global de viagem). Não precisei re-explicar nenhuma regra.
Primeiro teste real de auto-suficiência da spec — passou.

18/10 — AMB-005: pedi uma varredura por referências a ambiguidades já
decididas que ainda apareciam como pendentes. Eu tinha identificado duas
(AMB-002 e AMB-004); ele achou mais três do mesmo tipo (AMB-001 e duas na
AMB-006) e dois pontos menores que eu não teria visto — resultado incompleto
em consequências e uso da numeração da lista de trabalho em vez do ID da
spec. Delegar a varredura foi melhor que fazer à mão: ele lê a spec inteira
sem cansar.

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
