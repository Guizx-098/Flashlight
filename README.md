# 🔦 Flashlight — Edição Kugo

Uma versão modificada do plugin Flashlight, adaptada por **Kugo** para servidores Minecraft Bedrock que utilizam PowerNukkitX.

O projeto passou por alterações no código-fonte para adaptar seu funcionamento à API do PowerNukkitX, com ajustes na compilação, no processamento de eventos e no sistema de iluminação.

## ✨ Sobre o projeto

O Flashlight permite criar iluminação temporária ao redor do jogador utilizando itens reconhecidos pelo plugin. Esta versão reúne modificações feitas durante o processo de adaptação e testes no PowerNukkitX.

## 🚀 Recursos

- **Iluminação dinâmica:** criação de iluminação temporária ao redor do jogador.
- **Cálculo de luminosidade:** processamento dos níveis de luz dos itens compatíveis.
- **Atualização periódica:** atualização da iluminação conforme a configuração do plugin.
- **Limpeza automática:** remoção da iluminação temporária quando ela deixa de ser necessária.
- **Adaptações para PowerNukkitX:** alterações no código para compatibilidade com a API utilizada.
- **Eventos e inventário:** ajustes no processamento dos eventos relacionados ao funcionamento do plugin.

A disponibilidade de cada recurso depende da implementação e da configuração da versão instalada.

## 📥 Instalação

1. Baixe o arquivo `Flashlight.jar` na seção Releases deste repositório.
2. Copie o arquivo para a pasta `plugins/` do servidor PowerNukkitX.
3. Reinicie o servidor.
4. Verifique o console para confirmar que o plugin foi carregado corretamente.

## 🛠️ Compilação

Requisitos:
- Java 21.
- PowerNukkitX compatível com a API declarada no `plugin.yml`.
- Gradle Wrapper incluído no projeto.

Para compilar, execute na pasta do projeto:

`./gradlew clean build --no-daemon`

O arquivo JAR compilado deverá estar em `build/libs/`.

## 👨‍💻 Créditos

**Responsável pelas modificações desta versão:** Kugo.

**Projeto original:** [Flashlight — pixelwhiz](https://github.com/pixelwhiz/Flashlight).

Esta edição contém alterações realizadas por Kugo sobre o projeto original. Os créditos e os direitos relativos ao trabalho original permanecem com seus respectivos titulares.

## 📄 Licença

A licença do projeto-base é mantida conforme o arquivo `LICENSE` incluído neste repositório. Consulte esse arquivo para conhecer os termos aplicáveis.
