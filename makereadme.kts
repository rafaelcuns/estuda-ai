import java.io.File

val repoDir = File(".")
val readmeFile = File(repoDir, "README.md")

val header = """
# Estuda AI!
<img width="256" height="131" alt="image" src="https://github.com/user-attachments/assets/3ad5fbeb-34a8-40f8-99b2-1150cee13502" />

Um repositório de provas e listas antigas feito para ajudar nos estudos!  
Inspirado (na cara dura) em [awesome-ufma](https://github.com/elheremes/awesome-ufma/tree/master), obrigado pela ideia :3c

## Organização
Os arquivos estão organizados em diretórios seguindo esta ordem: **matéria > professor > período > PX (ou LX caso for uma lista)**

## Guia
""".trimIndent()

val footer = """
## Como contribuir?
1. Faça um **fork** do repositório.  
2. Adicione o conteúdo que deseja incluir.  
3. Abra um **Pull Request**.  
Se estiver tudo certo, o PR será revisado e aprovado

**Nota:** Caso você não seja o dono da prova que está adicionando, ou simplesmente não quiser ter seu nome exposto, por favor censure onde aparecer o nome do aluno na prova!

## NOTA LEGAL
Este repositório tem finalidade exclusivamente educacional e não possui qualquer intenção de violar direitos legais ou autorais. As provas e materiais aqui presentes pertencem aos seus respectivos autores (professores/instituições).

Caso algum detentor de direitos solicite, o conteúdo será removido prontamente. O responsável por este repositório não se responsabiliza por qualquer uso indevido das informações disponibilizadas.

### Nota aos professores
Caso deseje a remoção de algum conteúdo de sua autoria, basta abrir uma issue solicitando a retirada. Após a solicitação, a pasta correspondente será removida prontamente.
""".trimIndent()

fun runCommand(dir: File, vararg command: String) {
    val process = ProcessBuilder(*command)
        .directory(dir)
        .inheritIO()
        .start()

    process.waitFor()
}

fun ensureRepo(): Boolean {
    if (!File(repoDir, ".git").exists()) return false
    return true
}

fun generateTree(dir: File, indent: String = ""): String {
    val builder = StringBuilder()
    val items = dir.listFiles()?.sortedBy { it.name } ?: return ""

    for (file in items) {
        if (file.name == ".git" || file.name == ".github" || file.name == "README.md" || file.name == "makereadme.kts") 
            continue

        val link = file.relativeTo(repoDir).path.replace(" ", "%20")

        if (file.isDirectory) {
            builder.append("$indent- [${file.name}](./$link)\n")
            builder.append(generateTree(file, "$indent  "))
        } else {
            builder.append("$indent- [${file.name}](./$link)\n")
        }
    }
    return builder.toString()
}

if (!ensureRepo()) error("diretório atual não é um repositório git, você está rodando esse script no local certo?")

println("montando readme")

val tree = generateTree(repoDir)
readmeFile.writeText(header + "\n" + tree + "\n\n" + footer)

println("readme criado em: ${readmeFile.absolutePath}")

