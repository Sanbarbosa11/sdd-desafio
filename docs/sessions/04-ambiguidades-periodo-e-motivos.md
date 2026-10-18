
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

> Vamos para a AMB-009: número de diárias em um lançamento de hospedagem.

  Formato de sempre: texto do RH, as leituras possíveis com o argumento de
  negócio de cada uma, os números em d-010 e d-013, e o custo de cada escolha.
  Sem recomendação — eu decido.

  Inclua obrigatoriamente:
  - o efeito de cada leitura em d-010 (480,00, "Hotel Rio - 2 diarias", com NF)
    e d-013 (690,00, "Airbnb 3 noites", sem NF)
  - se alguma leitura depende de interpretar texto livre, e qual o custo disso
    em auditabilidade, considerando que as duas descrições usam palavras
    diferentes ("diarias" e "noites") e que um conjunto de dados diferente
    usaria uma terceira redação
  - se o resultado de d-013 muda em alguma leitura, dado que ele já é recusado
    pela RN-004
  - quais aceites e casos de borda hoje pendentes desta decisão passam a ter
    resultado, em cada leitura
  - se alguma leitura cria dependência entre despesas ou reintroduz agregação

● Bash(cd "c:/Users/dev/SDD-Desafio/sdd-desafio/specs/001-motor-reembolso" &&
      grep -n -i -E "AMB-009|diária|diárias" spec.md)
  ⎿  (No output)
● AMB-009: quantas diárias cabem num lançamento de hospedagem

  Texto do RH: item 3, "Hospedagem tem limite de R$ 250 por diária." A política
  põe o limite por diária, mas a entrada não traz o número de diárias. Cada
  lançamento tem uma única data, sem entrada nem saída, e nenhum campo com
  quantidade. Esse número só aparece no texto livre: d-010 diz "Hotel Rio - 2
  diarias" e d-013 diz "Airbnb 3 noites".

  O que já está decidido:
  - O limite é aplicado por lançamento. Em hospedagem, ele é 250,00 × o número
    de diárias (RN-001).
  - A AMB-001 registrou na decisão que "um lançamento pode cobrir múltiplas
    diárias numa única data".
  - Nunca há ampliação por viagem (AMB-007).
  - Uma hospedagem sem NF acima de 100,00 é recusada antes de chegar ao limite
    (RN-004, §8).

  ---

  As leituras

  Leitura A: cada lançamento vale uma diária

  O limite de qualquer lançamento de hospedagem é 250,00. Uma estadia de várias
  noites tem de ser lançada como uma despesa por diária.

  Argumentos de negócio a favor
  - Usa só campos estruturados. O resultado sai do valor e da categoria, sem ler
    nada.
  - O texto do item 3 ("por diária") vira uma regra de lançamento: uma diária,
    um lançamento. O financeiro confere item por item, como já faz.
  - O colaborador não tem como aumentar o próprio limite escrevendo um número na
    descrição.

  Leitura B: número extraído da descrição

  O número de diárias é lido da descricao: um número seguido de um termo de uma
  lista, como "diária(s)", "noite(s)" ou "pernoite(s)". Se não houver número,
  vale um valor padrão, que também precisa ser decidido. Pode ser 1 diária ou a
  recusa do item.

  Argumentos de negócio a favor
  - É a única leitura que usa a informação que existe na entrada. As duas
    hospedagens do exemplo dizem quantas noites cobrem.
  - Respeita a decisão da AMB-001 de que um lançamento pode cobrir várias
    diárias.
  - Paga a estadia real: d-010 custa 240,00 por noite, abaixo do limite.

  Leitura C: número inferido do valor

  n = valor ÷ 250, arredondado para cima.

  Argumento a favor: não lê texto e aceita estadias longas.

  Fato: nesta leitura o limite nunca corta nada. Qualquer valor cabe em ⌈valor ÷
  250⌉ × 250. O item 3 deixa de ter efeito. Registro esta leitura para a lista
  ficar completa, como a leitura (c) da AMB-002.

  Leitura D: campo explícito na entrada

  Seria criado um campo diarias, opcional, com um padrão quando ausente.

  Argumento a favor: é um dado estruturado, verificável contra a NF.

  Custo: é o mesmo que fez você rejeitar a leitura (b) da AMB-007. Estende um
  formato de entrada que o enunciado declara fixo. No exemplo o campo não
  existe, então o resultado depende inteiro do padrão escolhido.

  ---

  Números

  Item: d-010 480,00, com NF, "2 diarias"
  A (1 por lançamento): limite 250 → 250,00 parcial, excedente 230,00
  B (texto): n = 2, limite 500 → 480,00 integral
  C (valor): n = ⌈1,92⌉ = 2 → 480,00 integral
  D (campo; exemplo sem o campo, padrão = 1): 250,00 parcial
  ────────────────────────────────────────
  Item: d-013 690,00, sem NF, "3 noites"
  A (1 por lançamento): 0,00 recusado
  B (texto): 0,00 recusado
  C (valor): 0,00 recusado
  D (campo; exemplo sem o campo, padrão = 1): 0,00 recusado
  ────────────────────────────────────────
  Item: Hipotético: hospedagem de 400,00, com NF, "Hotel - 1 diaria"
  A (1 por lançamento): 250,00 parcial
  B (texto): 250,00 parcial
  C (valor): n = 2 → 400,00 integral
  D (campo; exemplo sem o campo, padrão = 1): 250,00
  ────────────────────────────────────────
  Item: Hipotético: o mesmo 400,00 descrito como "Hotel - 2 diarias"
  A (1 por lançamento): 250,00 parcial
  B (texto): 400,00 integral
  C (valor): 400,00 integral
  D (campo; exemplo sem o campo, padrão = 1): 250,00
  ────────────────────────────────────────
  Item: Hipotético: hospedagem de 480,00, com NF, "Hotel Copa Sul", sem número
  A (1 por lançamento): 250,00 parcial
  B (texto): depende do padrão: 250,00 (n = 1) ou 0,00 (recusa)
  C (valor): 480,00 integral
  D (campo; exemplo sem o campo, padrão = 1): 250,00

  d-013 não muda em nenhuma leitura. A RN-004 recusa o item e diz que ele "não é
  avaliado contra o limite" (§8). Então o número de diárias de d-013 nem chega
  a ser calculado. A única diferença seria quanto d-013 renderia se tivesse NF:
  250,00 na leitura A, 690,00 nas leituras B e C. Esse valor não aparece na
  saída, porque a AMB-005 rejeitou o campo de "valor potencial".

  ---

  Texto livre e auditabilidade

  Só a leitura B depende de texto livre.

  O custo:
  - O vocabulário não fecha. O exemplo já usa duas palavras para a mesma coisa
    ("diarias" e "noites"). Outro conjunto de dados poderia trazer:
    - "pernoites";
    - "2 nts";
    - "duas diárias", com o número por extenso;
    - "check-in 14/07, check-out 16/07";
    - "fim de semana", sem número nenhum.

    A lista de termos tem de estar escrita na spec. Qualquer redação fora dela
    cai no padrão, e o resultado muda sem que nada na despesa tenha mudado.
  - A grafia é um problema. A entrada vem sem acento ("diarias"), e a d-014
    mostra que nem a categoria chega padronizada. A regra precisaria decidir
    como tratar acento, maiúsculas e plural.
  - Pode haver mais de um número. Em "Hotel 3 estrelas - 2 diarias", só a regra
    "número seguido de termo da lista" escolhe o 2. Em "Pacote 2 pessoas 3
    noites", a regra pega o 3, mas o 2 é uma pista falsa.
  - O colaborador controla o limite. Cada diária escrita na descrição vale mais
    250,00 de limite, e o motor não confere nada. No hipotético acima, trocar
    "1" por "2" na descrição vale 150,00.
  - O que o auditor consegue conferir. Ele vê que o número está no texto. Para
    saber se a estadia teve aquele número de noites, precisa da NF, e o motor
    não lê NF.

  Este é o mesmo tipo de custo que pesou contra a leitura (d) da AMB-007: texto
  redigido pelo próprio colaborador decidindo o limite dele.

  ---

  Dependência entre despesas e agregação

  ┌─────────┬───────────────────────────────────────────┬────────────┐
  │ Leitura │        Dependência entre despesas?        │ Agregação? │
  ├─────────┼───────────────────────────────────────────┼────────────┤
  │ A       │ não                                       │ não        │
  ├─────────┼───────────────────────────────────────────┼────────────┤
  │ B       │ não: lê só a descrição da própria despesa │ não        │
  ├─────────┼───────────────────────────────────────────┼────────────┤
  │ C       │ não                                       │ não        │
  ├─────────┼───────────────────────────────────────────┼────────────┤
  │ D       │ não                                       │ não        │
  └─────────┴───────────────────────────────────────────┴────────────┘

  Nenhuma leitura cria dependência entre despesas por si só. A leitura A, porém,
  tem um efeito indireto que vale registrar:

  - Leitura A e duplicatas (AMB-011, pendente). Na leitura A, uma estadia de
    duas noites vira dois lançamentos de hospedagem, no mesmo fornecedor e
    muitas vezes com o mesmo valor. Se o colaborador lançar os dois na mesma
    data, o par fica igual a d-006 e d-007, que são exatamente o caso de
    duplicata do exemplo. A regra de duplicata teria de separar "duas diárias do
    mesmo hotel" de "o mesmo lançamento duas vezes", e só tem a data para isso.
    Isso não chega a ser agregação, mas a decisão de diárias passa a restringir
    a AMB-011.
  - Uma variante que seria agregação: deduzir as diárias juntando lançamentos
    consecutivos do mesmo fornecedor. Não está entre as leituras acima. Se for
    considerada, ela esbarra na AMB-001.

  ---

  Aceites e casos hoje pendentes desta decisão

  Onde: AMB-007, Consequências: d-010
  Texto atual: "resultado final condicionado à AMB-009"
  A: 250,00 parcial
  B: 480,00 integral
  C: 480,00 integral
  D: conforme o padrão
  ────────────────────────────────────────
  Onde: AMB-004, Consequências: d-010
  Texto atual: "segue para o limite" (sem valor)
  A: pode ganhar o valor: 250,00 parcial
  B: 480,00 integral
  C: 480,00 integral
  D: conforme o padrão
  ────────────────────────────────────────
  Onde: RN-001, Regra e "Depende de"; RN-002, "Depende de"
  Texto atual: "número definido pela AMB-009 (pendente)"
  A: passa a declarar: 1 por lançamento
  B: regra de extração + padrão
  C: fórmula
  D: campo + padrão
  ────────────────────────────────────────
  Onde: d-013, em RN-003, RN-004, AMB-005 e §7
  Texto atual: "qualquer que seja o número de diárias"
  A: continua verdadeiro
  B: continua verdadeiro
  C: continua verdadeiro
  D: continua verdadeiro
  ────────────────────────────────────────
  Onde: RN-004: hospedagem de 200,00 sem NF ("com NF seria 200,00, integral")
  Texto atual: —
  A: válido
  B: válido se n ≥ 1 for garantido
  C: válido
  D: válido se n ≥ 1

  Hoje não existe nenhum aceite de RN nem caso da §7 com o resultado de d-010.
  Em qualquer leitura, valeria acrescentar um.

  Três afirmações já salvas dependem desta decisão:

  1. O Custo aceito da AMB-007. Diz que "d-010 recebe 250,00 em vez dos 375,00…
     caso o lançamento conte uma diária; a diferença é de 125,00".
     - Na leitura A, a afirmação vale como está.
     - Nas leituras B e C, d-010 tem 2 diárias e o limite nominal de 500,00 já
       cobre os 480,00. Com a ampliação, o limite iria a 750,00, e o reembolso
       continuaria 480,00. A diferença seria zero, e o exemplo de custo da
       AMB-007 deixaria de mostrar custo nenhum no exemplo. Teria de ser
       reescrito com outro caso, por exemplo uma alimentação de 80,00 em viagem:
       recebe 60,00, contra 80,00 com a ampliação.
  2. A Justificativa e as Consequências da AMB-006. Dizem que "o limite de
     hospedagem nunca é inferior a R$ 250". Isso supõe n ≥ 1. É garantido nas
     leituras A e C. Nas leituras B e D, depende de a regra impedir n = 0, por
     exemplo com "0 diarias" no texto ou diarias: 0 no campo.
  3. A decisão da AMB-001. Diz que "um lançamento pode cobrir múltiplas diárias
     numa única data". A leitura A contradiz essa frase. Ela teria de ser
     corrigida, e a correção entraria no DECISIONS.md.

  ---

  Custo de cada escolha

  Leitura A: uma diária por lançamento
  - d-010 perde 230,00 de uma estadia que custou 240,00 por noite, abaixo do
    limite. A perda vem da forma como a despesa foi lançada, não do valor dela.
  - Contradiz a decisão já salva da AMB-001, que teria de ser corrigida.
  - O "por diária" do item 3 vira, na prática, "por lançamento", e o colaborador
    passa a ter de fracionar a estadia.
  - A AMB-011 (duplicatas) fica com a tarefa de distinguir diárias legítimas de
    lançamento duplicado.

  Leitura B: texto
  - Todos os custos de auditabilidade acima: vocabulário aberto, grafia, números
    ambíguos, limite controlado pelo colaborador.
  - Exige duas decisões a mais: qual é a lista de termos e qual é o padrão
    quando não há número.
  - O exemplo de custo da AMB-007 perde efeito e tem de ser reescrito.
  - A garantia n ≥ 1 tem de estar explícita, senão a justificativa da AMB-006
    perde a base.

  Leitura C: valor
  - O item 3 da política deixa de ter efeito: nenhuma hospedagem jamais é
    parcial.
  - O exemplo de custo da AMB-007 também perde efeito.

  Leitura D: campo
  - Estende a entrada. É o custo que você não aceitou na AMB-007.
  - No exemplo, o resultado depende só do padrão, que acaba sendo uma das
    leituras A, B ou C por outro caminho.

  Não alterei nenhum arquivo. Aguardo a sua decisão.

> /fast
  ⎿  Fast mode unavailable: Fast mode requires usage credits · /usage-credits to turn them on

> /fast
  ⎿  Fast mode unavailable: Fast mode requires usage credits · /usage-credits to turn them on

> #9 decidido — leitura A: cada lançamento de hospedagem vale uma diária.

  AMB-009 — Número de diárias em um lançamento de hospedagem

  Texto do RH, item 3: "Hospedagem tem limite de R$ 250 por diária."

  O que não está claro: a política estabelece o limite por diária, mas a
  entrada não informa quantas diárias um lançamento cobre. Cada despesa tem
  uma única data, sem entrada nem saída, e nenhum campo de quantidade. O
  número aparece apenas em texto livre na descrição, com redação variável —
  "2 diarias" em d-010 e "3 noites" em d-013. As leituras consideradas foram:
  (a) cada lançamento vale uma diária; (b) extrair o número da descrição;
  (c) inferir o número a partir do valor, dividindo pelo limite; (d) estender
  a entrada com um campo explícito.

  Decisão: adotada a leitura (a). Cada lançamento de hospedagem é avaliado
  como uma diária, e seu limite é sempre R$ 250,00. Uma estadia de várias
  noites deve ser lançada como uma despesa por diária. A descrição não é
  interpretada.

  Justificativa: o número de diárias não é dado estruturado, e as demais
  leituras o produzem de formas que o motor não consegue sustentar. A leitura
  (b) faria o limite depender de texto redigido pelo próprio colaborador —
  escrever uma diária a mais na descrição vale R$ 250,00 de limite adicional,
  sem que nada na despesa mude — e é o mesmo motivo pelo qual a AMB-007
  rejeitou inferir viagem a partir de texto livre; o vocabulário é
  comprovadamente aberto, já que o próprio arquivo de exemplo usa duas
  palavras distintas para a mesma coisa. A leitura (c) esvaziaria o item 3 da
  política: como o número de diárias seria sempre o suficiente para cobrir o
  valor, nenhuma hospedagem jamais excederia o limite. A leitura (d) estenderia
  um formato de entrada que o enunciado declara fixo, e no arquivo de exemplo
  o resultado dependeria inteiramente do valor padrão adotado. A leitura (a) é
  a única que produz o limite a partir de campos estruturados, é imune a
  redação, e converte o "por diária" da política em uma regra de lançamento
  verificável: uma diária, um lançamento.

  Custo aceito: uma estadia lançada de forma consolidada é penalizada pela
  forma do lançamento, não pelo valor. d-010 (R$ 480,00 por duas noites,
  R$ 240,00 por noite, abaixo do limite) recebe R$ 250,00 e perde R$ 230,00 —
  sendo que as duas diárias, lançadas separadamente, seriam reembolsadas
  integralmente. A correção cabe ao processo de lançamento, não ao motor.
  Aceito porque a alternativa é conceder limite adicional com base em
  informação que o colaborador escreve e que o sistema não tem como conferir.

  Consequências verificáveis:
  - o limite de qualquer lançamento de hospedagem é R$ 250,00
  - d-010 (hospedagem, 480,00, com NF, "Hotel Rio - 2 diarias") → reembolso
    250,00, parcial; excedente de 230,00 não reembolsado
  - d-013 (hospedagem, 690,00, sem NF) → reembolso 0,00, recusado, motivo
    SEM_NOTA_FISCAL; a recusa precede a avaliação do limite, de modo que esta
    decisão não altera seu resultado
  - hospedagem de 400,00 com NF descrita como "1 diaria" e a mesma hospedagem
    de 400,00 descrita como "2 diarias" produzem o mesmo resultado: 250,00,
    parcial
  - hospedagem de 200,00 com NF → reembolso 200,00, integral

  Efeitos sobre a spec:
  1. A decisão da AMB-001 afirma que "um lançamento pode cobrir múltiplas
     diárias numa única data". Essa frase deixa de ser verdadeira. Corrija-a
     para refletir que cada lançamento de hospedagem corresponde a uma diária,
     preservando o restante da decisão.
  2. A RN-001 diz que o limite de hospedagem é "R$ 250,00 multiplicado pelo
     número de diárias que ele cobre, número definido pela AMB-009 (pendente)".
     Substitua por "R$ 250,00 por lançamento (AMB-009)".
  3. Remova a AMB-009 dos campos "Depende de" das RN-001 e RN-002.
  4. Acrescente d-010 aos aceites da RN-001 e à §7, agora com resultado.
  5. Acrescente AMB-009 à Origem da RN-001.
  6. Verifique a afirmação "o limite de hospedagem nunca é inferior a R$ 250"
     na AMB-006: com esta decisão ela passa a ser exata, não apenas uma
     premissa — o limite é sempre R$ 250,00. Ajuste a redação se couber.
  7. Verifique o Custo aceito da AMB-007, que cita d-010 "caso o lançamento
     conte uma diária". A condicional deixa de existir. Ajuste para afirmar o
     resultado.

  Antes de salvar, varra a spec inteira procurando qualquer outra referência
  ao número de diárias, a "diárias" no plural, ou à AMB-009 como pendente, e
  me liste o que encontrar.

  Esta mudança altera registros já salvos (a decisão da AMB-001, a RN-001, a
  AMB-006 e a AMB-007), então crie a entrada D-003 no DECISIONS.md.

  Me mostre o diff antes de salvar.

● Bash(D="c:/Users/dev/SDD-Desafio/sdd-desafio/specs/001-motor-reembolso";
      S="C:/Users/dev/AppData/Local/Temp/claude/c--Users-dev-SDD-Desafio/0308efd
      8-0138-44dd-844f-6…)
  ⎿  (No output)

  Made 10 scratchpad edits +121 -28, read 2 files (ctrl+o to expand)

● Bash(S="C:/Users/dev/AppData/Local/Temp/claude/c--Users-dev-SDD-Desafio/0308ef
      d8-0138-44dd-844f-6d25461c434d/scratchpad";
      D="c:/Users/dev/SDD-Desafio/sdd-desafio/spe…)
  ⎿  (No output)

● Bash(S="C:/Users/dev/AppData/Local/Temp/claude/c--Users-dev-SDD-Desafio/0308ef
      d8-0138-44dd-844f-6d25461c434d/scratchpad"; grep -n -E "condicionado à
      AMB-009|250,00 p…)
  ⎿  (No output)

● Preparei a alteração da AMB-009 em cópias, no scratchpad. O diff está acima.
  Nenhum arquivo do projeto foi alterado.

  Os sete efeitos que você pediu

  1. AMB-001, Decisão. Ficou assim: "Em hospedagem, o limite incide por diária e
     não por dia de calendário; cada lançamento de hospedagem corresponde a uma
     diária (AMB-009), e seu limite é R$ 250,00." O resto da decisão não mudou.
     Mantive "e não por dia de calendário" porque continua valendo: dois
     lançamentos na mesma data são duas diárias, cada uma com o próprio limite.
  2. RN-001, Regra. Ajustei um pouco a sua redação. A troca literal ficaria "o
     limite de um lançamento de hospedagem é R$ 250,00 por lançamento", que
     repete "lançamento". Usei: "Em hospedagem, o limite é por diária e cada
     lançamento vale uma diária: R$ 250,00 por lançamento (AMB-009)." Se
     preferir a troca literal, eu volto.
  3. "Depende de". Na RN-001, tirei a AMB-009. Na RN-002 o campo ficaria vazio,
     então pus "RN-001 (limite da categoria)": é de lá que vem o limite
     aplicável.
  4. d-010 entrou como aceite da RN-001 e como caso de borda na §7: 250,00,
     parcial, excedente 230,00.
  5. Origem da RN-001. Agora diz "decisões AMB-001, AMB-007 e AMB-009".
  6. AMB-006. Nos dois lugares, "nunca é inferior a R$ 250" passou a "é sempre
     R$ 250,00 (AMB-009)". O argumento continua de pé e agora é exato.
  7. AMB-007, Custo aceito. Tirei a condicional. Ficou "recebe 250,00 em vez dos
     375,00 que receberia se a diária fosse ampliada; a diferença é de 125,00".

  A AMB-009 está registrada depois da AMB-007, com o seu texto e "Regra afetada:
  RN-001". O D-003 está no DECISIONS.md, com as linhas "de → para".

  Varredura: o que ficou desatualizado (não alterei)

  #: H1
  Onde: AMB-007, Consequências (l. 429–430)
  Texto atual: d-010 → "limite 250,00 por diária, sem ampliação; resultado final

  condicionado à AMB-009"
  Problema: Trata como pendente uma ambiguidade já decidida. É o mesmo tipo de
  problema que a D-001 corrigiu.
  ────────────────────────────────────────
  #: H2
  Onde: AMB-004, Consequências (l. 262–263)
  Texto atual: d-010 → "nota fiscal exigida e presente; segue para o limite"
  Problema: Não dá resultado. Na mesma lista, d-003 tem valor final.
  ────────────────────────────────────────
  #: H3
  Onde: RN-003 (l. 121), RN-004 (l. 138), AMB-005 (l. 311), §7 (l. 518)
  Texto atual: d-013 → "qualquer que seja o número de diárias"
  Problema: Continua verdadeiro, mas sugere que o número de diárias ainda varia.

  Agora é sempre 1.
  ────────────────────────────────────────
  #: H4
  Onde: Premissa da §5 (l. 53) e da §7 (l. 504), RN-001 (l. 61), AMB-007 (l.
  428–429)
  Texto atual: "R$ 250,00 por diária"
  Problema: Não é falso: uma diária equivale a um lançamento. Mas quem lê sem
  conhecer a AMB-009 pode multiplicar pelo número de noites. Na RN-001, a frase
   seguinte já esclarece.
  ────────────────────────────────────────
  #: H5
  Onde: Nenhum lugar (lacuna)
  Texto atual: —
  Problema: O lançamento "uma diária por despesa" cria um problema para a
  AMB-011. Duas diárias no mesmo hotel, na mesma data e com o mesmo valor ficam
   iguais ao par d-006/d-007, que é o caso de duplicata do exemplo. Esse
  problema não está registrado em lugar nenhum.

  Sugestões (não apliquei):
  - H1: "d-010 (hospedagem, 480,00, com NF) → limite 250,00, sem ampliação;
    reembolso 250,00, parcial (AMB-009)".
  - H2: "d-010 (hospedagem, 480,00, com NF) → nota fiscal exigida e presente;
    segue para o limite de 250,00 → reembolso 250,00, parcial". É a mesma forma
    que a linha de d-003 usa.
  - H3: tirar a frase nos quatro lugares, ou trocar por "a descrição ('3
    noites') não altera o resultado".
  - H4: pode ficar como está, porque é a redação da política. Ou trocar por "R$
    250,00 por lançamento (uma diária)" nos quatro lugares.
  - H5: registrar na §10 como dependência a levar para a AMB-011. Ou deixar para
    resolver quando chegarmos à AMB-011.

  H1 e H2 são do mesmo tipo do que você mandou corrigir na D-001. H3, H4 e H5
  dependem de como você quer a redação.

  Os itens que você aprovar entram na D-003, e depois disso eu salvo.

> Corrija H1, H2, H3 e H4. H5 vai para a §10 como dependência.

  Suas duas mudanças de redação estão aceitas: a RN-001 com "o limite é por
  diária e cada lançamento vale uma diária: R$ 250,00 por lançamento" evita a
  repetição, e "Depende de: RN-001 (limite da categoria)" na RN-002 é mais
  preciso que campo vazio.

  H1 e H2: aplique suas sugestões. São do mesmo tipo que a D-001 corrigiu.

  H3: tire a frase nos quatro lugares, não reescreva. "Qualquer que seja o
  número de diárias" e "a descrição não altera o resultado" sugerem ambas que
  o número poderia influenciar d-013. Não poderia: a recusa por ausência de
  nota precede a avaliação do limite, então nem diárias nem descrição entram
  na conta. Simplesmente remova.

  H4: troque nos quatro lugares para "R$ 250,00 por lançamento (uma diária)".
  Você está certo que não é falso, mas o risco que você mesmo apontou é real —
  quem lê a premissa global sem conhecer a AMB-009 implementaria multiplicação
  pelo número de noites. A spec precisa ser implementável por quem nunca viu
  o projeto, e a redação atual não é.

  H5: acrescente à §10:

  "Diárias e duplicatas (origem: AMB-009; afeta AMB-011). Com cada diária
  lançada como despesa própria, duas noites no mesmo hotel, na mesma data e
  pelo mesmo valor tornam-se indistinguíveis de um lançamento duplicado — a
  mesma configuração do par d-006/d-007. A regra de duplicidade precisará
  estabelecer um critério capaz de separar os dois casos, ou declarar que não
  os separa. A decisão cabe à AMB-011."

  Os itens corrigidos entram na D-003 com as linhas "de → para", incluindo o
  registro de que a decisão criou a dependência H5.

  Me mostre o diff final antes de salvar.

  Made 11 scratchpad edits +47 -22 (ctrl+o to expand)

● Bash(S="C:/Users/dev/AppData/Local/Temp/claude/c--Users-dev-SDD-Desafio/0308ef
      d8-0138-44dd-844f-6d25461c434d/scratchpad";
      D="c:/Users/dev/SDD-Desafio/sdd-desafio/spe…)
  ⎿  (No output)

● Duas quebras de linha ficaram truncadas. Vou acertar o fluxo do texto, sem
  mudar nenhuma palavra.

  Made 2 scratchpad edits +9 -10 (ctrl+o to expand)

● Bash(S="C:/Users/dev/AppData/Local/Temp/claude/c--Users-dev-SDD-Desafio/0308ef
      d8-0138-44dd-844f-6d25461c434d/scratchpad"; sed -n 50,72p
      "$S/spec.proposta.md"; echo .…)
  ⎿  (No output)

● As cópias do scratchpad estão prontas e o diff final está acima. Ainda não
  salvei nada no projeto.

  Ajustes de H1 a H5
  - H1. Na AMB-007, d-010 agora diz "limite 250,00, sem ampliação; reembolso
    250,00, parcial (AMB-009)".
  - H2. Na AMB-004, d-010 agora diz "segue para o limite de 250,00 → reembolso
    250,00, parcial".
  - H3. Tirei "qualquer que seja o número de diárias" nos quatro lugares:
    RN-003, RN-004, AMB-005 e §7. Só apaguei, sem pôr outra frase no lugar.
  - H4. "R$ 250,00 por diária" virou "R$ 250,00 por lançamento (uma diária)" nos
    quatro lugares: a premissa da §5, a da §7, a RN-001 e a AMB-007. Na AMB-007
    a lista não usa "R$", então lá ficou "250,00 por lançamento (uma diária)".
  - H5. O item "Diárias e duplicatas" entrou na §10 com o seu texto.

  Quebras de linha. A troca do H4 tinha deixado linhas curtas na premissa e na
  RN-001. Reorganizei as quebras sem mudar nenhuma palavra. O texto final está
  na última saída acima.

  D-003. Tem agora as linhas "de → para" de H1 a H4 e o novo item da §10. O
  campo "O que isso invalidou" registra a dependência que esta decisão criou com
  a AMB-011.

  Varredura de novo.
  - Não sobrou "número de diárias" usado como variável, nem "condicionado à
    AMB-009", nem "AMB-009 (pendente)".
  - Os "por diária" que restam são de três tipos:
    - citações da política, na AMB-001 e na AMB-009;
    - o texto da própria AMB-009;
    - frases que já dizem "cada lançamento vale uma diária".

  Uma repetição pequena. A RN-001 agora diz "R$ 250,00 por lançamento (uma
  diária)" na lista de limites e repete a ideia duas frases depois ("cada
  lançamento vale uma diária: R$ 250,00 por lançamento (AMB-009)"). Deixei
  assim: a primeira menção avisa quem lê a lista, e a segunda dá a origem. Se
  preferir enxugar, removo a segunda.

  Se aprovar, salvo as duas cópias sobre spec.md e DECISIONS.md.

> Aprovado, com um ajuste: enxugue a repetição na RN-001.

  Remova o parêntese da lista de limites — deixe apenas "hospedagem R$ 250,00"
  — e mantenha a frase explicativa que vem depois, que já diz "em hospedagem,
  o limite é por diária e cada lançamento vale uma diária: R$ 250,00 por
  lançamento (AMB-009)". A lista fica legível e a explicação completa fica
  onde pertence, com a origem.

  Mantenha "R$ 250,00 por lançamento (uma diária)" nas premissas da §5 e §7 e
  na AMB-007, onde não há frase explicativa logo em seguida — lá o parêntese
  é o que evita a leitura errada.

  Com isso, pode salvar as duas cópias sobre spec.md e DECISIONS.md.

  Made 2 scratchpad edits +11 -9 (ctrl+o to expand)

● Bash(S="C:/Users/dev/AppData/Local/Temp/claude/c--Users-dev-SDD-Desafio/0308ef
      d8-0138-44dd-844f-6d25461c434d/scratchpad";
      D="c:/Users/dev/SDD-Desafio/sdd-desafio/spe…)
  ⎿  Updated DECISIONS.md (+62 -0)
      10  
      11  ---
      12  
      13 +## D-003 — Limite de hospedagem fixado em uma diária por lançam
         +ento após a AMB-009 · `2026-10-18`                             
      14 +                                                               
      15 +**Gatilho:** a decisão da AMB-009 (leitura a: cada lançamento d
         +e hospedagem                                                   
      16 +vale uma diária) tornou falsa uma afirmação da decisão da AMB-0
         +01 e                                                           
      17 +obsoletos registros que tratavam o número de diárias como pende
         +nte ou                                                         
      18 +variável: a RN-001, a RN-002, a AMB-006 e a AMB-007.           
      19 +                                                               
      20 +**O que mudou na spec:**                                       
      21 +- AMB-001, Decisão: de "um lançamento pode cobrir múltiplas diá
         +rias numa                                                      
      22 +  única data; quantas é objeto da AMB-009, e o limite aplicável
         + a um                                                          
      23 +  lançamento de hospedagem fica condicionado àquela decisão" pa
         +ra "cada                                                       
      24 +  lançamento de hospedagem corresponde a uma diária (AMB-009), 
         +e seu limite                                                   
      25 +  é R$ 250,00". O restante da decisão foi preservado.          
      26 +- RN-001, Regra: de "o limite de um lançamento de hospedagem é 
         +R$ 250,00                                                      
      27 +  multiplicado pelo número de diárias que ele cobre, número def
         +inido pela                                                     
      28 +  AMB-009 (pendente)" para "cada lançamento vale uma diária: R$
         + 250,00 por                                                    
      29 +  lançamento (AMB-009)". Origem: acrescentada a AMB-009. Depend
         +e de: AMB-009                                                  
      30 +  removida. Aceite: acrescentado d-010 → 250,00, parcial.      
      31 +- RN-002, Depende de: de "AMB-009 (…), pendente" para "RN-001 (
         +limite da                                                      
      32 +  categoria)".                                                 
      33 +- AMB-006, Justificativa e Consequências (d-013): de "o limite 
         +de                                                             
      34 +  hospedagem nunca é inferior a R$ 250" para "o limite de hospe
         +dagem é                                                        
      35 +  sempre R$ 250,00 (AMB-009)".                                 
      36 +- AMB-007, Custo aceito: removida a condicional "caso o lançame
         +nto conte uma                                                  
      37 +  diária"; o resultado de d-010 (250,00 em vez de 375,00) passa
         + a ser                                                         
      38 +  afirmado.                                                    
      39 +- AMB-007, Consequências (d-010): de "limite 250,00 por diária,
         + sem                                                           
      40 +  ampliação; resultado final condicionado à AMB-009" para "limi
         +te 250,00,                                                     
      41 +  sem ampliação; reembolso 250,00, parcial (AMB-009)".         
      42 +- AMB-004, Consequências (d-010): de "segue para o limite" para
         + "segue para                                                   
      43 +  o limite de 250,00 → reembolso 250,00, parcial".             
      44 +- RN-003 e RN-004 (aceites de d-013), AMB-005 (Consequências de
         + d-013) e §7                                                   
      45 +  (caso de d-013): removida a expressão "qualquer que seja o nú
         +mero de                                                        
      46 +  diárias". A recusa por ausência de nota precede a avaliação d
         +o limite, de                                                   
      47 +  modo que nem diárias nem descrição entram na conta.          
      48 +- §5 e §7 (premissa global) e AMB-007 (Consequências): de "R$ 2
         +50,00 por                                                      
      49 +  diária" para "R$ 250,00 por lançamento (uma diária)", para qu
         +e quem lê sem                                                  
     … 28 more lines (ctrl+o to expand)
  ⎿  Updated spec.md (+90 -33)
      50  > **Limites nominais, sem ampliação por viagem:** o sistema não
           determina se o
      51  > colaborador está em viagem (AMB-007). Todas as despesas são a
          valiadas com os
      52  > limites nominais da política — alimentação R$ 60,00, transpor
          te urbano
      53 -> R$ 80,00, hospedagem R$ 250,00 por diária —, e os critérios d
         -e aceite e os                                                  
      54 -> casos de borda deste documento valem para qualquer despesa, s
         -em distinção                                                   
      55 -> de viagem.                                                   
      53 +> R$ 80,00, hospedagem R$ 250,00 por lançamento (uma diária) —,
         + e os                                                          
      54 +> critérios de aceite e os casos de borda deste documento valem
         + para qualquer                                                 
      55 +> despesa, sem distinção de viagem.                            
      56  
      57  ### RN-001 — Limite de categoria aplicado por despesa individua
          l
      58  
      59  **Regra:** cada despesa de alimentação, transporte urbano ou ho
          spedagem é
      60  comparada, isoladamente, com o limite da sua categoria (aliment
          ação R$ 60,00;
      61 -transporte urbano R$ 80,00; hospedagem R$ 250,00 por diária). N
         -enhuma outra                                                   
      62 -despesa altera o resultado da despesa avaliada por meio de agre
         -gação de                                                       
      63 -valores: não há soma de despesas da mesma categoria no mesmo di
         -a. Não existe                                                  
      64 -teto diário agregado por categoria. Em hospedagem, o limite é p
         -or diária: o                                                   
      65 -limite de um lançamento de hospedagem é R$ 250,00 multiplicado 
         -pelo número de                                                 
      66 -diárias que ele cobre, número definido pela AMB-009 (pendente).
      67 -**Origem:** política do RH, itens 1, 2 e 3; decisões AMB-001 e 
         -AMB-007                                                        
      68 -**Depende de:** RN-002 (reembolso parcial, decidido na AMB-002)
         -; AMB-009                                                      
      69 -(número de diárias em lançamento de hospedagem), pendente.     
      61 +transporte urbano R$ 80,00; hospedagem R$ 250,00). Nenhuma outr
         +a despesa                                                      
      62 +altera o resultado da despesa avaliada por meio de agregação de
         + valores: não                                                  
      63 +há soma de despesas da mesma categoria no mesmo dia. Não existe
         + teto diário                                                   
      64 +agregado por categoria. Em hospedagem, o limite é por diária e 
         +cada                                                           
      65 +lançamento vale uma diária: R$ 250,00 por lançamento (AMB-009).
      66 +**Origem:** política do RH, itens 1, 2 e 3; decisões AMB-001, A
         +MB-007 e AMB-009                                               
      67 +**Depende de:** RN-002 (reembolso parcial, decidido na AMB-002)
         +.                                                              
      68  **Aceite:**
      69  - d-001 (03/07, alimentação, 72,50) → reembolso 60,00, parcial.
      70  - d-002 (03/07, alimentação, 38,00) → reembolso 38,00, integral
          .
      71  - O dia 03/07 totaliza 98,00 reembolsados em alimentação.
      72  - Inverter a ordem de d-001 e d-002 na entrada não altera nenhu
          m dos valores acima.
      73  - Três despesas de alimentação de 59,00 no mesmo dia → 59,00 ca
          da, 177,00 no total.
      74 +- d-010 (14/07, hospedagem, 480,00, com NF, "Hotel Rio - 2 diar
         +ias") →                                                        
      75 +  limite 250,00; reembolso 250,00, parcial; excedente 230,00. A
         + descrição não                                                 
      76 +  altera o limite.                                             
      77  
     … 181 more lines (ctrl+o to expand)

● Salvei spec.md (specs/001-motor-reembolso/spec.md) e DECISIONS.md
  (specs/001-motor-reembolso/DECISIONS.md). Conferi byte a byte com as cópias
  aprovadas e estão idênticos.

  O ajuste final
  - RN-001: a lista de limites agora diz só "hospedagem R$ 250,00". A frase que
    vem logo depois explica que cada lançamento vale uma diária e cita a origem
    (AMB-009).
  - "R$ 250,00 por lançamento (uma diária)" continua em três lugares: na
    premissa da §5, na premissa da §7 e na AMB-007. São os pontos em que nenhuma
    frase explicativa vem logo depois.
  - D-003: separei em duas linhas o que mudou nas premissas e na AMB-007 e o que
    mudou na lista da RN-001. Assim o registro descreve exatamente o que foi
    feito.

  O DECISIONS.md agora tem D-003, D-002 e D-001, nessa ordem. Nada foi
  commitado.

> Vamos decidir três ambiguidades relacionadas ao período de competência, em
  bloco. Apresente as três de uma vez, no formato de sempre — texto do RH (ou
  a ausência dele), leituras possíveis com argumento de negócio, números nos
  itens afetados, custo de cada escolha. Sem recomendação.

  AMB-016 — Fonte da verdade do período: o campo competencia ("2026-07") ou o
  par inicio/fim? O que acontece se discordarem? As bordas são inclusivas?
  Afeta d-014 (31/07, exatamente periodo.fim).

  AMB-008 — O que caracteriza "lançado dentro do período de competência": a
  data da despesa, a data do lançamento (que a entrada não tem), ou outra
  coisa?

  AMB-010 — Efeito de uma despesa fora do período: recusa com motivo, ou
  exclusão do cálculo? Ela conta para alguma agregação? Afeta d-008 (15/04).

  Para as três, diga também se alguma interage com decisões já tomadas, e se
  alguma cria dependência para as ambiguidades ainda pendentes (011, 012, 013,
  014, 015, 017).

● Bash(cd "c:/Users/dev/SDD-Desafio/sdd-desafio" && grep -rn -E
      "AMB-01[0-7]|#1[0-7]\b" docs specs --include=*.md | grep -v "sessions/01"
      | head -40; echo ----; cat do…)
  ⎿  (No output)

● Bash(cd "c:/Users/dev/SDD-Desafio/sdd-desafio/docs/sessions" && ls; grep -n -E
      "AMB-01[0-7]|^ *#?1[0-7][ .)—-]" 01-ambiguidades-001-a-006.md | head -60)
  ⎿  (No output)

● Bash(cd "c:/Users/dev/SDD-Desafio/sdd-desafio/docs/sessions" && grep -n -i -E
      "estorno|negativ|coworking|ineleg|sábado|sabado|plantão|duplicat|17\b|#12|
      #15|#17|lista…)
  ⎿  (No output)

● Bash(cd "c:/Users/dev/SDD-Desafio/sdd-desafio/docs/sessions" && sed -n 30,125p
      01-ambiguidades-001-a-006.md; grep -n -E "AMB-01[5-7]|AMB-012" *.md |
      head)
  ⎿  (No output)

● Período de competência: AMB-016, AMB-008 e AMB-010

  Antes de começar, uma lacuna: não tenho registro do que são a AMB-015 e a
  AMB-017. Elas não aparecem na spec, no DECISIONS.md, nas notas de trabalho nem
  na lista original da sessão 01. Da lista original, sei que a AMB-011 trata de
  duplicata, a AMB-012 de estorno (d-009), a AMB-013 de categoria (d-014 em
  maiúsculas, d-005 coworking) e a AMB-014 de precisão monetária. Abaixo analiso
  as interações com essas quatro. Se você me disser o que são a 015 e a 017,
  completo a análise.

  O que a entrada informa:
  - periodo traz três campos: competencia: "2026-07", inicio: "2026-07-01" e
    fim: "2026-07-31".
  - Cada despesa traz só uma data. Não existe data de lançamento.

  Itens do exemplo que estas três decisões afetam:
  - d-008 (15/04, alimentação, 41,00, com NF, "Almoco de abril lancado com
    atraso").
  - d-014 (31/07, a data exata de fim, "ALIMENTACAO", 61,00, com NF).
  - As outras despesas têm datas entre 03/07 e 22/07, longe das bordas.

  ---

  AMB-016: qual campo define o período, e se as bordas contam

  Texto do RH: não há. O item 7 fala em "período de competência", mas não diz
  como ele é representado. A dúvida vem da entrada, que traz o período de dois
  jeitos.

  As leituras sobre a fonte do período

  Leitura A: vale a competencia. O período é o mês declarado, do dia 1 ao último
  dia. inicio e fim são informativos.
  - Argumento: "competência" é a palavra da política, e competência contábil é
    um mês. Os outros dois campos derivam dela.

  Leitura B: valem inicio e fim. O par define o intervalo, e competencia é só um
  rótulo.
  - Argumento: é a forma mais precisa e permite períodos que não coincidem com o
    mês, como um fechamento no dia 25.

  Leitura C: os três têm de concordar. Se discordarem, a entrada é inválida e o
  lote inteiro é rejeitado, sem cálculo nenhum.
  - Argumento: uma divergência no cabeçalho é erro de quem montou a entrada.
    Calcular sobre um período ambíguo produz um resultado que ninguém sabe qual
    período representa.

  As leituras sobre as bordas

  - Inclusivas nas duas pontas: o período é [01/07, 31/07].
    - Argumento: fim é uma data sem hora, e "fim: 31/07" em português corrente
      inclui o dia 31.
  - Semiaberto: o período é [01/07, 31/07), ou seja, o dia 31 fica fora.
    - Argumento: é a convenção comum em intervalos de tempo e evita sobreposição
      entre competências consecutivas. Com datas sem hora, porém, ela exclui o
      último dia do mês.

  Números

  Item: Exemplo (os três campos concordam)
  A: julho
  B: julho
  C: julho, entrada válida
  Borda inclusiva: —
  Borda semiaberta: —
  ────────────────────────────────────────
  Item: d-014 (31/07)
  A: —
  B: —
  C: —
  Borda inclusiva: dentro: segue para a AMB-013 e o limite; 60,00 parcial se
  "ALIMENTACAO" for normalizada
  Borda semiaberta: fora: o efeito vem da AMB-010
  ────────────────────────────────────────
  Item: Hipotético: competencia 2026-07, fim 2026-08-15, despesa em 10/08
  A: fora
  B: dentro
  C: lote inválido
  Borda inclusiva: —
  Borda semiaberta: —

  No exemplo, só a escolha da borda muda algum resultado, e só o de d-014.

  Custo de cada escolha

  - A: ignora dois campos da entrada. Se inicio e fim vierem diferentes do mês,
    o motor descarta a informação sem avisar.
  - B: a competencia passa a não significar nada. Um período de 45 dias seria
    aceito sem questionamento.
  - C: cria um caminho de erro de entrada que hoje não existe. A §4 precisaria
    dizer o que a saída mostra quando o lote é rejeitado. Um erro de digitação
    no cabeçalho bloqueia o reembolso inteiro.
  - Borda semiaberta: d-014, que é uma despesa do último dia do mês, sai do
    período. Todo dia 31 (ou 30, ou 28) fica fora de qualquer competência,
    porque também não entra na competência seguinte.

  ---

  AMB-008: o que é "lançado dentro do período"

  Texto do RH: item 7, "Despesas devem ser lançadas dentro do período de
  competência."

  As leituras

  Leitura A: vale a data da despesa. A despesa está dentro do período se a data
  dela está no período definido pela AMB-016.
  - Argumento: é o único dado de data que a entrada tem. Lida como regra
    contábil, a frase quer dizer "cada despesa deve ser lançada na competência a
    que pertence". Uma despesa de abril pertence a abril.

  Leitura B: vale a data do lançamento. É a leitura literal de "lançadas".
  - Fato: a entrada não tem esse campo. Há duas formas de ler isso:
    - o lote de julho é, por definição, lançado em julho, então toda despesa do
      arquivo foi lançada dentro do período e a regra nunca dispara;
    - a regra não pode ser avaliada e fica fora de escopo, como o item 6 na
      AMB-007.

    Nos dois casos, o item 7 não tem efeito no motor.
  - Argumento: é a leitura mais fiel à palavra "lançadas".

  Leitura C: despesa ocorrida até o fim do período, sem limite inferior. Aceita
  despesa atrasada e recusa despesa com data futura.
  - Argumento: trata o item 7 como prazo de entrega. O colaborador não perde o
    direito por lançar tarde, mas não pode antecipar.
  - Uma variante seria aceitar atraso só até N meses. A política não dá o N,
    então seria inventar um número, como a leitura (c) da AMB-002.

  Números

  ┌───────────────────────┬────────────────┬────────────────┬──────────────┐
  │         Item          │  A (data da    │  B (data do    │  C (até o    │
  │                       │    despesa)    │  lançamento)   │     fim)     │
  ├───────────────────────┼────────────────┼────────────────┼──────────────┤
  │ d-008 (15/04, 41,00,  │ fora: o efeito │ dentro → 41,00 │ dentro →     │
  │ com NF)               │  vem da        │  integral      │ 41,00        │
  │                       │ AMB-010        │                │ integral     │
  ├───────────────────────┼────────────────┼────────────────┼──────────────┤
  │                       │ depende da     │                │ depende da   │
  │ d-014 (31/07)         │ borda          │ dentro         │ borda        │
  │                       │ (AMB-016)      │                │              │
  ├───────────────────────┼────────────────┼────────────────┼──────────────┤
  │ Hipotético: despesa   │                │                │              │
  │ de 05/08 no lote de   │ fora           │ dentro         │ fora         │
  │ julho                 │                │                │              │
  ├───────────────────────┼────────────────┼────────────────┼──────────────┤
  │ As demais (03/07 a    │ dentro         │ dentro         │ dentro       │
  │ 22/07)                │                │                │              │
  └───────────────────────┴────────────────┴────────────────┴──────────────┘

  Sobre a descrição de d-008: ela diz "lancado com atraso". Lida ao pé da letra,
  a despesa foi lançada em julho, dentro do período, e aconteceu em abril. Na
  leitura B, a própria descrição satisfaria o item 7. Mas o texto não pode ser
  usado para decidir: a AMB-007 e a AMB-009 já estabeleceram que a descrição não
  é interpretada. Nenhuma das três leituras lê esse texto. Registro só que a
  narrativa do exemplo trata d-008 como o caso "fora da competência".

  Custo de cada escolha

  - A: uma despesa legítima lançada com atraso perde o reembolso por estar no
    lote errado. d-008 perde 41,00. Como no custo aceito da AMB-009, quem
    resolve isso é o processo de lançamento: lançar no lote da competência
    certa.
  - B: o item 7 deixa de ter efeito. O motor reembolsa uma despesa de qualquer
    data, inclusive de anos atrás ou futura, e o controle de competência fica
    com o financeiro, por conferência manual.
  - C: d-008 é paga em julho, então uma despesa de abril entra no fechamento de
    julho e a separação contábil por mês se perde. Também deixa de haver
    proteção contra a mesma despesa ser apresentada em lotes de meses
    diferentes. O motor só vê um lote por vez, então não detecta isso.

  ---

  AMB-010: o que acontece com uma despesa fora do período

  Texto do RH: não há. O item 7 diz "devem ser", mas não diz o que acontece
  quando a despesa não é.

  As leituras

  Leitura A: recusa com motivo próprio. Reembolso 0,00, classificação recusada,
  motivo FORA_DA_COMPETENCIA. Esse motivo é distinto de SEM_NOTA_FISCAL e de
  CATEGORIA_INELEGIVEL.
  - Argumento: é o mesmo padrão da AMB-005. O item aparece na saída, e o
    colaborador sabe por que não recebeu e o que fazer: lançar na competência
    certa.

  Leitura B: exclusão do cálculo. O item não é processado e não aparece na
  saída, ou aparece numa lista separada de "não processados", fora dos totais.
  - Argumento: o item não pertence a este lote. Recusá-lo sugere que ele foi
    avaliado e reprovado, quando na verdade nem cabia avaliar.

  Leitura C: pendente, para a competência correta. O item recebe um status
  próprio, indicando que deve ser reapresentado na competência dele.
  - Argumento: separa "não é reembolsável" de "é reembolsável, mas em outro
    lote".

  Leitura D: paga e sinaliza. Para ficar completo: o item segue normalmente e a
  saída registra um alerta.

  Números

  Item: d-008 (41,00, com NF)
  A: 0,00, recusado, FORA_DA_COMPETENCIA
  B: ausente da saída, ou listado como não processado
  C: 0,00, pendente
  D: 41,00 integral + alerta
  ────────────────────────────────────────
  Item: d-014, se a AMB-016 adotar borda semiaberta
  A: 0,00, recusado
  B: ausente
  C: 0,00, pendente
  D: conforme a AMB-013 + alerta

  Tudo isso só se aplica se a AMB-008 adotar a leitura A ou C. Na leitura B,
  nenhuma despesa fica fora e a AMB-010 não tem efeito.

  Ela conta para alguma agregação?

  - Agregação de valores entre despesas: não existe em nenhuma leitura, porque a
    AMB-001 já a rejeitou. A competência compara cada despesa com o cabeçalho
    do lote, não com outras despesas. Nenhuma leitura cria dependência entre
    despesas.
  - Totais da saída: a §4 ainda não define totais. Se houver um "total lançado"
    e um "total reembolsado":
    - na leitura A, d-008 entra no total lançado com 41,00 e no reembolsado com
      0,00;
    - na leitura B, fica fora dos dois;
    - na leitura D, entra nos dois.
  - Duplicata (AMB-011): a pergunta é se um item fora do período pode ser o
    "original" de um par de duplicatas. Na leitura B, como o item é excluído
    antes, ele não pode. Na leitura A, depende da ordem de aplicação das regras
    na §8.

  Custo de cada escolha

  - A: é preciso decidir a ordem do motivo quando um item tem dois problemas.
    Por exemplo, uma despesa de 150,00 sem NF, de abril, seria
    FORA_DA_COMPETENCIA ou SEM_NOTA_FISCAL? O reembolso é 0,00 nos dois casos,
    mas o motivo muda. A §8 e a §4 precisam definir se há um motivo único, e
    qual vale primeiro, ou uma lista de motivos.
  - B: o item some da saída. Se o colaborador não encontrar a despesa no
    resultado, não sabe se ela foi esquecida ou excluída. A justificativa por
    item, exigida pelo enunciado ("justifica cada decisão"), fica sem cobrir
    esse item, a menos que exista a lista de "não processados".
  - C: cria o quarto estado e o problema "até quando reapresentar", exatamente o
    que a AMB-005 rejeitou ao descartar a leitura (c). Escolher C aqui
    contradiz a justificativa já registrada na AMB-005.
  - D: trata "devem ser lançadas" como recomendação, a mesma objeção que a
    AMB-005 fez à leitura (d).

  ---

  Interações com decisões já tomadas

  Decisão: AMB-001
  Interação: Nenhuma leitura cria dependência entre despesas: a competência
  compara a despesa com o cabeçalho. A justificativa da AMB-001 ("o colaborador
   sabe se a despesa será paga no momento em que a faz") fica de pé nas
  leituras A e C da AMB-008.
  ────────────────────────────────────────
  Decisão: AMB-002
  Interação: As Consequências trazem "d-014 → 60,00, parcial (condicionado à
  AMB-013)". Se a AMB-016 adotar borda semiaberta, d-014 também passa a
  depender da competência e esse resultado deixa de valer. Com borda inclusiva,
   nada muda.
  ────────────────────────────────────────
  Decisão: AMB-005
  Interação: É precedente direto para a AMB-010: recusa com motivo distinto e
  rejeição tanto do estado pendente quanto do alerta. As leituras C e D da
  AMB-010 contradiriam a justificativa da AMB-005. A leitura A segue o mesmo
  padrão.
  ────────────────────────────────────────
  Decisão: AMB-007 / AMB-009
  Interação: São precedente para não ler texto livre. A descrição de d-008
  ("lancado com atraso") não pode decidir nada. A leitura B da AMB-008 tem a
  mesma estrutura da leitura (a) da AMB-007: o dado não existe, então a regra
  fica sem efeito.
  ────────────────────────────────────────
  Decisão: RN-004 e §8
  Interação: Uma recusa por competência entra na ordem da §8. Se ela vier antes
  da NF, um item com os dois problemas recebe FORA_DA_COMPETENCIA. Se vier
  depois, recebe SEM_NOTA_FISCAL. O reembolso é o mesmo, e só o motivo muda. No
   exemplo, isso não aparece: d-008 tem NF e vale menos de 100.
  ────────────────────────────────────────
  Decisão: AMB-003, 004, 006
  Interação: Sem interação.

  Dependências que estas decisões criam para as pendentes

  - AMB-011 (duplicata):
    - um item fora do período participa da detecção de duplicatas?
    - a ordem entre competência e duplicata vai para a §8;
    - continua valendo a restrição da §10 (diárias e duplicatas);
    - a leitura C da AMB-008 abre um caso que o motor não vê: a mesma despesa
      apresentada em lotes de meses diferentes.
  - AMB-012 (estorno, d-009 em 11/07): a competência se aplica a valores
    negativos? No exemplo, d-009 está dentro do período, então não há efeito.
    Mas surge uma pergunta: um estorno em julho de uma corrida de junho fica
    dentro ou fora? Na leitura A, fica dentro, pela data do estorno. E a ordem
    importa: se o estorno for entrada inválida, o motivo dele vem antes ou
    depois da competência?
  - AMB-013 (categoria): d-014 junta os dois problemas, borda e maiúsculas. A
    ordem entre CATEGORIA_INELEGIVEL e FORA_DA_COMPETENCIA também precisa ser
    definida, para itens que tenham os dois. No exemplo não há: d-005, o
    coworking, é de 07/07.
  - AMB-014 (precisão): sem interação, porque trata de valores e não de datas.
  - §4 (schema): depende destas decisões para três coisas: motivo único ou
    lista; como representar item excluído (leitura B da AMB-010); e como
    representar lote inválido (leitura C da AMB-016).
  - AMB-015 e AMB-017: não tenho o texto delas, então não sei dizer.

  Não alterei nenhum arquivo. Aguardo as três decisões.

> Antes das decisões, corrijo uma lacuna que você apontou: a AMB-015 e a
  AMB-017
  não estão na spec porque ainda não foram escritas. São duas ambiguidades que
  acrescentei à lista original:

  AMB-015 — centro_custo. O campo existe no JSON (colaborador.centro_custo,
  "CC-ENG-PLATAFORMA") e a política não o menciona em nenhum item.

  AMB-017 — Critério que distingue recusa de erro de entrada. d-005 (coworking)
  e d-009 (valor negativo) são ambos casos não previstos pela política, e não
  há critério declarado que diga por que um seria recusa de negócio e o outro
  dado inválido.

  Agora as decisões do bloco.

  === AMB-016 — Representação do período de competência ===

  Texto do RH: item 7, "Despesas devem ser lançadas dentro do período de
  competência." A política não diz como o período é representado. A entrada
  traz três campos: competencia ("2026-07"), inicio ("2026-07-01") e fim
  ("2026-07-31").

  O que não está claro: duas questões independentes. (i) qual campo define o
  período quando há mais de um — competencia, o par inicio/fim, ou os três
  precisam concordar; (ii) se as datas de borda pertencem ao período.

  Decisão: o período é o intervalo definido por inicio e fim, com ambas as
  bordas inclusivas. Uma despesa cuja data seja igual a inicio ou igual a fim
  está dentro do período. O campo competencia é tratado como rótulo
  informativo e não participa da avaliação; divergência entre ele e o par
  inicio/fim não impede o processamento.

  Justificativa: o par inicio/fim é o dado mais expressivo — a competencia é
  derivável dele, mas o inverso não: um fechamento que vá de 26/06 a 25/07 não
  cabe em nenhum rótulo de mês. Exigir concordância entre os três campos
  criaria um caminho de erro de entrada que faria um engano de digitação no
  cabeçalho bloquear o reembolso de todas as despesas do lote, custo alto para
  proteger contra algo que a entrada de referência não apresenta. Quanto às
  bordas, inicio e fim são datas sem hora, e "fim: 31/07" em linguagem corrente
  inclui o dia 31; adotar intervalo semiaberto faria o último dia de todo mês
  não pertencer a competência alguma, já que também não pertenceria à
  competência seguinte — o que não é convenção de intervalo, é perda de dia.

  Custo aceito: o campo competencia deixa de ter efeito sobre o cálculo. Um
  lote cujo rótulo diga "2026-07" mas cujo par de datas cubra agosto será
  processado como agosto, sem alerta. Aceito porque o par de datas é a
  representação precisa, e sinalizar divergência exigiria um caminho de erro
  que a política não prevê.

  Consequências verificáveis:
  - o período do arquivo de exemplo é [2026-07-01, 2026-07-31], ambas
  inclusivas
  - d-014 (31/07) está dentro do período; seu resultado segue para as demais
    regras
  - uma despesa em 2026-07-01 está dentro do período
  - uma despesa em 2026-08-01 está fora do período

  === AMB-008 — Dado que determina a pertinência ao período ===

  Texto do RH: item 7, "Despesas devem ser lançadas dentro do período de
  competência."

  O que não está claro: a palavra "lançadas" sugere a data do lançamento, que
  a entrada não possui — cada despesa traz apenas uma data, sem indicação de
  quando foi registrada. As leituras consideradas foram: (a) usar a data da
  despesa; (b) usar a data do lançamento, que não existe, deixando a regra sem
  efeito ou fora de escopo; (c) aceitar qualquer despesa ocorrida até o fim do
  período, recusando apenas datas futuras.

  Decisão: adotada a leitura (a). A pertinência ao período é avaliada pela data
  da despesa, comparada ao intervalo definido na AMB-016.

  Justificativa: a data da despesa é o único dado de data que a entrada
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

  Custo aceito: uma despesa legítima apresentada no lote de uma competência
  diferente da sua perde o reembolso. d-008 (alimentação de 41,00, com nota
  fiscal, ocorrida em 15/04 e apresentada no lote de julho) não é reembolsada,
  embora seja uma despesa válida em todos os outros aspectos. Como na AMB-009,
  a correção cabe ao processo de lançamento: apresentar a despesa no lote da
  competência correta.

  Consequências verificáveis:
  - d-008 (15/04, lote de julho) → fora do período; efeito definido pela
  AMB-010
  - as despesas entre 03/07 e 22/07 estão dentro do período
  - a descrição de d-008 ("Almoco de abril lancado com atraso") não é
    interpretada, em coerência com a AMB-007 e a AMB-009

  === AMB-010 — Efeito de uma despesa fora do período ===

  Texto do RH: item 7 diz que as despesas "devem ser lançadas dentro do período
  de competência", mas não estabelece a consequência de não estarem.

  O que não está claro: quatro leituras — (a) recusar com motivo próprio; (b)
  excluir do processamento, de modo que o item não apareça na saída; (c) marcar
  como pendente para a competência correta; (d) reembolsar normalmente e
  registrar um alerta.

  Decisão: adotada a leitura (a). Uma despesa cuja data esteja fora do período
  tem reembolso 0,00 e é classificada como recusada, com motivo
  FORA_DA_COMPETENCIA. O item aparece na saída com seu motivo, como qualquer
  outro.

  Justificativa: é o mesmo padrão adotado na AMB-005 — o item permanece visível
  e o colaborador sabe por que não foi reembolsado e o que fazer, que é
  apresentá-lo no lote da competência correta. A leitura (b) retiraria o item
  da
  saída, e o enunciado exige que o motor justifique cada decisão; um item
  ausente não distingue "excluído" de "esquecido". As leituras (c) e (d) foram
  rejeitadas na AMB-005 pelos mesmos motivos que valem aqui: a primeira cria um
  quarto estado e abre a questão de até quando a reapresentação é possível; a
  segunda trata uma obrigação da política como recomendação.

  Custo aceito: a saída passa a conter itens com reembolso zero que não foram
  avaliados contra as demais regras de valor. Aceito porque a visibilidade do
  item e a explicitação do motivo valem mais que a brevidade da saída.

  Consequências verificáveis:
  - d-008 (alimentação, 41,00, com NF, 15/04) → reembolso 0,00, recusado,
    motivo FORA_DA_COMPETENCIA
  - d-014 (31/07) → dentro do período; esta regra não se aplica
  - uma despesa fora do período não participa de nenhuma agregação ou
    comparação com outras despesas

  === Decisão transversal — Motivos múltiplos ===

  Registre também esta decisão, que as três acima tornaram necessária.

  O que não está claro: uma despesa pode violar mais de uma regra
  simultaneamente — por exemplo, uma despesa de abril, de R$ 150,00, sem nota
  fiscal, viola competência e documentação. A spec não declarava se a saída
  reporta um único motivo ou todos.

  Decisão: a saída de cada despesa traz a lista de todos os motivos aplicáveis,
  e não um motivo único. Todas as regras de elegibilidade são avaliadas e seus
  motivos coletados; o reembolso é zero se qualquer motivo de recusa estiver
  presente. A lista é ordenada de forma determinística, na ordem declarada na
  §8, de modo que a mesma entrada produza sempre a mesma saída.

  Justificativa: as causas de recusa são independentes entre si, e eleger uma
  delas exigiria uma ordem de precedência arbitrária que esconderia as demais.
  Reportar todas permite ao colaborador corrigir tudo de uma vez, em lugar de
  descobrir um problema por submissão. A decisão também simplifica a §8: a
  ordem das regras passa a tratar apenas do que afeta o valor reembolsado, não
  do que afeta o rótulo.

  Custo aceito: a representação da saída fica mais complexa, com uma lista no
  lugar de um valor escalar, e é necessário declarar a ordenação da lista para
  que o resultado seja determinístico.

  Consequências verificáveis:
  - uma despesa de 150,00, sem nota fiscal, com data fora do período →
    reembolso 0,00, recusado, motivos [FORA_DA_COMPETENCIA, SEM_NOTA_FISCAL]
  - d-008 → reembolso 0,00, recusado, motivos [FORA_DA_COMPETENCIA]
  - d-004 → reembolso 0,00, recusado, motivos [SEM_NOTA_FISCAL]
  - uma despesa sem nenhuma violação → lista de motivos vazia

  === Efeitos sobre a spec ===

  1. Registre AMB-008, AMB-010 e AMB-016 nas posições numéricas corretas, e a
     decisão transversal de motivos múltiplos onde fizer mais sentido —
  proponha
     onde, e me diga por quê.
  2. Crie as RN correspondentes.
  3. Atualize a §8: a verificação de período passa a integrar a ordem, e a
     ordem passa a tratar do que afeta o valor. Declare que todas as regras de
     elegibilidade são avaliadas e seus motivos coletados, e que a ordem da §8
     também define a ordenação da lista de motivos.
  4. Acrescente d-008 e d-014 à §7 com seus resultados.
  5. A RN-004 declara que o motivo SEM_NOTA_FISCAL "é distinto de qualquer
     outra causa de recusa, em particular de CATEGORIA_INELEGIVEL". Verifique
  se
     essa redação permanece coerente com a decisão de motivos múltiplos.
  6. Verifique se alguma consequência já registrada dizia que d-008 ou d-014
     tinham resultado condicionado a estas decisões, e atualize.

  Varra a spec inteira procurando referências a período, competência, datas de
  borda, motivo único, ou às AMB-008, 010 e 016 como pendentes, e me liste o
  que encontrar antes de alterar.

  Se alguma dessas decisões alterar registros já salvos, crie a entrada D-004.

  Me mostre o diff separado por ambiguidade, não em bloco único, para eu
  revisar uma por vez.

● Bash(cd "c:/Users/dev/SDD-Desafio/sdd-desafio/specs/001-motor-reembolso" &&
      grep -n -i -E "compet|período|periodo|d-008|d-014|AMB-008|AMB-010|AMB-016|
      motivo|borda|in…)  ⎿  (No output)

● A varredura veio limpa nos pontos de período e borda. Vou montar a proposta em
  etapas, uma por ambiguidade, para o diff sair separado.

● Bash(S="C:/Users/dev/AppData/Local/Temp/claude/c--Users-dev-SDD-Desafio/0308ef
      d8-0138-44dd-844f-6d25461c434d/scratchpad";
      D="c:/Users/dev/SDD-Desafio/sdd-desafio/spe…)
  ⎿  (No output)

  Read 2 files (ctrl+o to expand)

● Etapa 1 é a AMB-016.

  Made 3 scratchpad edits +64 -9 (ctrl+o to expand)

● Bash(S="C:/Users/dev/AppData/Local/Temp/claude/c--Users-dev-SDD-Desafio/0308ef
      d8-0138-44dd-844f-6d25461c434d/scratchpad"; cp "$S/spec.proposta.md"
      "$S/etapas/s1.md";…)
  ⎿  (No output)

● Etapa 2 é a AMB-008.

  Made 2 scratchpad edits +66 -18 (ctrl+o to expand)

● Bash(S="C:/Users/dev/AppData/Local/Temp/claude/c--Users-dev-SDD-Desafio/0308ef
      d8-0138-44dd-844f-6d25461c434d/scratchpad"; cp "$S/spec.proposta.md"
      "$S/etapas/s2.md";…)
  ⎿  (No output)

● Etapa 3 é a AMB-010.

  Made 5 scratchpad edits +66 -12 (ctrl+o to expand)

● Bash(S="C:/Users/dev/AppData/Local/Temp/claude/c--Users-dev-SDD-Desafio/0308ef
      d8-0138-44dd-844f-6d25461c434d/scratchpad"; cp "$S/spec.proposta.md"
      "$S/etapas/s3.md";…)
  ⎿  (No output)

● Etapa 4 é a decisão transversal de motivos múltiplos.

  Made 6 scratchpad edits +99 -35 (ctrl+o to expand)

● Bash(S="C:/Users/dev/AppData/Local/Temp/claude/c--Users-dev-SDD-Desafio/0308ef
      d8-0138-44dd-844f-6d25461c434d/scratchpad"; cp "$S/spec.proposta.md"
      "$S/etapas/s4.md";…)
  ⎿  (No output)

● Por fim, a entrada D-004 e os diffs por etapa.

  Made 1 scratchpad edit +38 -3 (ctrl+o to expand)

● Bash(S="C:/Users/dev/AppData/Local/Temp/claude/c--Users-dev-SDD-Desafio/0308ef
      d8-0138-44dd-844f-6d25461c434d/scratchpad/etapas"; for i in 1 2 3 4; do
      p=$((i-1)); ech…)
  ⎿  (No output)

● As alterações estão prontas em cópias no scratchpad, montadas em quatro etapas
  para você revisar uma ambiguidade por vez. O diff de cada etapa está acima.
  Para encurtar a leitura, omiti as Justificativas: são o seu texto, copiado
  literalmente. Nenhum arquivo do projeto foi alterado.

  Onde cada coisa entrou

  Ambiguidades, em ordem numérica:
  - AMB-007, AMB-008, AMB-009, AMB-010, AMB-016, AMB-018.
  - A 011 a 015 ainda não estão escritas. Quando forem, entram entre a AMB-010 e
    a AMB-016.

  Regras novas:
  - RN-005: período e pertinência da despesa. Junta a AMB-016 (o que é o
    período) e a AMB-008 (qual dado se compara com ele).
  - RN-006: recusa por despesa fora do período, que vem da AMB-010.
  - RN-007: lista de motivos, que vem da AMB-018.

  Isso repete a separação que já existe entre a RN-003 (quando a NF é exigida) e
  a RN-004 (o efeito da falta).

  Por que registrar a decisão de motivos múltiplos como AMB-018, e não só na §8
  ou na §4:
  - É uma ambiguidade real. Nenhum texto da política a resolve, e ela tem
    leitura rejeitada, justificativa e custo aceito. Fora do formato AMB,
    perderia esse registro.
  - O número 018 deixa o 017 reservado para a ambiguidade que você já definiu.
  - A regra que se pode verificar fica na RN-007, para que os aceites e as tasks
    a referenciem. A ordenação fica na §8, de onde a RN-007 a herda.
  - A §4 ainda está vazia. Quando o schema for escrito, o campo de motivos tem
    de aparecer lá como lista.

  Respostas aos itens 5 e 6

  Item 5 (RN-004). A frase "distinto de qualquer outra causa de recusa" continua
  coerente: os códigos seguem distintos. O que deixou de valer é "com motivo
  SEM_NOTA_FISCAL" no singular, porque dá a entender que é o único motivo. Na
  etapa 4:
  - reescrevi a regra para "com o motivo SEM_NOTA_FISCAL na sua lista de motivos
    (RN-007)";
  - acrescentei "quando mais de uma causa se aplica, os motivos coexistem na
    lista";
  - incluí a AMB-018 na Origem e a RN-007 em "Depende de".

  Item 6. Nenhuma consequência já salva condicionava d-008 ou d-014 a estas
  decisões, porque d-008 não aparecia em lugar nenhum. O único caso surgiu
  dentro deste lote: a consequência da AMB-008 que você ditou dizia "d-008 →
  efeito definido pela AMB-010". Como a AMB-010 é decidida junto, a frase
  nasceria desatualizada. Na etapa 3, troquei por "reembolso 0,00, recusado,
  motivo FORA_DA_COMPETENCIA (RN-006)".

  Acréscimos meus (confira)

  - RN-007: "o reembolso parcial não é motivo de recusa e não entra na lista",
    com o aceite "d-001 → 60,00, parcial; lista vazia". A sua frase "despesa sem
    nenhuma violação → lista vazia" deixa aberto se estourar o limite conta
    como violação. Sem essa linha, quem implementa poderia pôr um motivo de
    "excedente" em d-001.
  - RN-006: um aceite hipotético, "despesa em 01/08, 30,00, com NF → recusada,
    FORA_DA_COMPETENCIA". Ele testa a borda pelo lado de fora.
  - §7: além de d-008 e d-014, acrescentei o caso de dois motivos (150,00, sem
    NF, fora do período).
  - AMB-018, campo "Texto original do RH": escrevi "não há. A dúvida surgiu das
    decisões AMB-005 e AMB-010".

  Varredura: o que encontrei e não alterei

  #: S1
  O quê: "motivo SEM_NOTA_FISCAL" no singular em cerca de 16 aceites,
  consequências e linhas da §7: RN-004, AMB-004, AMB-005, AMB-006, AMB-009 e
  §7. Também os novos "motivo FORA_DA_COMPETENCIA" da RN-006 e da AMB-010.
  Ponto: Não é falso, porque nesses casos a lista tem um só item. Mas quem
  verifica pode ler "motivo X" como valor único, enquanto a RN-007 usa "motivos
   [X]". As opções são padronizar tudo em "motivos [X]" ou declarar na RN-007
  que "motivo X" nos aceites quer dizer "a lista contém exatamente X".
  ────────────────────────────────────────
  #: S2
  O quê: Na Decisão da AMB-005 e da AMB-010, "com motivo …" no singular.
  Ponto: É texto de decisão, não critério de aceite. Recomendo manter.
  ────────────────────────────────────────
  #: S3
  O quê: Na AMB-010 e na RN-006: "não participa de nenhuma agregação ou
  comparação com outras despesas".
  Ponto: Isso já decide parte da AMB-011: um item fora do período não pode ser o

  "original" de um par de duplicatas. Vale registrar essa restrição na §10,
  junto ao item "Diárias e duplicatas".
  ────────────────────────────────────────
  #: S4
  O quê: O caso de d-014 na §7 diz que o "resultado de valor depende da
  AMB-013".
  Ponto: É uma condição pendente dentro da §7, o mesmo tipo de problema que você

  corrigiu na AMB-002 (notas de 18/10). As opções são manter, porque a linha
  afirma só o que já está decidido, ou tirar a frase sobre a AMB-013.
  ────────────────────────────────────────
  #: S5
  O quê: AMB-017 (recusa ou erro de entrada).
  Ponto: Ela decide se um dado inválido, como d-009, entra na lista como motivo
  ou interrompe o processamento do item ou do lote. Também cobre casos de
  período inválido que a AMB-016 não trata: inicio posterior a fim e campos
  ausentes.
  ────────────────────────────────────────
  #: S6
  O quê: AMB-015 (centro_custo).
  Ponto: Não tem interação com nenhuma das decisões deste lote.
  ────────────────────────────────────────
  #: S7
  O quê: AMB-013 (categoria).
  Ponto: CATEGORIA_INELEGIVEL vai precisar de uma posição na etapa de
  elegibilidade da §8. A nota de ordem parcial já diz que a §8 ainda será
  estendida.

  D-004

  Cobre os dois registros já salvos que mudaram: a §8 reescrita e a RN-004. O
  conteúdo novo (AMB-008, 010, 016, 018 e RN-005 a 007) é escrita inicial, por
  isso não entra, como você definiu para as AMB-001 a 006. Se você aprovar o S1,
  a padronização dos aceites antigos também entra na D-004.

  Preciso das suas respostas sobre S1, S3 e S4 e da aprovação das quatro etapas.
  Depois disso, salvo.

> Aprovadas as quatro etapas, com os três ajustes abaixo.

  S1 — padronize. Troque "motivo X" por "motivos [X]" em todos os aceites,
  consequências verificáveis e linhas da §7. A redação no singular contradiz a
  RN-007, que estabelece o campo como lista; quem implementasse a partir de um
  aceite que diz "motivo SEM_NOTA_FISCAL" poderia produzir um campo escalar.
  Essa padronização entra na D-004.

  S2 — mantenha no singular. Concordo: é texto de decisão, não critério
  verificável.

  S3 — registre na §10, junto ao item "Diárias e duplicatas":

  "Despesa fora do período e duplicidade (origem: AMB-010; afeta AMB-011). A
  RN-006 estabelece que uma despesa fora do período não participa de nenhuma
  comparação com outras despesas, o que implica que ela não pode ser o
  original de um par de duplicatas. A AMB-011 deve confirmar essa implicação
  ou declarar exceção."

  S4 — tire a frase sobre a AMB-013. A linha de d-014 na §7 deve afirmar
  apenas o que a RN-005 garante: que a despesa está dentro do período. Mesmo
  princípio da D-001 — premissa pendente dentro de critério de aceite o torna
  não verificável hoje.

  Seus três acréscimos estão aprovados, e o primeiro é o mais importante: sem
  a frase de que o reembolso parcial não é motivo de recusa, alguém
  implementaria um motivo de excedente em d-001.

  A escolha de registrar como AMB-018 está certa, e a justificativa também:
  é ambiguidade real, com leitura rejeitada e custo aceito, e manter o 017
  reservado evita renumeração.

  Pode salvar depois desses ajustes.

● Bash(S="C:/Users/dev/AppData/Local/Temp/claude/c--Users-dev-SDD-Desafio/0308ef
      d8-0138-44dd-844f-6d25461c434d/scratchpad"; grep -n -E "motivo( |$)"
      "$S/spec.proposta.…)
  ⎿  (No output)

● Bash(S="C:/Users/dev/AppData/Local/Temp/claude/c--Users-dev-SDD-Desafio/0308ef
      d8-0138-44dd-844f-6d25461c434d/scratchpad"; for n in 141 143 376 378 427
      593; do sed -n…)
  ⎿  (No output)

● Bash(S="C:/Users/dev/AppData/Local/Temp/claude/c--Users-dev-SDD-Desafio/0308ef
      d8-0138-44dd-844f-6d25461c434d/scratchpad"; F="$S/spec.proposta.md"; cp
      "$F" "$S/etapas…)
  ⎿  (No output)

● Bash(S="C:/Users/dev/AppData/Local/Temp/claude/c--Users-dev-SDD-Desafio/0308ef
      d8-0138-44dd-844f-6d25461c434d/scratchpad"; F="$S/spec.proposta.md"; cmp
      "$F" "$S/etapa…)
  ⎿  (No output)

● Restam só textos de regra e de decisão, que ficam no singular, como você
  definiu no S2. Agora o S3, o S4 e a D-004.

  Made 3 scratchpad edits +22 -10, read 1 file (ctrl+o to expand)

● Bash(S="C:/Users/dev/AppData/Local/Temp/claude/c--Users-dev-SDD-Desafio/0308ef
      d8-0138-44dd-844f-6d25461c434d/scratchpad";
      D="c:/Users/dev/SDD-Desafio/sdd-desafio/spe…)
  ⎿  Updated DECISIONS.md (+42 -0)
      10  
      11  ---
      12  
      13 +## D-004 — Ordem de aplicação e motivo de recusa reescritos apó
         +s AMB-008, 010, 016 e 018 · `2026-10-18`                       
      14 +                                                               
      15 +**Gatilho:** a decisão de motivos múltiplos (AMB-018), tornada 
         +necessária pelo                                                
      16 +segundo motivo de recusa criado na AMB-010, alterou registros j
         +á salvos: a                                                    
      17 +§8, que tratava a recusa por nota fiscal como interrupção da av
         +aliação, e a                                                   
      18 +RN-004, que declarava um motivo único.                         
      19 +                                                               
      20 +**O que mudou na spec:**                                       
      21 +- §8: de uma sequência em que "se a nota é exigida e ausente, o
         + item é                                                        
      22 +  recusado (RN-004) e não segue para o passo 2" para duas etapa
         +s —                                                            
      23 +  elegibilidade, em que todas as regras são avaliadas e seus mo
         +tivos                                                          
      24 +  coletados na ordem período → nota fiscal, e valor, aplicada a
         +penas a                                                        
      25 +  despesas sem motivo de recusa. A ordem dentro da elegibilidad
         +e passa a                                                      
      26 +  definir a ordenação da lista de motivos.                     
      27 +- RN-004, Regra: de "classificada como recusada, com motivo SEM
         +_NOTA_FISCAL"                                                  
      28 +  para "com o motivo SEM_NOTA_FISCAL na sua lista de motivos (R
         +N-007)";                                                       
      29 +  acrescentado que, quando mais de uma causa se aplica, os moti
         +vos coexistem                                                  
      30 +  na lista. Origem: acrescentada a AMB-018. Depende de: acresce
         +ntada a                                                        
      31 +  RN-007.                                                      
      32 +- Aceites, consequências verificáveis e linhas da §7 (RN-004, A
         +MB-004,                                                        
      33 +  AMB-005, AMB-006, AMB-009 e §7, além dos novos RN-006, AMB-00
         +8 e AMB-010):                                                  
      34 +  de "motivo SEM_NOTA_FISCAL" e "motivo FORA_DA_COMPETENCIA" pa
         +ra                                                             
      35 +  "motivos [SEM_NOTA_FISCAL]" e "motivos [FORA_DA_COMPETENCIA]"
         +. O texto das                                                  
      36 +  regras e das decisões mantém o singular, por não ser critério
         + verificável.                                                  
      37 +                                                               
      38 +**Por quê:** a spec descreve o estado atual do sistema. Com doi
         +s motivos de                                                   
      39 +recusa independentes e a decisão de reportar todos, uma §8 que 
         +interrompe a                                                   
      40 +avaliação na primeira recusa, uma RN-004 com motivo único e ace
         +ites que                                                       
      41 +descrevem o motivo como valor escalar contradiriam a RN-007 — q
         +uem                                                            
      42 +implementasse a partir de um aceite no singular poderia produzi
         +r um campo                                                     
      43 +escalar.                                                       
      44 +                                                               
      45 +**O que isso invalidou:** nenhum critério de aceite. O valor re
         +embolsado de                                                   
      46 +todos os itens permanece o mesmo; muda apenas a representação d
         +os motivos e                                                   
      47 +a garantia de que todos são reportados.                        
      48 +                                                               
      49 +**Tasks afetadas:** nenhuma — tasks.md ainda não foi escrito.  
     … 8 more lines (ctrl+o to expand)
  ⎿  Updated spec.md (+268 -42)
      123  ### RN-004 — Recusa integral por ausência de nota fiscal exigi
           da
      124  
      125  **Regra:** uma despesa que exige nota fiscal conforme a RN-003
            e não a
      126 -apresenta tem reembolso 0,00 e é classificada como recusada, c
          -om motivo                                                     
      127 -SEM_NOTA_FISCAL. A recusa é integral e independe do limite da 
          -categoria: o                                                  
      128 -item não é avaliado contra o limite (RN-001, RN-002), e um lan
          -çamento que                                                   
      129 -seria integral caso tivesse nota é recusado da mesma forma. O 
          -motivo                                                        
      130 -SEM_NOTA_FISCAL é distinto de qualquer outra causa de recusa, 
          -em particular                                                 
      131 -de CATEGORIA_INELEGIVEL.                                      
      132 -**Origem:** política do RH, item 5; decisão AMB-005           
      133 -**Depende de:** RN-003 (exigência de nota fiscal).            
      126 +apresenta tem reembolso 0,00 e é classificada como recusada, c
          +om o motivo                                                   
      127 +SEM_NOTA_FISCAL na sua lista de motivos (RN-007). A recusa é i
          +ntegral e                                                     
      128 +independe do limite da categoria: o item não é avaliado contra
          + o limite                                                     
      129 +(RN-001, RN-002), e um lançamento que seria integral caso tive
          +sse nota é                                                    
      130 +recusado da mesma forma. O motivo SEM_NOTA_FISCAL é distinto d
          +e qualquer                                                    
      131 +outra causa de recusa, em particular de CATEGORIA_INELEGIVEL; 
          +quando mais de                                                
      132 +uma causa se aplica, os motivos coexistem na lista.           
      133 +**Origem:** política do RH, item 5; decisões AMB-005 e AMB-018
      134 +**Depende de:** RN-003 (exigência de nota fiscal); RN-007 (lis
          +ta de motivos).                                               
      135  **Aceite:**
      136  - d-004 (06/07, transporte urbano, 100,01, sem NF) → reembolso
            0,00,
      136 -  recusado, motivo SEM_NOTA_FISCAL.                           
      137 +  recusado, motivos [SEM_NOTA_FISCAL].                        
      138  - d-013 (22/07, hospedagem, 690,00, sem NF) → reembolso 0,00, 
           recusado,
      138 -  motivo SEM_NOTA_FISCAL.                                     
      139 -- Alimentação de 500,00, sem NF → reembolso 0,00, recusado, mo
          -tivo                                                          
      140 -  SEM_NOTA_FISCAL, embora o teto da categoria seja 60,00.     
      141 -- Hospedagem de 200,00, sem NF → reembolso 0,00, recusado, mot
          -ivo                                                           
      142 -  SEM_NOTA_FISCAL; com NF, seria 200,00, integral.            
      139 +  motivos [SEM_NOTA_FISCAL].                                  
      140 +- Alimentação de 500,00, sem NF → reembolso 0,00, recusado, mo
          +tivos                                                         
      141 +  [SEM_NOTA_FISCAL], embora o teto da categoria seja 60,00.   
      142 +- Hospedagem de 200,00, sem NF → reembolso 0,00, recusado, mot
          +ivos                                                          
      143 +  [SEM_NOTA_FISCAL]; com NF, seria 200,00, integral.          
      144  - d-003 (06/07, transporte urbano, 100,00, sem NF) → nota fisc
           al não exigida;
      145    esta regra não se aplica; reembolso 80,00, parcial.
      146  - Transporte urbano de 100,01, com NF → esta regra não se apli
           ca; reembolso
      147    80,00, parcial.
      148  
     … 344 more lines (ctrl+o to expand)

● Salvei spec.md (specs/001-motor-reembolso/spec.md) e DECISIONS.md
  (specs/001-motor-reembolso/DECISIONS.md) e conferi byte a byte: estão iguais
  às cópias aprovadas.

  Ajustes feitos antes de salvar
  - S1: troquei "motivo X" por "motivos [X]" em 22 critérios verificáveis
    (aceites, consequências e linhas da §7). Ficaram no singular só seis frases,
    todas de regra ou decisão: o texto das regras da RN-004 e da RN-006, a
    Decisão da AMB-005 e a da AMB-010, e duas frases em que "motivo" é palavra
    comum.
  - S2: mantive o singular no texto das decisões, como você pediu.
  - S3: registrei na §10 "Despesa fora do período e duplicidade", logo depois de
    "Diárias e duplicatas".
  - S4: a linha de d-014 na §7 agora diz só que a despesa está dentro do período
    e não recebe motivo de recusa por período.
  - D-004: passou a incluir a padronização do S1. O "Por quê" registra o risco
    de quem implementar ler o campo como um valor único.

  Como a spec ficou
  - Regras RN-001 a RN-007.
  - Ambiguidades 001 a 010, 016 e 018. A 011 a 015 e a 017 continuam pendentes.
    O número 017 está reservado.
  - Registro de mudanças da D-004 à D-001.

  Nada foi commitado. O lote agora tem quatro entradas de mudança (D-001 a
  D-004) e as ambiguidades novas.