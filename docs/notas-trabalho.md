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

18/10 — AMB-007: pedi a varredura de novo, agora por referências ao estado
de viagem. Achou quatro. Duas delas (F1 e F2) estavam no texto das próprias
RN-001 e RN-002 afirmando que o limite pode ser ampliado por viagem — ou
seja, as regras contradiziam a decisão que acabara de ser tomada. As duas
anteriores (AMB-005) eram em campos de registro; estas eram no enunciado
das regras, que é o que alguém usaria para implementar. A varredura
sistemática depois de cada decisão virou parte do meu processo.

18/10 — AMB-009: a varredura achou cinco itens, e o quinto (H5) não era
uma inconsistência — era uma consequência da decisão que eu não tinha
previsto. Lançar cada diária como despesa própria torna duas noites no mesmo
hotel indistinguíveis de uma duplicata, que é exatamente o par d-006/d-007
do exemplo. Nenhuma varredura por texto acharia isso; ele inferiu a partir
do efeito da decisão. Registrei na §10 como dependência para a AMB-011.

19/10 — Antes de implementar as regras de negócio, lancei um subagente
general-purpose para revisar a spec com contexto limpo, sem ter participado
de nenhuma decisão. Achou 14 itens em quatro categorias. Quatro mudavam
resultado: a AMB-013 listava as categorias elegíveis com acento, justamente
a grafia que ela torna inelegível; a decisão da AMB-020 sobre causa única
não chegou a nenhuma regra; a frase "não participa de nenhuma comparação"
conflitava com a RN-012; e "códigos dos caracteres" na RN-009 não definia se
eram code points ou unidades UTF-16 — quem decidiu isso foi o plan.md, que
é regra de negócio fora da spec. Este último o próprio subagente classificou
como vazamento técnico de gravidade baixa; a sessão principal discordou e o
reclassificou como indeterminação de gravidade média, o que eu confirmei.
Nenhuma dessas quatro teria sido encontrada pela sessão que escreveu a spec.

## Erros do agente
18/10 — AMB-004: o agente afirmou que, no arquivo de exemplo, nenhuma
despesa passa de R$ 100 antes do corte e fica abaixo depois — logo a AMB-006
(NF pelo valor original ou pelo reembolsável) não teria caso concreto. Usei
essa afirmação na minha resposta. Na rodada seguinte ele voltou atrás
sozinho: d-004 é exatamente esse caso (100,01 originais, 80,00 reembolsáveis),
e d-013 também se a AMB-009 contar 1 diária. A AMB-006 decide d-004 entre
0,00 e 80,00. Não detectei por conta própria — aceitei a afirmação dele sem
cruzar com os números do JSON.

18/10 — AMB-015: a redação que eu passei colocava o fornecedor na lista de
campos "que nenhuma regra utiliza", mas a frase seguinte declarava todos os
campos da lista como opcionais — o que deixaria a RN-009 sem chave de
comparação quando o fornecedor faltasse. Eu tinha levantado a dúvida no
próprio pedido, mas a redação que propus não a resolvia. Ele apontou a
contradição e separou "campo não utilizado" de "campo usado apenas como
chave, sem interpretação do conteúdo". Erro meu, pego por ele.

18/10 — §4/RN-012: pedi que a RN-012 declarasse que "d-006" e "D-006" não
formam duplicata. Estava errado: o identificador não integra o critério de
duplicidade da RN-009 (data, categoria, fornecedor, valor), então os dois
formam duplicata como qualquer par coincidente. Ele recusou a alteração,
explicou a contradição que ela criaria, e a análise expôs uma lacuna real
que nenhum de nós tinha visto: se formam duplicata, qual é mantida? A
RN-009 dizia "ordem lexicográfica" sem definir a ordenação dos caracteres
entre si. Erro meu, pego por ele, com ganho líquido.

19/10 — T-002: pedi três testes citando a RN-011 de memória, mas a task
define quatro. Ele apontou a divergência e perguntou se eu queria ajustar a
task ou manter os quatro, em vez de simplesmente seguir meu pedido. Mantive
os quatro — alterar o documento para caber numa imprecisão minha inverteria
a relação. Erro meu, pego por ele.

## Li o diff?
18/10 — AMB-004: li o diff, mas aceitei sem verificar uma afirmação factual
sobre o conteúdo do despesas-exemplo.json. Custou um raciocínio errado que
só não virou decisão errada porque ele corrigiu. Lição: conferir afirmação
sobre dado contra o dado, não contra a memória.

## Paradas por bug de spec
18/10 — plan.md: ao projetar o modelo de dados, o agente encontrou cinco
pontos que a spec não decidia (identificador ausente, múltiplas causas de
erro, arredondamento de negativos, tipo incompatível em despesas/periodo,
alcance de "espaços"). Nenhum deles era visível ao escrever a spec — só
apareceram quando alguém tentou implementar. Parei o plan, decidi os cinco,
registrei na spec e no DECISIONS (D-009), e só então segui. É a Regra 2
funcionando no sentido que eu não esperava: não foi o chat que vazou regra,
foi a implementação que revelou lacuna.


## Perguntas que eu faria ao RH
<!-- pergunta | decisão que tomei sem resposta -->
