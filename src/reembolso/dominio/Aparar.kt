package reembolso.dominio

// DT-005: os 25 code points da propriedade Unicode White_Space (§4.1).
// Explícitos porque Char.isWhitespace() e String.strip() divergem dessa propriedade.
internal val ESPACOS_EM_BRANCO: Set<Int> = buildSet {
    addAll(0x0009..0x000D)
    add(0x0020)
    add(0x0085)
    add(0x00A0)
    add(0x1680)
    addAll(0x2000..0x200A)
    add(0x2028)
    add(0x2029)
    add(0x202F)
    add(0x205F)
    add(0x3000)
}

// Todos os 25 estão no BMP, então nenhum é metade de um par substituto.
fun String.aparar(): String {
    var inicio = 0
    var fim = length
    while (inicio < fim && this[inicio].code in ESPACOS_EM_BRANCO) inicio++
    while (fim > inicio && this[fim - 1].code in ESPACOS_EM_BRANCO) fim--
    return substring(inicio, fim)
}
