
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
  ⎿  (No output)

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
  ⎿  (No output)

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

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 7 lines, removed 3 lines
      567     entrada não pode ser lida; o período está ausente ou tem ti
           po
      568     incompatível; a data de início ou a de fim está ausente ou 
           não é data
      569     válida; a data de início é posterior à de fim; as despesas 
           do lote não são
      570 -   informadas ou a relação de despesas tem tipo incompatível; 
          -ou há                                                         
      571 -   identificador de despesa repetido (RN-012). Nenhuma despesa
          - é processada e                                               
      570 +   informadas ou a relação de despesas tem tipo incompatível; 
          +há                                                            
      571 +   identificador de despesa repetido (RN-012); a política não 
          +pode ser lida,                                                
      572 +   não tem tabela padrão ou limiar de nota fiscal, ou tem limi
          +te negativo; ou                                               
      573 +   as cotações de câmbio não podem ser lidas ou têm taxa menor
          + ou igual a                                                   
      574 +   zero (AMB-037). Nenhuma despesa é processada e             
      575     o lote é rejeitado. A saída informa todas as causas de erro
            de lote
      576     detectáveis; a entrada que não pode ser lida é a exceção, p
           or impedir a
      574 -   detecção das demais. Uma entrada sintaticamente válida cuja
          - estrutura não                                                
      577 +   detecção das demais. Política e cotações inválidas são repo
          +rtadas junto                                                  
      578 +   com as demais causas do lote, porque não impedem a leitura 
          +do lote. Uma entrada sintaticamente válida cuja estrutura não 
      579     seja a de um lote também é entrada que não pode ser lida, e
            constitui
      580     causa única de rejeição: sem a estrutura de um lote não há 
           onde procurar o
      581     período nem a relação de despesas, e afirmar que estão ause
           ntes seria

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 12 lines, removed 7 lines
      584     da AMB-020.
      585  2. **Erro de item** — uma despesa não pode ser avaliada por in
           formação
      586     obrigatória ausente — inclusive o identificador e a indicaç
           ão de nota
      587 -   fiscal —, tipo incompatível, data inexistente no calendário
          - ou valor                                                     
      588 -   normalizado (RN-011) menor ou igual a zero, ou porque o ele
          -mento da                                                      
      589 -   relação de despesas não é uma despesa (AMB-020). Um texto o
          -brigatório que                                                
      590 -   fique vazio após a remoção de espaços nas extremidades (§4.
          -1) é tratado                                                  
      591 -   como ausente. Só as informações obrigatórias produzem erro 
          -por tipo                                                      
      592 -   incompatível; uma informação não obrigatória e sem regra as
          -sociada com                                                   
      593 -   tipo incompatível é tratada como ausente (AMB-015). A despe
          -sa é                                                          
      587 +   fiscal —, tipo incompatível, data inexistente no calendário
          +, moeda sem                                                   
      588 +   cotação aplicável à data da despesa (RN-014; AMB-028, AMB-0
          +29) ou valor                                                  
      589 +   normalizado em reais (RN-011) menor ou igual a zero, ou por
          +que o elemento                                                
      590 +   da relação de despesas não é uma despesa (AMB-020). Sem cot
          +ação aplicável                                                
      591 +   não há valor em reais, e a faixa de valor não é avaliada. U
          +m texto                                                       
      592 +   obrigatório que fique vazio após a remoção de espaços nas e
          +xtremidades                                                   
      593 +   (§4.1) é tratado como ausente. Só as informações obrigatóri
          +as produzem                                                   
      594 +   erro por tipo incompatível, com uma exceção: a moeda, não o
          +brigatória,                                                   
      595 +   produz erro de item quando informada sem valor, vazia, com 
          +tipo                                                          
      596 +   incompatível ou fora do formato de três letras maiúsculas (
          +AMB-033). Uma                                                 
      597 +   informação não obrigatória e sem regra associada com tipo i
          +ncompatível é                                                 
      598 +   tratada como ausente (AMB-015). A despesa é                
      599     classificada como erro, com reembolso 0,00, e
      600     não é avaliada por nenhuma regra de elegibilidade ou de val
           or; não integra
      601     grupo de duplicatas (RN-009). A saída traz todas as causas 
           do erro, na

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 2 lines, removed 2 lines
      611  
      612  Os valores monetários aceitos são estritamente maiores que zer
           o; um valor
      613  igual ou inferior a zero constitui erro de item. A faixa é ava
           liada sobre o
      614 -valor já normalizado para duas casas decimais (RN-011), e não 
          -sobre o valor                                                 
      615 -escrito na entrada.                                           
      614 +valor em reais já normalizado para duas casas decimais (RN-011
          +, RN-014), e                                                  
      615 +não sobre o valor escrito na entrada.                         
      616  
      617  Um lote sem nenhuma despesa é válido. Um período cuja data de 
           início é igual
      618  à de fim é válido e tem um dia. Informações não previstas no c
           ontrato de

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 19 lines, removed 4 lines
      617  Um lote sem nenhuma despesa é válido. Um período cuja data de 
           início é igual
      618  à de fim é válido e tem um dia. Informações não previstas no c
           ontrato de
      619  entrada são aceitas e ignoradas.
      620 -**Origem:** decisões AMB-014, AMB-015, AMB-017, AMB-019, AMB-0
          -20 e AMB-021;                                                 
      621 -§4.1                                                          
      620 +**Origem:** decisões AMB-014, AMB-015, AMB-017, AMB-019, AMB-0
          +20, AMB-021,                                                  
      621 +AMB-028, AMB-029, AMB-033 e AMB-037; §4.1                     
      622  **Depende de:** RN-011 (normalização monetária); RN-012 (unici
           dade do
      623 -identificador); §4.1 (contrato de entrada).                   
      624 -**Aceite:**                                                   
      623 +identificador); RN-014 (conversão); §4.1 (contrato de entrada)
          +.                                                             
      624 +**Aceite:** sob a tabela padrão, salvo indicação.             
      625 +- e-006 (21/07, representação, 55,00 GBP; as cotações não têm 
          +GBP em data                                                   
      626 +  alguma) → erro de item; causa: moeda sem cotação; reembolso 
          +0,00; as                                                      
      627 +  demais despesas do lote são processadas.                    
      628 +- Despesa com moeda "eur" (minúsculas), "EURO" ou informada co
          +mo número →                                                   
      629 +  erro de item; causa: moeda com tipo incompatível.           
      630 +- Despesa com a moeda informada sem valor ou vazia → erro de i
          +tem; causa:                                                   
      631 +  moeda com tipo incompatível; não é tratada como BRL.        
      632 +- Despesa sem a moeda → em BRL; processada normalmente (e-010,
          + f-001).                                                      
      633 +- Política ausente, ilegível, sem tabela padrão, sem limiar de
          + nota fiscal                                                  
      634 +  ou com algum limite negativo → lote rejeitado, mesmo que o c
          +entro de custo                                                
      635 +  do lote não use o limite inválido.                          
      636 +- Cotações ausentes, ilegíveis ou com alguma taxa menor ou igu
          +al a zero →                                                   
      637 +  lote rejeitado, inclusive um lote só com despesas em BRL.   
      638 +- Política inválida e data de início posterior à de fim no mes
          +mo lote →                                                     
      639 +  lote rejeitado; a saída informa as duas causas.             
      640  - d-009 (11/07, transporte urbano, −45,00, sem NF) → erro de i
           tem; reembolso
      641    0,00; não é avaliada por nenhuma regra de elegibilidade.
      642  - d-005 (coworking, 89,00) → bem formada; recusada, motivos

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 4 lines, removed 2 lines
      679    descrição é tratada como ausente (AMB-015).
      680  - Despesa com fornecedor informado como número → erro de item;
            causa:
      681    fornecedor com tipo incompatível.
      682 -- Lote com centro de custo do colaborador ou rótulo de competê
          -ncia                                                          
      683 -  informados com tipo incompatível → processado normalmente (A
          -MB-015).                                                      
      682 +- Lote com rótulo de competência informado com tipo incompatív
          +el →                                                          
      683 +  processado normalmente (AMB-015).                           
      684 +- Lote com centro de custo informado com tipo incompatível ou 
          +sem valor →                                                   
      685 +  processado normalmente, com a tabela padrão (AMB-024).      
      686  
      687  ### RN-012 — Unicidade do identificador de despesa
      688  
● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 4 lines, removed 1 line
      714  
      715  ### RN-011 — Normalização monetária
      716  
      717 -**Regra:** todo valor monetário da entrada é normalizado para 
          -duas casas                                                    
      717 +**Regra:** todo valor monetário da despesa, já expresso em rea
          +is — o                                                        
      718 +lançado, se a despesa está em reais, ou o resultado da convers
          +ão do valor                                                   
      719 +lançado, sem arredondamento prévio, se está em outra moeda (RN
          +-014,                                                         
      720 +AMB-030) —, é normalizado uma única vez para duas casas       
      721  decimais por arredondamento, com a metade arredondada para cim
           a, antes da
      722  aplicação de qualquer regra — inclusive da validação de faixa 
           da RN-010.
      723  "Para cima" significa afastando-se do zero, com o mesmo critér
           io para valores

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 4 lines, removed 1 line
      728  comparações de limite (RN-001, RN-002), exigência de nota fisc
           al (RN-003),
      729  critério de duplicidade (RN-009) e cálculo do reembolso. É tam
           bém o valor
      730  apresentado na saída.
      731 -**Origem:** decisão AMB-014                                   
      731 +**Origem:** decisões AMB-014 e AMB-030                        
      732  **Aceite:**
      733  - d-011 (15/07, alimentação, 33,333, com NF) → valor normaliza
           do 33,33;
      734    reembolso 33,33, integral.
      735 +- 33,333 EUR em 14/07 (cotação 5,93) → 33,333 × 5,93 = 197,664
          +69 → valor                                                    
      736 +  normalizado 197,66. Arredondar antes da conversão daria 33,3
          +3 × 5,93 =                                                    
      737 +  197,6469 → 197,65, e não é o que esta regra faz.            
      738  - Despesa de 100,004 → valor normalizado 100,00; nota fiscal n
           ão exigida.
      739  - Despesa de 100,005 → valor normalizado 100,01; nota fiscal e
           xigida.
      740  - Alimentação de 60,004 → valor normalizado 60,00; reembolso 6
           0,00, integral.

● Agora as duas RNs novas, inseridas depois da RN-011:

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 84 lines
      745  - Despesa de −45,005 → valor normalizado −45,01; erro de item 
           (RN-010); a
      746    saída apresenta −45,01.
      747  
      748 +### RN-013 — Tabela de limites aplicável ao lote              
      749 +                                                              
      750 +**Regra:** cada lote é avaliado com uma única tabela de limite
          +s da política.                                                
      751 +É a tabela do centro de custo do colaborador quando a política
          + tem entrada                                                  
      752 +para esse código, comparado exatamente como informado, sem con
          +versão de                                                     
      753 +caixa nem remoção de espaços (AMB-025). Quando o lote não info
          +rma o centro                                                  
      754 +de custo, informa-o sem valor ou com tipo incompatível, ou inf
          +orma um código                                                
      755 +que a política não lista, a tabela é a padrão da política (AMB
          +-022,                                                         
      756 +AMB-024). A tabela padrão nunca completa a tabela de um centro
          + de custo:                                                    
      757 +uma categoria ausente da tabela aplicável é inelegível para o 
          +lote, ainda                                                   
      758 +que a tabela padrão a liste (AMB-023). Uma categoria listada c
          +om limite 0,00                                                
      759 +também é inelegível (AMB-026). As categorias elegíveis do lote
          + são, portanto,                                               
      760 +as listadas na tabela aplicável com limite maior que zero, e o
          + limite de                                                    
      761 +cada uma é o da RN-001. A periodicidade, as observações e o ac
          +réscimo em                                                    
      762 +viagem que a política traz não participam da avaliação (AMB-02
          +6, AMB-034,                                                   
      763 +AMB-035).                                                     
      764 +**Origem:** política do RH v4, item A; decisões AMB-022 a AMB-
          +026                                                           
      765 +**Depende de:** RN-001 (limite por despesa); RN-008 (elegibili
          +dade da                                                       
      766 +categoria); RN-010 (política inválida).                       
      767 +**Aceite:**                                                   
      768 +- Lote de c-0912 (CC-COMERCIAL) → tabela do CC-COMERCIAL: alim
          +entação 90,00,                                                
      769 +  transporte urbano 150,00, hospedagem 400,00, representação 3
          +00,00.                                                        
      770 +- e-001 (13/07, representação, 340,00 BRL, com NF; CC-COMERCIA
          +L) → limite                                                   
      771 +  300,00; reembolso 300,00, parcial; excedente 40,00.         
      772 +- Lote de c-1103 (CC-SUPORTE-N2, sem entrada na política) → ta
          +bela padrão;                                                  
      773 +  f-002 (17/07, hospedagem, 310,00, com NF) → limite 250,00; r
          +eembolso                                                      
      774 +  250,00, parcial; excedente 60,00.                           
      775 +- f-003 (representação, 190,00, com NF; CC-SUPORTE-N2) → a tab
          +ela padrão não                                                
      776 +  lista representação; recusada, motivos [CATEGORIA_INELEGIVEL
          +].                                                            
      777 +- Lote sem centro de custo → tabela padrão.                   
      778 +- Lote com centro de custo "cc-comercial" → nenhuma entrada co
          +m esse código                                                 
      779 +  exato; tabela padrão; uma alimentação de 85,00 → reembolso 6
          +0,00, parcial,                                                
      780 +  e não 85,00.                                                
      781 +- Lote do CC-ADM (sem hospedagem na tabela) com hospedagem de 
          +200,00, com NF →                                              
      782 +  recusada, motivos [CATEGORIA_INELEGIVEL]; a tabela padrão nã
          +o é consultada.                                               
      783 +- Lote de exemplo (CC-ENG-PLATAFORMA, hospedagem com limite 0,
          +00): d-010                                                    
      784 +  (hospedagem, 480,00, com NF) → recusada, motivos [CATEGORIA_
          +INELEGIVEL];                                                  
      785 +  d-013 (hospedagem, 690,00, sem NF) → recusada, motivos      
      786 +  [CATEGORIA_INELEGIVEL, SEM_NOTA_FISCAL]; d-001 (alimentação,
          + 72,50) →                                                     
      787 +  limite 75,00; reembolso 72,50, integral.                    
      788 +- A saída do lote informa a tabela aplicada: CC-COMERCIAL para
          + c-0912,                                                      
      789 +  padrão para c-1103.                                         
      790 +                                                              
      791 +### RN-014 — Conversão para reais                             
      792 +                                                              
      793 +**Regra:** os limites e o limiar de nota fiscal da política es
          +tão em reais,                                                 
      794 +e toda despesa é avaliada pelo seu valor em reais. Uma despesa
          + sem moeda, ou                                                
      795 +com moeda BRL, está em reais e não é convertida. Uma despesa e
          +m outra moeda                                                 
      796 +é convertida multiplicando-se o valor lançado, sem arredondame
          +nto prévio,                                                   
      797 +pela cotação dessa moeda, e o resultado é normalizado uma únic
          +a vez                                                         
      798 +(RN-011; AMB-030). A cotação é a da data da despesa; se essa d
          +ata não tem                                                   
      799 +cotação para a moeda, usa-se a cotação da data anterior mais r
          +ecente que a                                                  
      800 +tenha, nunca a de uma data posterior (AMB-027, AMB-028). Se nã
          +o há cotação                                                  
      801 +da moeda na data da despesa nem antes dela, ou se a moeda não 
          +tem cotação em                                                
      802 +data alguma, a despesa é erro de item, com a causa moeda sem c
          +otação                                                        
      803 +(AMB-028, AMB-029). O valor em reais é o único considerado em 
          +todo o                                                        
      804 +processamento: limite (RN-001, RN-002), exigência de nota fisc
          +al (RN-003;                                                   
      805 +AMB-031), duplicidade (RN-009; AMB-032) e reembolso. A moeda e
          +strangeira não                                                
      806 +caracteriza viagem e não amplia limite (AMB-034).             
      807 +**Origem:** política do RH v4, item B; decisões AMB-027 a AMB-
          +034                                                           
      808 +**Depende de:** RN-010 (moeda inválida, cotações inválidas); R
          +N-011                                                         
      809 +(normalização).                                               
      810 +**Aceite:** lote de c-0912 (CC-COMERCIAL), cotações de        
      811 +`exemplos/envelope/cambio.json`.                              
      812 +- e-002 (14/07, alimentação, 22,00 EUR, com NF) → cotação de 1
          +4/07, 5,93 →                                                  
      813 +  130,46; nota fiscal exigida e presente; reembolso 90,00, par
          +cial;                                                         
      814 +  excedente 40,46.                                            
      815 +- e-003 (15/07, alimentação, 14,50 EUR, sem NF) → 5,88 → 85,26
          +; nota fiscal                                                 
      816 +  não exigida; reembolso 85,26, integral.                     
      817 +- e-004 (18/07, sábado, alimentação, 30,00 EUR, com NF) → 18/0
          +7 não tem                                                     
      818 +  cotação; usa-se a de 17/07, 5,96, e não a de 20/07 → 178,80;
          + reembolso                                                    
      819 +  90,00, parcial; excedente 88,80; a saída informa a cotação d
          +e 17/07.                                                      
      820 +- e-005 (20/07, transporte urbano, 40,00 USD, sem NF) → 5,50 →
          + 220,00; nota                                                 
      821 +  fiscal exigida e ausente; recusada, motivos [SEM_NOTA_FISCAL
          +].                                                            
      822 +- e-006 (21/07, representação, 55,00 GBP) → nenhuma cotação de
          + GBP; erro de                                                 
      823 +  item; causa: moeda sem cotação.                             
      824 +- e-010 (27/07, alimentação, 88,00, sem moeda) → em reais; ree
          +mbolso 88,00,                                                 
      825 +  integral.                                                   
      826 +- f-004 (21/07, transporte urbano, 12,00 USD, com NF; CC-SUPOR
          +TE-N2, tabela                                                 
      827 +  padrão) → 5,48 → 65,76; reembolso 65,76, integral.          
      828 +- Despesa de 10,00 EUR em 02/07 → nenhuma cotação de EUR em 02
          +/07 nem antes;                                                
      829 +  erro de item; causa: moeda sem cotação.                     
      830 +- Despesa de 10,00 USD em 31/07 → usa-se a cotação de 28/07, 5
          +,51 → 55,10.                                                  
      831 +                                                              
      832  ---
      833  
      834  ## 6. Ambiguidades identificadas e decisões

● Agora os ajustes nas AMB existentes que o envelope alcança: AMB-007, AMB-013 e
  AMB-015.

● Bash(S=specs/001-motor-reembolso/spec.md; n=$(grep -n '^### AMB-015' $S | cut
      -d: -f1); sed -n "${n},$((n+60))p" $S; grep -n 'todos os limites são os
      nominais' -A2 $…)
  ⎿  (No output)

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 2 lines, removed 2 lines
      1456  efeito; (b) mantê-los fora do cálculo mas reproduzi-los na sa
            ída; (c)
      1457  atribuir-lhes efeito sobre limites ou elegibilidade.
      1458  **Decisão:** adotada a leitura (a), aplicada a todos os campo
            s da entrada que
      1459 -nenhuma regra utiliza. São eles, na entrada de referência: o 
           -identificador, o                                             
      1460 -nome e o centro de custo do colaborador; o rótulo de competên
           -cia do período                                               
      1459 +nenhuma regra utiliza. São eles, na entrada de referência: o 
           +identificador e                                              
      1460 +o nome do colaborador; o rótulo de competência do período    
      1461  (AMB-016); e a descrição da despesa, que não é utilizada por 
            regra alguma
      1462  (AMB-007, AMB-008, AMB-009). Esses campos são aceitos na entr
            ada, não
      1463  participam de nenhuma regra, não afetam nenhum resultado, e s
            ão opcionais no

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 11 lines, removed 7 lines
      1482  critério único evita decisões separadas para cada um.
      1483  **Custo aceito:** se a política passar a estabelecer regras p
            or centro de
      1484  custo — limites diferenciados, por exemplo — a spec precisará
             ser alterada
      1485 -para incorporar o campo. Hoje não há perda, pois nenhum resul
           -tado depende                                                 
      1486 -dele.                                                        
      1485 +para incorporar o campo.                                     
      1486 +**Revisão pela política v4 (D-012):** o custo aceito acima se
           + materializou.                                               
      1487 +O centro de custo deixou de pertencer a esta decisão: ele sel
           +eciona a tabela                                              
      1488 +de limites (RN-013; AMB-022 a AMB-025), e na sua ausência ou 
           +com tipo                                                     
      1489 +incompatível o lote usa a tabela padrão (AMB-024), em vez de 
           +o campo                                                      
      1490 +simplesmente não ter efeito. A moeda, campo novo e não obriga
           +tório, também                                                
      1491 +não segue esta decisão: informada com tipo incompatível, é er
           +ro de item, e                                                
      1492 +não ausência (AMB-033). Para os demais campos listados, a dec
           +isão vale como                                               
      1493 +estava.                                                      
      1494  **Consequências verificáveis:**
      1488 -- o centro de custo CC-ENG-PLATAFORMA do lote de exemplo não 
           -altera                                                       
      1489 -  nenhum resultado                                           
      1490 -- um lote cujo colaborador não traga centro de custo é proces
           -sado normalmente                                             
      1491 -- dois lotes idênticos em tudo exceto o centro de custo produ
           -zem resultados                                               
      1492 -  idênticos                                                  
      1495 +- dois lotes idênticos em tudo exceto o identificador ou o no
           +me do                                                        
      1496 +  colaborador produzem resultados idênticos                  
      1497  - uma despesa sem descrição é processada normalmente
      1498  - um lote sem rótulo de competência, com início e fim present
            es, é processado
      1499    normalmente

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 5 lines, removed 2 lines
      1105  consegue comprovar, produzindo resultado que o auditor não te
            m como
      1106  verificar e que o colaborador pode influenciar.
      1107  **Consequências verificáveis:**
      1108 -- todos os limites são os nominais: alimentação 60,00; transp
           -orte urbano                                                  
      1109 -  80,00; hospedagem 250,00 por lançamento (uma diária)       
      1108 +- todos os limites são os da tabela aplicável (RN-013), sem a
           +mpliação; na                                                 
      1109 +  tabela padrão vigente, alimentação 60,00, transporte urbano
           + 80,00,                                                      
      1110 +  hospedagem 250,00 por lançamento (uma diária). Mantida pela
           + AMB-034 para a                                              
      1111 +  política v4: nem o acréscimo em viagem da política nem a mo
           +eda estrangeira                                              
      1112 +  ampliam limite                                             
      1113  - d-010 (hospedagem, 480,00, com NF) → limite 250,00, sem amp
            liação;
      1114    reembolso 250,00, parcial (AMB-009)
      1115  - d-011 (alimentação, 15/07, no mesmo fornecedor de d-010) → 
            limite 60,00;

● Bash(S=specs/001-motor-reembolso/spec.md; grep -n 'As categorias elegíveis são
      exatamente três — alimentacao' -A4 $S; n=$(grep -n '^## 7\. Casos de
      borda' $S | cut -…)
  ⎿  (No output)

● Vou editar o AMB-013 nas §7, §8, §9 e §10, tendo cuidado na §8 para evitar o
  padrão "→ CÓDIGO" que o teste OrdemMotivosSecao8Test verifica na seção
  inteira.

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 5 lines, removed 3 lines
      1351  Unicode, independente de localidade (RN-008) — e removendo-se
             espaços nas
      1352  extremidades. Nenhuma
      1353  outra transformação é aplicada: acentuação, separadores e sin
            ônimos não são
      1354 -normalizados. As categorias elegíveis são exatamente três — a
           -limentacao,                                                  
      1355 -transporte_urbano e hospedagem, na grafia do vocabulário de c
           -ategoria                                                     
      1356 -(§4.2, Notação). Qualquer outra categoria é inelegível, e a  
      1354 +normalizados. As categorias elegíveis são exatamente as da ta
           +bela aplicável                                               
      1355 +ao lote (RN-013), na grafia do vocabulário de categoria (§4.2
           +, Notação); na                                               
      1356 +política v3 e na tabela padrão da v4, alimentacao, transporte
           +_urbano e                                                    
      1357 +hospedagem; no CC-COMERCIAL, também representacao (AMB-023, A
           +MB-026).                                                     
      1358 +Qualquer outra categoria é inelegível, e a                   
      1359  despesa é recusada com o motivo CATEGORIA_INELEGIVEL em sua l
            ista de motivos.
      1360  **Justificativa:** a categoria é campo estruturado com vocabu
            lário fechado, e
      1361  uniformizar a caixa não constitui interpretação de texto livr
            e — distingue-se,

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 6 lines
      1800  | Valor com mais de duas casas decimais | d-011: 15/07, alime
            ntação, 33,333, com NF | Valor normalizado 33,33 (arredondame
            nto, metade para cima) antes de qualquer regra; reembolso 33,
            33 (integral); a saída apresenta 33,33 | RN-011, RN-001 |
      1801  | Categoria fora das três elegíveis | d-005: 07/07, "coworkin
            g", 89,00, com NF | Reembolso 0,00 (recusado), motivos [CATEG
            ORIA_INELEGIVEL]; não é erro de entrada | RN-008 |
      1802  | Despesa em dia não útil | d-012: 18/07 (sábado), alimentaçã
            o, 47,20, com NF | Avaliada normalmente; nenhuma regra consid
            era o dia da semana. Reembolso 47,20 (integral) | RN-001, RN-
            002 |
      1803 +| Categoria nova num centro de custo e ausente da tabela padr
           +ão | e-001 (CC-COMERCIAL): 13/07, representação, 340,00 BRL, 
           +com NF; f-003 (CC-SUPORTE-N2): 17/07, representação, 190,00 B
           +RL, com NF | e-001: limite 300,00; reembolso 300,00 (parcial)
           +; excedente 40,00. f-003: tabela padrão, que não lista repres
           +entação; reembolso 0,00 (recusado), motivos [CATEGORIA_INELEG
           +IVEL] | RN-008, RN-013 |                                     
      1804 +| Categoria com limite 0,00 no centro de custo | d-010 no lot
           +e de exemplo (CC-ENG-PLATAFORMA): hospedagem, 480,00, com NF 
           +| Categoria inelegível naquele centro; reembolso 0,00 (recusa
           +do), motivos [CATEGORIA_INELEGIVEL]; não é parcial de 0,00 | 
           +RN-008, RN-013 |                                             
      1805 +| Despesa em moeda estrangeira acima do limite | e-002: 14/07
           +, alimentação, 22,00 EUR, com NF (CC-COMERCIAL) | Cotação de 
           +14/07, 5,93: 130,46; reembolso 90,00 (parcial); excedente 40,
           +46, em reais | RN-001, RN-002, RN-014 |                      
      1806 +| Moeda estrangeira em data sem cotação | e-004: 18/07 (sábad
           +o), alimentação, 30,00 EUR, com NF (CC-COMERCIAL) | Cotação d
           +e 17/07, a anterior mais recente, 5,96: 178,80; reembolso 90,
           +00 (parcial); excedente 88,80 | RN-014 |                     
      1807 +| Moeda estrangeira que só passa do limiar de nota fiscal dep
           +ois de convertida | e-005: 20/07, transporte urbano, 40,00 US
           +D, sem NF (CC-COMERCIAL) | 220,00 em reais: nota fiscal exigi
           +da e ausente; reembolso 0,00 (recusado), motivos [SEM_NOTA_FI
           +SCAL] | RN-003, RN-004, RN-014 |                             
      1808 +| Moeda sem cotação em data alguma | e-006: 21/07, representa
           +ção, 55,00 GBP, com NF | Erro de item; causa: moeda sem cotaç
           +ão; reembolso 0,00; as demais despesas são processadas | RN-0
           +10, RN-014 |                                                 
      1809  
      1810  ## 8. Ordem de aplicação das regras
      1811  

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 10 lines, removed 6 lines
      1811  
      1812  A avaliação tem três etapas:
      1813  
      1814 -0. **Normalização e validação.** Primeiro, todo valor monetár
           -io é                                                         
      1815 -   normalizado para duas casas decimais (RN-011). Em seguida,
           - a entrada é                                                 
      1816 -   validada (RN-010), com a faixa de valor avaliada sobre o v
           -alor já                                                      
      1817 -   normalizado. Um erro de lote rejeita a entrada inteira, e 
           -nenhuma despesa                                              
      1818 -   é processada. Uma despesa com erro de item é classificada 
           -como erro, com                                               
      1819 -   reembolso 0,00, e não segue para as etapas 1 e 2.         
      1814 +0. **Conversão, normalização e validação.** Primeiro, a tabel
           +a de limites                                                 
      1815 +   do lote é selecionada (RN-013). Em seguida, o valor de cad
           +a despesa em                                                 
      1816 +   moeda estrangeira é convertido para reais a partir do valo
           +r lançado, sem                                               
      1817 +   arredondamento prévio (RN-014). Depois, todo valor em reai
           +s é                                                          
      1818 +   normalizado para duas casas decimais, uma única vez (RN-01
           +1). Por fim, a                                               
      1819 +   entrada é validada (RN-010), com a faixa de valor avaliada
           + sobre o valor                                               
      1820 +   em reais já normalizado. Um erro de lote rejeita a entrada
           + inteira, e                                                  
      1821 +   nenhuma despesa é processada. Uma despesa com erro de item
           + — inclusive                                                 
      1822 +   por moeda sem cotação — é classificada como erro, com reem
           +bolso 0,00, e                                                
      1823 +   não segue para as etapas 1 e 2.                           
      1824  1. **Elegibilidade.** Para cada despesa sem erro de item, tod
            as as regras de
      1825     elegibilidade são avaliadas, sem interrupção na primeira q
            ue se aplica, e
      1826     cada uma que se aplica acrescenta seu motivo à lista de mo
            tivos (RN-007),

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 4 lines, removed 1 line
      1834     categoria (RN-001) e reembolso parcial (RN-002). Uma despe
            sa com qualquer
      1835     motivo de recusa tem reembolso 0,00 e não é avaliada contr
            a o limite.
      1836  
      1837 -A normalização monetária precede a validação de faixa porque,
           - na ordem                                                    
      1837 +A conversão precede a normalização porque arredondar o valor 
           +lançado e                                                    
      1838 +depois arredondar o convertido acumularia dois arredondamento
           +s: 33,333 EUR a                                              
      1839 +5,93 vale 197,66 convertendo antes e 197,65 normalizando ante
           +s (origem:                                                   
      1840 +AMB-030). A normalização monetária precede a validação de fai
           +xa porque, na ordem                                          
      1841  inversa, um valor de 0,004 seria aceito como positivo e passa
            ria a constar
      1842  como despesa de 0,00 classificada como integral (origem: AMB-
            014). A
      1843  validação precede as demais etapas porque uma despesa que não
             pode ser

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 3 lines, removed 2 lines
      1863    todas as causas detectáveis informadas — quando o período f
            alta ou tem tipo
      1864    incompatível, quando a data de início ou de fim falta ou é 
            inválida, quando
      1865    a data de início é posterior à de fim, quando as despesas n
            ão são
      1866 -  informadas ou a relação de despesas tem tipo incompatível, 
           -ou quando há                                                 
      1867 -  identificador repetido (RN-010, RN-012, AMB-020, §4.2).    
      1866 +  informadas ou a relação de despesas tem tipo incompatível, 
           +quando há                                                    
      1867 +  identificador repetido, ou quando a política ou as cotações
           + são inválidas                                               
      1868 +  (RN-010, RN-012, AMB-020, AMB-037, §4.2).                  
      1869  - [ ] Uma despesa com informação obrigatória ausente — inclus
            ive o
      1870    identificador —, tipo incompatível, data inexistente ou val
            or normalizado
      1871    menor ou igual a zero, ou que não é uma despesa, recebe cla
            ssificação erro,

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 5 lines
      1881  - [ ] Todo valor é normalizado para centavos, com a metade ar
            redondada para
      1882    cima, antes de qualquer regra, e é esse valor que a saída a
            presenta
      1883    (RN-011; d-011).
      1884 +- [ ] Uma despesa em moeda estrangeira é convertida para reai
           +s pela cotação                                               
      1885 +  da sua data ou, sem ela, pela anterior mais recente, a part
           +ir do valor                                                  
      1886 +  lançado e com uma única normalização; sem cotação aplicável
           +, ou com moeda                                               
      1887 +  inválida, é erro de item; a saída apresenta valor e moeda o
           +riginais e a                                                 
      1888 +  cotação usada (RN-010, RN-011, RN-014; e-002, e-004, e-006)
           +.                                                            
      1889  
      1890  **Elegibilidade**
      1891  

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 10 lines, removed 5 lines
      1893    incluídos, é recusada com FORA_DA_COMPETENCIA (RN-005, RN-0
            06; d-008,
      1894    d-014).
      1895  - [ ] A categoria é normalizada apenas quanto à caixa e aos e
            spaços nas
      1896 -  extremidades, e qualquer categoria fora de alimentacao, tra
           -nsporte_urbano                                               
      1897 -  e hospedagem é recusada com CATEGORIA_INELEGIVEL (RN-008; d
           --005, d-014).                                                
      1898 -- [ ] A nota fiscal é exigida quando o valor normalizado exce
           -de 100,00; se                                                
      1899 -  exigida e ausente, a despesa é recusada com SEM_NOTA_FISCAL
           - (RN-003,                                                    
      1900 -  RN-004; d-003, d-004, d-013).                              
      1896 +  extremidades, e qualquer categoria fora das listadas com li
           +mite maior que                                               
      1897 +  zero na tabela aplicável ao lote é recusada com CATEGORIA_I
           +NELEGIVEL                                                    
      1898 +  (RN-008, RN-013; d-005, d-014, d-010, f-003).              
      1899 +- [ ] A tabela aplicável é a do centro de custo do lote, comp
           +arado                                                        
      1900 +  exatamente, ou a padrão quando o centro de custo falta, é i
           +nválido ou não                                               
      1901 +  tem entrada na política; a padrão nunca completa a tabela d
           +e um centro                                                  
      1902 +  (RN-013; e-001, f-002, f-003).                             
      1903 +- [ ] A nota fiscal é exigida quando o valor normalizado em r
           +eais excede o                                                
      1904 +  limiar da política (100,00); se exigida e ausente, a despes
           +a é recusada                                                 
      1905 +  com SEM_NOTA_FISCAL (RN-003, RN-004; d-003, d-004, d-013, e
           +-005).                                                       
      1906  - [ ] Despesas que coincidem em data, categoria e fornecedor 
            normalizados e
      1907    valor normalizado formam grupo; mantém-se a de menor identi
            ficador pela
      1908    ordem dos pontos de código Unicode, e as demais recebem DUP
            LICATA; despesas

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 5 lines, removed 3 lines
      1912  
      1913  **Valor**
      1914  
      1915 -- [ ] Cada despesa é comparada isoladamente com o limite nomi
           -nal da sua                                                   
      1916 -  categoria — 60,00, 80,00 ou 250,00 por lançamento —, sem so
           -ma entre                                                     
      1917 -  despesas e sem ampliação por viagem (RN-001; d-001, d-002, 
           -d-010).                                                      
      1915 +- [ ] Cada despesa é comparada isoladamente com o limite da s
           +ua categoria na                                              
      1916 +  tabela aplicável — na tabela padrão, 60,00, 80,00 ou 250,00
           + por                                                         
      1917 +  lançamento —, sem soma entre despesas e sem ampliação por v
           +iagem, nem pelo                                              
      1918 +  acréscimo da política nem por moeda estrangeira (RN-001, RN
           +-013; d-001,                                                 
      1919 +  d-002, d-010, e-007).                                      
      1920  - [ ] Uma despesa acima do limite recebe o limite e é classif
            icada como
      1921    parcial, com o excedente informado; uma despesa igual ao li
            mite ou abaixo
      1922    dele é integral (RN-002).

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 3 lines, removed 3 lines
      1926  
      1927  - [ ] Cada despesa da entrada aparece exatamente uma vez, na 
            ordem da
      1928    entrada, com as informações da §4.2 aplicáveis à sua classi
            ficação.
      1929 -- [ ] O consolidado traz o período, o total reembolsado — igu
           -al à soma dos                                                
      1930 -  reembolsos — e a contagem por classificação, cuja soma é ig
           -ual ao número                                                
      1931 -  de despesas (§4.2).                                        
      1929 +- [ ] O consolidado traz o período, a tabela de limites aplic
           +ada, o total                                                 
      1930 +  reembolsado — igual à soma dos reembolsos — e a contagem po
           +r                                                            
      1931 +  classificação, cuja soma é igual ao número de despesas (§4.
           +2).                                                          
      1932  - [ ] A mesma entrada produz sempre a mesma saída, e inverter
             a ordem das
      1933    despesas não altera o resultado de nenhuma delas (RN-001, R
            N-009,
      1934    AMB-018).

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 39 lines, removed 6 lines
      1937  
      1938  **Conformidade**
      1939  
      1940 -- [ ] Todos os aceites das RN-001 a RN-012 e todos os casos d
           -e borda da §7                                                
      1940 +- [ ] Todos os aceites das RN-001 a RN-014 e todos os casos d
           +e borda da §7                                                
      1941    são satisfeitos.
      1942 -- [ ] O lote de exemplo produz exatamente estes resultados:  
      1942  
      1943 +Os três lotes abaixo são avaliados com `exemplos/envelope/pol
           +itica-v4.json`                                               
      1944 +e `exemplos/envelope/cambio.json`.                           
      1945 +                                                             
      1946 +- [ ] O lote de exemplo (`exemplos/despesas-exemplo.json`, CC
           +-ENG-PLATAFORMA:                                             
      1947 +  alimentação 75,00, transporte urbano 80,00, hospedagem 0,00
           +) produz                                                     
      1948 +  exatamente estes resultados:                               
      1949 +                                                             
      1950  | d-001 | d-002 | d-003 | d-004 | d-005 | d-006 | d-007 |
      1951  |---|---|---|---|---|---|---|
      1946 -| 60,00 parcial | 38,00 integral | 80,00 parcial | 0,00 recus
           -ada [SEM_NOTA_FISCAL] | 0,00 recusada [CATEGORIA_INELEGIVEL] 
           -| 54,90 integral | 0,00 recusada [DUPLICATA] |               
      1952 +| 72,50 integral | 38,00 integral | 80,00 parcial | 0,00 recu
           +sada [SEM_NOTA_FISCAL] | 0,00 recusada [CATEGORIA_INELEGIVEL]
           + | 54,90 integral | 0,00 recusada [DUPLICATA] |              
      1953  
      1954  | d-008 | d-009 | d-010 | d-011 | d-012 | d-013 | d-014 |
      1955  |---|---|---|---|---|---|---|
      1950 -| 0,00 recusada [FORA_DA_COMPETENCIA] | 0,00 erro | 250,00 pa
           -rcial | 33,33 integral | 47,20 integral | 0,00 recusada [SEM_
           -NOTA_FISCAL] | 60,00 parcial |                               
      1956 +| 0,00 recusada [FORA_DA_COMPETENCIA] | 0,00 erro | 0,00 recu
           +sada [CATEGORIA_INELEGIVEL] | 33,33 integral | 47,20 integral
           + | 0,00 recusada [CATEGORIA_INELEGIVEL, SEM_NOTA_FISCAL] | 61
           +,00 integral |                                               
      1957  
      1952 -  Consolidado: total reembolsado 623,43; 4 integrais, 4 parci
           -ais, 5                                                       
      1953 -  recusadas, 1 erro; 14 despesas.                            
      1958 +  Consolidado: tabela CC-ENG-PLATAFORMA; total reembolsado 38
           +6,93; 6                                                      
      1959 +  integrais, 1 parcial, 6 recusadas, 1 erro; 14 despesas. Sob
           + a política v3,                                              
      1960 +  o mesmo lote totalizava 623,43; a diferença vem de d-001 (+
           +12,50), d-014                                                
      1961 +  (+1,00) e d-010 (−250,00).                                 
      1962  
      1963 +- [ ] O lote `exemplos/envelope/despesas-envelope.json` (CC-C
           +OMERCIAL)                                                    
      1964 +  produz exatamente estes resultados:                        
      1965 +                                                             
      1966 +| e-001 | e-002 | e-003 | e-004 | e-005 |                    
      1967 +|---|---|---|---|---|                                        
      1968 +| 300,00 parcial | 90,00 parcial (130,46 de 22,00 EUR a 5,93)
           + | 85,26 integral (14,50 EUR a 5,88) | 90,00 parcial (178,80 
           +de 30,00 EUR a 5,96, cotação de 17/07) | 0,00 recusada [SEM_N
           +OTA_FISCAL] (220,00 de 40,00 USD a 5,50) |                   
      1969 +                                                             
      1970 +| e-006 | e-007 | e-008 | e-009 | e-010 |                    
      1971 +|---|---|---|---|---|                                        
      1972 +| 0,00 erro (moeda sem cotação) | 400,00 parcial | 90,00 parc
           +ial | 0,00 recusada [CATEGORIA_INELEGIVEL] | 88,00 integral |
      1973 +                                                             
      1974 +  Consolidado: tabela CC-COMERCIAL; total reembolsado 1.143,2
           +6; 2 integrais,                                              
      1975 +  5 parciais, 2 recusadas, 1 erro; 10 despesas.              
      1976 +                                                             
      1977 +- [ ] O lote `exemplos/envelope/despesas-envelope-cc-desconhe
           +cido.json`                                                   
      1978 +  (CC-SUPORTE-N2, sem entrada na política) produz exatamente 
           +estes                                                        
      1979 +  resultados:                                                
      1980 +                                                             
      1981 +| f-001 | f-002 | f-003 | f-004 |                            
      1982 +|---|---|---|---|                                            
      1983 +| 58,00 integral | 250,00 parcial | 0,00 recusada [CATEGORIA_
           +INELEGIVEL] | 65,76 integral (12,00 USD a 5,48) |            
      1984 +                                                             
      1985 +  Consolidado: tabela padrão; total reembolsado 373,76; 2 int
           +egrais, 1                                                    
      1986 +  parcial, 1 recusada, 0 erro; 4 despesas.                   
      1987 +                                                             
      1988  ## 10. O que fica em aberto
      1989  
      1990  - **Ausência de teto diário agregado (origem: AMB-001).** Com
             avaliação por

● Update(specs\001-motor-reembolso\spec.md)  ⎿  Added 4 lines, removed 2 lines
      1997    informal podem não emitir nota. A spec não diferencia forne
            cedores porque a
      1998    política não prevê essa distinção; acomodá-la exigiria regr
            a específica
      1999    sobre formas alternativas de comprovação, que caberia ao RH
             definir.
      2000 -- **Ampliação de limites em viagem (origem: AMB-007).** O ite
           -m 6 da política                                              
      2001 -  não é implementado porque a entrada não registra o estado d
           -e viagem. Para                                               
      2000 +- **Ampliação de limites em viagem (origem: AMB-007, AMB-034)
           +.** O item 6 da                                              
      2001 +  política v3 não é implementado porque a entrada não registr
           +a o estado de                                                
      2002 +  viagem, e a v4 trouxe o percentual de acréscimo no arquivo 
           +da política sem                                              
      2003 +  trazer o que caracteriza viagem. Para                      
      2004    implementá-lo seria necessário um campo explícito na entrad
            a, informado
      2005    pelo processo que autoriza a viagem — não pelo colaborador 
            que lança a
      2006    despesa. Enquanto isso não existir, colaboradores em viagem
             recebem menos

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 15 lines, removed 1 line
      2007    do que a política lhes garante, e a regularização cabe ao f
            inanceiro.
      2008  - **Dia útil e dia não útil (origem: §7, d-012).** A política
             não distingue
      2009    despesas em dia útil de despesas em fim de semana ou feriad
            o, e esta spec
      2010 -  também não o faz: o dia da semana não é considerado por nen
           -huma regra. Se o                                             
      2010 +  também não o faz: o dia da semana não é considerado por nen
           +huma regra. Ele                                              
      2011 +  só aparece indiretamente na conversão, porque não há cotaçã
           +o em dia não útil                                            
      2012 +  e a despesa usa a cotação anterior (RN-014; e-004). Se o   
      2013    RH pretende tratamento diferente para dias não úteis — exig
            ir justificativa
      2014    para plantão, por exemplo —, a política precisaria estabele
            cê-lo.
      2015 +- **Política por competência (origem: AMB-036).** O motor ava
           +lia o lote com a                                             
      2016 +  política que recebe. Se o RH quiser que lotes de competênci
           +as anteriores à                                              
      2017 +  vigência da v4 continuem sendo avaliados pela v3, a polític
           +a precisará                                                  
      2018 +  declarar a sua vigência por competência e a entrada, a comp
           +etência do lote,                                             
      2019 +  e caberá a quem executa o motor fornecer a política certa. 
      2020 +- **Fila de aprovação manual (origem: AMB-038).** O item C da
           + v4 não é                                                    
      2021 +  implementado nesta entrega. Na leitura adotada aqui, nenhum
           +a despesa dos                                                
      2022 +  lotes de exemplo tem reembolso acima de R$ 500,00; o maior 
           +é e-007, com                                                 
      2023 +  400,00.                                                    
      2024 +- **Centro de custo digitado com outra caixa (origem: AMB-025
           +).** Um código                                               
      2025 +  como "cc-comercial" cai na tabela padrão sem erro. A saída 
           +mostra a tabela                                              
      2026 +  aplicada (§4.2), o que torna o caso visível ao auditor, mas
           + não o impede.                                               

● Agora as 17 AMB novas (AMB-022 a AMB-038), no formato das existentes,
  inseridas logo depois da AMB-021:

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 417 lines
      1768  
      1769  ---
      1770  
      1771 +### AMB-022 — O que é a "política padrão"                    
      1772 +                                                             
      1773 +**Texto original do RH:** "Alguns centros de custo não têm en
           +trada na                                                     
      1774 +tabela. Nesse caso, aplica-se a política padrão." (v4, item A
           +) e "O motor                                                 
      1775 +precisa ler a política de fora, não de dentro do código."    
      1776 +**O que não está claro:** "política padrão" pode ser o bloco 
           +`padrao` do                                                  
      1777 +arquivo da política ou a política v3, que esta spec já implem
           +entava com                                                   
      1778 +limites fixos. Hoje os números coincidem (60,00, 80,00 e 250,
           +00), mas o                                                   
      1779 +arquivo "muda sem aviso".                                    
      1780 +**Decisão:** a política padrão é a tabela padrão do arquivo d
           +a política, lida                                             
      1781 +de fora a cada avaliação, como as tabelas dos centros de cust
           +o. Nenhum limite                                             
      1782 +permanece fixo no motor.                                     
      1783 +**Justificativa:** a v4 determina que a política seja lida de
           + fora. Manter a                                              
      1784 +v3 como padrão no código recriaria, para os lotes sem tabela 
           +própria,                                                     
      1785 +exatamente o acoplamento que a v4 remove: o financeiro mudari
           +a a tabela                                                   
      1786 +padrão e o motor continuaria pagando os valores antigos.     
      1787 +**Custo aceito:** sem o arquivo da política o motor não avali
           +a lote algum,                                                
      1788 +nem os que usariam a tabela padrão (AMB-037).                
      1789 +**Consequências verificáveis:**                              
      1790 +- f-002 (hospedagem, 310,00, com NF; CC-SUPORTE-N2) → limite 
           +250,00, o da                                                 
      1791 +  tabela padrão do arquivo; reembolso 250,00, parcial        
      1792 +- alterar o limite de alimentação da tabela padrão do arquivo
           + para 70,00 →                                                
      1793 +  uma alimentação de 65,00 num lote sem centro de custo passa
           + a ser integral                                              
      1794 +                                                             
      1795 +**Regra afetada:** RN-013; RN-001, RN-003 (limites e limiar l
           +idos da                                                      
      1796 +política); §4.1                                              
      1797 +                                                             
      1798 +### AMB-023 — Alcance da política padrão: centro ausente ou c
           +ategoria ausente                                             
      1799 +                                                             
      1800 +**Texto original do RH:** "Alguns centros de custo não têm en
           +trada na                                                     
      1801 +tabela. Nesse caso, aplica-se a política padrão." (v4, item A
           +)                                                            
      1802 +**O que não está claro:** se a tabela padrão vale só para o c
           +entro de custo                                               
      1803 +sem entrada, ou também para a categoria que um centro listado
           + não traz — o                                                
      1804 +CC-ADM não lista hospedagem. E, em consequência, se `represen
           +tacao`, que só                                               
      1805 +existe na tabela do CC-COMERCIAL, é elegível para um lote sob
           + a tabela                                                    
      1806 +padrão. As leituras consideradas foram: (a) só centro ausente
           +; (b) também                                                 
      1807 +categoria ausente, completando a tabela do centro com a padrã
           +o.                                                           
      1808 +**Decisão:** adotada a leitura (a). A tabela padrão aplica-se
           + apenas quando                                               
      1809 +o centro de custo não tem entrada na política. Uma categoria 
           +ausente da                                                   
      1810 +tabela de um centro listado é inelegível para aquele centro. 
           +Sob a tabela                                                 
      1811 +padrão, que não lista `representacao`, essa categoria é inele
           +gível.                                                       
      1812 +**Justificativa:** o comunicado fala em centros sem entrada, 
           +não em                                                       
      1813 +categorias sem entrada. Um centro que tem tabela e não lista 
           +uma categoria                                                
      1814 +foi configurado assim pelo financeiro; completar a tabela com
           + a padrão seria                                              
      1815 +o motor supor uma omissão onde há uma escolha, e pagar uma ca
           +tegoria que o                                                
      1816 +centro não paga.                                             
      1817 +**Custo aceito:** se a ausência for um esquecimento do financ
           +eiro, a despesa                                              
      1818 +é recusada até que a tabela seja corrigida.                  
      1819 +**Consequências verificáveis:**                              
      1820 +- hospedagem de 200,00, com NF, num lote do CC-ADM → recusada
           +, motivos                                                    
      1821 +  [CATEGORIA_INELEGIVEL]                                     
      1822 +- f-003 (representação, 190,00 BRL, com NF; CC-SUPORTE-N2, ta
           +bela padrão) →                                               
      1823 +  recusada, motivos [CATEGORIA_INELEGIVEL]                   
      1824 +- e-001 (representação, 340,00 BRL, com NF; CC-COMERCIAL) → e
           +legível; limite                                              
      1825 +  300,00                                                     
      1826 +                                                             
      1827 +**Regra afetada:** RN-013; RN-008 (elegibilidade por tabela) 
      1828 +                                                             
      1829 +### AMB-024 — Lote sem centro de custo utilizável            
      1830 +                                                             
      1831 +**Texto original do RH:** não há. O comunicado trata de centr
           +os sem entrada                                               
      1832 +na tabela, não de lotes sem centro de custo; até a v3 o campo
           + era opcional e                                              
      1833 +sem efeito (AMB-015).                                        
      1834 +**O que não está claro:** o que fazer com um lote que não inf
           +orma o centro                                                
      1835 +de custo, que o informa sem valor ou com tipo incompatível. A
           +s leituras                                                   
      1836 +consideradas foram: (a) tabela padrão; (b) erro de lote.     
      1837 +**Decisão:** adotada a leitura (a). O lote usa a tabela padrã
           +o.                                                           
      1838 +**Justificativa:** o campo era opcional até a v4, e lotes sem
           + ele eram                                                    
      1839 +válidos. Rejeitá-los agora mudaria o contrato de entrada por 
           +uma informação                                               
      1840 +que tem, na política, um destino natural: um centro sem tabel
           +a própria já                                                 
      1841 +leva à padrão, e um lote sem centro é o caso extremo disso.  
      1842 +**Custo aceito:** um lote sem centro de custo é avaliado com 
           +a tabela padrão                                              
      1843 +sem aviso, em vez de rejeitado; se o colaborador pertencia a 
           +um centro com                                                
      1844 +limites maiores, recebe menos. A saída mostra a tabela aplica
           +da (§4.2).                                                   
      1845 +**Consequências verificáveis:**                              
      1846 +- lote sem centro de custo, alimentação de 72,50 → limite 60,
           +00; reembolso                                                
      1847 +  60,00, parcial                                             
      1848 +- lote com centro de custo informado como número → tabela pad
           +rão; processado                                              
      1849 +  normalmente                                                
      1850 +                                                             
      1851 +**Regra afetada:** RN-013; RN-010; AMB-015 (o centro de custo
           + deixa de ser                                                
      1852 +campo sem efeito)                                            
      1853 +                                                             
      1854 +### AMB-025 — Comparação do código do centro de custo        
      1855 +                                                             
      1856 +**Texto original do RH:** não há. Os códigos aparecem na polí
           +tica como                                                    
      1857 +"CC-COMERCIAL", "CC-ENG-PLATAFORMA", "CC-ADM".               
      1858 +**O que não está claro:** se o código informado no lote é com
           +parado                                                       
      1859 +exatamente ou após normalização de caixa e espaços, como a ca
           +tegoria e o                                                  
      1860 +fornecedor (RN-008).                                         
      1861 +**Decisão:** o código é comparado exatamente como informado, 
           +sem conversão de                                             
      1862 +caixa nem remoção de espaços. Um código que não coincide com 
           +nenhuma entrada                                              
      1863 +leva à tabela padrão (AMB-024).                              
      1864 +**Justificativa:** o centro de custo é identificador de siste
           +ma, atribuído e                                              
      1865 +transmitido por sistemas, não texto digitado por pessoa como 
           +a categoria e o                                              
      1866 +fornecedor. A RN-012 já estabeleceu o critério: normaliza-se 
           +o que é termo                                                
      1867 +de negócio, não o que é chave.                               
      1868 +**Custo aceito:** "cc-comercial" ou " CC-COMERCIAL" caem na t
           +abela padrão em                                              
      1869 +silêncio, com limites possivelmente menores.                 
      1870 +**Consequências verificáveis:**                              
      1871 +- lote com centro "cc-comercial", alimentação de 85,00 → tabe
           +la padrão;                                                   
      1872 +  reembolso 60,00, parcial                                   
      1873 +- lote com centro "CC-COMERCIAL", a mesma despesa → limite 90
           +,00; reembolso                                               
      1874 +  85,00, integral                                            
      1875 +                                                             
      1876 +**Regra afetada:** RN-013                                    
      1877 +                                                             
      1878 +### AMB-026 — Categoria com limite 0,00                      
      1879 +                                                             
      1880 +**Texto original do RH:** "`CC-ENG-PLATAFORMA` não reembolsa 
           +`hospedagem` de                                              
      1881 +forma alguma." (v4, item A). Na tabela, hospedagem do CC-ENG-
           +PLATAFORMA tem                                               
      1882 +limite 0,00 e a observação "nao reembolsavel".               
      1883 +**O que não está claro:** se uma categoria com limite 0,00 é 
           +uma categoria                                                
      1884 +elegível cujo teto é zero — e então a despesa seria parcial, 
           +com reembolso                                                
      1885 +0,00 e todo o valor como excedente — ou uma proibição. As lei
           +turas                                                        
      1886 +consideradas foram: (a) recusa por CATEGORIA_INELEGIVEL; (b) 
           +recusa com um                                                
      1887 +motivo novo; (c) parcial de 0,00.                            
      1888 +**Decisão:** adotada a leitura (a). Uma categoria com limite 
           +0,00 na tabela                                               
      1889 +aplicável é inelegível para o lote, e a despesa é recusada co
           +m                                                            
      1890 +CATEGORIA_INELEGIVEL. O critério é o limite 0,00, não o texto
           + da                                                          
      1891 +observação, que é informativo e não é interpretado.          
      1892 +**Justificativa:** limite zero é proibição, não teto. Classif
           +icar como                                                    
      1893 +parcial produziria uma saída que diz "aceita" sobre uma despe
           +sa que não paga                                              
      1894 +nada. Para o colaborador e o auditor, "este centro não reembo
           +lsa hospedagem"                                              
      1895 +é a mesma situação de uma categoria ausente da tabela, e o mo
           +tivo existente                                               
      1896 +a descreve; um motivo novo distinguiria duas formas de config
           +urar a mesma                                                 
      1897 +proibição. O critério não pode ser a observação porque o moto
           +r não                                                        
      1898 +interpreta texto livre (AMB-015), e um limite 0,00 sem observ
           +ação teria de                                                
      1899 +ter o mesmo efeito.                                          
      1900 +**Custo aceito:** a saída não distingue "categoria ausente da
           + tabela" de                                                  
      1901 +"categoria com limite 0,00".                                 
      1902 +**Consequências verificáveis:**                              
      1903 +- d-010 no lote de exemplo (CC-ENG-PLATAFORMA; hospedagem, 48
           +0,00, com NF) →                                              
      1904 +  recusada, motivos [CATEGORIA_INELEGIVEL]                   
      1905 +- d-013 no lote de exemplo (hospedagem, 690,00, sem NF) → rec
           +usada, motivos                                               
      1906 +  [CATEGORIA_INELEGIVEL, SEM_NOTA_FISCAL]                    
      1907 +- o consolidado do lote de exemplo conta d-010 como recusada,
           + não como                                                    
      1908 +  parcial: 6 recusadas e 1 parcial (§9)                      
      1909 +                                                             
      1910 +**Regra afetada:** RN-013; RN-008                            
      1911 +                                                             
      1912 +### AMB-027 — Data da despesa sem cotação                    
      1913 +                                                             
      1914 +**Texto original do RH:** "A conversão usa a taxa da data da 
           +despesa, não a                                               
      1915 +taxa de hoje." (v4, item B). O arquivo de câmbio declara "Cot
           +acoes publicadas                                             
      1916 +apenas em dias uteis bancarios".                             
      1917 +**O que não está claro:** que taxa usar quando a data da desp
           +esa não tem                                                  
      1918 +cotação, como e-004 (sábado, 18/07). As leituras consideradas
           + foram: (a) a                                                
      1919 +cotação anterior mais recente; (b) a seguinte mais próxima; (
           +c) erro de                                                   
      1920 +item; (d) recusa.                                            
      1921 +**Decisão:** adotada a leitura (a). Sem cotação na data da de
           +spesa, usa-se a                                              
      1922 +da data anterior mais recente que tenha cotação para a moeda,
           + nunca a de uma                                              
      1923 +data posterior.                                              
      1924 +**Justificativa:** no momento em que a despesa ocorre, a últi
           +ma cotação                                                   
      1925 +publicada é a do dia útil anterior; uma cotação futura não ex
           +istia. Recusar                                               
      1926 +ou tratar como erro uma despesa legítima de fim de semana pun
           +iria o                                                       
      1927 +colaborador pela agenda do mercado.                          
      1928 +**Custo aceito:** a despesa de sábado é convertida pela cotaç
           +ão de sexta,                                                 
      1929 +ainda que a de segunda seja conhecida quando o lote é avaliad
           +o.                                                           
      1930 +**Consequências verificáveis:**                              
      1931 +- e-004 (18/07, 30,00 EUR) → cotação de 17/07, 5,96 → 178,80;
           + reembolso                                                   
      1932 +  90,00, parcial; excedente 88,80, e não 90,30, que resultari
           +a de 20/07                                                   
      1933 +- a saída de e-004 informa a cotação e a data 17/07 (§4.2)   
      1934 +                                                             
      1935 +**Regra afetada:** RN-014                                    
      1936 +                                                             
      1937 +### AMB-028 — Data fora da faixa das cotações                
      1938 +                                                             
      1939 +**Texto original do RH:** não há. O arquivo de câmbio cobre 1
           +3/07 a 28/07; o                                              
      1940 +período do lote vai de 01/07 a 31/07.                        
      1941 +**O que não está claro:** o que fazer com uma despesa em moed
           +a estrangeira                                                
      1942 +cuja data é anterior à primeira cotação ou posterior à última
           +.                                                            
      1943 +**Decisão:** vale a mesma regra da AMB-027: usa-se a cotação 
           +anterior mais                                                
      1944 +recente. Se não há nenhuma cotação da moeda na data da despes
           +a nem antes                                                  
      1945 +dela, a despesa é erro de item, com a causa moeda sem cotação
           +.                                                            
      1946 +**Justificativa:** depois da última cotação, a anterior mais 
           +recente é a                                                  
      1947 +última publicada, pela mesma razão da AMB-027. Antes da prime
           +ira, não há                                                  
      1948 +cotação que existisse no momento da despesa, e usar uma poste
           +rior                                                         
      1949 +contrariaria a AMB-027; a despesa não pode ser expressa em re
           +ais.                                                         
      1950 +**Custo aceito:** uma despesa no início do período, antes da 
           +primeira                                                     
      1951 +cotação do arquivo, é erro até que o financeiro complete as c
           +otações.                                                     
      1952 +**Consequências verificáveis:**                              
      1953 +- 10,00 USD em 31/07 → cotação de 28/07, 5,51 → 55,10        
      1954 +- 10,00 EUR em 02/07 → erro de item; causa: moeda sem cotação
      1955 +                                                             
      1956 +**Regra afetada:** RN-014; RN-010                            
      1957 +                                                             
      1958 +### AMB-029 — Moeda sem cotação em data alguma               
      1959 +                                                             
      1960 +**Texto original do RH:** não há. e-006 está em GBP, e o arqu
           +ivo de câmbio só                                             
      1961 +tem USD e EUR.                                               
      1962 +**O que não está claro:** o que fazer com uma moeda que não t
           +em cotação em                                                
      1963 +nenhuma data. Não é caso da AMB-027: em 21/07, a data de e-00
           +6, há cotações,                                              
      1964 +só que não de GBP. As leituras consideradas foram: (a) erro d
           +e item; (b)                                                  
      1965 +recusa com um motivo novo; (c) erro de lote.                 
      1966 +**Decisão:** adotada a leitura (a). A despesa é erro de item,
           + com a causa                                                 
      1967 +moeda sem cotação. As demais despesas do lote são processadas
           +.                                                            
      1968 +**Justificativa:** a despesa não pode ser expressa em reais, 
           +e por isso não                                               
      1969 +pode ser comparada a limite nem a limiar: é dado que o motor 
           +não consegue                                                 
      1970 +processar, e não uma decisão de negócio sobre a despesa, que 
           +é o que a                                                    
      1971 +recusa expressa (AMB-017). O defeito é da despesa, não do lot
           +e, pelo critério                                             
      1972 +de abrangência da AMB-020.                                   
      1973 +**Custo aceito:** uma despesa legítima em moeda não coberta n
           +ão é paga até                                                
      1974 +que o financeiro inclua a moeda nas cotações.                
      1975 +**Consequências verificáveis:**                              
      1976 +- e-006 (55,00 GBP) → erro de item; causa: moeda sem cotação;
           + reembolso 0,00                                              
      1977 +- o lote de c-0912 é avaliado; e-006 conta como erro no conso
           +lidado                                                       
      1978 +                                                             
      1979 +**Regra afetada:** RN-014; RN-010; §4.2 (causa nova)         
      1980 +                                                             
      1981 +### AMB-030 — Ordem entre conversão e normalização           
      1982 +                                                             
      1983 +**Texto original do RH:** não há. A RN-011 normaliza todo val
           +or antes de                                                  
      1984 +qualquer regra; a v4 introduz a conversão.                   
      1985 +**O que não está claro:** se o valor lançado é normalizado pa
           +ra centavos                                                  
      1986 +antes de ser convertido, e o convertido normalizado de novo, 
           +ou se a                                                      
      1987 +conversão usa o valor cru e normaliza uma única vez.         
      1988 +**Decisão:** a conversão multiplica o valor lançado, sem arre
           +dondamento, pela                                             
      1989 +cotação, e o resultado é normalizado uma única vez (RN-011). 
      1990 +**Justificativa:** arredondar antes e depois acumula dois arr
           +edondamentos, e                                              
      1991 +o segundo amplifica o primeiro pela cotação. Normalizar uma v
           +ez, no valor em                                              
      1992 +reais, mantém a RN-011 como o único ponto de arredondamento e
           + a fronteira                                                 
      1993 +verificável.                                                 
      1994 +**Custo aceito:** nenhum identificado; os casos do envelope d
           +ão centavos                                                  
      1995 +exatos nas duas ordens.                                      
      1996 +**Consequências verificáveis:**                              
      1997 +- 33,333 EUR a 5,93 → 197,66469 → 197,66 (normalizando antes 
           +daria 197,65)                                                
      1998 +- e-002 (22,00 EUR a 5,93) → 130,46 nas duas ordens          
      1999 +                                                             
      2000 +**Regra afetada:** RN-011; RN-014; §8                        
      2001 +                                                             
      2002 +### AMB-031 — Moeda do limiar de nota fiscal                 
      2003 +                                                             
      2004 +**Texto original do RH:** "Os limites da política são sempre 
           +em BRL. Uma                                                  
      2005 +despesa em EUR é convertida antes de ser comparada ao limite.
           +" (v4, item B)                                               
      2006 +**O que não está claro:** se o limiar de nota fiscal é compar
           +ado com o valor                                              
      2007 +convertido para reais ou com o valor na moeda original. O com
           +unicado fala                                                 
      2008 +de limites, não do limiar. e-005 (40,00 USD = 220,00, sem NF)
           + muda de 0,00 a                                              
      2009 +150,00 conforme a leitura.                                   
      2010 +**Decisão:** o limiar compara o valor já convertido para reai
           +s.                                                           
      2011 +**Justificativa:** o limiar está em reais, como os limites. C
           +omparar 40,00                                                
      2012 +dólares com 100,00 reais seria comparar unidades diferentes, 
           +e o resultado                                                
      2013 +dependeria da moeda em que a despesa foi lançada, não do seu 
           +valor.                                                       
      2014 +**Custo aceito:** e-005 perde 150,00 por falta de uma nota qu
           +e, em dólares,                                               
      2015 +parecia dispensável.                                         
      2016 +**Consequências verificáveis:**                              
      2017 +- e-005 → nota fiscal exigida e ausente; recusada, motivos [S
           +EM_NOTA_FISCAL]                                              
      2018 +- e-003 (14,50 EUR = 85,26, sem NF) → nota fiscal não exigida
      2019 +                                                             
      2020 +**Regra afetada:** RN-003; RN-014                            
      2021 +                                                             
      2022 +### AMB-032 — Valor da chave de duplicidade                  
      2023 +                                                             
      2024 +**Texto original do RH:** não há. A RN-009 compara o valor no
           +rmalizado.                                                   
      2025 +**O que não está claro:** se a chave usa o valor em reais ou 
           +o valor e a                                                  
      2026 +moeda originais.                                             
      2027 +**Decisão:** a chave usa o valor normalizado em reais; a moed
           +a não integra a                                              
      2028 +chave.                                                       
      2029 +**Justificativa:** pela mesma razão da AMB-031: todas as regr
           +as operam sobre                                              
      2030 +o valor em reais, e a duplicidade identifica a mesma despesa 
           +lançada duas                                                 
      2031 +vezes, que tem o mesmo valor qualquer que seja a moeda do reg
           +istro.                                                       
      2032 +**Custo aceito:** duas despesas diferentes que coincidam no m
           +esmo dia,                                                    
      2033 +categoria, fornecedor e valor em reais, uma em moeda estrange
           +ira, formam                                                  
      2034 +grupo — coincidência improvável, pelo arredondamento da conve
           +rsão.                                                        
      2035 +**Consequências verificáveis:**                              
      2036 +- 22,00 EUR em 14/07 e 130,46 BRL, no mesmo dia, categoria e 
           +fornecedor →                                                 
      2037 +  grupo de duplicatas                                        
      2038 +- 22,00 EUR e 22,00 BRL, idem → não são duplicatas           
      2039 +                                                             
      2040 +**Regra afetada:** RN-009; RN-014                            
      2041 +                                                             
      2042 +### AMB-033 — Moeda informada de forma inválida              
      2043 +                                                             
      2044 +**Texto original do RH:** "A entrada agora pode trazer um cam
           +po `moeda` (ISO                                              
      2045 +4217). Quando ausente, assume-se `BRL`." (v4, item B)        
      2046 +**O que não está claro:** o que fazer com uma moeda informada
           + sem valor,                                                  
      2047 +vazia, de tipo incompatível ou fora do formato ISO 4217 — "eu
           +r", "EURO". Pela                                             
      2048 +regra geral da AMB-015, uma informação não obrigatória com ti
           +po incompatível                                              
      2049 +seria tratada como ausente, e então em reais.                
      2050 +**Decisão:** a moeda informada sem valor, vazia, de tipo inco
           +mpatível ou                                                  
      2051 +fora do formato de três letras maiúsculas é erro de item, com
           + a causa moeda                                               
      2052 +com tipo incompatível. Só a moeda não informada é BRL. Esta d
           +ecisão é uma                                                 
      2053 +exceção explícita à AMB-015. Um código no formato, mas sem co
           +tação, segue a                                               
      2054 +AMB-029.                                                     
      2055 +**Justificativa:** a moeda tem efeito no cálculo. Tratá-la co
           +mo ausente faria                                             
      2056 +o motor pagar em reais uma despesa lançada em outra moeda — 4
           +0,00 dólares                                                 
      2057 +como R$ 40,00 —, o erro que a AMB-015 não previa porque, na v
           +3, nenhum campo                                              
      2058 +opcional tinha efeito.                                       
      2059 +**Custo aceito:** um código em minúsculas, que um conferente 
           +reconheceria,                                                
      2060 +torna a despesa erro.                                        
      2061 +**Consequências verificáveis:**                              
      2062 +- moeda "eur" → erro de item; causa: moeda com tipo incompatí
           +vel                                                          
      2063 +- moeda informada sem valor → erro de item, e não BRL        
      2064 +- moeda não informada (e-010) → BRL                          
      2065 +                                                             
      2066 +**Regra afetada:** RN-010; RN-014; AMB-015 (exceção); §4.1   
      2067 +                                                             
      2068 +### AMB-034 — Viagem, moeda estrangeira e o acréscimo da polí
           +tica                                                         
      2069 +                                                             
      2070 +**Texto original do RH:** "Colaboradores em viagem internacio
           +nal lançam                                                   
      2071 +despesas em moeda estrangeira." (v4, item B). O arquivo da po
           +lítica traz                                                  
      2072 +`acrescimo_em_viagem_percentual: 50`.                        
      2073 +**O que não está claro:** se a moeda estrangeira caracteriza 
           +viagem e, com                                                
      2074 +ela, a ampliação de 50% que a AMB-007 deixou fora de escopo. 
           +As leituras                                                  
      2075 +consideradas foram: (a) manter a AMB-007; (b) viagem por desp
           +esa em moeda                                                 
      2076 +estrangeira; (c) o lote inteiro em viagem se houver alguma.  
      2077 +**Decisão:** adotada a leitura (a). A AMB-007 é mantida: viag
           +em continua fora                                             
      2078 +de escopo, e o acréscimo em viagem do arquivo da política é i
           +gnorado.                                                     
      2079 +**Justificativa:** o comunicado diz que quem viaja lança em m
           +oeda                                                         
      2080 +estrangeira, não que quem lança em moeda estrangeira está em 
           +viagem, e não                                                
      2081 +define o que caracteriza viagem. Inferir viagem da moeda seri
           +a o motor                                                    
      2082 +adivinhando um fato administrativo a partir de um indício, a 
           +objeção que                                                  
      2083 +decidiu a AMB-007; e-007, um hotel em Londres pago em reais, 
           +mostra que o                                                 
      2084 +indício nem é confiável.                                     
      2085 +**Custo aceito:** um colaborador em viagem real não recebe a 
           +ampliação de 50%:                                            
      2086 +e-002 recebe 90,00 em vez de 130,46; e-004, 90,00 em vez de 1
           +35,00.                                                       
      2087 +**Consequências verificáveis:**                              
      2088 +- e-002 (22,00 EUR = 130,46; CC-COMERCIAL) → limite 90,00; re
           +embolso 90,00,                                               
      2089 +  parcial                                                    
      2090 +- e-007 (hospedagem em Londres, 1.200,00 BRL) → limite 400,00
           +, sem                                                        
      2091 +  acréscimo                                                  
      2092 +                                                             
      2093 +**Regra afetada:** AMB-007; RN-001; RN-014; §3               
      2094 +                                                             
      2095 +### AMB-035 — Periodicidade dos limites na política          
      2096 +                                                             
      2097 +**Texto original do RH:** "`representacao` […] — limite de R$
           + 300 por dia."                                               
      2098 +(v4, item A). A tabela traz, para cada categoria, `periodicid
           +ade` "dia" ou                                                
      2099 +"diaria".                                                    
      2100 +**O que não está claro:** se a periodicidade declarada no arq
           +uivo reabre a                                                
      2101 +AMB-001 — limite por despesa ou por dia — e faz o motor somar
           + as despesas da                                              
      2102 +mesma categoria no mesmo dia.                                
      2103 +**Decisão:** a AMB-001 é mantida: o limite é por despesa. A p
           +eriodicidade é                                               
      2104 +metadado do arquivo, não regra nova, e não participa da avali
           +ação.                                                        
      2105 +**Justificativa:** "por dia" é a mesma redação da v3, que a A
           +MB-001 já                                                    
      2106 +decidiu; a v4 não diz que a decisão mudou. Tratar o campo com
           +o regra faria                                                
      2107 +o resultado de uma despesa depender de outras do mesmo dia, o
           + que a AMB-001                                               
      2108 +rejeitou.                                                    
      2109 +**Custo aceito:** se o financeiro pretendia um teto diário co
           +m a                                                          
      2110 +periodicidade, ele não é aplicado (§10).                     
      2111 +**Consequências verificáveis:**                              
      2112 +- d-001 (72,50) e d-002 (38,00) no lote de exemplo, ambas de 
           +alimentação em                                               
      2113 +  03/07, sob o CC-ENG-PLATAFORMA (75,00) → 72,50 e 38,00, tot
           +al de 110,50 no                                              
      2114 +  dia, e não 75,00                                           
      2115 +- nenhum lote do envelope tem duas despesas da mesma categori
           +a no mesmo dia;                                              
      2116 +  seus resultados não dependem desta decisão                 
      2117 +                                                             
      2118 +**Regra afetada:** AMB-001; RN-001                           
      2119 +                                                             
      2120 +### AMB-036 — Versão da política e retroatividade            
      2121 +                                                             
      2122 +**Texto original do RH:** "Vigência imediata, retroativa à co
           +mpetência                                                    
      2123 +atual." (v4). O arquivo declara `vigencia: 2026-07-01`.      
      2124 +**O que não está claro:** se o motor deve escolher a versão d
           +a política pela                                              
      2125 +competência do lote — v3 para lotes anteriores a 07/2026 — e,
           + portanto,                                                   
      2126 +manter as duas.                                              
      2127 +**Decisão:** o motor avalia o lote com a política que recebe,
           + sem                                                         
      2128 +versionamento. A vigência declarada no arquivo é informativa.
      2129 +**Justificativa:** o lote não declara qual versão da política
           + se aplica a                                                 
      2130 +ele, e decidir retroatividade é do RH, não do motor. Quem exe
           +cuta o motor                                                 
      2131 +fornece a política da competência que quer avaliar.          
      2132 +**Custo aceito:** um lote de junho avaliado com o arquivo da 
           +v4 recebe os                                                 
      2133 +limites da v4.                                               
      2134 +**Consequências verificáveis:**                              
      2135 +- o lote de exemplo (07/2026) avaliado com `politica-v4.json`
           + → resultado da                                              
      2136 +  §9 sob o CC-ENG-PLATAFORMA, total 386,93                   
      2137 +- a vigência do arquivo não é comparada com o período do lote
      2138 +                                                             
      2139 +**Regra afetada:** §3; §10                                   
      2140 +                                                             
      2141 +### AMB-037 — Política ou cotações inválidas                 
      2142 +                                                             
      2143 +**Texto original do RH:** não há. A v4 cria duas entradas que
           + o motor não                                                 
      2144 +controla: a política, que "muda sem aviso", e as cotações.   
      2145 +**O que não está claro:** o que o motor faz quando a política
           + ou as cotações                                              
      2146 +faltam, não podem ser lidas ou trazem valores impossíveis.   
      2147 +**Decisão:** política ausente, ilegível, sem tabela padrão, s
           +em limiar de                                                 
      2148 +nota fiscal ou com algum limite negativo, e cotações ausentes
           +, ilegíveis ou                                               
      2149 +com alguma taxa menor ou igual a zero, são erro de lote. Essa
           +s causas são                                                 
      2150 +informadas junto com as demais causas de erro de lote detectá
           +veis (RN-010).                                               
      2151 +**Justificativa:** sem política não há como avaliar nenhuma d
           +espesa, e uma                                                
      2152 +política com limite negativo ou cotação não positiva produzir
           +ia reembolsos                                                
      2153 +sem sentido. O defeito não é de uma despesa, é da base de ava
           +liação do lote                                               
      2154 +inteiro, pelo critério de abrangência da AMB-020.            
      2155 +**Custo aceito:** um lote só com despesas em reais é rejeitad
           +o se as                                                      
      2156 +cotações forem inválidas, e um defeito numa tabela que o lote
           + não usa                                                     
      2157 +rejeita o lote mesmo assim.                                  
      2158 +**Consequências verificáveis:**                              
      2159 +- política sem o arquivo → lote rejeitado                    
      2160 +- cotações com taxa 0,00 em alguma data → lote rejeitado, inc
           +lusive um lote                                               
      2161 +  só em BRL                                                  
      2162 +- política inválida e data de início posterior à de fim → as 
           +duas causas                                                  
      2163 +                                                             
      2164 +**Regra afetada:** RN-010; §4.1                              
      2165 +                                                             
      2166 +### AMB-038 — Fila de aprovação manual (item C)              
      2167 +                                                             
      2168 +**Texto original do RH:** "Itens cujo valor reembolsável pass
           +e de R$ 500 não                                              
      2169 +são mais aprovados automaticamente. Eles entram em estado de 
           +pendência                                                    
      2170 +aguardando aprovação do gestor." (v4, item C, opcional)      
      2171 +**O que não está claro:** se a fila entra nesta entrega e, se
           + entrar, o que é                                             
      2172 +"passe de" (estritamente maior?), se a pendência conta no tot
           +al reembolsado                                               
      2173 +e como o estado convive com as quatro classificações (RN-007)
           +.                                                            
      2174 +**Decisão:** o item C está fora do escopo desta entrega. Decl
           +arado, não                                                   
      2175 +esquecido.                                                   
      2176 +**Justificativa:** o comunicado o declara opcional, e impleme
           +ntá-lo exige um                                              
      2177 +estado novo de resultado, que altera a RN-007 e a §4.2. Fazê-
           +lo às pressas,                                               
      2178 +sem decidir as questões acima, deixaria a spec inconsistente.
      2179 +**Custo aceito:** despesas com reembolso acima de R$ 500,00 s
           +ão aprovadas                                                 
      2180 +automaticamente, contra o que o RH determina. Nos lotes do en
           +velope não há                                                
      2181 +nenhuma: o maior reembolso é e-007, 400,00.                  
      2182 +**Consequências verificáveis:**                              
      2183 +- nenhum resultado tem estado de pendência                   
      2184 +- e-007 → 400,00, parcial, sem pendência                     
      2185 +                                                             
      2186 +**Regra afetada:** §3; §10                                   
      2187 +                                                             
      2188  ## 7. Casos de borda
      2189  
      2190  > **Limites da tabela aplicável, sem ampliação por viagem:** 
            o sistema não

● As 17 AMB estão escritas. Agora procuro trechos que ficaram desatualizados,
  confiro todas as remissões a AMB e RN, e vejo o que o build diz dos testes que
  leem a spec:

● Bash(S=specs/001-motor-reembolso/spec.md; echo "--- termos possivelmente
      desatualizados"; grep -n 'três elegíveis\|fora das três\|limite
      nominal\|limites nominais\|e…)
  ⎿  2 files changed (binary, mode only or too large to show)
  ⎿  (timeout 5m)

● Confirmei que não há remissões sem cabeçalho (38 AMB) e que a §8 mantém as
  quatro setas esperadas pelo teste; vou rodar os testes novamente verificando o
  código de saída. Antes disso, corrijo os dois trechos desatualizados: a
  decisão da AMB-007 e uma linha da §7.

● Bash(S=specs/001-motor-reembolso/spec.md; sed -n '1074,1080p' $S)
  ⎿  (No output)

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 5 lines, removed 4 lines
      1073  fornecedor; (e) considerar todo o período em viagem se houver
             qualquer
      1074  hospedagem nele.
      1075  **Decisão:** adotada a leitura (a). A caracterização de estar
             em viagem está
      1076 -fora do escopo deste sistema. O motor aplica sempre os limite
           -s nominais de                                                
      1077 -cada categoria e o item 6 da política não é implementado. A p
           -remissa global                                               
      1078 -declarada nas seções 5 e 7 deixa de ser provisória e passa a 
           -ser regra                                                    
      1079 -permanente: todas as despesas são avaliadas com os limites no
           -minais.                                                      
      1076 +fora do escopo deste sistema. O motor aplica sempre os limite
           +s da tabela                                                  
      1077 +aplicável, sem ampliação, e o item 6 da política não é implem
           +entado. A                                                    
      1078 +premissa global declarada nas seções 5 e 7 deixa de ser provi
           +sória e passa a                                              
      1079 +ser regra permanente: todas as despesas são avaliadas sem amp
           +liação por                                                   
      1080 +viagem. Decisão mantida para a política v4 pela AMB-034.     
      1081  **Justificativa:** estar em viagem é um fato administrativo —
             alguém autoriza o
      1082  deslocamento — e a entrada não o registra. As leituras (c), (
            d) e (e)
      1083  deduzem a causa a partir de consequências e erram de formas p
            revisíveis: a

● Update(specs\001-motor-reembolso\spec.md)
  ⎿  Added 1 line, removed 1 line
      2216  | Par de despesas idênticas exceto pelo identificador; esta c
            om o maior | d-007: 09/07, alimentação, Bistro Central, 54,90
            , com NF | Grupo de duplicatas com d-006; reembolso 0,00 (rec
            usado), motivos [DUPLICATA]; o resultado não muda se a ordem 
            na entrada for invertida | RN-009 |
      2217  | Valor negativo (estorno) | d-009: 11/07, transporte urbano,
             −45,00, sem NF, "Estorno de corrida cancelada" | Erro de ite
            m (valor menor ou igual a zero); classificação erro; reembols
            o 0,00; não é avaliada por nenhuma regra de elegibilidade ou 
            de valor; não abate nenhuma outra despesa | RN-010 |
      2218  | Valor com mais de duas casas decimais | d-011: 15/07, alime
            ntação, 33,333, com NF | Valor normalizado 33,33 (arredondame
            nto, metade para cima) antes de qualquer regra; reembolso 33,
            33 (integral); a saída apresenta 33,33 | RN-011, RN-001 |
      2219 -| Categoria fora das três elegíveis | d-005: 07/07, "coworkin
           -g", 89,00, com NF | Reembolso 0,00 (recusado), motivos [CATEG
           -ORIA_INELEGIVEL]; não é erro de entrada | RN-008 |           
      2219 +| Categoria fora das elegíveis da tabela padrão | d-005: 07/0
           +7, "coworking", 89,00, com NF | Reembolso 0,00 (recusado), mo
           +tivos [CATEGORIA_INELEGIVEL]; não é erro de entrada | RN-008 
           +|                                                            
      2220  | Despesa em dia não útil | d-012: 18/07 (sábado), alimentaçã
            o, 47,20, com NF | Avaliada normalmente; nenhuma regra consid
            era o dia da semana. Reembolso 47,20 (integral) | RN-001, RN-
            002 |
      2221  | Categoria nova num centro de custo e ausente da tabela padr
            ão | e-001 (CC-COMERCIAL): 13/07, representação, 340,00 BRL, 
            com NF; f-003 (CC-SUPORTE-N2): 17/07, representação, 190,00 B
            RL, com NF | e-001: limite 300,00; reembolso 300,00 (parcial)
            ; excedente 40,00. f-003: tabela padrão, que não lista repres
            entação; reembolso 0,00 (recusado), motivos [CATEGORIA_INELEG
            IVEL] | RN-008, RN-013 |
      2222  | Categoria com limite 0,00 no centro de custo | d-010 no lot
            e de exemplo (CC-ENG-PLATAFORMA): hospedagem, 480,00, com NF 
            | Categoria inelegível naquele centro; reembolso 0,00 (recusa
            do), motivos [CATEGORIA_INELEGIVEL]; não é parcial de 0,00 | 
            RN-008, RN-013 |

● Bash(export JAVA_HOME="/c/Program Files/Android/Android Studio/jbr" &&
      ./gradlew test --rerun-tasks 2>&1 | grep -E 'FAILED|tests completed|BUILD'
      ; grep -l 'failures…)
  ⎿  5 files changed (binary, mode only or too large to show)
  ⎿  (timeout 5m)

● Confirmei que só o OrdemCamposSecao41Test falha, como esperado, por falta do
  campo "Moeda". Também identifiquei um problema de processo: o Gradle não trata
  specs/ como entrada da tarefa de teste, então mudanças só na spec não
  disparam reexecução — vou registrar isso como task. Agora vou salvar a análise
  em docs/analise-envelope.md e commitar junto com a spec, checando o status
  antes.

● Bash(SC=/c/Users/dev/AppData/Local/Temp/claude/c--Users-dev-SDD-Desafio/8aa9dd
      74-d4f2-42e8-b296-d294cb69c18d/scratchpad/relatorio-insumos && { printf
      '%s\n' '# Análi…)
  ⎿  Created docs\analise-envelope.md (+94 -0)
       1 +# Análise do envelope lacrado (Dia 2)                          
       2 +                                                               
       3 +> Feita em 2026-10-19, depois de commitar os arquivos do envelo
         +pe (`2aed7d1`) e                                               
       4 +> **antes** de qualquer mudança na spec. As ambiguidades listad
         +as aqui estão                                                  
       5 +> abertas de propósito: as decisões estão na spec 2.0, AMB-022 
         +a AMB-038, e o                                                 
       6 +> evento inteiro está registrado em DECISIONS.md, D-012.       
       7 +                                                               
       8 +                                                               
       9 +Arquivos analisados: exemplos/envelope/ (commit 2aed7d1): 00-EN
         +VELOPE-LACRADO.md,                                             
      10 +politica-v4.json, cambio.json, despesas-envelope.json, despesas
         +-envelope-cc-desconhecido.json.                                
      11 +                                                               
      12 +## Achado principal                                            
      13 +                                                               
      14 +O lote do Dia 1 (exemplos/despesas-exemplo.json) é do colaborad
         +or c-0417, centro de custo                                     
      15 +CC-ENG-PLATAFORMA. A v4 é "retroativa à competência atual" (07/
         +2026, a mesma do lote) e a                                     
      16 +tabela desse CC tem alimentação 75, transporte 80, hospedagem 0
         +,00 "não reembolsável".                                        
      17 +Consequência: a §9 da spec (total 623,43; 4 integrais, 4 parcia
         +is, 5 recusadas, 1 erro) deixa                                 
      18 +de valer. d-001 72,50 → integral 72,50 (era 60,00 parcial); d-0
         +14 61,00 → integral 61,00                                      
      19 +(era 60,00 parcial); d-010 hospedagem 480,00 → 0,00 (classifica
         +ção em aberto); d-013 pode                                     
      20 +ganhar CATEGORIA_INELEGIVEL além de SEM_NOTA_FISCAL. Total: 386
         +,93.                                                           
      21 +                                                               
      22 +## 1. O que quebra na spec                                     
      23 +                                                               
      24 +Seções: §3 (centro de custo "sem efeito" cai; viagem a rever); 
         +§4.1 (centro de custo deixa de                                 
      25 +ser sem efeito; falta campo moeda; política e câmbio viram entr
         +adas; a regra "informações não                                 
      26 +previstas são aceitas e ignoradas" faria o motor pagar e-002, 2
         +2,00 EUR, como R$ 22,00 em                                     
      27 +silêncio); §4.2 (valor considerado passa a ser convertido; audi
         +tor precisa de moeda, valor                                    
      28 +original e taxa); §7 (linhas com d-001, d-014, d-010, d-013 mud
         +am); §8 etapa 0 (onde entra a                                  
      29 +conversão); §9 (total e contagens caem); §10 (viagem).         
      30 +                                                               
      31 +RN: RN-001 (limites fixos 60/80/250 → tabela externa por CC; ac
         +eites de d-001 e d-010 caem);                                  
      32 +RN-002 (origem do limite; aceites genéricos só sob política pad
         +rão); RN-003 (limiar 100 vem do                                
      33 +arquivo; moeda de comparação em aberto); RN-008 ("exatamente tr
         +ês categorias" cai;                                            
      34 +representacao no CC-COMERCIAL; aceite de d-014 cai); RN-009 (va
         +lor normalizado em BRL ou na                                   
      35 +moeda original); RN-010 (validação da moeda, obrigatoriedade do
         + CC, política/câmbio                                           
      36 +ilegíveis); RN-011 (ordem normalizar/converter). RN-004, 005, 0
         +06, 007, 012: texto continua                                   
      37 +válido; RN-007 e §8 só mudam se surgir motivo novo.            
      38 +                                                               
      39 +AMB: AMB-013 invalidada no "exatamente três"; AMB-015 invalidad
         +a para o centro de custo e                                     
      40 +inadequada para moeda; AMB-007 a rever (comunicado liga viagem 
         +a moeda estrangeira; arquivo                                   
     … 54 more lines (ctrl+o to expand)

● O docs/RELATORIO.md alterado é o do subagente e fica fora, como você pediu.
  Commito só a spec e a análise:

● Bash(git add specs/001-motor-reembolso/spec.md docs/analise-envelope.md && git
      status --short && git diff --cached --stat && git commit -q -F - <<'EOF'
      docs(spec): s…)
  ⎿  (No output)

● O diff da spec ficou maior do que eu esperava (+969 linhas). Confiro se alguma
  reescrita acidental, de quebra de linha por exemplo, entrou junto:

● Bash(S=specs/001-motor-reembolso/spec.md; git show HEAD~1:$S | wc -l; wc -l <
      $S; git show HEAD~1:$S | tr -cd '\r' | wc -c; git show HEAD:$S | tr -cd
      '\r' | wc -c; g…)
  ⎿  (No output)

● O tamanho das alterações é legítimo: +875/−155 linhas na spec (1.724 → 2.444),
  com o bloco das 17 AMB somando 417 linhas, sem CR no arquivo. Agora vou
  analisar a D-012.

● Update(specs\001-motor-reembolso\DECISIONS.md)
  ⎿  Added 148 lines
       10  
       11  ---
       12  
       13 +## D-012 — Envelope do Dia 2: Política de Reembolso v4 · `2026
          +-10-19`                                                       
       14 +                                                              
       15 +**Gatilho:** o envelope lacrado do Dia 2 (`exemplos/envelope/`
          +, commit                                                      
       16 +`2aed7d1`), com o comunicado do RH da política v4: (A) limites
          + por centro de                                                
       17 +custo, lidos de uma política externa (`politica-v4.json`); (B)
          + despesas em                                                  
       18 +moeda estrangeira, convertidas pela taxa da data da despesa (`
          +cambio.json`);                                                
       19 +(C, opcional) fila de aprovação manual acima de R$ 500,00. Vig
          +ência imediata,                                               
       20 +retroativa à competência atual. A análise foi feita antes de q
          +ualquer                                                       
       21 +mudança na spec e está em `docs/analise-envelope.md`; ela iden
          +tificou o que                                                 
       22 +quebrava, dezoito pontos em aberto e o que a arquitetura absor
          +via. As                                                       
       23 +decisões foram tomadas pelo autor em bloco e escritas na spec 
          +2.0.                                                          
       24 +                                                              
       25 +**Achado que mais pesou:** o lote do Dia 1 (`exemplos/despesas
          +-exemplo.json`)                                               
       26 +é do centro de custo CC-ENG-PLATAFORMA, e a v4 é retroativa à 
          +sua                                                           
       27 +competência. Sob a tabela desse centro (alimentação 75,00, hos
          +pedagem 0,00),                                                
       28 +o resultado da §9 deixou de ser o que a spec 1.3 declarava: to
          +tal 386,93 em                                                 
       29 +vez de 623,43.                                                
       30 +                                                              
       31 +**O que mudou na spec (versão 1.3 → 2.0):**                   
       32 +- §6: dezessete AMB novas, AMB-022 a AMB-038. Política padrão 
          +(022, 023,                                                    
       33 +  024), comparação do centro de custo (025), limite 0,00 (026)
          +, cotação na                                                  
       34 +  data e fora da faixa (027, 028), moeda sem cotação (029), or
          +dem                                                           
       35 +  conversão-normalização (030), limiar de nota fiscal e chave 
          +de                                                            
       36 +  duplicidade em reais (031, 032), moeda inválida (033), viage
          +m (034),                                                      
       37 +  periodicidade (035), versionamento (036), política ou cotaçõ
          +es inválidas                                                  
       38 +  (037), item C (038). O ponto P5 da análise (representação so
          +b a política                                                  
       39 +  padrão) decorre da AMB-023 e entrou como consequência dela, 
          +não como AMB                                                  
       40 +  própria. §1: de "vinte e uma" para "trinta e oito".         
       41 +- RN novas: RN-013 (tabela de limites aplicável ao lote) e RN-
          +014 (conversão                                                
       42 +  para reais).                                                
       43 +- RN-001: limites da tabela aplicável, não mais fixos no texto
          +; aceites com                                                 
       44 +  e-007 e com o lote de exemplo sob o CC-ENG-PLATAFORMA.      
       45 +- RN-002: limite da tabela aplicável; aceites com e-008 e e-00
          +2.                                                            
       46 +- RN-003: limiar de nota fiscal lido da política e comparado e
          +m reais;                                                      
       47 +  título de "acima de R$ 100,00" para "acima do limiar da polí
          +tica"; aceites                                                
       48 +  com e-005 e e-003.                                          
       49 +- RN-004: aceite de d-013 sob o CC-ENG-PLATAFORMA e de e-005. 
       50 +- RN-008: elegíveis são as categorias da tabela aplicável com 
          +limite maior                                                  
       51 +  que zero, não mais "exatamente três"; aceites com e-001, f-0
          +03, e-009 e                                                   
       52 +  d-014 sob o CC-ENG-PLATAFORMA.                              
       53 +- RN-009: valor da chave em reais.                            
       54 +- RN-010: causas de lote para política e cotações inválidas; c
          +ausa de item                                                  
       55 +  "moeda sem cotação"; moeda inválida é erro mesmo sendo campo
          + opcional;                                                    
       56 +  centro de custo com tipo incompatível leva à tabela padrão; 
          +sete aceites                                                  
       57 +  novos.                                                      
       58 +- RN-011: normaliza o valor em reais, uma vez, depois da conve
          +rsão; aceite                                                  
       59 +  com 33,333 EUR.                                             
       60 +- §3: o centro de custo sai da lista de campos sem efeito; ent
          +ram o item C e                                                
       61 +  o versionamento de política como fora de escopo.            
       62 +- §4.1: o lote é avaliado contra a política e as cotações; cen
          +tro de custo                                                  
       63 +  com efeito; campo moeda na tabela de despesa; exceção à AMB-
          +015 para a                                                    
       64 +  moeda; política e cotações descritas.                       
       65 +- §4.2: valor considerado em reais; valor e moeda originais e 
          +cotação usada                                                 
       66 +  na saída; tabela aplicada no consolidado; causa nova; vocabu
          +lário com                                                     
       67 +  representacao, centros de custo e moedas; convenção "Despesa
          +s do lote de                                                  
       68 +  exemplo nos aceites".                                       
       69 +- §5 e §7, premissa: "limites nominais" passa a "limites da ta
          +bela                                                          
       70 +  aplicável".                                                 
       71 +- §7: seis casos novos (e-001/f-003, d-010 sob CC-ENG-PLATAFOR
          +MA, e-002,                                                    
       72 +  e-004, e-005, e-006); de 20 para 26 linhas.                 
       73 +- §8, etapa 0: seleção da tabela, conversão, normalização e va
          +lidação, nessa                                                
       74 +  ordem, com a justificativa da conversão antes da normalizaçã
          +o.                                                            
       75 +- §9: critérios de conversão e de tabela aplicável; resultado 
          +do lote de                                                    
       76 +  exemplo refeito sob o CC-ENG-PLATAFORMA (386,93; 6 integrais
          +, 1 parcial,                                                  
       77 +  6 recusadas, 1 erro); resultados esperados dos dois lotes do
          + envelope                                                     
       78 +  (1.143,26 e 373,76).                                        
       79 +- §10: viagem atualizada; versionamento de política, item C e 
          +centro de                                                     
       80 +  custo com outra caixa como pontos em aberto.                
       81 +- AMB-007: decisão mantida pela AMB-034 e redigida sem "limite
          +s nominais".                                                  
       82 +- AMB-013: elegíveis são as da tabela aplicável.              
       83 +- AMB-015: o centro de custo sai da decisão, com registro da r
          +evisão; a moeda                                               
       84 +  não segue a decisão (AMB-033); consequências sobre o centro 
          +de custo                                                      
       85 +  removidas.                                                  
       86 +                                                              
       87 +**Como as decisões foram tomadas:** o autor decidiu os dezoito
          + pontos em                                                    
       88 +bloco. Na escrita, o agente divergiu do texto ditado em três p
          +ontos e                                                       
       89 +escreveu a versão coerente, com o aviso registrado antes da es
          +crita:                                                        
       90 +- **Posição da conversão na §8.** O texto ditado punha a conve
          +rsão "depois                                                  
       91 +  da RN-011". Isso normalizaria o valor lançado e depois o con
          +vertido, o                                                    
       92 +  duplo arredondamento que a AMB-030 (T4) rejeita. A §8 foi es
          +crita com a                                                   
       93 +  conversão antes da normalização.                            
       94 +- **Critério da AMB-026 (P6).** O texto ditado era "limite 0,0
          +0 com                                                         
       95 +  observação 'nao reembolsavel'". A observação é texto livre, 
          +e o motor não o                                               
       96 +  interpreta (AMB-015); o critério escrito é o limite 0,00, e 
          +a observação é                                                
       97 +  informativa, como a periodicidade (AMB-035).                
       98 +- **Contagem.** O bloco foi apresentado como dezessete decisõe
          +s e tinha                                                     
       99 +  dezoito pontos; a P5 decorre da P2 e virou consequência da A
          +MB-023.                                                       
      100 +                                                              
      101 +Também por coerência com regras existentes, sem decisão nova: 
          +política e                                                    
      102 +cotações inválidas são informadas junto com as demais causas d
          +e lote (o                                                     
      103 +princípio de todas as causas detectáveis da RN-010); "política
          + inválida"                                                    
      104 +inclui a falta da tabela padrão e do limiar, sem os quais ela 
          +não pode ser                                                  
      105 +aplicada; sem cotação não há valor em reais, e a faixa de valo
          +r não é                                                       
      106 +avaliada; e o consolidado informa a tabela aplicada, para que 
          +o auditor saiba                                               
      107 +contra quais limites o lote foi julgado.                      
      108 +                                                              
      109 +**Convenção introduzida:** os aceites das RN-001 a RN-012, os 
          +das AMB-001 a                                                 
      110 +AMB-021 e os casos da §7 que citam uma despesa do lote de exem
          +plo com os                                                    
      111 +limites 60,00, 80,00 e 250,00 avaliam essa despesa num lote se
          +m centro de                                                   
      112 +custo, isto é, sob a tabela padrão (§4.2). Sem a convenção, ca
          +da um desses                                                  
      113 +aceites precisaria ser reescrito para o CC-ENG-PLATAFORMA e de
          +ixaria de                                                     
      114 +verificar a regra que verifica. O lote completo é verificado n
          +a §9.                                                         
      115 +                                                              
      116 +**Classificação:** mudança de requisito, não esclarecimento. A
          + política v3                                                  
      117 +deixou de valer.                                              
      118 +                                                              
      119 +**O que isso invalidou:**                                     
      120 +- O resultado da §9 da spec 1.3 (623,43; 4/4/5/1).            
      121 +- A decisão do plan §4, "constante e não arquivo de configuraç
          +ão", e o                                                      
      122 +  `enum Categoria` com limites fixos de `src/reembolso/dominio
          +/Politica.kt`.                                                
      123 +- A AMB-015 para o centro de custo, e com ela a premissa do pl
          +an §2 de que o                                                
      124 +  adaptador não lê o centro de custo.                         
      125 +- Os aceites previstos na T-017 para a AMB-015 que dependem do
          + centro de                                                    
      126 +  custo: "lotes que diferem só no centro de custo → resultados
          + idênticos"                                                   
      127 +  passou a ser falso.                                         
      128 +- Os aceites previstos na T-020 (623,43) e a contagem de 20 ca
          +sos da T-015.                                                 
      129 +- O `OrdemCamposSecao41Test` passa a falhar desde o commit da 
          +spec 2.0, por                                                 
      130 +  desenho: a tabela de despesa ganhou a moeda e `CampoDespesa`
          + ainda não.                                                   
      131 +                                                              
      132 +**O que continua valendo:** RN-004 a RN-007 e RN-012 no texto;
          + as AMB-001 a                                                 
      133 +AMB-021, com os ajustes acima; os 112 testes das T-001 a T-013
          +, que                                                         
      134 +constroem lotes sem centro de custo e continuam corretos pela 
          +convenção.                                                    
      135 +                                                              
      136 +**Tasks afetadas:**                                           
      137 +- Concluídas que precisam de retrabalho: T-004 (modelo: `Polit
          +ica.kt`,                                                      
      138 +  `CampoDespesa`, `Falha`, `CausaLote`), T-006 (causas de moed
          +a), T-008                                                     
      139 +  (`limiteAplicavel` passa a depender da tabela), T-010 (elegi
          +bilidade pela                                                 
      140 +  tabela), T-011 (limiar lido da política), T-013 (chave em re
          +ais, de graça                                                 
      141 +  depois da conversão). O retrabalho está nas T-023 a T-027.  
      142 +- Pendentes com aceites alterados: T-014 (tabela aplicada no c
          +onsolidado),                                                  
      143 +  T-015 (26 casos), T-017 (centro de custo e moeda), T-018 (ca
          +mpos novos da                                                 
      144 +  saída), T-019 (política e cotações na CLI), T-020 (três lote
          +s da §9). Os                                                  
      145 +  complementos estão nas T-028 a T-032.                       
      146 +- T-016 não muda: o `RastreabilidadeTest` lê os IDs e a contag
          +em da §7 da                                                   
      147 +  própria spec e passa a exigir testes para RN-013, RN-014 e A
          +MB-022 a                                                      
      148 +  AMB-038.                                                    
      149 +- Novas: T-021 a T-032.                                       
      150 +                                                              
      151 +**Achado de processo:** os testes que leem a spec não rodaram 
          +de novo depois                                                
      152 +de uma mudança só na spec, porque o Gradle não declara `specs/
          +` como entrada                                                
      153 +da tarefa de teste e os considerou atualizados. A falha só apa
          +receu com                                                     
      154 +`--rerun-tasks`. Vira a T-022.                                
      155 +                                                              
      156 +**Custo:** 3 arquivos de documentação (spec.md, DECISIONS.md, 
          +tasks.md),                                                    
      157 +mais `docs/analise-envelope.md`. Nenhum código alterado nesta 
          +etapa.                                                        
      158 +                                                              
      159 +---                                                           
      160 +                                                              
      161  ## D-011 — Conversão de caixa: mapeamento completo do Unicode,
            independente de localidade · `2026-10-19`
      162  
      163  **Gatilho:** a leitura da RN-008 antes da T-010. A spec dizia 
           apenas que

● Bash(git add specs/001-motor-reembolso/DECISIONS.md && git status --short &&
      git commit -q -F - <<'EOF'
      docs(decisions): D-012 — envelope do Dia 2, política v4, o qu…)
  ⎿  (No output)

● Commitei a D-012 como c89de59. Agora as tasks. Antes de escrever, leio a
  matriz de cobertura atual:

● Bash(T=specs/001-motor-reembolso/tasks.md; sed -n '/^## Cobertura/,$p' $T |
      cut -c1-60; sed -n '238,247p;250,252p;303,305p;330,332p;348,350p;359,362p'
      $T)
  ⎿  (No output)

● Escrevo a Fase 5 com as T-021 a T-032:

● Update(specs\001-motor-reembolso\tasks.md)
  ⎿  Added 178 lines, removed 3 lines
      370  
      371  ---
      372  
      373 -## Fase 5 — Envelope (criar no Dia 2)                         
      373 +## Fase 5 — Envelope: política v4 (spec 2.0, D-012)           
      374  
      375 -<Novas tasks a partir da mudança de requisito. Numeração conti
          -nua de onde parou —                                           
      376 -não reinicie e não renumere as antigas: a numeração é o eixo d
          -a rastreabilidade.>                                           
      375 +Tasks da mudança de requisito do Dia 2. Retrabalham as T-004, 
          +T-006, T-008,                                                 
      376 +T-010, T-011 e T-013, já concluídas, e complementam as T-014, 
          +T-015 e T-017 a                                               
      377 +T-020, ainda pendentes (ver as notas "Revisão D-012" em cada u
          +ma). A ordem                                                  
      378 +abaixo é a de execução: o plan primeiro, depois o modelo, as r
          +egras e a                                                     
      379 +fronteira. Até a T-023, o `OrdemCamposSecao41Test` falha por d
          +esenho (a §4.1                                                
      380 +já tem a moeda; `CampoDespesa` ainda não).                    
      381  
      382 +- [ ] **T-021** — Plan 2.0. Substituir o plan §4 ("constante, 
          +não arquivo de                                                
      383 +  configuração") por uma DT de política como dado, lida por um
          + adaptador                                                    
      384 +  `politica`; DT do adaptador `cambio`; decidir como a CLI rec
          +ebe a política                                                
      385 +  e as cotações sem quebrar a interface fixa `calcular --input
          + X --output Y`                                                
      386 +  (opções a avaliar: flags opcionais com caminho padrão, camin
          +ho convencional                                               
      387 +  ao lado da entrada, variável de ambiente); atualizar o model
          +o (§3.1, §3.2)                                                
      388 +  e o esquema de saída (§3.3) com moeda, cotação e tabela apli
          +cada; exit code                                               
      389 +  para política ou cotações ilegíveis (DT-008); declarar `spec
          +s/` como                                                      
      390 +  entrada dos testes (T-022).                                 
      391 +  - **Atende:** RN-013, RN-014, AMB-022, AMB-037; plan §2, §3,
          + §4, DT-008, DT-009                                           
      392 +  - **Aceite:** plan.md versão 2.0, baseado na spec 2.0; cada 
          +DT nova cita a RN                                             
      393 +    ou a AMB que implementa, e nenhuma decide regra de negócio
          +; o autor revisa                                              
      394 +    o diff antes do commit `docs(plan):`.                     
      395 +  - **Commit:** —                                             
      396 +                                                              
      397 +- [ ] **T-022** — Declarar `specs/` como entrada da tarefa de 
          +teste do Gradle,                                              
      398 +  para que os testes que leem a spec rodem quando só a spec mu
          +da (achado de                                                 
      399 +  processo da D-012).                                         
      400 +  - **Atende:** plan §6 (testes de rastreabilidade); D-012    
      401 +  - **Aceite:** `AmbienteTest` passa:                         
      402 +    `T-022 a tarefa de teste declara specs como entrada`;     
      403 +    e verificação manual registrada na mensagem do commit: com
          + o build verde,                                               
      404 +    alterar uma linha da §4.1 e rodar `./gradlew test`, sem `-
          +-rerun-tasks`,                                                
      405 +    faz o `OrdemCamposSecao41Test` rodar e falhar.            
      406 +  - **Commit:** —                                             
      407 +                                                              
      408 +- [ ] **T-023** — Modelo v2 (plan §3, versão 2.0): `CampoDespe
          +sa.MOEDA` entre                                               
      409 +  `VALOR` e `NOTA_FISCAL`; `Falha.MOEDA_SEM_COTACAO`; causas d
          +e lote                                                        
      410 +  `PoliticaInvalida` e `CotacoesInvalidas`; `LoteLido` com o c
          +entro de custo                                                
      411 +  e `DespesaLida` com a moeda, como `Lido<String>`.           
      412 +  - **Atende:** AMB-021, AMB-029, AMB-033, AMB-037; RN-010    
      413 +  - **Aceite:** passam                                        
      414 +    `OrdemCamposSecao41Test` › `§4-1 AMB-021 ordem de CampoDes
          +pesa é a ordem da tabela de despesa da spec`,                 
      415 +    com "Moeda" → `MOEDA` na correspondência, e               
      416 +    `FronteiraDominioTest` › `DT-009 nenhum arquivo do dominio
          + importa kotlinx ou java io`                                  
      417 +  - **Commit:** —                                             
      418 +                                                              
      419 +- [ ] **T-024** — RN-013: política como dado. Substituir o `en
          +um Categoria` com                                             
      420 +  limites e o `object Politica` por um tipo de domínio com tab
          +ela padrão,                                                   
      421 +  tabelas por centro de custo e limiar; resolver a tabela do l
          +ote (código                                                   
      422 +  exato, padrão como alternativa, categoria ausente ou com lim
          +ite 0,00                                                      
      423 +  inelegível); `limiteAplicavel` passa a receber a tabela; `Re
          +sultadoLote.Avaliado`                                         
      424 +  traz a tabela aplicada. Uma política de teste com a tabela p
          +adrão 60,00,                                                  
      425 +  80,00 e 250,00 mantém verdes os testes das T-001 a T-013, qu
          +e constroem                                                   
      426 +  lotes sem centro de custo.                                  
      427 +  - **Atende:** RN-013, RN-001, RN-002, RN-008, AMB-022, AMB-0
          +23, AMB-024,                                                  
      428 +    AMB-025, AMB-026, AMB-034, AMB-035                        
      429 +  - **Aceite:** `RN013TabelaAplicavelTest` passa, e os testes 
          +anteriores continuam passando:                                
      430 +    `RN-013 AMB-022 tabela padrão vem da política e alterar o 
          +seu limite altera o resultado`,                               
      431 +    `RN-013 c-0912 no CC-COMERCIAL usa a tabela do CC-COMERCIA
          +L`,                                                           
      432 +    `RN-013 AMB-023 e-001 representacao 340,00 no CC-COMERCIAL
          + → limite 300,00, reembolso 300,00 parcial`,                  
      433 +    `RN-013 AMB-023 f-003 representacao sob a tabela padrão → 
          +recusada com CATEGORIA_INELEGIVEL`,                           
      434 +    `RN-013 AMB-023 hospedagem no CC-ADM → recusada com CATEGO
          +RIA_INELEGIVEL sem consultar a tabela padrão`,                
      435 +    `RN-013 AMB-024 lote sem centro de custo ou com centro de 
          +tipo incompatível → tabela padrão`,                           
      436 +    `RN-013 AMB-025 cc-comercial em minúsculas → tabela padrão
          + e alimentacao 85,00 → 60,00 parcial`,                        
      437 +    `RN-013 AMB-026 d-010 hospedagem com limite 0,00 no CC-ENG
          +-PLATAFORMA → recusada com CATEGORIA_INELEGIVEL`,             
      438 +    `RN-013 AMB-026 d-013 no CC-ENG-PLATAFORMA → motivos CATEG
          +ORIA_INELEGIVEL e SEM_NOTA_FISCAL`,                           
      439 +    `RN-013 resultado do lote informa a tabela aplicada`,     
      440 +    `RN-001 AMB-035 d-001 e d-002 sob CC-ENG-PLATAFORMA → 72,5
          +0 e 38,00, total 110,50 no dia`,                              
      441 +    `RN-001 AMB-034 e-007 hospedagem 1200,00 no CC-COMERCIAL →
          + limite 400,00 sem acréscimo em viagem`,                      
      442 +    `RN-002 e-008 alimentacao 95,00 no CC-COMERCIAL → 90,00 pa
          +rcial e excedente 5,00`,                                      
      443 +    `RN-008 d-014 ALIMENTACAO 61,00 sob CC-ENG-PLATAFORMA → 61
          +,00 integral`                                                 
      444 +  - **Commit:** —                                             
      445 +                                                              
      446 +- [ ] **T-025** — RN-014: conversão para reais na etapa 0, ant
          +es da RN-011 (§8):                                            
      447 +  valor lançado × cotação da data ou da anterior mais recente,
          + normalizado uma                                              
      448 +  vez; moeda sem cotação aplicável e moeda inválida como erro 
          +de item.                                                      
      449 +  - **Atende:** RN-014, RN-011, RN-010, AMB-027, AMB-028, AMB-
          +029, AMB-030,                                                 
      450 +    AMB-033, AMB-034                                          
      451 +  - **Aceite:** `RN014ConversaoTest` passa:                   
      452 +    `RN-014 e-002 22,00 EUR em 14-07 → 130,46 e 90,00 parcial`
          +,                                                             
      453 +    `RN-014 e-003 14,50 EUR em 15-07 → 85,26 integral`,       
      454 +    `RN-014 AMB-027 e-004 sábado 18-07 → cotação de 17-07, 178
          +,80 e excedente 88,80`,                                       
      455 +    `RN-014 AMB-028 USD em 31-07 → cotação de 28-07`,         
      456 +    `RN-014 AMB-028 EUR em 02-07 sem cotação anterior → erro d
          +e item moeda sem cotação`,                                    
      457 +    `RN-014 AMB-029 e-006 55,00 GBP → erro de item moeda sem c
          +otação e as demais processadas`,                              
      458 +    `RN-014 e-010 sem moeda → BRL sem conversão`,             
      459 +    `RN-014 f-004 12,00 USD em 21-07 → 65,76 integral`,       
      460 +    `RN-014 AMB-034 e-002 em EUR não amplia o limite de 90,00`
          +;                                                             
      461 +    mais, nos arquivos das RN correspondentes:                
      462 +    `RN-011 AMB-030 33,333 EUR a 5,93 → 197,66 e não 197,65`, 
      463 +    `RN-010 AMB-033 moeda eur, EURO ou numérica → erro de item
          + tipo incompatível`,                                          
      464 +    `RN-010 AMB-033 moeda informada sem valor → erro de item e
          + não BRL`,                                                    
      465 +    `RN-010 AMB-021 moeda sem cotação e valor ausente → as dua
          +s causas na ordem da §4-1`                                    
      466 +  - **Commit:** —                                             
      467 +                                                              
      468 +- [ ] **T-026** — RN-003 com o limiar lido da política e compa
          +rado em reais;                                                
      469 +  RN-009 com a chave em reais.                                
      470 +  - **Atende:** RN-003, RN-004, RN-009, AMB-022, AMB-031, AMB-
          +032                                                           
      471 +  - **Aceite:** passam, nos arquivos das RN:                  
      472 +    `RN-003 AMB-031 e-005 40,00 USD = 220,00 sem NF → nota exi
          +gida e ausente`,                                              
      473 +    `RN-003 AMB-031 e-003 14,50 EUR = 85,26 sem NF → nota não 
          +exigida`,                                                     
      474 +    `RN-003 AMB-022 limiar lido da política: com limiar de 150
          +,00, transporte de 120,00 sem NF não exige nota`,             
      475 +    `RN-004 e-005 → recusada com SEM_NOTA_FISCAL apesar do lim
          +ite de 150,00`,                                               
      476 +    `RN-009 AMB-032 22,00 EUR e 130,46 BRL no mesmo dia → grup
          +o de duplicatas`,                                             
      477 +    `RN-009 AMB-032 22,00 EUR e 22,00 BRL → não são duplicatas
          +`                                                             
      478 +  - **Commit:** —                                             
      479 +                                                              
      480 +- [ ] **T-027** — RN-010, erro de lote: política e cotações in
          +válidas, com as                                               
      481 +  demais causas detectáveis.                                  
      482 +  - **Atende:** RN-010, AMB-037                               
      483 +  - **Aceite:** `RN010ValidacaoDeLoteTest` passa, além dos tes
          +tes da T-005:                                                 
      484 +    `RN-010 AMB-037 política sem tabela padrão, sem limiar ou 
          +com limite negativo → lote rejeitado`,                        
      485 +    `RN-010 AMB-037 cotação com taxa 0,00 → lote rejeitado mes
          +mo só com despesas em BRL`,                                   
      486 +    `RN-010 AMB-037 política inválida e início posterior ao fi
          +m → as duas causas`                                           
      487 +  - **Commit:** —                                             
      488 +                                                              
      489 +- [ ] **T-028** — Adaptadores de entrada v2: `entrada` lê o ce
          +ntro de custo e a                                             
      490 +  moeda; adaptadores novos `politica` e `cambio`, de JSON para
          + o domínio;                                                   
      491 +  arquivo ilegível vira causa de lote. Complementa a T-017.   
      492 +  - **Atende:** RN-010, RN-013, RN-014, AMB-015, AMB-024, AMB-
          +026, AMB-033,                                                 
      493 +    AMB-035, AMB-037                                          
      494 +  - **Aceite:** passam:                                       
      495 +    `EntradaJsonTest` › `AMB-024 centro_custo ausente, null ou
          + numérico → tabela padrão`,                                   
      496 +    `EntradaJsonTest` › `AMB-033 moeda null, vazia ou numérica
          + → erro de item tipo incompatível`,                           
      497 +    `EntradaJsonTest` › `AMB-033 moeda ausente → BRL`,        
      498 +    `PoliticaJsonTest` › `RN-013 politica-v4 lida com tabela p
          +adrão, quatro centros de custo e limiar 100,00`,              
      499 +    `PoliticaJsonTest` › `AMB-037 política ausente ou com JSON
          + inválido → lote rejeitado`,                                  
      500 +    `PoliticaJsonTest` › `AMB-026 AMB-035 periodicidade, obser
          +vacao e acrescimo_em_viagem não chegam ao domínio`,           
      501 +    `CambioJsonTest` › `RN-014 cambio lido com 12 datas e duas
          + moedas`,                                                     
      502 +    `CambioJsonTest` › `AMB-037 cotações ausentes ou com taxa 
          +não numérica → lote rejeitado`                                
      503 +  - **Commit:** —                                             
      504 +                                                              
      505 +- [ ] **T-029** — Saída v2: valor e moeda originais e cotação 
          +usada em cada                                                 
      506 +  despesa convertida; tabela aplicada no consolidado; causa mo
          +eda sem                                                       
      507 +  cotação. Complementa a T-018.                               
      508 +  - **Atende:** §4.2, RN-013, RN-014, AMB-027, AMB-029        
      509 +  - **Aceite:** `SaidaJsonTest` passa, além dos testes da T-01
          +8:                                                            
      510 +    `§4-2 RN-014 despesa em moeda estrangeira traz valor e moe
          +da originais e a cotação usada`,                              
      511 +    `§4-2 AMB-027 e-004 traz a cotação de 17-07`,             
      512 +    `§4-2 RN-014 despesa em BRL não traz cotação`,            
      513 +    `§4-2 AMB-029 e-006 traz a causa moeda sem cotação e não t
          +raz valor considerado`,                                       
      514 +    `§4-2 RN-013 consolidado traz a tabela aplicada`          
      515 +  - **Commit:** —                                             
      516 +                                                              
      517 +- [ ] **T-030** — CLI v2: recebe a política e as cotações conf
          +orme a DT da                                                  
      518 +  T-021, mantendo `calcular --input X --output Y`. Complementa
          + a T-019.                                                     
      519 +  - **Atende:** RN-010, AMB-037; plan DT-008                  
      520 +  - **Aceite:** `CliTest` passa, além dos testes da T-019:    
      521 +    `DT-008 calcular --input --output continua aceito`,       
      522 +    `DT-008 calcular com a política e as cotações do envelope 
          +→ exit 0`,                                                    
      523 +    `DT-008 AMB-037 política ausente → lote rejeitado e exit 1
          +`                                                             
      524 +  - **Commit:** —                                             
      525 +                                                              
      526 +- [ ] **T-031** — Ponta a ponta da §9 v2: o lote de exemplo e 
          +os dois lotes do                                              
      527 +  envelope, com `politica-v4.json` e `cambio.json`. Substitui 
          +os aceites da                                                 
      528 +  T-020.                                                      
      529 +  - **Atende:** §9, RN-001, RN-009, RN-013, RN-014, AMB-036, A
          +MB-038                                                        
      530 +  - **Aceite:** `Secao9CriteriosDeAceiteTest` passa:          
      531 +    `§9 AMB-036 lote de exemplo sob CC-ENG-PLATAFORMA → total 
          +386,93 com 6 integrais, 1 parcial, 6 recusadas e 1 erro`,     
      532 +    `§9 lote de exemplo produz o resultado da tabela da §9 des
          +pesa a despesa`,                                              
      533 +    `§9 despesas-envelope no CC-COMERCIAL → total 1143,26 com 
          +2 integrais, 5 parciais, 2 recusadas e 1 erro`,               
      534 +    `§9 despesas-envelope-cc-desconhecido sob a tabela padrão 
          +→ total 373,76 com 2 integrais, 1 parcial e 1 recusada`,      
      535 +    `§9 cada despesa aparece exatamente uma vez na ordem da en
          +trada`,                                                       
      536 +    `§9 a mesma entrada produz sempre a mesma saída`,         
      537 +    `§9 RN-001 RN-009 inverter a ordem dos três lotes não alte
          +ra nenhum resultado`,                                         
      538 +    `§9 AMB-038 nenhum resultado dos três lotes tem estado de 
          +pendência`                                                    
      539 +  - **Commit:** —                                             
      540 +                                                              
      541 +- [ ] **T-032** — Os seis casos novos da §7, que passa a ter 2
          +6 linhas.                                                     
      542 +  Complementa a T-015.                                        
      543 +  - **Atende:** §7; RN-001 a RN-004, RN-008, RN-010, RN-013, R
          +N-014                                                         
      544 +  - **Aceite:** `Secao7CasosDeBordaTest` passa, com os 20 test
          +es da T-015 e mais:                                           
      545 +    `§7 RN-008 RN-013 e-001 representacao no CC-COMERCIAL → 30
          +0,00 parcial e f-003 sob a padrão → recusada`,                
      546 +    `§7 RN-008 RN-013 d-010 sob CC-ENG-PLATAFORMA com limite 0
          +,00 → recusada com CATEGORIA_INELEGIVEL`,                     
      547 +    `§7 RN-001 RN-002 RN-014 e-002 22,00 EUR → 90,00 parcial e
          + excedente 40,46`,                                            
      548 +    `§7 RN-014 e-004 no sábado → cotação de 17-07 e 90,00 parc
          +ial`,                                                         
      549 +    `§7 RN-003 RN-004 RN-014 e-005 40,00 USD sem NF → recusada
          + com SEM_NOTA_FISCAL`,                                        
      550 +    `§7 RN-010 RN-014 e-006 em GBP → erro de item moeda sem co
          +tação`                                                        
      551 +  - **Commit:** —                                             
      552 +                                                              
      553  ---
      554  
      555  ## Cobertura

● Bash(T=specs/001-motor-reembolso/tasks.md && python3 --version >/dev/null
      2>&1; node -e '
      const fs=require("fs");const p=process.argv[1];let
      s=fs.readFileSync(p,"utf…)
  ⎿  Error: Exit code 1
     [eval]:13
       const re=new RegExp("(- \[ \] \*\*"+t+"\*\*[\s\S]*?\n)(  -
     \*\*Atende:\*\*)");
                ^

     SyntaxError: Invalid regular expression: /(- [ ] **T-014**[sS]*?
     )(  - **Atende:**)/: Nothing to repeat
         at new RegExp (<anonymous>)
         at [eval]:13:12
         at runScriptInThisContext (node:internal/vm:143:10)

     … +7 lines (ctrl+o to expand)
● A regex se perdeu no escape do shell. Passo o script para um arquivo no
  scratchpad:

  Made 1 scratchpad edit +26 (ctrl+o to expand)

● Bash(T=specs/001-motor-reembolso/tasks.md && node
      /c/Users/dev/AppData/Local/Temp/claude/c--Users-dev-SDD-Desafio/8aa9dd74-d
      4f2-42e8-b296-d294cb69c18d/scratchpad/not…)
  ⎿  Updated specs\001-motor-reembolso\tasks.md (+7 -1)
      1  # Tasks — Motor de Cálculo de Reembolso
      2  
      3 -**Baseado em:** spec 1.3 · plan 1.1                             
      3 +**Baseado em:** spec 2.0 · plan 1.1 (plan 2.0 na T-021)         
      4  
      5  > Cada task é pequena o bastante para virar **um commit**. Se vo
         cê não consegue
      6  > descrever o critério de aceite como "o teste X passa", a task 
         está grande demais.
     ...
      237  
      238  - [ ] **T-014** — Consolidado do lote: período avaliado, total
            reembolsado,
      239    quantidades por classificação e total; lote sem despesas.
      240 +  - **Revisão D-012:** O consolidado ganha a tabela aplicada (
          +RN-013), no domínio pela T-024 e na saída pela T-029.         
      241    - **Atende:** §4.2 (consolidado), RN-010, AMB-010, AMB-014
      242    - **Aceite:** `ConsolidadoTest` passa:
      243      `§4-2 AMB-014 total reembolsado é a soma dos reembolsos do
           s itens`,
     ...
      250  
      251  - [ ] **T-015** — Um teste por linha da tabela da §7 (20 linha
           s), nomeado com
      252    as RNs da coluna "Regra".
      253 +  - **Revisão D-012:** A §7 passou a ter 26 linhas; os seis ca
          +sos novos estão na T-032.                                     
      254    - **Atende:** §7
      255    - **Aceite:** `Secao7CasosDeBordaTest` passa, com os 20 test
           es:
      256      `§7 RN-001 RN-002 d-001 alimentacao 72,50 → 60,00 parcial,
            excedente 12,50`,
     ...
      306    `ENTRADA_ILEGIVEL` como causa única para JSON inválido ou ra
           iz que não é
      307    lote; `BigDecimal` a partir do texto cru; data com `Resolver
           Style.STRICT`;
      308    `null` como `Ausente`; campos sem regra não lidos.
      309 +  - **Revisão D-012:** O adaptador passa a ler o centro de cus
          +to e a moeda (T-028). O aceite `AMB-015 lotes que diferem só n
          +o centro de custo → resultados idênticos` deixou de valer e é 
          +substituído por `AMB-015 lotes que diferem só no identificador
          + ou no nome do colaborador → resultados idênticos`.           
      310    - **Atende:** RN-009, RN-010, RN-011, AMB-008, AMB-009, AMB-
           011, AMB-015, AMB-016, AMB-020
      311    - **Aceite:** `EntradaJsonTest` passa:
      312      `RN-010 JSON sintaticamente inválido → rejeitado com causa
            única ENTRADA_ILEGIVEL`,
     ...
      334    Campos não aplicáveis omitidos, `motivos` sempre presente, d
           inheiro com
      335    duas casas via `toPlainString()`, `posicao` no lugar de `id`
           , frase de
      336    justificativa montada só a partir dos campos do resultado.
      337 +  - **Revisão D-012:** Campos novos da saída na T-029: valor e
          + moeda originais, cotação usada, tabela aplicada, causa moeda 
          +sem cotação.                                                  
      338    - **Atende:** §4.2, RN-012, AMB-015, AMB-020
      339    - **Aceite:** `SaidaJsonTest` passa:
      340      `§4-2 parcial traz limite_aplicado e excedente e recusada 
           não traz limite_aplicado`,
     ...
      351  
      352  - [ ] **T-019** — CLI `calcular --input X --output Y` com pars
           ing manual e
      353    exit codes (DT-008).
      354 +  - **Revisão D-012:** Política e cotações como entradas da CL
          +I na T-030, conforme a DT da T-021.                           
      355    - **Atende:** RN-010 (entrada que não pode ser lida); plan D
           T-008
     … 9 more lines (ctrl+o to expand)

● Por último, a matriz de cobertura: linhas novas para RN-013, RN-014 e AMB-022
  a AMB-038, e as tasks novas nas linhas existentes que elas complementam.

  Made 1 scratchpad edit +61 (ctrl+o to expand)

● Bash(T=specs/001-motor-reembolso/tasks.md && node
      /c/Users/dev/AppData/Local/Temp/claude/c--Users-dev-SDD-Desafio/8aa9dd74-d
      4f2-42e8-b296-d294cb69c18d/scratchpad/cob…)
  ⎿  Updated specs\001-motor-reembolso\tasks.md (+33 -14)
      573  
      574  | Regra da spec | Task | Teste |
      575  |---|---|---|
      576 -| RN-001 | T-008 | `RN-001 AMB-001 d-001 alimentacao 72,50 → r
          -eembolso 60,00 parcial`; `RN-001 AMB-001 AMB-003 três alimenta
          -ções de 59,00 no mesmo dia → 59,00 cada e 177,00 no total`; `§
          -9 RN-001 RN-009 inverter a ordem do lote de exemplo não altera
          - nenhum resultado` (T-020) |                                  
      577 -| RN-002 | T-008 | `RN-002 AMB-002 alimentacao de 60,01 → reem
          -bolso 60,00 parcial e excedente 0,01`; `RN-002 transporte urba
          -no de 90,00 → reembolso 80,00 parcial e excedente 10,00` |    
      578 -| RN-003 | T-011 | `RN-003 AMB-004 d-003 transporte 100,00 sem
          - NF → nota não exigida, reembolso 80,00 parcial`; `RN-003 RN-0
          -11 AMB-014 100,004 → nota não exigida e 100,005 → nota exigida
          -` |                                                           
      579 -| RN-004 | T-011 | `RN-004 AMB-005 d-004 → reembolso 0,00 recu
          -sada com SEM_NOTA_FISCAL`; `RN-004 AMB-005 hospedagem 200,00 s
          -em NF → recusada com SEM_NOTA_FISCAL apesar de estar abaixo do
          - limite` |                                                    
      576 +| RN-001 | T-008, T-024 | `RN-001 AMB-001 d-001 alimentacao 72
          +,50 → reembolso 60,00 parcial`; `RN-001 AMB-001 AMB-003 três a
          +limentações de 59,00 no mesmo dia → 59,00 cada e 177,00 no tot
          +al`; `§9 RN-001 RN-009 inverter a ordem do lote de exemplo não
          + altera nenhum resultado` (T-020) |                           
      577 +| RN-002 | T-008, T-024 | `RN-002 AMB-002 alimentacao de 60,01
          + → reembolso 60,00 parcial e excedente 0,01`; `RN-002 transpor
          +te urbano de 90,00 → reembolso 80,00 parcial e excedente 10,00
          +` |                                                           
      578 +| RN-003 | T-011, T-026 | `RN-003 AMB-004 d-003 transporte 100
          +,00 sem NF → nota não exigida, reembolso 80,00 parcial`; `RN-0
          +03 RN-011 AMB-014 100,004 → nota não exigida e 100,005 → nota 
          +exigida` |                                                    
      579 +| RN-004 | T-011, T-026 | `RN-004 AMB-005 d-004 → reembolso 0,
          +00 recusada com SEM_NOTA_FISCAL`; `RN-004 AMB-005 hospedagem 2
          +00,00 sem NF → recusada com SEM_NOTA_FISCAL apesar de estar ab
          +aixo do limite` |                                             
      580  | RN-005 | T-009 | `RN-005 AMB-016 d-014 em 2026-07-31 igual à
            data de fim → dentro do período`; `RN-005 AMB-016 despesa em 
           2026-07-01 igual à data de início → dentro do período` |
      581  | RN-006 | T-009 | `RN-006 AMB-010 d-008 alimentacao 41,00 com
            NF → recusada com FORA_DA_COMPETENCIA sem limite aplicado` |
      582  | RN-007 | T-012 | `RN-007 AMB-018 150,00 sem NF fora do perío
           do → motivos FORA_DA_COMPETENCIA e SEM_NOTA_FISCAL nessa ordem
           `; `RN-007 AMB-017 d-009 erro → lista de motivos vazia` |
      583 -| RN-008 | T-010 | `RN-008 AMB-013 AMB-017 d-005 coworking → r
          -ecusada com CATEGORIA_INELEGIVEL e não erro`; `RN-008 AMB-013 
          -d-014 ALIMENTACAO 61,00 → categoria alimentacao, reembolso 60,
          -00 parcial` |                                                 
      584 -| RN-009 | T-013, T-017 | `RN-009 AMB-011 d-006 e d-007 → d-00
          -6 integral e d-007 recusada com DUPLICATA mantida d-006`; `RN-
          -009 identificador fora do BMP é comparado por code point`; `RN
          --009 AMB-011 iguais no critério com descrições diferentes → gr
          -upo de duplicatas` (T-017) |                                  
      585 -| RN-010 | T-005, T-006, T-007, T-014, T-017, T-019 | `RN-010 
          -AMB-017 início posterior ao fim → lote rejeitado sem despesa p
          -rocessada` (T-005); `RN-010 AMB-021 sem fornecedor e valor −5,
          -00 → causas fornecedor ausente e valor não positivo nessa orde
          -m` (T-006); `RN-010 AMB-020 despesa sem identificador na terce
          -ira posição → erro identificado pela posição 3` (T-007); `RN-0
          -10 lote sem despesas → total 0,00 e zero em todas as classific
          -ações` (T-014); `RN-010 raiz JSON que é uma lista ou um número
          - → rejeitado com causa única ENTRADA_ILEGIVEL` (T-017); `RN-01
          -0 DT-008 arquivo de entrada inexistente → rejeitado com ENTRAD
          -A_ILEGIVEL e exit 1` (T-019) |                                
      586 -| RN-011 | T-002, T-006, T-008, T-011, T-013, T-017 | `RN-011 
          -AMB-014 100,005 normaliza para 100,01 e 100,004 para 100,00` (
          -T-002); `RN-011 AMB-014 0,004 normaliza para 0,00 e é erro de 
          -item` (T-006); `RN-011 d-011 33,333 → reembolso 33,33 integral
          -` (T-008); `RN-011 AMB-014 54,90 e 54,904 → grupo de duplicata
          -s` (T-013); `RN-011 33,333 é lido do texto cru sem passar por 
          -Double` (T-017) |                                             
      583 +| RN-008 | T-010, T-024 | `RN-008 AMB-013 AMB-017 d-005 cowork
          +ing → recusada com CATEGORIA_INELEGIVEL e não erro`; `RN-008 A
          +MB-013 d-014 ALIMENTACAO 61,00 → categoria alimentacao, reembo
          +lso 60,00 parcial` |                                          
      584 +| RN-009 | T-013, T-017, T-026 | `RN-009 AMB-011 d-006 e d-007
          + → d-006 integral e d-007 recusada com DUPLICATA mantida d-006
          +`; `RN-009 identificador fora do BMP é comparado por code poin
          +t`; `RN-009 AMB-011 iguais no critério com descrições diferent
          +es → grupo de duplicatas` (T-017) |                           
      585 +| RN-010 | T-005, T-006, T-007, T-014, T-017, T-019, T-025, T-
          +027, T-028, T-030 | `RN-010 AMB-017 início posterior ao fim → 
          +lote rejeitado sem despesa processada` (T-005); `RN-010 AMB-02
          +1 sem fornecedor e valor −5,00 → causas fornecedor ausente e v
          +alor não positivo nessa ordem` (T-006); `RN-010 AMB-020 despes
          +a sem identificador na terceira posição → erro identificado pe
          +la posição 3` (T-007); `RN-010 lote sem despesas → total 0,00 
          +e zero em todas as classificações` (T-014); `RN-010 raiz JSON 
          +que é uma lista ou um número → rejeitado com causa única ENTRA
          +DA_ILEGIVEL` (T-017); `RN-010 DT-008 arquivo de entrada inexis
          +tente → rejeitado com ENTRADA_ILEGIVEL e exit 1` (T-019) |    
      586 +| RN-011 | T-002, T-006, T-008, T-011, T-013, T-017, T-025 | `
          +RN-011 AMB-014 100,005 normaliza para 100,01 e 100,004 para 10
          +0,00` (T-002); `RN-011 AMB-014 0,004 normaliza para 0,00 e é e
          +rro de item` (T-006); `RN-011 d-011 33,333 → reembolso 33,33 i
          +ntegral` (T-008); `RN-011 AMB-014 54,90 e 54,904 → grupo de du
          +plicatas` (T-013); `RN-011 33,333 é lido do texto cru sem pass
          +ar por Double` (T-017) |                                      
      587  | RN-012 | T-005, T-007, T-018 | `RN-012 AMB-019 duas despesas
            com identificador d-006 → lote rejeitado sem avaliar nenhuma`
           ; `RN-012 AMB-020 d-006 com espaço à esquerda repete d-006 → l
           ote rejeitado` (T-005); `RN-012 AMB-020 duas despesas sem iden
           tificador → ambas erro de item e lote não rejeitado` (T-007); 
           `RN-012 AMB-020 identificador com espaços nas extremidades sai
            aparado` (T-018) |
      588 +| RN-013 | T-024, T-026, T-028, T-029, T-031 | `RN-013 AMB-023
          + e-001 representacao 340,00 no CC-COMERCIAL → limite 300,00, r
          +eembolso 300,00 parcial`; `RN-013 AMB-026 d-010 hospedagem com
          + limite 0,00 no CC-ENG-PLATAFORMA → recusada com CATEGORIA_INE
          +LEGIVEL` (T-024); `§4-2 RN-013 consolidado traz a tabela aplic
          +ada` (T-029) |                                                
      589 +| RN-014 | T-025, T-028, T-029, T-031 | `RN-014 AMB-027 e-004 
          +sábado 18-07 → cotação de 17-07, 178,80 e excedente 88,80`; `R
          +N-014 AMB-029 e-006 55,00 GBP → erro de item moeda sem cotação
          + e as demais processadas` (T-025); `§4-2 RN-014 despesa em moe
          +da estrangeira traz valor e moeda originais e a cotação usada`
          + (T-029) |                                                    
      590  
      591  ### Ambiguidades
      592  
     ...
      598  | AMB-004 | T-011 | `RN-003 AMB-004 d-003 transporte 100,00 se
           m NF → nota não exigida, reembolso 80,00 parcial`; `RN-003 AMB
           -004 transporte 100,01 com NF → nota exigida e presente, reemb
           olso 80,00 parcial` |
      599  | AMB-005 | T-011 | `RN-004 AMB-005 hospedagem 200,00 sem NF →
            recusada com SEM_NOTA_FISCAL apesar de estar abaixo do limite
           `; `RN-004 AMB-005 alimentacao 500,00 sem NF → recusada com SE
           M_NOTA_FISCAL` |
      600  | AMB-006 | T-011 | `RN-003 AMB-006 alimentacao 500,00 sem NF 
           → nota exigida e ausente apesar do teto de 60,00`; `RN-003 AMB
           -004 AMB-006 d-004 transporte 100,01 sem NF → nota exigida e a
           usente` |
      599 -| AMB-007 | T-008 | `RN-001 AMB-007 AMB-009 d-010 hospedagem 4
          -80,00 → limite 250,00, reembolso 250,00 parcial, excedente 230
          -,00`; `AMB-007 d-011 no mesmo fornecedor de d-010 mantém limit
          -e 60,00` |                                                    
      601 +| AMB-007 | T-008, T-024 (mantida pela AMB-034) | `RN-001 AMB-
          +007 AMB-009 d-010 hospedagem 480,00 → limite 250,00, reembolso
          + 250,00 parcial, excedente 230,00`; `AMB-007 d-011 no mesmo fo
          +rnecedor de d-010 mantém limite 60,00` |                      
      602  | AMB-008 | T-009, T-017 (fronteira) | `RN-005 AMB-008 d-008 e
           m 2026-04-15 → fora do período`; `AMB-008 descrição de d-008 n
           ão altera o resultado FORA_DA_COMPETENCIA` (T-017) |
      603  | AMB-009 | T-008, T-013, T-017 (fronteira) | `RN-001 AMB-007 
           AMB-009 d-010 hospedagem 480,00 → limite 250,00, …`; `RN-009 A
           MB-009 duas diárias de 240,00 em 14-07 e 15-07 → não são dupli
           catas` (T-013); `AMB-009 hospedagem 400,00 descrita como 1 dia
           ria ou 2 diarias → mesmo resultado 250,00 parcial` (T-017) |
      604  | AMB-010 | T-009, T-013, T-014 | `RN-006 AMB-010 d-008 alimen
           tacao 41,00 com NF → recusada com FORA_DA_COMPETENCIA sem limi
           te aplicado`; `RN-009 AMB-010 duas iguais fora do período → am
           bas FORA_DA_COMPETENCIA e nenhuma DUPLICATA` (T-013); `§4-2 AM
           B-010 quantidades por classificação somam o número de despesas
            da entrada` (T-014) |
     ...
      612  | AMB-018 | T-004, T-012 | `§8 AMB-018 ordem de Motivo é a ord
           em da lista de elegibilidade da spec` (T-004); `RN-007 AMB-018
            150,00 sem NF fora do período → motivos FORA_DA_COMPETENCIA e
            SEM_NOTA_FISCAL nessa ordem` (T-012) |
      613  | AMB-019 | T-005 | `RN-012 AMB-019 duas despesas com identifi
           cador d-006 → lote rejeitado sem avaliar nenhuma`; `RN-010 AMB
           -019 início posterior ao fim e identificador repetido → as dua
           s causas` |
      614  | AMB-020 | T-005, T-007, T-017, T-018 | `RN-010 AMB-020 perío
           do com tipo incompatível → lote rejeitado` (T-005); `RN-010 AM
           B-020 despesa sem identificador na terceira posição → erro ide
           ntificado pela posição 3`; `RN-010 AMB-020 elemento que não é 
           despesa → erro de item na sua posição com causa única` (T-007)
           ; `AMB-020 despesa sem identificador sai com posicao no lugar 
           de id` (T-018) |
      613 -| AMB-021 | T-004, T-006, T-007 | `§4-1 AMB-021 ordem de Campo
          -Despesa é a ordem da tabela de despesa da spec` (T-004); `RN-0
          -10 AMB-021 sem fornecedor e valor −5,00 → causas fornecedor au
          -sente e valor não positivo nessa ordem` (T-006); `AMB-021 sem 
          -identificador e data 2026-02-30 → causas identificador ausente
          - e data inexistente nessa ordem` (T-007) |                    
     … 32 more lines (ctrl+o to expand)

● Todas as RNs e AMBs têm cobertura, mas encontrei um nome de teste com ":" que
  o JVM não aceita em nomes de método. Vou corrigir as duas ocorrências e fazer
  o commit.

● Bash(T=specs/001-motor-reembolso/tasks.md && sed -i 's/limiar lido da
      política: com limiar de 150,00/limiar lido da política e com limiar de
      150,00/g' $T && grep -c…)
  ⎿  Updated specs\001-motor-reembolso\tasks.md (+2 -2)
      477    - **Aceite:** passam, nos arquivos das RN:
      478      `RN-003 AMB-031 e-005 40,00 USD = 220,00 sem NF → nota exi
           gida e ausente`,
      479      `RN-003 AMB-031 e-003 14,50 EUR = 85,26 sem NF → nota não 
           exigida`,
      480 -    `RN-003 AMB-022 limiar lido da política: com limiar de 150
          -,00, transporte de 120,00 sem NF não exige nota`,             
      480 +    `RN-003 AMB-022 limiar lido da política e com limiar de 15
          +0,00, transporte de 120,00 sem NF não exige nota`,            
      481      `RN-004 e-005 → recusada com SEM_NOTA_FISCAL apesar do lim
           ite de 150,00`,
      482      `RN-009 AMB-032 22,00 EUR e 130,46 BRL no mesmo dia → grup
           o de duplicatas`,
      483      `RN-009 AMB-032 22,00 EUR e 22,00 BRL → não são duplicatas
           `
     ...
      613  | AMB-019 | T-005 | `RN-012 AMB-019 duas despesas com identifi
           cador d-006 → lote rejeitado sem avaliar nenhuma`; `RN-010 AMB
           -019 início posterior ao fim e identificador repetido → as dua
           s causas` |
      614  | AMB-020 | T-005, T-007, T-017, T-018 | `RN-010 AMB-020 perío
           do com tipo incompatível → lote rejeitado` (T-005); `RN-010 AM
           B-020 despesa sem identificador na terceira posição → erro ide
           ntificado pela posição 3`; `RN-010 AMB-020 elemento que não é 
           despesa → erro de item na sua posição com causa única` (T-007)
           ; `AMB-020 despesa sem identificador sai com posicao no lugar 
           de id` (T-018) |
      615  | AMB-021 | T-004, T-006, T-007, T-023, T-025 | `§4-1 AMB-021 
           ordem de CampoDespesa é a ordem da tabela de despesa da spec` 
           (T-004); `RN-010 AMB-021 sem fornecedor e valor −5,00 → causas
            fornecedor ausente e valor não positivo nessa ordem` (T-006);
            `AMB-021 sem identificador e data 2026-02-30 → causas identif
           icador ausente e data inexistente nessa ordem` (T-007) |
      616 -| AMB-022 | T-024, T-026 | `RN-013 AMB-022 tabela padrão vem d
          -a política e alterar o seu limite altera o resultado`; `RN-003
          - AMB-022 limiar lido da política: com limiar de 150,00, transp
          -orte de 120,00 sem NF não exige nota` (T-026) |               
      616 +| AMB-022 | T-024, T-026 | `RN-013 AMB-022 tabela padrão vem d
          +a política e alterar o seu limite altera o resultado`; `RN-003
          + AMB-022 limiar lido da política e com limiar de 150,00, trans
          +porte de 120,00 sem NF não exige nota` (T-026) |              
      617  | AMB-023 | T-024 | `RN-013 AMB-023 f-003 representacao sob a 
           tabela padrão → recusada com CATEGORIA_INELEGIVEL`; `RN-013 AM
           B-023 hospedagem no CC-ADM → recusada com CATEGORIA_INELEGIVEL
            sem consultar a tabela padrão` |
      618  | AMB-024 | T-024, T-028 | `RN-013 AMB-024 lote sem centro de 
           custo ou com centro de tipo incompatível → tabela padrão`; `AM
           B-024 centro_custo ausente, null ou numérico → tabela padrão` 
           (T-028) |
      619  | AMB-025 | T-024 | `RN-013 AMB-025 cc-comercial em minúsculas
            → tabela padrão e alimentacao 85,00 → 60,00 parcial` |

● Fiz os três commits do envelope na main, sem push, e o relatório está escrito
  mas não commitado.

  ┌───────────────────┬─────────────────────────────────────────────────────┐
  │      Commit       │                      Conteúdo                       │
  ├───────────────────┼─────────────────────────────────────────────────────┤
  │ a04c1d3           │ spec 2.0 com as RN-013 e RN-014 novas, as AMB-022 a │
  │ docs(spec)        │  AMB-038 e todas as seções pedidas, mais            │
  │                   │ docs/analise-envelope.md                            │
  ├───────────────────┼─────────────────────────────────────────────────────┤
  │ c89de59           │ D-012: o envelope inteiro como um evento, o que foi │
  │ docs(decisions)   │  invalidado e as tasks a refazer                    │
  ├───────────────────┼─────────────────────────────────────────────────────┤
  │ 46996d2           │ T-021 a T-032, notas "Revisão D-012" nas tasks      │
  │ docs(tasks)       │ pendentes afetadas e a matriz de cobertura          │
  └───────────────────┴─────────────────────────────────────────────────────┘

  O build da main está vermelho, como eu tinha avisado: 112 testes, 1 falha. O
  OrdemCamposSecao41Test falha porque a §4.1 ganhou a moeda e o código ainda não
  tem esse campo. Ele volta a passar na T-023.

  Onde escrevi diferente do que você ditou

  Avisei antes de escrever, e a D-012 registra os três pontos:
  1. A conversão vem antes da normalização na §8, e não "depois da RN-011".
     Converter depois de normalizar arredondaria duas vezes, o que a T4
     (AMB-030) rejeita. O aceite da RN-011 mostra a diferença: 33,333 EUR dá
     197,66 numa ordem e 197,65 na outra.
  2. O critério da P6 (AMB-026) é o limite 0,00, e não a observação "nao
     reembolsavel". A observação é texto livre, e o motor não interpreta texto
     livre.
  3. São 17 AMB. O bloco tinha 18 pontos, mas a P5 decorre da P2 e entrou como
     consequência da AMB-023.

  Decisões menores minhas, para você confirmar

  - Uma convenção para os aceites antigos. Os aceites e casos da §7 que usam
    despesas do lote do Dia 1 com os limites 60/80/250 passam a valer "num lote
    sem centro de custo", ou seja, sob a tabela padrão. O lote completo, sob o
    CC-ENG-PLATAFORMA, fica verificado na §9. Sem essa convenção, uns 30 aceites
    teriam de ser reescritos e deixariam de verificar a regra que verificam.
    Com ela, os 112 testes atuais continuam corretos.
  - O consolidado mostra a tabela aplicada (o centro de custo ou "padrão"). Isso
    deixa visível ao auditor o "cai na padrão em silêncio" das suas P3 e P4. Se
    preferir manter silencioso mesmo, é um campo a remover.
  - "Política inválida" inclui faltar a tabela padrão ou o limiar. E política ou
    cotações inválidas são informadas junto com as outras causas de lote, que é
    o princípio que a RN-010 já seguia.
  - Sem cotação não há valor em reais, então a faixa de valor não é avaliada. A
    e-006 sai só com a causa "moeda sem cotação".

  O §9 confirmado

  O lote do Dia 1 sob o CC-ENG-PLATAFORMA dá 386,93, com 6 integrais, 1 parcial,
  6 recusadas e 1 erro. A d-010 sai recusada pela P6, e a d-013 recebe
  [CATEGORIA_INELEGIVEL, SEM_NOTA_FISCAL]. A §9 também fixa os resultados dos
  lotes do envelope: 1.143,26 para o Rafael e 373,76 para a Dani.

  Achado de processo, que virou a T-022

  Os testes que leem a spec não rodaram de novo quando só a spec mudou. O Gradle
  não declara specs/ como entrada da tarefa de teste e os deu como atualizados.
  A falha só apareceu com --rerun-tasks. Sem isso, um ./gradlew build depois de
  mudar a spec diria verde.

  Relatório, do subagente

  O docs/RELATORIO.md está preenchido nas cinco seções, não commitado, com 13
  marcações [FALTA]. Três pontos dele pedem atenção sua:
  - As métricas do Envelope estão em [FALTA] porque ele escreveu antes desta
    absorção. Agora elas podem ser preenchidas com estes três commits: cerca de
    875 linhas na spec, 236 no tasks.md e nenhum código.
  - Ele achou datas trocadas, e o erro é meu. O arquivo de fatos que eu passei
    datava toda a sessão de 19/10, mas os commits das T-001 a T-007 e a D-010
    são de 18/10.
  - Outros dois pontos para você conferir:
    - o envelope foi commitado às 00:48 de 19/10, e o comunicado diz "não
      distribuir antes do Dia 2, 10h";
    - o CLAUDE.md cita o RastreabilidadeTest como se ele já existisse, mas ele
      só vem na T-016.

> git push

● Bash(git push 2>&1 && git status -sb | head -2)
  ⎿  (No output)

● Fiz o push: os três commits do envelope estão no GitHub (c5395fb..46996d2), e
  a main local e a origin/main estão iguais.

  O docs/RELATORIO.md continua modificado na sua máquina e não entrou no push,
  como você pediu.

  O CI, se houver, vai mostrar o build vermelho por causa do
  OrdemCamposSecao41Test. É a falha esperada, até a T-023 acrescentar a moeda ao
  código.

> Respostas aos [FALTA] do relatório:

  1. Delegação/envelope: eu decidi as 17 ambiguidades, o agente escreveu
     a spec 2.0. As T-021 a T-032 não serão implementadas nesta entrega.
  2. O relatório do subagente de revisão está na sessão 09, citado pela D-010.
  3. Nenhum skill ou MCP. Só subagente e CLAUDE.md como fiscal.
  4. Porcentagem de diffs lidos: [NÚMERO]%
  5. Em T-005 a T-013 li o diff de src/ e os nomes dos testes; não li
     todos os corpos de teste linha a linha.
  6. O commit vazio ed98aab foi arquivo não salvo no VS Code.

  Preencha as métricas do Envelope com a04c1d3, c89de59 e 46996d2:
  ~875 linhas na spec, 236 no tasks.md, nenhuma linha de código. Nenhum
  arquivo tocado na mão fora esses.

  Corrija as datas: T-001 a T-007 e a D-010 são de 18/10. Use sempre a
  data do commit.

  Use "seis lacunas" (D-009) e seis recusas suas às minhas redações:
  AMB-015, RN-012, A5, o caso grego da D-011, T-002 e a redação da D-001.

  Acrescente na Diligência o achado do Gradle: os testes que leem a spec
  não re-rodavam quando só a spec mudava, porque specs/ não é entrada
  declarada da tarefa de teste. Um ./gradlew build depois de mudar a spec
  dizia verde. Descoberto durante a absorção, virou a T-022.

  Acrescente também, no Fechamento, o que ficou de fora e por quê: T-014
  a T-020 (consolidado, bordas da §7, JSON, CLI, ponta a ponta) e T-021 a
  T-032 (envelope) não foram implementadas. A spec 2.0 absorveu o envelope
  por inteiro; o código parou na etapa 1 da §8 completa, com 112 testes.
  A escolha foi priorizar a absorção pela spec, que é o que o envelope
  avalia, sobre código que não caberia no tempo.

  Depois commite:
  docs(relatorio): relatorio final — delegacao, descricao, discernimento,
  diligencia e envelope

  Read 1 file (ctrl+o to expand)

● Antes de editar, pego no git os números exatos dos três commits do envelope
  (linhas e horários):

● Bash(for c in 2aed7d1 c5395fb a04c1d3 c89de59 46996d2; do git show -s
      --format='%h %ad %s' --date=format:'%d/%m %H:%M' $c | cut -c1-80; git show
      --shortstat --format…)
  ⎿  (No output)

● Confirmei os números: spec +875/−155, docs/analise-envelope.md +94, DECISIONS
  +148 e tasks.md +218/−18, com commits entre 00:48 e 01:21. Agora vou checar
  onde o relatório menciona "cinco" lacunas e se a sessão 09 realmente contém a
  revisão do subagente.

● Bash(grep -n 'cinco\|19/10\|18/10' docs/RELATORIO.md | cut -c1-120; grep -c
      'subagente\|D4' docs/sessions/09-tasks-e-rastreabilidade.md; grep -n
      'Achado\|achados\|14…)
  ⎿  (No output)

● A sessão 09 não serve como fonte: foi exportada às 21:32 de 18/10 (antes da
  revisão, por volta de 23:40, D-010) e não menciona "subagente" nem "D4". Vou
  verificar o export da sessão 10, commitado em c5395fb.

● Bash(ls docs/sessions/; f=$(ls docs/sessions/10-* 2>/dev/null | head -1); echo
      "arquivo: $f"; grep -n 'Revisão independente: specs\|Categoria (a):
      Contradição\|Tabel…)
  ⎿  (No output)

● O relatório estava na sessão 10, não na 09
  (docs/sessions/10-implementacao-regras-e-envelope.md, a partir da linha 1289).
  Vou citar essa sessão correta e sinalizar isso no final; agora sigo com as
  edições.

● Update(docs\RELATORIO.md)
  ⎿  Added 1 line, removed 1 line
      24  | Desenhar a arquitetura | O agente propôs e eu fixei a stack e
           aprovei | Fixei Kotlin/JVM 17 e os quatro pontos que o plan pr
          ecisava resolver (`docs/sessions/08-plan-tecnico.md`, primeiro 
          prompt). O agente propôs a arquitetura e a representação da pol
          ítica. Aprovei a proposta "no conteúdo" e escolhi o JDK (opção 
          a, DT-001). Commit `ac2c7a6`. |
      25  | Implementar | O agente | T-001 a T-013 (`3c18414` … `8d6c8b6`
          ), todos com `Co-Authored-By: Claude`. Meu papel foi aprovar fr
          onteiras e parar quando aparecia lacuna de spec. Na T-008 aprov
          ei explicitamente três fronteiras (sessão de implementação, `5a
          f7f0b`). |
      26  | Escrever testes | O agente | Todos os arquivos em `tests/` en
          traram em commits co-autorados pelo agente. Não escrevi nenhuma
           linha de teste. Em "Diligência" explico o que fiz para não con
          fiar cegamente neles. |
      27 -| Absorver o envelope | Por enquanto, só a análise, feita pelo 
         -agente | A análise do que a v4 quebra foi feita na sessão, ante
         -s de qualquer mudança na spec. A absorção em si ainda não foi c
         -ommitada: depois de `2aed7d1` não há nenhum commit. [FALTA: que
         -m vai fazer o quê na absorção do envelope] |                   
      27 +| Absorver o envelope | O agente analisou, eu decidi, o agente 
         +escreveu | O agente fez a análise do que a v4 quebra antes de q
         +ualquer mudança na spec (`docs/analise-envelope.md`). Eu decidi
         + as 17 ambiguidades novas, em bloco. O agente escreveu a spec 2
         +.0 (`a04c1d3`), a D-012 (`c89de59`) e as T-021 a T-032 (`46996d
         +2`). Na escrita, ele divergiu do meu texto em três pontos e avi
         +sou antes de escrever (D-012, "Como as decisões foram tomadas")
         +. As T-021 a T-032 não serão implementadas nesta entrega. |    
      28  
      29  **Onde deleguei e me arrependi:**
      30  

● Update(docs\RELATORIO.md)
  ⎿  Added 1 line, removed 1 line
      38  
      39  **Usei subagentes / skills / MCP / hooks?**
      40  
      41 -- **Subagente: sim, uma vez.** Antes da T-005, um subagente gen
         -eral-purpose revisou a spec 1.1 com contexto limpo, sem acesso 
         -a `plan.md`, `tasks.md`, `DECISIONS.md` nem ao código, e só rel
         -atou, sem alterar nada (D-010, "Gatilho"). Ele trouxe 14 achado
         -s em quatro categorias (A1–A5, B1–B2, C1, D1–D6, listados na D-
         -010). Quatro foram corrigidos na spec 1.2 (`2af517f`): D4 (pont
         -o de código), A1/D5 (vocabulário de categoria), C1/B1 (causa ún
         -ica) e A5 (alcance da RN-006). Os demais ficaram em "Pendente" 
         -na D-010. O plan e as tasks passaram a referenciar a 1.2, e a D
         -T-007 deixou de decidir a RN-009 e passou a implementá-la (`818
         -d6a6`). Valeu a pena: nenhum desses quatro tinha sido visto pel
         -a sessão que escreveu a spec (`docs/notas-trabalho.md`, nota de
         - 03005ba). A classificação do subagente, porém, não foi aceita 
         -como veio. Ele chamou o D4 ("códigos dos caracteres" na RN-009)
         - de vazamento técnico de gravidade baixa. A sessão principal o 
         -reclassificou como regra de negócio decidida fora da spec (pela
         - DT-007), de gravidade média, e eu confirmei (D-010, "Classific
         -ação"; sessão de implementação, `2af517f`). Ou seja: o revisor 
         -achou o problema e errou o peso. [FALTA: o relatório original d
         -o subagente, com os 14 achados no texto dele, não está no repos
         -itório]                                                        
      41 +- **Subagente: sim, uma vez.** Antes da T-005, um subagente gen
         +eral-purpose revisou a spec 1.1 com contexto limpo, sem acesso 
         +a `plan.md`, `tasks.md`, `DECISIONS.md` nem ao código, e só rel
         +atou, sem alterar nada (D-010, "Gatilho"). Ele trouxe 14 achado
         +s em quatro categorias (A1–A5, B1–B2, C1, D1–D6, listados na D-
         +010). Quatro foram corrigidos na spec 1.2 (`2af517f`): D4 (pont
         +o de código), A1/D5 (vocabulário de categoria), C1/B1 (causa ún
         +ica) e A5 (alcance da RN-006). Os demais ficaram em "Pendente" 
         +na D-010. O plan e as tasks passaram a referenciar a 1.2, e a D
         +T-007 deixou de decidir a RN-009 e passou a implementá-la (`818
         +d6a6`). Valeu a pena: nenhum desses quatro tinha sido visto pel
         +a sessão que escreveu a spec (`docs/notas-trabalho.md`, nota de
         + 03005ba). A classificação do subagente, porém, não foi aceita 
         +como veio. Ele chamou o D4 ("códigos dos caracteres" na RN-009)
         + de vazamento técnico de gravidade baixa. A sessão principal o 
         +reclassificou como regra de negócio decidida fora da spec (pela
         + DT-007), de gravidade média, e eu confirmei (D-010, "Classific
         +ação"; sessão de implementação, `2af517f`). Ou seja: o revisor 
         +achou o problema e errou o peso. O relatório original do subage
         +nte, com os 14 achados no texto dele, está no export da sessão 
         +10 (`docs/sessions/10-implementacao-regras-e-envelope.md`, a pa
         +rtir de "Revisão independente: specs/001-motor-reembolso/spec.m
         +d (v1.1)"), e é citado pela D-010.                             
      42  - **CLAUDE.md como fiscal.** Usei as "Regras de trabalho" que v
          ieram do template, sem mudança ("Se eu te explicar uma regra qu
          e não está na spec, pare e me diga isso"). Foram elas que fizer
          am o agente parar no plan (D-009) e antes da T-010 (D-011). Em 
          `c5a61d0` só removi o cabeçalho do template.
      43  - **Skills, MCP, hooks: não.** O repositório não tem `.claude/`
          . Por quê: anotei em `49672be` que subagente, MCP e hooks "são 
          multiplicadores de execução, não de direção", e que usá-los ant
          es da spec seria "chegar mais rápido ao lugar errado". [FALTA: 
          se houve alguma skill ou MCP usado fora do repositório, não há 
          registro]
      44  

● Update(docs\RELATORIO.md)
  ⎿  Added 1 line, removed 1 line
      40  
      41  - **Subagente: sim, uma vez.** Antes da T-005, um subagente gen
          eral-purpose revisou a spec 1.1 com contexto limpo, sem acesso 
          a `plan.md`, `tasks.md`, `DECISIONS.md` nem ao código, e só rel
          atou, sem alterar nada (D-010, "Gatilho"). Ele trouxe 14 achado
          s em quatro categorias (A1–A5, B1–B2, C1, D1–D6, listados na D-
          010). Quatro foram corrigidos na spec 1.2 (`2af517f`): D4 (pont
          o de código), A1/D5 (vocabulário de categoria), C1/B1 (causa ún
          ica) e A5 (alcance da RN-006). Os demais ficaram em "Pendente" 
          na D-010. O plan e as tasks passaram a referenciar a 1.2, e a D
          T-007 deixou de decidir a RN-009 e passou a implementá-la (`818
          d6a6`). Valeu a pena: nenhum desses quatro tinha sido visto pel
          a sessão que escreveu a spec (`docs/notas-trabalho.md`, nota de
           03005ba). A classificação do subagente, porém, não foi aceita 
          como veio. Ele chamou o D4 ("códigos dos caracteres" na RN-009)
           de vazamento técnico de gravidade baixa. A sessão principal o 
          reclassificou como regra de negócio decidida fora da spec (pela
           DT-007), de gravidade média, e eu confirmei (D-010, "Classific
          ação"; sessão de implementação, `2af517f`). Ou seja: o revisor 
          achou o problema e errou o peso. O relatório original do subage
          nte, com os 14 achados no texto dele, está no export da sessão 
          10 (`docs/sessions/10-implementacao-regras-e-envelope.md`, a pa
          rtir de "Revisão independente: specs/001-motor-reembolso/spec.m
          d (v1.1)"), e é citado pela D-010.
      42  - **CLAUDE.md como fiscal.** Usei as "Regras de trabalho" que v
          ieram do template, sem mudança ("Se eu te explicar uma regra qu
          e não está na spec, pare e me diga isso"). Foram elas que fizer
          am o agente parar no plan (D-009) e antes da T-010 (D-011). Em 
          `c5a61d0` só removi o cabeçalho do template.
      43 -- **Skills, MCP, hooks: não.** O repositório não tem `.claude/`
         -. Por quê: anotei em `49672be` que subagente, MCP e hooks "são 
         -multiplicadores de execução, não de direção", e que usá-los ant
         -es da spec seria "chegar mais rápido ao lugar errado". [FALTA: 
         -se houve alguma skill ou MCP usado fora do repositório, não há 
         -registro]                                                      
      43 +- **Skills, MCP, hooks: não.** O repositório não tem `.claude/`
         +. Por quê: anotei em `49672be` que subagente, MCP e hooks "são 
         +multiplicadores de execução, não de direção", e que usá-los ant
         +es da spec seria "chegar mais rápido ao lugar errado". Também n
         +ão usei nenhuma skill nem MCP fora do repositório: só o subagen
         +te e o CLAUDE.md como fiscal.                                  
      44  
      45  ---
      46  

● Update(docs\RELATORIO.md)
  ⎿  Added 1 line, removed 1 line
      137  **Padrão que eu notei:**
      138  
      139  - **Onde ele erra:** em afirmações sobre o conteúdo dos dados 
           feitas de passagem (Caso 1) e em propostas conservadoras de nã
           o mexer em texto já salvo (Caso 1-B). O sinal que passou a me 
           deixar em alerta foi qualquer frase do tipo "no exemplo, nenhu
           m…": afirmação universal sobre o JSON, feita sem tabela de núm
           eros ao lado.
      140 -- **O padrão inverso, que foi mais frequente: erro meu pego po
          -r ele.** Registro como tal, sem suavizar. Ele recusou ou corri
          -giu redações que eu ditei pelo menos seis vezes:              
      140 +- **O padrão inverso, que foi mais frequente: erro meu pego po
          +r ele.** Registro como tal, sem suavizar. Ele recusou ou corri
          +giu redações que eu ditei seis vezes:                         
      141    1. **AMB-015.** Pus o fornecedor na lista de campos "que nen
           huma regra utiliza", e a frase seguinte declarava todos esses 
           campos opcionais. Com isso, a RN-009 ficaria sem chave quando 
           o fornecedor faltasse. Eu tinha levantado a dúvida no próprio 
           pedido ("Verifique se a redação acima resolve isso"), mas a re
           dação que propus não a resolvia. Ele tirou o fornecedor da lis
           ta e separou "campo não utilizado" de "campo usado apenas como
            chave" (`docs/sessions/06-precisao-e-campos-sem-regra.md`, tr
           echo "A sua redação para o W2 não resolvia o caso do fornecedo
           r"; `f0a9d09`; nota em `45cf6cc`).
      142    2. **RN-012.** Pedi que a RN-012 declarasse que "d-006" e "D
           -006" não formam duplicata. Estava errado: o identificador não
            integra o critério da RN-009, e a consequência faria a RN-012
            contradizer a RN-009. Ele não gravou a consequência ("deixei 
           de fora a consequência que você pediu para a RN-012, porque el
           a está errada"), e a análise expôs a lacuna da ordenação descr
           ita em Descrição (`docs/sessions/07-fechamento-da-spec.md`; `9
           448765`, D-007). Respondi "Você está certo e eu errei".
      143    3. **A5 da revisão independente.** Ditei "não participa de n
           enhuma regra de elegibilidade nem de valor" para a despesa for
           a do período. Isso contradizia a RN-007 e o caso da §7 em que 
           a despesa de 150,00 sem NF fora do período recebe [FORA_DA_COM
           PETENCIA, SEM_NOTA_FISCAL]. Ele recusou e propôs a redação que
            entrou: só regras de valor e duplicidade deixam de se aplicar
            (D-010, A5; sessão de implementação, `2af517f`; nota em `8c73
           e05`). Se eu não tivesse sido corrigido, o erro entraria no có
           digo da T-005 e a saída teria um motivo a menos.

● Update(docs\RELATORIO.md)
  ⎿  Added 1 line, removed 1 line
      157  
      158  - **Fase de spec (18/10).** O agente montava a proposta numa c
           ópia e me mostrava o diff. Nada ia para o `spec.md` sem a minh
           a aprovação (por exemplo, "Se aprovar, copio a proposta", `doc
           s/sessions/02-ambiguidade-005.md`). Depois de cada decisão eu 
           pedia uma varredura por referências que tinham ficado obsoleta
           s. Ela passou a ser sistemática a partir da AMB-005 e da AMB-0
           07 (`docs/notas-trabalho.md`, "Delegação"), e cada achado viro
           u uma entrada D-001 a D-005. Na AMB-009, a varredura achou uma
            consequência não prevista (diárias no mesmo hotel indistinguí
           veis de duplicata, o par d-006/d-007), registrada na §10 e lev
           ada à AMB-011 (D-003, D-005).
      159  - **Fase de implementação (18–19/10).** Rodei os testes a cada
            task: 112 testes, todos passando, ao final da T-013 (sessão d
           e implementação, `8d6c8b6`; `build/test-results/test/`, 19 arq
           uivos, 0 falhas). A partir da T-004 (`b6734a8`), o agente pass
           ou a verificar que os testes falham quando devem: fazia altera
           ções deliberadas no código (inverter enum, trocar ordem, impor
           t proibido, bordas exclusivas, comparador errado), conferia qu
           antos testes quebravam e restaurava o código. Fez isso da T-00
           4 à T-013. Não há registro disso no repositório além da sessão
            de implementação. Aprovei explicitamente as fronteiras provis
           órias da T-008 (`5af7f0b`): TODO de categoria inelegível, fech
           ado na T-010 (`97504e0`); `notaExigida` calculado antes da T-0
           11; consolidado antes da T-014.
      160 -- **Paradas por bug de spec.** Quando a implementação revelou 
          -uma lacuna, parei, decidi, registrei na spec e só então segui.
          - Aconteceu no plan (D-009, `4940b8c`, antes de `ac2c7a6`) e an
          -tes da T-010 (D-011, `d7b07b6`, antes de `5af7f0b`). Nessa seg
          -unda vez, o agente verificou no JDK que, na localidade turca, 
          -"ALIMENTACAO" vira "alımentacao" e d-014 seria recusada.      
      160 +- **Paradas por bug de spec.** Quando a implementação revelou 
          +uma lacuna, parei, decidi, registrei na spec e só então segui.
          + Aconteceu no plan, com as seis lacunas da D-009 (`4940b8c`, a
          +ntes de `ac2c7a6`), e antes da T-010 (D-011, `d7b07b6`, antes 
          +de `5af7f0b`). Nessa segunda vez, o agente verificou no JDK qu
          +e, na localidade turca, "ALIMENTACAO" vira "alımentacao" e d-0
          +14 seria recusada.                                            
      161  
      162  **Li o diff inteiro em que porcentagem das entregas?** [FALTA:
            porcentagem real; não contei]. O que a evidência permite dize
           r:
      163  - Na fase de spec, tenho nota de ter lido o diff inteiro na AM
           B-001, AMB-002 e AMB-004 (`docs/notas-trabalho.md`). Para as d
           emais AMB não anotei.

● Update(docs\RELATORIO.md)
  ⎿  Added 1 line, removed 1 line
      159  - **Fase de implementação (18–19/10).** Rodei os testes a cada
            task: 112 testes, todos passando, ao final da T-013 (sessão d
           e implementação, `8d6c8b6`; `build/test-results/test/`, 19 arq
           uivos, 0 falhas). A partir da T-004 (`b6734a8`), o agente pass
           ou a verificar que os testes falham quando devem: fazia altera
           ções deliberadas no código (inverter enum, trocar ordem, impor
           t proibido, bordas exclusivas, comparador errado), conferia qu
           antos testes quebravam e restaurava o código. Fez isso da T-00
           4 à T-013. Não há registro disso no repositório além da sessão
            de implementação. Aprovei explicitamente as fronteiras provis
           órias da T-008 (`5af7f0b`): TODO de categoria inelegível, fech
           ado na T-010 (`97504e0`); `notaExigida` calculado antes da T-0
           11; consolidado antes da T-014.
      160  - **Paradas por bug de spec.** Quando a implementação revelou 
           uma lacuna, parei, decidi, registrei na spec e só então segui.
            Aconteceu no plan, com as seis lacunas da D-009 (`4940b8c`, a
           ntes de `ac2c7a6`), e antes da T-010 (D-011, `d7b07b6`, antes 
           de `5af7f0b`). Nessa segunda vez, o agente verificou no JDK qu
           e, na localidade turca, "ALIMENTACAO" vira "alımentacao" e d-0
           14 seria recusada.
      161  
      162 -**Li o diff inteiro em que porcentagem das entregas?** [FALTA:
          - porcentagem real; não contei]. O que a evidência permite dize
          -r:                                                            
      162 +**Li o diff inteiro em que porcentagem das entregas?** [NÚMERO
          +]%. O que a evidência permite dizer:                          
      163  - Na fase de spec, tenho nota de ter lido o diff inteiro na AM
           B-001, AMB-002 e AMB-004 (`docs/notas-trabalho.md`). Para as d
           emais AMB não anotei.
      164  - Na implementação, não tenho nota de leitura de diff de nenhu
           ma task. Os timestamps mostram T-008 a T-013 commitadas entre 
           00:24 e 00:42 de 19/10, cerca de 950 linhas inseridas em 18 mi
           nutos (`git show --shortstat` de `5af7f0b` a `8d6c8b6`). T-012
            e T-013 caíram no mesmo minuto. Esses números não sustentam a
            afirmação de que li cada linha. [FALTA: confirmar o que de fa
           to li nos diffs de T-005 a T-013]
      165  
● Update(docs\RELATORIO.md)
  ⎿  Added 1 line, removed 1 line
      161  
      162  **Li o diff inteiro em que porcentagem das entregas?** [NÚMERO
           ]%. O que a evidência permite dizer:
      163  - Na fase de spec, tenho nota de ter lido o diff inteiro na AM
           B-001, AMB-002 e AMB-004 (`docs/notas-trabalho.md`). Para as d
           emais AMB não anotei.
      164 -- Na implementação, não tenho nota de leitura de diff de nenhu
          -ma task. Os timestamps mostram T-008 a T-013 commitadas entre 
          -00:24 e 00:42 de 19/10, cerca de 950 linhas inseridas em 18 mi
          -nutos (`git show --shortstat` de `5af7f0b` a `8d6c8b6`). T-012
          - e T-013 caíram no mesmo minuto. Esses números não sustentam a
          - afirmação de que li cada linha. [FALTA: confirmar o que de fa
          -to li nos diffs de T-005 a T-013]                             
      164 +- Na implementação, não tenho nota de leitura de diff de nenhu
          +ma task. Os timestamps mostram T-008 a T-013 commitadas entre 
          +00:24 e 00:42 de 19/10, cerca de 950 linhas inseridas em 18 mi
          +nutos (`git show --shortstat` de `5af7f0b` a `8d6c8b6`). T-012
          + e T-013 caíram no mesmo minuto. Esses números não sustentam a
          + afirmação de que li cada linha, e eu não a faço. Em T-005 a T
          +-013 li o diff de `src/` e os nomes dos testes; não li todos o
          +s corpos de teste linha a linha.                              
      165  
      166  **O que aceitei sem verificar direito, e o que me custou:**
      167  

● Update(docs\RELATORIO.md)
  ⎿  Added 1 line, removed 1 line
      166  **O que aceitei sem verificar direito, e o que me custou:**
      167  
      168  - **A afirmação sobre d-004** (Discernimento, Caso 1). Custou 
           um raciocínio errado, que só não virou decisão errada porque o
            agente se corrigiu.
      169 -- **O `docs/notas-trabalho.md` commitado vazio.** Em `ed98aab`
          - (18/10 13:36, commit meu, sem co-autor), a mensagem é "docs: 
          -nota de diligencia da AMB-001", mas o arquivo entrou com 0 byt
          -es: o diff mostra `0 insertions(+), 0 deletions(-)`, e o blob 
          -é o vazio (`e69de29`). A nota da AMB-001 só entrou no repositó
          -rio 3h15 depois, em `7d379cc` (16:51), cujo diff parte de `ind
          -ex e69de29`. Nesse intervalo houve o commit da AMB-002 (`bca83
          -fa`). Não conferi o `--stat` antes de commitar. Custo concreto
          -: durante três horas, o histórico teve um commit que diz conte
          -r uma nota e não contém nada, e a "anotação na hora" que as no
          -tas pedem ("Anotar NA HORA") não ficou datada no commit que le
          -va o nome dela. [FALTA: a causa. A evidência é compatível com 
          -arquivo não salvo no editor no momento do commit, mas não há r
          -egistro que confirme] Houve um segundo incidente com o mesmo a
          -rquivo, do lado do agente: na T-003, o `git add -A` incluiu o 
          -`docs/notas-trabalho.md` no índice, e o agente o tirou antes d
          -o commit. A nota foi commitada separadamente em `338e1f6` (ses
          -são de implementação, `15ce265`).                             
      169 +- **O `docs/notas-trabalho.md` commitado vazio.** Em `ed98aab`
          + (18/10 13:36, commit meu, sem co-autor), a mensagem é "docs: 
          +nota de diligencia da AMB-001", mas o arquivo entrou com 0 byt
          +es: o diff mostra `0 insertions(+), 0 deletions(-)`, e o blob 
          +é o vazio (`e69de29`). A nota da AMB-001 só entrou no repositó
          +rio 3h15 depois, em `7d379cc` (16:51), cujo diff parte de `ind
          +ex e69de29`. Nesse intervalo houve o commit da AMB-002 (`bca83
          +fa`). Não conferi o `--stat` antes de commitar. Custo concreto
          +: durante três horas, o histórico teve um commit que diz conte
          +r uma nota e não contém nada, e a "anotação na hora" que as no
          +tas pedem ("Anotar NA HORA") não ficou datada no commit que le
          +va o nome dela. A causa: o arquivo não estava salvo no VS Code
          + no momento do commit. Houve um segundo incidente com o mesmo 
          +arquivo, do lado do agente: na T-003, o `git add -A` incluiu o
          + `docs/notas-trabalho.md` no índice, e o agente o tirou antes 
          +do commit. A nota foi commitada separadamente em `338e1f6` (se
          +ssão de implementação, `15ce265`).                            
      170  - **A ordenação da T-012.** A ordenação por `Motivo.ordinal`, 
           pedida pela task e pela DT-006, enfraqueceu os testes de ordem
            da T-010 e da T-011: uma coleta fora da ordem da §8 deixou de
            quebrá-los. O agente avisou antes de eu perguntar. Aceitei ma
           nter a ordenação, porque quem governa a saída é a §8. Esses te
           stes passam a verificar a saída e não a coleta, e a ordem cont
           inua amarrada pelo `OrdemMotivosSecao8Test`, que lê a spec (`d
           ocs/notas-trabalho.md`, nota em `8c73e05`; comentário em `src/
           reembolso/dominio/Motor.kt`, função `recusar`; `5db55cc`). É u
           m custo aceito conscientemente, não um descuido. Fica registra
           do porque reduz a cobertura.
      171  
      172  **Testes: quem escreveu, e como você sabe que eles testam a co
           isa certa?**

● Update(docs\RELATORIO.md)
  ⎿  Added 2 lines
      169  - **O `docs/notas-trabalho.md` commitado vazio.** Em `ed98aab`
            (18/10 13:36, commit meu, sem co-autor), a mensagem é "docs: 
           nota de diligencia da AMB-001", mas o arquivo entrou com 0 byt
           es: o diff mostra `0 insertions(+), 0 deletions(-)`, e o blob 
           é o vazio (`e69de29`). A nota da AMB-001 só entrou no repositó
           rio 3h15 depois, em `7d379cc` (16:51), cujo diff parte de `ind
           ex e69de29`. Nesse intervalo houve o commit da AMB-002 (`bca83
           fa`). Não conferi o `--stat` antes de commitar. Custo concreto
           : durante três horas, o histórico teve um commit que diz conte
           r uma nota e não contém nada, e a "anotação na hora" que as no
           tas pedem ("Anotar NA HORA") não ficou datada no commit que le
           va o nome dela. A causa: o arquivo não estava salvo no VS Code
            no momento do commit. Houve um segundo incidente com o mesmo 
           arquivo, do lado do agente: na T-003, o `git add -A` incluiu o
            `docs/notas-trabalho.md` no índice, e o agente o tirou antes 
           do commit. A nota foi commitada separadamente em `338e1f6` (se
           ssão de implementação, `15ce265`).
      170  - **A ordenação da T-012.** A ordenação por `Motivo.ordinal`, 
           pedida pela task e pela DT-006, enfraqueceu os testes de ordem
            da T-010 e da T-011: uma coleta fora da ordem da §8 deixou de
            quebrá-los. O agente avisou antes de eu perguntar. Aceitei ma
           nter a ordenação, porque quem governa a saída é a §8. Esses te
           stes passam a verificar a saída e não a coleta, e a ordem cont
           inua amarrada pelo `OrdemMotivosSecao8Test`, que lê a spec (`d
           ocs/notas-trabalho.md`, nota em `8c73e05`; comentário em `src/
           reembolso/dominio/Motor.kt`, função `recusar`; `5db55cc`). É u
           m custo aceito conscientemente, não um descuido. Fica registra
           do porque reduz a cobertura.
      171  
      172 +- **Os testes que leem a spec não rodavam quando só a spec mud
          +ava.** `specs/` não é entrada declarada da tarefa de teste do 
          +Gradle. Depois de uma mudança só na spec, o Gradle considerava
          + os testes atualizados, e um `./gradlew build` dizia verde. De
          +scobri isso durante a absorção do envelope: a spec 2.0 acresce
          +ntou a moeda à tabela da §4.1, e o `OrdemCamposSecao41Test` só
          + falhou com `--rerun-tasks`. Ou seja, a proteção que eu aprese
          +nto abaixo como "testes que leem a própria spec" tinha um bura
          +co desde a T-004 (`b6734a8`). Virou a T-022 (D-012, "Achado de
          + processo"; `c89de59`, `46996d2`).                            
      173 +                                                              
      174  **Testes: quem escreveu, e como você sabe que eles testam a co
           isa certa?**
      175  
      176  Todos foram escritos pelo agente, o mesmo que escreveu o códig
           o. É exatamente o risco que o template aponta. O que tenho con
           tra ele:

● Update(docs\RELATORIO.md)
  ⎿  Added 1 line, removed 1 line
      175  
      176  Todos foram escritos pelo agente, o mesmo que escreveu o códig
           o. É exatamente o risco que o template aponta. O que tenho con
           tra ele:
      177  - **Nomes amarrados à spec.** Cada teste começa pelo ID do req
           uisito e, quando verifica uma ambiguidade, cita a AMB (convenç
           ão do `CLAUDE.md`), e há um arquivo por RN em `tests/reembolso
           /regras/`.
      178 -- **Testes que leem a própria spec.** `OrdemMotivosSecao8Test`
          - e `OrdemCamposSecao41Test` leem `specs/001-motor-reembolso/sp
          -ec.md` através de `tests/reembolso/rastreabilidade/Spec.kt`. S
          -e a ordem da §8 ou da §4.1 mudar na spec, o teste quebra sem q
          -ue ninguém precise lembrar.                                   
      178 +- **Testes que leem a própria spec.** `OrdemMotivosSecao8Test`
          + e `OrdemCamposSecao41Test` leem `specs/001-motor-reembolso/sp
          +ec.md` através de `tests/reembolso/rastreabilidade/Spec.kt`. S
          +e a ordem da §8 ou da §4.1 mudar na spec, o teste quebra sem q
          +ue ninguém precise lembrar, desde que rode: até a T-022, um bu
          +ild depois de mudar só a spec não os executa (ver acima).     
      179  - **Oráculo independente.** O teste dos 25 code points de `apa
           rar()` usa `\p{IsWhite_Space}` do JDK, e não a lista do própri
           o código. Na T-003 o agente detectou nos bytes que os escapes 
           tinham sido gravados como caracteres invisíveis e trocou por c
           onstrução por code point antes do commit (sessão de implementa
           ção, `15ce265`).
      180  - **Verificação de que falham.** As mutações deliberadas de T-
           004 a T-013 (sessão de implementação). Foram feitas pelo mesmo
            agente, e eu não as repeti.
      181  

● Agora a seção do envelope, com as métricas medidas, e o Fechamento:

● Update(docs\RELATORIO.md)
  ⎿  Added 8 lines, removed 4 lines
      192  
      193  *A mudança de requisito do Dia 2.*
      194  
      195 -**Quantos arquivos toquei na mão:** [FALTA: a absorção ainda n
          -ão foi feita. Depois de `2aed7d1` (arquivos do envelope) não h
          -á commit de spec, DECISIONS, tasks ou código. A Fase 5 de `tas
          -ks.md` continua com o texto do template]                      
      196 -**Quanto tempo levou:** [FALTA: os arquivos do envelope foram 
          -commitados em 19/10 às 00:48 (`2aed7d1`); falta o horário de f
          -im da absorção]                                               
      197 -**Diff de absorção:** [FALTA: `<n> arquivos, +<n>/-<n> linhas`
          - (`git diff 2aed7d1 HEAD --stat`, depois da absorção)]        
      195 +**Quantos arquivos toquei na mão:** nenhum. A absorção mexeu e
          +m quatro arquivos, todos de documentação, escritos pelo agente
          + a partir das minhas decisões: `spec.md`, `DECISIONS.md`, `tas
          +ks.md` e `docs/analise-envelope.md`. Nenhuma linha de código, 
          +e nenhum outro arquivo.                                       
      196 +**Quanto tempo levou:** 33 minutos entre o commit dos arquivos
          + do envelope (19/10 00:48, `2aed7d1`) e o último commit da abs
          +orção (19/10 01:21, `46996d2`). Esse intervalo inclui a anális
          +e, as minhas decisões e a escrita.                            
      197 +**Diff de absorção:** 3 commits, 4 arquivos, +1.335/−173 linha
          +s.                                                            
      198 +- `a04c1d3`: spec 2.0, +875/−155 em `spec.md`, mais `docs/anal
          +ise-envelope.md` (+94).                                       
      199 +- `c89de59`: D-012, +148 em `DECISIONS.md`.                   
      200 +- `46996d2`: T-021 a T-032, +218/−18 em `tasks.md`, ou seja, 2
          +36 linhas alteradas.                                          
      201 +- Código: 0 linhas.                                           
      202  
      199 -**Absorveu de graça:** isto é previsão da análise feita antes 
          -de mexer na spec, não resultado medido. [FALTA: confirmar com 
          -o diff real]                                                  
      203 +**Absorveu de graça:** a absorção parou na spec, então o que v
          +em abaixo foi confirmado na spec e no tasks.md, não no código.
          + No código, continua sendo previsão da análise, porque as T-02
          +1 a T-032 não foram implementadas. O que o diff confirma: nenh
          +uma das RN-004 a RN-007 nem a RN-012 mudou de texto, e os 112 
          +testes existentes continuam corretos pela convenção "lote sem 
          +centro de custo usa a tabela padrão" (spec 2.0, §4.2; D-012). 
      204  - **A ordem da §8 e a coleta de motivos.** A ordem vive numa f
           unção só, `Motor.avaliar` (DT-006). Um motivo novo é uma entra
           da na enum mais uma linha na §8, e o `OrdemMotivosSecao8Test` 
           cobra a coerência entre as duas.
      205  - **Duplicidade, `aparar()` e as validações de lote e de item.
           ** A duplicidade em duas passadas (DT-007), o `aparar()` e as 
           validações não dependem de limite nem de moeda.
      206  - **O ponto único `limiteAplicavel`.** O plan §4 previa que "u
           ma ampliação condicional altera essa função e mais nada". O li
           mite por centro de custo entra ali.

● Update(docs\RELATORIO.md)
  ⎿  Added 1 line, removed 1 line
      208  - **`Valor` e a RN-011.** A conversão de moeda é uma multiplic
           ação seguida de `normalizar`.
      209  - **O que ainda não existe não precisa ser desfeito.** Entrada
           , saída e CLI (T-017 a T-019) ainda não foram implementadas.
      210  
      211 -**Resistiu:** previsão da análise, não medição.               
      211 +**Resistiu:** previsão da análise. As T-023 a T-030 é que vão 
          +medir isso no código.                                         
      212  1. **`Politica.kt`.** A enum `Categoria`, com três valores e l
           imites fixos, não representa categorias por centro de custo vi
           ndas de arquivo: `representacao` no CC-COMERCIAL, hospedagem 0
           ,00 no CC-ENG-PLATAFORMA. Isso contradiz o plan §4 ("constante
           , não arquivo de configuração") e a linha do `CLAUDE.md` sobre
            a política.
      213  2. **A CLI.** Ela é fixa pelo DESAFIO (`calcular --input --out
           put`) e não diz de onde vêm `politica-v4.json` e `cambio.json`
           .
      214  3. **A AMB-015 "por construção".** O adaptador não lê o centro
            de custo, e os testes previstos da T-017 para a AMB-015 se in
           vertem.

● Update(docs\RELATORIO.md)
  ⎿  Added 5 lines, removed 1 line
      219  **Ordem em que fiz:**
      220  - Os arquivos do envelope chegaram em duas tentativas. Na prim
           eira, eles não existiam na máquina, e o agente parou em vez de
            responder no escuro. Na segunda, vieram do gist e foram copia
           dos por blob com hash conferido (sessão de implementação, `2ae
           d7d1`).
      221  - Antes de qualquer mudança na spec, o agente fez a análise co
           mpleta. Ela lista o que quebra por seção, RN e AMB; levanta as
            ambiguidades novas sem decidir nenhuma (P1–P6 para "aplica-se
            a política padrão", T1–T8 para "taxa da data da despesa", mai
           s V1, A1, R1, F1 e C1, que o envelope não aponta); e separa o 
           que absorve do que resiste.
      222 -- A partir daí, a ordem prevista é spec → DECISIONS → tasks (F
          -ase 5) → código, como pede o envelope. [FALTA: os commits da a
          -bsorção, que são o que prova a ordem]                         
      222 +- Com a análise na mão, decidi as 17 ambiguidades em bloco. A 
          +ordem dos commits é a que o envelope pede: spec (`a04c1d3`, 01
          +:17), DECISIONS (`c89de59`, 01:18), tasks (`46996d2`, 01:21). 
          +O código não foi feito.                                       
      223 +- Na escrita da spec, o agente não aplicou meu texto em três p
          +ontos e avisou antes (D-012, "Como as decisões foram tomadas")
          +:                                                             
      224 +  - eu tinha posto a conversão "depois da RN-011", o que arred
          +ondaria duas vezes, contra a minha própria T4 (AMB-030);      
      225 +  - meu critério para a P6 dependia do texto livre "nao reembo
          +lsavel";                                                      
      226 +  - eu tinha chamado de 17 decisões um bloco de 18 pontos.    
      227  
      228  **Se eu tivesse escrito a spec original sabendo desta mudança:
           **
      229  - Não teria aceito a política como constante sem pelo menos is
           olar a tabela de limites atrás de uma consulta por centro de c
           usto. Eu mesmo tinha escrito no pedido do plan que essa era a 
           decisão mais cara se a política mudasse (`docs/sessions/08-pla
           n-tecnico.md`).

● Update(docs\RELATORIO.md)
  ⎿  Added 8 lines
      243  
      244  ## Fechamento
      245  
      246 +**O que ficou de fora, e por quê:**                           
      247 +- **Não implementadas:**                                      
      248 +  - T-014 a T-020: consolidado, bordas da §7, `Rastreabilidade
          +Test`, entrada JSON, saída JSON, CLI e ponta a ponta;         
      249 +  - T-021 a T-032: o envelope no código.                      
      250 +- **Onde parou:** a spec 2.0 absorveu o envelope por inteiro (
          +`a04c1d3`, `c89de59`, `46996d2`). O código parou com a etapa 1
          + da §8 completa, em `8d6c8b6`, com 112 testes.                
      251 +- **Por quê:** priorizei a absorção pela spec, que é o que o e
          +nvelope avalia, em vez de código que não caberia no tempo.    
      252 +- **O custo:** não há CLI executável. O `OrdemCamposSecao41Tes
          +t` falha desde `a04c1d3`, por desenho, até a T-023 (D-012).   
      253 +                                                              
      254  **Para qual tamanho de projeto isto valeu a pena?**
      255  
      256  **Para qual não valeria?**

● Bash(grep -n 'FALTA\|NÚMERO\|pelo menos seis\|cinco' docs/RELATORIO.md | cut
      -c1-140; git diff --stat docs/RELATORIO.md; git status --short)
  ⎿  (No output)

● Encontrei um único "cinco" remanescente na linha 22, mas é uma citação literal
  da sessão 08, então não vou alterá-la. Vou apenas acrescentar a referência às
  seis lacunas ao lado e commitar.

● Bash(grep -o 'Mas os cinco gaps vão para a spec primeiro")' docs/RELATORIO.md
      | head -1 && sed -i 's/Mas os cinco gaps vão para a spec primeiro")/Mas os
      cinco gaps v…)
  ⎿  1 file changed (binary, mode only or too large to show)

● Commitei o relatório na main como db07ead, com a mensagem que você deu. Não
  fiz push.

  O que mudou:
  - Delegação: a linha do envelope agora diz que o agente analisou, você decidiu
    as 17 ambiguidades e o agente escreveu a spec 2.0, a D-012 e as T-021 a
    T-032, que ficam sem implementação. Também registra que nenhuma skill nem
    MCP foi usada.
  - Discernimento: "seis vezes", sem o "pelo menos".
  - Diligência:
    - a parada do plan fala das seis lacunas da D-009;
    - o que você leu nas T-005 a T-013: o diff de src/ e os nomes dos testes;
    - a causa do commit vazio (ed98aab): arquivo não salvo no VS Code;
    - o achado do Gradle. Acrescentei também, no item dos testes que leem a
      spec, que essa proteção tinha um buraco desde a T-004.
  - Envelope:
    - nenhum arquivo tocado na mão;
    - 4 arquivos de documentação e 0 linhas de código;
    - 33 minutos (00:48 → 01:21);
    - +1.335/−173 linhas, detalhadas por commit (spec +875/−155; tasks +218/−18,
      que são as 236 alteradas);
    - a ordem spec → DECISIONS → tasks, com os horários dos commits.
  - Fechamento: nova seção "O que ficou de fora, e por quê", com o texto que
    você passou e o custo: não há CLI executável, e há 1 teste vermelho até a
    T-023.

  Três correções em relação ao que você me passou:
  1. O relatório do subagente está na sessão 10, não na 09. A sessão 09 foi
     exportada às 21:32 de 18/10, antes da revisão, e não menciona o subagente.
     O texto está em docs/sessions/10-implementacao-regras-e-envelope.md, a
     partir da linha 1289, e é isso que o relatório cita.
  2. "Cinco gaps" ficou numa citação literal sua da sessão 08. Não alterei a
     citação; acrescentei "ao serem escritos, viraram as seis lacunas da D-009".
  3. As datas do relatório já seguiam a data dos commits. As datas erradas
     (19/10 para a T-002 e para a revisão do subagente) estão no seu
     docs/notas-trabalho.md, que não alterei.

  Ainda há três marcações abertas no relatório, que está commitado assim:
  - Linha 162: "[NÚMERO]%". Você mandou o marcador, não o número, e eu não
    invento esse valor.
  - Linha 233: "[FALTA: o que mais eu mudaria, depois de absorver]".
  - Linha 240: "[FALTA: tempo ou retrabalho economizado, medido depois da
    absorção]".

  As perguntas do Fechamento (tamanho de projeto, o que faria diferente, o mais
  desconfortável) também continuam em branco. São respostas suas.

> Respostas às marcações abertas e ao Fechamento.

  Linha 162 — porcentagem de diffs lidos:

  60%. Nas tasks de spec e de decisão li tudo, linha a linha, porque era
  o artefato que eu estava construindo. Nas tasks de código, da T-005 à
  T-013, li o diff de src/ e os nomes dos testes, mas não os corpos dos
  testes. A T-008 à T-013 entraram em 18 minutos com cerca de 950 linhas:
  não dá para ler 950 linhas em 18 minutos e eu não li. O que me deixou
  confortável em aceitar foi o agente mostrar, em cada task, que os testes
  falhavam quando o código era alterado de propósito. Isso é uma prova
  indireta, não é ter lido.

  Linha 233 — o que mais eu mudaria, depois de absorver:

  A saída (§4.2) e a interface da CLI. A spec 2.0 passou a exigir que a
  saída mostre a moeda e o valor originais e a taxa usada, para o auditor
  conseguir refazer a conta. E o motor agora precisa de dois arquivos que
  ele não recebia antes, a política e o câmbio, mas o DESAFIO.md fixa a
  interface em --input e --output. Essa tensão está declarada na spec e
  não resolvida: não decidi se entram como flags novas, caminho
  convencional ou variável de ambiente. Deixei registrado em vez de
  decidir às pressas, porque essa decisão muda o contrato com quem chama
  o motor.

  Linha 240 — tempo ou retrabalho economizado, medido:

  33 minutos para absorver o envelope inteiro na spec, sem tocar em uma
  linha de código: 4 arquivos de documentação, +1.335/−173 linhas, entre
  00:48 e 01:21. O que fez ser rápido estava desenhado desde o plan: o
  limite era resolvido num ponto único (limiteAplicavel), o domínio não lê
  arquivos, e a categoria já era String e não enum no modelo de resultado,
  por causa do "coworking". A ordem da §8, a coleta de motivos e a
  duplicidade em duas passadas não precisaram mudar.

  O que não foi de graça foi o Politica.kt. O enum Categoria com três
  valores e limites fixos não representa um conjunto de categorias que
  varia por centro de custo e vem de um arquivo que muda sem aviso. E essa
  foi uma decisão minha, registrada no plan §4: recusei arquivo de
  configuração para não deixar o limite mudar sem o teste nem a spec
  mudarem. O envelope inverteu exatamente isso. A decisão estava certa
  para o que eu sabia e errada para o que veio depois — e eu só descobri
  porque o desafio tinha um Dia 2.

  Fechamento — para qual tamanho de projeto isso valeu a pena:

  Valeu pela densidade da regra, não pelo tamanho do código. São 112
  testes para umas 400 linhas de domínio, o que parece desproporcional até
  você lembrar que cada linha dessas decide quanto uma pessoa recebe de
  volta. Onde o erro custa dinheiro de alguém e a regra chega escrita em
  português ambíguo, escrever a spec primeiro paga. Eu diria que vale a
  partir do momento em que existe mais de uma leitura defensável do
  requisito — e aqui eram 21 só no Dia 1.

  Fechamento — para qual não valeria:

  Para um CRUD, ou qualquer coisa onde a regra é óbvia e o custo do erro é
  baixo. O nível de rigor da §4.1, com 25 code points de espaço em branco
  e conversão de caixa pelo mapeamento Unicode, é desperdício se o pior
  que acontece é um campo com espaço sobrando. O teste disso é simples:
  se eu não consigo escrever duas leituras defensáveis do requisito, não
  há ambiguidade para decidir, e a spec vira cerimônia.

  Fechamento — o que eu faria diferente:

  Duas coisas, as duas de processo.

  A revisão independente por subagente veio depois da T-004 e achou quatro
  problemas que mudavam resultado. Se ela tivesse vindo antes da primeira
  task de código, eu não teria revisado tasks já escritas. Foi a melhor
  hora que gastei no desafio e eu gastei tarde.

  E teria declarado specs/ como entrada da tarefa de teste no primeiro
  dia. O buraco existia desde a T-004: os testes que leem a spec não
  rodavam de novo quando só a spec mudava, e o build dizia verde. Eu
  construí uma proteção e não verifiquei se ela estava ligada. Só apareceu
  na absorção do envelope, por acidente.

  Fechamento — o mais desconfortável:

  Descobrir que a minha revisão era mais frágil do que eu achava. O agente
  afirmou que nenhuma despesa do exemplo cruzava os R$ 100 antes do corte
  e ficava abaixo depois, e eu usei a afirmação para decidir a AMB-006.
  Era falsa: a d-004 é exatamente esse caso, 100,01 de valor original e
  80,00 de reembolsável. Ele se corrigiu sozinho na rodada seguinte. Eu
  não cruzei com os números do JSON, que estavam abertos na minha frente.

  E não foi um episódio isolado: em seis momentos ele recusou ou corrigiu
  uma redação minha que estava errada — AMB-015, RN-012, o A5, o caso
  grego da D-011, a T-002 e a redação da D-001. É bom que ele tenha
  recusado. O desconfortável é que, nas seis, quem tinha o contexto
  completo era eu.

● Antes de colar o texto, confiro as afirmações factuais. Primeiro, o número de
  linhas do domínio:

  Searched for 1 pattern (ctrl+o to expand)

● O domínio tem 471 linhas (348 sem comentários/linhas em branco), confirmando
  as "umas 400". Vou ajustar minimamente dois pontos do texto que não se
  sustentam pela evidência e aplicar as respostas agora.

● Update(docs\RELATORIO.md)
  ⎿  Added 1 line, removed 1 line
      159  - **Fase de implementação (18–19/10).** Rodei os testes a cada
            task: 112 testes, todos passando, ao final da T-013 (sessão d
           e implementação, `8d6c8b6`; `build/test-results/test/`, 19 arq
           uivos, 0 falhas). A partir da T-004 (`b6734a8`), o agente pass
           ou a verificar que os testes falham quando devem: fazia altera
           ções deliberadas no código (inverter enum, trocar ordem, impor
           t proibido, bordas exclusivas, comparador errado), conferia qu
           antos testes quebravam e restaurava o código. Fez isso da T-00
           4 à T-013. Não há registro disso no repositório além da sessão
            de implementação. Aprovei explicitamente as fronteiras provis
           órias da T-008 (`5af7f0b`): TODO de categoria inelegível, fech
           ado na T-010 (`97504e0`); `notaExigida` calculado antes da T-0
           11; consolidado antes da T-014.
      160  - **Paradas por bug de spec.** Quando a implementação revelou 
           uma lacuna, parei, decidi, registrei na spec e só então segui.
            Aconteceu no plan, com as seis lacunas da D-009 (`4940b8c`, a
           ntes de `ac2c7a6`), e antes da T-010 (D-011, `d7b07b6`, antes 
           de `5af7f0b`). Nessa segunda vez, o agente verificou no JDK qu
           e, na localidade turca, "ALIMENTACAO" vira "alımentacao" e d-0
           14 seria recusada.
      161  
      162 -**Li o diff inteiro em que porcentagem das entregas?** [NÚMERO
          -]%. O que a evidência permite dizer:                          
      162 +**Li o diff inteiro em que porcentagem das entregas?** 60%. Na
          +s tasks de spec e de decisão li tudo, linha a linha, porque er
          +a o artefato que eu estava construindo. Nas tasks de código, d
          +a T-005 à T-013, li o diff de `src/` e os nomes dos testes, ma
          +s não os corpos dos testes. A T-008 à T-013 entraram em 18 min
          +utos com cerca de 950 linhas: não dá para ler 950 linhas em 18
          + minutos, e eu não li. O que me deixou confortável em aceitar 
          +foi o agente mostrar, em cada task, que os testes falhavam qua
          +ndo o código era alterado de propósito. Isso é uma prova indir
          +eta, não é ter lido. O que a evidência permite dizer:         
      163  - Na fase de spec, tenho nota de ter lido o diff inteiro na AM
           B-001, AMB-002 e AMB-004 (`docs/notas-trabalho.md`). Para as d
           emais AMB não anotei.
      164  - Na implementação, não tenho nota de leitura de diff de nenhu
           ma task. Os timestamps mostram T-008 a T-013 commitadas entre 
           00:24 e 00:42 de 19/10, cerca de 950 linhas inseridas em 18 mi
           nutos (`git show --shortstat` de `5af7f0b` a `8d6c8b6`). T-012
            e T-013 caíram no mesmo minuto. Esses números não sustentam a
            afirmação de que li cada linha, e eu não a faço. Em T-005 a T
           -013 li o diff de `src/` e os nomes dos testes; não li todos o
           s corpos de teste linha a linha.
      165  

● Update(docs\RELATORIO.md)
  ⎿  Added 3 lines, removed 1 line
      230  - Teria tratado "informação não prevista é ignorada" (RN-010, 
           D-007) e "centro de custo sem efeito" (AMB-015, §4.1) como dec
           isões de risco, com custo aceito explícito, e não como simplif
           icação.
      231  - Teria declarado na §4 a moeda do valor, mesmo que só BRL fos
           se aceito.
      232  
      233 -[FALTA: o que mais eu mudaria, depois de absorver]            
      233 +Depois de absorver, mudaria também a saída (§4.2) e a interfac
          +e da CLI:                                                     
      234 +- **A saída.** A spec 2.0 passou a exigir que a saída mostre a
          + moeda e o valor originais e a taxa usada, para o auditor cons
          +eguir refazer a conta.                                        
      235 +- **A CLI.** O motor agora precisa de dois arquivos que não re
          +cebia antes, a política e o câmbio, mas o `DESAFIO.md` fixa a 
          +interface em `--input` e `--output`. Essa tensão está declarad
          +a e não resolvida, na T-021 e na análise do envelope (`docs/an
          +alise-envelope.md`, "Resiste"). Não decidi se os arquivos entr
          +am como flags novas, caminho convencional ou variável de ambie
          +nte. Deixei registrado em vez de decidir às pressas, porque es
          +sa decisão muda o contrato com quem chama o motor.            
      236  
      237  **O que a spec me poupou, em concreto:**
      238  - **Uma análise de impacto endereçável.** A análise do envelop
           e pôde dizer, regra por regra, o que cai: RN-001, 002, 003, 00
           8, 009, 010 e 011; AMB-013, 015, 007, 001, 006, 014 e 009. E t
           ambém o que continua válido: o texto das RN-004, 005, 006, 007
            e 012. Não precisei reler o código para saber.

● Update(docs\RELATORIO.md)
  ⎿  Added 10 lines, removed 1 line
      239  - **Um impacto calculável.** A §7 e a §9 tinham os resultados 
           esperados do lote do Dia 1 item a item, o que permitiu calcula
           r o novo total (386,93 contra 623,43) antes de escrever uma li
           nha.
      240  - **Ambiguidades novas reconhecíveis pelo mesmo critério.** As
            decisões anteriores já tinham critério escrito: abrangência d
           o dano (AMB-020), alcance estreito da normalização (AMB-013), 
           campo sem regra (AMB-015). Por isso as ambiguidades novas, com
           o P3 (centro de custo ausente: padrão ou erro de lote) e T8 (m
           oeda nula ou minúscula), podem ser decididas por esses critéri
           os, e não do zero.
      241  
      242 -[FALTA: tempo ou retrabalho economizado, medido depois da abso
          -rção]                                                         
      242 +**O que isso economizou, medido:** 33 minutos para absorver o 
          +envelope inteiro na spec, sem tocar em uma linha de código: 4 
          +arquivos de documentação, +1.335/−173 linhas, entre 00:48 e 01
          +:21.                                                          
      243  
      244 +O que fez ser rápido estava desenhado desde o plan:           
      245 +- o limite era resolvido num ponto único (`limiteAplicavel`); 
      246 +- o domínio não lê arquivos;                                  
      247 +- a categoria já era `String`, e não enum, no modelo de result
          +ado, por causa do "coworking".                                
      248 +                                                              
      249 +A ordem da §8, a coleta de motivos e a duplicidade em duas pas
          +sadas não precisaram mudar.                                   
      250 +                                                              
      251 +O que não foi de graça foi o `Politica.kt`. O enum `Categoria`
          +, com três valores e limites fixos, não representa um conjunto
          + de categorias que varia por centro de custo e vem de um arqui
          +vo que muda sem aviso. E essa foi uma decisão minha, registrad
          +a no plan §4: recusei arquivo de configuração para não deixar 
          +o limite mudar sem o teste nem a spec mudarem. O envelope inve
          +rteu exatamente isso. A decisão estava certa para o que eu sab
          +ia e errada para o que veio depois, e eu só descobri porque o 
          +desafio tinha um Dia 2.                                       
      252 +                                                              
      253  ---
      254  
      255  ## Fechamento

● Bash(sed -n '/^\*\*Para qual tamanho de projeto/,$p' docs/RELATORIO.md | cat
      -A | cut -c1-80)
  ⎿  (No output)

● Update(docs\RELATORIO.md)
  ⎿  Added 13 lines
      264  
      265  **Para qual tamanho de projeto isto valeu a pena?**
      266  
      267 +Valeu pela densidade da regra, não pelo tamanho do código. São
          + 112 testes para umas 400 linhas de domínio (`src/reembolso/do
          +minio/`: 471 linhas, 348 sem comentários e linhas em branco). 
          +Isso parece desproporcional até você lembrar que cada linha de
          +ssas decide quanto uma pessoa recebe de volta. Onde o erro cus
          +ta dinheiro de alguém e a regra chega escrita em português amb
          +íguo, escrever a spec primeiro paga. Eu diria que vale a parti
          +r do momento em que existe mais de uma leitura defensável do r
          +equisito, e aqui eram 21 só no Dia 1.                         
      268 +                                                              
      269  **Para qual não valeria?**
      270  
      271 +Para um CRUD, ou qualquer coisa onde a regra é óbvia e o custo
          + do erro é baixo. O nível de rigor da §4.1, com 25 code points
          + de espaço em branco e conversão de caixa pelo mapeamento Unic
          +ode, é desperdício se o pior que acontece é um campo com espaç
          +o sobrando. O teste disso é simples: se eu não consigo escreve
          +r duas leituras defensáveis do requisito, não há ambiguidade p
          +ara decidir, e a spec vira cerimônia.                         
      272 +                                                              
      273  **O que eu faria diferente:**
      274  
      275 +Duas coisas, as duas de processo.                             
      276 +                                                              
      277 +- **A revisão independente mais cedo.** A revisão por subagent
          +e veio depois da T-004 e achou quatro problemas que mudavam re
          +sultado (D-010, `2af517f`). Se ela tivesse vindo antes da prim
          +eira task de código, eu não teria revisado tasks já escritas. 
          +Foi a melhor hora que gastei no desafio, e eu a gastei tarde. 
      278 +- **`specs/` como entrada dos testes desde o primeiro dia.** T
          +eria declarado `specs/` como entrada da tarefa de teste logo n
          +o início. O buraco existia desde a T-004: os testes que leem a
          + spec não rodavam de novo quando só a spec mudava, e o build d
          +izia verde. Eu construí uma proteção e não verifiquei se ela e
          +stava ligada. Só apareceu na absorção do envelope, por acident
          +e (T-022, D-012).                                             
      279 +                                                              
      280  **A coisa mais desconfortável que aprendi sobre como eu trabal
           ho com IA:**
      281 +                                                              
      282 +Descobrir que a minha revisão era mais frágil do que eu achava
          +. O agente afirmou que nenhuma despesa do exemplo cruzava os R
          +$ 100 antes do corte e ficava abaixo depois, e eu usei a afirm
          +ação no meu raciocínio sobre a AMB-006. Era falsa: a d-004 é e
          +xatamente esse caso, 100,01 de valor original e 80,00 de reemb
          +olsável. Ele se corrigiu sozinho na rodada seguinte, antes de 
          +a AMB-006 ser decidida (Discernimento, Caso 1). Eu não cruzei 
          +com os números do JSON, que estavam abertos na minha frente.  
      283 +                                                              
      284 +E não foi um episódio isolado. Em seis momentos ele recusou ou
          + corrigiu uma redação minha que estava errada: AMB-015, RN-012
          +, o A5, o caso grego da D-011, a T-002 e a redação da D-001 (D
          +iscernimento, "Padrão que eu notei"). É bom que ele tenha recu
          +sado. O desconfortável é que, nas seis, quem tinha o contexto 
          +completo era eu.                                              

● Bash(grep -n 'FALTA\|NÚMERO\|<nome>\|<link>\|<data>' docs/RELATORIO.md | cut
      -c1-120; git add docs/RELATORIO.md && git status --short && git commit -q
      -F - <<'EOF'
      d…)
  ⎿  (No output)

● Preenchi as três marcações e as quatro perguntas do Fechamento e commitei como
  c655ebb. Você não tinha pedido commit desta vez; se preferir outra mensagem,
  ou juntar com o db07ead, eu ajusto. Não fiz push.

  Ajustei o seu texto em dois pontos, para não contradizer a evidência nem o
  próprio relatório:
  1. Linha 233: "essa tensão está declarada na spec". A spec 2.0 não fala da
     CLI, porque isso é contrato de execução e fica no plan. A tensão está
     registrada na T-021 e na análise do envelope (seção "Resiste"), e o texto
     agora diz isso.
  2. No mais desconfortável: "usei a afirmação para decidir a AMB-006". O Caso 1
     do Discernimento diz que você usou a afirmação no raciocínio e que o
     agente se corrigiu antes de a AMB-006 ser decidida, que foi o que aconteceu
     (sessão 01, commit 0e148af). Ficou "usei a afirmação no meu raciocínio
     sobre a AMB-006 […] antes de a AMB-006 ser decidida". Com o texto original,
     o Fechamento contradiria o Caso 1.

  Conferi: "umas 400 linhas de domínio" se sustenta. São 471 linhas em
  src/reembolso/dominio/, 348 sem comentários e linhas em branco, e citei os
  dois números.

  Ainda aberto: o cabeçalho do relatório continua com os marcadores do template
  (<nome>, <link>, <data>). Pelo git, seriam "Sandro Barbosa" e
  https://github.com/Sanbarbosa11/sdd-desafio, mas a data de entrega é você quem
  define. Quer que eu preencha?
> Preencha o cabeçalho: Sandro Barbosa,
  https://github.com/Sanbarbosa11/sdd-desafio, data 19/10/2026.

  Depois, três coisas, cada uma no seu commit:

  1. Substitua o README.md da raiz pelo README do projeto. O atual é o do
     template do desafio. Deve ter: o que o motor faz, como buildar e
     rodar os testes (./gradlew build), a estrutura de pastas, o estado
     atual (etapa 1 da §8 completa, 112 testes, T-014 a T-032 em aberto)
     e por que o build está vermelho hoje — deixe claro que a falha do
     OrdemCamposSecao41Test é esperada, porque a spec 2.0 exige o campo
     moeda na §4.1 e o código só o ganha na T-023. Sem essa linha, quem
     clonar e rodar o build não sabe se é intencional.

     Se o template tiver instruções do desafio que valem manter, mova
     para docs/ antes de substituir.

     Commit: docs: README do projeto
  2. Preencha o campo "Commit:" de todas as tasks concluídas em tasks.md
     com os hashes reais.

     Commit: docs(tasks): hashes dos commits nas tasks concluidas
  3. git push

  Pode fazer os três em sequência, sem parar para confirmar entre eles.

● Começo pelo cabeçalho do relatório, num commit próprio, porque as três coisas
  que você listou têm commits definidos. Leio também o README atual:

● Bash(sed -i 's|^\*\*Aluno:\*\* `<nome>` · \*\*Repositório:\*\* `<link>` ·
      \*\*Data:\*\* `<data>`|**Aluno:** Sandro Barbosa · **Repositório:**
      https://github.com/Sanb…)
  ⎿  Updated docs\RELATORIO.md (+1 -1)
      1  # Relatório — Desafio SDD
      2  
      3 -**Aluno:** `<nome>` · **Repositório:** `<link>` · **Data:** `<da
        -ta>`                                                            
      3 +**Aluno:** Sandro Barbosa · **Repositório:** https://github.com/
        +Sanbarbosa11/sdd-desafio · **Data:** 19/10/2026                 
      4  
      5  > Isto não é redação. São **evidências**. Toda afirmação deve vi
         r acompanhada de
      6  > arquivo, hash de commit ou trecho de sessão exportada. Um pará
         grafo bonito sem

● Vou mover o README atual (com instruções do desafio) para
  docs/README-desafio.md usando git mv para preservar o histórico, e escrever um
  novo README do projeto. Antes disso, vou verificar o estado real do build em
  um clone limpo para descrevê-lo com precisão.

● Bash(cd /c/Users/dev/SDD-Desafio && rm -rf build-check && git clone -q
      sdd-desafio build-check && cd build-check && export JAVA_HOME="/c/Program
      Files/Android/Androi…)
  ⎿  (No output)
  ⎿  (timeout 10m)

● O clone limpo confirma o esperado: 112 testes com 1 falha em
  OrdemCamposSecao41Test (BUILD FAILED) e ./gradlew run executando sem efeito,
  já que main está vazio. Agora vou escrever o README.

● Bash(cd /c/Users/dev/SDD-Desafio/sdd-desafio && git mv README.md
      docs/README-desafio.md && git status --short)
  ⎿  (No output)

● Write(README.md)
  ⎿  Wrote 107 lines to README.md
       1 # Motor de cálculo de reembolso
       3 Motor que decide quanto de cada despesa de trabalho de um colabo
         rador é
          recebe um
          cada
       6 despesa, a classificação (integral, parcial, recusada ou erro),
       7 reembolsado, o limite aplicado e os motivos. Para o lote, devolv
         e o
       9
         io vive na
     … +132 lines (ctrl+o to expand)

● Bash(cd /c/Users/dev/SDD-Desafio/sdd-desafio && ls tests/reembolso; git add
      README.md docs/README-desafio.md && git status --short && git commit -q -F
      - <<'EOF'
      docs…)
  ⎿  (No output)

● O README está commitado (33d8ede). Agora os hashes: levanto no git o commit de
  cada task concluída.

● Bash(for i in $(seq -w 1 13); do t="T-0$i"; h=$(git log --format='%h'
      --grep="^feat($t)" ); echo "$t $h"; done; grep -n '^- \[x\]'
      specs/001-motor-reembolso/tasks.md…)
  ⎿  (No output)

  Made 1 scratchpad edit +30 (ctrl+o to expand)

● Bash(T=specs/001-motor-reembolso/tasks.md && node
      /c/Users/dev/AppData/Local/Temp/claude/c--Users-dev-SDD-Desafio/8aa9dd74-d
      4f2-42e8-b296-d294cb69c18d/scratchpad/has…)
  ⎿  Updated specs\001-motor-reembolso\tasks.md (+13 -13)
      43    - **Aceite:** `./gradlew --version` mostra Gradle 8.11.1 roda
          ndo no JDK 21,
      44      e `./gradlew build` termina com sucesso executando
      45      `AmbienteTest` › `build compila src e executa testes de tes
          ts`
      46 -  - **Commit:** —                                              
      46 +  - **Commit:** `3c18414`; `.gitattributes` do `gradlew` em `c5
         +a61d0`                                                         
      47  
      48  - [x] **T-002** — `value class Valor` com `normalizar` (`setSca
          le(2, HALF_UP)`)
      49    como único construtor, e os testes de guarda da aritmética (D
          T-003).
     ...
      53      `RN-011 AMB-014 100,005 normaliza para 100,01 e 100,004 par
          a 100,00`,
      54      `RN-011 AMB-014 −45,005 normaliza para −45,01 afastando-se 
          do zero`,
      55      `RN-011 AMB-014 54,90 e 54,900 são o mesmo valor`
      56 -  - **Commit:** —                                              
      56 +  - **Commit:** `9abc75c`                                      
      57  
      58  - [x] **T-003** — Função `aparar()` no domínio com o conjunto e
          xplícito dos 25
      59    code points da propriedade Unicode `White_Space` (DT-005).
     ...
      62      `§4-1 aparar remove os 25 code points White_Space nas extre
          midades`,
      63      `§4-1 aparar remove U+0085 e não remove U+001C a U+001F`,
      64      `§4-1 aparar não remove espaços do meio do texto`
      65 -  - **Commit:** —                                              
      65 +  - **Commit:** `15ce265`                                      
      66  
      67  - [x] **T-004** — Modelo do domínio (plan §3.1, §3.2, §4): `Lid
          o`, `LoteLido`,
      68    `Categoria` e `Politica` em `Politica.kt`, `Motivo`, `CampoDe
          spesa`, `Falha`,
     ...
      74      `OrdemMotivosSecao8Test` › `§8 AMB-018 ordem de Motivo é a 
          ordem da lista de elegibilidade da spec`,
      75      `OrdemCamposSecao41Test` › `§4-1 AMB-021 ordem de CampoDesp
          esa é a ordem da tabela de despesa da spec`,
      76      `FronteiraDominioTest` › `DT-009 nenhum arquivo do dominio 
          importa kotlinx ou java io`
      77 -  - **Commit:** —                                              
      77 +  - **Commit:** `b6734a8`                                      
      78  
      79  ## Fase 2 — Regras de negócio
      80  
     ...
       97      `RN-012 AMB-020 d-006 com espaço à esquerda repete d-006 →
            lote rejeitado`,
       98      `RN-012 d-006 e D-006 são identificadores distintos`,
       99      `RN-012 identificadores d-001 a d-014 distintos → sem erro
            de lote`
      100 -  - **Commit:** —                                             
      100 +  - **Commit:** `bcfc828`                                     
      101  
      102  - [x] **T-006** — Validação de item, campos: informação obriga
           tória ausente,
      103    tipo incompatível, data inexistente, texto obrigatório vazio
            após `aparar()`
     … 64 more lines (ctrl+o to expand)

● Fiz os três passos e o push. A main local e a origin/main estão iguais, no
  b14d328.

  ┌───────────────────────┬─────────────────────────────────────────────────┐
  │        Commit         │                    Conteúdo                     │
  ├───────────────────────┼─────────────────────────────────────────────────┤
  │ 8f0638d               │ cabeçalho com Sandro Barbosa, o link do         │
  │ docs(relatorio)       │ repositório e 19/10/2026, num commit próprio    │
  ├───────────────────────┼─────────────────────────────────────────────────┤
  │ 33d8ede docs: README  │ README novo; o do template foi movido para      │
  │ do projeto            │ docs/README-desafio.md                          │
  ├───────────────────────┼─────────────────────────────────────────────────┤
  │ b14d328 docs(tasks)   │ hashes reais nas 13 tasks concluídas            │
  └───────────────────────┴─────────────────────────────────────────────────┘

  O push (46996d2..b14d328) levou também os dois commits do relatório que
  estavam só na sua máquina, db07ead e c655ebb.

  README:
  - Antes de escrever, conferi o build num clone limpo: 112 testes, 1 falha no
    OrdemCamposSecao41Test, BUILD FAILED.
  - O README diz que essa falha é intencional: a spec 2.0 exige a moeda na §4.1,
    o código só ganha o campo na T-023, e os outros 111 testes passam.
  - Avisa também que, depois de mudar só a spec, o build pode sair verde sem
    rodar os testes, e indica o --rerun-tasks até a T-022.
  - Diz que ainda não há CLI: o ./gradlew run existe, mas não faz nada.
  - Inclui o pré-requisito de JDK 17+ no JAVA_HOME, os comandos (com o
    gradlew.bat do Windows), a estrutura de pastas e onde ler as decisões.

  Hashes: conferi no git que os 14 hashes existem. Na T-001, além do 3c18414,
  incluí o c5a61d0, que trouxe o .gitattributes do gradlew. As 19 tasks
  pendentes (T-014 a T-032) continuam com "—".

  Uma inconsistência que deixei como estava, porque você não pediu: a linha
  "Rodar" do CLAUDE.md ainda mostra ./gradlew run --args="calcular --input …
  --output …", como se a CLI existisse. O README diz o contrário. Se quiser,
  ajusto num commit docs:.

> Ajuste a linha "Rodar" do CLAUDE.md: a CLI não existe ainda, vem na
  T-019. Deixe o comando documentado como o contrato previsto, marcando
  que ainda não está implementado.

  Commit: docs: CLAUDE.md marca a CLI como prevista na T-019

  Searched for 1 pattern (ctrl+o to expand)

● Update(CLAUDE.md)
  ⎿  Added 4 lines, removed 1 line
      32  - Linguagem: Kotlin 2.0.21, bytecode e API do Java 17 (DT-001).
           Gradle 8.11.1
      33    via wrapper; exige `JAVA_HOME` com JDK 17+ (nesta máquina, o 
          JDK 21 do
      34    Android Studio).
      35 -- Rodar: `./gradlew run --args="calcular --input exemplos/despe
         -sas-exemplo.json --output saida.json"`                         
      35 +- Rodar: **ainda não implementado** — a CLI vem na T-019 (e rec
         +ebe política e                                                 
      36 +  câmbio na T-030). Contrato previsto:                         
      37 +  `./gradlew run --args="calcular --input exemplos/despesas-exe
         +mplo.json --output saida.json"`.                               
      38 +  Hoje `main` está vazio e o `run` não faz nada.               
      39  - Testes: `./gradlew test` (um só: `./gradlew test --tests 'ree
          mbolso.regras.RN002*'`)
      40  - Lint/format: nenhum; `./gradlew build` é o check.
      41  

● Bash(git add CLAUDE.md && git status --short && git commit -q -F - <<'EOF'
      docs: CLAUDE.md marca a CLI como prevista na T-019…)
  ⎿  (No output)

● Commitei o ajuste como 0a18202, com a mensagem que você deu. Ainda não fiz
  push.

  A linha "Rodar" do CLAUDE.md agora diz que a CLI ainda não está implementada e
  vem na T-019, com a política e o câmbio na T-030. O comando continua lá como
  contrato previsto, e a linha avisa que hoje main está vazio e o run não faz
  nada.