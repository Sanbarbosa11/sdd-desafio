# Relatório — Desafio SDD

**Aluno:** `<nome>` · **Repositório:** `<link>` · **Data:** `<data>`

> Isto não é redação. São **evidências**. Toda afirmação deve vir acompanhada de
> arquivo, hash de commit ou trecho de sessão exportada. Um parágrafo bonito sem
> evidência vale menos que uma frase curta com um hash.
>
> Vale 20 dos 100 pontos, e é a seção que mais separa notas.

---

## Delegação

*O que você fez, o que o Claude fez, e por que dividiu assim.*

**A divisão:**

| Atividade | Quem | Por quê |
|---|---|---|
| Identificar ambiguidades | Os dois, com o agente levantando a maior parte | O agente cruzou a política com `exemplos/despesas-exemplo.json` e abriu a primeira lista de ambiguidades (`docs/sessions/01-ambiguidades-001-a-006.md`, início da sessão). Depois de cada decisão ele fazia uma varredura de inconsistências (V1 a V9 em `docs/sessions/05-duplicidade-categoria-validacao.md`, W1 a W4 em `docs/sessions/06-precisao-e-campos-sem-regra.md`). Eu levantei algumas por conta própria, como os dois problemas da RN-001 na AMB-001 (`docs/sessions/01-…`, trecho "Dois ajustes antes de salvar", commit `b875cd2`). Algumas lacunas só apareceram quando alguém tentou implementar: as do plan (D-009, `4940b8c`) e a conversão de caixa (D-011, `d7b07b6`). |
| Decidir as ambiguidades | Eu | Sou eu que vou defender as decisões. Para cada AMB o agente trazia leituras e uma recomendação, e quem escolhia era eu. Exemplos: "V8: ordem lexicográfica, comparando caractere a caractere" (`docs/sessions/05-…`), GAP 1 a 4 do plan (`docs/sessions/08-plan-tecnico.md`, trecho "A proposta está aprovada no conteúdo. Mas os cinco gaps vão para a spec primeiro"; ao serem escritos, viraram as seis lacunas da D-009) e a escolha do mapeamento completo do Unicode na D-011 (sessão de implementação, `d7b07b6`). |
| Escrever a spec | O agente redigia e eu ditava as decisões e revisava | Eu ditava a decisão e, várias vezes, o texto literal: a premissa global da §5/§7 (`docs/sessions/01-…`), a AMB-012 dissolvida (`docs/sessions/05-…`), a W2 da AMB-015 (`docs/sessions/06-…`). O agente montava a proposta numa cópia, me mostrava o diff e só gravava no `spec.md` depois da minha aprovação. Os commits da fase de spec, de `b875cd2` a `4940b8c`, fui eu que fiz, à mão (não têm trailer `Co-Authored-By`). |
| Desenhar a arquitetura | O agente propôs e eu fixei a stack e aprovei | Fixei Kotlin/JVM 17 e os quatro pontos que o plan precisava resolver (`docs/sessions/08-plan-tecnico.md`, primeiro prompt). O agente propôs a arquitetura e a representação da política. Aprovei a proposta "no conteúdo" e escolhi o JDK (opção a, DT-001). Commit `ac2c7a6`. |
| Implementar | O agente | T-001 a T-013 (`3c18414` … `8d6c8b6`), todos com `Co-Authored-By: Claude`. Meu papel foi aprovar fronteiras e parar quando aparecia lacuna de spec. Na T-008 aprovei explicitamente três fronteiras (sessão de implementação, `5af7f0b`). |
| Escrever testes | O agente | Todos os arquivos em `tests/` entraram em commits co-autorados pelo agente. Não escrevi nenhuma linha de teste. Em "Diligência" explico o que fiz para não confiar cegamente neles. |
| Absorver o envelope | O agente analisou, eu decidi, o agente escreveu | O agente fez a análise do que a v4 quebra antes de qualquer mudança na spec (`docs/analise-envelope.md`). Eu decidi as 17 ambiguidades novas, em bloco. O agente escreveu a spec 2.0 (`a04c1d3`), a D-012 (`c89de59`) e as T-021 a T-032 (`46996d2`). Na escrita, ele divergiu do meu texto em três pontos e avisou antes de escrever (D-012, "Como as decisões foram tomadas"). As T-021 a T-032 não serão implementadas nesta entrega. |

**Onde deleguei e me arrependi:**

- **A representação da política como constantes.** No meu próprio pedido do plan escrevi que, segundo o template, essa é "a decisão que mais barato ou mais caro vai custar se a política mudar" (`docs/sessions/08-plan-tecnico.md`, ponto 1). O agente propôs constantes numa `enum Categoria` em `Politica.kt` e descartou o arquivo de configuração. O argumento era rastreabilidade: "um YAML deixaria o limite mudar sem o teste nem a spec mudarem". Aceitei sem contestar (`plan.md` §4, commit `ac2c7a6`). O envelope pede exatamente o contrário: "O motor precisa ler a política de fora, não de dentro do código" (`exemplos/envelope/00-ENVELOPE-LACRADO.md`, item A). A análise do envelope põe isso como a primeira resistência da arquitetura: a enum fixa não representa categorias por centro de custo vindas de arquivo.
- **Aceitar afirmação factual sobre os dados sem conferir.** Na AMB-004, aceitei a afirmação do agente de que nenhuma despesa do exemplo passava de 100 antes do corte e ficava abaixo depois, e usei isso no meu raciocínio. Detalhes em Discernimento, Caso 1.

**Onde não deleguei e deveria ter delegado:**

- **A revisão independente veio tarde.** Declarei a spec completa na D-008 (`f62beae`) e só lancei o subagente revisor depois da fundação (T-001 a T-004, `b6734a8`). Ele achou quatro problemas que mudavam resultado (D-010, `2af517f`). Um deles, o D4, já tinha sido decidido no código pela DT-007 do plan. Se eu tivesse rodado a revisão logo depois da D-008, antes do plan, esses quatro pontos teriam entrado na spec antes de qualquer decisão técnica.
- **Higiene de commit.** Fiz os commits da spec à mão e não deleguei nem automatizei nenhuma verificação. O resultado foi o `docs/notas-trabalho.md` commitado vazio em `ed98aab` (ver Diligência). Não configurei hook nenhum: o repositório não tem pasta `.claude/`.

**Usei subagentes / skills / MCP / hooks?**

- **Subagente: sim, uma vez.** Antes da T-005, um subagente general-purpose revisou a spec 1.1 com contexto limpo, sem acesso a `plan.md`, `tasks.md`, `DECISIONS.md` nem ao código, e só relatou, sem alterar nada (D-010, "Gatilho"). Ele trouxe 14 achados em quatro categorias (A1–A5, B1–B2, C1, D1–D6, listados na D-010). Quatro foram corrigidos na spec 1.2 (`2af517f`): D4 (ponto de código), A1/D5 (vocabulário de categoria), C1/B1 (causa única) e A5 (alcance da RN-006). Os demais ficaram em "Pendente" na D-010. O plan e as tasks passaram a referenciar a 1.2, e a DT-007 deixou de decidir a RN-009 e passou a implementá-la (`818d6a6`). Valeu a pena: nenhum desses quatro tinha sido visto pela sessão que escreveu a spec (`docs/notas-trabalho.md`, nota de 03005ba). A classificação do subagente, porém, não foi aceita como veio. Ele chamou o D4 ("códigos dos caracteres" na RN-009) de vazamento técnico de gravidade baixa. A sessão principal o reclassificou como regra de negócio decidida fora da spec (pela DT-007), de gravidade média, e eu confirmei (D-010, "Classificação"; sessão de implementação, `2af517f`). Ou seja: o revisor achou o problema e errou o peso. O relatório original do subagente, com os 14 achados no texto dele, está no export da sessão 10 (`docs/sessions/10-implementacao-regras-e-envelope.md`, a partir de "Revisão independente: specs/001-motor-reembolso/spec.md (v1.1)"), e é citado pela D-010.
- **CLAUDE.md como fiscal.** Usei as "Regras de trabalho" que vieram do template, sem mudança ("Se eu te explicar uma regra que não está na spec, pare e me diga isso"). Foram elas que fizeram o agente parar no plan (D-009) e antes da T-010 (D-011). Em `c5a61d0` só removi o cabeçalho do template.
- **Skills, MCP, hooks: não.** O repositório não tem `.claude/`. Por quê: anotei em `49672be` que subagente, MCP e hooks "são multiplicadores de execução, não de direção", e que usá-los antes da spec seria "chegar mais rápido ao lugar errado". Também não usei nenhuma skill nem MCP fora do repositório: só o subagente e o CLAUDE.md como fiscal.

---

## Descrição

*Como você transformou requisito ambíguo em requisito verificável.*

Pegue **um** requisito ambíguo da política do RH e mostre a evolução:

Requisito: item 8 da política, "**Duplicatas devem ser tratadas.**" (`DESAFIO.md`). A parte que eu acompanho aqui é o desempate dentro de um grupo de duplicatas: qual despesa é mantida. Esse texto mudou três vezes, e cada mudança veio de uma fonte diferente.

**Versão 1 (minha primeira escrita):**
> ```
> Dentro de um grupo, a despesa de menor identificador é avaliada normalmente
> e as demais são recusadas, com o motivo DUPLICATA na sua lista de motivos
> (RN-007). Os identificadores são comparados em ordem lexicográfica, caractere
> a caractere, sem extração de número; com identificadores de larguras
> diferentes, "d-10" precede "d-9".
> ```
> (RN-009 no commit `441be28`; decisão ditada por mim como "V8: ordem lexicográfica, comparando caractere a caractere", em `docs/sessions/05-duplicidade-categoria-validacao.md`)

Versão intermediária (`9448765`, D-007): "… em ordem lexicográfica, caractere a caractere, **pelos códigos dos caracteres**, sem extração de número e **sem normalização de caixa: letras maiúsculas precedem as minúsculas** …", com o aceite "d-006 e D-006 → mantém-se D-006".

**Versão final:**
> ```
> Os identificadores são comparados em ordem lexicográfica, caractere a
> caractere, pelo ponto de código Unicode de cada caractere, sem extração de
> número e sem normalização de caixa: letras maiúsculas precedem as
> minúsculas, e, com identificadores de larguras diferentes, "d-10" precede
> "d-9". O resultado não depende da ordem das despesas na entrada.
> ```
> Aceite novo: identificadores que diferem só em U+FF71 e U+1F600 → mantém-se o de U+FF71 (RN-009 e AMB-011, `2af517f`, D-010).

**O que estava ambíguo:** três coisas, em camadas.
1. **Qual é o "menor".** Primeiro, se o número dentro do identificador era extraído ("d-9" < "d-10") ou não. Decidido na Versão 1: não é, porque presumiria um formato que a entrada não garante.
2. **Como os caracteres se ordenam entre si.** "Ordem lexicográfica, caractere a caractere" não dizia se a comparação era pelo código do caractere (maiúscula antes de minúscula) ou alfabética ignorando a caixa. Na segunda leitura, "d-006" e "D-006" empatam e não há desempate, e a RN-001 exige resultado independente da ordem de entrada (D-007, "Por quê").
3. **Que "código".** "Códigos dos caracteres" não dizia se eram pontos de código Unicode ou unidades UTF-16. A diferença muda qual despesa é mantida quando o identificador tem um caractere fora do plano multilingue básico. Quem tinha decidido isso era a DT-007 do `plan.md` ("comparado por code point Unicode, com um comparador próprio"), ou seja, regra de negócio fora da spec, o que o CLAUDE.md proíbe (D-010).

**Como percebi:** em nenhuma das três vezes fui eu.
1. O agente, na varredura V8 depois da AMB-011: "A regra não diz como os identificadores são ordenados. Em ordem alfabética, 'd-10' vem antes de 'd-9'" (`docs/sessions/05-…`).
2. Um erro meu que o agente recusou. Pedi que a RN-012 dissesse que "d-006" e "D-006" não formam duplicata. Ele recusou e, ao explicar por que eles formam duplicata, expôs que a RN-009 não dizia qual dos dois é mantido (`docs/sessions/07-fechamento-da-spec.md`, trecho "Essa análise expôs uma lacuna na RN-009"). Detalhes em Discernimento, Caso 2.
3. O subagente revisor (achado D4), cuja gravidade a sessão principal corrigiu (D-010).

**Commit da mudança:** `441be28` (Versão 1) → `9448765` (códigos dos caracteres, D-007) → `2af517f` (ponto de código Unicode, D-010). O teste correspondente é o da T-013 (`8d6c8b6`, "desempate por ponto de código").

---

## Discernimento

*Onde o Claude errou e você pegou.*

> **Sem um caso concreto e verificável, esta seção vale zero.** Não existe projeto
> de dois dias em que o modelo acertou tudo. A ausência do caso não prova que o
> modelo foi perfeito — prova que ninguém estava conferindo.

### Caso 1

**O que ele propôs:** na rodada da AMB-004 (fronteira da nota fiscal), o agente afirmou sobre a AMB-006 (a NF é exigida pelo valor original ou pelo reembolsável?): "Na leitura A isso não muda nada no exemplo, porque nenhum valor passa de 100 antes do corte e fica em 100 ou menos depois" (`docs/sessions/01-ambiguidades-001-a-006.md`, trecho "A AMB-006 pode já ter sido decidida aqui").

**Por que estava errado:** d-004 é exatamente esse caso: 100,01 originais e 80,00 reembolsáveis depois do limite de transporte. d-013 também, se a AMB-009 contasse uma diária (690,00 → 250,00). A AMB-006 decide d-004 entre 0,00 (recusada, SEM_NOTA_FISCAL) e 80,00 (parcial).

**Como eu detectei:** não detectei. Usei a afirmação na minha resposta ("o caso onde isso diverge … não aparece no exemplo mas pode aparecer no conjunto do instrutor", mesmo arquivo, trecho "A AMB-006 continua aberta"). Na rodada seguinte o próprio agente voltou atrás: "Errei numa afirmação da rodada anterior, e você usou essa afirmação no seu raciocínio. […] d-004 é exatamente esse caso". Eu tinha lido o diff, mas não cruzei a afirmação com os números do JSON (`docs/notas-trabalho.md`, seções "Erros do agente" e "Li o diff?").

**O que eu fiz:** registrei a correção e inverti a ordem de trabalho: AMB-006 antes da AMB-005, porque a AMB-006 decide se d-004 sequer chega à AMB-005 (mesmo arquivo, trecho "Vamos inverter a ordem"). A AMB-006 foi decidida pelo valor original, com d-004 como caso de teste natural (commit `0e148af`). Lição anotada: "conferir afirmação sobre dado contra o dado, não contra a memória".

**Onde está a evidência:** `docs/sessions/01-ambiguidades-001-a-006.md`, trechos "nenhum valor passa de 100 antes do corte" e "Errei numa afirmação da rodada anterior"; `docs/notas-trabalho.md`, "Erros do agente", 18/10 — AMB-004; commit `0e148af`.

### Caso 1-A — erro do agente pego por mim, lendo o diff (AMB-001)

**O que ele propôs:** na RN-001, a frase "Nenhuma outra despesa — do mesmo dia, da mesma categoria ou de qualquer outra — altera o limite aplicável nem o resultado da despesa avaliada". Além disso, deixou a premissa "(assumindo AMB-002 = corta o excedente)" dentro do campo Aceite.

**Por que estava errado:** a frase contradizia o item 6 da política ("Colaborador em viagem tem limites ampliados em 50%") e fechava a porta para a AMB-007. A premissa pendente dentro do Aceite tornava o critério não verificável, porque o Aceite é o que o teste verifica.

**Como eu detectei:** lendo o diff inteiro antes de aprovar o salvamento.

**O que eu fiz:** ditei a nova redação e mandei mover a premissa para um campo "Depende de". O agente aplicou o mesmo critério a um terceiro lugar que eu não tinha apontado (a coluna Regra da §7).

**Onde está a evidência:** `docs/sessions/01-ambiguidades-001-a-006.md`, trecho "Dois ajustes antes de salvar"; `docs/notas-trabalho.md`, "Diligência", 18/10 — AMB-001; commit `b875cd2`.

### Caso 1-B — erro do agente pego por mim (AMB-005, "registro histórico")

**O que ele propôs:** manter na AMB-002 e na AMB-004 referências que ainda tratavam a AMB-005 como pendente ("condicionado à AMB-004/005", "resultado definido pela AMB-005"), "por serem registro histórico".

**Por que estava errado:** a spec descreve o estado atual. Manter as referências deixaria duas afirmações divergentes sobre d-003, e a spec deixaria de ser verificável. Histórico é função do git log.

**Como eu detectei:** no resumo de pontos em aberto que o agente apresentou antes de salvar.

**O que eu fiz:** recusei e mandei corrigir. Pedi também a varredura que achou o mesmo problema nas AMB-001 e AMB-006 (D-001).

**Onde está a evidência:** `docs/sessions/02-ambiguidade-005.md`, trechos "Deixei os dois como estão, por serem registro histórico" e "Ponto 2: corrija, não mantenha como histórico"; commit `bf28bc4` (D-001).

### Caso 2 *(opcional)*

**Padrão que eu notei:**

- **Onde ele erra:** em afirmações sobre o conteúdo dos dados feitas de passagem (Caso 1) e em propostas conservadoras de não mexer em texto já salvo (Caso 1-B). O sinal que passou a me deixar em alerta foi qualquer frase do tipo "no exemplo, nenhum…": afirmação universal sobre o JSON, feita sem tabela de números ao lado.
- **O padrão inverso, que foi mais frequente: erro meu pego por ele.** Registro como tal, sem suavizar. Ele recusou ou corrigiu redações que eu ditei seis vezes:
  1. **AMB-015.** Pus o fornecedor na lista de campos "que nenhuma regra utiliza", e a frase seguinte declarava todos esses campos opcionais. Com isso, a RN-009 ficaria sem chave quando o fornecedor faltasse. Eu tinha levantado a dúvida no próprio pedido ("Verifique se a redação acima resolve isso"), mas a redação que propus não a resolvia. Ele tirou o fornecedor da lista e separou "campo não utilizado" de "campo usado apenas como chave" (`docs/sessions/06-precisao-e-campos-sem-regra.md`, trecho "A sua redação para o W2 não resolvia o caso do fornecedor"; `f0a9d09`; nota em `45cf6cc`).
  2. **RN-012.** Pedi que a RN-012 declarasse que "d-006" e "D-006" não formam duplicata. Estava errado: o identificador não integra o critério da RN-009, e a consequência faria a RN-012 contradizer a RN-009. Ele não gravou a consequência ("deixei de fora a consequência que você pediu para a RN-012, porque ela está errada"), e a análise expôs a lacuna da ordenação descrita em Descrição (`docs/sessions/07-fechamento-da-spec.md`; `9448765`, D-007). Respondi "Você está certo e eu errei".
  3. **A5 da revisão independente.** Ditei "não participa de nenhuma regra de elegibilidade nem de valor" para a despesa fora do período. Isso contradizia a RN-007 e o caso da §7 em que a despesa de 150,00 sem NF fora do período recebe [FORA_DA_COMPETENCIA, SEM_NOTA_FISCAL]. Ele recusou e propôs a redação que entrou: só regras de valor e duplicidade deixam de se aplicar (D-010, A5; sessão de implementação, `2af517f`; nota em `8c73e05`). Se eu não tivesse sido corrigido, o erro entraria no código da T-005 e a saída teria um motivo a menos.
  4. **D-011, caso de aceite com o grego.** Ditei duas despesas de **categoria** "ΟΔΟΣ"/"Οδος" formando duplicata. Pela RN-009, categoria inelegível não integra grupo. Ele registrou o caso com o grego no **fornecedor**. Também apontou que "fim de uma palavra" descrevia mal a condição do sigma final, e eu mandei trocar por "precedido de letra e não seguido de letra" (D-011, "Correção no caso de aceite proposto" e "Ajuste de precisão"; sessão de implementação, `d7b07b6`).
  5. **T-002.** Pedi três testes citando a RN-011 de memória. A task define quatro. Ele apontou a divergência em vez de seguir meu pedido, e mantive os quatro (`docs/notas-trabalho.md`, nota em `338e1f6`; `9abc75c`).
  6. **D-001.** Escrevi que tinham mudado as "Consequências verificáveis" das quatro AMB, mas um dos ajustes estava no "Custo aceito" da AMB-006. Ele apontou a imprecisão, e o título da D-001 passou a ser "Registros das AMB-001, 002, 004 e 006" (`docs/notas-trabalho.md`, "Diligência"; `bf28bc4`).

  O meu erro recorrente é ditar redação de regra de cabeça, sem reler as regras vizinhas que ela toca (RN-007, RN-009). O que me salvou nos casos 2, 3 e 4 foi a regra do CLAUDE.md, que faz o agente devolver a decisão em vez de aplicar o que eu disse.

---

## Diligência

*O que você verificou antes de aceitar.*

**Meu procedimento de verificação:**

- **Fase de spec (18/10).** O agente montava a proposta numa cópia e me mostrava o diff. Nada ia para o `spec.md` sem a minha aprovação (por exemplo, "Se aprovar, copio a proposta", `docs/sessions/02-ambiguidade-005.md`). Depois de cada decisão eu pedia uma varredura por referências que tinham ficado obsoletas. Ela passou a ser sistemática a partir da AMB-005 e da AMB-007 (`docs/notas-trabalho.md`, "Delegação"), e cada achado virou uma entrada D-001 a D-005. Na AMB-009, a varredura achou uma consequência não prevista (diárias no mesmo hotel indistinguíveis de duplicata, o par d-006/d-007), registrada na §10 e levada à AMB-011 (D-003, D-005).
- **Fase de implementação (18–19/10).** Rodei os testes a cada task: 112 testes, todos passando, ao final da T-013 (sessão de implementação, `8d6c8b6`; `build/test-results/test/`, 19 arquivos, 0 falhas). A partir da T-004 (`b6734a8`), o agente passou a verificar que os testes falham quando devem: fazia alterações deliberadas no código (inverter enum, trocar ordem, import proibido, bordas exclusivas, comparador errado), conferia quantos testes quebravam e restaurava o código. Fez isso da T-004 à T-013. Não há registro disso no repositório além da sessão de implementação. Aprovei explicitamente as fronteiras provisórias da T-008 (`5af7f0b`): TODO de categoria inelegível, fechado na T-010 (`97504e0`); `notaExigida` calculado antes da T-011; consolidado antes da T-014.
- **Paradas por bug de spec.** Quando a implementação revelou uma lacuna, parei, decidi, registrei na spec e só então segui. Aconteceu no plan, com as seis lacunas da D-009 (`4940b8c`, antes de `ac2c7a6`), e antes da T-010 (D-011, `d7b07b6`, antes de `5af7f0b`). Nessa segunda vez, o agente verificou no JDK que, na localidade turca, "ALIMENTACAO" vira "alımentacao" e d-014 seria recusada.

**Li o diff inteiro em que porcentagem das entregas?** [NÚMERO]%. O que a evidência permite dizer:
- Na fase de spec, tenho nota de ter lido o diff inteiro na AMB-001, AMB-002 e AMB-004 (`docs/notas-trabalho.md`). Para as demais AMB não anotei.
- Na implementação, não tenho nota de leitura de diff de nenhuma task. Os timestamps mostram T-008 a T-013 commitadas entre 00:24 e 00:42 de 19/10, cerca de 950 linhas inseridas em 18 minutos (`git show --shortstat` de `5af7f0b` a `8d6c8b6`). T-012 e T-013 caíram no mesmo minuto. Esses números não sustentam a afirmação de que li cada linha, e eu não a faço. Em T-005 a T-013 li o diff de `src/` e os nomes dos testes; não li todos os corpos de teste linha a linha.

**O que aceitei sem verificar direito, e o que me custou:**

- **A afirmação sobre d-004** (Discernimento, Caso 1). Custou um raciocínio errado, que só não virou decisão errada porque o agente se corrigiu.
- **O `docs/notas-trabalho.md` commitado vazio.** Em `ed98aab` (18/10 13:36, commit meu, sem co-autor), a mensagem é "docs: nota de diligencia da AMB-001", mas o arquivo entrou com 0 bytes: o diff mostra `0 insertions(+), 0 deletions(-)`, e o blob é o vazio (`e69de29`). A nota da AMB-001 só entrou no repositório 3h15 depois, em `7d379cc` (16:51), cujo diff parte de `index e69de29`. Nesse intervalo houve o commit da AMB-002 (`bca83fa`). Não conferi o `--stat` antes de commitar. Custo concreto: durante três horas, o histórico teve um commit que diz conter uma nota e não contém nada, e a "anotação na hora" que as notas pedem ("Anotar NA HORA") não ficou datada no commit que leva o nome dela. A causa: o arquivo não estava salvo no VS Code no momento do commit. Houve um segundo incidente com o mesmo arquivo, do lado do agente: na T-003, o `git add -A` incluiu o `docs/notas-trabalho.md` no índice, e o agente o tirou antes do commit. A nota foi commitada separadamente em `338e1f6` (sessão de implementação, `15ce265`).
- **A ordenação da T-012.** A ordenação por `Motivo.ordinal`, pedida pela task e pela DT-006, enfraqueceu os testes de ordem da T-010 e da T-011: uma coleta fora da ordem da §8 deixou de quebrá-los. O agente avisou antes de eu perguntar. Aceitei manter a ordenação, porque quem governa a saída é a §8. Esses testes passam a verificar a saída e não a coleta, e a ordem continua amarrada pelo `OrdemMotivosSecao8Test`, que lê a spec (`docs/notas-trabalho.md`, nota em `8c73e05`; comentário em `src/reembolso/dominio/Motor.kt`, função `recusar`; `5db55cc`). É um custo aceito conscientemente, não um descuido. Fica registrado porque reduz a cobertura.

- **Os testes que leem a spec não rodavam quando só a spec mudava.** `specs/` não é entrada declarada da tarefa de teste do Gradle. Depois de uma mudança só na spec, o Gradle considerava os testes atualizados, e um `./gradlew build` dizia verde. Descobri isso durante a absorção do envelope: a spec 2.0 acrescentou a moeda à tabela da §4.1, e o `OrdemCamposSecao41Test` só falhou com `--rerun-tasks`. Ou seja, a proteção que eu apresento abaixo como "testes que leem a própria spec" tinha um buraco desde a T-004 (`b6734a8`). Virou a T-022 (D-012, "Achado de processo"; `c89de59`, `46996d2`).

**Testes: quem escreveu, e como você sabe que eles testam a coisa certa?**

Todos foram escritos pelo agente, o mesmo que escreveu o código. É exatamente o risco que o template aponta. O que tenho contra ele:
- **Nomes amarrados à spec.** Cada teste começa pelo ID do requisito e, quando verifica uma ambiguidade, cita a AMB (convenção do `CLAUDE.md`), e há um arquivo por RN em `tests/reembolso/regras/`.
- **Testes que leem a própria spec.** `OrdemMotivosSecao8Test` e `OrdemCamposSecao41Test` leem `specs/001-motor-reembolso/spec.md` através de `tests/reembolso/rastreabilidade/Spec.kt`. Se a ordem da §8 ou da §4.1 mudar na spec, o teste quebra sem que ninguém precise lembrar, desde que rode: até a T-022, um build depois de mudar só a spec não os executa (ver acima).
- **Oráculo independente.** O teste dos 25 code points de `aparar()` usa `\p{IsWhite_Space}` do JDK, e não a lista do próprio código. Na T-003 o agente detectou nos bytes que os escapes tinham sido gravados como caracteres invisíveis e trocou por construção por code point antes do commit (sessão de implementação, `15ce265`).
- **Verificação de que falham.** As mutações deliberadas de T-004 a T-013 (sessão de implementação). Foram feitas pelo mesmo agente, e eu não as repeti.

O que ainda **não** existe, e que seria a verificação independente de verdade:
- T-015: um teste por linha da §7;
- T-016: o `RastreabilidadeTest`, que exige teste para cada RN e cada AMB não dissolvida (`db8b1b6`);
- T-020: o ponta a ponta com o resultado da §9.

Os três estão em aberto em `specs/001-motor-reembolso/tasks.md`. Hoje, a única prova de que os testes testam a regra certa, e não só o código, são os nomes, os dois testes que leem a spec e as mutações feitas pelo próprio agente.

---

## O envelope

*A mudança de requisito do Dia 2.*

**Quantos arquivos toquei na mão:** nenhum. A absorção mexeu em quatro arquivos, todos de documentação, escritos pelo agente a partir das minhas decisões: `spec.md`, `DECISIONS.md`, `tasks.md` e `docs/analise-envelope.md`. Nenhuma linha de código, e nenhum outro arquivo.
**Quanto tempo levou:** 33 minutos entre o commit dos arquivos do envelope (19/10 00:48, `2aed7d1`) e o último commit da absorção (19/10 01:21, `46996d2`). Esse intervalo inclui a análise, as minhas decisões e a escrita.
**Diff de absorção:** 3 commits, 4 arquivos, +1.335/−173 linhas.
- `a04c1d3`: spec 2.0, +875/−155 em `spec.md`, mais `docs/analise-envelope.md` (+94).
- `c89de59`: D-012, +148 em `DECISIONS.md`.
- `46996d2`: T-021 a T-032, +218/−18 em `tasks.md`, ou seja, 236 linhas alteradas.
- Código: 0 linhas.

**Absorveu de graça:** a absorção parou na spec, então o que vem abaixo foi confirmado na spec e no tasks.md, não no código. No código, continua sendo previsão da análise, porque as T-021 a T-032 não foram implementadas. O que o diff confirma: nenhuma das RN-004 a RN-007 nem a RN-012 mudou de texto, e os 112 testes existentes continuam corretos pela convenção "lote sem centro de custo usa a tabela padrão" (spec 2.0, §4.2; D-012).
- **A ordem da §8 e a coleta de motivos.** A ordem vive numa função só, `Motor.avaliar` (DT-006). Um motivo novo é uma entrada na enum mais uma linha na §8, e o `OrdemMotivosSecao8Test` cobra a coerência entre as duas.
- **Duplicidade, `aparar()` e as validações de lote e de item.** A duplicidade em duas passadas (DT-007), o `aparar()` e as validações não dependem de limite nem de moeda.
- **O ponto único `limiteAplicavel`.** O plan §4 previa que "uma ampliação condicional altera essa função e mais nada". O limite por centro de custo entra ali.
- **A fronteira do domínio (DT-009, `FronteiraDominioTest`).** Política e câmbio entram por adaptadores, sem `java.io` no domínio.
- **`Valor` e a RN-011.** A conversão de moeda é uma multiplicação seguida de `normalizar`.
- **O que ainda não existe não precisa ser desfeito.** Entrada, saída e CLI (T-017 a T-019) ainda não foram implementadas.

**Resistiu:** previsão da análise. As T-023 a T-030 é que vão medir isso no código.
1. **`Politica.kt`.** A enum `Categoria`, com três valores e limites fixos, não representa categorias por centro de custo vindas de arquivo: `representacao` no CC-COMERCIAL, hospedagem 0,00 no CC-ENG-PLATAFORMA. Isso contradiz o plan §4 ("constante, não arquivo de configuração") e a linha do `CLAUDE.md` sobre a política.
2. **A CLI.** Ela é fixa pelo DESAFIO (`calcular --input --output`) e não diz de onde vêm `politica-v4.json` e `cambio.json`.
3. **A AMB-015 "por construção".** O adaptador não lê o centro de custo, e os testes previstos da T-017 para a AMB-015 se invertem.
4. **"Informações não previstas são ignoradas".** A regra da RN-010 (D-007) é o oposto do que a moeda precisa: o motor pagaria e-002 (22,00 EUR) como R$ 22,00, em silêncio.
5. **Os testes das RN-001, RN-002, RN-003 e RN-008 sem centro de custo.** Sobrevivem só se centro de custo ausente cair na política padrão (leitura P3(a) da análise).
6. **A §9 e a T-020.** Caem antes de serem implementadas. O lote do Dia 1 é do c-0417, do CC-ENG-PLATAFORMA, e a v4 é retroativa à competência 07/2026. d-001 passa de 60,00 parcial para 72,50 integral, d-014 de 60,00 para 61,00, d-010 de 250,00 para 0,00 (classificação em aberto), e o total vai de 623,43 para 386,93.

**Ordem em que fiz:**
- Os arquivos do envelope chegaram em duas tentativas. Na primeira, eles não existiam na máquina, e o agente parou em vez de responder no escuro. Na segunda, vieram do gist e foram copiados por blob com hash conferido (sessão de implementação, `2aed7d1`).
- Antes de qualquer mudança na spec, o agente fez a análise completa. Ela lista o que quebra por seção, RN e AMB; levanta as ambiguidades novas sem decidir nenhuma (P1–P6 para "aplica-se a política padrão", T1–T8 para "taxa da data da despesa", mais V1, A1, R1, F1 e C1, que o envelope não aponta); e separa o que absorve do que resiste.
- Com a análise na mão, decidi as 17 ambiguidades em bloco. A ordem dos commits é a que o envelope pede: spec (`a04c1d3`, 01:17), DECISIONS (`c89de59`, 01:18), tasks (`46996d2`, 01:21). O código não foi feito.
- Na escrita da spec, o agente não aplicou meu texto em três pontos e avisou antes (D-012, "Como as decisões foram tomadas"):
  - eu tinha posto a conversão "depois da RN-011", o que arredondaria duas vezes, contra a minha própria T4 (AMB-030);
  - meu critério para a P6 dependia do texto livre "nao reembolsavel";
  - eu tinha chamado de 17 decisões um bloco de 18 pontos.

**Se eu tivesse escrito a spec original sabendo desta mudança:**
- Não teria aceito a política como constante sem pelo menos isolar a tabela de limites atrás de uma consulta por centro de custo. Eu mesmo tinha escrito no pedido do plan que essa era a decisão mais cara se a política mudasse (`docs/sessions/08-plan-tecnico.md`).
- Teria tratado "informação não prevista é ignorada" (RN-010, D-007) e "centro de custo sem efeito" (AMB-015, §4.1) como decisões de risco, com custo aceito explícito, e não como simplificação.
- Teria declarado na §4 a moeda do valor, mesmo que só BRL fosse aceito.

[FALTA: o que mais eu mudaria, depois de absorver]

**O que a spec me poupou, em concreto:**
- **Uma análise de impacto endereçável.** A análise do envelope pôde dizer, regra por regra, o que cai: RN-001, 002, 003, 008, 009, 010 e 011; AMB-013, 015, 007, 001, 006, 014 e 009. E também o que continua válido: o texto das RN-004, 005, 006, 007 e 012. Não precisei reler o código para saber.
- **Um impacto calculável.** A §7 e a §9 tinham os resultados esperados do lote do Dia 1 item a item, o que permitiu calcular o novo total (386,93 contra 623,43) antes de escrever uma linha.
- **Ambiguidades novas reconhecíveis pelo mesmo critério.** As decisões anteriores já tinham critério escrito: abrangência do dano (AMB-020), alcance estreito da normalização (AMB-013), campo sem regra (AMB-015). Por isso as ambiguidades novas, como P3 (centro de custo ausente: padrão ou erro de lote) e T8 (moeda nula ou minúscula), podem ser decididas por esses critérios, e não do zero.

[FALTA: tempo ou retrabalho economizado, medido depois da absorção]

---

## Fechamento

**O que ficou de fora, e por quê:**
- **Não implementadas:**
  - T-014 a T-020: consolidado, bordas da §7, `RastreabilidadeTest`, entrada JSON, saída JSON, CLI e ponta a ponta;
  - T-021 a T-032: o envelope no código.
- **Onde parou:** a spec 2.0 absorveu o envelope por inteiro (`a04c1d3`, `c89de59`, `46996d2`). O código parou com a etapa 1 da §8 completa, em `8d6c8b6`, com 112 testes.
- **Por quê:** priorizei a absorção pela spec, que é o que o envelope avalia, em vez de código que não caberia no tempo.
- **O custo:** não há CLI executável. O `OrdemCamposSecao41Test` falha desde `a04c1d3`, por desenho, até a T-023 (D-012).

**Para qual tamanho de projeto isto valeu a pena?**

**Para qual não valeria?**

**O que eu faria diferente:**

**A coisa mais desconfortável que aprendi sobre como eu trabalho com IA:**
