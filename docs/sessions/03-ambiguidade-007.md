
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

> Vamos para a AMB-007: caracterização de viagem e ampliação de limites em 50%.

  Formato de sempre: texto do RH, as leituras possíveis com o argumento de
  negócio de cada uma, os números nos itens afetados, e o custo de cada
  escolha. Sem recomendação — eu decido.

  Inclua obrigatoriamente:

  1. A opção de declarar a caracterização de viagem fora de escopo, e o que
     isso implica para o item 6 da política.
  2. O efeito em d-010, d-011 e d-013, e a quais categorias o +50% se aplicaria
     em cada leitura.
  3. Como cada leitura interage com a AMB-009 (número de diárias) — se viagem
     cobrir o período da hospedagem, a decisão de diárias passa a determinar
     quais dias são viagem.
  4. A pendência já registrada na §10: se uma hospedagem recusada por ausência
     de nota fiscal (d-013, RN-004) ainda caracteriza viagem.
  5. O arredondamento do acréscimo de 50% sobre cada limite nominal.
  6. Se alguma leitura reintroduz agregação entre despesas ou circularidade.
     A AMB-001 rejeitou agregação de valores e a AMB-006 foi decidida para
     evitar circularidade; quero saber se inferir viagem a partir de outras
     despesas do mesmo dia recria algum dos dois problemas.
  7. Se alguma leitura depende de interpretar texto livre (descrição ou
     fornecedor), e qual o custo disso em auditabilidade.

● AMB-007: o que caracteriza "em viagem" e como aplicar o acréscimo de 50%

  Texto do RH: item 6, "Colaborador em viagem tem limites ampliados em 50%."

  A política não diz:
  - o que caracteriza estar em viagem;
  - se a viagem vale para o colaborador no período inteiro, num dia ou numa
    despesa;
  - quais limites são ampliados.

  A entrada não tem nenhum campo de viagem. Cada despesa traz só data,
  categoria, descricao, fornecedor, valor e tem_nota_fiscal.

  O que já está decidido:
  - O limite é aplicado por despesa (AMB-001).
  - A ampliação opera sobre o valor do limite, não sobre a soma das despesas
    (RN-001).
  - A nota fiscal é exigida pelo valor lançado, independentemente de viagem
    (AMB-006).
  - Sem a NF exigida, a despesa é recusada antes de chegar ao limite (RN-004,
    §8).

  Limites ampliados:

  ┌───────────────────┬───────────────────┬───────────────────┐
  │     Categoria     │      Nominal      │     Em viagem     │
  ├───────────────────┼───────────────────┼───────────────────┤
  │ Alimentação       │ 60,00             │ 90,00             │
  ├───────────────────┼───────────────────┼───────────────────┤
  │ Transporte urbano │ 80,00             │ 120,00            │
  ├───────────────────┼───────────────────┼───────────────────┤
  │ Hospedagem        │ 250,00 por diária │ 375,00 por diária │
  └───────────────────┴───────────────────┴───────────────────┘

  ---

  As leituras

  Leitura A: caracterização de viagem fora de escopo

  O motor aplica sempre os limites nominais. A §3 declara que o item 6 não é
  implementado, e a premissa que hoje é provisória ("assumem despesas fora de
  viagem") passa a ser regra permanente.

  Argumentos de negócio a favor
  - A entrada é fixa e não informa viagem. Qualquer inferência seria inventar um
    dado que o RH não fornece.
  - O erro tem sempre a mesma direção: o motor nunca paga mais do que a política
    permite. Um eventual ajuste é sempre a favor do colaborador, e quem o faz é
    o financeiro.
  - Nada do que já está na spec precisa ser revisto.

  O que isso implica para o item 6: o item 6 fica sem efeito no motor. O
  colaborador em viagem recebe menos do que a política lhe garante, sempre. A
  diferença tem de ser tratada fora do sistema, por conferência manual, que é
  justamente o processo que o motor substitui. A spec teria de dizer isso
  explicitamente na §3 e na §10. Não pode ficar apenas implícito.

  Leitura B: campo explícito de viagem, opcional na entrada

  A entrada ganha um indicador opcional, por despesa ou no período. Se ele não
  vier, a despesa é tratada como fora de viagem.

  Argumentos de negócio a favor
  - Viagem é um fato administrativo: alguém autorizou a viagem. O motor recebe
    esse fato em vez de adivinhar.
  - A regra fica trivial de verificar: o campo existe ou não existe.

  Leitura C: viagem inferida pela hospedagem

  Um dia é de viagem quando está coberto por um lançamento de hospedagem,
  contando da data do lançamento até o número de diárias definido pela AMB-009.
  As despesas desses dias usam os limites ampliados.

  Argumentos de negócio a favor
  - Hospedagem é o sinal mais forte de viagem que existe na entrada: ninguém
    paga hotel na cidade onde mora.
  - Usa só campos estruturados (categoria e data), sem ler texto livre. Isso
    vale se a AMB-009 também não ler texto.
  - "Colaborador em viagem" descreve um estado da pessoa no tempo. O dia é a
    unidade natural para esse estado.

  Sub-escolha que não pode ficar em aberto: a própria hospedagem recebe os +50%?
  - C1: sim. Toda hospedagem fica em viagem, porque é ela que caracteriza a
    viagem. O limite de 250,00 do item 3 nunca mais se aplica, e o limite real
    de hospedagem passa a ser 375,00.
  - C2: não. A hospedagem caracteriza a viagem para as outras despesas, mas o
    limite dela continua nominal. A política não dá base para essa exceção:
    "limites ampliados" está no plural, sem ressalva.

  Leitura D: viagem inferida por texto livre

  Uma despesa ou um dia é de viagem quando a descricao ou o fornecedor contém um
  termo de uma lista, como "aeroporto", "hotel", "Airbnb", ou um nome de
  cidade.

  Argumentos de negócio a favor
  - Detecta sinais que a leitura C não vê. d-003 ("Corrida aeroporto") é
    provavelmente o início de uma viagem, num dia sem hospedagem.
  - Cobre viagens sem pernoite, como um bate-volta, que a leitura C nunca
    captura.

  Leitura E: o período inteiro é de viagem se houver hospedagem no período

  Basta uma hospedagem na competência para todas as despesas do período usarem
  os limites ampliados.

  Argumentos de negócio a favor
  - É a regra mais simples, e fica de pé sem a AMB-009.
  - É a leitura mais literal de "colaborador em viagem": o texto descreve a
    pessoa, não a despesa nem o dia.

  ---

  A quais categorias o +50% se aplica (ponto 2)

  ┌─────────┬─────────────────┬────────────────┬────────────────────────────┐
  │ Leitura │   Alimentação   │   Transporte   │         Hospedagem         │
  ├─────────┼─────────────────┼────────────────┼────────────────────────────┤
  │ A       │ nunca           │ nunca          │ nunca                      │
  ├─────────┼─────────────────┼────────────────┼────────────────────────────┤
  │ B       │ despesas        │ despesas       │ despesas marcadas          │
  │         │ marcadas        │ marcadas       │                            │
  ├─────────┼─────────────────┼────────────────┼────────────────────────────┤
  │ C1      │ dias cobertos   │ dias cobertos  │ sempre                     │
  ├─────────┼─────────────────┼────────────────┼────────────────────────────┤
  │ C2      │ dias cobertos   │ dias cobertos  │ nunca                      │
  ├─────────┼─────────────────┼────────────────┼────────────────────────────┤
  │         │ despesas ou     │ despesas ou    │ depende de "hotel" estar   │
  │ D       │ dias com termo  │ dias com termo │ na lista (se estiver, vale │
  │         │                 │                │  o mesmo que C1)           │
  ├─────────┼─────────────────┼────────────────┼────────────────────────────┤
  │ E       │ período inteiro │ período        │ período inteiro            │
  │         │                 │ inteiro        │                            │
  └─────────┴─────────────────┴────────────────┴────────────────────────────┘

  Coworking (d-005) fica de fora em todas as leituras: não tem limite, porque
  está fora da política (item 9).

  ---

  Números (ponto 2)

  Premissa: d-011 normalizada para cerca de 33,33, conforme a AMB-014, ainda
  pendente.

  Item: d-010 hospedagem 480,00, com NF, 1 diária
  A: 250,00 parcial
  B (exemplo sem o campo): 250,00 parcial
  C1: 375,00 parcial
  C2: 250,00 parcial
  D: 375,00 se "hotel" for termo; senão 250,00
  E: 375,00 parcial
  ────────────────────────────────────────
  Item: d-010, 2 diárias
  A: 480,00 integral (limite 500)
  B (exemplo sem o campo): 480,00 integral
  C1: 480,00 integral (limite 750)
  C2: 480,00 integral
  D: 480,00 integral
  E: 480,00 integral
  ────────────────────────────────────────
  Item: d-011 alimentação ≈33,33 (15/07, "Cafe da manha hotel")
  A: 33,33 integral, limite 60
  B (exemplo sem o campo): idem, limite 60
  C1: 33,33 integral; limite 90 se 15/07 estiver coberto (ver ponto 3)
  C2: idem C1
  D: 33,33 integral, limite 90 ("hotel")
  E: 33,33 integral, limite 90
  ────────────────────────────────────────
  Item: d-013 hospedagem 690,00, sem NF
  A: 0,00 recusado
  B (exemplo sem o campo): 0,00 recusado
  C1: 0,00 recusado
  C2: 0,00 recusado
  D: 0,00 recusado
  E: 0,00 recusado
  ────────────────────────────────────────
  Item: d-003 transporte 100,00, sem NF (06/07, "aeroporto")
  A: 80,00 parcial
  B (exemplo sem o campo): 80,00 parcial
  C1: 80,00 parcial (06/07 sem hospedagem)
  C2: 80,00
  D: 100,00 integral (limite 120)
  E: 100,00 integral
  ────────────────────────────────────────
  Item: d-004 transporte 100,01, sem NF
  A: 0,00 recusado
  B (exemplo sem o campo): 0,00
  C1: 0,00
  C2: 0,00
  D: 0,00
  E: 0,00
  ────────────────────────────────────────
  Item: d-001 alimentação 72,50
  A: 60,00 parcial
  B (exemplo sem o campo): 60,00
  C1: 60,00
  C2: 60,00
  D: 60,00
  E: 72,50 integral
  ────────────────────────────────────────
  Item: d-014 alimentação 61,00
  A: 60,00 parcial
  B (exemplo sem o campo): 60,00
  C1: 60,00
  C2: 60,00
  D: 60,00
  E: 61,00 integral

  O que a tabela mostra:
  - d-013 rende 0,00 em todas as leituras. A RN-004 recusa o item antes do
    limite, então a viagem não altera nada nele. O único efeito possível de
    d-013 está nas outras despesas dos dias que ele cobre (ver ponto 4).
  - d-011 rende o mesmo valor em todas as leituras. O que muda é só o limite
    aplicado, 60 ou 90. Um aceite sobre o estado de viagem de d-011 só pode ser
    verificado se a saída (§4) mostrar o limite aplicado ou o estado de viagem.
    Pelo valor reembolsado, ninguém consegue verificar.
  - Diferença em relação à leitura A, no total do exemplo:
    - C1: +125,00 com 1 diária; 0,00 com 2 diárias.
    - C2: 0,00.
    - D: +20,00 (d-003), mais 125,00 se "hotel" for termo e a contagem for de 1
      diária.
    - E: +33,50 (d-001 +12,50, d-014 +1,00, d-003 +20,00), mais 125,00 com 1
      diária.

  ---

  Interação com a AMB-009 (ponto 3)

  - A e E: nenhuma interação com o calendário de viagem. A AMB-009 continua
    definindo só o limite da hospedagem: 250 × n na leitura A, 375 × n na
    leitura E.
  - B: nenhuma interação. O campo diz se há viagem, e a AMB-009 diz o limite.
  - C: a AMB-009 passa a ter duas funções: o limite da hospedagem e quais dias
    são de viagem. Decidir a AMB-007 pela leitura C sem a AMB-009 deixa o
    calendário de viagem indefinido. Surge também uma pergunta nova, sobre o dia
    de saída:
    - d-010 (14/07) com 2 diárias cobre as noites de 14 e 15. O dia 16, de
      saída, é viagem?
    - d-010 com 1 diária cobre a noite de 14. O dia 15, de saída, é viagem? É
      exatamente o dia de d-011, o café da manhã no hotel.
    - Ou seja: com 1 diária, o estado de viagem de d-011 depende da regra do dia
      de saída. Com 2 diárias, não depende.
  - D: se a AMB-009 contar as diárias pelo texto ("2 diarias", "3 noites"), a
    dependência de texto livre entra também no limite da hospedagem. Isso vale
    igualmente para a leitura C.

  ---

  Hospedagem recusada caracteriza viagem? (ponto 4)

  A pergunta só existe nas leituras C, D e E, que inferem a viagem a partir de
  despesas. Nas leituras A e B ela não se coloca.

  Argumento
  (i) Caracteriza: A falta de NF afeta o reembolso da hospedagem, não o fato da
  estadia.
  (ii) Não caracteriza: Sem comprovante, a estadia não está demonstrada.
  ────────────────────────────────────────
  d-013
  (i) Caracteriza: 0,00 nas duas
  (ii) Não caracteriza: 0,00 nas duas
  ────────────────────────────────────────
  Hipotético: alimentação de 80,00 em 23/07
  (i) Caracteriza: 80,00 integral (limite 90)
  (ii) Não caracteriza: 60,00 parcial
  ────────────────────────────────────────
  Efeito no exemplo
  (i) Caracteriza: nenhum: não há outra despesa entre 22 e 24/07
  (ii) Não caracteriza: nenhum
  ────────────────────────────────────────
  Coerência (fato, sem recomendação)
  (i) Caracteriza: A justificativa da AMB-005 diz que, sem comprovante, "a
  empresa não tem como atestar que a transação ocorreu". Usar essa mesma
  hospedagem como prova de viagem contradiz essa premissa.
  (ii) Não caracteriza: A ausência de NF num item reduz o reembolso de outros
  itens já comprovados. É uma penalidade em cascata, que a AMB-005 não previu.

  Na leitura C, se a AMB-009 contar 1 diária para d-013, a pergunta cobre só
  22/07, e talvez 23/07, conforme a regra do dia de saída.

  Na leitura E, a escolha (ii) faz o período de julho continuar em viagem por
  causa de d-010, então d-013 não muda nada. A escolha só importaria num período
  em que a única hospedagem fosse recusada.

  ---

  Arredondamento do acréscimo (ponto 5)

  Hoje o arredondamento não aparece: 60 × 1,5 = 90,00, 80 × 1,5 = 120,00 e 250 ×
  1,5 = 375,00, todos exatos em centavos.

  Na hospedagem, ampliar e depois multiplicar pelas diárias dá o mesmo resultado
  que multiplicar e depois ampliar: 375 × n = (250 × n) × 1,5, exato para
  qualquer n inteiro.

  A questão só aparece se um limite nominal mudar para um valor cujo acréscimo
  de 50% não caia em centavos inteiros (por exemplo, 60,01 × 1,5 = 90,015). As
  opções são:
  - declarar agora a regra de arredondamento para esse caso, que é uma
    salvaguarda;
  - vinculá-la à AMB-014 (precisão monetária);
  - declarar que hoje todos os limites ampliados são exatos e só tratar o
    assunto se a política mudar.

  O acréscimo de 50% incide só sobre limites de categoria. O limiar de NF de R$
  100,00 não é ampliado, conforme a AMB-006.

  ---

  Agregação e circularidade (ponto 6)

  Agregação. A RN-001 proíbe que outra despesa altere o resultado "por meio de
  agregação de valores".

  Leitura: A, B
  Uma despesa depende de outra?: não
  Fere a letra da RN-001?: não
  Fere a justificativa da AMB-001?: não
  ────────────────────────────────────────
  Leitura: C
  Uma despesa depende de outra?: sim, depende da existência da hospedagem, não
  do
  valor dela
  Fere a letra da RN-001?: não: não soma valores
  Fere a justificativa da AMB-001?: Sim, em parte. A AMB-001 justificou a
  decisão
  dizendo que o colaborador pode "saber se uma despesa será paga no momento em
  que a faz". Na leitura C, o reembolso de um almoço depende de o hotel ser
  lançado no mesmo lote.
  ────────────────────────────────────────
  Leitura: D (só o texto da própria despesa)
  Uma despesa depende de outra?: não
  Fere a letra da RN-001?: não
  Fere a justificativa da AMB-001?: não
  ────────────────────────────────────────
  Leitura: D (texto de outra despesa do dia)
  Uma despesa depende de outra?: sim
  Fere a letra da RN-001?: não
  Fere a justificativa da AMB-001?: sim, como na leitura C
  ────────────────────────────────────────
  Leitura: E
  Uma despesa depende de outra?: sim, e com alcance do período inteiro
  Fere a letra da RN-001?: não, mas é o que mais se aproxima de agregação
  Fere a justificativa da AMB-001?: sim: uma única despesa muda o limite de
  todas
  as outras do período

  Nas leituras C e E a dependência atravessa o período de competência. Um hotel
  lançado em junho que cubra 01/07, ou lançado com atraso (item 7, ambiguidade
  ainda não registrada), muda o estado de viagem das despesas de julho. O
  resultado continua independente da ordem dos lançamentos.

  Circularidade. Nenhuma leitura cria ciclo, graças à AMB-006 (a) e à RN-004:
  - a recusa de uma hospedagem depende só do valor lançado e da NF, não do
    limite nem da viagem;
  - portanto, "hospedagem válida → viagem → limite das outras despesas" é uma
    cadeia linear;
  - na leitura C1, a hospedagem amplia o próprio limite. Isso não é ciclo: a
    viagem nasce da existência da hospedagem, não do reembolso dela.

  O ciclo voltaria em três situações, que valem como restrições a registrar:
  1. Se o critério fosse "hospedagem reembolsada" (reembolso > 0) em vez de
     "hospedagem não recusada". O reembolso depende do limite, e o limite
     depende da viagem.
  2. Se uma regra futura, como duplicata (AMB-011) ou competência, decidir a
     validade de uma hospedagem com base no estado de viagem.
  3. Na leitura D, se um termo levar a viagem a uma despesa e essa viagem mudar
     a exigência de NF dela. Isso não acontece hoje, porque a NF não depende da
     viagem (AMB-006).

  ---

  Texto livre e auditabilidade (ponto 7)

  - Dependem de texto livre: a leitura D, sempre. A leitura C, só se a AMB-009
    contar diárias pela descrição.
  - Não dependem: A, B, E, e C com a AMB-009 estruturada.

  Custo do texto livre (leitura D):
  - A regra só pode ser verificada se a spec trouxer a lista de termos completa.
    Qualquer termo fora da lista é decisão implícita.
  - A grafia muda o resultado. A entrada vem sem acento ("Cafe da manha",
    "Almoco"). d-014 mostra que nem a categoria vem padronizada. "aeroporto",
    "aero" e "GRU" dariam resultados diferentes.
  - Há falso positivo: "Almoço com cliente no hotel" na própria cidade ganharia
    +50%.
  - O colaborador pode manipular a regra: escrever "aeroporto" na descrição vale
    20,00 a mais em d-003.
  - O auditor consegue verificar se o termo aparece no texto, mas não se a
    viagem aconteceu. A regra troca um fato (houve viagem) por um sinal
    (apareceu a palavra).

  ---

  Custo de cada escolha

  Leitura A: fora de escopo
  - O item 6 deixa de ser cumprido pelo motor, sempre em desfavor do colaborador
    em viagem. No exemplo, d-010 com 1 diária perde 125,00 em relação a
    qualquer leitura que amplie a hospedagem.
  - Os viajantes voltam a depender de ajuste manual.
  - A spec precisa assumir isso abertamente na §3 e na §10. Uma regra que a
    política tem e o motor ignora sem dizer é ambiguidade não resolvida.

  Leitura B: campo na entrada
  - Altera um formato que o desafio declara fixo ("Respeite esse formato").
    Mesmo com um campo opcional, é uma extensão que precisa estar justificada.
  - Abre duas perguntas: em que nível fica o campo (despesa ou período) e quem o
    preenche. Se for o próprio colaborador, a viagem é autodeclarada.
  - No exemplo, que não tem o campo, os números são idênticos aos da leitura A.

  Leitura C: hospedagem
  - Fica acoplada à AMB-009, que passa a definir o calendário de viagem, e cria
    a questão do dia de saída.
  - Obriga a escolher entre C1, que torna o limite de 250,00 do item 3 letra
    morta, e C2, que não tem base na política.
  - Não detecta viagem sem pernoite: d-003, "Corrida aeroporto", fica em 80,00.
  - Enfraquece a justificativa da AMB-001: o reembolso de uma despesa depende de
    outra existir no lote.
  - Exige resolver o ponto 4.

  Leitura D: texto livre
  - Todos os custos de auditabilidade listados acima: lista de termos, grafia,
    falso positivo, manipulação.
  - Uma despesa pode ganhar +50% sem que nenhuma regra estruturada sustente o
    ganho.

  Leitura E: período
  - Paga a maior diferença no exemplo: d-001 e d-014 deixam de ser parciais, e
    d-001 tinha sido o caso de aceite da RN-001 e da RN-002.
  - Uma noite de hotel amplia em 50% o limite do mês inteiro, inclusive de
    despesas sem relação com a viagem, como d-012, o almoço de sábado em
    plantão.
  - É o que mais se aproxima da agregação que a AMB-001 rejeitou.
  - Exige resolver o ponto 4, ainda que com menos efeito.

  Para todas as leituras menos a A: a premissa global da spec ("assumem despesas
  fora de viagem") tem de ser revista. Os aceites já escritos, como d-001,
  d-003, d-014 e a alimentação de 500,00, passam a declarar o estado de viagem,
  como a própria premissa prevê.

  Não alterei nenhum arquivo. Aguardo a sua decisão.

> #7 decidido — leitura A: caracterização de viagem fora de escopo, declarada.

  AMB-007 — Caracterização de "em viagem" e ampliação de limites

  Texto do RH, item 6: "Colaborador em viagem tem limites ampliados em 50%."

  O que não está claro: a política não define o que caracteriza estar em
  viagem, não diz se o estado se aplica ao colaborador no período, ao dia ou à
  despesa, e não informa quais limites são ampliados. A entrada não possui
  nenhum campo que registre o estado de viagem — os únicos dados por despesa
  são data, categoria, descrição, fornecedor, valor e presença de nota fiscal.
  As leituras consideradas foram: (a) declarar a caracterização fora de escopo;
  (b) estender a entrada com um campo explícito; (c) inferir viagem a partir de
  lançamentos de hospedagem; (d) inferir a partir de termos na descrição ou no
  fornecedor; (e) considerar todo o período em viagem se houver qualquer
  hospedagem nele.

  Decisão: adotada a leitura (a). A caracterização de estar em viagem está
  fora do escopo deste sistema. O motor aplica sempre os limites nominais de
  cada categoria e o item 6 da política não é implementado. A premissa global
  declarada nas seções 5 e 7 deixa de ser provisória e passa a ser regra
  permanente: todas as despesas são avaliadas com os limites nominais.

  Justificativa: estar em viagem é um fato administrativo — alguém autoriza o
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

  Custo aceito: o item 6 da política não é cumprido pelo motor, e o erro é
  sempre na mesma direção — contra o colaborador em viagem, nunca contra a
  empresa. No arquivo de exemplo, d-010 (hospedagem de 480,00 com nota)
  recebe 250,00 em vez dos 375,00 que receberia se a diária fosse ampliada,
  caso o lançamento conte uma diária; a diferença é de 125,00. Despesas de
  alimentação e transporte de colaboradores efetivamente em viagem são
  limitadas a 60,00 e 80,00 em vez de 90,00 e 120,00. A regularização desses
  casos permanece com o financeiro, por conferência manual. Aceito porque a
  alternativa é o motor conceder ampliação com base em indício que ele não
  consegue comprovar, produzindo resultado que o auditor não tem como
  verificar e que o colaborador pode influenciar.

  Consequências verificáveis:
  - todos os limites são os nominais: alimentação 60,00; transporte urbano
    80,00; hospedagem 250,00 por diária
  - d-010 (hospedagem, 480,00, com NF) → limite 250,00 por diária, sem
    ampliação; resultado final condicionado à AMB-009
  - d-011 (alimentação, 15/07, no mesmo fornecedor de d-010) → limite 60,00;
    a proximidade com um lançamento de hospedagem não altera o limite
  - d-003 (transporte urbano, 100,00, "Corrida aeroporto") → limite 80,00; a
    descrição não altera o limite
  - d-013 (hospedagem, 690,00, sem NF) → recusado pela RN-004; a ausência de
    efeito sobre outras despesas é consequência desta decisão, não da recusa
  - todos os aceites e casos de borda já registrados permanecem válidos sem
    alteração

  Efeitos sobre a spec:
  1. A premissa global das seções 5 e 7 deixa de ser provisória. Reescreva-a
     para refletir que a ausência de ampliação é regra permanente, não hipótese
     de trabalho, e remova a frase que prevê revisão futura dos casos afetados.
  2. Remova a AMB-007 dos campos "Depende de" das RN-001, RN-002 e RN-003 —
     ela deixa de ser pendência e passa a ser decisão.
  3. Acrescente à seção 3 (Fora de escopo): "Não determina se o colaborador
     está em viagem, e portanto não aplica a ampliação de 50% prevista no item
     6 da política. Ver AMB-007."
  4. Na seção 10, substitua o item "Hospedagem recusada como indício de
     viagem", que deixa de existir, por: "Ampliação de limites em viagem
     (origem: AMB-007). O item 6 da política não é implementado porque a
     entrada não registra o estado de viagem. Para implementá-lo seria
     necessário um campo explícito na entrada, informado pelo processo que
     autoriza a viagem — não pelo colaborador que lança a despesa. Enquanto
     isso não existir, colaboradores em viagem recebem menos do que a política
     lhes garante, e a regularização cabe ao financeiro."
  5. Se a §8 (ordem de aplicação) fizer qualquer referência à ampliação por
     viagem, remova.

  Não registre nada sobre arredondamento do acréscimo de 50%: sem ampliação,
  a questão não existe.

  Antes de salvar, varra a spec inteira procurando qualquer outra referência à
  AMB-007 ou ao estado de viagem que tenha se tornado obsoleta, e me liste o
  que encontrar.

  Esta mudança altera registros já salvos (a premissa global e os campos
  "Depende de" de três regras), então crie também a entrada D-002 no
  DECISIONS.md.

  Me mostre o diff antes de salvar.

● Vou partir da spec já salva e varrer as referências a viagem e à AMB-007.

● Bash(D="c:/Users/dev/SDD-Desafio/sdd-desafio/specs/001-motor-reembolso";
      S="C:/Users/dev/AppData/Local/Temp/claude/c--Users-dev-SDD-Desafio/0308efd
      8-0138-44dd-844f-6…)
  ⎿  (No output)

● Bash(S="C:/Users/dev/AppData/Local/Temp/claude/c--Users-dev-SDD-Desafio/0308ef
      d8-0138-44dd-844f-6d25461c434d/scratchpad"; sed -n 46,56p
      "$S/spec.proposta.md"; echo -…)
  ⎿  (No output)

● Bash(S="C:/Users/dev/AppData/Local/Temp/claude/c--Users-dev-SDD-Desafio/0308ef
      d8-0138-44dd-844f-6d25461c434d/scratchpad"; mkdir -p "$S/scripts"; cat >
      "$S/scripts/am…)
  ⎿  Error: Exit code 2
     /usr/bin/bash: -c: line 1: unexpected EOF while looking for matching `''

  Made 1 scratchpad edit +165 (ctrl+o to expand)

● Bash(S="C:/Users/dev/AppData/Local/Temp/claude/c--Users-dev-SDD-Desafio/0308ef
      d8-0138-44dd-844f-6d25461c434d/scratchpad";
      D="c:/Users/dev/SDD-Desafio/sdd-desafio/spe…)
  ⎿  (No output)

● Bash(for c in py python3 node; do command -v $c; done; ls
      /c/Users/dev/AppData/Local/Programs/Python 2>/dev/null)
  ⎿  Error: Exit code 2
     /c/Users/dev/AppData/Local/Microsoft/WindowsApps/python3
     /c/nvm4w/nodejs/node

● Não há Python instalado (só o atalho da Microsoft Store). Vou aplicar as
  mesmas edições com a ferramenta de edição.

  Made 8 scratchpad edits +121 -26, read 2 files (ctrl+o to expand)

● Bash(S="C:/Users/dev/AppData/Local/Temp/claude/c--Users-dev-SDD-Desafio/0308ef
      d8-0138-44dd-844f-6d25461c434d/scratchpad";
      D="c:/Users/dev/SDD-Desafio/sdd-desafio/spe…)
  ⎿  (No output)

● Preparei as alterações da AMB-007 em cópias, no scratchpad. O diff completo
  está acima, e nenhum arquivo do projeto foi alterado.

  O que já está no diff

  1. Premissa global (§5 e §7). Deixou de ser provisória e virou "Limites
     nominais, sem ampliação por viagem". Saiu a frase que previa revisar os
     casos depois.
  2. "Depende de" das RN-001, RN-002 e RN-003. Tirei a AMB-007. Onde sobrou uma
     pendência só, "pendentes" passou a "pendente".
  3. §3. Entrou o item que você ditou. Os dois marcadores do modelo (Não faz
     <...>) foram substituídos por ele.
  4. §10. Troquei o item, com o texto que você deu.
  5. §8. Não fala de viagem, então não mudou nada.
  6. AMB-007 está registrada depois da AMB-006, com o seu texto. Acrescentei "Em
     aberto: §10" e "Regra afetada: RN-001, RN-002; §3".
  7. D-002 entrou no DECISIONS.md, acima da D-001.

  Não registrei nada sobre o arredondamento do acréscimo de 50%, como você
  pediu.

  Varredura: o que ficou desatualizado (não alterei)

  #: F1
  Onde: RN-001, Regra (l. 63–64)
  Texto atual: "A eventual ampliação de limite por viagem é tratada na AMB-007 e

  opera sobre o valor do limite, não sobre a soma das despesas."
  Problema: É o texto da regra, não um campo de dependência, e ainda prevê uma
  ampliação que não existe mais.
  ────────────────────────────────────────
  #: F2
  Onde: RN-002, Regra (l. 86)
  Texto atual: "O limite aplicável é o da categoria conforme a RN-001, com a
  eventual ampliação por viagem da AMB-007."
  Problema: Mesmo problema: a regra diz que o limite pode ser ampliado.
  ────────────────────────────────────────
  #: F3
  Onde: AMB-005, Justificativa (l. 296)
  Texto atual: "esvaziando a regra em alimentação e em transporte fora de
  viagem"
  Problema: Agora todo transporte é "fora de viagem". A ressalva faz supor que
  existe transporte em viagem com outro limite.
  ────────────────────────────────────────
  #: F4
  Onde: AMB-006, Justificativa (l. 339–342)
  Texto atual: "o limite de alimentação é no máximo R$ 90 e o de transporte fora

  de viagem é R$ 80… exigir mais documentação de quem viaja, já que o limite
  ampliado passaria a ultrapassar R$ 100"
  Problema: O "R$ 90" era o limite de alimentação em viagem. O "efeito
  invertido"
  sobre quem viaja só existe com ampliação.

  Tenho uma sugestão de redação para cada um. Não apliquei nenhuma.
  - F1: apagar a frase. A frase seguinte ("Não existe teto diário agregado por
    categoria") continua fazendo sentido sem ela.
  - F2: "O limite aplicável é o limite nominal da categoria conforme a RN-001;
    não há ampliação por viagem (AMB-007)." Acrescentar AMB-007 à "Origem" da
    RN-001 e da RN-002, já que a AMB-007 agora decide o valor do limite.
  - F3: "esvaziando a regra em alimentação e em transporte urbano".

  F1 e F2 estão no texto das próprias regras, então afirmam algo falso sobre o
  sistema de hoje. F3 e F4 estão em justificativas, e o argumento delas continua
  valendo sem a ampliação.

  F4 tem dois caminhos:
  - (i) Reescrever. Ficaria assim: "o limite de alimentação é R$ 60 e o de
    transporte é R$ 80". O argumento do efeito invertido sairia.
  - (ii) Manter. Seria registrado como argumento da época, que continua valendo
    se o item 6 for implementado no futuro (§10).

  Pelo princípio que você fixou na D-001 ("a spec descreve o estado atual"), o
  caminho (i) é o coerente. Mas a escolha é sua.

  Os itens que você aprovar entram na D-002, com as linhas "de → para". Depois
  disso eu salvo.

> Corrija os quatro: F1, F2, F3 e F4.

  F1 e F2 são os mais graves — estão no texto das próprias regras e afirmam
  que o limite pode ser ampliado por viagem, o que a AMB-007 acabou de negar.
  Alguém implementando a partir da RN-001 e da RN-002 como estão hoje
  implementaria a ampliação.

  F1: apague a frase, como você sugeriu.

  F2: aplique sua redação — "O limite aplicável é o limite nominal da categoria
  conforme a RN-001; não há ampliação por viagem (AMB-007)." E sim, acrescente
  AMB-007 à Origem da RN-001 e da RN-002: ela passou a determinar o valor do
  limite aplicável.

  F3: aplique sua redação — "esvaziando a regra em alimentação e em transporte
  urbano".

  F4: caminho (i), reescrever. Pelo princípio da D-001, a spec descreve o
  estado atual, e "R$ 90" é um número que não existe mais em lugar nenhum do
  sistema.

  Ao reescrever o F4, confira se o argumento da AMB-006 continua se
  sustentando.
  Ele usava duas razões contra a leitura (b): que ela esvaziaria a regra, e o
  efeito invertido sobre quem viaja. A segunda deixa de existir sem ampliação.
  A primeira fica mais forte, porque sem ampliação nenhum teto de alimentação
  ou transporte se aproxima de R$ 100. Reescreva mantendo a primeira razão
  reforçada e removendo a segunda, e me mostre como ficou o parágrafo inteiro
  antes de salvar — quero conferir se a justificativa continua coerente.

  As quatro correções entram na D-002 com as linhas "de → para". Ajuste
  também o "O que isso invalidou": além do que já está lá, registre que o
  argumento do efeito invertido sobre quem viaja deixou de se aplicar na
  justificativa da AMB-006.

  Made 5 scratchpad edits +50 -28, read 1 file (ctrl+o to expand)

● Bash(S="C:/Users/dev/AppData/Local/Temp/claude/c--Users-dev-SDD-Desafio/0308ef
      d8-0138-44dd-844f-6d25461c434d/scratchpad"; grep -n -i -E "eventual|R.
      90|viaja|fora de…)
  ⎿  (No output)

  Made 1 scratchpad edit +3 -3 (ctrl+o to expand)

● Apliquei as quatro correções nas cópias do scratchpad. Nenhum arquivo do
  projeto foi alterado.

  Justificativa da AMB-006 reescrita (parágrafo inteiro)

  ▎ Justificativa: a nota fiscal comprova que a transação existiu e custou o
  ▎ valor declarado — o objeto da comprovação é a despesa, não a parcela que a
  ▎ empresa decide pagar. A leitura (b) esvaziaria a regra: como os limites são
  ▎ sempre os nominais (AMB-007) — alimentação R$ 60 e transporte urbano R$ 80,
  ▎ ambos abaixo de R$ 100 —, nenhuma despesa dessas categorias poderia exigir
  ▎ nota, qualquer que fosse o valor lançado, e um jantar de R$ 500 sem
  ▎ comprovante renderia R$ 60. O item 5 da política passaria a valer apenas
  ▎ para hospedagem, a única categoria cujo limite supera R$ 100 — e nela a
  ▎ leitura (b) produz o mesmo resultado que a (a), porque o limite de
  ▎ hospedagem nunca é inferior a R$ 250. A leitura (c) reintroduziria
  ▎ agregação, que a AMB-001 rejeitou, abriria a questão nova de qual é o grupo,
  ▎ e alteraria uma consequência já registrada na AMB-004.

  O parágrafo seguinte, sobre a coerência com a AMB-002, ficou sem mudança.

  O argumento continua de pé? Conferi, e sim.
  - A primeira razão ficou mais forte. Antes ela dependia de "no máximo R$ 90".
    Agora vale para qualquer valor, porque 60 e 80 estão abaixo de 100 sem
    exceção.
  - A frase nova sobre hospedagem completa o argumento. Nas outras duas
    categorias, a leitura (b) elimina a regra. Na hospedagem, ela dá o mesmo
    resultado que a (a). Então (b) não traz nenhum benefício em nenhuma
    categoria, só a perda.
  - A segunda razão saiu inteira. É o "efeito invertido de exigir mais
    documentação de quem viaja".
  - Nada no resto da AMB-006 entra em choque com o texto novo:
    - a consequência de d-013 já dizia "idêntico nas leituras (a) e (b)… nunca
      inferior a R$ 250";
    - o Custo aceito usa o teto de transporte de R$ 80;
    - o parágrafo "Sem circularidade" não fala de viagem.
  - Uma ressalva que já existia: "nunca inferior a R$ 250" supõe pelo menos uma
    diária por lançamento. É a mesma premissa que a spec já usa em outros
    pontos, e ela continua dependendo da AMB-009.

  As outras três correções

  - F1. Apaguei a frase da RN-001 e reorganizei as quebras de linha do trecho.
  - F2. Apliquei a sua redação na RN-002.
  - Origem. RN-001 e RN-002 agora dizem "decisões AMB-00x e AMB-007".
  - F3. A AMB-005 passou a dizer "esvaziando a regra em alimentação e em
    transporte urbano".

  D-002

  - Tem agora as quatro linhas "de → para" de F1 a F4, inclusive a mudança de
    Origem.
  - "Por quê" passou a citar quem implementa a partir das RN-001 e RN-002.
  - "O que isso invalidou" registra que o argumento do efeito invertido deixou
    de se aplicar na AMB-006. Diz também que a decisão se sustenta pela razão
    que sobrou, agora reforçada.

  Varredura de novo

  Procurei "eventual", "R$ 90", "viaja" e "fora de viagem" na spec. Não sobrou
  nenhuma ocorrência. Os termos "viagem" que ainda aparecem estão só na AMB-007,
  na §3, na premissa e na §10, todos coerentes com a decisão.

  Se aprovar, salvo as duas cópias sobre spec.md e DECISIONS.md.

> Aprovado. A reescrita ficou mais forte que o original — o argumento de que a
  leitura (b) ou esvazia a regra ou não muda nada, sem categoria onde traga
  benefício, fecha melhor do que o texto anterior. Pode salvar as duas cópias.

● Bash(S="C:/Users/dev/AppData/Local/Temp/claude/c--Users-dev-SDD-Desafio/0308ef
      d8-0138-44dd-844f-6d25461c434d/scratchpad";
      D="c:/Users/dev/SDD-Desafio/sdd-desafio/spe…)
  ⎿  Updated DECISIONS.md (+54 -0)
      10  
      11  ---
      12  
      13 +## D-002 — Premissa de viagem tornada permanente após a AMB-007
         + · `2026-10-18`                                                
      14 +                                                               
      15 +**Gatilho:** a decisão da AMB-007 (leitura a: caracterização de
         + viagem fora                                                   
      16 +de escopo) tornou obsoletos registros já salvos que tratavam a 
         +viagem como                                                    
      17 +pendência: a premissa global das seções 5 e 7 e os campos "Depe
         +nde de" das                                                    
      18 +RN-001, RN-002 e RN-003.                                       
      19 +                                                               
      20 +**O que mudou na spec:**                                       
      21 +- §5 e §7, premissa global: de "assumem despesas fora de viagem
         +… quando                                                       
      22 +  ela for decidida, os casos afetados serão revistos" para regr
         +a permanente                                                   
      23 +  de limites nominais, sem ampliação por viagem e sem previsão 
         +de revisão.                                                    
      24 +- RN-001, RN-002 e RN-003, "Depende de": a AMB-007 foi removida
         +.                                                              
      25 +- §3: novo item — o sistema não determina se o colaborador está
         + em viagem e                                                   
      26 +  não aplica a ampliação de 50% do item 6. Os marcadores de mod
         +elo da seção                                                   
      27 +  foram substituídos por esse item.                            
      28 +- §10: o item "Hospedagem recusada como indício de viagem" deix
         +ou de existir                                                  
      29 +  e foi substituído por "Ampliação de limites em viagem".      
      30 +- RN-001, Regra: removida a frase "A eventual ampliação de limi
         +te por viagem                                                  
      31 +  é tratada na AMB-007 e opera sobre o valor do limite, não sob
         +re a soma das                                                  
      32 +  despesas." Origem: de "decisão AMB-001" para "decisões AMB-00
         +1 e AMB-007".                                                  
      33 +- RN-002, Regra: de "O limite aplicável é o da categoria confor
         +me a RN-001,                                                   
      34 +  com a eventual ampliação por viagem da AMB-007" para "O limit
         +e aplicável é                                                  
      35 +  o limite nominal da categoria conforme a RN-001; não há ampli
         +ação por                                                       
      36 +  viagem (AMB-007)". Origem: de "decisão AMB-002" para "decisõe
         +s AMB-002 e                                                    
      37 +  AMB-007".                                                    
      38 +- AMB-005, Justificativa: de "esvaziando a regra em alimentação
         + e em                                                          
      39 +  transporte fora de viagem" para "esvaziando a regra em alimen
         +tação e em                                                     
      40 +  transporte urbano".                                          
      41 +- AMB-006, Justificativa: de "o limite de alimentação é no máxi
         +mo R$ 90 e o                                                   
      42 +  de transporte fora de viagem é R$ 80… Produziria ainda o efei
         +to invertido                                                   
      43 +  de exigir mais documentação de quem viaja" para "os limites s
         +ão sempre os                                                   
      44 +  nominais (AMB-007) — alimentação R$ 60 e transporte urbano R$
         + 80, ambos                                                     
      45 +  abaixo de R$ 100… O item 5 da política passaria a valer apena
         +s para                                                         
      46 +  hospedagem", sem o argumento do efeito invertido.            
      47 +                                                               
      48 +**Por quê:** a spec descreve o estado atual do sistema. Uma pre
         +missa                                                          
      49 +declarada como provisória, dependências apontando para uma ambi
         +guidade já                                                     
     … 20 more lines (ctrl+o to expand)
  ⎿  Updated spec.md (+108 -44)
      24  <Lista explícita. Este bloco vale ponto: ele impede o agente de
           inventar feature
      25  e impede você de mover a trave depois.>
      26  
      27 -- Não faz `<...>`                                              
      28 -- Não faz `<...>`                                              
      27 +- Não determina se o colaborador está em viagem, e portanto não
         + aplica a                                                      
      28 +  ampliação de 50% prevista no item 6 da política. Ver AMB-007.
      29  
      30  ## 4. Entrada e saída
      31  
     ...
      47  
      48  Cada regra recebe um ID (`RN-001`, ...). As tasks vão referenci
          ar esses IDs.
      49  
      50 -> **Premissa:** Salvo indicação em contrário, os critérios de a
         -ceite e os casos de borda                                      
      51 -> deste documento assumem despesas fora de viagem, com os limit
         -es nominais da                                                 
      52 -> política. A caracterização de viagem e a ampliação de limites
         - são tratadas                                                  
      53 -> na AMB-007; quando ela for decidida, os casos afetados serão 
         -revistos e os                                                  
      54 -> que dependerem do estado de viagem passarão a declará-lo expl
         -icitamente.                                                    
      50 +> **Limites nominais, sem ampliação por viagem:** o sistema não
         + determina se o                                                
      51 +> colaborador está em viagem (AMB-007). Todas as despesas são a
         +valiadas com os                                                
      52 +> limites nominais da política — alimentação R$ 60,00, transpor
         +te urbano                                                      
      53 +> R$ 80,00, hospedagem R$ 250,00 por diária —, e os critérios d
         +e aceite e os                                                  
      54 +> casos de borda deste documento valem para qualquer despesa, s
         +em distinção                                                   
      55 +> de viagem.                                                   
      56  
      57  ### RN-001 — Limite de categoria aplicado por despesa individua
          l
      58  
     ...
      60  comparada, isoladamente, com o limite da sua categoria (aliment
          ação R$ 60,00;
      61  transporte urbano R$ 80,00; hospedagem R$ 250,00 por diária). N
          enhuma outra
      62  despesa altera o resultado da despesa avaliada por meio de agre
          gação de
      62 -valores: não há soma de despesas da mesma categoria no mesmo di
         -a. A eventual                                                  
      63 -ampliação de limite por viagem é tratada na AMB-007 e opera sob
         -re o valor do                                                  
      64 -limite, não sobre a soma das despesas. Não existe teto diário  
      65 -agregado por categoria. Em hospedagem, o limite é por diária: o
         - limite de um                                                  
      66 -lançamento de hospedagem é R$ 250,00 multiplicado pelo número d
         -e diárias que                                                  
      67 -ele cobre, número definido pela AMB-009 (pendente).            
      68 -**Origem:** política do RH, itens 1, 2 e 3; decisão AMB-001    
      69 -**Depende de:** RN-002 (reembolso parcial, decidido na AMB-002)
         -; AMB-007                                                      
      70 -(ampliação de limite em viagem) e AMB-009 (número de diárias em
         - lançamento de                                                 
      71 -hospedagem), pendentes.                                        
     … 169 more lines (ctrl+o to expand)

● Salvei os dois arquivos e conferi byte a byte: spec.md
  (specs/001-motor-reembolso/spec.md) e DECISIONS.md
  (specs/001-motor-reembolso/DECISIONS.md) estão idênticos às versões que você
  aprovou.

  O DECISIONS.md agora tem duas entradas, a D-002 no alto e a D-001 logo abaixo.

  Não fiz commit. Se o lote vai até a AMB-007, há duas mudanças de spec
  registradas e ainda sem commit. Pela regra do CLAUDE.md, elas vão como
  docs(spec):.