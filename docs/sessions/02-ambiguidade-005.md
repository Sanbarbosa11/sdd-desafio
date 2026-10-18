
> /clear

> Leia specs/001-motor-reembolso/spec.md e CLAUDE.md. Estamos decidindo as
  ambiguidades da política de reembolso uma por uma. As AMB-001 a 004 e a 006
  já estão decididas e registradas; a AMB-005 está pendente.

  Apresente a AMB-005 (efeito da ausência de nota fiscal quando ela é exigida)
  no formato que a spec já usa: texto do RH, as leituras possíveis com o
  argumento de negócio de cada uma, os números em d-004, d-013 e no caso de
  alimentação de 500,00, e o custo de cada escolha. Sem recomendação — eu
  decido.

● Bash(cd "c:/Users/dev/SDD-Desafio" && cat CLAUDE.md && echo ===== && cat
      specs/001-motor-reembolso/spec.md && echo ===== && ls -R specs | head -50)
  ⎿  Error: Exit code 1
     cat: CLAUDE.md: No such file or directory

● Bash(cd "c:/Users/dev/SDD-Desafio" && ls -la && find . -maxdepth 4 -not -path
      '*/node_modules*' -not -path '*/.git/*' | head -80)
  ⎿  (No output)

● Bash(cd "c:/Users/dev/SDD-Desafio/sdd-desafio" && cat CLAUDE.md && echo =====
      && cat specs/001-motor-reembolso/spec.md && echo ===== && cat
      specs/001-motor-reembolso…)
  ⎿  (No output)

● Bash(cd "c:/Users/dev/SDD-Desafio/sdd-desafio" && cat DESAFIO.md && echo =====
      && cat exemplos/despesas-exemplo.json && echo ===== && grep -n -i -A3
      "AMB-005\|nota f…)
  ⎿  (No output)

● Bash(cd "c:/Users/dev/SDD-Desafio/sdd-desafio" && sed -n 1300,1480p
      docs/sessions/01-ambiguidades-001-a-006.md)
  ⎿  (No output)

● AMB-005: o que acontece quando falta a nota fiscal exigida

  Texto do RH: item 5, "Nota fiscal é obrigatória acima de R$ 100." A política
  diz quando a NF é exigida, mas não diz o que acontece se ela faltar. Nenhum
  outro item fala disso.

  O que já está decidido:
  - A NF é exigida para valores estritamente maiores que 100,00 (AMB-004).
  - A comparação usa o valor lançado, já normalizado (AMB-006).
  - Um item que exige NF e a apresenta segue normalmente para o limite (RN-003).
  - Sigo a premissa global de despesa fora de viagem.
  - Em d-013, mostro 1 diária e 3 diárias, porque a contagem fica para a
    AMB-009.

  ---

  Leitura A: recusa total

  Sem a NF exigida, o item rende 0,00 e é classificado como recusado.

  Argumentos de negócio a favor
  - "Obrigatória" quer dizer condição para pagar. Sem o comprovante, a empresa
    não sabe se a transação existiu nem quanto custou. Então não há base para
    pagar parcela nenhuma.
  - A AMB-006 já disse que a documentação é binária: o comprovante existe ou não
    existe. A recusa é o efeito que combina com essa natureza.
  - O incentivo é claro: o colaborador sempre tem motivo para pedir a nota.

  Leitura B: paga até o teto de dispensa (R$ 100,00)

  Sem a NF exigida, o reembolso é o menor valor entre o teto da RN-002 e 100,00.
  A parte acima de 100 é o que precisaria de comprovante. Os 100,00 primeiros a
  política já aceita pagar sem NF (AMB-004).

  Argumentos de negócio a favor
  - A política já confia no colaborador até 100,00 sem comprovante. Uma despesa
    de 100,01 sem NF não é menos crível do que uma de 100,00 nos primeiros 100
    reais.
  - O resultado não dá salto na fronteira: d-003 e d-004 rendem o mesmo valor. A
    AMB-006 deixou registrado que esse desconforto cabia à AMB-005, e esta
    leitura acaba com ele.
  - Combina com a justificativa da AMB-002 de não punir desvio mínimo.

  Leitura C: pendente de comprovação

  Sem a NF exigida, o item rende 0,00 nesta apuração, mas recebe um status
  próprio ("pendente"), diferente de "recusado". A saída informa quanto o item
  renderia se a nota fosse apresentada.

  Argumentos de negócio a favor
  - Falta de NF quase sempre tem conserto (segunda via, pedido ao fornecedor).
    Categoria fora da política (item 9) não tem. Tratar as duas coisas como a
    mesma "recusa" esconde do colaborador o que ele pode fazer.
  - A empresa não paga nada sem comprovante, e o colaborador sabe exatamente
    quanto receberá ao regularizar.

  Leitura D: paga normalmente e sinaliza

  Para ficar completo: o item segue para o limite como se tivesse a NF, e a
  saída só registra o alerta "NF exigida e ausente".

  Argumento a favor: o motor calcula e o financeiro audita. Quem decide o que
  fazer com o alerta é a pessoa que confere.

  Fato de coerência: esta leitura trata "obrigatória" como "recomendada", o que
  contraria a letra do item 5.

  ---

  Números

  Item: d-004 (transporte, 100,01, sem NF; teto 80,00)
  Com NF (referência): 80,00 parcial
  A: recusa: 0,00 recusado
  B: até 100,00: mín(80; 100) = 80,00 parcial
  C: pendente: 0,00 pendente (renderia 80,00)
  D: paga e sinaliza: 80,00 parcial + alerta
  ────────────────────────────────────────
  Item: d-013, 1 diária (hospedagem, 690,00, sem NF; teto 250,00)
  Com NF (referência): 250,00 parcial
  A: recusa: 0,00 recusado
  B: até 100,00: mín(250; 100) = 100,00 parcial
  C: pendente: 0,00 pendente (renderia 250,00)
  D: paga e sinaliza: 250,00 parcial + alerta
  ────────────────────────────────────────
  Item: d-013, 3 diárias (teto 750,00)
  Com NF (referência): 690,00 integral
  A: recusa: 0,00 recusado
  B: até 100,00: mín(690; 100) = 100,00 parcial
  C: pendente: 0,00 pendente (renderia 690,00)
  D: paga e sinaliza: 690,00 integral + alerta
  ────────────────────────────────────────
  Item: Alimentação 500,00, sem NF (teto 60,00)
  Com NF (referência): 60,00 parcial
  A: recusa: 0,00 recusado
  B: até 100,00: mín(60; 100) = 60,00 parcial
  C: pendente: 0,00 pendente (renderia 60,00)
  D: paga e sinaliza: 60,00 parcial + alerta
  ────────────────────────────────────────
  Item: d-003 (controle: 100,00, sem NF, não exige)
  Com NF (referência): —
  A: recusa: 80,00
  B: até 100,00: 80,00
  C: pendente: 80,00
  D: paga e sinaliza: 80,00

  Quando a falta de NF muda o resultado na leitura B: só quando o teto da RN-002
  passa de 100. Isso nunca acontece em alimentação (o teto é 60, ou 90 em
  viagem) nem em transporte fora de viagem (80). Nesses casos, a leitura B dá o
  mesmo valor que com NF. Na hospedagem o teto é sempre de pelo menos 250, então
  ali a leitura B sempre corta o valor para 100,00.

  Ordem das regras (§8): nas quatro leituras, o resultado não depende de a NF
  ser avaliada antes ou depois do limite.
  - A e C zeram o item de qualquer forma.
  - Em B, mín(valor, limite, 100) é o mesmo em qualquer ordem.
  - D não altera o valor.

  Nenhuma leitura cria ciclo, porque a exigência já foi fixada sobre o valor
  lançado (AMB-006).

  ---

  Custo de cada escolha

  Leitura A
  - O salto na fronteira fica em tamanho máximo: d-003 (100,00) rende 80,00 e
    d-004 (100,01) rende 0,00. Um centavo que a empresa nem pagaria custa 80,00
    de reembolso. A AMB-006 registrou esse custo como condicionado a esta
    decisão.
  - Fato de coerência, sem recomendação: o argumento da AMB-002 contra "punir
    desvio mínimo" se aplicaria aqui. O argumento da AMB-006 de que
    "documentação é binária" sustenta a recusa.
  - O custo cai inteiro sobre fornecedores que não emitem NF (§10: Airbnb em
    d-013, transporte informal). d-013 perde 250,00 ou 690,00.
  - Para a AMB-007: se a viagem for inferida a partir de uma hospedagem, falta
    decidir se uma hospedagem recusada ainda conta como prova de viagem.

  Leitura B
  - Para alimentação em qualquer situação e transporte fora de viagem, o item 5
    deixa de ter efeito. Um jantar de 500,00 sem NF rende 60,00, igual ao que
    renderia com NF.
  - Fato de coerência, sem recomendação: esse é o mesmo resultado que a
    justificativa da AMB-006 citou para rejeitar a leitura (b) de lá ("um jantar
    de R$ 500 sem comprovante renderia R$ 60"). Escolher B aqui leva ao mesmo
    resultado por outro caminho. A exigência continua formalmente avaliada sobre
    o valor lançado, mas não produz efeito nessas categorias.
  - Um colaborador racional nunca tem motivo para pedir NF de alimentação.
    "Obrigatória" passa a significar "sem ela, o reembolso tem teto de 100,00".
  - A saída precisa explicar um corte cuja causa não é o limite da categoria. Em
    d-013, o motivo de "parcial" é a falta de NF, não o limite de hospedagem.
    Ou seja, "parcial" passa a ter duas causas, e o schema (§4) tem de
    separá-las.

  Leitura C
  - Cria um terceiro status que a política não menciona. O schema (§4) passa a
    ter integral, parcial, recusado e pendente, além do "valor potencial".
  - O motor não tem fluxo para receber a nota depois. Quem regulariza e quando
    fica fora do sistema, e provavelmente fora de escopo (§3).
  - Liga-se ao item 7: o item ainda poderá ser regularizado se a nota chegar
    depois do período de competência? A política não diz. Isso abre uma pergunta
    nova.
  - Nesta apuração, os números são idênticos aos da leitura A. A diferença está
    só na classificação e na informação devolvida.

  Leitura D
  - Contraria a letra do item 5: "obrigatória" vira aviso.
  - d-013 rende até 690,00 sem comprovante nenhum. É o maior valor pago sem NF
    entre todas as leituras.
  - A decisão real sai do motor e vai para uma conferência manual, que é o
    processo que o desafio quer substituir.

  Fico no aguardo da sua decisão. Não alterei nenhum arquivo.

> #5 decidido — leitura A, com motivo estruturado distinto.

  AMB-005 — Efeito da ausência de nota fiscal exigida

  Texto do RH: item 5, "Nota fiscal é obrigatória acima de R$ 100." A política
  estabelece quando a nota é exigida, mas não diz o que acontece quando ela
  é exigida e não está presente.

  O que não está claro: quatro leituras — (a) o item é recusado integralmente;
  (b) o reembolso é limitado ao teto de dispensa de R$ 100,00; (c) o item fica
  pendente de comprovação, com status próprio; (d) o item é reembolsado
  normalmente e a saída registra um alerta.

  Decisão: adotada a leitura (a). Uma despesa que exige nota fiscal e não a
  apresenta tem reembolso zero e é classificada como recusada, com motivo
  SEM_NOTA_FISCAL. A recusa é integral e independe do limite da categoria: o
  item não é avaliado contra o limite, e um lançamento que estaria integral
  caso tivesse nota é recusado da mesma forma. O motivo da recusa é registrado
  de forma distinta de outras causas de recusa, em particular de
  CATEGORIA_INELEGIVEL.

  Justificativa: "obrigatória" estabelece condição, não recomendação. Sem o
  comprovante, a empresa não tem como atestar que a transação ocorreu nem
  quanto custou, de modo que não há base para pagar parcela alguma — o
  pagamento parcial reembolsaria parte de uma despesa não comprovada. Esta é
  também a consequência coerente com a AMB-006, que estabeleceu a natureza
  binária da documentação: o comprovante existe ou não existe. A leitura (b)
  produziria exatamente o resultado que a justificativa da AMB-006 usou para
  rejeitar a leitura pelo valor reembolsável — um jantar de R$ 500 sem
  comprovante renderia R$ 60 — esvaziando a regra em alimentação e em
  transporte fora de viagem. A leitura (d) trataria "obrigatória" como
  "recomendada" e devolveria a decisão à conferência manual. A leitura (c)
  tem mérito ao distinguir o que tem conserto do que não tem, mas exigiria um
  quarto estado e um campo de valor potencial que a política não prevê, além
  de abrir a questão nova de até quando a nota pode ser apresentada; o ganho
  que ela traz é capturado aqui pelo motivo estruturado, sem criar estado novo.

  Custo aceito: o salto na fronteira é máximo. d-003 (R$ 100,00, sem nota)
  rende R$ 80,00 e d-004 (R$ 100,01, sem nota) rende R$ 0,00 — um centavo que
  a empresa não pagaria em nenhum dos casos, já que o teto de transporte é
  R$ 80,00, determina uma diferença de R$ 80,00 no reembolso. Aceito porque a
  regra é documental e não comporta gradação: admitir pagamento parcial sem
  comprovante seria admitir que a obrigatoriedade é negociável. O custo recai
  sobretudo sobre fornecedores que habitualmente não emitem nota, conforme já
  registrado na §10 — d-013 perde integralmente o reembolso.

  Consequências verificáveis:
  - d-004 (transporte urbano, 100,01, sem NF) → reembolso 0,00, recusado,
    motivo SEM_NOTA_FISCAL
  - d-013 (hospedagem, 690,00, sem NF) → reembolso 0,00, recusado, motivo
    SEM_NOTA_FISCAL, qualquer que seja o número de diárias
  - alimentação de 500,00 sem NF → reembolso 0,00, recusado, motivo
    SEM_NOTA_FISCAL, embora o teto da categoria seja 60,00
  - d-003 (transporte urbano, 100,00, sem NF) → nota não exigida; reembolso
    80,00, parcial — inalterado
  - transporte urbano de 100,01 com NF → reembolso 80,00, parcial — inalterado

  Ordem das regras: a recusa por ausência de nota torna irrelevante a avaliação
  do limite para o item. Registre na §8 que a verificação de nota fiscal
  precede a aplicação do limite, ainda que nesta decisão o resultado seja o
  mesmo em qualquer ordem.

  Pergunta que fica aberta e que preciso registrar na §10: se a caracterização
  de viagem (AMB-007) vier a ser inferida a partir de despesas de hospedagem,
  é necessário decidir se uma hospedagem recusada por ausência de nota ainda
  caracteriza viagem. Registre essa dependência.

  Registre como AMB-005, ajuste a RN-003 ou crie a RN-004 conforme fizer mais
  sentido para a legibilidade, atualize os aceites pendentes de d-004, d-013 e
  alimentação de 500,00, acrescente os casos à §7, e me mostre o diff antes de
  salvar.

● Vou preparar a alteração numa cópia da spec, no scratchpad, e mostrar o diff.
  A spec real fica intacta até você aprovar.

● Bash(S="C:/Users/dev/AppData/Local/Temp/claude/c--Users-dev-SDD-Desafio/0308ef
      d8-0138-44dd-844f-6d25461c434d/scratchpad"; mkdir -p "$S" && cp
      "c:/Users/dev/SDD-Desaf…)
  ⎿  (No output)

  Made 5 scratchpad edits +125 -30, read 1 file (ctrl+o to expand)

● Bash(cd "c:/Users/dev/SDD-Desafio/sdd-desafio" && diff -u
      specs/001-motor-reembolso/spec.md "C:/Users/dev/AppData/Local/Temp/claude/
      c--Users-dev-SDD-Desafio/0308efd8…)
  ⎿  Files differ

● Preparei o diff numa cópia, como você pediu. A spec real ainda não foi
  alterada. A proposta está em
  C:\Users\dev\AppData\Local\Temp\claude\c--Users-dev-SDD-Desafio\0308efd8-0138-
  44dd-844f-6d25461c434d\scratchpad\spec.proposta.md, e o diff completo está
  logo acima.

  Como organizei
  - RN-004 nova em vez de crescer a RN-003. A RN-003 continua dizendo quando a
    nota é exigida. A RN-004 diz o que acontece se ela faltar. Com isso, cada
    aceite aponta para uma única regra.
  - RN-003: a frase "definido pela AMB-005" virou "recusada conforme a RN-004".
    Saiu de "Depende de" a pendência da AMB-005. Os três aceites pendentes agora
    têm resultado: 0,00, recusado (RN-004).
  - AMB-005: registrei o seu texto sem mudar o conteúdo. Acrescentei "Ordem de
    aplicação → §8", "Em aberto → §10" e "Regra afetada: RN-003, RN-004".
  - §7: os três casos (d-004, alimentação de 500,00, d-013) passam a ter
    resultado e motivo.
  - §8: a NF é verificada antes do limite, com a ressalva de que o resultado não
    muda com a ordem. A ordem está marcada como parcial.
  - §10: registrei a dependência entre hospedagem recusada e caracterização de
    viagem (AMB-007).

  Acrescentei algo que você não pediu
  - Um caso de hospedagem de 200,00 sem NF, na RN-004 e na §7. Ele testa a frase
    da sua decisão "um lançamento que estaria integral caso tivesse nota é
    recusado da mesma forma". Nenhum outro caso cobria isso, porque d-013
    depende das diárias (AMB-009). Se não quiser esse caso, eu tiro.

  Três pontos para você decidir (não mexi neles)
  1. CATEGORIA_INELEGIVEL aparece na spec antes de existir a regra do item 9. A
     §4, que define o schema de saída, ainda não lista os motivos. Sugiro manter
     assim e fechar quando decidirmos o item 9 e a §4.
  2. AMB-002 ainda diz "d-003 → 80,00, parcial (condicionado à AMB-004/005)".
     Desde a AMB-004, o resultado de d-003 não depende mais da AMB-005, porque a
     NF não é exigida ali. AMB-004 também ainda diz "resultado definido pela
     AMB-005" para d-004 e d-013. Deixei os dois como estão, por serem registro
     histórico. Se preferir, atualizo para apontar para a RN-004.
  3. DECISIONS.md ainda é só o template, sem nenhuma entrada D-001 ou D-002
     preenchida. Esta mudança e as anteriores ainda não estão registradas lá.

  Se aprovar, copio a proposta sobre specs/001-motor-reembolso/spec.md.

> Aprovado com três ajustes.

  1. Mantenha o caso de hospedagem de 200,00. Ele testa exatamente a frase
     "a recusa independe do limite" e nenhum outro caso cobria isso.
  2. Ponto 2: corrija, não mantenha como histórico. A spec descreve o estado
     atual do sistema; registro histórico é função do git log. Hoje ela afirma
     duas coisas divergentes sobre d-003, e isso torna a spec não verificável.

  Ajuste:
  - Na AMB-002, Consequências verificáveis: "d-003 (100,00, limite 80) →
    80,00, parcial (condicionado à AMB-004/005)" passa a "d-003 (100,00,
    limite 80) → 80,00, parcial; a nota fiscal não é exigida (RN-003)".
  - Na AMB-004, Consequências verificáveis: substitua "resultado definido pela
    AMB-005" por "reembolso 0,00, recusado, motivo SEM_NOTA_FISCAL (RN-004)"
    em d-004 e d-013.
  - Varra o restante da spec procurando outras referências a ambiguidades já
    decididas como se ainda estivessem pendentes, e me liste o que encontrar
    antes de alterar.

  3. Ponto 3: o DECISIONS.md continua fechado para as AMB-001 a 006, que são
     escrita inicial e não mudança. Mas a correção do item 2 acima É uma
  mudança
     de spec, e merece a primeira entrada. Crie D-001 no DECISIONS.md com:

  - Gatilho: a decisão da AMB-005 tornou obsoletas afirmações registradas nas
    AMB-002 e AMB-004, que referenciavam a AMB-005 como pendente.
  - O que mudou na spec: as Consequências verificáveis da AMB-002 e da AMB-004
    passam a declarar o resultado final de d-003, d-004 e d-013 em vez de
    apontar para uma ambiguidade pendente.
  - Por quê: a spec descreve o estado atual do sistema. Afirmações divergentes
    sobre o mesmo item tornam a spec não verificável.
  - O que isso invalidou: nenhuma regra ou critério de aceite. Apenas texto de
    registro que havia se tornado desatualizado.
  - Tasks afetadas: nenhuma — tasks.md ainda não foi escrito.
  - Custo: 1 arquivo, correção pontual.

  Me mostre o diff dos três ajustes antes de salvar.

● Bash(cd "C:/Users/dev/AppData/Local/Temp/claude/c--Users-dev-SDD-Desafio/0308e
      fd8-0138-44dd-844f-6d25461c434d/scratchpad" && grep -n -E "AMB-00[1-6]"
      spec.proposta.m…)
  ⎿  (No output)

● Bash(cd "C:/Users/dev/AppData/Local/Temp/claude/c--Users-dev-SDD-Desafio/0308e
      fd8-0138-44dd-844f-6d25461c434d/scratchpad" && sed -n 60,95p
      spec.proposta.md; echo ---…)
  ⎿  (No output)

● Bash(cd "C:/Users/dev/AppData/Local/Temp/claude/c--Users-dev-SDD-Desafio/0308e
      fd8-0138-44dd-844f-6d25461c434d/scratchpad" && sed -n 257,266p
      spec.proposta.md && cp "…)
  ⎿  (No output)

  Made 3 scratchpad edits +27 -35, read 1 file (ctrl+o to expand)

● Bash(cd "c:/Users/dev/SDD-Desafio/sdd-desafio/specs/001-motor-reembolso" &&
      S="C:/Users/dev/AppData/Local/Temp/claude/c--Users-dev-SDD-Desafio/0308efd
      8-0138-44dd-844…)
  ⎿  Files differ

● Bash(cd "c:/Users/dev/SDD-Desafio/sdd-desafio/specs/001-motor-reembolso" &&
      diff -U2 spec.md
      "C:/Users/dev/AppData/Local/Temp/claude/c--Users-dev-SDD-Desafio/0308efd…)
  ⎿  (No output)

● Os três ajustes foram aplicados nas cópias do scratchpad. Nenhum arquivo do
  projeto foi alterado ainda.

  1. Caso de hospedagem de 200,00

  Ficou na proposta como estava, na RN-004 e na §7. Não mudou nada aqui.

  2. AMB-002 e AMB-004

  @@ AMB-002 — Consequências verificáveis
  -- d-003 (100,00, limite 80) → 80,00, parcial (condicionado à AMB-004/005)
  +- d-003 (100,00, limite 80) → 80,00, parcial; a nota fiscal não é exigida
  (RN-003)

  @@ AMB-004 — Consequências verificáveis
   - d-004 (transporte urbano, 100,01, sem NF) → nota fiscal exigida e ausente;
  -  resultado definido pela AMB-005
  +  reembolso 0,00, recusado, motivo SEM_NOTA_FISCAL (RN-004)
   - d-013 (hospedagem, 690,00, sem NF) → nota fiscal exigida e ausente;
  -  resultado definido pela AMB-005
  +  reembolso 0,00, recusado, motivo SEM_NOTA_FISCAL (RN-004)

  Varredura: outras referências antigas (não alterei nenhuma)

  Procurei referências a ambiguidades já decididas que ainda as tratam como
  pendentes. Achei três. Os números de linha são da versão proposta.

  #: A
  Onde: AMB-001, cabeçalho das Consequências (l. 180)
  Texto atual: "(assumindo AMB-002 = corta o excedente)"
  Problema: A AMB-002 já foi decidida. A condição continua ali como se fosse uma

  hipótese.
  ────────────────────────────────────────
  #: B
  Onde: AMB-006, Custo aceito (l. 355–359)
  Texto atual: "A severidade dessa diferença depende da AMB-005: se a ausência
  de
  nota recusar… se limitar… Registro aqui que… seu tratamento cabe à AMB-005"
  Problema: Apresenta como hipótese algo que a AMB-005 já decidiu: d-004 perde
  R$
  80,00 pelo centavo.
  ────────────────────────────────────────
  #: C
  Onde: AMB-006, Consequências (l. 362)
  Texto atual: d-004 → "efeito definido pela AMB-005"
  Problema: É o mesmo caso da AMB-004 que acabamos de corrigir.

  Dois pontos menores, que você pode considerar:
  - D. Na mesma lista da AMB-006, d-013 e a alimentação de 500,00 dizem só
    "exigida e ausente", sem resultado. Não está desatualizado, mas não traz o
    resultado final, como na correção que você pediu para a AMB-004.
  - E. Na AMB-001 (l. 188), "a ambiguidade #3 … deixa de existir" usa a
    numeração antiga. Hoje ela é a AMB-003, já registrada como dissolvida.

  As referências às ambiguidades ainda pendentes (AMB-007, 009, 011, 013, 014)
  estão corretas.

  3. D-001 no DECISIONS.md

  Escrevi o D-001 com o seu texto. Acrescentei só a lista "de → para", porque o
  campo do template pede isso ("de → para, citando o ID da regra"). A data é
  2026-10-18.

  O arquivo tinha dois blocos vazios do template, D-002 e D-001. Substituí os
  dois pela entrada real. Assim não fica um "D-002" vazio acima do D-001 numa
  lista em ordem inversa. Se preferir manter o bloco vazio como modelo, eu
  devolvo.

  Uma observação: se você aprovar correções da varredura (A–E), elas fazem parte
  da mesma mudança. O D-001 precisaria citar também a AMB-001 e a AMB-006, e
  não só a 002 e a 004.

  Arquivos propostos

  - C:\Users\dev\AppData\Local\Temp\claude\c--Users-dev-SDD-Desafio\0308efd8-013
    8-44dd-844f-6d25461c434d\scratchpad\spec.proposta.md
  - C:\Users\dev\AppData\Local\Temp\claude\c--Users-dev-SDD-Desafio\0308efd8-013
    8-44dd-844f-6d25461c434d\scratchpad\DECISIONS.proposta.md

  Preciso de duas respostas antes de salvar:
  1. Corrijo algum dos itens A–E? Se sim, quais?
  2. Posso salvar sobre spec.md e DECISIONS.md?

> Corrija os cinco: A, B, C, D e E.

  A, B e C são a mesma classe de problema que você já corrigiu na AMB-002 e
  AMB-004 — ambiguidade decidida tratada como pendente. O B é o mais crítico:
  a AMB-006 apresenta como hipótese ("se a ausência recusar... se limitar...")
  algo que a AMB-005 já resolveu. Um leitor externo sai dali sem saber qual
  alternativa vale.

  Para o B especificamente, reescreva o Custo aceito da AMB-006 de modo que o
  resultado seja afirmado, não condicionado. Algo como: "A severidade dessa
  diferença foi fixada pela AMB-005: a ausência de nota recusa o item
  integralmente, de modo que d-004 perde R$ 80,00 por conta do centavo."
  Mantenha o reconhecimento do desconforto, só remova a hipótese.

  D: complete d-013 e a alimentação de 500,00 com o resultado final, mesma
  forma da correção da AMB-004.

  E: substitua "a ambiguidade #3" por "a AMB-003". A numeração #N era da sua
  lista de trabalho e não existe na spec; um leitor externo não saberia a que
  se refere.

  Sim quanto ao D-001: ele passa a citar AMB-001, AMB-002, AMB-004 e AMB-006.
  Ajuste o texto para "as Consequências verificáveis das AMB-001, 002, 004 e
  006 passam a declarar o resultado final em vez de apontar para ambiguidades
  já decididas, e a AMB-001 passa a usar a numeração AMB-003 em vez da
  numeração da lista de trabalho".

  Os blocos vazios do template: mantenha removidos, foi a escolha certa.

  Pode salvar sobre spec.md e DECISIONS.md depois dos cinco ajustes. Me mostre
  o diff final antes.

  Made 6 scratchpad edits +39 -24 (ctrl+o to expand)

● Bash(cd "c:/Users/dev/SDD-Desafio/sdd-desafio/specs/001-motor-reembolso" &&
      S="C:/Users/dev/AppData/Local/Temp/claude/c--Users-dev-SDD-Desafio/0308efd
      8-0138-44dd-844…)  ⎿  No matches found

● Fiz os cinco ajustes nas cópias do scratchpad. O diff final de spec.md e
  DECISIONS.md está logo acima. Os arquivos do projeto ainda não foram
  alterados.

  Os cinco ajustes
  - A. Na AMB-001, "(assumindo AMB-002 = corta o excedente)" passou a "(com o
    reembolso parcial definido na AMB-002: o excedente é cortado)".
  - B. No Custo aceito da AMB-006, o resultado agora é afirmado: "foi fixada
    pela AMB-005: a ausência de nota recusa o item integralmente, de modo que
    d-004 perde R$ 80,00 por conta do centavo". O reconhecimento do desconforto
    continua, agora remetendo ao custo aceito na AMB-005.
  - C. Em d-004 na AMB-006, "efeito definido pela AMB-005" passou a "reembolso
    0,00, recusado, motivo SEM_NOTA_FISCAL (RN-004)".
  - D. Na AMB-006, d-013 e a alimentação de 500,00 ganharam o mesmo resultado
    final, como na AMB-004.
  - E. "a ambiguidade #3" passou a "a AMB-003".

  D-001. Usei o texto que você deu e mantive a lista "de → para", agora com
  AMB-001, 002, 004 e 006. O título passou a "Registros das AMB-001, 002, 004 e
  006 atualizados após a AMB-005". No Gatilho, acrescentei que a varredura achou
  o mesmo problema nas AMB-001 e AMB-006.

  Uma diferença no seu texto. Ele diz que mudaram as "Consequências
  verificáveis" das quatro ambiguidades. O ajuste B, porém, está no Custo aceito
  da AMB-006, e a lista "de → para" registra isso. Se quiser, troco a frase por
  "os registros das AMB-001, 002, 004 e 006". Se não, deixo como está.

  Conferência. Procurei de novo, na proposta, frases como "definido pela",
  "depende da", "condicionado", "pendente" e a numeração "#N" ligadas às AMB-001
  a 006. Não sobrou nenhuma ocorrência.

  Você pediu para ver o diff antes de salvar. Se aprovar, copio as duas
  propostas sobre spec.md (specs/001-motor-reembolso/spec.md) e DECISIONS.md
  (specs/001-motor-reembolso/DECISIONS.md).

> Pode salvar os cinco ajustes.

  Sobre a diferença: sim, troque para "os registros das AMB-001, 002, 004 e
  006". Você está certo — o ajuste B está no Custo aceito, não nas
  Consequências verificáveis. "Registros" cobre os dois campos sem imprecisão.

  Depois de salvar, não vá para a AMB-007 ainda. Vou fechar o lote primeiro.

  Made 1 scratchpad edit +2 -2 (ctrl+o to expand)

● Bash(S="C:/Users/dev/AppData/Local/Temp/claude/c--Users-dev-SDD-Desafio/0308ef
      d8-0138-44dd-844f-6d25461c434d/scratchpad";
      D="c:/Users/dev/SDD-Desafio/sdd-desafio/spe…)
  ⎿  Updated DECISIONS.md (+46 -42)
       1 -# Log de Decisões e Mudanças de Spec                           
       2 -                                                               
       3 -> Uma entrada **toda vez** que a spec mudar. Este arquivo é a p
         -rova de que a spec                                             
       4 -> foi tratada como artefato vivo e não como cerimônia de abertu
         -ra.                                                            
       5 ->                                                              
       6 -> Spec que não muda em dois dias é spec que ninguém consultou. 
         -Mudança não é                                                  
       7 -> demérito — mudança não registrada é.                         
       8 -                                                               
       9 -Ordem cronológica inversa: a mais recente primeiro.            
      10 -                                                               
      11 ----                                                            
      12 -                                                               
      13 -## D-002 — <título curto> · `<data>`                           
      14 -                                                               
      15 -**Gatilho:** <o que provocou: envelope lacrado / erro descobert
         -o na implementação /                                           
      16 -ambiguidade que só apareceu ao testar / o Claude apontou uma co
         -ntradição>                                                     
      17 -                                                               
      18 -**O que mudou na spec:** <de → para, citando o ID da regra>    
      19 -                                                               
      20 -**Por quê:**                                                   
      21 -                                                               
      22 -**O que isso invalidou:** <requisitos, decisões técnicas, teste
         -s que caíram>                                                  
      23 -                                                               
      24 -**Tasks afetadas:** <as que precisaram ser refeitas + as novas 
         -criadas>                                                       
      25 -                                                               
      26 -**Custo:** <quantos arquivos tocados, quanto tempo>            
      27 -                                                               
      28 ----                                                            
      29 -                                                               
      30 -## D-001 — <título curto> · `<data>`                           
      31 -                                                               
      32 -**Gatilho:**                                                   
      33 -                                                               
      34 -**O que mudou na spec:**                                       
      35 -                                                               
      36 -**Por quê:**                                                   
      37 -                                                               
      38 -**O que isso invalidou:**                                      
      39 -                                                               
      40 -**Tasks afetadas:**                                            
     … 48 more lines (ctrl+o to expand)
  ⎿  Updated spec.md (+126 -28)
      101  já normalizado conforme a AMB-014, antes de qualquer aplicação
            de limite; o
      102  valor reembolsável não interfere na exigência. Uma despesa que
            exige nota fiscal e a
      103  apresenta não sofre nenhum efeito desta regra e segue para o l
           imite da sua
      104 -categoria. O efeito sobre uma despesa que exige nota fiscal e 
          -não a                                                         
      105 -apresenta é definido pela AMB-005.                            
      104 +categoria. Uma despesa que exige nota fiscal e não a apresenta
          + é recusada                                                   
      105 +conforme a RN-004.                                            
      106  **Origem:** política do RH, item 5; decisões AMB-004 e AMB-006
      107 -**Depende de:** RN-002 (reembolso parcial, decidido na AMB-002
          -); AMB-005                                                    
      108 -(efeito da ausência de nota fiscal exigida), AMB-007 (ampliaçã
          -o de limite em                                                
      109 -viagem) e AMB-014 (precisão monetária, que define a fronteira 
          -para valores                                                  
      110 -como 100,004), pendentes.                                     
      107 +**Depende de:** RN-002 (reembolso parcial, decidido na AMB-002
          +); RN-004                                                     
      108 +(recusa por ausência de nota fiscal exigida, decidida na AMB-0
          +05); AMB-007                                                  
      109 +(ampliação de limite em viagem) e AMB-014 (precisão monetária,
          + que define a                                                 
      110 +fronteira para valores como 100,004), pendentes.              
      111  **Aceite:**
      112  - d-003 (06/07, transporte urbano, 100,00, sem NF) → nota fisc
           al não exigida;
      113    reembolso 80,00, parcial.
      114  - d-004 (06/07, transporte urbano, 100,01, sem NF) → nota fisc
           al exigida e
      115 -  ausente.                                                    
      115 +  ausente; reembolso 0,00, recusado (RN-004).                 
      116  - Transporte urbano de 100,01, com NF → nota fiscal exigida e 
           presente;
      117    reembolso 80,00, parcial.
      118  - Alimentação de 50,00, sem NF → nota fiscal não exigida; reem
           bolso 50,00,
      119    integral.
      120  - Alimentação de 500,00, sem NF → nota fiscal exigida e ausent
           e, embora o
      121 -  teto da categoria seja 60,00.                               
      121 +  teto da categoria seja 60,00; reembolso 0,00, recusado (RN-0
          +04).                                                          
      122  - d-013 (22/07, hospedagem, 690,00, sem NF) → nota fiscal exig
           ida e ausente,
      123 -  qualquer que seja o número de diárias.                      
      124 -                                                              
      125 -### RN-004 — ...                                              
      123 +  qualquer que seja o número de diárias; reembolso 0,00, recus
          +ado (RN-004).                                                 
      124 +                                                              
      125 +### RN-004 — Recusa integral por ausência de nota fiscal exigi
          +da                                                            
      126 +                                                              
      127 +**Regra:** uma despesa que exige nota fiscal conforme a RN-003
          + e não a                                                      
      128 +apresenta tem reembolso 0,00 e é classificada como recusada, c
          +om motivo                                                     
      129 +SEM_NOTA_FISCAL. A recusa é integral e independe do limite da 
          +categoria: o                                                  
     … 189 more lines (ctrl+o to expand)

● Salvei os dois arquivos e conferi byte a byte com as propostas aprovadas:
  estão idênticos.

  - spec.md (specs/001-motor-reembolso/spec.md) recebeu a AMB-005, a RN-004, os
    aceites e casos da §7, a ordem parcial na §8, a pendência na §10 e os cinco
    ajustes A–E.
  - DECISIONS.md (specs/001-motor-reembolso/DECISIONS.md) recebeu o D-001. O
    texto agora diz "os registros das AMB-001, 002, 004 e 006 passam a
    declarar…".

  Não fiz commit. O CLAUDE.md pede docs(spec): para mudanças de spec. Ficou um
  detalhe: a pasta de trabalho SDD-Desafio não é repositório git, mas
  sdd-desafio/ tem um .git, então o commit sai de lá quando você fechar o lote.

  Paro aqui e não sigo para a AMB-007.