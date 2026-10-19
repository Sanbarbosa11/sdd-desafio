
> /clear

> Leia specs/001-motor-reembolso/tasks.md, plan.md e CLAUDE.md.

  Vamos executar a T-001: configuração do ambiente. JAVA_HOME apontando para o
  JDK 21 do Android Studio, Gradle Wrapper 8.11.1 a partir do cache,
  build.gradle.kts com os source sets de src/ e tests/, alvo Java 17 conforme
  a DT-001, e as dependências.

  Preencha também a seção "Stack e comandos" do CLAUDE.md, que ainda está com
  os marcadores do template.

  Critério de aceite da task: ./gradlew --version mostra 8.11.1 e ./gradlew
  build termina com sucesso.

  Antes de executar, me diga o que vai fazer e o que precisa que eu rode no
  terminal — a configuração do JAVA_HOME é ambiente, não código.

  Ao concluir, marque a T-001 como [x] no tasks.md no mesmo commit.

● Bash(cat CLAUDE.md && echo ===== && cat specs/001-motor-reembolso/plan.md &&
      echo ===== && cat specs/001-motor-reembolso/tasks.md && echo ===== && ls
      -la && git stat…)
  ⎿  Error: Exit code 1
     cat: CLAUDE.md: No such file or directory

  Listed 1 directory (ctrl+o to expand)

● Bash(cd /c/Users/dev/SDD-Desafio/sdd-desafio && find . -not -path './.git*' |
      head -80 && git status && git log --oneline | head -10 && echo ===== &&
      cat CLAUDE.md &…)
  ⎿  (No output)

  Read 1 file (ctrl+o to expand)

● Agora vou checar o ambiente (somente leitura) para que o plano se baseie em
  fatos.

● Bash(cd /c/Users/dev/SDD-Desafio/sdd-desafio
      echo "JAVA_HOME=[$JAVA_HOME]"; which java 2>&1…)
  ⎿  (No output)

● Li os três arquivos e chequei o ambiente sem alterar nada. Antes do plano, o
  que encontrei:

  Estado do ambiente
  - O JDK 21.0.5 está em C:\Program Files\Android\Android Studio\jbr e funciona.
    JAVA_HOME está vazio (usuário e máquina) e não há java no PATH.
  - A distribuição do Gradle 8.11.1 e o plugin Kotlin 2.0.21 já estão no cache.
  - R-02 se confirma: kotlinx-serialization-json 1.7.3 não está no cache, e do
    junit-bom só há 5.10.2 e 5.9.2. Por isso o primeiro ./gradlew build vai
    precisar de rede.
  - O repo está limpo na main, src/ e tests/ estão vazios, e o .gitignore já
    ignora build/, mas não .gradle/ nem .kotlin/.

  O que você precisa rodar no terminal

  No PowerShell (fica gravado no escopo do usuário, sem precisar de admin):

  [Environment]::SetEnvironmentVariable('JAVA_HOME', 'C:\Program
  Files\Android\Android Studio\jbr', 'User')

  Depois feche e reabra o VS Code, para que os terminais e eu herdemos a
  variável. Para conferir: $env:JAVA_HOME e & "$env:JAVA_HOME\bin\java"
  -version. Colocar o java no PATH não é obrigatório, porque o Gradle só usa o
  JAVA_HOME.

  O que eu vou fazer

  1. Wrapper: rodo gradle wrapper --gradle-version 8.11.1 a partir da
     distribuição que está no cache. Isso gera gradlew, gradlew.bat e
     gradle/wrapper/ (jar + properties), todos versionados.
  2. settings.gradle.kts: rootProject.name = "reembolso".
  3. build.gradle.kts:
     - Kotlin JVM 2.0.21 e o plugin application com mainClass =
       "reembolso.cli.MainKt".
     - Alvo Java 17 conforme a DT-001: jvmTarget = JVM_17, -Xjdk-release=17 e
       JavaCompile.options.release = 17.
     - Source sets da DT-002: src/ e tests/, sem recursos.
     - Dependências: kotlinx-serialization-json:1.7.3, junit-bom:5.11.x,
       kotlin-test-junit5 e junit-platform-launcher, com useJUnitPlatform().
  4. Código mínimo para o aceite. A task exige AmbienteTest › build compila src
     e executa testes de tests, e com src/ vazio não há nada para compilar.
     Proponho:
     - src/reembolso/cli/Main.kt com um fun main(args: Array<String>) {} vazio,
       só um esqueleto. O comportamento real fica para a T-019.
     - tests/reembolso/AmbienteTest.kt: carrega reembolso.cli.MainKt do
       classpath, o que prova que src/ compilou, e verifica que o bytecode é da
       versão 61 (Java 17). Esse segundo teste garante a DT-001, não só a
       configura.
  5. .gitignore: acrescento .gradle/ e .kotlin/.
  6. CLAUDE.md, seção "Stack e comandos":
     - Linguagem: Kotlin 2.0.21 / JVM 17 (o Gradle roda no JDK 21 via
       JAVA_HOME).
     - Rodar: ./gradlew run --args="calcular --input
       exemplos/despesas-exemplo.json --output saida.json".
     - Testes: ./gradlew test.
     - Lint: nenhum, ./gradlew build é o check.
     - Não mexo em "Convenções de código" e "Fora de escopo", que também ainda
       estão com os marcadores, porque não foram pedidas.
  7. Verificação: ./gradlew --version precisa mostrar 8.11.1 no JVM 21, e
     ./gradlew build precisa passar.
  8. Commit único na main, como o histórico vem fazendo, com a T-001 marcada
     [x]. Mensagem: feat(T-001): configura Gradle Wrapper 8.11.1, build Kotlin
     com alvo Java 17 e source sets src/tests. O campo "Commit:" da task fica
     "—", porque o hash só existe depois do commit. Se quiser, preencho num
     docs(tasks): em seguida.

  Preciso que você confirme

  - O JAVA_HOME foi gravado e o VS Code foi reiniciado. Se preferir não
    reiniciar agora, eu sigo passando o JAVA_HOME só nos meus comandos, mas o
    aceite deve valer no seu terminal.
  - O esqueleto Main.kt vazio e o teste da versão de bytecode estão ok.
  - O commit vai direto na main, ou prefere uma branch.

> [Environment]::SetEnvironmentVariable('JAVA_HOME', 'C:\Program
  Files\Android\Android Studio\jbr', 'User')

  rode isso

● PowerShell([Environment]::SetEnvironmentVariable('JAVA_HOME', 'C:\Program
            Files\Android\Android Studio\jbr', 'User');
            [Environment]::GetEnvironmentVariable('JAVA_HOME','Us…)
  ⎿  C:\Program Files\Android\Android Studio\jbr

● Rodei. O JAVA_HOME do seu usuário agora aponta para C:\Program
  Files\Android\Android Studio\jbr, conferi lendo a variável de volta.

  Ela só vale para processos novos. Os terminais abertos e o VS Code ainda não a
  enxergam, então reinicie o VS Code para o ./gradlew funcionar no seu
  terminal. Até lá, eu posso passar o JAVA_HOME só nos meus comandos.

  Ainda faltam duas respostas para eu começar a T-001:
  - Se o esqueleto Main.kt vazio e o AmbienteTest, que também confere que o
    bytecode é Java 17, estão ok.
  - Se o commit vai direto na main ou numa branch.

> $env:JAVA_HOME

● PowerShell("[$env:JAVA_HOME]"; if ($env:JAVA_HOME) { &
            "$env:JAVA_HOME\bin\java" -version })
  ⎿  [C:\Program Files\Android\Android Studio\jbr]
     openjdk version "21.0.5" 2024-10-15
     OpenJDK Runtime Environment (build 21.0.5+-13047016-b750.29)
     OpenJDK 64-Bit Server VM (build 21.0.5+-13047016-b750.29, mixed mode)

● O JAVA_HOME já aparece nesta sessão: $env:JAVA_HOME mostra C:\Program
  Files\Android\Android Studio\jbr. O java -version por ele responde OpenJDK
  21.0.5. O ambiente está pronto.

  Para começar a T-001, preciso de duas respostas:
  1. O esqueleto Main.kt vazio e o AmbienteTest, que confere também que o
     bytecode é Java 17, estão ok?
  2. O commit vai direto na main ou numa branch?

> Sim para as duas.

  1. Main.kt vazio e AmbienteTest com a verificação de bytecode: aprovados. O
     teste de versão de bytecode é melhor que o critério original da task,
  porque
     verifica a DT-001 em vez de apenas configurá-la.
  2. Commit direto na main. O histórico inteiro está nela e o enunciado não
     pede pull request.

  Um acréscimo: preencha também "Convenções de código" e "Fora de escopo" no
  CLAUDE.md. Eles ainda estão com os marcadores do template, e o CLAUDE.md é
  artefato entregável — marcadores não preenchidos num documento de entrega
  contam contra na avaliação. Derive o conteúdo do plan.md e da spec: a
  fronteira entre domínio e adaptadores, a representação monetária, a
  convenção de nomes de teste com os IDs de RN e AMB, e o que o projeto não
  faz.

  Sobre o campo "Commit:" das tasks: deixe "—" por enquanto e preencha todos
  ao final, num docs(tasks): próprio. Preencher um a um geraria um commit
  extra por task.

  Pode executar a T-001.

● Bash(cd /c/Users/dev/SDD-Desafio/sdd-desafio && grep -n '^## \|^### '
      specs/001-motor-reembolso/spec.md | head -60; sed -n '1,80p'
      specs/001-motor-reembolso/spec.md;…)
  ⎿  (No output)

● Bash(sed -n '1644,1700p' specs/001-motor-reembolso/spec.md; export
      JAVA_HOME="/c/Program Files/Android/Android Studio/jbr";
      G=~/.gradle/wrapper/dists/gradle-8.11.1-b…)
  ⎿  Created .gradle\8.11.1\gc.properties (+0 -0)
  ⎿  Created .gradle\vcs-1\gc.properties (+0 -0)
  ⎿  Created .gradle\buildOutputCleanup\cache.properties (+2 -0)
      1 +#Sun Oct 18 22:36:20 BRT 2026                                   
      2 +gradle.version=8.11.1                                           
  ⎿  Created gradle\wrapper\gradle-wrapper.properties (+7 -0)
      1 +distributionBase=GRADLE_USER_HOME                               
      2 +distributionPath=wrapper/dists                                  
      3 +distributionUrl=https\://services.gradle.org/distributions/gradl
        +e-8.11.1-bin.zip                                                
      4 +networkTimeout=10000                                            
      5 +validateDistributionUrl=true                                    
      6 +zipStoreBase=GRADLE_USER_HOME                                   
      7 +zipStorePath=wrapper/dists                                      
  ⎿  Created gradlew (+252 -0)
        1 +#!/bin/sh                                                     
        2 +                                                              
        3 +#                                                             
        4 +# Copyright © 2015-2021 the original authors.                 
        5 +#                                                             
        6 +# Licensed under the Apache License, Version 2.0 (the "License
          +");                                                           
        7 +# you may not use this file except in compliance with the Lice
          +nse.                                                          
        8 +# You may obtain a copy of the License at                     
        9 +#                                                             
       10 +#      https://www.apache.org/licenses/LICENSE-2.0            
       11 +#                                                             
       12 +# Unless required by applicable law or agreed to in writing, s
          +oftware                                                       
       13 +# distributed under the License is distributed on an "AS IS" B
          +ASIS,                                                         
       14 +# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
          + or implied.                                                  
       15 +# See the License for the specific language governing permissi
          +ons and                                                       
       16 +# limitations under the License.                              
       17 +#                                                             
       18 +# SPDX-License-Identifier: Apache-2.0                         
       19 +#                                                             
       20 +                                                              
       21 +##############################################################
          +################                                              
       22 +#                                                             
       23 +#   Gradle start up script for POSIX generated by Gradle.     
       24 +#                                                             
       25 +#   Important for running:                                    
       26 +#                                                             
       27 +#   (1) You need a POSIX-compliant shell to run this script. I
          +f your /bin/sh is                                             
       28 +#       noncompliant, but you have some other compliant shell 
          +such as ksh or                                                
       29 +#       bash, then to run this script, type that shell name be
          +fore the whole                                                
       30 +#       command line, like:                                   
       31 +#                                                             
       32 +#           ksh Gradle                                        
       33 +#                                                             
       34 +#       Busybox and similar reduced shells will NOT work, beca
          +use this script                                               
       35 +#       requires all of these POSIX shell features:           
       36 +#         * functions;                                        
       37 +#         * expansions «$var», «${var}», «${var:-default}», «$
          +{var+SET}»,                                                   
       38 +#           «${var#prefix}», «${var%suffix}», and «$( cmd )»; 
       39 +#         * compound commands having a testable exit status, e
          +specially «case»;                                             
       40 +#         * various built-in commands including «command», «se
          +t», and «ulimit».                                             
     … 212 more lines (ctrl+o to expand)
  ⎿  … 12 more files changed
  ⎿  (timeout 5m)

● Wrapper gerado. Agora os arquivos de build e o código mínimo.

● Write(build.gradle.kts)
  ⎿  Wrote 51 lines to build.gradle.kts
       1 import org.jetbrains.kotlin.gradle.dsl.JvmTarget
       2
       3 plugins {
       4     kotlin("jvm") version "2.0.21"
       5     application
       6 }
       7
       8 repositories {
       9     mavenCentral()
      10 }
     … +44 lines (ctrl+o to expand)

● Write(src\reembolso\cli\Main.kt)
  ⎿  Wrote 4 lines to src\reembolso\cli\Main.kt
      1 package reembolso.cli
      2
      3 fun main(args: Array<String>) {
      4 }

● Write(tests\reembolso\AmbienteTest.kt)
  ⎿  Wrote 26 lines to tests\reembolso\AmbienteTest.kt
       1 package reembolso
       2
       3 import java.io.DataInputStream
       4 import kotlin.test.Test
       5 import kotlin.test.assertEquals
       6 import kotlin.test.assertNotNull
       7
       8 class AmbienteTest {
       9
      10     @Test
     … +19 lines (ctrl+o to expand)

● Bash(cat >> .gitignore <<'EOF'…)
  ⎿  Updated .gitignore (+49 -45)
       1 -# -------------------------------------------------------------
         ---------------                                                 
       2 -# MATERIAL DO INSTRUTOR — NUNCA COMMITAR NESTE REPOSITORIO     
       3 -# Contem o envelope lacrado do Dia 2, o gabarito das ambiguidad
         -es e o                                                         
       4 -# avaliador. Publicar qualquer um destes arquivos invalida o de
         -safio.                                                         
       5 -# -------------------------------------------------------------
         ---------------                                                 
       6 -instrutor/                                                     
       1 +# -------------------------------------------------------------
         +--------------                                                 
       2 +# MATERIAL DO INSTRUTOR — NUNCA COMMITAR NESTE REPOSITORIO     
       3 +# Contem o envelope lacrado do Dia 2, o gabarito das ambiguidad
         +es e o                                                         
       4 +# avaliador. Publicar qualquer um destes arquivos invalida o de
         +safio.                                                         
       5 +# -------------------------------------------------------------
         +--------------                                                 
       6 +instrutor/                                                     
       7 +                                                               
       8 +# Pareceres de correcao, caso sejam gerados dentro do repo     
       9 +PARECER-*.md                                                   
      10 +                                                               
      11 +# -------------------------------------------------------------
         +--------------                                                 
      12 +# Ruido de ambiente                                            
      13 +# -------------------------------------------------------------
         +--------------                                                 
      14 +.DS_Store                                                      
      15 +Thumbs.db                                                      
      16 +desktop.ini                                                    
      17 +*.swp                                                          
      18 +*~                                                             
      19 +                                                               
      20 +.idea/                                                         
      21 +.vscode/                                                       
      22 +*.code-workspace                                               
      23 +                                                               
      24 +.env                                                           
      25 +.env.*                                                         
      26 +!.env.example                                                  
      27 +                                                               
      28 +# Dependencias e artefatos (stack livre — cobrindo as mais prov
         +aveis)                                                         
      29 +node_modules/                                                  
      30 +dist/                                                          
      31 +build/                                                         
      32 +coverage/                                                      
      33 +.venv/                                                         
      34 +venv/                                                          
     … 55 more lines (ctrl+o to expand)
  ⎿  … 7 more files changed
  ⎿  (timeout 10m)

● Bash(grep -h -o '<testsuite [^>]*>\|<testcase name="[^"]*"'
      build/test-results/test/*.xml; git status --short)
  ⎿  (No output)

● Build verde com os 2 testes. Agora CLAUDE.md e tasks.md.
● Update(CLAUDE.md)
  ⎿  Added 34 lines, removed 7 lines
      34  
      35  ## Stack e comandos
      36  
      37 -- Linguagem: `<...>`                                           
      38 -- Rodar: `<comando>`                                           
      39 -- Testes: `<comando>`                                          
      40 -- Lint/format: `<comando>`                                     
      37 +- Linguagem: Kotlin 2.0.21, bytecode e API do Java 17 (DT-001).
         + Gradle 8.11.1                                                 
      38 +  via wrapper; exige `JAVA_HOME` com JDK 17+ (nesta máquina, o 
         +JDK 21 do                                                      
      39 +  Android Studio).                                             
      40 +- Rodar: `./gradlew run --args="calcular --input exemplos/despe
         +sas-exemplo.json --output saida.json"`                         
      41 +- Testes: `./gradlew test` (um só: `./gradlew test --tests 'ree
         +mbolso.regras.RN002*'`)                                        
      42 +- Lint/format: nenhum; `./gradlew build` é o check.            
      43  
      44  ## Convenções de código
      45  
      44 -- `<nomenclatura, estrutura de pastas, tratamento de erro, o qu
         -e for relevante>`                                              
      45 -- Valores monetários: `<como são representados — decimal, centa
         -vos em inteiro, etc.>`                                         
      46 +- Layout (DT-002): `src/` e `tests/` na raiz, pacotes direto ab
         +aixo delas                                                     
      47 +  (`src/reembolso/dominio/…`). Sem pasta de recursos.          
      48 +- Fronteira (plan §2): `reembolso.dominio` contém toda regra e 
         +só usa                                                         
      49 +  `java.time` e `java.math` — nunca `kotlinx.*` nem `java.io.*`
         + (DT-009, teste                                                
      50 +  de arquitetura). `entrada`, `saida` e `cli` traduzem e não de
         +cidem nada.                                                    
      51 +- Entrada: árvore JSON traduzida para `Lido<T>` por campo (DT-0
         +04); campos sem                                                
      52 +  regra não são lidos.                                         
      53 +- Texto: `aparar()` do domínio, nunca `.trim()`/`.strip()` (DT-
         +005, R-05);                                                    
      54 +  minúsculas com `lowercase()`.                                
      55 +- Valores monetários: `BigDecimal` encapsulado em `value class 
         +Valor`, sempre                                                 
      56 +  escala 2 com `HALF_UP`, criado do texto cru do JSON e escrito
         + com                                                           
      57 +  `toPlainString()`. Nunca `Double` nem `Long` em centavos (DT-
         +003, R-04).                                                    
      58 +- Política (limites, limiar de NF): constantes em `src/reembols
         +o/dominio/Politica.kt`,                                        
      59 +  não em arquivo de configuração.                              
      60 +- Erros de entrada são resultado (`ResultadoLote.Rejeitado`, `R
         +esultadoDespesa.ComErro`),                                     
      61 +  não exceção. Exit codes da CLI na DT-008.                    
      62 +- Nomes de teste entre crases, começando pelo ID do requisito (
         +`RN-002`,                                                      
      63 +  `§7`, `§4-1`…) e, quando verificam uma ambiguidade, também o 
         +`AMB-xxx`. Um                                                  
      64 +  arquivo por RN em `tests/reembolso/regras/`. O `Rastreabilida
         +deTest` usa                                                    
      65 +  esses nomes para exigir cobertura.                           
      66  
      67  ## Fora de escopo
      68  
      49 -- `<o que este projeto explicitamente não faz — evita que o age
         -nte invente feature>`                                          
      69 +- Determinar se o colaborador está em viagem e aplicar a amplia
         +ção de 50% da                                                  
      70 +  política (spec §3, AMB-007).                                 
      71 +- Dar efeito a campos sem regra: dados do colaborador, rótulo d
         +e competência,                                                 
      72 +  descrição (spec §3, AMB-015).                                
      73 +- Teto diário agregado, comprovação alternativa à nota fiscal, 
         +tratamento de                                                  
      74 +  dia não útil (spec §10).                                     
      75 +- Tudo que não é a CLI `calcular`: sem API, banco, interface ou
         + arquivo de                                                    
      76 +  configuração da política.                                    

● Update(specs\001-motor-reembolso\tasks.md)
  ⎿  Added 1 line, removed 1 line
      32  
      33  ## Fase 1 — Fundação
      34  
      35 -- [ ] **T-001** — Configurar o ambiente (R-01): `JAVA_HOME` apo
         -ntando para o                                                  
      35 +- [x] **T-001** — Configurar o ambiente (R-01): `JAVA_HOME` apo
         +ntando para o                                                  
      36    JDK 21 do Android Studio; Gradle Wrapper 8.11.1 gerado a part
          ir da
      37    distribuição do cache e versionado; `build.gradle.kts` com Ko
          tlin 2.0.21,
      38    plugin `application`, alvo Java 17 (`jvmTarget`, `-Xjdk-relea
          se=17`,

● Bash(export JAVA_HOME="/c/Program Files/Android/Android Studio/jbr" &&
      ./gradlew build -q 2>&1 | tail -3; git add -A && git update-index
      --chmod=+x gradlew && git st…)
  ⎿  2 files changed (binary, mode only or too large to show)

● A T-001 está concluída e commitada na main (3c18414, sem push). Os dois
  critérios de aceite passaram:

  - ./gradlew --version: mostra Gradle 8.11.1 rodando no JDK 21.0.5 do Android
    Studio.
  - ./gradlew build: termina com sucesso e executa os dois testes do
    AmbienteTest, sem falhas.
    - build compila src e executa testes de tests
    - DT-001 bytecode de src é Java 17, que confere que o bytecode é da versão
      61 (Java 17).

  O que entrou no commit
  - O wrapper 8.11.1, gerado a partir do cache, sem baixar nada.
  - build.gradle.kts e settings.gradle.kts, com o alvo Java 17 (DT-001) e os
    source sets src/ e tests/ (DT-002).
  - Do JUnit, usei a versão 5.11.3, já que a task só fixava 5.11. Como previsto
    no R-02, ela e a kotlinx-serialization-json foram baixadas da internet no
    primeiro build.
  - Main.kt (src/reembolso/cli/Main.kt) vazio e AmbienteTest.kt
    (tests/reembolso/AmbienteTest.kt).
  - CLAUDE.md com as três seções preenchidas a partir do plan e da spec. "Fora
    de escopo" segue a §3 e a §10 da spec.
  - .gitignore agora ignora .gradle/ e .kotlin/.
  - T-001 marcada [x], com "Commit:" deixado em "—".

  Três pontos para você avaliar:
  - O ./gradlew --version mostra "Kotlin: 2.0.20". Esse é o Kotlin embutido no
    próprio Gradle. O projeto compila com o plugin 2.0.21.
  - O topo do CLAUDE.md ainda tem a nota do template ("Substitua os <...> e
    apague o que não usar…"). Não mexi porque você não pediu, mas talvez valha
    remover pelo mesmo motivo dos marcadores.
  - O git avisou que vai converter as quebras de linha para CRLF nesta máquina.
    Um .gitattributes com gradlew text eol=lf impediria que o gradlew seja
    gravado com CRLF a partir do Windows e deixe de rodar em Linux ou macOS.
    Posso adicionar se quiser.

> Pontos 2 e 3: sim para ambos.

  Ponto 3 primeiro, porque é o mais sério. Se o gradlew for gravado com CRLF,
  ele não executa em Linux nem macOS — o shebang quebra — e o avaliador
  provavelmente não está no Windows. Acrescente o .gitattributes com

  gradlew text eol=lf
  *.sh text eol=lf
  e verifique se o gradlew que já está no commit 3c18414 foi gravado com CRLF.
  Se foi, corrija e refaça o arquivo no índice (git add --renormalize ou
  equivalente), de modo que o que está versionado tenha LF. Confirme depois
  com git show HEAD:gradlew lido em modo binário, ou outro meio que mostre o
  terminador de linha real do blob.

  Ponto 2: remova a nota do template no topo do CLAUDE.md, pelo mesmo motivo
  dos marcadores — é instrução de preenchimento num documento de entrega.

  Ponto 1: entendido, nada a fazer. O Kotlin embutido no Gradle é outra coisa
  que o plugin do projeto.

  Faça os dois num commit próprio: chore: gitattributes para LF no gradlew e
  limpeza do CLAUDE.md. Depois disso eu faço o push.

● Bash(echo "blob CRLF count:"; git show HEAD:gradlew | grep -c $'\r'; echo
      "worktree CRLF count:"; grep -c $'\r' gradlew; git config core.autocrlf;
      head -8 CLAUDE.md…)
  ⎿  (No output)

● O blob do gradlew no commit 3c18414 já está em LF, mas mesmo assim o
  .gitattributes é necessário para garantir que futuros checkouts nesta máquina
  não convertam o arquivo para CRLF.

● Bash(printf 'gradlew text eol=lf\n*.sh text eol=lf\n' > .gitattributes && sed
      -i '3,7d' CLAUDE.md && head -4 CLAUDE.md && git add .gitattributes
      CLAUDE.md && git add…)
  ⎿  Created .gitattributes (+2 -0)
      1 +gradlew text eol=lf                                             
      2 +*.sh text eol=lf                                                
  ⎿  Updated CLAUDE.md (+71 -76)
       1 -# CLAUDE.md                                                    
       2 -                                                               
       3 -> Este arquivo é lido pelo Claude Code no início de toda sessão
         -. É onde moram as                                              
       4 -> convenções que você não quer repetir em todo prompt.         
       5 -> Substitua os `<...>` e apague o que não usar. Mantenha curto 
         -— CLAUDE.md longo                                              
       6 -> é CLAUDE.md ignorado.                                        
       7 -                                                               
       8 -## O projeto                                                   
       9 -                                                               
      10 -Motor de cálculo de reembolso de despesas corporativas. CLI que
         - lê um JSON de                                                 
      11 -despesas e emite um JSON com o valor reembolsável e a justifica
         -tiva de cada item.                                             
      12 -                                                               
      13 -## Fonte da verdade                                            
      14 -                                                               
      15 -`specs/001-motor-reembolso/spec.md` define **o que** o sistema 
         -faz.                                                           
      16 -`specs/001-motor-reembolso/plan.md` define **como**.           
      17 -`specs/001-motor-reembolso/tasks.md` define **em que ordem**.  
      18 -                                                               
      19 -Quando o código e a spec discordarem, a spec está certa e o cód
         -igo é o bug —                                                  
      20 -a menos que a spec esteja errada, e nesse caso corrigimos a spe
         -c primeiro e                                                   
      21 -registramos em `DECISIONS.md`.                                 
      22 -                                                               
      23 -**Antes de implementar qualquer coisa, leia a task corresponden
         -te em `tasks.md`.**                                            
      24 -Se o que eu pedi não está coberto por nenhuma task, me avise em
         - vez de implementar.                                           
      25 -                                                               
      26 -## Regras de trabalho                                          
      27 -                                                               
      28 -- Toda regra de negócio vive na spec, não no chat e não em come
         -ntário de código.                                              
      29 -- Se eu te explicar uma regra que não está na spec, **pare e me
         - diga isso** antes                                             
      30 -  de escrever código. Isso é um bug de spec.                   
      31 -- Todo commit referencia uma task: `feat(T-003): <descrição>`. 
      32 -  Mudanças de documentação: `docs(spec):`, `docs(plan):`, `docs
         -(tasks):`.                                                     
      33 -- Nenhuma regra de negócio entra sem teste.                    
      34 -                                                               
      35 -## Stack e comandos                                            
      36 -                                                               
      37 -- Linguagem: Kotlin 2.0.21, bytecode e API do Java 17 (DT-001).
         - Gradle 8.11.1                                                 
      38 -  via wrapper; exige `JAVA_HOME` com JDK 17+ (nesta máquina, o 
         -JDK 21 do                                                      
      39 -  Android Studio).                                             
      40 -- Rodar: `./gradlew run --args="calcular --input exemplos/despe
         -sas-exemplo.json --output saida.json"`                         
     … 107 more lines (ctrl+o to expand)

● Bash(git diff --cached --stat && git commit -q -F - <<'EOF'
      chore: gitattributes para LF no gradlew e limpeza do CLAUDE.md…)
  ⎿  (No output)

● O od acusou CR, o que contradiz a checagem anterior. Vou contar os bytes 0x0D
  diretamente.

● Bash(git show HEAD:gradlew | od -An -tx1 -v | tr ' ' '\n' | grep -c '^0d$';
      git show HEAD:gradlew | od -An -tx1 -v | tr ' ' '\n' | grep -c '^0a$'; git
      show HEAD:grad…)
  ⎿  (No output)

● Fiz os dois ajustes num commit separado na main: c5a61d0 chore: gitattributes
  para LF no gradlew e limpeza do CLAUDE.md. O push fica com você.

  Ponto 3, LF no gradlew
  - O gradlew do 3c18414 já estava gravado com LF, então não houve nada a
    corrigir.
    - O arquivo foi gerado com LF, e o core.autocrlf=true daqui só converte para
      CRLF na hora do checkout, não ao gravar no repositório.
    - O git add --renormalize . não mudou nada no gradlew.
  - O .gitattributes tem as duas linhas que você pediu. git check-attr eol
    gradlew responde lf, então nem o checkout nesta máquina vai mais trocar para
    CRLF.
  - Verificação do blob em HEAD, contando os bytes de git show HEAD:gradlew com
    od -tx1:
    - 0 bytes 0d (CR);
    - 252 bytes 0a (LF);
    - primeira linha #!/bin/sh\n, sem \r.
  - Uma primeira contagem com od -c | grep '\\r' acusou 212 CR, mas era falso
    positivo: o padrão do grep não pegava o \r de verdade, e a contagem dos
    bytes desmente.

  Ponto 2, CLAUDE.md: a nota do template no topo foi removida. São só 5 linhas
  apagadas, e o arquivo agora começa direto em "## O projeto".

> T-001 pushada. Vamos para a T-002.

  Implemente a classe Valor com normalizar (HALF_UP, escala 2) e os testes de
  guarda da aritmética, conforme a DT-003 do plan e a RN-011 da spec.

  Critério de aceite: RN011NormalizacaoMonetariaTest com os três testes —
  100,005 normaliza para 100,01; −45,005 normaliza para −45,01; 54,90 e 54,900
  são o mesmo valor.

  Marque a T-002 como [x] no tasks.md no mesmo commit.

  Antes de commitar, me mostre o diff.

● Bash(git status --short && git log --oneline -1 && sed -n '543,571p'
      specs/001-motor-reembolso/spec.md && sed -n '1123,1173p'
      specs/001-motor-reembolso/spec.md)
  ⎿  (No output)

● Encontrei uma divergência: a T-002 lista quatro testes, não três, incluindo o
  caso de normalização de 33,333 para 33,33. Vou implementar os quatro testes
  conforme definidos na task.

● Write(src\reembolso\dominio\Valor.kt)
  ⎿  Wrote 12 lines to src\reembolso\dominio\Valor.kt
       1 package reembolso.dominio
       2
       3 import java.math.BigDecimal
       5
       6 // DT-003: único caminho de criação é normalizar; escala sempre
         2, HALF_UP (afasta do zero).
       7 @JvmInline
       9     companion object {
      10         fun normalizar(bruto: BigDecimal): Valor = Valor(bruto.s
         etScale(2, RoundingMode.HALF_UP))
     … +4 lines (ctrl+o to expand)

● Write(tests\reembolso\regras\RN011NormalizacaoMonetariaTest.kt)
  ⎿  Wrote 36 lines to
     tests\reembolso\regras\RN011NormalizacaoMonetariaTest.kt
       1 package reembolso.regras
       2
       3 import java.math.BigDecimal
       4 import kotlin.test.Test
       5 import kotlin.test.assertEquals
       7
       8 class RN011NormalizacaoMonetariaTest {
       9
      10     private fun normalizado(texto: String) = Valor.normalizar(Bi
         gDecimal(texto)).decimal.toPlainString()
     … +29 lines (ctrl+o to expand)

● Update(specs\001-motor-reembolso\tasks.md)
  ⎿  Added 1 line, removed 1 line
      45      `AmbienteTest` › `build compila src e executa testes de tes
          ts`
      46    - **Commit:** —
      47  
      48 -- [ ] **T-002** — `value class Valor` com `normalizar` (`setSca
         -le(2, HALF_UP)`)                                               
      48 +- [x] **T-002** — `value class Valor` com `normalizar` (`setSca
         +le(2, HALF_UP)`)                                               
      49    como único construtor, e os testes de guarda da aritmética (D
          T-003).
      50    - **Atende:** RN-011, AMB-014
      51    - **Aceite:** `RN011NormalizacaoMonetariaTest` passa:

● Bash(export JAVA_HOME="/c/Program Files/Android/Android Studio/jbr" &&
      ./gradlew build -q 2>&1 | tail -20; grep -h -o '<testsuite name="[^"]*"
      tests="[0-9]*" skipped…)
  ⎿  5 files changed (binary, mode only or too large to show)
  ⎿  (timeout 5m)

● Fiz a T-002 e ela está pronta para commit, mas não commitei: o diff está acima
  para você revisar. As alterações estão no índice (git add feito).

  Divergência com o seu pedido: a T-002 no tasks.md
  (specs/001-motor-reembolso/tasks.md) define quatro testes, e você citou três.
  Implementei os quatro da task:
  - O que ficou de fora da sua lista é 33,333 normaliza para 33,33.
  - O teste de 100,005 também verifica 100,004 → 100,00.

  Se quiser só os três, eu tiro. Mas aí a task e o teste deixam de bater, e o
  certo seria ajustar o tasks.md antes.

  Resultado: ./gradlew build passa com 6 testes e nenhuma falha: os 4 do
  RN011NormalizacaoMonetariaTest e os 2 do AmbienteTest. O checklist de revisão
  está limpo: nenhum .trim() nem Double em src/.

  O que entra no commit:
  - Valor.kt (src/reembolso/dominio/Valor.kt): o único jeito de criar um Valor é
    normalizar, que fixa duas casas com setScale(2, HALF_UP).
  - Escolhas no teste de 54,90 ≡ 54,900:
    - Ele compara também o hashCode, porque a duplicidade (RN-009) vai usar
      Valor como chave de agrupamento.
    - Os demais testes comparam por toPlainString(), não por BigDecimal.equals,
      para que a escala também seja verificada.
  - Não criei o Valor.de("60.00") que aparece no plan §4. Ele só é necessário
    para o Politica.kt, que é da T-004.

  Mensagem de commit proposta:
  feat(T-002): Valor com normalizar HALF_UP escala 2 e testes de guarda da
  RN-011

  Posso commitar?

> Mantenha os quatro. Você está certo: a task define quatro testes e eu citei
  três de memória. Ajustar o tasks.md para caber na minha lista seria alterar
  o documento por causa de uma imprecisão minha, e o critério de aceite é o
  que está escrito na task.

  O teste de hashCode é um acréscimo correto — Valor vai integrar a chave de
  agrupamento da RN-009, e BigDecimal tem equals sensível à escala. Verificar
  equals e hashCode juntos fecha o contrato que a duplicidade vai precisar.

  Comparar por toPlainString nos demais testes também está certo: verifica a
  escala, não só o valor numérico.

  Pode commitar com a mensagem proposta.

● Bash(git commit -q -F - <<'EOF'
      feat(T-002): Valor com normalizar HALF_UP escala 2 e testes de guarda da
      RN-011…)
  ⎿  (No output)

● Commitei a T-002 na main como 9abc75c, com a mensagem que você aprovou. O
  commit tem os quatro testes da task, o Valor.kt e a T-002 marcada [x]. Não fiz
  push, e não ficou nada pendente fora do commit.

  A próxima é a T-003: a função aparar() com os 25 caracteres que o padrão
  Unicode considera espaço em branco (DT-005).

> git push

● Bash(git push 2>&1 && git status -sb | head -1)
  ⎿  (No output)

● Fiz o push: o commit 9abc75c da T-002 está no GitHub, e a main local está
  igual à origin/main.
> Vamos para a T-003: a função aparar() com os 25 code points de White_Space,
  conforme a DT-005 do plan e a definição de "espaços nas extremidades" da §4.1
  da spec.

  Critério de aceite: EspacosUnicodeTest com os testes da task, incluindo os
  dois casos que você identificou — U+001C a U+001F não são aparados, U+0085 é.

  Marque a T-003 como [x] no tasks.md no mesmo commit.

  Me mostre o diff antes de commitar.

  Searched for 1 pattern (ctrl+o to expand)

● Write(src\reembolso\dominio\Aparar.kt)
  ⎿  Wrote 26 lines to src\reembolso\dominio\Aparar.kt
       1 package reembolso.dominio
       2
       3 // DT-005: os 25 code points da propriedade Unicode White_Space
       4 // Explícitos porque Char.isWhitespace() e String.strip() diverg
         em dessa propriedade.
       5 internal val ESPACOS_EM_BRANCO: Set<Int> = buildSet {
       6     addAll(0x0009..0x000D)
       8     add(0x0085)
       9     add(0x00A0)
      10     add(0x1680)
     … +21 lines (ctrl+o to expand)

● Write(tests\reembolso\dominio\EspacosUnicodeTest.kt)
  ⎿  Wrote 43 lines to tests\reembolso\dominio\EspacosUnicodeTest.kt
       1 package reembolso.dominio
       2
       3 import kotlin.test.Test
       4 import kotlin.test.assertEquals
       5
       7
       8     private fun texto(vararg codePoints: Int) = String(codePoint
         s, 0, codePoints.size)
       9
      10     @Test
     … +41 lines (ctrl+o to expand)

● Update(specs\001-motor-reembolso\tasks.md)
  ⎿  Added 1 line, removed 1 line
      55      `RN-011 AMB-014 54,90 e 54,900 são o mesmo valor`
      56    - **Commit:** —
      57  
      58 -- [ ] **T-003** — Função `aparar()` no domínio com o conjunto e
         -xplícito dos 25                                                
      58 +- [x] **T-003** — Função `aparar()` no domínio com o conjunto e
         +xplícito dos 25                                                
      59    code points da propriedade Unicode `White_Space` (DT-005).
      60    - **Atende:** §4.1 (espaços nas extremidades); base de RN-008
          , RN-010, RN-012
      61    - **Aceite:** `EspacosUnicodeTest` passa:

● Bash(export JAVA_HOME="/c/Program Files/Android/Android Studio/jbr" &&
      ./gradlew build -q 2>&1 | tail -30; grep -h -o '<testsuite name="[^"]*"
      tests="[0-9]*" skipped…)
  ⎿  5 files changed (binary, mode only or too large to show)
  ⎿  (timeout 5m)

● Vou retirar docs/notas-trabalho.md do índice, já que é edição sua e não
  pertence ao commit feat(T-003), e vou verificar os bytes reais do último teste
  para confirmar se os escapes \u00A0, \u2007 e \u202F foram gravados
  corretamente.

● Bash(git restore --staged docs/notas-trabalho.md && grep -n
      'Bistro\|assertEquals("", ' tests/reembolso/dominio/EspacosUnicodeTest.kt
      | od -c | sed -n '1,20p')
  ⎿  (No output)

● Os escapes foram gravados como caracteres literais invisíveis (U+00A0, U+2007,
  U+202F). O teste funciona, mas assim não dá para revisar o código. Vou trocar
  pelos escapes \uXXXX explícitos.

● Bash(f=tests/reembolso/dominio/EspacosUnicodeTest.kt && sed -i
      's/\xC2\xA0/\\u00A0/g; s/\xE2\x80\x87/\\u2007/g; s/\xE2\x80\xAF/\\u202F/g'
      "$f" && LC_ALL=C grep -c -P…)
  ⎿  Updated tests\reembolso\dominio\EspacosUnicodeTest.kt (+2 -2)
      37  
      38      @Test
      39      fun `§4-1 aparar não remove espaços do meio do texto`() {
      40 -        assertEquals("Bistro \t  Central", "  Bistro \t  Centra
         -l  ".aparar())                                                 
      41 -        assertEquals("", " \t   ".aparar())                    
      40 +        assertEquals("Bistro \t00A0 Central", "  Bistro \t00A0 
         +Central2007 ".aparar())                                        
      41 +        assertEquals("", " \t00A0202F ".aparar())              
      42      }
      43  }
  ⎿  … 5 more files changed
  ⎿  (timeout 5m)

● O sed corrompeu os escapes ao interpretar \u como comando. Vou reescrever o
  teste construindo os caracteres pelo code point com a função texto(), evitando
  caracteres invisíveis no código-fonte.

● Update(tests\reembolso\dominio\EspacosUnicodeTest.kt)
  ⎿  Added 5 lines, removed 2 lines
      37  
      38      @Test
      39      fun `§4-1 aparar não remove espaços do meio do texto`() {
      40 -        assertEquals("Bistro \t00A0 Central", "  Bistro \t00A0 
         -Central2007 ".aparar())                                        
      41 -        assertEquals("", " \t00A0202F ".aparar())              
      40 +        val nbsp = texto(0x00A0)                               
      41 +        val figureSpace = texto(0x2007)                        
      42 +        val narrowNbsp = texto(0x202F)                         
      43 +        assertEquals("Bistro \t$nbsp Central", "  Bistro \t$nbs
         +p Central$figureSpace ".aparar())                              
      44 +        assertEquals("", " \t$nbsp$narrowNbsp ".aparar())      
      45      }
      46  }

● Bash(f=tests/reembolso/dominio/EspacosUnicodeTest.kt && sed -n '38,46p' "$f" |
      od -c | grep -E '302|342' ; echo "(acima: bytes não-ASCII nas linhas
      38-46, esperado s…)
  ⎿  5 files changed (binary, mode only or too large to show)
  ⎿  (timeout 5m)

● Bash(for f in tests/reembolso/dominio/EspacosUnicodeTest.kt
      src/reembolso/dominio/Aparar.kt; do echo "$f:"; od -An -c "$f" | grep -oE
      '(302|303|342) [0-9]{3}( [0-9]{…)
  ⎿  (No output)

● Os dois arquivos estão limpos agora. Os únicos bytes fora do ASCII são § e as
  letras acentuadas dos nomes e comentários, e o \u0085 ficou como escape.

  O diff da T-003 já está no índice, conforme mostrado acima, e não commitei. O
  ./gradlew build passa: os 3 testes do EspacosUnicodeTest e os outros 6 testes
  do projeto, sem nenhuma falha.

  O que entra no commit:
  - Aparar.kt (src/reembolso/dominio/Aparar.kt): a lista explícita dos 25
    caracteres de espaço em branco do Unicode (DT-005) e a extensão
    String.aparar().
    - A remoção percorre o texto letra a letra pelas duas pontas, em vez de usar
      trim() ou strip().
    - Isso é seguro porque os 25 caracteres estão no BMP, então nunca são metade
      de um par substituto.
  - EspacosUnicodeTest.kt (tests/reembolso/dominio/EspacosUnicodeTest.kt), com
    os 3 testes da task:
    - O teste dos 25 compara aparar() com uma referência independente da lista
      do domínio: a propriedade \p{IsWhite_Space} do regex do JDK. Ele passa por
      todos os code points de U+0000 a U+10FFFF e cobre dois erros: um
      caractere que falta na lista e um que sobra. Leva cerca de 1 s.
    - O de U+0085 e U+001C a U+001F é o caso em que Char.isWhitespace() erra.
    - O do meio do texto mantém tabulação e espaço não separável no meio, e
      aceita que um texto feito só de espaços vire vazio, que é a base da regra
      "texto vazio é ausente".
  - A T-003 marcada [x] no tasks.md.

  Dois problemas que corrigi no caminho:
  1. O git add -A tinha posto no índice o docs/notas-trabalho.md, que é uma
     edição sua. Tirei de lá e ele continua modificado no disco, fora deste
     commit.
  2. Os escapes \u00A0, \u2007 e \u202F foram gravados como caracteres
     invisíveis no código, e o teste passava, mas não dava para revisar. Agora o
     teste monta esses caracteres pelo code point, com texto(0x00A0) e
     similares.

  Mensagem proposta:
  feat(T-003): aparar() com os 25 code points White_Space e EspacosUnicodeTest

  Posso commitar?

> Pode commitar com a mensagem proposta.

  O teste contra \p{IsWhite_Space} do JDK é melhor que o critério original da
  task: comparar os 25 contra uma lista escrita à mão seria testar a lista
  contra ela mesma. E a correção dos escapes invisíveis estava certa — código
  que passa no teste mas não pode ser revisado é dívida que ninguém vê.

  Obrigado por tirar o notas-trabalho.md do índice. Ele é meu e vai em commit
  próprio.

● Bash(git commit -q -F - <<'EOF'
      feat(T-003): aparar() com os 25 code points White_Space e
      EspacosUnicodeTest…)
  ⎿  (No output)

● Commitei a T-003 na main como 15ce265, com a mensagem que você aprovou. Não
  fiz push.

  O docs/notas-trabalho.md ficou fora do commit e continua modificado no disco,
  à espera do seu commit próprio.

> T-004: o modelo do domínio — Lido, LoteLido, Categoria/Politica, Motivo,
  CampoDespesa, Falha, ResultadoDespesa, ResultadoLote — mais os testes de
  ordem e de fronteira.

  Aceite: OrdemMotivosSecao8Test, OrdemCamposSecao41Test e
  FronteiraDominioTest.

  Marque a T-004 como [x] no tasks.md no mesmo commit. Rode ./gradlew build e
  me mostre o diff antes de commitar.

● Bash(git status --short; sed -n '/^- \[ \] \*\*T-004/,/Commit/p'
      specs/001-motor-reembolso/tasks.md; sed -n '36,60p;80,166p'
      specs/001-motor-reembolso/spec.md; sed -…)
  ⎿  (No output)

● Bash(sed -n '/Informações de cada despesa/,/^$/p'
      specs/001-motor-reembolso/spec.md | cut -c1-60; sed -n '/Informações de
      cada despesa/,/^Um texto/p' specs/001-motor…)
  ⎿  (No output)

● Bash(sed -n '/Informações de cada despesa/,/^Um texto/p'
      specs/001-motor-reembolso/spec.md | cut -c1-50 | cat -A | cut -c1-70)
  ⎿  (No output)

● Agora escrevo o modelo e os três testes.

● Write(src\reembolso\dominio\Lido.kt)
  ⎿  Wrote 39 lines to src\reembolso\dominio\Lido.kt
       1 package reembolso.dominio
       2
       3 import java.math.BigDecimal
       4 import java.time.LocalDate
       5
         a.
       7 sealed interface Lido<out T> {
       8     data class Presente<out T>(val valor: T) : Lido<T>
       9     data object Ausente : Lido<Nothing>
      10     data object TipoIncompativel : Lido<Nothing>
     … +33 lines (ctrl+o to expand)

● Write(src\reembolso\dominio\Politica.kt)
  ⎿  Wrote 19 lines to src\reembolso\dominio\Politica.kt
       1 package reembolso.dominio
       2
       3 // Plan §4: a política inteira vive neste arquivo; mudá-la é mud
       4 enum class Categoria(val codigo: String, val limite: Valor) {
       5     ALIMENTACAO("alimentacao", Valor.de("60.00")),
       6     TRANSPORTE_URBANO("transporte_urbano", Valor.de("80.00")),
       7     HOSPEDAGEM("hospedagem", Valor.de("250.00"));
       9     companion object {
      10         fun porCodigo(codigo: String): Categoria? = entries.find
          { it.codigo == codigo }
     … +11 lines (ctrl+o to expand)
● Write(src\reembolso\dominio\Modelo.kt)
  ⎿  Wrote 44 lines to src\reembolso\dominio\Modelo.kt
       1 package reembolso.dominio
       3 import java.time.LocalDate
       4
       5 // Nomes = códigos de negócio da spec; ordem de declaração = ord
       6 enum class Motivo { FORA_DA_COMPETENCIA, CATEGORIA_INELEGIVEL, S
         EM_NOTA_FISCAL, DUPLICATA }
       8 // IDENTIFICADOR..NOTA_FISCAL na ordem da tabela de despesa da §
         4.1 (AMB-021, OrdemCamposSecao41Test).
       9 // DESPESA = elemento da relação que não é despesa (AMB-020).
         A, FORNECEDOR, VALOR, NOTA_FISCAL }
     … +41 lines (ctrl+o to expand)

● Write(src\reembolso\dominio\Resultado.kt)
  ⎿  Wrote 55 lines to src\reembolso\dominio\Resultado.kt
       1 package reembolso.dominio
       2
       3 import java.time.LocalDate
       4
       5 // Cada variante carrega só os campos que a §4.2 diz que aparece
       6 sealed interface ResultadoDespesa {
       7     // Integral sem excedente, parcial com excedente.
       8     data class Aceita(
       9         val id: String,
      10         val valor: Valor,
     … +47 lines (ctrl+o to expand)

● Update(src\reembolso\dominio\Valor.kt)
  ⎿  Added 3 lines
       8  value class Valor private constructor(val decimal: BigDecimal) 
          {
       9      companion object {
      10          fun normalizar(bruto: BigDecimal): Valor = Valor(bruto.
          setScale(2, RoundingMode.HALF_UP))
      11 +                                                               
      12 +        // Para as constantes da política; passa por normalizar
         + como todo outro valor.                                        
      13 +        fun de(texto: String): Valor = normalizar(BigDecimal(te
         +xto))                                                          
      14      }
      15  }

● Write(tests\reembolso\rastreabilidade\Spec.kt)
  ⎿  Wrote 20 lines to tests\reembolso\rastreabilidade\Spec.kt
       1 package reembolso.rastreabilidade
       2
       4
       5 // O diretório de trabalho dos testes é a raiz do projeto (plan
         DT-002).
       7     val linhas: List<String> by lazy { File("specs/001-motor-ree
         mbolso/spec.md").readLines() }
       8
          título de nível igual ou superior.
      10     fun secao(titulo: String): List<String> {
     … +14 lines (ctrl+o to expand)

● Write(tests\reembolso\rastreabilidade\OrdemMotivosSecao8Test.kt)
  ⎿  Wrote 21 lines to
     tests\reembolso\rastreabilidade\OrdemMotivosSecao8Test.kt
       1 package reembolso.rastreabilidade
       2
       3 import kotlin.test.Test
       4 import kotlin.test.assertEquals
       5 import reembolso.dominio.Motivo
       7 class OrdemMotivosSecao8Test {
       8
       9     @Test
      10     fun `§8 AMB-018 ordem de Motivo é a ordem da lista de elegib
         ilidade da spec`() {
     … +15 lines (ctrl+o to expand)

● Write(tests\reembolso\rastreabilidade\OrdemCamposSecao41Test.kt)
  ⎿  Wrote 36 lines to
     tests\reembolso\rastreabilidade\OrdemCamposSecao41Test.kt
       1 package reembolso.rastreabilidade
       2
       3 import kotlin.test.Test
       4 import kotlin.test.assertEquals
       5 import reembolso.dominio.CampoDespesa
       7 class OrdemCamposSecao41Test {
       8
       9     // Informação da tabela de despesa da §4.1 → campo do domíni
         o; null = sem regra, não lida (AMB-015).
      10     private val correspondencia = mapOf(
     … +32 lines (ctrl+o to expand)

● Write(tests\reembolso\rastreabilidade\FronteiraDominioTest.kt)
  ⎿  Wrote 23 lines to
     tests\reembolso\rastreabilidade\FronteiraDominioTest.kt
       1 package reembolso.rastreabilidade
       2
       3 import java.io.File
       4 import kotlin.test.Test
       5 import kotlin.test.assertTrue
       7 class FronteiraDominioTest {
       8
       9     @Test
      10     fun `DT-009 nenhum arquivo do dominio importa kotlinx ou jav
         a io`() {
     … +20 lines (ctrl+o to expand)

● Update(tests\reembolso\rastreabilidade\OrdemMotivosSecao8Test.kt)
  ⎿  Added 2 lines, removed 5 lines
      11          val motivosDaSpec = Spec.secao("## 8.")
      12              .flatMap { linha -> Regex("→ ([A-Z][A-Z_]+)").findA
          ll(linha).map { it.groupValues[1] } }
      13  
      14 -        assertEquals(                                          
      15 -            listOf("FORA_DA_COMPETENCIA", "CATEGORIA_INELEGIVEL
         -", "SEM_NOTA_FISCAL", "DUPLICATA").size,                       
      16 -            motivosDaSpec.size,                                
      17 -            "a §8 deveria listar quatro motivos; extraídos: $mo
         -tivosDaSpec",                                                  
      18 -        )                                                      
      14 +        // Guarda contra uma extração vazia ou parcial passar p
         +or coincidência.                                               
      15 +        assertEquals(4, motivosDaSpec.size, "motivos extraídos 
         +da §8: $motivosDaSpec")                                        
      16          assertEquals(motivosDaSpec, Motivo.entries.map { it.nam
          e })
      17      }
      18  }

● Bash(sed -i 's/- \[ \] \*\*T-004\*\*/- [x] **T-004**/'
      specs/001-motor-reembolso/tasks.md && export JAVA_HOME="/c/Program
      Files/Android/Android Studio/jbr" && ./grad…)
  ⎿  Updated specs\001-motor-reembolso\tasks.md (+1 -1)
      64      `§4-1 aparar não remove espaços do meio do texto`
      65    - **Commit:** —
      66  
      67 -- [ ] **T-004** — Modelo do domínio (plan §3.1, §3.2, §4): `Lid
         -o`, `LoteLido`,                                                
      67 +- [x] **T-004** — Modelo do domínio (plan §3.1, §3.2, §4): `Lid
         +o`, `LoteLido`,                                                
      68    `Categoria` e `Politica` em `Politica.kt`, `Motivo`, `CampoDe
          spesa`, `Falha`,
      69    `CausaErro`, `CausaLote`, `Despesa`, `Referencia`, `Resultado
          Despesa`,
      70    `ResultadoLote`. Testes que amarram a ordem dos enums à spec 
          e a fronteira
  ⎿  … 5 more files changed
  ⎿  (timeout 5m)

● Build verde. Antes de mostrar o diff, confiro que os três testes realmente
  quebram quando deveriam: altero o código de propósito, rodo o teste e reverto.

● Bash(export JAVA_HOME="/c/Program Files/Android/Android Studio/jbr" &&
      S=/c/Users/dev/AppData/Local/Temp/claude/c--Users-dev-SDD-Desafio/8aa9dd74
      -d4f2-42e8-b296-d294…)
  ⎿  5 files changed (binary, mode only or too large to show)
  ⎿  (timeout 5m)

● Com as três alterações feitas de propósito, os 3 testes falharam, e depois de
  restaurar o build voltou a passar. Agora o diff:

● Bash(git add specs src tests 2>/dev/null && git status --short && git diff
      --cached)
  ⎿  (No output)

● A T-004 está implementada e passa no ./gradlew build. O diff completo está
  acima e as alterações estão no índice, mas não commitei.

  O build tem 12 testes e nenhuma falha. Os 3 de aceite passam:
  - §8 AMB-018 ordem de Motivo é a ordem da lista de elegibilidade da spec
  - §4-1 AMB-021 ordem de CampoDespesa é a ordem da tabela de despesa da spec
  - DT-009 nenhum arquivo do dominio importa kotlinx ou java io

  Também confirmei que eles quebram quando devem. Inverti dois motivos no enum,
  troquei a ordem de FORNECEDOR e VALOR e pus import java.io.File no domínio: os
  3 falharam. Depois restaurei o arquivo e o build voltou a passar.

  Diferenças em relação ao seu pedido e ao plan:
  - Tipos a mais. A sua lista omitiu CausaErro, CausaLote, Despesa e Referencia,
    mas a task os inclui, então implementei todos.
  - Tipos novos. Criei Periodo e Consolidado, que o plan cita em
    ResultadoLote.Avaliado sem definir. Os dois só guardam dados, e a conta do
    consolidado continua na T-014.
  - Valor.de(texto). O plan §4 usa essa função no Politica.kt. Ela passa por
    normalizar, então continua não havendo outra forma de criar um Valor sem a
    normalização da DT-003.
  - CausaLote em PascalCase. Ficou como interface fechada (EntradaIlegivel,
    IdentificadorRepetido(identificadores)…), e não com os nomes em maiúsculas
    do plan. Isso segue a convenção do Kotlin para objetos e permite que
    IdentificadorRepetido carregue os ids. Traduzir para o codigo da saída fica
    para a T-018.
  - Categoria como texto. O campo categoria dos resultados é String, como em
    Despesa, porque um resultado recusado pode trazer "coworking".

  Sobre os testes:
  - O teste da §8 exige exatamente 4 motivos extraídos da spec. Sem isso, uma
    extração vazia ou incompleta poderia passar por coincidência.
  - O teste da §4.1 compara também a lista de informações da tabela com a sua
    tabela de correspondência. Uma informação nova na spec quebra o build até
    receber um lugar no código. "Descrição" fica mapeada para nenhum campo,
    porque nenhuma regra a usa (AMB-015).
  - O teste de fronteira pega import e também nome qualificado no meio do
    código, ignorando comentários.
  - Spec.kt é um utilitário de leitura da spec para os testes. O
    RastreabilidadeTest da T-016 vai reaproveitá-lo.

  Um ponto de rastreabilidade para você saber: a T-016 exclui o pacote
  rastreabilidade da contagem de IDs, e é nele que estes três testes estão. Por
  isso eles não vão contar como cobertura de AMB-018 nem de AMB-021. A matriz
  continua correta, porque a T-012 cobre a AMB-018, e a T-006 e a T-007 cobrem a
  AMB-021.

  Mensagem proposta:
  feat(T-004): modelo do domínio, política e testes de ordem da §8 e §4.1 e de
  fronteira

  Posso commitar?

> Pode commitar com a mensagem proposta.

  Três coisas no que você fez merecem registro:

  Verificar que os testes falham quando devem — inverter os motivos, trocar a
  ordem dos campos e inserir um import proibido — é mais do que rodar o build.
  Teste que passa sem nunca ter falhado não prova nada.

  O teste da §8 exigir exatamente quatro motivos extraídos da spec fecha o
  buraco de uma extração vazia passar por coincidência. O mesmo vale para a
  tabela de correspondência da §4.1.

  A observação sobre o pacote rastreabilidade não contar como cobertura está
  certa e preserva a matriz: a AMB-018 é coberta pela T-012 e a AMB-021 pela
  T-006 e T-007.

  Sobre as diferenças: todas aprovadas. Periodo e Consolidado como tipos de
  dados puros, com a conta na T-014, mantém a separação; CausaLote como
  interface fechada em PascalCase é a convenção correta e permite carregar os
  identificadores; e categoria como texto é necessário, já que um resultado
  recusado precisa trazer "coworking".

● Bash(git commit -q -F - <<'EOF'
      feat(T-004): modelo do domínio, política e testes de ordem da §8 e §4.1 e
      de fronteira…)
  ⎿  (No output)

● Commitei a T-004 na main como b6734a8, com a mensagem que você aprovou. Não
  sobrou nada pendente fora do commit, e ainda não fiz push.

  Com isso, a Fase 1 (Fundação) está concluída. A próxima é a T-005: validação
  de lote (RN-010 e RN-012).

> git push

● Bash(git push 2>&1 && git status -sb | head -1)
  ⎿  (No output)

● Fiz o push: a main local e a origin/main estão iguais, no b6734a8.

  O push levou 338e1f6..b6734a8. Ou seja, subiram junto o commit da T-003 e o
  seu commit do notas-trabalho.md, além da T-004.

> Antes da T-005, quero uma revisão independente da spec, com contexto limpo.

  Lance um subagente general-purpose com esta tarefa: ler
  specs/001-motor-reembolso/spec.md inteira, sem ter participado de nenhuma
  das decisões, e procurar quatro coisas:

  (a) duas afirmações que se contradigam;
  (b) uma regra cujo enunciado não permita determinar o resultado de um caso
      que a própria spec menciona;
  (c) uma ambiguidade cuja decisão não esteja refletida na regra
  correspondente;
  (d) qualquer menção a solução técnica que tenha escapado da varredura
      anti-vazamento — linguagem, biblioteca, estrutura de dados, nome de
      arquivo, formato de serialização.

  Ele deve relatar o que encontrar, com citação do trecho, e não alterar nada.

  Se não encontrar nada em alguma das quatro categorias, deve dizer isso
  explicitamente em vez de omitir.

  Me traga o relatório.

● Agent(Independent spec review)
  ⎿  Done (7 tool uses · 108.7k tokens · 3m 13s)
  (ctrl+o to expand)

› Message from general-purpose
  ## Revisão independente: specs/001-motor-reembolso/spec.md (v1.1)

  Li a spec inteira (linhas 1–1667), em quatro partes. Também li
  `C:\Users\dev\SDD-Desafio\sdd-desafio\exemplos\despesas-exemplo.json`, que é a
  exceção permitida. Usei o arquivo só para conferir os fatos do exemplo que a
  spec cita: fornecedores, descrições e datas de d-003, d-010, d-011, d-012 e
  d-013, e os totais da §4.2 e da §9. Não abri nenhum outro arquivo e não
  alterei nada.

  ### O que confere
  - **Total da §9:**
    - Integrais: 38,00 + 54,90 + 33,33 + 47,20 = 173,43.
    - Parciais: 60 + 80 + 250 + 60 = 450,00.
    - Total: 623,43. Contagem 4/4/5/1 = 14. Fecha.
  - **Exemplo da §4.2:** 60,00 + 54,90 = 114,90, com 1/1/1/1 = 4. Fecha.
  - **Ordem lexicográfica:** "d-10" < "d-9" e "D-006" < "d-006" estão corretas
  pelos códigos dos caracteres.
  - **Datas e fornecedores:** 18/07/2026 cai num sábado. d-011 tem o mesmo
  fornecedor de d-010 (Hotel Copa Sul). d-003 é "Corrida aeroporto".
  - **Arredondamento:** −45,005 vira −45,01 e 100,005 vira 100,01, coerente com
  "afastando-se do zero".

  ---

  ### Categoria (a): Contradição

  **A1. Média. AMB-013, Decisão contra suas próprias Consequências e contra a
  RN-008 (linhas ~1077–1079, 1107–1108, 1113–1114)**
  - **Decisão:** "As categorias elegíveis são exatamente três — alimentação,
  transporte urbano e hospedagem." Ela vem logo depois de "acentuação,
  separadores e sinônimos não são normalizados".
  - **Consequências:** "'alimentação', com cedilha e til, não é normalizada para
  'alimentacao' e é categoria inelegível". E também: "d-014 (…) → categoria
  normalizada para alimentação".
  - **Por que é problema:** lida ao pé da letra, a Decisão torna elegíveis
  justamente "alimentação" (com acento) e "transporte urbano" (com espaço). As
  Consequências e a RN-008 (`alimentacao`, `transporte_urbano`) dizem o
  contrário. A RN-008 está correta, mas a AMB, que é o registro da decisão, diz
  outra coisa. A própria spec trata a diferença de grafia como decisiva, por
  isso a gravidade é média.

  **A2. Baixa. AMB-018, Justificativa contra a Decisão da própria AMB e contra a
  §8 (linhas ~1348–1356 e 1554–1557)**
  - **Justificativa da AMB-018:** "a ordem das regras passa a tratar apenas do
  que afeta o valor reembolsado, não do que afeta o rótulo."
  - **Decisão da mesma AMB:** "A lista é ordenada (…) na ordem declarada na §8".
  - **§8:** "ela define a ordenação da lista de motivos".
  - **Por que é problema:** a §8 trata, sim, do rótulo, porque define a ordem
  dos motivos.

  **A3. Baixa. AMB-015, Justificativa (linha ~1206–1208)**
  - **Trecho:** "porque a situação é idêntica para os três campos".
  - **Por que é problema:** a Decisão lista cinco campos (identificador, nome e
  centro de custo do colaborador, rótulo de competência e descrição).

  **A4. Baixa. §1 (linha ~12–14)**
  - **Trecho:** "a política contém lacunas que cada conferente preenche à sua
  maneira — esta especificação identifica vinte e uma delas na §6".
  - **Por que é problema:** há de fato 21 AMBs, mas várias declaram "Texto
  original do RH: não há" (003, 012, 014, 015, 017–021). Duas foram dissolvidas
  (003, 012). A AMB-020 e a AMB-021 dizem ter surgido "ao projetar a
  implementação". Elas não são lacunas da política preenchidas por conferentes.

  **A5. Possível, baixa. RN-006 / AMB-010 (linhas ~303–304, 988–989)**
  - **Trecho:** "Uma despesa fora do período não participa de nenhuma agregação
  ou comparação com outras despesas."
  - **Por que é problema:** ao pé da letra, a frase conflita com dois pontos:
    - A RN-012 compara o identificador de toda despesa, inclusive a fora do
  período. Um identificador repetido nela rejeita o lote.
    - O consolidado agrega a despesa (contagem de recusadas e soma do total).
  - **Por que fica como "possível":** a intenção é claramente a duplicidade. Uma
  leitura caridosa diz que a RN-012 roda antes de a pertinência ser julgada.
  Falta a frase restringir o alcance, por exemplo "de nenhuma regra de
  elegibilidade ou de valor".

  ### Categoria (b): Indeterminação

  **B1. Média. Elemento da relação que "não é uma despesa": RN-010 item 2,
  aceite da RN-010 (linha ~496), §4.2 "Causas do erro" (linha ~118), §9 (linhas
  ~1571–1576)**
  - **O caso aparece na spec:** "Elemento da relação de despesas que não é uma
  despesa → erro de item, identificado pela sua posição".
  - **O que as regras de RN/§4.2/§9 dizem:**
    - "todas as causas do erro, na ordem em que as informações da despesa são
  declaradas na §4.1".
    - A §4.2 define causa como "para cada informação que falhou, qual e de que
  forma".
  - **Por que não dá para determinar:** por essas regras, a saída desse caso
  pode ter uma única causa ou seis causas "ausente" (identificador, data,
  categoria, fornecedor, valor, nota fiscal). A "própria despesa" não é uma das
  informações da tabela da §4.1.
  - **Onde está a resolução:** só na AMB-020 (ver C1).
  - **Ponto em aberto:** a spec não diz onde fica a fronteira entre "não é uma
  despesa" e "despesa sem nenhuma informação". Esse caso-limite não é citado,
  por isso fica apenas registrado.

  **B2. Possível, baixa. Aceite da RN-007 / AMB-018 / §7 (linhas ~332, 1361,
  1511)**
  - **Trecho:** "Despesa de 150,00, sem NF, com data fora do período → (…)
  motivos [FORA_DA_COMPETENCIA, SEM_NOTA_FISCAL]".
  - **Por que é problema:** o caso não informa a categoria. O resultado esperado
  só vale se ela for elegível. Com uma categoria inelegível a lista teria três
  motivos. É lacuna do enunciado do caso, não da regra. Para confirmar, bastaria
  fixar a categoria no aceite.

  ### Categoria (c): Decisão não refletida

  **C1. Média. AMB-020 contra RN-010, §4.2 e §9 (linhas ~1416–1417, 1448–1449)**
  - **Decisão da AMB-020:** "O elemento que não é uma despesa tem uma única
  causa de erro: a própria despesa, com tipo incompatível."
  - **Regras afetadas:** a AMB declara afetar "RN-010, RN-012; §4.1, §4.2".
  - **O que falta:**
    - O aceite da RN-010 para esse caso não diz a causa.
    - A §4.2 não prevê "a própria despesa" como informação que falhou.
    - A §9 manda reportar "todas as causas do erro na ordem da §4.1".
  - **Consequência:** a parte "única causa" não chegou a nenhuma das regras que
  a AMB diz afetar. É a origem do B1.

  Fora isso, nada encontrado nesta categoria. Conferi cada "Regra afetada" das
  AMB-001 a 021 contra o texto da RN ou seção correspondente. Os demais pontos
  estão refletidos:
  - limite por lançamento (AMB-001/009 → RN-001);
  - teto e igualdade (AMB-002 → RN-002);
  - fronteira e valor de referência da nota (AMB-004/006 → RN-003);
  - recusa integral (AMB-005 → RN-004);
  - data da despesa e bordas inclusivas (AMB-008/016 → RN-005);
  - normalização de fornecedor (AMB-013 → RN-008/009);
  - normalização antes da faixa (AMB-014 → RN-010/011);
  - tipo incompatível em campo opcional (AMB-015 → RN-010);
  - quarto valor de classificação (AMB-017 → RN-007);
  - ordem das causas (AMB-021 → RN-010/§4.2).

  ### Categoria (d): Vazamento técnico

  **D1. Baixa. Nome de arquivo com extensão, §4 (linha 39) e §4.2 (linha 122)**
  - **Trechos:** "A forma como essa informação é representada pertence ao
  plan.md." e "Uma frase legível construída a partir deles é apresentação, e
  pertence ao plan.md."
  - **Por que é problema:** é referência a um artefato da solução por nome de
  arquivo e extensão. A intenção de separar o QUE do COMO é boa, mas bastaria
  dizer "pertence ao documento de solução".

  **D2. Baixa. §5 (linha 168)**
  - **Trecho:** "Cada regra recebe um ID (`RN-001`, ...). As tasks vão
  referenciar esses IDs."
  - **Por que é problema:** é sobra de modelo que fala do processo de
  implementação, e não do comportamento do sistema.

  **D3. Possível, baixa. "sintaticamente válida", §4.1 (linha 83) e RN-010
  (linhas 431, 500)**
  - **Trecho:** "inclusive quando é sintaticamente válida mas sua estrutura não
  é a de um lote".
  - **Por que é problema:** pressupõe uma serialização com sintaxe a ser
  interpretada. É a fronteira mais próxima de prescrever formato. Os exemplos
  dados ("uma relação ou um número no lugar do lote") são neutros.

  **D4. Possível, baixa (fronteira duvidosa). "padrão Unicode" e "códigos dos
  caracteres"**
  - **Onde:** §4.1 (linha 78), §9 (linha 1577), RN-009 (linha 382), AMB-011
  (linha 1004).
  - **Trechos:** "todos os caracteres que o padrão Unicode reconhece como espaço
  em branco" e "pelos códigos dos caracteres".
  - **Por que é duvidoso:** são termos técnicos. Mesmo assim, definem O QUE é
  comparado e são necessários para a determinação exata, por isso não os trato
  como vazamento confirmado.
  - **Observação lateral:** "códigos dos caracteres" não diz se são pontos de
  código ou unidades de codificação. A diferença só aparece com caracteres fora
  do plano básico, que nenhum caso da spec usa.

  **D5. Possível, baixa (fronteira duvidosa). Códigos de categoria
  `alimentacao`, `transporte_urbano` (RN-008 e outros)**
  - **Por que é duvidoso:** a grafia sem acento e com sublinhado tem cara de
  identificador de implementação. A spec declara explicitamente como
  "identificadores de negócio" apenas os códigos de motivo em maiúsculas (linhas
  130–134), e não esses. Ao mesmo tempo, são os valores que vêm na própria
  entrada. Valeria a spec declará-los também como vocabulário de negócio.

  **D6. Possível, baixa. AMB-020 e AMB-021 (linhas 1401–1402, 1458–1459)**
  - **Trechos:** "A dúvida surgiu ao projetar a implementação do contrato de
  entrada (§4.1), depois do fechamento da spec." e "ao projetar a implementação
  do resultado de erro (§4.2)".
  - **Por que é duvidoso:** não nomeia tecnologia. É histórico de processo
  citando a implementação, e não afeta o resultado.

  O que não conta como vazamento: procurei JSON, XML, CSV, null, string, array,
  boolean, true/false, HTTP, código de saída, nomes de linguagem, enum, classe e
  objeto, e não há ocorrência. Os nomes de campo da entrada de referência
  (`tem_nota_fiscal`, `centro_custo` etc.) não aparecem na spec. "Campo", "tipo
  incompatível", "relação de despesas" e "lista de motivos" são usados como
  termos de negócio, e a spec ressalva que os colchetes e a grafia não
  prescrevem a forma da saída.

  ---

  ### Tabela-resumo

  | Categoria | Nº de achados | Gravidade mais alta |
  |---|---|---|
  | (a) Contradição | 5 (A1–A4 confirmados; A5 possível) | média (A1) |
  | (b) Indeterminação | 2 (B1 confirmado; B2 possível) | média (B1) |
  | (c) Decisão não refletida | 1 (C1) | média |
  | (d) Vazamento técnico | 6 (D1–D2 confirmados; D3–D6 possíveis ou de
  fronteira) | baixa |

  Nenhum achado é de gravidade alta: todos os resultados numéricos do lote de
  exemplo fecham e não se contradizem. Os pontos que mais pedem correção são A1
  (o texto da decisão da AMB-013 contradiz a regra sobre acentuação) e B1/C1 (a
  "única causa" do elemento que não é despesa ficou só na AMB-020, sem chegar a
  RN-010, §4.2 e §9).

● Chegou o relatório do subagente. Antes de repassar, confiro na spec os achados
  de gravidade média e os dois que mais afetam a T-005 em diante.

● Bash(cd /c/Users/dev/SDD-Desafio/sdd-desafio/specs/001-motor-reembolso && grep
      -n 'exatamente três\|sinônimos não são' spec.md; grep -n 'única
      causa\|própria despesa…)
  ⎿  (No output)
● Bash(sed -n '1076,1080p;1202,1210p' spec.md)
  ⎿  (No output)

● O subagente leu a spec inteira sem acessar plan, tasks, DECISIONS nem código,
  e não alterou nada. A única exceção foi o despesas-exemplo.json, que a revisão
  permitia, usado para conferir os dados do exemplo. Ele encontrou achados nas
  quatro categorias, nenhum de gravidade alta. Os totais da §9 (623,43 com
  4/4/5/1) e do exemplo da §4.2 (114,90) fecham.

  Conferi na spec os trechos dos achados de gravidade média e de A3, A5 e B2. As
  citações batem.

  Relatório

  (a) Contradição: 5 achados

  #: A1
  Grav.: média
  Onde: AMB-013, l. 1078
  Achado: A Decisão diz "As categorias elegíveis são exatamente três —
  alimentação, transporte urbano e hospedagem", com acento e espaço, logo
  depois de dizer que acentuação e separadores não são normalizados. A RN-008
  (l. 350) e as Consequências usam alimentacao e transporte_urbano. Lida ao pé
  da letra, a AMB torna elegível justamente a grafia que ela mesma diz ser
  inelegível.
  ────────────────────────────────────────
  #: A2
  Grav.: baixa
  Onde: AMB-018, l. 1355
  Achado: Diz que a ordem da §8 "passa a tratar apenas do que afeta o valor… não

  do que afeta o rótulo". Mas a mesma AMB e a §8 usam essa ordem para ordenar a
   lista de motivos, ou seja, o rótulo.
  ────────────────────────────────────────
  #: A3
  Grav.: baixa
  Onde: AMB-015, l. 1207
  Achado: "A situação é idêntica para os três campos", mas a decisão abrange
  cinco campos.
  ────────────────────────────────────────
  #: A4
  Grav.: baixa
  Onde: §1, l. 12–14
  Achado: Diz que as 21 AMBs são "lacunas que cada conferente preenche". Várias
  não têm texto do RH, duas foram dissolvidas, e a AMB-020 e a AMB-021 surgiram
   durante o projeto da implementação.
  ────────────────────────────────────────
  #: A5
  Grav.: possível, baixa
  Onde: RN-006 l. 303, AMB-010 l. 988
  Achado: "Uma despesa fora do período não participa de nenhuma agregação ou
  comparação". Ao pé da letra, isso conflita com a RN-012, que compara
  identificadores de todas as despesas, e com o consolidado, que soma e conta
  todas. A intenção é a duplicidade, mas a frase não limita o alcance.

  (b) Indeterminação: 2 achados

  - B1 (média). Para um elemento da relação que não é uma despesa, a RN-010, a
    §4.2 e a §9 mandam reportar "todas as causas, na ordem da §4.1". Por esse
    texto, a saída pode ter uma causa ou seis causas "ausente". Só a AMB-020
    resolve, e "a própria despesa" nem é uma informação da tabela da §4.1.
  - B2 (possível, baixa). O caso "150,00, sem NF, fora do período →
    [FORA_DA_COMPETENCIA, SEM_NOTA_FISCAL]" (RN-007 l. 332, §7 l. 1511) não fixa
    a categoria. Com uma categoria inelegível, seriam três motivos.

  (c) Decisão não refletida: 1 achado

  - C1 (média). A decisão da AMB-020, "uma única causa de erro: a própria
    despesa, com tipo incompatível" (l. 1417), não chegou a nenhuma das regras
    que a AMB diz afetar: o aceite da RN-010, a §4.2 e a §9. É a origem do B1.
  - As demais "Regras afetadas" das AMB-001 a AMB-021 foram conferidas e estão
    refletidas.

  (d) Vazamento técnico: 6 achados, todos de gravidade baixa

  - D1. "pertence ao plan.md" (l. 39 e l. 122): nome de arquivo.
  - D2. "As tasks vão referenciar esses IDs" (§5): sobra do template, que fala
    do processo.
  - D3 (possível). "sintaticamente válida" (§4.1, RN-010): pressupõe uma
    serialização.
  - D4 (de fronteira). "padrão Unicode" e "códigos dos caracteres". A observação
    lateral sobre este é a mais importante do relatório e está detalhada
    abaixo.
  - D5 (de fronteira). alimentacao e transporte_urbano têm cara de identificador
    técnico. A spec não os declara como vocabulário de negócio, como fez com os
    códigos de motivo.
  - D6 (possível). A AMB-020 e a AMB-021 citam "ao projetar a implementação": é
    histórico de processo.
  - Ele procurou JSON, XML, CSV, null, string, array, boolean, HTTP, exit code,
    enum e classe, e não achou nenhuma ocorrência.

  Minha leitura

  Concordo com o relatório, mas acho que um ponto ele subestimou: a observação
  lateral do D4. "Códigos dos caracteres" (RN-009) não diz se são code points ou
  unidades UTF-16, e quem escolheu "code point" foi o plan (DT-007). Isso é
  regra de negócio decidida no plan, exatamente o que o CLAUDE.md proíbe. Muda o
  resultado quando o identificador tem caractere fora do BMP, e a T-013 já tem
  um teste para esse caso. Eu trataria como achado da categoria (c) ou (b), de
  gravidade média, e não como vazamento.

  O que corrigiria na spec antes da T-005, num docs(spec): com registro em
  DECISIONS:
  1. A1: trocar a lista da AMB-013 por alimentacao, transporte_urbano e
     hospedagem.
  2. C1/B1: levar a "única causa" do elemento que não é despesa para o aceite da
     RN-010, a §4.2 e a §9. O código já segue a AMB-020: CampoDespesa.DESPESA
     existe no modelo.
  3. A5: restringir a frase da RN-006 e da AMB-010 a "nenhuma regra de
     elegibilidade ou de valor". Isso toca a T-005 diretamente, porque a RN-012
     compara identificadores de todas as despesas.
  4. D4: fixar "ponto de código Unicode" na RN-009 e na AMB-011.

  A2, A3, A4, B2, D1–D3, D5 e D6 são de redação. Dá para corrigir na mesma
  passada ou deixar para depois.

  Quer que eu faça essas correções agora, antes da T-005?

> Sim, corrija os quatro antes da T-005. E sua leitura do D4 está certa — ele
  é o mais sério do relatório e o subagente o classificou errado.

  D4: a RN-009 diz "códigos dos caracteres" sem definir se são pontos de
  código Unicode ou unidades UTF-16. Quem decidiu foi a DT-007 do plan, e isso
  é regra de negócio decidida fora da spec, que o CLAUDE.md proíbe. Não é
  teórico: muda qual despesa é mantida quando o identificador tem caractere
  fora do plano básico, e a T-013 já prevê esse caso. Fixe "ponto de código
  Unicode" na RN-009 e na AMB-011, e registre como esclarecimento com a
  justificativa de que a comparação precisa ser independente da representação
  interna de texto adotada pela implementação.

  A1: troque a lista da AMB-013 para alimentacao, transporte_urbano e
  hospedagem. A decisão dizia que acentuação e separadores não são
  normalizados, e logo em seguida listava justamente a grafia que ela torna
  inelegível.

  D5, junto com o A1: declare na §4.1, junto à notação já existente, que
  alimentacao, transporte_urbano e hospedagem são o vocabulário de valores da
  entrada — identificadores de negócio definidos pela própria spec, do mesmo
  modo que os códigos de motivo, e não nomes de implementação. Isso fecha o A1
  pela raiz.

  C1/B1: leve a decisão da AMB-020 — elemento que não é despesa tem causa
  única — para o aceite da RN-010, a §4.2 e a §9. Hoje a decisão existe só na
  AMB, e as regras mandam reportar "todas as causas", o que torna o resultado
  indeterminado.

  A5: restrinja a frase da RN-006 e da AMB-010 para "não participa de nenhuma
  regra de elegibilidade nem de valor". Como está, ela conflita com a RN-012,
  que compara identificadores de todas as despesas, e com o consolidado, que
  conta todas. Isso toca a T-005 diretamente.

  Os demais — A2, A3, A4, B2, D1 a D3 e D6 — são de redação e não alteram
  nenhum resultado. Deixe para uma passada final; se o tempo apertar, ficam
  como estão.

  Faça num commit docs(spec): próprio, com entrada D-010 no DECISIONS
  registrando que os achados vieram de uma revisão independente da spec por
  subagente com contexto limpo, antes da implementação das regras de negócio.

● Bash(cd /c/Users/dev/SDD-Desafio/sdd-desafio && git status --short; tail -60
      specs/001-motor-reembolso/DECISIONS.md; grep -n '^## \|^### D-'
      specs/001-motor-reembols…)
  ⎿  (No output)

● Bash(sed -n '1,96p' specs/001-motor-reembolso/DECISIONS.md)
  ⎿  (No output)

● Bash(cd /c/Users/dev/SDD-Desafio/sdd-desafio/specs/001-motor-reembolso && sed
      -n '93,98p;110,120p;125,136p;296,312p;342,368p;369,416p;436,450p;490,500p;
      984,992p;993,…)
  ⎿  (No output)

● Bash(grep -n 'códigos dos caracteres\|código dos caracteres\|caractere
      a\s*$\|ponto de código' spec.md; sed -n '448,456p' spec.md; grep -n
      'agregação' spec.md)
  ⎿  (No output)

● Bash(grep -n 'não participa\|não integra nenhuma\|nenhuma comparação' spec.md;
      sed -n '959,983p' spec.md)
  ⎿  (No output)

● Encontrei uma inconsistência: a redação proposta para A5 contradiz a RN-007,
  pois a despesa sem NF fora do período ainda deve receber a etiqueta
  SEM_NOTA_FISCAL. Vou reformular para manter sua intenção — a despesa fica
  isenta das regras de valor e de duplicatas, mas as demais regras de
  elegibilidade continuam valendo — e mostrar o diff antes de commitar.

● Bash(date '+%Y-%m-%d %H:%M %z'; cd /c/Users/dev/SDD-Desafio/sdd-desafio && git
      status --short)
  ⎿  (No output)

  Read 2 files (ctrl+o to expand)

● Agora as edições na spec.

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 1 line, removed 1 line
      1  # Spec — Motor de Cálculo de Reembolso
      2  
      3 -**Versão:** 1.1 · **Status:** completa · **Última alteração:** 2
        -026-10-18                                                       
      3 +**Versão:** 1.2 · **Status:** completa · **Última alteração:** 2
        +026-10-18                                                       
      4  
      5  ---
      6  

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 2 lines, removed 1 line
       94  texto —, quando a data não existe no
       95  calendário, quando o valor normalizado é menor ou igual a zero
           , ou quando o
       96  elemento da relação de despesas não é uma despesa (AMB-020). T
           odas as causas
       97 -aplicáveis são reportadas (AMB-021).                          
       97 +aplicáveis são reportadas (AMB-021); o elemento que não é uma 
          +despesa tem                                                   
       98 +uma única causa: a própria despesa, com tipo incompatível (AMB
          +-020).                                                        
       99  
      100  ### 4.2 Saída
      101  

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 1 line, removed 1 line
      116  | Exigência de nota fiscal: exigida ou não exigida | integral,
            parcial e recusada | RN-003; "d-003 → nota fiscal não exigida
           " e "transporte de 100,01 com NF → exigida e presente" produze
           m o mesmo reembolso, e sem esta informação os dois aceites não
            se distinguem |
      117  | Lista de motivos de recusa, na ordem da §8 | sempre; vazia e
           m integral, parcial e erro | RN-007, AMB-018 |
      118  | Despesa mantida: o identificador da despesa que permaneceu c
           om o reembolso | quando a lista de motivos contém DUPLICATA | 
           RN-009; permite verificar qual despesa do grupo foi mantida e 
           que inverter a ordem da entrada não a altera |
      119 -| Causas do erro: para cada informação que falhou, qual e de q
          -ue forma — ausente, tipo incompatível, data inexistente ou val
          -or não positivo —, todas as aplicáveis, na ordem em que as inf
          -ormações da despesa são declaradas na §4.1 | erro | RN-010, AM
          -B-017, AMB-021; separadas da lista de motivos, porque erro não
          - é recusa (RN-007) |                                          
      119 +| Causas do erro: para cada informação que falhou, qual e de q
          +ue forma — ausente, tipo incompatível, data inexistente ou val
          +or não positivo —, todas as aplicáveis, na ordem em que as inf
          +ormações da despesa são declaradas na §4.1; o elemento da rela
          +ção que não é uma despesa tem uma única causa: a própria despe
          +sa, com tipo incompatível | erro | RN-010, AMB-017, AMB-020, A
          +MB-021; separadas da lista de motivos, porque erro não é recus
          +a (RN-007) |                                                  
      120  
      121  Esses campos, em conjunto, são a justificativa de cada despesa
           : respondem por
      122  que ela recebeu o que recebeu. Uma frase legível construída a 
           partir deles é

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 6 lines, removed 2 lines
      131  motivos [FORA_DA_COMPETENCIA, SEM_NOTA_FISCAL]. Os códigos de 
           motivo escritos
      132  em maiúsculas (FORA_DA_COMPETENCIA, CATEGORIA_INELEGIVEL, SEM_
           NOTA_FISCAL,
      133  DUPLICATA) são identificadores de negócio definidos por esta s
           pec, não nomes
      134 -de implementação. Nem os colchetes nem a grafia dos códigos pr
          -escrevem a                                                    
      135 -forma da saída.                                               
      134 +de implementação. Do mesmo modo, alimentacao, transporte_urban
          +o e hospedagem                                                
      135 +são o vocabulário de valores de categoria da entrada: identifi
          +cadores de                                                    
      136 +negócio definidos por esta spec, escritos sem acento e com o s
          +eparador                                                      
      137 +sublinhado, e não nomes de implementação; é com eles que a cat
          +egoria                                                        
      138 +normalizada é comparada (RN-008, AMB-013). Nem os colchetes ne
          +m a grafia dos                                                
      139 +códigos de motivo prescrevem a forma da saída.                
      140  
      141  **Consolidado do lote.**
      142  

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 3 lines, removed 2 lines
      305  FORA_DA_COMPETENCIA na sua lista de motivos (RN-007). O item a
           parece na saída
      306  com seus motivos, como qualquer outro. A recusa independe do l
           imite da
      307  categoria: o item não é avaliado contra o limite (RN-001, RN-0
           02). Uma
      308 -despesa fora do período não participa de nenhuma agregação ou 
          -comparação com                                                
      309 -outras despesas.                                              
      308 +despesa fora do período não é avaliada por nenhuma regra de va
          +lor e não                                                     
      309 +integra grupo de duplicatas (RN-009); as demais regras de eleg
          +ibilidade                                                     
      310 +continuam a ser avaliadas e acrescentam seus motivos (RN-007).
      311  **Origem:** política do RH, item 7; decisões AMB-010 e AMB-018
      312  **Depende de:** RN-005 (período e pertinência); RN-007 (lista 
           de motivos).
      313  **Aceite:**

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 1 line, removed 1 line
      385  a despesa de menor identificador é avaliada normalmente e as d
           emais são
      386  recusadas, com o motivo DUPLICATA na sua lista de motivos (RN-
           007). Os
      387  identificadores são comparados em ordem lexicográfica, caracte
           re a
      388 -caractere, pelos códigos dos caracteres, sem extração de númer
          -o e sem                                                       
      388 +caractere, pelo ponto de código Unicode de cada caractere, sem
          + extração de número e sem                                     
      389  normalização de caixa: letras maiúsculas precedem as minúscula
           s, e, com
      390  identificadores de larguras diferentes, "d-10" precede "d-9". 
           O resultado
      391  não depende da ordem das despesas na entrada.

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 5 lines, removed 1 line
      418    "BISTRO CENTRAL" → grupo de duplicatas.
      419  - Duas despesas iguais no critério, com identificadores "d-006
           " e "D-006" →
      420    grupo de duplicatas; mantém-se "D-006", que precede "d-006" 
           pela ordem dos
      421 -  códigos dos caracteres; "d-006" → motivos [DUPLICATA].      
      421 +  pontos de código; "d-006" → motivos [DUPLICATA].            
      422 +- Duas despesas iguais no critério, com identificadores que di
          +ferem apenas no                                               
      423 +  último caractere — U+FF71 (ｱ, katakana de meia largura) num 
          +e U+1F600 (😀)                                                
      424 +  no outro → mantém-se o de U+FF71, cujo ponto de código é men
          +or; o outro →                                                 
      425 +  motivos [DUPLICATA].                                        
      426  
      427  ### RN-010 — Validação da entrada: erro de lote e erro de item
      428  

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 2 lines
      458     não é avaliada por nenhuma regra de elegibilidade ou de val
           or; não integra
      459     grupo de duplicatas (RN-009). A saída traz todas as causas 
           do erro, na
      460     ordem em que as informações da despesa são declaradas na §4
           .1 (AMB-021);
      461 +   o elemento que não é uma despesa tem uma única causa: a pró
          +pria despesa,                                                 
      462 +   com tipo incompatível (AMB-020);                           
      463     a despesa cujo identificador falta ou tem tipo incompatível
            é identificada
      464     pela sua posição na relação de despesas, contada a partir d
           e um (AMB-020).
      465     As demais despesas do lote são processadas normalmente.

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 2 lines, removed 1 line
      506  - Duas despesas sem identificador → ambas erro de item; não co
           nstituem
      507    identificador repetido; o lote não é rejeitado.
      508  - Elemento da relação de despesas que não é uma despesa → erro
            de item,
      509 -  identificado pela sua posição; as demais despesas são proces
          -sadas.                                                        
      509 +  identificado pela sua posição, com uma única causa: a própri
          +a despesa, com                                                
      510 +  tipo incompatível; as demais despesas são processadas.      
      511  - Relação de despesas com tipo incompatível → lote rejeitado.
      512  - Período com tipo incompatível → lote rejeitado.
      513  - Entrada sintaticamente válida cuja estrutura não é a de um l
           ote — por

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 3 lines, removed 2 lines
       998  - d-008 (alimentação, 41,00, com NF, 15/04) → reembolso 0,00,
             recusado,
       999    motivos [FORA_DA_COMPETENCIA]
      1000  - d-014 (31/07) → dentro do período; esta regra não se aplica
      1001 -- uma despesa fora do período não participa de nenhuma agrega
           -ção ou                                                       
      1002 -  comparação com outras despesas                             
      1001 +- uma despesa fora do período não é avaliada por nenhuma regr
           +a de valor e não                                             
      1002 +  integra grupo de duplicatas; as demais regras de elegibilid
           +ade continuam a                                              
      1003 +  ser avaliadas e acrescentam seus motivos (RN-007)          
      1004  
      1005  **Regra afetada:** RN-006
      1006  

● Update(specs\001-motor-reembolso\spec.md)  ⎿  Added 2 lines, removed 2 lines
      1015  coincidem em data, categoria normalizada (AMB-013), fornecedo
            r normalizado
      1016  (AMB-013) e valor normalizado (AMB-014). A descrição e a pres
            ença de nota
      1017  fiscal não integram o critério. Dentro de um grupo, a despesa
             de menor
      1018 -identificador — na ordem dos códigos dos caracteres, comparad
           -os um a um                                                   
      1019 -(RN-009) — é avaliada normalmente e as demais são recusadas, 
           -com o motivo                                                 
      1018 +identificador — na ordem dos pontos de código Unicode dos car
           +acteres,                                                     
      1019 +comparados um a um (RN-009) — é avaliada normalmente e as dem
           +ais são recusadas, com o motivo                              
      1020  DUPLICATA em sua lista de motivos. Despesas já recusadas por 
            outra regra de
      1021  elegibilidade não integram grupos de duplicatas.
      1022  **Justificativa:** incluir a descrição no critério faria uma 
            diferença de

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 7 lines, removed 2 lines
      1028  trataria "devem ser tratadas" como recomendação — objeção já 
            levantada na
      1029  AMB-005. Manter a de menor identificador, e não a primeira da
             entrada,
      1030  preserva o critério de aceite da RN-001, segundo o qual inver
            ter a ordem das
      1031 -despesas na entrada não altera nenhum resultado. A ordenação 
           -pelos códigos                                                
      1032 -dos caracteres é a única que produz resultado determinístico 
      1031 +despesas na entrada não altera nenhum resultado. A ordenação 
           +pelos pontos de                                              
      1032 +código é a única que produz resultado determinístico         
      1033  independentemente de configuração regional ou de convenção al
            fabética local;
      1034 +e é o ponto de código, e não a forma como o texto é codificad
           +o, porque a                                                  
      1035 +comparação precisa ser independente da representação interna 
           +de texto                                                     
      1036 +adotada pela implementação: em representações que dividem um 
           +caractere fora                                               
      1037 +do plano multilíngue básico em duas unidades, a ordem por uni
           +dade diverge da                                              
      1038 +ordem por caractere, e a despesa mantida mudaria com a repres
           +entação;                                                     
      1039  uma ordenação que ignore a caixa deixaria "d-006" e "D-006" e
            mpatados, sem
      1040  critério de desempate, e a independência de ordem exigida pel
            a RN-001 só se
      1041  garante com um critério total.

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 2 lines
      1059    grupo de duplicatas
      1060  - uma despesa fora do período (RN-006) não integra grupo de d
            uplicatas, nem
      1061    como mantida nem como recusada
      1062 +- identificadores que diferem apenas por U+FF71 num e U+1F600
           + no outro → o                                                
      1063 +  de U+FF71 precede, por ter o menor ponto de código         
      1064  
      1065  **Regra afetada:** RN-009; AMB-009 (data de cada diária)
      1066  

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 3 lines, removed 2 lines
      1096  **Decisão:** a categoria é normalizada antes da comparação, c
            onvertendo-se
      1097  maiúsculas em minúsculas e removendo-se espaços nas extremida
            des. Nenhuma
      1098  outra transformação é aplicada: acentuação, separadores e sin
            ônimos não são
      1099 -normalizados. As categorias elegíveis são exatamente três — a
           -limentação,                                                  
      1100 -transporte urbano e hospedagem. Qualquer outra categoria é in
           -elegível, e a                                                
      1099 +normalizados. As categorias elegíveis são exatamente três — a
           +limentacao,                                                  
      1100 +transporte_urbano e hospedagem, na grafia do vocabulário de c
           +ategoria                                                     
      1101 +(§4.2, Notação). Qualquer outra categoria é inelegível, e a  
      1102  despesa é recusada com o motivo CATEGORIA_INELEGIVEL em sua l
            ista de motivos.
      1103  **Justificativa:** a categoria é campo estruturado com vocabu
            lário fechado, e
      1104  uniformizar a caixa não constitui interpretação de texto livr
            e — distingue-se,

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 1 line, removed 1 line
      1127  inclusão de novas categorias cabe ao RH, por alteração da pol
            ítica.
      1128  **Consequências verificáveis:**
      1129  - d-014 ("ALIMENTACAO", 61,00, com NF, 31/07) → categoria nor
            malizada para
      1130 -  alimentação; dentro do período; reembolso 60,00, parcial; l
           -ista de motivos                                              
      1130 +  alimentacao; dentro do período; reembolso 60,00, parcial; l
           +ista de motivos                                              
      1131    vazia
      1132  - d-005 ("coworking", 89,00, com NF) → reembolso 0,00, recusa
            do, motivos
      1133    [CATEGORIA_INELEGIVEL]

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 3 lines, removed 2 lines
      1593  - [ ] Uma despesa com informação obrigatória ausente — inclus
            ive o
      1594    identificador —, tipo incompatível, data inexistente ou val
            or normalizado
      1595    menor ou igual a zero, ou que não é uma despesa, recebe cla
            ssificação erro,
      1596 -  reembolso 0,00, todas as causas do erro na ordem da §4.1 e 
           -lista de motivos                                             
      1597 -  vazia; sem identificador, é identificada pela posição; as d
           -emais despesas                                               
      1596 +  reembolso 0,00, todas as causas do erro na ordem da §4.1 — 
           +ou, se não é                                                 
      1597 +  uma despesa, a causa única "a própria despesa, com tipo inc
           +ompatível" — e                                               
      1598 +  lista de motivos vazia; sem identificador, é identificada p
           +ela posição; as demais despesas                              
      1599    são processadas normalmente (RN-010, AMB-020, AMB-021; d-00
            9).
      1600  - [ ] Espaços nas extremidades são todos os caracteres de esp
            aço em branco do
      1601    padrão Unicode, e o identificador apresentado é o normaliza
            do (§4.1,

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 1 line, removed 1 line
      1619    RN-004; d-003, d-004, d-013).
      1620  - [ ] Despesas que coincidem em data, categoria e fornecedor 
            normalizados e
      1621    valor normalizado formam grupo; mantém-se a de menor identi
            ficador pela
      1622 -  ordem dos códigos dos caracteres, e as demais recebem DUPLI
           -CATA; despesas                                               
      1622 +  ordem dos pontos de código Unicode, e as demais recebem DUP
           +LICATA; despesas                                             
      1623    com outro motivo ou com erro não integram grupo (RN-009; d-
            006, d-007).
      1624  - [ ] Todos os motivos de recusa aplicáveis são reportados, n
            a ordem da §8, e
      1625    o reembolso parcial não é motivo (RN-007).

● Bash(cd /c/Users/dev/SDD-Desafio/sdd-desafio/specs/001-motor-reembolso && grep
      -n 'pelo ponto de código Unicode de cada caractere' spec.md && sed -n
      '379,386p' spec.…)
  ⎿  (No output)

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 2 lines, removed 2 lines
      385  a despesa de menor identificador é avaliada normalmente e as d
           emais são
      386  recusadas, com o motivo DUPLICATA na sua lista de motivos (RN-
           007). Os
      387  identificadores são comparados em ordem lexicográfica, caracte
           re a
      388 -caractere, pelo ponto de código Unicode de cada caractere, sem
          - extração de número e sem                                     
      389 -normalização de caixa: letras maiúsculas precedem as minúscula
          -s, e, com                                                     
      388 +caractere, pelo ponto de código Unicode de cada caractere, sem
          + extração de                                                  
      389 +número e sem normalização de caixa: letras maiúsculas precedem
          + as minúsculas, e, com                                        
      390  identificadores de larguras diferentes, "d-10" precede "d-9". 
           O resultado
      391  não depende da ordem das despesas na entrada.
      392  **Origem:** política do RH, item 8; decisão AMB-011

● Update(specs\001-motor-reembolso\DECISIONS.md)
  ⎿  Added 85 lines
       10  
       11  ---
       12  
       13 +## D-010 — Achados de revisão independente da spec antes das r
          +egras de negócio · `2026-10-18`                               
       14 +                                                              
       15 +**Gatilho:** revisão independente da spec 1.1 por um subagente
          + com contexto                                                 
       16 +limpo — sem ter participado de nenhuma decisão e sem acesso a 
          +plan.md,                                                      
       17 +tasks.md, DECISIONS.md ou código —, feita depois da fundação (
          +T-001 a T-004)                                                
       18 +e antes da implementação das regras de negócio (T-005 em diant
          +e). O                                                         
       19 +subagente procurou contradições, indeterminações, decisões não
          + refletidas                                                   
       20 +nas regras e vazamentos técnicos, e relatou com citação, sem a
          +lterar nada.                                                  
       21 +Quatro achados foram corrigidos aqui; os demais são de redação
          + e não alteram                                                
       22 +nenhum resultado (ver Pendente).                              
       23 +                                                              
       24 +**O que mudou na spec (versão 1.1 → 1.2):**                   
       25 +- **Ponto de código (achado D4, reclassificado).** RN-009, AMB
          +-011 e §9: de                                                 
       26 +  "pelos códigos dos caracteres" para "pelo ponto de código Un
          +icode de cada                                                 
       27 +  caractere". AMB-011, Justificativa: acrescentado o porquê. R
          +N-009 e                                                       
       28 +  AMB-011: um caso acrescentado — identificadores que diferem 
          +apenas por                                                    
       29 +  U+FF71 e U+1F600 → o de U+FF71 é mantido.                   
       30 +- **Vocabulário de categoria (achados A1 e D5).** §4.2, Notaçã
          +o: alimentacao,                                               
       31 +  transporte_urbano e hospedagem declarados como vocabulário d
          +e valores de                                                  
       32 +  categoria da entrada — identificadores de negócio definidos 
          +pela spec, como                                               
       33 +  os códigos de motivo. AMB-013, Decisão: de "exatamente três 
          +— alimentação,                                                
       34 +  transporte urbano e hospedagem" para "exatamente três — alim
          +entacao,                                                      
       35 +  transporte_urbano e hospedagem, na grafia do vocabulário de 
          +categoria".                                                   
       36 +  AMB-013, Consequências: d-014 de "normalizada para alimentaç
          +ão" para                                                      
       37 +  "normalizada para alimentacao".                             
       38 +- **Causa única do elemento que não é despesa (achados C1 e B1
          +).** §4.1                                                     
       39 +  (Despesa inválida), RN-010 (item 2 e aceite), §4.2 (Causas d
          +o erro, com                                                   
       40 +  AMB-020 acrescentada à origem) e §9: o elemento da relação q
          +ue não é uma                                                  
       41 +  despesa tem uma única causa — a própria despesa, com tipo in
          +compatível. A                                                 
       42 +  decisão já existia na AMB-020; não tinha chegado às regras q
          +ue ela declara                                                
       43 +  afetar.                                                     
       44 +- **Alcance da despesa fora do período (achado A5).** RN-006 e
          + AMB-010,                                                     
       45 +  Consequências: de "não participa de nenhuma agregação ou com
          +paração com                                                   
       46 +  outras despesas" para "não é avaliada por nenhuma regra de v
          +alor e não                                                    
       47 +  integra grupo de duplicatas; as demais regras de elegibilida
          +de continuam a                                                
       48 +  ser avaliadas e acrescentam seus motivos (RN-007)".         
       49 +                                                              
       50 +**Classificação:** esclarecimentos. Nenhuma AMB nova: cada cor
          +reção declara                                                 
       51 +o alcance de um termo ou leva uma decisão existente às regras 
          +que ela já                                                    
       52 +afetava.                                                      
       53 +- O ponto de código é o achado mais sério. O subagente o class
          +ificou como                                                   
       54 +  possível vazamento técnico de gravidade baixa; ele é, na ver
          +dade, regra de                                                
       55 +  negócio decidida fora da spec. "Códigos dos caracteres" não 
          +dizia se eram                                                 
       56 +  pontos de código ou unidades de codificação, e quem decidiu 
          +foi a DT-007                                                  
       57 +  do plan — o que o CLAUDE.md proíbe. A diferença muda qual de
          +spesa é                                                       
       58 +  mantida quando um identificador tem caractere fora do plano 
          +multilíngue                                                   
       59 +  básico. Justificativa da escolha: a comparação precisa ser i
          +ndependente da                                                
       60 +  representação interna de texto adotada pela implementação.  
       61 +- O vocabulário de categoria fecha o A1 pela raiz: a AMB-013 d
          +izia que                                                      
       62 +  acentuação e separadores não são normalizados e, em seguida,
          + listava como                                                 
       63 +  elegível justamente a grafia acentuada e com espaço.        
       64 +- No A5, a redação "não participa de nenhuma regra de elegibil
          +idade nem de                                                  
       65 +  valor" foi considerada e descartada: contradiria a RN-007 e 
          +o caso da §7                                                  
       66 +  em que a despesa de 150,00 sem NF fora do período recebe    
       67 +  [FORA_DA_COMPETENCIA, SEM_NOTA_FISCAL]. A restrição adotada 
          +nomeia o que                                                  
       68 +  de fato não se aplica — regras de valor e duplicidade — e de
          +ixa explícito                                                 
       69 +  que a RN-012 (identificador) e o consolidado continuam a con
          +siderar a                                                     
       70 +  despesa.                                                    
       71 +                                                              
       72 +**Por quê:** cada um dos quatro achados admitia leituras com r
          +esultados                                                     
       73 +diferentes, ou deixava uma decisão fora da spec. Corrigi-los a
          +ntes da T-005                                                 
       74 +evita que o código fixe uma das leituras por conta própria.   
       75 +                                                              
       76 +**O que isso invalidou:** nenhum critério de aceite. O resulta
          +do do lote de                                                 
       77 +exemplo é o mesmo. A RN-009 ganhou um caso; a RN-010 explicita
          + a causa de um                                                
       78 +caso que já existia.                                          
       79 +                                                              
       80 +**Tasks afetadas:** nenhuma precisa ser reescrita. T-007 (caus
          +a única: o                                                    
       81 +modelo já tem `CampoDespesa.DESPESA`) e T-013 (o teste        
       82 +`RN-009 identificador fora do BMP é comparado por code point` 
          +passa a                                                       
       83 +verificar um aceite da spec, e não só a DT-007) já seguiam as 
          +leituras                                                      
       84 +adotadas. A DT-007 do plan deixa de decidir e passa a implemen
          +tar a RN-009.                                                 
       85 +                                                              
       86 +**Pendente:** achados de redação da mesma revisão, sem efeito 
          +em resultado,                                                 
       87 +para uma passada final — A2 (Justificativa da AMB-018 sobre "r
          +ótulo"), A3                                                   
       88 +("três campos" na AMB-015, que abrange cinco), A4 (§1 descreve
          + as 21 AMB                                                    
       89 +como lacunas preenchidas por conferentes), B2 (caso de 150,00 
          +sem categoria                                                 
       90 +fixada), D1 (referências a "plan.md" por nome de arquivo), D2 
          +(frase do                                                     
       91 +template sobre tasks na §5), D3 ("sintaticamente válida") e D6
          + (histórico de                                                
       92 +processo na AMB-020 e AMB-021).                               
       93 +                                                              
       94 +**Custo:** 2 arquivos (spec.md, DECISIONS.md), correção pontua
          +l.                                                            
       95 +                                                              
       96 +---                                                           
       97 +                                                              
       98  ## D-009 — Lacunas de validação descobertas ao projetar a impl
           ementação; AMB-020 e AMB-021 · `2026-10-18`
       99  
      100  **Gatilho:** a redação do plan.md. Ao desenhar o modelo de dad
           os e o

● Bash(cd /c/Users/dev/SDD-Desafio/sdd-desafio && grep -n 'códigos dos
      caracteres\|alimentação, transporte urbano e hospedagem\|nenhuma agregação
      ou' specs/001-motor-r…)
  ⎿  2 files changed (binary, mode only or too large to show)

● Três linhas ficaram longas demais depois das edições. Vou reorganizar a quebra
  delas antes de mostrar o resultado.

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 3 lines, removed 2 lines
      386  recusadas, com o motivo DUPLICATA na sua lista de motivos (RN-
           007). Os
      387  identificadores são comparados em ordem lexicográfica, caracte
           re a
      388  caractere, pelo ponto de código Unicode de cada caractere, sem
            extração de
      389 -número e sem normalização de caixa: letras maiúsculas precedem
          - as minúsculas, e, com                                        
      390 -identificadores de larguras diferentes, "d-10" precede "d-9". 
          -O resultado                                                   
      389 +número e sem normalização de caixa: letras maiúsculas precedem
          + as                                                           
      390 +minúsculas, e, com identificadores de larguras diferentes, "d-
          +10" precede                                                   
      391 +"d-9". O resultado                                            
      392  não depende da ordem das despesas na entrada.
      393  **Origem:** política do RH, item 8; decisão AMB-011
      394  **Depende de:** RN-007 (lista de motivos); RN-008 (categoria e
            fornecedor

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 2 lines, removed 2 lines
      1017  (AMB-013) e valor normalizado (AMB-014). A descrição e a pres
            ença de nota
      1018  fiscal não integram o critério. Dentro de um grupo, a despesa
             de menor
      1019  identificador — na ordem dos pontos de código Unicode dos car
            acteres,
      1020 -comparados um a um (RN-009) — é avaliada normalmente e as dem
           -ais são recusadas, com o motivo                              
      1021 -DUPLICATA em sua lista de motivos. Despesas já recusadas por 
           -outra regra de                                               
      1020 +comparados um a um (RN-009) — é avaliada normalmente e as dem
           +ais são                                                      
      1021 +recusadas, com o motivo DUPLICATA em sua lista de motivos. De
           +spesas já recusadas por outra regra de                       
      1022  elegibilidade não integram grupos de duplicatas.
      1023  **Justificativa:** incluir a descrição no critério faria uma 
            diferença de
      1024  acentuação ou de redação desfazer a detecção, e a spec já est
            abeleceu que a

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 2 lines, removed 2 lines
      1596    menor ou igual a zero, ou que não é uma despesa, recebe cla
            ssificação erro,
      1597    reembolso 0,00, todas as causas do erro na ordem da §4.1 — 
            ou, se não é
      1598    uma despesa, a causa única "a própria despesa, com tipo inc
            ompatível" — e
      1599 -  lista de motivos vazia; sem identificador, é identificada p
           -ela posição; as demais despesas                              
      1600 -  são processadas normalmente (RN-010, AMB-020, AMB-021; d-00
           -9).                                                          
      1599 +  lista de motivos vazia; sem identificador, é identificada p
           +ela posição; as                                              
      1600 +  demais despesas são processadas normalmente (RN-010, AMB-02
           +0, AMB-021; d-009).                                          
      1601  - [ ] Espaços nas extremidades são todos os caracteres de esp
            aço em branco do
      1602    padrão Unicode, e o identificador apresentado é o normaliza
            do (§4.1,
      1603    RN-012).

● Bash(git add specs && git diff --cached --stat && git diff --cached -U0 --
      specs/001-motor-reembolso/spec.md | grep '^+' | awk 'length > 90' | grep
      -v '^+|' ; echo "…)
  ⎿  2 files changed (binary, mode only or too large to show)

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 3 lines, removed 2 lines
      1018  fiscal não integram o critério. Dentro de um grupo, a despesa
             de menor
      1019  identificador — na ordem dos pontos de código Unicode dos car
            acteres,
      1020  comparados um a um (RN-009) — é avaliada normalmente e as dem
            ais são
      1021 -recusadas, com o motivo DUPLICATA em sua lista de motivos. De
           -spesas já recusadas por outra regra de                       
      1022 -elegibilidade não integram grupos de duplicatas.             
      1021 +recusadas, com o motivo DUPLICATA em sua lista de motivos. De
           +spesas já                                                    
      1022 +recusadas por outra regra de elegibilidade não integram grupo
           +s de                                                         
      1023 +duplicatas.                                                  
      1024  **Justificativa:** incluir a descrição no critério faria uma 
            diferença de
      1025  acentuação ou de redação desfazer a detecção, e a spec já est
            abeleceu que a
      1026  descrição não é interpretada; exigir apenas o identificador r
            epetido tornaria

● Bash(git add specs && git diff --cached --stat && git status --short)
  ⎿  (No output)

● As quatro correções estão feitas e no índice, junto com a D-010. Não commitei,
  porque mudei a sua redação do A5 e você precisa ver isso antes. O ./gradlew
  build continua passando, inclusive os testes que leem a §4.1 e a §8. O seu
  docs/notas-trabalho.md, que está modificado, ficou fora do índice.

  O que mudou na spec (1.1 → 1.2)
  - D4, ponto de código: a RN-009, a AMB-011 e a §9 dizem agora "pelo ponto de
    código Unicode de cada caractere". A justificativa entrou na AMB-011: a
    comparação precisa ser independente da representação interna de texto
    adotada pela implementação.
  - A1 e D5, vocabulário de categoria:
    - A AMB-013 lista alimentacao, transporte_urbano e hospedagem, e a
      consequência sobre d-014 diz "normalizada para alimentacao".
    - A declaração do vocabulário ficou no parágrafo "Notação usada nesta spec",
      que está na §4.2, não na §4.1. Segui o "junto à notação já existente" do
      seu pedido.
  - C1 e B1, causa única: a regra de que o elemento que não é despesa tem uma
    única causa entrou no aceite da RN-010, na §4.2 e na §9, como você pediu.
    - Pus também no item 2 da RN-010 e no parágrafo "Despesa inválida" da §4.1,
      porque os dois repetem "todas as causas são reportadas" e ficariam
      contraditórios.
    - Na §4.2, a AMB-020 entrou na origem da linha "Causas do erro".
  - A5: a regra mudou na RN-006 e nas consequências da AMB-010, mas com redação
    diferente da sua (explicado abaixo).

  Precisa da sua aprovação
  1. A redação do A5 não é a que você propôs. "Não participa de nenhuma regra de
     elegibilidade nem de valor" contradiria a RN-007 e o caso da §7: a despesa
     de 150,00 sem NF fora do período recebe [FORA_DA_COMPETENCIA,
     SEM_NOTA_FISCAL]. Logo, ela passa sim pela regra de nota fiscal. A frase
     que usei é: "não é avaliada por nenhuma regra de valor e não integra grupo
     de duplicatas (RN-009); as demais regras de elegibilidade continuam a ser
     avaliadas e acrescentam seus motivos (RN-007)." A D-010 registra a
     alternativa descartada e o motivo.
  2. Um aceite a mais na RN-009 e na AMB-011, que você não pediu:
     identificadores que diferem só por U+FF71 e U+1F600 mantêm o de U+FF71. Sem
     ele, o esclarecimento do ponto de código não teria nenhum caso verificável
     na spec. Com ele, o teste da T-013 passa a verificar a spec, e não só a
     DT-007. O caractere ｱ e o emoji aparecem visíveis no texto, como
     ilustração.

  A D-010 registra:
  - o gatilho, isto é, a revisão independente por subagente com contexto limpo,
    antes das regras de negócio;
  - as mudanças, classificadas como esclarecimentos;
  - a reclassificação do D4, que o subagente tinha marcado como vazamento
    técnico;
  - que nenhum critério de aceite foi invalidado e nenhuma task precisa ser
    reescrita;
  - os oito achados de redação como pendentes.

  Fica inconsistente se eu não mexer: o plan.md e o tasks.md ainda dizem
  "Baseado na spec: 1.1". Corrigir isso exige um docs(plan)/docs(tasks) à parte,
  que não fiz por você ter pedido um commit docs(spec): próprio.

  Mensagem proposta:
  docs(spec): esclarecimentos da revisão independente (ponto de código,
  vocabulário de categoria, causa única, alcance da RN-006); D-010

  Posso commitar? E quer que eu atualize a referência de versão no plan e no
  tasks em seguida?

> Aprovado, com a sua redação do A5. Você está certo e eu errei: "não
  participa de nenhuma regra de elegibilidade" contradiz a RN-007 e o caso da
  §7, onde a despesa de 150,00 sem nota e fora do período recebe dois motivos
  — logo passa pela regra de nota fiscal. Minha frase teria entrado no código
  da T-005 e produzido um motivo a menos.

  O aceite com U+FF71 e U+1F600 está aprovado e é necessário: sem um caso
  verificável, o esclarecimento do ponto de código existiria apenas no plan, e
  o teste da T-013 verificaria a decisão técnica em vez da regra.

  Pôr a declaração do vocabulário na §4.2, junto à notação existente, está
  certo — era onde o parágrafo estava.

  E sim, atualize a referência de versão no plan.md e no tasks.md para 1.2,
  num commit docs: próprio depois deste. Documento que aponta para versão
  anterior da spec é a mesma classe de problema que a revisão acabou de
  encontrar.

  Pode commitar.

● Bash(git commit -q -F - <<'EOF'
      docs(spec): esclarecimentos da revisão independente (ponto de código,
      vocabulário de categoria, causa única, alcance da RN-006); D-01…)
  ⎿  (No output)

● A spec foi commitada como 2af517f. Agora vou atualizar as referências de
  versão e corrigir uma citação obsoleta na DT-007 do plan ("Códigos dos
  caracteres" / RN-009), termo que a spec não usa mais.

● Bash(grep -n 'Códigos dos caracteres' specs/001-motor-reembolso/plan.md && sed
      -n '/^### DT-007/,/^### DT-008/p' specs/001-motor-reembolso/plan.md)  ⎿  (No output)

● Bash(cd /c/Users/dev/SDD-Desafio/sdd-desafio/specs/001-motor-reembolso && sed
      -i 's/^\*\*Versão:\*\* 1\.1 · \*\*Baseado na spec:\*\* 1\.1$/**Versão:**
      1.1 · **Basead…)
  ⎿  Updated specs\001-motor-reembolso\plan.md (+3 -2)
      1  # Plano Técnico — Motor de Cálculo de Reembolso
      2  
      3 -**Versão:** 1.1 · **Baseado na spec:** 1.1                      
      3 +**Versão:** 1.1 · **Baseado na spec:** 1.2                      
      4  
      5  > Aqui mora o COMO. Este arquivo pode e deve falar de linguagem,
          biblioteca e
      6  > arquitetura. O que ele **não** pode é introduzir regra de negó
         cio nova — se
     ...
      346  identificador e marca as demais com `DUPLICATA` e `mantida`. "
           Menor" é
      347  comparado por **code point Unicode**, com um comparador própri
           o, porque o
      348  `String.compareTo` do Kotlin compara unidades UTF-16 e diverge
            fora do BMP.
      349 -"Códigos dos caracteres" (RN-009) é code point.               
      349 +A RN-009 fixa a comparação pelo ponto de código Unicode (spec 
          +1.2, D-010);                                                  
      350 +esta DT implementa a regra, não a decide.                     
      351  **Alternativa descartada:** comparar cada despesa com as anter
           iores na ordem
      352  da entrada, o que torna o resultado dependente da ordem e viol
           a a RN-001 e a
      353  RN-009.
  ⎿  Updated specs\001-motor-reembolso\tasks.md (+1 -1)
      1  # Tasks — Motor de Cálculo de Reembolso
      2  
      3 -**Baseado em:** spec 1.1 · plan 1.1                             
      3 +**Baseado em:** spec 1.2 · plan 1.1                             
      4  
      5  > Cada task é pequena o bastante para virar **um commit**. Se vo
         cê não consegue
      6  > descrever o critério de aceite como "o teste X passa", a task 
         está grande demais.

● Bash(git commit -q -F - <<'EOF'
      docs: plan e tasks referenciam a spec 1.2; DT-007 implementa a RN-009 em
      vez de decidi-la…)
  ⎿  (No output)

● Fiz os dois commits na main, sem push:

  - 2af517f docs(spec): as quatro correções da revisão, com a sua aprovação,
    mais a D-010. A spec agora está na versão 1.2.
  - 818d6a6 docs:
    - o plan.md e o tasks.md agora dizem "Baseado na spec: 1.2";
    - a DT-007 do plan perdeu a citação obsoleta "'Códigos dos caracteres'
      (RN-009) é code point". No lugar, ela diz: "A RN-009 fixa a comparação
      pelo ponto de código Unicode (spec 1.2, D-010); esta DT implementa a
      regra, não a decide."

  Deixei a versão do próprio plan em 1.1, porque nenhuma decisão técnica mudou:
  só a referência à spec e uma citação. Se você preferir subir para 1.2 por
  causa da DT-007, é uma linha.

  O docs/notas-trabalho.md continua modificado e fora dos dois commits, para o
  seu commit próprio.

  A próxima é a T-005, a validação de lote.

> Mantenha o plan em 1.1 — você está certo, nenhuma decisão técnica mudou.

  Vamos para a T-005: validação de lote. Período e despesas ausentes ou com
  tipo incompatível, datas, início posterior ao fim, identificador repetido.
  Reporta todas as causas detectáveis.

  Atende RN-010 (lote), RN-012, AMB-017, AMB-019 e AMB-020 (ii).

  Aceite: os quatro testes da task — início posterior ao fim rejeita o lote;
  início posterior ao fim mais identificador repetido reporta as duas causas;
  duas despesas com identificador d-006 rejeitam o lote; período com tipo
  incompatível rejeita o lote.

  Marque a T-005 como [x] no tasks.md no mesmo commit. Rode ./gradlew build e
  me mostre o diff antes de commitar.

● Bash(sed -n '/^### RN-010/,/^2\. \*\*Erro de item/p'
      specs/001-motor-reembolso/spec.md; sed -n '/^### RN-012/,/^### RN-011/p'
      specs/001-motor-reembolso/spec.md; sed…)
  ⎿  (No output)

● Encontrei a mesma divergência que na T-002: a T-005 em tasks.md define onze
  testes, não quatro, embora os quatro citados estejam entre eles. Vou
  implementar os onze testes definidos na task.

● Write(src\reembolso\dominio\ValidacaoDeLote.kt)
  ⎿  Wrote 48 lines to src\reembolso\dominio\ValidacaoDeLote.kt
       1 package reembolso.dominio
       3 // RN-010 (erro de lote) e RN-012. Devolve a rejeição com todas
         as causas detectáveis,
       4 // ou null se o lote pode ser avaliado.
       6     val causas = causasDoPeriodo(lote.periodo) + causasDasDespes
         as(lote.despesas)
         do(causas)
       8 }
       9
         e> = when (periodo) {
     … +53 lines (ctrl+o to expand)

● Write(tests\reembolso\regras\Lotes.kt)
  ⎿  Wrote 32 lines to tests\reembolso\regras\Lotes.kt
      1 package reembolso.regras
      2
      3 import java.math.BigDecimal
      4 import java.time.LocalDate
      5 import reembolso.dominio.DespesasLidas
      6 import reembolso.dominio.ElementoLido
      7 import reembolso.dominio.Lido
      8 import reembolso.dominio.LoteLido
      9 import reembolso.dominio.PeriodoLido
     … +28 lines (ctrl+o to expand)

● Write(tests\reembolso\regras\RN010ValidacaoDeLoteTest.kt)
  ⎿  Wrote 75 lines to tests\reembolso\regras\RN010ValidacaoDeLoteTest.kt
       1 package reembolso.regras
       2
       3 import kotlin.test.Test
       4 import kotlin.test.assertEquals
       5 import kotlin.test.assertNull
       6 import reembolso.dominio.CausaLote
       7 import reembolso.dominio.DespesasLidas
       8 import reembolso.dominio.Lido
       9 import reembolso.dominio.PeriodoLido
      10 import reembolso.dominio.ResultadoLote
     … +83 lines (ctrl+o to expand)

● Write(tests\reembolso\regras\RN012IdentificadorUnicoTest.kt)
  ⎿  Wrote 56 lines to
     tests\reembolso\regras\RN012IdentificadorUnicoTest.kt
      1 package reembolso.regras
      2
      3 import kotlin.test.Test
      4 import kotlin.test.assertEquals
      5 import kotlin.test.assertNull
      6 import reembolso.dominio.CausaLote
      7 import reembolso.dominio.Lido
      8 import reembolso.dominio.ResultadoLote
      9 import reembolso.dominio.validarLote
     … +58 lines (ctrl+o to expand)

● Vou separar a verificação de despesas ausentes num teste próprio, já que
  estava misturada com o teste de tipo incompatível, e trocar o nome qualificado
  reembolso.dominio.LoteLido por um import.

● Update(tests\reembolso\regras\RN010ValidacaoDeLoteTest.kt)
  ⎿  Added 4 lines
      40              listOf(CausaLote.DespesasTipoIncompativel),
      41              causas(lote(despesas = DespesasLidas.TipoIncompativ
          el)),
      42          )
      43 +    }                                                          
      44 +                                                               
      45 +    @Test                                                      
      46 +    fun `RN-010 despesas não informadas → lote rejeitado`() {  
      47          assertEquals(listOf(CausaLote.DespesasAusentes), causas
          (lote(despesas = DespesasLidas.Ausente)))
      48      }
      49  

● Update(tests\reembolso\regras\RN010ValidacaoDeLoteTest.kt)
  ⎿  Added 2 lines, removed 1 line
       6  import reembolso.dominio.CausaLote
       7  import reembolso.dominio.DespesasLidas
       8  import reembolso.dominio.Lido
       9 +import reembolso.dominio.LoteLido                              
      10  import reembolso.dominio.PeriodoLido
      11  import reembolso.dominio.ResultadoLote
      12  import reembolso.dominio.validarLote
      13  
      14  class RN010ValidacaoDeLoteTest {
      15  
      15 -    private fun causas(lote: reembolso.dominio.LoteLido) = vali
         -darLote(lote)?.causas                                          
      16 +    private fun causas(lote: LoteLido) = validarLote(lote)?.cau
         +sas                                                            
      17  
      18      @Test
      19      fun `RN-010 AMB-017 início posterior ao fim → lote rejeitad
          o sem despesa processada`() {

● Bash(sed -i 's/- \[ \] \*\*T-005\*\*/- [x] **T-005**/'
      specs/001-motor-reembolso/tasks.md && export JAVA_HOME="/c/Program
      Files/Android/Android Studio/jbr" && ./grad…)
  ⎿  Updated specs\001-motor-reembolso\tasks.md (+1 -1)
      80  
      81  Todos os testes desta fase constroem `LoteLido` direto, sem JSO
          N (plan §6).
      82  
      83 -- [ ] **T-005** — Validação de lote: período ausente ou de tipo
         - incompatível,                                                 
      83 +- [x] **T-005** — Validação de lote: período ausente ou de tipo
         + incompatível,                                                 
      84    início ou fim ausente ou inválido, início posterior ao fim, d
          espesas não
      85    informadas ou de tipo incompatível, identificador repetido ap
          ós `aparar()`.
      86    Todas as causas detectáveis são reportadas.
  ⎿  … 5 more files changed
  ⎿  (timeout 5m)

● Todos os testes passam. Agora vou verificar se eles falham corretamente,
  desfazendo uma por vez três validações: o aparar() do identificador, a
  checagem de início posterior ao fim, e o tratamento de identificador vazio
  como repetição.

● Bash(export JAVA_HOME="/c/Program Files/Android/Android Studio/jbr" &&
      F=src/reembolso/dominio/ValidacaoDeLote.kt &&
      S=/c/Users/dev/AppData/Local/Temp/claude/c--User…)
  ⎿  5 files changed (binary, mode only or too large to show)
  ⎿  (timeout 10m)

● Bash(git add specs/001-motor-reembolso/tasks.md src tests && git status
      --short && git diff --cached)
  ⎿  (No output)

● A T-005 está pronta: o diff completo está acima e as alterações estão no
  índice, mas não commitei. O seu docs/notas-trabalho.md continua fora do
  índice.

  O ./gradlew build passa com 25 testes e nenhuma falha. Destes, 13 são da
  T-005: 8 no RN010ValidacaoDeLoteTest e 5 no RN012IdentificadorUnicoTest. O
  checklist de revisão está limpo, sem nenhum .trim() nem Double em src/.

  Os testes falham quando devem. Fiz três alterações de propósito na validação,
  uma por vez, e todas quebraram o build:

  ┌─────────────────────────────────────────────┬───────────────────────────┐
  │                  Alteração                  │    Testes que falharam    │
  ├─────────────────────────────────────────────┼───────────────────────────┤
  │ sem aparar() no identificador               │ 1 (o de espaço à          │
  │                                             │ esquerda)                 │
  ├─────────────────────────────────────────────┼───────────────────────────┤
  │ sem a verificação de início posterior ao    │ 2                         │
  │ fim                                         │                           │
  ├─────────────────────────────────────────────┼───────────────────────────┤
  │ identificador vazio contando como repetição │ 1                         │
  └─────────────────────────────────────────────┴───────────────────────────┘

  Depois restaurei o arquivo e o build voltou a passar.

  Diferenças em relação ao seu pedido e à task:
  - Onze testes, não quatro. Implementei os 11 da task com os nomes exatos. Os
    seus quatro estão entre eles.
  - Dois testes a mais, ambos para cobrir partes da task que nenhum dos onze
    verificava:
    - RN-010 despesas não informadas → lote rejeitado: a task cita "despesas não
      informadas", mas nenhum aceite cobre esse caso.
    - RN-012 AMB-020 duas despesas sem identificador ou com identificador vazio
      não constituem repetição: o lado de lote desse aceite da RN-012. O lado de
      erro de item fica para a T-007. Esse teste também cobre um identificador
      só de espaços, que é tratado como ausente (§4.1).
  - Sem Motor ainda. A entrada é validarLote(lote): ResultadoLote.Rejeitado?,
    que devolve null para lote válido. O Motor.avaliar só nasce na T-008, e vai
    chamar essa função como etapa 0.
    - "Sem despesa processada" é garantido pelo tipo: Rejeitado não tem
      resultado por despesa nem consolidado, e o teste compara o resultado
      inteiro.

  Decisões de implementação para você conferir:
  - Data inexistente (2026-02-30) e data de tipo incompatível produzem a mesma
    causa, InicioInvalido/FimInvalido. A spec diz "não é data válida" para os
    dois casos.
  - Ordem das causas: primeiro as do período (início, fim, início posterior ao
    fim), depois as das despesas.
  - Identificadores repetidos vêm numa única causa,
    IdentificadorRepetido(identificadores), na ordem em que aparecem pela
    primeira vez. A spec não define essa ordem, e ela não afeta nenhum resultado
    por despesa.
  - Lotes.kt reúne os construtores de LoteLido usados pelos testes de domínio.
    As tasks T-006 a T-014 vão reaproveitá-los.

  Mensagem proposta:
  feat(T-005): validação de lote com todas as causas detectáveis (RN-010,
  RN-012)

  Posso commitar?

> Pode commitar com a mensagem proposta.

  Implementar os 11 da task e não os 4 que citei está certo — mesma situação
  da T-002: o critério escrito prevalece sobre o pedido verbal.

  Os dois testes acrescentados são necessários. "Despesas não informadas"
  aparece na task sem aceite correspondente, e o lado de lote do aceite da
  RN-012 não estava coberto. Ambos fecham buracos reais.

  Verificar que os testes falham quando devem, com três alterações isoladas,
  é a parte que separa suíte que prova de suíte que acompanha.

  Sobre as decisões de implementação, as quatro estão corretas. Em particular:
  tratar data inexistente e data de tipo incompatível como a mesma causa segue
  a letra da spec, que fala em "não é data válida" sem distinguir; e a ordem
  dos identificadores repetidos não afeta nenhum resultado, de modo que não
  precisa estar na spec.

● Bash(git commit -q -F - <<'EOF'
      feat(T-005): validação de lote com todas as causas detectáveis (RN-010,
      RN-012)…)
  ⎿  (No output)

● Commitei a T-005 na main como bcfc828, com a mensagem aprovada. A working tree
  está limpa: pelo jeito você já commitou o notas-trabalho.md. Ainda não fiz
  push.

  A próxima é a T-006, a validação dos campos de cada despesa (RN-010, RN-011 e
  AMB-021).

> git push

● Bash(git push 2>&1 && git status -sb | head -1)
  ⎿  (No output)

● Fiz o push: a main local e a origin/main estão iguais, no bcfc828.

  Subiram juntos (03005ba..bcfc828) o commit da spec 1.2 (D-010), o da
  referência de versão no plan e no tasks, a T-005 e o seu commit do
  notas-trabalho.md.

> T-006: validação de item. Campos obrigatórios, tipo incompatível, data
  inexistente, texto vazio após aparar, valor normalizado ≤ 0. Reporta todas
  as causas na ordem da §4.1.

  Atende RN-010 (item), RN-011, AMB-012, AMB-015, AMB-017 e AMB-021.

  Aceite: os testes da task.

  Marque a T-006 como [x] no tasks.md. Rode ./gradlew build, verifique que os
  testes falham quando devem, e me mostre o diff antes de commitar.
● Bash(git status --short; sed -n '/^- \[ \] \*\*T-006/,/Commit/p;/^- \[ \]
      \*\*T-007/,/Commit/p' specs/001-motor-reembolso/tasks.md; grep -n '"id":
      "d-009"' -A8 exemp…)
  ⎿  (No output)

● Resolvo essa fronteira entre T-006 e T-007: a escolha entre Id/Posicao para a
  Referencia de erro fica na T-006, enquanto NaoEDespesa e os testes de
  identidade ficam para a T-007. Nesta task, sigo com a função recebendo apenas
  DespesaLida.

● Write(src\reembolso\dominio\ValidacaoDeItem.kt)
  ⎿  Wrote 55 lines to src\reembolso\dominio\ValidacaoDeItem.kt
       1 package reembolso.dominio
       2
       3 import java.math.BigDecimal
       5 sealed interface ItemValidado {
       6     data class Valido(val despesa: Despesa) : ItemValidado
       7     data class Invalido(val erro: ResultadoDespesa.ComErro) : It
         emValidado
       9
      10 // RN-010 (erro de item). Causas na ordem de CampoDespesa, que é
          a da §4.1 (AMB-021).
     … +62 lines (ctrl+o to expand)

● Removo um resto inútil que deixei no fim do arquivo: o import de BigDecimal e
  a tipoDoValorLido.

● Update(src\reembolso\dominio\ValidacaoDeItem.kt)
  ⎿  Removed 3 lines
      50      }
      51      return aparado
      52  }
      53 -                                                               
      54 -@Suppress("unused")                                            
      55 -private val tipoDoValorLido: Class<BigDecimal> = BigDecimal::cl
         -ass.java                                                       

● Update(src\reembolso\dominio\ValidacaoDeItem.kt)
  ⎿  Removed 2 lines
      1  package reembolso.dominio
      2  
      3 -import java.math.BigDecimal                                     
      4 -                                                                
      3  sealed interface ItemValidado {
      4      data class Valido(val despesa: Despesa) : ItemValidado
      5      data class Invalido(val erro: ResultadoDespesa.ComErro) : It
         emValidado

● Write(tests\reembolso\regras\RN010ValidacaoDeItemTest.kt)
  ⎿  Wrote 171 lines to tests\reembolso\regras\RN010ValidacaoDeItemTest.kt
       1 package reembolso.regras
       2
       3 import java.math.BigDecimal
       4 import kotlin.test.Test
       5 import kotlin.test.assertEquals
       6 import kotlin.test.assertIs
       7 import reembolso.dominio.CampoDespesa
       8 import reembolso.dominio.CausaErro
       9 import reembolso.dominio.ElementoLido
      10 import reembolso.dominio.Falha
     … +199 lines (ctrl+o to expand)

● Bash(sed -i 's/- \[ \] \*\*T-006\*\*/- [x] **T-006**/'
      specs/001-motor-reembolso/tasks.md && export JAVA_HOME="/c/Program
      Files/Android/Android Studio/jbr" && ./grad…)
  ⎿  Updated specs\001-motor-reembolso\tasks.md (+1 -1)
       99      `RN-012 identificadores d-001 a d-014 distintos → sem erro
            de lote`
      100    - **Commit:** —
      101  
      102 -- [ ] **T-006** — Validação de item, campos: informação obriga
          -tória ausente,                                                
      102 +- [x] **T-006** — Validação de item, campos: informação obriga
          +tória ausente,                                                
      103    tipo incompatível, data inexistente, texto obrigatório vazio
            após `aparar()`
      104    tratado como ausente, valor normalizado menor ou igual a zer
           o. Todas as
      105    causas, na ordem de `CampoDespesa`.
  ⎿  … 5 more files changed
  ⎿  (timeout 5m)

● Build verde com 12 testes no RN010ValidacaoDeItemTest. Agora confiro que eles
  falham quando devem, com quatro alterações isoladas.

● Bash(export JAVA_HOME="/c/Program Files/Android/Android Studio/jbr" &&
      F=src/reembolso/dominio/ValidacaoDeItem.kt &&
      S=/c/Users/dev/AppData/Local/Temp/claude/c--User…)
  ⎿  5 files changed (binary, mode only or too large to show)
  ⎿  (timeout 10m)

● As quatro alterações quebraram o build e o arquivo foi restaurado. Antes do
  diff, deixo um comentário explicando por que os !! são seguros.

● Update(src\reembolso\dominio\ValidacaoDeItem.kt)
  ⎿  Added 1 line
      25          val ref = if (id != null) Referencia.Id(id) else Refere
          ncia.Posicao(posicao)
      26          return ItemValidado.Invalido(ResultadoDespesa.ComErro(r
          ef, valor, categoria, causas))
      27      }
      28 +    // Sem causas, todo campo foi lido como Presente e não é nu
         +lo.                                                            
      29      return ItemValidado.Valido(Despesa(id!!, data!!, categoria!
          !, fornecedor!!, valor!!, temNotaFiscal!!))
      30  }
      31  

● Bash(export JAVA_HOME="/c/Program Files/Android/Android Studio/jbr" &&
      ./gradlew build -q 2>&1 | tail -2; echo "build: $?"; git add
      specs/001-motor-reembolso/tasks.m…)
  ⎿  5 files changed (binary, mode only or too large to show)
  ⎿  (timeout 5m)

● Bash(git diff --cached -- tests)
  ⎿  (No output)

● A T-006 está pronta: o diff completo está acima e as alterações estão no
  índice, mas não commitei.

  O ./gradlew build passa com 37 testes e nenhuma falha. Destes, 12 são do
  RN010ValidacaoDeItemTest: os 11 da task, com os nomes exatos, e mais 1. O
  checklist está limpo, sem nenhum .trim() nem Double em src/.

  Os testes falham quando devem. Fiz quatro alterações isoladas e todas
  quebraram o build:

  ┌──────────────────────────────────────────────────────┬──────────────────┐
  │                      Alteração                       │   Testes que     │
  │                                                      │     falharam     │
  ├──────────────────────────────────────────────────────┼──────────────────┤
  │ fornecedor validado depois do valor (causas fora da  │ 2                │
  │ ordem da §4.1)                                       │                  │
  ├──────────────────────────────────────────────────────┼──────────────────┤
  │ texto sem aparar() (fornecedor só com espaços passa  │ 2                │
  │ a ser aceito)                                        │                  │
  ├──────────────────────────────────────────────────────┼──────────────────┤
  │ valor 0,00 aceito como positivo (< 0 no lugar de <=  │ 2                │
  │ 0)                                                   │                  │
  ├──────────────────────────────────────────────────────┼──────────────────┤
  │ faixa testada no valor bruto, antes de normalizar    │ 1                │
  │ (0,004 passa)                                        │                  │
  └──────────────────────────────────────────────────────┴──────────────────┘

  Depois restaurei o arquivo e o build voltou a passar.

  Para você avaliar:
  - A fronteira entre a T-006 e a T-007. ResultadoDespesa.ComErro exige uma
    Referencia, então a escolha entre Id com o identificador aparado e Posicao
    já entra aqui, senão o erro de item nem compila.
    - Fica para a T-007 o elemento que não é despesa, por isso validarDespesa
      recebe só DespesaLida.
    - Também ficam para a T-007 os 5 testes de identidade, que vão verificar
      essa referência.
    - A T-007 fica menor do que o tasks.md sugere, mas continua com conteúdo
      próprio.
  - Categoria sem minúsculas por enquanto. A categoria da Despesa e a
    categoriaConsiderada saem só aparadas. A conversão para minúsculas da RN-008
    é da T-010, que diz explicitamente "na construção de Despesa". Até lá,
    "ALIMENTACAO" sai como "ALIMENTACAO".
  - Um teste a mais: RN-010 AMB-021 todas as informações inválidas → uma causa
    por informação na ordem da §4-1. Ele quebra as seis informações de uma vez e
    confere as seis causas na ordem. Os testes da task só cobrem pares de
    causas, e a ordem completa depende da sequência das linhas no código. Esse
    teste verifica também que um identificador só de espaços leva à referência
    pela posição.
  - !! na criação da Despesa. Ele é seguro porque, quando não há nenhuma causa,
    todos os campos foram lidos e não são nulos. Deixei um comentário dizendo
    isso no código.

  Mensagem proposta:
  feat(T-006): validação de item com todas as causas na ordem da §4.1 (RN-010,
  RN-011)

  Posso commitar?

> Pode commitar com a mensagem proposta.

  O teste que invalida as seis informações de uma vez é o mais importante do
  conjunto. Os da task cobrem pares de causas, e a ordem completa da §4.1
  depende da sequência das linhas no código — sem esse teste, uma
  reordenação acidental passaria. Ele também cobre o caso do identificador só
  com espaços levando à referência por posição.

  A fronteira que você desenhou entre T-006 e T-007 está correta: a referência
  precisa existir para o erro de item compilar, então ela nasce aqui. A T-007
  fica com o elemento que não é despesa e os testes de identidade, que é
  conteúdo próprio.

  A categoria sair apenas aparada até a T-010 está certo — a RN-008 atribui a
  conversão para minúsculas à construção da Despesa, que é escopo daquela task.

  Sobre o !!: aceito com o comentário que você deixou. O invariante é real —
  sem causas, todos os campos foram lidos — mas é um invariante que o
  compilador não enxerga, e o comentário é o que permite a quem lê verificar
  o raciocínio.

● Bash(git commit -q -F - <<'EOF'
      feat(T-006): validação de item com todas as causas na ordem da §4.1
      (RN-010, RN-011)…)
  ⎿  (No output)

● Commitei a T-006 na main como e375e73, com a mensagem aprovada. Não ficou nada
  pendente fora do commit, e ainda não fiz push.

  A próxima é a T-007: falta tratar o elemento da relação que não é despesa e
  escrever os testes de identidade do item.

> git push

● Bash(git push 2>&1 && git status -sb | head -1)
  ⎿  (No output)

● Fiz o push: a main local e a origin/main estão iguais, no e375e73. O commit da
  T-006 já está no GitHub.

> T-007: identidade do item. Identificador aparado, Referencia.Posicao quando
  o identificador falta, elemento que não é despesa, ausências que não contam
  como repetição.

  Atende RN-010, RN-012, AMB-020 (i, iii) e AMB-021.

  Aceite: os testes da task.

  Marque a T-007 como [x] no tasks.md. Rode ./gradlew build, verifique que os
  testes falham quando devem, e me mostre o diff antes de commitar.

● Update(src\reembolso\dominio\ValidacaoDeItem.kt)
  ⎿  Added 17 lines
       5      data class Invalido(val erro: ResultadoDespesa.ComErro) : I
          temValidado
       6  }
       7  
       8 +// AMB-020: a posição na relação de despesas conta a partir de 
         +1.                                                             
       9 +fun validarItens(elementos: List<ElementoLido>): List<ItemValid
         +ado> =                                                         
      10 +    elementos.mapIndexed { indice, elemento -> validarElemento(
         +elemento, indice + 1) }                                        
      11 +                                                               
      12 +fun validarElemento(elemento: ElementoLido, posicao: Int): Item
         +Validado = when (elemento) {                                   
      13 +    // AMB-020: causa única — a própria despesa, com tipo incom
         +patível.                                                       
      14 +    ElementoLido.NaoEDespesa -> ItemValidado.Invalido(         
      15 +        ResultadoDespesa.ComErro(                              
      16 +            ref = Referencia.Posicao(posicao),                 
      17 +            valorConsiderado = null,                           
      18 +            categoriaConsiderada = null,                       
      19 +            causas = listOf(CausaErro(CampoDespesa.DESPESA, Fal
         +ha.TIPO_INCOMPATIVEL)),                                        
      20 +        ),                                                     
      21 +    )                                                          
      22 +    is ElementoLido.DespesaLida -> validarDespesa(elemento, pos
         +icao)                                                          
      23 +}                                                              
      24 +                                                               
      25  // RN-010 (erro de item). Causas na ordem de CampoDespesa, que 
          é a da §4.1 (AMB-021).
      26  // `posicao` conta a partir de 1 e só aparece na saída quando f
          alta identificador (AMB-020).
      27  fun validarDespesa(lida: ElementoLido.DespesaLida, posicao: Int
          ): ItemValidado {

● Write(tests\reembolso\regras\RN010IdentidadeDoItemTest.kt)
  ⎿  Wrote 91 lines to tests\reembolso\regras\RN010IdentidadeDoItemTest.kt
       1 package reembolso.regras
       2
       3 import kotlin.test.Test
       4 import kotlin.test.assertEquals
       5 import kotlin.test.assertIs
       6 import kotlin.test.assertNull
       7 import reembolso.dominio.CampoDespesa
       8 import reembolso.dominio.CausaErro
       9 import reembolso.dominio.ElementoLido
      10 import reembolso.dominio.Falha
     … +103 lines (ctrl+o to expand)

● Bash(sed -i 's/- \[ \] \*\*T-007\*\*/- [x] **T-007**/'
      specs/001-motor-reembolso/tasks.md && export JAVA_HOME="/c/Program
      Files/Android/Android Studio/jbr" && ./grad…)
  ⎿  Updated specs\001-motor-reembolso\tasks.md (+1 -1)
      118      `RN-010 erro de item em uma despesa não altera o resultado
            das demais`
      119    - **Commit:** —
      120  
      121 -- [ ] **T-007** — Validação de item, identidade: identificador
          - aparado,                                                     
      121 +- [x] **T-007** — Validação de item, identidade: identificador
          + aparado,                                                     
      122    `Referencia.Posicao` (contada a partir de 1) quando o identi
           ficador falta ou
      123    tem tipo incompatível, elemento que não é despesa, ausências
            que não contam
      124    como repetição.
  ⎿  … 5 more files changed
  ⎿  (timeout 10m)
● Bash(git add specs/001-motor-reembolso/tasks.md src tests && git status
      --short && git diff --cached)
  ⎿  (No output)

● A T-007 está pronta: o diff completo está acima e as alterações estão no
  índice, mas não commitei.

  O ./gradlew build passa com 43 testes e nenhuma falha. Destes, 6 são do
  RN010IdentidadeDoItemTest: os 5 da task, com os nomes exatos, e mais 1.

  Os testes falham quando devem. Fiz quatro alterações isoladas e todas
  quebraram o build:

  ┌─────────────────────────────────────────────────────┬───────────────────┐
  │                      Alteração                      │    Testes que     │
  │                                                     │     falharam      │
  ├─────────────────────────────────────────────────────┼───────────────────┤
  │ posição contada a partir de 0                       │ 5                 │
  ├─────────────────────────────────────────────────────┼───────────────────┤
  │ elemento que não é despesa com causa "identificador │ 1                 │
  │  ausente"                                           │                   │
  ├─────────────────────────────────────────────────────┼───────────────────┤
  │ referência por Id mesmo com identificador vazio e   │ 2                 │
  │ sem aparar                                          │                   │
  ├─────────────────────────────────────────────────────┼───────────────────┤
  │ Despesa.id gravado sem aparar                       │ 1                 │
  └─────────────────────────────────────────────────────┴───────────────────┘

  Depois restaurei o arquivo e o build voltou a passar.

  O que entra no código:
  - validarItens(elementos) é o único lugar onde a posição é contada a partir de
    1, como manda a AMB-020.
  - validarElemento cuida do elemento que não é despesa. Ele vira erro de item
    na sua posição, com causa única: a própria despesa, com tipo incompatível.
    Valor e categoria considerados ficam vazios. Os demais elementos seguem para
    a validarDespesa da T-006.

  Para você avaliar:
  - Um teste a mais: RN-010 AMB-020 identificador com tipo incompatível → erro
    identificado pela posição. A task cobre a referência por posição quando o
    identificador "falta ou tem tipo incompatível", mas os 5 aceites só testam o
    identificador ausente.
  - Dois testes conferem também o lote. O de "duas despesas sem identificador" e
    o de "elemento que não é despesa" verificam que validarLote não rejeita o
    lote, além de checar o erro de cada item. Assim, o "lote não rejeitado" do
    nome é verificado de fato.
  - O teste do identificador com espaços cobre os dois lados: uma despesa válida
    guarda o id aparado, e uma com erro recebe Referencia.Id com o
    identificador aparado.

  Mensagem proposta:
  feat(T-007): identidade do item — posição a partir de 1 e elemento que não é
  despesa (RN-010, RN-012)

  Posso commitar?

> Pode commitar com a mensagem proposta.

  O teste a mais está certo: a task cobre a referência por posição quando o
  identificador "falta ou tem tipo incompatível", mas os cinco aceites só
  exercitavam a ausência. Sem ele, metade da condição ficava sem verificação.

  Os dois testes que conferem também o lote resolvem um problema sutil — um
  teste chamado "lote não rejeitado" que não verifica o lote testaria apenas
  o nome.

● Bash(git commit -q -F - <<'EOF'
      feat(T-007): identidade do item — posição a partir de 1 e elemento que não
      é despesa (RN-010, RN-012)…)
  ⎿  (No output)

● Commitei a T-007 na main como 6868228, com a mensagem aprovada. Não ficou nada
  pendente fora do commit, e ainda não fiz push.

  A próxima é a T-008: o Motor.avaliar, começando pelo cálculo do valor, com
  limite por despesa e reembolso parcial (RN-001 e RN-002).

> T-008: Motor.avaliar com a etapa de valor. Limite por despesa e reembolso
  parcial.

  Atende RN-001, RN-002, AMB-001, AMB-002, AMB-003, AMB-007 e AMB-009.

  Aceite: RN001LimitePorDespesaTest e RN002ReembolsoParcialTest, com todos os
  aceites das duas regras.

  Marque a T-008 como [x] no tasks.md. Rode ./gradlew build, verifique que os
  testes falham quando devem, e me mostre o diff antes de commitar.

● Bash(git status --short; sed -n '/^### RN-001/,/^### RN-003/p'
      specs/001-motor-reembolso/spec.md; grep -n -B1 -A7 '"id":
      "d-0\(01\|02\|10\|11\)"' exemplos/despesas-e…)
  ⎿  (No output)

● Identifiquei três pontos de fronteira na T-008: a regra de categoria
  inelegível fica como TODO para a T-010, o cálculo de notaExigida (RN-003)
  entra agora mas sua recusa fica para a T-011, e o Consolidado já calcula soma
  e contagens, com as contagens validadas só na T-014. Vou implementar com esses
  limites explícitos.

● Update(src\reembolso\dominio\Valor.kt)
  ⎿  Added 8 lines, removed 1 line
       5  
       6  // DT-003: único caminho de criação é normalizar; escala sempre
           2, HALF_UP (afasta do zero).
       7  @JvmInline
       8 -value class Valor private constructor(val decimal: BigDecimal) 
         -{                                                              
       8 +value class Valor private constructor(val decimal: BigDecimal) 
         +: Comparable<Valor> {                                          
       9 +    // Escala 2 nos dois lados: soma e diferença continuam em e
         +scala 2.                                                       
      10 +    operator fun plus(outro: Valor) = Valor(decimal + outro.dec
         +imal)                                                          
      11 +    operator fun minus(outro: Valor) = Valor(decimal - outro.de
         +cimal)                                                         
      12 +    override fun compareTo(other: Valor) = decimal.compareTo(ot
         +her.decimal)                                                   
      13 +                                                               
      14      companion object {
      15 +        val ZERO = de("0.00")                                  
      16 +                                                               
      17          fun normalizar(bruto: BigDecimal): Valor = Valor(bruto.
          setScale(2, RoundingMode.HALF_UP))
      18  
      19          // Para as constantes da política; passa por normalizar
           como todo outro valor.

● Write(src\reembolso\dominio\Motor.kt)
  ⎿  Wrote 62 lines to src\reembolso\dominio\Motor.kt
       1 package reembolso.dominio
       2
       3 // DT-006: ler esta função é ler a §8.
       4 object Motor {
       5     fun avaliar(lote: LoteLido): ResultadoLote {
       7         validarLote(lote)?.let { return it }
       8
       9         // Lote válido: período e relação de despesas estão pres
         entes e o período é composto de datas.
      10         val periodoLido = lote.periodo as PeriodoLido.Presente
     … +68 lines (ctrl+o to expand)

● Agora os testes. Antes, acrescento ao Lotes.kt um atalho para avaliar despesas
  pelo Motor.

● Update(tests\reembolso\regras\Lotes.kt)
  ⎿  Added 13 lines
      30      LoteLido(periodo, DespesasLidas.Presente(despesas.toList())
          )
      31  
      32  fun lote(periodo: PeriodoLido = periodo(), despesas: DespesasLi
          das) = LoteLido(periodo, despesas)
      33 +                                                               
      34 +fun avaliado(lote: LoteLido): ResultadoLote.Avaliado = assertIs
         +<ResultadoLote.Avaliado>(Motor.avaliar(lote))                  
      35 +                                                               
      36 +// Resultados por despesa de um lote de julho/2026 com as despe
         +sas dadas.                                                     
      37 +fun avaliar(vararg despesas: ElementoLido): List<ResultadoDespe
         +sa> = avaliado(lote(periodo(), *despesas)).despesas            
      38 +                                                               
      39 +fun List<ResultadoDespesa>.aceita(id: String): ResultadoDespesa
         +.Aceita =                                                      
      40 +    filterIsInstance<ResultadoDespesa.Aceita>().single { it.id 
         +== id }                                                        
      41 +                                                               
      42 +fun valor(texto: String): Lido<BigDecimal> = Lido.Presente(BigD
         +ecimal(texto))                                                 
      43 +                                                               
      44 +fun despesa(id: String, data: String, categoria: String, fornec
         +edor: String, valor: String, nf: Boolean) =                    
      45 +    despesa(Lido.Presente(id), data(data), Lido.Presente(catego
         +ria), Lido.Presente(fornecedor), valor(valor), Lido.Presente(nf
         +))                                                             

● Update(tests\reembolso\regras\Lotes.kt)
  ⎿  Added 4 lines
       2  
       3  import java.math.BigDecimal
       4  import java.time.LocalDate
       5 +import kotlin.test.assertIs                                    
       6  import reembolso.dominio.DespesasLidas
       7  import reembolso.dominio.ElementoLido
       8  import reembolso.dominio.Lido
       9  import reembolso.dominio.LoteLido
      10 +import reembolso.dominio.Motor                                 
      11  import reembolso.dominio.PeriodoLido
      12 +import reembolso.dominio.ResultadoDespesa                      
      13 +import reembolso.dominio.ResultadoLote                         
      14  
      15  // Construtores de LoteLido para os testes de domínio, sem JSON
           (plan §6).
      16  

  Searched for 1 pattern (ctrl+o to expand)

● Write(tests\reembolso\regras\RN001LimitePorDespesaTest.kt)
  ⎿  Wrote 82 lines to tests\reembolso\regras\RN001LimitePorDespesaTest.kt
       1 package reembolso.regras
       2
       3 import java.time.LocalDate
       4 import kotlin.test.Test
       5 import kotlin.test.assertEquals
       6 import kotlin.test.assertNull
       7 import reembolso.dominio.ResultadoDespesa
       8 import reembolso.dominio.Valor
       9
      10 class RN001LimitePorDespesaTest {
     … +90 lines (ctrl+o to expand)

● Write(tests\reembolso\regras\RN002ReembolsoParcialTest.kt)
  ⎿  Wrote 45 lines to tests\reembolso\regras\RN002ReembolsoParcialTest.kt
      1 package reembolso.regras
      2
      3 import kotlin.test.Test
      4 import kotlin.test.assertEquals
      5 import kotlin.test.assertNull
      6 import reembolso.dominio.ResultadoDespesa
      7 import reembolso.dominio.Valor
      8
      9 class RN002ReembolsoParcialTest {
     … +44 lines (ctrl+o to expand)

● Tiro do RN001LimitePorDespesaTest uma sobra inútil (a julho e o import de
  LocalDate). Aproveito para acrescentar a verificação de notaExigida nos casos
  de d-010 e d-001.

● Update(tests\reembolso\regras\RN001LimitePorDespesaTest.kt)
  ⎿  Added 1 line
      60          assertEquals(Valor.de("250.00"), d.limite)
      61          assertEquals(Valor.de("250.00"), d.reembolsado)
      62          assertEquals(Valor.de("230.00"), d.excedente)
      63 +        assertTrue(d.notaExigida)                              
      64      }
      65  
      66      @Test

● Update(tests\reembolso\regras\RN001LimitePorDespesaTest.kt)
  ⎿  Added 1 line
      20          val d = avaliar(d001, d002).aceita("d-001")
      21          assertEquals(Valor.de("60.00"), d.reembolsado)
      22          assertEquals(Valor.de("12.50"), d.excedente)
      23 +        assertFalse(d.notaExigida)                             
      24      }
      25  
      26      @Test

● Update(tests\reembolso\regras\RN001LimitePorDespesaTest.kt)
  ⎿  Removed 3 lines
      78          assertEquals(Valor.de("200.00"), d.reembolsado)
      79          assertNull(d.excedente)
      80      }
      81 -                                                               
      82 -    @Suppress("unused")                                        
      83 -    private val julho = LocalDate.of(2026, 7, 1)               
      81  }

● Update(tests\reembolso\regras\RN001LimitePorDespesaTest.kt)
  ⎿  Added 2 lines, removed 1 line
       1  package reembolso.regras
       2  
       3 -import java.time.LocalDate                                     
       3  import kotlin.test.Test
       4  import kotlin.test.assertEquals
       5 +import kotlin.test.assertFalse                                 
       6  import kotlin.test.assertNull
       7 +import kotlin.test.assertTrue                                  
       8  import reembolso.dominio.ResultadoDespesa
       9  import reembolso.dominio.Valor
      10  

● Update(tests\reembolso\regras\RN011NormalizacaoMonetariaTest.kt)
  ⎿  Added 19 lines
      33          assertEquals(a.hashCode(), b.hashCode())
      34          assertEquals("54.90", b.decimal.toPlainString())
      35      }
      36 +                                                               
      37 +    // Aceites que passam a ser verificáveis com a etapa de val
         +or (T-008).                                                    
      38 +                                                               
      39 +    @Test                                                      
      40 +    fun `RN-011 d-011 33,333 → reembolso 33,33 integral`() {   
      41 +        val d = avaliar(despesa("d-011", "2026-07-15", "aliment
         +acao", "Hotel Copa Sul", "33.333", nf = true))                 
      42 +            .aceita("d-011")                                   
      43 +        assertEquals("33.33", d.valor.decimal.toPlainString()) 
      44 +        assertEquals("33.33", d.reembolsado.decimal.toPlainStri
         +ng())                                                          
      45 +        assertNull(d.excedente)                                
      46 +    }                                                          
      47 +                                                               
      48 +    @Test                                                      
      49 +    fun `RN-011 alimentacao de 60,004 → reembolso 60,00 integra
         +l`() {                                                         
      50 +        val d = avaliar(despesa("x", "2026-07-15", "alimentacao
         +", "Bistro Central", "60.004", nf = true)).aceita("x")         
      51 +        assertEquals("60.00", d.valor.decimal.toPlainString()) 
      52 +        assertEquals("60.00", d.reembolsado.decimal.toPlainStri
         +ng())                                                          
      53 +        assertNull(d.excedente)                                
      54 +    }                                                          
      55  }

● Update(tests\reembolso\regras\RN011NormalizacaoMonetariaTest.kt)
  ⎿  Added 1 line
      3  import java.math.BigDecimal
      4  import kotlin.test.Test
      5  import kotlin.test.assertEquals
      6 +import kotlin.test.assertNull                                   
      7  import reembolso.dominio.Valor
      8  
      9  class RN011NormalizacaoMonetariaTest {

● Bash(sed -i 's/- \[ \] \*\*T-008\*\*/- [x] **T-008**/'
      specs/001-motor-reembolso/tasks.md && export JAVA_HOME="/c/Program
      Files/Android/Android Studio/jbr" && ./grad…)
  ⎿  Updated specs\001-motor-reembolso\tasks.md (+1 -1)
      131      `RN-012 AMB-020 identificador com espaços nas extremidades
            é considerado aparado`
      132    - **Commit:** —
      133  
      134 -- [ ] **T-008** — `Motor.avaliar` com a etapa 2 (valor) da §8:
          - limite nominal                                               
      134 +- [x] **T-008** — `Motor.avaliar` com a etapa 2 (valor) da §8:
          + limite nominal                                               
      135    por despesa via `limiteAplicavel`, reembolso parcial com exc
           edente.
      136    - **Atende:** RN-001, RN-002, RN-011, AMB-001, AMB-002, AMB-
           003 (dissolvida), AMB-007, AMB-009
      137    - **Aceite:** `RN001LimitePorDespesaTest` e `RN002ReembolsoP
           arcialTest` passam,
  ⎿  … 5 more files changed
  ⎿  (timeout 5m)
● Build verde: 8 + 4 + 2 testes novos, que são os 14 da task. Agora as
  alterações de propósito no Motor:

● Bash(export JAVA_HOME="/c/Program Files/Android/Android Studio/jbr" &&
      F=src/reembolso/dominio/Motor.kt &&
      S=/c/Users/dev/AppData/Local/Temp/claude/c--Users-dev-SDD-…)
  ⎿  5 files changed (binary, mode only or too large to show)
  ⎿  (timeout 10m)

● Bash(git add specs/001-motor-reembolso/tasks.md src tests && git status
      --short && git diff --cached)
  ⎿  (No output)

● A T-008 está pronta: o diff completo está acima e as alterações estão no
  índice, mas não commitei. Antes, preciso que você aprove três pontos de
  fronteira.

  O ./gradlew build passa com 57 testes e nenhuma falha. Os 14 da task estão com
  os nomes exatos:
  - 8 no RN001LimitePorDespesaTest, incluindo os de AMB-007 e AMB-009;
  - 4 no RN002ReembolsoParcialTest;
  - 2 no RN011NormalizacaoMonetariaTest, porque a convenção é um arquivo por RN.

  O checklist está limpo, sem .trim() nem Double em src/.

  Os testes falham quando devem. Fiz cinco alterações isoladas no Motor e todas
  quebraram o build:

  ┌────────────────────────────────────────────────────┬────────────────────┐
  │                     Alteração                      │    Testes que      │
  │                                                    │      falharam      │
  ├────────────────────────────────────────────────────┼────────────────────┤
  │ valor igual ao limite vira parcial (>= no lugar de │ 2                  │
  │  >)                                                │                    │
  ├────────────────────────────────────────────────────┼────────────────────┤
  │ sem teto: reembolsa o valor cheio                  │ 6                  │
  ├────────────────────────────────────────────────────┼────────────────────┤
  │ limite de alimentação aplicado a toda categoria    │ 3                  │
  ├────────────────────────────────────────────────────┼────────────────────┤
  │ excedente com sinal trocado                        │ 5                  │
  ├────────────────────────────────────────────────────┼────────────────────┤
  │ total só da primeira despesa                       │ 1                  │
  └────────────────────────────────────────────────────┴────────────────────┘

  Depois restaurei o arquivo e o build voltou a passar.

  O que entra no código:
  - Motor.avaliar segue a ordem da §8: validação de lote, validação de item e
    etapa de valor. A elegibilidade (etapa 1) aparece como comentário, porque
    vem nas T-009 a T-013.
  - Valor ganhou +, −, comparação e ZERO. Soma e diferença de valores com duas
    casas continuam com duas casas, então nada sai da normalização.

  Fronteiras que precisam da sua aprovação. O Motor exige três coisas que
  pertencem a tasks futuras:
  1. Categoria fora das três: TODO("T-010: …"). A etapa de valor precisa de uma
     Categoria para achar o limite. Em vez de implementar CATEGORIA_INELEGIVEL
     sem teste, deixei um TODO explícito, que lança exceção se for alcançado.
     Até a T-010, um lote com "coworking" ou "ALIMENTACAO" quebra, mas ninguém
     roda o programa antes disso: a CLI só vem na T-019.
  2. notaExigida = valor > 100,00 (RN-003) entra agora, porque é campo
     obrigatório de Aceita. Ele já é verificado no d-010, onde é exigida, e no
     d-001, onde não é. A recusa SEM_NOTA_FISCAL e os demais aceites da RN-003
     continuam na T-011.
  3. O consolidado entra agora, porque ResultadoLote.Avaliado exige um. O total
     é verificado no teste das três alimentações de 59,00, que dá 177,00. As
     contagens por classificação ficam sem teste até a T-014.

  A alternativa seria implementar a RN-008 e as contagens agora, ou criar
  valores provisórios falsos. As duas me parecem piores: a primeira põe regra de
  negócio sem teste no código, a segunda põe resultado errado. Também é o mesmo
  padrão de fronteira da T-006 e da T-007.

  Detalhes de teste:
  - As três alimentações de 59,00 usam fornecedores diferentes. Com o mesmo
    fornecedor, a partir da T-013 elas formariam um grupo de duplicatas e o
    teste mudaria de sentido.
  - O teste "inverter a ordem" compara os conjuntos de resultados, não as
    listas, porque a §4.2 exige que a saída mantenha a ordem da entrada.

  Mensagem proposta:
  feat(T-008): Motor.avaliar com a etapa de valor — limite por despesa e
  reembolso parcial (RN-001, RN-002)

  Posso commitar com essas três fronteiras?

> Execute a T-009 e a T-010 na mesma rodada.

  Elas são independentes entre si: ambas são etapa 1 da §8, ambas apenas
  coletam motivo sem interromper, e nenhuma lê o resultado da outra.
  A T-010 fecha o TODO("T-010: …") que a T-008 deixou no Motor.

  Leia as duas tasks em tasks.md e as RN que elas citam na spec antes de
  começar. A ordem da §8 (1.1 período, 1.2 categoria) tem que aparecer na
  ordem de coleta dos motivos — o OrdemMotivosSecao8Test já cobre a ordem
  do enum, mas quero a coleta coerente com ele.

  Ao terminar, mostre os dois diffs separados e a contagem de testes de
  cada task. Se alguma das duas revelar algo que a spec não decide, pare
  antes de escrever o código e me diga qual é — não decida por mim.

● Antes de começar, um ponto pendente: a T-008 ainda não foi commitada. O diff
  dela está no índice, aguardando a aprovação das três fronteiras que mostrei, e
  você passou direto para a T-009 e a T-010. Não vou commitá-la sem o seu ok.
  Vou trabalhar por cima dela e manter cada task num diff separado, para que os
  três commits possam sair na ordem T-008, T-009 e T-010.

  Agora leio as duas tasks e as RNs que elas citam.

● Bash(sed -n '/^- \[ \] \*\*T-009/,/Commit/p;/^- \[ \] \*\*T-010/,/Commit/p'
      specs/001-motor-reembolso/tasks.md; S=specs/001-motor-reembolso/spec.md;
      sed -n '/^### RN…)
  ⎿  (No output)

● Já li as tasks e as regras RN-005, RN-006 e RN-008. A T-009 parece definida
  pela spec, mas a conversão para minúsculas na T-010 não está clara, então vou
  verificar o comportamento real do JDK usando o jshell, sem alterar nada no
  projeto.

● Bash(cd /c/Users/dev/AppData/Local/Temp/claude/c--Users-dev-SDD-Desafio/8aa9dd
      74-d4f2-42e8-b296-d294cb69c18d/scratchpad && cat > caixa.jsh <<'EOF'
      import java.util.L…)
  ⎿  Error: Exit code 127
     /usr/bin/bash: line 29: /c/Program Files/Android/Android
     Studio/jbr/bin/jshell: No such file or directory

● O JBR do Android Studio não traz o jshell. Uso o modo de arquivo único do java
  (java Arquivo.java), também no scratchpad.

● Bash(cd /c/Users/dev/AppData/Local/Temp/claude/c--Users-dev-SDD-Desafio/8aa9dd
      74-d4f2-42e8-b296-d294cb69c18d/scratchpad && mkdir -p caixa && cat >
      caixa/Caixa.java <…)
  ⎿  (No output)

● Parei antes de escrever qualquer código: a T-010 depende de algo que a spec
  não decide. A T-009 está decidida. A T-008 continua no índice, ainda sem
  commit.

  O que a spec não decide

  A RN-008 e a AMB-013 dizem apenas que "letras maiúsculas são convertidas em
  minúsculas". Ficam duas questões em aberto, e as duas mudam o resultado:

  1. A conversão depende da localidade (idioma do sistema)? Isso afeta
  diretamente o d-014, que é um caso da própria spec. Rodei no JDK desta
  máquina:

  ┌─────────────┬───────────────┬───────────────────────┬──────────────────┐
  │   Entrada   │   Conversão   │       Resultado       │    Elegível?     │
  ├─────────────┼───────────────┼───────────────────────┼──────────────────┤
  │ ALIMENTACAO │ localidade    │ alimentacao           │ sim              │
  │             │ neutra        │                       │                  │
  ├─────────────┼───────────────┼───────────────────────┼──────────────────┤
  │ ALIMENTACAO │ localidade    │ alımentacao (U+0131,  │ não: d-014 seria │
  │             │ turca         │ i sem ponto)          │  recusada        │
  └─────────────┴───────────────┴───────────────────────┴──────────────────┘

  2. A conversão é letra a letra (mapeamento simples) ou pode trocar uma letra
  por duas e depender da posição na palavra (mapeamento completo)?

  ┌──────────────────────┬──────────────────┬───────────────────────────────┐
  │       Entrada        │   Mapeamento     │      Mapeamento completo      │
  │                      │     simples      │                               │
  ├──────────────────────┼──────────────────┼───────────────────────────────┤
  │ ALİMENTACAO (U+0130, │ alimentacao:     │ ali̇mentacao (i + U+0307):     │
  │  İ turco)            │ elegível         │ inelegível                    │
  ├──────────────────────┼──────────────────┼───────────────────────────────┤
  │ ΟΔΟΣ (sigma no fim)  │ οδοσ             │ οδος (ς final): outro         │
  │                      │                  │ fornecedor na duplicidade     │
  └──────────────────────┴──────────────────┴───────────────────────────────┘

  É a mesma classe de problema do D4. O plan (DT-005) já escolheu "lowercase()
  do Kotlin, independente de localidade", que é o mapeamento completo. Mas essa
  é uma regra de negócio decidida fora da spec. A §4.1 define "espaço" com
  precisão pelo padrão Unicode, e a conversão de caixa ficou sem a mesma
  precisão.

  Opções

  - (a) Independente de localidade, mapeamento simples (letra a letra). Esta é a
    minha recomendação.
    - É a mais verificável: uma letra vira sempre uma letra, sem depender da
      posição na palavra, no mesmo espírito do "alcance mantido estreito" da
      AMB-013.
    - ALİMENTACAO passa a ser elegível.
    - Exige ajustar a DT-005, que hoje diz lowercase() do Kotlin.
  - (b) Independente de localidade, mapeamento completo do Unicode. É o que o
    plan já faz. ALİMENTACAO fica inelegível e o sigma final vira ς.
  - (c) Só converter A–Z para a–z. Eu descartaria: "BISTRÔ CENTRAL" e "Bistrô
    Central" passariam a ser fornecedores distintos, porque Ô não seria
    convertido.

  Nos três casos, a independência de localidade precisa entrar na spec, porque é
  ela que protege o d-014. Também vale um caso de aceite, como o U+FF71 da
  RN-009, para que o teste verifique a regra e não a escolha técnica.

  Encaminhamento proposto

  1. Você escolhe (a), (b) ou outra.
  2. Eu faço o docs(spec): com a D-011, nos moldes da D-010: RN-008, AMB-013, um
     caso de aceite, mais a DT-005 se a escolha for (a).
  3. Depois commito a T-008, se você aprovar as três fronteiras que mostrei, e
     faço a T-009 e a T-010 como você pediu, com diffs e contagens separados.

  Se preferir, posso adiantar a T-009 enquanto você decide, porque ela não toca
  na conversão de caixa.

> Em RN-008, na parte que trata da conversão de caixa, substitua o texto
  atual por:

  A conversão de maiúsculas em minúsculas usa o mapeamento completo de
  caixa definido pelo padrão Unicode, independente da localidade do
  ambiente de execução. Independente de localidade significa que a mesma
  entrada produz a mesma saída em qualquer máquina, qualquer idioma de
  sistema e qualquer configuração regional: o mapeamento é o do padrão
  Unicode, não o do idioma do usuário.

  Mapeamento completo significa que um caractere pode ser convertido em
  mais de um, e que a conversão pode depender da posição do caractere na
  palavra. Isso é intencional: é o que faz a conversão respeitar a grafia
  da língua. O sigma grego maiúsculo (U+03A3) convertido no fim de uma
  palavra produz o sigma final (U+03C2), que é a forma correta em grego;
  convertido em qualquer outra posição produz o sigma comum (U+03C3).

  Consequências verificáveis:
  - ALIMENTACAO produz alimentacao em qualquer localidade, inclusive
    turca e azerbaijana, onde a conversão dependente de localidade
    produziria alımentacao (U+0131, i sem ponto) e tornaria a despesa
    inelegível. O caso d-014 da §9 depende desta garantia.
  - ΟΔΟΣ e Οδος produzem a mesma chave οδος, logo são o mesmo fornecedor
    para a RN-009. Com mapeamento simples produziriam οδοσ e οδος, chaves
    distintas, e um grupo de duplicatas deixaria de ser detectado.
  - ALİMENTACAO (U+0130, I turco com ponto) produz ali̇mentacao, com o
    ponto como caractere combinante separado, que não é nenhuma das três
    categorias elegíveis. A despesa é recusada por CATEGORIA_INELEGIVEL.
    Isso é intencional: U+0130 é um caractere distinto de U+0049, e
    tratá-los como iguais seria o motor adivinhando a intenção de quem
    digitou, contra o alcance estreito decidido na AMB-013.

  Caso de aceite: uma despesa de categoria "ΟΔΟΣ" e outra de categoria
  "Οδος", ambas no mesmo dia, mesmo valor e mesmo fornecedor, formam um
  grupo de duplicatas pela RN-009.

● Antes de aplicar o texto, conferi cada afirmação contra o comportamento do JDK
  e contra a própria spec. Encontrei uma contradição no caso de aceite, e não
  vou aplicá-lo como está:

  ▎ "uma despesa de categoria "ΟΔΟΣ" e outra de categoria "Οδος" […] formam um
  ▎ grupo de duplicatas pela RN-009"

  "οδος" não é uma das três categorias elegíveis. As duas despesas recebem
  CATEGORIA_INELEGIVEL, e a RN-009 diz que despesa "de categoria inelegível […]
  nunca é mantida nem recusada como duplicata". Pela spec atual, esse caso dá
  duas recusas CATEGORIA_INELEGIVEL e nenhuma DUPLICATA, ou seja, o contrário do
  aceite. A sua segunda consequência fala de fornecedor ("mesmo fornecedor para
  a RN-009"), e acredito que era essa a intenção. Vou escrever o aceite com o
  fornecedor "ΟΔΟΣ" contra "Οδος", em categoria elegível. Mostro antes de
  commitar.

  O resto confere: ΟΔΟΣ vira οδος com ς final, ALİMENTACAO vira ali̇mentacao com
  U+0307, e na localidade turca o resultado é alımentacao. Aplico agora.

● Bash(cd /c/Users/dev/SDD-Desafio/sdd-desafio && git status --short && grep -n
      'letras maiúsculas são convertidas\|convertendo-se\|^- Fornecedores
      "Bistro Central"\|^…)
  ⎿  (No output)

● Bash(sed -n '348,375p;1096,1102p' specs/001-motor-reembolso/spec.md)
  ⎿  (No output)

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 18 lines, removed 2 lines
      350  **Regra:** antes de qualquer comparação, a categoria e o forne
           cedor de cada
      351  despesa são normalizados: letras maiúsculas são convertidas em
            minúsculas e
      352  espaços nas extremidades, no alcance declarado na §4.1, são re
           movidos.
      353 -Nenhuma outra transformação é aplicada — acentuação, separador
          -es e sinônimos não são normalizados. Toda                     
      354 -regra que compara categorias ou fornecedores usa os valores no
          -rmalizados.                                                   
      353 +Nenhuma outra transformação é aplicada — acentuação, separador
          +es e                                                          
      354 +sinônimos não são normalizados. Toda regra que compara categor
          +ias ou                                                        
      355 +fornecedores usa os valores normalizados.                     
      356 +                                                              
      357 +A conversão de maiúsculas em minúsculas usa o mapeamento compl
          +eto de caixa                                                  
      358 +definido pelo padrão Unicode, independente da localidade do am
          +biente de                                                     
      359 +execução. Independente de localidade significa que a mesma ent
          +rada produz a                                                 
      360 +mesma saída em qualquer máquina, qualquer idioma de sistema e 
          +qualquer                                                      
      361 +configuração regional: o mapeamento é o do padrão Unicode, não
          + o do idioma                                                  
      362 +do usuário.                                                   
      363 +                                                              
      364 +Mapeamento completo significa que um caractere pode ser conver
          +tido em mais                                                  
      365 +de um, e que a conversão pode depender da posição do caractere
          + na palavra.                                                  
      366 +Isso é intencional: é o que faz a conversão respeitar a grafia
          + da língua. O                                                 
      367 +sigma grego maiúsculo (U+03A3) convertido no fim de uma palavr
          +a produz o                                                    
      368 +sigma final (U+03C2), que é a forma correta em grego; converti
          +do em qualquer                                                
      369 +outra posição produz o sigma comum (U+03C3).                  
      370 +                                                              
      371  As categorias elegíveis são
      372  exatamente três: alimentacao, transporte_urbano e hospedagem. 
           Uma despesa
      373  cuja categoria normalizada seja qualquer outra é recusada, com
            o motivo

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 15 lines
      387    motivos [CATEGORIA_INELEGIVEL].
      388  - Fornecedores "Bistro Central", "BISTRO CENTRAL" e " Bistro C
           entral " → o
      389    mesmo fornecedor; "Bistrô Central" → fornecedor distinto.
      390 +- "ALIMENTACAO" → alimentacao em qualquer localidade, inclusiv
          +e turca e                                                     
      391 +  azerbaijana, onde a conversão dependente de localidade produ
          +ziria                                                         
      392 +  alımentacao (U+0131, i sem ponto) e tornaria a despesa inele
          +gível. O caso                                                 
      393 +  d-014 da §9 depende desta garantia.                         
      394 +- Fornecedores "ΟΔΟΣ" e "Οδος" → a mesma chave, οδος; são o me
          +smo fornecedor                                                
      395 +  para a RN-009. Com mapeamento simples produziriam οδοσ e οδο
          +ς, chaves                                                     
      396 +  distintas, e um grupo de duplicatas deixaria de ser detectad
          +o.                                                            
      397 +- "ALİMENTACAO" (U+0130, I turco com ponto) → ali̇mentacao, com
          + o ponto como                                                 
      398 +  caractere combinante separado (U+0307), que não é nenhuma da
          +s três                                                        
      399 +  categorias elegíveis; recusada, motivos [CATEGORIA_INELEGIVE
          +L]. Isso é                                                    
      400 +  intencional: U+0130 é um caractere distinto de U+0049, e tra
          +tá-los como                                                   
      401 +  iguais seria o motor adivinhando a intenção de quem digitou,
          + contra o                                                     
      402 +  alcance estreito decidido na AMB-013.                       
      403 +- Duas despesas de alimentação no mesmo dia e de mesmo valor, 
          +com                                                           
      404 +  fornecedores "ΟΔΟΣ" e "Οδος" → grupo de duplicatas pela RN-0
          +09.                                                           
      405  
      406  ### RN-009 — Duplicidade
      407  

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 3 lines, removed 1 line
      1127  as três categorias nomeadas esgotam as elegíveis, ou se são a
            penas aquelas
      1128  que possuem limite, podendo haver outras elegíveis sem teto.
      1129  **Decisão:** a categoria é normalizada antes da comparação, c
            onvertendo-se
      1130 -maiúsculas em minúsculas e removendo-se espaços nas extremida
           -des. Nenhuma                                                 
      1130 +maiúsculas em minúsculas — pelo mapeamento completo de caixa 
           +do padrão                                                    
      1131 +Unicode, independente de localidade (RN-008) — e removendo-se
           + espaços nas                                                 
      1132 +extremidades. Nenhuma                                        
      1133  outra transformação é aplicada: acentuação, separadores e sin
            ônimos não são
      1134  normalizados. As categorias elegíveis são exatamente três — a
            limentacao,
      1135  transporte_urbano e hospedagem, na grafia do vocabulário de c
            ategoria

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 1 line, removed 1 line
      1  # Spec — Motor de Cálculo de Reembolso
      2  
      3 -**Versão:** 1.2 · **Status:** completa · **Última alteração:** 2
        -026-10-18                                                       
      3 +**Versão:** 1.3 · **Status:** completa · **Última alteração:** 2
        +026-10-19                                                       
      4  
      5  ---
      6  

● Agora a D-011, e em seguida confiro os bytes dos caracteres gregos e turcos
  que entraram na spec.

● Update(specs\001-motor-reembolso\DECISIONS.md)
  ⎿  Added 64 lines
      10  
      11  ---
      12  
      13 +## D-011 — Conversão de caixa: mapeamento completo do Unicode, 
         +independente de localidade · `2026-10-19`                      
      14 +                                                               
      15 +**Gatilho:** a leitura da RN-008 antes da T-010. A spec dizia a
         +penas que                                                      
      16 +"letras maiúsculas são convertidas em minúsculas", sem dizer se
         + a conversão                                                   
      17 +depende da localidade do ambiente nem se é feita caractere a ca
         +ractere                                                        
      18 +(mapeamento simples) ou com o mapeamento completo do padrão Uni
         +code, que                                                      
      19 +pode produzir mais de um caractere e depender da posição. As du
         +as questões                                                    
      20 +mudam resultado, e a primeira alcança um caso da própria spec: 
         +na                                                             
      21 +localidade turca, "ALIMENTACAO" vira "alımentacao" (U+0131) e d
         +-014 seria                                                     
      22 +recusada. Verificado no JDK do projeto antes de escrever código
         +. O plan já                                                    
      23 +tinha escolhido (DT-005: `lowercase()` do Kotlin, independente 
         +de                                                             
      24 +localidade), o que é regra de negócio decidida fora da spec — m
         +esma classe                                                    
      25 +do achado D4 da D-010.                                         
      26 +                                                               
      27 +**O que mudou na spec (versão 1.2 → 1.3):**                    
      28 +- RN-008, Regra: dois parágrafos novos — a conversão usa o mape
         +amento                                                         
      29 +  completo de caixa do padrão Unicode, independente da localida
         +de; o que                                                      
      30 +  "independente de localidade" e "mapeamento completo" signific
         +am, com o                                                      
      31 +  sigma final (U+03A3 → U+03C2 no fim da palavra, U+03C3 nas de
         +mais                                                           
      32 +  posições).                                                   
      33 +- RN-008, Aceite: quatro casos acrescentados — "ALIMENTACAO" em
         + qualquer                                                      
      34 +  localidade; fornecedores "ΟΔΟΣ" e "Οδος" com a mesma chave; "
         +ALİMENTACAO"                                                   
      35 +  (U+0130) inelegível; duas alimentações com fornecedores "ΟΔΟΣ
         +" e "Οδος"                                                     
      36 +  formam grupo de duplicatas.                                  
      37 +- AMB-013, Decisão: a conversão remete ao mapeamento completo e
         + à                                                             
      38 +  independência de localidade da RN-008.                       
      39 +                                                               
      40 +**Classificação:** esclarecimento. A AMB-013 já decidia convert
         +er a caixa e                                                   
      41 +manter o alcance estreito; faltava declarar o alcance do termo 
         +"converter".                                                   
      42 +                                                               
      43 +**Alternativas consideradas:** (a) mapeamento simples, caracter
         +e a                                                            
      44 +caractere, independente de localidade; (c) converter apenas A–Z
         +. A (c) foi                                                    
      45 +descartada porque "BISTRÔ CENTRAL" e "Bistrô Central" seriam fo
         +rnecedores                                                     
      46 +distintos. Entre (a) e o mapeamento completo, adotado o complet
         +o: respeita a                                                  
      47 +grafia da língua (sigma final), de modo que "ΟΔΟΣ" e "Οδος" são
         + o mesmo                                                       
      48 +fornecedor — com o simples seriam chaves distintas e uma duplic
         +ata deixaria                                                   
      49 +de ser detectada. Custo aceito: "ALİMENTACAO" (U+0130) vira "al
         +i̇mentacao" e                                                   
      50 +é inelegível; U+0130 é caractere distinto de U+0049, e igualá-l
         +os seria                                                       
      51 +inferir a intenção de quem digitou, contra o alcance estreito d
         +a AMB-013.                                                     
      52 +                                                               
      53 +**Correção no caso de aceite proposto:** o caso foi proposto co
         +m duas                                                         
      54 +despesas de **categoria** "ΟΔΟΣ" e "Οδος" formando grupo de dup
         +licatas. Pela                                                  
      55 +RN-009, despesa de categoria inelegível não integra grupo — as 
         +duas seriam                                                    
      56 +recusadas por CATEGORIA_INELEGIVEL, sem DUPLICATA. O caso foi r
         +egistrado com                                                  
      57 +"ΟΔΟΣ" e "Οδος" como **fornecedor**, em categoria elegível, coe
         +rente com a                                                    
      58 +consequência que trata o grego como chave de fornecedor.       
      59 +                                                               
      60 +**Por quê:** decidir a conversão no código seria introduzir reg
         +ra de negócio                                                  
      61 +fora da spec, e a dependência de localidade faria o mesmo lote 
         +produzir                                                       
      62 +resultados diferentes conforme a máquina.                      
      63 +                                                               
      64 +**O que isso invalidou:** nenhum critério de aceite. O lote de 
         +exemplo é o                                                    
      65 +mesmo em qualquer localidade.                                  
      66 +                                                               
      67 +**Tasks afetadas:** T-010 (os quatro casos novos entram no     
      68 +`RN008CategoriaTest`) e T-013 (o grupo de duplicatas com fornec
         +edores                                                         
      69 +gregos é verificável na RN-009). A DT-005 do plan já implementa
         + a escolha                                                     
      70 +(`lowercase()` do Kotlin = mapeamento completo, `Locale.ROOT`);
         + passa a                                                       
      71 +implementar a RN-008 em vez de decidi-la.                      
      72 +                                                               
      73 +**Custo:** 2 arquivos (spec.md, DECISIONS.md), correção pontual
         +.                                                              
      74 +                                                               
      75 +---                                                            
      76 +                                                               
      77  ## D-010 — Achados de revisão independente da spec antes das re
          gras de negócio · `2026-10-18`
      78  
      79  **Gatilho:** revisão independente da spec 1.1 por um subagente 
          com contexto

● Bash(S=specs/001-motor-reembolso/spec.md && for w in 'ΟΔΟΣ' 'Οδος' 'οδος'
      'alımentacao' 'ALİMENTACAO' 'mentacao, com o ponto'; do :; done; grep -o
      'ali.\{1,2\}mentac…)
  ⎿  (No output)

● Os bytes estão corretos: ali + U+0307 (cc 87), Οδος com ς final (cf 82) e İ =
  U+0130 (c4 b0). Agora confiro que os testes que leem a spec continuam
  passando, e mostro o diff.

● Bash(export JAVA_HOME="/c/Program Files/Android/Android Studio/jbr" &&
      ./gradlew build -q 2>&1 | tail -3; echo "build: $?"; git diff --
      specs/001-motor-reembolso/spe…)
  ⎿  2 files changed (binary, mode only or too large to show)

● O texto que você ditou está na RN-008, com dois ajustes, e a D-011 está
  pronta. Não commitei nada. As mudanças da spec estão fora do índice, separadas
  da T-008, que continua no índice. O ./gradlew build passa, inclusive os
  testes que leem a spec.

  Os dois ajustes:
  1. O caso de aceite usa fornecedor, não categoria. Como está no seu texto, ele
     contradiz a RN-009: "οδος" é categoria inelegível, e despesa com categoria
     inelegível não integra grupo de duplicatas. O resultado seriam duas
     recusas CATEGORIA_INELEGIVEL e nenhuma DUPLICATA. Ficou assim: "duas
     despesas de alimentação no mesmo dia e de mesmo valor, com fornecedores
     "ΟΔΟΣ" e "Οδος" → grupo de duplicatas". A D-011 registra a correção.
  2. As consequências entraram na lista "Aceite" da RN-008, e não num bloco
     "Consequências verificáveis". Esse cabeçalho é o formato das AMB; as RN
     usam "Aceite". O conteúdo é o seu, palavra por palavra. A AMB-013 também
     ganhou uma remissão ao mapeamento completo e à independência de localidade
     da RN-008.

  Um ponto de precisão, pequeno, que deixei como está. "Sigma convertido no fim
  de uma palavra produz ς" é uma aproximação. Pela regra do Unicode, o sigma só
  vira ς quando vem depois de uma letra e não é seguido de outra. Um "Σ" sozinho
  vira σ, embora esteja "no fim de uma palavra". Nenhum caso da spec é afetado.
  Se quiser fechar isso, eu troco por "precedido de letra e não seguido de
  letra".

  A D-011 registra:
  - o gatilho: a leitura antes da T-010, conferida no JDK;
  - as alternativas (a) e (c), e por que foram descartadas;
  - o custo aceito do U+0130;
  - a correção do caso de aceite;
  - que a DT-005 do plan já faz exatamente isso, com lowercase() e Locale.ROOT,
    e agora implementa a RN-008 em vez de decidi-la.

  Commits que proponho, nesta ordem:
  1. docs(spec): conversão de caixa pelo mapeamento completo do Unicode,
     independente de localidade; D-011. Só a spec e o DECISIONS, sem tocar no
     que está no índice.
  2. docs: plan e tasks referenciam a spec 1.3, como fizemos na 1.2.
  3. A T-008, se você aprovar as três fronteiras que mostrei antes: o TODO da
     T-010, o notaExigida e o consolidado.
  4. A T-009 e a T-010, com os diffs separados e a contagem de testes de cada
     uma. A T-010 fecha o TODO e inclui os quatro aceites novos da RN-008.

  Posso seguir?
> Troque "convertido no fim de uma palavra produz o sigma final (U+03C2)"
  por "convertido quando precedido de letra e não seguido de letra produz
  o sigma final (U+03C2)". Registre a troca na D-011 como ajuste de
  precisão da redação, não como mudança de decisão.

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 3 lines, removed 3 lines
      364  Mapeamento completo significa que um caractere pode ser conver
           tido em mais
      365  de um, e que a conversão pode depender da posição do caractere
            na palavra.
      366  Isso é intencional: é o que faz a conversão respeitar a grafia
            da língua. O
      367 -sigma grego maiúsculo (U+03A3) convertido no fim de uma palavr
          -a produz o                                                    
      368 -sigma final (U+03C2), que é a forma correta em grego; converti
          -do em qualquer                                                
      369 -outra posição produz o sigma comum (U+03C3).                  
      367 +sigma grego maiúsculo (U+03A3) convertido quando precedido de 
          +letra e não                                                   
      368 +seguido de letra produz o sigma final (U+03C2), que é a forma 
          +correta em                                                    
      369 +grego; convertido em qualquer outra posição produz o sigma com
          +um (U+03C3).                                                  
      370  
      371  As categorias elegíveis são
      372  exatamente três: alimentacao, transporte_urbano e hospedagem. 
           Uma despesa

● Update(specs\001-motor-reembolso\DECISIONS.md)
  ⎿  Added 2 lines, removed 2 lines
      28  - RN-008, Regra: dois parágrafos novos — a conversão usa o mape
          amento
      29    completo de caixa do padrão Unicode, independente da localida
          de; o que
      30    "independente de localidade" e "mapeamento completo" signific
          am, com o
      31 -  sigma final (U+03A3 → U+03C2 no fim da palavra, U+03C3 nas de
         -mais                                                           
      32 -  posições).                                                   
      31 +  sigma final (U+03A3 → U+03C2 quando precedido de letra e não 
         +seguido de                                                     
      32 +  letra, U+03C3 nas demais posições).                          
      33  - RN-008, Aceite: quatro casos acrescentados — "ALIMENTACAO" em
           qualquer
      34    localidade; fornecedores "ΟΔΟΣ" e "Οδος" com a mesma chave; "
          ALİMENTACAO"
      35    (U+0130) inelegível; duas alimentações com fornecedores "ΟΔΟΣ
          " e "Οδος"

● Update(specs\001-motor-reembolso\DECISIONS.md)
  ⎿  Added 9 lines
      57  "ΟΔΟΣ" e "Οδος" como **fornecedor**, em categoria elegível, coe
          rente com a
      58  consequência que trata o grego como chave de fornecedor.
      59  
      60 +**Ajuste de precisão da redação:** o texto proposto dizia que o
         + sigma                                                         
      61 +"convertido no fim de uma palavra" produz o sigma final. A cond
         +ição do                                                        
      62 +padrão Unicode é outra: o sigma final aparece quando o sigma é 
         +precedido de                                                   
      63 +letra e não seguido de letra — um "Σ" isolado está no fim de um
         +a palavra e                                                    
      64 +produz o sigma comum. A redação foi trocada para "convertido qu
         +ando                                                           
      65 +precedido de letra e não seguido de letra". Não é mudança de de
         +cisão: a                                                       
      66 +regra continua sendo o mapeamento completo do padrão Unicode, e
         + a frase                                                       
      67 +apenas descreve com exatidão o que esse mapeamento faz.        
      68 +                                                               
      69  **Por quê:** decidir a conversão no código seria introduzir reg
          ra de negócio
      70  fora da spec, e a dependência de localidade faria o mesmo lote 
          produzir
      71  resultados diferentes conforme a máquina.

● Bash(grep -n 'no fim de uma palavra' specs/001-motor-reembolso/spec.md
      specs/001-motor-reembolso/DECISIONS.md; git diff -U1 --
      specs/001-motor-reembolso/spec.md | se…)
  ⎿  (No output)

● Antes de considerar pronto, confiro no JDK a afirmação que acabei de registrar
  na D-011: um "Σ" isolado produz σ.

● Bash(cd /c/Users/dev/AppData/Local/Temp/claude/c--Users-dev-SDD-Desafio/8aa9dd
      74-d4f2-42e8-b296-d294cb69c18d/scratchpad && cat > caixa/Sigma.java
      <<'EOF'
      import java…)
  ⎿  (No output)

● Fiz a troca, e o JDK confirma a nova redação. Ainda não commitei nada.

  Na RN-008, a frase agora é: "O sigma grego maiúsculo (U+03A3) convertido
  quando precedido de letra e não seguido de letra produz o sigma final
  (U+03C2), que é a forma correta em grego; convertido em qualquer outra posição
  produz o sigma comum (U+03C3)."

  Na D-011 a troca entrou de duas formas:
  - uma seção nova, "Ajuste de precisão da redação". Ela registra o texto
    anterior e por que estava impreciso: um "Σ" isolado está no fim de uma
    palavra e produz σ. Diz explicitamente que não é mudança de decisão, porque
    a regra continua sendo o mapeamento completo do Unicode;
  - o resumo do sigma em "O que mudou na spec" agora usa a mesma condição.

  ┌──────────┬────────────────────┬────────────────────────────────────────┐
  │ Entrada  │    Saída no JDK    │                Por quê                 │
  ├──────────┼────────────────────┼────────────────────────────────────────┤
  │ Σ        │ σ (U+03C3)         │ não vem depois de letra                │
  │ sozinho  │                    │                                        │
  ├──────────┼────────────────────┼────────────────────────────────────────┤
  │ ΟΔΟΣ     │ οδος (U+03C2 no    │ vem depois de letra e não é seguido de │
  │          │ fim)               │  letra                                 │
  ├──────────┼────────────────────┼────────────────────────────────────────┤
  │ ΣΟ       │ σο                 │ no início da palavra                   │
  ├──────────┼────────────────────┼────────────────────────────────────────┤
  │ ΟΣ Ο     │ ος ο               │ vem depois de letra e é seguido de     │
  │          │                    │ espaço                                 │
  └──────────┴────────────────────┴────────────────────────────────────────┘

  O estado continua o mesmo de antes:
  - a spec e o DECISIONS estão alterados, mas fora do índice;
  - a T-008 está no índice, separada.

  Posso seguir com os commits na ordem que propus?
  1. o docs(spec): com a D-011;
  2. o docs: que atualiza para spec 1.3 a referência no plan e no tasks;
  3. a T-008, se você aprovar as três fronteiras;
  4. a T-009 e a T-010.

> Aprovado. Siga os quatro na ordem que propôs, sem parar entre eles para
  confirmar:

  1. docs(spec): conversão de caixa pelo mapeamento completo do Unicode,
     independente de localidade; D-011
  2. docs: plan e tasks referenciam a spec 1.3
  3. A T-008, com as três fronteiras aprovadas (TODO da T-010, notaExigida
     e consolidado). Antes de commitar, acrescente o comentário em cima do
     bloco do consolidado:

     // As contagens por classificação refletem apenas as classificações
     // que o motor produz hoje. Completas na T-014, quando a etapa 1 da
     // §8 existir e houver recusa possível.
  4. A T-009 e a T-010 na mesma rodada. São independentes entre si: ambas
     são etapa 1 da §8, ambas apenas coletam motivo sem interromper, e
     nenhuma lê o resultado da outra. A T-010 fecha o TODO e inclui os
     aceites novos da RN-008.

  Em cada commit, marque a task como [x] no mesmo commit, e rode git status
  e leia antes de commitar.

  Na T-009 e na T-010, me mostre os dois diffs separados e a contagem de
  testes de cada uma. Se alguma das duas revelar algo que a spec não
  decide, pare antes de escrever o código e me diga qual é — mas verifique
  primeiro se o plan.md já decidiu aquilo. Se decidiu e a decisão é
  coerente com a spec, não é ambiguidade nova: é regra que precisa ser
  escrita na spec, e você me diz isso em vez de abrir decisão.

● Sigo os quatro na ordem, lendo o git status antes de cada commit.

  1. docs(spec)

● Bash(cd /c/Users/dev/SDD-Desafio/sdd-desafio && git status --short && git diff
      --stat -- specs/001-motor-reembolso/spec.md
      specs/001-motor-reembolso/DECISIONS.md &&…)
  ⎿  (No output)

● Vou separar o [x] da T-008 do commit, já que ainda não deve entrar, e incluir
  só o cabeçalho e os aceites novos; depois restauro a marcação. Também vou
  adicionar os nomes dos testes da RN-008 nas listas da T-010 e T-013 e uma
  linha na DT-005 do plan.

  Searched for 1 pattern (ctrl+o to expand)

● Bash(sed -n '1,4p;323,329p' specs/001-motor-reembolso/plan.md)
  ⎿  (No output)

● Bash(T=specs/001-motor-reembolso/tasks.md P=specs/001-motor-reembolso/plan.md
      && git restore --staged $T && sed -i 's/^- \[x\] \*\*T-008\*\*/- [ ]
      **T-008**/' $T &&…)
  ⎿  Updated specs\001-motor-reembolso\plan.md (+4 -2)
  ⎿  Updated specs\001-motor-reembolso\tasks.md (+8 -4)
  ⎿  (a git step in this command can move the working tree, so these may be
     its changes, not edits) (ctrl+o to expand)

● Bash(git commit -q -F - -- specs/001-motor-reembolso/plan.md
      specs/001-motor-reembolso/tasks.md <<'EOF'
      docs: plan e tasks referenciam a spec 1.3…)
  ⎿  Updated specs\001-motor-reembolso\tasks.md (+1 -1)
      131      `RN-012 AMB-020 identificador com espaços nas extremidades
            é considerado aparado`
      132    - **Commit:** —
      133  
      134 -- [ ] **T-008** — `Motor.avaliar` com a etapa 2 (valor) da §8:
          - limite nominal                                               
      134 +- [x] **T-008** — `Motor.avaliar` com a etapa 2 (valor) da §8:
          + limite nominal                                               
      135    por despesa via `limiteAplicavel`, reembolso parcial com exc
           edente.
      136    - **Atende:** RN-001, RN-002, RN-011, AMB-001, AMB-002, AMB-
           003 (dissolvida), AMB-007, AMB-009
      137    - **Aceite:** `RN001LimitePorDespesaTest` e `RN002ReembolsoP
           arcialTest` passam,

● 3. T-008. Acrescento o comentário que você ditou sobre o bloco do consolidado,
  rodo o build e leio o status.

● Update(src\reembolso\dominio\Motor.kt)
  ⎿  Added 3 lines
      49      }
      50  
      51      // §4.2: consolidado do lote.
      52 +    // As contagens por classificação refletem apenas as classi
         +ficações                                                       
      53 +    // que o motor produz hoje. Completas na T-014, quando a et
         +apa 1 da                                                       
      54 +    // §8 existir e houver recusa possível.                    
      55      private fun consolidar(despesas: List<ResultadoDespesa>): C
          onsolidado {
      56          val aceitas = despesas.filterIsInstance<ResultadoDespes
          a.Aceita>()
      57          return Consolidado(

● Bash(export JAVA_HOME="/c/Program Files/Android/Android Studio/jbr" &&
      ./gradlew build -q 2>&1 | tail -3; echo "build: $?"; git add
      specs/001-motor-reembolso/tasks.m…)
  ⎿  5 files changed (binary, mode only or too large to show)
  ⎿  (timeout 5m)

● Bash(git commit -q -F - <<'EOF'
      feat(T-008): Motor.avaliar com a etapa de valor — limite por despesa e
      reembolso parcial (RN-001, RN-002)…)
  ⎿  (No output)

● Commitei a T-008 como 5af7f0b. Agora a T-009 e a T-010. Antes de escrever
  código, procuro o que a spec não decide e confiro se o plan já decidiu.

● Bash(sed -n '/^### RN-003/,/^\*\*Origem/p' specs/001-motor-reembolso/spec.md;
      python3 -I -c "
      import json;d=json.load(open('exemplos/despesas-exemplo.json',encoding=…)
  ⎿  (No output)

● Resolvi todas as pendências das T-009 e T-010 com base nas RNs e decisões já
  fechadas (RN-005, RN-006, RN-003, D-011), e confirmei que o novo aceite de
  duplicata da RN-008 pertence à T-013, já listado no commit 2. Seguindo para a
  T-009.

● Update(src\reembolso\dominio\Resultado.kt)
  ⎿  Added 4 lines, removed 1 line
      32      ) : ResultadoDespesa
      33  }
      34  
      35 -data class Periodo(val inicio: LocalDate, val fim: LocalDate)  
      35 +data class Periodo(val inicio: LocalDate, val fim: LocalDate) {
      36 +    // RN-005: as duas bordas pertencem ao período.            
      37 +    operator fun contains(data: LocalDate): Boolean = data in i
         +nicio..fim                                                     
      38 +}                                                              
      39  
      40  data class Consolidado(
      41      val totalReembolsado: Valor,

● Update(src\reembolso\dominio\Motor.kt)
  ⎿  Added 22 lines, removed 3 lines
      18          val despesas = validarItens(elementos).map { item ->
      19              when (item) {
      20                  is ItemValidado.Invalido -> item.erro
      21 -                is ItemValidado.Valido -> avaliarDespesa(item.d
         -espesa)                                                        
      21 +                is ItemValidado.Valido -> avaliarDespesa(item.d
         +espesa, periodo)                                               
      22              }
      23          }
      24          return ResultadoLote.Avaliado(periodo, despesas, consol
          idar(despesas))
      25      }
      26  
      27 -    private fun avaliarDespesa(despesa: Despesa): ResultadoDesp
         -esa {                                                          
      28 -        // Etapa 1 (elegibilidade): período, categoria, nota fi
         -scal e duplicidade entram nas T-009 a T-013.                   
      27 +    private fun avaliarDespesa(despesa: Despesa, periodo: Perio
         +do): ResultadoDespesa {                                        
      28 +        // Etapa 1 (elegibilidade): todas as regras são avaliad
         +as, sem interrupção, na ordem da §8.                           
      29 +        val motivos = mutableListOf<Motivo>()                  
      30 +        // 1.1 Pertinência ao período (RN-005, RN-006).        
      31 +        if (despesa.data !in periodo) motivos += Motivo.FORA_DA
         +_COMPETENCIA                                                   
      32 +        // 1.2 a 1.4 entram nas T-010 a T-013.                 
      33          val categoria = Categoria.porCodigo(despesa.categoria)
      34              ?: TODO("T-010: categoria inelegível é recusada com
           CATEGORIA_INELEGIVEL (RN-008)")
      35  
      36 +        // Com qualquer motivo, reembolso 0,00 e nenhuma avalia
         +ção contra o limite (§8, etapa 2).                             
      37 +        if (motivos.isNotEmpty()) return recusar(despesa, motiv
         +os)                                                            
      38 +                                                               
      39          // Etapa 2 (valor).
      40          return avaliarValor(despesa, categoria)
      41      }
      42  
      43 +    private fun recusar(despesa: Despesa, motivos: List<Motivo>
         +) = ResultadoDespesa.Recusada(                                 
      44 +        id = despesa.id,                                       
      45 +        valor = despesa.valor,                                 
      46 +        categoria = despesa.categoria,                         
      47 +        notaExigida = notaExigida(despesa),                    
      48 +        motivos = motivos,                                     
      49 +        mantida = null,                                        
      50 +    )                                                          
      51 +                                                               
      52 +    // RN-003: sobre o valor normalizado, antes de qualquer lim
         +ite.                                                           
      53 +    private fun notaExigida(despesa: Despesa) = despesa.valor >
         + Politica.LIMIAR_NOTA_FISCAL                                   
      54 +                                                               
      55      // RN-001: cada despesa contra o limite da sua categoria, i
          soladamente. RN-002: o limite é teto.
      56      private fun avaliarValor(despesa: Despesa, categoria: Categ
          oria): ResultadoDespesa.Aceita {
      57          val limite = limiteAplicavel(categoria)
● Update(src\reembolso\dominio\Motor.kt)
  ⎿  Added 1 line, removed 1 line
      63              limite = limite,
      64              reembolsado = if (parcial) limite else despesa.valo
          r,
      65              excedente = if (parcial) despesa.valor - limite els
          e null,
      66 -            notaExigida = despesa.valor > Politica.LIMIAR_NOTA_
         -FISCAL, // RN-003                                              
      66 +            notaExigida = notaExigida(despesa),                
      67          )
      68      }
      69  

● Write(tests\reembolso\regras\RN005PeriodoTest.kt)
  ⎿  Wrote 57 lines to tests\reembolso\regras\RN005PeriodoTest.kt
       1 package reembolso.regras
       2
       3 import java.time.LocalDate
       4 import kotlin.test.Test
       5 import kotlin.test.assertEquals
       6 import kotlin.test.assertFalse
       7 import kotlin.test.assertTrue
       8 import reembolso.dominio.Periodo
       9
      10 class RN005PeriodoTest {
     … +60 lines (ctrl+o to expand)

● Write(tests\reembolso\regras\RN006ForaDoPeriodoTest.kt)
  ⎿  Wrote 37 lines to tests\reembolso\regras\RN006ForaDoPeriodoTest.kt
      1 package reembolso.regras
      2
      3 import kotlin.test.Test
      4 import kotlin.test.assertEquals
      5 import reembolso.dominio.Motivo
      6 import reembolso.dominio.ResultadoDespesa
      7 import reembolso.dominio.Valor
      8
      9 class RN006ForaDoPeriodoTest {
     … +34 lines (ctrl+o to expand)

● Ajusto o teste extra do RN-006 para não usar um "d-014" com categoria em
  minúsculas, que não é o d-014 real do exemplo:

● Update(tests\reembolso\regras\RN006ForaDoPeriodoTest.kt)
  ⎿  Added 2 lines, removed 2 lines
      31      }
      32  
      33      @Test
      34 -    fun `RN-006 d-014 em 2026-07-31 → regra não se aplica`() { 
      35 -        avaliar(despesa("d-014", "2026-07-31", "alimentacao", "
         -Restaurante Tavola", "61.00", nf = true)).aceita("d-014")      
      34 +    fun `RN-006 despesa em 2026-07-31 igual à data de fim → reg
         +ra não se aplica`() {                                          
      35 +        avaliar(despesa("x", "2026-07-31", "alimentacao", "Rest
         +aurante Tavola", "61.00", nf = true)).aceita("x")              
      36      }
      37  }

● Bash(sed -i 's/^- \[ \] \*\*T-009\*\*/- [x] **T-009**/'
      specs/001-motor-reembolso/tasks.md && export JAVA_HOME="/c/Program
      Files/Android/Android Studio/jbr" && ./gra…)
  ⎿  Updated specs\001-motor-reembolso\tasks.md (+1 -1)
      152      `RN-011 alimentacao de 60,004 → reembolso 60,00 integral`
      153    - **Commit:** —
      154  
      155 -- [ ] **T-009** — Etapa 1.1 da §8: pertinência ao período, bor
          -das inclusivas,                                               
      155 +- [x] **T-009** — Etapa 1.1 da §8: pertinência ao período, bor
          +das inclusivas,                                               
      156    e recusa com `FORA_DA_COMPETENCIA` sem avaliação contra o li
           mite.
      157    - **Atende:** RN-005, RN-006, AMB-008, AMB-010, AMB-016
      158    - **Aceite:** `RN005PeriodoTest` e `RN006ForaDoPeriodoTest` 
           passam:
  ⎿  … 5 more files changed
  ⎿  (timeout 10m)

● Bash(git add specs/001-motor-reembolso/tasks.md src tests && git status
      --short && git diff --cached --stat && git commit -q -F - <<'EOF'
      feat(T-009): etapa 1.1 da §…)
  ⎿  (No output)

● Commitei a T-009 como 41f8d4f. Agora a T-010: a normalização de categoria e
  fornecedor na construção da Despesa, e a etapa 1.2, que fecha o TODO.

● Update(src\reembolso\dominio\ValidacaoDeItem.kt)
  ⎿  Added 2 lines, removed 2 lines
      29  
      30      val id = texto(lida.id, CampoDespesa.IDENTIFICADOR, causas)
      31      val data = valorDe(lida.data, CampoDespesa.DATA, causas)
      32 -    val categoria = texto(lida.categoria, CampoDespesa.CATEGORI
         -A, causas)                                                     
      33 -    val fornecedor = texto(lida.fornecedor, CampoDespesa.FORNEC
         -EDOR, causas)                                                  
      32 +    val categoria = texto(lida.categoria, CampoDespesa.CATEGORI
         +A, causas)?.let(::normalizarTermo)                             
      33 +    val fornecedor = texto(lida.fornecedor, CampoDespesa.FORNEC
         +EDOR, causas)?.let(::normalizarTermo)                          
      34      // RN-011: normaliza antes da validação de faixa.
      35      val valor = valorDe(lida.valor, CampoDespesa.VALOR, causas)
          ?.let(Valor::normalizar)
      36      if (valor != null && valor.decimal.signum() <= 0) {

● Update(src\reembolso\dominio\ValidacaoDeItem.kt)
  ⎿  Added 5 lines
      57      return null
      58  }
      59  
      60 +// RN-008: categoria e fornecedor em minúsculas pelo mapeamento
         + completo de caixa do Unicode,                                 
      61 +// independente de localidade — lowercase() do Kotlin usa Local
         +e.ROOT (DT-005). O identificador                               
      62 +// não passa por aqui: normaliza-se o que é termo de negócio, n
         +ão o que é chave (RN-012).                                     
      63 +private fun normalizarTermo(aparado: String): String = aparado.
         +lowercase()                                                    
      64 +                                                               
      65  // §4.1: texto obrigatório vazio após aparar() é tratado como a
          usente.
      66  private fun texto(lido: Lido<String>, campo: CampoDespesa, caus
          as: MutableList<CausaErro>): String? {
      67      val aparado = valorDe(lido, campo, causas)?.aparar() ?: ret
          urn null

● Update(src\reembolso\dominio\Motor.kt)
  ⎿  Added 4 lines, removed 3 lines
      29          val motivos = mutableListOf<Motivo>()
      30          // 1.1 Pertinência ao período (RN-005, RN-006).
      31          if (despesa.data !in periodo) motivos += Motivo.FORA_DA
          _COMPETENCIA
      32 -        // 1.2 a 1.4 entram nas T-010 a T-013.                 
      32 +        // 1.2 Elegibilidade da categoria, já normalizada (RN-0
         +08).                                                           
      33          val categoria = Categoria.porCodigo(despesa.categoria)
      34 -            ?: TODO("T-010: categoria inelegível é recusada com
         - CATEGORIA_INELEGIVEL (RN-008)")                               
      34 +        if (categoria == null) motivos += Motivo.CATEGORIA_INEL
         +EGIVEL                                                         
      35 +        // 1.3 e 1.4 entram nas T-011 e T-013.                 
      36  
      37          // Com qualquer motivo, reembolso 0,00 e nenhuma avalia
          ção contra o limite (§8, etapa 2).
      37 -        if (motivos.isNotEmpty()) return recusar(despesa, motiv
         -os)                                                            
      38 +        if (motivos.isNotEmpty() || categoria == null) return r
         +ecusar(despesa, motivos)                                       
      39  
      40          // Etapa 2 (valor).
      41          return avaliarValor(despesa, categoria)

● Write(tests\reembolso\regras\RN008CategoriaTest.kt)
  ⎿  Wrote 112 lines to tests\reembolso\regras\RN008CategoriaTest.kt
       1 package reembolso.regras
       2
       3 import java.util.Locale
       4 import kotlin.test.Test
       5 import kotlin.test.assertEquals
       6 import kotlin.test.assertIs
       7 import kotlin.test.assertNotEquals
       8 import kotlin.test.assertTrue
       9 import reembolso.dominio.ItemValidado
      10 import reembolso.dominio.Lido
     … +136 lines (ctrl+o to expand)

● Bash(grep -n 'TODO' src -r; sed -i 's/^- \[ \] \*\*T-010\*\*/- [x] **T-010**/'
      specs/001-motor-reembolso/tasks.md && export JAVA_HOME="/c/Program
      Files/Android/Andro…)
  ⎿  Updated specs\001-motor-reembolso\tasks.md (+1 -1)
      166      `RN-006 despesa em 2026-08-01 com NF → recusada com FORA_D
           A_COMPETENCIA`
      167    - **Commit:** —
      168  
      169 -- [ ] **T-010** — Normalização de categoria e fornecedor (`apa
          -rar()` +                                                      
      169 +- [x] **T-010** — Normalização de categoria e fornecedor (`apa
          +rar()` +                                                      
      170    `lowercase()`) na construção de `Despesa`; etapa 1.2 da §8: 
           elegibilidade
      171    da categoria.
      172    - **Atende:** RN-008, AMB-013, AMB-017
  ⎿  … 5 more files changed
  ⎿  (timeout 5m)

● Build verde, sem TODO em src/ e sem caracteres invisíveis no teste. Agora as
  alterações de propósito:

● Bash(export JAVA_HOME="/c/Program Files/Android/Android Studio/jbr" &&
      V=src/reembolso/dominio/ValidacaoDeItem.kt
      M=src/reembolso/dominio/Motor.kt SC=/c/Users/dev/Ap…)
  ⎿  5 files changed (binary, mode only or too large to show)
  ⎿  (timeout 10m)

● Bash(git add specs/001-motor-reembolso/tasks.md src tests && git status
      --short && git diff --cached --stat && git commit -q -F - <<'EOF'
      feat(T-010): normalização d…)
  ⎿  (No output)

● Os dois diffs pedidos, um por commit:

● Bash(echo "################ T-009 (41f8d4f) ################" && git show
      --format= 41f8d4f -- src specs && git show --format= --stat 41f8d4f --
      tests && echo && ech…)
  ⎿  (No output)

● Fiz os quatro commits na ordem combinada, lendo o git status antes de cada um.
  Não fiz push. O build passa com 76 testes e nenhuma falha.

  ┌────────────────┬────────────────────────────────────────────────────────┐
  │     Commit     │                        Conteúdo                        │
  ├────────────────┼────────────────────────────────────────────────────────┤
  │ d7b07b6        │ RN-008 com a conversão de caixa pelo mapeamento        │
  │ docs(spec)     │ completo do Unicode, independente de localidade; D-011 │
  ├────────────────┼────────────────────────────────────────────────────────┤
  │ 47e8fdc docs   │ plan e tasks na spec 1.3, mais duas coisas no mesmo    │
  │                │ commit (abaixo)                                        │
  ├────────────────┼────────────────────────────────────────────────────────┤
  │ 5af7f0b        │ etapa de valor, com o comentário do consolidado que    │
  │ feat(T-008)    │ você ditou                                             │
  ├────────────────┼────────────────────────────────────────────────────────┤
  │ 41f8d4f        │ etapa 1.1: pertinência ao período e                    │
  │ feat(T-009)    │ FORA_DA_COMPETENCIA                                    │
  ├────────────────┼────────────────────────────────────────────────────────┤
  │ 97504e0        │ normalização de categoria e fornecedor, etapa 1.2 e    │
  │ feat(T-010)    │ fim do TODO                                            │
  └────────────────┴────────────────────────────────────────────────────────┘

  O commit docs também levou:
  - os nomes de teste dos aceites novos da RN-008 nas listas da T-010 e da
    T-013;
  - uma linha na DT-005 dizendo que ela implementa a RN-008.

  Antes de escrever código, procurei o que a spec não decide nas duas tasks e
  não achei nada.

  Contagem de testes

  Task: T-009
  Testes: 9
  Da task: 8 (RN005PeriodoTest: 6, RN006ForaDoPeriodoTest: 2)
  A mais: 1: RN-006 despesa em 2026-07-31 igual à data de fim → regra não se
  aplica. É um aceite da RN-006 na spec que não estava na task.
  ────────────────────────────────────────
  Task: T-010
  Testes: 10 (RN008CategoriaTest)
  Da task: 6 originais + 3 dos aceites novos da RN-008
  A mais: 1: RN-008 AMB-018 fora do período e categoria inelegível →
  FORA_DA_COMPETENCIA antes de CATEGORIA_INELEGIVEL. É a coerência da coleta
  com a ordem da §8 que você pediu.

  Os testes falham quando devem. Fiz nove alterações isoladas no código, todas
  quebraram o build, e o código foi restaurado depois de cada uma.

  ┌───────┬───────────────────────────────────────────┬─────────────────────┐
  │ Task  │                 Alteração                 │ Testes que falharam │
  ├───────┼───────────────────────────────────────────┼─────────────────────┤
  │ T-009 │ borda de início exclusiva                 │ 2                   │
  ├───────┼───────────────────────────────────────────┼─────────────────────┤
  │ T-009 │ borda de fim exclusiva                    │ 4                   │
  ├───────┼───────────────────────────────────────────┼─────────────────────┤
  │ T-009 │ sem a regra de período                    │ 2                   │
  ├───────┼───────────────────────────────────────────┼─────────────────────┤
  │ T-009 │ motivo não impede a etapa de valor        │ 2                   │
  ├───────┼───────────────────────────────────────────┼─────────────────────┤
  │ T-010 │ sem conversão de caixa                    │ 6                   │
  ├───────┼───────────────────────────────────────────┼─────────────────────┤
  │ T-010 │ conversão dependente de localidade        │ 1                   │
  ├───────┼───────────────────────────────────────────┼─────────────────────┤
  │ T-010 │ mapeamento simples, letra a letra         │ 2                   │
  ├───────┼───────────────────────────────────────────┼─────────────────────┤
  │ T-010 │ categoria inelegível sem o próprio motivo │ 5                   │
  ├───────┼───────────────────────────────────────────┼─────────────────────┤
  │ T-010 │ categoria coletada antes do período       │ 1                   │
  └───────┴───────────────────────────────────────────┴─────────────────────┘

  Pontos para você conferir no diff

  - A ordem de coleta segue a §8. No Motor, o motivo da 1.1 (período) é
    acrescentado antes do da 1.2 (categoria), e o teste a mais da T-010 verifica
    isso.
  - Os testes da RN-005 usam a pertinência direto, sem passar pelo Motor. No
    commit da T-009, o TODO da categoria ainda existia. Avaliar d-005
    ("coworking") ou d-014 ("ALIMENTACAO") pelo Motor teria quebrado nesse
    commit. A RN-006 é testada pelo Motor.
  - O teste de localidade troca a localidade do processo para turca e
    azerbaijana e restaura a original no fim. Ele também confere, antes, que
    nessas localidades a conversão dependente de localidade daria mesmo
    "alımentacao". Assim, o teste não passa por acaso.
  - O quarto aceite novo da RN-008, a duplicata entre os fornecedores "ΟΔΟΣ" e
    "Οδος", fica para a T-013, porque depende da RN-009. Ele já está listado lá.
  - O comentário do consolidado envelheceu em parte. Ele diz "quando a etapa 1
    da §8 existir e houver recusa possível", e depois da T-009 a recusa já é
    possível. A parte "Completas na T-014" continua certa. Deixei o texto como
    você ditou; se quiser ajustar, entra na T-014.

  A próxima é a T-011: a recusa por falta de nota fiscal (RN-003 e RN-004).

> git push

● Bash(git push 2>&1 && git status -sb | head -1)
  ⎿  (No output)

● Fiz o push: a main local e a origin/main estão iguais, no 97504e0. Subiram os
  seis commits desde o último push, e375e73..97504e0:
  - a T-007;
  - o docs(spec) da D-011 e o docs que atualiza plan e tasks para a spec 1.3;
  - a T-008, a T-009 e a T-010.

> Execute a T-011: recusa por falta de nota fiscal, etapa 1.3 da §8
  (RN-003, RN-004).

  Pontos de atenção:

  - A RN-003 já tem o limiar implementado na T-008, no campo notaExigida
    de Aceita. A T-011 acrescenta a RECUSA SEM_NOTA_FISCAL, que é outra
    coisa: é a etapa 1.3, não a §4.1. Verifique se o notaExigida existente
    e a nova recusa usam o mesmo limiar, do mesmo lugar — não duplique a
    constante.
  - A ordem da §8 continua valendo: período (1.1), categoria (1.2), nota
    fiscal (1.3). Quero um teste que verifique a coleta nessa ordem com as
    três causas presentes na mesma despesa, como você fez na T-010.
  - A RN-007 diz que os motivos são coletados sem interrupção. Uma despesa
    sem NF, fora do período e de categoria inelegível tem três motivos, não
    um.

  Se alguma coisa aqui não estiver decidida na spec, verifique primeiro se
  o plan.md já decidiu. Se decidiu e é coerente, me diga que é regra a
  escrever na spec, não ambiguidade a abrir.

● Bash(git status --short; sed -n '/^- \[ \] \*\*T-011/,/Commit/p'
      specs/001-motor-reembolso/tasks.md; S=specs/001-motor-reembolso/spec.md;
      sed -n '/^### RN-003/,/^###…)
  ⎿  (No output)
● T-011 fica resolvida: RN-003 fixa o limiar (>R$100,00 sobre o valor
  normalizado), e RN-004/RN-007 cobrem a recusa e a coleta sem interrupção. O
  limiar está centralizado em Politica.LIMIAR_NOTA_FISCAL, usado só por
  notaExigida(despesa) no Motor, e a nova recusa vai reutilizar essa mesma
  função.

● Update(src\reembolso\dominio\Motor.kt)
  ⎿  Added 3 lines, removed 1 line
      32          // 1.2 Elegibilidade da categoria, já normalizada (RN-0
          08).
      33          val categoria = Categoria.porCodigo(despesa.categoria)
      34          if (categoria == null) motivos += Motivo.CATEGORIA_INEL
          EGIVEL
      35 -        // 1.3 e 1.4 entram nas T-011 e T-013.                 
      35 +        // 1.3 Exigência de nota fiscal (RN-003, RN-004).      
      36 +        if (notaExigida(despesa) && !despesa.temNotaFiscal) mot
         +ivos += Motivo.SEM_NOTA_FISCAL                                 
      37 +        // 1.4 entra na T-013.                                 
      38  
      39          // Com qualquer motivo, reembolso 0,00 e nenhuma avalia
          ção contra o limite (§8, etapa 2).
      40          if (motivos.isNotEmpty() || categoria == null) return r
          ecusar(despesa, motivos)

● Update(src\reembolso\dominio\Motor.kt)
  ⎿  Added 2 lines, removed 1 line
      52          mantida = null,
      53      )
      54  
      55 -    // RN-003: sobre o valor normalizado, antes de qualquer lim
         -ite.                                                           
      55 +    // RN-003: sobre o valor normalizado, antes de qualquer lim
         +ite. Único ponto que lê o limiar:                              
      56 +    // alimenta o campo notaExigida da saída e a recusa SEM_NOT
         +A_FISCAL da etapa 1.3.                                         
      57      private fun notaExigida(despesa: Despesa) = despesa.valor >
           Politica.LIMIAR_NOTA_FISCAL
      58  
      59      // RN-001: cada despesa contra o limite da sua categoria, i
          soladamente. RN-002: o limite é teto.

● Update(tests\reembolso\regras\Lotes.kt)
  ⎿  Added 4 lines
      43  fun List<ResultadoDespesa>.aceita(id: String): ResultadoDespesa
          .Aceita =
      44      filterIsInstance<ResultadoDespesa.Aceita>().single { it.id 
          == id }
      45  
      46 +// Recusada não tem campo de limite nem de reembolso: o tipo ga
         +rante "sem limite aplicado" e 0,00.                            
      47 +fun List<ResultadoDespesa>.recusada(id: String): ResultadoDespe
         +sa.Recusada =                                                  
      48 +    filterIsInstance<ResultadoDespesa.Recusada>().single { it.i
         +d == id }                                                      
      49 +                                                               
      50  fun valor(texto: String): Lido<BigDecimal> = Lido.Presente(BigD
          ecimal(texto))
      51  
      52  fun despesa(id: String, data: String, categoria: String, fornec
          edor: String, valor: String, nf: Boolean) =

● Update(tests\reembolso\regras\RN006ForaDoPeriodoTest.kt)
  ⎿  Removed 4 lines
       8  
       9  class RN006ForaDoPeriodoTest {
      10  
      11 -    // Recusada não tem campo de limite nem de reembolso: o tip
         -o garante "sem limite aplicado" e 0,00.                        
      12 -    private fun List<ResultadoDespesa>.recusada(id: String) =  
      13 -        filterIsInstance<ResultadoDespesa.Recusada>().single { 
         -it.id == id }                                                  
      14 -                                                               
      11      @Test
      12      fun `RN-006 AMB-010 d-008 alimentacao 41,00 com NF → recusa
          da com FORA_DA_COMPETENCIA sem limite aplicado`() {
      13          val d008 = despesa("d-008", "2026-04-15", "alimentacao"
          , "Restaurante Tavola", "41.00", nf = true)

● Update(tests\reembolso\regras\RN006ForaDoPeriodoTest.kt)
  ⎿  Removed 1 line
      3  import kotlin.test.Test
      4  import kotlin.test.assertEquals
      5  import reembolso.dominio.Motivo
      6 -import reembolso.dominio.ResultadoDespesa                       
      6  import reembolso.dominio.Valor
      7  
      8  class RN006ForaDoPeriodoTest {

● Write(tests\reembolso\regras\RN003NotaFiscalTest.kt)
  ⎿  Wrote 74 lines to tests\reembolso\regras\RN003NotaFiscalTest.kt
      1 package reembolso.regras
      2
      3 import kotlin.test.Test
      4 import kotlin.test.assertEquals
      5 import kotlin.test.assertFalse
      6 import kotlin.test.assertNull
      7 import kotlin.test.assertTrue
      8 import reembolso.dominio.Motivo
      9 import reembolso.dominio.Valor
     … +80 lines (ctrl+o to expand)

● Write(tests\reembolso\regras\RN004SemNotaFiscalTest.kt)
  ⎿  Wrote 59 lines to tests\reembolso\regras\RN004SemNotaFiscalTest.kt
      1 package reembolso.regras
      2
      3 import kotlin.test.Test
      4 import kotlin.test.assertEquals
      5 import kotlin.test.assertNull
      6 import reembolso.dominio.Motivo
      7 import reembolso.dominio.Valor
      8
      9 class RN004SemNotaFiscalTest {
     … +64 lines (ctrl+o to expand)

● Bash(sed -i 's/^- \[ \] \*\*T-011\*\*/- [x] **T-011**/'
      specs/001-motor-reembolso/tasks.md && export JAVA_HOME="/c/Program
      Files/Android/Android Studio/jbr" && ./gra…)
  ⎿  Updated specs\001-motor-reembolso\tasks.md (+1 -1)
      182      `RN-008 AMB-013 ALİMENTACAO com U+0130 → recusada com CATE
           GORIA_INELEGIVEL`
      183    - **Commit:** —
      184  
      185 -- [ ] **T-011** — Etapa 1.3 da §8: exigência de nota fiscal so
          -bre o valor                                                   
      185 +- [x] **T-011** — Etapa 1.3 da §8: exigência de nota fiscal so
          +bre o valor                                                   
      186    normalizado (`Politica.LIMIAR_NOTA_FISCAL`) e recusa com `SE
           M_NOTA_FISCAL`.
      187    - **Atende:** RN-003, RN-004, RN-011, AMB-004, AMB-005, AMB-
           006, AMB-014
      188    - **Aceite:** `RN003NotaFiscalTest` e `RN004SemNotaFiscalTes
           t` passam:
  ⎿  … 5 more files changed
  ⎿  (timeout 5m)

● Build verde: 7 + 6 testes novos, e o limiar é lido num só ponto. Agora as
  alterações de propósito:

● Bash(export JAVA_HOME="/c/Program Files/Android/Android Studio/jbr" &&
      M=src/reembolso/dominio/Motor.kt
      SC=/c/Users/dev/AppData/Local/Temp/claude/c--Users-dev-SDD-De…)
  ⎿  5 files changed (binary, mode only or too large to show)
  ⎿  (timeout 10m)

● Bash(git add specs/001-motor-reembolso/tasks.md src tests && git status
      --short && git diff --cached -- specs src tests/reembolso/regras/Lotes.kt
      tests/reembolso/reg…)
  ⎿  (No output)

● A T-011 está pronta e no índice, mas não commitei: a sua mensagem não dizia se
  era para commitar direto, e o padrão até aqui tem sido mostrar o diff antes.
  O diff de src, do tasks.md e dos ajustes nos testes está acima. Os dois
  arquivos de teste novos estão no índice.

  O ./gradlew build passa com 89 testes e nenhuma falha. O checklist está limpo,
  sem .trim() nem Double em src/.

  Nada ficou sem decisão na spec. A RN-003 fixa o limiar como "estritamente
  maior que R$ 100,00, sobre o valor normalizado". A RN-004 fixa a recusa
  integral, e a RN-007 manda coletar os motivos sem interromper. Não precisei
  recorrer ao plan.

  Os seus três pontos:
  1. Um só limiar, de um só lugar. Politica.LIMIAR_NOTA_FISCAL é lido apenas
     pela função notaExigida(despesa) do Motor, e essa função alimenta tanto o
     campo notaExigida da saída quanto a recusa nova: notaExigida(despesa) &&
     !despesa.temNotaFiscal. Confirmei com grep em src/ que não há outra leitura
     nem outro "100…" além da constante. Uma alteração de propósito também
     mostra isso: dar à recusa um limiar próprio e divergente (100,01) quebra 3
     testes.
  2. Ordem 1.1 → 1.2 → 1.3 com as três causas na mesma despesa: RN-004 AMB-018
     fora do período, categoria inelegível e sem NF exigida → três motivos na
     ordem da §8. A despesa usada é coworking, 150,00, sem NF, em 2026-08-01, e
     o teste exige [FORA_DA_COMPETENCIA, CATEGORIA_INELEGIVEL, SEM_NOTA_FISCAL].
  3. Sem interrupção: o mesmo teste exige os três motivos. Fiz uma alteração que
     retorna logo depois do primeiro motivo, e ela quebra 2 testes.

  Testes: 13 no total.
  - RN003NotaFiscalTest: 7, todos os da task.
  - RN004SemNotaFiscalTest: 6. São os 5 da task mais o de ordem do item 2.
  - O de 100,004/100,005 usa hospedagem sem NF, para que a fronteira decida
    entre aceita e recusada, e não só o valor do campo notaExigida.
  - O de hospedagem de 200,00 sem NF confere também o outro lado: com NF, a
    mesma despesa sai integral.

  Os testes falham quando devem. Fiz cinco alterações isoladas e todas quebraram
  o build:

  ┌─────────────────────────────────────────┬─────────────────────┐
  │                Alteração                │ Testes que falharam │
  ├─────────────────────────────────────────┼─────────────────────┤
  │ 100,00 passa a exigir nota (>=)         │ 3                   │
  ├─────────────────────────────────────────┼─────────────────────┤
  │ recusa ignora a nota apresentada        │ 5                   │
  ├─────────────────────────────────────────┼─────────────────────┤
  │ recusa com limiar próprio, divergente   │ 3                   │
  ├─────────────────────────────────────────┼─────────────────────┤
  │ nota fiscal coletada antes da categoria │ 1                   │
  ├─────────────────────────────────────────┼─────────────────────┤
  │ interrompe no primeiro motivo           │ 2                   │
  └─────────────────────────────────────────┴─────────────────────┘

  Depois restaurei o código e o build voltou a passar.

  Um ajuste pequeno de teste: o atalho recusada(id), que estava privado no
  RN006ForaDoPeriodoTest, passou para o Lotes.kt, porque agora três arquivos o
  usam.

  Mensagem proposta:
  feat(T-011): etapa 1.3 da §8 — recusa SEM_NOTA_FISCAL com o limiar único da
  RN-003 (RN-003, RN-004)

  Posso commitar?

> Pode commitar. A mensagem está boa.

● Bash(git status --short && git commit -q -F - <<'EOF'
      feat(T-011): etapa 1.3 da §8 — recusa SEM_NOTA_FISCAL com o limiar único
      da RN-003 (RN-003, RN-004)…)
  ⎿  (No output)

● Commitei a T-011 na main como dcc4601, com a T-011 marcada [x] no mesmo
  commit. Não ficou nada pendente fora dele, e ainda não fiz push.

  A próxima é a T-012: os testes da lista de motivos da RN-007. A coleta sem
  interrupção já existe, então a task deve ser principalmente de verificação.

> Execute a T-012 e a T-013 na mesma rodada, mostrando os dois diffs
  separados e a contagem de testes de cada uma.

  A T-013 fecha a etapa 1 da §8 (duplicidade, 1.4) e a T-012 consolida a
  lista de motivos (RN-007). Elas se tocam: a duplicidade acrescenta um
  motivo à lista que a T-012 governa. Faça a T-012 primeiro.

  Pontos de atenção na T-013:

  - A chave de comparação da RN-009 usa fornecedor normalizado, que agora
    passa pela conversão de caixa da RN-008. O quarto aceite da RN-008 —
    "ΟΔΟΣ" e "Οδος" formando grupo de duplicatas — está listado nesta task
    e precisa virar teste aqui.
  - A ordenação lexicográfica por code point que decidimos na RN-009:
    quero um teste que falharia se a comparação usasse a ordem de alguma
    localidade em vez de code point.
  - Despesa de categoria inelegível não integra grupo de duplicatas. Isso
    já está na spec; verifique que o código respeita.

  Mesma regra de antes: se faltar decisão, cheque o plan primeiro.

● A T-012 vem primeiro. Antes, leio de novo as duas tasks, a RN-009 e o que o
  plan diz sobre a ordenação dos motivos e a duplicidade (DT-006 e DT-007).

● Bash(git status --short; T=specs/001-motor-reembolso/tasks.md; sed -n '/^- \[
      \] \*\*T-012/,/Commit/p;/^- \[ \] \*\*T-013/,/Commit/p' $T; sed -n '/^###
      DT-006/,/^###…)
  ⎿  (No output)

● Antes de implementar a T-012, noto que a ordenação por Motivo.ordinal passa a
  garantir a ordem de saída via enum, o que faz com que os testes de ordem da
  T-010/T-011 deixem de detectar coletas fora de ordem, passando a validar
  apenas a ordem final. Vou seguir agora com a implementação da T-012.

● Update(src\reembolso\dominio\Motor.kt)
  ⎿  Added 2 lines, removed 1 line
      48          valor = despesa.valor,
      49          categoria = despesa.categoria,
      50          notaExigida = notaExigida(despesa),
      51 -        motivos = motivos,                                     
      51 +        // RN-007: ordem da §8, que é a ordem de declaração de 
         +Motivo (DT-006, OrdemMotivosSecao8Test).                       
      52 +        motivos = motivos.sortedBy { it.ordinal },             
      53          mantida = null,
      54      )
      55  

● Write(tests\reembolso\regras\RN007ListaDeMotivosTest.kt)
  ⎿  Wrote 70 lines to tests\reembolso\regras\RN007ListaDeMotivosTest.kt
       1 package reembolso.regras
       2
       3 import kotlin.test.Test
       4 import kotlin.test.assertEquals
       5 import kotlin.test.assertFalse
       6 import kotlin.test.assertIs
       7 import kotlin.test.assertTrue
       8 import reembolso.dominio.Lido
       9 import reembolso.dominio.Motivo
      10 import reembolso.dominio.ResultadoDespesa
     … +77 lines (ctrl+o to expand)

● Bash(sed -i 's/^- \[ \] \*\*T-012\*\*/- [x] **T-012**/'
      specs/001-motor-reembolso/tasks.md && export JAVA_HOME="/c/Program
      Files/Android/Android Studio/jbr" && ./gra…)
  ⎿  Updated specs\001-motor-reembolso\tasks.md (+1 -1)
      200      `RN-004 d-003 e transporte 100,01 com NF → regra não se ap
           lica, reembolso 80,00 parcial`
      201    - **Commit:** —
      202  
      203 -- [ ] **T-012** — Lista de motivos: todas as regras de elegibi
          -lidade avaliadas                                              
      203 +- [x] **T-012** — Lista de motivos: todas as regras de elegibi
          +lidade avaliadas                                              
      204    sem interrupção, lista ordenada por `Motivo.ordinal`, recusa
            sem limite,
      205    erro com lista vazia.
      206    - **Atende:** RN-007, AMB-017, AMB-018; §8 (recusada não é a
           valiada contra o limite)
  ⎿  … 5 more files changed
  ⎿  (timeout 10m)

● T-012 está verde com 7 testes, e as três alterações propositais quebraram o
  build como esperado. Vou deixá-la no índice sem commitar, para que git diff
  --cached mostre só a T-012 enquanto a T-013 fica visível em git diff, mantendo
  os dois diffs separados.

● Bash(git add specs/001-motor-reembolso/tasks.md src tests && git status
      --short)  ⎿  (No output)

● T-013. Para a duplicidade em duas passadas, o Motor precisa ser reestruturado.
  Leio o estado atual:

  Read 1 file (ctrl+o to expand)

● Update(src\reembolso\dominio\Motor.kt)
  ⎿  Added 71 lines, removed 24 lines
       14          )
       15          val elementos = (lote.despesas as DespesasLidas.Presen
           te).elementos
       16  
       17 -        // Etapa 0: validação de item (RN-010); etapas 1 e 2 s
          -ó para as válidas.                                            
       18 -        val despesas = validarItens(elementos).map { item ->  
       17 +        // Etapa 0: validação de item (RN-010). Etapa 1, passa
          +da 1: regras 1.1 a 1.3 de cada despesa válida.                
       18 +        val avaliacoes = validarItens(elementos).map { item ->
       19              when (item) {
       20 -                is ItemValidado.Invalido -> item.erro         
       21 -                is ItemValidado.Valido -> avaliarDespesa(item.
          -despesa, periodo)                                             
       20 +                is ItemValidado.Invalido -> Avaliacao.Erro(ite
          +m.erro)                                                       
       21 +                is ItemValidado.Valido -> elegibilidade(item.d
          +espesa, periodo)                                              
       22              }
       23          }
       24 +        // Etapa 1, passada 2: 1.4 duplicidade, só entre as de
          +spesas sem motivo (RN-009, DT-007).                           
       25 +        marcarDuplicatas(avaliacoes.filterIsInstance<Avaliacao
          +.Elegibilidade>().filter { it.motivos.isEmpty() })            
       26 +                                                              
       27 +        // Etapa 2 só para as despesas sem motivo; a ordem da 
          +saída é a da entrada (§4.2).                                  
       28 +        val despesas = avaliacoes.map(::concluir)             
       29          return ResultadoLote.Avaliado(periodo, despesas, conso
           lidar(despesas))
       30      }
       31  
       27 -    private fun avaliarDespesa(despesa: Despesa, periodo: Peri
          -odo): ResultadoDespesa {                                      
       28 -        // Etapa 1 (elegibilidade): todas as regras são avalia
          -das, sem interrupção, na ordem da §8.                         
       29 -        val motivos = mutableListOf<Motivo>()                 
       30 -        // 1.1 Pertinência ao período (RN-005, RN-006).       
       31 -        if (despesa.data !in periodo) motivos += Motivo.FORA_D
          -A_COMPETENCIA                                                 
       32 +    private sealed interface Avaliacao {                      
       33 +        class Erro(val erro: ResultadoDespesa.ComErro) : Avali
          +acao                                                          
       34 +                                                              
       35 +        class Elegibilidade(val despesa: Despesa, val categori
          +a: Categoria?) : Avaliacao {                                  
       36 +            val motivos = mutableListOf<Motivo>()             
       37 +            var mantida: String? = null                       
       38 +        }                                                     
       39 +    }                                                         
       40 +                                                              
       41 +    // Etapa 1 (elegibilidade): todas as regras são avaliadas,
          + sem interrupção, na ordem da §8.                             
       42 +    private fun elegibilidade(despesa: Despesa, periodo: Perio
          +do): Avaliacao.Elegibilidade {                                
       43          // 1.2 Elegibilidade da categoria, já normalizada (RN-
           008).
       44          val categoria = Categoria.porCodigo(despesa.categoria)
       34 -        if (categoria == null) motivos += Motivo.CATEGORIA_INE
          -LEGIVEL                                                       
       35 -        // 1.3 Exigência de nota fiscal (RN-003, RN-004).     
       36 -        if (notaExigida(despesa) && !despesa.temNotaFiscal) mo
          -tivos += Motivo.SEM_NOTA_FISCAL                               
       37 -        // 1.4 entra na T-013.                                
       45 +        return Avaliacao.Elegibilidade(despesa, categoria).app
          +ly {                                                          
       46 +            // 1.1 Pertinência ao período (RN-005, RN-006).   
       47 +            if (despesa.data !in periodo) motivos += Motivo.FO
          +RA_DA_COMPETENCIA                                             
       48 +            // 1.2                                            
       49 +            if (categoria == null) motivos += Motivo.CATEGORIA
          +_INELEGIVEL                                                   
       50 +            // 1.3 Exigência de nota fiscal (RN-003, RN-004). 
       51 +            if (notaExigida(despesa) && !despesa.temNotaFiscal
          +) motivos += Motivo.SEM_NOTA_FISCAL                           
       52 +        }                                                     
       53 +    }                                                         
       54  
       39 -        // Com qualquer motivo, reembolso 0,00 e nenhuma avali
          -ação contra o limite (§8, etapa 2).                           
       40 -        if (motivos.isNotEmpty() || categoria == null) return 
          -recusar(despesa, motivos)                                     
       55 +    // RN-009: chave de comparação, com categoria, fornecedor 
          +e valor já normalizados.                                      
       56 +    private data class ChaveDuplicidade(val data: LocalDate, v
          +al categoria: String, val fornecedor: String, val valor: Valor
          +)                                                             
       57  
       42 -        // Etapa 2 (valor).                                   
       43 -        return avaliarValor(despesa, categoria)               
       58 +    // 1.4 Duplicidade (RN-009): em cada grupo, mantém a de me
          +nor identificador; as demais recebem DUPLICATA.               
       59 +    private fun marcarDuplicatas(semMotivo: List<Avaliacao.Ele
          +gibilidade>) {                                                
       60 +        semMotivo                                             
       61 +            .groupBy { ChaveDuplicidade(it.despesa.data, it.de
          +spesa.categoria, it.despesa.fornecedor, it.despesa.valor) }   
       62 +            .values                                           
       63 +            .filter { it.size > 1 }                           
       64 +            .forEach { grupo ->                               
       65 +                val mantida = grupo.minWith(compareBy(porPonto
          +DeCodigo) { it.despesa.id })                                  
       66 +                grupo.filter { it !== mantida }.forEach {     
       67 +                    it.motivos += Motivo.DUPLICATA            
       68 +                    it.mantida = mantida.despesa.id           
       69 +                }                                             
       70 +            }                                                 
       71      }
       72  
       46 -    private fun recusar(despesa: Despesa, motivos: List<Motivo
          ->) = ResultadoDespesa.Recusada(                               
       47 -        id = despesa.id,                                      
       48 -        valor = despesa.valor,                                
       49 -        categoria = despesa.categoria,                        
       50 -        notaExigida = notaExigida(despesa),                   
       73 +    // RN-009: ordem lexicográfica pelo ponto de código Unicod
          +e. String.compareTo compara unidades                          
       74 +    // UTF-16 e diverge fora do BMP (DT-007, R-07); um Collato
          +r seguiria a ordem de uma localidade.                         
       75 +    private val porPontoDeCodigo = Comparator<String> { a, b -
          +>                                                             
       76 +        val pa = a.codePoints().toArray()                     
       77 +        val pb = b.codePoints().toArray()                     
       78 +        for (i in 0 until minOf(pa.size, pb.size)) {          
       79 +            if (pa[i] != pb[i]) return@Comparator pa[i].compar
          +eTo(pb[i])                                                    
       80 +        }                                                     
       81 +        pa.size.compareTo(pb.size)                            
       82 +    }                                                         
       83 +                                                              
       84 +    private fun concluir(avaliacao: Avaliacao): ResultadoDespe
          +sa = when (avaliacao) {                                       
       85 +        is Avaliacao.Erro -> avaliacao.erro                   
       86 +        // Com qualquer motivo, reembolso 0,00 e nenhuma avali
          +ação contra o limite (§8, etapa 2).                           
       87 +        is Avaliacao.Elegibilidade ->                         
       88 +            if (avaliacao.motivos.isNotEmpty() || avaliacao.ca
          +tegoria == null) recusar(avaliacao)                           
       89 +            // Etapa 2 (valor).                               
       90 +            else avaliarValor(avaliacao.despesa, avaliacao.cat
          +egoria)                                                       
       91 +    }                                                         
       92 +                                                              
       93 +    private fun recusar(avaliacao: Avaliacao.Elegibilidade) = 
          +ResultadoDespesa.Recusada(                                    
       94 +        id = avaliacao.despesa.id,                            
       95 +        valor = avaliacao.despesa.valor,                      
       96 +        categoria = avaliacao.despesa.categoria,              
       97 +        notaExigida = notaExigida(avaliacao.despesa),         
       98          // RN-007: ordem da §8, que é a ordem de declaração de
            Motivo (DT-006, OrdemMotivosSecao8Test).
       52 -        motivos = motivos.sortedBy { it.ordinal },            
       53 -        mantida = null,                                       
       99 +        motivos = avaliacao.motivos.sortedBy { it.ordinal },  
      100 +        mantida = avaliacao.mantida,                          
      101      )
      102  
      103      // RN-003: sobre o valor normalizado, antes de qualquer li
           mite. Único ponto que lê o limiar:

● Update(src\reembolso\dominio\Motor.kt)
  ⎿  Added 2 lines
      1  package reembolso.dominio
      2  
      3 +import java.time.LocalDate                                      
      4 +                                                                
      5  // DT-006: ler esta função é ler a §8.
      6  object Motor {
      7      fun avaliar(lote: LoteLido): ResultadoLote {

● Update(src\reembolso\dominio\Motor.kt)
  ⎿  Added 2 lines, removed 2 lines
      42  
      43      // Etapa 1 (elegibilidade): todas as regras são avaliadas, 
          sem interrupção, na ordem da §8.
      44      private fun elegibilidade(despesa: Despesa, periodo: Period
          o): Avaliacao.Elegibilidade {
      45 -        // 1.2 Elegibilidade da categoria, já normalizada (RN-0
         -08).                                                           
      45 +        // Categoria da política, ou null se inelegível; usada 
         +na 1.2 e na etapa 2.                                           
      46          val categoria = Categoria.porCodigo(despesa.categoria)
      47          return Avaliacao.Elegibilidade(despesa, categoria).appl
          y {
      48              // 1.1 Pertinência ao período (RN-005, RN-006).
      49              if (despesa.data !in periodo) motivos += Motivo.FOR
          A_DA_COMPETENCIA
      50 -            // 1.2                                             
      50 +            // 1.2 Elegibilidade da categoria, já normalizada (
         +RN-008).                                                       
      51              if (categoria == null) motivos += Motivo.CATEGORIA_
          INELEGIVEL
      52              // 1.3 Exigência de nota fiscal (RN-003, RN-004).
      53              if (notaExigida(despesa) && !despesa.temNotaFiscal)
           motivos += Motivo.SEM_NOTA_FISCAL

● Agora os testes da T-013:

● Write(tests\reembolso\regras\RN009DuplicidadeTest.kt)
  ⎿  Wrote 173 lines to tests\reembolso\regras\RN009DuplicidadeTest.kt
       1 package reembolso.regras
       2
       3 import java.math.BigDecimal
       4 import java.text.Collator
       5 import java.util.Locale
       6 import kotlin.test.Test
       7 import kotlin.test.assertEquals
       8 import kotlin.test.assertNull
       9 import kotlin.test.assertTrue
      10 import reembolso.dominio.Lido
     … +222 lines (ctrl+o to expand)

● Bash(sed -i 's/^- \[ \] \*\*T-013\*\*/- [x] **T-013**/'
      specs/001-motor-reembolso/tasks.md && export JAVA_HOME="/c/Program
      Files/Android/Android Studio/jbr" && ./gra…)
  ⎿  Updated specs\001-motor-reembolso\tasks.md (+1 -1)
      214      `§8 despesa recusada não tem limite aplicado`
      215    - **Commit:** —
      216  
      217 -- [ ] **T-013** — Etapa 1.4 da §8: duplicidade em duas passada
          -s, só entre                                                   
      217 +- [x] **T-013** — Etapa 1.4 da §8: duplicidade em duas passada
          +s, só entre                                                   
      218    despesas sem motivo, com comparador de identificadores por c
           ode point
      219    (DT-007, R-07).
      220    - **Atende:** RN-009, RN-011, AMB-009, AMB-010, AMB-011, AMB
           -013 (fornecedor), AMB-014, AMB-017
  ⎿  … 5 more files changed
  ⎿  (timeout 5m)

● Build verde, com 16 testes na T-013. Corrijo um comentário no teste de erro de
  item, que falava de "d-2" quando o id é "d-0". Depois vêm as alterações de
  propósito.

● Update(tests\reembolso\regras\RN009DuplicidadeTest.kt)
  ⎿  Added 1 line, removed 1 line
      149  
      150      @Test
      151      fun `RN-009 AMB-017 despesa com erro de item não integra g
           rupo`() {
      152 -        // d-2 seria igual a d-1 no critério, mas não tem indi
          -cação de nota fiscal: erro de item.                           
      152 +        // d-0 seria igual a d-1 no critério, mas não tem indi
          +cação de nota fiscal: erro de item.                           
      153          val comErro = despesa(
      154              Lido.Presente("d-0"), data("2026-07-09"), Lido.Pre
           sente("alimentacao"),
      155              Lido.Presente("Bistro Central"), Lido.Presente(Big
           Decimal("54.90")), Lido.Ausente,

● Bash(export JAVA_HOME="/c/Program Files/Android/Android Studio/jbr" &&
      M=src/reembolso/dominio/Motor.kt
      SC=/c/Users/dev/AppData/Local/Temp/claude/c--Users-dev-SDD-De…)
  ⎿  5 files changed (binary, mode only or too large to show)
  ⎿  (timeout 10m)

● Todas as alterações de propósito quebraram o build, e o código foi restaurado.
  Os dois diffs, separados:

● Bash(git add -N tests/reembolso/regras/RN009DuplicidadeTest.kt && git status
      --short && echo "################ T-012 (índice) ################" && git
      diff --cached…)
  ⎿  (No output)

● As duas tasks estão prontas, mas não commitei. A sua mensagem não dizia para
  commitar direto, então deixei as duas separadas:
  - T-012 está no índice (git diff --cached);
  - T-013 está na árvore de trabalho (git diff), aplicada por cima da T-012.

  Os diffs estão acima. O ./gradlew build passa com 112 testes e nenhuma falha,
  e o checklist está limpo, sem .trim(), Double nem TODO em src/. Nada ficou sem
  decisão na spec: a RN-009 fixa a chave, quem entra no grupo e o desempate, e
  a DT-007 apenas implementa.

  Contagem de testes

  ┌───────┬───────────────────────────┬─────────────────────────┬──────────┐
  │ Task  │          Testes           │         Da task         │  A mais  │
  ├───────┼───────────────────────────┼─────────────────────────┼──────────┤
  │ T-012 │ 7                         │ 7                       │ —        │
  │       │ (RN007ListaDeMotivosTest) │                         │          │
  ├───────┼───────────────────────────┼─────────────────────────┼──────────┤
  │       │                           │ 14, incluindo os        │ 2        │
  │ T-013 │ 16 (RN009DuplicidadeTest) │ fornecedores ΟΔΟΣ e     │ (abaixo) │
  │       │                           │ Οδος                    │          │
  └───────┴───────────────────────────┴─────────────────────────┴──────────┘

  Os dois testes a mais da T-013:
  - Categoria inelegível: RN-009 duas iguais de categoria inelegível → ambas
    CATEGORIA_INELEGIVEL e nenhuma DUPLICATA.
  - Localidade: RN-009 AMB-011 d-é e d-f → d-f mantida por code point e não pela
    ordem alfabética de uma localidade.

  Os seus três pontos na T-013

  1. ΟΔΟΣ e Οδος formam grupo. O fornecedor chega à chave já normalizado pela
     RN-008 (lowercase(), mapeamento completo), e o teste usa os code points
     explícitos.
  2. Code point, e não a ordem de uma localidade. O teste só tem sentido se as
     duas ordens discordarem, então ele começa conferindo isso:
     - O Collator do português põe "d-é" antes de "d-f" e "d-006" antes de
       "D-006".
     - Por code point é o contrário, e a despesa mantida tem de ser a de "d-f".
     - Trocar o comparador pelo Collator pt-BR quebra 3 testes.
     - O teste fora do BMP faz o mesmo para String.compareTo. Ele confere que,
       por unidade UTF-16, o emoji viria antes; trocar o comparador por
       String.compareTo quebra esse teste.
  3. Categoria inelegível não integra grupo. Só entram na passada 2 as despesas
     sem motivo. Deixar entrar despesas com motivo quebra 3 testes.

  Os testes falham quando devem

  ┌───────┬──────────────────────────────────────┬─────────────────────┐
  │ Task  │              Alteração               │ Testes que falharam │
  ├───────┼──────────────────────────────────────┼─────────────────────┤
  │ T-012 │ lista ordenada ao contrário          │ 3                   │
  ├───────┼──────────────────────────────────────┼─────────────────────┤
  │ T-012 │ motivo não impede o limite           │ 13                  │
  ├───────┼──────────────────────────────────────┼─────────────────────┤
  │ T-012 │ interrompe no primeiro motivo        │ 3                   │
  ├───────┼──────────────────────────────────────┼─────────────────────┤
  │ T-013 │ String.compareTo (unidades UTF-16)   │ 1                   │
  ├───────┼──────────────────────────────────────┼─────────────────────┤
  │ T-013 │ ordem alfabética da localidade pt-BR │ 3                   │
  ├───────┼──────────────────────────────────────┼─────────────────────┤
  │ T-013 │ mantém a primeira da entrada         │ 5                   │
  ├───────┼──────────────────────────────────────┼─────────────────────┤
  │ T-013 │ despesas com motivo entram no grupo  │ 3                   │
  ├───────┼──────────────────────────────────────┼─────────────────────┤
  │ T-013 │ nota fiscal entra na chave           │ 1                   │
  ├───────┼──────────────────────────────────────┼─────────────────────┤
  │ T-013 │ data deslocada na chave              │ 2                   │
  └───────┴──────────────────────────────────────┴─────────────────────┘

  Depois de cada alteração restaurei o código, e o build voltou a passar.

  Para você saber

  - A ordenação da T-012 enfraquece testes anteriores. A lista agora é ordenada
    por Motivo.ordinal, como pedem a task e a DT-006, e a ordem da saída depende
    do enum, que o OrdemMotivosSecao8Test amarra à §8. A coleta continua na
    ordem 1.1 → 1.3, com a 1.4 na segunda passada. Os testes de ordem da T-010 e
    da T-011 continuam passando, mas uma coleta fora de ordem deixaria de
    quebrá-los, porque a ordenação corrige a saída.
  - Erro de item não integra grupo por construção. Uma despesa com erro nunca
    vira Elegibilidade, então nem chega à passada 2. O teste usa uma despesa com
    erro que seria a mantida, se entrasse no grupo, e confirma que a outra sai
    integral.
  - O Motor agora tem duas passadas (DT-007):
    - a passada 1 coleta os motivos 1.1 a 1.3 de cada despesa;
    - a passada 2 agrupa só as despesas sem motivo e marca DUPLICATA com
      mantida;
    - no fim, concluir decide entre recusada e etapa de valor, e a saída mantém
      a ordem da entrada.

  Mensagens propostas, uma por commit:
  feat(T-012): lista de motivos ordenada pela §8, recusa sem limite e erro sem
  motivos (RN-007)
  feat(T-013): etapa 1.4 da §8 — duplicidade em duas passadas com desempate por
  ponto de código (RN-009)

  Posso commitar as duas?

> Pode commitar as duas.
  Antes de commitar a T-012, acrescente um comentário no ponto onde a
  lista é ordenada:

  // A ordenação por Motivo.ordinal torna a ordem da saída independente
  // da ordem de coleta. Os testes de ordem da T-010 e da T-011 passam a
  // verificar a saída, não a coleta. A ordem da §8 continua amarrada pelo
  // OrdemMotivosSecao8Test, que lê a spec.

  E registre a observação em docs/notas-trabalho.md, na seção Diligência:
  a T-012 enfraqueceu a cobertura dos testes de ordem da T-010 e T-011, o
  agente avisou antes de eu perguntar, e a decisão foi manter a ordenação
  porque é a §8 que governa a saída.

● Para que o comentário entre no commit da T-012 e continue na T-013, sigo esta
  sequência:
  1. guardo a versão da T-013 no scratchpad;
  2. volto a árvore de trabalho à T-012 e afasto temporariamente o teste da
     T-013, que não passa sem ela;
  3. aplico o comentário, rodo o build e commito a T-012;
  4. restauro a T-013, reaplico o comentário e commito.

  Começo pela seção Diligência do notas-trabalho.

● Bash(grep -n '^## \|^### ' docs/notas-trabalho.md; sed -n '/^##
      Diligência/,/^## /p' docs/notas-trabalho.md | head -40)
  ⎿  (No output)

● Bash(SC=/c/Users/dev/AppData/Local/Temp/claude/c--Users-dev-SDD-Desafio/8aa9dd
      74-d4f2-42e8-b296-d294cb69c18d/scratchpad/t013 && mkdir -p $SC && cp
      src/reembolso/domi…)
  ⎿  Updated src\reembolso\dominio\Motor.kt (+89 -138)
  ⎿  Deleted tests\reembolso\regras\RN009DuplicidadeTest.kt (+0 -173)
  ⎿  … 1 more file changed
  ⎿  (a git step in this command can move the working tree, so these may be
     its changes, not edits) (ctrl+o to expand)

● Update(src\reembolso\dominio\Motor.kt)
  ⎿  Added 4 lines
      49          categoria = despesa.categoria,
      50          notaExigida = notaExigida(despesa),
      51          // RN-007: ordem da §8, que é a ordem de declaração de 
          Motivo (DT-006, OrdemMotivosSecao8Test).
      52 +        // A ordenação por Motivo.ordinal torna a ordem da saíd
         +a independente                                                 
      53 +        // da ordem de coleta. Os testes de ordem da T-010 e da
         + T-011 passam a                                                
      54 +        // verificar a saída, não a coleta. A ordem da §8 conti
         +nua amarrada pelo                                              
      55 +        // OrdemMotivosSecao8Test, que lê a spec.              
      56          motivos = motivos.sortedBy { it.ordinal },
      57          mantida = null,
      58      )

● Update(docs\notas-trabalho.md)
  ⎿  Added 9 lines
      23  aceito" da AMB-006. Ele apontou a imprecisão e sugeriu "os regi
          stros das
      24  AMB-001, 002, 004 e 006". Aceitei. Erro meu, pego por ele.
      25  
      26 +19/10 — T-012: a ordenação da lista de motivos por `Motivo.ordi
         +nal`, pedida                                                   
      27 +pela task e pela DT-006, enfraqueceu a cobertura dos testes de 
         +ordem da T-010                                                 
      28 +e da T-011: uma coleta fora da ordem da §8 deixa de quebrá-los,
         + porque a                                                      
      29 +ordenação corrige a saída. O agente avisou antes de eu pergunta
         +r. Decisão:                                                    
      30 +manter a ordenação, porque é a §8 que governa a saída. Os teste
         +s de ordem                                                     
      31 +passam a verificar a saída, não a coleta, e a ordem da §8 conti
         +nua amarrada                                                   
      32 +pelo OrdemMotivosSecao8Test, que lê a spec. Registrei a observa
         +ção também em                                                  
      33 +comentário no ponto onde a lista é ordenada.                   
      34 +                                                               
      35  ## Delegação
      36  18/10 — AMB-002: o agente apontou por conta própria duas incons
          istências
      37  que eu não tinha visto (o "Depende de" da RN-001 ficaria falso 
          depois da

● Bash(export JAVA_HOME="/c/Program Files/Android/Android Studio/jbr" &&
      ./gradlew build -q 2>&1 | tail -3; echo "build: $?"; grep -ho
      'tests="[0-9]*"' build/test-resu…)
  ⎿  5 files changed (binary, mode only or too large to show)
  ⎿  (timeout 5m)

● Commitei a T-012 como 5db55cc, com 96 testes no build dela sozinha. Agora
  restauro a T-013 e reaplico o comentário na versão dela de recusar.

● Bash(SC=/c/Users/dev/AppData/Local/Temp/claude/c--Users-dev-SDD-Desafio/8aa9dd
      74-d4f2-42e8-b296-d294cb69c18d/scratchpad/t013 && cp $SC/Motor.kt
      src/reembolso/dominio…)
  ⎿  Updated src\reembolso\dominio\Motor.kt (+138 -93)
        1 -package reembolso.dominio                                     
        2 -                                                              
        3 -// DT-006: ler esta função é ler a §8.                        
        4 -object Motor {                                                
        5 -    fun avaliar(lote: LoteLido): ResultadoLote {              
        6 -        // Etapa 0: validação de lote (RN-010, RN-012).       
        7 -        validarLote(lote)?.let { return it }                  
        8 -                                                              
        9 -        // Lote válido: período e relação de despesas estão pr
          -esentes e o período é composto de datas.                      
       10 -        val periodoLido = lote.periodo as PeriodoLido.Presente
       11 -        val periodo = Periodo(                                
       12 -            (periodoLido.inicio as Lido.Presente).valor,      
       13 -            (periodoLido.fim as Lido.Presente).valor,         
       14 -        )                                                     
       15 -        val elementos = (lote.despesas as DespesasLidas.Presen
          -te).elementos                                                 
       16 -                                                              
       17 -        // Etapa 0: validação de item (RN-010); etapas 1 e 2 s
          -ó para as válidas.                                            
       18 -        val despesas = validarItens(elementos).map { item ->  
       19 -            when (item) {                                     
       20 -                is ItemValidado.Invalido -> item.erro         
       21 -                is ItemValidado.Valido -> avaliarDespesa(item.
          -despesa, periodo)                                             
       22 -            }                                                 
       23 -        }                                                     
       24 -        return ResultadoLote.Avaliado(periodo, despesas, conso
          -lidar(despesas))                                              
       25 -    }                                                         
       26 -                                                              
       27 -    private fun avaliarDespesa(despesa: Despesa, periodo: Peri
          -odo): ResultadoDespesa {                                      
       28 -        // Etapa 1 (elegibilidade): todas as regras são avalia
          -das, sem interrupção, na ordem da §8.                         
       29 -        val motivos = mutableListOf<Motivo>()                 
       30 -        // 1.1 Pertinência ao período (RN-005, RN-006).       
       31 -        if (despesa.data !in periodo) motivos += Motivo.FORA_D
          -A_COMPETENCIA                                                 
       32 -        // 1.2 Elegibilidade da categoria, já normalizada (RN-
          -008).                                                         
       33 -        val categoria = Categoria.porCodigo(despesa.categoria)
       34 -        if (categoria == null) motivos += Motivo.CATEGORIA_INE
          -LEGIVEL                                                       
       35 -        // 1.3 Exigência de nota fiscal (RN-003, RN-004).     
       36 -        if (notaExigida(despesa) && !despesa.temNotaFiscal) mo
          -tivos += Motivo.SEM_NOTA_FISCAL                               
       37 -        // 1.4 entra na T-013.                                
       38 -                                                              
       39 -        // Com qualquer motivo, reembolso 0,00 e nenhuma avali
          -ação contra o limite (§8, etapa 2).                           
       40 -        if (motivos.isNotEmpty() || categoria == null) return 
          -recusar(despesa, motivos)                                     
     … 191 more lines (ctrl+o to expand)
  ⎿  Created tests\reembolso\regras\RN009DuplicidadeTest.kt (+173 -0)
        1 +package reembolso.regras                                      
        2 +                                                              
        3 +import java.math.BigDecimal                                   
        4 +import java.text.Collator                                     
        5 +import java.util.Locale                                       
        6 +import kotlin.test.Test                                       
        7 +import kotlin.test.assertEquals                               
        8 +import kotlin.test.assertNull                                 
        9 +import kotlin.test.assertTrue                                 
       10 +import reembolso.dominio.Lido                                 
       11 +import reembolso.dominio.Motivo                               
       12 +import reembolso.dominio.ResultadoDespesa                     
       13 +import reembolso.dominio.Valor                                
       14 +                                                              
       15 +class RN009DuplicidadeTest {                                  
       16 +                                                              
       17 +    private fun cp(vararg codePoints: Int) = String(codePoints
          +, 0, codePoints.size)                                         
       18 +                                                              
       19 +    // Despesa igual a d-006 em tudo que integra o critério, s
          +alvo o que for passado.                                       
       20 +    private fun igual(                                        
       21 +        id: String,                                           
       22 +        data: String = "2026-07-09",                          
       23 +        categoria: String = "alimentacao",                    
       24 +        fornecedor: String = "Bistro Central",                
       25 +        valor: String = "54.90",                              
       26 +        nf: Boolean = true,                                   
       27 +    ) = despesa(id, data, categoria, fornecedor, valor, nf)   
       28 +                                                              
       29 +    private fun assertDuplicata(resultados: List<ResultadoDesp
          +esa>, recusada: String, mantida: String) {                    
       30 +        val r = resultados.recusada(recusada)                 
       31 +        assertEquals(listOf(Motivo.DUPLICATA), r.motivos)     
       32 +        assertEquals(mantida, r.mantida)                      
       33 +        resultados.aceita(mantida)                            
       34 +    }                                                         
       35 +                                                              
       36 +    private fun semDuplicata(resultados: List<ResultadoDespesa
          +>) =                                                          
       37 +        resultados.none { it is ResultadoDespesa.Recusada && M
          +otivo.DUPLICATA in it.motivos }                               
       38 +                                                              
       39 +    @Test                                                     
       40 +    fun `RN-009 AMB-011 d-006 e d-007 → d-006 integral e d-007
          + recusada com DUPLICATA mantida d-006`() {                    
     … 133 more lines (ctrl+o to expand)
  ⎿  … 1 more file changed

● Update(src\reembolso\dominio\Motor.kt)
  ⎿  Added 4 lines
       98          categoria = avaliacao.despesa.categoria,
       99          notaExigida = notaExigida(avaliacao.despesa),
      100          // RN-007: ordem da §8, que é a ordem de declaração de
            Motivo (DT-006, OrdemMotivosSecao8Test).
      101 +        // A ordenação por Motivo.ordinal torna a ordem da saí
          +da independente                                               
      102 +        // da ordem de coleta. Os testes de ordem da T-010 e d
          +a T-011 passam a                                              
      103 +        // verificar a saída, não a coleta. A ordem da §8 cont
          +inua amarrada pelo                                            
      104 +        // OrdemMotivosSecao8Test, que lê a spec.             
      105          motivos = avaliacao.motivos.sortedBy { it.ordinal },
      106          mantida = avaliacao.mantida,
      107      )
● Bash(git diff -- docs/notas-trabalho.md; export JAVA_HOME="/c/Program
      Files/Android/Android Studio/jbr" && ./gradlew build -q 2>&1 | tail -3;
      echo "build: $?"; grep…)
  ⎿  5 files changed (binary, mode only or too large to show)
  ⎿  (timeout 5m)

● Bash(git commit -q -F - <<'EOF'
      feat(T-013): etapa 1.4 da §8 — duplicidade em duas passadas com desempate
      por ponto de código (RN-009)…)
  ⎿  (No output)

● Commitei as duas tasks na main, cada uma com a sua task marcada [x]. Não fiz
  push.

  ┌─────────────────────┬───────────────────────────┐
  │       Commit        │   Build daquele commit    │
  ├─────────────────────┼───────────────────────────┤
  │ 5db55cc feat(T-012) │ 96 testes, nenhuma falha  │
  ├─────────────────────┼───────────────────────────┤
  │ 8d6c8b6 feat(T-013) │ 112 testes, nenhuma falha │
  └─────────────────────┴───────────────────────────┘

  - O comentário que você ditou está no ponto onde a lista é ordenada, nos dois
    commits. A T-013 reescreveu a função recusar, então reapliquei o comentário
    na versão nova dela e conferi que ele existe no Motor.kt de cada um.
  - A T-012 foi commitada e testada sozinha. Afastei temporariamente o teste da
    T-013, rodei o build, commitei e depois restaurei os arquivos da T-013.
    Assim, cada commit tem um build verde próprio.
  - No docs/notas-trabalho.md, escrevi a entrada na seção Diligência, com data
    de 19/10. Ela registra os três pontos que você pediu: a ordenação
    enfraqueceu os testes de ordem da T-010 e da T-011; eu avisei antes de você
    perguntar; e a ordenação foi mantida porque é a §8 que governa a saída. Não
    commitei o arquivo, porque você disse que ele vai em commit próprio.
  - Há outra alteração no mesmo arquivo: uma entrada "Correções pós-revisão",
    sobre o A5, na seção "Erros do agente". Ela não é minha e eu não toquei
    nela. As duas vão juntas no seu commit.

  Com isso, a etapa 1 da §8 está completa. A próxima é a T-014, o consolidado do
  lote. Ela fecha as contagens e pode atualizar o comentário que ficou
  desatualizado em parte.

> Commite o docs/notas-trabalho.md com as duas entradas (a da Diligência
  e a dos Erros do agente sobre o A5):

  docs: notas de diligencia — ordenacao da T-012 e correcao A5

  Depois git push.

● Bash(git status --short && git diff --stat && git add docs/notas-trabalho.md
      && git commit -q -F - <<'EOF'
      docs: notas de diligencia — ordenacao da T-012 e correcao…)
  ⎿  (No output)

● Commitei o docs/notas-trabalho.md como 8c73e05, com as duas entradas e a
  mensagem que você deu, e fiz o push. A main local e a origin/main estão
  iguais.

  O push levou, junto com as notas, os três commits que estavam só na sua
  máquina: a T-011, a T-012 e a T-013 (97504e0..8c73e05).

> Vamos absorver o envelope lacrado. Nada de código ainda.

  Leia exemplos/envelope/00-ENVELOPE-LACRADO.md e os quatro arquivos que
  o acompanham (politica-v4.json, cambio.json, despesas-envelope.json,
  despesas-envelope-cc-desconhecido.json).

  Antes de propor qualquer mudança, me responda três coisas:

  1. O que a mudança quebra na spec atual. Quais RN deixam de valer, quais
     AMB foram invalidadas, quais seções precisam mudar. Seja específico:
     cite os números.
  2. Quais ambiguidades novas o comunicado traz. O próprio envelope avisa
     que traz várias e aponta duas frases: "aplica-se a política padrão" e
     "a taxa da data da despesa". Não decida nenhuma — liste o que está em
     aberto e, para cada uma, as leituras possíveis e o que muda no
     resultado em cada leitura. Use os dois arquivos de despesas como
     fonte de casos concretos.
  3. O que a arquitetura atual absorve sem mudança e o que resiste. Quero
     isso por escrito agora, antes de mexer, porque vai para o relatório.

  Um ponto que eu já vi e quero sua análise: o despesas-envelope.json tem
  uma despesa em GBP (e-006), e o cambio.json só tem USD e EUR. Isso é
  caso da ambiguidade de "taxa da data da despesa" ou é outra coisa?

  Não escreva na spec ainda. Só a análise.

● Bash(ls -la exemplos/envelope/ && git status --short && git log --oneline -1
      -- exemplos/envelope; cat exemplos/envelope/00-ENVELOPE-LACRADO.md)
  ⎿  Error: Exit code 1
     ls: cannot access 'exemplos/envelope/': No such file or directory
     cat: exemplos/envelope/00-ENVELOPE-LACRADO.md: No such file or directory

  Searched for 1 pattern (ctrl+o to expand)

● Bash(cd /c/Users/dev/SDD-Desafio && find . -path ./sdd-desafio/.git -prune -o
      -type f -print | grep -iv '/build/\|/\.gradle/\|/\.kotlin/' | grep -iE
      'envelope|politi…)
  ⎿  (No output)

● Bash(for d in ~/Downloads ~/Desktop ~/Documents ~/OneDrive; do [ -d "$d" ] &&
      find "$d" -maxdepth 4 -iname '*envelope*' -o -maxdepth 4 -iname
      'politica-v4*' -o -maxd…)
  ⎿  (No output)

● Não consegui fazer a análise: os arquivos do envelope não estão nesta máquina.

  Procurei de três formas:
  - No caminho indicado: exemplos/envelope/ não existe. Em exemplos/ só há o
    despesas-exemplo.json.
  - No projeto inteiro e no GitHub: nenhum arquivo com envelope, politica-v4,
    cambio ou lacrado no nome, nem na árvore local nem na origin/main (fiz fetch
    antes).
  - Nas pastas comuns do usuário: Downloads, Desktop, Documents e OneDrive
    também não têm nada com esses nomes.

  Não vou responder às três perguntas sem os arquivos. Qualquer coisa que eu
  dissesse sobre o que muda, as ambiguidades ou os casos seria inventada.

  Sobre o GBP, só uma leitura preliminar, a partir do que você descreveu: se o
  cambio.json não tem nenhuma cotação de GBP, o problema não parece ser da frase
  "a taxa da data da despesa". Essa frase pergunta qual data usar para uma
  moeda que tem cotações. A e-006 levanta outra questão: o que fazer quando a
  moeda não tem cotação nenhuma, em data alguma. Isso pede uma decisão separada,
  provavelmente entre erro de item, erro de lote ou recusa. Mas só confirmo
  depois de ler o comunicado, porque ele pode dizer algo sobre moedas não
  cobertas.

  Para seguir: coloque os cinco arquivos em exemplos/envelope/ ou me diga onde
  estão, e eu faço a análise completa sem escrever na spec.

  Um cuidado antes disso: o .gitignore exclui instrutor/, com o aviso de que o
  envelope do Dia 2 não pode ser commitado. Se os arquivos vierem dessa pasta,
  confirme se podem entrar em exemplos/envelope/ e ser versionados. Não vou
  commitar nada deles sem a sua confirmação.

> Os arquivos do envelope estão neste gist:
  https://gist.github.com/Sassine/c9c7d72a4aae51306752c4f9f87a32dc

  Clone e copie para exemplos/envelope/:

  git clone
  https://gist.github.com/Sassine/c9c7d72a4aae51306752c4f9f87a32dc.git
  ../envelope-tmp

  São cinco arquivos: 00-ENVELOPE-LACRADO.md, politica-v4.json,
  cambio.json, despesas-envelope.json e despesas-envelope-cc-desconhecido.json.
  Copie os cinco para exemplos/envelope/ sem alterar nada — nem
  formatação, nem acentuação. Apague a pasta temporária depois.

  Podem e devem ser commitados: o próprio comunicado diz "guarde no seu
  repositório (sugestão: exemplos/envelope/), commitando junto — eles
  fazem parte da entrega". O .gitignore de instrutor/ vale para antes do
  Dia 2.

  Commite:
  docs(envelope): arquivos do envelope lacrado do Dia 2

  Depois faça a análise que pedi: o que quebra na spec (cite RN e AMB por
  número), quais ambiguidades novas o comunicado traz com as leituras
  possíveis e o que muda no resultado em cada uma, e o que a arquitetura
  absorve sem mudança e o que resiste. Use os dois arquivos de despesas
  como fonte de casos concretos. Nada de spec ainda — só a análise.

● Bash(git clone -q
      https://gist.github.com/Sassine/c9c7d72a4aae51306752c4f9f87a32dc.git
      envelope-tmp 2>&1; ls -la envelope-tmp; git -C envelope-tmp log --oneline
      | he…)
  ⎿  (No output)

● O clone trouxe os cinco arquivos esperados. Como o core.autocrlf=true pode ter
  alterado as quebras de linha, vou copiar o conteúdo direto dos blobs do gist
  com git show, byte a byte, e conferir os hashes.

● Bash(mkdir -p sdd-desafio/exemplos/envelope && for f in 00-ENVELOPE-LACRADO.md
      politica-v4.json cambio.json despesas-envelope.json
      despesas-envelope-cc-desconhecido.…)
  ⎿  (No output)

● Os cinco arquivos são idênticos aos do gist (mesmo hash de blob). Leio todos
  antes de commitar:

● Bash(cd /c/Users/dev/SDD-Desafio/sdd-desafio/exemplos/envelope && for f in
      00-ENVELOPE-LACRADO.md politica-v4.json cambio.json despesas-envelope.json
      despesas-envelo…)
  ⎿  (No output)

● Li os cinco: são texto e dados, sem nada executável. Commito e apago a pasta
  temporária.

● Bash(cd /c/Users/dev/SDD-Desafio/sdd-desafio && git status --short && git add
      exemplos/envelope && git status --short && git commit -q -F - <<'EOF'
      docs(envelope): a…)
  ⎿  (No output)

● Commitei o envelope como 2aed7d1 — os hashes dos cinco arquivos conferem com o
  gist e já apaguei a pasta temporária. Agora vou verificar alguns pontos na
  spec, no plan e no lote do Dia 1, começando pelo centro de custo em
  despesas-exemplo.json.

● Bash(head -12 exemplos/despesas-exemplo.json;
      S=specs/001-motor-reembolso/spec.md; sed -n '/^### AMB-001/,/^\*\*O que
      não/p;/^### AMB-007/,/^\*\*Regra afetada/p' $S…)
  ⎿  (No output)

● A análise está abaixo. Antes, o commit:

  2aed7d1 docs(envelope) traz os cinco arquivos, copiados byte a byte dos blobs
  do gist. Os hashes conferem com o gist, e a pasta temporária foi apagada. Não
  escrevi nada na spec.

  Achado principal, antes das três respostas

  O lote do Dia 1 (despesas-exemplo.json) é do centro de custo
  CC-ENG-PLATAFORMA. A v4 vale para a "competência atual", que é julho de 2026,
  a mesma desse lote. Com a tabela desse centro (alimentação 75, transporte 80,
  hospedagem 0, "não reembolsável"), a §9 deixa de valer:

  ┌────────────────────┬───────────────────┬────────────────────────────────┐
  │      Despesa       │  v3 (spec atual)  │     v4, CC-ENG-PLATAFORMA      │
  ├────────────────────┼───────────────────┼────────────────────────────────┤
  │ d-001 alimentação  │ 60,00 parcial     │ 72,50 integral                 │
  │ 72,50              │                   │                                │
  ├────────────────────┼───────────────────┼────────────────────────────────┤
  │ d-014 ALIMENTACAO  │ 60,00 parcial     │ 61,00 integral                 │
  │ 61,00              │                   │                                │
  ├────────────────────┼───────────────────┼────────────────────────────────┤
  │ d-010 hospedagem   │ 250,00 parcial    │ 0,00 (classificação em aberto, │
  │ 480,00 com NF      │                   │  ver P6)                       │
  ├────────────────────┼───────────────────┼────────────────────────────────┤
  │ d-013 hospedagem   │ [SEM_NOTA_FISCAL] │ talvez [CATEGORIA_INELEGIVEL,  │
  │ 690,00 sem NF      │                   │ SEM_NOTA_FISCAL]               │
  ├────────────────────┼───────────────────┼────────────────────────────────┤
  │ Total              │ 623,43            │ 386,93                         │
  └────────────────────┴───────────────────┴────────────────────────────────┘

  As demais despesas não mudam.

  ---

  1. O que quebra na spec

  Seções
  - §3 (Fora de escopo): a frase "não atribui efeito a […] centro de custo"
    deixa de valer. O item sobre viagem precisa ser revisto (V1 abaixo).
  - §4.1 (Entrada):
    - O centro de custo está como "não obrigatório; sem efeito no cálculo", e
      agora decide os limites.
    - Falta o campo moeda na tabela de despesa.
    - Política e câmbio passam a ser entradas.
    - O ponto mais grave é a regra "informações não previstas são aceitas e
      ignoradas". Aplicada à v4, ela faria o motor pagar e-002 (22,00 EUR) como
      R$ 22,00, em silêncio.
  - §4.2 (Saída): o "valor considerado" passa a ser um valor convertido. Para o
    auditor verificar, a saída precisaria mostrar a moeda e o valor originais e
    a taxa usada.
  - §7: as linhas que usam despesas do lote de exemplo mudam de valor: d-001,
    d-014, d-010 e d-013. As linhas genéricas, como "alimentação 61,00", só
    continuam valendo se a política aplicada for a padrão.
  - §8, etapa 0: a conversão de moeda precisa de um lugar na ordem, em relação à
    normalização (RN-011) e à validação (RN-010).
  - §9: caem o total de 623,43 e as contagens 4/4/5/1.
  - §10: a pendência sobre viagem muda de natureza.

  Regras

  ┌────────────┬────────────────────────────────────────────────────────────┐
  │     RN     │                        O que quebra                        │
  ├────────────┼────────────────────────────────────────────────────────────┤
  │            │ Os limites estão fixados no texto (60/80/250) e agora vêm  │
  │ RN-001     │ de uma tabela externa, por centro de custo. Os aceites de  │
  │            │ d-001 e d-010 deixam de valer.                             │
  ├────────────┼────────────────────────────────────────────────────────────┤
  │            │ "Limite nominal conforme a RN-001": muda de onde vem o     │
  │ RN-002     │ limite. Os aceites genéricos (60,00 / 60,01) só se mantêm  │
  │            │ sob a política padrão.                                     │
  ├────────────┼────────────────────────────────────────────────────────────┤
  │            │ O limiar de 100 passa a estar no arquivo                   │
  │ RN-003     │ (nota_fiscal_obrigatoria_acima_de). Falta decidir em que   │
  │            │ moeda ele é comparado (T6).                                │
  ├────────────┼────────────────────────────────────────────────────────────┤
  │            │ "Exatamente três categorias elegíveis" deixa de valer: o   │
  │ RN-008     │ conjunto passa a depender do centro de custo, com          │
  │            │ representacao no CC-COMERCIAL. O aceite de d-014 (60,00    │
  │            │ parcial) também cai.                                       │
  ├────────────┼────────────────────────────────────────────────────────────┤
  │ RN-009     │ "Valor normalizado": em BRL ou na moeda original?          │
  ├────────────┼────────────────────────────────────────────────────────────┤
  │ RN-010     │ Faltam a validação da moeda, a obrigatoriedade do centro   │
  │            │ de custo e o tratamento de política ou câmbio ilegíveis.   │
  ├────────────┼────────────────────────────────────────────────────────────┤
  │ RN-011     │ Falta a ordem entre normalizar e converter (T4).           │
  ├────────────┼────────────────────────────────────────────────────────────┤
  │ RN-004,    │ O texto continua válido. A RN-007 e a §8 só mudam se       │
  │ 005, 006,  │ surgir motivo novo (P6, T3).                               │
  │ 007, 012   │                                                            │
  └────────────┴────────────────────────────────────────────────────────────┘

  Ambiguidades

  ┌────────────┬────────────────────────────────────────────────────────────┐
  │    AMB     │                          Situação                          │
  ├────────────┼────────────────────────────────────────────────────────────┤
  │ AMB-013    │ Invalidada no ponto "exatamente três". A lista de          │
  │            │ vocabulário da §4.2 (Notação) ganha representacao.         │
  ├────────────┼────────────────────────────────────────────────────────────┤
  │            │ Invalidada para o centro de custo. Além disso, o campo     │
  │ AMB-015    │ moeda não pode cair na regra "campo opcional com tipo      │
  │            │ incompatível é ausente" (T8).                              │
  ├────────────┼────────────────────────────────────────────────────────────┤
  │            │ Precisa ser revista: o comunicado liga viagem a moeda      │
  │ AMB-007    │ estrangeira, e o arquivo traz                              │
  │            │ acrescimo_em_viagem_percentual: 50.                        │
  ├────────────┼────────────────────────────────────────────────────────────┤
  │ AMB-001    │ Reaberta em parte: o arquivo distingue periodicidade "dia" │
  │            │  de "diaria" (A1 abaixo).                                  │
  ├────────────┼────────────────────────────────────────────────────────────┤
  │ AMB-006,   │ Ganham a dimensão da moeda: a nota fiscal e o              │
  │ AMB-014    │ arredondamento depois da conversão.                        │
  ├────────────┼────────────────────────────────────────────────────────────┤
  │            │ A decisão se mantém (uma diária por lançamento), mas as    │
  │ AMB-009    │ consequências sobre d-010 caem. A e-007 ("3 noites") volta │
  │            │  a pôr a decisão à prova.                                  │
  └────────────┴────────────────────────────────────────────────────────────┘

  ---

  2. Ambiguidades novas, sem decidir nenhuma

  Valores convertidos com as taxas do cambio.json. CC-COMERCIAL: alimentação 90,
  transporte 150, hospedagem 400, representação 300. Política padrão:
  60/80/250.

  "Aplica-se a política padrão"

  #: P1
  Em aberto: O que é a "política padrão"
  Leituras: (a) o bloco padrao do arquivo; (b) a v3 fixa no código
  O que muda, com casos: Hoje os números são iguais (60/80/250). Divergem quando

  o financeiro editar o arquivo, o que pode acontecer "sem aviso".
  ────────────────────────────────────────
  #: P2
  Em aberto: A política padrão vale só para centro de custo ausente da tabela,
  ou
  também para categoria ausente num centro listado?
  Leituras: (a) só centro ausente; (b) também categoria ausente
  O que muda, com casos: CC-ADM não tem hospedagem. Uma hospedagem de 200,00 com

  NF daria 200,00 integral em (b) e CATEGORIA_INELEGIVEL em (a). Nenhum arquivo
   do envelope tem esse caso.
  ────────────────────────────────────────
  #: P3
  Em aberto: Centro de custo ausente, nulo ou de tipo incompatível no lote
  Leituras: (a) política padrão; (b) erro de lote
  O que muda, com casos: Os dois arquivos trazem o centro de custo. Todos os
  testes atuais constroem lotes sem centro de custo: em (a) eles continuam
  valendo, em (b) passariam a rejeitar o lote.
  ────────────────────────────────────────
  #: P4
  Em aberto: Como comparar o código do centro de custo (caixa, espaços)
  Leituras: (a) exato; (b) normalizado como na RN-008
  O que muda, com casos: "cc-comercial" cairia, em silêncio, na política padrão
  em (a): limites menores, sem nenhum aviso.
  ────────────────────────────────────────
  #: P5
  Em aberto: representacao sob a política padrão
  Leituras: (a) inelegível; (b) elegível sem limite
  O que muda, com casos: f-003 (190,00 BRL com NF): (a) 0,00 recusada; (b)
  190,00.
  ────────────────────────────────────────
  #: P6
  Em aberto: Limite 0,00 "não reembolsável" (hospedagem no CC-ENG-PLATAFORMA)
  Leituras: (a) recusa com CATEGORIA_INELEGIVEL; (b) recusa com um motivo novo;
  (c) parcial com reembolso 0,00
  O que muda, com casos: Afeta d-010 do lote do Dia 1: (a) e (b) dão 6 recusadas

  e 1 parcial; (c) dá 5 recusadas e 2 parciais, sendo uma "parcial" que não
  paga nada. Afeta também a lista de motivos de d-013.

  "A taxa da data da despesa"

  #: T1
  Em aberto: Data sem cotação (fim de semana ou feriado; o arquivo avisa "apenas

  dias úteis")
  Leituras: (a) último dia útil anterior; (b) próximo dia útil; (c) erro de
  item;
  (d) recusa
  O que muda, com casos: e-004 (30,00 EUR, sábado 18/07): (a) 178,80 → 90,00
  parcial, excedente 88,80; (b) 180,30 → 90,00 parcial, excedente 90,30; (c) e
  (d) 0,00. O reembolso é igual em (a) e (b), só o excedente muda.
  ────────────────────────────────────────
  #: T2
  Em aberto: Data fora da faixa da tabela (13/07 a 28/07; o período vai de 01/07

  a 31/07)
  Leituras: as mesmas de T1, mas (a) falha para datas de 01/07 a 10/07 e (b)
  falha para 29/07 a 31/07
  O que muda, com casos: Nenhuma despesa do envelope cai aí; é um caso
  hipotético.
  ────────────────────────────────────────
  #: T4
  Em aberto: Ordem entre normalizar (RN-011) e converter
  Leituras: (a) converter o valor cru e arredondar uma vez; (b) arredondar,
  converter e arredondar de novo
  O que muda, com casos: Todos os casos do envelope dão valores exatos em
  centavos (22,00 × 5,93 = 130,46…), então nada muda aqui. Um valor como 33,333
   EUR já divergiria.
  ────────────────────────────────────────
  #: T6
  Em aberto: Moeda do limiar da nota fiscal
  Leituras: (a) valor convertido para BRL; (b) valor na moeda original
  O que muda, com casos: e-005 (40,00 USD sem NF, igual a 220,00 BRL): (a) nota
  exigida e ausente → 0,00 recusada; (b) nota não exigida → 150,00 parcial. É o
   caso de maior impacto.
  ────────────────────────────────────────
  #: T7
  Em aberto: Chave de duplicidade
  Leituras: (a) valor em BRL; (b) valor e moeda originais
  O que muda, com casos: Nenhum par no envelope. Muda quando uma despesa em BRL
  e
  outra em EUR coincidem depois da conversão.
  ────────────────────────────────────────
  #: T8
  Em aberto: moeda nula, vazia, em minúsculas ("eur") ou inválida
  Leituras: (a) tratar como ausente, logo BRL (regra atual da AMB-015 para campo

  opcional); (b) erro de item
  O que muda, com casos: Em (a), um USD mal digitado vira BRL em silêncio.

  Sua pergunta sobre o GBP (e-006): não é caso da ambiguidade "taxa da data da
  despesa". É outra ambiguidade (T3: moeda sem cotação).
  - Em 21/07 há cotação, só que não de GBP. Nenhuma das leituras de T1 resolve o
    caso, porque não existe cotação de GBP em data nenhuma.
  - As duas questões se tocam numa situação só: quando uma moeda tem cotação em
    algumas datas e não na da despesa. Isso cai em T1/T2.
  - Leituras para a e-006 (55,00 GBP, representação):
    - (a) erro de item, com uma causa nova "moeda sem cotação": 0,00, conta como
      erro;
    - (b) recusa com um motivo novo: 0,00, conta como recusada;
    - (c) erro de lote: todo o lote do Rafael é rejeitado.
  - O reembolso da e-006 é 0,00 em todas as leituras razoáveis. O que muda é a
    classificação, o consolidado e, em (c), o lote inteiro.

  Ambiguidades que o envelope não aponta

  #: V1
  Em aberto: Moeda estrangeira caracteriza viagem? O comunicado diz
  "colaboradores em viagem internacional lançam despesas em moeda estrangeira",
   e o arquivo traz o acréscimo de 50%.
  O que muda: Leituras: (a) manter a AMB-007; (b) viagem por despesa em moeda
  estrangeira; (c) viagem no período inteiro se houver alguma.<br>• e-002:
  90,00 em (a) contra 130,46 integral em (b) e (c).<br>• e-004: 90,00 contra
  135,00.<br>• e-007 (hotel em Londres pago em BRL): 400,00 em (a) e (b),
  600,00 em (c). Em (c) ela passaria de 500 e entraria na fila do item C.
  ────────────────────────────────────────
  #: A1
  Em aberto: A periodicidade "dia" reabre a AMB-001 (limite por despesa ou por
  dia)?
  O que muda: O envelope não tem duas despesas da mesma categoria no mesmo dia.
  No lote do Dia 1, d-001 e d-002 (03/07) dariam 110,50 por despesa contra
  75,00 por dia sob o CC-ENG.
  ────────────────────────────────────────
  #: R1
  Em aberto: Vigência retroativa
  O que muda: Um lote de competência anterior a 07/2026 usa a v3 ou a v4? O
  motor
  precisaria manter as duas políticas?
  ────────────────────────────────────────
  #: F1
  Em aberto: Política ou câmbio ilegível: arquivo ausente, malformado, limite
  negativo
  O que muda: O plan §4 descartou usar arquivo de configuração justamente por
  esses modos de falha não definidos. Agora eles são obrigatórios.
  ────────────────────────────────────────
  #: C1
  Em aberto: Item C, opcional: "reembolsável passe de R$ 500"
  O que muda: Fica em aberto se o limite é estrito, se a pendência entra no
  total
  e qual é o estado novo. No envelope, nenhuma despesa passa de 500, salvo
  e-007 na leitura V1(c).

  Resultado do lote do Rafael numa leitura de referência, só para comparar, sem
  decidir nada:
  - leituras adotadas: limiar da nota em BRL, sem ampliação por viagem, e-004
    pelo dia útil anterior, e-006 como erro;
  - resultado por despesa: e-001 300,00 · e-002 90,00 · e-003 85,26 · e-004
    90,00 · e-005 0,00 · e-006 erro · e-007 400,00 · e-008 90,00 · e-009
    recusada · e-010 88,00;
  - total 1.143,26.

  Lote da Dani (política padrão): f-001 58,00 · f-002 250,00 · f-003 0,00 (P5a)
  · f-004 65,76 → total 373,76.

  ---

  3. O que a arquitetura absorve e o que resiste

  Absorve sem mudança, ou com uma linha:
  - A ordem da §8 no Motor, a coleta de motivos, a duplicidade em duas passadas,
    aparar(), a validação de lote, a identidade do item e o
    OrdemMotivosSecao8Test. Nada disso depende de onde vem o limite. Um motivo
    novo (P6b ou T3b) é uma entrada no enum mais uma linha na §8, e o teste de
    ordem obriga as duas a andarem juntas.
  - Um ponto só para o limite: limiteAplicavel(categoria). O plan §4 desenhou
    isso para esta situação: "uma ampliação condicional altera essa função e
    mais nada". Muda a assinatura, para algo como (categoria, centroDeCusto), e
    ela continua sendo o único ponto.
  - A fronteira do domínio (DT-009). O domínio não lê arquivos. Política e
    câmbio entram por adaptadores JSON novos, como a entrada, e o domínio recebe
    valores prontos. A regra continua testável sem JSON.
  - Valor e a RN-011. A conversão é uma multiplicação seguida de normalizar, e o
    arredondamento continua num lugar só.
  - Categoria como texto. Despesa.categoria e os resultados já usam String, e
    não o enum, por causa do "coworking". "representacao" entra sem mexer no
    modelo de resultado.
  - A saída, a entrada e a CLI ainda não foram implementadas (T-017 a T-019). O
    esquema de saída e o adaptador nascem já com moeda e centro de custo, sem
    retrabalho.

  Resiste:
  1. Politica.kt. O enum Categoria, com limites fixos e exatamente três valores,
     não consegue representar um conjunto de categorias por centro de custo,
     vindo de um arquivo que "muda sem aviso". Vira dado, um mapa vindo do
     adaptador. O Motor usa Categoria.porCodigo na etapa 1.2 e na etapa 2. É a
     mudança estrutural real, e contradiz a decisão do plan §4 ("constante, não
     arquivo de configuração").
  2. A interface da CLI é fixa (calcular --input X --output Y, pelo DESAFIO).
     Ela não diz de onde vêm politica-v4.json e cambio.json. As opções seriam
     flags novas, um caminho convencional ou uma variável de ambiente. É uma
     decisão do plan, mas esbarra numa restrição do enunciado.
  3. AMB-015 "por construção". O plan fez o adaptador não ler o centro de custo,
     para que ele não tivesse efeito. Agora ele precisa ser lido. Os testes da
     T-017 previstos para a AMB-015, como "lotes que diferem só no centro de
     custo → resultados idênticos", se invertem.
  4. A regra "campo não previsto é ignorado". Ela é o oposto do que a moeda
     precisa: aplicada a esse campo, faria o motor errar em silêncio.
  5. Os testes que dependem de constantes. Os testes das RN-001, 002, 003 e 008
     constroem lotes sem centro de custo e esperam 60/80/250. Eles sobrevivem
     sem alteração se a leitura P3(a) for adotada, porque lote sem centro de
     custo cai na política padrão, que hoje tem os mesmos números. Com P3(b),
     precisam todos de um centro de custo.
  6. §9 e T-020. O critério de ponta a ponta (623,43) cai antes mesmo de ser
     implementado, porque o lote do Dia 1 muda de política.

  ---

  Se quiser, salvo esta análise em docs/ (por exemplo, docs/analise-envelope.md)
  para usar no relatório.

> Suba um subagent (general-purpose) com esta tarefa:

  Leia docs/notas-trabalho.md, specs/001-motor-reembolso/DECISIONS.md,
  specs/001-motor-reembolso/spec.md, o git log completo e a análise do
  envelope que está nesta sessão. Preencha docs/RELATORIO.md seguindo o
  template, nas seções Delegação, Descrição, Discernimento, Diligência e
  Envelope.

  Regras:
  - Cada afirmação precisa de evidência no repositório: hash de commit,
    arquivo, número de RN ou AMB. Nada genérico.
  - Não invente. Se faltar material para uma seção, escreva
    [FALTA: o que você precisa saber] e siga.
  - Use os casos reais: o erro do agente sobre AMB-004/AMB-006 e d-004, as
    três recusas dele a redações minhas (AMB-015, RN-012, A5), a revisão
    por subagente, o bug do notas-trabalho.md commitado vazio.
  - Escreva em primeira pessoa, como o Sandro escreveria. Não suavize os
    erros.

  Salve e me avise. Não commite.

● O subagente não enxerga esta sessão. Por isso, primeiro salvo no scratchpad a
  análise do envelope e um resumo factual do que aconteceu nesta sessão e não
  está no repositório, e passo os dois caminhos para ele.

  Made 2 scratchpad edits +132 (ctrl+o to expand)

● Agent(Preencher RELATORIO.md)
  ⎿  Done (46 tool uses · 154.9k tokens · 7m 51s)
  (ctrl+o to expand)

> Bloco de decisões do envelope, aprovado. Escreva na spec v2 com estas
  17 decisões, mantendo o formato das AMB existentes (Texto original,
  O que não está claro, Decisão, Justificativa, Custo aceito,
  Consequências verificáveis, Regra afetada). Numere a partir de AMB-022.

  P1 — "Política padrão" é o bloco `padrao` do arquivo, não a v3 no código.
       A v4 determina que o motor leia a política de fora; manter os
       números no código recriaria o acoplamento que ela remove.

  P2 — A política padrão aplica-se apenas quando o centro de custo não
       tem entrada na tabela. Categoria ausente num centro listado é
       omissão deliberada do financeiro, não lacuna a preencher: a
       categoria é inelegível para aquele centro.

  P3 — Lote sem centro de custo, com centro nulo ou de tipo incompatível
       usa a política padrão. Custo aceito: um lote sem centro de custo é
       avaliado sem aviso, em vez de rejeitado.

  P4 — O código do centro de custo é comparado exatamente, sem conversão
       de caixa nem aparar. É identificador de sistema, não texto digitado
       por humano. Custo aceito: "cc-comercial" cai na política padrão em
       silêncio.

  P5 — Decorre de P2: `representacao` é inelegível sob a política padrão.
       f-003 (190,00 BRL com NF) é recusada por CATEGORIA_INELEGIVEL.

  P6 — Limite 0,00 com observação "nao reembolsavel" é proibição, não
       limite: a despesa é recusada por CATEGORIA_INELEGIVEL. Classificar
       como parcial de 0,00 produziria uma saída que diz "aceita" sobre
       uma despesa que não paga nada.

  T1 — Data sem cotação usa a cotação do último dia útil anterior
       disponível. Cotação futura não existe no momento do lançamento.

  T2 — Data fora da faixa da tabela segue a mesma regra de T1. Se não
       houver nenhuma cotação anterior para a moeda, é erro de item.

  T3 — Moeda sem cotação em data alguma é erro de item, com causa nova
       "moeda sem cotação". É dado que o motor não consegue processar, não
       decisão de negócio. e-006 (55,00 GBP) é erro.

  T4 — A conversão multiplica o valor cru pela taxa e normaliza uma vez
       (RN-011, HALF_UP escala 2). Arredondar antes e depois acumularia
       erro.

  T6 — O limiar de nota fiscal compara o valor já convertido para BRL. A
       política é em BRL; comparar 40,00 USD contra 100,00 BRL seria
       comparar unidades diferentes. e-005 (40,00 USD = 220,00 BRL, sem
       NF) é recusada por SEM_NOTA_FISCAL.

  T7 — A chave de duplicidade da RN-009 usa o valor em BRL, pela mesma
       razão de T6.

  T8 — Campo `moeda` nulo, vazio, de tipo incompatível ou com código que
       não é ISO 4217 de três letras maiúsculas é erro de item. Esta
       decisão abre exceção explícita à AMB-015: `moeda` tem efeito no
       cálculo, e tratá-la como ausente faria o motor pagar em BRL uma
       despesa lançada em outra moeda.

  V1 — A AMB-007 é mantida: viagem continua fora de escopo e o campo
       `acrescimo_em_viagem_percentual` do arquivo é ignorado. O
       comunicado não define o que caracteriza viagem, e inferir viagem
       de moeda estrangeira seria o motor adivinhando. Custo aceito: um
       colaborador em viagem real não recebe a ampliação de 50%.

  A1 — A AMB-001 é mantida: o limite é por despesa. O campo
       `periodicidade` é metadado do arquivo, não regra nova.

  R1 — O motor processa a política que recebe, sem versionamento. O lote
       não declara qual versão da política se aplica a ele; decidir
       retroatividade é do RH, não do motor.

  F1 — Política ou câmbio ausente, malformado, com limite negativo ou
       taxa não positiva é erro de lote. Sem política não há como avaliar
       nenhuma despesa.

  C1 — O item C (fila de aprovação manual) está fora de escopo desta
       entrega. Declarado, não esquecido.

  Além das AMB novas, a spec v2 precisa de:

  - §3: remover "não atribui efeito a centro de custo"
  - §4.1: campo `moeda`; centro de custo com efeito; política e câmbio
    como entradas; exceção a "campo não previsto é ignorado" para `moeda`
  - §4.2: moeda e valor originais e taxa usada na saída, para auditoria
  - §8: onde a conversão entra na ordem (etapa 0, depois da RN-011)
  - §9: refazer o resultado esperado do lote de exemplo sob
    CC-ENG-PLATAFORMA — você calculou 386,93, confirme
  - RN-001, RN-002, RN-003, RN-008, RN-009, RN-010, RN-011: ajustar
    conforme sua análise
  - RN nova para a conversão cambial
  - RN nova para a resolução da política por centro de custo

  Depois: DECISIONS (D-012, registrando o envelope inteiro como um evento,
  com o que foi invalidado e quais tasks precisam ser refeitas) e tasks
  novas a partir de T-021.

  As tasks T-021+ não serão implementadas hoje. Escreva-as completas
  mesmo assim, com aceite e RN atendidas, porque a absorção pela spec é o
  que o envelope avalia.

  Commits separados: docs(spec) para a v2, docs(decisions) para a D-012,
  docs(tasks) para as novas.

  Salve também a análise em docs/analise-envelope.md e commite junto.
› Message from general-purpose
  Preenchi as seções Delegação, Descrição, Discernimento, Diligência e Envelope
  de `C:\Users\dev\SDD-Desafio\sdd-desafio\docs\RELATORIO.md`. O cabeçalho
  (Aluno/Repositório/Data) e o Fechamento ficaram como estavam, e a estrutura do
  template foi mantida. Na Discernimento acrescentei dois subcasos, 1-A e 1-B.
  O arquivo continua com CRLF. Não rodei add nem commit, e nenhum outro arquivo
  do repositório foi alterado. Os rascunhos ficaram no scratchpad.

  ## 1. O que escrevi em cada seção

  **Delegação.**
  - A tabela tem as 7 linhas, cada uma com sessão e commit de evidência.
  - Os commits da spec (`b875cd2`..`4940b8c`) foram manuais, sem trailer; de
  `db8b1b6` em diante os commits são co-autorados pelo agente.
  - O arrependimento é a política como constantes: o agente propôs, eu aprovei,
  e o envelope pede o oposto (session 08, plan §4).
  - O que deveria ter delegado: a revisão independente veio só depois da T-004,
  e não automatizei a higiene de commit.
  - Ferramentas: um subagente (D-010, `2af517f`, `818d6a6`), incluindo a
  reclassificação do D4 de "baixa" para "média". O CLAUDE.md funcionou como
  fiscal. Não há `.claude/`, então nada de hooks nem skills.

  **Descrição.**
  - Requisito escolhido: item 8 ("Duplicatas devem ser tratadas"), no desempate
  da RN-009.
  - Mostro três versões: `441be28` "ordem lexicográfica", `9448765` "códigos dos
  caracteres" e `2af517f` "ponto de código Unicode".
  - Cada camada de ambiguidade veio de uma fonte diferente: a V8 do agente, a
  recusa da RN-012 e o D4 do subagente. Nenhuma delas fui eu que percebi.

  **Discernimento.**
  - Caso 1 é o erro do agente sobre d-004/AMB-006. É honesto sobre o ponto
  fraco: eu não detectei, foi o agente que se corrigiu (session 01; `0e148af`).
  - 1-A: RN-001 contradizendo o item 6 da política, pego por mim ao ler o diff
  (`b875cd2`).
  - 1-B: o agente quis manter referências obsoletas "como registro histórico" e
  eu recusei (session 02; `bf28bc4`).
  - O "Padrão" lista seis erros meus pegos pelo agente: AMB-015, RN-012, A5, o
  caso grego da D-011, T-002 e a redação da D-001.

  **Diligência.**
  - Descreve o procedimento real: proposta com diff antes de salvar, varredura
  depois de cada decisão, 112 testes, mutações feitas pelo próprio agente e as
  paradas por bug de spec (D-009, D-011).
  - A porcentagem de diffs lidos ficou como [FALTA]. Registrei que T-008 a T-013
  entraram em 18 minutos, com cerca de 950 linhas.
  - O bug do notas-trabalho vazio está descrito em detalhe, junto com o `git add
  -A` da T-003.
  - Também estão o enfraquecimento dos testes de ordem pela T-012 e os testes
  que ainda faltam (T-015, T-016, T-020).

  **Envelope.**
  - A absorção ainda não aconteceu: não há nenhum commit depois de `2aed7d1`, e
  a Fase 5 de tasks.md continua com o texto do template. Por isso as três
  métricas são [FALTA].
  - "Absorveu" e "Resistiu" vêm de `analise-envelope.md` e estão marcados como
  previsão.
  - "Ordem" conta a primeira tentativa sem os arquivos, a cópia por blob e a
  análise antes de mexer na spec.
  - "Poupou" cita a análise feita regra a regra e o novo total, 386,93 contra
  623,43.

  ## 2. Os [FALTA], 13 no total

  1. Delegação, linha do envelope: quem fará o quê na absorção.
  2. Relatório original do subagente com os 14 achados; não está no repositório.
  3. Se houve skill ou MCP usado fora do repositório.
  4. Porcentagem real de diffs lidos.
  5. O que o autor de fato leu nos diffs de T-005 a T-013.
  6. Causa do commit vazio `ed98aab`. A evidência é compatível com arquivo não
  salvo no editor, mas nada confirma.
  7. Arquivos tocados na mão na absorção.
  8. Tempo da absorção (falta o horário de fim).
  9. Diff de absorção (`git diff 2aed7d1 HEAD --stat` depois da absorção).
  10. Confirmação de "absorveu de graça" com o diff real.
  11. Commits da absorção, que provam a ordem.
  12. O que mais mudaria na spec, depois de absorver.
  13. Tempo ou retrabalho economizado, medido.

  Todos os itens de 7 a 13 se resolvem quando o envelope for absorvido e
  commitado.

  ## 3. Contradições entre as fontes

  - **Datas.** As notas datam de "19/10" a nota da revisão por subagente e a da
  T-002, mas os commits são de 18/10, às 23:41 (`03005ba`) e às 23:17
  (`338e1f6`). A D-010 também é datada de 2026-10-18. O `fatos-da-sessao.md`
  chama tudo de "sessão de implementação (19/10)", mas T-001 a T-007 e a D-010
  são de 18/10.
  - **Cinco ou seis lacunas do plan.** As notas falam em "cinco pontos" que a
  spec não decidia; a D-009 fala em "seis situações". O sexto aparece mais tarde
  na session 08: o tipo incompatível em campo opcional e/ou a "entrada
  sintaticamente válida".
  - **"Três recusas" ou mais.** A tarefa e as notas ("Terceiro erro meu pego por
  ele", no A5) falam em três. A evidência mostra pelo menos mais três: o caso
  grego de categoria e o sigma final na D-011, a T-002 e a redação da D-001.
  - **AMB-015.** As notas dizem que o agente "apontou a contradição". Na session
  06 ele ajustou a redação e salvou antes de avisar, mas com autorização prévia
  ("Verifique se a redação acima resolve isso ou se precisa de ajuste").
  - **Envelope antes da hora.** `00-ENVELOPE-LACRADO.md` diz "Não distribuir
  antes do Dia 2, 10h", e os arquivos foram commitados em 19/10 às 00:48
  (`2aed7d1`). No relatório citei só o timestamp, sem comentar. Vale você
  avaliar.
  - **Sessão de implementação não exportada.** `docs/sessions/` só vai até a 09.
  Os fatos do `fatos-da-sessao.md` (mutações, recusa do A5, reclassificação do
  D4, D-011) não têm export no repositório, e o README.md diz que sem
  `docs/sessions/` o critério de relatório vale zero. Recomendo exportar essa
  sessão.
  - **RastreabilidadeTest.** O CLAUDE.md o cita como se já existisse, mas o
  teste só vem na T-016, que está em aberto. Não há arquivo
  `RastreabilidadeTest.kt`.
  - **Contagem de testes conferida.** Os 112 testes do `fatos-da-sessao.md`
  batem com `build/test-results/test/` (19 arquivos, 0 falhas, gerados às 00:42
  de 19/10).

● Vou validar o bloco do envelope contra ele mesmo e contra a spec, começando
  por checar o quanto depende do lote do Dia 1 e dos limites fixos, antes de
  trazer o resultado final do relatório.

● Bash(git status --short; S=specs/001-motor-reembolso/spec.md; wc -l $S; grep
      -n 'd-001\|d-010\|d-014\|d-013' $S | cut -c1-150; echo ---; grep -n
      'exatamente três\|ce…)
  ⎿   M docs/RELATORIO.md
     1724 specs/001-motor-reembolso/spec.md
     112:| Categoria considerada: a categoria após normalização | sempre qu
     … +149 lines (ctrl+o to expand)

● Encontrei quatro inconsistências no bloco (ordem de conversão vs T4,
  dependência de texto livre na P6, P5 como consequência da P2, e quebra do
  build pela tabela §4.1) e já defini como ajustar cada uma. Agora vou ler os
  trechos que preciso editar.

  Read 1 file (ctrl+o to expand)

● Leitura concluída. Começo pelas edições da spec, de cima para baixo.

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 6 lines, removed 1 line
       1  # Spec — Motor de Cálculo de Reembolso
       2  
       3 -**Versão:** 1.3 · **Status:** completa · **Última alteração:** 
         -2026-10-19                                                     
       3 +**Versão:** 2.0 · **Status:** completa · **Última alteração:** 
         +2026-10-19                                                     
       4  
       5 +> A versão 2.0 incorpora a Política de Reembolso v4 (comunicado
         + do RH no                                                      
       6 +> envelope do Dia 2, `exemplos/envelope/`): limites por centro 
         +de custo lidos                                                 
       7 +> de uma política externa, despesas em moeda estrangeira e as A
         +MB-022 a                                                       
       8 +> AMB-038. Registro em DECISIONS.md, D-012.                    
       9 +                                                               
      10  ---
      11  
      12  ## 1. Problema

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 2 lines, removed 1 line
      15  reembolso é decidido à mão: alguém do financeiro confere cada d
          espesa contra a
      16  política do RH e devolve uma relação de aprovações e recusas. O
           processo é
      17  lento, e a política contém lacunas que cada conferente preenche
           à sua maneira
      18 -— esta especificação identifica vinte e uma delas na §6 —, de m
         -odo que a mesma                                                
      18 +— esta especificação identifica trinta e oito delas na §6, vint
         +e e uma na                                                     
      19 +política v3 e dezessete na v4 —, de modo que a mesma           
      20  despesa pode receber decisões diferentes e nenhuma delas vem ac
          ompanhada de
      21  uma justificativa que o colaborador ou o auditor consigam verif
          icar. O custo
      22  aparece em pagamentos indevidos, recusas injustas e retrabalho 
          para contestar

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 12 lines, removed 5 lines
      32  ## 3. Fora de escopo
      33  
      34  - Não determina se o colaborador está em viagem, e portanto não
           aplica a
      35 -  ampliação de 50% prevista no item 6 da política. Ver AMB-007.
      35 +  ampliação de 50% prevista no item 6 da política v3 e no acrés
         +cimo em viagem                                                 
      36 +  da política v4, nem infere viagem de moeda estrangeira. Ver A
         +MB-007 e                                                       
      37 +  AMB-034.                                                     
      38  - Não atribui efeito a campos da entrada que nenhuma regra util
          ize —
      37 -  identificador, nome e centro de custo do colaborador, rótulo 
         -de competência                                                 
      38 -  do período e descrição da despesa. Esses campos são aceitos, 
         -opcionais, e                                                   
      39 -  não alteram nenhum resultado. O fornecedor não é interpretado
         -, mas é                                                        
      40 -  obrigatório, por ser chave de comparação da RN-009. Ver AMB-0
         -15.                                                            
      39 +  identificador e nome do colaborador, rótulo de competência do
         + período e                                                     
      40 +  descrição da despesa. Esses campos são aceitos, opcionais, e 
         +não alteram                                                    
      41 +  nenhum resultado. O fornecedor não é interpretado, mas é obri
         +gatório, por                                                   
      42 +  ser chave de comparação da RN-009. Ver AMB-015. O centro de c
         +usto deixou de                                                 
      43 +  pertencer a esta lista: ele seleciona a tabela de limites (RN
         +-013).                                                         
      44 +- Não implementa a fila de aprovação manual do item C da políti
         +ca v4                                                          
      45 +  (pendência acima de R$ 500,00). Ver AMB-038.                 
      46 +- Não seleciona versão de política por competência: avalia o lo
         +te com a                                                       
      47 +  política que recebe. Ver AMB-036.                            
      48  
      49  ## 4. Entrada e saída
      50  

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 4 lines, removed 2 lines
      53  
      54  ### 4.1 Entrada
      55  
      56 -A entrada é um **lote**: as despesas de um colaborador em um pe
         -ríodo.                                                         
      56 +A entrada é um **lote**: as despesas de um colaborador em um pe
         +ríodo. O lote                                                  
      57 +é avaliado contra duas outras entradas, mantidas fora do motor:
         + a                                                             
      58 +**política** e as **cotações de câmbio**, descritas ao fim dest
         +a seção.                                                       
      59  
      60  **Informações do lote**
      61  
     ...
      63  |---|---|---|---|---|
      64  | Identificador do colaborador | texto | Quem apresenta o lote 
          | não | AMB-015 |
      65  | Nome do colaborador | texto | Idem | não | AMB-015 |
      64 -| Centro de custo do colaborador | texto | Alocação contábil; s
         -em efeito no cálculo | não | AMB-015 |                         
      66 +| Centro de custo do colaborador | texto | Seleciona a tabela d
         +e limites da política; comparado exatamente como informado; aus
         +ente, sem valor, de tipo incompatível ou sem entrada na polític
         +a leva à tabela padrão | não | RN-013, AMB-022 a AMB-024 |     
      67  | Data de início do período | data (dia, sem hora) | Primeiro d
          ia da competência, incluído | **sim** | RN-005 |
      68  | Data de fim do período | data (dia, sem hora) | Último dia da
           competência, incluído | **sim** | RN-005 |
      69  | Rótulo de competência | texto (ano e mês) | Informativo; não 
          participa da avaliação | não | AMB-015, AMB-016 |

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 7 lines, removed 3 lines
      75  |---|---|---|---|---|
      76  | Identificador | texto | Identifica a despesa; único no lote; 
          desempata duplicatas em ordem lexicográfica; comparado e aprese
          ntado após a remoção de espaços nas extremidades | **sim** | RN
          -009, RN-012, AMB-020 |
      77  | Data | data (dia, sem hora) | Dia em que a despesa ocorreu; d
          ecide a pertinência ao período | **sim** | RN-005 |
      78 -| Categoria | texto | Comparada após normalização; qualquer tex
         -to é aceito, e categoria fora das três elegíveis é recusa, não 
         -erro | **sim** | RN-008, AMB-017 |                             
      78 +| Categoria | texto | Comparada após normalização; qualquer tex
         +to é aceito, e categoria fora das elegíveis da tabela aplicável
         + é recusa, não erro | **sim** | RN-008, RN-013, AMB-017 |      
      79  | Descrição | texto | Não é interpretada | não | AMB-015 |
      80  | Fornecedor | texto | Chave de comparação da duplicidade, após
           normalização; não é interpretado | **sim** | RN-009, AMB-015 |
      81 -| Valor | número decimal | Valor lançado; normalizado para cent
         -avos antes de qualquer regra; após normalização, deve ser maior
         - que zero | **sim** | RN-010, RN-011 |                         
      81 +| Valor | número decimal | Valor lançado, na moeda da despesa; 
         +convertido para reais (RN-014) e normalizado para centavos (RN-
         +011) antes de qualquer regra; após normalização, deve ser maior
         + que zero | **sim** | RN-010, RN-011, RN-014 |                 
      82 +| Moeda | código de três letras maiúsculas (ISO 4217) | Moeda e
         +m que o valor foi lançado; quando não informada, a despesa está
         + em reais (BRL) | não | RN-014, AMB-033 |                      
      83  | Nota fiscal apresentada | sim ou não | Se a despesa tem compr
          ovante fiscal | **sim** | RN-003, RN-010 |
      84  
      85  Um texto obrigatório que fique vazio após a remoção de espaços 
          nas
      86  extremidades é tratado como ausente. Uma informação não obrigat
          ória
      87  informada com tipo incompatível é tratada como ausente e não co
          nstitui erro
      87 -(AMB-015). Informações não previstas nesta seção são aceitas e 
         -ignoradas                                                      
      88 +(AMB-015), com uma exceção: a moeda. Uma moeda informada sem va
         +lor, vazia,                                                    
      89 +de tipo incompatível ou fora do formato de três letras maiúscul
         +as é erro de                                                   
      90 +item, porque tratá-la como não informada faria a despesa ser pa
         +ga em reais                                                    
      91 +(AMB-033). Informações não previstas nesta seção são aceitas e 
         +ignoradas                                                      
      92  (AMB-015).
      93  
      94  **Espaços nas extremidades.** Em toda esta spec — na regra acim
          a, na

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 3 lines, removed 2 lines
      103  de um lote (RN-010); o período está ausente ou tem tipo incomp
           atível; a
      104  data de início ou a de fim está ausente ou não é data válida; 
           a data de início é posterior
      105  à de fim; as despesas do lote não são informadas ou a relação 
           de despesas
      106 -tem tipo incompatível; ou duas ou mais despesas têm o mesmo id
          -entificador.                                                  
      107 -Uma data de início igual à de fim é válida: o período tem um d
          -ia.                                                           
      106 +tem tipo incompatível; duas ou mais despesas têm o mesmo ident
          +ificador; ou a                                                
      107 +política ou as cotações de câmbio não podem ser lidas ou são i
          +nválidas                                                      
      108 +(AMB-037). Uma data de início igual à de fim é válida: o perío
          +do tem um dia.                                                
      109  
      110  **Despesa inválida (erro de item, RN-010).** Uma despesa é cla
           ssificada como
      111  erro quando lhe falta informação obrigatória — inclusive o ide
           ntificador —,

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 23 lines, removed 5 lines
      111  erro quando lhe falta informação obrigatória — inclusive o ide
           ntificador —,
      112  quando uma informação obrigatória tem tipo incompatível — valo
           r que não é
      113  número, indicação de nota fiscal que não é sim ou não, fornece
           dor que não é
      114 -texto —, quando a data não existe no                          
      115 -calendário, quando o valor normalizado é menor ou igual a zero
          -, ou quando o                                                 
      116 -elemento da relação de despesas não é uma despesa (AMB-020). T
          -odas as causas                                                
      117 -aplicáveis são reportadas (AMB-021); o elemento que não é uma 
          -despesa tem                                                   
      118 -uma única causa: a própria despesa, com tipo incompatível (AMB
          --020).                                                        
      114 +texto, moeda fora do formato —, quando a data não existe no   
      115 +calendário, quando a moeda informada não tem cotação aplicável
          + à data da                                                    
      116 +despesa (AMB-028, AMB-029), quando o valor normalizado é menor
          + ou igual a                                                   
      117 +zero, ou quando o elemento da relação de despesas não é uma de
          +spesa                                                         
      118 +(AMB-020). Todas as causas aplicáveis são reportadas (AMB-021)
          +; o elemento                                                  
      119 +que não é uma despesa tem uma única causa: a própria despesa, 
          +com tipo                                                      
      120 +incompatível (AMB-020).                                       
      121  
      122 +**Política.** Mantida pelo financeiro fora do motor, que a lê 
          +a cada                                                        
      123 +avaliação (AMB-022).                                          
      124 +                                                              
      125 +| Informação | Significado | Origem |                         
      126 +|---|---|---|                                                 
      127 +| Tabela padrão | Para cada categoria elegível, o limite em re
          +ais; aplicada ao lote cujo centro de custo não tem tabela próp
          +ria | RN-013, AMB-022, AMB-024 |                              
      128 +| Tabelas por centro de custo | Para cada centro de custo list
          +ado, as suas categorias elegíveis e o limite de cada uma, em r
          +eais; categoria ausente da tabela é inelegível para aquele cen
          +tro; limite 0,00 torna a categoria inelegível | RN-013, AMB-02
          +3, AMB-026 |                                                  
      129 +| Limiar de nota fiscal | Valor em reais acima do qual a nota 
          +fiscal é exigida | RN-003 |                                   
      130 +                                                              
      131 +A periodicidade, as observações e o acréscimo em viagem que ac
          +ompanham a                                                    
      132 +política são informativos e não participam da avaliação (AMB-0
          +34, AMB-035).                                                 
      133 +                                                              
      134 +**Cotações de câmbio.** Para cada data e cada moeda cotada, o 
          +valor em reais                                                
      135 +de uma unidade da moeda. Só há cotação em algumas datas; a dat
          +a sem cotação é                                               
      136 +resolvida pela RN-014 (AMB-027).                              
      137 +                                                              
      138  ### 4.2 Saída
      139  
      140  **Por despesa.** Cada despesa da entrada aparece exatamente um
           a vez na saída,

● Ajusto duas remissões que acabei de escrever e sigo para a §4.2.

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 1 line, removed 1 line
      63  |---|---|---|---|---|
      64  | Identificador do colaborador | texto | Quem apresenta o lote 
          | não | AMB-015 |
      65  | Nome do colaborador | texto | Idem | não | AMB-015 |
      66 -| Centro de custo do colaborador | texto | Seleciona a tabela d
         -e limites da política; comparado exatamente como informado; aus
         -ente, sem valor, de tipo incompatível ou sem entrada na polític
         -a leva à tabela padrão | não | RN-013, AMB-022 a AMB-024 |     
      66 +| Centro de custo do colaborador | texto | Seleciona a tabela d
         +e limites da política; comparado exatamente como informado; aus
         +ente, sem valor, de tipo incompatível ou sem entrada na polític
         +a leva à tabela padrão | não | RN-013, AMB-022 a AMB-025 |     
      67  | Data de início do período | data (dia, sem hora) | Primeiro d
          ia da competência, incluído | **sim** | RN-005 |
      68  | Data de fim do período | data (dia, sem hora) | Último dia da
           competência, incluído | **sim** | RN-005 |
      69  | Rótulo de competência | texto (ano e mês) | Informativo; não 
          participa da avaliação | não | AMB-015, AMB-016 |

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 2 lines, removed 1 line
      129  | Limiar de nota fiscal | Valor em reais acima do qual a nota 
           fiscal é exigida | RN-003 |
      130  
      131  A periodicidade, as observações e o acréscimo em viagem que ac
           ompanham a
      132 -política são informativos e não participam da avaliação (AMB-0
          -34, AMB-035).                                                 
      132 +política são informativos e não participam da avaliação (AMB-0
          +26, AMB-034,                                                  
      133 +AMB-035).                                                     
      134  
      135  **Cotações de câmbio.** Para cada data e cada moeda cotada, o 
           valor em reais
      136  de uma unidade da moeda. Só há cotação em algumas datas; a dat
           a sem cotação é

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 3 lines, removed 1 line
      147  |---|---|---|
      148  | Identificador, após a remoção de espaços nas extremidades | 
           sempre; em despesa com erro cujo identificador falta ou tem ti
           po incompatível, é substituído pela posição da despesa na rela
           ção de despesas da entrada, contada a partir de um | Relaciona
            o resultado à despesa que o originou; todo aceite desta spec 
           se refere à despesa por ele (RN-009, RN-012); apresentado norm
           alizado para mostrar o mesmo valor que a comparação usa (AMB-0
           20) |
      149  | Classificação: integral, parcial, recusada ou erro | sempre 
           | RN-002 (integral, parcial), RN-007 (os quatro valores), RN-0
           10 (erro) |
      150 -| Valor considerado: o valor após normalização | sempre que o 
          -valor da entrada for numérico; ausente apenas quando a informa
          -ção falta ou tem tipo incompatível | RN-011 ("é também o valor
          - apresentado na saída"); permite verificar a fronteira da nota
          - fiscal (RN-003) e o arredondamento de d-011 |                
      150 +| Valor considerado: o valor em reais após conversão e normali
          +zação | sempre que o valor da entrada for numérico e puder ser
          + expresso em reais; ausente quando a informação falta, tem tip
          +o incompatível ou a moeda não pôde ser convertida | RN-011 ("é
          + também o valor apresentado na saída"), RN-014; permite verifi
          +car a fronteira da nota fiscal (RN-003) e o arredondamento de 
          +d-011 |                                                       
      151 +| Valor e moeda originais: o valor como lançado e a sua moeda 
          +| quando a despesa informa moeda diferente de BRL | RN-014; se
          +m eles o auditor não reconstitui o valor considerado |        
      152 +| Cotação usada: a taxa e a data da cotação | quando houve con
          +versão | RN-014, AMB-027; mostra qual cotação foi usada quando
          + a data da despesa não tem cotação própria (e-004: cotação de 
          +17/07 para despesa de 18/07) |                                
      153  | Categoria considerada: a categoria após normalização | sempr
           e que a categoria for informada como texto; ausente apenas qua
           ndo falta ou tem tipo incompatível | RN-008; permite verificar
            que "ALIMENTACAO" em d-014 foi tratada como alimentacao |
      154  | Valor reembolsado | sempre; 0,00 em recusa e em erro | RN-00
           2, RN-004, RN-006, RN-008, RN-009, RN-010 |
      155  | Limite aplicado | integral e parcial | RN-001, RN-002; sem e
           le, aceites como "d-011 → limite 60,00" (AMB-007) e "d-010 → l
           imite 250,00" (AMB-009) não são verificáveis, pois o valor ree
           mbolsado de d-011 é o mesmo com qualquer limite acima de 33,33
            |

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 1 line, removed 1 line
      157  | Exigência de nota fiscal: exigida ou não exigida | integral,
            parcial e recusada | RN-003; "d-003 → nota fiscal não exigida
           " e "transporte de 100,01 com NF → exigida e presente" produze
           m o mesmo reembolso, e sem esta informação os dois aceites não
            se distinguem |
      158  | Lista de motivos de recusa, na ordem da §8 | sempre; vazia e
           m integral, parcial e erro | RN-007, AMB-018 |
      159  | Despesa mantida: o identificador da despesa que permaneceu c
           om o reembolso | quando a lista de motivos contém DUPLICATA | 
           RN-009; permite verificar qual despesa do grupo foi mantida e 
           que inverter a ordem da entrada não a altera |
      160 -| Causas do erro: para cada informação que falhou, qual e de q
          -ue forma — ausente, tipo incompatível, data inexistente ou val
          -or não positivo —, todas as aplicáveis, na ordem em que as inf
          -ormações da despesa são declaradas na §4.1; o elemento da rela
          -ção que não é uma despesa tem uma única causa: a própria despe
          -sa, com tipo incompatível | erro | RN-010, AMB-017, AMB-020, A
          -MB-021; separadas da lista de motivos, porque erro não é recus
          -a (RN-007) |                                                  
      160 +| Causas do erro: para cada informação que falhou, qual e de q
          +ue forma — ausente, tipo incompatível, data inexistente, moeda
          + sem cotação ou valor não positivo —, todas as aplicáveis, na 
          +ordem em que as informações da despesa são declaradas na §4.1;
          + o elemento da relação que não é uma despesa tem uma única cau
          +sa: a própria despesa, com tipo incompatível | erro | RN-010, 
          +AMB-017, AMB-020, AMB-021; separadas da lista de motivos, porq
          +ue erro não é recusa (RN-007) |                               
      161  
      162  Esses campos, em conjunto, são a justificativa de cada despesa
           : respondem por
      163  que ela recebeu o que recebeu. Uma frase legível construída a 
           partir deles é

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 3 lines, removed 3 lines
      163  que ela recebeu o que recebeu. Uma frase legível construída a 
           partir deles é
      164  apresentação, e pertence ao plan.md.
      165  
      166 -Não aparecem na saída o identificador, o nome e o centro de cu
          -sto do                                                        
      167 -colaborador, o rótulo de competência e a descrição das despesa
          -s: são                                                        
      168 -informações que nenhuma regra utiliza (AMB-015).              
      166 +Não aparecem na saída o identificador e o nome do colaborador,
          + o rótulo de                                                  
      167 +competência e a descrição das despesas: são informações que ne
          +nhuma regra                                                   
      168 +utiliza (AMB-015).                                            
      169  
      170  **Notação usada nesta spec.** Nos aceites e nos casos de borda
           , a lista de
      171  motivos de recusa é escrita entre colchetes, na ordem da §8 — 
           por exemplo,

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 17 lines, removed 6 lines
      172  motivos [FORA_DA_COMPETENCIA, SEM_NOTA_FISCAL]. Os códigos de 
           motivo escritos
      173  em maiúsculas (FORA_DA_COMPETENCIA, CATEGORIA_INELEGIVEL, SEM_
           NOTA_FISCAL,
      174  DUPLICATA) são identificadores de negócio definidos por esta s
           pec, não nomes
      175 -de implementação. Do mesmo modo, alimentacao, transporte_urban
          -o e hospedagem                                                
      176 -são o vocabulário de valores de categoria da entrada: identifi
          -cadores de                                                    
      177 -negócio definidos por esta spec, escritos sem acento e com o s
          -eparador                                                      
      178 -sublinhado, e não nomes de implementação; é com eles que a cat
          -egoria                                                        
      179 -normalizada é comparada (RN-008, AMB-013). Nem os colchetes ne
          -m a grafia dos                                                
      180 -códigos de motivo prescrevem a forma da saída.                
      175 +de implementação. Do mesmo modo, alimentacao, transporte_urban
          +o, hospedagem                                                 
      176 +e representacao são o vocabulário de valores de categoria: ide
          +ntificadores de                                               
      177 +negócio, escritos sem acento e com o separador sublinhado, e n
          +ão nomes de                                                   
      178 +implementação; é com eles, tal como aparecem na tabela aplicáv
          +el da                                                         
      179 +política, que a categoria normalizada é comparada (RN-008, RN-
          +013, AMB-013).                                                
      180 +Os códigos de centro de custo (CC-COMERCIAL, CC-ENG-PLATAFORMA
          +…) e de moeda                                                 
      181 +(BRL, EUR, USD…) também são identificadores, comparados exatam
          +ente                                                          
      182 +(AMB-025, AMB-033). Nem os colchetes nem a grafia dos códigos 
          +de motivo                                                     
      183 +prescrevem a forma da saída.                                  
      184  
      185 +**Despesas do lote de exemplo nos aceites.** Os aceites das RN
          +-001 a RN-012,                                                
      186 +os das AMB-001 a AMB-021 e os casos da §7 que citam uma despes
          +a do lote de                                                  
      187 +exemplo (d-001 a d-014) com limites de 60,00, 80,00 e 250,00 a
          +valiam essa                                                   
      188 +despesa num lote **sem centro de custo**, isto é, sob a tabela
          + padrão                                                       
      189 +(AMB-024), para isolar a regra que verificam. O lote de exempl
          +o completo é                                                  
      190 +do centro de custo CC-ENG-PLATAFORMA, e o seu resultado sob a 
          +tabela desse                                                  
      191 +centro está na §9.                                            
      192 +                                                              
      193  **Consolidado do lote.**
      194  
      195  | Informação | Por que existe |

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 1 line
      195  | Informação | Por que existe |
      196  |---|---|
      197  | Período avaliado: datas de início e de fim | Mostra contra q
           ual intervalo a pertinência foi julgada (RN-005) |
      198 +| Tabela de limites aplicada: o centro de custo cuja tabela fo
          +i usada, ou a indicação de que foi usada a tabela padrão | RN-
          +013; sem ela, o auditor não sabe contra quais limites cada des
          +pesa foi comparada |                                          
      199  | Total reembolsado: soma dos valores reembolsados de todas as
            despesas | É o que o motor existe para determinar; confere co
           m a soma dos itens porque cada valor já está em centavos (AMB-
           014) |
      200  | Quantidade de despesas por classificação — integral, parcial
           , recusada, erro — e quantidade total | Prova que nenhuma desp
           esa foi omitida: a quantidade total é igual à de despesas da e
           ntrada (AMB-010) |
      201  

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 2 lines, removed 1 line
      209  um total de 0,00 seria confundido com um lote avaliado em que 
           nada foi
      210  reembolsado.
      211  
      212 -**Exemplo.** Lote de 01/07 a 31/07 com d-001, d-006, d-007 e d
          --009:                                                         
      212 +**Exemplo.** Lote sem centro de custo (tabela padrão), de 01/0
          +7 a 31/07, com                                                
      213 +d-001, d-006, d-007 e d-009:                                  
      214  
      215  | Despesa | Classificação | Valor considerado | Categoria cons
           iderada | Limite | Reembolsado | Excedente | Nota fiscal | Mot
           ivos | Outras |
      216  |---|---|---|---|---|---|---|---|---|---|

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 2 lines, removed 2 lines
      219  | d-007 (idêntica a d-006) | recusada | 54,90 | alimentacao | 
           — | 0,00 | — | não exigida | DUPLICATA | despesa mantida: d-00
           6 |
      220  | d-009 (transporte urbano, −45,00, sem NF) | erro | −45,00 | 
           transporte_urbano | — | 0,00 | — | — | nenhum | causas: valor 
           não positivo |
      221  
      222 -Consolidado: período de 01/07 a 31/07; total reembolsado 114,9
          -0; 1 integral,                                                
      223 -1 parcial, 1 recusada, 1 erro; 4 despesas.                    
      222 +Consolidado: período de 01/07 a 31/07; tabela padrão; total re
          +embolsado                                                     
      223 +114,90; 1 integral, 1 parcial, 1 recusada, 1 erro; 4 despesas.
      224  
      225  ## 5. Regras de negócio
      226  
● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Error editing file

● O bloco de premissa aparece duas vezes, na §5 e na §7, com o mesmo texto.
  Substituo as duas ocorrências:

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 14 lines, removed 12 lines
      226  
      227  Cada regra recebe um ID (`RN-001`, ...). As tasks vão referenc
           iar esses IDs.
      228  
      229 -> **Limites nominais, sem ampliação por viagem:** o sistema nã
          -o determina se o                                              
      230 -> colaborador está em viagem (AMB-007). Todas as despesas são 
          -avaliadas com os                                              
      231 -> limites nominais da política — alimentação R$ 60,00, transpo
          -rte urbano                                                    
      232 -> R$ 80,00, hospedagem R$ 250,00 por lançamento (uma diária) —
          -, e os                                                        
      233 -> critérios de aceite e os casos de borda deste documento vale
          -m para qualquer                                               
      234 -> despesa, sem distinção de viagem.                           
      229 +> **Limites da tabela aplicável, sem ampliação por viagem:** o
          + sistema não                                                  
      230 +> determina se o colaborador está em viagem (AMB-007, AMB-034)
          +. Toda despesa é                                              
      231 +> avaliada com os limites da tabela que a RN-013 seleciona par
          +a o lote — na                                                 
      232 +> tabela padrão vigente, alimentação R$ 60,00, transporte urba
          +no R$ 80,00,                                                  
      233 +> hospedagem R$ 250,00 por lançamento (uma diária) —, e os cri
          +térios de                                                     
      234 +> aceite e os casos de borda deste documento valem para qualqu
          +er despesa,                                                   
      235 +> sem distinção de viagem.                                    
      236  
      237  ### RN-001 — Limite de categoria aplicado por despesa individu
           al
      238  
     ...
      1599  
      1600  ## 7. Casos de borda
      1601  
      1601 -> **Limites nominais, sem ampliação por viagem:** o sistema n
           -ão determina se o                                            
      1602 -> colaborador está em viagem (AMB-007). Todas as despesas são
           - avaliadas com os                                            
      1603 -> limites nominais da política — alimentação R$ 60,00, transp
           -orte urbano                                                  
      1604 -> R$ 80,00, hospedagem R$ 250,00 por lançamento (uma diária) 
           -—, e os                                                      
      1605 -> critérios de aceite e os casos de borda deste documento val
           -em para qualquer                                             
      1606 -> despesa, sem distinção de viagem.                          
      1602 +> **Limites da tabela aplicável, sem ampliação por viagem:** 
           +o sistema não                                                
      1603 +> determina se o colaborador está em viagem (AMB-007, AMB-034
           +). Toda despesa é                                            
      1604 +> avaliada com os limites da tabela que a RN-013 seleciona pa
           +ra o lote — na                                               
      1605 +> tabela padrão vigente, alimentação R$ 60,00, transporte urb
           +ano R$ 80,00,                                                
      1606 +> hospedagem R$ 250,00 por lançamento (uma diária) —, e os cr
           +itérios de                                                   
      1607 +> aceite e os casos de borda deste documento valem para qualq
           +uer despesa,                                                 
      1608 +> sem distinção de viagem.                                   
      1609  
      1610  | Caso | Entrada | Comportamento esperado | Regra |
      1611  |---|---|---|---|

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 14 lines, removed 10 lines
      236  
      237  ### RN-001 — Limite de categoria aplicado por despesa individu
           al
      238  
      239 -**Regra:** cada despesa de alimentação, transporte urbano ou h
          -ospedagem é                                                   
      240 -comparada, isoladamente, com o limite da sua categoria (alimen
          -tação R$ 60,00;                                               
      241 -transporte urbano R$ 80,00; hospedagem R$ 250,00). Nenhuma out
          -ra despesa                                                    
      242 -altera o resultado da despesa avaliada por meio de agregação d
          -e valores: não                                                
      243 -há soma de despesas da mesma categoria no mesmo dia. Não exist
          -e teto diário                                                 
      244 -agregado por categoria. Em hospedagem, o limite é por diária e
          - cada                                                         
      245 -lançamento vale uma diária: R$ 250,00 por lançamento (AMB-009)
          -.                                                             
      246 -**Origem:** política do RH, itens 1, 2 e 3; decisões AMB-001, 
          -AMB-007 e AMB-009                                             
      247 -**Depende de:** RN-002 (reembolso parcial, decidido na AMB-002
          -).                                                            
      248 -**Aceite:**                                                   
      239 +**Regra:** cada despesa de categoria elegível é comparada, iso
          +ladamente, com                                                
      240 +o limite da sua categoria na tabela que a RN-013 seleciona par
          +a o lote. O                                                   
      241 +limite está em reais e é comparado com o valor em reais da des
          +pesa (RN-014).                                                
      242 +Nenhuma outra despesa altera o resultado da despesa avaliada p
          +or meio de                                                    
      243 +agregação de valores: não há soma de despesas da mesma categor
          +ia no mesmo                                                   
      244 +dia. Não existe teto diário agregado por categoria, ainda que 
          +a política                                                    
      245 +descreva o limite "por dia" (AMB-035). Em hospedagem, o limite
          + é por diária                                                 
      246 +e cada lançamento vale uma diária (AMB-009).                  
      247 +**Origem:** política do RH v3, itens 1, 2 e 3, e v4, item A; d
          +ecisões                                                       
      248 +AMB-001, AMB-007, AMB-009 e AMB-035                           
      249 +**Depende de:** RN-002 (reembolso parcial, decidido na AMB-002
          +); RN-013                                                     
      250 +(tabela aplicável); RN-014 (valor em reais).                  
      251 +**Aceite:** os aceites com despesas do lote de exemplo valem s
          +ob a tabela                                                   
      252 +padrão (§4.2, "Despesas do lote de exemplo nos aceites").     
      253  - d-001 (03/07, alimentação, 72,50) → reembolso 60,00, parcial
           .
      254  - d-002 (03/07, alimentação, 38,00) → reembolso 38,00, integra
           l.
      255  - O dia 03/07 totaliza 98,00 reembolsados em alimentação.

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 5 lines
      258  - d-010 (14/07, hospedagem, 480,00, com NF, "Hotel Rio - 2 dia
           rias") →
      259    limite 250,00; reembolso 250,00, parcial; excedente 230,00. 
           A descrição não
      260    altera o limite.
      261 +- No lote de exemplo completo (CC-ENG-PLATAFORMA, alimentação 
          +75,00), d-001                                                 
      262 +  → reembolso 72,50, integral, e o dia 03/07 totaliza 110,50 (
          +§9).                                                          
      263 +- e-007 (22/07, hospedagem, 1.200,00 BRL, com NF, "Hotel Londr
          +es - 3                                                        
      264 +  noites"; CC-COMERCIAL, hospedagem 400,00) → limite 400,00; r
          +eembolso                                                      
      265 +  400,00, parcial; excedente 800,00. A descrição não altera o 
          +limite.                                                       
      266  
      267  ### RN-002 — Reembolso parcial: o limite é teto
      268  

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 8 lines, removed 3 lines
      270  tem reembolso igual ao limite aplicável; o excedente (valor me
           nos limite) não
      271  é reembolsado e a despesa é classificada como parcial. Uma des
           pesa cujo valor
      272  é menor ou igual ao limite aplicável tem reembolso igual ao se
           u valor e é
      273 -classificada como integral. O limite aplicável é o limite nomi
          -nal da                                                        
      274 -categoria conforme a RN-001; não há ampliação por viagem (AMB-
          -007).                                                         
      273 +classificada como integral. O limite aplicável é o limite da c
          +ategoria na                                                   
      274 +tabela aplicável, conforme a RN-001 e a RN-013; não há ampliaç
          +ão por viagem                                                 
      275 +(AMB-007, AMB-034). Valor, limite, reembolso e excedente estão
          + em reais.                                                    
      276  **Origem:** política do RH, item 4; decisões AMB-002 e AMB-007
      277  **Depende de:** RN-001 (limite da categoria).
      277 -**Aceite:**                                                   
      278 +**Aceite:** sob a tabela padrão, salvo indicação.             
      279  - d-001 (03/07, alimentação, 72,50) → reembolso 60,00, parcial
           ; excedente 12,50.
      280  - Alimentação de 60,00 → reembolso 60,00, integral.
      281  - Alimentação de 60,01 → reembolso 60,00, parcial; excedente 0
           ,01.
      282  - Transporte urbano de 90,00 → reembolso 80,00, parcial; exced
           ente 10,00.
      283 +- e-008 (23/07, alimentação, 95,00 BRL; CC-COMERCIAL, alimenta
          +ção 90,00) →                                                  
      284 +  reembolso 90,00, parcial; excedente 5,00.                   
      285 +- e-002 (14/07, alimentação, 22,00 EUR = 130,46; CC-COMERCIAL)
          + → reembolso                                                  
      286 +  90,00, parcial; excedente 40,46, em reais.                  
      287  
      288  ### RN-003 — Nota fiscal obrigatória acima de R$ 100,00
      289  

● Bash(S=specs/001-motor-reembolso/spec.md; grep -n '^### RN-0\|^### AMB-0\|^##
      ' $S | head -60)
  ⎿  12:## 1. Problema
     25:## 2. Objetivo
     32:## 3. Fora de escopo
     … +40 lines (ctrl+o to expand)

  Read 1 file (ctrl+o to expand)

● A RN-003 até a RN-012 estão lidas. Agora edito as RNs afetadas, e depois leio
  as AMB que precisam de ajuste e as seções §7 a §10.

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 22 lines, removed 12 lines
      285  - e-002 (14/07, alimentação, 22,00 EUR = 130,46; CC-COMERCIAL)
            → reembolso
      286    90,00, parcial; excedente 40,46, em reais.
      287  
      288 -### RN-003 — Nota fiscal obrigatória acima de R$ 100,00       
      288 +### RN-003 — Nota fiscal obrigatória acima do limiar da políti
          +ca                                                            
      289  
      290 -**Regra:** uma despesa cujo valor é estritamente maior que R$ 
          -100,00 exige                                                  
      291 -nota fiscal. Uma despesa de valor menor ou igual a R$ 100,00 n
          -ão exige nota                                                 
      292 -fiscal, tenha ela nota ou não. A comparação usa o valor lançad
          -o da despesa,                                                 
      293 -já normalizado conforme a RN-011, antes de qualquer aplicação 
          -de limite; o                                                  
      294 -valor reembolsável não interfere na exigência. Uma despesa que
          - exige nota                                                   
      295 -fiscal e a apresenta não sofre nenhum efeito desta regra e seg
          -ue para o                                                     
      296 -limite da sua categoria. Uma despesa que exige nota fiscal e n
          -ão a                                                          
      297 -apresenta é recusada conforme a RN-004.                       
      298 -**Origem:** política do RH, item 5; decisões AMB-004 e AMB-006
      290 +**Regra:** uma despesa cujo valor é estritamente maior que o l
          +imiar de nota                                                 
      291 +fiscal da política exige nota fiscal; na política vigente, o l
          +imiar é                                                       
      292 +R$ 100,00. Uma despesa de valor menor ou igual ao limiar não e
          +xige nota                                                     
      293 +fiscal, tenha ela nota ou não. A comparação usa o valor da des
          +pesa em reais                                                 
      294 +— convertido (RN-014), se lançado em outra moeda (AMB-031) — j
          +á normalizado                                                 
      295 +conforme a RN-011, antes de qualquer aplicação de limite; o va
          +lor                                                           
      296 +reembolsável não interfere na exigência. Uma despesa que exige
          + nota fiscal e                                                
      297 +a apresenta não sofre nenhum efeito desta regra e segue para o
          + limite da sua                                                
      298 +categoria. Uma despesa que exige nota fiscal e não a apresenta
          + é recusada                                                   
      299 +conforme a RN-004.                                            
      300 +**Origem:** política do RH v3, item 5, e v4; decisões AMB-004,
          + AMB-006,                                                     
      301 +AMB-022 e AMB-031                                             
      302  **Depende de:** RN-002 (reembolso parcial, decidido na AMB-002
           ); RN-004
      303  (recusa por ausência de nota fiscal exigida, decidida na AMB-0
           05); RN-011
      301 -(normalização monetária, que define a fronteira para valores c
          -omo 100,004).                                                 
      302 -**Aceite:**                                                   
      304 +(normalização monetária, que define a fronteira para valores c
          +omo 100,004);                                                 
      305 +RN-014 (valor em reais).                                      
      306 +**Aceite:** sob a tabela padrão e com o limiar de 100,00, salv
          +o indicação.                                                  
      307 +- e-005 (20/07, transporte urbano, 40,00 USD = 220,00, sem NF;
          + CC-COMERCIAL)                                                
      308 +  → nota fiscal exigida, porque 220,00 excede 100,00, e ausent
          +e; reembolso                                                  
      309 +  0,00, recusado (RN-004). Comparar 40,00 com 100,00 seria com
          +parar dólares                                                 
      310 +  com reais.                                                  
      311 +- e-003 (15/07, alimentação, 14,50 EUR = 85,26, sem NF) → nota
          + fiscal não                                                   
      312 +  exigida.                                                    
      313  - d-003 (06/07, transporte urbano, 100,00, sem NF) → nota fisc
           al não exigida;
      314    reembolso 80,00, parcial.
      315  - d-004 (06/07, transporte urbano, 100,01, sem NF) → nota fisc
           al exigida e

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 7 lines, removed 2 lines
      339  uma causa se aplica, os motivos coexistem na lista.
      340  **Origem:** política do RH, item 5; decisões AMB-005 e AMB-018
      341  **Depende de:** RN-003 (exigência de nota fiscal); RN-007 (lis
           ta de motivos).
      342 -**Aceite:**                                                   
      342 +**Aceite:** sob a tabela padrão, salvo indicação.             
      343  - d-004 (06/07, transporte urbano, 100,01, sem NF) → reembolso
            0,00,
      344    recusado, motivos [SEM_NOTA_FISCAL].
      345  - d-013 (22/07, hospedagem, 690,00, sem NF) → reembolso 0,00, 
           recusado,
      346 -  motivos [SEM_NOTA_FISCAL].                                  
      346 +  motivos [SEM_NOTA_FISCAL]. No lote de exemplo completo (CC-E
          +NG-PLATAFORMA,                                                
      347 +  hospedagem com limite 0,00) → motivos [CATEGORIA_INELEGIVEL,
      348 +  SEM_NOTA_FISCAL] (RN-013).                                  
      349 +- e-005 (40,00 USD = 220,00, sem NF; CC-COMERCIAL) → reembolso
          + 0,00,                                                        
      350 +  recusado, motivos [SEM_NOTA_FISCAL], embora o limite de tran
          +sporte urbano                                                 
      351 +  seja 150,00.                                                
      352  - Alimentação de 500,00, sem NF → reembolso 0,00, recusado, mo
           tivos
      353    [SEM_NOTA_FISCAL], embora o teto da categoria seja 60,00.
      354  - Hospedagem de 200,00, sem NF → reembolso 0,00, recusado, mot
           ivos

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 1 line, removed 1 line
      418  motivos de recusa vazia: erro não é recusa (RN-010).
      419  **Origem:** decisões AMB-017 e AMB-018
      420  **Depende de:** §8 (ordem das regras de elegibilidade).
      421 -**Aceite:**                                                   
      421 +**Aceite:** sob a tabela padrão, salvo indicação.             
      422  - Despesa de 150,00, sem NF, com data fora do período → reembo
           lso 0,00,
      423    recusado, motivos [FORA_DA_COMPETENCIA, SEM_NOTA_FISCAL].
      424  - d-008 → reembolso 0,00, recusado, motivos [FORA_DA_COMPETENC
           IA].

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 17 lines, removed 6 lines
      452  seguido de letra produz o sigma final (U+03C2), que é a forma 
           correta em
      453  grego; convertido em qualquer outra posição produz o sigma com
           um (U+03C3).
      454  
      455 -As categorias elegíveis são                                   
      456 -exatamente três: alimentacao, transporte_urbano e hospedagem. 
          -Uma despesa                                                   
      457 -cuja categoria normalizada seja qualquer outra é recusada, com
          - o motivo                                                     
      455 +As categorias elegíveis são exatamente as que a tabela aplicáv
          +el ao lote                                                    
      456 +(RN-013) lista com limite maior que zero; sob a tabela padrão 
          +vigente,                                                      
      457 +alimentacao, transporte_urbano e hospedagem. Uma despesa cuja 
          +categoria                                                     
      458 +normalizada seja qualquer outra é recusada, com o motivo      
      459  CATEGORIA_INELEGIVEL na sua lista de motivos (RN-007).
      459 -**Origem:** política do RH, item 9; decisão AMB-013           
      460 -**Depende de:** RN-007 (lista de motivos).                    
      461 -**Aceite:**                                                   
      460 +**Origem:** política do RH v3, item 9, e v4, item A; decisões 
          +AMB-013,                                                      
      461 +AMB-023 e AMB-026                                             
      462 +**Depende de:** RN-007 (lista de motivos); RN-013 (tabela apli
          +cável).                                                       
      463 +**Aceite:** sob a tabela padrão, salvo indicação.             
      464 +- e-001 (13/07, "representacao", 340,00 BRL, com NF; CC-COMERC
          +IAL) →                                                        
      465 +  categoria elegível naquele centro; reembolso 300,00, parcial
          +.                                                             
      466 +- f-003 (17/07, "representacao", 190,00 BRL, com NF; CC-SUPORT
          +E-N2, sem                                                     
      467 +  tabela própria, logo tabela padrão) → recusada, motivos     
      468 +  [CATEGORIA_INELEGIVEL] (AMB-023).                           
      469 +- e-009 (24/07, "coworking", 120,00, com NF; CC-COMERCIAL) → r
          +ecusada,                                                      
      470 +  motivos [CATEGORIA_INELEGIVEL].                             
      471 +- No lote de exemplo completo (CC-ENG-PLATAFORMA, alimentação 
          +75,00), d-014                                                 
      472 +  ("ALIMENTACAO", 61,00) → reembolso 61,00, integral (§9).    
      473  - d-014 ("ALIMENTACAO", 61,00, com NF, 31/07) → categoria norm
           alizada
      474    alimentacao; reembolso 60,00, parcial; lista de motivos vazi
           a.
      475  - d-005 ("coworking", 89,00, com NF) → reembolso 0,00, recusad
           o, motivos

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 2 lines, removed 2 lines
      490    para a RN-009. Com mapeamento simples produziriam οδοσ e οδο
           ς, chaves
      491    distintas, e um grupo de duplicatas deixaria de ser detectad
           o.
      492  - "ALİMENTACAO" (U+0130, I turco com ponto) → ali̇mentacao, com
            o ponto como
      493 -  caractere combinante separado (U+0307), que não é nenhuma da
          -s três                                                        
      494 -  categorias elegíveis; recusada, motivos [CATEGORIA_INELEGIVE
          -L]. Isso é                                                    
      493 +  caractere combinante separado (U+0307), que não é nenhuma da
          +s categorias                                                  
      494 +  elegíveis; recusada, motivos [CATEGORIA_INELEGIVEL]. Isso é 
      495    intencional: U+0130 é um caractere distinto de U+0049, e tra
           tá-los como
      496    iguais seria o motor adivinhando a intenção de quem digitou,
            contra o
      497    alcance estreito decidido na AMB-013.

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 3 lines, removed 2 lines
      502  
      503  **Regra:** duas ou mais despesas formam um grupo de duplicatas
            quando
      504  coincidem em data, categoria normalizada (RN-008), fornecedor 
           normalizado
      505 -(RN-008) e valor normalizado (RN-011). A descrição e a presenç
          -a de nota                                                     
      506 -fiscal não integram o critério. Só integram grupos as despesas
          - que não                                                      
      505 +(RN-008) e valor normalizado em reais (RN-011, RN-014; AMB-032
          +). A moeda em                                                 
      506 +que cada uma foi lançada, a descrição e a presença de nota fis
          +cal não                                                       
      507 +integram o critério. Só integram grupos as despesas que não   
      508  receberam motivo de nenhuma outra regra de elegibilidade e que
            não têm erro
      509  de item (RN-010): uma despesa
      510  fora do período, de categoria inelegível, sem a nota fiscal ex
           igida ou com

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 7 lines, removed 2 lines
      517  minúsculas, e, com identificadores de larguras diferentes, "d-
           10" precede
      518  "d-9". O resultado
      519  não depende da ordem das despesas na entrada.
      520 -**Origem:** política do RH, item 8; decisão AMB-011           
      520 +**Origem:** política do RH, item 8; decisões AMB-011 e AMB-032
      521  **Depende de:** RN-007 (lista de motivos); RN-008 (categoria e
            fornecedor
      522 -normalizados); RN-011 (valor normalizado).                    
      522 +normalizados); RN-011 (valor normalizado); RN-014 (valor em re
          +ais).                                                         
      523  **Aceite:**
      524 +- Duas despesas no mesmo dia, categoria e fornecedor, uma de 2
          +2,00 EUR em                                                   
      525 +  14/07 (= 130,46) e outra de 130,46 BRL → mesmo valor em reai
          +s; grupo de                                                   
      526 +  duplicatas.                                                 
      527 +- Duas despesas no mesmo dia, categoria e fornecedor, de 22,00
          + EUR e de                                                     
      528 +  22,00 BRL → valores em reais 130,46 e 22,00; não são duplica
          +tas.                                                          
      529  - d-006 e d-007 (09/07, alimentação, Bistro Central, 54,90, co
           m NF) → grupo de
      530    duplicatas; d-006 → reembolso 54,90, integral, lista de moti
           vos vazia;
      531    d-007 → reembolso 0,00, recusado, motivos [DUPLICATA].

● Agora a RN-010 (lote, item e aceites) e a RN-011: